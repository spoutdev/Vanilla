/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, SpoutDev <http://www.spout.org/>
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
package org.spout.vanilla.material;

import gnu.trove.map.hash.TShortObjectHashMap;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.spout.api.Spout;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.Material;
import org.spout.api.material.MaterialRegistry;
import org.spout.api.util.map.concurrent.AtomicShortArray;
import org.spout.vanilla.material.block.Solid;
import org.spout.vanilla.material.block.controlled.BrewingStand;
import org.spout.vanilla.material.block.controlled.Chest;
import org.spout.vanilla.material.block.controlled.Dispenser;
import org.spout.vanilla.material.block.controlled.EnchantmentTable;
import org.spout.vanilla.material.block.controlled.Furnace;
import org.spout.vanilla.material.block.controlled.Jukebox;
import org.spout.vanilla.material.block.controlled.LockedChest;
import org.spout.vanilla.material.block.controlled.MonsterSpawner;
import org.spout.vanilla.material.block.controlled.NoteBlock;
import org.spout.vanilla.material.block.controlled.PistonExtensionMoving;
import org.spout.vanilla.material.block.controlled.SignBase;
import org.spout.vanilla.material.block.door.IronDoorBlock;
import org.spout.vanilla.material.block.door.WoodenDoorBlock;
import org.spout.vanilla.material.block.liquid.Lava;
import org.spout.vanilla.material.block.liquid.Water;
import org.spout.vanilla.material.block.misc.BedBlock;
import org.spout.vanilla.material.block.misc.CakeBlock;
import org.spout.vanilla.material.block.misc.EndPortalFrame;
import org.spout.vanilla.material.block.misc.FarmLand;
import org.spout.vanilla.material.block.misc.FenceGate;
import org.spout.vanilla.material.block.misc.Fire;
import org.spout.vanilla.material.block.misc.GlassPane;
import org.spout.vanilla.material.block.misc.Ladder;
import org.spout.vanilla.material.block.misc.Lever;
import org.spout.vanilla.material.block.misc.Slab;
import org.spout.vanilla.material.block.misc.Snow;
import org.spout.vanilla.material.block.misc.StoneButton;
import org.spout.vanilla.material.block.misc.Torch;
import org.spout.vanilla.material.block.misc.TrapDoor;
import org.spout.vanilla.material.block.misc.Web;
import org.spout.vanilla.material.block.misc.fence.NetherBrickFence;
import org.spout.vanilla.material.block.misc.fence.WoodenFence;
import org.spout.vanilla.material.block.ore.CoalOre;
import org.spout.vanilla.material.block.ore.DiamondBlock;
import org.spout.vanilla.material.block.ore.DiamondOre;
import org.spout.vanilla.material.block.ore.Glowstone;
import org.spout.vanilla.material.block.ore.GoldBlock;
import org.spout.vanilla.material.block.ore.GoldOre;
import org.spout.vanilla.material.block.ore.IronBlock;
import org.spout.vanilla.material.block.ore.IronOre;
import org.spout.vanilla.material.block.ore.LapisLazuliBlock;
import org.spout.vanilla.material.block.ore.LapisLazuliOre;
import org.spout.vanilla.material.block.ore.MelonBlock;
import org.spout.vanilla.material.block.ore.RedstoneOre;
import org.spout.vanilla.material.block.piston.Piston;
import org.spout.vanilla.material.block.piston.PistonExtension;
import org.spout.vanilla.material.block.plant.Cactus;
import org.spout.vanilla.material.block.plant.DeadBush;
import org.spout.vanilla.material.block.plant.Flower;
import org.spout.vanilla.material.block.plant.LilyPad;
import org.spout.vanilla.material.block.plant.MelonStem;
import org.spout.vanilla.material.block.plant.Mushroom;
import org.spout.vanilla.material.block.plant.NetherWartBlock;
import org.spout.vanilla.material.block.plant.PumpkinStem;
import org.spout.vanilla.material.block.plant.Sapling;
import org.spout.vanilla.material.block.plant.SugarCaneBlock;
import org.spout.vanilla.material.block.plant.TallGrass;
import org.spout.vanilla.material.block.plant.Vines;
import org.spout.vanilla.material.block.plant.WheatCrop;
import org.spout.vanilla.material.block.portal.EndPortal;
import org.spout.vanilla.material.block.portal.NetherPortal;
import org.spout.vanilla.material.block.pressureplate.StonePressurePlate;
import org.spout.vanilla.material.block.pressureplate.WoodenPressurePlate;
import org.spout.vanilla.material.block.rail.DetectorRail;
import org.spout.vanilla.material.block.rail.PoweredRail;
import org.spout.vanilla.material.block.rail.Rail;
import org.spout.vanilla.material.block.redstone.RedstoneRepeater;
import org.spout.vanilla.material.block.redstone.RedstoneTorch;
import org.spout.vanilla.material.block.redstone.RedstoneWire;
import org.spout.vanilla.material.block.solid.Bedrock;
import org.spout.vanilla.material.block.solid.BookShelf;
import org.spout.vanilla.material.block.solid.Brick;
import org.spout.vanilla.material.block.solid.ClayBlock;
import org.spout.vanilla.material.block.solid.Cobblestone;
import org.spout.vanilla.material.block.solid.CraftingTable;
import org.spout.vanilla.material.block.solid.Dirt;
import org.spout.vanilla.material.block.solid.DoubleSlab;
import org.spout.vanilla.material.block.solid.Endstone;
import org.spout.vanilla.material.block.solid.Glass;
import org.spout.vanilla.material.block.solid.Grass;
import org.spout.vanilla.material.block.solid.Gravel;
import org.spout.vanilla.material.block.solid.Ice;
import org.spout.vanilla.material.block.solid.IronBarsBlock;
import org.spout.vanilla.material.block.solid.Leaves;
import org.spout.vanilla.material.block.solid.Log;
import org.spout.vanilla.material.block.solid.MossStone;
import org.spout.vanilla.material.block.solid.MushroomBlock;
import org.spout.vanilla.material.block.solid.Mycelium;
import org.spout.vanilla.material.block.solid.NetherBrick;
import org.spout.vanilla.material.block.solid.NetherRack;
import org.spout.vanilla.material.block.solid.Obsidian;
import org.spout.vanilla.material.block.solid.Plank;
import org.spout.vanilla.material.block.solid.Pumpkin;
import org.spout.vanilla.material.block.solid.RedstoneLamp;
import org.spout.vanilla.material.block.solid.Sand;
import org.spout.vanilla.material.block.solid.Sandstone;
import org.spout.vanilla.material.block.solid.SilverfishStone;
import org.spout.vanilla.material.block.solid.SnowBlock;
import org.spout.vanilla.material.block.solid.SoulSand;
import org.spout.vanilla.material.block.solid.Sponge;
import org.spout.vanilla.material.block.solid.Stone;
import org.spout.vanilla.material.block.solid.StoneBrick;
import org.spout.vanilla.material.block.solid.TNT;
import org.spout.vanilla.material.block.solid.Wool;
import org.spout.vanilla.material.block.stair.BrickStairs;
import org.spout.vanilla.material.block.stair.CobblestoneStairs;
import org.spout.vanilla.material.block.stair.NetherBrickStairs;
import org.spout.vanilla.material.block.stair.StoneBrickStairs;
import org.spout.vanilla.material.block.stair.WoodenStairs;
import org.spout.vanilla.material.item.BlockItem;
import org.spout.vanilla.material.item.EmptyContainer;
import org.spout.vanilla.material.item.Food;
import org.spout.vanilla.material.item.Food.FoodEffectType;
import org.spout.vanilla.material.item.FullContainer;
import org.spout.vanilla.material.item.VanillaItemMaterial;
import org.spout.vanilla.material.item.armor.chain.ChainBoots;
import org.spout.vanilla.material.item.armor.chain.ChainChestplate;
import org.spout.vanilla.material.item.armor.chain.ChainHelmet;
import org.spout.vanilla.material.item.armor.chain.ChainLeggings;
import org.spout.vanilla.material.item.armor.diamond.DiamondBoots;
import org.spout.vanilla.material.item.armor.diamond.DiamondChestplate;
import org.spout.vanilla.material.item.armor.diamond.DiamondHelmet;
import org.spout.vanilla.material.item.armor.diamond.DiamondLeggings;
import org.spout.vanilla.material.item.armor.gold.GoldBoots;
import org.spout.vanilla.material.item.armor.gold.GoldChestplate;
import org.spout.vanilla.material.item.armor.gold.GoldHelmet;
import org.spout.vanilla.material.item.armor.gold.GoldLeggings;
import org.spout.vanilla.material.item.armor.iron.IronBoots;
import org.spout.vanilla.material.item.armor.iron.IronChestplate;
import org.spout.vanilla.material.item.armor.iron.IronHelmet;
import org.spout.vanilla.material.item.armor.iron.IronLeggings;
import org.spout.vanilla.material.item.armor.leather.LeatherBoots;
import org.spout.vanilla.material.item.armor.leather.LeatherCap;
import org.spout.vanilla.material.item.armor.leather.LeatherPants;
import org.spout.vanilla.material.item.armor.leather.LeatherTunic;
import org.spout.vanilla.material.item.food.RawBeef;
import org.spout.vanilla.material.item.food.RawChicken;
import org.spout.vanilla.material.item.food.RawFish;
import org.spout.vanilla.material.item.food.RawPorkchop;
import org.spout.vanilla.material.item.misc.BlazeRod;
import org.spout.vanilla.material.item.misc.Clay;
import org.spout.vanilla.material.item.misc.Coal;
import org.spout.vanilla.material.item.misc.Dye;
import org.spout.vanilla.material.item.misc.LavaBucket;
import org.spout.vanilla.material.item.misc.MinecartItem;
import org.spout.vanilla.material.item.misc.MusicDisc;
import org.spout.vanilla.material.item.misc.PaintingItem;
import org.spout.vanilla.material.item.misc.Potion;
import org.spout.vanilla.material.item.misc.PoweredMinecartItem;
import org.spout.vanilla.material.item.misc.Shears;
import org.spout.vanilla.material.item.misc.Sign;
import org.spout.vanilla.material.item.misc.SpawnEgg;
import org.spout.vanilla.material.item.misc.Stick;
import org.spout.vanilla.material.item.misc.StorageMinecartItem;
import org.spout.vanilla.material.item.tool.Axe;
import org.spout.vanilla.material.item.tool.FlintAndSteel;
import org.spout.vanilla.material.item.tool.Hoe;
import org.spout.vanilla.material.item.tool.Pickaxe;
import org.spout.vanilla.material.item.tool.Spade;
import org.spout.vanilla.material.item.tool.Tool;
import org.spout.vanilla.material.item.weapon.Bow;
import org.spout.vanilla.material.item.weapon.Sword;
import org.spout.vanilla.util.Music;

