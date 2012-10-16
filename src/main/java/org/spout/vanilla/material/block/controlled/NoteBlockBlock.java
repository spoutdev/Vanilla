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
package org.spout.vanilla.material.block.controlled;

import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.BlockMaterial;

import org.spout.vanilla.component.substance.material.NoteBlock;
import org.spout.vanilla.data.Instrument;
import org.spout.vanilla.data.MoveReaction;
import org.spout.vanilla.material.Fuel;
import org.spout.vanilla.material.block.Solid;
import org.spout.vanilla.util.RedstoneUtil;

public class NoteBlockBlock extends Solid implements Fuel {
	public final float BURN_TIME = 15.f;

	public NoteBlockBlock(String name, int id) {
		super(name, id);
		this.setHardness(0.8F).setResistance(1.3F);
	}

	@Override
	public boolean hasPhysics() {
		return true;
	}

	@Override
	public boolean isPlacementSuppressed() {
		return true;
	}

	@Override
	public MoveReaction getMoveReaction(Block block) {
		return MoveReaction.DENY;
	}

	@Override
	public Instrument getInstrument() {
		return Instrument.BASSGUITAR;
	}

	@Override
	public void onUpdate(BlockMaterial oldMaterial, Block block) {
		super.onUpdate(oldMaterial, block);
		NoteBlock note = (NoteBlock) block.getComponent();
		note.setPowered(isReceivingPower(block));
	}

	@Override
	public float getFuelTime() {
		return BURN_TIME;
	}

	public boolean isReceivingPower(Block block) {
		return RedstoneUtil.isReceivingPower(block);
	}
}
