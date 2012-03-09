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
package org.spout.vanilla.entity;

import org.spout.vanilla.entity.living.creature.hostile.Blaze;
import org.spout.vanilla.entity.living.creature.hostile.CaveSpider;
import org.spout.vanilla.entity.living.creature.hostile.Creeper;
import org.spout.vanilla.entity.living.creature.hostile.Ghast;
import org.spout.vanilla.entity.living.creature.hostile.Giant;
import org.spout.vanilla.entity.living.creature.hostile.MagmaCube;
import org.spout.vanilla.entity.living.creature.hostile.Silverfish;
import org.spout.vanilla.entity.living.creature.hostile.Skeleton;
import org.spout.vanilla.entity.living.creature.hostile.Slime;
import org.spout.vanilla.entity.living.creature.hostile.Spider;
import org.spout.vanilla.entity.living.creature.hostile.Zombie;
import org.spout.vanilla.entity.living.creature.hostile.Enderdragon;
import org.spout.vanilla.entity.living.creature.neutral.Enderman;
import org.spout.vanilla.entity.living.creature.passive.Ocelot;
import org.spout.vanilla.entity.living.creature.neutral.PigZombie;
import org.spout.vanilla.entity.living.creature.neutral.Wolf;
import org.spout.vanilla.entity.living.creature.passive.Chicken;
import org.spout.vanilla.entity.living.creature.passive.Cow;
import org.spout.vanilla.entity.living.creature.passive.Mooshroom;
import org.spout.vanilla.entity.living.creature.passive.Pig;
import org.spout.vanilla.entity.living.creature.passive.Sheep;
import org.spout.vanilla.entity.living.creature.util.SnowGolem;
import org.spout.vanilla.entity.living.creature.util.IronGolem;
import org.spout.vanilla.entity.living.creature.passive.Squid;
import org.spout.vanilla.entity.living.creature.passive.Villager;
import org.spout.vanilla.entity.object.falling.EnderCrystal;
import org.spout.vanilla.entity.object.falling.FallingBlock;
import org.spout.vanilla.entity.object.Item;
import org.spout.vanilla.entity.object.falling.PrimedTnt;
import org.spout.vanilla.entity.object.projectile.Arrow;
import org.spout.vanilla.entity.object.projectile.BlazeFireball;
import org.spout.vanilla.entity.object.projectile.GhastFireball;
import org.spout.vanilla.entity.object.projectile.EnderPearl;
import org.spout.vanilla.entity.object.projectile.EyeOfEnder;
import org.spout.vanilla.entity.object.projectile.Snowball;
import org.spout.vanilla.entity.object.vehicle.Boat;
import org.spout.vanilla.entity.object.vehicle.Minecart;

import gnu.trove.map.hash.TIntObjectHashMap;

public enum Entity {
	DroppedItem(1, Item.class),
	XPOrb(2, Item.class), // TODO: is this correct?
	ShotArrow(10, Arrow.class),
	ThrownSnowball(11, Snowball.class),
	GhastFireball(12, GhastFireball.class),
	BlazeFireball(13, BlazeFireball.class),
	ThrownEnderPearl(14, EnderPearl.class),
	ThrownEyeOfEnder(15, EyeOfEnder.class),
	PrimedTNT(16, PrimedTnt.class),
	FallingBlock(21, FallingBlock.class),
	Minecart(40, Minecart.class),
	Boat(41, Boat.class),
	Creeper(50, Creeper.class),
	Skeleton(51, Skeleton.class),
	Spider(52, Spider.class),
	GiantZombie(53, Giant.class),
	Zombie(54, Zombie.class),
	Slime(55, Slime.class),
	Ghast(56, Ghast.class),
	PigZombie(57, PigZombie.class),
	Enderman(58, Enderman.class),
	CaveSpider(59, CaveSpider.class),
	Silverfish(60, Silverfish.class),
	Blaze(61, Blaze.class),
	MagmaCube(62, MagmaCube.class),
	EnderDragon(64, Enderdragon.class),
	Pig(90, Pig.class),
	Sheep(91, Sheep.class),
	Cow(92, Cow.class),
	Chicken(93, Chicken.class),
	Squid(94, Squid.class),
	Wolf(95, Wolf.class),
	Mooshroom(96, Mooshroom.class),
	SnowGolem(97, SnowGolem.class),
	Ocelot(98, Ocelot.class),
	Villager(120, Villager.class),
	EnderCrystal(200, EnderCrystal.class),
	IronGolem(99, IronGolem.class);
	
	public final int id;
	public final Class<? extends VanillaEntity> controller;
	public static final String KEY = "EntityID";
	private final static TIntObjectHashMap<Entity> map = new TIntObjectHashMap<Entity>();

	private Entity(int data, Class<? extends VanillaEntity> defaultController) {
		id = (byte) data;
		controller = defaultController;
	}

	public int getID() {
		return id;
	}

	public Class<? extends VanillaEntity> getController() {
		return controller;
	}

	public static Entity getByID(int id) {
		return map.get(id);
	}

	static {
		for (Entity m : Entity.values()) {
			map.put(m.getID(), m);
		}
	}
}
