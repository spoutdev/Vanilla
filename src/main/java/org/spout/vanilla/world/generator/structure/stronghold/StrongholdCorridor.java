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

import org.spout.api.math.MathHelper;
import org.spout.api.math.Vector3;

import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.world.generator.structure.ComponentCuboidPart;
import org.spout.vanilla.world.generator.structure.Structure;
import org.spout.vanilla.world.generator.structure.StructureComponent;

public class StrongholdCorridor extends StructureComponent {
	private static final int LENGTH = 4;

	public StrongholdCorridor(Structure parent) {
		super(parent);
	}

	@Override
	public boolean canPlace() {
		final ComponentCuboidPart box = new ComponentCuboidPart(this);
		box.setMinMax(-1, -1, -1, 6, 6, LENGTH + 1);
		return !box.intersectsLiquids();
	}

	@Override
	public void place() {
		// it's a simple tube
		for (int i = 0; i < LENGTH; i++) {
			setBlockMaterial(0, 0, i, VanillaMaterials.STONE_BRICK);
			setBlockMaterial(1, 0, i, VanillaMaterials.STONE_BRICK);
			setBlockMaterial(2, 0, i, VanillaMaterials.STONE_BRICK);
			setBlockMaterial(3, 0, i, VanillaMaterials.STONE_BRICK);
			setBlockMaterial(4, 0, i, VanillaMaterials.STONE_BRICK);
			for (int ii = 1; ii <= 3; ii++) {
				setBlockMaterial(0, ii, i, VanillaMaterials.STONE_BRICK);
				setBlockMaterial(1, ii, i, VanillaMaterials.AIR);
				setBlockMaterial(2, ii, i, VanillaMaterials.AIR);
				setBlockMaterial(3, ii, i, VanillaMaterials.AIR);
				setBlockMaterial(4, ii, i, VanillaMaterials.STONE_BRICK);
			}
			setBlockMaterial(0, 4, i, VanillaMaterials.STONE_BRICK);
			setBlockMaterial(1, 4, i, VanillaMaterials.STONE_BRICK);
			setBlockMaterial(2, 4, i, VanillaMaterials.STONE_BRICK);
			setBlockMaterial(3, 4, i, VanillaMaterials.STONE_BRICK);
			setBlockMaterial(4, 4, i, VanillaMaterials.STONE_BRICK);
		}
	}

	@Override
	public void randomize() {
	}

	@Override
	public List<StructureComponent> getNextComponents() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public BoundingBox getBoundingBox() {
		final Vector3 rotatedMin = transform(0, 0, 0);
		final Vector3 rotatedMax = transform(5, 5, LENGTH);
		return new BoundingBox(MathHelper.min(rotatedMin, rotatedMax), MathHelper.max(rotatedMin, rotatedMax));
	}
}
