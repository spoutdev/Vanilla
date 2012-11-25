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

public class StrongholdIntersection extends StructureComponent {
	private boolean nextComponentX = false;
	private boolean nextComponentZ = false;

	public StrongholdIntersection(Structure parent) {
		super(parent);
	}

	@Override
	public boolean canPlace() {
		final ComponentCuboidPart box = new ComponentCuboidPart(this);
		box.setMinMax(-1, -1, -1, 5, 5, 7);
		return !box.intersectsLiquids();
	}

	@Override
	public void place() {
		// General shape
		final ComponentCuboidPart box = new ComponentCuboidPart(this);
		box.setPicker(new StrongholdBlockMaterialPicker(getRandom()));
		box.setMinMax(0, 0, 0, 4, 4, 6);
		box.fill(true);
		// Place the doors
		StrongholdDoor.getRandomDoor(this, getRandom()).place(1, 1, 0);
		new StrongholdDoor.EmptyDoorway(this).place(1, 1, 6);
		// Place random torches
		attachMaterial(0.1f, 1, 2, 1, VanillaMaterials.TORCH);
		attachMaterial(0.1f, 3, 2, 1, VanillaMaterials.TORCH);
		attachMaterial(0.1f, 1, 2, 5, VanillaMaterials.TORCH);
		attachMaterial(0.1f, 3, 2, 5, VanillaMaterials.TORCH);
		// Access for the next components
		box.setPicker(new SimpleBlockMaterialPicker());
		if (nextComponentX) {
			box.setMinMax(0, 1, 2, 0, 3, 4);
			box.fill(false);
		}
		if (nextComponentZ) {
			box.setMinMax(4, 1, 2, 4, 3, 4);
			box.fill(false);
		}
	}

	@Override
	public void randomize() {
		final Random random = getRandom();
		nextComponentX = random.nextBoolean();
		nextComponentZ = random.nextBoolean();
	}

	@Override
	public List<StructureComponent> getNextComponents() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public BoundingBox getBoundingBox() {
		final Vector3 rotatedMin = transform(-1, -1, 0);
		final Vector3 rotatedMax = transform(5, 5, 7);
		return new BoundingBox(MathHelper.min(rotatedMin, rotatedMax), MathHelper.max(rotatedMin, rotatedMax));
	}

	public boolean hasNextComponentX() {
		return nextComponentX;
	}

	public void setNextComponentX(boolean nextComponentX) {
		this.nextComponentX = nextComponentX;
	}

	public boolean hasNextComponentZ() {
		return nextComponentZ;
	}

	public void setNextComponentZ(boolean nextComponentZ) {
		this.nextComponentZ = nextComponentZ;
	}
}
