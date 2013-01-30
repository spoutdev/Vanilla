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
package org.spout.vanilla.plugin.protocol.entity.object;

import java.util.HashMap;

import org.spout.vanilla.plugin.component.substance.object.EnderCrystal;
import org.spout.vanilla.plugin.component.substance.object.EyeOfEnder;
import org.spout.vanilla.plugin.component.substance.object.FallingBlock;
import org.spout.vanilla.plugin.component.substance.object.FallingDragonEgg;
import org.spout.vanilla.plugin.component.substance.object.FireworksRocket;
import org.spout.vanilla.plugin.component.substance.object.Item;
import org.spout.vanilla.plugin.component.substance.object.ItemFrame;
import org.spout.vanilla.plugin.component.substance.object.ObjectEntity;
import org.spout.vanilla.plugin.component.substance.object.Tnt;
import org.spout.vanilla.plugin.component.substance.object.WitherSkull;
import org.spout.vanilla.plugin.component.substance.object.projectile.Arrow;
import org.spout.vanilla.plugin.component.substance.object.projectile.Egg;
import org.spout.vanilla.plugin.component.substance.object.projectile.EnderPearl;
import org.spout.vanilla.plugin.component.substance.object.projectile.FishingBob;
import org.spout.vanilla.plugin.component.substance.object.projectile.Potion;
import org.spout.vanilla.plugin.component.substance.object.projectile.Snowball;
import org.spout.vanilla.plugin.component.substance.object.projectile.XPBottle;
import org.spout.vanilla.plugin.component.substance.object.vehicle.Boat;
import org.spout.vanilla.plugin.component.substance.object.vehicle.minecart.Minecart;
import org.spout.vanilla.plugin.component.substance.object.vehicle.minecart.PoweredMinecart;
import org.spout.vanilla.plugin.component.substance.object.vehicle.minecart.StorageMinecart;

public enum ObjectType {
	BOAT(1, Boat.class),
	ITEM(2, Item.class),
	MINECART(10, Minecart.class),
	STORAGE_MINECART(11, StorageMinecart.class),
	POWERED_MINECART(12, PoweredMinecart.class),
	PRIMED_TNT(50, Tnt.class),
	ENDER_CRYSTAL(51, EnderCrystal.class),
	ARROW(60, Arrow.class),
	SNOWBALL(61, Snowball.class),
	EGG(62, Egg.class),
	ENDER_PEARL(65, EnderPearl.class),
	WITHER_SKULL(66, WitherSkull.class),
	FALLING_OBJECT(70, FallingBlock.class),
	ITEM_FRAME(71, ItemFrame.class),
	EYE_OF_ENDER(72, EyeOfEnder.class),
	POTION(73, Potion.class),
	DRAGON_EGG(74, FallingDragonEgg.class),
	EXP_BOTTLE(75, XPBottle.class),
	FIREWORKS_ROCKET(76, FireworksRocket.class),
	FISHING_BOB(90, FishingBob.class);
	private final int id;
	private final Class<? extends ObjectEntity> componentType;
	private static final HashMap<Integer, ObjectType> idMap = new HashMap<Integer, ObjectType>();
	private static final HashMap<Class<? extends ObjectEntity>, ObjectType> typeMap = new HashMap<Class<? extends ObjectEntity>, ObjectType>();

	private ObjectType(int id, Class<? extends ObjectEntity> componentType) {
		this.id = id;
		this.componentType = componentType;
	}

	public int getId() {
		return id;
	}

	public Class<? extends ObjectEntity> getComponentType() {
		return componentType;
	}

	public static ObjectType get(int id) {
		return idMap.get(id);
	}

	public static ObjectType get(Class<? extends ObjectType> type) {
		return typeMap.get(type);
	}

	static {
		for (ObjectType type : ObjectType.values()) {
			idMap.put(type.getId(), type);
			typeMap.put(type.getComponentType(), type);
		}
	}
}
