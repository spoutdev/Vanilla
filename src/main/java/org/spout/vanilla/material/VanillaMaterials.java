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
package org.spout.vanilla.material;

import static org.spout.api.material.MaterialRegistry.register;

import org.spout.api.material.BlockMaterial;

import org.spout.vanilla.material.Food.FoodEffectType;
import org.spout.vanilla.material.attachable.GroundAttachable;
import org.spout.vanilla.material.attachable.WallAttachable;
import org.spout.vanilla.material.block.Cactus;
import org.spout.vanilla.material.block.DoubleSlab;
import org.spout.vanilla.material.block.Fire;
import org.spout.vanilla.material.block.Grass;
import org.spout.vanilla.material.block.Gravel;
import org.spout.vanilla.material.block.Ice;
import org.spout.vanilla.material.block.Leaves;
import org.spout.vanilla.material.block.LongGrass;
import org.spout.vanilla.material.block.MinecartTrack;
import org.spout.vanilla.material.block.MinecartTrackDetector;
import org.spout.vanilla.material.block.MinecartTrackPowered;
import org.spout.vanilla.material.block.Ore;
import org.spout.vanilla.material.block.Plank;
import org.spout.vanilla.material.block.Sandstone;
import org.spout.vanilla.material.block.Sapling;
import org.spout.vanilla.material.block.SignBase;
import org.spout.vanilla.material.block.Slab;
import org.spout.vanilla.material.block.Snow;
import org.spout.vanilla.material.block.Solid;
import org.spout.vanilla.material.block.StoneBrick;
import org.spout.vanilla.material.block.TallGrass;
import org.spout.vanilla.material.block.Tree;
import org.spout.vanilla.material.block.WheatCrop;
import org.spout.vanilla.material.block.Wool;
import org.spout.vanilla.material.generic.GenericArmor;
import org.spout.vanilla.material.generic.GenericBlockItem;
import org.spout.vanilla.material.generic.GenericEmptyContainer;
import org.spout.vanilla.material.generic.GenericFood;
import org.spout.vanilla.material.generic.GenericFullContainer;
import org.spout.vanilla.material.generic.GenericItem;
import org.spout.vanilla.material.generic.GenericLiquid;
import org.spout.vanilla.material.generic.GenericRangedWeapon;
import org.spout.vanilla.material.generic.GenericTool;
import org.spout.vanilla.material.generic.GenericWeapon;
import org.spout.vanilla.material.item.Coal;
import org.spout.vanilla.material.item.DoorBlock;
import org.spout.vanilla.material.item.DoorItem;
import org.spout.vanilla.material.item.Dye;
import org.spout.vanilla.material.item.Minecart;
import org.spout.vanilla.material.item.Potion;
import org.spout.vanilla.material.item.PoweredMinecart;
import org.spout.vanilla.material.item.RedstoneTorch;
import org.spout.vanilla.material.item.RedstoneWire;
import org.spout.vanilla.material.item.Shears;
import org.spout.vanilla.material.item.SpawnEgg;
import org.spout.vanilla.material.item.StorageMinecart;

public final class VanillaMaterials {
	public static final BlockMaterial AIR = BlockMaterial.AIR;
	public static final Solid STONE = (Solid) register(new Solid("Stone", 1).setHardness(1.5F).setResistance(10.0F));
	public static final Grass GRASS = (Grass) register(new Grass("Grass").setHardness(0.6F).setResistance(0.8F));
	public static final Solid DIRT = (Solid) register(new Solid("Dirt", 3).setHardness(0.5F).setResistance(0.8F));
	public static final Solid COBBLESTONE = (Solid) register(new Solid("Cobblestone", 4).setHardness(2.0F).setResistance(10.0F));
	public static final Plank PLANK = Plank.PLANK;
	public static final Sapling SAPLING = Sapling.DEFAULT;
	public static final Solid BEDROCK = (Solid) register(new Solid("Bedrock", 7).setResistance(6000000.0F));
	public static final GenericLiquid WATER = (GenericLiquid) register(new GenericLiquid("Water", 8, true).setHardness(100.0F).setResistance(166.7F).setOpacity((byte) 2));
	public static final GenericLiquid STATIONARY_WATER = (GenericLiquid) register(new GenericLiquid("Stationary Water", 9, false).setHardness(100.0F).setResistance(166.7F).setOpacity((byte) 2));
	public static final GenericLiquid LAVA = (GenericLiquid) register(new GenericLiquid("Lava", 10, true).setHardness(0.0F).setLightLevel(15).setResistance(0.0F));
	public static final GenericLiquid STATIONARY_LAVA = (GenericLiquid) register(new GenericLiquid("Stationary Lava", 11, false).setHardness(100.0F).setLightLevel(15).setResistance(166.7F));
	public static final Solid SAND = (Solid) register(new Solid("Sand", 12, true).setHardness(0.5F).setResistance(0.8F));
	public static final Gravel GRAVEL = (Gravel) register(new Gravel("Gravel").setHardness(0.6F).setResistance(1.0F));
	public static final Ore GOLD_ORE = (Ore) register(new Ore("Gold Ore", 14).setHardness(3.0F).setResistance(5.0F));
	public static final Ore IRON_ORE = (Ore) register(new Ore("Iron Ore", 15).setHardness(3.0F).setResistance(5.0F));
	public static final Ore COAL_ORE = (Ore) register(new Ore("Coal Ore", 16).setHardness(3.0F).setResistance(5.0F));
	public static final Tree LOG = Tree.DEFAULT;
	public static final Leaves LEAVES = Leaves.DEFAULT;
	public static final Solid SPONGE = (Solid) register(new Solid("Sponge", 19).setHardness(0.6F).setResistance(1.0F));
	public static final Solid GLASS = (Solid) register(new Solid("Glass", 20).setHardness(0.3F).setResistance(0.5F));
	public static final Ore LAPIS_ORE = (Ore) register(new Ore("Lapis Lazuli Ore", 21).setMinDropCount(4).setMaxDropCount(8).setHardness(3.0F).setResistance(5.0F));
	public static final Solid LAPIS_BLOCK = (Solid) register(new Solid("Lapis Lazuli Block", 22).setHardness(3.0F).setResistance(5.0F));
	public static final Solid DISPENSER = (Solid) register(new Solid("Dispenser", 23).setHardness(3.5F).setResistance(5.8F));
	public static final Sandstone SANDSTONE = Sandstone.SANDSTONE;
	public static final Solid NOTEBLOCK = (Solid) register(new Solid("Note Block", 25).setHardness(0.8F).setResistance(1.3F));
	public static final Solid BED_BLOCK = (Solid) register(new Solid("Bed", 26).setHardness(0.2F).setResistance(0.3F));
	public static final MinecartTrackPowered POWERED_RAIL = (MinecartTrackPowered) register(new MinecartTrackPowered("Powered Rail", 27).setHardness(0.7F).setResistance(1.2F));
	public static final MinecartTrackDetector DETECTOR_RAIL = (MinecartTrackDetector) register(new MinecartTrackDetector("Detector Rail", 28).setHardness(0.7F).setResistance(1.2F));
	public static final Solid PISTON_STICKY_BASE = (Solid) register(new Solid("Sticky Piston", 29).setResistance(0.8F));
	public static final Solid WEB = (Solid) register(new Solid("Cobweb", 30).setHardness(4.0F).setResistance(20.0F));

