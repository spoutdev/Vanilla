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
package org.spout.vanilla.world.generator.structure.stronghold;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import org.spout.api.Spout;

import org.spout.api.geo.World;
import org.spout.api.geo.discrete.Point;
import org.spout.api.math.Quaternion;
import org.spout.api.math.Vector3;

import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.world.generator.structure.Structure;
import org.spout.vanilla.world.generator.structure.StructureComponent;
import org.spout.vanilla.world.generator.structure.StructureComponent.BoundingBox;

public class Stronghold extends Structure {
	private static final byte MAX_SIZE_BASE = 50;
	private static final byte MAX_SIZE_RAND = 50;

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
		final List<StructureComponent> activeBranches = new ArrayList<StructureComponent>(4);
		final StrongholdCorridor corridor = new StrongholdCorridor(this);
		corridor.setEndOfStronghold(true);
		corridor.setPosition(new Point(w, x, y, z));
		corridor.setRotation(new Quaternion(random.nextInt(4) * 90, 0, 1, 0));
		corridor.randomize();
		activeBranches.add(corridor);
		byte size = (byte) (random.nextInt(MAX_SIZE_RAND) + MAX_SIZE_BASE);
		byte count = 0;
		while (!activeBranches.isEmpty()) {
			if (Spout.debugMode()) {
				System.out.println("Current actives branches for Stronghold: " + activeBranches);
			}
			final StructureComponent active = activeBranches.get(0);
			final BoundingBox activeBox = active.getBoundingBox();
			if (!collides(activeBox, active.getLastComponent(), placed) && active.canPlace()
					&& active.getPosition().getY() >= 10) {
				if (Spout.debugMode()) {
					fill(w, activeBox);
				}
				active.place();
				if (++count > size) {
					return;
				}
				placed.add(activeBox);
				try {
					final List<StructureComponent> next = active.getNextComponents();
					for (StructureComponent component : next) {
						component.setLastComponent(active);
					}
					activeBranches.addAll(next);
				} catch (UnsupportedOperationException ex) {
					continue;
				}
			}
			activeBranches.remove(active);
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

	/**
	 * A testing method, fills the bounding box with glass. Basically, it builds
	 * "ground" so I can test this above ground. Also, i can visualize the BBs
	 * with this.
	 *
	 * TODO: remove when done.
	 */
	private void fill(World world, BoundingBox box) {
		final Vector3 max = box.getMax();
		final Vector3 min = box.getMin();
		for (int x = min.getFloorX(); x <= max.getFloorX(); x++) {
			for (int y = min.getFloorY(); y <= max.getFloorY(); y++) {
				for (int z = min.getFloorZ(); z <= max.getFloorZ(); z++) {
					if (world.getBlockMaterial(x, y, z).isMaterial(VanillaMaterials.AIR)) {
						world.setBlockMaterial(x, y, z, VanillaMaterials.GLASS, (short) 0, null);
					}
				}
			}
		}
	}
}
