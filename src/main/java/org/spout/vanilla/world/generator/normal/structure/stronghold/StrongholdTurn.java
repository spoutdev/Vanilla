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

import java.util.List;

import com.google.common.collect.Lists;

import org.spout.vanilla.world.generator.structure.PieceCuboidBuilder;
import org.spout.vanilla.world.generator.structure.SimpleBlockMaterialPicker;
import org.spout.vanilla.world.generator.structure.Structure;
import org.spout.vanilla.world.generator.structure.StructurePiece;
import org.spout.vanilla.world.generator.structure.WeightedNextStructurePiece;

public class StrongholdTurn extends WeightedNextStructurePiece {
	private static final WeightedNextPiecesDefaults DEFAULT_NEXT = new WeightedNextPiecesDefaults().
			addDefault(StrongholdChestCorridor.class, 1).
			addDefault(StrongholdPrison.class, 2).
			addDefault(StrongholdCorridor.class, 2).
			addDefault(StrongholdSpiralStaircase.class, 2).
			addDefault(StrongholdStaircase.class, 2).
			addDefault(StrongholdIntersection.class, 2);
	private boolean left = false;

	public StrongholdTurn(Structure parent) {
		super(parent, DEFAULT_NEXT);
	}

	@Override
	public boolean canPlace() {
		final PieceCuboidBuilder box = new PieceCuboidBuilder(this);
		box.setMinMax(-1, -1, -1, 5, 5, 5);
		return !box.intersectsLiquids();
	}

	@Override
	public void place() {
		// Building objects
		final PieceCuboidBuilder box = new PieceCuboidBuilder(this);
		// General shape
		box.setPicker(new StrongholdBlockMaterialPicker(getRandom()));
		box.setMinMax(0, 0, 0, 4, 4, 4).toggleIgnoreAir().fill();
		box.toggleIgnoreAir();
		// Place the door
		StrongholdDoor.getRandomDoor(this, getRandom()).place(1, 1, 0);
		// Place the access way depending on the direction
		box.setPicker(new SimpleBlockMaterialPicker());
		if (left) {
			box.setMinMax(4, 1, 1, 4, 3, 3).fill();
		} else {
			box.setMinMax(0, 1, 1, 0, 3, 3).fill();
		}
	}

	@Override
	public void randomize() {
		left = getRandom().nextBoolean();
	}

	@Override
	public List<StructurePiece> getNextPieces() {
		final StructurePiece piece = getNextPiece();
		if (left) {
			piece.setPosition(position.add(rotate(5, 0, 4)));
			piece.setRotation(rotation.rotate(90, 0, 1, 0));
		} else {
			piece.setPosition(position.add(rotate(-1, 0, 0)));
			piece.setRotation(rotation.rotate(-90, 0, 1, 0));
		}
		piece.randomize();
		return Lists.newArrayList(piece);
	}

	@Override
	public BoundingBox getBoundingBox() {
		return new BoundingBox(transform(0, 0, 0), transform(4, 4, 4));
	}

	public void setLeft(boolean left) {
		this.left = left;
	}

	public boolean isLeft() {
		return left;
	}
}
