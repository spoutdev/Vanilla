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
import java.util.Random;

import org.spout.api.math.MathHelper;
import org.spout.api.math.Quaternion;
import org.spout.api.math.Vector3;

import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.world.generator.structure.ComponentCuboidPart;
import org.spout.vanilla.world.generator.structure.SimpleBlockMaterialPicker;
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
		final Random random = getRandom();
		final List<StructureComponent> components = new ArrayList<StructureComponent>(4);
		for (byte direction = 0; direction < 4; direction++) {
			if (random.nextBoolean()) {
				continue;
			}
			final StructureComponent component;
			if (random.nextInt(4) == 0) {
				component = new MineshaftStaircase(parent);
				component.setPosition(position.add(0, -5, 0));
			} else {
				component = new MineshaftCorridor(parent);
				component.setPosition(position);
			}
			component.randomize();
			final int yaw = 90 * direction;
			component.setRotation(new Quaternion(yaw, 0, 1, 0));
			switch (yaw) {
				case 90:
					component.offsetPosition(lenght, 1, depth / 2);
					break;
				case 180:
					component.offsetPosition(lenght / 2, 1, 0);
					break;
				case 270:
					component.offsetPosition(0, 1, depth / 2);
					break;
				default:
					component.offsetPosition(lenght / 2, 1, depth);
			}
			components.add(component);
		}
		return components;
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
		return new BoundingBox(MathHelper.min(rotatedMin, rotatedMax), MathHelper.max(rotatedMin, rotatedMax));
	}
}
