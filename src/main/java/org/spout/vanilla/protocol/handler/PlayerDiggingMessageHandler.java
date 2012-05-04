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

import org.spout.api.event.EventManager;
import org.spout.api.event.player.PlayerInteractEvent;
import org.spout.api.event.player.PlayerInteractEvent.Action;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.inventory.ItemStack;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.Material;
import org.spout.api.material.block.BlockFace;
import org.spout.api.player.Player;
import org.spout.api.protocol.MessageHandler;
import org.spout.api.protocol.Session;
import org.spout.vanilla.controller.living.player.VanillaPlayer;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.protocol.msg.PlayerDiggingMessage;
import org.spout.vanilla.util.VanillaMessageHandlerUtils;

public final class PlayerDiggingMessageHandler extends MessageHandler<PlayerDiggingMessage> {
	@Override
	public void handleServer(Session session, Player player, PlayerDiggingMessage message) {
		if (player == null) {
			return;
		}

		boolean blockBroken = false;

		EventManager eventManager = player.getSession().getGame().getEventManager();
		World world = player.getEntity().getWorld();

		BlockFace clickedface = VanillaMessageHandlerUtils.messageToBlockFace(message.getFace());
		int x = message.getX();
		int y = message.getY();
		int z = message.getZ();

		Block block = world.getBlock(x, y, z, player.getEntity());
		BlockMaterial mat = block.getSubMaterial();

		//TODO Need to have some sort of verification to deal with malicious clients.
		if (message.getState() == PlayerDiggingMessage.STATE_START_DIGGING) {
			ItemStack holding = player.getEntity().getInventory().getCurrentItem();
			Material holdingMat = holding == null ? VanillaMaterials.AIR : holding.getMaterial();
			boolean isAir = false;
			if (mat.equals(VanillaMaterials.AIR)) {
				isAir = true;
			}

			PlayerInteractEvent interactEvent = new PlayerInteractEvent(player, block.getPosition(), holding, Action.LEFT_CLICK, isAir);
			eventManager.callEvent(interactEvent);
			if (interactEvent.isCancelled()) {
				return;
			} else if (isAir) {
				holdingMat.onInteract(player.getEntity(), Action.LEFT_CLICK); //call onInteract on item held for air
				return;
			} else {
				holdingMat.onInteract(player.getEntity(), block, Action.LEFT_CLICK, clickedface); //call onInteract on item held
				mat.onInteractBy(player.getEntity(), block, Action.LEFT_CLICK, clickedface); //call onInteract on block clicked
			}
			if (player.getEntity().getController() instanceof VanillaPlayer && !((VanillaPlayer) player.getEntity().getController()).isSurvival()) {
				blockBroken = true;
			}
		} else if (message.getState() == PlayerDiggingMessage.STATE_DONE_DIGGING) {
			//TODO: Timing checks!
			blockBroken = true;
		}

		BlockMaterial material = block.getMaterial();
		if (material == VanillaMaterials.WATER || material == VanillaMaterials.LAVA) {
			blockBroken = false; //deny digging for water or lava
		} else if (material.getHardness() == 0.0f) {
			blockBroken = true; //insta-break
		}

		if (blockBroken) {
			block.getMaterial().onDestroy(block);
		}
	}
}
