/*
 * This file is part of Vanilla (http://www.spout.org/).
 *
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

import org.spout.api.Spout;
import org.spout.api.entity.Entity;
import org.spout.api.entity.PlayerController;
import org.spout.api.geo.discrete.Transform;
import org.spout.api.inventory.Inventory;
import org.spout.api.inventory.ItemStack;
import org.spout.api.inventory.PlayerInventory;
import org.spout.api.math.Quaternion;
import org.spout.api.math.Vector3;
import org.spout.api.player.Player;

import org.spout.vanilla.configuration.VanillaConfiguration;
import org.spout.vanilla.controller.VanillaControllerTypes;
import org.spout.vanilla.controller.living.Human;
import org.spout.vanilla.controller.source.Reason;
import org.spout.vanilla.protocol.event.HealthEvent;
import org.spout.vanilla.protocol.msg.PingMessage;
import org.spout.vanilla.protocol.msg.StateChangeMessage;
import org.spout.vanilla.protocol.msg.UserListItemMessage;

/**
 * Represents a player on a server with the VanillaPlugin; specific methods to
 * Vanilla.
 */
public class VanillaPlayer extends Human implements PlayerController {
	private final Player owner;
	private int unresponsiveTicks = VanillaConfiguration.PLAYER_TIMEOUT_TICKS.getInt();
	private int lastPing = 0;
	private int lastUserList = 0;
	private short count = 0;
	private short ping;
	private boolean sneaking, sprinting, onGround;
	private final Vector3 moveSpeed = new Vector3(10, 0, 0);
	private final Vector3 horizSpeed = new Vector3(0, 0, -10);
	private Inventory activeInventory;
	private ItemStack itemOnCursor;
	private GameModeHandler gmhandler;
	private boolean poisoned = false;
	private short hunger = 20;
	private float foodSaturation = 5.0f;
	private float exhaustion = 0.0f;
	private long foodTimer = 0;

	public VanillaPlayer(Player p) {
		super(VanillaControllerTypes.PLAYER);
		owner = p;
		p.getEntity().setInventorySize(45);
	}

	@Override
	public void onAttached() {
		Transform spawn = getParent().getWorld().getSpawnPoint();
		Quaternion rotation = spawn.getRotation();
		getParent().setPosition(spawn.getPosition());
		getParent().setRotation(rotation);
		getParent().setScale(spawn.getScale());
		getParent().setMaxHealth(20);
		getParent().setHealth(20, new Reason(Reason.Type.SPAWN));
		// TODO: Persistent health
	}

	@Override
	public void onTick(float dt) {
		gmhandler.onTick(dt);
		Player player = getPlayer();
		if (player == null || player.getSession() == null) {
			return;
		}

		/* TODO COMMENTED OUT PENDING TESTING
		if(player.input().getForward() > 0){
			getParent().translate(moveSpeed.transform(getParent().getRotation()));
		}
		if(player.input().getForward() < 0){
			getParent().translate(moveSpeed.transform(getParent().getRotation()).multiply(-1));
		}
		if(player.input().getHorizantal() < 0){
			getParent().translate(horizSpeed.transform(getParent().getRotation()).multiply(-1));
		}
		if(player.input().getHorizantal() > 0){
			getParent().translate(horizSpeed.transform(getParent().getRotation()));
		}*/

		if (lastPing++ > VanillaConfiguration.PLAYER_TIMEOUT_TICKS.getInt()/2) {
			sendMessage(player, new PingMessage(getRandom().nextInt()));
			lastPing = 0;
		}

		count++;
		unresponsiveTicks--;
		if (unresponsiveTicks == 0) {
			player.kick("Connection Timeout!");
		}

		if (lastUserList++ > 20) {
			sendMessage(new HashSet<Player>(Arrays.asList(Spout.getEngine().getOnlinePlayers())), new UserListItemMessage(player.getName(), true, ping));
			lastUserList = 0;
		}

		if (sprinting) {
			exhaustion += 0.1;
		}
		
		// TODO: Check for swimming, jumping, sprint jumping, block breaking, attacking, receiving damage for exhaustion level.
		
		if (poisoned) {
			exhaustion += 15.0;
		}

		// Track hunger
		foodTimer++;
		if (foodTimer >= 80) {
			updateHealth();
			foodTimer = 0;
		}
		
		super.onTick(dt);
	}

	/**
	 * Gets the amount of ticks it takes the client to respond to the server.
	 *
	 * @return ping of player.
	 */
	public short getPing() {
		return ping;
	}

	@Override
	public Player getPlayer() {
		return owner;
	}

	/**
	 * Returns the hunger of the player attached to the controller.
	 *
	 * @return hunger
	 */
	public short getHunger() {
		return hunger;
	}

	/**
	 * Sets the hunger of the player attached to the controller.
	 *
	 * @param hunger
	 */
	public void setHunger(short hunger) {
		this.hunger = hunger;
	}

	/**
	 * Returns the food saturation level of the player attached to the controller. The food bar "jitters" when the bar reaches 0.
	 *
	 * @return food saturation level
	 */
	public float getFoodSaturation() {
		return foodSaturation;
	}

	/**
	 * Sets the food saturation level of the player attached to the controller.
	 *
	 * @param foodSaturation
	 */
	public void setFoodSaturation(float foodSaturation) {
		this.foodSaturation = foodSaturation;
	}

