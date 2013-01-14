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
package org.spout.vanilla.plugin.material.item.misc;

import org.spout.api.entity.Entity;
import org.spout.api.inventory.ItemStack;

import org.spout.vanilla.api.data.effect.StatusEffect;
import org.spout.vanilla.api.inventory.Slot;
import org.spout.vanilla.plugin.component.inventory.PlayerInventory;
import org.spout.vanilla.plugin.component.misc.EffectsComponent;
import org.spout.vanilla.plugin.data.effect.StatusEffectContainer;
import org.spout.vanilla.plugin.material.VanillaMaterials;
import org.spout.vanilla.plugin.material.item.VanillaItemMaterial;

public class Potion extends VanillaItemMaterial {
	/*
	 * Potion effects
	 */
	private static final short EFFECT_MASK = 15;
	/*
	 * Potion tiers
	 */
	public static final short TIER_MASK = 48;
	public static final short TIER0 = 0;
	public static final short TIER1 = 16;
	public static final short TIER2 = 32;
	public static final short TIER3 = 48;
	/*
	 * Duration data
	 */
	private static final short DURATION_MASK = 64;
	private static final short DURATION_NORMAL = 0;
	private static final short DURATION_EXTENDED = 64;
	/*
	 * Splash
	 */
	private static final short ACTION_MASK = 24576;
	private static final short ACTION_NONE = 0;
	private static final short ACTION_USE = 8192;
	private static final short ACTION_SPLASH = 16384;
	public static final Potion WATER_BOTTLE = new Potion("Water Bottle");
	public static final Potion AWKWARD = new Potion("Awkward Potion", StatusEffect.NONE, TIER1, DURATION_NORMAL, ACTION_NONE, WATER_BOTTLE);
	public static final Potion SPLASH_AWKWARD = new Potion("Splash Awkward Potion", StatusEffect.NONE, TIER0, DURATION_NORMAL, ACTION_SPLASH, WATER_BOTTLE);
	public static final Potion THICK = new Potion("Thick Potion", StatusEffect.NONE, TIER2, DURATION_NORMAL, ACTION_NONE, WATER_BOTTLE);
	public static final Potion MUNDANE = new Potion("Mundane Potion", StatusEffect.NONE, TIER0, DURATION_NORMAL, ACTION_USE, WATER_BOTTLE);
	public static final Potion MUNDANE_EXTENDED = new Potion("Mundane Potion (Extended)", StatusEffect.NONE, TIER0, DURATION_EXTENDED, ACTION_NONE, WATER_BOTTLE);
	/*
	 * Unused but available
	 */
	public static final Potion CLEAR1 = new Potion("Clear Potion", 6, TIER0, DURATION_NORMAL, ACTION_NONE, WATER_BOTTLE);
	public static final Potion CLEAR2 = new Potion("Clear Potion", 7, TIER0, DURATION_NORMAL, ACTION_NONE, WATER_BOTTLE);
	public static final Potion DIFFUSE = new Potion("Diffuse Potion", 11, TIER0, DURATION_NORMAL, ACTION_NONE, WATER_BOTTLE);
	public static final Potion ARTLESS = new Potion("Artless Potion", 13, TIER0, DURATION_NORMAL, ACTION_NONE, WATER_BOTTLE);
	public static final Potion THIN1 = new Potion("Thin Potion", 14, TIER0, DURATION_NORMAL, ACTION_NONE, WATER_BOTTLE);
	public static final Potion THIN2 = new Potion("Thin Potion", 15, TIER0, DURATION_NORMAL, ACTION_NONE, WATER_BOTTLE);
	public static final Potion BUNGLING1 = new Potion("Bungling Potion", 6, TIER1, DURATION_NORMAL, ACTION_NONE, WATER_BOTTLE);
	public static final Potion BUNGLING2 = new Potion("Bungling Potion", 7, TIER1, DURATION_NORMAL, ACTION_NONE, WATER_BOTTLE);
	public static final Potion SMOOTH = new Potion("Smooth Potion", 11, TIER1, DURATION_NORMAL, ACTION_NONE, WATER_BOTTLE);
	public static final Potion SUAVE = new Potion("Suave Potion", 13, TIER1, DURATION_NORMAL, ACTION_NONE, WATER_BOTTLE);
	public static final Potion DEBONAIR1 = new Potion("Debonair Potion", 14, TIER1, DURATION_NORMAL, ACTION_NONE, WATER_BOTTLE);
	public static final Potion DEBONAIR2 = new Potion("Debonair Potion", 15, TIER1, DURATION_NORMAL, ACTION_NONE, WATER_BOTTLE);
	public static final Potion CHARMING1 = new Potion("Charming Potion", 6, TIER2, DURATION_NORMAL, ACTION_NONE, WATER_BOTTLE);
	public static final Potion CHARMING2 = new Potion("Charming Potion", 7, TIER2, DURATION_NORMAL, ACTION_NONE, WATER_BOTTLE);
	public static final Potion REFINED = new Potion("Refined Potion", 11, TIER2, DURATION_NORMAL, ACTION_NONE, WATER_BOTTLE);
	public static final Potion CORDIAL = new Potion("Cordial Potion", 13, TIER2, DURATION_NORMAL, ACTION_NONE, WATER_BOTTLE);
	public static final Potion SPARKLING1 = new Potion("Cordial Potion", 14, TIER2, DURATION_NORMAL, ACTION_NONE, WATER_BOTTLE);
	public static final Potion SPARKLING2 = new Potion("Cordial Potion", 15, TIER2, DURATION_NORMAL, ACTION_NONE, WATER_BOTTLE);
	public static final Potion POTENT = new Potion("Potent Potion", 0, TIER3, DURATION_NORMAL, ACTION_NONE, WATER_BOTTLE);
	public static final Potion RANK1 = new Potion("Rank Potion", 6, TIER3, DURATION_NORMAL, ACTION_NONE, WATER_BOTTLE);
	public static final Potion RANK2 = new Potion("Rank Potion", 7, TIER3, DURATION_NORMAL, ACTION_NONE, WATER_BOTTLE);
	public static final Potion ACRID = new Potion("Rank Potion", 11, TIER3, DURATION_NORMAL, ACTION_NONE, WATER_BOTTLE);
	public static final Potion GROSS = new Potion("Rank Potion", 13, TIER3, DURATION_NORMAL, ACTION_NONE, WATER_BOTTLE);
	public static final Potion STINKY1 = new Potion("Rank Potion", 14, TIER3, DURATION_NORMAL, ACTION_NONE, WATER_BOTTLE);
	public static final Potion STINKY2 = new Potion("Rank Potion", 15, TIER3, DURATION_NORMAL, ACTION_NONE, WATER_BOTTLE);
	/*
	 * Use and splash potions
	 */
	public static final Potion REGENERATION = new Potion("Potion of Regeneration", StatusEffect.REGENERATION, TIER0, DURATION_NORMAL, ACTION_USE, 45, WATER_BOTTLE);
	public static final Potion REGENERATION_II = new Potion("Potion of Regeneration II", StatusEffect.REGENERATION, TIER2, DURATION_NORMAL, ACTION_USE, 120, WATER_BOTTLE);
	public static final Potion REGENERATION_EXTENDED = new Potion("Potion of Regeneration (Extended)", StatusEffect.REGENERATION, TIER0, DURATION_EXTENDED, ACTION_USE, 22.5f, WATER_BOTTLE);
	public static final Potion SPLASH_REGENERATION_EXTENDED = new Potion("Splash Potion of Regeneration (Extended)", StatusEffect.REGENERATION, TIER0, DURATION_EXTENDED, ACTION_SPLASH, 22.5f,
			WATER_BOTTLE);
	public static final Potion SPLASH_REGENERATION = new Potion("Splash Potion of Regeneration", StatusEffect.REGENERATION, TIER0, DURATION_NORMAL, ACTION_SPLASH, 45, WATER_BOTTLE);
	public static final Potion SPLASH_REGENERATION_II = new Potion("Splash Potion of Regeneration II", StatusEffect.REGENERATION, TIER2, DURATION_NORMAL, ACTION_SPLASH, 120, WATER_BOTTLE);
	public static final Potion SWIFTNESS = new Potion("Potion of Swiftness", StatusEffect.SPEED, TIER0, DURATION_NORMAL, ACTION_USE, 180, WATER_BOTTLE);
	public static final Potion SWIFTNESS_EXTENDED = new Potion("Potion of Swiftness (Extended)", StatusEffect.SPEED, TIER0, DURATION_EXTENDED, ACTION_USE, 480, WATER_BOTTLE);
	public static final Potion SPLASH_SWIFTNESS_EXTENDED = new Potion("Splash Potion of Swiftness (Extended)", StatusEffect.SPEED, TIER0, DURATION_EXTENDED, ACTION_SPLASH, 480, WATER_BOTTLE);
	public static final Potion SPLASH_SWIFTNESS = new Potion("Splash Potion of Swiftness", StatusEffect.SPEED, TIER0, DURATION_NORMAL, ACTION_SPLASH, 180, WATER_BOTTLE);
	public static final Potion SWIFTNESS_II = new Potion("Potion of Swiftness II", StatusEffect.SPEED, TIER2, DURATION_EXTENDED, ACTION_USE, 90, WATER_BOTTLE);
	public static final Potion SPLASH_SWIFTNESS_II = new Potion("Splash Potion of Swiftness II", StatusEffect.SPEED, TIER2, DURATION_EXTENDED, ACTION_SPLASH, 90, WATER_BOTTLE);
	public static final Potion FIRE_EXTENDED = new Potion("Potion of Fire Resistance (Extended)", StatusEffect.FIRE_RESISTANCE, TIER0, DURATION_EXTENDED, ACTION_USE, 480, WATER_BOTTLE);
	public static final Potion SPLASH_FIRE_EXTENDED = new Potion("Splash Potion of Fire Resistance (Extended)", StatusEffect.FIRE_RESISTANCE, TIER0, DURATION_EXTENDED, ACTION_SPLASH, 480,
			WATER_BOTTLE);
	public static final Potion FIRE = new Potion("Potion of Fire Resistance", StatusEffect.FIRE_RESISTANCE, TIER0, DURATION_NORMAL, ACTION_USE, 180, WATER_BOTTLE);
	public static final Potion SPLASH_FIRE = new Potion("Splash Potion of Fire Resistance", StatusEffect.FIRE_RESISTANCE, TIER0, DURATION_NORMAL, ACTION_SPLASH, 180, WATER_BOTTLE);
	public static final Potion FIRE_REVERTED = new Potion("Potion of Fire Resistance", StatusEffect.FIRE_RESISTANCE, TIER2, DURATION_NORMAL, ACTION_USE, 480, WATER_BOTTLE);
	public static final Potion SPLASH_FIRE_REVERTED = new Potion("Splash Potion of Fire Resistance", StatusEffect.FIRE_RESISTANCE, TIER2, DURATION_NORMAL, ACTION_SPLASH, 180, WATER_BOTTLE);
	public static final Potion POISON = new Potion("Potion of Poison", StatusEffect.POISON, TIER0, DURATION_NORMAL, ACTION_USE, 45, WATER_BOTTLE);
	public static final Potion SPLASH_POISON = new Potion("Splash Potion of Poison", StatusEffect.POISON, TIER0, DURATION_NORMAL, ACTION_SPLASH, 45, WATER_BOTTLE);
	public static final Potion POISON_EXTENDED = new Potion("Potion of Poison", StatusEffect.POISON, TIER0, DURATION_EXTENDED, ACTION_USE, 120, WATER_BOTTLE);
	public static final Potion SPLASH_POISON_EXTENDED = new Potion("Splash Potion of Poison", StatusEffect.POISON, TIER0, DURATION_EXTENDED, ACTION_SPLASH, 120, WATER_BOTTLE);
	public static final Potion POISON_II = new Potion("Potion of Poison II", StatusEffect.POISON, TIER2, DURATION_NORMAL, ACTION_USE, 22.5f, WATER_BOTTLE);
	public static final Potion SPLASH_POISON_II = new Potion("Splash Potion of Poison II", StatusEffect.POISON, TIER2, DURATION_NORMAL, ACTION_SPLASH, 22.5f, WATER_BOTTLE);
	public static final Potion HEALING = new Potion("Potion of Healing", StatusEffect.INSTANT_HEALTH, TIER0, DURATION_NORMAL, ACTION_USE, WATER_BOTTLE);
	public static final Potion SPLASH_HEALING = new Potion("Splash Potion of Healing", StatusEffect.INSTANT_HEALTH, TIER0, DURATION_NORMAL, ACTION_SPLASH, WATER_BOTTLE);
	public static final Potion HEALING_REVERTED = new Potion("Potion of Healing", StatusEffect.INSTANT_HEALTH, TIER0, DURATION_EXTENDED, ACTION_USE, WATER_BOTTLE);
	public static final Potion SPLASH_HEALING_REVERTED = new Potion("Splash Potion of Healing", StatusEffect.INSTANT_HEALTH, TIER0, DURATION_EXTENDED, ACTION_SPLASH, WATER_BOTTLE);
	public static final Potion HEALING_II = new Potion("Potion of Healing II", StatusEffect.INSTANT_HEALTH, TIER2, DURATION_NORMAL, ACTION_USE, WATER_BOTTLE);
	public static final Potion SPLASH_HEALING_II = new Potion("Splash Potion of Healing II", StatusEffect.INSTANT_HEALTH, TIER2, DURATION_NORMAL, ACTION_SPLASH, WATER_BOTTLE);
	public static final Potion WEAKNESS = new Potion("Potion of Weakness", StatusEffect.WEAKNESS, TIER0, DURATION_NORMAL, ACTION_USE, 90, WATER_BOTTLE);
	public static final Potion SPLASH_WEAKNESS = new Potion("Splash Potion of Weakness", StatusEffect.WEAKNESS, TIER0, DURATION_NORMAL, ACTION_SPLASH, 90, WATER_BOTTLE);
	public static final Potion WEAKNESS_EXTENDED = new Potion("Potion of Weakness (Extended)", StatusEffect.WEAKNESS, TIER0, DURATION_EXTENDED, ACTION_USE, 240, WATER_BOTTLE);
	public static final Potion SPLASH_WEAKNESS_EXTENDED = new Potion("Splash Potion of Weakness (Extended)", StatusEffect.WEAKNESS, TIER0, DURATION_EXTENDED, ACTION_SPLASH, 240, WATER_BOTTLE);
	public static final Potion WEAKNESS_REVERTED = new Potion("Potion of Weakness", StatusEffect.WEAKNESS, TIER2, DURATION_NORMAL, ACTION_USE, 90, WATER_BOTTLE);
	public static final Potion SPLASH_WEAKNESS_REVERTED = new Potion("Splash Potion of Weakness", StatusEffect.WEAKNESS, TIER2, DURATION_NORMAL, ACTION_SPLASH, 90, WATER_BOTTLE);
	public static final Potion STRENGTH = new Potion("Potion of Strength", StatusEffect.STRENGTH, TIER0, DURATION_NORMAL, ACTION_USE, 180, WATER_BOTTLE);
	public static final Potion SPLASH_STRENGTH = new Potion("Splash Potion of Strength", StatusEffect.STRENGTH, TIER0, DURATION_NORMAL, ACTION_SPLASH, 180, WATER_BOTTLE);
	public static final Potion STRENGTH_EXTENDED = new Potion("Potion of Strength (Extended)", StatusEffect.STRENGTH, TIER0, DURATION_EXTENDED, ACTION_USE, 480, WATER_BOTTLE);
	public static final Potion SPLASH_STRENGTH_EXTENDED = new Potion("Spash Potion of Strength (Extended)", StatusEffect.STRENGTH, TIER0, DURATION_EXTENDED, ACTION_SPLASH, 480, WATER_BOTTLE);
	public static final Potion STRENGTH_II = new Potion("Potion of Strength II", StatusEffect.STRENGTH, TIER2, DURATION_NORMAL, ACTION_USE, 90, WATER_BOTTLE);
	public static final Potion SPLASH_STRENGTH_II = new Potion("Splash Potion of Strength II", StatusEffect.STRENGTH, TIER2, DURATION_NORMAL, ACTION_SPLASH, 90, WATER_BOTTLE);
	public static final Potion SLOWNESS = new Potion("Potion of Slowness", StatusEffect.SLOWNESS, TIER0, DURATION_NORMAL, ACTION_USE, 90, WATER_BOTTLE);
	public static final Potion SPLASH_SLOWNESS = new Potion("Splash Potion of Slowness", StatusEffect.SLOWNESS, TIER0, DURATION_NORMAL, ACTION_SPLASH, 90, WATER_BOTTLE);
	public static final Potion SLOWNESS_EXTENDED = new Potion("Potion of Slowness (Extended)", StatusEffect.SLOWNESS, TIER0, DURATION_EXTENDED, ACTION_USE, 240, WATER_BOTTLE);
	public static final Potion SPLASH_SLOWNESS_EXTENDED = new Potion("Splash Potion of Slowness (Extended)", StatusEffect.SLOWNESS, TIER0, DURATION_EXTENDED, ACTION_SPLASH, 240, WATER_BOTTLE);
	public static final Potion SLOWNESS_REVERTED = new Potion("Potion of Slowness", StatusEffect.SLOWNESS, TIER2, DURATION_NORMAL, ACTION_USE, 90, WATER_BOTTLE);
	public static final Potion SPLASH_SLOWNESS_REVERTED = new Potion("Splash Potion of Slowness", StatusEffect.SLOWNESS, TIER2, DURATION_NORMAL, ACTION_SPLASH, 90, WATER_BOTTLE);
	public static final Potion HARMING = new Potion("Potion of Harming", StatusEffect.INSTANT_DAMAGE, TIER0, DURATION_NORMAL, ACTION_USE, WATER_BOTTLE);
	public static final Potion SPLASH_HARMING = new Potion("Splash Potion of Harming", StatusEffect.INSTANT_DAMAGE, TIER0, DURATION_NORMAL, ACTION_SPLASH, WATER_BOTTLE);
	public static final Potion HARMING_REVERTED = new Potion("Potion of Harming", StatusEffect.INSTANT_DAMAGE, TIER0, DURATION_EXTENDED, ACTION_USE, WATER_BOTTLE);
	public static final Potion SPLASH_HARMING_REVERTED = new Potion("Splash Potion of Harming", StatusEffect.INSTANT_DAMAGE, TIER0, DURATION_EXTENDED, ACTION_SPLASH, WATER_BOTTLE);
	public static final Potion HARMING_II = new Potion("Potion of Harming II", StatusEffect.INSTANT_DAMAGE, TIER2, DURATION_NORMAL, ACTION_USE, WATER_BOTTLE);
	public static final Potion SPLASH_HARMING_II = new Potion("Splash Potion of Harming", StatusEffect.INSTANT_DAMAGE, TIER2, DURATION_NORMAL, ACTION_SPLASH, WATER_BOTTLE);
	public static final Potion NIGHT_VISION = new Potion("Potion of Night Vision", StatusEffect.NIGHT_VISION, TIER0, DURATION_NORMAL, ACTION_USE, 180, WATER_BOTTLE);
	public static final Potion NIGHT_VISION_EXTENDED = new Potion("Potion of Night Vision (Extended)", StatusEffect.NIGHT_VISION, TIER0, DURATION_EXTENDED, ACTION_USE, 480, WATER_BOTTLE);
	public static final Potion INVISIBILITY = new Potion("Potion of Invisibility", StatusEffect.INVISIBILITY, TIER0, DURATION_NORMAL, ACTION_USE, 180, WATER_BOTTLE);
	public static final Potion INVISIBILITY_EXTENDED = new Potion("Potion of Invisibility (Extended)", StatusEffect.INVISIBILITY, TIER0, DURATION_EXTENDED, ACTION_USE, 480, WATER_BOTTLE);
	/**
	 * Contains the amount of time this potion have.
	 */
	private float time;
	private StatusEffect effect;

