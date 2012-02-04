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
 * the MIT license and the SpoutDev license version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license,
 * including the MIT license.
 */
package org.spout.vanilla.block;

import org.spout.api.geo.World;
import org.spout.api.geo.discrete.Point;
import org.spout.api.material.GenericBlockMaterial;
import org.spout.vanilla.VanillaMaterials;
import org.spout.vanilla.entity.objects.FallingBlock;
import org.spout.vanilla.material.SolidBlock;
import org.spout.vanilla.material.VanillaBlockMaterial;

public class Solid extends GenericVanillaBlockMaterial implements SolidBlock {
	private final boolean falling;

	public Solid(String name, int id, int data, boolean falling) {
		super(name, id, data);
		this.falling = falling;
	}

	public Solid(String name, int id, boolean falling) {
		super(name, id, 0);
		this.falling = falling;
	}

	public Solid(String name, int id) {
		super(name, id, 0);
		falling = false;
	}

	public Solid(String name, int id, int data) {
		super(name, id, data);
		falling = false;
	}

	@Override
	public boolean isFallingBlock() {
		return falling;
	}

	@Override
	public boolean hasPhysics() {
		return isFallingBlock();
	}

	@Override
	public void onUpdate(World world, int x, int y, int z) {
		if (falling){
			VanillaBlockMaterial material =(VanillaBlockMaterial) world.getBlockMaterial(x, y - 1, z);
			if (material == VanillaMaterials.AIR || material.isLiquid()) {
				if (world.setBlockId(x, y, z, (short)0, world)) {
					world.createAndSpawnEntity(new Point(world, x, y, z), new FallingBlock(this));
				}
			}
		}
	}
}
