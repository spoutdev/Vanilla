/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011 Spout LLC <http://www.spout.org/>
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
package org.spout.vanilla.data.drops;

import java.util.List;
import java.util.Random;
import java.util.Set;

import org.spout.api.inventory.ItemStack;
import org.spout.api.util.flag.Flag;

/**
 * If all flags match, it drops the first segment, else the other<br> If a chance is set, this Chance also defines what mode is used
 */
public class SwitchDrops extends Drops {
	public final Drops TRUE = new Drops();
	public final Drops FALSE = new Drops();

	@Override
	public List<ItemStack> getDrops(Random random, Set<Flag> flags, List<ItemStack> drops) {
		if (this.canDrop(random, flags)) {
			drops = this.TRUE.getDrops(random, flags, drops);
		} else {
			drops = this.FALSE.getDrops(random, flags, drops);
		}
		return drops;
	}

	@Override
	public SwitchDrops addFlags(Flag... dropFlags) {
		super.addFlags(dropFlags);
		return this;
	}

	@Override
	public SwitchDrops setChance(double chance) {
		super.setChance(chance);
		return this;
	}
}
