/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, Spout LLC <http://www.spout.org/>
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
package org.spout.vanilla.world.generator.nether.structure.fortress;

import org.spout.api.geo.World;
import org.spout.api.geo.discrete.Point;
import org.spout.api.math.Quaternion;
import org.spout.api.math.Vector3;

import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.world.generator.structure.Structure;
import org.spout.vanilla.world.generator.structure.StructurePiece.BoundingBox;

public class Fortress extends Structure {
	@Override
	public boolean canPlaceObject(World w, int x, int y, int z) {
		return true;
	}

	@Override
	public void placeObject(World w, int x, int y, int z) {
		final FortressBlazeBalcony test = new FortressBlazeBalcony(this);
		test.setPosition(new Point(w, x, y, z));
		test.setRotation(new Quaternion(random.nextInt(4) * 90, 0, 1, 0));
		test.randomize();
		fillBoundingBox(w, test.getBoundingBox());
		test.place();
	}

	/**
	 * Test method for displaying bounding boxes. TODO: remove when done.
	 *
	 * @param world World to fill in
	 */
	private void fillBoundingBox(World world, BoundingBox box) {
		final Vector3 min = box.getMin();
		final Vector3 max = box.getMax();
		final int sx = min.getFloorX();
		final int sy = min.getFloorY();
		final int sz = min.getFloorZ();
		final int ex = max.getFloorX();
		final int ey = max.getFloorY();
		final int ez = max.getFloorZ();
		for (int x = sx; x <= ex; x++) {
			for (int y = sy; y <= ey; y++) {
				for (int z = sz; z <= ez; z++) {
					world.setBlockMaterial(x, y, z, VanillaMaterials.GLASS, (short) 0, null);
				}
			}
		}
	}
}
