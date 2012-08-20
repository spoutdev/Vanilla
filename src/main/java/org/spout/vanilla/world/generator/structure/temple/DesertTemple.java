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
package org.spout.vanilla.world.generator.structure.temple;

import java.util.Collections;
import java.util.List;

import org.spout.api.material.BlockMaterial;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.block.BlockFaces;
import org.spout.api.math.MathHelper;
import org.spout.api.math.Quaternion;
import org.spout.api.math.Vector3;

import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.Stairs;
import org.spout.vanilla.material.block.solid.Sandstone;
import org.spout.vanilla.material.block.solid.Wool;
import org.spout.vanilla.material.block.stair.SandstoneStairs;
import org.spout.vanilla.world.generator.normal.object.LootChestObject;
import org.spout.vanilla.world.generator.structure.ComplexLayoutPainter;
import org.spout.vanilla.world.generator.structure.ComponentCuboidPart;
import org.spout.vanilla.world.generator.structure.ComponentPlanePart;
import org.spout.vanilla.world.generator.structure.SimpleBlockMaterialPicker;
import org.spout.vanilla.world.generator.structure.Structure;
import org.spout.vanilla.world.generator.structure.StructureComponent;

public class DesertTemple extends StructureComponent {
	private BlockMaterial CENTER_MATERIAL = Wool.BLUE_WOOL;
	private BlockMaterial ACCENT_MATERIAL = Wool.ORANGE_WOOL;
	private BlockMaterial PRIMARY_BLOCK = VanillaMaterials.SANDSTONE;
	private BlockMaterial SMOOTH_BLOCK = Sandstone.SMOOTH;
	private BlockMaterial GLYPH_BLOCK = Sandstone.DECORATIVE;
	private LootChestObject lootChest = new LootChestObject();
	private ComplexLayoutPainter centerCross = new ComplexLayoutPainter(this);
	private ComplexLayoutPainter glyph = new ComplexLayoutPainter(this);

	public DesertTemple(Structure parent) {
		super(parent);
		lootChest.addMaterial(VanillaMaterials.IRON_BARS, 0.1, 1, 3); //TODO Investigate how the materials are distributed
		centerCross.setBlockMaterial('0', PRIMARY_BLOCK);
		centerCross.setBlockMaterial('o', ACCENT_MATERIAL);
		centerCross.setBlockMaterial('b', CENTER_MATERIAL);
		centerCross.setLayout("000o000\n000o000\n00o0o00\noo0b0oo\n00o0o00\n000o000\n000o000");

		glyph.setBlockMaterial('-', SMOOTH_BLOCK);
		glyph.setBlockMaterial('o', ACCENT_MATERIAL);
		glyph.setBlockMaterial('=', GLYPH_BLOCK);
		glyph.setLayout("---\nooo\no=o\n-o-\no=o\n-o-\n-o-");
	}

	@Override
	public void randomize() {
	}

