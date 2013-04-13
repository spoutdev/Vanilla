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

import org.spout.api.math.GenericMath;
import org.spout.api.math.Vector3;

import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.world.generator.structure.PieceCuboidBuilder;
import org.spout.vanilla.world.generator.structure.SimpleBlockMaterialPicker;
import org.spout.vanilla.world.generator.structure.Structure;
import org.spout.vanilla.world.generator.structure.StructurePiece;

public class FortressStaircase extends StructurePiece {
	public FortressStaircase(Structure parent) {
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
		// Built section by section
		for (int zz = 0; zz <= 9; zz++) {
			// Starting and ending yy for the section
			final int syy = Math.max(1, 7 - zz) - 7;
			final int eyy = GenericMath.clamp(syy + 5, 14 - zz, 13) - 7;
			// Basic outline
			picker.setOuterInnerMaterials(VanillaMaterials.NETHER_BRICK, VanillaMaterials.NETHER_BRICK);
			box.setMinMax(0, -7, zz, 4, syy, zz).fill();
			picker.setOuterInnerMaterials(VanillaMaterials.AIR, VanillaMaterials.AIR);
			box.setMinMax(1, syy + 1, zz, 3, eyy - 1, zz).fill();
			// Add some stairs
			if (zz <= 6) {
				setBlockMaterial(1, syy + 1, zz, VanillaMaterials.STAIRS_NETHER_BRICK, (short) 3);
				setBlockMaterial(2, syy + 1, zz, VanillaMaterials.STAIRS_NETHER_BRICK, (short) 3);
				setBlockMaterial(3, syy + 1, zz, VanillaMaterials.STAIRS_NETHER_BRICK, (short) 3);
			}
			// Precise outline for the section 
			picker.setOuterInnerMaterials(VanillaMaterials.NETHER_BRICK, VanillaMaterials.NETHER_BRICK);
			box.setMinMax(0, eyy, zz, 4, eyy, zz).fill();
			box.setMinMax(0, syy + 1, zz, 0, eyy - 1, zz).fill();
			box.offsetMinMax(4, 0, 0, 4, 0, 0).fill();
			// Windows on the right and left with odd spacing
			if (zz % 2 != 0) {
				picker.setOuterInnerMaterials(VanillaMaterials.NETHER_BRICK_FENCE, VanillaMaterials.NETHER_BRICK_FENCE);
				box.setMinMax(0, syy + 2, zz, 0, syy + 3, zz).fill();
				box.offsetMinMax(4, 0, 0, 4, 0, 0).fill();
			}
			// Fill down to the ground
			for (int xx = 0; xx <= 4; xx++) {
				fillDownwards(xx, -8, zz, 50, VanillaMaterials.NETHER_BRICK);
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
		final Vector3 rotatedMin = transform(0, -7, 0);
		final Vector3 rotatedMax = transform(4, 7, 9);
		return new BoundingBox(Vector3.min(rotatedMin, rotatedMax), Vector3.max(rotatedMin, rotatedMax));
	}
}