	/**
	 * Returns the exhaustion of the player attached to the controller.
	 *
	 * @return exhaustion
	 */
	public float getExhaustion() {
		return exhaustion;
	}

	/**
	 * Sets the exhaustion of the player attached to the controller.
	 *
	 * @param exhaustion
	 */
	public void setExhaustion(float exhaustion) {
		this.exhaustion = exhaustion;
	}

	/**
	 * Whether or not the controller is poisoned.
	 *
	 * @return true if poisoned.
	 */
	public boolean isPoisoned() {
		return poisoned;
	}

	/**
	 * Sets whether or not the controller is poisoned.
	 *
	 * @param poisoned
	 */
	public void setPoisoned(boolean poisoned) {
		this.poisoned = poisoned;
	}

	@Override
	public PlayerInventory createInventory(int size) {
		PlayerInventory inventory = new PlayerInventory(size);
		for (int i = 37; i < inventory.getSize(); i++) {
			inventory.setHiddenSlot(i, true);
		}
		inventory.setCurrentSlot(0);
		return new PlayerInventory(size);
	}

	public void resetTimeoutTicks() {
		ping = count;
		count = 0;
		unresponsiveTicks = VanillaConfiguration.PLAYER_TIMEOUT_TICKS.getInt();
	}

	@Override
	public Set<ItemStack> getDrops() {
		Set<ItemStack> drops = new HashSet<ItemStack>();
		ItemStack[] contents = getParent().getInventory().getContents();
		drops.addAll(Arrays.asList(contents));
		return drops;
	}

	/**
	 * Sets whether or not the player is sneaking.
	 *
	 * @param sneaking
	 */
	public void setSneaking(boolean sneaking) {
		this.sneaking = sneaking;
	}

	/**
	 * Whether or not the player is sneaking.
	 *
	 * @return true if player is sneaking
	 */
	public boolean isSneaking() {
		return sneaking;
	}

	/**
	 * Sets whether or not th player is
	 *
	 * @param sprinting
	 */
	public void setSprinting(boolean sprinting) {
		this.sprinting = sprinting;
	}

	/**
	 * Whether or not the player is sprinting.
	 *
	 * @return true if sprinting
	 */
	public boolean isSprinting() {
		return sprinting;
	}

	/**
	 * Sets whether or not the player is on the ground.
	 *
	 * @param onGround
	 */
	public void setOnGround(boolean onGround) {
		this.onGround = onGround;
	}

	/**
	 * Whether or not the player is on the ground.
	 *
	 * @return true if on ground.
	 */
	public boolean isOnGround() {
		return onGround;
	}

	/**
	 * Makes the player a server operator.
	 *
	 * @param op
	 */
	public void setOp(boolean op) {
		String playerName = getPlayer().getName();
		VanillaConfiguration.OPS.setOp(playerName, op);
	}

	/**
	 * Whether or not the player is a server operator.
	 *
	 * @return true if an operator.
	 */
	public boolean isOp() {
		String playerName = getPlayer().getName();
		return VanillaConfiguration.OPS.isOp(playerName);
	}

	public Inventory getActiveInventory() {
		return activeInventory;
	}

	public void setActiveInventory(Inventory newActive) {
		newActive = activeInventory;
	}

	public ItemStack getItemOnCursor() {
		return itemOnCursor;
	}

	public void setItemOnCursor(ItemStack newItem) {
		itemOnCursor = newItem;
	}

	@Override
	public boolean hasInfiniteResources() {
		return gmhandler.hasInfiniteResources();
	}

	public void setGameMode(GameModeHandler newHandler) {
		setGameMode(newHandler, true);
	}
	
	public void setGameMode(GameModeHandler newHandler, boolean sendMessage) {
		gmhandler = newHandler;
		if (sendMessage) {
			this.getPlayer().getSession().send(new StateChangeMessage((byte) 3, (byte) gmhandler.getPacketId()));
		}
	}

	public boolean isSurvival() {
		return gmhandler instanceof SurvivalPlayer;
	}

	public GameModeHandler getGameModeHandler() {
		return gmhandler;
	}

	private void updateHealth() {
		short health;
		Entity parent = getParent();
		if (isSurvival()) {
			foodSaturation -= 0.1;
			health = (short) parent.getHealth();
			if (foodSaturation <= 0) {
				hunger--;
			} else {
				health++;
			}

			if (exhaustion >= 4.0) {
				exhaustion = 0;
				if (foodSaturation <= 0) {
					hunger--;
				} else {
					foodSaturation--;
				}
			}
			
			if (hunger <= 0) {
				health--;
			}
		} else {
			foodSaturation = 5.0f;
			exhaustion = 0;
			hunger = 20;
			health = 20;
		}

		System.out.println("Performing health/hunger update...");
		System.out.println("Food saturation: " + foodSaturation);
		System.out.println("Hunger: " + hunger);
		System.out.println("Health: " + health);
		System.out.println("Exhaustion: " + exhaustion);
		parent.setHealth(health, new Reason(Reason.Type.REGENERATION));
		getPlayer().getNetworkSynchronizer().callProtocolEvent(new HealthEvent(health, hunger, foodSaturation));
	}
}
