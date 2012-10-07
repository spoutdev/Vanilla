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
package org.spout.vanilla.component.substance.object;

import java.util.Random;

import org.spout.api.Source;
import org.spout.api.entity.Entity;

import org.spout.vanilla.VanillaPlugin;
import org.spout.vanilla.protocol.entity.BasicObjectEntityProtocol;
import org.spout.vanilla.util.explosion.ExplosionModel;
import org.spout.vanilla.util.explosion.ExplosionModelSpherical;

public class Tnt extends ObjectEntity implements Source {
	public static final int ID = 50;
	private final Random random = new Random();
	private float fuse = random.nextInt(5) + 1;
	private float explosionSize = 4;
	private boolean makesFire = false;
	private Entity holder;

	public float getExplosionSize() {
		return explosionSize;
	}

	public void setExplosionSize(float explosionSize) {
		this.explosionSize = explosionSize;
	}

	public boolean makesFire() {
		return makesFire;
	}

	public void setMakesFire(boolean makesFire) {
		this.makesFire = makesFire;
	}

	@Override
	public void onAttached() {
		getHolder().getNetwork().setEntityProtocol(VanillaPlugin.VANILLA_PROTOCOL_ID, new BasicObjectEntityProtocol(ID));
		holder = getHolder();
	}

	@Override
	public void onTick(float dt) {
		fuse -= dt;
		if (fuse <= 0) {
			ExplosionModel explosion = new ExplosionModelSpherical();
			explosion.execute(holder.getTransform().getPosition(), explosionSize, makesFire, this);
			holder.remove();
		}
	}
}
