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
package org.spout.vanilla.material.generic;

import java.util.HashMap;

import org.spout.api.entity.Entity;
import org.spout.api.event.player.PlayerInteractEvent.Action;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.discrete.Point;
import org.spout.api.inventory.Inventory;
import org.spout.api.inventory.ItemStack;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.source.GenericMaterialSource;
import org.spout.api.material.source.MaterialSource;

import org.spout.vanilla.VanillaMaterials;

public class GenericEmptyContainer extends GenericBlockItem {
	private HashMap<MaterialSource, GenericFullContainer> fullContainers = new HashMap<MaterialSource, GenericFullContainer>();

	public GenericEmptyContainer(String name, int id) {
		super(name, id, VanillaMaterials.AIR);
	}

	@Override
	public void onInteract(Entity entity, Point position, Action type, BlockFace clickedFace) {
		// TODO: ignore position and get the first non-air in the line of sight to prevent NPE's
		Block block = entity.getWorld().getBlock(position);

		super.onInteract(entity, position, type, clickedFace);

		Inventory inventory = entity.getInventory();
		if (inventory.getCurrentItem() == null) {
			GenericFullContainer full = this.getFullItem(block.getMaterial(), block.getData());

			if (full == null) {
				return;
			}

			inventory.setItem(new ItemStack(full, 1), inventory.getCurrentSlot());
		}
	}

	public GenericFullContainer getFullItem(BlockMaterial material, short data) {
		return this.fullContainers.get(new GenericMaterialSource(material, data));
	}

	public void register(GenericFullContainer item) {
		this.fullContainers.put(item.getBlock(), item);
	}
}
