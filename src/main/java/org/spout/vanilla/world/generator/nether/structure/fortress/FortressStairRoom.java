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

import com.google.common.collect.Lists;

import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.world.generator.structure.PieceCuboidBuilder;
import org.spout.vanilla.world.generator.structure.SimpleBlockMaterialPicker;
import org.spout.vanilla.world.generator.structure.Structure;
import org.spout.vanilla.world.generator.structure.StructurePiece;
import org.spout.vanilla.world.generator.structure.StructurePiece.BoundingBox;
import org.spout.vanilla.world.generator.structure.WeightedNextStructurePiece;

public class FortressStairRoom extends WeightedNextStructurePiece {
	private static final WeightedNextPieceCache DEFAULT_NEXT = new WeightedNextPieceCache().
			add(FortressBlazeBalcony.class, 1).
			add(FortressBalconyIntersection.class, 4).
			add(FortressGateIntersection.class, 4).
			add(FortressTurn.class, 6).
			add(FortressBridge.class, 8).
			add(FortressCorridor.class, 10);

	public FortressStairRoom(Structure parent) {
		super(parent, DEFAULT_NEXT);
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
		box.setMinMax(-1, 0, 0, 5, 1, 6).fill();
		// Interior space
		picker.setOuterInnerMaterials(VanillaMaterials.AIR, VanillaMaterials.AIR);
		box.offsetMinMax(0, 2, 0, 0, 9, 0).fill();
		// Walls
		picker.setOuterInnerMaterials(VanillaMaterials.NETHER_BRICK, VanillaMaterials.NETHER_BRICK);
		box.setMinMax(-1, 2, 0, 0, 8, 0).fill();
		box.offsetMinMax(5, 0, 0, 5, 0, 0).fill();
		box.setMinMax(-1, 2, 1, -1, 8, 6).fill();
		box.offsetMinMax(6, 0, 0, 6, 0, 0).fill();
		box.setMinMax(0, 2, 6, 4, 8, 6).fill();
		// Windows
		picker.setOuterInnerMaterials(VanillaMaterials.NETHER_BRICK_FENCE, VanillaMaterials.NETHER_BRICK_FENCE);
		box.setMinMax(-1, 3, 2, -1, 5, 4).fill();
		box.offsetMinMax(6, 0, 0, 6, 0, -2).fill();
		box.offsetMinMax(0, 0, 2, 0, 0, 2).fill();
		// Stairs to the roof
		setBlockMaterial(4, 2, 5, VanillaMaterials.NETHER_BRICK);
		picker.setOuterInnerMaterials(VanillaMaterials.NETHER_BRICK, VanillaMaterials.NETHER_BRICK);
		box.setMinMax(3, 2, 5, 3, 3, 5).fill();
		box.offsetMinMax(-1, 0, 0, -1, 1, 0).fill();
		box.offsetMinMax(-1, 0, 0, -1, 1, 0).fill();
		box.offsetMinMax(-1, 0, 0, -1, 1, 0).fill();
		// Roof
		box.setMinMax(0, 7, 1, 4, 7, 4).fill();
		// Remove part of the side wall on the roof to make an entrance
		picker.setOuterInnerMaterials(VanillaMaterials.AIR, VanillaMaterials.AIR);
		box.setMinMax(5, 8, 2, 5, 8, 4).fill();
		// Material above the lower entrance to make shorter
		picker.setOuterInnerMaterials(VanillaMaterials.NETHER_BRICK, VanillaMaterials.NETHER_BRICK);
		box.setMinMax(1, 6, 0, 3, 8, 0).fill();
		// Fences above the lower entrance to make a gateway
		picker.setOuterInnerMaterials(VanillaMaterials.NETHER_BRICK_FENCE, VanillaMaterials.NETHER_BRICK_FENCE);
		box.setMinMax(1, 5, 0, 3, 5, 0).fill();
		// Fill down to the ground
		for (int xx = -1; xx <= 5; xx++) {
			for (int zz = 0; zz <= 6; zz++) {
				fillDownwards(xx, -1, zz, 50, VanillaMaterials.NETHER_BRICK);
			}
		}
	}

	@Override
	public void randomize() {
	}

	@Override
	public List<StructurePiece> getNextPieces() {
		final StructurePiece piece = getNextPiece();
		piece.setPosition(position.add(rotate(6, 6, 5)));
		piece.setRotation(rotation.rotate(90, 0, 1, 0));
		piece.randomize();
		return Lists.newArrayList(piece);
	}

	@Override
	public BoundingBox getBoundingBox() {
		return new BoundingBox(transform(-1, 0, 0), transform(5, 10, 6));
	}
}
