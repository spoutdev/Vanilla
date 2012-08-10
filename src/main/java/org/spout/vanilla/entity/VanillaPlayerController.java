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

import java.util.Arrays;
import java.util.List;

import org.spout.api.Source;
import org.spout.api.entity.controller.PlayerController;
import org.spout.api.geo.discrete.Point;
import org.spout.api.geo.discrete.Transform;
import org.spout.api.inventory.ItemStack;
import org.spout.api.inventory.special.InventorySlot;
import org.spout.api.material.BlockMaterial;
import org.spout.api.math.MathHelper;
import org.spout.api.math.Quaternion;
import org.spout.api.math.Vector3;
import org.spout.api.player.Player;
import org.spout.api.protocol.event.ProtocolEvent;
import org.spout.api.tickable.LogicPriority;

import org.spout.vanilla.configuration.VanillaConfiguration;
import org.spout.vanilla.data.GameMode;
import org.spout.vanilla.entity.component.basic.PlayerSuffocationComponent;
import org.spout.vanilla.entity.component.effect.PoisonEffectComponent;
import org.spout.vanilla.entity.component.gamemode.CreativeComponent;
import org.spout.vanilla.entity.component.gamemode.SurvivalComponent;
import org.spout.vanilla.entity.component.physics.PlayerStepSoundComponent;
import org.spout.vanilla.entity.component.player.PingComponent;
import org.spout.vanilla.entity.component.player.StatsUpdateComponent;
import org.spout.vanilla.entity.source.DamageCause;
import org.spout.vanilla.inventory.player.PlayerInventory;
import org.spout.vanilla.material.block.Liquid;
import org.spout.vanilla.protocol.msg.ChangeGameStateMessage;
import org.spout.vanilla.protocol.msg.SpawnPositionMessage;
import org.spout.vanilla.util.ItemUtil;
import org.spout.vanilla.window.DefaultWindow;
import org.spout.vanilla.window.Window;

import static org.spout.vanilla.util.VanillaMathHelper.getLookAtPitch;
import static org.spout.vanilla.util.VanillaMathHelper.getLookAtYaw;
import static org.spout.vanilla.util.VanillaMathHelper.getRandomDirection;
import static org.spout.vanilla.util.VanillaNetworkUtil.sendPacket;

/**
 * Represents a player on a server with the VanillaPlugin; specific methods to Vanilla.
 */
public class VanillaPlayerController extends PlayerController implements VanillaController {
	private PingComponent pingComponent;
	private PoisonEffectComponent poisonEffectComponent;
	private SurvivalComponent survivalProcess;
	private PlayerStepSoundComponent stepSoundProcess;
	private StatsUpdateComponent statsUpdateProcess;
	protected boolean flying;
	protected boolean falling;
	protected boolean jumping;
	protected float initialYFalling = 0.0f;
	protected final PlayerInventory playerInventory = new PlayerInventory(this);
	protected Window activeWindow = new DefaultWindow(this);
	protected String tabListName;
	protected GameMode gameMode;
	protected Point compassTarget;
	private short experience = 0;

	public VanillaPlayerController(GameMode mode) {
		super(VanillaControllerTypes.HUMAN);
	}

	@Override
	public void onAttached() {
		super.onAttached();
		compassTarget = getParent().getWorld().getSpawnPoint().getPosition();
		tabListName = getParent().getName();
		Transform spawn = getParent().getWorld().getSpawnPoint();
		Quaternion rotation = spawn.getRotation();
		getParent().setPosition(spawn.getPosition());
		getParent().setRotation(rotation);
		getParent().setScale(spawn.getScale());
		getHealth().setSpawnHealth(20);
		getHealth().setDeathAnimation(false);

		unregisterProcess(suffocationProcess);
		suffocationProcess = registerProcess(new PlayerSuffocationComponent(this, LogicPriority.HIGHEST));
		statsUpdateProcess = registerProcess(new StatsUpdateComponent(this, LogicPriority.NORMAL));
		pingComponent = registerProcess(new PingComponent(this, LogicPriority.HIGHEST));
		poisonEffectComponent = registerProcess(new PoisonEffectComponent(this, LogicPriority.HIGHEST));
		stepSoundProcess = registerProcess(new PlayerStepSoundComponent(this, LogicPriority.NORMAL));
		// Survival mode
		survivalProcess = registerProcess(new SurvivalComponent(this, LogicPriority.HIGHEST));
		// Creative mode
		registerProcess(new CreativeComponent(this, LogicPriority.HIGHEST));
	}

	@Override
	public void onTick(float dt) {
		if (getHealth().isDying()) {
			getHealth().run();
			return;
		}
		if (getHealth().isDead()) {
			return;
		}
		super.onTick(dt);

		Player player = getParent();
		if (player == null || player.getSession() == null) {
			return;
		}

		// Update window
		this.getActiveWindow().onTick(dt);
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

	@Override
	public List<ItemStack> getDrops(Source source, VanillaEntityController lastDamager) {
		List<ItemStack> drops = super.getDrops(source, lastDamager);
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
		sendPacket(getParent(), new SpawnPositionMessage(compassTarget.getBlockX(), compassTarget.getBlockY(), compassTarget.getBlockZ()));
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
		sendPacket(getParent(), new ChangeGameStateMessage(ChangeGameStateMessage.CHANGE_GAME_MODE, gameMode));
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

	@Override
	public Transform getHeadTransform() {
		Transform trans = new Transform();
		trans.setPosition(this.getHeadPosition());
		Vector3 offset = this.getLookingAt();
		trans.setRotation(MathHelper.rotation(getLookAtPitch(offset), getLookAtYaw(offset), 0.0f));
		return trans;
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
		dropItem(item, getLookingAt().multiply(0.3).add(getRandomDirection(0.02f, 0.1f)).add(0.0, 0.1, 0.0));
	}

	/**
	 * Drops the item at the velocity specified
	 * @param item to drop
	 * @param velocity to drop at
	 */
	public void dropItem(ItemStack item, Vector3 velocity) {
		ItemUtil.dropItem(this.getHeadPosition().subtract(0.0, 0.3, 0.0), item, velocity);
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

	public StatsUpdateComponent getStatsUpdateProcess() {
		return statsUpdateProcess;
	}

	public SurvivalComponent getSurvivalLogic() {
		return survivalProcess;
	}

	public PlayerStepSoundComponent getStepSoundLogic() {
		return stepSoundProcess;
	}

	public short getExperience() {
		return experience;
	}

	public void setExperience(short experience) {
		this.experience = experience;
	}

	@Override
	public void callProtocolEvent(ProtocolEvent event) {
		//To change body of implemented methods use File | Settings | File Templates.
	}
}
