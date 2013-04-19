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

import org.spout.api.material.block.BlockFace;

import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.world.generator.structure.PieceCuboidBuilder;
import org.spout.vanilla.world.generator.structure.SimpleBlockMaterialPicker;
import org.spout.vanilla.world.generator.structure.Structure;
import org.spout.vanilla.world.generator.structure.StructurePiece;
import org.spout.vanilla.world.generator.structure.WeightedNextStructurePiece;

public class StrongholdPrison extends WeightedNextStructurePiece {
	private static final WeightedNextPieceCache DEFAULT_NEXT = new WeightedNextPieceCache().
			add(StrongholdLibrary.class, 5).
			add(StrongholdLargeIntersection.class, 5).
			add(StrongholdSpiralStaircase.class, 15).
			add(StrongholdRoom.class, 15).
			add(StrongholdPrison.class, 15).
			add(StrongholdIntersection.class, 15).
			add(StrongholdStaircase.class, 15).
			add(StrongholdTurn.class, 15);

	public StrongholdPrison(Structure parent) {
		super(parent, DEFAULT_NEXT);
	}

	@Override
	public boolean canPlace() {
		final PieceCuboidBuilder box = new PieceCuboidBuilder(this);
		box.setMinMax(-1, -1, -1, 9, 5, 11);
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
		box.setMinMax(0, 0, 0, 8, 4, 10).toggleIgnoreAir().fill();
		box.toggleIgnoreAir();
		// Place the door
		StrongholdDoor.getRandomDoor(this, getRandom()).place(1, 1, 0);
		// Make some room
		box.setPicker(picker);
		box.setMinMax(1, 1, 10, 3, 3, 10).fill();
		// Cell walls
		box.setPicker(stone);
		box.setMinMax(4, 1, 1, 4, 3, 1).fill();
		box.offsetMinMax(0, 0, 2, 0, 0, 2).fill();
		box.offsetMinMax(0, 0, 4, 0, 0, 4).fill();
		box.offsetMinMax(0, 0, 2, 0, 0, 2).fill();
		// Build the cells
		box.setPicker(picker);
		picker.setOuterInnerMaterials(VanillaMaterials.IRON_BARS, VanillaMaterials.IRON_BARS);
		box.setMinMax(4, 1, 4, 4, 3, 6).fill();
		box.setMinMax(5, 1, 5, 7, 3, 5).fill();
		setBlockMaterial(4, 3, 2, VanillaMaterials.IRON_BARS);
		setBlockMaterial(4, 3, 8, VanillaMaterials.IRON_BARS);
		// Add the cell doors
		placeDoor(4, 1, 2, VanillaMaterials.IRON_DOOR_BLOCK, BlockFace.NORTH);
		placeDoor(4, 1, 8, VanillaMaterials.IRON_DOOR_BLOCK, BlockFace.NORTH);
	}

	@Override
	public void randomize() {
	}

	@Override
	public List<StructurePiece> getNextPieces() {
		final StructurePiece piece = getNextPiece();
		piece.setPosition(position.add(rotate(0, 0, 11)));
		piece.setRotation(rotation);
		piece.randomize();
		return Lists.newArrayList(piece);
	}

	@Override
	public BoundingBox getBoundingBox() {
		return new BoundingBox(transform(0, 0, 0), transform(8, 4, 10));
	}
}
