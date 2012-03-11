/*
 * This file is part of Vanilla (http://www.spout.org/).
 *
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
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.block.BlockFace;
import org.spout.vanilla.material.block.MinecartTrack;
import org.spout.vanilla.material.block.data.Rails;
import org.spout.vanilla.material.generic.GenericBlock;

public class MinecartTrackLogic implements Source {
	public int x, y, z;
	public World world;
	public Rails data;
	public boolean isPowered;
	public BlockFace direction;
	public boolean changed = false;
	public List<MinecartTrackLogic> neighbours = new ArrayList<MinecartTrackLogic>();

	private MinecartTrackLogic(World world, int x, int y, int z, Rails data) {
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
		this.isPowered = ((GenericBlock) data.getMaterial()).getIndirectRedstonePower(world, x, y, z) > 0;
		this.data = data;
		this.direction = BlockFace.THIS;
	}

	public static MinecartTrackLogic create(World world, int x, int y, int z) {
		BlockMaterial mat = world.getBlockMaterial(x, y, z);
		if (mat instanceof MinecartTrack) {
			Rails rails = ((MinecartTrack) mat).createData(world.getBlockData(x, y, z));
			return new MinecartTrackLogic(world, x, y, z, rails);
		} else {
			return null;
		}
	}

	public MinecartTrackLogic addNeighbour(BlockFace direction, boolean genSubNeighbours) {
		MinecartTrackLogic logic = this.getLogic(direction);
		if (logic != null) {
			//find out what connections this rails has
			if (genSubNeighbours) {
				for (BlockFace conn : logic.data.getDirections()) {
					MinecartTrackLogic neigh = logic.addNeighbour(conn, false);
					if (conn == direction.getOpposite() && neigh != null) {
						neigh.data = this.data;
					}
				}
				if (!logic.data.isConnected(direction.getOpposite())) {
					if (logic.neighbours.size() >= 2) {
						return null;
					}
				}
			}
			logic.direction = direction;
			this.neighbours.add(logic);
		}
		return logic;
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

	public BlockFace getDirection(MinecartTrackLogic to) {
		if (to.direction == BlockFace.THIS) {
			return this.direction.getOpposite();
		} else {
			return to.direction;
		}
	}

	public String toString() {
		return this.world.getName() + " " + this.x + "/" + this.y + "/" + this.z + " neighbours: " + this.neighbours.size();
	}

	public void refresh() {
		this.refresh(true);
	}

	private void refresh(boolean isMain) {
		//Update this track piece based on environment
		this.neighbours.clear();
		this.addNeighbour(BlockFace.NORTH, true);
		this.addNeighbour(BlockFace.EAST, true);
		this.addNeighbour(BlockFace.SOUTH, true);
		this.addNeighbour(BlockFace.WEST, true);
		if (this.neighbours.size() == 3) {
			//sort neighbours: middle at index 1
			BlockFace middle = this.neighbours.get(1).direction.getOpposite();
			if (middle == this.neighbours.get(2).direction) {
				//dirs[1] need to be swapped with dirs[2]
				Collections.swap(this.neighbours, 1, 2);
			} else if (middle == this.neighbours.get(0).direction) {
				//dirs[1] need to be swapped with dirs[0]
				Collections.swap(this.neighbours, 1, 0);
			}
		}

		System.out.println(this.toString());
		if (this.neighbours.size() == 1) {
			//align tracks straight to face this direction
			MinecartTrackLogic to = this.neighbours.get(0);
			BlockFace direction = this.getDirection(to);
			this.setDirection(direction, to.y > this.y);
		} else if (this.neighbours.size() == 2) {
			//try to make a fixed curve
			MinecartTrackLogic r1 = this.neighbours.get(0);
			MinecartTrackLogic r2 = this.neighbours.get(1);
			if (r1.direction.getOpposite() == r2.direction) {
				//straight track
				//needs slope?
				if (r1.y > this.y) {
					if (this.setDirection(r1.direction, true) && isMain) {
						r1.refresh(false);
					}
				} else if (r2.y > this.y) {
					if (this.setDirection(r2.direction, true) && isMain) {
						r2.refresh(false);
					}
				} else {
					if (this.setDirection(r1.direction, false) && isMain) {
						r1.refresh(false);
					}
				}
			} else if (this.data.canCurve()) {
				//curve
				if (this.setDirection(r1.direction, r2.direction) && isMain) {
					r1.refresh(false);
					r2.refresh(false);
				}
			} else {
				//face to r1 anyway
				if (this.setDirection(r1.direction, r1.y > this.y) && isMain) {
					r1.refresh(false);
				}
			}
		} else if (this.neighbours.size() == 3) {
			//this will ALWAYS be a curve leading to [1]
			//pick [0] or [2]?
			BlockFace main = this.neighbours.get(1).direction;
			MinecartTrackLogic to;
			if (this.isPowered) {
				to = this.neighbours.get(0);
			} else {
				to = this.neighbours.get(2);
			}
			if (this.setDirection(main, to.direction) && isMain) {
				to.refresh(false);
			}
		}
		if (isMain) {
			this.refreshData();
		}
	}

	private void refreshData() {
		if (this.changed) {
			this.world.setBlockData(this.x, this.y, this.z, this.data.getData(), false, this);
		}
		for (MinecartTrackLogic logic : this.neighbours) {
			logic.refreshData();
		}
	}
}
