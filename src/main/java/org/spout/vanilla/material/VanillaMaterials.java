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

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.spout.api.Spout;
import org.spout.api.material.BlockMaterial;

import org.spout.vanilla.material.block.Ore;
import org.spout.vanilla.material.block.Solid;
import org.spout.vanilla.material.block.door.IronDoorBlock;
import org.spout.vanilla.material.block.door.WoodenDoorBlock;
import org.spout.vanilla.material.block.liquid.Lava;
import org.spout.vanilla.material.block.liquid.Water;
import org.spout.vanilla.material.block.misc.BedBlock;
import org.spout.vanilla.material.block.misc.CakeBlock;
import org.spout.vanilla.material.block.misc.Chest;
import org.spout.vanilla.material.block.misc.EndPortalFrame;
import org.spout.vanilla.material.block.misc.FarmLand;
import org.spout.vanilla.material.block.misc.Fence;
import org.spout.vanilla.material.block.misc.FenceGate;
import org.spout.vanilla.material.block.misc.Fire;
import org.spout.vanilla.material.block.misc.GlassPane;
import org.spout.vanilla.material.block.misc.Ladder;
import org.spout.vanilla.material.block.misc.Lever;
import org.spout.vanilla.material.block.misc.MonsterSpawner;
import org.spout.vanilla.material.block.misc.SignBase;
import org.spout.vanilla.material.block.misc.Slab;
import org.spout.vanilla.material.block.misc.Snow;
import org.spout.vanilla.material.block.misc.StoneButton;
import org.spout.vanilla.material.block.misc.Torch;
import org.spout.vanilla.material.block.misc.TrapDoor;
import org.spout.vanilla.material.block.misc.Web;
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
import org.spout.vanilla.material.block.ore.RedstoneOre;
import org.spout.vanilla.material.block.piston.Piston;
import org.spout.vanilla.material.block.piston.PistonExtension;
import org.spout.vanilla.material.block.piston.PistonExtensionMoving;
import org.spout.vanilla.material.block.plant.Cactus;
import org.spout.vanilla.material.block.plant.DeadBush;
import org.spout.vanilla.material.block.plant.Flower;
import org.spout.vanilla.material.block.plant.LilyPad;
import org.spout.vanilla.material.block.plant.Mushroom;
import org.spout.vanilla.material.block.plant.NetherWartBlock;
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
import org.spout.vanilla.material.block.solid.Dispenser;
import org.spout.vanilla.material.block.solid.DoubleSlab;
import org.spout.vanilla.material.block.solid.EnchantmentTable;
import org.spout.vanilla.material.block.solid.Furnace;
import org.spout.vanilla.material.block.solid.Glass;
import org.spout.vanilla.material.block.solid.Grass;
import org.spout.vanilla.material.block.solid.Gravel;
import org.spout.vanilla.material.block.solid.Ice;
import org.spout.vanilla.material.block.solid.Jukebox;
import org.spout.vanilla.material.block.solid.Leaves;
import org.spout.vanilla.material.block.solid.MossStone;
import org.spout.vanilla.material.block.solid.MushroomBlock;
import org.spout.vanilla.material.block.solid.Mycelium;
import org.spout.vanilla.material.block.solid.NetherBrick;
import org.spout.vanilla.material.block.solid.NetherRack;
import org.spout.vanilla.material.block.solid.NoteBlock;
import org.spout.vanilla.material.block.solid.Obsidian;
import org.spout.vanilla.material.block.solid.Plank;
import org.spout.vanilla.material.block.solid.Pumpkin;
import org.spout.vanilla.material.block.solid.RedstoneLamp;
import org.spout.vanilla.material.block.solid.Sand;
import org.spout.vanilla.material.block.solid.Sandstone;
import org.spout.vanilla.material.block.solid.SnowBlock;
import org.spout.vanilla.material.block.solid.SoulSand;
import org.spout.vanilla.material.block.solid.Stone;
import org.spout.vanilla.material.block.solid.StoneBrick;
import org.spout.vanilla.material.block.solid.TNT;
import org.spout.vanilla.material.block.solid.Log;
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
import org.spout.vanilla.material.item.Tool;
import org.spout.vanilla.material.item.VanillaItemMaterial;
import org.spout.vanilla.material.item.armor.Chestwear;
import org.spout.vanilla.material.item.armor.Footwear;
import org.spout.vanilla.material.item.armor.Headwear;
import org.spout.vanilla.material.item.armor.Legwear;
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
import org.spout.vanilla.material.item.weapon.Bow;
import org.spout.vanilla.material.item.weapon.Sword;
import org.spout.vanilla.util.Music;

