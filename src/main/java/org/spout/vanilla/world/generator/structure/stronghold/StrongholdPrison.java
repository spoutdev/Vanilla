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
import org.spout.vanilla.world.generator.structure.ComponentCuboidPart;
import org.spout.vanilla.world.generator.structure.SimpleBlockMaterialPicker;
import org.spout.vanilla.world.generator.structure.Structure;
import org.spout.vanilla.world.generator.structure.StructureComponent;

public class StrongholdPrison extends StructureComponent {
	public StrongholdPrison(Structure parent) {
		super(parent);
	}

	@Override
	public boolean canPlace() {
		final ComponentCuboidPart box = new ComponentCuboidPart(this);
		box.setMinMax(-1, -1, -1, 9, 5, 11);
		return !box.intersectsLiquids();
	}

	@Override
	public void place() {
		// Building objects
		final ComponentCuboidPart box = new ComponentCuboidPart(this);
		final SimpleBlockMaterialPicker picker = new SimpleBlockMaterialPicker();
		final StrongholdBlockMaterialPicker stone = new StrongholdBlockMaterialPicker(getRandom());
		// General shape
		box.setPicker(stone);
		box.setMinMax(0, 0, 0, 8, 4, 10);
		box.fill(true);
		// Place the door
		StrongholdDoor.getRandomDoor(this, getRandom()).place(1, 1, 0);
		//
		box.setPicker(picker);
		box.setMinMax(1, 1, 10, 3, 3, 10);
		box.fill(false);
		//
		box.setPicker(stone);
		box.setMinMax(4, 1, 1, 4, 3, 1);
		box.fill(false);
		box.offsetMinMax(0, 0, 2, 0, 0, 2);
		box.fill(false);
		box.offsetMinMax(0, 0, 4, 0, 0, 4);
		box.fill(false);
		box.offsetMinMax(0, 0, 2, 0, 0, 2);
		box.fill(false);
		// Build the cells
		box.setPicker(picker);
		picker.setOuterInnerMaterials(VanillaMaterials.IRON_BARS, VanillaMaterials.IRON_BARS);
		box.setMinMax(4, 1, 4, 4, 3, 6);
		box.fill(false);
		box.setMinMax(5, 1, 5, 7, 3, 5);
		box.fill(false);
		setBlockMaterial(4, 3, 2, VanillaMaterials.IRON_BARS);
		setBlockMaterial(4, 3, 8, VanillaMaterials.IRON_BARS);
		// Add the cell doors
		setBlockMaterial(4, 1, 2, VanillaMaterials.IRON_DOOR_BLOCK, (short) 3);
		setBlockMaterial(4, 2, 2, VanillaMaterials.IRON_DOOR_BLOCK, (short) 11);
		setBlockMaterial(4, 1, 8, VanillaMaterials.IRON_DOOR_BLOCK, (short) 3);
		setBlockMaterial(4, 2, 8, VanillaMaterials.IRON_DOOR_BLOCK, (short) 11);
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
		final Vector3 rotatedMax = transform(9, 5, 11);
		return new BoundingBox(MathHelper.min(rotatedMin, rotatedMax), MathHelper.max(rotatedMin, rotatedMax));
	}
}
