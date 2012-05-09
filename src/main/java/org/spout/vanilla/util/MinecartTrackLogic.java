/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, SpoutDev <http://www.spout.org/>
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
package org.spout.vanilla.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.spout.api.Source;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.block.BlockFaces;

import org.spout.vanilla.material.VanillaBlockMaterial;
import org.spout.vanilla.material.block.data.Rails;
import org.spout.vanilla.material.block.other.MinecartTrack;

public class MinecartTrackLogic implements Source {
	public int x, y, z;
	public World world;
	public Rails data;
	public boolean isPowered;
	public BlockFace direction;
	public boolean changed = false;
	public MinecartTrackLogic parent = null;
	public List<MinecartTrackLogic> neighbours = new ArrayList<MinecartTrackLogic>();

	private MinecartTrackLogic(World world, int x, int y, int z, Rails data) {
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
		this.isPowered = ((VanillaBlockMaterial) data.getMaterial()).getIndirectRedstonePower(world.getBlock(x, y, z)) > 0;
		this.data = data;
		this.direction = BlockFace.THIS;
	}

	public static MinecartTrackLogic create(Block block) {
		BlockMaterial mat = block.getMaterial();
		if (mat instanceof MinecartTrack) {
			Rails rails = ((MinecartTrack) mat).createData(block.getData());
			return new MinecartTrackLogic(block.getWorld(), block.getX(), block.getY(), block.getZ(), rails);
		} else {
			return null;
		}
	}

	public static MinecartTrackLogic create(World world, int x, int y, int z) {
		return create(world.getBlock(x, y, z));
	}

	/**
	 * Generates all neighbours around this rails
	 * @param deep whether to perform a deep search
	 */
	public void genNeighbours(boolean deep) {
		if (deep) {
			for (BlockFace direction : BlockFaces.NESW) {
				MinecartTrackLogic logic = this.getLogic(direction);
				if (logic != null) {
					logic.parent = this;
					logic.direction = direction;
					//find out what connections this rails has
					logic.genNeighbours(false);
					if (logic.neighbours.size() >= 2) {
						continue; //already connected to two other track pieces
					}
					MinecartTrackLogic self = logic.getLogic(logic.direction.getOpposite());
					self.data = this.data;
					self.parent = logic;
					self.direction = logic.direction.getOpposite();
					logic.neighbours.add(self);
					this.neighbours.add(logic);
				}
			}
		} else {
			for (BlockFace direction : this.data.getDirections()) {
				if (direction == this.direction.getOpposite()) {
					continue;
				}
				MinecartTrackLogic logic = this.getLogic(direction);
				if (logic != null) {
					logic.parent = this;
					logic.direction = direction;
					if (logic.data.isConnected(direction.getOpposite())) {
						this.neighbours.add(logic);
					}
				}
			}
		}
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
		int x = this.x + (int) direction.getOffset().getX();
		int y = this.y + (int) direction.getOffset().getY();
		int z = this.z + (int) direction.getOffset().getZ();
		MinecartTrackLogic logic;
		logic = create(this.world, x, y, z);
		if (logic != null) {
			return logic;
		}
		logic = create(this.world, x, y + 1, z);
		if (logic != null) {
			return logic;
		}
		logic = create(this.world, x, y - 1, z);
		return logic;
	}

	public boolean setDirection(BlockFace dir1, BlockFace dir2) {
		return this.setDirection(RailsState.get(dir1, dir2));
	}

	public boolean setDirection(BlockFace direction, boolean sloped) {
		return this.setDirection(RailsState.get(direction, sloped));
	}

	public boolean setDirection(RailsState state) {
		System.out.println("SETTING TO " + state);
		if (state != this.data.getState()) {
			this.data.setState(state);
			this.changed = true;
			return true;
		} else {
			return false;
		}
	}

	public String toString() {
		return this.world.getName() + " " + this.x + "/" + this.y + "/" + this.z + " neighbours: " + this.neighbours.size();
	}

	/**
	 * Connects the two neighbouring rails using this piece of track
	 * It is allowed to pass in null for the rails to connect to one rails, or to none
	 * @param rails1
	 * @param rails2
	 */
	public void connect(MinecartTrackLogic rails1, MinecartTrackLogic rails2) {
		if (rails1 == null || rails1 == rails2) {
			rails1 = rails2;
			rails2 = null;
		}
		if (rails1 == null) {
			//both rails null - switch to default
			this.setDirection(RailsState.WEST);
		} else if (rails2 == null) {
			//connect to rails 1
			this.setDirection(rails1.direction, rails1.y > this.y);
		} else if (rails1.direction == rails2.direction.getOpposite()) {
			//connect the two rails, sloped if needed
			if (rails1.y > this.y) {
				this.setDirection(rails1.direction, true);
			} else if (rails2.y > this.y) {
				this.setDirection(rails2.direction, true);
			} else {
				this.setDirection(rails1.direction, false);
			}
		} else if (this.data.canCurve()) {
			this.setDirection(rails1.direction, rails2.direction);
		} else {
			this.setDirection(rails1.direction, rails1.y > this.y);
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
			this.setDirection(RailsState.WEST);
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

		System.out.println("Refreshing: " + this.toString());

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
				//dirs[1] need to be swapped with dirs[2]
				Collections.swap(this.neighbours, 1, 2);
			} else if (middle == this.neighbours.get(0).direction) {
				//dirs[1] need to be swapped with dirs[0]
				Collections.swap(this.neighbours, 1, 0);
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
		this.refreshData();
	}

	private void refreshData() {
		if (this.changed) {
			this.world.getBlock(this.x, this.y, this.z).setData(data).update(true);
		}
		for (MinecartTrackLogic logic : this.neighbours) {
			logic.refreshData();
		}
	}
}
