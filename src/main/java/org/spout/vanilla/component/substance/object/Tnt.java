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

import org.spout.api.Source;
import org.spout.api.entity.Entity;

import org.spout.vanilla.VanillaPlugin;
import org.spout.vanilla.data.VanillaData;
import org.spout.vanilla.protocol.entity.ObjectEntityProtocol;
import org.spout.vanilla.util.explosion.ExplosionModel;
import org.spout.vanilla.util.explosion.ExplosionModelSpherical;

public class Tnt extends ObjectEntity implements Source {
	public static final int ID = 50;
	private Entity holder;

	public float getExplosionSize() {
		return getData().get(VanillaData.EXPLOSION_SIZE);
	}

	public void setExplosionSize(float explosionSize) {
		getData().put(VanillaData.EXPLOSION_SIZE, explosionSize);
	}

	public boolean makesFire() {
		return getData().get(VanillaData.MAKES_FIRE);
	}

	public float getFuse() {
		return getData().get(VanillaData.FUSE);
	}

	public void setFuse(float fuse) {
		getData().put(VanillaData.FUSE, fuse);
	}

	public void pulse(float dt) {
		setFuse(getFuse() - dt);
	}

	public void setMakesFire(boolean makesFire) {
		getData().put(VanillaData.MAKES_FIRE, makesFire);
	}

	@Override
	public void onAttached() {
		getOwner().getNetwork().setEntityProtocol(VanillaPlugin.VANILLA_PROTOCOL_ID, new ObjectEntityProtocol(ID));
		holder = getOwner();

	}

	@Override
	public void onTick(float dt) {
		pulse(dt);
		if (getFuse() <= 0) {
			ExplosionModel explosion = new ExplosionModelSpherical();
			explosion.execute(holder.getTransform().getPosition(), getExplosionSize(), makesFire(), this);
			holder.remove();
		}
	}
}
