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
package org.spout.vanilla.protocol.reposition;

import org.spout.api.protocol.reposition.RepositionManager;
import org.spout.api.protocol.reposition.RepositionManagerImpl;

public class VanillaRepositionManager extends RepositionManagerImpl {
	private final VanillaRepositionManager inverse;
	private volatile int offset = 0;

	public VanillaRepositionManager() {
		this(0);
	}

	public VanillaRepositionManager(int offset) {
		this.inverse = new VanillaRepositionManager(-offset, this);
		this.offset = offset;
	}

	private VanillaRepositionManager(int offset, VanillaRepositionManager inverse) {
		this.inverse = inverse;
		this.offset = offset;
	}

	@Override
	public RepositionManager getInverse() {
		return inverse;
	}

	@Override
	public double convertX(double x) {
		return x;
	}

	@Override
	public double convertY(double y) {
		return y + offset;
	}

	@Override
	public double convertZ(double z) {
		return z;
	}

	public void setOffset(int offset) {
		this.offset = offset;
		inverse.offset = -offset;
	}
}
