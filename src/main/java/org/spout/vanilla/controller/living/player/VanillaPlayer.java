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
import java.util.Set;

import org.spout.api.Source;
import org.spout.api.Spout;
import org.spout.api.collision.BoundingBox;
import org.spout.api.collision.CollisionModel;
import org.spout.api.entity.Entity;
import org.spout.api.entity.component.controller.PlayerController;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.discrete.Point;
import org.spout.api.geo.discrete.Transform;
import org.spout.api.inventory.ItemStack;
import org.spout.api.math.MathHelper;
import org.spout.api.math.Quaternion;
import org.spout.api.math.Vector3;
import org.spout.api.player.Player;

import org.spout.vanilla.configuration.VanillaConfiguration;
import org.spout.vanilla.controller.VanillaActionController;
import org.spout.vanilla.controller.VanillaControllerTypes;
import org.spout.vanilla.controller.living.Human;
import org.spout.vanilla.controller.object.moving.Item;
import org.spout.vanilla.controller.source.DamageCause;
import org.spout.vanilla.controller.source.HealthChangeReason;
import org.spout.vanilla.data.GameMode;
import org.spout.vanilla.enchantment.Enchantments;
import org.spout.vanilla.inventory.player.PlayerInventory;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.item.armor.Armor;
import org.spout.vanilla.protocol.msg.AnimationMessage;
import org.spout.vanilla.protocol.msg.ChangeGameStateMessage;
import org.spout.vanilla.protocol.msg.DestroyEntityMessage;
import org.spout.vanilla.protocol.msg.KeepAliveMessage;
import org.spout.vanilla.protocol.msg.PlayerListMessage;
import org.spout.vanilla.protocol.msg.SpawnPlayerMessage;
import org.spout.vanilla.protocol.msg.SpawnPositionMessage;
import org.spout.vanilla.protocol.msg.UpdateHealthMessage;
import org.spout.vanilla.util.EnchantmentUtil;
import org.spout.vanilla.util.VanillaNetworkUtil;
import org.spout.vanilla.util.VanillaPlayerUtil;
import org.spout.vanilla.window.DefaultWindow;
import org.spout.vanilla.window.Window;

import static org.spout.vanilla.util.VanillaNetworkUtil.sendPacket;

/**
 * Represents a player on a server with the VanillaPlugin; specific methods to Vanilla.
 */
public class VanillaPlayer extends Human implements PlayerController {
	protected final Player owner;
	protected long unresponsiveTicks = VanillaConfiguration.PLAYER_TIMEOUT_TICKS.getInt(), lastPing = 0, lastUserList = 0, foodTimer = 0;
	protected short count = 0, ping, hunger = 20;
	protected float foodSaturation = 5.0f, exhaustion = 0.0f;
	protected boolean sprinting, onGround, poisoned;
	protected final Vector3 moveSpeed = new Vector3(10, 0, 0), horizSpeed = new Vector3(0, 0, -10);
	protected final PlayerInventory playerInventory = new PlayerInventory();
	protected Window activeWindow = new DefaultWindow(this);
	protected String tabListName;
	protected GameMode gameMode;
	protected int distanceMoved, miningDamagePeriod = VanillaConfiguration.PLAYER_SPEEDMINING_PREVENTION_PERIOD.getInt(),
			miningDamageAllowance = VanillaConfiguration.PLAYER_SPEEDMINING_PREVENTION_ALLOWANCE.getInt();
	protected final Set<Player> invisibleFor = new HashSet<Player>();
	protected Point compassTarget;
	protected Vector3 lookingAt;
	protected Point diggingPosition;
	protected long diggingStartTime;
	protected boolean isDigging;
	protected int[] miningDamage;
	protected int miningDamagePosition = 0;
	protected long previousDiggingTime = 0;

