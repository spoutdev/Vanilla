/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011 Spout LLC <http://www.spout.org/>
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

import org.spout.api.event.Cause;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.block.BlockFaces;

import org.spout.math.vector.Vector3f;
import org.spout.vanilla.component.block.material.Skull;
import org.spout.vanilla.data.MoveReaction;
import org.spout.vanilla.material.VanillaBlockMaterial;
import org.spout.vanilla.material.block.attachable.Attachable;
import org.spout.vanilla.util.PlayerUtil;

public class SkullBlock extends VanillaBlockMaterial implements Attachable {
	public static final SkullBlock SKELETON_SKULL = new SkullBlock("Skeleton Skull", 144);
	public static final SkullBlock WITHER_SKELETON_SKULL = new SkullBlock("Wither Skeleton Skull", 1, SKELETON_SKULL);
	public static final SkullBlock ZOMBIE_HEAD = new SkullBlock("Zombie Head", 2, SKELETON_SKULL);
	public static final SkullBlock HEAD = new SkullBlock("Head", 3, SKELETON_SKULL);
	public static final SkullBlock CREEPER_HEAD = new SkullBlock("Creeper Head", 4, SKELETON_SKULL);

	private SkullBlock(String name, int id) {
		//TODO: Box Shape
		super((short) 0x7, name, id, null, null, Skull.class);
		this.setHardness(1.0F).setResistance(5.0F).setTransparent();
	}

	private SkullBlock(String name, int data, SkullBlock parent) {
		//TODO: Box Shape
		super(name, parent.getId(), data, parent, null, null);
		this.setHardness(1.0F).setResistance(3.0F).setTransparent();
	}

	@Override
	public MoveReaction getMoveReaction(Block block) {
		return MoveReaction.DENY;
	}

	@Override
	public void setAttachedFace(Block block, BlockFace attachedFace, Cause<?> cause) {
		if (attachedFace == BlockFace.BOTTOM) {
			// Attached to the ground
			block.setData(0x1, cause);

			// This below bit is completely wrong - rotation is not stored in the block data
			// It is stored in the block component instead
			// It should be set to 0x1, but it needs to be fully tested first
			// Skull type needs to be set somewhere too, most likely in the block component
			// TODO: Finish this off...
			/*
			short data = 0;
			Object source = cause.getSource();
			if (source instanceof Entity) {
				Vector3 direction = block.getPosition().subtract(((Entity) source).getTransform().getPosition());
				float rotation = direction.rotationTo(Vector3.RIGHT).getYaw();
				rotation = rotation / 360f * 16f;
				data = (short) rotation;
			}
			if (block.setMaterial(this, data)) {
				block.queueUpdate(EffectRange.THIS);
			}
			*/
		} else {
			// Attached to a wall
			block.setData(BlockFaces.NSWE.indexOf(attachedFace, 0) + 2, cause);
		}
	}

	@Override
	public BlockFace getAttachedFace(short data) {
		if (data == 0x0) {
			return BlockFace.TOP;
		} else if (data == 0x1) {
			return BlockFace.BOTTOM;
		} else {
			return BlockFaces.NSWE.get(data - 2);
		}
	}

	@Override
	public void onPlacement(Block block, short data, BlockFace against, Vector3f clickedPos, boolean isClickedBlock, Cause<?> cause) {
		super.onPlacement(block, data, against, clickedPos, isClickedBlock, cause);
		this.setAttachedFace(block, against, cause);
		if (against == BlockFace.BOTTOM || against == BlockFace.TOP) {
			Skull skull = block.get(Skull.class);
			skull.setRotation(PlayerUtil.getYaw(cause));
		}
	}

	@Override
	public BlockFace getAttachedFace(Block block) {
		return getAttachedFace(block.getBlockData());
	}

	@Override
	public Block getBlockAttachedTo(Block block) {
		return block.translate(getAttachedFace(block));
	}

	@Override
	public boolean canSupport(BlockMaterial material, BlockFace face) {
		return false;
	}

	@Override
	public boolean canAttachTo(Block block, BlockFace face) {
		return true;
	}
}
