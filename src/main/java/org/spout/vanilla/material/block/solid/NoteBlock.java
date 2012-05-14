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

import org.spout.api.entity.Entity;
import org.spout.api.event.player.PlayerInteractEvent.Action;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.block.BlockFace;
import org.spout.vanilla.controller.VanillaControllerTypes;
import org.spout.vanilla.controller.block.NoteBlockController;
import org.spout.vanilla.material.Fuel;
import org.spout.vanilla.material.block.Solid;
import org.spout.vanilla.util.Instrument;
import org.spout.vanilla.util.RedstoneUtil;
import org.spout.vanilla.util.VanillaPlayerUtil;

public class NoteBlock extends Solid implements Fuel {

	public final float BURN_TIME = 15.f;

	public NoteBlock(String name, int id) {
		super(name, id);
	}

	@Override
	public void loadProperties() {
		super.loadProperties();
		this.setController(VanillaControllerTypes.NOTEBLOCK);
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
	public NoteBlockController getController(Block block) {
		return (NoteBlockController) super.getController(block);
	}

	@Override
	public Instrument getInstrument() {
		return Instrument.BASSGUITAR;
	}

	@Override
	public void onInteractBy(Entity entity, Block block, Action type, BlockFace clickedFace) {
		super.onInteractBy(entity, block, type, clickedFace);
		if (type == Action.RIGHT_CLICK) {
			NoteBlockController controller = getController(block);
			controller.setNote(controller.getNote() + 1);
			controller.play();
		} else if (type == Action.LEFT_CLICK && VanillaPlayerUtil.isCreative(entity)) {
			getController(block).play();
		}
	}

	@Override
	public void onUpdate(Block block) {
		super.onUpdate(block);
		getController(block).setPowered(this.isReceivingPower(block));
	}

	@Override
	public boolean onPlacement(Block block, short data, BlockFace against, boolean isClickedBlock) {
		if (super.onPlacement(block, data, against, isClickedBlock)) {
			this.getController(block);
			return true;
		}
		return false;
	}

	@Override
	public float getFuelTime() {
		return BURN_TIME;
	}

	public boolean isReceivingPower(Block block) {
		return RedstoneUtil.isReceivingPower(block, false);
	}
}
