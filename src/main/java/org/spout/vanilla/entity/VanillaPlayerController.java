/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, VanillaDev <http://www.spout.org/>
 * Vanilla is licensed under the SpoutDev License Version 1.
 *
 * Vanilla is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the SpoutDev License Version 1.
 *
 * Vanilla is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the SpoutDev License Version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license,
 * including the MIT license.
 */
package org.spout.vanilla.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

import org.spout.api.Source;
import org.spout.api.Spout;
import org.spout.api.entity.Entity;
import org.spout.api.entity.Player;
import org.spout.api.entity.controller.PlayerController;
import org.spout.api.geo.discrete.Point;
import org.spout.api.geo.discrete.Transform;
import org.spout.api.inventory.ItemStack;
import org.spout.api.inventory.special.InventorySlot;
import org.spout.api.material.BlockMaterial;
import org.spout.api.math.Quaternion;
import org.spout.api.math.Vector3;
import org.spout.api.protocol.event.ProtocolEvent;
import org.spout.api.tickable.TickPriority;

import org.spout.vanilla.configuration.VanillaConfiguration;
import org.spout.vanilla.data.GameMode;
import org.spout.vanilla.entity.component.GameModeOwner;
import org.spout.vanilla.entity.component.HeadOwner;
import org.spout.vanilla.entity.component.HealthOwner;
import org.spout.vanilla.entity.component.SuffocationOwner;
import org.spout.vanilla.entity.component.basic.DiggingComponent;
import org.spout.vanilla.entity.component.basic.HeadComponent;
import org.spout.vanilla.entity.component.basic.HealthComponent;
import org.spout.vanilla.entity.component.basic.PlayerHeadComponent;
import org.spout.vanilla.entity.component.basic.PlayerSuffocationComponent;
import org.spout.vanilla.entity.component.basic.SuffocationComponent;
import org.spout.vanilla.entity.component.effect.PoisonEffectComponent;
import org.spout.vanilla.entity.component.gamemode.AdventureComponent;
import org.spout.vanilla.entity.component.gamemode.CreativeComponent;
import org.spout.vanilla.entity.component.gamemode.SurvivalComponent;
import org.spout.vanilla.entity.component.physics.BlockCollisionComponent;
import org.spout.vanilla.entity.component.physics.PlayerStepSoundComponent;
import org.spout.vanilla.entity.component.player.PingComponent;
import org.spout.vanilla.entity.component.player.StatsUpdateComponent;
import org.spout.vanilla.entity.source.DamageCause;
import org.spout.vanilla.event.player.network.PlayerGameStateEvent;
import org.spout.vanilla.inventory.player.PlayerInventory;
import org.spout.vanilla.material.block.Liquid;
import org.spout.vanilla.protocol.msg.ChangeGameStateMessage;
import org.spout.vanilla.util.ItemUtil;
import org.spout.vanilla.window.DefaultWindow;
import org.spout.vanilla.window.Window;

import static org.spout.vanilla.util.VanillaMathHelper.getRandomDirection;

/**
 * Represents a player on a server with the VanillaPlugin; specific methods to Vanilla.
 */
public class VanillaPlayerController extends PlayerController implements VanillaController, HealthOwner, SuffocationOwner, HeadOwner, GameModeOwner {
	private PingComponent pingComponent;
	private PoisonEffectComponent poisonEffectComponent;
	private PlayerStepSoundComponent stepSoundComponent;
	private StatsUpdateComponent statsUpdateComponent;
	private HealthComponent healthComponent;
	private PlayerSuffocationComponent suffocationComponent;
	private PlayerHeadComponent headComponent;
	private DiggingComponent diggingComponent;
	private SurvivalComponent survivalComponent;
	private CreativeComponent creativeComponent;
	private AdventureComponent adventureComponent;
	protected BlockCollisionComponent blockCollisionComponent;
	protected boolean flying;
	protected boolean falling;
	protected boolean jumping;
	protected boolean crouching;
	protected boolean onGround, sprinting;
	protected ItemStack renderedItemInHand;
	protected String title; //TODO title isn't really a good name...
	protected float initialYFalling = 0.0f;
	protected final PlayerInventory playerInventory = new PlayerInventory(this);
	protected Window activeWindow = new DefaultWindow(this);
	protected String tabListName;
	protected GameMode gameMode;
	protected Point compassTarget;
	private short experience = 0;
	// Protocol: last known updated client transform
	private Transform lastClientTransform = new Transform();

