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
package org.spout.vanilla.plugin.world.generator.normal.structure.stronghold;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.spout.api.geo.World;
import org.spout.api.geo.discrete.Point;
import org.spout.api.math.Quaternion;

import org.spout.vanilla.plugin.world.generator.structure.Structure;
import org.spout.vanilla.plugin.world.generator.structure.StructureComponent;
import org.spout.vanilla.plugin.world.generator.structure.StructureComponent.BoundingBox;

public class Stronghold extends Structure {
	private static final byte MAX_SIZE_BASE = 101;
	private static final byte MAX_SIZE_RAND = 100;

	public Stronghold() {
	}

	public Stronghold(Random random) {
		super(random);
	}

	@Override
	public boolean canPlaceObject(World w, int x, int y, int z) {
		return y >= 10;
	}

	@Override
	public void placeObject(World w, int x, int y, int z) {
		final Set<BoundingBox> placed = new HashSet<BoundingBox>();
		final List<StructureComponent> activeBranches = new LinkedList<StructureComponent>();
		final StrongholdCorridor corridor = new StrongholdCorridor(this);
		corridor.setStartOfStronghold(true);
		corridor.setPosition(new Point(w, x, y, z));
		corridor.setRotation(new Quaternion(random.nextInt(4) * 90, 0, 1, 0));
		corridor.randomize();
		activeBranches.add(corridor);
		final int size = random.nextInt(MAX_SIZE_RAND) + MAX_SIZE_BASE;
		byte count = 0;
		while (!activeBranches.isEmpty()) {
			final StructureComponent active = activeBranches.remove(0);
			final BoundingBox activeBox = active.getBoundingBox();
			if (active.getPosition().getY() >= 10
					&& !collides(activeBox, active.getLastComponent(), placed)
					&& active.canPlace()) {
				active.place();
				if (++count > size) {
					return;
				}
				placed.add(activeBox);
				final List<StructureComponent> next = active.getNextComponents();
				for (StructureComponent component : next) {
					component.setLastComponent(active);
				}
				activeBranches.addAll(next);
			}
		}
	}

	private boolean collides(BoundingBox box, StructureComponent lastComponent, Collection<BoundingBox> boxes) {
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
