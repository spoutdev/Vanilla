/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, Spout LLC <http://www.spout.org/>
 * Vanilla is licensed under the Spout License Version 1.
 *
 * Vanilla is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the Spout License Version 1.
 *
 * Vanilla is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for
 * more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the Spout License Version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://spout.in/licensev1> for the full license, including
 * the MIT license.
 */
package org.spout.vanilla.protocol.handler.player;

import java.util.Collection;
import java.util.HashSet;

import org.spout.api.entity.Player;
import org.spout.api.event.player.Action;
import org.spout.api.event.player.PlayerInteractBlockEvent;
import org.spout.api.geo.Protection;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.discrete.Point;
import org.spout.api.inventory.ItemStack;
import org.spout.api.inventory.Slot;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.block.BlockFace;
import org.spout.api.plugin.services.ProtectionService;
import org.spout.api.protocol.MessageHandler;
import org.spout.api.protocol.ServerSession;
import org.spout.api.protocol.reposition.RepositionManager;
import org.spout.api.util.flag.Flag;
import org.spout.vanilla.ChatStyle;
import org.spout.vanilla.component.block.material.Sign;
import org.spout.vanilla.component.entity.living.Human;
import org.spout.vanilla.component.entity.misc.Digging;
import org.spout.vanilla.component.entity.misc.Hunger;
import org.spout.vanilla.data.GameMode;
import org.spout.vanilla.data.VanillaData;
import org.spout.vanilla.data.drops.flag.PlayerFlags;
import org.spout.vanilla.data.effect.store.GeneralEffects;
import org.spout.vanilla.event.cause.PlayerBreakCause;
import org.spout.vanilla.material.VanillaMaterial;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.item.Food;
import org.spout.vanilla.material.item.potion.PotionItem;
import org.spout.vanilla.material.item.tool.Tool;
import org.spout.vanilla.material.item.tool.weapon.Sword;
import org.spout.vanilla.protocol.msg.player.PlayerDiggingMessage;
import org.spout.vanilla.protocol.msg.world.block.BlockChangeMessage;
import org.spout.vanilla.protocol.msg.world.block.SignMessage;
import org.spout.vanilla.util.PlayerUtil;

