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
package org.spout.vanilla.plugin.world.generator.normal.structure.temple;

import java.util.Collections;
import java.util.List;

import org.spout.api.math.Vector3;

import org.spout.vanilla.plugin.material.VanillaMaterials;
import org.spout.vanilla.plugin.world.generator.normal.object.LootChestObject;
import org.spout.vanilla.plugin.world.generator.structure.ComponentCuboidPart;
import org.spout.vanilla.plugin.world.generator.structure.SimpleBlockMaterialPicker;
import org.spout.vanilla.plugin.world.generator.structure.Structure;
import org.spout.vanilla.plugin.world.generator.structure.StructureComponent;

public class JungleTemple extends StructureComponent {
	private final LootChestObject lootChest = new LootChestObject();

	public JungleTemple(Structure parent) {
		super(parent);
		//TODO Investigate how the materials are distributed
		lootChest.addMaterial(VanillaMaterials.IRON_BARS, 0.1, 1, 3);
	}

	@Override
	public void place() {
		// Building objects
		final ComponentCuboidPart box = new ComponentCuboidPart(this);
		final SimpleBlockMaterialPicker air = new SimpleBlockMaterialPicker();
		air.setOuterInnerMaterials(VanillaMaterials.AIR, VanillaMaterials.AIR);
		final JungleTempleBlockMaterialPicker cobble = new JungleTempleBlockMaterialPicker(getRandom());
		// Basic shape: a box with two floors on full box
		box.setPicker(cobble);
		box.setMinMax(0, -4, 0, 11, 0, 14);
		box.fill(false);
		box.setMinMax(2, 1, 2, 9, 2, 2);
		box.fill(false);
		box.offsetMinMax(0, 0, 10, 0, 0, 10);
		box.fill(false);
		box.offsetMinMax(0, 0, -9, -7, 0, -1);
		box.fill(false);
		box.offsetMinMax(7, 0, 0, 7, 0, 0);
		box.fill(false);
		box.setMinMax(1, 3, 1, 10, 6, 1);
		box.fill(false);
		box.offsetMinMax(0, 0, 12, 0, 0, 12);
		box.fill(false);
		box.offsetMinMax(0, 0, -11, -9, 0, -1);
		box.fill(false);
		box.offsetMinMax(9, 0, 0, 9, 0, 0);
		box.fill(false);
		box.offsetMinMax(-8, 0, 0, -1, -3, 0);
		box.fill(false);
		box.offsetMinMax(0, 3, 0, 0, 3, 0);
		box.fill(false);
		box.offsetMinMax(1, 1, 1, -1, 1, -1);
		box.fill(false);
		box.offsetMinMax(1, 1, 1, -1, 1, -1);
		box.fill(false);
		// Holes like windows and doorways
		box.setPicker(air);
		box.setMinMax(3, 1, 3, 8, 2, 11);
		box.fill(false);
		box.setMinMax(4, 3, 6, 7, 3, 9);
		box.fill(false);
		box.setMinMax(2, 4, 2, 9, 5, 12);
		box.fill(false);
		box.setMinMax(4, 6, 5, 7, 6, 9);
		box.fill(false);
		box.offsetMinMax(1, 1, 1, -1, 1, -1);
		box.fill(false);
		box.offsetMinMax(0, -6, -4, 0, -5, -6);
		box.fill(false);
		box.offsetMinMax(0, 1, 10, 0, 0, 10);
		box.fill(false);
		box.offsetMinMax(0, 3, -11, 0, 3, -11);
		box.fill(false);
		box.offsetMinMax(0, 0, 12, 0, 0, 12);
		box.fill(false);
		setBlockMaterial(1, 5, 5, VanillaMaterials.AIR);
		setBlockMaterial(10, 5, 5, VanillaMaterials.AIR);
		setBlockMaterial(1, 5, 9, VanillaMaterials.AIR);
		setBlockMaterial(10, 5, 9, VanillaMaterials.AIR);
		// Decoration on the exterior walls
		box.setPicker(cobble);
		for (int i = 0; i < 2; i++) {
			box.setMinMax(2, 4, i * 14, 2, 5, i * 14);
			box.fill(false);
			box.offsetMinMax(2, 0, 0, 2, 0, 0);
			box.fill(false);
			box.offsetMinMax(3, 0, 0, 3, 0, 0);
			box.fill(false);
			box.offsetMinMax(2, 0, 0, 2, 0, 0);
			box.fill(false);
		}
		box.setMinMax(5, 6, 0, 6, 6, 0);
		box.fill(false);
		for (int i = 0; i < 2; i++) {
			for (int ii = 2; ii <= 12; ii += 2) {
				box.setMinMax(i * 11, 4, ii, i * 11, 5, ii);
				box.fill(false);
			}
			box.setMinMax(i * 11, 6, 5, i * 11, 6, 5);
			box.fill(false);
			box.offsetMinMax(0, 0, 4, 0, 0, 4);
			box.fill(false);
		}
		// Rooftop decoration
		box.setMinMax(2, 7, 2, 2, 9, 2);
		box.fill(false);
		box.offsetMinMax(7, 0, 0, 7, 0, 0);
		box.fill(false);
		box.offsetMinMax(-7, 0, 10, -7, 0, 10);
		box.fill(false);
		box.offsetMinMax(7, 0, 0, 7, 0, 0);
		box.fill(false);
		box.setMinMax(4, 9, 4, 4, 9, 4);
		box.fill(false);
		box.offsetMinMax(3, 0, 0, 3, 0, 0);
		box.fill(false);
		box.offsetMinMax(-3, 0, 6, -3, 0, 6);
		box.fill(false);
		box.offsetMinMax(3, 0, 0, 3, 0, 0);
		box.fill(false);
		box.setMinMax(5, 9, 7, 6, 9, 7);
		box.fill(false);
		// Place ALL the stairs!
		setBlockMaterial(5, 9, 6, VanillaMaterials.STAIRS_COBBLESTONE, (short) 2);
		setBlockMaterial(6, 9, 6, VanillaMaterials.STAIRS_COBBLESTONE, (short) 2);
		setBlockMaterial(5, 9, 8, VanillaMaterials.STAIRS_COBBLESTONE, (short) 3);
		setBlockMaterial(6, 9, 8, VanillaMaterials.STAIRS_COBBLESTONE, (short) 3);
		setBlockMaterial(4, 0, 0, VanillaMaterials.STAIRS_COBBLESTONE, (short) 2);
		setBlockMaterial(5, 0, 0, VanillaMaterials.STAIRS_COBBLESTONE, (short) 2);
		setBlockMaterial(6, 0, 0, VanillaMaterials.STAIRS_COBBLESTONE, (short) 2);
		setBlockMaterial(7, 0, 0, VanillaMaterials.STAIRS_COBBLESTONE, (short) 2);
		setBlockMaterial(4, 1, 8, VanillaMaterials.STAIRS_COBBLESTONE, (short) 2);
		setBlockMaterial(4, 2, 9, VanillaMaterials.STAIRS_COBBLESTONE, (short) 2);
		setBlockMaterial(4, 3, 10, VanillaMaterials.STAIRS_COBBLESTONE, (short) 2);
		setBlockMaterial(7, 1, 8, VanillaMaterials.STAIRS_COBBLESTONE, (short) 2);
		setBlockMaterial(7, 2, 9, VanillaMaterials.STAIRS_COBBLESTONE, (short) 2);
		setBlockMaterial(7, 3, 10, VanillaMaterials.STAIRS_COBBLESTONE, (short) 2);
		setBlockMaterial(4, 4, 5, VanillaMaterials.STAIRS_COBBLESTONE, (short) 0);
		setBlockMaterial(7, 4, 5, VanillaMaterials.STAIRS_COBBLESTONE, (short) 1);
		// Fill in some blocks for the stairs
		box.setMinMax(4, 1, 9, 4, 1, 9);
		box.fill(false);
		box.offsetMinMax(3, 0, 0, 3, 0, 0);
		box.fill(false);
		box.setMinMax(4, 1, 10, 7, 2, 10);
		box.fill(false);
		box.setMinMax(5, 4, 5, 6, 4, 5);
		box.fill(false);
		// Stair case to the basement
		box.setPicker(air);
		for (int i = 0; i < 4; i++) {
			setBlockMaterial(5, -i, 6 + i, VanillaMaterials.STAIRS_COBBLESTONE, (short) 3);
			setBlockMaterial(6, -i, 6 + i, VanillaMaterials.STAIRS_COBBLESTONE, (short) 3);
			box.setMinMax(5, -i, 7 + i, 6, -i, 9 + i);
			box.fill(false);
		}
		// Make some room in the basement
		box.setMinMax(1, -3, 12, 10, -1, 13);
		box.fill(false);
		box.offsetMinMax(0, 0, -11, -7, 0, 0);
		box.fill(false);
		box.offsetMinMax(0, 0, 0, 6, 0, -8);
		box.fill(false);
		// Decorate the ceiling of the basement
		box.setPicker(cobble);
		for (int i = 1; i <= 13; i += 2) {
			box.setMinMax(1, -3, i, 1, -2, i);
			box.fill(false);
		}
		for (int i = 2; i <= 12; i += 2) {
			box.setMinMax(1, -1, i, 3, -1, i);
			box.fill(false);
		}
		// Decoration for the back wall of the basement
		box.setMinMax(2, -2, 1, 5, -2, 1);
		box.fill(false);
		box.offsetMinMax(5, 0, 0, 4, 0, 0);
		box.fill(false);
		box.offsetMinMax(-1, -1, 0, -3, -1, 0);
		box.fill(false);
		box.offsetMinMax(0, 2, 0, 0, 2, 0);
		box.fill(false);
		// Setup a tripwire and arrow dispenser trap
		setBlockMaterial(1, -3, 8, VanillaMaterials.TRIPWIRE_HOOK, (short) 7);
		setBlockMaterial(4, -3, 8, VanillaMaterials.TRIPWIRE_HOOK, (short) 5);
		setBlockMaterial(2, -3, 8, VanillaMaterials.TRIPWIRE, (short) 4);
		setBlockMaterial(3, -3, 8, VanillaMaterials.TRIPWIRE, (short) 4);
		setBlockMaterial(5, -3, 7, VanillaMaterials.REDSTONE_WIRE);
		setBlockMaterial(5, -3, 6, VanillaMaterials.REDSTONE_WIRE);
		setBlockMaterial(5, -3, 5, VanillaMaterials.REDSTONE_WIRE);
		setBlockMaterial(5, -3, 4, VanillaMaterials.REDSTONE_WIRE);
		setBlockMaterial(5, -3, 3, VanillaMaterials.REDSTONE_WIRE);
		setBlockMaterial(5, -3, 2, VanillaMaterials.REDSTONE_WIRE);
		setBlockMaterial(5, -3, 1, VanillaMaterials.REDSTONE_WIRE);
		setBlockMaterial(4, -3, 1, VanillaMaterials.REDSTONE_WIRE);
		setBlockMaterial(3, -3, 1, VanillaMaterials.MOSS_STONE);
		// TODO: fill the dispenser
		setBlockMaterial(3, -2, 1, VanillaMaterials.DISPENSER, (short) 3);
		setBlockMaterial(3, -2, 2, VanillaMaterials.VINES, (short) 15);
		// Another tripwire and arrow dispenser trap
		setBlockMaterial(7, -3, 1, VanillaMaterials.TRIPWIRE_HOOK, (short) 4);
		setBlockMaterial(7, -3, 5, VanillaMaterials.TRIPWIRE_HOOK, (short) 6);
		setBlockMaterial(7, -3, 2, VanillaMaterials.TRIPWIRE, (short) 4);
		setBlockMaterial(7, -3, 3, VanillaMaterials.TRIPWIRE, (short) 4);
		setBlockMaterial(7, -3, 4, VanillaMaterials.TRIPWIRE, (short) 4);
		setBlockMaterial(8, -3, 6, VanillaMaterials.REDSTONE_WIRE);
		setBlockMaterial(9, -3, 6, VanillaMaterials.REDSTONE_WIRE);
		setBlockMaterial(9, -3, 5, VanillaMaterials.REDSTONE_WIRE);
		setBlockMaterial(9, -3, 4, VanillaMaterials.MOSS_STONE);
		setBlockMaterial(9, -2, 4, VanillaMaterials.REDSTONE_WIRE);
		// TODO: fill the dispenser
		setBlockMaterial(9, -2, 3, VanillaMaterials.DISPENSER, (short) 4);
		setBlockMaterial(8, -1, 3, VanillaMaterials.VINES, (short) 15);
		setBlockMaterial(8, -2, 3, VanillaMaterials.VINES, (short) 15);
		// Place the loot chest
		lootChest.setRandom(getRandom());
		placeObject(8, -3, 3, lootChest);
		// Setup the lever puzzle
		setBlockMaterial(9, -3, 2, VanillaMaterials.MOSS_STONE);
		setBlockMaterial(8, -3, 1, VanillaMaterials.MOSS_STONE);
		setBlockMaterial(4, -3, 5, VanillaMaterials.MOSS_STONE);
		setBlockMaterial(5, -2, 5, VanillaMaterials.MOSS_STONE);
		setBlockMaterial(5, -1, 5, VanillaMaterials.MOSS_STONE);
		setBlockMaterial(6, -3, 5, VanillaMaterials.MOSS_STONE);
		setBlockMaterial(7, -2, 5, VanillaMaterials.MOSS_STONE);
		setBlockMaterial(7, -1, 5, VanillaMaterials.MOSS_STONE);
		setBlockMaterial(8, -3, 5, VanillaMaterials.MOSS_STONE);
		box.setMinMax(9, -1, 1, 9, -1, 5);
		box.fill(false);
		box.setPicker(air);
		box.setMinMax(8, -3, 8, 10, -1, 10);
		box.fill(false);
		setBlockMaterial(8, -2, 11, VanillaMaterials.STONE_BRICK, (short) 3);
		setBlockMaterial(9, -2, 11, VanillaMaterials.STONE_BRICK, (short) 3);
		setBlockMaterial(10, -2, 11, VanillaMaterials.STONE_BRICK, (short) 3);
		setBlockMaterial(8, -2, 12, VanillaMaterials.LEVER, (short) 3);
		setBlockMaterial(9, -2, 12, VanillaMaterials.LEVER, (short) 3);
		setBlockMaterial(10, -2, 12, VanillaMaterials.LEVER, (short) 3);
		box.setPicker(cobble);
		box.setMinMax(8, -3, 8, 8, -3, 10);
		box.fill(false);
		box.offsetMinMax(2, 0, 0, 2, 0, 0);
		box.fill(false);
		setBlockMaterial(10, -2, 9, VanillaMaterials.MOSS_STONE);
		setBlockMaterial(8, -2, 9, VanillaMaterials.REDSTONE_WIRE);
		setBlockMaterial(8, -2, 10, VanillaMaterials.REDSTONE_WIRE);
		setBlockMaterial(10, -1, 9, VanillaMaterials.REDSTONE_WIRE);
		setBlockMaterial(9, -2, 8, VanillaMaterials.PISTON_STICKY_BASE, (short) 1);
		setBlockMaterial(10, -2, 8, VanillaMaterials.PISTON_STICKY_BASE, (short) 4);
		setBlockMaterial(10, -1, 8, VanillaMaterials.PISTON_STICKY_BASE, (short) 4);
		setBlockMaterial(10, -2, 10, VanillaMaterials.REDSTONE_REPEATER_OFF, (short) 0);
		// Place another loot chest
		lootChest.setRandom(getRandom());
		placeObject(9, -3, 10, lootChest);
	}

	@Override
	public boolean canPlace() {
		return true;
	}

	@Override
	public void randomize() {
	}

	@Override
	public List<StructureComponent> getNextComponents() {
		return Collections.emptyList();
	}

	@Override
	public BoundingBox getBoundingBox() {
		final Vector3 rotatedMin = transform(0, 0, 0);
		final Vector3 rotatedMax = transform(11, 9, 14);
		return new BoundingBox(Vector3.min(rotatedMin, rotatedMax), Vector3.max(rotatedMin, rotatedMax));
	}
}
