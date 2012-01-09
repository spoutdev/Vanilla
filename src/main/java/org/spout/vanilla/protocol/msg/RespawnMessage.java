/*
 * This file is part of Vanilla (http://www.spout.org/).
 *
 * Vanilla is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Vanilla is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.spout.vanilla.protocol.msg;

import org.spout.api.protocol.Message;

public final class RespawnMessage extends Message {
	private final byte dimension, difficulty, mode;
	private final int worldHeight;
	private final long seed;

	public RespawnMessage(byte dimension, byte difficulty, byte mode, int worldHeight, long seed) {
		this.dimension = dimension;
		this.difficulty = difficulty;
		this.mode = mode;
		this.worldHeight = worldHeight;
		this.seed = seed;
	}

	public byte getDimension() {
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

	@Override
	public String toString() {
		return "RespawnMessage{dimension=" + dimension + ",difficulty=" + difficulty + ",gameMode=" + mode + ",worldHeight=" + worldHeight + ",seed=" + seed + "}";
	}
}