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
package org.spout.vanilla.material.block.attachable;

import org.spout.api.Source;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.Material;
import org.spout.api.material.block.BlockFace;

import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.generic.Solid;
import org.spout.vanilla.material.block.generic.VanillaBlockMaterial;

public abstract class AbstractAttachable extends VanillaBlockMaterial implements Attachable {
	protected AbstractAttachable(String name, int id) {
		super(name, id);
	}

	public AbstractAttachable(String name, int id, int data, Material parent) {
		super(name, id, data, parent);
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
		if (getBlockAttachedTo(block).getMaterial().equals(VanillaMaterials.AIR)) {
			block.setMaterial(VanillaMaterials.AIR).update(true);
		}
	}

	@Override
	public Block getBlockAttachedTo(Block block) {
		BlockFace base = getFaceAttachedTo(block.getData());
		return block.translate(base);
	}

	@Override
	public boolean canAttachTo(BlockMaterial material, BlockFace face) {
		return material instanceof Solid;
	}
	
	@Override
	public boolean canAttachTo(Block block, BlockFace face) {
		return this.canAttachTo(block.translate(face.getOpposite()).getMaterial(), face);
	}

	@Override
	public boolean canPlace(Block block, short data, BlockFace against, Source source) {
		if (super.canPlace(block, data, against, source)) {
			if (this.canAttachTo(block, against.getOpposite())) {
				return true;
			} else if (this.canSeekAttachedAlternative()) {
				for (BlockFace face : BlockFace.values()) {
					if (this.canAttachTo(block, face)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	public boolean onPlacement(Block block, short data, BlockFace against, Source source) {
		if (this.canAttachTo(block, against.getOpposite())) {
			return super.onPlacement(block, this.getDataForFace(against.getOpposite()), against, source);
		} else if (this.canSeekAttachedAlternative()) {
			for (BlockFace face : BlockFace.values()) {
				if (this.canAttachTo(block, face)) {
					return super.onPlacement(block, this.getDataForFace(face), face, source);
				}
			}
		}
		return false;
	}
}
