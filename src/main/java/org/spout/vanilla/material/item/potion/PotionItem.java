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
package org.spout.vanilla.material.item.potion;

import org.spout.api.component.entity.PhysicsComponent;
import org.spout.api.entity.Entity;
import org.spout.api.event.player.Action;
import org.spout.api.inventory.ItemStack;
import org.spout.api.inventory.Slot;
import org.spout.api.math.VectorMath;

import org.spout.vanilla.component.entity.inventory.PlayerInventory;
import org.spout.vanilla.component.entity.misc.Effects;
import org.spout.vanilla.component.entity.substance.projectile.Potion;
import org.spout.vanilla.data.effect.EntityEffect;
import org.spout.vanilla.data.effect.EntityEffectType;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.item.VanillaItemMaterial;

public class PotionItem extends VanillaItemMaterial {
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
	public static final PotionItem WATER_BOTTLE = new PotionItem("Water Bottle");
	public static final PotionItem AWKWARD = new PotionItem("Awkward Potion", EntityEffectType.NONE, TIER1, DURATION_NORMAL, ACTION_NONE, WATER_BOTTLE);
	public static final PotionItem SPLASH_AWKWARD = new PotionItem("Splash Awkward Potion", EntityEffectType.NONE, TIER0, DURATION_NORMAL, ACTION_SPLASH, WATER_BOTTLE);
	public static final PotionItem THICK = new PotionItem("Thick Potion", EntityEffectType.NONE, TIER2, DURATION_NORMAL, ACTION_NONE, WATER_BOTTLE);
	public static final PotionItem MUNDANE = new PotionItem("Mundane Potion", EntityEffectType.NONE, TIER0, DURATION_NORMAL, ACTION_USE, WATER_BOTTLE);
	public static final PotionItem MUNDANE_EXTENDED = new PotionItem("Mundane Potion (Extended)", EntityEffectType.NONE, TIER0, DURATION_EXTENDED, ACTION_NONE, WATER_BOTTLE);
	/*
	 * Unused but available
	 */
	public static final PotionItem CLEAR1 = new PotionItem("Clear Potion", 6, TIER0, DURATION_NORMAL, ACTION_NONE, WATER_BOTTLE);
	public static final PotionItem CLEAR2 = new PotionItem("Clear Potion", 7, TIER0, DURATION_NORMAL, ACTION_NONE, WATER_BOTTLE);
	public static final PotionItem DIFFUSE = new PotionItem("Diffuse Potion", 11, TIER0, DURATION_NORMAL, ACTION_NONE, WATER_BOTTLE);
	public static final PotionItem ARTLESS = new PotionItem("Artless Potion", 13, TIER0, DURATION_NORMAL, ACTION_NONE, WATER_BOTTLE);
	public static final PotionItem THIN1 = new PotionItem("Thin Potion", 14, TIER0, DURATION_NORMAL, ACTION_NONE, WATER_BOTTLE);
	public static final PotionItem THIN2 = new PotionItem("Thin Potion", 15, TIER0, DURATION_NORMAL, ACTION_NONE, WATER_BOTTLE);
	public static final PotionItem BUNGLING1 = new PotionItem("Bungling Potion", 6, TIER1, DURATION_NORMAL, ACTION_NONE, WATER_BOTTLE);
	public static final PotionItem BUNGLING2 = new PotionItem("Bungling Potion", 7, TIER1, DURATION_NORMAL, ACTION_NONE, WATER_BOTTLE);
	public static final PotionItem SMOOTH = new PotionItem("Smooth Potion", 11, TIER1, DURATION_NORMAL, ACTION_NONE, WATER_BOTTLE);
	public static final PotionItem SUAVE = new PotionItem("Suave Potion", 13, TIER1, DURATION_NORMAL, ACTION_NONE, WATER_BOTTLE);
	public static final PotionItem DEBONAIR1 = new PotionItem("Debonair Potion", 14, TIER1, DURATION_NORMAL, ACTION_NONE, WATER_BOTTLE);
	public static final PotionItem DEBONAIR2 = new PotionItem("Debonair Potion", 15, TIER1, DURATION_NORMAL, ACTION_NONE, WATER_BOTTLE);
	public static final PotionItem CHARMING1 = new PotionItem("Charming Potion", 6, TIER2, DURATION_NORMAL, ACTION_NONE, WATER_BOTTLE);
	public static final PotionItem CHARMING2 = new PotionItem("Charming Potion", 7, TIER2, DURATION_NORMAL, ACTION_NONE, WATER_BOTTLE);
	public static final PotionItem REFINED = new PotionItem("Refined Potion", 11, TIER2, DURATION_NORMAL, ACTION_NONE, WATER_BOTTLE);
	public static final PotionItem CORDIAL = new PotionItem("Cordial Potion", 13, TIER2, DURATION_NORMAL, ACTION_NONE, WATER_BOTTLE);
	public static final PotionItem SPARKLING1 = new PotionItem("Cordial Potion", 14, TIER2, DURATION_NORMAL, ACTION_NONE, WATER_BOTTLE);
	public static final PotionItem SPARKLING2 = new PotionItem("Cordial Potion", 15, TIER2, DURATION_NORMAL, ACTION_NONE, WATER_BOTTLE);
	public static final PotionItem POTENT = new PotionItem("Potent Potion", 0, TIER3, DURATION_NORMAL, ACTION_NONE, WATER_BOTTLE);
	public static final PotionItem RANK1 = new PotionItem("Rank Potion", 6, TIER3, DURATION_NORMAL, ACTION_NONE, WATER_BOTTLE);
	public static final PotionItem RANK2 = new PotionItem("Rank Potion", 7, TIER3, DURATION_NORMAL, ACTION_NONE, WATER_BOTTLE);
	public static final PotionItem ACRID = new PotionItem("Rank Potion", 11, TIER3, DURATION_NORMAL, ACTION_NONE, WATER_BOTTLE);
	public static final PotionItem GROSS = new PotionItem("Rank Potion", 13, TIER3, DURATION_NORMAL, ACTION_NONE, WATER_BOTTLE);
	public static final PotionItem STINKY1 = new PotionItem("Rank Potion", 14, TIER3, DURATION_NORMAL, ACTION_NONE, WATER_BOTTLE);
	public static final PotionItem STINKY2 = new PotionItem("Rank Potion", 15, TIER3, DURATION_NORMAL, ACTION_NONE, WATER_BOTTLE);
	/*
	 * Use and splash potions
	 */
	public static final PotionItem REGENERATION = new PotionItem("Potion of Regeneration", EntityEffectType.REGENERATION, TIER0, DURATION_NORMAL, ACTION_USE, 45, WATER_BOTTLE);
	public static final PotionItem REGENERATION_II = new PotionItem("Potion of Regeneration II", EntityEffectType.REGENERATION, TIER2, DURATION_NORMAL, ACTION_USE, 120, WATER_BOTTLE);
	public static final PotionItem REGENERATION_EXTENDED = new PotionItem("Potion of Regeneration (Extended)", EntityEffectType.REGENERATION, TIER0, DURATION_EXTENDED, ACTION_USE, 22.5f, WATER_BOTTLE);
	public static final PotionItem SPLASH_REGENERATION_EXTENDED = new PotionItem("Splash Potion of Regeneration (Extended)", EntityEffectType.REGENERATION, TIER0, DURATION_EXTENDED, ACTION_SPLASH, 22.5f,
			WATER_BOTTLE);
	public static final PotionItem SPLASH_REGENERATION = new PotionItem("Splash Potion of Regeneration", EntityEffectType.REGENERATION, TIER0, DURATION_NORMAL, ACTION_SPLASH, 45, WATER_BOTTLE);
	public static final PotionItem SPLASH_REGENERATION_II = new PotionItem("Splash Potion of Regeneration II", EntityEffectType.REGENERATION, TIER2, DURATION_NORMAL, ACTION_SPLASH, 120, WATER_BOTTLE);
	public static final PotionItem SWIFTNESS = new PotionItem("Potion of Swiftness", EntityEffectType.SPEED, TIER0, DURATION_NORMAL, ACTION_USE, 180, WATER_BOTTLE);
	public static final PotionItem SWIFTNESS_EXTENDED = new PotionItem("Potion of Swiftness (Extended)", EntityEffectType.SPEED, TIER0, DURATION_EXTENDED, ACTION_USE, 480, WATER_BOTTLE);
	public static final PotionItem SPLASH_SWIFTNESS_EXTENDED = new PotionItem("Splash Potion of Swiftness (Extended)", EntityEffectType.SPEED, TIER0, DURATION_EXTENDED, ACTION_SPLASH, 480, WATER_BOTTLE);
	public static final PotionItem SPLASH_SWIFTNESS = new PotionItem("Splash Potion of Swiftness", EntityEffectType.SPEED, TIER0, DURATION_NORMAL, ACTION_SPLASH, 180, WATER_BOTTLE);
	public static final PotionItem SWIFTNESS_II = new PotionItem("Potion of Swiftness II", EntityEffectType.SPEED, TIER2, DURATION_EXTENDED, ACTION_USE, 90, WATER_BOTTLE);
	public static final PotionItem SPLASH_SWIFTNESS_II = new PotionItem("Splash Potion of Swiftness II", EntityEffectType.SPEED, TIER2, DURATION_EXTENDED, ACTION_SPLASH, 90, WATER_BOTTLE);
	public static final PotionItem FIRE_EXTENDED = new PotionItem("Potion of Fire Resistance (Extended)", EntityEffectType.FIRE_RESISTANCE, TIER0, DURATION_EXTENDED, ACTION_USE, 480, WATER_BOTTLE);
	public static final PotionItem SPLASH_FIRE_EXTENDED = new PotionItem("Splash Potion of Fire Resistance (Extended)", EntityEffectType.FIRE_RESISTANCE, TIER0, DURATION_EXTENDED, ACTION_SPLASH, 480,
			WATER_BOTTLE);
	public static final PotionItem FIRE = new PotionItem("Potion of Fire Resistance", EntityEffectType.FIRE_RESISTANCE, TIER0, DURATION_NORMAL, ACTION_USE, 180, WATER_BOTTLE);
	public static final PotionItem SPLASH_FIRE = new PotionItem("Splash Potion of Fire Resistance", EntityEffectType.FIRE_RESISTANCE, TIER0, DURATION_NORMAL, ACTION_SPLASH, 180, WATER_BOTTLE);
	public static final PotionItem FIRE_REVERTED = new PotionItem("Potion of Fire Resistance", EntityEffectType.FIRE_RESISTANCE, TIER2, DURATION_NORMAL, ACTION_USE, 480, WATER_BOTTLE);
	public static final PotionItem SPLASH_FIRE_REVERTED = new PotionItem("Splash Potion of Fire Resistance", EntityEffectType.FIRE_RESISTANCE, TIER2, DURATION_NORMAL, ACTION_SPLASH, 180, WATER_BOTTLE);
	public static final PotionItem POISON = new PotionItem("Potion of Poison", EntityEffectType.POISON, TIER0, DURATION_NORMAL, ACTION_USE, 45, WATER_BOTTLE);
	public static final PotionItem SPLASH_POISON = new PotionItem("Splash Potion of Poison", EntityEffectType.POISON, TIER0, DURATION_NORMAL, ACTION_SPLASH, 45, WATER_BOTTLE);
	public static final PotionItem POISON_EXTENDED = new PotionItem("Potion of Poison", EntityEffectType.POISON, TIER0, DURATION_EXTENDED, ACTION_USE, 120, WATER_BOTTLE);
	public static final PotionItem SPLASH_POISON_EXTENDED = new PotionItem("Splash Potion of Poison", EntityEffectType.POISON, TIER0, DURATION_EXTENDED, ACTION_SPLASH, 120, WATER_BOTTLE);
	public static final PotionItem POISON_II = new PotionItem("Potion of Poison II", EntityEffectType.POISON, TIER2, DURATION_NORMAL, ACTION_USE, 22.5f, WATER_BOTTLE);
	public static final PotionItem SPLASH_POISON_II = new PotionItem("Splash Potion of Poison II", EntityEffectType.POISON, TIER2, DURATION_NORMAL, ACTION_SPLASH, 22.5f, WATER_BOTTLE);
	public static final PotionItem HEALING = new PotionItem("Potion of Healing", EntityEffectType.INSTANT_HEALTH, TIER0, DURATION_NORMAL, ACTION_USE, WATER_BOTTLE);
	public static final PotionItem SPLASH_HEALING = new PotionItem("Splash Potion of Healing", EntityEffectType.INSTANT_HEALTH, TIER0, DURATION_NORMAL, ACTION_SPLASH, WATER_BOTTLE);
	public static final PotionItem HEALING_REVERTED = new PotionItem("Potion of Healing", EntityEffectType.INSTANT_HEALTH, TIER0, DURATION_EXTENDED, ACTION_USE, WATER_BOTTLE);
	public static final PotionItem SPLASH_HEALING_REVERTED = new PotionItem("Splash Potion of Healing", EntityEffectType.INSTANT_HEALTH, TIER0, DURATION_EXTENDED, ACTION_SPLASH, WATER_BOTTLE);
	public static final PotionItem HEALING_II = new PotionItem("Potion of Healing II", EntityEffectType.INSTANT_HEALTH, TIER2, DURATION_NORMAL, ACTION_USE, WATER_BOTTLE);
	public static final PotionItem SPLASH_HEALING_II = new PotionItem("Splash Potion of Healing II", EntityEffectType.INSTANT_HEALTH, TIER2, DURATION_NORMAL, ACTION_SPLASH, WATER_BOTTLE);
	public static final PotionItem WEAKNESS = new PotionItem("Potion of Weakness", EntityEffectType.WEAKNESS, TIER0, DURATION_NORMAL, ACTION_USE, 90, WATER_BOTTLE);
	public static final PotionItem SPLASH_WEAKNESS = new PotionItem("Splash Potion of Weakness", EntityEffectType.WEAKNESS, TIER0, DURATION_NORMAL, ACTION_SPLASH, 90, WATER_BOTTLE);
	public static final PotionItem WEAKNESS_EXTENDED = new PotionItem("Potion of Weakness (Extended)", EntityEffectType.WEAKNESS, TIER0, DURATION_EXTENDED, ACTION_USE, 240, WATER_BOTTLE);
	public static final PotionItem SPLASH_WEAKNESS_EXTENDED = new PotionItem("Splash Potion of Weakness (Extended)", EntityEffectType.WEAKNESS, TIER0, DURATION_EXTENDED, ACTION_SPLASH, 240, WATER_BOTTLE);
	public static final PotionItem WEAKNESS_REVERTED = new PotionItem("Potion of Weakness", EntityEffectType.WEAKNESS, TIER2, DURATION_NORMAL, ACTION_USE, 90, WATER_BOTTLE);
	public static final PotionItem SPLASH_WEAKNESS_REVERTED = new PotionItem("Splash Potion of Weakness", EntityEffectType.WEAKNESS, TIER2, DURATION_NORMAL, ACTION_SPLASH, 90, WATER_BOTTLE);
	public static final PotionItem STRENGTH = new PotionItem("Potion of Strength", EntityEffectType.STRENGTH, TIER0, DURATION_NORMAL, ACTION_USE, 180, WATER_BOTTLE);
	public static final PotionItem SPLASH_STRENGTH = new PotionItem("Splash Potion of Strength", EntityEffectType.STRENGTH, TIER0, DURATION_NORMAL, ACTION_SPLASH, 180, WATER_BOTTLE);
	public static final PotionItem STRENGTH_EXTENDED = new PotionItem("Potion of Strength (Extended)", EntityEffectType.STRENGTH, TIER0, DURATION_EXTENDED, ACTION_USE, 480, WATER_BOTTLE);
	public static final PotionItem SPLASH_STRENGTH_EXTENDED = new PotionItem("Spash Potion of Strength (Extended)", EntityEffectType.STRENGTH, TIER0, DURATION_EXTENDED, ACTION_SPLASH, 480, WATER_BOTTLE);
	public static final PotionItem STRENGTH_II = new PotionItem("Potion of Strength II", EntityEffectType.STRENGTH, TIER2, DURATION_NORMAL, ACTION_USE, 90, WATER_BOTTLE);
	public static final PotionItem SPLASH_STRENGTH_II = new PotionItem("Splash Potion of Strength II", EntityEffectType.STRENGTH, TIER2, DURATION_NORMAL, ACTION_SPLASH, 90, WATER_BOTTLE);
	public static final PotionItem SLOWNESS = new PotionItem("Potion of Slowness", EntityEffectType.SLOWNESS, TIER0, DURATION_NORMAL, ACTION_USE, 90, WATER_BOTTLE);
	public static final PotionItem SPLASH_SLOWNESS = new PotionItem("Splash Potion of Slowness", EntityEffectType.SLOWNESS, TIER0, DURATION_NORMAL, ACTION_SPLASH, 90, WATER_BOTTLE);
	public static final PotionItem SLOWNESS_EXTENDED = new PotionItem("Potion of Slowness (Extended)", EntityEffectType.SLOWNESS, TIER0, DURATION_EXTENDED, ACTION_USE, 240, WATER_BOTTLE);
	public static final PotionItem SPLASH_SLOWNESS_EXTENDED = new PotionItem("Splash Potion of Slowness (Extended)", EntityEffectType.SLOWNESS, TIER0, DURATION_EXTENDED, ACTION_SPLASH, 240, WATER_BOTTLE);
	public static final PotionItem SLOWNESS_REVERTED = new PotionItem("Potion of Slowness", EntityEffectType.SLOWNESS, TIER2, DURATION_NORMAL, ACTION_USE, 90, WATER_BOTTLE);
	public static final PotionItem SPLASH_SLOWNESS_REVERTED = new PotionItem("Splash Potion of Slowness", EntityEffectType.SLOWNESS, TIER2, DURATION_NORMAL, ACTION_SPLASH, 90, WATER_BOTTLE);
	public static final PotionItem HARMING = new PotionItem("Potion of Harming", EntityEffectType.INSTANT_DAMAGE, TIER0, DURATION_NORMAL, ACTION_USE, WATER_BOTTLE);
	public static final PotionItem SPLASH_HARMING = new PotionItem("Splash Potion of Harming", EntityEffectType.INSTANT_DAMAGE, TIER0, DURATION_NORMAL, ACTION_SPLASH, WATER_BOTTLE);
	public static final PotionItem HARMING_REVERTED = new PotionItem("Potion of Harming", EntityEffectType.INSTANT_DAMAGE, TIER0, DURATION_EXTENDED, ACTION_USE, WATER_BOTTLE);
	public static final PotionItem SPLASH_HARMING_REVERTED = new PotionItem("Splash Potion of Harming", EntityEffectType.INSTANT_DAMAGE, TIER0, DURATION_EXTENDED, ACTION_SPLASH, WATER_BOTTLE);
	public static final PotionItem HARMING_II = new PotionItem("Potion of Harming II", EntityEffectType.INSTANT_DAMAGE, TIER2, DURATION_NORMAL, ACTION_USE, WATER_BOTTLE);
	public static final PotionItem SPLASH_HARMING_II = new PotionItem("Splash Potion of Harming", EntityEffectType.INSTANT_DAMAGE, TIER2, DURATION_NORMAL, ACTION_SPLASH, WATER_BOTTLE);
	public static final PotionItem NIGHT_VISION = new PotionItem("Potion of Night Vision", EntityEffectType.NIGHT_VISION, TIER0, DURATION_NORMAL, ACTION_USE, 180, WATER_BOTTLE);
	public static final PotionItem NIGHT_VISION_EXTENDED = new PotionItem("Potion of Night Vision (Extended)", EntityEffectType.NIGHT_VISION, TIER0, DURATION_EXTENDED, ACTION_USE, 480, WATER_BOTTLE);
	public static final PotionItem INVISIBILITY = new PotionItem("Potion of Invisibility", EntityEffectType.INVISIBILITY, TIER0, DURATION_NORMAL, ACTION_USE, 180, WATER_BOTTLE);
	public static final PotionItem INVISIBILITY_EXTENDED = new PotionItem("Potion of Invisibility (Extended)", EntityEffectType.INVISIBILITY, TIER0, DURATION_EXTENDED, ACTION_USE, 480, WATER_BOTTLE);
	/**
	 * Contains the amount of time this potion have.
	 */
	private float time;
	private EntityEffectType effect;

