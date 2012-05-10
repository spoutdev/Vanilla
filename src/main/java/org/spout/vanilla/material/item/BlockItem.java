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

import org.spout.api.entity.Entity;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.Placeable;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.source.GenericMaterialSource;
import org.spout.api.material.source.MaterialSource;

import org.spout.vanilla.util.VanillaPlayerUtil;

/**
 * A simplistic class which redirects placement requests to another (official) block material<br>
 * Can be used to store multi-block creations
 */
public class BlockItem extends VanillaItemMaterial implements Placeable {
	private MaterialSource onPlace;
	private BlockMaterial onPlaceBlock;

	public BlockItem(String name, int id, BlockMaterial onPlaceMaterial) {
		this(name, id, onPlaceMaterial, (short) 0);
	}

	public BlockItem(String name, int id, BlockMaterial onPlaceMaterial, short onPlaceData) {
		super(name, id);
		this.setPlacedBlock(new GenericMaterialSource(onPlaceMaterial, onPlaceData));
	}

	@Override
	public boolean canPlace(Block block, short data, BlockFace against, boolean isClickedBlock) {
		return this.onPlaceBlock.canPlace(block, data, against, isClickedBlock);
	}

	@Override
	public boolean onPlacement(Block block, short data, BlockFace against, boolean isClickedBlock) {
		if (block.getSource() instanceof Entity) {
			Entity entity = (Entity) block.getSource();
			if (!entity.getInventory().isCurrentItem(this)) {
				throw new IllegalStateException("Interaction with an controller that is not holding this block!");
			}
		}
		if (this.onPlaceBlock.onPlacement(block, data, against, isClickedBlock)) {
			if (block.getSource() instanceof Entity) {
				Entity entity = (Entity) block.getSource();
				if (VanillaPlayerUtil.isSurvival(entity)) {
					if (!entity.getInventory().addCurrentItemAmount(-1)) {
						throw new IllegalStateException("ControllerType is holding zero or negative sized item!");
					}
				}
			}
		}
		return true;
	}

	/**
	 * Sets the block material this block item places
	 * @param blockmaterial to set to
	 * @return this block item
	 */
	public BlockItem setPlacedBlock(MaterialSource blockmaterial) {
		if (blockmaterial == null || blockmaterial.getMaterial() == null) {
			throw new NullPointerException("Block block can not be null");
		} else {
			this.onPlace = blockmaterial;
			this.onPlaceBlock = (BlockMaterial) this.onPlace.getSubMaterial();
		}
		return this;
	}

	/**
	 * Gets the block material this block item places
	 * @return the Block material
	 */
	public BlockMaterial getPlacedBlockMaterial() {
		return this.onPlaceBlock;
	}

	/**
	 * Gets the material and data this block item placed
	 * @return the Block material and data
	 */
	public MaterialSource getPlacedBlock() {
		return onPlace;
	}
}
