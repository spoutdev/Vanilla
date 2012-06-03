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
package org.spout.vanilla.controller.living.player;

import java.util.HashMap;
import java.util.Map;

import org.spout.api.geo.cuboid.Block;
import org.spout.api.inventory.ItemStack;
import org.spout.api.protocol.ProtocolUtil;
import org.spout.vanilla.controller.source.DamageCause;
import org.spout.vanilla.controller.source.HealthChangeReason;
import org.spout.vanilla.enchantment.Enchantments;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.protocol.event.AnimationProtocolEvent;
import org.spout.vanilla.protocol.event.UpdateHealthProtocolEvent;
import org.spout.vanilla.util.EnchantmentUtil;

public enum GameMode {
	SURVIVAL((byte) 0) {
		public void onTick(VanillaPlayer player, float dt) {
			if (player.isDigging() && (player.getDiggingTicks() % 20) == 0) {
				ProtocolUtil.executeWithNearbyPlayers(player.getParent(), player.getParent().getViewDistance(), new ProtocolUtil.CallProtocolEvent(new AnimationProtocolEvent(player.getParent().getId(), AnimationProtocolEvent.Animation.SWING_ARM)));
			}

			if ((player.distanceMoved += player.getPreviousPosition().distanceSquared(player.getParent().getPosition())) >= 1) {
				player.incrementExhaustion(0.01F);
				player.distanceMoved = 0;
			}

			if (player.isSprinting()) {
				player.incrementExhaustion(0.1F);
			}

			// TODO: Check for swimming, jumping, sprint jumping, block breaking, attacking, receiving damage for exhaustion level.
			Block head = player.getParent().getWorld().getBlock(player.getHeadPosition());
			if (head.getMaterial().equals(VanillaMaterials.GRAVEL, VanillaMaterials.SAND, VanillaMaterials.STATIONARY_WATER, VanillaMaterials.WATER)) {
				player.incrementAirTicks(1);
				ItemStack helmet = player.getInventory().getArmor().getHelmet();
				int level = 0;
				if (helmet != null && EnchantmentUtil.hasEnchantment(helmet, Enchantments.RESPIRATION)) {
					level = EnchantmentUtil.getEnchantmentLevel(helmet, Enchantments.RESPIRATION);
				}
				if (head.getMaterial().equals(VanillaMaterials.STATIONARY_WATER, VanillaMaterials.WATER)) {
					// Drowning
					int ticksBeforeDrowning = level == 0 ? 300 : level * 300; // Increase time before drowning by 15 seconds per enchantment level
					if (player.getAirTicks() >= ticksBeforeDrowning && player.getAirTicks() % 20 == 0) {
						player.damage(4, DamageCause.DROWN);
					}
				} else {
					// Suffocation
					int noDamageTicks /* TODO noDamageTicks should probably be made a global variable to account for other damage */ = level == 0 ? 10 : 10 + 20 * level; // Increase time between damage by 1 second per enchantment level
					if (player.getAirTicks() % noDamageTicks == 0) {
						player.damage(1, DamageCause.SUFFOCATE);
					}
				}
			} else {
				// Reset air ticks if necessary
				player.setAirTicks(0);
			}

			if (player.isPoisoned()) {
				player.incrementExhaustion(15.0F / 30 * dt);
			}

			// Track hunger
			player.foodTimer++;
			if (player.foodTimer >= 80) {
				updateHealthAndHunger(player);
				player.foodTimer = 0;
			}
		}

		private void updateHealthAndHunger(VanillaPlayer player) {
			short health;
			health = (short) player.getHealth();

			if (player.getExhaustion() > 4.0) {
				player.incrementExhaustion(-4.0F);
				if (player.getFoodSaturation() > 0) {
					player.setFoodSaturation(Math.max(player.getFoodSaturation() - 0.1f, 0));
				} else {
					player.setHunger((short) Math.max(player.getHunger() - 1, 0));
				}
			}

			boolean changed = false;
			if (player.getHunger() <= 0 && health > 0) {
				health = (short) Math.max(health - 1, 0);
				player.setHealth(health, DamageCause.STARVE);
				changed = true;
			} else if (player.getHunger() >= 18 && health < 20) {
				health = (short) Math.min(health + 1, 20);
				player.setHealth(health, HealthChangeReason.REGENERATION);
				changed = true;
			}

			if (changed) {
				System.out.println("Performing health/hunger update...");
				System.out.println("Food saturation: " + player.getFoodSaturation());
				System.out.println("Hunger: " + player.getHunger());
				System.out.println("Health: " + health);
				System.out.println("Exhaustion: " + player.getExhaustion());
				// sendPacket(owner, new UpdateHealthMessage(health, hunger, foodSaturation));
			}
		}
	},
	CREATIVE((byte) 1) {
		public void onTick(VanillaPlayer player, float dt) {

		}
	};
	private final byte id;
	private static final Map<Byte, GameMode> idMap = new HashMap<Byte, GameMode>();

	private GameMode(byte id) {
		this.id = id;
	}

	public byte getId() {
		return id;
	}

	public static GameMode getById(byte id) {
		return idMap.get(id);
	}

	public abstract void onTick(VanillaPlayer player, float dt);

	static {
		for (GameMode mode : GameMode.values()) {
			idMap.put(mode.getId(), mode);
		}
	}
}