	private Potion(String name) {
		super((short) 0x7FFF, name, 373, null);
		this.time = 0;
	}

	private Potion(String name, int effect, short tier, short duration, short splash, Potion parent) {
		this(name, effect, tier, duration, splash, 0, parent);
	}

	private Potion(String name, StatusEffect effect, short tier, short duration, short splash, Potion parent) {
		this(name, effect, tier, duration, splash, 0, parent);
	}

	private Potion(String name, int effect, short tier, short duration, short splash, float time, Potion parent) {
		this(name, (effect & EFFECT_MASK) | (tier & TIER_MASK) | (duration & DURATION_MASK) | (splash & ACTION_MASK), parent);
		this.time = time;
	}

	private Potion(String name, int data, Potion parent) {
		super(name, 373, data, parent, null);
		this.time = 0;
	}

	public Potion(String name, StatusEffect effect, short tier, short duration, short splash, float time, Potion parent) {
		this(name, effect.getPotionID(), tier, duration, splash, time, parent);
		this.effect = effect;
	}

	/**
	 * Gets whether this type of Potion is a Splash potion
	 * @return True if it is a Splash potion, False if not
	 */
	public boolean isSplash() {
		return (this.getData() & ACTION_SPLASH) == ACTION_SPLASH;
	}

	public int getTier() {
		return (this.getData() & TIER_MASK);
	}

	public StatusEffect getEffect() {
		return this.effect;
	}

	public int getEffectId() {
		return this.getData() & EFFECT_MASK;
	}

	@Override
	public Potion getParentMaterial() {
		return (Potion) super.getParentMaterial();
	}

	public float getTime() {
		return time;
	}

	public void onDrink(Entity entity, Slot slot) {
		if (this.effect != null) {
			entity.add(EffectsComponent.class).addEffect(new StatusEffectContainer(effect, this.getTime(), this.getTier()));
		}

		slot.addAmount(-1);
		entity.get(PlayerInventory.class).getMain().add(new ItemStack(VanillaMaterials.GLASS_BOTTLE, 1));
	}
}
