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
package org.spout.vanilla.entity.object.falling;

import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.discrete.Point;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.block.BlockFace;
import org.spout.vanilla.VanillaMaterials;
import org.spout.vanilla.entity.object.Falling;

public class FallingBlock extends Falling {
	private final BlockMaterial block;

	public FallingBlock(BlockMaterial block) {
		this.block = block;
	}

	@Override
	public void onAttached() {
		super.onAttached();
	}

	@Override
	public void onTick(float dt) {
		if (getParent() == null || getParent().getWorld() == null) {
			return;
		}

		Point position = getParent().getPosition();
		if (position == null) {
			return;
		}

		Block block = position.getWorld().getBlock(position).move(BlockFace.BOTTOM);
		if (block.getMaterial() == VanillaMaterials.AIR || block.getMaterial().isLiquid()) {
			getParent().translate(0f, -.004f, 0f); //gravity
		} else {
			block.move(BlockFace.TOP).setMaterial(this.block);
			getParent().kill();
		}

		super.onTick(dt);
	}
}
