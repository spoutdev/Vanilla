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

import org.spout.api.math.Vector3;

import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.solid.Sandstone;
import org.spout.vanilla.material.block.solid.Wool;
import org.spout.vanilla.world.generator.normal.object.LootChestObject;
import org.spout.vanilla.world.generator.structure.ComponentCuboidPart;
import org.spout.vanilla.world.generator.structure.BlockMaterialLayout;
import org.spout.vanilla.world.generator.structure.ComponentPlanePart;
import org.spout.vanilla.world.generator.structure.SimpleBlockMaterialPicker;
import org.spout.vanilla.world.generator.structure.Structure;
import org.spout.vanilla.world.generator.structure.StructureComponent;

public class DesertTemple extends StructureComponent {
	private static final LootChestObject LOOT_CHEST = new LootChestObject();
	private static final BlockMaterialLayout CENTER_CROSS;
	private static final BlockMaterialLayout GLYPH;

	static {
		LOOT_CHEST.addMaterial(VanillaMaterials.IRON_BARS, 0.1, 1, 3); //TODO Investigate how the materials are distributed
		CENTER_CROSS = new BlockMaterialLayout("000o000\n000o000\n00o0o00\noo0b0oo\n00o0o00\n000o000\n000o000");
		CENTER_CROSS.setBlockMaterial('0', VanillaMaterials.SANDSTONE);
		CENTER_CROSS.setBlockMaterial('o', Wool.ORANGE_WOOL);
		CENTER_CROSS.setBlockMaterial('b', Wool.BLUE_WOOL);
		GLYPH = new BlockMaterialLayout("---\nooo\no=o\n-o-\no=o\n-o-\n-o-");
		GLYPH.setBlockMaterial('-', Sandstone.SMOOTH);
		GLYPH.setBlockMaterial('o', Wool.ORANGE_WOOL);
		GLYPH.setBlockMaterial('=', Sandstone.DECORATIVE);
	}

	public DesertTemple(Structure parent) {
		super(parent);
	}

	@Override
	public void randomize() {
	}

	@Override
	public void place() {
		// building objects
		final ComponentCuboidPart box = new ComponentCuboidPart(this);
		final ComponentPlanePart plane = new ComponentPlanePart(this);
		final SimpleBlockMaterialPicker picker = new SimpleBlockMaterialPicker();
		box.setPicker(picker);
		plane.setPicker(picker);
		// size x, y, z = 21, 15, 21
		// foundations
		picker.setOuterInnerMaterials(VanillaMaterials.SANDSTONE, VanillaMaterials.SANDSTONE);
		box.setMinMax(0, -4, 0, 20, 0, 20);
		box.fill(false);
		// pyramid
		picker.setOuterInnerMaterials(VanillaMaterials.SANDSTONE, VanillaMaterials.AIR);
		for (byte yy = 1; yy < 10; yy++) {
			plane.setMinMax(yy, yy, yy, 20 - yy, yy, 20 - yy);
			plane.fill(false);
		}
		// fill the land under
		for (byte xx = 0; xx < 21; xx++) {
			for (byte zz = 0; zz < 21; zz++) {
				fillDownwards(xx, -5, zz, VanillaMaterials.SANDSTONE);
			}
		}
		// first tower body
		picker.setOuterInnerMaterials(VanillaMaterials.SANDSTONE, VanillaMaterials.AIR);
		box.setMinMax(0, 0, 0, 4, 9, 4);
		box.fill(false);
		picker.setInnerMaterial(VanillaMaterials.SANDSTONE);
		box.setMinMax(1, 10, 1, 3, 10, 3);
		box.fill(false);
		setBlockMaterial(2, 10, 0, VanillaMaterials.STAIRS_SANDSTONE, (short) 2);
		setBlockMaterial(2, 10, 4, VanillaMaterials.STAIRS_SANDSTONE, (short) 3);
		setBlockMaterial(0, 10, 2, VanillaMaterials.STAIRS_SANDSTONE, (short) 0);
		setBlockMaterial(4, 10, 2, VanillaMaterials.STAIRS_SANDSTONE, (short) 1);
		// second tower body
		picker.setOuterInnerMaterials(VanillaMaterials.SANDSTONE, VanillaMaterials.AIR);
		box.setMinMax(16, 0, 0, 20, 9, 4);
		box.fill(false);
		picker.setInnerMaterial(VanillaMaterials.SANDSTONE);
		box.setMinMax(17, 10, 1, 19, 10, 3);
		box.fill(false);
		setBlockMaterial(18, 10, 0, VanillaMaterials.STAIRS_SANDSTONE, (short) 2);
		setBlockMaterial(18, 10, 4, VanillaMaterials.STAIRS_SANDSTONE, (short) 3);
		setBlockMaterial(16, 10, 2, VanillaMaterials.STAIRS_SANDSTONE, (short) 0);
		setBlockMaterial(20, 10, 2, VanillaMaterials.STAIRS_SANDSTONE, (short) 1);
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