	public VanillaPlayer(Player p, GameMode gameMode) {
		super(VanillaControllerTypes.PLAYER);
		owner = p;
		tabListName = owner.getName();
		compassTarget = owner.getEntity().getWorld().getSpawnPoint().getPosition();
		this.setHeadHeight(1.62f);
		this.gameMode = gameMode;
		miningDamage = new int[miningDamagePeriod];
	}

	public VanillaPlayer(Player p) {
		this(p, GameMode.SURVIVAL);
	}

	@Override
	public boolean isSavable() {
		return false; // Players are a special case, handled elsewhere
	}

	@Override
	public void onAttached() {
		Transform spawn = getParent().getWorld().getSpawnPoint();
		Quaternion rotation = spawn.getRotation();
		getParent().setPosition(spawn.getPosition());
		getParent().setRotation(rotation);
		getParent().setScale(spawn.getScale());
		getParent().setCollision(new CollisionModel(new BoundingBox(1, 2, 1, 2, 2, 1))); //TODO Absolutely guessed here.
		setMaxHealth(20);
		setHealth(20, HealthChangeReason.SPAWN);
		getParent().setObserver(true);
		getParent().setViewDistance(64);
	}

	@Override
	public void onTick(float dt) {
		super.onTick(dt);
		Player player = getPlayer();
		if (player == null || player.getSession() == null) {
			return;
		}

		if (lastPing++ > VanillaConfiguration.PLAYER_TIMEOUT_TICKS.getInt() / 2) {
			VanillaNetworkUtil.sendPacket(player, new KeepAliveMessage(getRandom().nextInt()));
			lastPing = 0;
		}

		count++;
		unresponsiveTicks--;
		if (unresponsiveTicks == 0) {
			player.kick("Connection Timeout!");
		}

		if (lastUserList++ > 20) {
			VanillaNetworkUtil.broadcastPacket(new PlayerListMessage(tabListName, true, ping));
			lastUserList = 0;
		}

		if (isSurvival()) {
			survivalTick(dt);
		} else {
			creativeTick(dt);
		}
	}

	private void survivalTick(float dt) {
		if (isDigging && (getDiggingTicks() % 20) == 0) {
			VanillaNetworkUtil.sendPacketsToNearbyPlayers(getParent(), getParent().getViewDistance(), new AnimationMessage(getParent().getId(), AnimationMessage.ANIMATION_SWING_ARM));
		}

		if ((distanceMoved += getPreviousPosition().distanceSquared(getParent().getPosition())) >= 1) {
			exhaustion += 0.01;
			distanceMoved = 0;
		}

		if (sprinting) {
			exhaustion += 0.1;
		}

		// TODO: Check for swimming, jumping, sprint jumping, block breaking, attacking, receiving damage for exhaustion level.
		Block head = getParent().getWorld().getBlock(getHeadPosition());
		if (head.getMaterial().equals(VanillaMaterials.GRAVEL, VanillaMaterials.SAND, VanillaMaterials.STATIONARY_WATER, VanillaMaterials.WATER)) {
			airTicks++;
			ItemStack helmet = getInventory().getArmor().getHelmet();
			int level = 0;
			if (helmet != null && EnchantmentUtil.hasEnchantment(helmet, Enchantments.RESPIRATION)) {
				level = EnchantmentUtil.getEnchantmentLevel(helmet, Enchantments.RESPIRATION);
			}
			if (head.getMaterial().equals(VanillaMaterials.STATIONARY_WATER, VanillaMaterials.WATER)) {
				// Drowning
				int ticksBeforeDrowning = level == 0 ? 300 : level * 300; // Increase time before drowning by 15 seconds per enchantment level
				if (airTicks >= ticksBeforeDrowning && airTicks % 20 == 0) {
					damage(4, DamageCause.DROWN);
				}
			} else {
				// Suffocation
				int noDamageTicks /* TODO noDamageTicks should probably be made a global variable to account for other damage */ = level == 0 ? 10 : 10 + 20 * level; // Increase time between damage by 1 second per enchantment level
				if (airTicks % noDamageTicks == 0) {
					damage(1, DamageCause.SUFFOCATE);
				}
			}
		} else {
			// Reset air ticks if necessary
			airTicks = 0;
		}

		if (poisoned) {
			exhaustion += 15.0 / 30 * dt;
		}

		// Track hunger
		foodTimer++;
		if (foodTimer >= 80) {
			updateHealthAndHunger();
			foodTimer = 0;
		}
	}

