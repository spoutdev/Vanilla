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

import org.spout.api.geo.cuboid.Block;
import org.spout.api.inventory.ItemStack;
import org.spout.api.material.source.DataSource;

import org.spout.vanilla.material.Fuel;
import org.spout.vanilla.material.Mineable;
import org.spout.vanilla.material.item.MiningTool;
import org.spout.vanilla.material.item.tool.Axe;
import org.spout.vanilla.util.Instrument;

public class Plank extends Solid implements Fuel, Mineable {
	public static final Plank PLANK = register(new Plank("Oak Plank"));
	public static final Plank PINE = register(new Plank("Pine Plank", WoodType.PINE, PLANK));
	public static final Plank BIRCH = register(new Plank("Birch Plank", WoodType.BIRCH, PLANK));
	public static final Plank JUNGLE = register(new Plank("Jungle Plank", WoodType.JUNGLE, PLANK));
	private final WoodType type;
	public final float BURN_TIME = 15.f;

	public Plank(String name) {
		super(name, 05);
		this.type = WoodType.OAK;
	}

	public Plank(String name, WoodType type, Plank parent) {
		super(name, 05, type.getData(), parent);
		this.type = type;
	}

	@Override
	public void initialize() {
		super.initialize();
		this.setHardness(0.8F).setResistance(1.3F);
	}

	@Override
	public Instrument getInstrument() {
		return Instrument.BASSGUITAR;
	}

	public WoodType getType() {
		return type;
	}

	@Override
	public short getData() {
		return type.getData();
	}

	@Override
	public Plank getParentMaterial() {
		return (Plank) super.getParentMaterial();
	}

	@Override
	public short getDurabilityPenalty(MiningTool tool) {
		return tool instanceof Axe ? (short) 1 : (short) 2;
	}

	@Override
	public ArrayList<ItemStack> getDrops(Block block) {
		ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
		drops.add(new ItemStack(this, 1));
		return drops;
	}

	public static enum WoodType implements DataSource {
		OAK(0),
		PINE(1),
		BIRCH(2),
		JUNGLE(3),;
		private final short data;

		private WoodType(int data) {
			this.data = (short) data;
		}

		@Override
		public short getData() {
			return this.data;
		}
	}

	@Override
	public float getFuelTime() {
		return BURN_TIME;
	}
}
