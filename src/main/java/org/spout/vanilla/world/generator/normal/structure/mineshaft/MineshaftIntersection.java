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
package org.spout.vanilla.world.generator.normal.structure.mineshaft;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.spout.api.geo.cuboid.Block;

import org.spout.math.imaginary.Quaternionf;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.world.generator.structure.PieceCuboidBuilder;
import org.spout.vanilla.world.generator.structure.SimpleBlockMaterialPicker;
import org.spout.vanilla.world.generator.structure.Structure;
import org.spout.vanilla.world.generator.structure.StructurePiece;
import org.spout.vanilla.world.generator.structure.WeightedNextStructurePiece;

public class MineshaftIntersection extends WeightedNextStructurePiece {
	private static final WeightedNextPieceCache DEFAULT_NEXT = new WeightedNextPieceCache().
			add(MineshaftCorridor.class, 4).
			add(MineshaftStaircase.class, 1);
	private byte height;

	public MineshaftIntersection(Structure parent) {
		super(parent, DEFAULT_NEXT);
		randomize();
	}

	@Override
	public boolean canPlace() {
		final PieceCuboidBuilder box = new PieceCuboidBuilder(this);
		box.setMinMax(-2, -1, -1, 4, height + 1, 5);
		return !box.intersectsLiquids();
	}

	@Override
	public void place() {
		// building objects
		final PieceCuboidBuilder box = new PieceCuboidBuilder(this);
		final SimpleBlockMaterialPicker picker = new SimpleBlockMaterialPicker();
		box.setPicker(picker);
		// hollow out some space
		picker.setOuterInnerMaterials(VanillaMaterials.AIR, VanillaMaterials.AIR);
		if (hasTwoFloors()) {
			// for the extra floor
			box.setMinMax(0, 0, 0, 2, 2, 4).fill();
			box.offsetMinMax(-1, 0, 1, 1, 0, -1).fill();
			box.setMinMax(0, height - 2, 0, 2, height, 4).fill();
			box.offsetMinMax(-1, 0, 1, 1, 0, -1).fill();
			box.setMinMax(0, 3, 1, 2, 3, 3).fill();
		} else {
			box.setMinMax(0, 0, 0, 2, height, 4).fill();
			box.offsetMinMax(-1, 0, 1, 1, 0, -1).fill();
		}
		// wooden pillars
		picker.setOuterMaterial(VanillaMaterials.PLANK);
		box.setMinMax(0, 0, 1, 0, height, 1).fill();
		box.setMinMax(0, 0, 3, 0, height, 3).fill();
		box.setMinMax(2, 0, 1, 2, height, 1).fill();
		box.setMinMax(2, 0, 3, 2, height, 3).fill();
		// bridge gaps
		for (int xx = -1; xx <= 3; xx++) {
			for (int zz = 0; zz <= 4; zz++) {
				final Block block = getBlock(xx, -1, zz);
				if (block.isMaterial(VanillaMaterials.AIR)) {
					block.setMaterial(VanillaMaterials.PLANK);
				}
			}
		}
	}

	@Override
	public final void randomize() {
		setTwoFloors(getRandom().nextInt(4) == 0);
	}

	@Override
	public List<StructurePiece> getNextPieces() {
		final List<StructurePiece> pieces = new ArrayList<StructurePiece>(3);
		final boolean twoFloors = hasTwoFloors();
		final Random random = getRandom();
		if (random.nextBoolean()) {
			final StructurePiece bottomFront = getNextPiece();
			bottomFront.setPosition(position.add(rotate(0, 0, 5)));
			bottomFront.setRotation(rotation);
			bottomFront.randomize();
			pieces.add(bottomFront);
		}
		if (random.nextBoolean()) {
			final StructurePiece bottomRight = getNextPiece();
			bottomRight.setPosition(position.add(rotate(-1, 0, 1)));
			bottomRight.setRotation(Quaternionf.fromAngleDegAxis(-90, 0, 1, 0).mul(rotation));
			bottomRight.randomize();
			pieces.add(bottomRight);
		}
		if (random.nextBoolean()) {
			final StructurePiece bottomLeft = getNextPiece();
			bottomLeft.setPosition(position.add(rotate(4, 0, 3)));
			bottomLeft.setRotation(Quaternionf.fromAngleDegAxis(90, 0, 1, 0).mul(rotation));
			bottomLeft.randomize();
			pieces.add(bottomLeft);
		}
		if (twoFloors && random.nextBoolean()) {
			final StructurePiece topFront = getNextPiece();
			topFront.setPosition(position.add(rotate(0, 4, 5)));
			topFront.setRotation(rotation);
			topFront.randomize();
			pieces.add(topFront);
		}
		if (twoFloors && random.nextBoolean()) {
			final StructurePiece topRight = getNextPiece();
			topRight.setPosition(position.add(rotate(-1, 4, 1)));
			topRight.setRotation(Quaternionf.fromAngleDegAxis(-90, 0, 1, 0).mul(rotation));
			topRight.randomize();
			pieces.add(topRight);
		}
		if (twoFloors && random.nextBoolean()) {
			final StructurePiece topLeft = getNextPiece();
			topLeft.setPosition(position.add(rotate(4, 4, 3)));
			topLeft.setRotation(Quaternionf.fromAngleDegAxis(90, 0, 1, 0).mul(rotation));
			topLeft.randomize();
			pieces.add(topLeft);
		}
		if (twoFloors && random.nextBoolean()) {
			final StructurePiece topBack = getNextPiece();
			topBack.setPosition(position.add(rotate(2, 4, 0)));
			topBack.setRotation(Quaternionf.fromAngleDegAxis(180, 0, 1, 0).mul(rotation));
			topBack.randomize();
			pieces.add(topBack);
		}
		return pieces;
	}

	public boolean hasTwoFloors() {
		return height > 2;
	}

	public void setTwoFloors(boolean twoFloors) {
		height = (byte) (twoFloors ? 6 : 2);
	}

	@Override
	public BoundingBox getBoundingBox() {
		return new BoundingBox(transform(-1, 0, 0), transform(3, height, 4));
	}
}
