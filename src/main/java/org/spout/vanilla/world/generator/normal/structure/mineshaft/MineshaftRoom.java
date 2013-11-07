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

import org.spout.math.GenericMath;
import org.spout.math.imaginary.Quaternionf;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.world.generator.structure.PieceCuboidBuilder;
import org.spout.vanilla.world.generator.structure.SimpleBlockMaterialPicker;
import org.spout.vanilla.world.generator.structure.Structure;
import org.spout.vanilla.world.generator.structure.StructurePiece;
import org.spout.vanilla.world.generator.structure.WeightedNextStructurePiece;

public class MineshaftRoom extends WeightedNextStructurePiece {
	private static final WeightedNextPieceCache DEFAULT_NEXT = new WeightedNextPieceCache().
			add(MineshaftCorridor.class, 4).
			add(MineshaftStaircase.class, 1);
	private int height;
	private int depth;
	private int xStart;
	private int xEnd;

	public MineshaftRoom(Structure parent) {
		super(parent, DEFAULT_NEXT);
		randomize();
	}

	@Override
	public boolean canPlace() {
		final PieceCuboidBuilder box = new PieceCuboidBuilder(this);
		box.setMinMax(xStart - 1, -2, -1, xEnd + 1, height, depth + 1);
		return !box.intersectsLiquids();
	}

	@Override
	public void place() {
		// building objects
		final PieceCuboidBuilder box = new PieceCuboidBuilder(this);
		final SimpleBlockMaterialPicker picker = new SimpleBlockMaterialPicker();
		box.setPicker(picker);
		// basic room
		picker.setOuterInnerMaterials(VanillaMaterials.DIRT, VanillaMaterials.AIR);
		box.setMinMax(xStart, -1, 0, xEnd, height - 1, depth).toggleIgnoreAir().fill();
		box.toggleIgnoreAir();
		picker.setOuterMaterial(VanillaMaterials.AIR);
		box.setMinMax(xStart, 0, 0, xEnd, 2, depth).fill();
		// some 'decoration'
		box.setMinMax(xStart, 3, 0, xEnd, height - 1, depth).sphericalFill();
	}

	@Override
	public final void randomize() {
		final Random random = getRandom();
		setLength(7 + random.nextInt(6));
		setHeight(4 + random.nextInt(6));
		setDepth(7 + random.nextInt(6));
	}

	@Override
	public List<StructurePiece> getNextPieces() {
		final List<StructurePiece> pieces = new ArrayList<StructurePiece>(3);
		final Random random = getRandom();
		if (random.nextBoolean()) {
			final StructurePiece front = getNextPiece();
			front.setPosition(position.add(rotate(0, 0, depth)));
			front.setRotation(rotation);
			front.randomize();
			pieces.add(front);
		}
		if (random.nextBoolean()) {
			final StructurePiece right = getNextPiece();
			right.setPosition(position.add(rotate(xStart, 0, depth / 2)));
			right.setRotation(Quaternionf.fromAngleDegAxis(-90, 0, 1, 0).mul(rotation));
			right.randomize();
			pieces.add(right);
		}
		if (random.nextBoolean()) {
			final StructurePiece left = getNextPiece();
			left.setPosition(position.add(rotate(xEnd, 0, depth / 2)));
			left.setRotation(Quaternionf.fromAngleDegAxis(90, 0, 1, 0).mul(rotation));
			left.randomize();
			pieces.add(left);
		}
		return pieces;
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getLength() {
		return xEnd - xStart;
	}

	public void setLength(int length) {
		xStart = GenericMath.floor(-length / 2f + 1.5f);
		xEnd = GenericMath.floor(length / 2f + 1.5f);
	}

	@Override
	public BoundingBox getBoundingBox() {
		return new BoundingBox(transform(xStart, -1, 0), transform(xEnd, height - 1, depth));
	}
}
