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
package org.spout.vanilla.world.generator.normal.structure.stronghold;

import java.util.ArrayList;
import java.util.List;

import org.spout.api.math.Vector3;

import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.world.generator.structure.PieceCuboidBuilder;
import org.spout.vanilla.world.generator.structure.Structure;
import org.spout.vanilla.world.generator.structure.StructurePiece;

public class StrongholdCorridor extends StructurePiece {
	private boolean startOfStronghold = false;
	private byte length = 4;

	public StrongholdCorridor(Structure parent) {
		super(parent);
	}

	@Override
	public boolean canPlace() {
		final PieceCuboidBuilder box = new PieceCuboidBuilder(this);
		box.setMinMax(-1, -1, -1, 5, 5, length + 1);
		return !box.intersectsLiquids();
	}

	@Override
	public void place() {
		// It's a simple tube
		for (int i = 0; i < length; i++) {
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
		length = (byte) (getRandom().nextInt(5) + 4);
	}

	@Override
	public List<StructurePiece> getNextComponents() {
		final List<StructurePiece> components = new ArrayList<StructurePiece>();
		if (startOfStronghold) {
			final StructurePiece component = new StrongholdPortalRoom(parent);
			component.setPosition(position.add(rotate(4, 0, -1)));
			component.setRotation(rotation.rotate(180, 0, 1, 0));
			component.randomize();
			components.add(component);
		}
		final StructurePiece component;
		final float draw = getRandom().nextFloat();
		if (draw > 0.9) {
			component = new StrongholdLargeIntersection(parent);
		} else if (draw > 0.8) {
			component = new StrongholdIntersection(parent);
		} else if (draw > 0.7) {
			component = new StrongholdRoom(parent);
		} else if (draw > 0.6) {
			component = new StrongholdSpiralStaircase(parent);
		} else if (draw > 0.4) {
			component = new StrongholdPrison(parent);
		} else if (draw > 0.2) {
			component = new StrongholdTurn(parent);
		} else {
			component = new StrongholdStaircase(parent);
		}
		component.setPosition(position.add(rotate(0, 0, length)));
		component.setRotation(rotation);
		component.randomize();
		components.add(component);
		return components;
	}

	@Override
	public BoundingBox getBoundingBox() {
		final Vector3 rotatedMin = transform(0, 0, 0);
		final Vector3 rotatedMax = transform(4, 4, length - 1);
		return new BoundingBox(Vector3.min(rotatedMin, rotatedMax), Vector3.max(rotatedMin, rotatedMax));
	}

	public int getLength() {
		return length;
	}

	public void setLength(byte length) {
		this.length = length;
	}

	public boolean isStartOfStronghold() {
		return startOfStronghold;
	}

	public void setStartOfStronghold(boolean startOfStronghold) {
		this.startOfStronghold = startOfStronghold;
	}
}