	private void updateHealthAndHunger() {
		short health;
		health = (short) getHealth();

		if (exhaustion > 4.0) {
			exhaustion -= 4.0;
			if (foodSaturation > 0) {
				foodSaturation = Math.max(foodSaturation - 0.1f, 0);
			} else {
				hunger = (short) Math.max(hunger - 1, 0);
			}
		}

		boolean changed = false;
		if (hunger <= 0 && health > 0) {
			health = (short) Math.max(health - 1, 0);
			setHealth(health, DamageCause.STARVE);
			changed = true;
		} else if (hunger >= 18 && health < 20) {
			health = (short) Math.min(health + 1, 20);
			setHealth(health, HealthChangeReason.REGENERATION);
			changed = true;
		}

		if (changed) {
			System.out.println("Performing health/hunger update...");
			System.out.println("Food saturation: " + foodSaturation);
			System.out.println("Hunger: " + hunger);
			System.out.println("Health: " + health);
			System.out.println("Exhaustion: " + exhaustion);
			// sendPacket(owner, new UpdateHealthMessage(health, hunger, foodSaturation));
		}
	}

	private void creativeTick(float dt) {
	}

	@Override
	public float getHeadHeight() {
		float height = super.getHeadHeight();
		if (this.crouching) {
			height -= 0.08f;
		}
		return height;
	}

	@Override
	public void onDeath() {
		// Don't count disconnects/unknown exceptions as dead (Yes that's a difference!)
		if (owner.getSession() != null && owner.getSession().getPlayer() != null) {
			super.onDeath();
		}
	}

	@Override
	public Player getPlayer() {
		return owner;
	}

	@Override
	public boolean hasInfiniteResources() {
		return gameMode.equals(GameMode.CREATIVE);
	}

	public void resetTimeoutTicks() {
		ping = count;
		count = 0;
		unresponsiveTicks = VanillaConfiguration.PLAYER_TIMEOUT_TICKS.getInt();
	}

	@Override
	public Set<ItemStack> getDrops(Source source, VanillaActionController lastDamager) {
		Set<ItemStack> drops = new HashSet<ItemStack>();
		ItemStack[] contents = this.getInventory().getItems().getContents();
		drops.addAll(Arrays.asList(contents));
		return drops;
	}

	/**
	 * Sets the position of player's compass target.
	 * @param compassTarget The new compass target position
	 */
	public void setCompassTarget(Point compassTarget) {
		this.compassTarget = compassTarget;
		sendPacket(owner, new SpawnPositionMessage(compassTarget.getBlockX(), compassTarget.getBlockY(), compassTarget.getBlockZ()));
	}

	/**
	 * Gets the position of the player's compass target.
	 * @return
	 */
	public Point getCompassTarget() {
		return compassTarget;
	}

	/**
	 * Gets the amount of ticks it takes the client to respond to the server.
	 * @return ping of player.
	 */
	public short getPing() {
		return ping;
	}

	/**
	 * Sets whether the player is visible for the collection of players given.
	 * @param visible
	 * @param players
	 */
	public void setVisibleFor(boolean visible, Player... players) {
		Entity parent = getParent();
		for (Player player : players) {
			if (player.getEntity().getController() != this) {
				if (visible) {
					invisibleFor.remove(player);
					ItemStack currentItem = VanillaPlayerUtil.getCurrentItem(player.getEntity());
					int itemId = 0;
					if (currentItem != null) {
						itemId = currentItem.getMaterial().getId();
					}

					sendPacket(player, new SpawnPlayerMessage(parent.getId(), owner.getName(), parent.getPosition(), (int) parent.getYaw(), (int) parent.getPitch(), itemId));
				} else {
					invisibleFor.add(player);
					sendPacket(player, new DestroyEntityMessage(parent.getId()));
				}
			}
		}
	}