	/**
	 * Warning: This is NOT the data=0 sub-material!
	 */
	public static final TallGrass TALL_GRASS = TallGrass.TALL_GRASS;
	public static final LongGrass DEAD_BUSH = (LongGrass) register(new LongGrass("Dead Shrubs", 32).setHardness(0.0F).setResistance(0.0F));
	public static final Solid PISTON_BASE = (Solid) register(new Solid("Piston", 33).setResistance(0.8F));
	public static final Solid PISTON_EXTENSION = (Solid) register(new Solid("Piston (Head)", 34).setResistance(0.8F));
	public static final Wool WOOL = Wool.WHITE;
	public static final Solid MOVED_BY_PISTON = (Solid) register(new Solid("Moved By Piston", 36).setResistance(0.0F));
	public static final GroundAttachable DANDELION = (GroundAttachable) register(new GroundAttachable("Dandelion", 37).setHardness(0.0F).setResistance(0.0F));
	public static final GroundAttachable ROSE = (GroundAttachable) register(new GroundAttachable("Rose", 38).setHardness(0.0F).setResistance(0.0F));
	public static final GroundAttachable BROWN_MUSHROOM = (GroundAttachable) register(new GroundAttachable("Brown Mushroom", 39).setHardness(0.0F).setResistance(0.0F).setLightLevel(1));
	public static final GroundAttachable RED_MUSHROOM = (GroundAttachable) register(new GroundAttachable("Red Mushroom", 40).setHardness(0.0F).setResistance(0.0F));
	public static final Solid GOLD_BLOCK = (Solid) register(new Solid("Gold Block", 41).setHardness(3.0F).setResistance(10.0F));
	public static final Solid IRON_BLOCK = (Solid) register(new Solid("Iron Block", 42).setHardness(5.0F).setResistance(10.0F));
	public static final DoubleSlab DOUBLE_SLABS = DoubleSlab.STONE;
	public static final Slab SLAB = Slab.STONE;
	public static final Solid BRICK = (Solid) register(new Solid("Brick Block", 45).setHardness(2.0F).setResistance(10.0F));
	public static final Solid TNT = (Solid) register(new Solid("TNT", 46).setHardness(0.0F).setResistance(0.0F));
	public static final Solid BOOKSHELF = (Solid) register(new Solid("Bookshelf", 47).setHardness(1.5F).setResistance(2.5F));
	public static final Solid MOSS_STONE = (Solid) register(new Solid("Moss Stone", 48).setHardness(2.0F).setResistance(10.0F).setResistance(10.0F));
	public static final Solid OBSIDIAN = (Solid) register(new Solid("Obsidian", 49).setHardness(50.0F).setResistance(2000.0F));
	public static final WallAttachable TORCH = (WallAttachable) register(new WallAttachable("Torch", 50).setHardness(0.0F).setResistance(0.0F).setLightLevel(14));
	public static final Fire FIRE = (Fire) register(new Fire("Fire").setHardness(0.0F).setResistance(0.0F).setLightLevel(15));
	public static final Solid MONSTER_SPAWNER = (Solid) register(new Solid("MonsterEntity Spawner", 52).setHardness(5.0F).setResistance(8.3F));
	public static final Solid WOODEN_STAIRS = (Solid) register(new Solid("Wooden Stairs", 53).setResistance(3.0F));
	public static final Solid CHEST = (Solid) register(new Solid("Chest", 54).setHardness(2.5F).setResistance(4.2F));
	public static final RedstoneWire REDSTONE_WIRE = (RedstoneWire) register(new RedstoneWire("Redstone Wire", 55).setHardness(0.0F).setResistance(0.0F));
	public static final Solid DIAMOND_ORE = (Solid) register(new Solid("Diamond Ore", 56).setHardness(3.0F).setResistance(5.0F));
	public static final Solid DIAMOND_BLOCK = (Solid) register(new Solid("Diamond Block", 57).setHardness(5.0F).setResistance(10.0F));
	public static final Solid CRAFTING_TABLE = (Solid) register(new Solid("Crafting Table", 58).setHardness(4.2F));
	public static final WheatCrop WHEATCROP = (WheatCrop) register(new WheatCrop("Wheat Crop").setResistance(0.0F));
	public static final Solid FARMLAND = (Solid) register(new Solid("Farmland", 60).setHardness(0.6F).setResistance(1.0F));
	public static final Solid FURNACE = (Solid) register(new Solid("Furnace", 61).setHardness(3.5F).setResistance(5.8F));
	public static final Solid BURNINGFURNACE = (Solid) register(new Solid("Burning Furnace", 62).setHardness(3.5F).setResistance(5.8F).setLightLevel(13));
	public static final SignBase SIGN_POST = (SignBase) register(new SignBase("Sign Post", 63).setHardness(1.0F).setResistance(1.6F));
	public static final DoorBlock WOODEN_DOOR_BLOCK = (DoorBlock) register(new DoorBlock("Wooden Door", 64, true).setHardness(3.0F));
	public static final Solid LADDERS = (Solid) register(new Solid("Ladders", 65).setHardness(0.4F).setResistance(0.7F));
	public static final MinecartTrack RAILS = (MinecartTrack) register(new MinecartTrack("Rails", 66).setHardness(0.7F).setResistance(1.2F));
	public static final Solid COBBLESTONE_STAIRS = (Solid) register(new Solid("Cobblestone Stairs", 67).setResistance(10.0F));
	public static final SignBase WALL_SIGN = (SignBase) register(new SignBase("Wall Sign", 68).setHardness(1.0F));
	public static final Solid LEVER = (Solid) register(new Solid("Lever", 69).setHardness(0.5F).setResistance(1.7F));
	public static final Solid STONE_PRESSURE_PLATE = (Solid) register(new Solid("Stone Pressure Plate", 70).setHardness(0.5F).setResistance(0.8F));
	public static final DoorBlock IRON_DOOR_BLOCK = (DoorBlock) register(new DoorBlock("Iron Door", 71, false).setHardness(5.0F).setResistance(8.3F));
	public static final Solid WOODEN_PRESSURE_PLATE = (Solid) register(new Solid("Wooden Pressure Plate", 72).setHardness(0.5F).setResistance(0.8F));
	public static final Ore REDSTONE_ORE = (Ore) register(new Ore("Redstone Ore", 73).setMinDropCount(4).setMaxDropCount(5).setHardness(3.0F).setResistance(5.0F));
	public static final Ore GLOWING_REDSTONE_ORE = (Ore) register(new Ore("Glowing Redstone Ore", 74).setMinDropCount(4).setMaxDropCount(5).setHardness(3.0F).setResistance(5.0F).setLightLevel(3));
	public static final RedstoneTorch REDSTONE_TORCH_OFF = (RedstoneTorch) register(new RedstoneTorch("Redstone Torch", 75, false).setHardness(0.0F).setResistance(0.0F));
	public static final RedstoneTorch REDSTONE_TORCH_ON = (RedstoneTorch) register(new RedstoneTorch("Redstone Torch (On)", 76, true).setHardness(0.0F).setResistance(0.0F).setLightLevel(7));
	public static final Solid STONE_BUTTON = (Solid) register(new Solid("Stone Button", 77).setHardness(0.5F).setResistance(0.8F));
	public static final Snow SNOW = (Snow) register(new Snow("Snow").setHardness(0.1F).setResistance(0.2F));
	public static final Ice ICE = (Ice) register(new Ice("Ice", 79).setHardness(0.5F).setResistance(0.8F));
	public static final Solid SNOW_BLOCK = (Solid) register(new Solid("Snow Block", 80).setHardness(0.2F).setResistance(0.3F));
	public static final Cactus CACTUS = (Cactus) register(new Cactus("Cactus", 81).setHardness(0.4F).setResistance(0.7F));
	public static final Solid CLAY_BLOCK = (Solid) register(new Solid("Clay Block", 82).setHardness(0.6F).setResistance(1.0F));
	public static final Solid SUGAR_CANE_BLOCK = (Solid) register(new Solid("Sugar Cane", 83).setHardness(0.0F).setResistance(0.0F));
	public static final Solid JUKEBOX = (Solid) register(new Solid("Jukebox", 84).setHardness(2.0F).setResistance(10.0F));
	public static final Solid FENCE = (Solid) register(new Solid("Fence", 85).setResistance(5.0F).setResistance(5.0F));
	public static final Solid PUMPKIN = (Solid) register(new Solid("Pumpkin", 86).setHardness(1.0F).setResistance(1.7F));
	public static final Solid NETHERRACK = (Solid) register(new Solid("Netherrack", 87).setHardness(0.7F));
	public static final Solid SOUL_SAND = (Solid) register(new Solid("Soul Sand", 88).setHardness(0.5F).setResistance(0.8F));
	public static final Ore GLOWSTONE_BLOCK = (Ore) register(new Ore("Glowstone Block", 89).setMinDropCount(2).setMaxDropCount(4).setHardness(0.3F).setResistance(0.5F).setLightLevel(15));
	public static final Solid PORTAL = (Solid) register(new Solid("Portal", 90).setHardness(-1.0F).setResistance(0.0F).setLightLevel(11));
	public static final Solid JACK_O_LANTERN = (Solid) register(new Solid("Jack 'o' Lantern", 91).setHardness(1.0F).setResistance(1.7F).setLightLevel(15));
	public static final Solid CAKE_BLOCK = (Solid) register(new Solid("Cake Block", 92).setHardness(0.5F).setResistance(0.8F));
	public static final Solid REDSTONE_REPEATER_OFF = (Solid) register(new Solid("Redstone Repeater", 93).setHardness(0.0F).setResistance(0.0F));
	public static final Solid REDSTONE_REPEATER_ON = (Solid) register(new Solid("Redstone Repeater (On)", 94).setHardness(0.0F).setResistance(0.0F).setLightLevel(9));
	public static final Solid LOCKED_CHEST = (Solid) register(new Solid("Locked Chest", 95).setHardness(0.0F).setResistance(0.0F).setLightLevel(15));
	public static final Solid TRAPDOOR = (Solid) register(new Solid("Trapdoor", 96).setHardness(3.0F).setResistance(5.0F));
	public static final Solid SILVERFISH_STONE = (Solid) register(new Solid("Silverfish Stone", 97).setHardness(0.75F).setResistance(10.0F)); //Placeholder, block resistance unknown
	public static final StoneBrick STONE_BRICK = StoneBrick.STONE;
	public static final Ore HUGE_BROWN_MUSHROOM = (Ore) register(new Ore("Huge Brown Mushroom", 99).setMinDropCount(0).setMaxDropCount(2).setHardness(0.2F).setResistance(0.3F)); //Placeholder, block resistance unknown
	public static final Ore HUGE_RED_MUSHROOM = (Ore) register(new Ore("Huge Red Mushroom", 100).setMinDropCount(0).setMaxDropCount(2).setHardness(0.2F).setResistance(0.3F)); //Placeholder, block resistance unknown
	public static final Solid IRON_BARS = (Solid) register(new Solid("Iron Bars", 101).setHardness(5.0F).setResistance(10.0F));
	public static final Solid GLASS_PANE = (Solid) register(new Solid("Glass Pane", 102).setHardness(0.3F).setResistance(0.3F)); //Placeholder, block resistance unknown
	public static final Ore MELON = (Ore) register(new Ore("Melon", 103).setMinDropCount(3).setMaxDropCount(7).setHardness(1.0F).setResistance(1.7F));
	public static final Solid PUMPKIN_STEM = (Solid) register(new Solid("Pumpkin Stem", 104).setHardness(0.0F).setResistance(0.0F));
	public static final Solid MELON_STEM = (Solid) register(new Solid("Melon Stem", 105).setHardness(0.0F).setResistance(0.3F));
	public static final Solid VINES = (Solid) register(new Solid("Vines", 106).setHardness(0.2F).setResistance(0.3F));
	public static final Solid FENCE_GATE = (Solid) register(new Solid("Fence Gate", 107).setHardness(2.0F).setResistance(3.0F));
	public static final Solid BRICK_STAIRS = (Solid) register(new Solid("Brick Stairs", 108).setResistance(10.0F));
	public static final Solid STONE_BRICK_STAIRS = (Solid) register(new Solid("Stone Brick Stairs", 109).setResistance(10.0F));
	public static final Solid MYCELIUM = (Solid) register(new Solid("Mycelium", 110).setHardness(0.6F).setResistance(0.8F));
	public static final Solid LILY_PAD = (Solid) register(new Solid("Lily Pad", 111).setHardness(0.0F).setResistance(0.3F)); //Placeholder, block resistance unknown
	public static final Solid NETHER_BRICK = (Solid) register(new Solid("Nether Brick", 112).setHardness(2.0F).setResistance(10.0F));
	public static final Solid NETHER_BRICK_FENCE = (Solid) register(new Solid("Nether Brick Fence", 113).setHardness(2.0F).setResistance(10.0F));
	public static final Solid NETHER_BRICK_STAIRS = (Solid) register(new Solid("Nether Brick Stairs", 114).setResistance(10.0F));
	public static final Solid NETHER_WART_BLOCK = (Solid) register(new Solid("Nether Wart", 115).setResistance(0.0F));
	public static final Solid ENCHANTMENT_TABLE = (Solid) register(new Solid("Enchantment Table", 116).setHardness(5.0F).setResistance(2000.0F));
	public static final Solid BREWING_STAND_BLOCK = (Solid) register(new Solid("Brewing Stand", 117).setHardness(0.5F).setResistance(0.8F).setLightLevel(1));
	public static final Solid CAULDRON_BLOCK = (Solid) register(new Solid("Cauldron", 118).setHardness(2.0F).setResistance(3.3F));
	public static final Solid END_PORTAL = (Solid) register(new Solid("End Portal", 119).setHardness(-1.0F).setResistance(6000000.0F).setLightLevel(1));
	public static final Solid END_PORTAL_FRAME = (Solid) register(new Solid("End Portal Frame", 120).setHardness(-1.0F));
	public static final Solid END_STONE = (Solid) register(new Solid("End Stone", 121).setHardness(3.0F).setResistance(15.0F));
	public static final Solid DRAGON_EGG = (Solid) register(new Solid("Dragon Egg", 122).setHardness(3.0F).setResistance(15.0F).setLightLevel(1));
	public static final Solid REDSTONE_LAMP_OFF = (Solid) register(new Solid("Redstone Lamp", 123).setHardness(0.3F).setResistance(0.5F)); //Placeholder Values
	public static final Solid REDSTONE_LAMP_ON = (Solid) register(new Solid("Redstone Lamp (On)", 124).setHardness(0.3F).setResistance(0.5F).setLightLevel(15)); //Placeholder Values
	/*
		 * Items
		 */
	public static final GenericTool IRON_SHOVEL = register(new GenericTool("Iron Shovel", 256, (short) 251));
	public static final GenericWeapon IRON_PICKAXE = register(new GenericWeapon("Iron Pickaxe", 257, 4, (short) 251));
	public static final GenericWeapon IRON_AXE = register(new GenericWeapon("Iron Axe", 258, 5, (short) 251));
	public static final GenericTool FLINT_AND_STEEL = register(new GenericTool("Flint and Steel", 259, (short) 64));
	public static final GenericFood RED_APPLE = register(new GenericFood("Apple", 260, 4, FoodEffectType.HUNGER));
	public static final GenericRangedWeapon BOW = register(new GenericRangedWeapon("Bow", 261, 1, 9, (short) 385));
	public static final GenericItem ARROW = register(new GenericItem("Arrow", 262));
	public static final Coal COAL = Coal.COAL;
	public static final GenericItem DIAMOND = register(new GenericItem("Diamond", 264));
	public static final GenericItem IRON_INGOT = register(new GenericItem("Iron Ingot", 265));
	public static final GenericItem GOLD_INGOT = register(new GenericItem("Gold Ingot", 266));
	public static final GenericWeapon IRON_SWORD = register(new GenericWeapon("Iron Sword", 267, 6, (short) 251));
	public static final GenericWeapon WOODEN_SWORD = register(new GenericWeapon("Wooden Sword", 268, 4, (short) 60));
	public static final GenericTool WOODEN_SHOVEL = register(new GenericTool("Wooden Shovel", 269, (short) 60));
	public static final GenericWeapon WOODEN_PICKAXE = register(new GenericWeapon("Wooden Pickaxe", 270, 2, (short) 60));
	public static final GenericWeapon WOODEN_AXE = register(new GenericWeapon("Wooden Axe", 271, 3, (short) 60));
	public static final GenericWeapon STONE_SWORD = register(new GenericWeapon("Stone Sword", 272, 5, (short) 132));
	public static final GenericTool STONE_SHOVEL = register(new GenericTool("Stone Shovel", 273, (short) 132));
	public static final GenericWeapon STONE_PICKAXE = register(new GenericWeapon("Stone Pickaxe", 274, 3, (short) 132));
	public static final GenericWeapon STONE_AXE = register(new GenericWeapon("Stone Axe", 275, 3, (short) 132));
	public static final GenericWeapon DIAMOND_SWORD = register(new GenericWeapon("Diamond Sword", 276, 7, (short) 1562));
	public static final GenericTool DIAMOND_SHOVEL = register(new GenericTool("Diamond Shovel", 277, (short) 1562));
	public static final GenericWeapon DIAMOND_PICKAXE = register(new GenericWeapon("Diamond Pickaxe", 278, 5, (short) 1562));
	public static final GenericWeapon DIAMOND_AXE = register(new GenericWeapon("Diamond Axe", 279, 6, (short) 1562));
	public static final GenericItem STICK = register(new GenericItem("Stick", 280));
	public static final GenericItem BOWL = register(new GenericItem("Bowl", 281));
	public static final GenericFood MUSHROOM_SOUP = register(new GenericFood("Mushroom Soup", 282, 8, FoodEffectType.HUNGER));
	public static final GenericWeapon GOLD_SWORD = register(new GenericWeapon("Gold Sword", 283, 4, (short) 33));
	public static final GenericTool GOLD_SHOVEL = register(new GenericTool("Gold Shovel", 284, (short) 33));
	public static final GenericWeapon GOLD_PICKAXE = register(new GenericWeapon("Gold Pickaxe", 285, 2, (short) 33));
	public static final GenericWeapon GOLD_AXE = register(new GenericWeapon("Gold Axe", 286, 3, (short) 33));
	public static final GenericItem STRING = register(new GenericItem("String", 287));
	public static final GenericItem FEATHER = register(new GenericItem("Feather", 288));
	public static final GenericItem GUNPOWDER = register(new GenericItem("Gunpowder", 289));
	public static final GenericTool WOODEN_HOE = register(new GenericTool("Wooden Hoe", 290, (short) 60));
	public static final GenericTool STONE_HOE = register(new GenericTool("Stone Hoe", 291, (short) 132));
	public static final GenericTool IRON_HOE = register(new GenericTool("Iron Hoe", 292, (short) 251));
	public static final GenericTool DIAMOND_HOE = register(new GenericTool("Diamond Hoe", 293, (short) 1562));
	public static final GenericTool GOLD_HOE = register(new GenericTool("Gold Hoe", 294, (short) 33));
	public static final GenericItem SEEDS = register(new GenericItem("Seeds", 295));
	public static final GenericItem WHEAT = register(new GenericItem("Wheat", 296));
	public static final GenericFood BREAD = register(new GenericFood("Bread", 297, 5, FoodEffectType.HUNGER));
	public static final GenericArmor LEATHER_CAP = register(new GenericArmor("Leather Cap", 298, 1));
	public static final GenericArmor LEATHER_TUNIC = register(new GenericArmor("Leather Tunic", 299, 3));
	public static final GenericArmor LEATHER_PANTS = register(new GenericArmor("Leather Pants", 300, 2));
	public static final GenericArmor LEATHER_BOOTS = register(new GenericArmor("Leather Boots", 301, 1));
	public static final GenericArmor CHAIN_HELMET = register(new GenericArmor("Chain Helmet", 302, 2));
	public static final GenericArmor CHAIN_CHESTPLATE = register(new GenericArmor("Chain Chestplate", 303, 5));
	public static final GenericArmor CHAIN_LEGGINGS = register(new GenericArmor("Chain Leggings", 304, 4));
	public static final GenericArmor CHAIN_BOOTS = register(new GenericArmor("Chain Boots", 305, 1));
	public static final GenericArmor IRON_HELMET = register(new GenericArmor("Iron Helmet", 306, 2));
	public static final GenericArmor IRON_CHESTPLATE = register(new GenericArmor("Iron Chestplate", 307, 6));
	public static final GenericArmor IRON_LEGGINGS = register(new GenericArmor("Iron Leggings", 308, 5));
	public static final GenericArmor IRON_BOOTS = register(new GenericArmor("Iron Boots", 309, 2));
	public static final GenericArmor DIAMOND_HELMET = register(new GenericArmor("Diamond Helmet", 310, 3));
	public static final GenericArmor DIAMOND_CHESTPLATE = register(new GenericArmor("Diamond Chestplate", 311, 8));
	public static final GenericArmor DIAMOND_LEGGINGS = register(new GenericArmor("Diamond Leggings", 312, 6));
	public static final GenericArmor DIAMOND_BOOTS = register(new GenericArmor("Diamond Boots", 313, 3));
	public static final GenericArmor GOLD_HELMET = register(new GenericArmor("Gold Helmet", 314, 2));
	public static final GenericArmor GOLD_CHESTPLATE = register(new GenericArmor("Gold Chestplate", 315, 5));
	public static final GenericArmor GOLD_LEGGINGS = register(new GenericArmor("Gold Leggings", 316, 3));
	public static final GenericArmor GOLD_BOOTS = register(new GenericArmor("Gold Boots", 317, 1));
	public static final GenericItem FLINT = register(new GenericItem("Flint", 318));
	public static final GenericFood RAW_PORKCHOP = register(new GenericFood("Raw Porkchop", 319, 3, FoodEffectType.HUNGER));
	public static final GenericFood COOKED_PORKCHOP = register(new GenericFood("Cooked Porkchop", 320, 8, FoodEffectType.HUNGER));
	public static final GenericItem PAINTINGS = register(new GenericItem("Paintings", 321));
	public static final GenericFood GOLDEN_APPLE = register(new GenericFood("Golden Apple", 322, 10, FoodEffectType.HUNGER));
	public static final GenericItem SIGN = register(new GenericItem("Sign", 323));
	public static final DoorItem WOODEN_DOOR = register(new DoorItem("Wooden Door", 324, WOODEN_DOOR_BLOCK));
	public static final GenericEmptyContainer BUCKET = register(new GenericEmptyContainer("Bucket", 325));
	public static final GenericFullContainer WATER_BUCKET = register(new GenericFullContainer("Water Bucket", 326, WATER, BUCKET));
	public static final GenericFullContainer LAVA_BUCKET = register(new GenericFullContainer("Lava Bucket", 327, LAVA, BUCKET));
	public static final Minecart MINECART = register(new Minecart("Minecart", 328));
	public static final GenericItem SADDLE = register(new GenericItem("Saddle", 329));
	public static final DoorItem IRON_DOOR = register(new DoorItem("Iron Door", 330, IRON_DOOR_BLOCK));
	public static final GenericBlockItem REDSTONE = register(new GenericBlockItem("Redstone", 331, VanillaMaterials.REDSTONE_WIRE));
	public static final GenericItem SNOWBALL = register(new GenericItem("Snowball", 332));
	public static final GenericItem BOAT = register(new GenericItem("Boat", 333));
	public static final GenericItem LEATHER = register(new GenericItem("Leather", 334));
	public static final GenericItem MILK = register(new GenericItem("Milk", 335));
	public static final GenericItem CLAY_BRICK = register(new GenericItem("Brick", 336));
	public static final GenericItem CLAY = register(new GenericItem("Clay", 337));
	public static final GenericBlockItem SUGAR_CANE = register(new GenericBlockItem("Sugar Cane", 338, VanillaMaterials.SUGAR_CANE_BLOCK));
	public static final GenericItem PAPER = register(new GenericItem("Paper", 339));
	public static final GenericItem BOOK = register(new GenericItem("Book", 340));
	public static final GenericItem SLIMEBALL = register(new GenericItem("Slimeball", 341));
	public static final StorageMinecart MINECART_CHEST = register(new StorageMinecart("Minecart with Chest", 342));
	public static final PoweredMinecart MINECART_FURNACE = register(new PoweredMinecart("Minecart with Furnace", 343));
	public static final GenericItem EGG = register(new GenericItem("Egg", 344));
	public static final GenericItem COMPASS = register(new GenericItem("Compass", 345));
	public static final GenericTool FISHING_ROD = register(new GenericTool("Fishing Rod", 346, (short) 65));
	public static final GenericItem CLOCK = register(new GenericItem("Clock", 347));
	public static final GenericItem GLOWSTONE_DUST = register(new GenericItem("Glowstone Dust", 348));
	public static final GenericFood RAW_FISH = register(new GenericFood("Raw Fish", 349, 2, FoodEffectType.HUNGER));
	public static final GenericFood COOKED_FISH = register(new GenericFood("Cooked Fish", 350, 5, FoodEffectType.HUNGER));
	public static final Dye DYE = Dye.INK_SAC;
	public static final GenericItem BONE = register(new GenericItem("Bone", 352));
	public static final GenericItem SUGAR = register(new GenericItem("Sugar", 353));
	public static final GenericBlockItem CAKE = register(new GenericBlockItem("Cake", 354, VanillaMaterials.CAKE_BLOCK));
	public static final GenericBlockItem BED = register(new GenericBlockItem("Bed", 355, VanillaMaterials.BED_BLOCK));
	public static final GenericBlockItem REDSTONE_REPEATER = register(new GenericBlockItem("Redstone Repeater", 356, VanillaMaterials.REDSTONE_REPEATER_OFF));
	public static final GenericFood COOKIE = register(new GenericFood("Cookie", 357, 1, FoodEffectType.HUNGER));
	public static final GenericItem MAP = register(new GenericItem("Map", 358));
	public static final Shears SHEARS = register(new Shears("Shears", 359, (short) 238));
	public static final GenericFood MELON_SLICE = register(new GenericFood("Melon Slice", 360, 2, FoodEffectType.HUNGER));
	public static final GenericItem PUMPKIN_SEEDS = register(new GenericItem("Pumpkin Seeds", 361));
	public static final GenericItem MELON_SEEDS = register(new GenericItem("Melon Seeds", 362));
	public static final GenericFood RAW_BEEF = register(new GenericFood("Raw Beef", 363, 3, FoodEffectType.HUNGER));
	public static final GenericFood STEAK = register(new GenericFood("Steak", 364, 8, FoodEffectType.HUNGER));
	public static final GenericFood RAW_CHICKEN = register(new GenericFood("Raw Chicken", 365, 2, FoodEffectType.HUNGER));
	public static final GenericFood COOKED_CHICKEN = register(new GenericFood("Cooked Chicken", 366, 6, FoodEffectType.HUNGER));
	public static final GenericFood ROTTEN_FLESH = register(new GenericFood("Rotten Flesh", 367, 4, FoodEffectType.HUNGER));
	public static final GenericItem ENDER_PEARL = register(new GenericItem("Ender Pearl", 368));
	public static final GenericItem BLAZE_ROD = register(new GenericItem("Blaze Rod", 369));
	public static final GenericItem GHAST_TEAR = register(new GenericItem("Ghast Tear", 370));
	public static final GenericItem GOLD_NUGGET = register(new GenericItem("Gold Nugget", 371));
	public static final GenericItem NETHER_WART = register(new GenericItem("Nether Wart", 372));
	public static final GenericItem GLASS_BOTTLE = register(new GenericItem("Glass Bottle", 374));
	public static final GenericFood SPIDER_EYE = register(new GenericFood("Spider Eye", 375, 2, FoodEffectType.HUNGER));
	public static final GenericItem FERMENTED_SPIDER_EYE = register(new GenericItem("Fermented Spider Eye", 376));
	public static final GenericItem BLAZE_POWDER = register(new GenericItem("Blaze Powder", 377));
	public static final GenericItem MAGMA_CREAM = register(new GenericItem("Magma Cream", 378));
	public static final GenericBlockItem BREWING_STAND = register(new GenericBlockItem("Brewing Stand", 379, VanillaMaterials.BREWING_STAND_BLOCK));
	public static final GenericBlockItem CAULDRON = register(new GenericBlockItem("Cauldron", 380, VanillaMaterials.CAULDRON_BLOCK));
	public static final GenericItem EYE_OF_ENDER = register(new GenericItem("Eye of Ender", 381));
	public static final GenericItem GLISTERING_MELON = register(new GenericItem("Glistering Melon", 382));
	/**
	 * Warning: This is NOT the data=0 sub-material!
	 */
	public static final SpawnEgg SPAWN_EGG = SpawnEgg.PIG;
	public static final GenericItem BOTTLE_O_ENCHANTING = register(new GenericItem("Bottle o' Enchanting", 384));
	public static final GenericBlockItem FIRE_CHARGE = register(new GenericBlockItem("Fire Charge", 385, VanillaMaterials.FIRE)); //Basic Implementation
	public static final GenericItem GOLD_MUSIC_DISC = register(new GenericItem("Music Disc", 2256));
	public static final GenericItem GREEN_MUSIC_DISC = register(new GenericItem("Music Disc", 2257));
	public static final GenericItem ORANGE_MUSIC_DISC = register(new GenericItem("Music Disc", 2258));
	public static final GenericItem RED_MUSIC_DISC = register(new GenericItem("Music Disc", 2259));
	public static final GenericItem CYAN_MUSIC_DISC = register(new GenericItem("Music Disc", 2260));
	public static final GenericItem BLUE_MUSIC_DISC = register(new GenericItem("Music Disc", 2261));
	public static final GenericItem PURPLE_MUSIC_DISC = register(new GenericItem("Music Disc", 2262));
	public static final GenericItem BLACK_MUSIC_DISC = register(new GenericItem("Music Disc", 2263));
	public static final GenericItem WHITE_MUSIC_DISC = register(new GenericItem("Music Disc", 2264));
	public static final GenericItem FOREST_GREEN_MUSIC_DISC = register(new GenericItem("Music Disc", 2265));
	public static final GenericItem BROKEN_MUSIC_DISC = register(new GenericItem("Music Disc", 2266));
	public static final Potion POTION = Potion.EMPTY;
	
