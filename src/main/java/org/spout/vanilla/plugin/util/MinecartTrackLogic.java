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
package org.spout.vanilla.plugin.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.block.BlockFaces;

import org.spout.vanilla.plugin.data.RailsState;
import org.spout.vanilla.plugin.material.block.rail.Rail;
import org.spout.vanilla.api.material.block.rail.RailBase;

public class MinecartTrackLogic {
	public Block block;
	public RailBase rails;
	public boolean isPowered;
	public BlockFace direction;
	public boolean changed = false;
	public MinecartTrackLogic parent = null;
	public List<MinecartTrackLogic> neighbours = new ArrayList<MinecartTrackLogic>();

	private MinecartTrackLogic(Block block, RailBase material) {
		this.block = block;
		this.rails = material;
		this.isPowered = false;
		if (this.rails instanceof Rail) {
			this.isPowered = ((Rail) material).isReceivingPower(block);
		}
		this.direction = BlockFace.THIS;
	}

	public static MinecartTrackLogic create(Block block) {
		BlockMaterial mat = block.getMaterial();
		if (!(mat instanceof RailBase)) {
			return null;
		}

		return new MinecartTrackLogic(block, (RailBase) mat);
	}

	public static MinecartTrackLogic create(World world, int x, int y, int z) {
		return create(world.getBlock(x, y, z));
	}

	/**
	 * Generates all neighbours around this rail
	 * @param deep whether to perform a deep search
	 */
	public void genNeighbours(boolean deep) {
		if (deep) {
			for (BlockFace direction : BlockFaces.NESW) {
				MinecartTrackLogic logic = this.getLogic(direction);
				if (logic != null) {
					logic.parent = this;
					logic.direction = direction;
					//find out what connections this rail has
					logic.genNeighbours(false);
					if (logic.neighbours.size() >= 2) {
						continue; //already connected to two misc track pieces
					}
					MinecartTrackLogic self = logic.getLogic(logic.direction.getOpposite());
					self.parent = logic;
					self.direction = logic.direction.getOpposite();
					logic.neighbours.add(self);
					this.neighbours.add(logic);
				}
			}
		} else {
			for (BlockFace direction : this.getState().getDirections()) {
				if (direction == this.direction.getOpposite()) {
					continue;
				}
				MinecartTrackLogic logic = this.getLogic(direction);
				if (logic != null) {
					logic.parent = this;
					logic.direction = direction;
					if (logic.getState().isConnected(direction.getOpposite())) {
						this.neighbours.add(logic);
					}
				}
			}
		}
	}

	public boolean setState(RailsState state) {
		if (state == this.getState()) {
			return false;
		}

		this.rails.setState(this.block, state);
		this.changed = true;
		return true;
	}

	public RailsState getState() {
		return this.rails.getState(this.block);
	}

	/**
	 * Tries to find a neighbour in the direction given.
	 * Returns null if none was found
	 * @param direction
	 */
	public MinecartTrackLogic getNeighbour(BlockFace direction) {
		for (MinecartTrackLogic logic : this.neighbours) {
			if (logic.direction == direction) {
				return logic;
			}
		}
		return null;
	}

	public MinecartTrackLogic getLogic(BlockFace direction) {
		Block block = this.block.translate(direction);
		MinecartTrackLogic logic;
		logic = create(block);
		if (logic != null) {
			return logic;
		}
		logic = create(block.translate(BlockFace.TOP));
		if (logic != null) {
			return logic;
		}
		logic = create(block.translate(BlockFace.BOTTOM));
		return logic;
	}

	public boolean setDirection(BlockFace dir1, BlockFace dir2) {
		return this.setState(RailsState.get(dir1, dir2));
	}

	public boolean setDirection(BlockFace direction, boolean sloped) {
		return this.setState(RailsState.get(direction, sloped));
	}

	public String toString() {
		return this.block.toString() + " neighbours: " + this.neighbours.size();
	}

	/**
	 * Connects the two neighbouring rail using this piece of track
	 * It is allowed to pass in null for the rail to connect to one rail, or to none
	 * @param rails1
	 * @param rails2
	 */
	public void connect(MinecartTrackLogic rails1, MinecartTrackLogic rails2) {
		if (rails1 == null || rails1 == rails2) {
			rails1 = rails2;
			rails2 = null;
		}
		if (rails1 == null) {
			//both rail null - switch to default
			this.setState(RailsState.WEST);
		} else if (rails2 == null) {
			//connect to rail 1
			this.setDirection(rails1.direction, rails1.block.getY() > this.block.getY());
		} else if (rails1.direction == rails2.direction.getOpposite()) {
			//connect the two rail, sloped if needed
			if (rails1.block.getY() > this.block.getY()) {
				this.setDirection(rails1.direction, true);
			} else if (rails2.block.getY() > this.block.getY()) {
				this.setDirection(rails2.direction, true);
			} else {
				this.setDirection(rails1.direction, false);
			}
		} else if (this.rails.canCurve()) {
			this.setDirection(rails1.direction, rails2.direction);
		} else {
			this.setDirection(rails1.direction, rails1.block.getY() > this.block.getY());
		}
	}

	/**
	 * Connects this neighbour to it's parent automatically
	 */
	public void connect() {
		this.connect(this.direction.getOpposite());
	}

	/**
	 * Tries to connect this rail to the given direction
	 */
	public void connect(BlockFace direction) {
		if (this.neighbours.isEmpty()) {
			this.setState(RailsState.WEST);
			return;
		}
		MinecartTrackLogic from = this.getNeighbour(direction);
		if (from == null) {
			from = this.neighbours.get(0);
			direction = from.direction;
		}
		//try to find a rail on a 90-degree angle
		for (BlockFace face : BlockFaces.NESW) {
			if (face != direction && face != direction.getOpposite()) {
				MinecartTrackLogic logic = this.getNeighbour(face);
				if (logic != null) {
					this.connect(from, logic);
					return;
				}
			}
		}
		//try to use the opposite, null is handled
		this.connect(from, this.getNeighbour(direction.getOpposite()));
	}

	public void refresh() {
		//Update this track piece based on environment
		this.neighbours.clear();
		this.genNeighbours(true);

		if (this.neighbours.size() == 1) {
			//align tracks straight to face this direction
			MinecartTrackLogic to = this.neighbours.get(0);
			this.connect(to, null);
			to.connect();
		} else if (this.neighbours.size() == 2 || this.neighbours.size() == 4) {
			//try to make a fixed curve
			MinecartTrackLogic r1 = this.neighbours.get(0);
			MinecartTrackLogic r2 = this.neighbours.get(1);
			this.connect(r1, r2);
			r1.connect();
			r2.connect();
		} else if (this.neighbours.size() == 3) {
			//sort neighbours: middle at index 1
			BlockFace middle = this.neighbours.get(1).direction.getOpposite();
			if (middle == this.neighbours.get(2).direction) {
				//dirs[1] need to be swapped with dirs[0]
				Collections.swap(this.neighbours, 1, 0);
			} else if (middle == this.neighbours.get(0).direction) {
				//dirs[1] need to be swapped with dirs[2]
				Collections.swap(this.neighbours, 1, 2);
			}

			//this will ALWAYS be a curve leading to [1]
			//pick [0] or [2]?
			MinecartTrackLogic from = this.neighbours.get(1);
			MinecartTrackLogic to;
			if (this.isPowered) {
				to = this.neighbours.get(0);
			} else {
				to = this.neighbours.get(2);
			}
			this.connect(from, to);
			from.connect();
			to.connect();
		}
	}
}
