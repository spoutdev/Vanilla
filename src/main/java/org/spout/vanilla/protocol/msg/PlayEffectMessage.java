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
package org.spout.vanilla.protocol.msg;

import org.apache.commons.lang3.builder.ToStringBuilder;

import org.spout.api.geo.cuboid.Block;
import org.spout.api.math.Vector3;
import org.spout.api.protocol.Message;
import org.spout.api.util.SpoutToStringStyle;

public final class PlayEffectMessage extends Message {
	private final int id;
	private final int x, y, z;
	private final int data;

	public PlayEffectMessage(int id, Block block, int data) {
		this(id, block.getX(), block.getY(), block.getZ(), data);
	}

	public PlayEffectMessage(int id, int x, int y, int z, int data) {
		this.id = id;
		this.x = x;
		this.y = y;
		this.z = z;
		this.data = data;
	}

	public static final int SMOKE_NORTH_EAST = 0;
	public static final int SMOKE_EAST = 1;
	public static final int SMOKE_SOUTH_EAST = 2;
	public static final int SMOKE_NORTH = 3;
	public static final int SMOKE_MIDDLE = 4;
	public static final int SMOKE_SOUTH = 5;
	public static final int SMOKE_NORTH_WEST = 6;
	public static final int SMOKE_WEST = 7;
	public static final int SMOKE_SOUTH_WEST = 8;

	/**
	 * Gets the data effect value to use for the smoke particle effect
	 * @param direction the smoke goes
	 * @return the data of that direction
	 */
	public static int getSmokeDirection(Vector3 direction) {
		if (direction.equals(Vector3.ZERO)) {
			return 4;
		}
		float dx = direction.getX();
		float dz = direction.getZ();
		if (dz < 0) {
			if (dx < 0) {
				if (dx * 2 < dz) {
					return 2 * dz < dx ? SMOKE_NORTH_EAST : SMOKE_NORTH;
				} else {
					return SMOKE_EAST;
				}
			} else {
				if (dz * -2 > dx) {
					return -2 * dx < dz ? SMOKE_SOUTH_EAST : SMOKE_EAST;
				} else {
					return SMOKE_SOUTH;
				}
			}
		} else {
			if (dx < 0) {
				if (-2 * dz < dx) {
					return -2 * dx > dz ? SMOKE_NORTH_WEST : SMOKE_WEST;
				} else {
					return SMOKE_NORTH;
				}
			} else {
				if (2 * dx > dz) {
					return 2 * dz > dx ? SMOKE_SOUTH_WEST : SMOKE_SOUTH;
				} else {
					return SMOKE_WEST;
				}
			}
		}
	}

	public int getId() {
		return id;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getZ() {
		return z;
	}

	public int getData() {
		return data;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, SpoutToStringStyle.INSTANCE)
				.append("id", id)
				.append("x", x)
				.append("y", y)
				.append("z", z)
				.append("data", data)
				.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final PlayEffectMessage other = (PlayEffectMessage) obj;
		return new org.apache.commons.lang3.builder.EqualsBuilder()
				.append(this.id, other.id)
				.append(this.x, other.x)
				.append(this.y, other.y)
				.append(this.z, other.z)
				.append(this.data, other.data)
				.isEquals();
	}

	public static enum Messages {
		/**
		 * Plays a click sound, Id = 1000
		 */
		RANDOM_CLICK_1(1000),
		/**
		 * Plays a click sound, Id = 1001
		 */
		RANDOM_CLICK_2(1001),
		/**
		 * Plays the sound of an arrow being shot by a bow, Id = 1002
		 */
		RANDOM_BOW(1002),
		/**
		 * Plays a random sound for a door open/closing, Id = 1003
		 */
		RANDOM_DOOR(1003),
		/**
		 * Plays a random sound of fire sizzling, Id = 1004
		 */
		RANDOM_FIZZ(1004),
		/**
		 * Plays the music of a certain record, Id = 1005
		 */
		MUSIC_DISC(1005),
		/**
		 * Plays the sound of a ghast shrieking, Id = 1007
		 */
		GHAST_CHARGE(1007),
		/**
		 * Plays the sound of a ghast shooting a fireball, Id = 1008
		 */
		GHAST_FIREBALL(1008),
		/**
		 * Plays the sound of something other than ghasts shooting a fireball, Id = 1009
		 */
		SHOOT_FIREBALL(1009),
		/**
		 * Plays the sound of a zombie breaking wood, Id = 1010
		 */
		ZOMBIE_DAMAGE_WOOD(1010),
		/**
		 * Plays the sound of a zombie breaking metal, Id = 1011
		 */
		ZOMBIE_DAMAGE_METAL(1011),
		/**
		 * Plays the sound of a zombie breaking something other than wood or metal, Id = 1012
		 */
		ZOMBIE_BREAK(1012),
		/**
		 * Plays a particle effect of Smoke, Id = 2000
		 */
		PARTICLE_SMOKE(2000),
		/**
		 * Plays the sound and particle effect of a Block being broken, Id = 2001
		 */
		PARTICLE_BREAKBLOCK(2001),
		/**
		 * Plays the sound and particle effect of a Splash potion breaking, Id = 2002
		 */
		PARTICLE_SPLASHPOTION(2002),
		/**
		 * Plays the particle effect of the ender eye item, Id = 2003
		 */
		PARTICLE_ENDEREYE(2003),
		/**
		 * Plays the mob spawn particle effect of mob spawners, Id = 2004
		 */
		MOB_SPAWN(2004);
		private int id;

		private Messages(int id) {
			this.id = id;
		}

		public int getId() {
			return this.id;
		}
	}
}
