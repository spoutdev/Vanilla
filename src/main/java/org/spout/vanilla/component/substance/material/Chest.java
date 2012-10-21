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
package org.spout.vanilla.component.substance.material;

import org.spout.api.Source;
import org.spout.api.component.ChunkComponentOwner;
import org.spout.api.entity.Entity;
import org.spout.api.entity.Player;
import org.spout.api.event.player.PlayerInteractEvent.Action;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.block.BlockFace;

import org.spout.vanilla.component.inventory.window.block.ChestWindow;
import org.spout.vanilla.data.VanillaData;
import org.spout.vanilla.inventory.Container;
import org.spout.vanilla.inventory.block.ChestInventory;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.util.VanillaBlockUtil;

public class Chest extends WindowBlockComponent implements Container, Source {
	private boolean opened = false;

	public void setDouble(boolean d) {
		ChestInventory oldInventory = getInventory();
		if ((d && oldInventory.size() == ChestInventory.DOUBLE_SIZE) || (!d && oldInventory.size() == ChestInventory.SINGLE_SIZE)) {
			return;
		}
		ChestInventory newInventory = new ChestInventory(d);
		newInventory.addAll(oldInventory);
		getData().put(VanillaData.CHEST_INVENTORY, newInventory);
	}

	public void setOpened(Source source, boolean opened) {
		if ((this.opened && opened) || (!this.opened && !opened)) {
			return;
		}
		VanillaBlockUtil.playBlockAction(getBlock(source), (byte) 1, opened ? (byte) 1 : (byte) 0);
	}

	@Override
	public void onTick(float dt) {
		if (viewers.isEmpty()) {
			setOpened(this, false);
		}
	}

	@Override
	public void onInteractBy(Entity entity, Action type, BlockFace clickedFace) {
		if (type != Action.RIGHT_CLICK) {
			return;
		}
		ChunkComponentOwner owner = getOwner();
		Block block = getBlock(entity);
		setDouble(VanillaMaterials.CHEST.isDouble(block));
		setOpened(entity, true);
		super.onInteractBy(entity, type, clickedFace);
	}

	@Override
	public ChestInventory getInventory() {
		return getData().get(VanillaData.CHEST_INVENTORY);
	}

	@Override
	public void open(Player player) {
		String title;
		ChestInventory inventory = getInventory();
		switch (inventory.size()) {
			case ChestInventory.SINGLE_SIZE:
				title = "Chest";
				break;
			case ChestInventory.DOUBLE_SIZE:
				title = "Large chest";
				break;
			default:
				title = "Unknown chest";
				break;
		}
		player.add(ChestWindow.class).init(title, inventory).open();
	}
}
