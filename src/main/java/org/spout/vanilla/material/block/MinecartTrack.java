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
package org.spout.vanilla.material.block.specific;

import org.spout.api.geo.World;
import org.spout.vanilla.material.block.data.Rails;
import org.spout.vanilla.material.block.specific.redstone.RedstoneTarget;

public class MinecartTrack extends MinecartTrackBase implements RedstoneTarget {
	public MinecartTrack(String name, int id) {
		super(name, id);
	}

	@Override
	public boolean canCurve() {
		return true;
	}

	@Override
	public Rails createData(short data) {
		return new Rails(data);
	}

	@Override
	public boolean providesAttachPoint(World world, int x, int y, int z, int tx, int ty, int tz) {
		// TODO Auto-generated method stub
		return false;
	}
}
