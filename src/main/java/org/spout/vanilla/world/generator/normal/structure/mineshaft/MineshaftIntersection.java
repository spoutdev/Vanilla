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
import java.util.Random;

import org.spout.api.geo.cuboid.Block;
import org.spout.api.math.Vector3;

import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.world.generator.structure.ComponentCuboidPart;
import org.spout.vanilla.world.generator.structure.SimpleBlockMaterialPicker;
import org.spout.vanilla.world.generator.structure.Structure;
import org.spout.vanilla.world.generator.structure.StructureComponent;

public class MineshaftIntersection extends StructureComponent {
	private byte height;

	public MineshaftIntersection(Structure parent) {
		super(parent);
		randomize();
	}

	@Override
	public boolean canPlace() {
		final ComponentCuboidPart box = new ComponentCuboidPart(this);
		box.setMinMax(-2, -1, -1, 4, height + 1, 5);
		return !box.intersectsLiquids();
	}

	@Override
	public void place() {
		// building objects
		final ComponentCuboidPart box = new ComponentCuboidPart(this);
		final SimpleBlockMaterialPicker picker = new SimpleBlockMaterialPicker();
		box.setPicker(picker);
		// hollow out some space
		picker.setOuterInnerMaterials(VanillaMaterials.AIR, VanillaMaterials.AIR);
		if (hasTwoFloors()) {
			// for the extra floor
			box.setMinMax(0, 0, 0, 2, 2, 4);
			box.fill(false);
			box.offsetMinMax(-1, 0, 1, 1, 0, -1);
			box.fill(false);
			box.setMinMax(0, height - 2, 0, 2, height, 4);
			box.fill(false);
			box.offsetMinMax(-1, 0, 1, 1, 0, -1);
			box.fill(false);
			box.setMinMax(0, 3, 1, 2, 3, 3);
			box.fill(false);
		} else {
			box.setMinMax(0, 0, 0, 2, height, 4);
			box.fill(false);
			box.offsetMinMax(-1, 0, 1, 1, 0, -1);
			box.fill(false);
		}
		// wooden pillars
		picker.setOuterMaterial(VanillaMaterials.PLANK);
		box.setMinMax(0, 0, 1, 0, height, 1);
		box.fill(false);
		box.setMinMax(0, 0, 3, 0, height, 3);
		box.fill(false);
		box.setMinMax(2, 0, 1, 2, height, 1);
		box.fill(false);
		box.setMinMax(2, 0, 3, 2, height, 3);
		box.fill(false);
		// bridge gaps
		for (int xx = -1; xx <= 3; xx++) {
			for (int zz = 0; zz <= 4; zz++) {
				final Block block = getBlock(xx, -1, zz);
				if (block.isMaterial(VanillaMaterials.AIR)) {
					block.setMaterial(VanillaMaterials.PLANK);
				}
			}
		}
	}

	@Override
	public final void randomize() {
		setTwoFloors(getRandom().nextInt(4) == 0);
	}

	@Override
	public List<StructureComponent> getNextComponents() {
		final List<StructureComponent> components = new ArrayList<StructureComponent>(7);
		final boolean twoFloors = hasTwoFloors();
		final Random random = getRandom();
		if (random.nextBoolean()) {
			final StructureComponent bottomFront = pickComponent(random);
			bottomFront.setPosition(position.add(rotate(0, 0, 5)));
			bottomFront.setRotation(rotation);
			bottomFront.randomize();
			components.add(bottomFront);
		}
		if (random.nextBoolean()) {
			final StructureComponent bottomRight = pickComponent(random);
			bottomRight.setPosition(position.add(rotate(-1, 0, 1)));
			bottomRight.setRotation(rotation.rotate(-90, 0, 1, 0));
			bottomRight.randomize();
			components.add(bottomRight);
		}
		if (random.nextBoolean()) {
			final StructureComponent bottomLeft = pickComponent(random);
			bottomLeft.setPosition(position.add(rotate(4, 0, 3)));
			bottomLeft.setRotation(rotation.rotate(90, 0, 1, 0));
			bottomLeft.randomize();
			components.add(bottomLeft);
		}
		if (twoFloors && random.nextBoolean()) {
			final StructureComponent topFront = pickComponent(random);
			topFront.setPosition(position.add(rotate(0, 4, 5)));
			topFront.setRotation(rotation);
			topFront.randomize();
			components.add(topFront);
		}
		if (twoFloors && random.nextBoolean()) {
			final StructureComponent topRight = pickComponent(random);
			topRight.setPosition(position.add(rotate(-1, 4, 1)));
			topRight.setRotation(rotation.rotate(-90, 0, 1, 0));
			topRight.randomize();
			components.add(topRight);
		}
		if (twoFloors && random.nextBoolean()) {
			final StructureComponent topLeft = pickComponent(random);
			topLeft.setPosition(position.add(rotate(4, 4, 3)));
			topLeft.setRotation(rotation.rotate(90, 0, 1, 0));
			topLeft.randomize();
			components.add(topLeft);
		}
		if (twoFloors && random.nextBoolean()) {
			final StructureComponent topBack = pickComponent(random);
			topBack.setPosition(position.add(rotate(2, 4, 0)));
			topBack.setRotation(rotation.rotate(180, 0, 1, 0));
			topBack.randomize();
			components.add(topBack);
		}
		return components;
	}

	private StructureComponent pickComponent(Random random) {
		if (random.nextInt(4) == 0) {
			return new MineshaftStaircase(parent);
		} else {
			return new MineshaftCorridor(parent);
		}
	}

	public boolean hasTwoFloors() {
		return height > 2;
	}

	public void setTwoFloors(boolean twoFloors) {
		height = (byte) (twoFloors ? 6 : 2);
	}

	@Override
	public BoundingBox getBoundingBox() {
		final Vector3 rotatedMin = transform(-1, 0, 0);
		final Vector3 rotatedMax = transform(3, height, 4);
		return new BoundingBox(Vector3.min(rotatedMin, rotatedMax), Vector3.max(rotatedMin, rotatedMax));
	}
}
