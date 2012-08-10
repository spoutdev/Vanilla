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
package org.spout.vanilla.data.drops.flag;

import java.util.Set;

public class DropFlagSingle implements DropFlag {
	/**
	 * No drops should be spawned at all
	 */
	public static final DropFlagSingle NO_DROPS = new DropFlagSingle();

	private final boolean notState;
	/**
	 * The inverted version for this flag, the NOT<br><br>
	 * <b>Sending a NOT as flag to getDrops has no effect!</b>
	 */
	public final DropFlagSingle NOT;

	public DropFlagSingle() {
		this.NOT = new DropFlagSingle(this);
		this.notState = false;
	}

	private DropFlagSingle(DropFlagSingle parent) {
		this.NOT = parent;
		this.notState = true;
	}

	/**
	 * Gets whether the flag is inverted
	 * 
	 * @return True if the flag is inverted, False if not
	 */
	public boolean isNot() {
		return this.notState;
	}

	@Override
	public boolean evaluate(Set<DropFlagSingle> flags) {
		if (this.isNot()) {
			return !flags.contains(this.NOT);
		} else {
			return flags.contains(this);
		}
	}
}
