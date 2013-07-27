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
package org.spout.vanilla.data.effect;

import java.util.Collection;

import org.spout.api.entity.Player;
import org.spout.api.geo.discrete.Point;

import org.spout.vanilla.data.effect.type.RandomPitchSoundEffect;
import org.spout.vanilla.event.world.PlaySoundEffectEvent;

public class SoundEffect extends Effect {
	private static final long serialVersionUID = 1L;

	private static final int SOUND_RANGE = 16;
	private final String name;
	private final float volume, pitch;

	public SoundEffect(SoundEffect sound, float volume, float pitch) {
		this(sound.getName(), volume, pitch, sound.getRange());
	}

	public SoundEffect(String name) {
		this(name, 1.0f, 1.0f);
	}

	public SoundEffect(String name, int range) {
		this(name, 1.0f, 1.0f, range);
	}

	public SoundEffect(String name, float volume, float pitch) {
		this(name, volume, pitch, SOUND_RANGE);
	}

	public SoundEffect(String name, float volume, float pitch, int range) {
		super(range);
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

	/**
	 * Gets this Sound Effect with the volume and pitch adjusted
	 * @param volume to set to
	 * @param pitch to set to
	 * @return a new sound effect with the new settings
	 */
	public SoundEffect adjust(float volume, float pitch) {
		return new SoundEffect(this, volume, pitch);
	}

	/**
	 * Adds the amount of randomness in the pitch of this Sound
	 * @param amount of randomness to add
	 * @return a new sound effect with the new settings
	 */
	public SoundEffect randomPitch(float amount) {
		return new RandomPitchSoundEffect(this, amount);
	}

	public String getName() {
		return this.name;
	}

	@Override
	public String toString() {
		return "Sound{" + this.name + "}";
	}

	/**
	 * Gets all the Players nearby a certain Point that can receive this Effect
	 * @param position of this Effect
	 * @param ignore Entity to ignore
	 * @return a Set of nearby Players
	 */
	public Collection<Player> getNearbyPlayers(Point position, Player ignore, float volume) {
		int range = this.getRange();
		if (volume > 1.0f) {
			// Multiply range for different volumes
			range *= volume;
		}
		return position.getWorld().getNearbyPlayers(position, ignore, range);
	}

	@Override
	public void play(Player player, Point position) {
		this.play(player, position, this.getDefaultVolume(), this.getDefaultPitch());
	}

	public void play(Player player, Point position, float volume, float pitch) {
		//TODO Fix this, these should NEVER BE NULL
		if (player == null || player.getSession() == null | player.getSession().getNetworkSynchronizer() == null) {
			return;
		}
		player.getSession().getNetworkSynchronizer().callProtocolEvent(new PlaySoundEffectEvent(position, this, volume, pitch));
	}

	public void play(Collection<Player> players, Point position, float volume, float pitch) {
		for (Player player : players) {
			this.play(player, position, volume, pitch);
		}
	}

	public void playGlobal(Point position, float volume, float pitch) {
		this.playGlobal(position, volume, pitch, null);
	}

	public void playGlobal(Point position, float volume, float pitch, Player ignore) {
		this.play(getNearbyPlayers(position, ignore, volume), position, volume, pitch);
	}
}
