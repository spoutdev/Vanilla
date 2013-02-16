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
package org.spout.vanilla.material.block.solid;

import org.spout.api.material.BlockMaterial;
import org.spout.api.material.block.BlockFace;

import org.spout.vanilla.data.Instrument;
import org.spout.vanilla.data.resources.VanillaMaterialModels;
import org.spout.vanilla.data.tool.ToolLevel;
import org.spout.vanilla.data.tool.ToolType;
import org.spout.vanilla.material.Burnable;
import org.spout.vanilla.material.Fuel;
import org.spout.vanilla.material.InitializableMaterial;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.Solid;

public class BookShelf extends Solid implements Fuel, InitializableMaterial, Burnable {
	public final float BURN_TIME = 15;

	public BookShelf(String name, int id) {
		super(name, id, VanillaMaterialModels.BOOK_SHELF);
		this.setHardness(1.5F).setResistance(2.5F).addMiningType(ToolType.AXE).setMiningLevel(ToolLevel.WOOD);
	}

	@Override
	public void initialize() {
		this.getDrops().DEFAULT.clear().add(VanillaMaterials.BOOK, 3);
	}

	@Override
	public float getFuelTime() {
		return BURN_TIME;
	}

	@Override
	public int getCombustChance() {
		return 20;
	}

	@Override
	public int getBurnPower() {
		return 30;
	}

	@Override
	public Instrument getInstrument() {
		return Instrument.BASS_GUITAR;
	}

	@Override
	public boolean canSupport(BlockMaterial material, BlockFace face) {
		if (material.equals(VanillaMaterials.FIRE)) {
			return true;
		} else {
			return super.canSupport(material, face);
		}
	}
}
