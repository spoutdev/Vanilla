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
package org.spout.vanilla.world.generator.normal.structure.stronghold;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.world.generator.normal.structure.stronghold.StrongholdDoor.EmptyDoorway;
import org.spout.vanilla.world.generator.structure.PieceCuboidBuilder;
import org.spout.vanilla.world.generator.structure.SimpleBlockMaterialPicker;
import org.spout.vanilla.world.generator.structure.Structure;
import org.spout.vanilla.world.generator.structure.StructurePiece;
import org.spout.vanilla.world.generator.structure.WeightedNextStructurePiece;

public class StrongholdIntersection extends WeightedNextStructurePiece {
	private static final WeightedNextPieceCache DEFAULT_NEXT = new WeightedNextPieceCache().
			add(StrongholdChestCorridor.class, 1).
			add(StrongholdPrison.class, 2).
			add(StrongholdCorridor.class, 2).
			add(StrongholdSpiralStaircase.class, 2).
			add(StrongholdStaircase.class, 2);
	private boolean nextComponentRight = false;
	private boolean nextComponentLeft = false;

	public StrongholdIntersection(Structure parent) {
		super(parent, DEFAULT_NEXT);
	}

	@Override
	public boolean canPlace() {
		final PieceCuboidBuilder box = new PieceCuboidBuilder(this);
		box.setMinMax(-1, -1, -1, 5, 5, 7);
		return !box.intersectsLiquids();
	}

	@Override
	public void place() {
		// General shape
		final PieceCuboidBuilder box = new PieceCuboidBuilder(this);
		box.setPicker(new StrongholdBlockMaterialPicker(getRandom()));
		box.setMinMax(0, 0, 0, 4, 4, 6).toggleIgnoreAir().fill();
		box.toggleIgnoreAir();
		// Place the doors
		StrongholdDoor.getRandomDoor(this, getRandom()).place(1, 1, 0);
		new EmptyDoorway(this).place(1, 1, 6);
		// Place random torches
		attachMaterial(0.1f, 1, 2, 1, VanillaMaterials.TORCH);
		attachMaterial(0.1f, 3, 2, 1, VanillaMaterials.TORCH);
		attachMaterial(0.1f, 1, 2, 5, VanillaMaterials.TORCH);
		attachMaterial(0.1f, 3, 2, 5, VanillaMaterials.TORCH);
		// Access for the next components
		box.setPicker(new SimpleBlockMaterialPicker());
		if (nextComponentRight) {
			box.setMinMax(0, 1, 2, 0, 3, 4).fill();
		}
		if (nextComponentLeft) {
			box.setMinMax(4, 1, 2, 4, 3, 4).fill();
		}
	}

	@Override
	public void randomize() {
		final Random random = getRandom();
		nextComponentRight = random.nextBoolean();
		nextComponentLeft = random.nextBoolean();
	}

	@Override
	public List<StructurePiece> getNextPieces() {
		final List<StructurePiece> pieces = new ArrayList<StructurePiece>();
		final StructurePiece piece = getNextPiece();
		piece.setPosition(position.add(rotate(0, 0, 7)));
		piece.setRotation(rotation);
		piece.randomize();
		pieces.add(piece);
		if (nextComponentRight) {
			final StructurePiece next = getNextPiece();
			next.setPosition(position.add(rotate(-1, 0, 1)));
			next.setRotation(rotation.rotate(-90, 0, 1, 0));
			next.randomize();
			pieces.add(next);
		}
		if (nextComponentLeft) {
			final StructurePiece next = getNextPiece();
			next.setPosition(position.add(rotate(5, 0, 5)));
			next.setRotation(rotation.rotate(90, 0, 1, 0));
			next.randomize();
			pieces.add(next);
		}
		return pieces;
	}

	@Override
	public BoundingBox getBoundingBox() {
		return new BoundingBox(transform(0, 0, 0), transform(4, 4, 6));
	}

	public boolean hasNextComponentRight() {
		return nextComponentRight;
	}

	public void setNextComponentRight(boolean nextComponentRight) {
		this.nextComponentRight = nextComponentRight;
	}

	public boolean hasNextComponentLeft() {
		return nextComponentLeft;
	}

	public void setNextComponentLeft(boolean nextComponentLeft) {
		this.nextComponentLeft = nextComponentLeft;
	}
}
