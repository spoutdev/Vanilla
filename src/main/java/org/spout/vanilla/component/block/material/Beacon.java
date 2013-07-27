/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011 Spout LLC <http://www.spout.org/>
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
package org.spout.vanilla.component.block.material;

import org.spout.api.entity.Player;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.discrete.Point;
import org.spout.api.material.BlockMaterial;

import org.spout.vanilla.component.block.ViewedBlockComponent;
import org.spout.vanilla.component.entity.inventory.WindowHolder;
import org.spout.vanilla.component.entity.misc.Effects;
import org.spout.vanilla.data.VanillaData;
import org.spout.vanilla.data.effect.EntityEffect;
import org.spout.vanilla.data.effect.EntityEffectType;
import org.spout.vanilla.event.inventory.BeaconCloseEvent;
import org.spout.vanilla.event.inventory.BeaconOpenEvent;
import org.spout.vanilla.inventory.block.BeaconInventory;
import org.spout.vanilla.inventory.window.block.BeaconWindow;
import org.spout.vanilla.material.VanillaMaterials;

/**
 * Component that represents a Anvil in the world.
 */
public class Beacon extends ViewedBlockComponent {
	/**
	 * Returns the amount of levels on the pyramid below this Beacon, this will only account for up to a 4-level pyramid.
	 *
	 * @return levels on pyramid
	 */
	public int getLevels() {
		Block block = getBlock();
		for (int lvl = 1; lvl <= 4; lvl++) {
			for (int dx = -lvl; dx <= lvl; dx++) {
				for (int dz = -lvl; dz <= lvl; dz++) {
					if (!isPyramidMaterial(block.translate(dx, -lvl, dz).getMaterial())) {
						return lvl - 1;
					}
				}
			}
		}
		return 4;
	}

	/**
	 * Returns the range in which players are effected by this Beacon determined by an arbitrary algorithm.
	 *
	 * @return range of effects
	 */
	public float getEffectRange() {
		return getLevels() * 10 + 10;
	}

	/**
	 * Returns the amount of time (in seconds) until the Beacon will update the players in the vicinity specified by {@link #getEffectRange()}.
	 *
	 * @return time (in seconds) until beacon sends an update
	 */
	public float getUpdateDelay() {
		return getData().get(VanillaData.UPDATE_DELAY);
	}

	/**
	 * Sets the amount of time (in seconds) until the Beacon will update the players in the vicinity specified by {@link #getEffectRange()}.
	 *
	 * @param delay (in seconds) until beacon should send an update
	 */
	public void setUpdateDelay(float delay) {
		getData().put(VanillaData.UPDATE_DELAY, delay);
	}

	/**
	 * Returns the time that the update delay should be set to upon reaching zero.
	 *
	 * @return delay to update to when delay reaches zero
	 */
	public float getMaxUpdateDelay() {
		return getData().get(VanillaData.MAX_UPDATE_DELAY);
	}

	/**
	 * Sets the time that the delay will be reset to upon reaching zero.
	 *
	 * @param delay to reset to
	 */
	public void setMaxUpdateDelay(float delay) {
		getData().put(VanillaData.MAX_UPDATE_DELAY, delay);
	}

	/**
	 * Returns the primary effect of this Beacon.
	 *
	 * @return primary effect of beacon
	 */
	public EntityEffectType getPrimaryEffect() {
		return getData().get(VanillaData.PRIMARY_EFFECT);
	}

	/**
	 * Sets the primary effect of this Beacon.
	 *
	 * @param type of effect to use
	 */
	public void setPrimaryEffect(EntityEffectType type) {
		getData().put(VanillaData.PRIMARY_EFFECT, type);
	}

	/**
	 * Returns the secondary effect of this Beacon.
	 *
	 * @return type of effect to use
	 */
	public EntityEffectType getSecondaryEffect() {
		return getData().get(VanillaData.SECONDARY_EFFECT);
	}

