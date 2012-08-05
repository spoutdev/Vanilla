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
package org.spout.vanilla.controller.living.player;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import org.spout.api.Source;
import org.spout.api.entity.component.controller.PlayerController;
import org.spout.api.geo.discrete.Point;
import org.spout.api.geo.discrete.Transform;
import org.spout.api.inventory.ItemStack;
import org.spout.api.inventory.special.InventorySlot;
import org.spout.api.material.BlockMaterial;
import org.spout.api.math.MathHelper;
import org.spout.api.math.Quaternion;
import org.spout.api.math.Vector3;
import org.spout.api.player.Player;
import org.spout.api.tickable.LogicPriority;
import org.spout.vanilla.configuration.VanillaConfiguration;
import org.spout.vanilla.controller.VanillaEntityController;
import org.spout.vanilla.controller.living.Human;
import org.spout.vanilla.controller.logic.gamemode.AdventureLogic;
import org.spout.vanilla.controller.logic.gamemode.CreativeLogic;
import org.spout.vanilla.controller.logic.gamemode.SurvivalLogic;
import org.spout.vanilla.controller.source.DamageCause;
import org.spout.vanilla.data.GameMode;
import org.spout.vanilla.inventory.player.PlayerInventory;
import org.spout.vanilla.material.block.Liquid;
import org.spout.vanilla.material.enchantment.Enchantments;
import org.spout.vanilla.material.item.armor.Armor;
import org.spout.vanilla.protocol.msg.ChangeGameStateMessage;
import org.spout.vanilla.protocol.msg.SpawnPositionMessage;
import org.spout.vanilla.protocol.msg.UpdateHealthMessage;
import org.spout.vanilla.util.EnchantmentUtil;
import org.spout.vanilla.util.ItemUtil;
import static org.spout.vanilla.util.VanillaNetworkUtil.sendPacket;
import org.spout.vanilla.window.DefaultWindow;
import org.spout.vanilla.window.Window;

/**
 * Represents a player on a server with the VanillaPlugin; specific methods to Vanilla.
 */
public class VanillaPlayer extends Human implements PlayerController {
  private PingProcess pingProcess;
  private EffectProcess effectProcess;
  private SurvivalLogic survivalLogic;
	protected boolean flying;
	protected boolean falling;
	protected boolean jumping;
  protected boolean healthDirty;
	protected float initialYFalling = 0.0f;
	protected final PlayerInventory playerInventory = new PlayerInventory(this);
	protected Window activeWindow = new DefaultWindow(this);
	protected String tabListName;
	protected GameMode gameMode;
	protected Point compassTarget;

	/**
	 * Constructs a new VanillaPlayer to use as a {@link PlayerController} for the given player.
	 * @param gameMode {@link GameMode} of the player.
	 */
	public VanillaPlayer(GameMode gameMode) {
		this.gameMode = gameMode;
		setRenderedItemInHand(playerInventory.getQuickbar().getCurrentItem());
	}

	/**
	 * Constructs a new VanillaPlayer to use as a {@link PlayerController} for the given player.
	 */
	public VanillaPlayer() {
		this(GameMode.SURVIVAL);
	}

	@Override
	public void onAttached() {
		compassTarget = getParent().getWorld().getSpawnPoint().getPosition();
		tabListName = getParent().getName();
		Transform spawn = getParent().getWorld().getSpawnPoint();
		Quaternion rotation = spawn.getRotation();
		getParent().setPosition(spawn.getPosition());
		getParent().setRotation(rotation);
		getParent().setScale(spawn.getScale());
    
    pingProcess = new PingProcess(this, LogicPriority.HIGHEST);
    effectProcess = new EffectProcess(this, LogicPriority.HIGHEST);
    survivalLogic = new SurvivalLogic(this, LogicPriority.HIGHEST);
		//Survival mode
		registerProcess(survivalLogic);
		//Creative mode
		registerProcess(new CreativeLogic(this, LogicPriority.HIGHEST));
    //Adventure mode
		registerProcess(new AdventureLogic(this, LogicPriority.HIGHEST));
    //Ping and timeout handler
    registerProcess(pingProcess);
    //Effect handler
    registerProcess(effectProcess);
    
		super.onAttached();
	}

	@Override
	public void onTick(float dt) {
		super.onTick(dt);

		// Update window
		this.getActiveWindow().onTick(dt);
	}

	@Override
	public boolean isSavable() {
		return false;
	}

	@Override
	public void damage(int amount, DamageCause cause, VanillaEntityController damager, boolean sendHurtMessage) {
		double amt = amount;
		// Calculate damage reduction based on armor and enchantments
		for (ItemStack item : getInventory().getArmor().getContents()) {
			if (item != null && item.getMaterial() instanceof Armor) { // Ignore pumpkins
				Armor armor = (Armor) item.getMaterial();
				amt -= .04 * (armor.getBaseProtection() + armor.getProtection(item, cause)); // Each protection point reduces damage by 4%

				// Remove durability from each piece of armor
				short penalty = cause.getDurabilityPenalty();
				getInventory().getQuickbar().getCurrentSlotInventory().addItemData(0, penalty);
			}
		}

		super.damage((int) Math.ceil(amt), cause, damager, sendHurtMessage);
	}

