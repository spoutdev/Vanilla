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
import org.spout.vanilla.protocol.msg.NamedSoundEffectMessage;
import org.spout.vanilla.util.VanillaNetworkUtil;

public class Sound extends SoundStore implements VanillaSound {
	private final String name;
	private final float volume, pitch;
	private final int range;

	public Sound(String name) {
		this(name, 1.0f, 1.0f, 16);
	}

	public Sound(String name, float volume, float pitch, int range) {
		this.name = name;
		this.volume = volume;
		this.pitch = pitch;
		this.range = range;
	}

	public float getDefaultVolume() {
		return this.volume;
	}

	public float getDefaultPitch() {
		return this.pitch;
	}

	public int getDefaultRange() {
		return this.range;
	}

	public String getName() {
		return this.name;
	}

	@Override
	public void play(Player player, Vector3 position) {
		this.play(player, position, this.getDefaultVolume(), this.getDefaultPitch());
	}

	@Override
	public void play(Player player, Vector3 position, float volume, float pitch) {
		player.getSession().send(false, new NamedSoundEffectMessage(this.name, position, volume, pitch));
	}

	@Override
	public void playGlobal(Point position) {
		this.playGlobal(position, this.getDefaultVolume(), this.getDefaultPitch());
	}

	@Override
	public void playGlobal(Point position, float volume, float pitch) {
		this.playGlobal(position, volume, pitch, this.getDefaultRange());
	}

	@Override
	public void playGlobal(Point position, float volume, float pitch, int range) {
		VanillaNetworkUtil.sendPacketsToNearbyPlayers(position, range, new NamedSoundEffectMessage(this.name, position, volume, pitch));
	}
}
