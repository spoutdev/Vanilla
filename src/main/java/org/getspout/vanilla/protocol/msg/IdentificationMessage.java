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
package org.getspout.vanilla.protocol.msg;

import org.getspout.api.protocol.Message;

public final class IdentificationMessage extends Message {
	private final int id, dimension, mode, difficulty, worldHeight, maxPlayers;
	private final String name;
	private final long seed;

	public IdentificationMessage(int id, String name, long seed, int mode, int dimension, int difficulty, int worldHeight, int maxPlayers) {
		this.id = id;
		this.name = name;
		this.seed = seed;
		this.mode = mode;
		this.dimension = dimension;
		this.difficulty = difficulty;
		this.worldHeight = worldHeight;
		this.maxPlayers = maxPlayers;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public long getSeed() {
		return seed;
	}

	public int getGameMode() {
		return mode;
	}

	public int getDimension() {
		return dimension;
	}

	public int getDifficulty() {
		return difficulty;
	}

	public int getWorldHeight() {
		return worldHeight;
	}

	public int getMaxPlayers() {
		return maxPlayers;
	}

	@Override
	public String toString() {
		return "IdentificationMessage{id=" + id + ",name=" + name + ",seed=" + seed +
				",gameMode=" + mode + ",dimension=" + dimension + ",difficulty=" +
				difficulty + ",worldHeight=" + worldHeight + ",maxPlayers=" + maxPlayers + "}";
	}
}