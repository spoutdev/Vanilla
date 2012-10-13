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

import org.spout.api.geo.cuboid.Block;
import org.spout.api.math.MathHelper;
import org.spout.api.math.Quaternion;
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
		if (height > 2) {
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
		height = (byte) (getRandom().nextInt(4) == 0 ? 6 : 2);
	}

	@Override
	public List<StructureComponent> getNextComponents() {
		final Random random = getRandom();
		final List<StructureComponent> components = new ArrayList<StructureComponent>(4);
		for (byte direction = 0; direction < height + 2; direction++) {
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
			if (direction > 3) {
				component.offsetPosition(0, 4, 0);
			}
			component.randomize();
			final int yaw = 90 * direction;
			component.setRotation(new Quaternion(yaw, 0, 1, 0));
			switch (yaw) {
				case 450:
				case 90:
					component.offsetPosition(4, 0, 3);
					break;
				case 540:
				case 180:
					component.offsetPosition(2, 0, 0);
					break;
				case 630:
				case 270:
					component.offsetPosition(-1, 0, 1);
					break;
				default:
					component.offsetPosition(0, 0, 5);
			}
			components.add(component);
		}
		return components;
	}

	public byte getHeight() {
		return height;
	}

	public void setDoubleHeight(boolean doubleHeight) {
		height = (byte) (doubleHeight ? 6 : 2);
	}

	@Override
	public BoundingBox getBoundingBox() {
		final Vector3 rotatedMin = transform(-1, 0, 0);
		final Vector3 rotatedMax = transform(3, height, 4);
		return new BoundingBox(MathHelper.min(rotatedMin, rotatedMax), MathHelper.max(rotatedMin, rotatedMax));
	}
}
