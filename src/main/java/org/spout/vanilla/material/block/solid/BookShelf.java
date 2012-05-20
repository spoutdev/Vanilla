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

import java.util.ArrayList;

import org.spout.api.entity.Entity;
import org.spout.api.geo.cuboid.Block;

import org.spout.vanilla.enchantment.Enchantments;
import org.spout.vanilla.inventory.VanillaItemStack;
import org.spout.vanilla.material.Fuel;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.Solid;
import org.spout.vanilla.material.item.tool.MiningTool;
import org.spout.vanilla.util.Instrument;

public class BookShelf extends Solid implements Fuel {
	public final float BURN_TIME = 15.f;

	public BookShelf(String name, int id) {
		super(name, id);
	}

	@Override
	public void initialize() {
		super.initialize();
		this.setHardness(1.5F).setResistance(2.5F).setOpacity((byte) 1);
	}

	@Override
	public float getFuelTime() {
		return BURN_TIME;
	}

	@Override
	public Instrument getInstrument() {
		return Instrument.BASSGUITAR;
	}

	@Override
	public boolean canBurn() {
		return true;
	}

	@Override
	public ArrayList<VanillaItemStack> getDrops(Block block) {
		ArrayList<VanillaItemStack> drops = new ArrayList<VanillaItemStack>();
		if (block.getSource() instanceof Entity) {
			VanillaItemStack held = (VanillaItemStack) ((Entity) block.getSource()).getInventory().getCurrentItem();
			if (held != null && held.getMaterial() instanceof MiningTool && held.hasEnchantment(Enchantments.SILK_TOUCH)) {
				drops.add(new VanillaItemStack(this, 1));
			} else {
				drops.add(new VanillaItemStack(VanillaMaterials.BOOK, 3));
			}
		}
		return drops;
	}
}
