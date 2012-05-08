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

import java.util.Set;

import org.spout.api.Spout;
import org.spout.api.event.EventManager;
import org.spout.api.event.player.PlayerInteractEvent;
import org.spout.api.event.player.PlayerInteractEvent.Action;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.inventory.ItemStack;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.Material;
import org.spout.api.material.basic.BasicAir;
import org.spout.api.material.block.BlockFace;
import org.spout.api.player.Player;
import org.spout.api.protocol.MessageHandler;
import org.spout.api.protocol.Session;

import org.spout.vanilla.controller.living.player.VanillaPlayer;
import org.spout.vanilla.material.VanillaMaterial;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.item.generic.VanillaItemMaterial;
import org.spout.vanilla.protocol.VanillaNetworkSynchronizer;
import org.spout.vanilla.protocol.msg.AnimationMessage;
import org.spout.vanilla.protocol.msg.PlayEffectMessage;
import org.spout.vanilla.protocol.msg.PlayerDiggingMessage;
import org.spout.vanilla.util.VanillaMessageHandlerUtils;

public final class PlayerDiggingMessageHandler extends MessageHandler<PlayerDiggingMessage> {
	@Override
	public void handleServer(Session session, Player player, PlayerDiggingMessage message) {
		if (player == null || player.getEntity() == null) {
			return;
		}

		int x = message.getX();
		int y = message.getY();
		int z = message.getZ();

		Block block = player.getEntity().getWorld().getBlock(x, y, z, player.getEntity());
		BlockMaterial blockMaterial = block.getSubMaterial();
		boolean isInteractable = true;

		if (blockMaterial == VanillaMaterials.AIR || blockMaterial == BasicAir.AIR || blockMaterial == VanillaMaterials.WATER || blockMaterial == VanillaMaterials.LAVA) {
			isInteractable = false;
		}

		ItemStack heldItem = player.getEntity().getInventory().getCurrentItem();
		VanillaPlayer vp = ((VanillaPlayer) player.getEntity().getController());

		if (message.getState() == PlayerDiggingMessage.STATE_START_DIGGING) {
			PlayerInteractEvent event = new PlayerInteractEvent(player, block.getPosition(), heldItem, Action.LEFT_CLICK, isInteractable);
			Spout.getEngine().getEventManager().callEvent(event);
			vp.setDigging(true);

			//Call interactions.
			if (event.isCancelled() || (!isInteractable && heldItem == null)) {
				vp.setDigging(false);
				return;
			//punching with our fist
			} else if (heldItem == null) {
				blockMaterial.onInteractBy(player.getEntity(), block, Action.LEFT_CLICK, VanillaMessageHandlerUtils.messageToBlockFace(message.getFace()));
			} else if (!isInteractable) {
				heldItem.getMaterial().onInteract(player.getEntity(), Action.LEFT_CLICK);
			} else {
				heldItem.getMaterial().onInteract(player.getEntity(), block, Action.LEFT_CLICK, VanillaMessageHandlerUtils.messageToBlockFace(message.getFace()));
				blockMaterial.onInteractBy(player.getEntity(), block, Action.LEFT_CLICK, VanillaMessageHandlerUtils.messageToBlockFace(message.getFace()));
			}
		} else if (message.getState() == PlayerDiggingMessage.STATE_DONE_DIGGING) {
			vp.setDigging(false);
			long diggingTicks = ((VanillaPlayer) player.getEntity().getController()).getDiggingTicks();
			int damageDone = 0;
			int totalDamage = 0;

			if (isInteractable) {
				if (heldItem == null) {
					damageDone = ((int) diggingTicks * 1);
				} else {
					damageDone = ((int) diggingTicks * ((VanillaMaterial) heldItem.getMaterial()).getDamage());
				}

				totalDamage = ((int) blockMaterial.getHardness() - damageDone);
				if (totalDamage <= 0) {
					blockMaterial.onDestroy(block);
					PlayEffectMessage pem = new PlayEffectMessage(player.getEntity().getId(), block.getX(), block.getY(), block.getZ(), block.getMaterial().getId());
					VanillaNetworkSynchronizer.sendPacketsToNearbyPlayers(player.getEntity(), player.getEntity().getViewDistance(), pem);
				}
			}
		}
	}
}