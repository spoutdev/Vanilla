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

import java.util.HashMap;
import java.util.Map;

import org.spout.api.math.MathHelper;
import org.spout.api.math.Vector3;

public class ExplosionLayer {

	public ExplosionLayer(final ExplosionModelSpherical model) {
		this.model = model;
		this.index = 0;
		this.slotArray = new ExplosionSlot[] {this.createSlot(0.0, 0.0, 0.0)};
	}
	public ExplosionLayer(ExplosionLayer previous) {
		this(previous, 16);
	}
	public ExplosionLayer(ExplosionLayer previous, final int scale) {
		this.model = previous.model;
		this.index = previous.index + 1;
		final int scaleminone = scale - 1;
		final double fact = (double) scaleminone / 2.0;
		int x, y, z;
		double dx, dy, dz;
		for (x = 0; x < scale; ++x) {
			for (y = 0; y < scale; ++y) {
				for (z = 0; z < scale; ++z) {
					if (x == 0 || x == scaleminone || y == 0 || y == scaleminone || z == 0 || z == scaleminone) {
						dx = (double) x / fact - 1.0;
						dy = (double) y / fact - 1.0;
						dz = (double) z / fact - 1.0;
						double d = 0.3 / MathHelper.length(dx, dy, dz);
						dx *= d;
						dy *= d;
						dz *= d;
						//=============================================
						previous.createSlot(dx, dy, dz).nextSet.add(this.createSlot(dx, dy, dz));
					}
				}
			}
		}
		this.slotArray = this.slots.values().toArray(new ExplosionSlot[0]);
	}
	private final int index;
	public final ExplosionSlot[] slotArray;
	private final ExplosionModelSpherical model;
	public Map<Vector3, ExplosionSlot> slots = new HashMap<Vector3, ExplosionSlot>();
	public ExplosionSlot createSlot(final double dx, final double dy, final double dz) {
		int x = MathHelper.floor(dx * (double) this.index + 0.5);
		int y = MathHelper.floor(dy * (double) this.index + 0.5);
		int z = MathHelper.floor(dz * (double) this.index + 0.5);
		Vector3 pos = new Vector3(x, y, z);
		ExplosionSlot slot = this.slots.get(pos);
		if (slot == null) {
			slot = new ExplosionSlot(this.model.getBlock(pos));
			this.slots.put(pos, slot);
		}
		return slot;
	}
}
