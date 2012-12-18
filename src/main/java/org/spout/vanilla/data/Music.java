/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, Spout LLC <http://www.spout.org/>
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
package org.spout.vanilla.data;

public enum Music {
	NONE(0, "none", "none"),
	THIRTEEN(2256, "13", "C418"),
	CAT(2257, "cat", "C418"),
	BLOCKS(2258, "blocks", "C418"),
	CHIRP(2259, "chirp", "C418"),
	FAR(2260, "far", "C418"),
	MALL(2261, "mall", "C418"),
	MELLOHI(2262, "mellohi", "C418"),
	STAL(2263, "stal", "C418"),
	STRAD(2264, "strad", "C418"),
	WARD(2265, "ward", "C418"),
	ELEVEN(2266, "11", "C418"),
	WAIT(2267, "wait", "C418");
	private final int id;
	private final String name;
	private final String author;

	private Music(int id, String name, String author) {
		this.id = id;
		this.name = name;
		this.author = author;
	}

	public int getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public String getAuthor() {
		return this.author;
	}

	@Override
	public String toString() {
		return this.author + " - " + this.name;
	}
}
