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
package org.spout.vanilla.event.cause;

import org.spout.api.event.Cause;
import org.spout.api.event.cause.BlockCause;
import org.spout.api.geo.cuboid.Block;

public class BlockDamageCause extends BlockCause implements DamageCause<Block> {
	private final DamageType type;

	/**
	 * Contains the source and type of damage.
	 * @param block The block causing the damage
	 * @param type The cause of the damage.
	 */
	public BlockDamageCause(Block block, DamageType type) {
		super(block);
		this.type = type;
	}

	/**
	 * Contains the source and type of damage.
	 * @param parent cause of this cause
	 * @param block who caused this cause
	 * @param type who caused this cause
	 */
	public BlockDamageCause(Cause<?> parent, Block block, DamageType type) {
		super(parent, block);
		this.type = type;
	}

	@Override
	public DamageType getType() {
		return type;
	}
}
