/*
 * This file is part of Vanilla (http://www.spout.org/).
 *
 * Vanilla is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Vanilla is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.spout.vanilla.protocol.handler;

import org.spout.api.player.Player;
import org.spout.api.protocol.MessageHandler;
import org.spout.api.protocol.Session;
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

		/*boolean blockBroken = false;

		SpoutWorld world = player.getWorld();

		int x = message.getX();
		int y = message.getY();
		int z = message.getZ();

		SpoutBlock block = world.getBlockAt(x, y, z);

		// Need to have some sort of verification to deal with malicious clients.
		if (message.getState() == DiggingMessage.STATE_START_DIGGING) {
			Action act = Action.LEFT_CLICK_BLOCK;
			if (player.getLocation().distanceSquared(block.getLocation()) > 36 || block.getTypeId() == BlockID.AIR) {
				act = Action.LEFT_CLICK_AIR;
			}
			BlockFace face = MessageHandlerUtils.messageToBlockFace(message.getFace());
			PlayerInteractEvent interactEvent = EventFactory.onPlayerInteract(player, act, block, face);
			if (interactEvent.isCancelled()) {
				return;
			}
			if (interactEvent.useItemInHand() != Event.Result.DENY) {
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
			}
			BlockDamageEvent event = EventFactory.onBlockDamage(player, block);
			if (!event.isCancelled()) {
				blockBroken = event.getInstaBreak() || player.getGameMode() == GameMode.CREATIVE;
			}
		} else if (message.getState() == DiggingMessage.STATE_DONE_DIGGING) {
			BlockBreakEvent event = EventFactory.onBlockBreak(block, player);
			if (!event.isCancelled()) {
				blockBroken = true;
			}
		}

		if (blockBroken) {
			if (!block.isEmpty() && !block.isLiquid()) {
				if (!player.getInventory().contains(block.getTypeId()) || player.getGameMode() != GameMode.CREATIVE) {
					player.getInventory().addItem(BlockProperties.get(block.getTypeId()).getDrops(block.getData()));
				}
			}
			world.playEffectExceptTo(block.getLocation(), Effect.STEP_SOUND, block.getTypeId(), 64, player);
			block.setTypeId(BlockID.AIR);
		}*/
	}
}