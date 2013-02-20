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
package org.spout.vanilla.material.block.component;

import org.spout.api.component.type.BlockComponent;
import org.spout.api.event.Cause;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.block.BlockFace;

import org.spout.vanilla.component.block.material.Jukebox;
import org.spout.vanilla.data.Instrument;
import org.spout.vanilla.data.MoveReaction;
import org.spout.vanilla.data.resources.VanillaMaterialModels;
import org.spout.vanilla.material.Fuel;
import org.spout.vanilla.material.VanillaMaterials;

public class JukeboxBlock extends ComponentMaterial implements Fuel {
	public final float BURN_TIME = 15;

	public JukeboxBlock(String name, int id) {
		super(name, id, Jukebox.class, VanillaMaterialModels.JUKEBOX);
		this.setHardness(2.0F).setResistance(10.0F);
	}

	@Override
	public boolean onDestroy(Block block, Cause<?> cause) {
		//Note: Eject logic not part of drops, because item is always ejected
		// Or shouldn't it? If it becomes part of drops, turn this into setPlaying(false).
		BlockComponent comp = block.getComponent();
		if (comp instanceof Jukebox) {
			((Jukebox) comp).eject();
		}
		return super.onDestroy(block, cause);
	}

	@Override
	public MoveReaction getMoveReaction(Block block) {
		return MoveReaction.DENY;
	}

	@Override
	public Instrument getInstrument() {
		return Instrument.BASS_GUITAR;
	}

	@Override
	public float getFuelTime() {
		return BURN_TIME;
	}

	@Override
	public boolean canSupport(BlockMaterial material, BlockFace face) {
		if (material.isMaterial(VanillaMaterials.FIRE)) {
			return face == BlockFace.TOP;
		}
		return true;
	}
}
