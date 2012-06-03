/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, VanillaDev <http://www.spout.org/>
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
import org.spout.api.event.player.PlayerInteractEvent.Action;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.discrete.Point;
import org.spout.api.inventory.InventoryBase;
import org.spout.api.inventory.ItemStack;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.Material;
import org.spout.api.material.Placeable;
import org.spout.api.material.block.BlockFace;
import org.spout.api.player.Player;
import org.spout.api.protocol.MessageHandler;
import org.spout.api.protocol.Session;

import org.spout.vanilla.controller.living.player.VanillaPlayer;
import org.spout.vanilla.material.VanillaBlockMaterial;
import org.spout.vanilla.material.item.tool.InteractTool;
import org.spout.vanilla.protocol.msg.BlockChangeMessage;
import org.spout.vanilla.protocol.msg.PlayerBlockPlacementMessage;
import org.spout.vanilla.util.VanillaMessageHandlerUtils;
import org.spout.vanilla.util.VanillaPlayerUtil;

public final class PlayerBlockPlacementMessageHandler extends MessageHandler<PlayerBlockPlacementMessage> {
	private void undoPlacement(Session sess, Block clickedBlock, Block alterBlock) {
		//refresh the client just in case it assumed something
		sess.send(new BlockChangeMessage(clickedBlock));
		sess.send(new BlockChangeMessage(alterBlock));
		InventoryBase inv = VanillaPlayerUtil.getInventory(sess.getPlayer().getEntity());
		if (inv != null) {
			inv.setCurrentItem(inv.getCurrentItem());
		}
	}

	@Override
	public void handleServer(Session session, Player player, PlayerBlockPlacementMessage message) {
		EventManager eventManager = session.getGame().getEventManager();
		World world = player.getEntity().getWorld();
		InventoryBase inventory = VanillaPlayerUtil.getInventory(player.getEntity());
		ItemStack holding = inventory.getCurrentItem();
		Material holdingMat = holding == null ? null : holding.getSubMaterial();

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
			// Right clicked air with an item.
			PlayerInteractEvent event = eventManager.callEvent(new PlayerInteractEvent(player, null, holding, Action.RIGHT_CLICK, true));
			if (!event.isCancelled() && holdingMat != null) {
				holdingMat.onInteract(player.getEntity(), Action.RIGHT_CLICK);
			}
		} else {

			//TODO: Validate the x/y/z coordinates of the message to check if it is in range of the player
			//This is an anti-hack requirement (else hackers can load far-away chunks and crash the server)

			//Get clicked block and validated face against it was placed
			Block clickedBlock = world.getBlock(message.getX(), message.getY(), message.getZ(), player.getEntity());
			BlockFace clickedFace = VanillaMessageHandlerUtils.messageToBlockFace(message.getDirection());
			if (clickedFace == BlockFace.THIS) {
				return;
			}
			if (clickedBlock.getY() >= world.getHeight() || clickedBlock.getY() < 0) {
				return;
			}

			//Perform interaction event
			PlayerInteractEvent interactEvent = eventManager.callEvent(new PlayerInteractEvent(player, clickedBlock.getPosition(), inventory.getCurrentItem(), Action.RIGHT_CLICK, false));

			//Get the target block and validate
			BlockMaterial clickedMaterial = clickedBlock.getSubMaterial();

			//alternative block to place at may the clicked block deny placement
			Block alterBlock = clickedBlock.translate(clickedFace);
			BlockFace alterFace = clickedFace.getOpposite();

			//check if the interaction was cancelled by the event
			if (interactEvent.isCancelled()) {
				undoPlacement(session, clickedBlock, alterBlock);
				return;
			}
			short durability = 0;

			//perform interaction on the server
			if (holdingMat != null) {
				if (holdingMat instanceof InteractTool) {
					durability = ((InteractTool) holdingMat).getMaxDurability();
				}
				holdingMat.onInteract(player.getEntity(), clickedBlock, Action.RIGHT_CLICK, clickedFace);
			}
			clickedMaterial.onInteractBy(player.getEntity(), clickedBlock, Action.RIGHT_CLICK, clickedFace);

			if (holdingMat instanceof InteractTool && VanillaPlayerUtil.isSurvival(clickedBlock.getSource())) { //TODO Total hack and is BADDDDDD
				short newDurability = ((short) (durability - ((InteractTool) holdingMat).getMaxDurability()));

				inventory.addCurrentItemData(newDurability);
				if (((InteractTool) holdingMat).getMaxDurability() < 1 && durability != ((InteractTool) holdingMat).getMaxDurability()) { //TODO Total hack!!!
					inventory.setCurrentItem(null); //Break a tool if their onInteract takes away durability. TODO Probably not the best place to do this...
				}
			}

			if (clickedMaterial instanceof VanillaBlockMaterial && ((VanillaBlockMaterial) clickedMaterial).isPlacementSuppressed()) {
				undoPlacement(session, clickedBlock, alterBlock);
				return;
			}

			//if the material can be placed, place it
			if (holdingMat != null && holdingMat instanceof Placeable) {
				short placedData = holding.getData(); //TODO: shouldn't the sub-material deal with this?
				Placeable toPlace = (Placeable) holdingMat;

				Block target;
				BlockFace targetFace;

				if (toPlace.canPlace(clickedBlock, placedData, clickedFace, true)) {
					target = clickedBlock;
					targetFace = clickedFace;
				} else if (toPlace.canPlace(alterBlock, placedData, alterFace, false)) {
					target = alterBlock;
					targetFace = alterFace;
				} else {
					undoPlacement(session, clickedBlock, alterBlock);
					return;
				}

				if (target.getY() >= world.getHeight() || target.getY() < 0) {
					return;
				}

				//is the player not solid-colliding with the block?
				if (toPlace instanceof BlockMaterial) {
					BlockMaterial mat = (BlockMaterial) toPlace;
					if (mat.isSolid()) {
						//TODO: Implement collision models to make this work
						//CollisionModel playerModel = player.getEntity().getCollision();
						//Vector3 offset = playerModel.resolve(mat.getCollisionModel());
						//Vector3 dist = player.getEntity().getPosition().subtract(target.getPosition());
						//if (dist.getX() < offset.getX() || dist.getY() < offset.getY() || dist.getZ() < offset.getZ()) {
						//	undoPlacement(player, clickedBlock, alterBlock);
						//	return;
						//}

						//For now: simple distance checking
						Point pos1 = player.getEntity().getPosition();
						Point pos2 = ((VanillaPlayer) player.getEntity().getController()).getHeadPosition();
						Point tpos = target.getPosition();

						if (pos1.distance(tpos) < 0.6 || pos2.distance(tpos) < 0.6) {
							undoPlacement(session, clickedBlock, alterBlock);
							return;
						}
					}
				}

				//perform actual placement
				if (toPlace.onPlacement(target, placedData, targetFace, target == clickedBlock)) {
					//Remove block from inventory if not in creative mode.
					if (!((PlayerController) player.getEntity().getController()).hasInfiniteResources()) {
						if (inventory.getCurrentItem().getAmount() > 1) {
							inventory.addCurrentItemAmount(-1);
						} else {
							inventory.setCurrentItem(null);
						}
					}
				} else {
					undoPlacement(session, clickedBlock, alterBlock);
				}
			}
		}
	}
}
