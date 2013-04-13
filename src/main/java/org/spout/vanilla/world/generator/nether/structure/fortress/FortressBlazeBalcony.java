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

public class FortressBlazeBalcony extends StructurePiece {
	public FortressBlazeBalcony(Structure parent) {
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
		// Clear some space
		picker.setOuterInnerMaterials(VanillaMaterials.AIR, VanillaMaterials.AIR);
		box.setMinMax(-1, 2, 0, 5, 7, 7).fill();
		// Base of the balcony
		picker.setOuterInnerMaterials(VanillaMaterials.NETHER_BRICK, VanillaMaterials.NETHER_BRICK);
		box.setMinMax(0, 0, 0, 4, 1, 7).fill();
		// Stairs to the balcony
		box.setMinMax(0, 2, 1, 4, 2, 7).fill();
		box.offsetMinMax(0, 1, 1, 0, 1, 0).fill();
		box.offsetMinMax(0, 1, 1, 0, 1, 0).fill();
		// Walls on the sides of the stairs
		box.setMinMax(0, 2, 0, 0, 4, 2).fill();
		box.offsetMinMax(4, 0, 0, 4, 0, 0).fill();
		box.setMinMax(0, 5, 2, 0, 5, 3).fill();
		box.offsetMinMax(4, 0, 0, 4, 0, 0).fill();
		// Side walls of the balcony
		box.setMinMax(-1, 5, 3, -1, 5, 8).fill();
		box.offsetMinMax(6, 0, 0, 6, 0, 0).fill();
		box.setMinMax(0, 5, 8, 4, 5, 8).fill();
		// Fences above the side walls of the balcony
		setBlockMaterial(0, 6, 3, VanillaMaterials.NETHER_BRICK_FENCE);
		setBlockMaterial(4, 6, 3, VanillaMaterials.NETHER_BRICK_FENCE);
		picker.setOuterInnerMaterials(VanillaMaterials.NETHER_BRICK_FENCE, VanillaMaterials.NETHER_BRICK_FENCE);
		box.setMinMax(-1, 6, 3, -1, 6, 8).fill();
		box.offsetMinMax(6, 0, 0, 6, 0, 0).fill();
		box.setMinMax(0, 6, 8, 4, 7, 8).fill();
		box.setMinMax(1, 8, 8, -3, 8, 8).fill();
		setBlockMaterial(2, 5, 5, VanillaMaterials.MONSTER_SPAWNER);
		// Fill down to the ground
		for (int xx = -1; xx <= 5; xx++) {
			for (int zz = 0; zz <= 6; zz++) {
				fillDownwards(xx, -1, zz, 50, VanillaMaterials.NETHER_BRICK);
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
		final Vector3 rotatedMin = transform(-1, 0, 0);
		final Vector3 rotatedMax = transform(5, 8, 8);
		return new BoundingBox(Vector3.min(rotatedMin, rotatedMax), Vector3.max(rotatedMin, rotatedMax));
	}
}
