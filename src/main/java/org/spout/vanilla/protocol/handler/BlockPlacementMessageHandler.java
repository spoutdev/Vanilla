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
import org.spout.api.geo.discrete.Point;
import org.spout.api.inventory.Inventory;
import org.spout.api.inventory.ItemStack;
import org.spout.api.material.Material;
import org.spout.api.material.block.BlockFace;
import org.spout.api.math.MathHelper;
import org.spout.api.player.Player;
import org.spout.api.protocol.MessageHandler;
import org.spout.api.protocol.Session;
import org.spout.vanilla.VanillaMaterials;
import org.spout.vanilla.material.Block;
import org.spout.vanilla.material.Item;
import org.spout.vanilla.protocol.msg.BlockChangeMessage;
import org.spout.vanilla.protocol.msg.BlockPlacementMessage;
import org.spout.vanilla.util.VanillaMessageHandlerUtils;

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

		Point offsetPos = pos.add(face.getOffset());
		org.spout.api.geo.cuboid.Block target = world.getBlock(offsetPos);

		if (pos.getY() >= world.getHeight() || pos.getY() < 0) {
			return;
		}
		boolean sendRevert = false;

		PlayerInteractEvent interactEvent = eventManager.callEvent(new PlayerInteractEvent(player, new Point(pos, world), inventory.getCurrentItem(), PlayerInteractEvent.Action.RIGHT_CLICK, false));
		if (interactEvent.isCancelled()) {
			sendRevert = true;
		}

		int x = MathHelper.floor(offsetPos.getX());
		int y = MathHelper.floor(offsetPos.getY());
		int z = MathHelper.floor(offsetPos.getZ());

		if (holding != null && !sendRevert) {
			/*if (interactEvent.useItemInHand() != Event.Result.DENY) { //TODO: Implement items (they are not in yet!)
				if (holding.getMaterial().getId() > 255 &&  (holding.getTypeId()).getPhysics().interact(player, against, holding, PlayerInteractEvent.Action.RIGHT_CLICK, face)) {
					sendRevert = true;
				}
			}*/
			Material placedMaterial = holding.getMaterial();
			if (placedMaterial instanceof Item) {
				((Item) placedMaterial).onInteract(player.getEntity(), pos, PlayerInteractEvent.Action.RIGHT_CLICK, face);
				return;
			}
			if (placedMaterial.getId() > 255) {
				//placedId = ItemProperties.get(placedId.getItemTypeId()).getPhysics().getPlacedBlock(face, holding.getDurability()); //TODO: Implement items (they are not in yet!)
				return;
			}
			if (placedMaterial.getId() < 0) {
				sendRevert = true;
			}

			short placedData = holding.getDamage();
			if (placedData == 0) {
				placedData = placedMaterial.getData();
			}

			if (face == BlockFace.BOTTOM && world.getBlockMaterial(x, y - 1, z) == VanillaMaterials.SNOW) {
				//make sure the target switches one block below (just like water)
				--y;
				target = world.getBlock(x, y, z);
			}

			Block newBlock = (Block) placedMaterial;
			Block oldBlock = target != null ? (Block) target.getBlockMaterial() : null;

			if (!sendRevert && (oldBlock == null || oldBlock.isLiquid() || oldBlock.getId() == 0 || oldBlock == VanillaMaterials.SNOW)) {

				//if (EventFactory.onBlockCanBuild(target, placedId.getItemTypeId(), face).isBuildable()) {
				//SpoutBlockState newState = BlockProperties.get(placedId.getItemTypeId()).getPhysics().placeAgainst(player, target.getState(), placedId, face);
				//BlockPlaceEvent event = EventFactory.onBlockPlace(target, newState, against, player);

				//if (!event.isCancelled() && event.canBuild()) {
				/*newState.update(true);
				if (newState.getX() != target.getX() || newState.getY() != target.getY() || newState.getZ() != target.getZ()) {
					sendRevert = true;
				}*/

				if (newBlock.onPlacement(world, x, y, z, placedData, face, player)) {
					if (!((PlayerController) player.getEntity().getController()).hasInfiniteResources()) {
						holding.setAmount(holding.getAmount() - 1);
						inventory.setItem(holding, inventory.getCurrentSlot());
					}
				}

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
			player.getSession().send(new BlockChangeMessage(x, y, z, target != null ? target.getBlockId() : 0, world.getBlockData((int) pos.getX(), (int) pos.getY(), (int) pos.getZ())));
			inventory.setItem(holding, inventory.getCurrentSlot());
		}
	}
}
