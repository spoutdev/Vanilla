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

import java.util.Collections;
import java.util.List;

import org.spout.api.material.BlockMaterial;
import org.spout.api.math.Vector3;

import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.world.generator.normal.object.LootChestObject;
import org.spout.vanilla.world.generator.structure.PieceCuboidBuilder;
import org.spout.vanilla.world.generator.structure.SimpleBlockMaterialPicker;
import org.spout.vanilla.world.generator.structure.Structure;
import org.spout.vanilla.world.generator.structure.StructurePiece;

public class StrongholdLibrary extends StructurePiece {
	private final LootChestObject chestObject;
	private byte height = 11;

	public StrongholdLibrary(Structure parent) {
		super(parent);
		chestObject = new LootChestObject(getRandom());
		chestObject.setMinNumberOfStacks(1);
		chestObject.setMaxNumberOfStacks(4);
		chestObject.addMaterial(VanillaMaterials.BOOK, 20, 1, 3)
				.addMaterial(VanillaMaterials.PAPER, 20, 2, 7)
				.addMaterial(VanillaMaterials.MAP, 1, 1, 1)
				.addMaterial(VanillaMaterials.COMPASS, 1, 1, 1);
	}

	@Override
	public boolean canPlace() {
		final PieceCuboidBuilder box = new PieceCuboidBuilder(this);
		box.setMinMax(-1, -1, -1, 14, height, 15);
		return !box.intersectsLiquids();
	}

