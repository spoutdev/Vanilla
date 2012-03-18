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
package org.spout.vanilla.material.item;

import org.spout.api.material.Material;
import org.spout.api.material.SubMaterial;
import org.spout.vanilla.material.generic.GenericItem;

public class Potion extends GenericItem implements SubMaterial {
	public final Potion EMPTY;	
	public final Potion AWKWARD;
	public final Potion THICK;
	public final Potion MUNDANE;
	public final Potion MUNDANE_EXTENDED;
	public final Potion REGENERATION;
	public final Potion REGENERATION_II;
	public final Potion REGENERATION_EXTENDED;
	public final Potion SWIFTNESS;
	public final Potion SWIFTNESS_II;
	public final Potion SWIFTNESS_EXTENDED;
	public final Potion FIRE;
	public final Potion FIRE_II;
	public final Potion FIRE_EXTENDED;
	public final Potion FIRE_REVERTED;
	public final Potion HEALING;
	public final Potion HEALING_II;
	public final Potion HEALING_REVERTED;
	public final Potion HARMING;
	public final Potion HARMING_REVERTED;
	public final Potion HARMING_II;
	public final Potion STRENGTH;
	public final Potion STRENGTH_EXTENDED;
	public final Potion SLOWNESS_REVERTED;
	public final Potion SLOWNESS_EXTENDED;
	public final Potion SPLASH_AWKWARD;
	public final Potion SPLASH_REGENERATION;
	public final Potion SPLASH_REGENERATION_EXTENDED;
	public final Potion SPLASH_REGENERATION_II;
	public final Potion SPLASH_SWIFTNESS;
	public final Potion SPLASH_SWIFTNESS_EXTENDED;
	public final Potion SPLASH_SWIFTNESS_II;
	public final Potion SPLASH_FIRE;
	public final Potion SPLASH_FIRE_EXTENDED;
	public final Potion SPLASH_FIRE_REVERTED;
	public final Potion SPLASH_HEALING;
	public final Potion SPLASH_HEALING_REVERTED;
	public final Potion SPLASH_HEALING_II;
	public final Potion SPLASH_STRENGTH;
	public final Potion SPLASH_STRENGTH_EXTENDED;
	public final Potion SPLASH_SLOWNESS_REVERTED;
	public final Potion SPLASH_SLOWNESS_EXTENDED;
	public final Potion SPLASH_HARMING;
	public final Potion SPLASH_HARMING_REVERTED;
	public final Potion SPLASH_HARMING_II;

	private final short data;
	private final Potion parent;
	
	private Potion(String name, int data, Potion parent) {
		super(name, 373);
		this.data = (short) data;
		this.parent = parent;
		this.register();
		parent.registerSubMaterial(this);
		
		this.EMPTY = parent.EMPTY;
		this.AWKWARD = parent.AWKWARD;
		this.FIRE = parent.FIRE;
		this.FIRE_EXTENDED = parent.FIRE_EXTENDED;
		this.FIRE_II = parent.FIRE_II;
		this.FIRE_REVERTED = parent.FIRE_REVERTED;
		this.HEALING = parent.HEALING;
		this.HEALING_II = parent.HEALING_II;
		this.HEALING_REVERTED = parent.HEALING_REVERTED;
		this.MUNDANE = parent.MUNDANE;
		this.MUNDANE_EXTENDED = parent.MUNDANE_EXTENDED;
		this.REGENERATION = parent.REGENERATION;
		this.REGENERATION_EXTENDED = parent.REGENERATION_EXTENDED;
		this.REGENERATION_II = parent.REGENERATION_II;
		this.SPLASH_AWKWARD = parent.SPLASH_AWKWARD;
		this.SPLASH_FIRE = parent.SPLASH_FIRE;
		this.SPLASH_FIRE_EXTENDED = parent.SPLASH_FIRE_EXTENDED;
		this.SPLASH_FIRE_REVERTED = parent.SPLASH_FIRE_REVERTED;
		this.SPLASH_HEALING = parent.SPLASH_HEALING;
		this.SPLASH_HEALING_II = parent.SPLASH_HEALING_II;
		this.SPLASH_HEALING_REVERTED = parent.SPLASH_HEALING_REVERTED;
		this.SPLASH_REGENERATION = parent.SPLASH_REGENERATION;
		this.SPLASH_REGENERATION_EXTENDED = parent.SPLASH_REGENERATION_EXTENDED;
		this.SPLASH_REGENERATION_II = parent.SPLASH_REGENERATION_II;
		this.SPLASH_STRENGTH = parent.SPLASH_STRENGTH;
		this.SPLASH_STRENGTH_EXTENDED = parent.SPLASH_STRENGTH_EXTENDED;
		this.SPLASH_SWIFTNESS = parent.SPLASH_SWIFTNESS;
		this.SPLASH_SWIFTNESS_EXTENDED = parent.SPLASH_SWIFTNESS_EXTENDED;
		this.SPLASH_SWIFTNESS_II = parent.SPLASH_SWIFTNESS_II;
		this.THICK = parent.THICK;
		this.SWIFTNESS = parent.SWIFTNESS;
		this.SWIFTNESS_EXTENDED = parent.SWIFTNESS_EXTENDED;
		this.SWIFTNESS_II = parent.SWIFTNESS_II;
		this.STRENGTH = parent.STRENGTH;
		this.STRENGTH_EXTENDED = parent.STRENGTH_EXTENDED;
		this.HARMING = parent.HARMING;
		this.HARMING_REVERTED = parent.HARMING_REVERTED;
		this.HARMING_II = parent.HARMING_II;
		this.SPLASH_HARMING = parent.SPLASH_HARMING;
		this.SPLASH_HARMING_REVERTED = parent.SPLASH_HARMING_REVERTED;
		this.SPLASH_HARMING_II = parent.SPLASH_HARMING_II;
		this.SLOWNESS_EXTENDED = parent.SLOWNESS_EXTENDED;
		this.SLOWNESS_REVERTED = parent.SLOWNESS_REVERTED;
		this.SPLASH_SLOWNESS_EXTENDED = parent.SPLASH_SLOWNESS_EXTENDED;
		this.SPLASH_SLOWNESS_REVERTED = parent.SPLASH_SLOWNESS_REVERTED;
	}
	