	private PotionItem(String name) {
		super((short) 0x7FFF, name, 373, null);
		this.time = 0;
		setMaxStackSize(1);
	}

	private PotionItem(String name, int effect, short tier, short duration, short splash, PotionItem parent) {
		this(name, effect, tier, duration, splash, 0, parent);
	}

	private PotionItem(String name, EntityEffectType effect, short tier, short duration, short splash, PotionItem parent) {
		this(name, effect, tier, duration, splash, 0, parent);
	}

	private PotionItem(String name, int effect, short tier, short duration, short splash, float time, PotionItem parent) {
		this(name, (effect & EFFECT_MASK) | (tier & TIER_MASK) | (duration & DURATION_MASK) | (splash & ACTION_MASK), parent);
		this.time = time;
	}

	private PotionItem(String name, int data, PotionItem parent) {
		super(name, 373, data, parent, null);
		this.time = 0;
		setMaxStackSize(1);
	}

	public PotionItem(String name, EntityEffectType effect, short tier, short duration, short splash, float time, PotionItem parent) {
		this(name, effect.getPotionId(), tier, duration, splash, time, parent);
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

	public EntityEffectType getEffect() {
		return this.effect;
	}

	public int getEffectId() {
		return this.getData() & EFFECT_MASK;
	}

	@Override
	public PotionItem getParentMaterial() {
		return (PotionItem) super.getParentMaterial();
	}

	public float getTime() {
		return time;
	}

	@Override
	public void onInteract(Entity entity, Action action) {
		if (this.isSplash()) {
			Potion item = entity.getWorld().createEntity(entity.getPhysics().getPosition().add(0, 1.6f, 0)).add(Potion.class);
			PhysicsComponent physics = item.getOwner().getPhysics();
			//cene.setShape(6f, new SphereShape(0.3f)); // TODO: Correct this
			physics.impulse(VectorMath.getDirection(entity.getPhysics().getRotation()).multiply(55)); //TODO: Need real parameters
			item.setShooter(entity);
			item.setPotion(this);
			entity.getWorld().spawnEntity(item.getOwner());
		}
	}

	public void onDrink(Entity entity, Slot slot) {
		if (this.effect != null) {
			entity.add(Effects.class).add(new EntityEffect(effect, this.getTier(), this.getTime()));
		}

		slot.addAmount(-1);
		entity.get(PlayerInventory.class).getMain().add(new ItemStack(VanillaMaterials.GLASS_BOTTLE, 1));
	}
}