	private int positionTicks = 0;
	private int velocityTicks = 0;
	private Vector3 velocity = Vector3.ZERO;

	public VanillaPlayerController() {
		super(VanillaControllerTypes.VANILLA_PLAYER);
	}

	@Override
	public void onAttached() {
		//Components
		healthComponent = addComponent(new HealthComponent(TickPriority.HIGHEST));
		headComponent = addComponent(new PlayerHeadComponent());
		statsUpdateComponent = addComponent(new StatsUpdateComponent(TickPriority.HIGHEST));
		pingComponent = addComponent(new PingComponent(TickPriority.HIGH));
		poisonEffectComponent = addComponent(new PoisonEffectComponent(TickPriority.HIGH));
		stepSoundComponent = addComponent(new PlayerStepSoundComponent(TickPriority.HIGHEST));
		diggingComponent = addComponent(new DiggingComponent(TickPriority.HIGH));
		blockCollisionComponent = addComponent(new BlockCollisionComponent(TickPriority.HIGHEST));
		survivalComponent = addComponent(new SurvivalComponent(TickPriority.HIGH));
		creativeComponent = addComponent(new CreativeComponent(TickPriority.HIGH));
		adventureComponent = addComponent(new AdventureComponent(TickPriority.HIGH));

		compassTarget = getParent().getWorld().getSpawnPoint().getPosition();
		tabListName = getParent().getName();
		getParent().setObserver(true);
		getHealth().setSpawnHealth(20);
		getHealth().setDeathAnimation(false);
		getHead().setHeight(1.62f);
	}

	@Override
	public void onTick(float dt) {
		if (getHealth().isDying()) {
			getHealth().onTick(dt);
			return;
		}
		if (getHealth().isDead()) {
			return;
		}
		
		positionTicks++;
		velocityTicks++;		
		super.onTick(dt);

		Player player = getParent();
		if (player == null || player.getSession() == null) {
			return;
		}

		// Update window
		this.getActiveWindow().onTick(dt);
	}

	public DiggingComponent getDiggingLogic() {
		return this.diggingComponent;
	}

	@Override
	public SurvivalComponent getSurvivalComponent() {
		return this.survivalComponent;
	}

	@Override
	public CreativeComponent getCreativeComponent() {
		return this.creativeComponent;
	}

	@Override
	public AdventureComponent getAdventureComponent() {
		return this.adventureComponent;
	}

	@Override
	public SuffocationComponent getSuffocation() {
		return this.suffocationComponent;
	}

	@Override
	public HealthComponent getHealth() {
		return this.healthComponent;
	}

	@Override
	public HeadComponent getHead() {
		return this.headComponent;
	}

	public void kill() {
		//Don't allow the game to kill the entity, allow the network sync to handle death
	}

	@Override
	public boolean isSavable() {
		return false;
	}

	@Override
	public void onDeath() {
		// Don't count disconnects/unknown exceptions as dead
		if (getParent().isOnline()) {
			super.onDeath();
		}
	}

	@Override
	public Player getParent() {
		return (Player) super.getParent();
	}

	public boolean hasInfiniteResources() {
		return gameMode.equals(GameMode.CREATIVE);
	}

	public List<ItemStack> getDrops(Source source, Entity lastDamager) {
		List<ItemStack> drops = new ArrayList<ItemStack>(); // super.getDrops(source, lastDamager);
		ItemStack[] contents = this.getInventory().getMain().getContents();
		drops.addAll(Arrays.asList(contents));
		return drops;
	}

	/**
	 * Sets the position of player's compass target.
	 * @param compassTarget The new compass target position
	 */
	public void setCompassTarget(Point compassTarget) {
		this.compassTarget = compassTarget;
		//sendPacket(getParent(), new SpawnPositionMessage(compassTarget.getBlockX(), compassTarget.getBlockY(), compassTarget.getBlockZ()));
	}

	/**
	 * Gets the position of the player's compass target.
	 * @return
	 */
	public Point getCompassTarget() {
		return compassTarget;
	}

	/**
	 * Makes the player a server operator.
	 * @param op
	 */
	public void setOp(boolean op) {
		String playerName = getParent().getName();
		VanillaConfiguration.OPS.setOp(playerName, op);
	}

