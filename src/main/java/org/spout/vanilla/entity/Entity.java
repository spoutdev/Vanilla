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

import gnu.trove.map.hash.TIntObjectHashMap;

import org.spout.vanilla.entity.living.hostile.Creeper;
import org.spout.vanilla.entity.living.hostile.Skeleton;
import org.spout.vanilla.entity.living.hostile.Spider;
import org.spout.vanilla.entity.living.hostile.Giant;
import org.spout.vanilla.entity.living.hostile.Zombie;
import org.spout.vanilla.entity.living.hostile.Slime;
import org.spout.vanilla.entity.living.hostile.Ghast;
import org.spout.vanilla.entity.living.neutral.PigZombie;
import org.spout.vanilla.entity.living.neutral.Enderman;
import org.spout.vanilla.entity.living.hostile.CaveSpider;
import org.spout.vanilla.entity.living.hostile.Silverfish;
import org.spout.vanilla.entity.living.hostile.Blaze;
import org.spout.vanilla.entity.living.hostile.MagmaCube;
import org.spout.vanilla.entity.living.hostile.boss.Enderdragon;
import org.spout.vanilla.entity.living.passive.Pig;
import org.spout.vanilla.entity.living.passive.Sheep;
import org.spout.vanilla.entity.living.passive.Cow;
import org.spout.vanilla.entity.living.passive.Chicken;
import org.spout.vanilla.entity.living.passive.Squid;
import org.spout.vanilla.entity.living.neutral.Wolf;
import org.spout.vanilla.entity.living.passive.Mooshroom;
import org.spout.vanilla.entity.living.passive.SnowGolem;
import org.spout.vanilla.entity.living.passive.Villager;

public enum Entity {
	DroppedItem(1),
	XPOrb(2),
	ShotArrow(10),
	ThrownSnowball(11),
	GhastFireball(12),
	BlazeFireball(13),
	ThrownEnderPearl(14),
	ThrownEyeOfEnder(15),
	PrimedTNT(16),
	FallingBlock(21),
	Minecart(40),
	Boat(41),
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
	Villager(120, Villager.class),
	//Squidward
	EnderCrystal(200);

	public final int id;
	public final Class<? extends VanillaEntity> className;

	public static final String KEY = "EntityID";

	final static TIntObjectHashMap<Entity> map = new TIntObjectHashMap<Entity>();
	
	private Entity(int data) {
		this(data, null);
	}
	
	private Entity(int data, Class<? extends VanillaEntity> className) {
		id = (byte) data;
		this.className = className;
	}

	public int getID() {
		return id;
	}
	
	public Class<? extends VanillaEntity> getClassName() {
		return className;
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
