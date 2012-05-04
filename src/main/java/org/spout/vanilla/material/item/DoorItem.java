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
package org.spout.vanilla.material.item;

import org.spout.api.entity.Entity;
import org.spout.api.event.player.PlayerInteractEvent.Action;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.block.BlockFace;
import org.spout.api.math.MathHelper;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.DoorBlock;
import org.spout.vanilla.material.block.generic.Solid;
import org.spout.vanilla.material.item.generic.VanillaItemMaterial;

public class DoorItem extends VanillaItemMaterial {
	private DoorBlock doorBlock;

	public DoorItem(String name, int id, DoorBlock woodenDoorBlock) {
		super(name, id);
		this.doorBlock = woodenDoorBlock;
	}

	@Override
	public void onInteract(Entity entity, Block block, Action type, BlockFace clickedFace) {
		super.onInteract(entity, block, type, clickedFace);
		if (clickedFace != BlockFace.TOP) {
			return;
		}
		Block b = block.translate(BlockFace.BOTTOM);

		if (b.getMaterial() instanceof Solid && (b = b.translate(BlockFace.TOP)).getMaterial() == VanillaMaterials.AIR && (b = b.translate(BlockFace.TOP)).getMaterial() == VanillaMaterials.AIR) {
			System.out.println("Placing door!");
		}
		/*
		 * Formula kinda copied from minecraft source
		 * - and that is terrible...this needs more functions!
		 * * Entity yaw to blockface
		 * * Blockface to sub-door-material ('hinge?')
		 */
		short hinge = (short) ((MathHelper.floor((double) ((entity.getYaw() + 180F) * 4F) - 0.5)) & 3);
		placeDoorBlock(block, hinge, doorBlock);
	}

	public static void placeDoorBlock(Block block, short hinge, DoorBlock doorBlock) {
		block.setMaterial(doorBlock, hinge);
		block.translate(BlockFace.TOP).setMaterial(doorBlock, (short) (hinge | 0x8));
	}
}
