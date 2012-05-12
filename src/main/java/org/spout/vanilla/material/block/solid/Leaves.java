/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, SpoutDev <http://www.spout.org/>
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
package org.spout.vanilla.material.block.solid;

import java.util.Random;

import org.spout.api.entity.Entity;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.inventory.ItemStack;
import org.spout.api.material.Material;

import org.spout.vanilla.controller.object.moving.Item;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.Solid;
import org.spout.vanilla.util.VanillaPlayerUtil;

public class Leaves extends Solid {
	public static final Leaves DEFAULT = register(new Leaves("Leaves"));
	public static final Leaves SPRUCE = register(new Leaves("Spruce Leaves", 0, DEFAULT));
	public static final Leaves BIRCH = register(new Leaves("Birch Leaves", 0, DEFAULT));
	public static final Leaves JUNGLE = register(new Leaves("Jungle Leaves", 0, DEFAULT));
	private Random rand = new Random();

	private Leaves(String name) {
		super(name, 18);
	}

	private Leaves(String name, int data, Leaves parent) {
		super(name, 18, data, parent);
	}

	@Override
	public void loadProperties() {
		super.loadProperties();
		this.setHardness(0.2F).setResistance(0.3F).setOpacity((byte) 0);
	}

	@Override
	public void onDestroySpawnDrops(Block block) {
		Material dropMat = getDrop();
		if (block.getSource() instanceof Entity) {
			Entity entity = (Entity) block.getSource();
			if (VanillaPlayerUtil.isCreative(entity)) {
				return;
			}
			ItemStack current = entity.getInventory().getCurrentItem();
			if (current != null && current.getMaterial().equals(VanillaMaterials.SHEARS)) {
				dropMat = block.getSubMaterial();
			}
		}

		if (dropMat != null) {
			int count = this.getDropCount();
			for (int i = 0; i < count && dropMat.getId() != 0; ++i) {
				block.getWorld().createAndSpawnEntity(block.getPosition(), new Item(new ItemStack(dropMat, 1), block.getPosition().normalize().add(0, 5, 0)));
			}
		}
	}

	@Override
	public Material getDrop() {
		if (rand.nextInt(20) == 0) {
			return VanillaMaterials.SAPLING;
		} else if (rand.nextInt(200) == 0) {
			return VanillaMaterials.RED_APPLE;
		}
		return VanillaMaterials.AIR;
	}

	@Override
	public boolean canBurn() {
		return true;
	}

	// TODO: Decay
}
