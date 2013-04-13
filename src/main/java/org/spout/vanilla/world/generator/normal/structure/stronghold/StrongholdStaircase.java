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

import org.spout.api.math.Vector3;

import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.world.generator.normal.structure.stronghold.StrongholdDoor.EmptyDoorway;
import org.spout.vanilla.world.generator.structure.PieceCuboidBuilder;
import org.spout.vanilla.world.generator.structure.Structure;
import org.spout.vanilla.world.generator.structure.StructurePiece;

public class StrongholdStaircase extends StructurePiece {
	public StrongholdStaircase(Structure parent) {
		super(parent);
	}

	@Override
	public boolean canPlace() {
		final PieceCuboidBuilder box = new PieceCuboidBuilder(this);
		box.setMinMax(-1, -7, -1, 5, 5, 8);
		return !box.intersectsLiquids();
	}

	@Override
	public void place() {
		// General shape
		final PieceCuboidBuilder box = new PieceCuboidBuilder(this);
		box.setPicker(new StrongholdBlockMaterialPicker(getRandom()));
		box.setMinMax(0, -6, 0, 4, 4, 7).toggleIgnoreAir().fill();
		box.toggleIgnoreAir();
		// Place the doors
		StrongholdDoor.getRandomDoor(this, getRandom()).place(1, 1, 0);
		new EmptyDoorway(this).place(1, -5, 7);
		// Place the steps
		for (int i = 0; i < 6; i++) {
			setBlockMaterial(1, -i, 1 + i, VanillaMaterials.STAIRS_STONE_BRICK, (short) 3);
			setBlockMaterial(2, -i, 1 + i, VanillaMaterials.STAIRS_STONE_BRICK, (short) 3);
			setBlockMaterial(3, -i, 1 + i, VanillaMaterials.STAIRS_STONE_BRICK, (short) 3);
			if (i < 5) {
				setBlockMaterial(1, -1 - i, 1 + i, VanillaMaterials.STONE_BRICK);
				setBlockMaterial(2, -1 - i, 1 + i, VanillaMaterials.STONE_BRICK);
				setBlockMaterial(3, -1 - i, 1 + i, VanillaMaterials.STONE_BRICK);
			}
		}
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
		} else if (draw > 0.90) {
			component = new StrongholdChestCorridor(parent);
		} else if (draw > 0.75) {
			component = new StrongholdLargeIntersection(parent);
		} else if (draw > 0.60) {
			component = new StrongholdRoom(parent);
		} else if (draw > 0.45) {
			component = new StrongholdPrison(parent);
		} else if (draw > 0.30) {
			component = new StrongholdIntersection(parent);
		} else if (draw > 0.15) {
			component = new StrongholdCorridor(parent);
		} else {
			component = new StrongholdTurn(parent);
		}
		component.setPosition(position.add(rotate(0, -6, 8)));
		component.setRotation(rotation);
		component.randomize();
		return Lists.newArrayList(component);
	}

	@Override
	public BoundingBox getBoundingBox() {
		final Vector3 rotatedMin = transform(0, -6, 0);
		final Vector3 rotatedMax = transform(4, 4, 7);
		return new BoundingBox(Vector3.min(rotatedMin, rotatedMax), Vector3.max(rotatedMin, rotatedMax));
	}
}