	/**
	 * Whether or not the player is a server operator.
	 * @return true if an operator.
	 */
	public boolean isOp() {
		return VanillaConfiguration.OPS.isOp(getParent().getName());
	}

	/**
	 * The list displayed in the user list on the client when a client presses TAB.
	 * @return user list name
	 */
	public String getTabListName() {
		return tabListName;
	}

	/**
	 * Sets the list displayed in the user list on the client when a client presses TAB.
	 * @param tabListName
	 */
	public void setTabListName(String tabListName) {
		this.tabListName = tabListName;
	}

	/**
	 * Returns the current game-mode the entity is in.
	 * @return game mode of entity
	 */
	public GameMode getGameMode() {
		return gameMode;
	}

	/**
	 * Sets the current game-mode the entity is in.
	 * @param gameMode
	 */
	public void setGameMode(GameMode gameMode) {
		this.gameMode = gameMode;
		getParent().getNetworkSynchronizer().callProtocolEvent(new PlayerGameStateEvent(getParent(), ChangeGameStateMessage.CHANGE_GAME_MODE, gameMode));
	}

	/**
	 * Whether or not the entity is in survival mode.
	 * @return true if in survival mode
	 */
	public boolean isSurvival() {
		return gameMode.equals(GameMode.SURVIVAL);
	}

	public boolean isFalling() {
		return falling;
	}

	public void setFalling(boolean newFalling) {
		this.falling = newFalling;
		if (this.falling) {
			if (this.initialYFalling == 0.0f) {
				this.initialYFalling = getParent().getPosition().getY();
			}
		} else {
			int totalDmg = (int) ((this.initialYFalling - getParent().getPosition().getY()) - 3);
			if (!isSwimming() && totalDmg > 0) {
				getHealth().damage(totalDmg, DamageCause.FALL);
			}
			this.initialYFalling = 0.0f;
		}
	}

	public boolean isSwimming() {
		Point location = getParent().getPosition();
		BlockMaterial first = location.getWorld().getBlockMaterial(location.getBlockX(), location.getBlockY(), location.getBlockZ());
		BlockMaterial second = location.getWorld().getBlockMaterial(location.getBlockX(), location.getBlockY() + 1, location.getBlockZ());
		return first instanceof Liquid && second instanceof Liquid;
	}

	public boolean isJumping() {
		return jumping;
	}

	public void setJumping(boolean jumping) {
		this.jumping = jumping;
	}

	public boolean isFlying() {
		return flying;
	}

	public void setFlying(boolean newFlying) {
		this.flying = newFlying;
	}

	/**
	 * Gets the name displayed above the human's head.
	 * @return title name
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the name displayed above the human's head.
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Sets whether or not th player is
	 * @param sprinting
	 */
	public void setSprinting(boolean sprinting) {
		this.sprinting = sprinting;
	}

	/**
	 * Whether or not the player is sprinting.
	 * @return true if sprinting
	 */
	public boolean isSprinting() {
		return sprinting;
	}

	/**
	 * Sets whether or not the player is perceived by the client as being on the ground.
	 * @param onGround
	 */
	public void setOnGround(boolean onGround) {
		this.onGround = onGround;
	}

	/**
	 * Whether or not the player is on the ground.
	 * @return true if on ground.
	 */
	public boolean isOnGround() {
		return onGround;
	}

	/**
	 * Sets and opens the new active {@link Window} for the player.
	 * @param activeWindow the window to open and set as the active window.
	 */
	public void setWindow(Window activeWindow) {
		Window old = this.activeWindow;
		this.activeWindow = activeWindow;
		if (old.isOpen()) {
			old.close();
		}
		if (!activeWindow.isOpen()) {
			activeWindow.open();
		}
	}

	/**
	 * Closes the active {@link Window}.
	 */
	public void closeWindow() {
		this.setWindow(new DefaultWindow(this));
	}

	/**
	 * Gets the {@link Window} currently opened. If no window is opened a {@link DefaultWindow} will be returned.
	 * @return the currently active window.
	 */
	public Window getActiveWindow() {
		return this.activeWindow;
	}

	/**
	 * Gets the player's {@link PlayerInventory}.
	 * @return the player's inventory
	 */
	public PlayerInventory getInventory() {
		return playerInventory;
	}

	public boolean isCrouching() {
		return crouching;
	}

	public void setCrouching(boolean crouching) {
		this.crouching = crouching;
	}

