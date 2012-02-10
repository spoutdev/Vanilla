/*
 * This file is part of Vanilla (http://www.spout.org/).
 *
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
 * the MIT license and the SpoutDev license version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license,
 * including the MIT license.
 */
package org.spout.vanilla;

public enum Instrument {
	BASS_GUITAR(0),
	SNARE_DRUM(1),
	CLICKS(2),
	BASS_DRUM(3),
	PIANO(4);

	private final byte id;
	private final String name;

	Instrument(int id) {
		this.id = (byte) id;
		name = name().charAt(0) + name().substring(1).toLowerCase().replaceAll("_", " ");
	}

	public byte getId() {
		return id;
	}

	@Override
	public String toString() {
		return name;
	}

	public static Instrument getInstrumentFromId(final byte id) {
		switch (id) {
			case 0:
				return BASS_GUITAR;
			case 1:
				return SNARE_DRUM;
			case 2:
				return CLICKS;
			case 3:
				return BASS_DRUM;
			case 4:
				return PIANO;
			default:
				throw new IllegalArgumentException("Invalid id");
		}
	}
}
