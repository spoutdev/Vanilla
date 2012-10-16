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
package org.spout.vanilla.material.block.controlled;

import org.spout.api.Source;
import org.spout.api.component.components.BlockComponent;
import org.spout.api.entity.Entity;
import org.spout.api.geo.LoadOption;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.block.BlockFaces;
import org.spout.api.material.range.EffectRange;
import org.spout.api.math.Vector3;

import org.spout.vanilla.component.substance.material.Sign;
import org.spout.vanilla.data.MoveReaction;
import org.spout.vanilla.material.InitializableMaterial;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.attachable.AbstractAttachable;

public abstract class SignBase extends AbstractAttachable implements InitializableMaterial {
	public SignBase(String name, int id) {
		super(name, id);
		this.setAttachable(BlockFaces.NESWB).setHardness(1.0F).setResistance(1.6F).setOpacity((byte) 1);
	}

	@Override
	public void initialize() {
		getDrops().add(VanillaMaterials.SIGN);
	}

	@Override
	public void handlePlacement(Block block, short data, BlockFace attachedFace) {
		block.getWorld().createAndSpawnEntity(block.getPosition(), Sign.class, LoadOption.NO_LOAD);
		this.setAttachedFace(block, attachedFace);
	}

	@Override
	public MoveReaction getMoveReaction(Block block) {
		return MoveReaction.DENY;
	}

	@Override
	public void setAttachedFace(Block block, BlockFace attachedFace) {
		if (attachedFace == BlockFace.BOTTOM) {
			Source source = block.getSource();
			short data = 0;
			if (source instanceof Entity) {
				Vector3 direction = block.getPosition().subtract(((Entity) source).getTransform().getPosition());
				float rotation = direction.rotationTo(Vector3.RIGHT).getYaw();
				rotation = rotation / 360f * 16f;
				data = (short) rotation;
			}
			block.setMaterial(VanillaMaterials.SIGN_POST, data).queueUpdate(EffectRange.THIS);
		} else {
			// get the data for this face
			short data = (short) (BlockFaces.NSWE.indexOf(attachedFace, 0) + 2);
			block.setMaterial(VanillaMaterials.WALL_SIGN, data).queueUpdate(EffectRange.THIS);
		}
	}

	@Override
	public BlockFace getAttachedFace(short data) {
		return BlockFaces.NSWE.get(data - 2);
	}

	@Override
	public abstract boolean canSupport(BlockMaterial material, BlockFace face);

	@Override
	public BlockComponent getBlockComponent() {
		return new Sign();
	}
}
