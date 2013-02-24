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
import java.util.Random;

import org.spout.api.math.Vector3;

import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.world.generator.structure.PieceCuboidBuilder;
import org.spout.vanilla.world.generator.structure.SimpleBlockMaterialPicker;
import org.spout.vanilla.world.generator.structure.Structure;
import org.spout.vanilla.world.generator.structure.StructurePiece;

public class StrongholdLargeIntersection extends StructurePiece {
	private boolean nextComponentRightLow = false;
	private boolean nextComponentRightHigh = false;
	private boolean nextComponentLeftLow = false;
	private boolean nextComponentLeftHigh = false;

	public StrongholdLargeIntersection(Structure parent) {
		super(parent);
	}

	@Override
	public boolean canPlace() {
		final PieceCuboidBuilder box = new PieceCuboidBuilder(this);
		box.setMinMax(-1, -1, -1, 10, 9, 11);
		return !box.intersectsLiquids();
	}

	@Override
	public void place() {
		// Building objects
		final PieceCuboidBuilder box = new PieceCuboidBuilder(this);
		final SimpleBlockMaterialPicker picker = new SimpleBlockMaterialPicker();
		final StrongholdBlockMaterialPicker stone = new StrongholdBlockMaterialPicker(getRandom());
		// General shape
		box.setPicker(stone);
		box.setMinMax(0, 0, 0, 9, 8, 10);
		box.toggleIgnoreAir();
		box.fill();
		box.toggleIgnoreAir();
		// Place the door
		StrongholdDoor.getRandomDoor(this, getRandom()).place(4, 3, 0);
		// Access to the next components of the intersection
		box.setPicker(picker);
		if (nextComponentRightLow) {
			box.setMinMax(0, 3, 1, 0, 5, 3);
			box.fill();
		}
		if (nextComponentRightHigh) {
			box.setMinMax(0, 5, 7, 0, 7, 9);
			box.fill();
		}
		if (nextComponentLeftLow) {
			box.setMinMax(9, 3, 1, 9, 5, 3);
			box.fill();
		}
		if (nextComponentLeftHigh) {
			box.setMinMax(9, 5, 7, 9, 7, 9);
			box.fill();
		}
		// The bottom access way
		box.setMinMax(5, 1, 10, 7, 3, 10);
		box.fill();
		// Finish the interior
		box.setPicker(stone);
		box.setMinMax(1, 2, 1, 8, 2, 6);
		box.fill();
		box.setMinMax(4, 1, 5, 4, 4, 9);
		box.fill();
		box.offsetMinMax(4, 0, 0, 4, 0, 0);
		box.fill();
		box.setMinMax(1, 4, 7, 3, 4, 9);
		box.fill();
		box.setMinMax(1, 3, 5, 3, 3, 6);
		box.fill();
		picker.setOuterInnerMaterials(VanillaMaterials.SLAB, VanillaMaterials.SLAB);
		box.setPicker(picker);
		box.setMinMax(1, 3, 4, 3, 3, 4);
		box.fill();
		box.offsetMinMax(0, 1, 2, 0, 1, 2);
		box.fill();
		box.setPicker(stone);
		box.setMinMax(5, 1, 7, 7, 1, 8);
		box.fill();
		box.setPicker(picker);
		box.offsetMinMax(0, 0, 2, 0, 0, 1);
		box.fill();
		box.offsetMinMax(0, 1, -2, 0, 1, -2);
		box.fill();
		box.setMinMax(4, 5, 7, 4, 5, 9);
		box.fill();
		box.offsetMinMax(4, 0, 0, 4, 0, 0);
		box.fill();
		box.offsetMinMax(-3, 0, 0, -1, 0, 0);
		box.fill();
		// Place a torch
		attachMaterial(6, 5, 6, VanillaMaterials.TORCH);
	}

	@Override
	public void randomize() {
		final Random random = getRandom();
		nextComponentRightLow = random.nextBoolean();
		nextComponentRightHigh = random.nextBoolean();
		nextComponentLeftLow = random.nextBoolean();
		nextComponentLeftHigh = random.nextInt(3) > 0;
	}

	@Override
	public List<StructurePiece> getNextComponents() {
		final List<StructurePiece> components = new ArrayList<StructurePiece>();
		final Random random = getRandom();
		final StructurePiece component = pickComponent(random, true);
		component.setPosition(position.add(rotate(4, 0, 11)));
		component.setRotation(rotation);
		component.randomize();
		components.add(component);
		if (nextComponentRightLow) {
			final StructurePiece next = pickComponent(random, false);
			next.setPosition(position.add(rotate(-1, 2, 0)));
			next.setRotation(rotation.rotate(-90, 0, 1, 0));
			next.randomize();
			components.add(next);
		}
		if (nextComponentRightHigh) {
			final StructurePiece next = pickComponent(random, true);
			next.setPosition(position.add(rotate(-1, 4, 6)));
			next.setRotation(rotation.rotate(-90, 0, 1, 0));
			next.randomize();
			components.add(next);
		}
		if (nextComponentLeftLow) {
			final StructurePiece next = pickComponent(random, false);
			next.setPosition(position.add(rotate(10, 2, 4)));
			next.setRotation(rotation.rotate(90, 0, 1, 0));
			next.randomize();
			components.add(next);
		}
		if (nextComponentLeftHigh) {
			final StructurePiece next = pickComponent(random, true);
			next.setPosition(position.add(rotate(10, 4, 10)));
			next.setRotation(rotation.rotate(90, 0, 1, 0));
			next.randomize();
			components.add(next);
		}
		return components;
	}

	private StructurePiece pickComponent(Random random, boolean allowLarge) {
		final float draw = random.nextFloat();
		if (draw > 0.8) {
			return new StrongholdChestCorridor(parent);
		} else if (allowLarge && draw > 0.6) {
			return new StrongholdPrison(parent);
		} else if (draw > 0.4) {
			return new StrongholdCorridor(parent);
		} else if (draw > 0.2) {
			return new StrongholdSpiralStaircase(parent);
		} else {
			return new StrongholdStaircase(parent);
		}
	}

	@Override
	public BoundingBox getBoundingBox() {
		final Vector3 rotatedMin = transform(0, 0, 0);
		final Vector3 rotatedMax = transform(9, 8, 10);
		return new BoundingBox(Vector3.min(rotatedMin, rotatedMax), Vector3.max(rotatedMin, rotatedMax));
	}

	public boolean hasNextComponentRightLow() {
		return nextComponentRightLow;
	}

	public void setNextComponentRightLow(boolean nextComponentRightLow) {
		this.nextComponentRightLow = nextComponentRightLow;
	}

	public boolean hasNextComponentRightHigh() {
		return nextComponentRightHigh;
	}

	public void setNextComponentRightHigh(boolean nextComponentRightHigh) {
		this.nextComponentRightHigh = nextComponentRightHigh;
	}

	public boolean hasNextComponentLeftLow() {
		return nextComponentLeftLow;
	}

	public void setNextComponentLeftLow(boolean nextComponentLeftLow) {
		this.nextComponentLeftLow = nextComponentLeftLow;
	}

	public boolean hasNextComponentLeftHigh() {
		return nextComponentLeftHigh;
	}

	public void setNextComponentLeftHigh(boolean nextComponentLeftHigh) {
		this.nextComponentLeftHigh = nextComponentLeftHigh;
	}
}