// TODO: Remove all casts and separate remaining "set" methods out into each material's init() method
public final class VanillaMaterials {
	public static final BlockMaterial AIR = BlockMaterial.AIR;
	public static final Stone STONE = new Stone("Stone", 1);
	public static final Grass GRASS = new Grass("Grass", 2);
	public static final Dirt DIRT = new Dirt("Dirt", 3);
	public static final Cobblestone COBBLESTONE = new Cobblestone("Cobblestone", 4);
	public static final Plank PLANK = Plank.PLANK;
	public static final Bedrock BEDROCK = new Bedrock("Bedrock", 7);
	public static final Sand SAND = new Sand("Sand", 12);
	public static final Gravel GRAVEL = new Gravel("Gravel", 13);
	public static final Log LOG = Log.DEFAULT;
	public static final Leaves LEAVES = Leaves.DEFAULT;
	public static final Sponge SPONGE = new Sponge("Sponge", 19);
	public static final Glass GLASS = new Glass("Glass", 20);
	public static final Dispenser DISPENSER = new Dispenser("Dispenser", 23);
	public static final Sandstone SANDSTONE = Sandstone.SANDSTONE;
	public static final NoteBlock NOTEBLOCK = new NoteBlock("Note Block", 25);
	public static final BedBlock BED_BLOCK = new BedBlock("Bed", 26);
	public static final Web WEB = new Web("Cobweb", 30);
	//== Piston ==
	public static final Piston PISTON_STICKY_BASE = new Piston("Sticky Piston", 29, true);
	public static final Piston PISTON_BASE = new Piston("Piston", 33, false);
	public static final PistonExtension PISTON_EXTENSION = new PistonExtension("Piston (Head)", 34);
	public static final PistonExtensionMoving PISTON_MOVING = new PistonExtensionMoving("Moved By Piston", 36);
	//== Rails ==
	public static final Rail RAIL = new Rail("Rail", 66);
	public static final PoweredRail RAIL_POWERED = new PoweredRail("Powered Rail", 27);
	public static final DetectorRail RAIL_DETECTOR = new DetectorRail("Detector Rail", 28);
	//== Liquids ==
	public static final Water WATER = new Water("Water", 8, true);
	public static final Water STATIONARY_WATER = new Water("Stationary Water", 9, false);
	public static final Lava LAVA = new Lava("Lava", 10, true);
	public static final Lava STATIONARY_LAVA = new Lava("Stationary Lava", 11, false);
	//== Ores ==
	public static final CoalOre COAL_ORE = new CoalOre("Coal Ore", 16);
	public static final IronOre IRON_ORE = new IronOre("Iron Ore", 15);
	public static final GoldOre GOLD_ORE = new GoldOre("Gold Ore", 14);
	public static final DiamondOre DIAMOND_ORE = new DiamondOre("Diamond Ore", 56);
	public static final LapisLazuliOre LAPIS_LAZULI_ORE = new LapisLazuliOre("Lapis Lazuli Ore", 21);
	public static final RedstoneOre REDSTONE_ORE = new RedstoneOre("Redstone Ore", 73, false);
	public static final RedstoneOre GLOWING_REDSTONE_ORE = new RedstoneOre("Glowing Redstone Ore", 74, true);
	//== Solid blocks ==
	public static final GoldBlock GOLD_BLOCK = new GoldBlock("Gold Block", 41);
	public static final IronBlock IRON_BLOCK = new IronBlock("Iron Block", 42);
	public static final DiamondBlock DIAMOND_BLOCK = new DiamondBlock("Diamond Block", 57);
	public static final LapisLazuliBlock LAPIS_LAZULI_BLOCK = new LapisLazuliBlock("Lapis Lazuli Block", 22);
	//== Plants ==
	public static final TallGrass TALL_GRASS = TallGrass.TALL_GRASS;
	public static final DeadBush DEAD_BUSH = new DeadBush("Dead Shrubs", 32);
	public static final Sapling SAPLING = Sapling.DEFAULT;
	public static final Flower DANDELION = new Flower("Dandelion", 37);
	public static final Flower ROSE = new Flower("Rose", 38);
	public static final Mushroom BROWN_MUSHROOM = new Mushroom("Brown Mushroom", 39);
	public static final Mushroom RED_MUSHROOM = new Mushroom("Red Mushroom", 40);
	//== Stairs ==
	public static final NetherBrickStairs STAIRS_NETHER_BRICK = new NetherBrickStairs("Nether Brick Stairs", 114);
	public static final BrickStairs STAIRS_BRICK = new BrickStairs("Brick Stairs", 108);
	public static final CobblestoneStairs STAIRS_COBBLESTONE = new CobblestoneStairs("Cobblestone Stairs", 67);
	public static final WoodenStairs STAIRS_WOODEN = new WoodenStairs("Wooden Stairs", 53);
	public static final StoneBrickStairs STAIRS_STONE_BRICK = new StoneBrickStairs("Stone Brick Stairs", 109);
	//== Portals ==
	public static final NetherPortal PORTAL = new NetherPortal("Portal", 90);
	public static final EndPortal END_PORTAL = new EndPortal("End Portal", 119);
	public static final EndPortalFrame END_PORTAL_FRAME = new EndPortalFrame("End Portal Frame", 120);
	//================
	public static final DoubleSlab DOUBLE_SLABS = DoubleSlab.STONE;
	public static final Slab SLAB = Slab.STONE;
	public static final Wool WOOL = Wool.WHITE;
	public static final Brick BRICK = new Brick("Brick Block", 45);
	public static final TNT TNT = new TNT("TNT", 46);
	public static final BookShelf BOOKSHELF = new BookShelf("Bookshelf", 47);
	public static final MossStone MOSS_STONE = new MossStone("Moss Stone", 48);
	public static final Obsidian OBSIDIAN = new Obsidian("Obsidian", 49);
	public static final Torch TORCH = new Torch("Torch", 50);
	public static final Fire FIRE = new Fire("Fire", 51);
	public static final MonsterSpawner MONSTER_SPAWNER = new MonsterSpawner("Monster Spawner", 52);
	public static final Chest CHEST = new Chest("Chest", 54);
	public static final RedstoneWire REDSTONE_WIRE = new RedstoneWire("Redstone Wire", 55);
	public static final CraftingTable CRAFTING_TABLE = new CraftingTable("Crafting Table", 58);
	public static final WheatCrop WHEATCROP = new WheatCrop("Wheat Crop", 59);
	public static final FarmLand FARMLAND = new FarmLand("Farmland", 60);
	public static final Furnace FURNACE = new Furnace("Furnace", 61, false);
	public static final Furnace FURNACE_BURNING = new Furnace("Burning Furnace", 62, true);
	public static final SignBase SIGN_POST = new SignBase("Sign Post", 63);
	public static final Ladder LADDER = new Ladder("Ladder", 65);
	public static final SignBase WALL_SIGN = new SignBase("Wall Sign", 68);
	public static final Lever LEVER = new Lever("Lever", 69);
	public static final StonePressurePlate STONE_PRESSURE_PLATE = new StonePressurePlate("Stone Pressure Plate", 70);
	public static final IronDoorBlock IRON_DOOR_BLOCK = new IronDoorBlock("Iron Door", 71);
	public static final WoodenDoorBlock WOODEN_DOOR_BLOCK = new WoodenDoorBlock("Wooden Door", 64);
	public static final WoodenPressurePlate WOODEN_PRESSURE_PLATE = new WoodenPressurePlate("Wooden Pressure Plate", 72);
	public static final RedstoneTorch REDSTONE_TORCH_OFF = new RedstoneTorch("Redstone Torch", 75, false);
	public static final RedstoneTorch REDSTONE_TORCH_ON = new RedstoneTorch("Redstone Torch (On)", 76, true);
	public static final StoneButton STONE_BUTTON = new StoneButton("Stone Button", 77);
	public static final Snow SNOW = new Snow("Snow", 78);
	public static final Ice ICE = new Ice("Ice", 79);
	public static final SnowBlock SNOW_BLOCK = new SnowBlock("Snow Block", 80);
	public static final Cactus CACTUS = new Cactus("Cactus", 81);
	public static final ClayBlock CLAY_BLOCK = new ClayBlock("Clay Block", 82);
	public static final SugarCaneBlock SUGAR_CANE_BLOCK = new SugarCaneBlock("Sugar Cane", 83);
	public static final Jukebox JUKEBOX = new Jukebox("Jukebox", 84);
	public static final WoodenFence WOODEN_FENCE = new WoodenFence("Wooden Fence", 85);
	public static final Pumpkin PUMPKIN = new Pumpkin("Pumpkin", 86, false);
	public static final NetherRack NETHERRACK = new NetherRack("Netherrack", 87);
	public static final SoulSand SOUL_SAND = new SoulSand("Soul Sand", 88);
	public static final Glowstone GLOWSTONE_BLOCK = new Glowstone("Glowstone Block", 89);
	public static final Pumpkin JACK_O_LANTERN = new Pumpkin("Jack 'o' Lantern", 91, true);
	public static final Endstone END_STONE = new Endstone("End Stone", 121);
	public static final CakeBlock CAKE_BLOCK = new CakeBlock("Cake Block", 92);
	public static final RedstoneRepeater REDSTONE_REPEATER_OFF = new RedstoneRepeater("Redstone Repeater", 93, false);
	public static final RedstoneRepeater REDSTONE_REPEATER_ON = new RedstoneRepeater("Redstone Repeater (On)", 94, true);
	public static final LockedChest LOCKED_CHEST = new LockedChest("Locked Chest", 95);
	public static final TrapDoor TRAPDOOR = new TrapDoor("Trapdoor", 96);
	public static final SilverfishStone SILVERFISH_STONE = new SilverfishStone("Silverfish Stone", 97);
	public static final StoneBrick STONE_BRICK = StoneBrick.STONE;
	public static final MushroomBlock HUGE_BROWN_MUSHROOM = new MushroomBlock("Huge Brown Mushroom", 99);
	public static final MushroomBlock HUGE_RED_MUSHROOM = new MushroomBlock("Huge Red Mushroom", 100);
	public static final MelonBlock MELON_BLOCK = new MelonBlock("Melon", 103);
	public static final IronBarsBlock IRON_BARS = new IronBarsBlock("Iron Bars", 101);
	public static final GlassPane GLASS_PANE = new GlassPane("Glass Pane", 102);
	public static final PumpkinStem PUMPKIN_STEM = new PumpkinStem("Pumpkin Stem", 104);
	public static final MelonStem MELON_STEM = new MelonStem("Melon Stem", 105);
	public static final Vines VINES = new Vines("Vines", 106);
	public static final FenceGate FENCE_GATE = new FenceGate("Fence Gate", 107);
	public static final Mycelium MYCELIUM = new Mycelium("Mycelium", 110);
	public static final LilyPad LILY_PAD = new LilyPad("Lily Pad", 111);
	public static final NetherBrick NETHER_BRICK = new NetherBrick("Nether Brick", 112);
	public static final NetherBrickFence NETHER_BRICK_FENCE = new NetherBrickFence("Nether Brick Fence", 113);
	public static final NetherWartBlock NETHER_WART_BLOCK = new NetherWartBlock("Nether Wart", 115);
	public static final EnchantmentTable ENCHANTMENT_TABLE = new EnchantmentTable("Enchantment Table", 116);
	public static final BrewingStand BREWING_STAND_BLOCK = new BrewingStand("Brewing Stand", 117);
	public static final Solid CAULDRON_BLOCK = (Solid) new Solid("Cauldron", 118).setHardness(2.0F).setResistance(3.3F);
	public static final Solid DRAGON_EGG = (Solid) new Solid("Dragon Egg", 122).setHardness(3.0F).setResistance(15.0F).setLightLevel(1);
	public static final RedstoneLamp REDSTONE_LAMP_OFF = new RedstoneLamp("Redstone Lamp", 123, false);
	public static final RedstoneLamp REDSTONE_LAMP_ON = new RedstoneLamp("Redstone Lamp (On)", 124, true);
	/*
	 * Items
	 */
	//== Swords ==
	public static final Sword WOODEN_SWORD = (Sword) new Sword("Wooden Sword", 268, (short) 60).setDamage(4);
	public static final Sword GOLD_SWORD = (Sword) new Sword("Gold Sword", 283, (short) 33).setDamage(4);
	public static final Sword STONE_SWORD = (Sword) new Sword("Stone Sword", 272, (short) 132).setDamage(5);
	public static final Sword IRON_SWORD = (Sword) new Sword("Iron Sword", 267, (short) 251).setDamage(6);
	public static final Sword DIAMOND_SWORD = (Sword) new Sword("Diamond Sword", 276, (short) 1562).setDamage(7);
	//== Spades ==
	public static final Spade GOLD_SPADE = new Spade("Gold Spade", 284, (short) 33);
	public static final Spade WOODEN_SPADE = new Spade("Wooden Spade", 269, (short) 60);
	public static final Spade STONE_SPADE = new Spade("Stone Spade", 273, (short) 132);
	public static final Spade IRON_SPADE = new Spade("Iron Spade", 256, (short) 251);
	public static final Spade DIAMOND_SPADE = new Spade("Diamond Spade", 277, (short) 1562);
	//== Pickaxes ==
	public static final Pickaxe GOLD_PICKAXE = (Pickaxe) new Pickaxe("Gold Pickaxe", 285, (short) 33).setDamage(2);
	public static final Pickaxe WOODEN_PICKAXE = (Pickaxe) new Pickaxe("Wooden Pickaxe", 270, (short) 60).setDamage(2);
	public static final Pickaxe STONE_PICKAXE = (Pickaxe) new Pickaxe("Stone Pickaxe", 274, (short) 132).setDamage(3);
	public static final Pickaxe IRON_PICKAXE = (Pickaxe) new Pickaxe("Iron Pickaxe", 257, (short) 251).setDamage(4);
	public static final Pickaxe DIAMOND_PICKAXE = (Pickaxe) new Pickaxe("Diamond Pickaxe", 278, (short) 1562).setDamage(5);
	//== Axes ==
	public static final Axe GOLD_AXE = (Axe) new Axe("Gold Axe", 286, (short) 33).setDamage(3);
	public static final Axe WOODEN_AXE = (Axe) new Axe("Wooden Axe", 271, (short) 60).setDamage(3);
	public static final Axe STONE_AXE = (Axe) new Axe("Stone Axe", 275, (short) 132).setDamage(3);
	public static final Axe IRON_AXE = (Axe) new Axe("Iron Axe", 258, (short) 251).setDamage(5);
	public static final Axe DIAMOND_AXE = (Axe) new Axe("Diamond Axe", 279, (short) 1562).setDamage(6);
	//== Hoes ==
	public static final Hoe GOLD_HOE = new Hoe("Gold Hoe", 294, (short) 33);
	public static final Hoe WOODEN_HOE = new Hoe("Wooden Hoe", 290, (short) 60);
	public static final Hoe STONE_HOE = new Hoe("Stone Hoe", 291, (short) 132);
	public static final Hoe IRON_HOE = new Hoe("Iron Hoe", 292, (short) 251);
	public static final Hoe DIAMOND_HOE = new Hoe("Diamond Hoe", 293, (short) 1562);
	//== Headwear ==
	public static final LeatherCap LEATHER_CAP = new LeatherCap("Leather Cap", 298, 1);
	public static final ChainHelmet CHAIN_HELMET = new ChainHelmet("Chain Helmet", 302, 2);
	public static final IronHelmet IRON_HELMET = new IronHelmet("Iron Helmet", 306, 2);
	public static final GoldHelmet GOLD_HELMET = new GoldHelmet("Gold Helmet", 314, 2);
	public static final DiamondHelmet DIAMOND_HELMET = new DiamondHelmet("Diamond Helmet", 310, 3);
	//== Chestwear ==
	public static final LeatherTunic LEATHER_TUNIC = new LeatherTunic("Leather Tunic", 299, 3);
	public static final ChainChestplate CHAIN_CHESTPLATE = new ChainChestplate("Chain Chestplate", 303, 5);
	public static final IronChestplate IRON_CHESTPLATE = new IronChestplate("Iron Chestplate", 307, 6);
	public static final DiamondChestplate DIAMOND_CHESTPLATE = new DiamondChestplate("Diamond Chestplate", 311, 8);
	public static final GoldChestplate GOLD_CHESTPLATE = new GoldChestplate("Gold Chestplate", 315, 5);
	//== Legwear ==
	public static final LeatherPants LEATHER_PANTS = new LeatherPants("Leather Pants", 300, 2);
	public static final ChainLeggings CHAIN_LEGGINGS = new ChainLeggings("Chain Leggings", 304, 4);
	public static final IronLeggings IRON_LEGGINGS = new IronLeggings("Iron Leggings", 308, 5);
	public static final DiamondLeggings DIAMOND_LEGGINGS = new DiamondLeggings("Diamond Leggings", 312, 6);
	public static final GoldLeggings GOLD_LEGGINGS = new GoldLeggings("Gold Leggings", 316, 3);
	//== Footwear ==
	public static final LeatherBoots LEATHER_BOOTS = new LeatherBoots("Leather Boots", 301, 1);
	public static final ChainBoots CHAIN_BOOTS = new ChainBoots("Chain Boots", 305, 1);
	public static final IronBoots IRON_BOOTS = new IronBoots("Iron Boots", 309, 2);
	public static final DiamondBoots DIAMOND_BOOTS = new DiamondBoots("Diamond Boots", 313, 3);
	public static final GoldBoots GOLD_BOOTS = new GoldBoots("Gold Boots", 317, 1);
	//== Other tool, weapon and equipment ==
	public static final FlintAndSteel FLINT_AND_STEEL = new FlintAndSteel("Flint and Steel", 259, (short) 64);
	public static final Bow BOW = new Bow("Bow", 261, (short) 385);
	public static final VanillaItemMaterial ARROW = new VanillaItemMaterial("Arrow", 262);
	public static final Tool FISHING_ROD = new Tool("Fishing Rod", 346, (short) 65);
	//== Buckets=
	public static final EmptyContainer BUCKET = new EmptyContainer("Bucket", 325);
	public static final FullContainer WATER_BUCKET = new FullContainer("Water Bucket", 326, WATER, BUCKET);
	public static final LavaBucket LAVA_BUCKET = new LavaBucket("Lava Bucket", 327, LAVA, BUCKET);
	public static final VanillaItemMaterial MILK_BUCKET = new VanillaItemMaterial("Milk", 335);
	//== Minecarts ==
	public static final MinecartItem MINECART = new MinecartItem("Minecart", 328);
	public static final StorageMinecartItem MINECART_CHEST = new StorageMinecartItem("Minecart with Chest", 342);
	public static final PoweredMinecartItem MINECART_FURNACE = new PoweredMinecartItem("Minecart with Furnace", 343);
	//== Others ==
	public static final Coal COAL = Coal.COAL;
	public static final Clay CLAY = new Clay("Clay", 337);
	public static final BlockItem REDSTONE_DUST = new BlockItem("Redstone", 331, VanillaMaterials.REDSTONE_WIRE);
	public static final VanillaItemMaterial DIAMOND = new VanillaItemMaterial("Diamond", 264);
	public static final VanillaItemMaterial IRON_INGOT = new VanillaItemMaterial("Iron Ingot", 265);
	public static final VanillaItemMaterial GOLD_INGOT = new VanillaItemMaterial("Gold Ingot", 266);
	public static final Stick STICK = new Stick("Stick", 280);
	public static final VanillaItemMaterial BOWL = new VanillaItemMaterial("Bowl", 281);
	public static final VanillaItemMaterial STRING = new VanillaItemMaterial("String", 287);
	public static final VanillaItemMaterial FEATHER = new VanillaItemMaterial("Feather", 288);
	public static final VanillaItemMaterial GUNPOWDER = new VanillaItemMaterial("Gunpowder", 289);
	public static final VanillaItemMaterial WHEAT = new VanillaItemMaterial("Wheat", 296);
	public static final VanillaItemMaterial FLINT = new VanillaItemMaterial("Flint", 318);
	public static final PaintingItem PAINTING = new PaintingItem("Painting", 321);
	public static final Sign SIGN = new Sign("Sign", 323);
	public static final BlockItem SEEDS = new BlockItem("Seeds", 295, WHEATCROP);
	public static final BlockItem WOODEN_DOOR = new BlockItem("Wooden Door", 324, WOODEN_DOOR_BLOCK);
	public static final BlockItem IRON_DOOR = new BlockItem("Iron Door", 330, IRON_DOOR_BLOCK);
	public static final VanillaItemMaterial SADDLE = new VanillaItemMaterial("Saddle", 329);
	public static final VanillaItemMaterial SNOWBALL = new VanillaItemMaterial("Snowball", 332);
	public static final VanillaItemMaterial BOAT = new VanillaItemMaterial("Boat", 333);
	public static final VanillaItemMaterial LEATHER = new VanillaItemMaterial("Leather", 334);
	public static final VanillaItemMaterial CLAY_BRICK = new VanillaItemMaterial("Brick", 336);
	public static final BlockItem SUGAR_CANE = new BlockItem("Sugar Cane", 338, VanillaMaterials.SUGAR_CANE_BLOCK);
	public static final VanillaItemMaterial PAPER = new VanillaItemMaterial("Paper", 339);
	public static final VanillaItemMaterial BOOK = new VanillaItemMaterial("Book", 340);
	public static final VanillaItemMaterial SLIMEBALL = new VanillaItemMaterial("Slimeball", 341);
	public static final VanillaItemMaterial EGG = new VanillaItemMaterial("Egg", 344);
	public static final VanillaItemMaterial COMPASS = new VanillaItemMaterial("Compass", 345);
	public static final VanillaItemMaterial CLOCK = new VanillaItemMaterial("Clock", 347);
	public static final VanillaItemMaterial GLOWSTONE_DUST = new VanillaItemMaterial("Glowstone Dust", 348);
	public static final RawFish RAW_FISH = new RawFish("Raw Fish", 349, 2, FoodEffectType.HUNGER);
	public static final Food COOKED_FISH = new Food("Cooked Fish", 350, 5, FoodEffectType.HUNGER);
	public static final Dye DYE = Dye.INK_SAC;
	public static final VanillaItemMaterial BONE = new VanillaItemMaterial("Bone", 352);
	public static final VanillaItemMaterial SUGAR = new VanillaItemMaterial("Sugar", 353);
	public static final BlockItem CAKE = new BlockItem("Cake", 354, VanillaMaterials.CAKE_BLOCK);
	public static final BlockItem BED = new BlockItem("Bed", 355, VanillaMaterials.BED_BLOCK);
	public static final BlockItem REDSTONE_REPEATER = new BlockItem("Redstone Repeater", 356, VanillaMaterials.REDSTONE_REPEATER_OFF);
	public static final VanillaItemMaterial MAP = new VanillaItemMaterial("Map", 358);
	public static final Shears SHEARS = new Shears("Shears", 359, (short) 238);
	public static final VanillaItemMaterial PUMPKIN_SEEDS = new VanillaItemMaterial("Pumpkin Seeds", 361);
	public static final VanillaItemMaterial MELON_SEEDS = new VanillaItemMaterial("MelonBlock Seeds", 362);
	//== Food ==
	public static final Food RED_APPLE = new Food("Apple", 260, 4, FoodEffectType.HUNGER);
	public static final Food MUSHROOM_SOUP = new Food("Mushroom Soup", 282, 8, FoodEffectType.HUNGER);
	public static final Food BREAD = new Food("Bread", 297, 5, FoodEffectType.HUNGER);
	public static final RawPorkchop RAW_PORKCHOP = new RawPorkchop("Raw Porkchop", 319, 3, FoodEffectType.HUNGER);
	public static final Food COOKED_PORKCHOP = new Food("Cooked Porkchop", 320, 8, FoodEffectType.HUNGER);
	public static final Food GOLDEN_APPLE = new Food("Golden Apple", 322, 10, FoodEffectType.HUNGER);
	public static final Food MELON_SLICE = new Food("MelonBlock Slice", 360, 2, FoodEffectType.HUNGER);
	public static final Food COOKIE = new Food("Cookie", 357, 1, FoodEffectType.HUNGER);
	public static final RawBeef RAW_BEEF = new RawBeef("Raw Beef", 363, 3, FoodEffectType.HUNGER);
	public static final Food STEAK = new Food("Steak", 364, 8, FoodEffectType.HUNGER);
	public static final RawChicken RAW_CHICKEN = new RawChicken("Raw Chicken", 365, 2, FoodEffectType.HUNGER);
	public static final Food COOKED_CHICKEN = new Food("Cooked Chicken", 366, 6, FoodEffectType.HUNGER);
	public static final Food ROTTEN_FLESH = new Food("Rotten Flesh", 367, 4, FoodEffectType.HUNGER);
	//== Music Discs ==
	public static final MusicDisc GOLD_MUSIC_DISC = new MusicDisc("Music Disc - 13", 2256).setMusic(Music.THIRTEEN);
	public static final MusicDisc GREEN_MUSIC_DISC = new MusicDisc("Music Disc - cat", 2257).setMusic(Music.CAT);
	public static final MusicDisc ORANGE_MUSIC_DISC = new MusicDisc("Music Disc - blocks", 2258).setMusic(Music.BLOCKS);
	public static final MusicDisc RED_MUSIC_DISC = new MusicDisc("Music Disc - chirp", 2259).setMusic(Music.CHIRP);
	public static final MusicDisc CYAN_MUSIC_DISC = new MusicDisc("Music Disc - far", 2260).setMusic(Music.FAR);
	public static final MusicDisc BLUE_MUSIC_DISC = new MusicDisc("Music Disc - mall", 2261).setMusic(Music.MALL);
	public static final MusicDisc PURPLE_MUSIC_DISC = new MusicDisc("Music Disc - mellohi", 2262).setMusic(Music.MELLOHI);
	public static final MusicDisc BLACK_MUSIC_DISC = new MusicDisc("Music Disc - stal", 2263).setMusic(Music.STAL);
	public static final MusicDisc WHITE_MUSIC_DISC = new MusicDisc("Music Disc - strad", 2264).setMusic(Music.STRAD);
	public static final MusicDisc FOREST_GREEN_MUSIC_DISC = new MusicDisc("Music Disc - ward", 2265).setMusic(Music.WARD);
	public static final MusicDisc BROKEN_MUSIC_DISC = new MusicDisc("Music Disc - 11", 2266).setMusic(Music.ELEVEN);
	//== Potions and special items ==
	public static final VanillaItemMaterial ENDER_PEARL = new VanillaItemMaterial("Ender Pearl", 368);
	public static final BlazeRod BLAZE_ROD = new BlazeRod("Blaze Rod", 369);
	public static final VanillaItemMaterial GHAST_TEAR = new VanillaItemMaterial("Ghast Tear", 370);
	public static final VanillaItemMaterial GOLD_NUGGET = new VanillaItemMaterial("Gold Nugget", 371);
	public static final VanillaItemMaterial NETHER_WART = new VanillaItemMaterial("Nether Wart", 372);
	public static final VanillaItemMaterial GLASS_BOTTLE = new VanillaItemMaterial("Glass Bottle", 374);
	public static final Food SPIDER_EYE = new Food("Spider Eye", 375, 2, FoodEffectType.HUNGER);
	public static final VanillaItemMaterial FERMENTED_SPIDER_EYE = new VanillaItemMaterial("Fermented Spider Eye", 376);
	public static final VanillaItemMaterial BLAZE_POWDER = new VanillaItemMaterial("Blaze Powder", 377);
	public static final VanillaItemMaterial MAGMA_CREAM = new VanillaItemMaterial("Magma Cream", 378);
	public static final BlockItem BREWING_STAND = new BlockItem("Brewing Stand", 379, VanillaMaterials.BREWING_STAND_BLOCK);
	public static final BlockItem CAULDRON = new BlockItem("Cauldron", 380, VanillaMaterials.CAULDRON_BLOCK);
	public static final VanillaItemMaterial EYE_OF_ENDER = new VanillaItemMaterial("Eye of Ender", 381);
	public static final VanillaItemMaterial GLISTERING_MELON = new VanillaItemMaterial("Glistering MelonBlock", 382);
	public static final SpawnEgg SPAWN_EGG = SpawnEgg.PIG;
	public static final VanillaItemMaterial BOTTLE_O_ENCHANTING = new VanillaItemMaterial("Bottle o' Enchanting", 384);
	public static final BlockItem FIRE_CHARGE = new BlockItem("Fire Charge", 385, VanillaMaterials.FIRE); //Basic Implementation
	public static final Potion POTION = Potion.EMPTY;
	private static boolean initialized = false;
	
