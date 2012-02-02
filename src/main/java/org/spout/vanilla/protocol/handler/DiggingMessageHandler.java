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
 * the MIT license and the SpoutDev license version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license,
 * including the MIT license.
 */
package org.spout.vanilla.protocol.handler;

import org.spout.api.event.EventManager;
import org.spout.api.event.player.PlayerInteractEvent;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.discrete.Point;
import org.spout.api.material.block.BlockFace;
import org.spout.api.player.Player;
import org.spout.api.protocol.MessageHandler;
import org.spout.api.protocol.Session;
import org.spout.vanilla.VanillaMessageHandlerUtils;
import org.spout.vanilla.material.VanillaBlockMaterial;
import org.spout.vanilla.protocol.msg.BlockChangeMessage;
import org.spout.vanilla.protocol.msg.DiggingMessage;

/**
 * A {@link MessageHandler} which processes digging messages.
 */
public final class DiggingMessageHandler extends MessageHandler<DiggingMessage> {
	@Override
	public void handle(Session session, Player player, DiggingMessage message) {
		if (player == null) {
			return;
		}

		boolean blockBroken = false;

		EventManager eventManager = player.getSession().getGame().getEventManager();
		World world = player.getEntity().getWorld();

		int x = message.getX();
		int y = message.getY();
		int z = message.getZ();

		Block block = world.getBlock(x, y, z);

		// Need to have some sort of verification to deal with malicious clients.
		if (message.getState() == DiggingMessage.STATE_START_DIGGING) {
			boolean isAir = false;
			if (block == null || block.getBlockId() == 0 || block.getBlockMaterial() == null) {
				isAir = true;
			}
			PlayerInteractEvent interactEvent = new PlayerInteractEvent(player, new Point(world, x, y, z), player.getEntity().getInventory().getCurrentItem(), PlayerInteractEvent.Action.LEFT_CLICK, isAir);
			eventManager.callEvent(interactEvent);
			if (interactEvent.isCancelled()) {
				return;
			}

			BlockFace face = VanillaMessageHandlerUtils.messageToBlockFace(message.getFace());

			if(isAir || ((VanillaBlockMaterial)block.getBlockMaterial()).isLiquid()) {
				return;
			}
			/*if (interactEvent.useItemInHand() != Event.Result.DENY) { //TODO: Interactivity!
				SpoutItemStack heldItem = player.getItemInHand();
				if (heldItem != null && heldItem.getTypeId() > 255) {
					ItemProperties props = ItemProperties.get(heldItem.getTypeId());
					if (props != null) {
						if (!props.getPhysics().interact(player, block, heldItem, Action.LEFT_CLICK_BLOCK, face)) {
							return;
						}
					}
				}
			}
			if (interactEvent.useInteractedBlock() != Event.Result.DENY) {
				if (!BlockProperties.get(block.getTypeId()).getPhysics().interact(player, block, false, face)) {
					return;
				}
			}*/
			//if(player.getGamemode() == GameMode.CREATIVE) { //TODO: Gamemodes!
				blockBroken = true;
			//}
		} else if (message.getState() == DiggingMessage.STATE_DONE_DIGGING) {
			//TODO: Timing checks!
			blockBroken = true;
		}

		System.out.print(message + "|" + blockBroken);

		if (blockBroken) { //TODO: We *drop* the block, don't add it >.<
			world.setBlockIdAndData(x, y, z, (short)0, (short)0, player);
			player.getSession().send(new BlockChangeMessage(x, y, z, (short)0, (byte)0));
			/*if (!block.isEmpty() && !block.isLiquid()) {
				if (!player.getInventory().contains(block.getTypeId()) || player.getGameMode() != GameMode.CREATIVE) {
					player.getInventory().addItem(BlockProperties.get(block.getTypeId()).getDrops(block.getData()));
				}
			}
			world.playEffectExceptTo(block.getLocation(), Effect.STEP_SOUND, block.getTypeId(), 64, player);
			block.setTypeId(BlockID.AIR);*/
		}
	}
}