	private static boolean initialized = false;

	@SuppressWarnings("static-access")
	public static void initialize() {
		if(initialized)
			return;
		VanillaMaterials.STONE.setDrop(VanillaMaterials.COBBLESTONE);
		VanillaMaterials.GRASS.setDrop(VanillaMaterials.DIRT);
		VanillaMaterials.COAL_ORE.setDrop(VanillaMaterials.COAL);
		VanillaMaterials.GLASS.setDrop(VanillaMaterials.AIR);
		VanillaMaterials.LAPIS_ORE.setDrop(VanillaMaterials.DYE.LAPIS_LAZULI);
		VanillaMaterials.BED_BLOCK.setDrop(VanillaMaterials.BED);
		VanillaMaterials.WEB.setDrop(VanillaMaterials.STRING);
		VanillaMaterials.TALL_GRASS.DEAD_GRASS.setDrop(VanillaMaterials.AIR);
		VanillaMaterials.TALL_GRASS.TALL_GRASS.setDrop(VanillaMaterials.AIR);
		VanillaMaterials.TALL_GRASS.FERN.setDrop(VanillaMaterials.AIR);
		VanillaMaterials.DEAD_BUSH.setDrop(VanillaMaterials.AIR);
		VanillaMaterials.DOUBLE_SLABS.STONE.setSlabMaterial(VanillaMaterials.SLAB.STONE).setDropCount(2);
		VanillaMaterials.DOUBLE_SLABS.SANDSTONE.setSlabMaterial(VanillaMaterials.SLAB.SANDSTONE).setDropCount(2);
		VanillaMaterials.DOUBLE_SLABS.WOOD.setSlabMaterial(VanillaMaterials.SLAB.WOOD).setDropCount(2);
		VanillaMaterials.DOUBLE_SLABS.COBBLESTONE.setSlabMaterial(VanillaMaterials.SLAB.COBBLESTONE).setDropCount(2);
		VanillaMaterials.DOUBLE_SLABS.BRICK.setSlabMaterial(VanillaMaterials.SLAB.BRICK).setDropCount(2);
		VanillaMaterials.DOUBLE_SLABS.STONE_BRICK.setSlabMaterial(VanillaMaterials.SLAB.STONE_BRICK).setDropCount(2);
		VanillaMaterials.BOOKSHELF.setDrop(VanillaMaterials.BOOK).setDropCount(3);
		VanillaMaterials.FIRE.setDrop(VanillaMaterials.AIR);
		VanillaMaterials.MONSTER_SPAWNER.setDrop(VanillaMaterials.AIR);
		VanillaMaterials.REDSTONE_WIRE.setDrop(VanillaMaterials.REDSTONE);
		VanillaMaterials.DIAMOND_ORE.setDrop(VanillaMaterials.DIAMOND);
		VanillaMaterials.WHEATCROP.setDrop(VanillaMaterials.WHEAT);
		VanillaMaterials.FARMLAND.setDrop(VanillaMaterials.DIRT);
		VanillaMaterials.BURNINGFURNACE.setDrop(VanillaMaterials.FURNACE);
		VanillaMaterials.SIGN_POST.setDrop(VanillaMaterials.SIGN);
		VanillaMaterials.WOODEN_DOOR_BLOCK.setDrop(VanillaMaterials.WOODEN_DOOR);
		VanillaMaterials.WALL_SIGN.setDrop(VanillaMaterials.SIGN);
		VanillaMaterials.REDSTONE_ORE.setDrop(VanillaMaterials.REDSTONE);
		VanillaMaterials.GLOWING_REDSTONE_ORE.setDrop(VanillaMaterials.REDSTONE);
		VanillaMaterials.REDSTONE_TORCH_OFF.setDrop(VanillaMaterials.REDSTONE_TORCH_ON);
		VanillaMaterials.SNOW.setDrop(VanillaMaterials.SNOWBALL); // TODO: Make shovels drop snowballs
		VanillaMaterials.ICE.setDrop(VanillaMaterials.AIR);
		VanillaMaterials.CLAY_BLOCK.setDrop(VanillaMaterials.CLAY).setDropCount(4);
		VanillaMaterials.GLOWSTONE_BLOCK.setDrop(VanillaMaterials.GLOWSTONE_DUST);
		VanillaMaterials.PORTAL.setDrop(VanillaMaterials.AIR);
		VanillaMaterials.CAKE_BLOCK.setDrop(VanillaMaterials.AIR);
		VanillaMaterials.REDSTONE_REPEATER_OFF.setDrop(VanillaMaterials.REDSTONE_REPEATER);
		VanillaMaterials.REDSTONE_REPEATER_ON.setDrop(VanillaMaterials.REDSTONE_REPEATER);
		VanillaMaterials.SILVERFISH_STONE.setDrop(VanillaMaterials.STONE); // TODO: Get drop item based on data
		VanillaMaterials.HUGE_BROWN_MUSHROOM.setDrop(VanillaMaterials.BROWN_MUSHROOM);
		VanillaMaterials.HUGE_RED_MUSHROOM.setDrop(VanillaMaterials.RED_MUSHROOM);
		VanillaMaterials.GLASS_PANE.setDrop(VanillaMaterials.AIR);
		VanillaMaterials.MELON.setDrop(VanillaMaterials.MELON_SLICE);
		VanillaMaterials.PUMPKIN_STEM.setDrop(VanillaMaterials.AIR);
		VanillaMaterials.MELON_STEM.setDrop(VanillaMaterials.MELON_SEEDS);
		VanillaMaterials.VINES.setDrop(VanillaMaterials.AIR);
		VanillaMaterials.MYCELIUM.setDrop(VanillaMaterials.DIRT);
		VanillaMaterials.END_PORTAL.setDrop(VanillaMaterials.AIR);
		VanillaMaterials.END_PORTAL_FRAME.setDrop(VanillaMaterials.AIR);
		VanillaMaterials.REDSTONE_LAMP_ON.setDrop(VanillaMaterials.REDSTONE_LAMP_OFF);
		initialized = true;
	}
}
