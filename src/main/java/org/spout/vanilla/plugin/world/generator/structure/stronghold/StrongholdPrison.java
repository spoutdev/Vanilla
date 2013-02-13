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
package org.spout.vanilla.plugin.world.generator.structure.stronghold;

import java.util.Arrays;
import java.util.List;

import org.spout.api.material.block.BlockFace;
import org.spout.api.math.Vector3;

import org.spout.vanilla.plugin.material.VanillaMaterials;
import org.spout.vanilla.plugin.world.generator.structure.ComponentCuboidPart;
import org.spout.vanilla.plugin.world.generator.structure.SimpleBlockMaterialPicker;
import org.spout.vanilla.plugin.world.generator.structure.Structure;
import org.spout.vanilla.plugin.world.generator.structure.StructureComponent;

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
		placeDoor(4, 1, 2, VanillaMaterials.IRON_DOOR_BLOCK, BlockFace.NORTH);
		placeDoor(4, 1, 8, VanillaMaterials.IRON_DOOR_BLOCK, BlockFace.NORTH);
	}

	@Override
	public void randomize() {
	}

	@Override
	public List<StructureComponent> getNextComponents() {
		final StructureComponent component;
		final float draw = getRandom().nextFloat();
		if (draw > 0.95) {
			component = new StrongholdLibrary(parent);
			component.setPosition(position.add(rotate(-3, 0, 11)));
		} else if (draw > 0.90) {
			component = new StrongholdChestCorridor(parent);
			component.setPosition(position.add(rotate(0, 0, 11)));
		} else if (draw > 0.75) {
			component = new StrongholdSpiralStaircase(parent);
			component.setPosition(position.add(rotate(0, 0, 11)));
		} else if (draw > 0.60) {
			component = new StrongholdRoom(parent);
			component.setPosition(position.add(rotate(-3, 0, 11)));
		} else if (draw > 0.45) {
			component = new StrongholdLargeIntersection(parent);
			component.setPosition(position.add(rotate(-3, -2, 11)));
		} else if (draw > 0.30) {
			component = new StrongholdIntersection(parent);
			component.setPosition(position.add(rotate(0, 0, 11)));
		} else if (draw > 0.15) {
			component = new StrongholdStaircase(parent);
			component.setPosition(position.add(rotate(0, 0, 11)));
		} else {
			component = new StrongholdTurn(parent);
			component.setPosition(position.add(rotate(0, 0, 11)));
		}
		component.setRotation(rotation);
		component.randomize();
		return Arrays.asList(component);
	}

	@Override
	public BoundingBox getBoundingBox() {
		final Vector3 rotatedMin = transform(0, 0, 0);
		final Vector3 rotatedMax = transform(8, 4, 10);
		return new BoundingBox(Vector3.min(rotatedMin, rotatedMax), Vector3.max(rotatedMin, rotatedMax));
	}
}
