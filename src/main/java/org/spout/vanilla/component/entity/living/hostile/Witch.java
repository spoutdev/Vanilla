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
package org.spout.vanilla.component.entity.living.hostile;

import java.util.Random;

import org.spout.api.inventory.ItemStack;

import org.spout.vanilla.VanillaPlugin;
import org.spout.vanilla.component.entity.living.Hostile;
import org.spout.vanilla.component.entity.living.Living;
import org.spout.vanilla.component.entity.misc.DeathDrops;
import org.spout.vanilla.component.entity.misc.Health;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.protocol.entity.creature.CreatureProtocol;
import org.spout.vanilla.protocol.entity.creature.CreatureType;

/**
 * A component that identifies the entity as a CaveSpider.
 */
public class Witch extends Living implements Hostile {
	@Override
	public void onAttached() {
		super.onAttached();
		setEntityProtocol(new CreatureProtocol(CreatureType.WITCH));
		DeathDrops dropComponent = getOwner().add(DeathDrops.class);
		Random random = getRandom();
		dropComponent.addDrop(new ItemStack(VanillaMaterials.GLASS_BOTTLE, random.nextInt(6)));
		dropComponent.addDrop(new ItemStack(VanillaMaterials.GLOWSTONE_DUST, random.nextInt(6)));
		dropComponent.addDrop(new ItemStack(VanillaMaterials.GUNPOWDER, random.nextInt(6)));
		dropComponent.addDrop(new ItemStack(VanillaMaterials.REDSTONE_DUST, random.nextInt(6)));
		dropComponent.addDrop(new ItemStack(VanillaMaterials.SPIDER_EYE, random.nextInt(6)));
		dropComponent.addDrop(new ItemStack(VanillaMaterials.STICK, random.nextInt(6)));
		dropComponent.addDrop(new ItemStack(VanillaMaterials.SUGAR, random.nextInt(6)));
		dropComponent.addXpDrop((short) 5);
		if (getAttachedCount() == 1) {
			getOwner().add(Health.class).setSpawnHealth(26);
		}

		/*
		 * TODO:
		 * 
		 * The witch mob uses splash potions of poison, weakness, damage, and slowness to fight the player. 
		 * The current order of potions thrown is; slowness, poison, they will then proceed to throw potions of 
		 * harming until the player is no longer poisoned which they will rectify by throwing another splash 
		 * potion of poison and will continue to do this. The splash potion of harming will occasionally be 
		 * substituted by potions of slowness. These potions will affect other mobs - not just the player. 
		 */
	}
}
