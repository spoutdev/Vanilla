/*
 * This file is part of Vanilla (http://www.spout.org/).
 *
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
package org.spout.vanilla.util.explosion;

import java.util.HashSet;
import java.util.Set;

/**
 * Contains live information of a single element in the chain of the explosion layers
 */
public class ExplosionSlot {
	public ExplosionSlot(final ExplosionBlockSlot block) {
		this.block = block;
	}
	public final ExplosionBlockSlot block;
	private Set<ExplosionSlot> nextSet = new HashSet<ExplosionSlot>();
	public ExplosionSlot[] next;
	public float sourcedamage = 0;
	
	/**
	 * Adds the next linked block slot to the chain
	 * @param next slot to add
	 */
	public void addNext(ExplosionSlot next) {
		this.nextSet.add(next);
	}
	
	/**
	 * Transfers all next slots previously added to the next array
	 * After this is called you can no longer add new 'next' slots
	 */
	public void finish() {
		this.next = this.nextSet.toArray(new ExplosionSlot[0]);
		this.nextSet = null;
	}
}
