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
import org.spout.vanilla.world.generator.normal.object.LootChestObject;
import org.spout.vanilla.world.generator.structure.PieceCuboidBuilder;
import org.spout.vanilla.world.generator.structure.SimpleBlockMaterialPicker;
import org.spout.vanilla.world.generator.structure.Structure;
import org.spout.vanilla.world.generator.structure.StructurePiece;
import org.spout.vanilla.world.generator.structure.WeightedNextStructurePiece;

public class StrongholdRoom extends WeightedNextStructurePiece {
	private static final WeightedNextPiecesDefaults DEFAULT_NEXT = new WeightedNextPiecesDefaults().
			addDefault(StrongholdChestCorridor.class, 1).
			addDefault(StrongholdPrison.class, 2).
			addDefault(StrongholdCorridor.class, 2).
			addDefault(StrongholdSpiralStaircase.class, 2).
			addDefault(StrongholdStaircase.class, 2);
	private final LootChestObject chestObject;
	private StrongholdRoomType type = null;

	public StrongholdRoom(Structure parent) {
		super(parent, DEFAULT_NEXT);
		chestObject = new LootChestObject(getRandom());
		chestObject.setMinNumberOfStacks(1);
		chestObject.setMaxNumberOfStacks(4);
		chestObject.addMaterial(VanillaMaterials.IRON_INGOT, 10, 1, 5)
				.addMaterial(VanillaMaterials.GOLD_INGOT, 5, 1, 3)
				.addMaterial(VanillaMaterials.REDSTONE_DUST, 5, 4, 9)
				.addMaterial(VanillaMaterials.COAL, 10, 3, 8)
				.addMaterial(VanillaMaterials.BREAD, 15, 1, 3)
				.addMaterial(VanillaMaterials.RED_APPLE, 15, 1, 3)
				.addMaterial(VanillaMaterials.IRON_PICKAXE, 1, 1, 1);
	}

	@Override
	public boolean canPlace() {
		final PieceCuboidBuilder box = new PieceCuboidBuilder(this);
		box.setMinMax(-4, -1, -1, 8, 7, 11);
		return !box.intersectsLiquids();
	}

