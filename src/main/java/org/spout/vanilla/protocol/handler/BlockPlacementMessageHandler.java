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
package org.spout.vanilla.protocol.handler;

import org.spout.api.entity.PlayerController;
import org.spout.api.event.EventManager;
import org.spout.api.event.player.PlayerInteractEvent;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.discrete.Point;
import org.spout.api.inventory.Inventory;
import org.spout.api.inventory.ItemStack;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.Material;
import org.spout.api.material.block.BlockFace;
import org.spout.api.player.Player;
import org.spout.api.protocol.MessageHandler;
import org.spout.api.protocol.Session;

import org.spout.vanilla.protocol.msg.BlockChangeMessage;
import org.spout.vanilla.protocol.msg.BlockPlacementMessage;
import org.spout.vanilla.util.VanillaMessageHandlerUtils;

public final class BlockPlacementMessageHandler extends MessageHandler<BlockPlacementMessage> {
	@Override
	public void handleServer(Session session, Player player, BlockPlacementMessage message) {
		EventManager eventManager = session.getGame().getEventManager();
		World world = player.getEntity().getWorld();
		Inventory inventory = player.getEntity().getInventory();
		ItemStack holding = inventory.getCurrentItem();

		/**
		 * The notch client's packet sending is weird. Here's how it works: If
		 * the client is clicking a block not in range, sends a packet with
		 * x=-1,y=255,z=-1 If the client is clicking a block in range with an
		 * item in hand (id > 255) Sends both the normal block placement packet
		 * and a (-1,255,-1) one If the client is placing a block in range with
		 * a block in hand, only one normal packet is sent That is how it
		 * usually happens. Sometimes it doesn't happen like that. Therefore, a
		 * hacky workaround.
		 */

		// Right clicked air with an item.
		if (message.getDirection() == 255) {
			eventManager.callEvent(new PlayerInteractEvent(player, null, inventory.getCurrentItem(), PlayerInteractEvent.Action.RIGHT_CLICK, true));
			return;
		}

		// Block face is invalid
		Point pos = new Point(world, message.getX(), message.getY(), message.getZ());
		BlockFace face = VanillaMessageHandlerUtils.messageToBlockFace(message.getDirection());
		if (face == BlockFace.THIS) {
			return;
		}

		// Within world bounds
		Point offsetPos = pos.add(face.getOffset());
		Block target = world.getBlock(offsetPos);
		if (pos.getY() >= world.getHeight() || pos.getY() < 0) {
			return;
		}

		// If denied, revert the block
		boolean cancelled = false;
		PlayerInteractEvent interactEvent = eventManager.callEvent(new PlayerInteractEvent(player, new Point(pos, world), inventory.getCurrentItem(), PlayerInteractEvent.Action.RIGHT_CLICK, false));
		if (interactEvent.isCancelled()) {
			cancelled = true;
		}

		int x = offsetPos.getBlockX();
		int y = offsetPos.getBlockY();
		int z = offsetPos.getBlockZ();

		Block block = world.getBlock(pos);
		if (!cancelled) {
			block.getMaterial().onInteract(player.getEntity(), pos, PlayerInteractEvent.Action.RIGHT_CLICK, face);
		}

		// If placement is accepted
		if (holding != null && !cancelled) {

			// If it's an item, send event and go no further.
			Material placedMaterial = holding.getMaterial();
			placedMaterial.onInteract(player.getEntity(), pos, PlayerInteractEvent.Action.RIGHT_CLICK, face);

			if (!(placedMaterial instanceof BlockMaterial)) {
				return;
			}

			// Make sure the target switches one block below
			short placedData = holding.getData();
			if (face == BlockFace.TOP && !world.getBlockMaterial(x, y - 1, z).isPlacementObstacle()) {
				y = target.move(BlockFace.BOTTOM).getY();
			}

			BlockMaterial oldBlock = target.getMaterial();
			BlockMaterial newBlock = (BlockMaterial) placedMaterial;

			// Remove block from inventory if not in creative mode.
			if (!((PlayerController) player.getEntity().getController()).hasInfiniteResources()) {
				holding.setAmount(holding.getAmount() - 1);
				inventory.setItem(holding, inventory.getCurrentSlot());
			}

			// Make sure the block can be placed
			if (oldBlock.isPlacementObstacle() || !newBlock.canPlace(world, x, y, z, placedData, face, player) || !newBlock.onPlacement(world, x, y, z, placedData, face, player)) {
				cancelled = true;
			}
		}

		// If placement is denied revert the new block to the old
		if (cancelled) {
			player.getSession().send(new BlockChangeMessage(x, y, z, target != null ? target.getMaterial().getId() : 0, world.getBlockData((int) pos.getX(), (int) pos.getY(), (int) pos.getZ())));
			inventory.setItem(holding, inventory.getCurrentSlot());
		}
	}
}
