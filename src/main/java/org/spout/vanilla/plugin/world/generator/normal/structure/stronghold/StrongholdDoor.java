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
package org.spout.vanilla.plugin.world.generator.normal.structure.stronghold;

import java.util.Random;

import org.spout.api.material.block.BlockFace;

import org.spout.vanilla.plugin.material.VanillaMaterials;
import org.spout.vanilla.plugin.world.generator.structure.ComponentCuboidPart;
import org.spout.vanilla.plugin.world.generator.structure.SimpleBlockMaterialPicker;
import org.spout.vanilla.plugin.world.generator.structure.StructureComponent;

public abstract class StrongholdDoor {
	protected StructureComponent parent;

	public StrongholdDoor(StructureComponent parent) {
		this.parent = parent;
	}

	public abstract void place(int xx, int yy, int zz);

	public static StrongholdDoor getRandomDoor(StructureComponent parent, Random random) {
		final byte draw = (byte) random.nextInt(5);
		switch (draw) {
			case 0:
				return new WoodDoor(parent);
			case 1:
				return new IronFenceDoor(parent);
			case 2:
				return new IronDoor(parent);
			default:
				return new EmptyDoorway(parent);
		}
	}

	public static class EmptyDoorway extends StrongholdDoor {
		public EmptyDoorway(StructureComponent parent) {
			super(parent);
		}

		@Override
		public void place(int xx, int yy, int zz) {
			final ComponentCuboidPart box = new ComponentCuboidPart(parent);
			box.setPicker(new SimpleBlockMaterialPicker());
			box.setMinMax(xx, yy, zz, xx + 2, yy + 2, zz);
			box.fill(false);
		}
	}

	public static class IronFenceDoor extends StrongholdDoor {
		public IronFenceDoor(StructureComponent parent) {
			super(parent);
		}

		@Override
		public void place(int xx, int yy, int zz) {
			parent.setBlockMaterial(xx + 1, yy, zz, VanillaMaterials.AIR);
			parent.setBlockMaterial(xx + 1, yy + 1, zz, VanillaMaterials.AIR);
			parent.setBlockMaterial(xx, yy, zz, VanillaMaterials.IRON_BARS);
			parent.setBlockMaterial(xx, yy + 1, zz, VanillaMaterials.IRON_BARS);
			parent.setBlockMaterial(xx, yy + 2, zz, VanillaMaterials.IRON_BARS);
			parent.setBlockMaterial(xx + 1, yy + 2, zz, VanillaMaterials.IRON_BARS);
			parent.setBlockMaterial(xx + 2, yy + 2, zz, VanillaMaterials.IRON_BARS);
			parent.setBlockMaterial(xx + 2, yy + 1, zz, VanillaMaterials.IRON_BARS);
			parent.setBlockMaterial(xx + 2, yy, zz, VanillaMaterials.IRON_BARS);
		}
	}

	public static class WoodDoor extends StrongholdDoor {
		public WoodDoor(StructureComponent parent) {
			super(parent);
		}

		@Override
		public void place(int xx, int yy, int zz) {
			parent.setBlockMaterial(xx, yy, zz, VanillaMaterials.STONE_BRICK);
			parent.setBlockMaterial(xx, yy + 1, zz, VanillaMaterials.STONE_BRICK);
			parent.setBlockMaterial(xx, yy + 2, zz, VanillaMaterials.STONE_BRICK);
			parent.setBlockMaterial(xx + 1, yy + 2, zz, VanillaMaterials.STONE_BRICK);
			parent.setBlockMaterial(xx + 2, yy + 2, zz, VanillaMaterials.STONE_BRICK);
			parent.setBlockMaterial(xx + 2, yy + 1, zz, VanillaMaterials.STONE_BRICK);
			parent.setBlockMaterial(xx + 2, yy, zz, VanillaMaterials.STONE_BRICK);
			parent.placeDoor(xx + 1, yy, zz, VanillaMaterials.WOODEN_DOOR_BLOCK, BlockFace.EAST);
		}
	}

	public static class IronDoor extends StrongholdDoor {
		public IronDoor(StructureComponent parent) {
			super(parent);
		}

		@Override
		public void place(int xx, int yy, int zz) {
			parent.setBlockMaterial(xx, yy, zz, VanillaMaterials.STONE_BRICK);
			parent.setBlockMaterial(xx, yy + 1, zz, VanillaMaterials.STONE_BRICK);
			parent.setBlockMaterial(xx, yy + 2, zz, VanillaMaterials.STONE_BRICK);
			parent.setBlockMaterial(xx + 1, yy + 2, zz, VanillaMaterials.STONE_BRICK);
			parent.setBlockMaterial(xx + 2, yy + 2, zz, VanillaMaterials.STONE_BRICK);
			parent.setBlockMaterial(xx + 2, yy + 1, zz, VanillaMaterials.STONE_BRICK);
			parent.setBlockMaterial(xx + 2, yy, zz, VanillaMaterials.STONE_BRICK);
			parent.placeDoor(xx + 1, yy, zz, VanillaMaterials.IRON_DOOR_BLOCK, BlockFace.EAST);
			parent.setBlockMaterial(xx + 2, yy + 1, zz + 1, VanillaMaterials.STONE_BUTTON, (short) 3);
			parent.setBlockMaterial(xx + 2, yy + 1, zz - 1, VanillaMaterials.STONE_BUTTON, (short) 4);
		}
	}
}
