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
package org.spout.vanilla.data.effect.type;

import java.util.List;

import org.spout.api.entity.Entity;
import org.spout.api.entity.Player;
import org.spout.api.geo.discrete.Point;

import org.spout.vanilla.data.effect.Effect;
import org.spout.vanilla.event.world.PlayExplosionEffectEvent;

public class ExplosionEffect extends Effect {
	private static final int EXPLOSION_RANGE = 64;
	private static final float DEFAULT_SIZE = 5.0f;
	private float size;

	public ExplosionEffect() {
		this(EXPLOSION_RANGE, DEFAULT_SIZE);
	}

	public ExplosionEffect(int range, float size) {
		super(range);
		this.size = size;
	}

	public float getDefaultSize() {
		return this.size;
	}

	@Override
	public void play(Player player, Point position) {
		this.play(player, position, this.getDefaultSize());
	}

	public void play(Player player, Point position, float size) {
		player.getSession().getNetworkSynchronizer().callProtocolEvent(new PlayExplosionEffectEvent(position, this, size));
	}

	public void play(List<Player> players, Point position, float size) {
		for (Player player : players) {
			this.play(player, position, size);
		}
	}

	public void playGlobal(Point position, float size) {
		this.playGlobal(position, size, null);
	}

	public void playGlobal(Point position, float size, Player ignore) {
		this.play(getNearbyPlayers(position, ignore), position, size);
	}
}