	@Override
	public void place() {
		// General shape
		final PieceCuboidBuilder box = new PieceCuboidBuilder(this);
		box.setPicker(new StrongholdBlockMaterialPicker(getRandom()));
		box.setMinMax(-3, 0, 0, 7, 6, 10).toggleIgnoreAir().fill();
		box.toggleIgnoreAir();
		// Place the door
		StrongholdDoor.getRandomDoor(this, getRandom()).place(1, 1, 0);
		// More access ways
		box.setPicker(new SimpleBlockMaterialPicker());
		box.setMinMax(1, 1, 10, 3, 3, 10).fill();
		box.setMinMax(-3, 1, 4, -3, 3, 6).fill();
		box.offsetMinMax(10, 0, 0, 10, 0, 0).fill();
		// Add the features for the room type
		if (type == null) {
			return;
		}
		switch (type) {
			case CENTRAL_PILLAR:
				// The pillar
				setBlockMaterial(2, 1, 5, VanillaMaterials.STONE_BRICK);
				setBlockMaterial(2, 2, 5, VanillaMaterials.STONE_BRICK);
				setBlockMaterial(2, 3, 5, VanillaMaterials.STONE_BRICK);
				// Torches on the pillar
				attachMaterial(1, 3, 5, VanillaMaterials.TORCH);
				attachMaterial(3, 3, 5, VanillaMaterials.TORCH);
				attachMaterial(2, 3, 4, VanillaMaterials.TORCH);
				attachMaterial(2, 3, 6, VanillaMaterials.TORCH);
				// Slabs all around
				setBlockMaterial(1, 1, 4, VanillaMaterials.SLAB);
				setBlockMaterial(1, 1, 5, VanillaMaterials.SLAB);
				setBlockMaterial(1, 1, 6, VanillaMaterials.SLAB);
				setBlockMaterial(3, 1, 4, VanillaMaterials.SLAB);
				setBlockMaterial(3, 1, 5, VanillaMaterials.SLAB);
				setBlockMaterial(3, 1, 6, VanillaMaterials.SLAB);
				setBlockMaterial(2, 1, 4, VanillaMaterials.SLAB);
				setBlockMaterial(2, 1, 6, VanillaMaterials.SLAB);
				break;
			case FOUNTAIN:
				// The pool
				for (int i = 0; i < 5; i++) {
					setBlockMaterial(0, 1, 3 + i, VanillaMaterials.STONE_BRICK);
					setBlockMaterial(4, 1, 3 + i, VanillaMaterials.STONE_BRICK);
					setBlockMaterial(i, 1, 3, VanillaMaterials.STONE_BRICK);
					setBlockMaterial(i, 1, 7, VanillaMaterials.STONE_BRICK);
				}
				// Pillar at the center of the fountain
				setBlockMaterial(2, 1, 5, VanillaMaterials.STONE_BRICK);
				setBlockMaterial(2, 2, 5, VanillaMaterials.STONE_BRICK);
				setBlockMaterial(2, 3, 5, VanillaMaterials.STONE_BRICK);
				// The water source
				setBlockMaterial(2, 4, 5, VanillaMaterials.WATER);
				break;
			case TWO_FLOORS:
				// Cobblestone ring for the upper floor
				for (int i = 1; i <= 9; i++) {
					setBlockMaterial(-2, 3, i, VanillaMaterials.COBBLESTONE);
					setBlockMaterial(6, 3, i, VanillaMaterials.COBBLESTONE);
					setBlockMaterial(i - 3, 3, 1, VanillaMaterials.COBBLESTONE);
					setBlockMaterial(i - 3, 3, 9, VanillaMaterials.COBBLESTONE);
				}
				// Second and third cobblestone rings on the bottom and upper floors
				setBlockMaterial(2, 1, 4, VanillaMaterials.COBBLESTONE);
				setBlockMaterial(2, 1, 6, VanillaMaterials.COBBLESTONE);
				setBlockMaterial(2, 3, 4, VanillaMaterials.COBBLESTONE);
				setBlockMaterial(2, 3, 6, VanillaMaterials.COBBLESTONE);
				setBlockMaterial(1, 1, 5, VanillaMaterials.COBBLESTONE);
				setBlockMaterial(3, 1, 5, VanillaMaterials.COBBLESTONE);
				setBlockMaterial(1, 3, 5, VanillaMaterials.COBBLESTONE);
				setBlockMaterial(3, 3, 5, VanillaMaterials.COBBLESTONE);
				// Cobblestone pillars to the upper floor in the middle
				for (int i = 1; i <= 3; i++) {
					setBlockMaterial(1, i, 4, VanillaMaterials.COBBLESTONE);
					setBlockMaterial(3, i, 4, VanillaMaterials.COBBLESTONE);
					setBlockMaterial(1, i, 6, VanillaMaterials.COBBLESTONE);
					setBlockMaterial(3, i, 6, VanillaMaterials.COBBLESTONE);
				}
				// A torch in the middle
				attachMaterial(2, 3, 5, VanillaMaterials.TORCH);
				// A wood ring to complete the upper floor
				for (int i = 2; i <= 8; ++i) {
					setBlockMaterial(-1, 3, i, VanillaMaterials.PLANK);
					setBlockMaterial(0, 3, i, VanillaMaterials.PLANK);
					if (i <= 3 || i >= 7) {
						setBlockMaterial(1, 3, i, VanillaMaterials.PLANK);
						setBlockMaterial(2, 3, i, VanillaMaterials.PLANK);
						setBlockMaterial(3, 3, i, VanillaMaterials.PLANK);
					}
					setBlockMaterial(4, 3, i, VanillaMaterials.PLANK);
					setBlockMaterial(5, 3, i, VanillaMaterials.PLANK);
				}
				// A ladder to the upper floor
				setBlockMaterial(6, 1, 3, VanillaMaterials.LADDER, (short) 4);
				setBlockMaterial(6, 2, 3, VanillaMaterials.LADDER, (short) 4);
				setBlockMaterial(6, 3, 3, VanillaMaterials.LADDER, (short) 4);
				// Place the loot chest
				chestObject.setRandom(getRandom());
				placeObject(0, 4, 8, chestObject);
				break;
		}
	}

	@Override
	public void randomize() {
		final Random random = getRandom();
		if (random.nextInt(5) < 3) {
			type = StrongholdRoomType.getRandomType(random);
		} else {
			type = null;
		}
	}

	@Override
	public List<StructurePiece> getNextPieces() {
		final List<StructurePiece> pieces = new ArrayList<StructurePiece>();
		final StructurePiece nextFront = getNextPiece();
		nextFront.setPosition(position.add(rotate(0, 0, 11)));
		nextFront.setRotation(rotation);
		nextFront.randomize();
		pieces.add(nextFront);
		final StructurePiece nextRight = getNextPiece();
		nextRight.setPosition(position.add(rotate(-4, 0, 3)));
		nextRight.setRotation(rotation.rotate(-90, 0, 1, 0));
		nextRight.randomize();
		pieces.add(nextRight);
		final StructurePiece nextLeft = getNextPiece();
		nextLeft.setPosition(position.add(rotate(8, 0, 7)));
		nextLeft.setRotation(rotation.rotate(90, 0, 1, 0));
		nextLeft.randomize();
		pieces.add(nextLeft);
		return pieces;
	}

	@Override
	public BoundingBox getBoundingBox() {
		return new BoundingBox(transform(-3, 0, 0), transform(7, 6, 10));
	}

	public StrongholdRoomType getType() {
		return type;
	}

	public void setType(StrongholdRoomType type) {
		this.type = type;
	}

	public static enum StrongholdRoomType {
		CENTRAL_PILLAR,
		FOUNTAIN,
		TWO_FLOORS;

		public static StrongholdRoomType getRandomType(Random random) {
			final StrongholdRoomType[] types = StrongholdRoomType.values();
			return types[random.nextInt(types.length)];
		}
	}
}
