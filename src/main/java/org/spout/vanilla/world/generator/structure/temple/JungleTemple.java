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
package org.spout.vanilla.world.generator.structure.temple;

import java.util.Collections;
import java.util.List;

import org.spout.api.math.Vector3;

import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.world.generator.structure.ComponentCuboidPart;
import org.spout.vanilla.world.generator.structure.SimpleBlockMaterialPicker;
import org.spout.vanilla.world.generator.structure.Structure;
import org.spout.vanilla.world.generator.structure.StructureComponent;

public class JungleTemple extends StructureComponent {
	public JungleTemple(Structure parent) {
		super(parent);
	}

	@Override
	public void place() {
		// Building objects
		final ComponentCuboidPart box = new ComponentCuboidPart(this);
		final SimpleBlockMaterialPicker picker = new SimpleBlockMaterialPicker();
		final MossyCobbleBlockMaterialPicker cobble = new MossyCobbleBlockMaterialPicker(getRandom());
		// Basic shape: a box with two floors on full box
		box.setPicker(cobble);
		box.setMinMax(0, -4, 0, 11, 0, 14);
		box.fill(false);
		box.setMinMax(2, 1, 2, 9, 2, 2);
		box.fill(false);
		box.offsetMinMax(0, 0, 10, 0, 0, 10);
		box.fill(false);
		box.offsetMinMax(0, 0, -9, -7, 0, -1);
		box.fill(false);
		box.offsetMinMax(7, 0, 0, 7, 0, 0);
		box.fill(false);
		box.setMinMax(1, 3, 1, 10, 6, 1);
		box.fill(false);
		box.offsetMinMax(0, 0, 12, 0, 0, 12);
		box.fill(false);
		box.offsetMinMax(0, 0, -11, -9, 0, -1);
		box.fill(false);
		box.offsetMinMax(9, 0, 0, 9, 0, 0);
		box.fill(false);
		box.offsetMinMax(-8, 0, 0, -1, -3, 0);
		box.fill(false);
		box.offsetMinMax(0, 3, 0, 0, 3, 0);
		box.fill(false);
		box.offsetMinMax(1, 1, 1, 1, -1, 1);
		box.fill(false);
		box.offsetMinMax(1, 1, 1, 1, -1, 1);
		box.fill(false);
		// Holes like windows and doorways
		box.setPicker(picker);
		picker.setOuterInnerMaterials(VanillaMaterials.AIR, VanillaMaterials.AIR);
		box.setMinMax(3, 1, 3, 8, 2, 11);
		box.fill(false);
		box.setMinMax(4, 3, 6, 7, 3, 9);
		box.fill(false);
		box.setMinMax(2, 4, 2, 9, 5, 12);
		box.fill(false);
		box.setMinMax(4, 6, 5, 7, 6, 9);
		box.fill(false);
		box.offsetMinMax(1, 1, 1, -1, 1, -1);
		box.fill(false);
		box.offsetMinMax(0, -6, -4, 0, -5, -6);
		box.fill(false);
		box.offsetMinMax(0, 1, 10, 0, 0, 10);
		box.fill(false);
		box.offsetMinMax(0, 3, -11, 0, 3, -11);
		box.fill(false);
		box.offsetMinMax(0, 0, 12, 0, 0, 12);
		box.fill(false);
		setBlockMaterial(1, 5, 5, VanillaMaterials.AIR);
		setBlockMaterial(10, 5, 5, VanillaMaterials.AIR);
		setBlockMaterial(1, 5, 9, VanillaMaterials.AIR);
		setBlockMaterial(10, 5, 9, VanillaMaterials.AIR);
		// Decoration on the exterior walls
		box.setPicker(cobble);
		for (int i = 0; i < 2; i++) {
			box.setMinMax(2, 4, i * 14, 2, 5, i * 14);
			box.fill(false);
			box.offsetMinMax(2, 0, 0, 2, 0, 0);
			box.fill(false);
			box.offsetMinMax(3, 0, 0, 3, 0, 0);
			box.fill(false);
			box.offsetMinMax(2, 0, 0, 2, 0, 0);
			box.fill(false);
		}
		box.setMinMax(5, 6, 0, 6, 6, 0);
		box.fill(false);
		for (int i = 0; i < 2; i++) {
			for (int ii = 2; ii <= 12; ii += 2) {
				box.setMinMax(i * 11, 4, ii, i * 11, 5, ii);
				box.fill(false);
			}
			box.setMinMax(i * 11, 6, 5, i * 11, 6, 5);
			box.fill(false);
			box.offsetMinMax(0, 0, 4, 0, 0, 4);
			box.fill(false);
		}
	}

	@Override
	public boolean canPlace() {
		return true;
	}

	@Override
	public void randomize() {
	}

	@Override
	public List<StructureComponent> getNextComponents() {
		return Collections.emptyList();
	}

	@Override
	public BoundingBox getBoundingBox() {
		return new BoundingBox(new Vector3(0, 0, 0), new Vector3(12, 10, 15));
	}
}
