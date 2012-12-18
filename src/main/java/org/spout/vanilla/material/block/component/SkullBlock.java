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
package org.spout.vanilla.material.block.component;

import org.spout.api.entity.Entity;
import org.spout.api.event.Cause;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.block.BlockFaces;
import org.spout.api.material.range.EffectRange;
import org.spout.api.math.Vector3;

import org.spout.vanilla.data.MoveReaction;
import org.spout.vanilla.material.block.attachable.AbstractAttachable;

public class SkullBlock extends AbstractAttachable {
	public static final SkullBlock SKELETON_SKULL = new SkullBlock("Skeleton Skull", 144);
	public static final SkullBlock WITHER_SKELETON_SKULL = new SkullBlock("Wither Skeleton Skull", 1, SKELETON_SKULL);
	public static final SkullBlock ZOMBIE_HEAD = new SkullBlock("Zombie Head", 2, SKELETON_SKULL);
	public static final SkullBlock HEAD = new SkullBlock("Head", 3, SKELETON_SKULL);
	public static final SkullBlock CREEPER_HEAD = new SkullBlock("Creeper Head", 4, SKELETON_SKULL);

	private SkullBlock(String name, int id) {
		super((short) 0x7, name, id, null);
		this.setAttachable(BlockFaces.NESWB).setHardness(1.0F).setResistance(3.0F).setOpaque();
	}

	private SkullBlock(String name, int data, SkullBlock parent) {
		super(name, parent.getId(), data, parent, null);
		this.setAttachable(BlockFaces.NESWB).setHardness(1.0F).setResistance(3.0F).setOpaque();
	}

	@Override
	public void handlePlacement(Block block, short data, BlockFace against, Cause<?> cause) {
		this.setAttachedFace(block, against, cause);
	}

	@Override
	public MoveReaction getMoveReaction(Block block) {
		return MoveReaction.DENY;
	}

	@Override
	public void setAttachedFace(Block block, BlockFace attachedFace, Cause<?> cause) {
		//TODO: data alteration ok? looks wrong
		if (attachedFace == BlockFace.BOTTOM) {
			short data = 0;
			Object source = cause.getSource();
			if (source instanceof Entity) {
				Vector3 direction = block.getPosition().subtract(((Entity) source).getTransform().getPosition());
				float rotation = direction.rotationTo(Vector3.RIGHT).getYaw();
				rotation = rotation / 360f * 16f;
				data = (short) rotation;
			}
			block.setMaterial(this, data).queueUpdate(EffectRange.THIS);
		} else {
			// get the data for this face
			short data = (short) (BlockFaces.NSWE.indexOf(attachedFace, 0) + 2);
			block.setMaterial(this, data).queueUpdate(EffectRange.THIS);
		}
	}

	@Override
	public BlockFace getAttachedFace(short data) {
		//TODO: can also be attached to top...
		return BlockFaces.NSWE.get(data - 2);
	}

	@Override
	public boolean canSupport(BlockMaterial material, BlockFace face) {
		return false;
	}
}
