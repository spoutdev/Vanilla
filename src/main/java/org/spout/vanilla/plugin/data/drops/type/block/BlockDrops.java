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
package org.spout.vanilla.plugin.data.drops.type.block;

import org.spout.vanilla.plugin.data.drops.Drops;
import org.spout.vanilla.plugin.data.drops.flag.PlayerFlags;
import org.spout.vanilla.plugin.data.drops.flag.ToolEnchantFlags;

public class BlockDrops extends Drops {
	/**
	 * All the drops spawned when the no creative player is involved
	 */
	public final Drops NOT_CREATIVE;
	/**
	 * All the drops when broken using a tool enchanted with silk touch
	 */
	public final Drops SILK_TOUCH;
	/**
	 * All the drops when not broken using a tool enchanted with silk touch
	 */
	public final Drops DEFAULT;

	public BlockDrops() {
		this.NOT_CREATIVE = this.forFlags(PlayerFlags.CREATIVE.NOT);
		this.SILK_TOUCH = this.NOT_CREATIVE.forFlags(ToolEnchantFlags.SILK_TOUCH);
		this.DEFAULT = this.NOT_CREATIVE.forFlags(ToolEnchantFlags.SILK_TOUCH.NOT);
	}

	@Override
	public BlockDrops clear() {
		super.clear();
		add(NOT_CREATIVE).clear();
		NOT_CREATIVE.add(SILK_TOUCH).clear();
		NOT_CREATIVE.add(DEFAULT).clear();
		return this;
	}
}
