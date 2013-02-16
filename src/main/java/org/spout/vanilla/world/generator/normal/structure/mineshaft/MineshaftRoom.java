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
import org.spout.vanilla.world.generator.structure.SimpleBlockMaterialPicker;

import org.spout.api.math.Vector3;

import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.world.generator.structure.ComponentCuboidPart;
import org.spout.vanilla.world.generator.structure.Structure;
import org.spout.vanilla.world.generator.structure.StructureComponent;

public class MineshaftRoom extends StructureComponent {
	private byte lenght;
	private byte height;
	private byte depth;

	public MineshaftRoom(Structure parent) {
		super(parent);
		randomize();
	}

	@Override
	public boolean canPlace() {
		final ComponentCuboidPart box = new ComponentCuboidPart(this);
		box.setMinMax(-1, -1, -1, lenght + 1, height + 1, depth + 1);
		return !box.intersectsLiquids();
	}

	@Override
	public void place() {
		// building objects
		final ComponentCuboidPart box = new ComponentCuboidPart(this);
		final SimpleBlockMaterialPicker picker = new SimpleBlockMaterialPicker();
		box.setPicker(picker);
		// basic room
		picker.setOuterInnerMaterials(VanillaMaterials.DIRT, VanillaMaterials.AIR);
		box.setMinMax(0, 0, 0, lenght, height, depth);
		box.fill(true);
		picker.setOuterMaterial(VanillaMaterials.AIR);
		box.setMinMax(0, 1, 0, lenght, 3, depth);
		box.fill(false);
		// some 'decoration'
		box.setMinMax(0, 4, 0, lenght, height, depth);
		box.sphericalFill(false);
	}

	@Override
	public final void randomize() {
		final Random random = getRandom();
		lenght = (byte) (7 + random.nextInt(6));
		height = (byte) (4 + random.nextInt(6));
		depth = (byte) (7 + random.nextInt(6));
	}

	@Override
	public List<StructureComponent> getNextComponents() {
		final List<StructureComponent> components = new ArrayList<StructureComponent>(3);
		final Random random = getRandom();
		if (random.nextBoolean()) {
			final StructureComponent front = pickComponent(random);
			front.setPosition(position.add(rotate(lenght / 2, 1, depth)));
			front.setRotation(rotation);
			front.randomize();
			components.add(front);
		}
		if (random.nextBoolean()) {
			final StructureComponent right = pickComponent(random);
			right.setPosition(position.add(rotate(0, 1, depth / 2)));
			right.setRotation(rotation.rotate(-90, 0, 1, 0));
			right.randomize();
			components.add(right);
		}
		if (random.nextBoolean()) {
			final StructureComponent left = pickComponent(random);
			left.setPosition(position.add(rotate(lenght, 1, depth / 2)));
			left.setRotation(rotation.rotate(90, 0, 1, 0));
			left.randomize();
			components.add(left);
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

	public byte getDepth() {
		return depth;
	}

	public void setDepth(byte depth) {
		this.depth = depth;
	}

	public byte getHeight() {
		return height;
	}

	public void setHeight(byte height) {
		this.height = height;
	}

	public byte getLenght() {
		return lenght;
	}

	public void setLenght(byte lenght) {
		this.lenght = lenght;
	}

	@Override
	public BoundingBox getBoundingBox() {
		final Vector3 rotatedMin = transform(0, 0, 0);
		final Vector3 rotatedMax = transform(lenght, height, depth);
		return new BoundingBox(Vector3.min(rotatedMin, rotatedMax), Vector3.max(rotatedMin, rotatedMax));
	}
}
