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
import org.spout.api.geo.World;
import org.spout.api.geo.discrete.Point;
import org.spout.api.inventory.ItemStack;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.Material;
import org.spout.api.material.MaterialData;
import org.spout.api.player.Player;
import org.spout.api.protocol.MessageHandler;
import org.spout.api.protocol.Session;
import org.spout.vanilla.entity.living.player.CreativePlayer;
import org.spout.vanilla.entity.living.player.SurvivalPlayer;
import org.spout.vanilla.entity.object.Item;
import org.spout.vanilla.material.Block;
import org.spout.vanilla.protocol.msg.DiggingMessage;
import org.spout.vanilla.util.VanillaMessageHandlerUtils;

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

		org.spout.api.geo.cuboid.Block block = world.getBlock(x, y, z);

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

			VanillaMessageHandlerUtils.messageToBlockFace(message.getFace());

			if (isAir || ((Block) block.getBlockMaterial()).isLiquid()) {
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
			if (player.getEntity().getController() instanceof CreativePlayer) {
				blockBroken = true;
			}
		} else if (message.getState() == DiggingMessage.STATE_DONE_DIGGING) {
			//TODO: Timing checks!
			blockBroken = true;
		}

		System.out.print(message + "|" + blockBroken);

		if (blockBroken) {
			BlockMaterial oldMat = (BlockMaterial) MaterialData.getMaterial(world.getBlockId(x, y, z), world.getBlockData(x, y, z));
			world.setBlockIdAndData(x, y, z, (short) 0, (short) 0, player);
			oldMat.onDestroy(world, x, y, z);

			if (player.getEntity().getController() instanceof SurvivalPlayer) {
				Material dropMat = oldMat;
				int count = 1;
				if (oldMat instanceof Block) {
					Block blockMat = (Block) oldMat;
					dropMat = MaterialData.getMaterial((short) blockMat.getDrop().getId(), (short) blockMat.getDrop().getData());
					count = blockMat.getDropCount();
				}

				for (int i = 0; i < count && dropMat.getId() != 0; ++i) {
					world.createAndSpawnEntity(block.getBase(), new Item(new ItemStack(dropMat, 1), player.getEntity().getPosition().normalize().add(0, 5, 0)));
				}
			}
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
