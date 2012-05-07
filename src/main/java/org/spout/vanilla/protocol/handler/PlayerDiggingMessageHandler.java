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
import org.spout.vanilla.material.item.Spade;
import org.spout.vanilla.material.item.generic.Tool;
import org.spout.vanilla.material.item.generic.VanillaItemMaterial;
import org.spout.vanilla.protocol.msg.AnimationMessage;
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

		if (message.getState() == PlayerDiggingMessage.STATE_START_DIGGING) {
			PlayerInteractEvent event = new PlayerInteractEvent(player, block.getPosition(), heldItem, Action.LEFT_CLICK, isInteractable);
			Spout.getEngine().getEventManager().callEvent(event);
			((VanillaPlayer) player.getEntity().getController()).setDigging(true);

			//Call interactions.
			if (event.isCancelled() || (!isInteractable && heldItem == null)) {
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
			((VanillaPlayer) player.getEntity().getController()).setDigging(false);
			long diggingTicks = ((VanillaPlayer) player.getEntity().getController()).getDiggingTicks();
			int damageDone = 0;
			int totalDamage = 0;

			if (heldItem != null) {
                
			    
                if (heldItem.getMaterial() instanceof Tool) {
                    
                    Tool item = (Tool) heldItem.getMaterial();
                    
                    if (blockMaterial == VanillaMaterials.CLAY_BLOCK || blockMaterial == VanillaMaterials.GRASS || blockMaterial == VanillaMaterials.MYCELIUM || blockMaterial == VanillaMaterials.GRAVEL || blockMaterial == VanillaMaterials.DIRT || blockMaterial == VanillaMaterials.SAND || blockMaterial == VanillaMaterials.SOUL_SAND || blockMaterial == VanillaMaterials.SNOW_BLOCK || blockMaterial == VanillaMaterials.SNOW) {
                            
                        if (item instanceof Spade) {
                            heldItem.setData(((short) (heldItem.getData() + 1)));
                        }
                        else {
                            heldItem.setData(((short) (heldItem.getData() + 2)));
                        }
                    }
                    else if (blockMaterial == VanillaMaterials.NETHER_BRICK_STAIRS || blockMaterial == VanillaMaterials.WOODEN_STAIRS || blockMaterial == VanillaMaterials.WOODEN_PRESSURE_PLATE || blockMaterial == VanillaMaterials.COBBLESTONE_STAIRS || blockMaterial == VanillaMaterials.STONE_PRESSURE_PLATE || blockMaterial == VanillaMaterials.STONE_BRICK_STAIRS || blockMaterial == VanillaMaterials.OBSIDIAN || blockMaterial == VanillaMaterials.IRON_DOOR_BLOCK || blockMaterial == VanillaMaterials.DIAMOND_BLOCK || blockMaterial == VanillaMaterials.IRON_BLOCK || blockMaterial == VanillaMaterials.MONSTER_SPAWNER || blockMaterial == VanillaMaterials.DISPENSER || blockMaterial == VanillaMaterials.FURNACE || blockMaterial == VanillaMaterials.COAL_ORE || blockMaterial == VanillaMaterials.IRON_ORE || blockMaterial == VanillaMaterials.DIAMOND_ORE || blockMaterial == VanillaMaterials.GOLD_ORE || blockMaterial == VanillaMaterials.IRON_ORE || blockMaterial == VanillaMaterials.LAPIS_LAZULI_ORE || blockMaterial == VanillaMaterials.REDSTONE_ORE || blockMaterial == VanillaMaterials.GOLD_BLOCK || blockMaterial == VanillaMaterials.LAPIS_LAZULI_BLOCK || blockMaterial == VanillaMaterials.BRICK || blockMaterial == VanillaMaterials.NETHER_BRICK || blockMaterial == VanillaMaterials.COBBLESTONE || blockMaterial == VanillaMaterials.MOSS_STONE || blockMaterial == VanillaMaterials.SLAB || blockMaterial == VanillaMaterials.STONE || blockMaterial == VanillaMaterials.SANDSTONE || blockMaterial == VanillaMaterials.ICE || blockMaterial == VanillaMaterials.NETHERRACK) {
                        
                        if (item.equals(VanillaMaterials.WOODEN_PICKAXE) || item.equals(VanillaMaterials.IRON_PICKAXE) || item.equals(VanillaMaterials.GOLD_PICKAXE) || item.equals(VanillaMaterials.DIAMOND_PICKAXE) ) {
                            heldItem.setData(((short) (heldItem.getData() + 1)));
                        }
                        else {
                            heldItem.setData(((short) (heldItem.getData() + 2)));
                        }
                    }
                    else if (blockMaterial == VanillaMaterials.LOG || blockMaterial == VanillaMaterials.CHEST || blockMaterial == VanillaMaterials.PLANK) {
                        
                        if (item.equals(VanillaMaterials.WOODEN_AXE) || item.equals(VanillaMaterials.IRON_AXE) || item.equals(VanillaMaterials.GOLD_AXE) || item.equals(VanillaMaterials.DIAMOND_AXE)) {
                            heldItem.setData(((short) (heldItem.getData() + 1)));
                        }
                        else {
                            heldItem.setData(((short) (heldItem.getData() + 2)));
                        }
                    
                    }
                    
                    //Let's update the player window with the new data.
                    if (item.getDurability() <= heldItem.getData()) {
                        player.getEntity().getInventory().setCurrentItem(null);
                    }
                    else {
                        player.getEntity().getInventory().setCurrentItem(heldItem);
                    }
                }
                
            }
			
			if (isInteractable) {
				if (heldItem == null) {
					damageDone = ((int) diggingTicks * 1);
				} else {
					damageDone = ((int) diggingTicks * ((VanillaMaterial) heldItem.getMaterial()).getDamage());
				}

				totalDamage = ((int) blockMaterial.getHardness() - damageDone);
				if (totalDamage <= 0) {
					blockMaterial.onDestroy(block);
				}
			}
		}
	}
}