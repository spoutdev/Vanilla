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
package org.spout.vanilla.material.block.interactive;

import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.Material;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.block.BlockFaces;

import org.spout.vanilla.material.block.attachable.AbstractAttachable;
import org.spout.vanilla.material.block.attachable.PointAttachable;

public class StoneButton extends AbstractAttachable implements PointAttachable {
	public StoneButton(String name, int id, int data, Material parent) {
		super(name, id, data, parent);
		this.setAttachable(BlockFaces.NESW);
	}

	public StoneButton(String name, int id) {
		super(name, id);
		this.setAttachable(BlockFaces.NESW);
	}

	@Override
	public boolean canAttachTo(Block block, BlockFace face) {
		return face != BlockFace.TOP && super.canAttachTo(block, face);
	}

	@Override
	public void setAttachedFace(Block block, BlockFace attachedFace) {
		block.setData((short) (BlockFaces.indexOf(BlockFaces.NSEW, attachedFace, 3) + 1));
	}

	@Override
	public BlockFace getAttachedFace(Block block) {
		return BlockFaces.get(BlockFaces.NSEW, (block.getData() & ~0x8) - 1);
	}

	@Override
	public boolean isPlacementSuppressed() {
		return true;
	}
}