	/**
	 * Gets the item rendered in the human's hand; not neccassaily the actual item in the human's hand.
	 * @return rendered item in hand
	 */
	public ItemStack getRenderedItemInHand() {
		return renderedItemInHand;
	}

	/**
	 * Sets the item rendered in the human's hand; not neccassaily the actual item in the human's hand.
	 * @param renderedItemInHand
	 */
	public void setRenderedItemInHand(ItemStack renderedItemInHand) {
		this.renderedItemInHand = renderedItemInHand;
	}

	/**
	 * Drops the item specified into a random direction
	 * @param item to drop
	 */
	public void dropItemRandom(ItemStack item) {
		dropItem(item, getRandomDirection(0.5f, 0.0f).add(0.0, 0.2, 0.0));
	}

	/**
	 * Drops the item specified into the direction the player looks
	 * @param item to drop
	 */
	public void dropItem(ItemStack item) {
		dropItem(item, getHead().getLookingAt().multiply(0.3).add(getRandomDirection(0.02f, 0.1f)).add(0.0, 0.1, 0.0));
	}

	/**
	 * Drops the item at the velocity specified
	 * @param item to drop
	 * @param velocity to drop at
	 */
	public void dropItem(ItemStack item, Vector3 velocity) {
		ItemUtil.dropItem(this.getHead().getPosition().subtract(0.0, 0.3, 0.0), item, velocity);
	}

	/**
	 * Drops the player's current item.
	 */
	public void dropItem() {
		InventorySlot slot = this.getInventory().getQuickbar().getCurrentSlotInventory();
		ItemStack current = slot.getItem();
		if (current == null) {
			return;
		}
		ItemStack drop = current.clone().setAmount(1);
		slot.addItemAmount(-1);
		this.dropItem(drop);
	}

	/**
	 * Rolls the credits located on the client.
	 */
	public void rollCredits() {
		getParent().getSession().send(false, new ChangeGameStateMessage(ChangeGameStateMessage.ENTER_CREDITS));
	}

	public PingComponent getPingComponent() {
		return pingComponent;
	}

	public PoisonEffectComponent getPoisonEffectComponent() {
		return poisonEffectComponent;
	}

	public StatsUpdateComponent getStatsUpdateComponent() {
		return statsUpdateComponent;
	}

	public PlayerStepSoundComponent getStepSoundComponent() {
		return stepSoundComponent;
	}

	public short getExperience() {
		return experience;
	}

	public void setExperience(short experience) {
		this.experience = experience;
	}

	/**
	 * Sets the last known transformation known by the clients<br>
	 * This should only be called by the protocol classes
	 * @param transform to set to
	 */
	public void setLastClientTransform(Transform transform) {
		this.lastClientTransform = transform.copy();
	}

	/**
	 * Gets the last known transformation updated to the clients
	 * @return the last known transform by the clients
	 */
	public Transform getLastClientTransform() {
		return this.lastClientTransform;
	}
	
	@Override
	public void callProtocolEvent(ProtocolEvent event) {
		for (Player player : getParent().getWorld().getNearbyPlayers(getParent(), getParent().getViewDistance())) {
			if (player == null || player.getNetworkSynchronizer() == null) {
				continue;
			}
			player.getNetworkSynchronizer().callProtocolEvent(event);
		}
	}

	/**
	 * Tests if a velocity update is needed for this entity<br>
	 * This is called by the entity protocol
	 * @return True if a velocity update is needed
	 */
	public boolean needsVelocityUpdate() {
		return velocityTicks % 5 == 0;
	}

	/**
	 * Tests if a position update is needed for this entity<br>
	 * This is called by the entity protocol
	 * @return True if a position update is needed
	 */
	public boolean needsPositionUpdate() {
		return positionTicks % 30 == 0;
	}

	/**
	 * Gets the current velocity of this entity
	 * @return the velocity
	 */
	public Vector3 getVelocity() {
		return velocity;
	}

	/**
	 * Sets the current velocity for this entity
	 * @param velocity to set to
	 */
	public void setVelocity(Vector3 velocity) {
		if (velocity == null) {
			if (Spout.debugMode()) {
				Spout.getLogger().log(Level.SEVERE, "Velocity of " + this.toString() + " set to null!");
				Spout.getLogger().log(Level.SEVERE, "Report this to http://issues.spout.org");
			}
			velocity = Vector3.ZERO;
		}
		this.velocity = velocity;
	}	
}
