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
package org.spout.vanilla.plugin.material.block.misc;

import org.spout.api.entity.Entity;
import org.spout.api.event.player.PlayerInteractEvent.Action;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.Material;
import org.spout.api.material.block.BlockFace;

import org.spout.api.inventory.Slot;
import org.spout.vanilla.api.material.InitializableMaterial;

import org.spout.vanilla.plugin.material.VanillaMaterials;
import org.spout.vanilla.plugin.material.block.attachable.GroundAttachable;
import org.spout.vanilla.plugin.material.block.plant.Sapling;
import org.spout.vanilla.plugin.resources.VanillaMaterialModels;
import org.spout.vanilla.plugin.util.PlayerUtil;

public class FlowerPotBlock extends GroundAttachable implements InitializableMaterial {
	public static final FlowerPotBlock EMPTY = new FlowerPotBlock("Empty Flower Pot", VanillaMaterialModels.FLOWER_POT_EMPTY);
	public static final FlowerPotBlock ROSE = new FlowerPotBlock("Rose Flower Pot", 1, VanillaMaterials.ROSE, VanillaMaterialModels.FLOWER_POT_ROSE);
	public static final FlowerPotBlock DANDELION = new FlowerPotBlock("Dandelion Flower Pot", 2, VanillaMaterials.DANDELION, VanillaMaterialModels.FLOWER_POT_DANDELION);
	public static final FlowerPotBlock OAK_SAPLING = new FlowerPotBlock("Oak Sapling Flower Pot", 3, Sapling.DEFAULT, VanillaMaterialModels.FLOWER_POT_OAK_SAPLING);
	public static final FlowerPotBlock SPRUCE_SAPLING = new FlowerPotBlock("Spruce Sapling Flower Pot", 4, Sapling.SPRUCE, VanillaMaterialModels.FLOWER_POT_SPRUCE_SAPLING);
	public static final FlowerPotBlock BIRCH_SAPLING = new FlowerPotBlock("Birch Sapling Flower Pot", 5, Sapling.BIRCH, VanillaMaterialModels.FLOWER_POT_BIRCH_SAPLING);
	public static final FlowerPotBlock JUNGLE_SAPLING = new FlowerPotBlock("Jungle Tree Sapling Flower Pot", 6, Sapling.JUNGLE, VanillaMaterialModels.FLOWER_POT_JUNGLE_SAPLING);
	public static final FlowerPotBlock RED_MUSHROOM = new FlowerPotBlock("Red Mushroom Flower Pot", 7, VanillaMaterials.RED_MUSHROOM, VanillaMaterialModels.FLOWER_POT_RED_MUSHROOM);
	public static final FlowerPotBlock BROWN_MUSHROOM = new FlowerPotBlock("Brown Mushroom Flower Pot", 8, VanillaMaterials.BROWN_MUSHROOM, VanillaMaterialModels.FLOWER_POT_BROWN_MUSHROOM);
	public static final FlowerPotBlock CACTUS = new FlowerPotBlock("Cactus Flower Pot", 9, VanillaMaterials.CACTUS, VanillaMaterialModels.FLOWER_POT_CACTUS);
	public static final FlowerPotBlock DEAD_BUSH = new FlowerPotBlock("Dead Bush Flower Pot", 10, VanillaMaterials.DEAD_BUSH, VanillaMaterialModels.FLOWER_POT_DEAD_BUSH);
	public static final FlowerPotBlock FERN = new FlowerPotBlock("Fern Flower Pot", 11, VanillaMaterials.FERN, VanillaMaterialModels.FLOWER_POT_FERN);
	private final BlockMaterial flowerMaterial;

	private FlowerPotBlock(String name, String model) {
		super((short) 0xF, name, 140, model);
		setHardness(0.0f).setResistance(0.0f).setTransparent();
		this.flowerMaterial = null;
	}

	private FlowerPotBlock(String name, int data, BlockMaterial flowerMaterial, String model) {
		super(name, 140, data, EMPTY, model);
		setHardness(0.0f).setResistance(0.0f).setTransparent();
		this.flowerMaterial = flowerMaterial;
	}

	/**
	 * Obtains the Flower Pot Block material for the flower material specified
	 * @param flowerMaterial to get the flower pot for
	 * @return The flower pot block of the flower, or null if none is found
	 */
	public FlowerPotBlock getForFlower(Material flowerMaterial) {
		for (Material material : EMPTY.getSubMaterials()) {
			if (material instanceof FlowerPotBlock && flowerMaterial.isMaterial(((FlowerPotBlock) material).flowerMaterial)) {
				return (FlowerPotBlock) material;
			}
		}
		return null;
	}

	@Override
	public void onInteractBy(Entity entity, Block block, Action type, BlockFace clickedFace) {
		super.onInteract(entity, block, type, clickedFace);
		if (type != Action.RIGHT_CLICK) {
			return;
		}
		Slot selected = PlayerUtil.getHeldSlot(entity);
		if (selected == null || selected.get() == null) {
			return;
		}
		FlowerPotBlock material = getForFlower(selected.get().getMaterial());
		if (material == null) {
			return;
		}
		block.setMaterial(material);
		if (!PlayerUtil.isCostSuppressed(entity)) {
			selected.addAmount(-1);
		}
	}

	@Override
	public void initialize() {
		getDrops().DEFAULT.clear();
		getDrops().DEFAULT.add(VanillaMaterials.FLOWER_POT);
	}
}
