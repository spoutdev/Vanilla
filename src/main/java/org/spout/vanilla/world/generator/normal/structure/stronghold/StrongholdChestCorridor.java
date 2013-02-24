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

import java.util.Arrays;
import java.util.List;

import org.spout.api.math.Vector3;

import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.misc.Slab;
import org.spout.vanilla.world.generator.normal.object.LootChestObject;
import org.spout.vanilla.world.generator.structure.PieceCuboidBuilder;
import org.spout.vanilla.world.generator.structure.SimpleBlockMaterialPicker;
import org.spout.vanilla.world.generator.structure.Structure;
import org.spout.vanilla.world.generator.structure.StructurePiece;

public class StrongholdChestCorridor extends StructurePiece {
	private final LootChestObject lootChest = new LootChestObject();

	public StrongholdChestCorridor(Structure parent) {
		super(parent);
		lootChest.addMaterial(VanillaMaterials.IRON_BARS, 0.1, 1, 3);// TODO: get the proper contents
	}

	@Override
	public boolean canPlace() {
		final PieceCuboidBuilder box = new PieceCuboidBuilder(this);
		box.setMinMax(-1, -1, -1, 5, 5, 7);
		return !box.intersectsLiquids();
	}

	@Override
	public void place() {
		// Building objects
		final PieceCuboidBuilder box = new PieceCuboidBuilder(this);
		// General shape
		box.setPicker(new StrongholdBlockMaterialPicker(getRandom()));
		box.setMinMax(0, 0, 0, 4, 4, 6);
		box.toggleIgnoreAir();
		box.fill();
		box.toggleIgnoreAir();
		// Place the doors
		StrongholdDoor.getRandomDoor(this, getRandom()).place(1, 1, 0);
		new StrongholdDoor.EmptyDoorway(this).place(1, 1, 6);
		// Place the floor
		box.setPicker(new SimpleBlockMaterialPicker(VanillaMaterials.STONE_BRICK, VanillaMaterials.STONE_BRICK));
		box.setMinMax(3, 1, 2, 3, 1, 4);
		box.fill();
		// Build the loot chest pedestal
		setBlockMaterial(3, 1, 1, Slab.STONE);
		setBlockMaterial(3, 1, 5, Slab.STONE);
		setBlockMaterial(3, 2, 2, Slab.STONE);
		setBlockMaterial(3, 2, 4, Slab.STONE);
		for (int i = 2; i <= 4; i++) {
			setBlockMaterial(2, 1, i, Slab.STONE);
		}
		// Place the loot chest
		lootChest.setRandom(getRandom());
		placeObject(3, 2, 3, lootChest);
	}

	@Override
	public void randomize() {
	}

	@Override
	public List<StructurePiece> getNextComponents() {
		final StructurePiece component;
		final float draw = getRandom().nextFloat();
		if (draw > 0.95) {
			component = new StrongholdLibrary(parent);
			component.setPosition(position.add(rotate(-3, 0, 7)));
		} else if (draw > 0.90) {
			component = new StrongholdLargeIntersection(parent);
			component.setPosition(position.add(rotate(-3, -2, 7)));
		} else if (draw > 0.75) {
			component = new StrongholdSpiralStaircase(parent);
			component.setPosition(position.add(rotate(0, 0, 7)));
		} else if (draw > 0.60) {
			component = new StrongholdRoom(parent);
			component.setPosition(position.add(rotate(-3, 0, 7)));
		} else if (draw > 0.45) {
			component = new StrongholdPrison(parent);
			component.setPosition(position.add(rotate(0, 0, 7)));
		} else if (draw > 0.30) {
			component = new StrongholdIntersection(parent);
			component.setPosition(position.add(rotate(0, 0, 7)));
		} else if (draw > 0.15) {
			component = new StrongholdStaircase(parent);
			component.setPosition(position.add(rotate(0, 0, 7)));
		} else {
			component = new StrongholdTurn(parent);
			component.setPosition(position.add(rotate(0, 0, 7)));
		}
		component.setRotation(rotation);
		component.randomize();
		return Arrays.asList(component);
	}

	@Override
	public BoundingBox getBoundingBox() {
		final Vector3 rotatedMin = transform(0, 0, 0);
		final Vector3 rotatedMax = transform(4, 4, 6);
		return new BoundingBox(Vector3.min(rotatedMin, rotatedMax), Vector3.max(rotatedMin, rotatedMax));
	}
}