	@Override
	public void place() {
		ComponentCuboidPart cuboid = new ComponentCuboidPart(this);
		ComponentPlanePart plane = new ComponentPlanePart(this);
		SimpleBlockMaterialPicker picker = new SimpleBlockMaterialPicker();
		final Vector3[] diagonalOffsets = {
				new Vector3(-1, 0, -1),
				new Vector3(1, 0, -1),
				new Vector3(-1, 0, 1),
				new Vector3(1, 0, 1),
		};

		//		int x = 0, y = 0, z = 0;

		// Place the ground
		picker.setOuterInnerMaterials(PRIMARY_BLOCK, PRIMARY_BLOCK);
		cuboid.setMinMax(-10, -4, -10, 10, 0, 10);
		cuboid.fill(picker, false);

		// Build the pyramid
		for (int i = 1; i <= 10; i++) {
			plane.setMinMax(-i, 11 - i, -i, i, 11 - i, i);
			picker.setOuterInnerMaterials(PRIMARY_BLOCK, VanillaMaterials.AIR);
			if (i == 7) {
				picker.setInnerMaterial(PRIMARY_BLOCK);
			}
			plane.fill(picker, false);
			if (i == 7) {
				plane.setMinMax(-1, 11 - i, -1, 1, 11 - i, 1);
				picker.setOuterInnerMaterials(VanillaMaterials.AIR, VanillaMaterials.AIR);
				plane.fill(picker, false);
			}
		}

		// Decorate the interior
		//		setBlockMaterial(0, 0, 0, CENTER_MATERIAL);
		for (Vector3 offset : diagonalOffsets) {
			int x = (int) offset.getX();
			int z = (int) offset.getZ();
			//			setBlockMaterial(x, 0, z, ACCENT_MATERIAL);

			// Set the piles
			for (int yy = 1; yy <= 3; yy++) {
				setBlockMaterial(x * 2, yy, z * 2, SMOOTH_BLOCK);
			}
		}

		offsetPosition(-3, 0, -3);
		centerCross.draw();
		offsetPosition(3, 0, 3);

		//		for (BlockFace face:BlockFaces.NESW) {
		//			Vector3 offset = face.getOffset();
		//			int x = (int) offset.getX();
		//			int z = (int) offset.getZ();
		//			setBlockMaterial(x * 2, 0, z * 2, ACCENT_MATERIAL);
		//			setBlockMaterial(x * 3, 0, z * 3, ACCENT_MATERIAL);
		//		}

		// Dig the pitfall
		for (int yy = -1; yy >= -14; yy--) {
			BlockMaterial current = null;
			if (yy >= -8) {
				current = PRIMARY_BLOCK;
			} else if (yy == -10) {
				current = GLYPH_BLOCK;
			} else {
				current = SMOOTH_BLOCK;
			}

			plane.setMinMax(-2, yy, -2, 2, yy, 2);
			picker.setOuterInnerMaterials(current, yy != -14 && yy != -12 ? VanillaMaterials.AIR : SMOOTH_BLOCK);
			if (yy == -13) {
				picker.setInnerMaterial(VanillaMaterials.TNT);
			}
			plane.fill(picker, false);

			// Make room for the loot chests
			if (yy == -10 || yy == -11) {
				setBlockMaterial(2, yy, 0, VanillaMaterials.AIR);
				setBlockMaterial(-2, yy, 0, VanillaMaterials.AIR);
				setBlockMaterial(0, yy, -2, VanillaMaterials.AIR);
				setBlockMaterial(0, yy, 2, VanillaMaterials.AIR);

				setBlockMaterial(-3, yy, 0, current);
				setBlockMaterial(3, yy, 0, current);
				setBlockMaterial(0, yy, -3, current);
				setBlockMaterial(0, yy, 3, current);
			}
		}

		setBlockMaterial(0, -11, 0, VanillaMaterials.STONE_PRESSURE_PLATE); // Install the trigger

		// Hide the loot
		placeObject(2, -11, 0, lootChest);
		placeObject(-2, -11, 0, lootChest);
		placeObject(0, -11, 2, lootChest);
		placeObject(0, -11, -2, lootChest);

		// Build the towers
		placeTower(-8, 1, -8);
		placeTower(-8, 1, 8);

		Quaternion backup = getRotation();
		Quaternion q = new Quaternion(90, new Vector3(0, 1, 0));
		//		q = q.rotate(90, 0, 0, 1);
		setRotation(q);
		offsetPosition(-10, 1, -10);
		setRotationPoint(new Vector3(0, 0, 0));
		glyph.draw();
	}

	private void placeTower(int x, int y, int z) {
		offsetPosition(x, y, z);
		ComponentPlanePart plane = new ComponentPlanePart(this);
		SimpleBlockMaterialPicker picker = new SimpleBlockMaterialPicker();
		picker.setInnerMaterial(VanillaMaterials.AIR);
		picker.setOuterMaterial(PRIMARY_BLOCK);
		plane.setMinMax(-2, 0, -2, 2, 0, 2);
		int width = 5, height = 9;
		for (int yy = 0; yy < height; yy++) {
			offsetPosition(0, 1, 0);
			plane.fill(picker, false);
		}

		picker.setInnerMaterial(PRIMARY_BLOCK);
		plane.setMinMax(-1, 1, -1, 1, 1, 1);
		plane.fill(picker, false);

		for (BlockFace face : BlockFaces.NESW) {
			int xx = face.getOffset().getFloorX();
			int yy = face.getOffset().getFloorY();
			int zz = face.getOffset().getFloorZ();
			setBlockMaterial(xx * 2, 0, zz * 2, VanillaMaterials.STAIRS_SANDSTONE);
		}

		offsetPosition(-x, -y - height, -z);
	}

	@Override
	public boolean canPlace() {
		return true;
	}

	@Override
	public List<StructureComponent> getNextComponents() {
		return Collections.emptyList();
	}

	@Override
	public BoundingBox getBoundingBox() {
		return new BoundingBox(new Vector3(-10, -14, -10), new Vector3(10, 10, 10));
	}
}