	private final static AtomicShortArray conversionTable = new AtomicShortArray(Short.MAX_VALUE);
	private final static TShortObjectHashMap<Material> reverseTable = new TShortObjectHashMap<Material>(500);
	
	static {
		for (Field field : VanillaMaterials.class.getFields()) {
			try {
				if ((field.getModifiers() & (Modifier.STATIC | Modifier.PUBLIC)) > 0) {
					Object temp = field.get(null);
					if (temp instanceof VanillaMaterial) {
						VanillaMaterial material = (VanillaMaterial) temp;
						if (material != null) {
							if (!((Material)material).isSubMaterial()) {
								reverseTable.put((short)material.getMinecraftId(), (Material) material);
							}
						}
					}
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	public static Material getMaterial(short minecraftId) {
		return reverseTable.get(minecraftId);
	}

	/**
	 * Gets the minecraft id associated with the Spout material
	 * 
	 * @param material to convert
	 * @return minecraft id
	 */
	public static short getMinecraftId(Material material) {
		if (material instanceof VanillaMaterial) {
			return (short) ((VanillaMaterial)material).getMinecraftId();
		} else {
			return BlockMaterial.AIR.getId();
		}
	}
	/**
	 * Gets the minecraft id associated with the Spout material id
	 * 
	 * @param id to convert
	 * @return minecraft id
	 */
	public static short getMinecraftId(short id) {
		if (id <= 0) {
			return 0;
		}
		short minecraftId = conversionTable.get(id);
		if (minecraftId == 0) {
			Material mat = MaterialRegistry.get(id);
			if (mat instanceof VanillaMaterial) {
				minecraftId = (short) ((VanillaMaterial)mat).getMinecraftId();
			} else {
				minecraftId = BlockMaterial.AIR.getId();
			}
			conversionTable.set(id, minecraftId);
		}
		return minecraftId;
	}

	public static void initialize() {
		if (initialized) {
			return;
		}
		for (Field field : VanillaMaterials.class.getFields()) {
			try {
				if (field == null || ((field.getModifiers() & (Modifier.STATIC | Modifier.PUBLIC)) != (Modifier.STATIC | Modifier.PUBLIC)) || !VanillaMaterial.class.isAssignableFrom(field.getType())) {
					continue;
				}
				VanillaMaterial material = (VanillaMaterial) field.get(null);
				if (material == null) {
					Spout.getLogger().severe("Vanilla Material field '" + field.getName() + "' is not yet initialized");
					continue;
				}
				try {
					material.initialize();
				} catch (Throwable t) {
					Spout.getLogger().severe("An exception occurred while loading the properties of Vanilla Material '" + field.getName() + "':");
					t.printStackTrace();
				}
			} catch (Throwable t) {
				Spout.getLogger().severe("An exception occurred while reading Vanilla Material field '" + field.getName() + "':");
				t.printStackTrace();
			}
		}
		initialized = true;
	}
}
