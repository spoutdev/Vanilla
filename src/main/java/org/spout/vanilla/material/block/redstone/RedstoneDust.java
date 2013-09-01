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
package org.spout.vanilla.material.block.redstone;

import org.spout.api.material.BlockMaterial;

import org.spout.math.vector.Vector2;
import org.spout.vanilla.material.PotionReagent;
import org.spout.vanilla.material.item.BlockItem;
import org.spout.vanilla.material.item.potion.PotionItem;

public class RedstoneDust extends BlockItem implements PotionReagent {
	public RedstoneDust(String name, int id, BlockMaterial place, Vector2 pos) {
		super(name, id, place, pos);
	}

	@Override
	public PotionItem getResult(PotionItem original) {
		if (PotionItem.REGENERATION.equals(original)) {
			return PotionItem.REGENERATION_EXTENDED;
		} else if (PotionItem.REGENERATION_II.equals(original)) {
			return PotionItem.REGENERATION_EXTENDED;
		} else if (PotionItem.SPLASH_REGENERATION.equals(original)) {
			return PotionItem.SPLASH_REGENERATION_EXTENDED;
		} else if (PotionItem.SPLASH_REGENERATION_II.equals(original)) {
			return PotionItem.SPLASH_REGENERATION_EXTENDED;
		} else if (PotionItem.SWIFTNESS.equals(original)) {
			return PotionItem.SWIFTNESS_EXTENDED;
		} else if (PotionItem.SWIFTNESS_II.equals(original)) {
			return PotionItem.SWIFTNESS_EXTENDED;
		} else if (PotionItem.SPLASH_SWIFTNESS.equals(original)) {
			return PotionItem.SPLASH_SWIFTNESS_EXTENDED;
		} else if (PotionItem.SPLASH_SWIFTNESS_II.equals(original)) {
			return PotionItem.SPLASH_SWIFTNESS_EXTENDED;
		} else if (PotionItem.FIRE.equals(original)) {
			return PotionItem.FIRE_EXTENDED;
		} else if (PotionItem.SPLASH_FIRE.equals(original)) {
			return PotionItem.SPLASH_FIRE_EXTENDED;
		} else if (PotionItem.POISON.equals(original)) {
			return PotionItem.POISON_EXTENDED;
		} else if (PotionItem.POISON_II.equals(original)) {
			return PotionItem.POISON_EXTENDED;
		} else if (PotionItem.SPLASH_POISON.equals(original)) {
			return PotionItem.SPLASH_POISON_EXTENDED;
		} else if (PotionItem.SPLASH_POISON_II.equals(original)) {
			return PotionItem.SPLASH_POISON_EXTENDED;
		} else if (PotionItem.HEALING_II.equals(original)) {
			return PotionItem.HEALING;
		} else if (PotionItem.SPLASH_HEALING_II.equals(original)) {
			return PotionItem.SPLASH_HEALING;
		} else if (PotionItem.NIGHT_VISION.equals(original)) {
			return PotionItem.NIGHT_VISION_EXTENDED;
		} else if (PotionItem.WEAKNESS.equals(original)) {
			return PotionItem.WEAKNESS_EXTENDED;
		} else if (PotionItem.SPLASH_WEAKNESS.equals(original)) {
			return PotionItem.SPLASH_WEAKNESS_EXTENDED;
		} else if (PotionItem.STRENGTH.equals(original)) {
			return PotionItem.STRENGTH_EXTENDED;
		} else if (PotionItem.STRENGTH_II.equals(original)) {
			return PotionItem.STRENGTH_EXTENDED;
		} else if (PotionItem.SPLASH_STRENGTH.equals(original)) {
			return PotionItem.SPLASH_STRENGTH_EXTENDED;
		} else if (PotionItem.SPLASH_STRENGTH_II.equals(original)) {
			return PotionItem.SPLASH_STRENGTH_EXTENDED;
		} else if (PotionItem.SLOWNESS.equals(original)) {
			return PotionItem.SLOWNESS_EXTENDED;
		} else if (PotionItem.SPLASH_SLOWNESS.equals(original)) {
			return PotionItem.SPLASH_SLOWNESS_EXTENDED;
		} else if (PotionItem.HARMING_II.equals(original)) {
			return PotionItem.HARMING;
		} else if (PotionItem.SPLASH_HARMING_II.equals(original)) {
			return PotionItem.SPLASH_HARMING;
		} else if (PotionItem.INVISIBILITY.equals(original)) {
			return PotionItem.INVISIBILITY_EXTENDED;
		} else if (PotionItem.WATER_BOTTLE.equals(original)) {
			return PotionItem.MUNDANE_EXTENDED;
		}

		return null;
	}
}