	@Override
	public void place() {
		// General shape
		final PieceCuboidBuilder box = new PieceCuboidBuilder(this);
		box.setPicker(new StrongholdBlockMaterialPicker(getRandom()));
		box.setMinMax(0, 0, 0, 13, height - 1, 14);
		box.toggleIgnoreAir();
		box.fill();
		box.toggleIgnoreAir();
		// Place the door
		StrongholdDoor.getRandomDoor(this, getRandom()).place(4, 1, 0);
		// Place some random spider webs
		final SimpleBlockMaterialPicker picker = new SimpleBlockMaterialPicker(VanillaMaterials.WEB, VanillaMaterials.WEB);
		box.setPicker(picker);
		box.setMinMax(2, 1, 1, 11, 4, 13);
		box.randomFill(0.07f);
		// Place bookshelves on the walls
		for (int i = 1; i <= 13; i++) {
			final boolean planks = (i - 1) % 4 == 0;
			final BlockMaterial material = planks ? VanillaMaterials.PLANK : VanillaMaterials.BOOKSHELF;
			picker.setOuterInnerMaterials(material, material);
			box.setMinMax(1, 1, i, 1, 4, i);
			box.fill();
			box.offsetMinMax(11, 0, 0, 11, 0, 0);
			box.fill();
			if (planks) {
				attachMaterial(2, 3, i, VanillaMaterials.TORCH);
				attachMaterial(11, 3, i, VanillaMaterials.TORCH);
			}
			if (isLargeRoom()) {
				box.setMinMax(1, 6, i, 1, 9, i);
				box.fill();
				box.offsetMinMax(11, 0, 0, 11, 0, 0);
				box.fill();
			}
		}
		// Add more book shelves in the library
		picker.setOuterInnerMaterials(VanillaMaterials.BOOKSHELF, VanillaMaterials.BOOKSHELF);
		for (int i = 3; i < 12; i += 2) {
			box.setMinMax(3, 1, i, 4, 3, i);
			box.fill();
			box.offsetMinMax(3, 0, 0, 3, 0, 0);
			box.fill();
			box.offsetMinMax(3, 0, 0, 3, 0, 0);
			box.fill();
		}
		// More decoration for large libraries
		if (isLargeRoom()) {
			// Place the planks for the second floor
			picker.setOuterInnerMaterials(VanillaMaterials.PLANK, VanillaMaterials.PLANK);
			box.setMinMax(1, 5, 1, 3, 5, 13);
			box.fill();
			box.offsetMinMax(9, 0, 0, 9, 0, 0);
			box.fill();
			box.setMinMax(4, 5, 1, 9, 5, 2);
			box.fill();
			box.offsetMinMax(0, 0, 11, 0, 0, 11);
			box.fill();
			setBlockMaterial(9, 5, 11, VanillaMaterials.PLANK);
			setBlockMaterial(8, 5, 11, VanillaMaterials.PLANK);
			setBlockMaterial(9, 5, 10, VanillaMaterials.PLANK);
			// Fences  on the second floor
			picker.setOuterInnerMaterials(VanillaMaterials.WOODEN_FENCE, VanillaMaterials.WOODEN_FENCE);
			box.setMinMax(3, 6, 2, 3, 6, 12);
			box.fill();
			box.offsetMinMax(7, 0, 0, 7, 0, -2);
			box.fill();
			box.setMinMax(4, 6, 2, 9, 6, 2);
			box.fill();
			box.offsetMinMax(0, 0, 10, -1, 0, 10);
			box.fill();
			setBlockMaterial(9, 6, 11, VanillaMaterials.WOODEN_FENCE);
			setBlockMaterial(8, 6, 11, VanillaMaterials.WOODEN_FENCE);
			setBlockMaterial(9, 6, 10, VanillaMaterials.WOODEN_FENCE);
			// Ladders for the first to the second floor
			setBlockMaterial(10, 1, 13, VanillaMaterials.LADDER, (short) 2);
			setBlockMaterial(10, 2, 13, VanillaMaterials.LADDER, (short) 2);
			setBlockMaterial(10, 3, 13, VanillaMaterials.LADDER, (short) 2);
			setBlockMaterial(10, 4, 13, VanillaMaterials.LADDER, (short) 2);
			setBlockMaterial(10, 5, 13, VanillaMaterials.LADDER, (short) 2);
			setBlockMaterial(10, 6, 13, VanillaMaterials.LADDER, (short) 2);
			setBlockMaterial(10, 7, 13, VanillaMaterials.LADDER, (short) 2);
			// Fence and torch ceiling light
			setBlockMaterial(6, 9, 7, VanillaMaterials.WOODEN_FENCE);
			setBlockMaterial(7, 9, 7, VanillaMaterials.WOODEN_FENCE);
			setBlockMaterial(6, 8, 7, VanillaMaterials.WOODEN_FENCE);
			setBlockMaterial(7, 8, 7, VanillaMaterials.WOODEN_FENCE);
			setBlockMaterial(6, 7, 7, VanillaMaterials.WOODEN_FENCE);
			setBlockMaterial(7, 7, 7, VanillaMaterials.WOODEN_FENCE);
			setBlockMaterial(5, 7, 7, VanillaMaterials.WOODEN_FENCE);
			setBlockMaterial(8, 7, 7, VanillaMaterials.WOODEN_FENCE);
			setBlockMaterial(6, 7, 6, VanillaMaterials.WOODEN_FENCE);
			setBlockMaterial(6, 7, 8, VanillaMaterials.WOODEN_FENCE);
			setBlockMaterial(7, 7, 6, VanillaMaterials.WOODEN_FENCE);
			setBlockMaterial(7, 7, 8, VanillaMaterials.WOODEN_FENCE);
			attachMaterial(5, 8, 7, VanillaMaterials.TORCH);
			attachMaterial(8, 8, 7, VanillaMaterials.TORCH);
			attachMaterial(6, 8, 6, VanillaMaterials.TORCH);
			attachMaterial(6, 8, 8, VanillaMaterials.TORCH);
			attachMaterial(7, 8, 6, VanillaMaterials.TORCH);
			attachMaterial(7, 8, 8, VanillaMaterials.TORCH);
		}
		// Place the loot chest
		chestObject.setRandom(getRandom());
		placeObject(3, 3, 5, chestObject);
		// An extra chest for large libraries
		if (isLargeRoom()) {
			setBlockMaterial(12, 9, 1, VanillaMaterials.AIR);
			placeObject(12, 8, 1, chestObject);
		}
	}

	@Override
	public void randomize() {
		setLargeRoom(getRandom().nextInt(4) == 0);
	}

	@Override
	public List<StructurePiece> getNextComponents() {
		return Collections.emptyList();
	}

	@Override
	public BoundingBox getBoundingBox() {
		final Vector3 rotatedMin = transform(0, 0, 0);
		final Vector3 rotatedMax = transform(13, height - 1, 14);
		return new BoundingBox(Vector3.min(rotatedMin, rotatedMax), Vector3.max(rotatedMin, rotatedMax));
	}

	public boolean isLargeRoom() {
		return height > 6;
	}

	public void setLargeRoom(boolean largeRoom) {
		height = (byte) (largeRoom ? 11 : 6);
	}
}
