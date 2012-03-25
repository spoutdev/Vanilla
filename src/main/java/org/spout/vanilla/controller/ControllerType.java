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
package org.spout.vanilla.controller;

import gnu.trove.map.hash.TIntObjectHashMap;

import org.spout.vanilla.controller.living.creature.hostile.Blaze;
import org.spout.vanilla.controller.living.creature.hostile.CaveSpider;
import org.spout.vanilla.controller.living.creature.hostile.Creeper;
import org.spout.vanilla.controller.living.creature.hostile.Enderdragon;
import org.spout.vanilla.controller.living.creature.hostile.Ghast;
import org.spout.vanilla.controller.living.creature.hostile.Giant;
import org.spout.vanilla.controller.living.creature.hostile.MagmaCube;
import org.spout.vanilla.controller.living.creature.hostile.Silverfish;
import org.spout.vanilla.controller.living.creature.hostile.Skeleton;
import org.spout.vanilla.controller.living.creature.hostile.Slime;
import org.spout.vanilla.controller.living.creature.hostile.Spider;
import org.spout.vanilla.controller.living.creature.hostile.Zombie;
import org.spout.vanilla.controller.living.creature.neutral.Enderman;
import org.spout.vanilla.controller.living.creature.neutral.PigZombie;
import org.spout.vanilla.controller.living.creature.neutral.Wolf;
import org.spout.vanilla.controller.living.creature.passive.Chicken;
import org.spout.vanilla.controller.living.creature.passive.Cow;
import org.spout.vanilla.controller.living.creature.passive.Mooshroom;
import org.spout.vanilla.controller.living.creature.passive.Ocelot;
import org.spout.vanilla.controller.living.creature.passive.Pig;
import org.spout.vanilla.controller.living.creature.passive.Sheep;
import org.spout.vanilla.controller.living.creature.passive.Squid;
import org.spout.vanilla.controller.living.creature.passive.Villager;
import org.spout.vanilla.controller.living.creature.util.IronGolem;
import org.spout.vanilla.controller.living.creature.util.SnowGolem;
import org.spout.vanilla.controller.object.MovingBlock;
import org.spout.vanilla.controller.object.misc.EnderCrystal;
import org.spout.vanilla.controller.object.moving.Item;
import org.spout.vanilla.controller.object.moving.PrimedTnt;
import org.spout.vanilla.controller.object.moving.XPOrb;
import org.spout.vanilla.controller.object.projectile.Arrow;
import org.spout.vanilla.controller.object.projectile.BlazeFireball;
import org.spout.vanilla.controller.object.projectile.EnderPearl;
import org.spout.vanilla.controller.object.projectile.EyeOfEnder;
import org.spout.vanilla.controller.object.projectile.GhastFireball;
import org.spout.vanilla.controller.object.projectile.Snowball;
import org.spout.vanilla.controller.object.vehicle.Boat;
import org.spout.vanilla.controller.object.vehicle.Minecart;

/**
 * Enum that serves as a lookup for all controllers in Vanilla.
 */
public enum ControllerType {
	DROPPEDITEM(1, Item.class),
	XPORB(2, XPOrb.class),
	SHOTARROW(10, Arrow.class),
	THROWNSNOWBALL(11, Snowball.class),
	GHASTFIREBALL(12, GhastFireball.class),
	BLAZEFIREBALL(13, BlazeFireball.class),
	THROWNENDERPEARL(14, EnderPearl.class),
	THROWNEYEOFENDER(15, EyeOfEnder.class),
	PRIMEDTNT(16, PrimedTnt.class),
	FALLINGBLOCK(21, MovingBlock.class),
	MINECART(40, Minecart.class),
	BOAT(41, Boat.class),
	CREEPER(50, Creeper.class),
	SKELETON(51, Skeleton.class),
	SPIDER(52, Spider.class),
	GIANT(53, Giant.class),
	ZOMBIE(54, Zombie.class),
	SLIME(55, Slime.class),
	GHAST(56, Ghast.class),
	PIGZOMBIE(57, PigZombie.class),
	ENDERMAN(58, Enderman.class),
	CAVESPIDER(59, CaveSpider.class),
	SILVERFISH(60, Silverfish.class),
	BLAZE(61, Blaze.class),
	MAGMACUBE(62, MagmaCube.class),
	ENDEDRAGON(64, Enderdragon.class),
	PIG(90, Pig.class),
	SHEEP(91, Sheep.class),
	COW(92, Cow.class),
	CHICKEN(93, Chicken.class),
	SQUID(94, Squid.class),
	WOLF(95, Wolf.class),
	MOOSHROOM(96, Mooshroom.class),
	SNOWGOLEM(97, SnowGolem.class),
	OCELOT(98, Ocelot.class),
	VILLAGER(120, Villager.class),
	ENDECRYSTAL(200, EnderCrystal.class),
	IRONGOLEM(99, IronGolem.class);
	public final int id;
	public final Class<? extends VanillaController> controller;
	public static final String KEY = "ControllerID";
	private final static TIntObjectHashMap<ControllerType> map = new TIntObjectHashMap<ControllerType>();

	private ControllerType(int data, Class<? extends VanillaController> defaultController) {
		id = (byte) data;
		controller = defaultController;
	}

	/**
	 * Gets the numerical id of the controller type.
	 * @return id of controller type.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Gets the controller class of the type.
	 * @return class of controller type.
	 */
	public Class<? extends VanillaController> getController() {
		return controller;
	}

	/**
	 * Gets a controller type by it's numerical value.
	 * @param id
	 * @return controller type of id
	 */
	public static ControllerType getById(int id) {
		return map.get(id);
	}

	static {
		for (ControllerType m : ControllerType.values()) {
			map.put(m.getId(), m);
		}
	}
}
