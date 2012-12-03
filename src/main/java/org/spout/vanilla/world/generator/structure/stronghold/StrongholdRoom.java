/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, VanillaDev <http://www.spout.org/>
 * Vanilla is licensed under the SpoutDev License Version 1.
 *
 * Vanilla is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the SpoutDev License Version 1.
 *
 * Vanilla is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the SpoutDev License Version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license,
 * including the MIT license.
 */
package org.spout.vanilla.world.generator.structure.stronghold;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.spout.api.math.MathHelper;
import org.spout.api.math.Vector3;

import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.world.generator.normal.object.LootChestObject;
import org.spout.vanilla.world.generator.structure.ComponentCuboidPart;
import org.spout.vanilla.world.generator.structure.SimpleBlockMaterialPicker;
import org.spout.vanilla.world.generator.structure.Structure;
import org.spout.vanilla.world.generator.structure.StructureComponent;

public class StrongholdRoom extends StructureComponent {
	private final LootChestObject lootChest = new LootChestObject();
	private StrongholdRoomType type = null;

	public StrongholdRoom(Structure parent) {
		super(parent);
		lootChest.addMaterial(VanillaMaterials.IRON_BARS, 0.1, 1, 3);// TODO: get the proper contents
	}

	@Override
	public boolean canPlace() {
		final ComponentCuboidPart box = new ComponentCuboidPart(this);
		box.setMinMax(-1, -1, -1, 11, 7, 11);
		return !box.intersectsLiquids();
	}

