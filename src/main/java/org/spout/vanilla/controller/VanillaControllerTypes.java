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

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import gnu.trove.map.hash.TIntObjectHashMap;

import org.spout.vanilla.controller.block.BrewingStand;
import org.spout.vanilla.controller.block.Chest;
import org.spout.vanilla.controller.block.CraftingTable;
import org.spout.vanilla.controller.block.Dispenser;
import org.spout.vanilla.controller.block.EnchantmentTable;
import org.spout.vanilla.controller.block.Furnace;
import org.spout.vanilla.controller.block.Jukebox;
import org.spout.vanilla.controller.block.MonsterSpawner;
import org.spout.vanilla.controller.block.MovingPiston;
import org.spout.vanilla.controller.block.NoteBlock;
import org.spout.vanilla.controller.block.Sign;
import org.spout.vanilla.controller.living.Human;
import org.spout.vanilla.controller.living.MobControllerType;
import org.spout.vanilla.controller.living.creature.hostile.Blaze;
import org.spout.vanilla.controller.living.creature.hostile.CaveSpider;
import org.spout.vanilla.controller.living.creature.hostile.Creeper;
import org.spout.vanilla.controller.living.creature.hostile.Enderdragon;
import org.spout.vanilla.controller.living.creature.hostile.Ghast;
import org.spout.vanilla.controller.living.creature.hostile.Giant;
import org.spout.vanilla.controller.living.creature.hostile.MagmaSlime;
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
import org.spout.vanilla.controller.object.misc.EnderCrystal;
import org.spout.vanilla.controller.object.misc.Lightning;
import org.spout.vanilla.controller.object.misc.Painting;
import org.spout.vanilla.controller.object.moving.Item;
import org.spout.vanilla.controller.object.moving.MovingBlock;
import org.spout.vanilla.controller.object.moving.PrimedTnt;
import org.spout.vanilla.controller.object.moving.XPOrb;
import org.spout.vanilla.controller.object.projectile.Arrow;
import org.spout.vanilla.controller.object.projectile.BlazeFireball;
import org.spout.vanilla.controller.object.projectile.Egg;
import org.spout.vanilla.controller.object.projectile.EnderPearl;
import org.spout.vanilla.controller.object.projectile.EyeOfEnder;
import org.spout.vanilla.controller.object.projectile.GhastFireball;
import org.spout.vanilla.controller.object.projectile.Snowball;
import org.spout.vanilla.controller.object.vehicle.Boat;
import org.spout.vanilla.controller.object.vehicle.minecart.TransportMinecart;
import org.spout.vanilla.protocol.controller.BasicObjectEntityProtocol;
import org.spout.vanilla.protocol.controller.BasicProjectileEntityProtocol;
import org.spout.vanilla.protocol.controller.living.BlazeEntityProtocol;
import org.spout.vanilla.protocol.controller.living.CreeperEntityProtocol;
import org.spout.vanilla.protocol.controller.living.EnderdragonEntityProtocol;
import org.spout.vanilla.protocol.controller.living.EndermanEntityProtocol;
import org.spout.vanilla.protocol.controller.living.GhastEntityProtocol;
import org.spout.vanilla.protocol.controller.living.HumanEntityProtocol;
import org.spout.vanilla.protocol.controller.living.MagmaCubeEntityProtocol;
import org.spout.vanilla.protocol.controller.living.PigEntityProtocol;
import org.spout.vanilla.protocol.controller.living.SheepEntityProtocol;
import org.spout.vanilla.protocol.controller.living.SlimeEntityProtocol;
import org.spout.vanilla.protocol.controller.living.SpiderEntityProtocol;
import org.spout.vanilla.protocol.controller.living.WolfEntityProtocol;
import org.spout.vanilla.protocol.controller.object.FallingBlockProtocol;
import org.spout.vanilla.protocol.controller.object.LightningEntityProtocol;
import org.spout.vanilla.protocol.controller.object.PaintingEntityProtocol;
import org.spout.vanilla.protocol.controller.object.PickupEntityProtocol;
import org.spout.vanilla.protocol.controller.object.XPOrbEntityProtocol;
import org.spout.vanilla.protocol.controller.object.vehicle.BoatEntityProtocol;
import org.spout.vanilla.protocol.controller.object.vehicle.TransportMinecartEntityProtocol;

