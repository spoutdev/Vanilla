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
package org.spout.vanilla.world.generator.nether.structure.fortress;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.Set;

import org.spout.api.geo.World;
import org.spout.api.geo.discrete.Point;
import org.spout.api.math.Quaternion;

import org.spout.vanilla.world.generator.structure.Structure;
import org.spout.vanilla.world.generator.structure.StructurePiece;
import org.spout.vanilla.world.generator.structure.StructurePiece.BoundingBox;

public class Fortress extends Structure {
	private static final byte MAX_SIZE_BASE = 75;
	private static final byte MAX_SIZE_RAND = 75;

	public Fortress() {
	}

	public Fortress(Random random) {
		super(random);
	}

	@Override
	public boolean canPlaceObject(World w, int x, int y, int z) {
		return true;
	}

	@Override
	public void placeObject(World w, int x, int y, int z) {
		final Set<BoundingBox> placed = new HashSet<BoundingBox>();
		final Queue<StructurePiece> activeBranches = new LinkedList<StructurePiece>();
		final Map<StructurePiece, BoundingBox> lastBoxes = new HashMap<StructurePiece, BoundingBox>();
		final FortressCorridor corridor = new FortressCorridor(this);
		corridor.setStartOfFortress(true);
		corridor.setPosition(new Point(w, x, y, z));
		corridor.setRotation(new Quaternion(random.nextInt(4) * 90, 0, 1, 0));
		corridor.randomize();
		activeBranches.add(corridor);
		final int size = random.nextInt(MAX_SIZE_RAND + 1) + MAX_SIZE_BASE;
		byte count = 0;
		while (!activeBranches.isEmpty()) {
			final StructurePiece active = activeBranches.poll();
			final BoundingBox activeBox = active.getBoundingBox();
			if (!collides(activeBox, lastBoxes.remove(active), placed) && active.canPlace()
					&& active.getPosition().getY() >= 10) {
				active.place();
				if (++count > size) {
					final List<StructurePiece> ends = new ArrayList<StructurePiece>();
					final Iterator<StructurePiece> iterator = activeBranches.iterator();
					while (iterator.hasNext()) {
						final StructurePiece stop = iterator.next();
						final StructurePiece end = new FortressEnd(this);
						end.setPosition(stop.getPosition());
						end.setRotation(stop.getRotation());
						ends.add(end);
						iterator.remove();
					}
					activeBranches.addAll(ends);
				}
				placed.add(activeBox);
				final List<StructurePiece> next = active.getNextPieces();
				for (StructurePiece piece : next) {
					lastBoxes.put(piece, activeBox);
				}
				activeBranches.addAll(next);
			}
		}
	}

	private boolean collides(BoundingBox box, BoundingBox last, Collection<BoundingBox> boxes) {
		for (BoundingBox other : boxes) {
			if (!other.equals(last) && other.intersects(box)) {
				return true;
			}
		}
		return false;
	}
}
