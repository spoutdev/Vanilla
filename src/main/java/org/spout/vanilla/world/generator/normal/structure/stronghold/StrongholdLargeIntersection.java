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
import org.spout.vanilla.world.generator.structure.PieceCuboidBuilder;
import org.spout.vanilla.world.generator.structure.SimpleBlockMaterialPicker;
import org.spout.vanilla.world.generator.structure.Structure;
import org.spout.vanilla.world.generator.structure.StructurePiece;
import org.spout.vanilla.world.generator.structure.WeightedNextStructurePiece;

public class StrongholdLargeIntersection extends WeightedNextStructurePiece {
	private static final WeightedNextPiecesDefaults DEFAULT_NEXT = new WeightedNextPiecesDefaults().
			addDefault(StrongholdChestCorridor.class, 1).
			addDefault(StrongholdPrison.class, 2).
			addDefault(StrongholdCorridor.class, 2).
			addDefault(StrongholdSpiralStaircase.class, 2).
			addDefault(StrongholdStaircase.class, 2);
	private boolean nextComponentRightLow = false;
	private boolean nextComponentRightHigh = false;
	private boolean nextComponentLeftLow = false;
	private boolean nextComponentLeftHigh = false;

	public StrongholdLargeIntersection(Structure parent) {
		super(parent, DEFAULT_NEXT);
	}

	@Override
	public boolean canPlace() {
		final PieceCuboidBuilder box = new PieceCuboidBuilder(this);
		box.setMinMax(-4, -3, -1, 7, 7, 11);
		return !box.intersectsLiquids();
	}

	@Override
	public void place() {
		// Building objects
		final PieceCuboidBuilder box = new PieceCuboidBuilder(this);
		final SimpleBlockMaterialPicker picker = new SimpleBlockMaterialPicker();
		final StrongholdBlockMaterialPicker stone = new StrongholdBlockMaterialPicker(getRandom());
		// General shape
		box.setPicker(stone);
		box.setMinMax(-3, -2, 0, 6, 6, 10).toggleIgnoreAir().fill();
		box.toggleIgnoreAir();
		// Place the door
		StrongholdDoor.getRandomDoor(this, getRandom()).place(1, 1, 0);
		// Access to the next components of the intersection
		box.setPicker(picker);
		if (nextComponentRightLow) {
			box.setMinMax(-3, 1, 1, -3, 3, 3).fill();
		}
		if (nextComponentRightHigh) {
			box.setMinMax(-3, 3, 7, -3, 5, 9).fill();
		}
		if (nextComponentLeftLow) {
			box.setMinMax(6, 1, 1, 6, 3, 3).fill();
		}
		if (nextComponentLeftHigh) {
			box.setMinMax(6, 3, 7, 6, 5, 9).fill();
		}
		// The bottom access way
		box.setMinMax(2, -1, 10, 4, 1, 10).fill();
		// Finish the interior
		box.setPicker(stone);
		box.setMinMax(-2, 0, 1, 5, 0, 6).fill();
		box.setMinMax(1, -1, 5, 1, 2, 9).fill();
		box.offsetMinMax(4, 0, 0, 4, 0, 0).fill();
		box.setMinMax(-2, 2, 7, 0, 2, 9).fill();
		box.setMinMax(-2, 1, 5, 0, 1, 6).fill();
		picker.setOuterInnerMaterials(VanillaMaterials.SLAB, VanillaMaterials.SLAB);
		box.setPicker(picker);
		box.setMinMax(-2, 1, 4, 0, 1, 4).fill();
		box.offsetMinMax(0, 1, 2, 0, 1, 2).fill();
		box.setPicker(stone);
		box.setMinMax(2, -1, 7, 4, -1, 8).fill();
		box.setPicker(picker);
		box.offsetMinMax(0, 0, 2, 0, 0, 1).fill();
		box.offsetMinMax(0, 1, -2, 0, 1, -2).fill();
		box.setMinMax(1, 3, 7, 1, 3, 9).fill();
		box.offsetMinMax(4, 0, 0, 4, 0, 0).fill();
		box.offsetMinMax(-3, 0, 0, -1, 0, 0).fill();
		// Place a torch
		attachMaterial(3, 3, 6, VanillaMaterials.TORCH);
	}

	@Override
	public void randomize() {
		final Random random = getRandom();
		nextComponentRightLow = random.nextBoolean();
		nextComponentRightHigh = random.nextBoolean();
		nextComponentLeftLow = random.nextBoolean();
		nextComponentLeftHigh = random.nextInt(3) > 0;
	}

	@Override
	public List<StructurePiece> getNextPieces() {
		final List<StructurePiece> pieces = new ArrayList<StructurePiece>();
		final StructurePiece piece = getNextPiece();
		piece.setPosition(position.add(rotate(1, -2, 11)));
		piece.setRotation(rotation);
		piece.randomize();
		pieces.add(piece);
		if (nextComponentRightLow) {
			final StructurePiece next = getNextPiece();
			next.setPosition(position.add(rotate(-4, 0, 0)));
			next.setRotation(rotation.rotate(-90, 0, 1, 0));
			next.randomize();
			pieces.add(next);
		}
		if (nextComponentRightHigh) {
			final StructurePiece next = getNextPiece();
			next.setPosition(position.add(rotate(-4, 2, 6)));
			next.setRotation(rotation.rotate(-90, 0, 1, 0));
			next.randomize();
			pieces.add(next);
		}
		if (nextComponentLeftLow) {
			final StructurePiece next = getNextPiece();
			next.setPosition(position.add(rotate(7, 0, 4)));
			next.setRotation(rotation.rotate(90, 0, 1, 0));
			next.randomize();
			pieces.add(next);
		}
		if (nextComponentLeftHigh) {
			final StructurePiece next = getNextPiece();
			next.setPosition(position.add(rotate(7, 2, 10)));
			next.setRotation(rotation.rotate(90, 0, 1, 0));
			next.randomize();
			pieces.add(next);
		}
		return pieces;
	}

	@Override
	public BoundingBox getBoundingBox() {
		return new BoundingBox(transform(-3, -2, 0), transform(6, 6, 10));
	}

	public boolean hasNextComponentRightLow() {
		return nextComponentRightLow;
	}

	public void setNextComponentRightLow(boolean nextComponentRightLow) {
		this.nextComponentRightLow = nextComponentRightLow;
	}

	public boolean hasNextComponentRightHigh() {
		return nextComponentRightHigh;
	}

	public void setNextComponentRightHigh(boolean nextComponentRightHigh) {
		this.nextComponentRightHigh = nextComponentRightHigh;
	}

	public boolean hasNextComponentLeftLow() {
		return nextComponentLeftLow;
	}

	public void setNextComponentLeftLow(boolean nextComponentLeftLow) {
		this.nextComponentLeftLow = nextComponentLeftLow;
	}

	public boolean hasNextComponentLeftHigh() {
		return nextComponentLeftHigh;
	}

	public void setNextComponentLeftHigh(boolean nextComponentLeftHigh) {
		this.nextComponentLeftHigh = nextComponentLeftHigh;
	}
}
