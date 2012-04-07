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

		if (lastPing++ > 600) {
			sendMessage(player, new PingMessage(getRandom().nextInt()));
			lastPing = 0;
		}
		count++;
		unresponsiveTicks--;
		if (unresponsiveTicks == 0) {
			player.getSession().disconnect("Connection timeout!");
		}
		if (lastUserList++ > 20) {
			sendMessage(new HashSet<Player>(Arrays.asList(Spout.getEngine().getOnlinePlayers())), new UserListItemMessage(player.getName(), true, ping));
			lastUserList = 0;
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
}
