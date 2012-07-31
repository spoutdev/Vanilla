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
package org.spout.vanilla.util.sound;

import org.spout.api.geo.discrete.Point;
import org.spout.api.math.Vector3;
import org.spout.api.player.Player;

public class RandomSound extends RandomSoundStore implements VanillaSound {
	private final Sound[] sounds;

	public RandomSound(Sound... sounds) {
		this.sounds = sounds;
	}

	public Sound[] getSounds() {
		return this.sounds;
	}

	public Sound getRandomSound() {
		return sounds[(int) (Math.random() * sounds.length)];
	}

	@Override
	public void playGlobal(Point position) {
		this.getRandomSound().playGlobal(position);
	}

	@Override
	public void playGlobal(Point position, float volume, float pitch) {
		this.getRandomSound().playGlobal(position, volume, pitch);
	}

	@Override
	public void playGlobal(Point position, float volume, float pitch, int range) {
		this.getRandomSound().playGlobal(position, volume, pitch, range);
	}

	@Override
	public void play(Player player, Vector3 position) {
		this.getRandomSound().play(player, position);
	}

	@Override
	public void play(Player player, Vector3 position, float volume, float pitch) {
		this.getRandomSound().play(player, position, volume, pitch);
	}
}
