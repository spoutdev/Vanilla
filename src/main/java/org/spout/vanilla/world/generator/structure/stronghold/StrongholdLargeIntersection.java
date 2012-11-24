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
import java.util.Random;

import org.spout.api.math.MathHelper;
import org.spout.api.math.Vector3;

import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.world.generator.structure.ComponentCuboidPart;
import org.spout.vanilla.world.generator.structure.SimpleBlockMaterialPicker;
import org.spout.vanilla.world.generator.structure.Structure;
import org.spout.vanilla.world.generator.structure.StructureComponent;

public class StrongholdLargeIntersection extends StructureComponent {
	private boolean nextComponentPosX = false;
	private boolean nextComponentNegX = false;
	private boolean nextComponentPosZ = false;
	private boolean nextComponentNegZ = false;

	public StrongholdLargeIntersection(Structure parent) {
		super(parent);
	}

	@Override
	public boolean canPlace() {
		final ComponentCuboidPart box = new ComponentCuboidPart(this);
		box.setMinMax(-4, -3, -1, 10, 9, 11);
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
		box.setMinMax(0, 0, 0, 9, 8, 10);
		box.fill(true);
		// Place the door
		StrongholdDoor.getRandomDoor(this, getRandom()).place(4, 3, 0);
		// Access to the next components of the intersection
		box.setPicker(picker);
		if (nextComponentPosX) {
			box.setMinMax(0, 3, 1, 0, 5, 3);
			box.fill(false);
		}
		if (nextComponentNegX) {
			box.setMinMax(0, 5, 7, 0, 7, 9);
			box.fill(false);
		}
		if (nextComponentPosZ) {
			box.setMinMax(9, 3, 1, 9, 5, 3);
			box.fill(false);
		}
		if (nextComponentNegZ) {
			box.setMinMax(9, 5, 7, 9, 7, 9);
			box.fill(false);
		}
		// Finish the interior
		box.setMinMax(5, 1, 10, 7, 3, 10);
		box.fill(false);
		box.setPicker(stone);
		box.setMinMax(1, 2, 1, 8, 2, 6);
		box.fill(false);
		box.setMinMax(4, 1, 5, 4, 4, 9);
		box.fill(false);
		box.offsetMinMax(4, 0, 0, 4, 0, 0);
		box.fill(false);
		box.setMinMax(1, 4, 7, 3, 4, 9);
		box.fill(false);
		box.setMinMax(1, 3, 5, 3, 3, 6);
		box.fill(false);
		picker.setOuterInnerMaterials(VanillaMaterials.SLAB, VanillaMaterials.SLAB);
		box.setPicker(picker);
		box.setMinMax(1, 3, 4, 3, 3, 4);
		box.fill(false);
		box.offsetMinMax(0, 1, 2, 0, 1, 2);
		box.fill(false);
		box.setPicker(stone);
		box.setMinMax(5, 1, 7, 7, 1, 8);
		box.fill(false);
		box.setPicker(picker);
		box.offsetMinMax(0, 0, 2, 0, 0, 1);
		box.fill(false);
		box.offsetMinMax(0, 1, -2, 0, 1, -2);
		box.fill(false);
		box.setMinMax(4, 5, 7, 4, 5, 9);
		box.fill(false);
		box.offsetMinMax(4, 0, 0, 4, 0, 0);
		box.fill(false);
		box.offsetMinMax(-3, 0, 0, -1, 0, 0);
		box.fill(false);
		// Place a torch
		setBlockMaterial(6, 5, 6, VanillaMaterials.TORCH);
	}

	@Override
	public void randomize() {
		final Random random = getRandom();
		nextComponentPosX = random.nextBoolean();
		nextComponentNegX = random.nextBoolean();
		nextComponentPosZ = random.nextBoolean();
		nextComponentNegZ = random.nextInt(3) > 0;
	}

	@Override
	public List<StructureComponent> getNextComponents() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public BoundingBox getBoundingBox() {
		final Vector3 rotatedMin = transform(-4, -3, 0);
		final Vector3 rotatedMax = transform(10, 9, 11);
		return new BoundingBox(MathHelper.min(rotatedMin, rotatedMax), MathHelper.max(rotatedMin, rotatedMax));
	}

	public boolean hasNextCompPosX() {
		return nextComponentPosX;
	}

	public void setNextComponentPosX(boolean nextComponentPosX) {
		this.nextComponentPosX = nextComponentPosX;
	}

	public boolean hasNextComponentNegX() {
		return nextComponentNegX;
	}

	public void setNextComponentNegX(boolean nextComponentNegX) {
		this.nextComponentNegX = nextComponentNegX;
	}

	public boolean hasNextComponentPosZ() {
		return nextComponentPosZ;
	}

	public void setNextComponentPosZ(boolean nextComponentPosZ) {
		this.nextComponentPosZ = nextComponentPosZ;
	}

	public boolean hasNextComponentNegZ() {
		return nextComponentNegZ;
	}

	public void setNextComponentNegZ(boolean nextComponentNegZ) {
		this.nextComponentNegZ = nextComponentNegZ;
	}
}
