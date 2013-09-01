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
package org.spout.vanilla.material.block.attachable;

import org.spout.api.component.block.BlockComponent;
import org.spout.api.event.Cause;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.block.BlockFaces;
import org.spout.api.util.bytebit.ByteBitSet;

import org.spout.math.vector.Vector3;
import org.spout.physics.collision.shape.CollisionShape;
import org.spout.vanilla.material.VanillaBlockMaterial;

public abstract class AbstractAttachable extends VanillaBlockMaterial implements Attachable {
	private final ByteBitSet attachableFaces = new ByteBitSet(BlockFaces.NONE);

	protected AbstractAttachable(short dataMask, String name, int id, String model, CollisionShape shape, Class<? extends BlockComponent>... components) {
		super(dataMask, name, id, model, shape, components);
	}

	protected AbstractAttachable(String name, int id, String model, CollisionShape shape, Class<? extends BlockComponent>... components) {
		super(name, id, model, shape, components);
	}

	public AbstractAttachable(String name, int id, int data, VanillaBlockMaterial parent, String model, CollisionShape shape, Class<? extends BlockComponent>... components) {
		super(name, id, data, parent, model, shape, components);
	}

	/**
	 * Gets whether a certain face is attachable
	 *
	 * @param face to get it of
	 * @return attachable state
	 */
	public boolean isAttachable(BlockFace face) {
		return this.attachableFaces.get(face);
	}

	/**
	 * Sets multiple faces attachable to true
	 *
	 * @param faces to set
	 * @return this attachable material
	 */
	public AbstractAttachable setAttachable(BlockFace... faces) {
		for (BlockFace face : faces) {
			this.setAttachable(face, true);
		}
		return this;
	}

	/**
	 * Sets multiple faces attachable to true
	 *
	 * @param faces to set
	 * @return this attachable material
	 */
	public AbstractAttachable setAttachable(BlockFaces faces) {
		for (BlockFace face : faces) {
			this.setAttachable(face, true);
		}
		return this;
	}

	/**
	 * Sets whether a certain face is attachable
	 *
	 * @param face to set
	 * @param attachable state
	 * @return this attachable material
	 */
	public AbstractAttachable setAttachable(BlockFace face, boolean attachable) {
		this.attachableFaces.set(face, attachable);
		return this;
	}

	@Override
	public BlockFace getAttachedFace(Block block) {
		return this.getAttachedFace(block.getBlockData());
	}

	@Override
	public boolean hasPhysics() {
		return true;
	}

	/**
	 * Whether this material seeks an alternative attachment block upon placement
	 */
	public boolean canSeekAttachedAlternative() {
		return false;
	}

	@Override
	public void onUpdate(BlockMaterial oldMaterial, Block block) {
		super.onUpdate(oldMaterial, block);
		if (!this.isValidPosition(block, this.getAttachedFace(block), false)) {
			this.destroy(block, toCause(block));
		}
	}

	/**
	 * Finds out what face this attachable can properly attach to<br> The north-east-south-west-bottom-top search pattern is used.
	 *
	 * @param block of the attachable
	 * @return the attached face, or null if not found
	 */
	public BlockFace findAttachedFace(Block block) {
		for (BlockFace face : BlockFaces.NESWBT) {
			if (this.canAttachTo(block.translate(face), face.getOpposite())) {
				return face;
			}
		}
		return null;
	}

	@Override
	public Block getBlockAttachedTo(Block block) {
		return block.translate(this.getAttachedFace(block));
	}

	@Override
	public boolean canAttachTo(Block block, BlockFace face) {
		if (!this.isAttachable(face.getOpposite())) {
			return false;
		}
		BlockMaterial material = block.getMaterial();
		if (!(material instanceof VanillaBlockMaterial)) {
			return false;
		}
		return ((VanillaBlockMaterial) material).canSupport(this, face);
	}

	@Override
	public boolean canPlace(Block block, short data, BlockFace against, Vector3 clickedPos, boolean isClickedBlock, Cause<?> cause) {
		if (!super.canPlace(block, data, against, clickedPos, isClickedBlock, cause)) {
			return false;
		}
		return this.isValidPosition(block, against, this.canSeekAttachedAlternative());
	}

	/**
	 * Checks if this attachable is at a position it can actually be<br> This is called in the underlying physics function to check if the block has to be broken<br> No checks on the block itself should
	 * be performed other than the face it is attached to
	 *
	 * @param block to place at
	 * @param attachedFace to use
	 * @param seekAlternative whether an alternative attached face should be sought
	 * @return whether placement is possible
	 */
	public boolean isValidPosition(Block block, BlockFace attachedFace, boolean seekAlternative) {
		if (this.canAttachTo(block.translate(attachedFace), attachedFace.getOpposite())) {
			return true;
		} else if (seekAlternative) {
			attachedFace = this.findAttachedFace(block);
			if (attachedFace != null) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void onPlacement(Block block, short data, BlockFace against, Vector3 clickedPos, boolean isClickedBlock, Cause<?> cause) {
		if (!this.canAttachTo(block.translate(against), against.getOpposite())) {
			against = this.findAttachedFace(block);
		}
		super.onPlacement(block, data, against, clickedPos, isClickedBlock, cause);
		this.setAttachedFace(block, against, cause);
	}

	@Override
	public boolean canSupport(BlockMaterial material, BlockFace face) {
		return false;
	}
}
