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
package org.spout.vanilla.world.generator.nether.structure.fortress;

import java.util.List;

import org.spout.api.math.Vector3;

import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.world.generator.structure.PieceCuboidBuilder;
import org.spout.vanilla.world.generator.structure.SimpleBlockMaterialPicker;
import org.spout.vanilla.world.generator.structure.Structure;
import org.spout.vanilla.world.generator.structure.StructurePiece;

public class FortressNetherWartStairs extends StructurePiece {
	public FortressNetherWartStairs(Structure parent) {
		super(parent);
	}

	@Override
	public boolean canPlace() {
		return true;
	}

	@Override
	public void place() {
		// Building objects
		final PieceCuboidBuilder box = new PieceCuboidBuilder(this);
		final SimpleBlockMaterialPicker picker = new SimpleBlockMaterialPicker();
		box.setPicker(picker);
		// Floor
		picker.setOuterInnerMaterials(VanillaMaterials.NETHER_BRICK, VanillaMaterials.NETHER_BRICK);
		box.setMinMax(0, 3, 0, 12, 4, 12).fill();
		// Interior space
		picker.setOuterInnerMaterials(VanillaMaterials.AIR, VanillaMaterials.AIR);
		box.offsetMinMax(0, 2, 0, 0, 9, 0).fill();
		// The too walls without entrances
		picker.setOuterInnerMaterials(VanillaMaterials.NETHER_BRICK, VanillaMaterials.NETHER_BRICK);
		box.setMinMax(0, 5, 0, 1, 12, 12).fill();
		box.offsetMinMax(11, 0, 0, 11, 0, 0).fill();
		box.setMinMax(2, 5, 11, 4, 12, 12).fill();
		box.offsetMinMax(6, 0, 0, 6, 0, 0).fill();
		// The two other walls
		box.setMinMax(5, 9, 0, 7, 12, 1).fill();
		box.offsetMinMax(0, 0, 11, 0, 0, 11).fill();
		box.setMinMax(2, 5, 0, 4, 12, 1).fill();
		box.offsetMinMax(6, 0, 0, 6, 0, 0).fill();
		// Roof
		box.setMinMax(2, 11, 2, 10, 12, 10).fill();
		// Decorate the outside
		picker.setOuterInnerMaterials(VanillaMaterials.NETHER_BRICK_FENCE, VanillaMaterials.NETHER_BRICK_FENCE);
		for (int xxzz = 1; xxzz <= 11; xxzz += 2) {
			// Fences on the outside walls
			box.setMinMax(xxzz, 10, 0, xxzz, 11, 0).fill();
			box.offsetMinMax(0, 0, 12, 0, 0, 12).fill();
			box.setMinMax(0, 10, xxzz, 0, 11, xxzz).fill();
			box.offsetMinMax(12, 0, 0, 12, 0, 0).fill();
			// Fences and brick on the roof
			setBlockMaterial(xxzz, 13, 0, VanillaMaterials.NETHER_BRICK);
			setBlockMaterial(xxzz, 13, 12, VanillaMaterials.NETHER_BRICK);
			setBlockMaterial(0, 13, xxzz, VanillaMaterials.NETHER_BRICK);
			setBlockMaterial(12, 13, xxzz, VanillaMaterials.NETHER_BRICK);
			setBlockMaterial(xxzz + 1, 13, 0, VanillaMaterials.NETHER_BRICK_FENCE);
			setBlockMaterial(xxzz + 1, 13, 12, VanillaMaterials.NETHER_BRICK_FENCE);
			setBlockMaterial(0, 13, xxzz + 1, VanillaMaterials.NETHER_BRICK_FENCE);
			setBlockMaterial(12, 13, xxzz + 1, VanillaMaterials.NETHER_BRICK_FENCE);
		}
		// Corner fences for the roof
		setBlockMaterial(0, 13, 0, VanillaMaterials.NETHER_BRICK_FENCE);
		setBlockMaterial(0, 13, 12, VanillaMaterials.NETHER_BRICK_FENCE);
		setBlockMaterial(0, 13, 0, VanillaMaterials.NETHER_BRICK_FENCE);
		setBlockMaterial(12, 13, 0, VanillaMaterials.NETHER_BRICK_FENCE);
		// Fences on the inside
		for (int zz = 3; zz <= 9; zz += 2) {
			box.setMinMax(1, 7, zz, 1, 8, zz).fill();
			box.offsetMinMax(10, 0, 0, 10, 0, 0).fill();
		}
		// Generate the staircase to the roof
		for (int yy = 0; yy <= 6; yy++) {
			final int zz = yy + 4;
			for (int xx = 5; xx <= 7; xx++) {
				setBlockMaterial(xx, 5 + yy, zz, VanillaMaterials.STAIRS_NETHER_BRICK, (short) 2);
			}
			picker.setOuterInnerMaterials(VanillaMaterials.NETHER_BRICK, VanillaMaterials.NETHER_BRICK);
			if (zz >= 5) {
				if (zz <= 8) {
					box.setMinMax(5, 5, zz, 7, yy + 4, zz).fill();
				} else {
					box.setMinMax(5, 8, zz, 7, yy + 4, zz).fill();
				}
			}
			picker.setOuterInnerMaterials(VanillaMaterials.AIR, VanillaMaterials.AIR);
			if (yy >= 1) {
				box.setMinMax(5, 6 + yy, zz, 7, 9 + yy, zz).fill();
			}
		}
		for (int xx = 5; xx <= 7; xx++) {
			setBlockMaterial(xx, 12, 11, VanillaMaterials.STAIRS_NETHER_BRICK, (short) 2);
		}
		// Fences in the sides of the staircase
		picker.setOuterInnerMaterials(VanillaMaterials.NETHER_BRICK_FENCE, VanillaMaterials.NETHER_BRICK_FENCE);
		box.setMinMax(5, 6, 7, 5, 7, 7).fill();
		box.offsetMinMax(2, 0, 0, 2, 0, 0).fill();
		// Remove part of the fence on the roof to make an entrance
		picker.setOuterInnerMaterials(VanillaMaterials.AIR, VanillaMaterials.AIR);
		box.setMinMax(5, 13, 12, 7, 13, 12).fill();
		// Passage ways through the room with stairs on both ends
		picker.setOuterInnerMaterials(VanillaMaterials.NETHER_BRICK, VanillaMaterials.NETHER_BRICK);
		box.setMinMax(2, 5, 2, 3, 5, 3).fill();
		box.offsetMinMax(0, 0, 7, 0, 0, 7).fill();
		box.setMinMax(2, 5, 4, 2, 5, 8).fill();
		box.setMinMax(9, 5, 2, 10, 5, 3).fill();
		box.offsetMinMax(0, 0, 7, 0, 0, 7).fill();
		box.setMinMax(10, 5, 4, 10, 5, 8).fill();
		setBlockMaterial(4, 5, 2, VanillaMaterials.STAIRS_NETHER_BRICK, (short) 1);
		setBlockMaterial(4, 5, 3, VanillaMaterials.STAIRS_NETHER_BRICK, (short) 1);
		setBlockMaterial(4, 5, 9, VanillaMaterials.STAIRS_NETHER_BRICK, (short) 1);
		setBlockMaterial(4, 5, 10, VanillaMaterials.STAIRS_NETHER_BRICK, (short) 1);
		setBlockMaterial(8, 5, 2, VanillaMaterials.STAIRS_NETHER_BRICK, (short) 0);
		setBlockMaterial(8, 5, 3, VanillaMaterials.STAIRS_NETHER_BRICK, (short) 0);
		setBlockMaterial(8, 5, 9, VanillaMaterials.STAIRS_NETHER_BRICK, (short) 0);
		setBlockMaterial(8, 5, 10, VanillaMaterials.STAIRS_NETHER_BRICK, (short) 0);
		// Nether wart around the stairs
		picker.setOuterInnerMaterials(VanillaMaterials.SOUL_SAND, VanillaMaterials.SOUL_SAND);
		box.setMinMax(3, 4, 4, 4, 4, 8).fill();
		box.offsetMinMax(5, 0, 0, 5, 0, 0).fill();
		picker.setOuterInnerMaterials(VanillaMaterials.NETHER_WART_BLOCK, VanillaMaterials.NETHER_WART_BLOCK);
		box.setMinMax(3, 5, 4, 4, 5, 8).fill();
		box.offsetMinMax(5, 0, 0, 5, 0, 0).fill();
		// Material just above the pillar to create some arch shape
		picker.setOuterInnerMaterials(VanillaMaterials.NETHER_BRICK, VanillaMaterials.NETHER_BRICK);
		box.setMinMax(4, 2, 0, 8, 2, 12).fill();
		box.offsetMinMax(-4, 0, 4, 4, 0, -4).fill();
		box.setMinMax(4, 0, 0, 8, 1, 3).fill();
		box.offsetMinMax(0, 0, 9, 0, 0, 9).fill();
		box.setMinMax(0, 0, 4, 3, 1, 8).fill();
		box.offsetMinMax(9, 0, 0, 9, 0, 0).fill();
		// Build the four pillars down to the gound
		for (int xx = 4; xx <= 8; xx++) {
			for (int zz = 0; zz <= 2; zz++) {
				fillDownwards(xx, -1, zz, 50, VanillaMaterials.NETHER_BRICK);
				fillDownwards(xx, -1, 12 - zz, 50, VanillaMaterials.NETHER_BRICK);
			}
		}
		for (int xx = 0; xx <= 2; xx++) {
			for (int zz = 4; zz <= 8; zz++) {
				fillDownwards(xx, -1, zz, 50, VanillaMaterials.NETHER_BRICK);
				fillDownwards(12 - xx, -1, zz, 50, VanillaMaterials.NETHER_BRICK);
			}
		}
	}

	@Override
	public void randomize() {
	}

	@Override
	public List<StructurePiece> getNextComponents() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public BoundingBox getBoundingBox() {
		final Vector3 rotatedMin = transform(0, 0, 0);
		final Vector3 rotatedMax = transform(12, 15, 12);
		return new BoundingBox(Vector3.min(rotatedMin, rotatedMax), Vector3.max(rotatedMin, rotatedMax));
	}
}
