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
package org.spout.vanilla.material.block.attachable;

import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.block.BlockFaces;
import org.spout.api.material.range.EffectRange;
import org.spout.api.util.flag.ByteFlagContainer;

import org.spout.vanilla.material.VanillaBlockMaterial;
import org.spout.vanilla.material.block.Attachable;

public abstract class AbstractAttachable extends VanillaBlockMaterial implements Attachable {
	protected AbstractAttachable(short dataMask, String name, int id) {
		super(dataMask, name, id);
	}

	protected AbstractAttachable(String name, int id) {
		super(name, id);
	}

	public AbstractAttachable(String name, int id, int data, VanillaBlockMaterial parent) {
		super(name, id, data, parent);
	}

	private ByteFlagContainer attachableFaces = new ByteFlagContainer(BlockFaces.NONE);

	/**
	 * Gets whether a certain face is attachable
	 * @param face to get it of
	 * @return attachable state
	 */
	public boolean isAttachable(BlockFace face) {
		return this.attachableFaces.get(face);
	}

	/**
	 * Sets multiple faces attachable to true
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
	 * @param face to set
	 * @param attachable state
	 * @return this attachable material
	 */
	public AbstractAttachable setAttachable(BlockFace face, boolean attachable) {
		this.attachableFaces.set(face, attachable);
		return this;
	}

	@Override
	public boolean hasPhysics() {
		return true;
	}

	@Override
	public boolean canSeekAttachedAlternative() {
		return false;
	}

	@Override
	public void onUpdate(Block block) {
		super.onUpdate(block);
		if (!this.isValidPosition(block, this.getAttachedFace(block), false)) {
			this.onDestroy(block);
		}
	}

	@Override
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
	public boolean canAttachTo(BlockMaterial material, BlockFace face) {
		if (!(material instanceof VanillaBlockMaterial)) {
			return false;
		}

		return ((VanillaBlockMaterial) material).canSupport(this, face);
	}

	@Override
	public boolean canAttachTo(Block block, BlockFace face) {
		return this.isAttachable(face.getOpposite()) && this.canAttachTo(block.getMaterial(), face);
	}

	@Override
	public boolean canPlace(Block block, short data, BlockFace against, boolean isClickedBlock) {
		if (!super.canPlace(block, data, against, isClickedBlock)) {
			return false;
		}

		return this.isValidPosition(block, against, this.canSeekAttachedAlternative());
	}

	@Override
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
	public boolean onPlacement(Block block, short data, BlockFace against, boolean isClickedBlock) {
		if (!this.canAttachTo(block.translate(against), against.getOpposite())) {
			if (this.canSeekAttachedAlternative()) {
				against = this.findAttachedFace(block);
				if (against == null) {
					return false;
				}
			} else {
				return false;
			}
		}
		this.handlePlacement(block, data, against);
		return true;
	}

	@Override
	public void handlePlacement(Block block, short data, BlockFace against) {
		block.setMaterial(this, data);
		this.setAttachedFace(block, against);
		block.queueUpdate(EffectRange.THIS);
	}

	@Override
	public boolean canSupport(BlockMaterial material, BlockFace face) {
		return false;
	}
}
