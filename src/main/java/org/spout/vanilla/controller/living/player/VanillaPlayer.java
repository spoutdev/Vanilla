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
import org.spout.vanilla.controller.source.HealthChangeReason;
import org.spout.vanilla.protocol.event.entity.player.PlayerHealthEvent;
import org.spout.vanilla.protocol.event.entity.player.PlayerListEvent;
import org.spout.vanilla.protocol.event.world.StateChangeEvent;
import org.spout.vanilla.protocol.msg.PingMessage;

/**
 * Represents a player on a server with the VanillaPlugin; specific methods to
 * Vanilla.
 */
public class VanillaPlayer extends Human implements PlayerController {
	protected final Player owner;
	protected long unresponsiveTicks = VanillaConfiguration.PLAYER_TIMEOUT_TICKS.getInt(), lastPing = 0, lastUserList = 0, foodTimer = 0;
	protected short count = 0, ping, hunger = 20;
	protected float foodSaturation = 5.0f, exhaustion = 0.0f;
	protected boolean sneaking, sprinting, onGround, poisoned;
	protected final Vector3 moveSpeed = new Vector3(10, 0, 0), horizSpeed = new Vector3(0, 0, -10);
	protected Inventory activeInventory;
	protected ItemStack itemOnCursor;
	protected String tabListName;
	protected GameMode gameMode;

	public VanillaPlayer(Player p, GameMode gameMode) {
		super(VanillaControllerTypes.PLAYER);
		owner = p;
		tabListName = owner.getName();
		this.gameMode = gameMode;
		p.getEntity().setInventorySize(45);
	}
	
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
		getParent().setMaxHealth(20);
		getParent().setHealth(20, new HealthChangeReason(HealthChangeReason.Type.SPAWN));
		// TODO: Persistent health
	}

	@Override
	public void onTick(float dt) {
		super.onTick(dt);
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
			for (Player p : Spout.getEngine().getOnlinePlayers()) {
				p.getNetworkSynchronizer().callProtocolEvent(new PlayerListEvent(tabListName, true, ping));
			}

			lastUserList = 0;
		}

		if (isSurvival()) {
			survivalTick(dt);
		} else {
			creativeTick(dt);
		}
	}
	
	private void survivalTick(float dt) {
		exhaustion += 0.1;
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
	}

	private void updateHealth() {
		short health;
		Entity parent = getParent();
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

		System.out.println("Performing health/hunger update...");
		System.out.println("Food saturation: " + foodSaturation);
		System.out.println("Hunger: " + hunger);
		System.out.println("Health: " + health);
		System.out.println("Exhaustion: " + exhaustion);
		parent.setHealth(health, new HealthChangeReason(HealthChangeReason.Type.REGENERATION));
		getPlayer().getNetworkSynchronizer().callProtocolEvent(new PlayerHealthEvent(health, hunger, foodSaturation));
	}
	
	private void creativeTick(float dt) {
		
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

	@Override
	public PlayerInventory createInventory(int size) {
		PlayerInventory inventory = new PlayerInventory(size);
		for (int i = 37; i < inventory.getSize(); i++) {
			inventory.setHiddenSlot(i, true);
		}

		inventory.setCurrentSlot(0);
		return new PlayerInventory(size);
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

	/**
	 * The list displayed in the user list on the client when a client presses TAB.
	 *
	 * @return user list name
	 */
	public String getTabListName() {
		return tabListName;
	}

	/**
	 * Sets the list displayed in the user list on the client when a client presses TAB.
	 *
	 * @param tabListName
	 */
	public void setTabListName(String tabListName) {
		this.tabListName = tabListName;
	}

	/**
	 * Returns the current game-mode the controller is in.
	 *
	 * @return game mode of controller
	 */
	public GameMode getGameMode() {
		return gameMode;
	}

	/**
	 * Sets the current game-mode the controller is in.
	 *
	 * @param gameMode
	 */
	public void setGameMode(GameMode gameMode) {
		this.gameMode = gameMode;
		owner.getNetworkSynchronizer().callProtocolEvent(new StateChangeEvent(StateChangeEvent.Reason.CHANGE_GAME_MODE, gameMode));
	}

	/**
	 * Whether or not the controller is in survival mode.
	 *
	 * @return true if in survival mode
	 */
	public boolean isSurvival() {
		return gameMode.equals(GameMode.SURVIVAL);
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

	/**
	 * Returns the hunger of the player attached to the controller.
	 *
	 * @return hunger
	 */
	public short getHunger() {
		return hunger;
	}

	/**
	 * Sets the hunger of the controller.
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
		return this.foodSaturation;
	}

	/**
	 * Sets the food saturation of the controller. The food bar "jitters" when the bar reaches 0.
	 *
	 * @param foodSaturation
	 */
	public void setFoodSaturation(float foodSaturation) {
		this.foodSaturation = foodSaturation;
	}

	/**
	 * Returns the exhaustion of the controller; affects hunger loss.
	 *
	 * @return
	 */
	public float getExhaustion() {
		return exhaustion;
	}

	/**
	 * Sets the exhaustion of the controller; affects hunger loss.
	 *
	 * @param exhaustion
	 */
	public void setExhaustion(float exhaustion) {
		this.exhaustion = exhaustion;
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
}
