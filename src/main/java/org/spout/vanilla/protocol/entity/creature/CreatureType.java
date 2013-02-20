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

import org.spout.vanilla.component.living.Living;
import org.spout.vanilla.component.living.hostile.Blaze;
import org.spout.vanilla.component.living.hostile.CaveSpider;
import org.spout.vanilla.component.living.hostile.Creeper;
import org.spout.vanilla.component.living.hostile.EnderDragon;
import org.spout.vanilla.component.living.hostile.Ghast;
import org.spout.vanilla.component.living.hostile.Giant;
import org.spout.vanilla.component.living.hostile.MagmaCube;
import org.spout.vanilla.component.living.hostile.Silverfish;
import org.spout.vanilla.component.living.hostile.Skeleton;
import org.spout.vanilla.component.living.hostile.Slime;
import org.spout.vanilla.component.living.hostile.Spider;
import org.spout.vanilla.component.living.hostile.Witch;
import org.spout.vanilla.component.living.hostile.Wither;
import org.spout.vanilla.component.living.hostile.Zombie;
import org.spout.vanilla.component.living.neutral.Bat;
import org.spout.vanilla.component.living.neutral.Enderman;
import org.spout.vanilla.component.living.neutral.PigZombie;
import org.spout.vanilla.component.living.neutral.Wolf;
import org.spout.vanilla.component.living.passive.Chicken;
import org.spout.vanilla.component.living.passive.Cow;
import org.spout.vanilla.component.living.passive.MooshroomCow;
import org.spout.vanilla.component.living.passive.Ocelot;
import org.spout.vanilla.component.living.passive.Pig;
import org.spout.vanilla.component.living.passive.Sheep;
import org.spout.vanilla.component.living.passive.Squid;
import org.spout.vanilla.component.living.passive.Villager;
import org.spout.vanilla.component.living.util.IronGolem;
import org.spout.vanilla.component.living.util.SnowGolem;

public enum CreatureType {
	BAT(65, Bat.class),
	BLAZE(61, Blaze.class),
	CAVE_SPIDER(59, CaveSpider.class),
	CHICKEN(93, Chicken.class),
	COW(92, Cow.class),
	CREEPER(50, Creeper.class),
	ENDER_DRAGON(63, EnderDragon.class),
	ENDERMAN(58, Enderman.class),
	GHAST(56, Ghast.class),
	GIANT(53, Giant.class),
	IRON_GOLEM(99, IronGolem.class),
	MAGMA_CUBE(62, MagmaCube.class),
	MUSHROOM_COW(96, MooshroomCow.class),
	OCELOT(98, Ocelot.class),
	PIG(90, Pig.class),
	PIG_ZOMBIE(57, PigZombie.class),
	SHEEP(91, Sheep.class),
	SILVERFISH(60, Silverfish.class),
	SKELETON(51, Skeleton.class),
	SLIME(55, Slime.class),
	SNOW_GOLEM(97, SnowGolem.class),
	SPIDER(52, Spider.class),
	SQUID(94, Squid.class),
	VILLAGER(120, Villager.class),
	WITCH(66, Witch.class),
	WITHER(64, Wither.class),
	WOLF(95, Wolf.class),
	ZOMBIE(54, Zombie.class);
	private final int id;
	private final Class<? extends Living> componentType;
	private static final Map<Class<?>, CreatureType> types = new HashMap<Class<?>, CreatureType>();
	private static final Map<Integer, CreatureType> idMap = new HashMap<Integer, CreatureType>();

	private CreatureType(int id, Class<? extends Living> componentType) {
		this.id = id;
		this.componentType = componentType;
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
