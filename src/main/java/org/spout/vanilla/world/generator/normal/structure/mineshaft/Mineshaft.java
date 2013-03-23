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
package org.spout.vanilla.world.generator.normal.structure.mineshaft;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.Set;

import org.spout.api.geo.World;
import org.spout.api.geo.discrete.Point;

import org.spout.vanilla.world.generator.structure.Structure;
import org.spout.vanilla.world.generator.structure.StructurePiece;
import org.spout.vanilla.world.generator.structure.StructurePiece.BoundingBox;

public class Mineshaft extends Structure {
	private static final byte MAX_SIZE_BASE = 50;
	private static final byte MAX_SIZE_RAND = 50;

	public Mineshaft() {
	}

	public Mineshaft(Random random) {
		super(random);
	}

	@Override
	public boolean canPlaceObject(World w, int x, int y, int z) {
		return y >= 20;
	}

	@Override
	public void placeObject(World w, int x, int y, int z) {
		final Set<BoundingBox> placed = new HashSet<BoundingBox>();
		final Queue<StructurePiece> activeBranches = new LinkedList<StructurePiece>();
		final MineshaftRoom room = new MineshaftRoom(this);
		room.setPosition(new Point(w, x, y, z));
		room.randomize();
		activeBranches.add(room);
		final byte size = (byte) (random.nextInt(MAX_SIZE_RAND) + MAX_SIZE_BASE);
		byte count = 0;
		while (!activeBranches.isEmpty()) {
			final StructurePiece active = activeBranches.poll();
			final BoundingBox activeBox = active.getBoundingBox();
			if (!collides(activeBox, active.getLastComponent(), placed) && active.canPlace()
					&& active.getPosition().getY() >= 20) {
				active.place();
				if (++count > size) {
					return;
				}
				placed.add(activeBox);
				final List<StructurePiece> next = active.getNextComponents();
				for (StructurePiece component : next) {
					component.setLastComponent(active);
				}
				activeBranches.addAll(next);
			}
		}
	}

	private boolean collides(BoundingBox box, StructurePiece lastComponent, Collection<BoundingBox> boxes) {
		final BoundingBox last;
		if (lastComponent == null) {
			last = null;
		} else {
			last = lastComponent.getBoundingBox();
		}
		for (BoundingBox other : boxes) {
			if (!other.equals(last) && other.intersects(box)) {
				return true;
			}
		}
		return false;
	}
}