/**
 * A lookup for all controllers in Vanilla.
 */
public class VanillaControllerTypes {
	// Entity
	public static final VanillaControllerType DROPPED_ITEM = new VanillaControllerType(1, Item.class, "Item", new PickupEntityProtocol());
	public static final VanillaControllerType XP_ORB = new VanillaControllerType(2, XPOrb.class, "XP Orb", new XPOrbEntityProtocol());
	public static final VanillaControllerType SHOT_ARROW = new VanillaControllerType(10, Arrow.class, "Arrow", new BasicProjectileEntityProtocol(60));
	public static final VanillaControllerType THROWN_SNOWBALL = new VanillaControllerType(11, Snowball.class, "Snowball", new BasicProjectileEntityProtocol(61));
	public static final VanillaControllerType GHAST_FIREBALL = new VanillaControllerType(12, GhastFireball.class, "Fireball", new BasicProjectileEntityProtocol(63));
	public static final VanillaControllerType BLAZE_FIREBALL = new VanillaControllerType(13, BlazeFireball.class, "Blaze Fireball", new BasicProjectileEntityProtocol(64));
	public static final VanillaControllerType THROWN_ENDER_PEARL = new VanillaControllerType(14, EnderPearl.class, "Ender Pearl", new BasicObjectEntityProtocol(65));
	public static final VanillaControllerType THROWN_EYE_OF_ENDER = new VanillaControllerType(15, EyeOfEnder.class, "Eye of Ender", new BasicObjectEntityProtocol(72));
	public static final VanillaControllerType EGG = new VanillaControllerType(15, Egg.class, "Egg", new BasicObjectEntityProtocol(62));
	public static final VanillaControllerType PRIMED_TNT = new VanillaControllerType(16, PrimedTnt.class, "Primed TNT", new BasicObjectEntityProtocol(50));
	public static final VanillaControllerType FALLING_BLOCK = new VanillaControllerType(21, MovingBlock.class, "Falling Block", new FallingBlockProtocol());
	public static final VanillaControllerType MINECART = new VanillaControllerType(40, TransportMinecart.class, "Minecart", new TransportMinecartEntityProtocol());
	public static final VanillaControllerType BOAT = new VanillaControllerType(41, Boat.class, "Boat", new BoatEntityProtocol());
	public static final VanillaControllerType CREEPER = new MobControllerType(50, Creeper.class, "Creeper", new CreeperEntityProtocol());
	public static final VanillaControllerType SKELETON = new MobControllerType(51, Skeleton.class, "Skeleton");
	public static final VanillaControllerType SPIDER = new MobControllerType(52, Spider.class, "Spider", new SpiderEntityProtocol());
	public static final VanillaControllerType GIANT = new MobControllerType(53, Giant.class, "Giant");
	public static final VanillaControllerType ZOMBIE = new MobControllerType(54, Zombie.class, "Zombie");
	public static final VanillaControllerType SLIME = new MobControllerType(55, Slime.class, "Slime", new SlimeEntityProtocol());
	public static final VanillaControllerType GHAST = new MobControllerType(56, Ghast.class, "Ghast", new GhastEntityProtocol());
	public static final VanillaControllerType PIG_ZOMBIE = new MobControllerType(57, PigZombie.class, "Zombie Pigman");
	public static final VanillaControllerType ENDERMAN = new MobControllerType(58, Enderman.class, "Enderman", new EndermanEntityProtocol());
	public static final VanillaControllerType CAVE_SPIDER = new MobControllerType(59, CaveSpider.class, "Cave Spider");
	public static final VanillaControllerType SILVERFISH = new MobControllerType(60, Silverfish.class, "Silverfish");
	public static final VanillaControllerType BLAZE = new MobControllerType(61, Blaze.class, "Blaze", new BlazeEntityProtocol());
	public static final VanillaControllerType MAGMA_CUBE = new MobControllerType(62, MagmaSlime.class, "Magma Cube", new MagmaCubeEntityProtocol());
	public static final VanillaControllerType ENDERDRAGON = new MobControllerType(64, Enderdragon.class, "Enderdragon", new EnderdragonEntityProtocol());
	public static final VanillaControllerType PIG = new MobControllerType(90, Pig.class, "Pig", new PigEntityProtocol());
	public static final VanillaControllerType SHEEP = new MobControllerType(91, Sheep.class, "Sheep", new SheepEntityProtocol());
	public static final VanillaControllerType COW = new MobControllerType(92, Cow.class, "Cow");
	public static final VanillaControllerType CHICKEN = new MobControllerType(93, Chicken.class, "Chicken");
	public static final VanillaControllerType SQUID = new MobControllerType(94, Squid.class, "Squid");
	public static final VanillaControllerType WOLF = new MobControllerType(95, Wolf.class, "Wolf", new WolfEntityProtocol());
	public static final VanillaControllerType MOOSHROOM = new MobControllerType(96, Mooshroom.class, "Mooshroom");
	public static final VanillaControllerType SNOW_GOLEM = new MobControllerType(97, SnowGolem.class, "Snow Golem");
	public static final VanillaControllerType OCELOT = new MobControllerType(98, Ocelot.class, "Ocelot");
	public static final VanillaControllerType VILLAGER = new MobControllerType(120, Villager.class, "Villager");
	public static final VanillaControllerType IRON_GOLEM = new MobControllerType(99, IronGolem.class, "Iron Golem");
	public static final VanillaControllerType ENDER_CRYSTAL = new VanillaControllerType(200, EnderCrystal.class, "Ender Crystal", new BasicObjectEntityProtocol(51));
	public static final VanillaControllerType PAINTINGS = new VanillaControllerType(-1, Painting.class, "Painting", new PaintingEntityProtocol());
	public static final VanillaControllerType HUMAN = new VanillaControllerType(-1, Human.class, "Human", new HumanEntityProtocol());
	// World
	public static final VanillaControllerType LIGHTNING = new VanillaControllerType(-7, Lightning.class, "Lightning", new LightningEntityProtocol());
	// Block
	public static final VanillaControllerType FURNACE = new VanillaControllerType(-5, Furnace.class, "Furnace");
	public static final VanillaControllerType DISPENSER = new VanillaControllerType(-6, Dispenser.class, "Dispenser");
	public static final VanillaControllerType NOTE_BLOCK = new VanillaControllerType(-7, NoteBlock.class, "Note Block");
	public static final VanillaControllerType JUKEBOX = new VanillaControllerType(-8, Jukebox.class, "Jukebox");
	public static final VanillaControllerType SIGN = new VanillaControllerType(-9, Sign.class, "Sign");
	public static final VanillaControllerType PISTON_MOVING = new VanillaControllerType(-10, MovingPiston.class, "Moving Piston");
	public static final VanillaControllerType CHEST = new VanillaControllerType(-11, Chest.class, "Chest");
	public static final VanillaControllerType ENCHANTMENT_TABLE = new VanillaControllerType(-12, EnchantmentTable.class, "Enchantment Table");
	public static final VanillaControllerType MONSTER_SPAWNER = new VanillaControllerType(-13, MonsterSpawner.class, "Monster Spawner");
	public static final VanillaControllerType BREWING_STAND = new VanillaControllerType(-15, BrewingStand.class, "Brewing Stand");
	public static final VanillaControllerType MOVING_BLOCK = new VanillaControllerType(-16, MovingBlock.class, "Moving Block");
	public static final VanillaControllerType CRAFTING_TABLE = new VanillaControllerType(-17, CraftingTable.class, "Crafting Table");
	private static final TIntObjectHashMap<VanillaControllerType> ID_LOOKUP = new TIntObjectHashMap<VanillaControllerType>();

	public static VanillaControllerType getByID(int id) {
		return ID_LOOKUP.get(id);
	}

	static {
		for (Field field : VanillaControllerTypes.class.getFields()) {
			field.setAccessible(true);
			if (Modifier.isStatic(field.getModifiers()) && VanillaControllerType.class.isAssignableFrom(field.getType())) {
				try {
					VanillaControllerType type = (VanillaControllerType) field.get(null);
					ID_LOOKUP.put(type.getMinecraftId(), type);
				} catch (IllegalAccessException e) {
					continue;
				}
			}
		}
	}
}
