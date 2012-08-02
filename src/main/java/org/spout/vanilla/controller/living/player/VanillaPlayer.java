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
import org.spout.api.Spout;
import org.spout.api.entity.Entity;
import org.spout.api.entity.component.controller.PlayerController;
import org.spout.api.geo.discrete.Point;
import org.spout.api.geo.discrete.Transform;
import org.spout.api.inventory.ItemStack;
import org.spout.api.inventory.special.InventorySlot;
import org.spout.api.math.MathHelper;
import org.spout.api.math.Quaternion;
import org.spout.api.math.Vector3;
import org.spout.api.player.Player;

import org.spout.vanilla.configuration.VanillaConfiguration;
import org.spout.vanilla.controller.VanillaActionController;
import org.spout.vanilla.controller.living.Human;
import org.spout.vanilla.controller.source.DamageCause;
import org.spout.vanilla.controller.source.HealthChangeReason;
import org.spout.vanilla.data.ExhaustionLevel;
import org.spout.vanilla.data.GameMode;
import org.spout.vanilla.event.player.PlayerFoodSaturationChangeEvent;
import org.spout.vanilla.event.player.PlayerHungerChangeEvent;
import org.spout.vanilla.inventory.player.PlayerInventory;
import org.spout.vanilla.material.enchantment.Enchantments;
import org.spout.vanilla.material.item.armor.Armor;
import org.spout.vanilla.protocol.msg.ChangeGameStateMessage;
import org.spout.vanilla.protocol.msg.KeepAliveMessage;
import org.spout.vanilla.protocol.msg.PlayerListMessage;
import org.spout.vanilla.protocol.msg.SpawnPositionMessage;
import org.spout.vanilla.protocol.msg.UpdateHealthMessage;
import org.spout.vanilla.util.EnchantmentUtil;
import org.spout.vanilla.util.ItemUtil;
import org.spout.vanilla.util.VanillaNetworkUtil;
import org.spout.vanilla.window.DefaultWindow;
import org.spout.vanilla.window.Window;

import static org.spout.vanilla.util.VanillaNetworkUtil.sendPacket;

/**
 * Represents a player on a server with the VanillaPlugin; specific methods to Vanilla.
 */
public class VanillaPlayer extends Human implements PlayerController {
	protected final Player owner;
	protected long unresponsiveTicks = VanillaConfiguration.PLAYER_TIMEOUT_TICKS.getInt(), lastPing = 0, lastUserList = 0, foodTimer = 0;
	protected short count = 0, ping, maxHunger = 20, hunger = maxHunger;
	protected float foodSaturation = 5.0f, exhaustion = 0.0f;
	protected boolean poisoned;
	protected boolean flying;
	protected boolean falling;
	protected boolean jumping;
	protected float initialYFalling = 0.0f;
	protected final PlayerInventory playerInventory = new PlayerInventory(this);
	protected Window activeWindow = new DefaultWindow(this);
	protected String tabListName;
	protected GameMode gameMode;
	protected int distanceMoved;
	protected final Set<Player> invisibleFor = new HashSet<Player>();
	protected Point compassTarget;
	protected boolean playerDead = false;

	/**
	 * Constructs a new VanillaPlayer to use as a {@link PlayerController} for the given player.
	 * @param p {@link Player} parent of the controller.
	 * @param gameMode {@link GameMode} of the player.
	 */
	public VanillaPlayer(Player p, GameMode gameMode) {
		super(p.getName());
		setRenderedItemInHand(playerInventory.getQuickbar().getCurrentItem());
		owner = p;
		tabListName = owner.getName();
		compassTarget = owner.getWorld().getSpawnPoint().getPosition();
		this.gameMode = gameMode;
		this.title = p.getName();
	}

	/**
	 * Constructs a new VanillaPlayer to use as a {@link PlayerController} for the given player.
	 * @param p {@link Player} parent of the controller.
	 */
	public VanillaPlayer(Player p) {
		this(p, GameMode.SURVIVAL);
	}

	@Override
	public void onAttached() {
		Transform spawn = getParent().getWorld().getSpawnPoint();
		Quaternion rotation = spawn.getRotation();
		getParent().setPosition(spawn.getPosition());
		getParent().setRotation(rotation);
		getParent().setScale(spawn.getScale());
		super.onAttached();
	}

	@Override
	public void onTick(float dt) {
		super.onTick(dt);
		if (playerDead) {
			return;
		}

		Player player = getParent();
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

		if (lastUserList++ >= 20) {
			VanillaNetworkUtil.broadcastPacket(new PlayerListMessage(tabListName, true, ping));
			lastUserList = 0;
		}

		if (isSurvival()) {
			survivalTick(dt);
		} else {
			creativeTick(dt);
		}

		// Update window
		this.getActiveWindow().onTick(dt);
	}

	@Override
	public boolean isSavable() {
		return false;
	}