	@Override
	public boolean needsPositionUpdate() {
		return true;
	}

	@Override
	public boolean needsVelocityUpdate() {
		return true;
	}

	/**
	 * Sets whether the player is visible for everyone.
	 * @param visible
	 */
	public void setVisible(boolean visible) {
		setVisibleFor(visible, Spout.getEngine().getOnlinePlayers());
	}

	/**
	 * Whether or not the player is visible for that player.
	 * @param player
	 * @return true if visible for that player
	 */
	public boolean isVisibleFor(Player player) {
		return !invisibleFor.contains(player);
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
	 * Sets whether or not the player is on the ground.
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
	 * Makes the player a server operator.
	 * @param op
	 */
	public void setOp(boolean op) {
		String playerName = getPlayer().getName();
		VanillaConfiguration.OPS.setOp(playerName, op);
	}

	/**
	 * Whether or not the player is a server operator.
	 * @return true if an operator.
	 */
	public boolean isOp() {
		String playerName = getPlayer().getName();
		return VanillaConfiguration.OPS.isOp(playerName);
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
		sendPacket(owner, new ChangeGameStateMessage(ChangeGameStateMessage.CHANGE_GAME_MODE, gameMode));
	}

	/**
	 * Whether or not the controller is in survival mode.
	 * @return true if in survival mode
	 */
	public boolean isSurvival() {
		return gameMode.equals(GameMode.SURVIVAL);
	}

	/**
	 * Whether or not the controller is poisoned.
	 * @return true if poisoned.
	 */
	public boolean isPoisoned() {
		return poisoned;
	}

	/**
	 * Sets whether or not the controller is poisoned.
	 * @param poisoned
	 */
	public void setPoisoned(boolean poisoned) {
		this.poisoned = poisoned;
	}

	/**
	 * Returns the hunger of the player attached to the controller.
	 * @return hunger
	 */
	public short getHunger() {
		return hunger;
	}

	/**
	 * Sets the hunger of the controller.
	 * @param hunger
	 */
	public void setHunger(short hunger) {
		this.hunger = hunger;
	}

	/**
	 * Returns the food saturation level of the player attached to the controller. The food bar "jitters" when the bar reaches 0.
	 * @return food saturation level
	 */
	public float getFoodSaturation() {
		return this.foodSaturation;
	}

	/**
	 * Sets the food saturation of the controller. The food bar "jitters" when the bar reaches 0.
	 * @param foodSaturation
	 */
	public void setFoodSaturation(float foodSaturation) {
		this.foodSaturation = foodSaturation;
	}

	/**
	 * Returns the exhaustion of the controller; affects hunger loss.
	 * @return
	 */
	public float getExhaustion() {
		return exhaustion;
	}

	/**
	 * Sets the exhaustion of the controller; affects hunger loss.
	 * @param exhaustion
	 */
	public void setExhaustion(float exhaustion) {
		this.exhaustion = exhaustion;
	}

	public void setWindow(Window activeWindow) {
		this.activeWindow.close();
		this.activeWindow = activeWindow;
		this.activeWindow.open();
	}

	public void closeWindow() {
		this.setWindow(new DefaultWindow(this));
	}

	public Window getActiveWindow() {
		return this.activeWindow;
	}

	public PlayerInventory getInventory() {
		return playerInventory;
	}

	public void setLookingAtVector(Vector3 lookingAt) {
		this.lookingAt = lookingAt;
	}

	public Vector3 getLookingAt() {
		return lookingAt;
	}

	/**
	 * Returns the digging state of the controller
	 * @return true if player is digging
	 */
	public boolean isDigging() {
		return isDigging;
	}

	/**
	 * Sets isDigging true and records start time, unless already digging
	 * @return true if successful
	 */
	public boolean startDigging(Point position) {
		if (owner.getEntity().getPosition().getDistance(position) > 6) { // TODO: Actually get block reach from somewhere instead of just using 6
			return false;
		}
		isDigging = true;
		diggingPosition = position;
		diggingStartTime = System.currentTimeMillis();
		return true;
	}

	/**
	 * Sets isDigging false and records total time, unless the dig was invalid/never started.
	 * @return true if successful
	 */
	public boolean stopDigging(Point position) {
		if (!isDigging) {
			return false;
		}
		previousDiggingTime = getDiggingTime();
		isDigging = false;
		VanillaNetworkUtil.sendPacketsToNearbyPlayers(getParent(), getParent().getViewDistance(), new AnimationMessage(getParent().getId(), AnimationMessage.ANIMATION_NONE));
		if (!position.equals(diggingPosition)) {
			return false;
		}
		return true;
	}

	/**
	 * Gets time spent digging
	 * @return time spent digging
	 */
	public long getDiggingTime() {
		if (isDigging) {
			// Is this correct?
			return System.currentTimeMillis() - diggingStartTime;
		} else {
			return previousDiggingTime;
		}
	}

	/**
	 * Gets last time spent digging in real(client) ticks
	 * @return ticks spent digging
	 */
	public long getDiggingTicks() {
		return getDiggingTime() / 50;
	}

	/**
	 * Adds and checks mining speed for cheating.
	 * @param damageRemaining Remaining damage on block
	 * @return false if player is cheating
	 */
	public boolean addAndCheckMiningSpeed(int damageRemaining) {
		if (!VanillaConfiguration.PLAYER_SPEEDMINING_PREVENTION_ENABLED.getBoolean()) {
			return true;
		}

		miningDamage[miningDamagePosition++] = damageRemaining;

		if (miningDamagePosition >= miningDamagePeriod) {
			miningDamagePosition = 0;
		}

		return checkMiningSpeed();
	}

	/**
	 * Checks mining speed for cheating.
	 * @return false if player is cheating
	 */
	public boolean checkMiningSpeed() {
		if (MathHelper.mean(miningDamage) > miningDamageAllowance) { // TODO: Make this configurable?
			return false;
		}
		return true;
	}

	public void dropItem() {
		ItemStack current = this.getInventory().getItems().getCurrentItem();
		if (current == null) {
			return;
		}
		ItemStack drop = new ItemStack(current.getMaterial(), 1);
		drop.setNBTData(current.getNBTData());
		Item control = new Item(drop, getHeadPosition().subtract(getLookingAt()));
		if (current.getAmount() > 1) {
			current.setAmount(current.getAmount() - 1);
		} else {
			current = null;
		}
		this.getInventory().getItems().setCurrentItem(current);
		getParent().getWorld().createAndSpawnEntity(getHeadPosition().add(0.0, 0.4, 0.0), control);
	}

	@Override
	public void damage(int amount, DamageCause cause, VanillaActionController damager, boolean sendHurtMessage) {
		double amt = amount;
		// Calculate damage reduction based on armor and enchantments
		for (ItemStack item : getInventory().getArmor().getContents()) {
			if (item != null && item.getMaterial() instanceof Armor) { // Ignore pumpkins
				Armor armor = (Armor) item.getMaterial();
				amt -= .04 * (armor.getBaseProtection() + armor.getProtection(item, cause)); // Each protection point reduces damage by 4%

				// Remove durability from each piece of armor
				short penalty = cause.getDurabilityPenalty();
				if (item.getData() - penalty < 1) {
					getInventory().setCurrentItem(null);
				} else {
					getInventory().addCurrentItemData(penalty);
				}
			}
		}

		super.damage((int) Math.ceil(amt), cause, damager, sendHurtMessage);
	}

	@Override
	public void setHealth(int health, Source source) {
		super.setHealth(health, source);
		sendPacket(owner, new UpdateHealthMessage((short) getHealth(), hunger, foodSaturation));
	}
}
