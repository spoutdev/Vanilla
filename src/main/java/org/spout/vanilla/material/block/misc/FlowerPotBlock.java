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
package org.spout.vanilla.material.block.misc;

import org.spout.api.entity.Entity;
import org.spout.api.event.player.PlayerInteractEvent.Action;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.Material;
import org.spout.api.material.block.BlockFace;

import org.spout.vanilla.component.inventory.PlayerInventory;
import org.spout.vanilla.component.living.Human;
import org.spout.vanilla.inventory.player.PlayerQuickbar;
import org.spout.vanilla.material.InitializableMaterial;
import org.spout.vanilla.material.VanillaBlockMaterial;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.plant.Sapling;

public class FlowerPotBlock extends VanillaBlockMaterial implements InitializableMaterial {
	public static final FlowerPotBlock EMPTY = new FlowerPotBlock("Empty Flower Pot");
	public static final FlowerPotBlock ROSE = new FlowerPotBlock("Rose Flower Pot", 1);
	public static final FlowerPotBlock DANDELION = new FlowerPotBlock("Dandelion Flower Pot", 2);
	public static final FlowerPotBlock OAK_SAPLING = new FlowerPotBlock("Oak Sapling Flower Pot", 3);
	public static final FlowerPotBlock SPRUCE_SAPLING = new FlowerPotBlock("Spruce Sapling Flower Pot", 4);
	public static final FlowerPotBlock BIRCH_SAPLING = new FlowerPotBlock("Birch Sapling Flower Pot", 5);
	public static final FlowerPotBlock JUNGLE_TREE_SAPLING = new FlowerPotBlock("Jungle Tree Sapling Flower Pot", 6);
	public static final FlowerPotBlock RED_MUSHROOM = new FlowerPotBlock("Red Mushroom Flower Pot", 7);
	public static final FlowerPotBlock BROWN_MUSHROOM = new FlowerPotBlock("Brown Mushroom Flower Pot", 8);
	public static final FlowerPotBlock CACTUS = new FlowerPotBlock("Cactus Flower Pot", 9);
	public static final FlowerPotBlock DEAD_BUSH = new FlowerPotBlock("Dead Bush Flower Pot", 10);
	public static final FlowerPotBlock FERN = new FlowerPotBlock("Fern Flower Pot", 11);

	private FlowerPotBlock(String name) {
		super((short) 0xF, name, 140, null);
		setHardness(0.0f).setResistance(0.0f).setTransparent();
	}

	private FlowerPotBlock(String name, int data) {
		super(name, 140, data, EMPTY, null);
		setHardness(0.0f).setResistance(0.0f).setTransparent();
	}

	public void onInteract(Entity entity, Block block, Action type, BlockFace clickedFace) {
		super.onInteract(entity, block, type, clickedFace);
		if (type != Action.RIGHT_CLICK || !entity.has(PlayerInventory.class)) {
			return;
		}
		PlayerQuickbar quickbar = entity.get(PlayerInventory.class).getQuickbar();
		Material held = quickbar.getCurrentItem().getMaterial();
		int data;
		if (held == VanillaMaterials.ROSE) {
			data = 1;
		} else if (held == VanillaMaterials.DANDELION) {
			data = 2;
		} else if (held == Sapling.DEFAULT) {
			data = 3;
		} else if (held == Sapling.SPRUCE) {
			data = 4;
		} else if (held == Sapling.BIRCH) {
			data = 5;
		} else if (held == Sapling.JUNGLE) {
			data = 6;
		} else if (held == VanillaMaterials.RED_MUSHROOM) {
			data = 7;
		} else if (held == VanillaMaterials.BROWN_MUSHROOM) {
			data = 8;
		} else if (held == VanillaMaterials.CACTUS) {
			data = 9;
		} else if (held == VanillaMaterials.DEAD_BUSH) {
			data = 10;
		} else if (held == VanillaMaterials.TALL_GRASS) {
			data = 11;
		} else {
			return;
		}
		block.setData(data);
		quickbar.addAmount(quickbar.getCurrentSlot(), -1);
	}

	@Override
	public void initialize() {
		getDrops().DEFAULT.clear();
		getDrops().DEFAULT.add(VanillaMaterials.FLOWER_POT);
	}
}