import static org.spout.api.material.MaterialRegistry.register;

// TODO: Remove all casts and separate remaining "set" methods out into each material's init() method
public final class VanillaMaterials {
	public static final BlockMaterial AIR = BlockMaterial.AIR;
	public static final Stone STONE = register(new Stone("Stone", 1));
	public static final Grass GRASS = register(new Grass("Grass", 2));
	public static final Dirt DIRT = register(new Dirt("Dirt", 3));
	public static final Cobblestone COBBLESTONE = register(new Cobblestone("Cobblestone", 4));
	public static final Plank PLANK = Plank.PLANK;
	public static final Bedrock BEDROCK = register(new Bedrock("Bedrock", 7));
	public static final Sand SAND = register(new Sand("Sand", 12));
	public static final Gravel GRAVEL = register(new Gravel("Gravel", 13));
	public static final Log LOG = Log.DEFAULT;
	public static final Leaves LEAVES = Leaves.DEFAULT;
	public static final Solid SPONGE = (Solid) register(new Solid("Sponge", 19).setHardness(0.6F).setResistance(1.0F));
	public static final Glass GLASS = register(new Glass("Glass", 20));
	public static final Dispenser DISPENSER = register(new Dispenser("Dispenser", 23));
	public static final Sandstone SANDSTONE = Sandstone.SANDSTONE;
	public static final NoteBlock NOTEBLOCK = register(new NoteBlock("Note Block", 25));
	public static final BedBlock BED_BLOCK = register(new BedBlock("Bed", 26));
	public static final Web WEB = register(new Web("Cobweb", 30));
	//== Piston ==
	public static final Piston PISTON_STICKY_BASE = register(new Piston("Sticky Piston", 29, true));
	public static final Piston PISTON_BASE = register(new Piston("Piston", 33, false));
	public static final PistonExtension PISTON_EXTENSION = register(new PistonExtension("Piston (Head)", 34));
	public static final PistonExtensionMoving PISTON_MOVING = register(new PistonExtensionMoving("Moved By Piston", 36));
	//== Rails ==
	public static final Rail RAIL = register(new Rail("Rail", 66));
	public static final PoweredRail RAIL_POWERED = register(new PoweredRail("Powered Rail", 27));
	public static final DetectorRail RAIL_DETECTOR = register(new DetectorRail("Detector Rail", 28));
	//== Liquids ==
	public static final Water WATER = register(new Water("Water", 8, true));
	public static final Water STATIONARY_WATER = register(new Water("Stationary Water", 9, false));
	public static final Lava LAVA = register(new Lava("Lava", 10, true));
	public static final Lava STATIONARY_LAVA = register(new Lava("Stationary Lava", 11, false));
	//== Ores ==
	public static final CoalOre COAL_ORE = register(new CoalOre("Coal Ore", 16));
	public static final IronOre IRON_ORE = register(new IronOre("Iron Ore", 15));
	public static final GoldOre GOLD_ORE = register(new GoldOre("Gold Ore", 14));
	public static final DiamondOre DIAMOND_ORE = register(new DiamondOre("Diamond Ore", 56));
	public static final LapisLazuliOre LAPIS_LAZULI_ORE = register(new LapisLazuliOre("Lapis Lazuli Ore", 21));
	public static final RedstoneOre REDSTONE_ORE = register(new RedstoneOre("Redstone Ore", 73, false));
	public static final RedstoneOre GLOWING_REDSTONE_ORE = register(new RedstoneOre("Glowing Redstone Ore", 74, true));
	//== Solid blocks ==
	public static final GoldBlock GOLD_BLOCK = register(new GoldBlock("Gold Block", 41));
	public static final IronBlock IRON_BLOCK = register(new IronBlock("Iron Block", 42));
	public static final DiamondBlock DIAMOND_BLOCK = register(new DiamondBlock("Diamond Block", 57));
	public static final LapisLazuliBlock LAPIS_LAZULI_BLOCK = register(new LapisLazuliBlock("Lapis Lazuli Block", 22));
	//== Plants ==
	public static final TallGrass TALL_GRASS = TallGrass.TALL_GRASS;
	public static final DeadBush DEAD_BUSH = register(new DeadBush("Dead Shrubs", 32));
	public static final Sapling SAPLING = Sapling.DEFAULT;
	public static final Flower DANDELION = register(new Flower("Dandelion", 37));
	public static final Flower ROSE = register(new Flower("Rose", 38));
	public static final Mushroom BROWN_MUSHROOM = (Mushroom) register(new Mushroom("Brown Mushroom", 39).setLightLevel(1));
	public static final Mushroom RED_MUSHROOM = register(new Mushroom("Red Mushroom", 40));
	//== Stairs ==
	public static final NetherBrickStairs STAIRS_NETHER_BRICK = register(new NetherBrickStairs("Nether Brick Stairs", 114));
	public static final BrickStairs STAIRS_BRICK = register(new BrickStairs("Brick Stairs", 108));
	public static final CobblestoneStairs STAIRS_COBBLESTONE = register(new CobblestoneStairs("Cobblestone Stairs", 67));
	public static final WoodenStairs STAIRS_WOODEN = register(new WoodenStairs("Wooden Stairs", 53));
	public static final StoneBrickStairs STAIRS_STONE_BRICK = register(new StoneBrickStairs("Stone Brick Stairs", 109));
	//== Portals ==
	public static final NetherPortal PORTAL = register(new NetherPortal("Portal", 90));
	public static final EndPortal END_PORTAL = register(new EndPortal("End Portal", 119));
	public static final EndPortalFrame END_PORTAL_FRAME = register(new EndPortalFrame("End Portal Frame", 120));
	//================
	public static final DoubleSlab DOUBLE_SLABS = DoubleSlab.STONE;
	public static final Slab SLAB = Slab.STONE;
	public static final Wool WOOL = Wool.WHITE;
	public static final Brick BRICK = register(new Brick("Brick Block", 45));
	public static final TNT TNT = register(new TNT("TNT", 46));
	public static final BookShelf BOOKSHELF = register(new BookShelf("Bookshelf", 47));
	public static final MossStone MOSS_STONE = register(new MossStone("Moss Stone", 48));
	public static final Obsidian OBSIDIAN = register(new Obsidian("Obsidian", 49));
	public static final Torch TORCH = register(new Torch("Torch", 50));
	public static final Fire FIRE = register(new Fire("Fire", 51));
	public static final MonsterSpawner MONSTER_SPAWNER = register(new MonsterSpawner("Monster Spawner", 52));
	public static final Chest CHEST = register(new Chest("Chest", 54));
	public static final RedstoneWire REDSTONE_WIRE = register(new RedstoneWire("Redstone Wire", 55));
	public static final CraftingTable CRAFTING_TABLE = register(new CraftingTable("Crafting Table", 58));
	public static final WheatCrop WHEATCROP = register(new WheatCrop("Wheat Crop", 59));
	public static final FarmLand FARMLAND = register(new FarmLand("Farmland", 60));
	public static final Furnace FURNACE = register(new Furnace("Furnace", 61, false));
	public static final Furnace FURNACE_BURNING = register(new Furnace("Burning Furnace", 62, true));
	public static final SignBase SIGN_POST = register(new SignBase("Sign Post", 63));
	public static final Ladder LADDER = register(new Ladder("Ladder", 65));
	public static final SignBase WALL_SIGN = register(new SignBase("Wall Sign", 68));
	public static final Lever LEVER = register(new Lever("Lever", 69));
	public static final StonePressurePlate STONE_PRESSURE_PLATE = register(new StonePressurePlate("Stone Pressure Plate", 70));
	public static final IronDoorBlock IRON_DOOR_BLOCK = register(new IronDoorBlock("Iron Door", 71));
	public static final WoodenDoorBlock WOODEN_DOOR_BLOCK = register(new WoodenDoorBlock("Wooden Door", 64));
	public static final WoodenPressurePlate WOODEN_PRESSURE_PLATE = register(new WoodenPressurePlate("Wooden Pressure Plate", 72));
	public static final RedstoneTorch REDSTONE_TORCH_OFF = register(new RedstoneTorch("Redstone Torch", 75, false));
	public static final RedstoneTorch REDSTONE_TORCH_ON = register(new RedstoneTorch("Redstone Torch (On)", 76, true));
	public static final StoneButton STONE_BUTTON = register(new StoneButton("Stone Button", 77));
	public static final Snow SNOW = register(new Snow("Snow", 78));
	public static final Ice ICE = register(new Ice("Ice", 79));
	public static final SnowBlock SNOW_BLOCK = register(new SnowBlock("Snow Block", 80));
	public static final Cactus CACTUS = register(new Cactus("Cactus", 81));
	public static final ClayBlock CLAY_BLOCK = register(new ClayBlock("Clay Block", 82));
	public static final SugarCaneBlock SUGAR_CANE_BLOCK = register(new SugarCaneBlock("Sugar Cane", 83));
	public static final Jukebox JUKEBOX = register(new Jukebox("Jukebox", 84));
	public static final Fence FENCE = register(new Fence("Fence", 85));
	public static final Pumpkin PUMPKIN = register(new Pumpkin("Pumpkin", 86, false));
	public static final NetherRack NETHERRACK = register(new NetherRack("Netherrack", 87));
	public static final SoulSand SOUL_SAND = register(new SoulSand("Soul Sand", 88));
	public static final Glowstone GLOWSTONE_BLOCK = register(new Glowstone("Glowstone Block", 89));
	public static final Pumpkin JACK_O_LANTERN = register(new Pumpkin("Jack 'o' Lantern", 91, true));
	public static final Solid END_STONE = (Solid) register(new Solid("End Stone", 121).setHardness(3.0F).setResistance(15.0F));
	public static final CakeBlock CAKE_BLOCK = register(new CakeBlock("Cake Block", 92));
	public static final RedstoneRepeater REDSTONE_REPEATER_OFF = register(new RedstoneRepeater("Redstone Repeater", 93, false));
	public static final RedstoneRepeater REDSTONE_REPEATER_ON = register(new RedstoneRepeater("Redstone Repeater (On)", 94, true));
	public static final Solid LOCKED_CHEST = (Solid) register(new Solid("Locked Chest", 95).setHardness(0.0F).setResistance(0.0F).setLightLevel(15));
	public static final TrapDoor TRAPDOOR = register(new TrapDoor("Trapdoor", 96));
	public static final Solid SILVERFISH_STONE = (Solid) register(new Solid("Silverfish Stone", 97).setHardness(0.75F).setResistance(10.0F)); //Placeholder, block resistance unknown
	public static final StoneBrick STONE_BRICK = StoneBrick.STONE;
	public static final MushroomBlock HUGE_BROWN_MUSHROOM = register(new MushroomBlock("Huge Brown Mushroom", 99));
	public static final MushroomBlock HUGE_RED_MUSHROOM = register(new MushroomBlock("Huge Red Mushroom", 100));
	public static final Ore MELON = (Ore) register(new Ore("Melon", 103).setMinDropCount(3).setMaxDropCount(7).setHardness(1.0F).setResistance(1.7F));
	public static final Solid IRON_BARS = (Solid) register(new Solid("Iron Bars", 101).setHardness(5.0F).setResistance(10.0F));
	public static final GlassPane GLASS_PANE = register(new GlassPane("Glass Pane", 102));
	public static final Solid PUMPKIN_STEM = (Solid) register(new Solid("Pumpkin Stem", 104).setHardness(0.0F).setResistance(0.0F));
	public static final Solid MELON_STEM = (Solid) register(new Solid("Melon Stem", 105).setHardness(0.0F).setResistance(0.3F));
	public static final Vines VINES = register(new Vines("Vines", 106));
	public static final FenceGate FENCE_GATE = register(new FenceGate("Fence Gate", 107));
	public static final Mycelium MYCELIUM = register(new Mycelium("Mycelium", 110));
	public static final LilyPad LILY_PAD = register(new LilyPad("Lily Pad", 111));
	public static final NetherBrick NETHER_BRICK = register(new NetherBrick("Nether Brick", 112));
	public static final Solid NETHER_BRICK_FENCE = (Solid) register(new Solid("Nether Brick Fence", 113).setHardness(2.0F).setResistance(10.0F));
	public static final NetherWartBlock NETHER_WART_BLOCK = register(new NetherWartBlock("Nether Wart", 115));
	public static final EnchantmentTable ENCHANTMENT_TABLE = register(new EnchantmentTable("Enchantment Table", 116));
	public static final Solid BREWING_STAND_BLOCK = (Solid) register(new Solid("Brewing Stand", 117).setHardness(0.5F).setResistance(0.8F).setLightLevel(1));
	public static final Solid CAULDRON_BLOCK = (Solid) register(new Solid("Cauldron", 118).setHardness(2.0F).setResistance(3.3F));
	public static final Solid DRAGON_EGG = (Solid) register(new Solid("Dragon Egg", 122).setHardness(3.0F).setResistance(15.0F).setLightLevel(1));
	public static final RedstoneLamp REDSTONE_LAMP_OFF = register(new RedstoneLamp("Redstone Lamp", 123, false));
	public static final RedstoneLamp REDSTONE_LAMP_ON = register(new RedstoneLamp("Redstone Lamp (On)", 124, true));
	/*
	 * Items
	 */
	//== Swords ==
	public static final Sword WOODEN_SWORD = (Sword) register(new Sword("Wooden Sword", 268, (short) 60).setDamage(4));
	public static final Sword GOLD_SWORD = (Sword) register(new Sword("Gold Sword", 283, (short) 33).setDamage(4));
	public static final Sword STONE_SWORD = (Sword) register(new Sword("Stone Sword", 272, (short) 132).setDamage(5));
	public static final Sword IRON_SWORD = (Sword) register(new Sword("Iron Sword", 267, (short) 251).setDamage(6));
	public static final Sword DIAMOND_SWORD = (Sword) register(new Sword("Diamond Sword", 276, (short) 1562).setDamage(7));
	//== Spades ==
	public static final Spade GOLD_SPADE = register(new Spade("Gold Spade", 284, (short) 33));
	public static final Spade WOODEN_SPADE = register(new Spade("Wooden Spade", 269, (short) 60));
	public static final Spade STONE_SPADE = register(new Spade("Stone Spade", 273, (short) 132));
	public static final Spade IRON_SPADE = register(new Spade("Iron Spade", 256, (short) 251));
	public static final Spade DIAMOND_SPADE = register(new Spade("Diamond Spade", 277, (short) 1562));
	//== Pickaxes ==
	public static final Pickaxe GOLD_PICKAXE = (Pickaxe) register(new Pickaxe("Gold Pickaxe", 285, (short) 33).setDamage(2));
	public static final Pickaxe WOODEN_PICKAXE = (Pickaxe) register(new Pickaxe("Wooden Pickaxe", 270, (short) 60).setDamage(2));
	public static final Pickaxe STONE_PICKAXE = (Pickaxe) register(new Pickaxe("Stone Pickaxe", 274, (short) 132).setDamage(3));
	public static final Pickaxe IRON_PICKAXE = (Pickaxe) register(new Pickaxe("Iron Pickaxe", 257, (short) 251).setDamage(4));
	public static final Pickaxe DIAMOND_PICKAXE = (Pickaxe) register(new Pickaxe("Diamond Pickaxe", 278, (short) 1562).setDamage(5));
	//== Axes ==
	public static final Axe GOLD_AXE = (Axe) register(new Axe("Gold Axe", 286, (short) 33).setDamage(3));
	public static final Axe WOODEN_AXE = (Axe) register(new Axe("Wooden Axe", 271, (short) 60).setDamage(3));
	public static final Axe STONE_AXE = (Axe) register(new Axe("Stone Axe", 275, (short) 132).setDamage(3));
	public static final Axe IRON_AXE = (Axe) register(new Axe("Iron Axe", 258, (short) 251).setDamage(5));
	public static final Axe DIAMOND_AXE = (Axe) register(new Axe("Diamond Axe", 279, (short) 1562).setDamage(6));
	//== Hoes ==
	public static final Hoe GOLD_HOE = register(new Hoe("Gold Hoe", 294, (short) 33));
	public static final Hoe WOODEN_HOE = register(new Hoe("Wooden Hoe", 290, (short) 60));
	public static final Hoe STONE_HOE = register(new Hoe("Stone Hoe", 291, (short) 132));
	public static final Hoe IRON_HOE = register(new Hoe("Iron Hoe", 292, (short) 251));
	public static final Hoe DIAMOND_HOE = register(new Hoe("Diamond Hoe", 293, (short) 1562));
	//== Headwear ==
	public static final Headwear LEATHER_CAP = register(new Headwear("Leather Cap", 298, 1));
	public static final Headwear CHAIN_HELMET = register(new Headwear("Chain Helmet", 302, 2));
	public static final Headwear IRON_HELMET = register(new Headwear("Iron Helmet", 306, 2));
	public static final Headwear GOLD_HELMET = register(new Headwear("Gold Helmet", 314, 2));
	public static final Headwear DIAMOND_HELMET = register(new Headwear("Diamond Helmet", 310, 3));
	//== Chestwear ==
	public static final Chestwear LEATHER_TUNIC = register(new Chestwear("Leather Tunic", 299, 3));
	public static final Chestwear CHAIN_CHESTPLATE = register(new Chestwear("Chain Chestplate", 303, 5));
	public static final Chestwear IRON_CHESTPLATE = register(new Chestwear("Iron Chestplate", 307, 6));
	public static final Chestwear DIAMOND_CHESTPLATE = register(new Chestwear("Diamond Chestplate", 311, 8));
	public static final Chestwear GOLD_CHESTPLATE = register(new Chestwear("Gold Chestplate", 315, 5));
	//== Legwear ==
	public static final Legwear LEATHER_PANTS = register(new Legwear("Leather Pants", 300, 2));
	public static final Legwear CHAIN_LEGGINGS = register(new Legwear("Chain Leggings", 304, 4));
	public static final Legwear IRON_LEGGINGS = register(new Legwear("Iron Leggings", 308, 5));
	public static final Legwear DIAMOND_LEGGINGS = register(new Legwear("Diamond Leggings", 312, 6));
	public static final Legwear GOLD_LEGGINGS = register(new Legwear("Gold Leggings", 316, 3));
	//== Footwear ==
	public static final Footwear LEATHER_BOOTS = register(new Footwear("Leather Boots", 301, 1));
	public static final Footwear CHAIN_BOOTS = register(new Footwear("Chain Boots", 305, 1));
	public static final Footwear IRON_BOOTS = register(new Footwear("Iron Boots", 309, 2));
	public static final Footwear DIAMOND_BOOTS = register(new Footwear("Diamond Boots", 313, 3));
	public static final Footwear GOLD_BOOTS = register(new Footwear("Gold Boots", 317, 1));
	//== Other tool, weapon and equipment ==
	public static final FlintAndSteel FLINT_AND_STEEL = register(new FlintAndSteel("Flint and Steel", 259, (short) 64));
	public static final Bow BOW = (Bow) register(new Bow("Bow", 261, (short) 385)).setRangedDamage(9);
	public static final VanillaItemMaterial ARROW = register(new VanillaItemMaterial("Arrow", 262));
	public static final Tool FISHING_ROD = register(new Tool("Fishing Rod", 346, (short) 65));
	//== Buckets=
	public static final EmptyContainer BUCKET = register(new EmptyContainer("Bucket", 325));
	public static final FullContainer WATER_BUCKET = register(new FullContainer("Water Bucket", 326, WATER, BUCKET));
	public static final LavaBucket LAVA_BUCKET = register(new LavaBucket("Lava Bucket", 327, LAVA, BUCKET));
	public static final VanillaItemMaterial MILK_BUCKET = register(new VanillaItemMaterial("Milk", 335));
	//== Minecarts ==
	public static final MinecartItem MINECART = register(new MinecartItem("Minecart", 328));
	public static final StorageMinecartItem MINECART_CHEST = register(new StorageMinecartItem("Minecart with Chest", 342));
	public static final PoweredMinecartItem MINECART_FURNACE = register(new PoweredMinecartItem("Minecart with Furnace", 343));
	//== Others ==
	public static final Coal COAL = Coal.COAL;
	public static final Clay CLAY = register(new Clay("Clay", 337));
	public static final BlockItem REDSTONE_DUST = register(new BlockItem("Redstone", 331, VanillaMaterials.REDSTONE_WIRE));
	public static final VanillaItemMaterial DIAMOND = register(new VanillaItemMaterial("Diamond", 264));
	public static final VanillaItemMaterial IRON_INGOT = register(new VanillaItemMaterial("Iron Ingot", 265));
	public static final VanillaItemMaterial GOLD_INGOT = register(new VanillaItemMaterial("Gold Ingot", 266));
	public static final Stick STICK = register(new Stick("Stick", 280));
	public static final VanillaItemMaterial BOWL = register(new VanillaItemMaterial("Bowl", 281));
	public static final VanillaItemMaterial STRING = register(new VanillaItemMaterial("String", 287));
	public static final VanillaItemMaterial FEATHER = register(new VanillaItemMaterial("Feather", 288));
	public static final VanillaItemMaterial GUNPOWDER = register(new VanillaItemMaterial("Gunpowder", 289));
	public static final VanillaItemMaterial WHEAT = register(new VanillaItemMaterial("Wheat", 296));
	public static final VanillaItemMaterial FLINT = register(new VanillaItemMaterial("Flint", 318));
	public static final PaintingItem PAINTING = register(new PaintingItem("Painting", 321));
	public static final Sign SIGN = register(new Sign("Sign", 323));
	public static final BlockItem SEEDS = register(new BlockItem("Seeds", 295, WHEATCROP));
	public static final BlockItem WOODEN_DOOR = register(new BlockItem("Wooden Door", 324, WOODEN_DOOR_BLOCK));
	public static final BlockItem IRON_DOOR = register(new BlockItem("Iron Door", 330, IRON_DOOR_BLOCK));
	public static final VanillaItemMaterial SADDLE = register(new VanillaItemMaterial("Saddle", 329));
	public static final VanillaItemMaterial SNOWBALL = register(new VanillaItemMaterial("Snowball", 332));
	public static final VanillaItemMaterial BOAT = register(new VanillaItemMaterial("Boat", 333));
	public static final VanillaItemMaterial LEATHER = register(new VanillaItemMaterial("Leather", 334));
	public static final VanillaItemMaterial CLAY_BRICK = register(new VanillaItemMaterial("Brick", 336));
	public static final BlockItem SUGAR_CANE = register(new BlockItem("Sugar Cane", 338, VanillaMaterials.SUGAR_CANE_BLOCK));
	public static final VanillaItemMaterial PAPER = register(new VanillaItemMaterial("Paper", 339));
	public static final VanillaItemMaterial BOOK = register(new VanillaItemMaterial("Book", 340));
	public static final VanillaItemMaterial SLIMEBALL = register(new VanillaItemMaterial("Slimeball", 341));
	public static final VanillaItemMaterial EGG = register(new VanillaItemMaterial("Egg", 344));
	public static final VanillaItemMaterial COMPASS = register(new VanillaItemMaterial("Compass", 345));
	public static final VanillaItemMaterial CLOCK = register(new VanillaItemMaterial("Clock", 347));
	public static final VanillaItemMaterial GLOWSTONE_DUST = register(new VanillaItemMaterial("Glowstone Dust", 348));
	public static final RawFish RAW_FISH = register(new RawFish("Raw Fish", 349, 2, FoodEffectType.HUNGER));
	public static final Food COOKED_FISH = register(new Food("Cooked Fish", 350, 5, FoodEffectType.HUNGER));
	public static final Dye DYE = Dye.INK_SAC;
	public static final VanillaItemMaterial BONE = register(new VanillaItemMaterial("Bone", 352));
	public static final VanillaItemMaterial SUGAR = register(new VanillaItemMaterial("Sugar", 353));
	public static final BlockItem CAKE = register(new BlockItem("Cake", 354, VanillaMaterials.CAKE_BLOCK));
	public static final BlockItem BED = register(new BlockItem("Bed", 355, VanillaMaterials.BED_BLOCK));
	public static final BlockItem REDSTONE_REPEATER = register(new BlockItem("Redstone Repeater", 356, VanillaMaterials.REDSTONE_REPEATER_OFF));
	public static final VanillaItemMaterial MAP = register(new VanillaItemMaterial("Map", 358));
	public static final Shears SHEARS = register(new Shears("Shears", 359, (short) 238));
	public static final VanillaItemMaterial PUMPKIN_SEEDS = register(new VanillaItemMaterial("Pumpkin Seeds", 361));
	public static final VanillaItemMaterial MELON_SEEDS = register(new VanillaItemMaterial("Melon Seeds", 362));
	//== Food ==
	public static final Food RED_APPLE = register(new Food("Apple", 260, 4, FoodEffectType.HUNGER));
	public static final Food MUSHROOM_SOUP = register(new Food("Mushroom Soup", 282, 8, FoodEffectType.HUNGER));
	public static final Food BREAD = register(new Food("Bread", 297, 5, FoodEffectType.HUNGER));
	public static final RawPorkchop RAW_PORKCHOP = register(new RawPorkchop("Raw Porkchop", 319, 3, FoodEffectType.HUNGER));
	public static final Food COOKED_PORKCHOP = register(new Food("Cooked Porkchop", 320, 8, FoodEffectType.HUNGER));
	public static final Food GOLDEN_APPLE = register(new Food("Golden Apple", 322, 10, FoodEffectType.HUNGER));
	public static final Food MELON_SLICE = register(new Food("Melon Slice", 360, 2, FoodEffectType.HUNGER));
	public static final Food COOKIE = register(new Food("Cookie", 357, 1, FoodEffectType.HUNGER));
	public static final RawBeef RAW_BEEF = register(new RawBeef("Raw Beef", 363, 3, FoodEffectType.HUNGER));
	public static final Food STEAK = register(new Food("Steak", 364, 8, FoodEffectType.HUNGER));
	public static final RawChicken RAW_CHICKEN = register(new RawChicken("Raw Chicken", 365, 2, FoodEffectType.HUNGER));
	public static final Food COOKED_CHICKEN = register(new Food("Cooked Chicken", 366, 6, FoodEffectType.HUNGER));
	public static final Food ROTTEN_FLESH = register(new Food("Rotten Flesh", 367, 4, FoodEffectType.HUNGER));
	//== Music Discs ==
	public static final MusicDisc GOLD_MUSIC_DISC = register(new MusicDisc("Music Disc", 2256).setMusic(Music.THIRTEEN));
	public static final MusicDisc GREEN_MUSIC_DISC = register(new MusicDisc("Music Disc", 2257).setMusic(Music.CAT));
	public static final MusicDisc ORANGE_MUSIC_DISC = register(new MusicDisc("Music Disc", 2258).setMusic(Music.BLOCKS));
	public static final MusicDisc RED_MUSIC_DISC = register(new MusicDisc("Music Disc", 2259).setMusic(Music.CHIRP));
	public static final MusicDisc CYAN_MUSIC_DISC = register(new MusicDisc("Music Disc", 2260).setMusic(Music.FAR));
	public static final MusicDisc BLUE_MUSIC_DISC = register(new MusicDisc("Music Disc", 2261).setMusic(Music.MALL));
	public static final MusicDisc PURPLE_MUSIC_DISC = register(new MusicDisc("Music Disc", 2262).setMusic(Music.MELLOHI));
	public static final MusicDisc BLACK_MUSIC_DISC = register(new MusicDisc("Music Disc", 2263).setMusic(Music.STAL));
	public static final MusicDisc WHITE_MUSIC_DISC = register(new MusicDisc("Music Disc", 2264).setMusic(Music.STRAD));
	public static final MusicDisc FOREST_GREEN_MUSIC_DISC = register(new MusicDisc("Music Disc", 2265).setMusic(Music.WARD));
	public static final MusicDisc BROKEN_MUSIC_DISC = register(new MusicDisc("Music Disc", 2266).setMusic(Music.ELEVEN));
	//== Potions and special items ==
	public static final VanillaItemMaterial ENDER_PEARL = register(new VanillaItemMaterial("Ender Pearl", 368));
	public static final BlazeRod BLAZE_ROD = register(new BlazeRod("Blaze Rod", 369));
	public static final VanillaItemMaterial GHAST_TEAR = register(new VanillaItemMaterial("Ghast Tear", 370));
	public static final VanillaItemMaterial GOLD_NUGGET = register(new VanillaItemMaterial("Gold Nugget", 371));
	public static final VanillaItemMaterial NETHER_WART = register(new VanillaItemMaterial("Nether Wart", 372));
	public static final VanillaItemMaterial GLASS_BOTTLE = register(new VanillaItemMaterial("Glass Bottle", 374));
	public static final Food SPIDER_EYE = register(new Food("Spider Eye", 375, 2, FoodEffectType.HUNGER));
	public static final VanillaItemMaterial FERMENTED_SPIDER_EYE = register(new VanillaItemMaterial("Fermented Spider Eye", 376));
	public static final VanillaItemMaterial BLAZE_POWDER = register(new VanillaItemMaterial("Blaze Powder", 377));
	public static final VanillaItemMaterial MAGMA_CREAM = register(new VanillaItemMaterial("Magma Cream", 378));
	public static final BlockItem BREWING_STAND = register(new BlockItem("Brewing Stand", 379, VanillaMaterials.BREWING_STAND_BLOCK));
	public static final BlockItem CAULDRON = register(new BlockItem("Cauldron", 380, VanillaMaterials.CAULDRON_BLOCK));
	public static final VanillaItemMaterial EYE_OF_ENDER = register(new VanillaItemMaterial("Eye of Ender", 381));
	public static final VanillaItemMaterial GLISTERING_MELON = register(new VanillaItemMaterial("Glistering Melon", 382));
	public static final SpawnEgg SPAWN_EGG = SpawnEgg.PIG;
	public static final VanillaItemMaterial BOTTLE_O_ENCHANTING = register(new VanillaItemMaterial("Bottle o' Enchanting", 384));
	public static final BlockItem FIRE_CHARGE = register(new BlockItem("Fire Charge", 385, VanillaMaterials.FIRE)); //Basic Implementation
	public static final Potion POTION = Potion.EMPTY;
	private static boolean initialized = false;

	public static void initialize() {
		if (initialized) {
			return;
		}
		for (Field field : VanillaMaterials.class.getFields()) {
			try {
				if (field == null || ((field.getModifiers()&(Modifier.STATIC|Modifier.PUBLIC))!=(Modifier.STATIC|Modifier.PUBLIC)) || !VanillaMaterial.class.isAssignableFrom(field.getType())) {
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

		// TODO: Give each own class
		VanillaMaterials.PORTAL.setDrop(VanillaMaterials.AIR);
		VanillaMaterials.END_PORTAL.setDrop(VanillaMaterials.AIR);
		VanillaMaterials.END_PORTAL_FRAME.setDrop(VanillaMaterials.AIR);
		VanillaMaterials.SILVERFISH_STONE.setDrop(VanillaMaterials.STONE); // TODO: Get drop item based on data
		VanillaMaterials.PUMPKIN_STEM.setDrop(VanillaMaterials.AIR);
		VanillaMaterials.MELON.setDrop(VanillaMaterials.MELON_SLICE);
		VanillaMaterials.MELON_STEM.setDrop(VanillaMaterials.MELON_SEEDS);
		initialized = true;
	}
}
