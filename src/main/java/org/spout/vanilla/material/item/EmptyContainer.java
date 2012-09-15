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
import org.spout.api.math.Vector3;
import org.spout.api.util.BlockIterator;

import org.spout.vanilla.components.living.Human;
import org.spout.vanilla.components.misc.HeadComponent;
import org.spout.vanilla.data.GameMode;
import org.spout.vanilla.data.VanillaData;
import org.spout.vanilla.components.player.PlayerInventory;
import org.spout.vanilla.inventory.player.PlayerQuickbar;
import org.spout.vanilla.material.VanillaMaterials;

public class EmptyContainer extends BlockItem {
	private HashMap<MaterialSource, FullContainer> fullContainers = new HashMap<MaterialSource, FullContainer>();

	public EmptyContainer(String name, int id) {
		super(name, id, VanillaMaterials.AIR);
		this.setMaxStackSize(16);
	}

	@Override
	public boolean canPlace(Block block, short data, BlockFace against, Vector3 clickedPos, boolean isClickedBlock) {
		return false;
	}

	@Override
	public boolean onPlacement(Block block, short data, BlockFace against, Vector3 clickedPos, boolean isClickedBlock) {
		return false;
	}

	@Override
	public void onInteract(Entity entity, Action type) {
		super.onInteract(entity, type);
		//TODO fix this with collisions?
		if (type == Action.RIGHT_CLICK && entity.has(HeadComponent.class)) {
			BlockIterator iterator = entity.get(HeadComponent.class).getBlockView();
			if (iterator == null || !iterator.hasNext()) {
				return;
			}
			Block block = iterator.next();
			FullContainer cont = this.getFullItem(block.getMaterial(), block.getData());
			if (cont == null) {
				return;
			}
			block.setMaterial(VanillaMaterials.AIR);

			// Subtract item
			if (!entity.getData().get(VanillaData.GAMEMODE).equals(GameMode.SURVIVAL)) {
				return;
			}
			PlayerInventory inventory = entity.get(Human.class).getInventory();
			PlayerQuickbar quickbar = inventory.getQuickbar();
			quickbar.addItemAmount(quickbar.getCurrentSlot(), -1);
			inventory.getMain().addItem(new ItemStack(cont, 1));
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
