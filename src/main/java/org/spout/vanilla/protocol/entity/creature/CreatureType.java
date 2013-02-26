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
package org.spout.vanilla.protocol.entity.creature;

import java.util.HashMap;
import java.util.Map;

import org.spout.api.component.type.EntityComponent;

import org.spout.vanilla.component.entity.living.Living;
import org.spout.vanilla.component.entity.living.hostile.Blaze;
import org.spout.vanilla.component.entity.living.hostile.CaveSpider;
import org.spout.vanilla.component.entity.living.hostile.Creeper;
import org.spout.vanilla.component.entity.living.hostile.EnderDragon;
import org.spout.vanilla.component.entity.living.hostile.Ghast;
import org.spout.vanilla.component.entity.living.hostile.Giant;
import org.spout.vanilla.component.entity.living.hostile.MagmaCube;
import org.spout.vanilla.component.entity.living.hostile.Silverfish;
import org.spout.vanilla.component.entity.living.hostile.Skeleton;
import org.spout.vanilla.component.entity.living.hostile.Slime;
import org.spout.vanilla.component.entity.living.hostile.Spider;
import org.spout.vanilla.component.entity.living.hostile.Witch;
import org.spout.vanilla.component.entity.living.hostile.Wither;
import org.spout.vanilla.component.entity.living.hostile.Zombie;
import org.spout.vanilla.component.entity.living.neutral.Bat;
import org.spout.vanilla.component.entity.living.neutral.Enderman;
import org.spout.vanilla.component.entity.living.neutral.Human;
import org.spout.vanilla.component.entity.living.neutral.PigZombie;
import org.spout.vanilla.component.entity.living.neutral.Wolf;
import org.spout.vanilla.component.entity.living.passive.Chicken;
import org.spout.vanilla.component.entity.living.passive.Cow;
import org.spout.vanilla.component.entity.living.passive.MooshroomCow;
import org.spout.vanilla.component.entity.living.passive.Ocelot;
import org.spout.vanilla.component.entity.living.passive.Pig;
import org.spout.vanilla.component.entity.living.passive.Sheep;
import org.spout.vanilla.component.entity.living.passive.Squid;
import org.spout.vanilla.component.entity.living.passive.Villager;
import org.spout.vanilla.component.entity.living.util.IronGolem;
import org.spout.vanilla.component.entity.living.util.SnowGolem;

public enum CreatureType {
	BAT(65, Bat.class, "Bat"),
	BLAZE(61, Blaze.class, "Blaze"),
	CAVE_SPIDER(59, CaveSpider.class, "CaveSpider"),
	CHICKEN(93, Chicken.class, "Chicken"),
	COW(92, Cow.class, "Cow"),
	CREEPER(50, Creeper.class, "Creeper"),
	ENDER_DRAGON(63, EnderDragon.class, "EnderDragon"),
	ENDERMAN(58, Enderman.class, "Enderman"),
	GHAST(56, Ghast.class, "Ghast"),
	GIANT(53, Giant.class, "Giant"),
	IRON_GOLEM(99, IronGolem.class, "VillagerGolem"),
	MAGMA_CUBE(62, MagmaCube.class, "LavaSlime"),
	MUSHROOM_COW(96, MooshroomCow.class, "MushroomCow"),
	OCELOT(98, Ocelot.class, "Ocelot"),
	PIG(90, Pig.class, "Pig"),
	PIG_ZOMBIE(57, PigZombie.class, "PigZombie"),
	SHEEP(91, Sheep.class, "Sheep"),
	SILVERFISH(60, Silverfish.class, "Silverfish"),
	SKELETON(51, Skeleton.class, "Skeleton"),
	SLIME(55, Slime.class, "Slime"),
	SNOW_GOLEM(97, SnowGolem.class, "SnowMan"),
	SPIDER(52, Spider.class, "Spider"),
	SQUID(94, Squid.class, "Squid"),
	VILLAGER(120, Villager.class, "Villager"),
	WITCH(66, Witch.class, "Witch"),
	WITHER(64, Wither.class, "WitherBoss"),
	WOLF(95, Wolf.class, "Wolf"),
	ZOMBIE(54, Zombie.class, "Zombie");
	private final int id;
	private final Class<? extends Living> componentType;
	private final String name;
	private static final Map<Class<?>, CreatureType> types = new HashMap<Class<?>, CreatureType>();
	private static final Map<Integer, CreatureType> idMap = new HashMap<Integer, CreatureType>();

	private CreatureType(int id, Class<? extends Living> componentType, String name) {
		this.id = id;
		this.componentType = componentType;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	/**
	 * Gets the minecraft protocol id for the creature type
	 */
	public int getId() {
		return id;
	}

	/**
	 * Gets the entity component associated with the creature type
	 * @return component
	 */
	public Class<? extends Living> getComponentType() {
		return componentType;
	}

	/**
	 * Gets the creature type associated with its exact entity component class
	 * @return creature type, or null if no creature type matching the component could be found
	 */
	public static CreatureType get(Class<? extends EntityComponent> clazz) {
		if (clazz != null) {
			return types.get(clazz);
		}
		return null;
	}

	public static CreatureType get(int id) {
		return idMap.get(id);
	}

	static {
		for (CreatureType ct : values()) {
			types.put(ct.getComponentType(), ct);
			idMap.put(ct.getId(), ct);
		}
	}
}
