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
package org.spout.vanilla.data.effect;

import java.util.List;

import org.spout.api.entity.Entity;
import org.spout.api.entity.Player;
import org.spout.api.geo.discrete.Point;

import org.spout.vanilla.event.world.PlayParticleEffectEvent;

public class GeneralEffect extends Effect {
	private static final int PARTICLE_RANGE = 32;
	private final int id;
	private int data;

	public GeneralEffect(int id) {
		this(id, 0);
	}

	public GeneralEffect(int id, int data) {
		this(id, data, PARTICLE_RANGE);
	}

	public GeneralEffect(int id, int data, int range) {
		super(range);
		this.id = id;
		this.data = data;
	}

	/**
	 * Gets the Id of this Effect
	 * @return Effect Id
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * Gets the Default data for this Effect
	 * @return default data
	 */
	public int getDefaultData() {
		return this.data;
	}

	@Override
	public void play(Player player, Point position) {
		this.play(player, position, this.getDefaultData());
	}

	public void play(Player player, Point position, int data) {
		player.getSession().getNetworkSynchronizer().callProtocolEvent(new PlayParticleEffectEvent(position, this, data));
	}

	public void play(List<Player> players, Point position, int data) {
		for (Player player : players) {
			this.play(player, position, data);
		}
	}

	public void playGlobal(Point position, int data) {
		this.playGlobal(position, data, null);
	}

	public void playGlobal(Point position, int data, Player ignore) {
		this.play(getNearbyPlayers(position, ignore), position, data);
	}
}
