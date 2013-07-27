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
package org.spout.vanilla.world.generator.nether.structure.fortress;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.world.generator.structure.PieceCuboidBuilder;
import org.spout.vanilla.world.generator.structure.SimpleBlockMaterialPicker;
import org.spout.vanilla.world.generator.structure.Structure;
import org.spout.vanilla.world.generator.structure.StructurePiece;

public class FortressEnd extends StructurePiece {
	public FortressEnd(Structure parent) {
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
		final Random random = getRandom();
		// Partially built higher part of the bridge
		picker.setOuterInnerMaterials(VanillaMaterials.NETHER_BRICK, VanillaMaterials.NETHER_BRICK);
		for (int xx = 0; xx <= 4; xx++) {
			for (int yy = 0; yy <= 1; yy++) {
				box.setMinMax(xx, yy, 0, xx, yy, random.nextInt(8)).fill();
			}
		}
		// Partially built side walls of the bridge
		box.setMinMax(0, 2, 0, 0, 2, random.nextInt(8)).fill();
		box.setMinMax(4, 2, 0, 4, 2, random.nextInt(8)).fill();
		// Partially built middle part of the bridge
		for (int xx = 0; xx <= 4; xx++) {
			box.setMinMax(xx, -1, 0, xx, -1, random.nextInt(5)).fill();
		}
		// Partially built lower part of the bridge
		for (int xx = 0; xx <= 4; xx++) {
			for (int yy = -3; yy <= -2; yy++) {
				box.setMinMax(xx, yy, 0, xx, yy, random.nextInt(3)).fill();
			}
		}
	}

	@Override
	public void randomize() {
	}

	@Override
	public List<StructurePiece> getNextPieces() {
		return Collections.emptyList();
	}

	@Override
	public BoundingBox getBoundingBox() {
		return new BoundingBox(transform(0, -3, 0), transform(4, 4, 7));
	}
}
