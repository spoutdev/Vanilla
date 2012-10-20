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
package org.spout.vanilla.material.block.solid;

import org.spout.api.material.BlockMaterial;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.source.DataSource;

import org.spout.vanilla.data.Instrument;
import org.spout.vanilla.data.effect.store.SoundEffects;
import org.spout.vanilla.data.tool.ToolType;
import org.spout.vanilla.material.Burnable;
import org.spout.vanilla.material.Fuel;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.Solid;

public class Plank extends Solid implements Fuel, Burnable {
	public static final Plank PLANK = new Plank("Oak Plank");
	public static final Plank PINE = new Plank("Pine Plank", WoodType.PINE, PLANK);
	public static final Plank BIRCH = new Plank("Birch Plank", WoodType.BIRCH, PLANK);
	public static final Plank JUNGLE = new Plank("Jungle Plank", WoodType.JUNGLE, PLANK);
	private final WoodType type;
	public final float BURN_TIME = 15;

	public Plank(String name) {
		super((short) 0x0003, name, 05);
		this.type = WoodType.OAK;
		this.setHardness(0.8F).setResistance(1.3F).setStepSound(SoundEffects.STEP_WOOD);
		this.addMiningType(ToolType.AXE);
	}

	public Plank(String name, WoodType type, Plank parent) {
		super(name, 05, type.getData(), parent);
		this.type = type;
		this.setHardness(0.8F).setResistance(1.3F).setStepSound(SoundEffects.STEP_WOOD);
		this.addMiningType(ToolType.AXE);
	}

	@Override
	public Instrument getInstrument() {
		return Instrument.BASSGUITAR;
	}

	@Override
	public boolean canSupport(BlockMaterial material, BlockFace face) {
		if (material.equals(VanillaMaterials.FIRE)) {
			return true;
		} else {
			return super.canSupport(material, face);
		}
	}

	public WoodType getType() {
		return type;
	}

	@Override
	public Plank getParentMaterial() {
		return (Plank) super.getParentMaterial();
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

	@Override
	public int getBurnPower() {
		return 5;
	}

	@Override
	public int getCombustChance() {
		return 20;
	}
}
