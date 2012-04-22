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
import org.spout.api.collision.BoundingBox;
import org.spout.api.collision.CollisionStrategy;
import org.spout.api.geo.World;
import org.spout.api.material.block.BlockFace;

import org.spout.vanilla.material.block.attachable.GroundAttachable;
import org.spout.vanilla.util.MinecartTrackLogic;

public abstract class MinecartTrackBase extends GroundAttachable {
	public MinecartTrackBase(String name, int id) {
		super(name, id);
		this.setCollision(CollisionStrategy.NOCOLLIDE);
		//TODO: Fix this up so we can set this area ourselves in the volume!
		BoundingBox bb = (BoundingBox) this.getBoundingArea();
		bb.set(0.0f, 0.0f, 0.0f, 1.0f, 0.125f, 1.0f);
	}

	public abstract boolean canCurve();

	public void doTrackLogic(World world, int x, int y, int z) {
		MinecartTrackLogic logic = MinecartTrackLogic.create(world, x, y, z);
		if (logic != null) {
			logic.refresh();
		}
	}

	@Override
	public boolean onPlacement(World world, int x, int y, int z, short data, BlockFace against, Source source) {
		System.out.println("PLACE");
		if (super.onPlacement(world, x, y, z, data, against, source)) {
			System.out.println("LOGIC");
			this.doTrackLogic(world, x, y, z);
			return true;
		} else {
			return false;
		}
	}
}
