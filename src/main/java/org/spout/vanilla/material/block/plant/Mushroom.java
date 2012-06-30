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
package org.spout.vanilla.material.block.plant;

import java.util.ArrayList;

import org.spout.api.entity.Entity;
import org.spout.api.event.player.PlayerInteractEvent;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.inventory.ItemStack;
import org.spout.api.inventory.special.InventorySlot;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.block.BlockFace;

import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.Plant;
import org.spout.vanilla.material.block.attachable.GroundAttachable;
import org.spout.vanilla.material.item.misc.Dye;
import org.spout.vanilla.material.item.tool.Tool;
import org.spout.vanilla.material.item.weapon.Sword;
import org.spout.vanilla.util.VanillaPlayerUtil;
import org.spout.vanilla.world.generator.normal.object.largeplant.HugeMushroomObject;
import org.spout.vanilla.world.generator.normal.object.largeplant.HugeMushroomObject.HugeMushroomType;
import org.spout.vanilla.world.generator.object.LargePlantObject;

public class Mushroom extends GroundAttachable implements Plant {
	public Mushroom(String name, int id) {
		super(name, id);
		this.setHardness(0.0F).setResistance(0.0F).setOpacity((byte) 0);
	}

	@Override
	public void onInteractBy(Entity entity, Block block, PlayerInteractEvent.Action type, BlockFace clickedFace) {
		super.onInteractBy(entity, block, type, clickedFace);
		if (type != PlayerInteractEvent.Action.RIGHT_CLICK) {
			return;
		}
		InventorySlot inv = VanillaPlayerUtil.getCurrentSlot(entity);
		ItemStack current = inv.getItem();
		if (current != null && current.getSubMaterial().equals(Dye.BONE_MEAL)) {
			if (VanillaPlayerUtil.isSurvival(entity)) {
				inv.addItemAmount(0, -1);
			}
			final BlockMaterial mushroomType = block.getMaterial();
			final LargePlantObject mushroom;
			if (mushroomType == VanillaMaterials.RED_MUSHROOM) {
				mushroom = new HugeMushroomObject(HugeMushroomType.RED);
			} else {
				mushroom = new HugeMushroomObject(HugeMushroomType.BROWN);
			}
			final World world = block.getWorld();
			final int x = block.getX();
			final int y = block.getY();
			final int z = block.getZ();
			if (mushroom.canPlaceObject(world, x, y, z)) {
				mushroom.placeObject(world, x, y, z);
			}
		}
	}

	@Override
	public boolean hasGrowthStages() {
		return false;
	}

	@Override
	public int getNumGrowthStages() {
		return 0;
	}

	@Override
	public int getMinimumLightToGrow() {
		return 8;
	}

	@Override
	public boolean isValidPosition(Block block, BlockFace attachedFace, boolean seekAlternative) {
		if (super.isValidPosition(block, attachedFace, seekAlternative)) {
			return block.getLight() <= 12 && block.getSkyLight() <= 12;
		}
		return false;
	}

	@Override
	public ArrayList<ItemStack> getDrops(Block block) {
		ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
		drops.add(new ItemStack(this, 1));
		return drops;
	}

	@Override
	public short getDurabilityPenalty(Tool tool) {
		return tool instanceof Sword ? (short) 2 : (short) 1;
	}
}
