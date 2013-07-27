/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011 Spout LLC <http://www.spout.org/>
 * Vanilla is licensed under the Spout License Version 1.
 *
 * Vanilla is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the Spout License Version 1.
 *
 * Vanilla is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for
 * more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the Spout License Version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://spout.in/licensev1> for the full license, including
 * the MIT license.
 */
package org.spout.vanilla.material.block.plant;

import java.util.HashSet;
import java.util.Set;

import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.Material;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.block.BlockFaces;
import org.spout.api.math.GenericMath;

import org.spout.vanilla.data.resources.VanillaMaterialModels;
import org.spout.vanilla.material.InitializableMaterial;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.liquid.Water;

public class SugarCaneBlock extends StackGrowingBase implements InitializableMaterial {
	private final Set<Material> allowedBases = new HashSet<Material>(4);

	public SugarCaneBlock(String name, int id) {
		super(name, id, VanillaMaterialModels.SUGARCANE, null);
		this.setHardness(0.0F).setResistance(0.0F).setTransparent().setGhost(true);
		this.addAllowedBase(VanillaMaterials.DIRT, VanillaMaterials.GRASS, VanillaMaterials.SAND);
	}

	@Override
	public void initialize() {
		this.getDrops().DEFAULT.clear().add(VanillaMaterials.SUGAR_CANE);
	}

	@Override
	public long getGrowTime(Block block) {
		return 180000L + GenericMath.getRandom().nextInt(180000);
	}

	@Override
	public boolean canAttachTo(Block block, BlockFace face) {
		if (!super.canAttachTo(block, face)) {
			return false;
		}
		BlockMaterial material = block.getMaterial();
		// Can always attach to sugar canes
		if (material.equals(VanillaMaterials.SUGAR_CANE_BLOCK)) {
			return true;
		}
		// Only attach to bases with water around it
		if (this.allowedBases.contains(material)) {
			for (BlockFace around : BlockFaces.NESW) {
				if (block.translate(around).getMaterial() instanceof Water) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean canSupport(BlockMaterial material, BlockFace face) {
		return face == BlockFace.TOP && material.equals(VanillaMaterials.SUGAR_CANE_BLOCK);
	}

	public void addAllowedBase(Material... materials) {
		for (Material mat : materials) {
			allowedBases.add(mat);
		}
	}

	@Override
	public boolean grow(Block block, Material material) {
		return false;
	}
}
