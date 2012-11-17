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
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public License,
* the MIT license and the SpoutDev License Version 1 along with this program.
* If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
* License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license,
* including the MIT license.
*/
package org.spout.vanilla.component.living;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.spout.api.Spout;
import org.spout.vanilla.component.living.hostile.*;
import org.spout.vanilla.component.living.neutral.*;
import org.spout.vanilla.component.living.passive.*;
import org.spout.vanilla.component.living.util.*;

@SuppressWarnings("unchecked")
public final class VanillaLivingComponents {
	
	// Misc
	public final static Class<Human> HUMAN = Human.class;
	
	// Hostile
	public final static Class<Blaze> BLAZE = Blaze.class;
	public final static Class<CaveSpider> CAVE_SPIDER = CaveSpider.class;
	public final static Class<Creeper> CREEPER = Creeper.class;
	public final static Class<EnderDragon> ENDER_DRAGON = EnderDragon.class;
	public final static Class<Ghast> GHAST = Ghast.class;
	public final static Class<MagmaCube> MAGMA_CUBE = MagmaCube.class;
	public final static Class<Silverfish> SILVERFISH = Silverfish.class;
	public final static Class<Skeleton> SKELETON = Skeleton.class;
	public final static Class<Slime> SLIME = Slime.class;
	public final static Class<Spider> SPIDER = Spider.class;
	public final static Class<Witch> WITCH = Witch.class;
	public final static Class<Wither> WITHER = Wither.class;
	public final static Class<WitherSkeleton> WITHER_SKELETON = WitherSkeleton.class;
	public final static Class<Zombie> ZOMBIE = Zombie.class;
	
	// Neutral
	public final static Class<Bat> BAT = Bat.class;
	public final static Class<Enderman> ENDERMAN = Enderman.class;
	public final static Class<IronGolem> IRON_GOLEM = IronGolem.class;
	public final static Class<PigZombie> PIG_ZOMBIE = PigZombie.class;
	public final static Class<Wolf> WOLF = Wolf.class;
	
	// Passive
	public final static Class<Butcher> BUTCHER = Butcher.class;
	public final static Class<Chicken> CHICKEN = Chicken.class;
	public final static Class<Cow> COW = Cow.class;
	public final static Class<Farmer> FARMER = Farmer.class;
	public final static Class<Librarian> LIBRARIAN = Librarian.class;
	public final static Class<MooshroomCow> MOOSHROOM_COW = MooshroomCow.class;
	public final static Class<Ocelot> OCELOT = Ocelot.class;
	public final static Class<Pig> PIG = Pig.class;
	public final static Class<Priest> PRIEST = Priest.class;
	public final static Class<Sheep> SHEEP = Sheep.class;
	public final static Class<Smith> SMITH = Smith.class;
	public final static Class<SnowGolem> SNOW_GOLEM = SnowGolem.class;
	public final static Class<Squid> SQUID = Squid.class;
	public final static Class<Villager> VILLAGER = Villager.class;
	
	private final static Map<String, Class<? extends LivingComponent>> BY_NAME = new HashMap<String, Class<? extends LivingComponent>>();
	
	static {
		
		for (Field field : VanillaLivingComponents.class.getDeclaredFields()) {
			field.setAccessible(true);
			try {
				final Object object = field.get(null);
				if (object instanceof Class) {
					BY_NAME.put(field.getName().toLowerCase(), (Class<? extends LivingComponent>) object);
				}
			} catch (Exception e) {
				Spout.getLogger().severe("An exception occured while reading field '" + field.getName()
						+ "please report to http://issues.spout.org!");
				e.printStackTrace();
			}
		}
	}
	
	public static Class<? extends LivingComponent> byName(String name)
	{
		return BY_NAME.get(name.toLowerCase());
	}
	
	public static Collection<Class<? extends LivingComponent>> getComponents()
	{
		return BY_NAME.values();
	}
}

