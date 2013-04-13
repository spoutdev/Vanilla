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

import java.util.List;

import org.spout.api.math.Vector3;

import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.world.generator.structure.PieceCuboidBuilder;
import org.spout.vanilla.world.generator.structure.SimpleBlockMaterialPicker;
import org.spout.vanilla.world.generator.structure.Structure;
import org.spout.vanilla.world.generator.structure.StructurePiece;

public class FortressBridgeIntersection extends StructurePiece {
	public FortressBridgeIntersection(Structure parent) {
		super(parent);
	}

	@Override
	public boolean canPlace() {
		return true;
	}

	@Override
	public void place() {
		// Building objects
		final PieceCuboidBuilder box = new PieceCuboidBuilder(this);
		final SimpleBlockMaterialPicker picker = new SimpleBlockMaterialPicker();
		box.setPicker(picker);
		// Floors for both bridges
		picker.setOuterInnerMaterials(VanillaMaterials.NETHER_BRICK, VanillaMaterials.NETHER_BRICK);
		box.setMinMax(0, 0, 0, 4, 1, 18).fill();
		box.offsetMinMax(-7, 0, 7, 7, 0, -7).fill();
		// Clear some space
		picker.setOuterInnerMaterials(VanillaMaterials.AIR, VanillaMaterials.AIR);
		box.setMinMax(1, 2, 0, 3, 4, 18).fill();
		box.offsetMinMax(-8, 0, 8, 8, 0, -8).fill();
		// Side walls for the bridges
		picker.setOuterInnerMaterials(VanillaMaterials.NETHER_BRICK, VanillaMaterials.NETHER_BRICK);
		box.setMinMax(0, 2, 0, 0, 2, 7).fill();
		box.offsetMinMax(0, 0, 11, 0, 0, 11).fill();
		box.offsetMinMax(4, 0, -11, 4, 0, -11).fill();
		box.offsetMinMax(0, 0, 11, 0, 0, 11).fill();
		box.offsetMinMax(-11, 0, -4, -4, 0, -11).fill();
		box.offsetMinMax(11, 0, 0, 11, 0, 0).fill();
		box.offsetMinMax(-11, 0, 4, -11, 0, 4).fill();
		box.offsetMinMax(11, 0, 0, 11, 0, 0).fill();
		// Add material under the first bridge to make an arch
		box.setMinMax(0, -1, 0, 4, -1, 5).fill();
		box.offsetMinMax(0, 0, 13, 0, 0, 13).fill();
		box.setMinMax(0, -3, 0, 4, -2, 3).fill();
		box.offsetMinMax(0, 0, 15, 0, 0, 15).fill();
		// Build the pillars of the first bridge
		for (int xx = 0; xx <= 4; xx++) {
			for (int zz = 0; zz <= 2; zz++) {
				fillDownwards(xx, -4, zz, 50, VanillaMaterials.NETHER_BRICK);
				fillDownwards(xx, -4, 18 - zz, 50, VanillaMaterials.NETHER_BRICK);
			}
		}
		// Add material under the second bridge to make an arch
		box.setMinMax(-7, -1, 7, -2, -1, 11).fill();
		box.offsetMinMax(13, 0, 0, 13, 0, 0).fill();
		box.setMinMax(-7, -3, 7, -4, -2, 11).fill();
		box.offsetMinMax(15, 0, 0, 15, 0, 0).fill();
		// Build the pillars of the second bridge
		for (int xx = -7; xx <= -5; xx++) {
			for (int zz = 7; zz <= 11; zz++) {
				fillDownwards(xx, -4, zz, 50, VanillaMaterials.NETHER_BRICK);
				fillDownwards(18 - xx, -4, zz, 50, VanillaMaterials.NETHER_BRICK);
			}
		}
	}

	@Override
	public void randomize() {
	}

	@Override
	public List<StructurePiece> getNextComponents() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public BoundingBox getBoundingBox() {
		final Vector3 rotatedMin = transform(-7, -3, 0);
		final Vector3 rotatedMax = transform(11, 4, 18);
		return new BoundingBox(Vector3.min(rotatedMin, rotatedMax), Vector3.max(rotatedMin, rotatedMax));
	}
}
