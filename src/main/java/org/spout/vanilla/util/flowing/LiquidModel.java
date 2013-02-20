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
package org.spout.vanilla.util.flowing;

import gnu.trove.map.hash.TIntObjectHashMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.block.BlockFaces;
import org.spout.api.material.block.BlockFullState;
import org.spout.api.material.range.DiamondEffectRange;
import org.spout.api.math.IntVector3;
import org.spout.api.util.hashing.ShortPairHashed;

import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.Liquid;

/**
 * A model used to find the faces to which a liquid can flow<br>
 * Is basically a flat disk where 64 elements can have a state.
 */
public class LiquidModel {
	public static enum State {
		BLOCKED, OPEN, HOLE;
	}

	public static class Element {
		public Element(int dX, int dZ) {
			this.dX = dX;
			this.dZ = dZ;
			this.absdX = Math.abs(this.dX);
			this.absdZ = Math.abs(this.dZ);
			this.center = (this.dX == 0 && this.absdZ == 1) || (this.dZ == 0 && this.absdX == 1);
		}

		public final int dX, dZ;
		public final int absdX, absdZ;
		private State state;
		private int holeDistance;
		public final boolean center;
		private final List<Element> neighbours = new ArrayList<Element>();
		public final List<AtomicBoolean> mainDirection = new ArrayList<AtomicBoolean>();

		public void load(World world, int x, int y, int z) {
			for (AtomicBoolean bool : this.mainDirection) {
				if (bool.get()) {
					x += this.dX;
					z += this.dZ;
					int fullState = world.getBlockFullState(x, y, z);
					BlockMaterial mat = BlockFullState.getMaterial(fullState);
					if (Liquid.isLiquidObstacle(mat) || (VanillaMaterials.WATER.isMaterial(mat) && VanillaMaterials.WATER.isSource(BlockFullState.getData(fullState)))) {
						this.state = State.BLOCKED;
						this.holeDistance = Integer.MAX_VALUE;
					} else if (!Liquid.isLiquidObstacle(world.getBlockMaterial(x, y - 1, z))) {
						this.state = State.HOLE;
						this.holeDistance = 0;
					} else {
						this.state = State.OPEN;
						this.holeDistance = Integer.MAX_VALUE;
					}
					return;
				}
			}
			this.state = State.BLOCKED;
			this.holeDistance = Integer.MAX_VALUE;
		}

		public void link(Element with) {
			if (with != null) {
				this.neighbours.add(with);
			}
		}

		public void spreadHole() {
			if (this.holeDistance != -1) {
				for (Element element : this.neighbours) {
					if (element.state == State.OPEN && element.holeDistance > this.holeDistance) {
						element.holeDistance = this.holeDistance + 1;
					}
				}
			}
		}
	}

	private final List<Element> elementList = new ArrayList<Element>();
	private final Element[] center = new Element[4];
	private final TIntObjectHashMap<Element> elementMap = new TIntObjectHashMap<Element>();
	private final AtomicBoolean[] mainDirections = new AtomicBoolean[4];
	private final boolean[] possible = new boolean[4];

	private Element getElement(BlockFace face) {
		return getElement((int) face.getOffset().getX(), (int) face.getOffset().getZ());
	}

	private Element getElement(int dX, int dZ) {
		return elementMap.get(ShortPairHashed.key((short) dX, (short) dZ));
	}

	private void link(Element element, int dx, int dz) {
		element.link(getElement(element.dX + dx, element.dZ + dz));
		for (int i = 0; i < 4; i++) {
			IntVector3 offset = new IntVector3(BlockFaces.NESW.get(i).getOpposite().getOffset());
			if (dx == offset.getX() && dz == offset.getZ()) {
				element.mainDirection.add(this.mainDirections[i]);
			}
		}
	}

	private LiquidModel() {
		for (int i = 0; i < this.mainDirections.length; i++) {
			this.mainDirections[i] = new AtomicBoolean(true);
		}
		for (IntVector3 delta : new DiamondEffectRange(1, 5)) {
			if (delta.getY() == 0) {
				Element element = new Element(delta.getX(), delta.getZ());
				elementList.add(element);
				elementMap.put(ShortPairHashed.key((short) element.dX, (short) element.dZ), element);
			}
		}
		for (int i = 0; i < 4; i++) {
			center[i] = getElement(BlockFaces.NESW.get(i));
			center[i].mainDirection.add(new AtomicBoolean(true));
		}
		// make basic x/z connections
		for (Element element : this.elementList) {
			if (element.center) {
				continue;
			}
			if (element.dX == 0) {
				if (element.absdZ <= 3) {
					// 4x middle connections
					link(element, -1, 0);
					link(element, 1, 0);
				}
			} else {
				// Simple connections
				link(element, -Integer.signum(element.dX), 0);
			}
			if (element.dZ == 0) {
				if (element.absdX <= 3) {
					// 4x middle connections
					link(element, 0, -1);
					link(element, 0, 1);
				}
			} else {
				// Simple connections
				link(element, 0, -Integer.signum(element.dZ));
			}
			// Advanced
			if (element.absdX == 2 && element.absdZ == 1) {
				link(element, 0, element.dZ);
			}
			if (element.absdZ == 2 && element.absdX == 1) {
				link(element, element.dX, 0);
			}
		}
	}

	public static final LiquidModel INSTANCE = new LiquidModel();

	private BlockFace[] getFaces() {
		List<BlockFace> faces = new ArrayList<BlockFace>(4);
		for (int i = 0; i < 4; i++) {
			if (this.possible[i]) {
				faces.add(BlockFaces.NESW.get(i));
			}
		}
		return faces.toArray(new BlockFace[0]);
	}

	public synchronized BlockFace[] getHoleDirections(Block block) {
		return this.getHoleDirections(block.getWorld(), block.getX(), block.getY(), block.getZ());
	}

	public synchronized BlockFace[] getHoleDirections(World world, int x, int y, int z) {
		// Load center
		boolean instant = false; // for faster generation
		for (int i = 0; i < 4; i++) {
			this.center[i].load(world, x, y, z);
			instant |= (this.possible[i] = this.center[i].state == State.HOLE);
			this.mainDirections[i].set(this.center[i].state == State.OPEN);
		}
		if (!instant) {
			// Load other elements
			for (Element element : elementList) {
				if (element.center) {
					continue;
				}
				element.load(world, x, y, z);
			}
			// Spread to neighbors
			for (int c = 0; c < 4; c++) {
				for (int i = elementList.size() - 1; i >= 0; i--) {
					elementList.get(i).spreadHole();
				}
			}
			// Get minimum distance
			int distance = Integer.MAX_VALUE;
			for (Element elem : this.center) {
				if (elem.holeDistance < distance) {
					distance = elem.holeDistance;
				}
			}
			if (distance == Integer.MAX_VALUE) {
				// No hole, flow in all directions
				Arrays.fill(this.possible, true);
			} else {
				// Flow to all hole directions
				for (int i = 0; i < 4; i++) {
					this.possible[i] = this.center[i].holeDistance <= distance;
				}
			}
		}
		return getFaces();
	}
}
