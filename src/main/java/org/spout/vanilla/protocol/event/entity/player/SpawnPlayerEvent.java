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
package org.spout.vanilla.protocol.event.entity.player;

import org.spout.api.entity.Entity;
import org.spout.api.geo.discrete.Point;
import org.spout.api.inventory.ItemStack;
import org.spout.api.player.Player;
import org.spout.api.protocol.event.ProtocolEvent;
import org.spout.vanilla.controller.living.player.VanillaPlayer;

public class SpawnPlayerEvent extends ProtocolEvent {
	private final int id;
	private int x, y, z, yaw, pitch, itemId;
	private final String playerName;

	public SpawnPlayerEvent(Player player) {
		Entity playerEntity = player.getEntity();
		this.id = playerEntity.getId();
		this.playerName = player.getName();
		Point pos = playerEntity.getPosition();
		this.x = pos.getBlockX();
		this.y = pos.getBlockY();
		this.z = pos.getBlockZ();
		this.yaw = (int) playerEntity.getYaw();
		this.pitch = (int) playerEntity.getPitch();
		if (!(player instanceof VanillaPlayer)) {
			itemId = 0;
			return;
		}
		
		VanillaPlayer p = (VanillaPlayer) player;
		ItemStack currentItem = p.getActiveInventory().getCurrentItem();
		int itemId = 0;
		if (currentItem != null) {
			itemId = currentItem.getMaterial().getId();
		}
		
		this.itemId = itemId;
	}
	
	public int getEntityId() {
		return id;
	}
	
	public String getPlayerName() {
		return playerName;
	}
	
	public int getX() {
		return x;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public int getY() {
		return y;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public int getZ() {
		return z;
	}
	
	public void setZ(int z) {
		this.z = z;
	}
	
	public int getYaw() {
		return yaw;
	}
	
	public void setYaw(int yaw) {
		this.yaw = yaw;
	}
	
	public int getPitch() {
		return pitch;
	}
	
	public void setPitch(int pitch) {
		this.pitch = pitch;
	}
	
	public int getItemId() {
		return itemId;
	}
	
	public void setItemId(int itemId) {
		this.itemId = itemId;
	}
}
