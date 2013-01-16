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
package org.spout.vanilla.plugin.protocol.handler.player;

import java.util.Collection;
import java.util.logging.Level;

import org.spout.api.Spout;
import org.spout.api.chat.style.ChatStyle;
import org.spout.api.entity.Player;
import org.spout.api.event.Cause;
import org.spout.api.event.EventManager;
import org.spout.api.event.Result;
import org.spout.api.event.player.PlayerInteractEvent;
import org.spout.api.event.player.PlayerInteractEvent.Action;
import org.spout.api.geo.Protection;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.discrete.Point;
import org.spout.api.inventory.ItemStack;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.Material;
import org.spout.api.material.Placeable;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.block.BlockFaces;
import org.spout.api.plugin.services.ProtectionService;
import org.spout.api.protocol.MessageHandler;
import org.spout.api.protocol.Session;
import org.spout.api.protocol.reposition.RepositionManager;

import org.spout.vanilla.plugin.component.living.Living;
import org.spout.vanilla.api.inventory.Slot;
import org.spout.vanilla.plugin.component.misc.HeadComponent;
import org.spout.vanilla.plugin.component.misc.HungerComponent;
import org.spout.vanilla.plugin.configuration.VanillaConfiguration;
import org.spout.vanilla.plugin.data.VanillaData;
import org.spout.vanilla.plugin.data.effect.SoundEffect;
import org.spout.vanilla.plugin.data.effect.store.SoundEffects;
import org.spout.vanilla.plugin.event.cause.PlayerClickBlockCause;
import org.spout.vanilla.plugin.event.cause.PlayerPlacementCause;
import org.spout.vanilla.plugin.material.VanillaBlockMaterial;
import org.spout.vanilla.plugin.material.item.Food;
import org.spout.vanilla.plugin.material.item.tool.weapon.Sword;
import org.spout.vanilla.plugin.protocol.msg.player.PlayerBlockPlacementMessage;
import org.spout.vanilla.plugin.protocol.msg.world.block.BlockChangeMessage;
import org.spout.vanilla.plugin.util.PlayerUtil;

public final class PlayerBlockPlacementHandler extends MessageHandler<PlayerBlockPlacementMessage> {
	private void refreshClient(Player player, Block clickedBlock, BlockFace clickedFace, RepositionManager rm) {
		// refresh the client just in case it assumed something
		player.getSession().send(false, new BlockChangeMessage(clickedBlock, rm));
		player.getSession().send(false, new BlockChangeMessage(clickedBlock.translate(clickedFace), rm));
		Slot held = PlayerUtil.getHeldSlot(player);
		if (held != null) {
			held.set(held.get());
		}
	}

