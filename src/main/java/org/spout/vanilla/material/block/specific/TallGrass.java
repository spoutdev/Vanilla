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
package org.spout.vanilla.material.block.specific;

import org.spout.api.Source;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.block.BlockFace;
import org.spout.vanilla.material.VanillaMaterials;

public class TallGrass extends DeadBush {
	public static final TallGrass DEAD_GRASS = register(new TallGrass("Dead Grass"));
	public static final TallGrass TALL_GRASS = register(new TallGrass("Tall Grass", 1, DEAD_GRASS));
	public static final TallGrass FERN = register(new TallGrass("Fern", 2, DEAD_GRASS));

	private TallGrass(String name) {
		super(name, 31);
		this.setDefault();
	}

	private TallGrass(String name, int data, TallGrass parent) {
		super(name, 31, data, parent);
		this.setDefault();
	}

	private void setDefault() {
		this.setHardness(0.0F).setResistance(0.0F);
	}

	@Override
	public TallGrass getParentMaterial() {
		return (TallGrass) super.getParentMaterial();
	}
	
	@Override
	public boolean canPlace(World world, int x, int y, int z, short data, BlockFace against, Source source) {
		if (super.canPlace(world, x, y, z, data, against, source)) {
			Block block = world.getBlock(x, y, z).move(against.getOpposite());
			return block.getMaterial() == VanillaMaterials.GRASS || block.getMaterial() == VanillaMaterials.DIRT;
		} else {
			return false;
		}
	}
}
