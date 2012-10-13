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
package org.spout.vanilla.world.generator.structure.mineshaft;

import java.util.ArrayList;
import java.util.List;

import org.spout.api.math.MathHelper;
import org.spout.api.math.Vector3;

import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.world.generator.structure.ComponentCuboidPart;
import org.spout.vanilla.world.generator.structure.SimpleBlockMaterialPicker;
import org.spout.vanilla.world.generator.structure.Structure;
import org.spout.vanilla.world.generator.structure.StructureComponent;

public class MineshaftStaircase extends StructureComponent {
	public MineshaftStaircase(Structure parent) {
		super(parent);
	}

	@Override
	public boolean canPlace() {
		final ComponentCuboidPart box = new ComponentCuboidPart(this);
		box.setMinMax(-1, -6, -1, 3, 3, 9);
		return !box.intersectsLiquids();
	}

	@Override
	public void place() {
		// building objects
		final ComponentCuboidPart box = new ComponentCuboidPart(this);
		final SimpleBlockMaterialPicker picker = new SimpleBlockMaterialPicker();
		box.setPicker(picker);
		// case
		picker.setOuterInnerMaterials(VanillaMaterials.AIR, VanillaMaterials.AIR);
		box.setMinMax(0, 5, 0, 2, 7, 1);
		box.fill(false);
		box.setMinMax(0, 0, 7, 2, 2, 8);
		box.fill(false);
		// steps
		for (byte steps = 0; steps < 5; steps++) {
			box.setMinMax(0, 5 - steps - (steps >= 4 ? 0 : 1), 2 + steps, 2, 7 - steps, 2 + steps);
			box.fill(false);
		}
	}

	@Override
	public final void randomize() {
	}

	@Override
	public List<StructureComponent> getNextComponents() {
		final List<StructureComponent> components = new ArrayList<StructureComponent>(1);
		final StructureComponent component;
		final float draw = getRandom().nextFloat();
		if (draw > 0.8) {
			component = new MineshaftRoom(parent);
			component.setPosition(position.add(0, -1, 0));
		} else if (draw > 0.4) {
			component = new MineshaftIntersection(parent);
			component.setPosition(position);
		} else {
			component = new MineshaftCorridor(parent);
			component.setPosition(position);
			component.setRotation(rotation);
		}
		component.randomize();
		switch ((int) rotation.getYaw()) {
			case 90:
				component.offsetPosition(8, 0, 0);
				if (component instanceof MineshaftRoom) {
					final MineshaftRoom room = ((MineshaftRoom) component);
					component.offsetPosition(0, 0, -room.getDepth() / 2 - 1);
				} else if (component instanceof MineshaftIntersection) {
					component.offsetPosition(1, 0, -3);
				}
				break;
			case 180:
				component.offsetPosition(0, 0, -8);
				if (component instanceof MineshaftRoom) {
					final MineshaftRoom room = ((MineshaftRoom) component);
					component.offsetPosition(-room.getLenght() / 2 - 1, 0, -room.getDepth());
				} else if (component instanceof MineshaftIntersection) {
					component.offsetPosition(-2, 0, -4);
				}
				break;
			case -90:
				component.offsetPosition(-8, 0, 0);
				if (component instanceof MineshaftRoom) {
					final MineshaftRoom room = ((MineshaftRoom) component);
					component.offsetPosition(-room.getLenght(), 0, -room.getDepth() / 2 + 1);
				} else if (component instanceof MineshaftIntersection) {
					component.offsetPosition(-3, 0, -1);
				}
				break;
			default:
				component.offsetPosition(0, 0, 8);
				if (component instanceof MineshaftRoom) {
					final MineshaftRoom room = ((MineshaftRoom) component);
					component.offsetPosition(-room.getLenght() / 2 + 1, 0, 0);
				}
		}
		components.add(component);
		return components;
	}

	@Override
	public BoundingBox getBoundingBox() {
		final Vector3 rotatedMin = transform(0, 0, 0);
		final Vector3 rotatedMax = transform(2, 7, 8);
		return new BoundingBox(MathHelper.min(rotatedMin, rotatedMax), MathHelper.max(rotatedMin, rotatedMax));
	}
}
