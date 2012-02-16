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
package org.spout.vanilla.protocol.msg;

import org.spout.api.protocol.Message;

public final class RespawnMessage extends Message {
	private final byte difficulty, mode;
	private final int worldHeight, dimension;
	private final long seed;
	private final String worldType;

	public RespawnMessage(int dimension, byte difficulty, byte mode, int worldHeight, long seed, String worldType) {
		this.dimension = dimension;
		this.difficulty = difficulty;
		this.mode = mode;
		this.worldHeight = worldHeight;
		this.seed = seed;
		this.worldType = worldType;
	}

	public int getDimension() {
		return dimension;
	}

	public byte getDifficulty() {
		return difficulty;
	}

	public byte getGameMode() {
		return mode;
	}

	public int getWorldHeight() {
		return worldHeight;
	}

	public long getSeed() {
		return seed;
	}

	public String getWorldType() {
		return worldType;
	}

	@Override
	public String toString() {
		return "RespawnMessage{dimension=" + dimension + ",difficulty=" + difficulty + ",gameMode=" + mode + ",worldHeight=" + worldHeight + ",seed=" + seed + ",worldType=" + worldType + "}";
	}
}
