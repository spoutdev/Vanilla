/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, SpoutDev <http://www.spout.org/>
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

import org.spout.api.Spout;
import org.spout.api.event.player.PlayerInteractEvent;
import org.spout.api.event.player.PlayerInteractEvent.Action;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.discrete.Point;
import org.spout.api.inventory.InventoryBase;
import org.spout.api.inventory.ItemStack;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.basic.BasicAir;
import org.spout.api.material.block.BlockFace;
import org.spout.api.player.Player;
import org.spout.api.protocol.MessageHandler;
import org.spout.api.protocol.Session;

import org.spout.vanilla.controller.living.player.VanillaPlayer;
import org.spout.vanilla.material.Mineable;
import org.spout.vanilla.material.VanillaMaterial;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.item.tool.Tool;
import org.spout.vanilla.protocol.VanillaNetworkSynchronizer;
import org.spout.vanilla.protocol.msg.BlockChangeMessage;
import org.spout.vanilla.protocol.msg.PlayEffectMessage;
import org.spout.vanilla.protocol.msg.PlayEffectMessage.Messages;
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
		int state = message.getState();
		int face = message.getFace();

		World w = player.getEntity().getWorld();
		Block block = w.getBlock(x, y, z, player.getEntity());
		BlockMaterial blockMaterial = block.getSubMaterial();
		BlockFace clickedFace = VanillaMessageHandlerUtils.messageToBlockFace(face);

		if (x == 0 && y == 0 && z == 0 && face == 0 && state == 4) {
			((VanillaPlayer) player.getEntity().getController()).dropItem();
			return;
		}

		boolean isInteractable = true;
		// FIXME: How so not interactable? I am pretty sure I can interact with water to place a boat, no?
		if (blockMaterial == VanillaMaterials.AIR || blockMaterial == BasicAir.AIR || blockMaterial == VanillaMaterials.WATER || blockMaterial == VanillaMaterials.LAVA) {
			isInteractable = false;
		}

		InventoryBase inv = ((VanillaPlayer) player.getEntity().getController()).getInventory();
		ItemStack heldItem = inv.getCurrentItem();
		VanillaPlayer vp = ((VanillaPlayer) player.getEntity().getController());

		if (state == PlayerDiggingMessage.STATE_START_DIGGING) {
			PlayerInteractEvent event = new PlayerInteractEvent(player, block.getPosition(), heldItem, Action.LEFT_CLICK, isInteractable);
			if (Spout.getEngine().getEventManager().callEvent(event).isCancelled()) {
				return;
			}

			// Perform interactions
			if (!isInteractable && heldItem == null) {
				// interacting with nothing using fist
				return;
			} else if (heldItem == null) {
				// interacting with block using fist
				blockMaterial.onInteractBy(player.getEntity(), block, Action.LEFT_CLICK, clickedFace);
			} else if (!isInteractable) {
				// interacting with nothing using item
				heldItem.getSubMaterial().onInteract(player.getEntity(), Action.LEFT_CLICK);
			} else {
				// interacting with block using item
				heldItem.getSubMaterial().onInteract(player.getEntity(), block, Action.LEFT_CLICK, clickedFace);
				blockMaterial.onInteractBy(player.getEntity(), block, Action.LEFT_CLICK, clickedFace);
			}

			if (isInteractable) {
				Block neigh = block.translate(clickedFace);
				boolean fire = neigh.getMaterial().equals(VanillaMaterials.FIRE);
				if (fire) {
					// put out fire
					VanillaMaterials.FIRE.onDestroy(neigh);
					VanillaNetworkSynchronizer.playBlockEffect(block, player.getEntity(), PlayEffectMessage.Messages.RANDOM_FIZZ);
				} else if (vp.isSurvival() && blockMaterial.getHardness() != 0.0f) {
					vp.startDigging(new Point(w, x, y, z));
				} else {
					// insta-break
					blockMaterial.onDestroy(block);
					PlayEffectMessage pem = new PlayEffectMessage(Messages.PARTICLE_BREAKBLOCK.getId(), block, blockMaterial.getId());
					VanillaNetworkSynchronizer.sendPacketsToNearbyPlayers(player.getEntity(), player.getEntity().getViewDistance(), pem);
				}
			}
		} else if (state == PlayerDiggingMessage.STATE_DONE_DIGGING) {
			if (!vp.stopDigging(new Point(w, x, y, z)) || !isInteractable) {
				return;
			}

			long diggingTicks = vp.getDiggingTicks();
			int damageDone = 0;
			int totalDamage = 0;

			if (heldItem != null) {
				if (heldItem.getMaterial() instanceof Tool && blockMaterial instanceof Mineable) {
					short penalty = ((Tool) heldItem.getMaterial()).getDurabilityPenalty((Mineable) blockMaterial.getMaterial(), heldItem);
					if (penalty != 0) {
						if (heldItem.getData() - penalty < 1) {
							inv.setCurrentItem(null);
						} else {
							inv.addCurrentItemData(penalty);
						}
					}
				}
			}
			if (heldItem == null) {
				damageDone = ((int) diggingTicks * 1);
			} else {
				damageDone = ((int) diggingTicks * ((VanillaMaterial) heldItem.getMaterial()).getDamage());
			}
			// TODO: Take into account enchantments

			totalDamage = ((int) blockMaterial.getHardness() - damageDone);
			if (totalDamage <= 40) { // Yes, this is a very high allowance - this is because this is only over a single block, and this will spike due to varying latency.
				if (!(blockMaterial instanceof Mineable)) {
					player.kick("The block you tried to dig is not MINEABLE. No blocks for you.");
					return;
				}
				if (totalDamage < -30) {// This can be removed after VANILLA-97 is fixed.
					System.out.println("[DEBUG] Player spent much more time mining than expected: " + (-totalDamage) + " damage more. Block: " + blockMaterial.getClass().getName());
				}
				if (!vp.addAndCheckMiningSpeed(totalDamage)) {
					player.kick("Stop speed-mining!");
					return;
				}
				blockMaterial.onDestroy(block);
			}
			if (block.getMaterial() != VanillaMaterials.AIR) {
				player.getSession().send(new BlockChangeMessage(x, y, z, blockMaterial.getId(), blockMaterial.getData()));
			} else {
				PlayEffectMessage pem = new PlayEffectMessage(Messages.PARTICLE_BREAKBLOCK.getId(), block, blockMaterial.getId());
				VanillaNetworkSynchronizer.sendPacketsToNearbyPlayers(player.getEntity(), player.getEntity().getViewDistance(), pem);
			}
		} else if (state == PlayerDiggingMessage.STATE_CANCEL_DIGGING) {
			vp.stopDigging(new Point(w, x, y, z));
		}
	}
}