	@Override
	public void handleServer(Session session, PlayerBlockPlacementMessage message) {
		if (!session.hasPlayer()) {
			return;
		}
		Player player = session.getPlayer();
		RepositionManager rm = player.getNetworkSynchronizer().getRepositionManager();
		RepositionManager rmInverse = rm.getInverse();
		message = message.convert(rmInverse);

		EventManager eventManager = session.getEngine().getEventManager();
		World world = player.getWorld();
		Slot currentSlot = PlayerUtil.getHeldSlot(player);
		if (currentSlot == null) {
			Spout.getLogger().log(Level.WARNING, "Block placement failed for " + player.getName() + ": Missing player inventory");
			return;
		}
		ItemStack holding = currentSlot.get();
		Material holdingMat = holding == null ? null : holding.getMaterial();

		/*
		 * The notch client's packet sending is weird. Here's how it works: If the client is clicking a block not in range, sends a packet with x=-1,y=255,z=-1 If the client is clicking a block in
		 * range with an item in hand (id > 255) Sends both the normal block placement packet and a (-1,255,-1) one If the client is placing a block in range with a block in hand, only one normal
		 * packet is sent That is how it usually happens. Sometimes it doesn't happen like that. Therefore, a hacky workaround.
		 */
		final BlockFace clickedFace = message.getDirection();

		if ((holdingMat instanceof Food && player.add(HungerComponent.class).getHunger() != VanillaData.HUNGER.getDefaultValue()) || holdingMat instanceof Sword) {
			player.get(Living.class).setEatingBlocking(true);
			return;
		}
		if (clickedFace == BlockFace.THIS) {
			// Right clicked air with an item.
			PlayerInteractEvent event = eventManager.callEvent(new PlayerInteractEvent(player, null, holding, Action.RIGHT_CLICK, true, clickedFace));

			// May have been changed by the event
			holding = currentSlot.get();
			holdingMat = holding == null ? null : holding.getMaterial();

			if (event.useItemInHand() != Result.DENY && holdingMat != null) {
				holdingMat.onInteract(player, Action.RIGHT_CLICK);
			}
		} else {
			// TODO: Validate the x/y/z coordinates of the message to check if it is in range of the player
			// This is an anti-hack requirement (else hackers can load far-away chunks and crash the server)

			// Get clicked block and validated face against it was placed
			final Block clickedBlock = world.getBlock(message.getX(), message.getY(), message.getZ());
			final BlockMaterial clickedMaterial = clickedBlock.getMaterial();

			// Perform interaction event
			PlayerInteractEvent interactEvent = eventManager.callEvent(new PlayerInteractEvent(player, clickedBlock.getPosition(), currentSlot.get(), Action.RIGHT_CLICK, false, clickedFace));

			// May have been changed by the event
			holding = currentSlot.get();
			holdingMat = holding == null ? null : holding.getMaterial();

			// check if the interaction was cancelled by the event
			if (interactEvent.isCancelled()) {
				refreshClient(player, clickedBlock, clickedFace, rm);
				return;
			}

			// perform interaction on the server
			if (interactEvent.interactWithBlock() != Result.DENY) {
				if (holdingMat != null && interactEvent.useItemInHand() != Result.DENY) {
					holdingMat.onInteract(player, clickedBlock, Action.RIGHT_CLICK, clickedFace);
				}
				clickedMaterial.onInteractBy(player, clickedBlock, Action.RIGHT_CLICK, clickedFace);

				// TODO: Put block component interaction handling back in
				// if (clickedMaterial.hasController()) {
				// clickedMaterial.getController(clickedBlock).onInteract(player, Action.RIGHT_CLICK);
				// }
			}

			// Checks before placement
			if (interactEvent.useItemInHand() == Result.DENY) {
				refreshClient(player, clickedBlock, clickedFace, rm);
				return;
			}
			if (interactEvent.useItemInHand() != Result.ALLOW) {
				if (clickedMaterial instanceof VanillaBlockMaterial && (((VanillaBlockMaterial) clickedMaterial).isPlacementSuppressed())) {
					refreshClient(player, clickedBlock, clickedFace, rm);
					return;
				}
			}

			// If the holding material can be placed, place it
			if (holdingMat instanceof Placeable) {
				Cause<?> cause = new PlayerClickBlockCause(player, clickedBlock);
				short placedData = holding.getData(); // TODO: shouldn't the sub-material deal with this?
				Placeable toPlace = (Placeable) holdingMat;

				final Block placedBlock;
				final BlockFace placedAgainst;
				final boolean placedIsClicked;
				// For snow, tall grass, and the like, place at the clicked block
				final BlockFace clickedAgainst;
				if (!clickedBlock.getMaterial().isPlacementObstacle() && BlockFaces.NESW.contains(clickedFace)) {
					clickedAgainst = BlockFace.BOTTOM;
				} else {
					clickedAgainst = clickedFace.getOpposite();
				}
				if (toPlace.canPlace(clickedBlock, placedData, clickedAgainst, message.getFace(), true, cause) || interactEvent.useItemInHand() == Result.ALLOW) {
					placedBlock = clickedBlock;
					placedAgainst = clickedAgainst;
					placedIsClicked = true;
				} else {
					placedBlock = clickedBlock.translate(clickedFace);
					placedAgainst = clickedFace.getOpposite();
					placedIsClicked = false;
					if (!toPlace.canPlace(placedBlock, placedData, placedAgainst, message.getFace(), false, cause)) {
						refreshClient(player, clickedBlock, clickedFace, rm);
						return;
					}
				}

				// is the player not solid-colliding with the block?
				if (toPlace instanceof BlockMaterial) {
					BlockMaterial mat = (BlockMaterial) toPlace;
					if (mat.isSolid()) {
						// TODO: Implement collision models to make this work
						// CollisionModel playerModel = player.getEntity().getCollision();
						// Vector3 offset = playerModel.resolve(mat.getCollisionModel());
						// Vector3 dist = player.getEntity().getPosition().subtract(target.getPosition());
						// if (dist.getX() < offset.getX() || dist.getY() < offset.getY() || dist.getZ() < offset.getZ()) {
						// undoPlacement(player, clickedBlock, alterBlock);
						// return;
						// }

						// For now: simple distance checking
						Point tpos = placedBlock.getPosition();
						if (player.getTransform().getPosition().distance(tpos) < 0.6) {
							refreshClient(player, clickedBlock, clickedFace, rm);
							return;
						}
						HeadComponent head = player.get(HeadComponent.class);
						if (head != null && head.getPosition().distance(tpos) < 0.6) {
							refreshClient(player, clickedBlock, clickedFace, rm);
							return;
						}
					}
				}

				// Check if the player can place the block.
				Collection<Protection> protections = Spout.getEngine().getServiceManager().getRegistration(ProtectionService.class).getProvider().getAllProtections(placedBlock.getPosition());
				for (Protection p : protections) {
					if (p.contains(placedBlock.getPosition()) && !VanillaConfiguration.OPS.isOp(player.getName())) {
						refreshClient(player, clickedBlock, clickedFace, rm);
						player.sendMessage(ChatStyle.DARK_RED, "This area is a protected spawn point!");
						return;
					}
				}
				cause = new PlayerPlacementCause(player, (Material) toPlace, placedBlock);

				// Perform actual placement
				toPlace.onPlacement(placedBlock, placedData, placedAgainst, message.getFace(), placedIsClicked, cause);

				// Play sound
				BlockMaterial material = placedBlock.getMaterial();
				SoundEffect sound;
				if (material instanceof VanillaBlockMaterial) {
					sound = ((VanillaBlockMaterial) material).getStepSound();
				} else {
					sound = SoundEffects.STEP_STONE;
				}
				sound.playGlobal(placedBlock.getPosition(), 0.8f, 0.8f);

				// Remove block from inventory
				if (!PlayerUtil.isCostSuppressed(player) && holdingMat == (currentSlot.get() != null ? currentSlot.get().getMaterial() : null)) {
					currentSlot.addAmount(-1);
				}
			}

			refreshClient(player, clickedBlock, clickedFace, rm);
		}
	}
}
