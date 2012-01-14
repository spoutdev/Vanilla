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

import org.spout.api.event.EventManager;
import org.spout.api.event.player.PlayerInteractEvent;
import org.spout.api.geo.World;
import org.spout.api.geo.discrete.Point;
import org.spout.api.inventory.Inventory;
import org.spout.api.inventory.ItemStack;
import org.spout.api.material.Material;
import org.spout.api.material.block.BlockFace;
import org.spout.api.player.Player;
import org.spout.api.protocol.MessageHandler;
import org.spout.api.protocol.Session;
import org.spout.vanilla.VanillaMessageHandlerUtils;
import org.spout.vanilla.material.VanillaBlockMaterial;
import org.spout.vanilla.protocol.msg.BlockChangeMessage;
import org.spout.vanilla.protocol.msg.BlockPlacementMessage;

/**
 * A {@link MessageHandler} which processes placement messages.
 */
public final class BlockPlacementMessageHandler extends MessageHandler<BlockPlacementMessage> {
	@Override
	public void handle(Session session, Player player, BlockPlacementMessage message) {
		//if (player == null) {
		//	return;
		//}

		EventManager eventManager = player.getSession().getGame().getEventManager();
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
		if (message.getDirection() == 255) {
			// Right-clicked air. Note that the client doesn't send this if they are holding nothing.
			//BlockPlacementMessage previous = session.getPreviousPlacement();
			//if (previous == null || previous.getCount() != message.getCount() && previous.getId() != message.getId() && previous.getDamage() != message.getDamage()) {
				eventManager.callEvent(new PlayerInteractEvent(player, null, inventory.getCurrentItem(), PlayerInteractEvent.Action.RIGHT_CLICK, true));
			//}
			//session.setPreviousPlacement(null);
			return;
		}
		//session.setPreviousPlacement(message);

		Point pos = new Point(world, message.getX(), message.getY(), message.getZ());

		BlockFace face = VanillaMessageHandlerUtils.messageToBlockFace(message.getDirection());
		if (face == BlockFace.THIS) {
			return;
		}

		pos = pos.add(face.getOffset());
		org.spout.api.geo.cuboid.Block target = world.getBlock(pos);

		if (pos.getY() >= world.getHeight() || pos.getY() < 0) {
			return;
		}
		boolean sendRevert = false;

		PlayerInteractEvent interactEvent = new PlayerInteractEvent(player, new Point(pos.add(face.getOffset()), world), inventory.getCurrentItem(), PlayerInteractEvent.Action.RIGHT_CLICK, false);
		eventManager.callEvent(interactEvent);
		if (interactEvent.isCancelled()) {
			sendRevert = true;
		}

		if (holding != null && !sendRevert) {
			/*if (interactEvent.useItemInHand() != Event.Result.DENY) { //TODO: Implement items (they are not in yet!)
				if (holding.getMaterial().getId() > 255 &&  (holding.getTypeId()).getPhysics().interact(player, against, holding, PlayerInteractEvent.Action.RIGHT_CLICK, face)) {
					sendRevert = true;
				}
			}*/
			Material placedId = holding.getMaterial();
			if (placedId.getId() > 255) {
				//placedId = ItemProperties.get(placedId.getItemTypeId()).getPhysics().getPlacedBlock(face, holding.getDurability()); //TODO: Implement items (they are not in yet!)
				return;
			}
			if (placedId.getId() < 0) {
				sendRevert = true;
			}

			VanillaBlockMaterial newBlock = (VanillaBlockMaterial)placedId;
			VanillaBlockMaterial oldBlock = target != null ? (VanillaBlockMaterial)target.getBlockMaterial() : null;

			if (!sendRevert && (oldBlock == null || oldBlock.isLiquid() || oldBlock.getId() == 0)) {
				//if (EventFactory.onBlockCanBuild(target, placedId.getItemTypeId(), face).isBuildable()) {
					//SpoutBlockState newState = BlockProperties.get(placedId.getItemTypeId()).getPhysics().placeAgainst(player, target.getState(), placedId, face);
					//BlockPlaceEvent event = EventFactory.onBlockPlace(target, newState, against, player);

					//if (!event.isCancelled() && event.canBuild()) {
						/*newState.update(true);
						if (newState.getX() != target.getX() || newState.getY() != target.getY() || newState.getZ() != target.getZ()) {
							sendRevert = true;
						}*/

						if(!sendRevert) {
							world.setBlockMaterial((int)pos.getX(), (int)pos.getY(), (int)pos.getZ(), newBlock);
							player.getSession().send(new BlockChangeMessage((int)pos.getX(), (int)pos.getY(), (int)pos.getZ(), holding.getMaterial().getId(), world.getBlockData((int)pos.getX(), (int)pos.getY(), (int)pos.getZ())));
						}

						/*if(player.getGameMode() != GameMode.CREATIVE) { //TODO: Gamemode is currently not changeable
							holding.setAmount(holding.getAmount() - 1);
							if (holding.getAmount() == 0) {
								player.setItemInHand(null);
							} else {
								player.setItemInHand(holding);
							}
						}*/
					//} else {
					//	sendRevert = true;
					//}
				//} else {
				//	sendRevert = true;
				//}
			} else {
				sendRevert = true;
			}
		}
		if (sendRevert) {
			player.getSession().send(new BlockChangeMessage((int)pos.getX(), (int)pos.getY(), (int)pos.getZ(), target != null ? target.getBlockId() : 0, world.getBlockData((int)pos.getX(), (int)pos.getY(), (int)pos.getZ())));
			//TODO: Send potential amount change/whatever!
		}
	}
}