	public Potion(String name) {
		super(name, 373);
		this.parent = this;
		this.data = 0;
		this.register();
		
		this.EMPTY = new Potion("Empty Potion", 0, this);
		this.AWKWARD = new Potion("Awkward Potion", 16, this);
		this.SPLASH_AWKWARD = new Potion("Splash Awkward Potion", 16384, this);
		this.THICK = new Potion("Thick Potion", 32, this);
		this.MUNDANE_EXTENDED = new Potion("Mundane Potion (Extended)", 64, this);
		this.MUNDANE = new Potion("Mundane Potion", 8192, this);
		this.REGENERATION_EXTENDED = new Potion("Potion of Regeneration (Extended)", 8193, this);
		this.SPLASH_REGENERATION_EXTENDED = new Potion("Splash Potion of Regeneration (Extended)", 16449, this);
		this.REGENERATION = new Potion("Potion of Regeneration", 8257, this);
		this.SPLASH_REGENERATION = new Potion("Splash Potion of Regeneration", 16385, this);
		this.REGENERATION_II = new Potion("Potion of Regeneration II", 8225, this);
		this.SPLASH_REGENERATION_II = new Potion("Splash Potion of Regeneration II", 16417, this);
		this.SWIFTNESS_EXTENDED = new Potion("Potion of Swiftness (Extended)", 8194, this);
		this.SPLASH_SWIFTNESS_EXTENDED = new Potion("Splash Potion of Swiftness (Extended)", 16450, this);
		this.SWIFTNESS = new Potion("Potion of Swiftness", 8258, this);
		this.SPLASH_SWIFTNESS = new Potion("Splash Potion of Swiftness", 16386, this);
		this.SWIFTNESS_II = new Potion("Potion of Swiftness II", 8226, this);
		this.SPLASH_SWIFTNESS_II = new Potion("Splash Potion of Swiftness II", 16418, this);
		this.FIRE_EXTENDED = new Potion("Potion of Fire Resistance (Extended)", 8194, this);
		this.SPLASH_FIRE_EXTENDED = new Potion("Splash Potion of Fire Resistance (Extended)", 16415, this);
		this.FIRE = new Potion("Potion of Fire Resistance", 8258, this);
		this.SPLASH_FIRE = new Potion("Splash Potion of Fire Resistance", 16387, this);
		this.FIRE_II = new Potion("Potion of Fire Resistance II", 8226, this);
		this.FIRE_REVERTED = new Potion("Potion of Fire Resistance", 8227, this);
		this.SPLASH_FIRE_REVERTED = new Potion("Splash Potion of Fire Resistance", 16419, this);
		this.HEALING = new Potion("Potion of Healing", 8197, this);
		this.SPLASH_HEALING = new Potion("Splash Potion of Healing", 16389, this);
		this.HEALING_REVERTED = new Potion("Potion of Healing", 8261, this);
		this.SPLASH_HEALING_REVERTED = new Potion("Splash Potion of Healing", 16453, this);
		this.HEALING_II = new Potion("Potion of Healing II", 8229, this);
		this.SPLASH_HEALING_II = new Potion("Splash Potion of Healing II", 16421, this);
		this.STRENGTH = new Potion("Potion of Strength", 8201, this);
		this.SPLASH_STRENGTH = new Potion("Splash Potion of Strength", 16393, this);
		this.STRENGTH_EXTENDED = new Potion("Potion of Strength (Extended)", 8265, this);
		this.SPLASH_STRENGTH_EXTENDED = new Potion("Spash Potion of Strength (Extended)", 16457, this);
		this.SLOWNESS_EXTENDED = new Potion("Potion of Slowness (Extended)", 8226, this);
		this.SPLASH_SLOWNESS_EXTENDED = new Potion("Splash Potion of Slowness (Extended)", 16458, this);
		this.SLOWNESS_REVERTED = new Potion("Potion of Slowness", 8234, this);
		this.SPLASH_SLOWNESS_REVERTED = new Potion("Splash Potion of Slowness", 16426, this);
		this.HARMING = new Potion("Potion of Harming", 8202, this);
		this.SPLASH_HARMING = new Potion("Splash Potion of Harming", 16396, this);
		this.HARMING_REVERTED = new Potion("Potion of Harming", 8264, this);
		this.SPLASH_HARMING_REVERTED = new Potion("Splash Potion of Harming", 16460, this);
		this.HARMING_II = new Potion("Potion of Harming II", 8236, this);
		this.SPLASH_HARMING_II = new Potion("Splash Potion of Harming", 16428, this);
	}

	@Override
	public short getData() {
		return this.data;
	}

	@Override
	public Material getParentMaterial() {
		return this.parent;
	}
}