	private void survivalTick(float dt) {
		if ((distanceMoved += getPreviousPosition().distanceSquared(getParent().getPosition())) >= 1) {
			exhaustion += ExhaustionLevel.WALKING.getAmount();
			distanceMoved = 0;
		}

		if (sprinting) {
			exhaustion += ExhaustionLevel.SPRINTING.getAmount();
		}

		// TODO: Check for swimming, jumping, sprint jumping, block breaking, attacking, receiving damage for exhaustion level.
		if (poisoned) {
			exhaustion += ExhaustionLevel.FOOD_POISONING.getAmount() / 30 * dt;
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

		boolean changed = false;
		if (exhaustion > 4.0) {
			exhaustion -= 4.0;
			if (foodSaturation > 0) {
				setFoodSaturation(Math.max(foodSaturation - 1f, 0));
				changed = true;
			} else {
				setHunger((short) Math.max(hunger - 1, 0));
				changed = true;
			}
		}

		if (hunger <= 0 && health > 0) {

			int maxDrop = 0;
			
			//TODO: Disabled since there's a save error or something. The if NPE
			/*if (owner.getEntity().getWorld().get(VanillaData.DIFFICULTY) == Difficulty.EASY) {
				maxDrop = 10;
			} else if (owner.getEntity().getWorld().get(VanillaData.DIFFICULTY) == Difficulty.NORMAL) {
				maxDrop = 1;
			}*/
			if (maxDrop < health) {
				setHealth((short) Math.max(health - 1, maxDrop), DamageCause.STARVE);
				changed = true;
			}
			
		} else if (hunger >= 18 && health < 20) {
			setHealth((short) Math.min(health + 1, 20), HealthChangeReason.REGENERATION);
			changed = true;
		}

		if (changed && Spout.debugMode()) {
			System.out.println("Performing health/hunger update...");
			System.out.println("Food saturation: " + foodSaturation);
			System.out.println("Hunger: " + hunger);
			System.out.println("Health: " + health);
			System.out.println("Exhaustion: " + exhaustion);
		}
	}

	private void creativeTick(float dt) {
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
				getInventory().getQuickbar().getCurrentSlotInventory().addItemData(0, penalty);
			}
		}

		super.damage((int) Math.ceil(amt), cause, damager, sendHurtMessage);
	}

	@Override
	public void setHealth(int health, Source source) {
		super.setHealth(health, source);
		playerDead = health <= 0;
		sendUpdateHealth();
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
	 * @param foodSaturation The value to set to
	 */
	public void setFoodSaturation(float foodSaturation) {
		PlayerFoodSaturationChangeEvent event = new PlayerFoodSaturationChangeEvent(this.getParent(), foodSaturation);
		Spout.getEngine().getEventManager().callEvent(event);
		if (!event.isCancelled()) {
			if (event.getFoodSaturation() > hunger) {
				this.foodSaturation = hunger;
			} else {
				this.foodSaturation = event.getFoodSaturation();
			}
		}
		sendUpdateHealth();
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
		PlayerHungerChangeEvent event = new PlayerHungerChangeEvent(this.getParent(), hunger);
		Spout.getEngine().getEventManager().callEvent(event);
		if (!event.isCancelled()) {
			if (event.getHunger() > maxHunger) {
				this.hunger = maxHunger;
			} else {
				this.hunger = event.getHunger();
			}
		}
		sendUpdateHealth();
	}

	public void sendUpdateHealth() {
		sendPacket(owner, new UpdateHealthMessage((short) getHealth(), hunger, foodSaturation));
	}

	@Override
	public void onDeath() {
		// Don't count disconnects/unknown exceptions as dead (Yes that's a difference!)
		if (owner.getSession() != null && owner.getSession().getPlayer() != null) {
			super.onDeath();
			playerDead = true;
		}
	}

	@Override
	public Player getParent() {
		return (Player) super.getParent();
	}

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
			if (player.getController() != this) {
				if (visible) {
					invisibleFor.remove(player);
					player.getNetworkSynchronizer().spawnEntity(parent);
				} else {
					invisibleFor.add(player);
					player.getNetworkSynchronizer().destroyEntity(parent);
				}
			}
		}
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
		String playerName = getParent().getName();
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

	/**
	 * Adds a value to the exhaustion of the controller; affects hunger loss.
	 * @param exhaustion to add
	 */
	public void addExhaustion(float exhaustion) {
		this.exhaustion += exhaustion;
	}

	public boolean isFalling() {
		return falling;
	}

	public void setFalling(boolean newFalling) {
		this.falling = newFalling;
		if (this.falling) {
			if (this.initialYFalling == 0.0f) {
				this.initialYFalling = owner.getPosition().getY();
			}

		} else {
			int totalDmg = (int) ((this.initialYFalling - owner.getPosition().getY()) - 3);
			if (totalDmg > 0) {
				setHealth(getHealth() - totalDmg, DamageCause.FALL);
			}
			this.initialYFalling = 0.0f;
		}
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
		owner.getSession().send(false, new ChangeGameStateMessage(ChangeGameStateMessage.ENTER_CREDITS));
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
}
