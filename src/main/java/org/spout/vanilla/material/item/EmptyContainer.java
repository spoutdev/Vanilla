/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, SpoutDev <http://www.spout.org/>
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
package org.spout.vanilla.material.item;

import java.util.HashMap;

import org.spout.api.entity.Entity;
import org.spout.api.event.player.PlayerInteractEvent.Action;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.inventory.ItemStack;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.source.GenericMaterialSource;
import org.spout.api.material.source.MaterialSource;
import org.spout.api.util.BlockIterator;

import org.spout.vanilla.controller.living.Living;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.util.VanillaPlayerUtil;

public class EmptyContainer extends BlockItem {

	private HashMap<MaterialSource, FullContainer> fullContainers = new HashMap<MaterialSource, FullContainer>();

	public EmptyContainer(String name, int id) {
		super(name, id, VanillaMaterials.AIR);
	}

	@Override
	public boolean canPlace(Block block, short data, BlockFace against, boolean isClickedBlock) {
		return false;
	}

	@Override
	public boolean onPlacement(Block block, short data, BlockFace against, boolean isClickedBlock) {
		return false;
	}

	@Override
	public void onInteract(Entity entity, Action type) {
		super.onInteract(entity, type);
		if (type == Action.RIGHT_CLICK && entity.getController() instanceof Living) {
			//TODO: Possibly use hitboxes?
			BlockIterator iter = ((Living) entity.getController()).getHeadBlockView();
			Block block;
			while (iter.hasNext()) {
				block = iter.next();
				if (block.getMaterial().equals(VanillaMaterials.AIR)) {
					continue;
				} else {
					FullContainer cont = this.getFullItem(block.getMaterial(), block.getData());
					if (cont != null) {
						block.setMaterial(VanillaMaterials.AIR);
						if (!VanillaPlayerUtil.isCreative(entity)) {
							entity.getInventory().setCurrentItem(new ItemStack(cont, 1));
						}
					}
					return;
				}
			}
		}
	}

	@Override
	public void onInteract(Entity entity, Block block, Action type, BlockFace clickedFace) {
		super.onInteract(entity, block, type, clickedFace);
		this.onInteract(entity, type);
	}

	public FullContainer getFullItem(BlockMaterial material, short data) {
		return this.fullContainers.get(new GenericMaterialSource(material, data));
	}

	public void register(FullContainer item) {
		this.fullContainers.put(item.getPlacedBlock(), item);
	}
}
