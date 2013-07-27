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
package org.spout.vanilla.util.explosion;

import java.util.HashMap;
import java.util.Map;

import org.spout.api.math.GenericMath;
import org.spout.api.math.Vector3;

public class ExplosionLayer {
	public ExplosionLayer(final ExplosionModelSpherical model) {
		this.model = model;
		this.index = 0;
		this.slots = new ExplosionSlot[]{this.createSlot(0.0, 0.0, 0.0)};
	}

	public ExplosionLayer(ExplosionLayer previous) {
		this(previous, 16);
	}

	public ExplosionLayer(ExplosionLayer previous, final int scale) {
		this.model = previous.model;
		this.index = previous.index + 1;
		final int scaleminone = scale - 1;
		final double fact = scaleminone / 2.0;
		int x, y, z;
		double dx, dy, dz;
		for (x = 0; x < scale; ++x) {
			for (y = 0; y < scale; ++y) {
				for (z = 0; z < scale; ++z) {
					if (x == 0 || x == scaleminone || y == 0 || y == scaleminone || z == 0 || z == scaleminone) {
						dx = x / fact - 1.0;
						dy = y / fact - 1.0;
						dz = z / fact - 1.0;
						double d = 0.3 / GenericMath.length(dx, dy, dz);
						dx *= d;
						dy *= d;
						dz *= d;
						//=============================================
						previous.createSlot(dx, dy, dz).addNext(this.createSlot(dx, dy, dz));
					}
				}
			}
		}
		this.finish();
	}

	private final int index;
	public ExplosionSlot[] slots;
	private final Map<Vector3, ExplosionSlot> tmpSlotMap = new HashMap<Vector3, ExplosionSlot>();
	private final ExplosionModelSpherical model;

	/**
	 * Transfers all slots previously added to the 'slots' array
	 */
	public void finish() {
		this.slots = this.tmpSlotMap.values().toArray(new ExplosionSlot[0]);
	}

	/**
	 * Creates a new slot using a given direction
	 * @param dx is the delta x motion
	 * @param dy is the delta y motion
	 * @param dz is the delta z motion
	 * @return an already stored or new slot
	 */
	public ExplosionSlot createSlot(final double dx, final double dy, final double dz) {
		int x = GenericMath.floor(dx * this.index + 0.5);
		int y = GenericMath.floor(dy * this.index + 0.5);
		int z = GenericMath.floor(dz * this.index + 0.5);
		Vector3 pos = new Vector3(x, y, z);
		ExplosionSlot slot = this.tmpSlotMap.get(pos);
		if (slot == null) {
			slot = new ExplosionSlot(this.model.getBlock(pos));
			this.tmpSlotMap.put(pos, slot);
		}
		return slot;
	}
}
