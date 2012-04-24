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
package org.spout.vanilla.material.block;

import org.spout.vanilla.material.block.generic.Solid;
import org.spout.api.geo.World;
import org.spout.api.geo.discrete.Point;
import org.spout.vanilla.controller.object.moving.PrimedTnt;
import org.spout.vanilla.material.VanillaMaterials;

public class TNT extends Solid {

	public TNT() {
		super("TnT", 46);
	}
	
	@Override
	public boolean hasPhysics() {
		return true;
	}

	public void onIgnite(World world, int x, int y, int z) {
		world.setBlockMaterial(x, y, z, VanillaMaterials.AIR, (short) 0, true, world);
		//spawn a primed TNT
		Point point = new Point(world, (float) x + 0.5f, (float) y + 0.5f, (float) z + 0.5f);
		world.createAndSpawnEntity(point, new PrimedTnt());
	}
	
	@Override
	public void onUpdate(World world, int x, int y, int z) {
		super.onUpdate(world, x, y, z);
		if (this.getIndirectRedstonePower(world, x, y, z) > 0) {
			this.onIgnite(world, x, y, z);
		}
	}
}