	@Override
	public void place() {
		// General shape
		final ComponentCuboidPart box = new ComponentCuboidPart(this);
		box.setPicker(new StrongholdBlockMaterialPicker(getRandom()));
		box.setMinMax(0, 0, 0, 10, 6, 10);
		box.fill(true);
		// Place the door
		StrongholdDoor.getRandomDoor(this, getRandom()).place(4, 1, 0);
		// More access ways
		box.setPicker(new SimpleBlockMaterialPicker());
		box.setMinMax(4, 1, 10, 6, 3, 10);
		box.fill(false);
		box.setMinMax(0, 1, 4, 0, 3, 6);
		box.fill(false);
		box.offsetMinMax(10, 0, 0, 10, 0, 0);
		box.fill(false);
		// Add the features for the room type
		if (type == null) {
			return;
		}
		switch (type) {
			case CENTRAL_PILLAR:
				// The pillar
				setBlockMaterial(5, 1, 5, VanillaMaterials.STONE_BRICK);
				setBlockMaterial(5, 2, 5, VanillaMaterials.STONE_BRICK);
				setBlockMaterial(5, 3, 5, VanillaMaterials.STONE_BRICK);
				// Torches on the pillar
				attachMaterial(4, 3, 5, VanillaMaterials.TORCH);
				attachMaterial(6, 3, 5, VanillaMaterials.TORCH);
				attachMaterial(5, 3, 4, VanillaMaterials.TORCH);
				attachMaterial(5, 3, 6, VanillaMaterials.TORCH);
				// Slabs all around
				setBlockMaterial(4, 1, 4, VanillaMaterials.SLAB);
				setBlockMaterial(4, 1, 5, VanillaMaterials.SLAB);
				setBlockMaterial(4, 1, 6, VanillaMaterials.SLAB);
				setBlockMaterial(6, 1, 4, VanillaMaterials.SLAB);
				setBlockMaterial(6, 1, 5, VanillaMaterials.SLAB);
				setBlockMaterial(6, 1, 6, VanillaMaterials.SLAB);
				setBlockMaterial(5, 1, 4, VanillaMaterials.SLAB);
				setBlockMaterial(5, 1, 6, VanillaMaterials.SLAB);
				break;
			case FOUNTAIN:
				// The pool
				for (int i = 0; i < 5; i++) {
					setBlockMaterial(3, 1, 3 + i, VanillaMaterials.STONE_BRICK);
					setBlockMaterial(7, 1, 3 + i, VanillaMaterials.STONE_BRICK);
					setBlockMaterial(3 + i, 1, 3, VanillaMaterials.STONE_BRICK);
					setBlockMaterial(3 + i, 1, 7, VanillaMaterials.STONE_BRICK);
				}
				// Pillar at the center of the fountain
				setBlockMaterial(5, 1, 5, VanillaMaterials.STONE_BRICK);
				setBlockMaterial(5, 2, 5, VanillaMaterials.STONE_BRICK);
				setBlockMaterial(5, 3, 5, VanillaMaterials.STONE_BRICK);
				// The water source
				setBlockMaterial(5, 4, 5, VanillaMaterials.WATER);
				break;
			case TWO_FLOORS:
				// Cobblestone ring for the upper floor
				for (int i = 1; i <= 9; i++) {
					setBlockMaterial(1, 3, i, VanillaMaterials.COBBLESTONE);
					setBlockMaterial(9, 3, i, VanillaMaterials.COBBLESTONE);
					setBlockMaterial(i, 3, 1, VanillaMaterials.COBBLESTONE);
					setBlockMaterial(i, 3, 9, VanillaMaterials.COBBLESTONE);
				}
				// Second and third cobblestone rings on the bottom and upper floors
				setBlockMaterial(5, 1, 4, VanillaMaterials.COBBLESTONE);
				setBlockMaterial(5, 1, 6, VanillaMaterials.COBBLESTONE);
				setBlockMaterial(5, 3, 4, VanillaMaterials.COBBLESTONE);
				setBlockMaterial(5, 3, 6, VanillaMaterials.COBBLESTONE);
				setBlockMaterial(4, 1, 5, VanillaMaterials.COBBLESTONE);
				setBlockMaterial(6, 1, 5, VanillaMaterials.COBBLESTONE);
				setBlockMaterial(4, 3, 5, VanillaMaterials.COBBLESTONE);
				setBlockMaterial(6, 3, 5, VanillaMaterials.COBBLESTONE);
				// Cobblestone pillars to the upper floor in the middle
				for (int i = 1; i <= 3; i++) {
					setBlockMaterial(4, i, 4, VanillaMaterials.COBBLESTONE);
					setBlockMaterial(6, i, 4, VanillaMaterials.COBBLESTONE);
					setBlockMaterial(4, i, 6, VanillaMaterials.COBBLESTONE);
					setBlockMaterial(6, i, 6, VanillaMaterials.COBBLESTONE);
				}
				// A torch in the middle
				attachMaterial(5, 3, 5, VanillaMaterials.TORCH);
				// A wood ring to complete the upper floor
				for (int i = 2; i <= 8; ++i) {
					setBlockMaterial(2, 3, i, VanillaMaterials.PLANK);
					setBlockMaterial(3, 3, i, VanillaMaterials.PLANK);
					if (i <= 3 || i >= 7) {
						setBlockMaterial(4, 3, i, VanillaMaterials.PLANK);
						setBlockMaterial(5, 3, i, VanillaMaterials.PLANK);
						setBlockMaterial(6, 3, i, VanillaMaterials.PLANK);
					}
					setBlockMaterial(7, 3, i, VanillaMaterials.PLANK);
					setBlockMaterial(8, 3, i, VanillaMaterials.PLANK);
				}
				// A ladder to the upper floor
				setBlockMaterial(9, 1, 3, VanillaMaterials.LADDER, (short) 4);
				setBlockMaterial(9, 2, 3, VanillaMaterials.LADDER, (short) 4);
				setBlockMaterial(9, 3, 3, VanillaMaterials.LADDER, (short) 4);
				// Place the loot chest
				lootChest.setRandom(getRandom());
				placeObject(3, 4, 8, lootChest);
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
	public List<StructureComponent> getNextComponents() {
		final List<StructureComponent> components = new ArrayList<StructureComponent>();
		final Random random = getRandom();
		final StructureComponent nextFront = pickComponent(random, true);
		nextFront.setPosition(position.add(rotate(3, 0, 11)));
		nextFront.setRotation(rotation);
		nextFront.randomize();
		components.add(nextFront);
		final StructureComponent nextRight = pickComponent(random, false);
		nextRight.setPosition(position.add(rotate(-1, 0, 3)));
		nextRight.setRotation(rotation.rotate(-90, 0, 1, 0));
		nextRight.randomize();
		components.add(nextRight);
		final StructureComponent nextLeft = pickComponent(random, false);
		nextLeft.setPosition(position.add(rotate(11, 0, 7)));
		nextLeft.setRotation(rotation.rotate(90, 0, 1, 0));
		nextLeft.randomize();
		components.add(nextLeft);
		return components;
	}

	private StructureComponent pickComponent(Random random, boolean allowLarge) {
		final float draw = random.nextFloat();
		if (draw > 0.8) {
			return new StrongholdChestCorridor(parent);
		} else if (allowLarge && draw > 0.6) {
			return new StrongholdPrison(parent);
		} else if (draw > 0.4) {
			return new StrongholdCorridor(parent);
		} else if (draw > 0.2) {
			return new StrongholdSpiralStaircase(parent);
		} else {
			return new StrongholdStaircase(parent);
		}
	}

	@Override
	public BoundingBox getBoundingBox() {
		final Vector3 rotatedMin = transform(0, 0, 0);
		final Vector3 rotatedMax = transform(10, 6, 10);
		return new BoundingBox(MathHelper.min(rotatedMin, rotatedMax), MathHelper.max(rotatedMin, rotatedMax));
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
