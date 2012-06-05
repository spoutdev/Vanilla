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
package org.spout.vanilla.controller.block;

import org.spout.api.material.BlockMaterial;
import org.spout.api.material.block.BlockFace;

import org.spout.vanilla.controller.VanillaBlockController;
import org.spout.vanilla.controller.VanillaControllerTypes;
import org.spout.vanilla.material.VanillaBlockMaterial;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.protocol.VanillaNetworkSynchronizer;
import org.spout.vanilla.util.Instrument;

public class NoteBlock extends VanillaBlockController {
	private int note = 0;
	private boolean powered = false;

	public NoteBlock() {
		super(VanillaControllerTypes.NOTE_BLOCK, VanillaMaterials.NOTEBLOCK);
	}

	public Instrument getInstrument() {
		BlockMaterial below = this.getBlock().translate(BlockFace.BOTTOM).getMaterial();
		return below instanceof VanillaBlockMaterial ? ((VanillaBlockMaterial) below).getInstrument() : Instrument.PIANO;
	}

	public void setPowered(boolean powered) {
		if (this.powered != powered) {
			this.powered = powered;
			if (powered) {
				this.play();
			}
		}
	}

	public boolean isPowered() {
		return this.powered;
	}

	public int getNote() {
		return this.note;
	}

	public void setNote(int note) {
		this.note = note % 25;
	}

	public void play() {
		VanillaNetworkSynchronizer.playBlockAction(this.getBlock(), this.getInstrument().getId(), (byte) this.getNote());
	}

	@Override
	public void onTick(float dt) {
	}

	@Override
	public void onAttached() {
	}
}
