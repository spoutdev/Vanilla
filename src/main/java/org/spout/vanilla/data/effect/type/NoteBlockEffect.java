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

import org.spout.api.geo.discrete.Point;
import org.spout.api.player.Player;
import org.spout.vanilla.data.effect.GeneralEffect;
import org.spout.vanilla.data.effect.SoundEffect;

public class NoteBlockEffect extends GeneralEffect {
	private static final int NOTE_RANGE = 48;
	private SoundEffect sound;

	public NoteBlockEffect(SoundEffect sound) {
		super(NOTE_RANGE);
		this.sound = sound;
	}

	public SoundEffect getSound() {
		return this.sound;
	}

	@Override
	public void play(Player player, Point position, int data) {
		// calculate pitch
		float pitch = (float) Math.pow(2.0, (double) (data - 12) / 12.0);
		this.sound.play(player, position, 3.0f, pitch);
		//TODO: Find a way to trigger the 'note' particle through a message!
		//world.a("note", x, y, z, (double) data / 24.0, 0.0, 0.0);
	}
}
