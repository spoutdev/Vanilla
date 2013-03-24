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
package org.spout.vanilla.world.generator.normal.structure.mineshaft;

import java.util.ArrayList;
import java.util.List;

import org.spout.api.math.Vector3;

import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.world.generator.structure.PieceCuboidBuilder;
import org.spout.vanilla.world.generator.structure.SimpleBlockMaterialPicker;
import org.spout.vanilla.world.generator.structure.Structure;
import org.spout.vanilla.world.generator.structure.StructurePiece;

public class MineshaftStaircase extends StructurePiece {
	public MineshaftStaircase(Structure parent) {
		super(parent);
	}

	@Override
	public boolean canPlace() {
		final PieceCuboidBuilder box = new PieceCuboidBuilder(this);
		box.setMinMax(-1, -6, -1, 3, 3, 9);
		return !box.intersectsLiquids();
	}

	@Override
	public void place() {
		// building objects
		final PieceCuboidBuilder box = new PieceCuboidBuilder(this);
		final SimpleBlockMaterialPicker picker = new SimpleBlockMaterialPicker();
		box.setPicker(picker);
		// case
		picker.setOuterInnerMaterials(VanillaMaterials.AIR, VanillaMaterials.AIR);
		box.setMinMax(0, 0, 0, 2, 2, 1).fill();
		box.setMinMax(0, -5, 7, 2, -3, 8).fill();
		// steps
		for (byte steps = 0; steps < 5; steps++) {
			box.setMinMax(0, -steps - (steps >= 4 ? 0 : 1), 2 + steps, 2, 2 - steps, 2 + steps).fill();
		}
	}

	@Override
	public final void randomize() {
	}

	@Override
	public List<StructurePiece> getNextComponents() {
		final List<StructurePiece> components = new ArrayList<StructurePiece>(1);
		final StructurePiece component;
		final float draw = getRandom().nextFloat();
		if (draw > 0.8) {
			final MineshaftRoom room = new MineshaftRoom(parent);
			room.randomize();
			room.setPosition(position.add(rotate(-room.getLenght() / 2 + 1, -6, 8)));
			component = room;
		} else if (draw > 0.4) {
			component = new MineshaftIntersection(parent);
			component.setPosition(position.add(rotate(0, -5, 8)));
			component.randomize();
		} else {
			component = new MineshaftCorridor(parent);
			component.setPosition(position.add(rotate(0, -5, 8)));
			component.randomize();
		}
		component.setRotation(rotation);
		components.add(component);
		return components;
	}

	@Override
	public BoundingBox getBoundingBox() {
		final Vector3 rotatedMin = transform(0, -5, 0);
		final Vector3 rotatedMax = transform(2, 2, 8);
		return new BoundingBox(Vector3.min(rotatedMin, rotatedMax), Vector3.max(rotatedMin, rotatedMax));
	}
}
