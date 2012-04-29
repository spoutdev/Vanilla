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
package org.spout.vanilla.material.block.data;

import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.MinecartTrackPowered;
import org.spout.vanilla.util.RailsState;

public class PoweredRails extends Rails {
	private boolean powered;

	public PoweredRails(short data) {
		super((short) (data & 0x7));
		this.powered = (data & 0x8) == 0x8;
	}

	public PoweredRails(RailsState state, boolean powered) {
		super(state);
		this.powered = powered;
	}

	public boolean isPowered() {
		return this.powered;
	}

	public void setPowered(boolean pressed) {
		this.powered = pressed;
	}

	@Override
	public short getData() {
		short data = super.getData();
		if (this.powered) {
			data |= 0x8;
		}
		return data;
	}

	@Override
	public MinecartTrackPowered getMaterial() {
		return VanillaMaterials.POWERED_RAIL;
	}
}
