/*
 * This file is part of vanilla (http://www.spout.org/).
 *
 * vanilla is licensed under the SpoutDev License Version 1.
 *
 * vanilla is free software: you can redistribute it and/or modify
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
 * the MIT license and the SpoutDev license version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license,
 * including the MIT license.
 */
package org.spout.vanilla.controller.action;

import org.spout.api.entity.Entity;
import org.spout.api.entity.action.EntityAction;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.discrete.Point;
import org.spout.api.inventory.ItemStack;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.block.BlockFace;
import org.spout.api.math.Vector3;

import org.spout.vanilla.controller.object.MovingBlock;
import org.spout.vanilla.controller.object.moving.Item;

public class MovingBlockAction extends EntityAction<MovingBlock> {
	@Override
	public boolean shouldRun(Entity entity, MovingBlock block) {
		BlockMaterial mat = entity.getWorld().getBlock(entity.getPosition()).getMaterial();
		return !mat.isSolid();
	}

	@Override
	public void run(Entity entity, MovingBlock controller, float dt) {
		Point pos = entity.getPosition();
		Block block = pos.getWorld().getBlock(pos);
		if (block.clone().move(BlockFace.BOTTOM).getMaterial().isSolid()) {
			BlockMaterial mat = controller.getBlock();
			short data = mat.getData();
			//can we place here?
			if (block.getMaterial().isPlacementObstacle()) {
				//spawn an item
				Item item = new Item(new ItemStack(mat, data, 1), Vector3.ZERO);
				block.getWorld().createAndSpawnEntity(pos, item);
			} else {
				block.setMaterial(mat, data);
			}
			entity.kill();
		} else {
			controller.move(new Vector3(0, -0.50f, 0));
		}
	}
}
