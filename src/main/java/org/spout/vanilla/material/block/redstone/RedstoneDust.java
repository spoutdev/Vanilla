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
package org.spout.vanilla.material.block.redstone;

import org.spout.api.material.BlockMaterial;
import org.spout.api.math.Vector2;

import org.spout.vanilla.material.PotionReagent;
import org.spout.vanilla.material.item.BlockItem;
import org.spout.vanilla.material.item.potion.Potion;

public class RedstoneDust extends BlockItem implements PotionReagent {
	public RedstoneDust(String name, int id, BlockMaterial place, Vector2 pos) {
		super(name, id, place, pos);
	}

	@Override
	public Potion getResult(Potion original) {
		if (Potion.REGENERATION.equals(original)) {
			return Potion.REGENERATION_EXTENDED;
		} else if (Potion.REGENERATION_II.equals(original)) {
			return Potion.REGENERATION_EXTENDED;
		}
		else if (Potion.SPLASH_REGENERATION.equals(original)) {
			return Potion.SPLASH_REGENERATION_EXTENDED;
		} else if (Potion.SPLASH_REGENERATION_II.equals(original)) {
			return Potion.SPLASH_REGENERATION_EXTENDED;
		}
		else if (Potion.SWIFTNESS.equals(original)) {
			return Potion.SWIFTNESS_EXTENDED;
		} else if (Potion.SWIFTNESS_II.equals(original)) {
			return Potion.SWIFTNESS_EXTENDED;
		}
		else if (Potion.SPLASH_SWIFTNESS.equals(original)) {
			return Potion.SPLASH_SWIFTNESS_EXTENDED;
		} else if (Potion.SPLASH_SWIFTNESS_II.equals(original)) {
			return Potion.SPLASH_SWIFTNESS_EXTENDED;
		}
		else if (Potion.FIRE.equals(original)) {
			return Potion.FIRE_EXTENDED;
		} else if (Potion.SPLASH_FIRE.equals(original)) {
			return Potion.SPLASH_FIRE_EXTENDED;
		}
		else if (Potion.POISON.equals(original)) {
			return Potion.POISON_EXTENDED;
		} else if (Potion.POISON_II.equals(original)) {
			return Potion.POISON_EXTENDED;
		}
		else if (Potion.SPLASH_POISON.equals(original)) {
			return Potion.SPLASH_POISON_EXTENDED;
		} else if (Potion.SPLASH_POISON_II.equals(original)) {
			return Potion.SPLASH_POISON_EXTENDED;
		}
		else if (Potion.HEALING_II.equals(original)) {
			return Potion.HEALING;
		} else if (Potion.SPLASH_HEALING_II.equals(original)) {
			return Potion.SPLASH_HEALING;
		}
		else if (Potion.NIGHT_VISION.equals(original)) {
			return Potion.NIGHT_VISION_EXTENDED;
		}
		else if (Potion.WEAKNESS.equals(original)) {
			return Potion.WEAKNESS_EXTENDED;
		} else if (Potion.SPLASH_WEAKNESS.equals(original)) {
			return Potion.SPLASH_WEAKNESS_EXTENDED;
		}
		else if (Potion.STRENGTH.equals(original)) {
			return Potion.STRENGTH_EXTENDED;
		} else if (Potion.STRENGTH_II.equals(original)) {
			return Potion.STRENGTH_EXTENDED;
		}
		else if (Potion.SPLASH_STRENGTH.equals(original)) {
			return Potion.SPLASH_STRENGTH_EXTENDED;
		} else if (Potion.SPLASH_STRENGTH_II.equals(original)) {
			return Potion.SPLASH_STRENGTH_EXTENDED;
		}
		else if (Potion.SLOWNESS.equals(original)) {
			return Potion.SLOWNESS_EXTENDED;
		} else if (Potion.SPLASH_SLOWNESS.equals(original)) {
			return Potion.SPLASH_SLOWNESS_EXTENDED;
		}
		else if (Potion.HARMING_II.equals(original)) {
			return Potion.HARMING;
		} else if (Potion.SPLASH_HARMING_II.equals(original)) {
			return Potion.SPLASH_HARMING;
		}
		else if (Potion.INVISIBILITY.equals(original)) {
			return Potion.INVISIBILITY_EXTENDED;
		}
		else if (Potion.WATER_BOTTLE.equals(original)) {
			return Potion.MUNDANE_EXTENDED;
		}
		
		return null;
	}


}
