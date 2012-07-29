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

import org.spout.api.math.Vector3;
import org.spout.api.player.Player;
import org.spout.vanilla.protocol.msg.NamedSoundEffectMessage;

public class Sound {
	private final String name;
	private final float volume, pitch;

	public Sound(String name, float volume, float pitch) {
		this.name = name;
		this.volume = volume;
		this.pitch = pitch;
	}

	public float getDefaultVolume() {
		return this.volume;
	}

	public float getDefaultPitch() {
		return this.pitch;
	}

	public String getName() {
		return this.name;
	}

	public void play(Player player, Vector3 position) {
		this.play(player, position.getX(), position.getY(), position.getZ());
	}

	public void play(Player player, Vector3 position, float volume, float pitch) {
		this.play(player, position.getX(), position.getY(), position.getZ(), volume, pitch);
	}

	public void play(Player player, float x, float y, float z) {
		this.play(player, x, y, z, this.volume, this.pitch);
	}

	public void play(Player player, float x, float y, float z, float volume, float pitch) {
		player.getSession().send(false, new NamedSoundEffectMessage(this.name, x, y, z, volume, pitch));
	}
}
