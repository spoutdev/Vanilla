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

import java.util.ArrayList;
import java.util.List;

import org.spout.math.imaginary.Quaternion;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.world.generator.structure.PieceCuboidBuilder;
import org.spout.vanilla.world.generator.structure.SimpleBlockMaterialPicker;
import org.spout.vanilla.world.generator.structure.Structure;
import org.spout.vanilla.world.generator.structure.StructurePiece;
import org.spout.vanilla.world.generator.structure.WeightedNextStructurePiece;

public class FortressBalconyIntersection extends WeightedNextStructurePiece {
	private static final WeightedNextPieceCache DEFAULT_NEXT = new WeightedNextPieceCache().
			add(FortressBlazeBalcony.class, 1).
			add(FortressNetherWartStairs.class, 1).
			add(FortressStaircase.class, 3).
			add(FortressRoom.class, 3).
			add(FortressStairRoom.class, 4).
			add(FortressTurn.class, 6).
			add(FortressBridgeIntersection.class, 6).
			add(FortressBridge.class, 8).
			add(FortressCorridor.class, 10);

	public FortressBalconyIntersection(Structure parent) {
		super(parent, DEFAULT_NEXT);
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
		// Floor
		picker.setOuterInnerMaterials(VanillaMaterials.NETHER_BRICK, VanillaMaterials.NETHER_BRICK);
		box.setMinMax(-2, 0, 0, 6, 1, 8).fill();
		// Interior space
		picker.setOuterInnerMaterials(VanillaMaterials.AIR, VanillaMaterials.AIR);
		box.offsetMinMax(0, 2, 0, 0, 4, 0).fill();
		// Roof
		picker.setOuterInnerMaterials(VanillaMaterials.NETHER_BRICK, VanillaMaterials.NETHER_BRICK);
		box.setMinMax(-2, 6, 0, 6, 6, 5).fill();
		// First wall
		box.setMinMax(-2, 2, 0, 0, 5, 0).fill();
		box.offsetMinMax(6, 0, 0, 6, 0, 0).fill();
		// Windows for the first wall
		picker.setOuterInnerMaterials(VanillaMaterials.NETHER_BRICK_FENCE, VanillaMaterials.NETHER_BRICK_FENCE);
		box.setMinMax(-1, 3, 0, -1, 4, 0).fill();
		box.offsetMinMax(6, 0, 0, 6, 0, 0).fill();
		// Balcony floor
		picker.setOuterInnerMaterials(VanillaMaterials.NETHER_BRICK, VanillaMaterials.NETHER_BRICK);
		box.setMinMax(-2, 2, 4, 6, 2, 8).fill();
		// Air spaces next to the balcony entrance
		picker.setOuterInnerMaterials(VanillaMaterials.AIR, VanillaMaterials.AIR);
		box.setMinMax(-1, 1, 4, 0, 2, 4).fill();
		box.offsetMinMax(5, 0, 0, 5, 0, 0).fill();
		// Fences for the balcony
		picker.setOuterInnerMaterials(VanillaMaterials.NETHER_BRICK_FENCE, VanillaMaterials.NETHER_BRICK_FENCE);
		box.setMinMax(-2, 3, 8, 6, 3, 8).fill();
		box.setMinMax(-2, 3, 6, -2, 3, 7).fill();
		box.offsetMinMax(8, 0, 0, 8, 0, 0).fill();
		// Wall separating the room and balcony
		picker.setOuterInnerMaterials(VanillaMaterials.NETHER_BRICK, VanillaMaterials.NETHER_BRICK);
		box.setMinMax(-2, 3, 4, -2, 5, 5).fill();
		box.offsetMinMax(8, 0, 0, 8, 0, 0).fill();
		box.setMinMax(-1, 3, 5, 0, 5, 5).fill();
		box.offsetMinMax(5, 0, 0, 5, 0, 0).fill();
		// Windows for that wall
		picker.setOuterInnerMaterials(VanillaMaterials.NETHER_BRICK_FENCE, VanillaMaterials.NETHER_BRICK_FENCE);
		box.setMinMax(-1, 4, 5, -1, 5, 5).fill();
		box.offsetMinMax(6, 0, 0, 6, 0, 0).fill();
		// Fill down to the ground
		for (int xx = -2; xx <= 6; xx++) {
			for (int zz = 0; zz <= 5; zz++) {
				fillDownwards(xx, -1, zz, 50, VanillaMaterials.NETHER_BRICK);
			}
		}
	}

	@Override
	public void randomize() {
	}

	@Override
	public List<StructurePiece> getNextPieces() {
		final List<StructurePiece> pieces = new ArrayList<StructurePiece>(2);
		final StructurePiece right = getNextPiece();
		right.setPosition(position.add(rotate(-2, 0, 0)));
		right.setRotation(Quaternion.fromAngleDegAxis(-90, 0, 1, 0).mul(rotation));
		right.randomize();
		pieces.add(right);
		final StructurePiece left = getNextPiece();
		left.setPosition(position.add(rotate(7, 0, 4)));
		left.setRotation(Quaternion.fromAngleDegAxis(90, 0, 1, 0).mul(rotation));
		left.randomize();
		pieces.add(left);
		return pieces;
	}

	@Override
	public BoundingBox getBoundingBox() {
		return new BoundingBox(transform(-2, 0, 0), transform(6, 6, 8));
	}
}
