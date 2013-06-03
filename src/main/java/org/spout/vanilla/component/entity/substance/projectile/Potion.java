/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, Spout LLC <http://www.spout.org/>
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
package org.spout.vanilla.component.entity.substance.projectile;

import org.spout.api.entity.Entity;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.discrete.Point;

import org.spout.vanilla.VanillaPlugin;
import org.spout.vanilla.component.entity.substance.Substance;
import org.spout.vanilla.data.effect.store.GeneralEffects;
import org.spout.vanilla.material.item.potion.PotionItem;
import org.spout.vanilla.protocol.entity.object.PotionEntityProtocol;

public class Potion extends Substance implements Projectile {
	private Entity shooter;
	private PotionItem potion;

	@Override
	public void onAttached() {
		getOwner().getNetwork().setEntityProtocol(VanillaPlugin.VANILLA_PROTOCOL_ID, new PotionEntityProtocol());
	}

	@Override
	public Entity getShooter() {
		return shooter;
	}

	@Override
	public void setShooter(Entity shooter) {
		this.shooter = shooter;
	}

	public PotionItem getPotion() {
		return potion;
	}

	public void setPotion(PotionItem potion) {
		this.potion = potion;
	}

	@Override
	public void onCollided(Point point, Entity entity) {
		GeneralEffects.SPLASHPOTION.playGlobal(point, getPotion().getData());
		getOwner().remove();
	}

	@Override
	public void onCollided(Point point, Block block) {
		GeneralEffects.SPLASHPOTION.playGlobal(point, getPotion().getData());
		getOwner().remove();
	}
}
