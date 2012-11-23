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

import java.util.List;

import org.spout.api.math.MathHelper;
import org.spout.api.math.Vector3;

import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.misc.Slab;
import org.spout.vanilla.world.generator.normal.object.LootChestObject;
import org.spout.vanilla.world.generator.structure.ComponentCuboidPart;
import org.spout.vanilla.world.generator.structure.SimpleBlockMaterialPicker;
import org.spout.vanilla.world.generator.structure.Structure;
import org.spout.vanilla.world.generator.structure.StructureComponent;
import org.spout.vanilla.world.generator.structure.stronghold.StrongholdDoor.EmptyDoorway;

public class StrongholdChestCorridor extends StructureComponent {
	private final LootChestObject lootChest = new LootChestObject();

	public StrongholdChestCorridor(Structure parent) {
		super(parent);
		lootChest.addMaterial(VanillaMaterials.IRON_BARS, 0.1, 1, 3);// TODO: get the proper contents
	}

	@Override
	public boolean canPlace() {
		final ComponentCuboidPart box = new ComponentCuboidPart(this);
		box.setMinMax(-2, -2, -1, 6, 6, 8);
		return !box.intersectsLiquids();
	}

	@Override
	public void place() {
		final ComponentCuboidPart box = new ComponentCuboidPart(this);
		// General shape
		box.setPicker(new StrongholdBlockMaterialPicker(getRandom()));
		box.setMinMax(0, 0, 0, 4, 4, 6);
		box.fill(true);
		// Place the doors
		StrongholdDoor.getRandomDoor(this, getRandom()).place(1, 1, 0);
		new EmptyDoorway(this).place(1, 1, 6);
		// Place the floor
		box.setPicker(new SimpleBlockMaterialPicker(VanillaMaterials.STONE_BRICK, VanillaMaterials.STONE_BRICK));
		box.setMinMax(3, 1, 2, 3, 1, 4);
		box.fill(false);
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
	public List<StructureComponent> getNextComponents() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public BoundingBox getBoundingBox() {
		final Vector3 rotatedMin = transform(-1, -1, 0);
		final Vector3 rotatedMax = transform(5, 5, 7);
		return new BoundingBox(MathHelper.min(rotatedMin, rotatedMax), MathHelper.max(rotatedMin, rotatedMax));
	}
}
