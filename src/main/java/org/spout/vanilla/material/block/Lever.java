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
package org.spout.vanilla.material.block;

import org.spout.api.Source;
import org.spout.api.entity.Entity;
import org.spout.api.event.player.PlayerInteractEvent.Action;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.Material;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.block.BlockFaces;
import org.spout.api.math.Vector3;

import org.spout.vanilla.material.block.attachable.AbstractAttachable;

public class Lever extends AbstractAttachable {
	public Lever(String name, int id, int data, Material parent) {
		super(name, id, data, parent);
		this.setAttachable(BlockFaces.NESWB);
	}

	public Lever(String name, int id) {
		super(name, id);
		this.setAttachable(BlockFaces.NESWB);
	}

	@Override
	public boolean isPlacementSuppressed() {
		return true;
	}

	@Override
	public void onInteractBy(Entity entity, Block block, Action type, BlockFace clickedFace) {
		super.onInteractBy(entity, block, type, clickedFace);
		this.toggle(block);
	}

	public boolean isToggled(Block block) {
		return (block.getData() & 0x8) == 0x8;
	}

	public void setToggled(Block block, boolean toggled) {
		this.setToggled(block, toggled, true);
	}

	public void setToggled(Block block, boolean toggled, boolean update) {
		short data = block.getData();
		if (toggled) {
			data |= 0x8;
		} else {
			data &= ~0x8;
		}
		block.setData(data);
		if (update) {
			block.update();
		}
	}

	public void toggle(Block block) {
		this.toggle(block, true);
	}

	public void toggle(Block block, boolean update) {
		short data = block.getData();
		if ((data & 0x8) == 0x8) {
			data &= ~0x8;
		} else {
			data |= 0x8;
		}
		block.setData(data);
		if (update) {
			block.update();
		}
	}

	public static short datat = 0;

	@Override
	public void setAttachedFace(Block block, BlockFace attachedFace) {
		short data;
		if (attachedFace == BlockFace.BOTTOM) {
			Source source = block.getSource();
			data = (short) (5 + Math.random());
			if (source instanceof Entity) {
				//set data using direction
				Vector3 direction = block.getPosition().subtract(((Entity) source).getPosition());
				direction = direction.abs();
				if (direction.getX() > direction.getZ()) {
					data = 6;
				} else {
					data = 5;
				}
			}
		} else {
			data = (short) (BlockFaces.indexOf(BlockFaces.NSEW, attachedFace, 0) + 1);
		}
		block.setData(data);
	}

	@Override
	public BlockFace getAttachedFace(Block block) {
		return BlockFaces.get(BlockFaces.NSEWB, (block.getData() & ~0x8) - 1);
	}
}