	/**
	 * Sets the secondary effect of this Beacon.
	 *
	 * @param type of effect to use
	 */
	public void setSecondaryEffect(EntityEffectType type) {
		getData().put(VanillaData.SECONDARY_EFFECT, type);
	}

	/**
	 * Returns the duration of the effects applied by this Beacon.
	 *
	 * @return duration of effects that are applied
	 */
	public float getEffectDuration() {
		return getData().get(VanillaData.EFFECT_DURATION);
	}

	/**
	 * Sets the duration of the effects applied by this Beacon.
	 *
	 * @param duration of effects
	 */
	public void setEffectDuration(float duration) {
		getData().put(VanillaData.EFFECT_DURATION, duration);
	}

	/**
	 * Returns the amplifier on the primary effect. This returns one if this Beacon has four levels, and it's primary effect is the same as it's secondary effect.
	 *
	 * @return amplifier of primary effect
	 */
	public int getPrimaryAmplifier() {
		return getLevels() == 4 && getPrimaryEffect() == getSecondaryEffect() ? 1 : 0;
	}

	/**
	 * Performs an update on the Players in the vicinity specified by {@link #getEffectRange()}. This method applies the primary effect to each player with the duration specified by {@link
	 * #getEffectDuration()} and the amplifier specified by {@link #getPrimaryAmplifier()}. The Beacon will apply the secondary effect if it is set, the Beacon's pyramid contains four levels, and the
	 * primary effect is not the same as the secondary effect. Note that in order for the secondary effect to be applied it must differ from the primary effect otherwise a buff will just be applied to
	 * the primary effect.
	 */
	public void doUpdate() {
		resetUpdateDelay();
		Point pos = getPoint();
		EntityEffectType primary = getPrimaryEffect();
		EntityEffectType secondary = getSecondaryEffect();
		if (getLevels() < 1 || primary == null || primary == EntityEffectType.NONE) {
			return;
		}

		for (Player player : pos.getWorld().getNearbyPlayers(pos, (int) getEffectRange())) {
			Effects effects = player.add(Effects.class);
			effects.add(new EntityEffect(primary, getPrimaryAmplifier(), getEffectDuration()));
			if (secondary != null && secondary != EntityEffectType.NONE && getLevels() == 4 && primary != secondary) {
				effects.add(new EntityEffect(secondary, getEffectDuration()));
			}
		}
	}

	/**
	 * Resets the update delay specified by {@link #getMaxUpdateDelay()}.
	 */
	public void resetUpdateDelay() {
		setUpdateDelay(getMaxUpdateDelay());
	}

	/**
	 * Returns true if the specified {@link BlockMaterial} is a valid material to use in the construction of a Beacon's power pyramid.
	 *
	 * @param mat to check
	 * @return true if material is valid
	 */
	public static boolean isPyramidMaterial(BlockMaterial mat) {
		return mat.isMaterial(VanillaMaterials.IRON_BLOCK, VanillaMaterials.GOLD_BLOCK, VanillaMaterials.DIAMOND_BLOCK, VanillaMaterials.EMERALD_BLOCK);
	}

	private float pulseUpdateDelay(float dt) {
		float delay = getUpdateDelay() - dt;
		setUpdateDelay(delay);
		return delay;
	}

	@Override
	public void onTick(float dt) {
		if (pulseUpdateDelay(dt) <= 0) {
			doUpdate();
		}
	}

	@Override
	public boolean open(Player player) {
		BeaconOpenEvent event = player.getEngine().getEventManager().callEvent(new BeaconOpenEvent(this, player));
		if (!event.isCancelled()) {
			player.get(WindowHolder.class).openWindow(new BeaconWindow(player, this, new BeaconInventory()));
			return true;
		}
		return false;
	}

	@Override
	public boolean close(Player player) {
		BeaconCloseEvent event = player.getEngine().getEventManager().callEvent(new BeaconCloseEvent(this, player));
		if (!event.isCancelled()) {
			return super.close(player);
		}
		return false;
	}
}