	@Override
	public void setHealth(int health, Source source) {
		super.setHealth(health, source);
    healthDirty = true;
	}

	public void updateHealth() {
		sendPacket(getParent(), new UpdateHealthMessage((short) getHealth(), getSurvivalLogic().getHunger(), getSurvivalLogic().getFoodSaturation()));
    healthDirty = false;
	}

	@Override
	public void onDeath() {
		// Don't count disconnects/unknown exceptions as dead (Yes that's a difference!)
		if (getParent().getSession() != null && getParent().getSession().getPlayer() != null) {
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
	public Set<ItemStack> getDrops(Source source, VanillaEntityController lastDamager) {
		Set<ItemStack> drops = new HashSet<ItemStack>();
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
	 * Returns the current game-mode the controller is in.
	 * @return game mode of controller
	 */
	public GameMode getGameMode() {
		return gameMode;
	}

	/**
	 * Sets the current game-mode the controller is in.
	 * @param gameMode
	 */
	public void setGameMode(GameMode gameMode) {
		this.gameMode = gameMode;
		sendPacket(getParent(), new ChangeGameStateMessage(ChangeGameStateMessage.CHANGE_GAME_MODE, gameMode));
	}

	/**
	 * Whether or not the controller is in survival mode.
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
				setHealth(getHealth() - totalDmg, DamageCause.FALL);
			}
			this.initialYFalling = 0.0f;
		}
	}
  
  public boolean isSwimming() {
    Point location = getParent().getPosition();
    BlockMaterial first = location.getWorld().getBlockMaterial(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    BlockMaterial second = location.getWorld().getBlockMaterial(location.getBlockX(), location.getBlockY()+1, location.getBlockZ());
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

	//TODO: Get these two functions working in the API!
	public static float getLookAtYaw(Vector3 offset) {
		float yaw = 0;
		// Set yaw
		if (offset.getX() != 0) {
			// Set yaw start value based on dx
			if (offset.getX() < 0) {
				yaw = 270;
			} else {
				yaw = 90;
			}
			yaw -= Math.toDegrees(Math.atan(offset.getZ() / offset.getX()));
		} else if (offset.getZ() < 0) {
			yaw = 180;
		}
		return yaw;
	}

	public static float getLookAtPitch(Vector3 offset) {
		return (float) -Math.toDegrees(Math.atan(offset.getY() / MathHelper.length(offset.getX(), offset.getZ())));
	}

	private static Vector3 getRandomVelocity(Random rand, float force) {
		return new Vector3(rand.nextFloat(), 0.0, rand.nextFloat()).normalize().multiply(force * rand.nextFloat());
	}

	/**
	 * Drops the item specified into a random direction
	 * @param item to drop
	 */
	public void dropItemRandom(ItemStack item) {
		Random rand = new Random(getParent().getWorld().getAge());
		Point position = this.getHeadPosition().subtract(0.0, 0.3, 0.0);
		Vector3 velocity = getRandomVelocity(rand, 0.5f).add(0.0, 0.2, 0.0);
		ItemUtil.dropItem(position, item, velocity);
	}

	/**
	 * Drops the item specified into the direction the player looks
	 * @param item to drop
	 */
	public void dropItem(ItemStack item) {
		Random rand = new Random(getParent().getWorld().getAge());
		Point position = this.getHeadPosition().subtract(0.0, 0.3, 0.0);
		Vector3 velocity = getLookingAt().multiply(0.3).add(0.0, 0.1, 0.0);
		velocity = velocity.add(getRandomVelocity(rand, 0.02f));
		velocity = velocity.add(0.0f, (rand.nextFloat() - rand.nextFloat()) * 0.1f, 0.0f);
		ItemUtil.dropItem(position, item, velocity);
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

	@Override
	public int getMaxAirTicks() {
		ItemStack helmet = getInventory().getArmor().getHelmet().getItem();
		int level = 0;
		if (helmet != null && EnchantmentUtil.hasEnchantment(helmet, Enchantments.RESPIRATION)) {
			level = EnchantmentUtil.getEnchantmentLevel(helmet, Enchantments.RESPIRATION);
		}
		return level == 0 ? 300 : level * 300;
	}

  public boolean isDirty() {
    return healthDirty;
  }
  
  public void setDirty(boolean newDirty) {
    healthDirty = newDirty;
  }
  
  public PingProcess getPingProcess() {
    return pingProcess;
  }
  
  public EffectProcess getEffectProcess() {
    return effectProcess;
  }
  
  public SurvivalLogic getSurvivalLogic() {
    return survivalLogic;
  }
  
}