public final class PlayerDiggingHandler extends MessageHandler<PlayerDiggingMessage> {
	@Override
	public void handleServer(ServerSession session, PlayerDiggingMessage message) {
		if (!session.hasPlayer()) {
			return;
		}

		Player player = session.getPlayer();
		RepositionManager rm = player.getNetworkSynchronizer().getRepositionManager();
		RepositionManager rmInverse = rm.getInverse();

		int x = rmInverse.convertX(message.getX());
		int y = rmInverse.convertY(message.getY());
		int z = rmInverse.convertZ(message.getZ());
		int state = message.getState();

		World w = player.getWorld();
		Point point = new Point(w, x, y, z);
		Block block = w.getBlock(point);
		BlockMaterial blockMaterial = block.getMaterial();

		short minecraftID = VanillaMaterials.getMinecraftId(blockMaterial);
		BlockFace clickedFace = message.getFace();
		Human human = player.get(Human.class);
		if (human == null) {
			return;
		}
		Slot currentSlot = PlayerUtil.getHeldSlot(player);
		if (currentSlot == null) {
			return;
		}
		ItemStack heldItem = currentSlot.get();

		// Don't block protections if dropping an item, silly Notch...
		if (state != PlayerDiggingMessage.STATE_DROP_ITEM && state != PlayerDiggingMessage.STATE_SHOOT_ARROW_EAT_FOOD) {
			Collection<Protection> protections = player.getEngine().getServiceManager().getRegistration(ProtectionService.class).getProvider().getAllProtections(point);
			for (Protection p : protections) {
				if (p.contains(point) && !human.isOp()) {
					player.getSession().send(new BlockChangeMessage(x, y, z, minecraftID, block.getBlockData() & 0xF, rm));
					player.sendMessage(ChatStyle.DARK_RED + "This area is a protected spawn point!");
					return;
				}
			}
		}

		if (state == PlayerDiggingMessage.STATE_DROP_ITEM && x == 0 && y == 0 && z == 0) {
			human.dropItem();
			return;
		}

		boolean isInteractable = true;
		if (blockMaterial == VanillaMaterials.AIR) {
			isInteractable = false;
		}
		// TODO VanillaPlayerInteractBlockEvent and add in Results to it (to
		// more indepthly take away durability).
		switch (state) {
		case PlayerDiggingMessage.STATE_START_DIGGING:
			if (!isProtected(player, rm, x, y, z, block, minecraftID)) {
				final PlayerInteractBlockEvent event = new PlayerInteractBlockEvent(player, block, point, clickedFace, Action.LEFT_CLICK);
				if (player.getEngine().getEventManager().callEvent(event).isCancelled()) {
					if (human.isCreative() || blockMaterial.getHardness() == 0.0f) {
						session.send(new BlockChangeMessage(block, session.getPlayer().getNetworkSynchronizer().getRepositionManager()));
						Sign sign = block.get(Sign.class);
						if (sign != null) {
							session.send(new SignMessage(block.getX(), block.getY(), block.getZ(), sign.getText(), player.getNetworkSynchronizer().getRepositionManager()));
						}
					}
				} else {
					// Perform interactions
					if (heldItem == null) {
						// interacting using fist
 					} else if (!isInteractable) {
						// interacting with nothing using item
						heldItem.getMaterial().onInteract(player, Action.LEFT_CLICK);
					} else {
						// interacting with block using item
						heldItem.getMaterial().onInteract(player, block, Action.LEFT_CLICK, clickedFace);
					}

					if (isInteractable) {
						Block neigh = block.translate(clickedFace);
						boolean fire = neigh.getMaterial().equals(VanillaMaterials.FIRE);
						if (fire) {
							// put out fire
							if (VanillaMaterials.FIRE.onDestroy(neigh, new PlayerBreakCause(player, neigh))) {
								GeneralEffects.RANDOM_FIZZ.playGlobal(block.getPosition());
							}
						} else if (human.isSurvival() && blockMaterial.getHardness() != 0.0f) {
							ItemStack currentItem = PlayerUtil.getHeldSlot(player).get();
							if (currentItem != null) {
								player.get(Digging.class).startDigging(new Point(w, x, y, z), currentItem.getMaterial());
							} else {
								player.get(Digging.class).startDigging(new Point(w, x, y, z), VanillaMaterials.AIR);
							}
						} else {
							// insta-break
							if (breakBlock(blockMaterial, block, human, session)) {
								GeneralEffects.BREAKBLOCK.playGlobal(block.getPosition(), blockMaterial, player);
							}
						}
					}
				}
			}
			break;
		case PlayerDiggingMessage.STATE_CANCEL_DIGGING:
			if (!isProtected(player, rm, x, y, z, block, minecraftID)) {
				player.get(Digging.class).stopDigging(new Point(w, x, y, z), false);
			}
			break;
		case PlayerDiggingMessage.STATE_DONE_DIGGING:
			if (!isProtected(player, rm, x, y, z, block, minecraftID)) {
				Digging diggingComponent = player.get(Digging.class);

				if (!diggingComponent.stopDigging(new Point(w, x, y, z), true) || !isInteractable) {
					if (!diggingComponent.isDigging()) {
						session.send(new BlockChangeMessage(block, session.getPlayer().getNetworkSynchronizer().getRepositionManager()));
						Sign sign = block.get(Sign.class);
						if (sign != null) {
							session.send(new SignMessage(block.getX(), block.getY(), block.getZ(), sign.getText(), player.getNetworkSynchronizer().getRepositionManager()));
						}
					}
					return;
				}

				if (player.getData().get(VanillaData.GAMEMODE).equals(GameMode.SURVIVAL)) {
					long diggingTicks = diggingComponent.getDiggingTicks();
					int damageDone;
					int totalDamage;

					if (heldItem == null) {
						damageDone = ((int) diggingTicks);
					} else {
						damageDone = ((int) diggingTicks * ((VanillaMaterial) heldItem.getMaterial()).getDamage());
					}
					// TODO: Take into account EFFICIENCY enchantment
					// TODO: Digging is slower while under water, on ladders,
					// etc. AQUA_AFFINITY enchantment speeds up underwater
					// digging

					totalDamage = ((int) blockMaterial.getHardness() - damageDone);
					if (totalDamage <= 40) { // Yes, this is a very high
												// allowance - this is because
												// this is only over a single
												// block, and this will spike
												// due to varying latency.
						if (breakBlock(blockMaterial, block, human, session)) {
							GeneralEffects.BREAKBLOCK.playGlobal(block.getPosition(), blockMaterial, player);

							if (heldItem != null && heldItem.getMaterial() instanceof Tool) {
								Tool tool = (Tool) heldItem.getMaterial();
								short damage = tool.getDurabilityPenalty(heldItem);
								if (currentSlot.get().getData() + damage >= tool.getMaxDurability()) {
									currentSlot.set(null);
								} else {
									currentSlot.addData(damage);
								}
							}
						}
					}
				}
			}
			break;
		case PlayerDiggingMessage.STATE_DROP_ITEM:
			human.dropItem();
			break;
		case PlayerDiggingMessage.STATE_SHOOT_ARROW_EAT_FOOD:
			if (heldItem.getMaterial() instanceof Food || heldItem.getMaterial() instanceof PotionItem) {
				player.add(Hunger.class).setEating(false, currentSlot);
			} else if (heldItem.getMaterial() instanceof Sword) {
				human.setEatingBlocking(false);
			}
			break;
		case PlayerDiggingMessage.STATE_UPDATE_BLOCK:
			break;
		}
	}

	private boolean breakBlock(BlockMaterial blockMaterial, Block block, Human human, ServerSession session) {
		HashSet<Flag> flags = new HashSet<Flag>();
		if (human.isSurvival()) {
			flags.add(PlayerFlags.SURVIVAL);
		} else {
			flags.add(PlayerFlags.CREATIVE);
		}
		ItemStack heldItem = PlayerUtil.getHeldSlot(session.getPlayer()).get();
		if (heldItem != null) {
			heldItem.getMaterial().getItemFlags(heldItem, flags);
		}
		if (!blockMaterial.destroy(block, flags, new PlayerBreakCause((Player) human.getOwner(), block))) {
			RepositionManager rm = session.getPlayer().getNetworkSynchronizer().getRepositionManager();
			session.send(new BlockChangeMessage(block, rm));
			Sign sign = block.get(Sign.class);
			if (sign != null) {
				session.send(new SignMessage(block.getX(), block.getY(), block.getZ(), sign.getText(), rm));
			}
			return false;
		}
		return true;
	}

	private boolean isProtected(Player player, RepositionManager rm, int x, int y, int z, Block block, short minecraftID) {
		final Point point = new Point(block.getWorld(), x, y, z);
		Collection<Protection> protections = player.getEngine().getServiceManager().getRegistration(ProtectionService.class).getProvider().getAllProtections(point);
		for (Protection p : protections) {
			if (p.contains(point) && !player.get(Human.class).isOp()) {
				player.getSession().send(new BlockChangeMessage(x, y, z, minecraftID, block.getBlockData() & 0xF, rm));
				player.sendMessage(ChatStyle.DARK_RED + "This area is a protected spawn point!");
				return true;
			}
		}
		return false;
	}
}
