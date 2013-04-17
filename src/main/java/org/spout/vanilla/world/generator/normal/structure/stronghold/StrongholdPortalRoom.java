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

import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.world.generator.normal.structure.stronghold.StrongholdDoor.IronFenceDoor;
import org.spout.vanilla.world.generator.structure.PieceCuboidBuilder;
import org.spout.vanilla.world.generator.structure.SimpleBlockMaterialPicker;
import org.spout.vanilla.world.generator.structure.Structure;
import org.spout.vanilla.world.generator.structure.StructurePiece;
import org.spout.vanilla.world.generator.theend.object.EndPortalObject;

public class StrongholdPortalRoom extends StructurePiece {
	public StrongholdPortalRoom(Structure parent) {
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
		final StrongholdBlockMaterialPicker stone = new StrongholdBlockMaterialPicker(getRandom());
		// General shape
		box.setPicker(stone);
		box.setMinMax(-3, 0, 0, 7, 7, 15).fill();
		// Place the door
		new IronFenceDoor(this).place(1, 1, 0);
		// Some stone for the side lava pools and ceiling
		box.setMinMax(-2, 6, 1, -2, 6, 14).fill();
		box.offsetMinMax(8, 0, 0, 8, 0, 0).fill();
		box.setMinMax(-2, 6, 1, 5, 6, 2).fill();
		box.offsetMinMax(0, 0, 13, 0, 0, 12).fill();
		box.setMinMax(-2, 1, 1, -1, 1, 4).fill();
		box.offsetMinMax(7, 0, 0, 7, 0, 0).fill();
		// Fill the side lava pools
		box.setPicker(picker);
		picker.setOuterInnerMaterials(VanillaMaterials.STATIONARY_LAVA, VanillaMaterials.STATIONARY_LAVA);
		box.setMinMax(-2, 1, 1, -2, 1, 3).fill();
		box.offsetMinMax(8, 0, 0, 8, 0, 0).fill();
		// Place the stone for the portal lava pool
		box.setPicker(stone);
		box.setMinMax(0, 1, 8, 4, 1, 12).fill();
		// Place the lava for the portal lava pool
		box.setPicker(picker);
		box.offsetMinMax(1, 0, 1, -1, 0, -1).fill();
		// Add iron bars to decorate the walls
		picker.setOuterInnerMaterials(VanillaMaterials.IRON_BARS, VanillaMaterials.IRON_BARS);
		for (int i = 3; i < 14; i += 2) {
			box.setMinMax(-3, 3, i, -3, 4, i).fill();
			box.offsetMinMax(10, 0, 0, 10, 0, 0).fill();
		}
		for (int i = -1; i < 6; i += 2) {
			box.setMinMax(i, 3, 15, i, 4, 15).fill();
		}
		// Some stone for the stairs
		box.setPicker(stone);
		box.setMinMax(1, 1, 5, 3, 1, 7).fill();
		box.offsetMinMax(0, 1, 1, 0, 1, 0).fill();
		box.offsetMinMax(0, 1, 1, 0, 1, 0).fill();
		// Place the stairs
		for (int i = 1; i <= 3; i++) {
			setBlockMaterial(i, 1, 4, VanillaMaterials.STAIRS_STONE_BRICK, (short) 2);
			setBlockMaterial(i, 2, 5, VanillaMaterials.STAIRS_STONE_BRICK, (short) 2);
			setBlockMaterial(i, 3, 6, VanillaMaterials.STAIRS_STONE_BRICK, (short) 2);
		}
		// Place the unlit portal
		placeObject(2, 3, 10, new EndPortalObject(getRandom()));
		// Place the mob spawner
		setBlockMaterial(2, 3, 6, VanillaMaterials.MONSTER_SPAWNER);
	}

	@Override
	public void randomize() {
	}

	@Override
	public List<StructurePiece> getNextPieces() {
		return Collections.emptyList();
	}

	@Override
	public BoundingBox getBoundingBox() {
		return new BoundingBox(transform(-3, 0, 0), transform(7, 7, 15));
	}
}
