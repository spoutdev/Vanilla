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
package org.spout.vanilla;

import org.spout.api.material.BlockMaterial;
import org.spout.api.material.ItemMaterial;
import org.spout.vanilla.material.Food.FoodEffectType;
import org.spout.vanilla.material.attachable.GroundAttachable;
import org.spout.vanilla.material.attachable.WallAttachable;
import org.spout.vanilla.material.block.Cactus;
import org.spout.vanilla.material.block.DoubleSlab;
import org.spout.vanilla.material.block.Grass;
import org.spout.vanilla.material.block.Gravel;
import org.spout.vanilla.material.block.Ice;
import org.spout.vanilla.material.block.Leaves;
import org.spout.vanilla.material.block.LongGrass;
import org.spout.vanilla.material.block.MinecartTrack;
import org.spout.vanilla.material.block.MinecartTrackDetector;
import org.spout.vanilla.material.block.MinecartTrackPowered;
import org.spout.vanilla.material.block.Ore;
import org.spout.vanilla.material.block.Sapling;
import org.spout.vanilla.material.block.Slab;
import org.spout.vanilla.material.block.Solid;
import org.spout.vanilla.material.block.StoneBrick;
import org.spout.vanilla.material.block.TallGrass;
import org.spout.vanilla.material.block.Tree;
import org.spout.vanilla.material.block.WheatCrop;
import org.spout.vanilla.material.block.Wool;
import org.spout.vanilla.material.generic.GenericArmor;
import org.spout.vanilla.material.generic.GenericBlock;
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
	public static final BlockMaterial STONE = new Solid("Stone", 1).setHardness(1.5F).setResistance(10.0F);
	public static final BlockMaterial GRASS = new Grass("Grass").setHardness(0.6F).setResistance(0.8F);
	public static final BlockMaterial DIRT = new Solid("Dirt", 3).setHardness(0.5F).setResistance(0.8F);
	public static final BlockMaterial COBBLESTONE = new Solid("Cobblestone", 4).setHardness(2.0F).setResistance(10.0F);
	public static final BlockMaterial WOOD = new Solid("Wooden Planks", 5).setHardness(2.0F).setResistance(5.0F);
	public static final Sapling SAPLING = Sapling.PARENT;	
	public static final BlockMaterial BEDROCK = new Solid("Bedrock", 7).setResistance(6000000.0F);
	public static final BlockMaterial WATER = new GenericLiquid("Water", 8, true).setHardness(100.0F).setResistance(166.7F).setOpacity((byte) 2);
	public static final BlockMaterial STATIONARY_WATER = new GenericLiquid("Stationary Water", 9, false).setHardness(100.0F).setResistance(166.7F).setOpacity((byte) 2);
	public static final BlockMaterial LAVA = new GenericLiquid("Lava", 10, true).setHardness(0.0F).setLightLevel(15).setResistance(0.0F);
	public static final BlockMaterial STATIONARY_LAVA = new GenericLiquid("Stationary Lava", 11, false).setHardness(100.0F).setLightLevel(15).setResistance(166.7F);
	public static final BlockMaterial SAND = new Solid("Sand", 12, true).setHardness(0.5F).setResistance(0.8F);
	public static final BlockMaterial GRAVEL = new Gravel("Gravel").setHardness(0.6F).setResistance(1.0F);
	public static final BlockMaterial GOLD_ORE = new Ore("Gold Ore", 14).setHardness(3.0F).setResistance(5.0F);
	public static final BlockMaterial IRON_ORE = new Ore("Iron Ore", 15).setHardness(3.0F).setResistance(5.0F);
	public static final BlockMaterial COAL_ORE = new Ore("Coal Ore", 16).setHardness(3.0F).setResistance(5.0F);
	public static final Tree LOG = Tree.PARENT;
	public static final Leaves LEAVES = Leaves.PARENT;
	public static final BlockMaterial SPONGE = new Solid("Sponge", 19).setHardness(0.6F).setResistance(1.0F);
	public static final BlockMaterial GLASS = new Solid("Glass", 20).setHardness(0.3F).setResistance(0.5F);
	public static final BlockMaterial LAPIS_ORE = new Ore("Lapis Lazuli Ore", 21).setMinDropCount(4).setMaxDropCount(8).setHardness(3.0F).setResistance(5.0F);
	public static final BlockMaterial LAPIS_BLOCK = new Solid("Lapis Lazuli Block", 22).setHardness(3.0F).setResistance(5.0F);
	public static final BlockMaterial DISPENSER = new Solid("Dispenser", 23).setHardness(3.5F).setResistance(5.8F);
	public static final BlockMaterial SANDSTONE = new Solid("Sandstone", 24).setHardness(0.8F).setResistance(1.3F);
	public static final BlockMaterial NOTEBLOCK = new Solid("Note Block", 25).setHardness(0.8F).setResistance(1.3F);
	public static final BlockMaterial BED_BLOCK = new Solid("Bed", 26).setHardness(0.2F).setResistance(0.3F);
	public static final BlockMaterial POWERED_RAIL = new MinecartTrackPowered("Powered Rail", 27).setHardness(0.7F).setResistance(1.2F);
	public static final BlockMaterial DETECTOR_RAIL = new MinecartTrackDetector("Detector Rail", 28).setHardness(0.7F).setResistance(1.2F);
	public static final BlockMaterial PISTON_STICKY_BASE = new Solid("Sticky Piston", 29).setResistance(0.8F);
	public static final BlockMaterial WEB = new Solid("Cobweb", 30).setHardness(4.0F).setResistance(20.0F);
	public static final TallGrass TALL_GRASS = TallGrass.PARENT;	
	public static final BlockMaterial DEAD_BUSH = new LongGrass("Dead Shrubs", 32).setHardness(0.0F).setResistance(0.0F);
	public static final BlockMaterial PISTON_BASE = new Solid("Piston", 33).setResistance(0.8F);
	public static final BlockMaterial PISTON_EXTENSION = new Solid("Piston (Head)", 34).setResistance(0.8F);
	public static final Wool WOOL = Wool.PARENT;
	public static final BlockMaterial MOVED_BY_PISTON = new Solid("Moved By Piston", 36).setResistance(0.0F);
	public static final BlockMaterial DANDELION = new GroundAttachable("Dandelion", 37).setHardness(0.0F).setResistance(0.0F);
	public static final BlockMaterial ROSE = new GroundAttachable("Rose", 38).setHardness(0.0F).setResistance(0.0F);
	public static final BlockMaterial BROWN_MUSHROOM = new GroundAttachable("Brown Mushroom", 39).setHardness(0.0F).setResistance(0.0F).setLightLevel(1);
	public static final BlockMaterial RED_MUSHROOM = new GroundAttachable("Red Mushroom", 40).setHardness(0.0F).setResistance(0.0F);
	public static final BlockMaterial GOLD_BLOCK = new Solid("Gold Block", 41).setHardness(3.0F).setResistance(10.0F);
	public static final BlockMaterial IRON_BLOCK = new Solid("Iron Block", 42).setHardness(5.0F).setResistance(10.0F);
	public static final DoubleSlab DOUBLE_SLABS = DoubleSlab.PARENT;
	public static final Slab SLAB = Slab.PARENT;
	public static final BlockMaterial BRICK = new Solid("Brick Block", 45).setHardness(2.0F).setResistance(10.0F);
	public static final BlockMaterial TNT = new Solid("TNT", 46).setHardness(0.0F).setResistance(0.0F);
	public static final BlockMaterial BOOKSHELF = new Solid("Bookshelf", 47).setHardness(1.5F).setResistance(2.5F);
	public static final BlockMaterial MOSS_STONE = new Solid("Moss Stone", 48).setHardness(2.0F).setResistance(10.0F).setResistance(10.0F);
	public static final BlockMaterial OBSIDIAN = new Solid("Obsidian", 49).setHardness(50.0F).setResistance(2000.0F);
	public static final BlockMaterial TORCH = new WallAttachable("Torch", 50).setHardness(0.0F).setResistance(0.0F).setLightLevel(14);
	public static final BlockMaterial FIRE = new Solid("Fire", 51).setHardness(0.0F).setResistance(0.0F).setLightLevel(15);
	public static final BlockMaterial MONSTER_SPAWNER = new Solid("MonsterEntity Spawner", 52).setHardness(5.0F).setResistance(8.3F);
	public static final BlockMaterial WOODEN_STAIRS = new Solid("Wooden Stairs", 53).setResistance(3.0F);
	public static final BlockMaterial CHEST = new Solid("Chest", 54).setHardness(2.5F).setResistance(4.2F);
	public static final BlockMaterial REDSTONE_WIRE = new RedstoneWire("Redstone Wire", 55).setHardness(0.0F).setResistance(0.0F);
	public static final BlockMaterial DIAMOND_ORE = new Solid("Diamond Ore", 56).setHardness(3.0F).setResistance(5.0F);
	public static final BlockMaterial DIAMOND_BLOCK = new Solid("Diamond Block", 57).setHardness(5.0F).setResistance(10.0F);
	public static final BlockMaterial CRAFTING_TABLE = new Solid("Crafting Table", 58).setHardness(4.2F);
	public static final BlockMaterial WHEATCROP = new WheatCrop("Wheat Crop").setResistance(0.0F);
	public static final BlockMaterial FARMLAND = new Solid("Farmland", 60).setHardness(0.6F).setResistance(1.0F);
	public static final BlockMaterial FURNACE = new Solid("Furnace", 61).setHardness(3.5F).setResistance(5.8F);
	public static final BlockMaterial BURNINGFURNACE = new Solid("Burning Furnace", 62).setHardness(3.5F).setResistance(5.8F).setLightLevel(13);
	public static final BlockMaterial SIGN_POST = new Solid("Sign Post", 63).setHardness(1.0F).setResistance(1.6F);
	public static final BlockMaterial WOODEN_DOOR_BLOCK = new DoorBlock("Wooden Door", 64, true).setHardness(3.0F);
	public static final BlockMaterial LADDERS = new Solid("Ladders", 65).setHardness(0.4F).setResistance(0.7F);
	public static final BlockMaterial RAILS = new MinecartTrack("Rails", 66).setHardness(0.7F).setResistance(1.2F);
	public static final BlockMaterial COBBLESTONE_STAIRS = new Solid("Cobblestone Stairs", 67).setResistance(10.0F);
	public static final BlockMaterial WALL_SIGN = new Solid("Wall Sign", 68).setHardness(1.0F);
	public static final BlockMaterial LEVER = new Solid("Lever", 69).setHardness(0.5F).setResistance(1.7F);
	public static final BlockMaterial STONE_PRESSURE_PLATE = new Solid("Stone Pressure Plate", 70).setHardness(0.5F).setResistance(0.8F);
	public static final BlockMaterial IRON_DOOR_BLOCK = new DoorBlock("Iron Door", 71, false).setHardness(5.0F).setResistance(8.3F);
	public static final BlockMaterial WOODEN_PRESSURE_PLATE = new Solid("Wooden Pressure Plate", 72).setHardness(0.5F).setResistance(0.8F);
	public static final BlockMaterial REDSTONE_ORE = new Ore("Redstone Ore", 73).setMinDropCount(4).setMaxDropCount(5).setHardness(3.0F).setResistance(5.0F);
	public static final BlockMaterial GLOWING_REDSTONE_ORE = new Ore("Glowing Redstone Ore", 74).setMinDropCount(4).setMaxDropCount(5).setHardness(3.0F).setResistance(5.0F).setLightLevel(3);
	public static final BlockMaterial REDSTONE_TORCH_OFF = new RedstoneTorch("Redstone Torch", 75, false).setHardness(0.0F).setResistance(0.0F);
	public static final BlockMaterial REDSTONE_TORCH_ON = new RedstoneTorch("Redstone Torch (On)", 76, true).setHardness(0.0F).setResistance(0.0F).setLightLevel(7);
	public static final BlockMaterial STONE_BUTTON = new Solid("Stone Button", 77).setHardness(0.5F).setResistance(0.8F);
	public static final BlockMaterial SNOW = new Solid("Snow", 78).setHardness(0.1F).setResistance(0.2F);
	public static final BlockMaterial ICE = new Ice("Ice", 79).setHardness(0.5F).setResistance(0.8F);
	public static final BlockMaterial SNOW_BLOCK = new Solid("Snow Block", 80).setHardness(0.2F).setResistance(0.3F);
	public static final BlockMaterial CACTUS = new Cactus("Cactus", 81).setHardness(0.4F).setResistance(0.7F);
	public static final BlockMaterial CLAY_BLOCK = new Solid("Clay Block", 82).setHardness(0.6F).setResistance(1.0F);
	public static final BlockMaterial SUGAR_CANE_BLOCK = new Solid("Sugar Cane", 83).setHardness(0.0F).setResistance(0.0F);
	public static final BlockMaterial JUKEBOX = new Solid("Jukebox", 84).setHardness(2.0F).setResistance(10.0F);
	public static final BlockMaterial FENCE = new Solid("Fence", 85).setResistance(5.0F).setResistance(5.0F);
	public static final BlockMaterial PUMPKIN = new Solid("Pumpkin", 86).setHardness(1.0F).setResistance(1.7F);
	public static final BlockMaterial NETHERRACK = new Solid("Netherrack", 87).setHardness(0.7F);
	public static final BlockMaterial SOUL_SAND = new Solid("Soul Sand", 88).setHardness(0.5F).setResistance(0.8F);
	public static final BlockMaterial GLOWSTONE_BLOCK = new Ore("Glowstone Block", 89).setMinDropCount(2).setMaxDropCount(4).setHardness(0.3F).setResistance(0.5F).setLightLevel(15);
	public static final BlockMaterial PORTAL = new Solid("Portal", 90).setHardness(-1.0F).setResistance(0.0F).setLightLevel(11);
	public static final BlockMaterial JACK_O_LANTERN = new Solid("Jack 'o' Lantern", 91).setHardness(1.0F).setResistance(1.7F).setLightLevel(15);
	public static final BlockMaterial CAKE_BLOCK = new Solid("Cake Block", 92).setHardness(0.5F).setResistance(0.8F);
	public static final BlockMaterial REDSTONE_REPEATER_OFF = new Solid("Redstone Repeater", 93).setHardness(0.0F).setResistance(0.0F);
	public static final BlockMaterial REDSTONE_REPEATER_ON = new Solid("Redstone Repeater (On)", 94).setHardness(0.0F).setResistance(0.0F).setLightLevel(9);
	public static final BlockMaterial LOCKED_CHEST = new Solid("Locked Chest", 95).setHardness(0.0F).setResistance(0.0F).setLightLevel(15);
	public static final BlockMaterial TRAPDOOR = new Solid("Trapdoor", 96).setHardness(3.0F).setResistance(5.0F);
	public static final BlockMaterial SILVERFISH_STONE = new Solid("Silverfish Stone", 97).setHardness(0.75F).setResistance(10.0F); //Placeholder, block resistance unknown
	public static final StoneBrick STONE_BRICK = StoneBrick.PARENT;
	public static final BlockMaterial HUGE_BROWN_MUSHROOM = new Ore("Huge Brown Mushroom", 99).setMinDropCount(0).setMaxDropCount(2).setHardness(0.2F).setResistance(0.3F); //Placeholder, block resistance unknown
	public static final BlockMaterial HUGE_RED_MUSHROOM = new Ore("Huge Red Mushroom", 100).setMinDropCount(0).setMaxDropCount(2).setHardness(0.2F).setResistance(0.3F); //Placeholder, block resistance unknown
	public static final BlockMaterial IRON_BARS = new Solid("Iron Bars", 101).setHardness(5.0F).setResistance(10.0F);
	public static final BlockMaterial GLASS_PANE = new Solid("Glass Pane", 102).setHardness(0.3F).setResistance(0.3F); //Placeholder, block resistance unknown
	public static final BlockMaterial MELON = new Ore("Melon", 103).setMinDropCount(3).setMaxDropCount(7).setHardness(1.0F).setResistance(1.7F);
	public static final BlockMaterial PUMPKIN_STEM = new Solid("Pumpkin Stem", 104).setHardness(0.0F).setResistance(0.0F);
	public static final BlockMaterial MELON_STEM = new Solid("Melon Stem", 105).setHardness(0.0F).setResistance(0.3F);
	public static final BlockMaterial VINES = new Solid("Vines", 106).setHardness(0.2F).setResistance(0.3F);
	public static final BlockMaterial FENCE_GATE = new Solid("Fence Gate", 107).setHardness(2.0F).setResistance(3.0F);
	public static final BlockMaterial BRICK_STAIRS = new Solid("Brick Stairs", 108).setResistance(10.0F);
	public static final BlockMaterial STONE_BRICK_STAIRS = new Solid("Stone Brick Stairs", 109).setResistance(10.0F);
	public static final BlockMaterial MYCELIUM = new Solid("Mycelium", 110).setHardness(0.6F).setResistance(0.8F);
	public static final BlockMaterial LILY_PAD = new Solid("Lily Pad", 111).setHardness(0.0F).setResistance(0.3F); //Placeholder, block resistance unknown
	public static final BlockMaterial NETHER_BRICK = new Solid("Nether Brick", 112).setHardness(2.0F).setResistance(10.0F);
	public static final BlockMaterial NETHER_BRICK_FENCE = new Solid("Nether Brick Fence", 113).setHardness(2.0F).setResistance(10.0F);
	public static final BlockMaterial NETHER_BRICK_STAIRS = new Solid("Nether Brick Stairs", 114).setResistance(10.0F);
	public static final BlockMaterial NETHER_WART_BLOCK = new Solid("Nether Wart", 115).setResistance(0.0F);
	public static final BlockMaterial ENCHANTMENT_TABLE = new Solid("Enchantment Table", 116).setHardness(5.0F).setResistance(2000.0F);
	public static final BlockMaterial BREWING_STAND_BLOCK = new Solid("Brewing Stand", 117).setHardness(0.5F).setResistance(0.8F).setLightLevel(1);
	public static final BlockMaterial CAULDRON_BLOCK = new Solid("Cauldron", 118).setHardness(2.0F).setResistance(3.3F);
	public static final BlockMaterial END_PORTAL = new Solid("End Portal", 119).setHardness(-1.0F).setResistance(6000000.0F).setLightLevel(1);
	public static final BlockMaterial END_PORTAL_FRAME = new Solid("End Portal Frame", 120).setHardness(-1.0F);
	public static final BlockMaterial END_STONE = new Solid("End Stone", 121).setHardness(3.0F).setResistance(15.0F);
	public static final BlockMaterial DRAGON_EGG = new Solid("Dragon Egg", 122).setHardness(3.0F).setResistance(15.0F).setLightLevel(1);
	public static final BlockMaterial REDSTONE_LAMP_OFF = new Solid("Redstone Lamp", 123).setHardness(0.3F).setResistance(0.5F); //Placeholder Values
	public static final BlockMaterial REDSTONE_LAMP_ON = new Solid("Redstone Lamp (On)", 124).setHardness(0.3F).setResistance(0.5F).setLightLevel(15); //Placeholder Values
	/*
	 * Items
	 */
	public static final ItemMaterial IRON_SHOVEL = new GenericTool("Iron Shovel", 256, (short) 251);
	public static final ItemMaterial IRON_PICKAXE = new GenericWeapon("Iron Pickaxe", 257, 4, (short) 251);
	public static final ItemMaterial IRON_AXE = new GenericWeapon("Iron Axe", 258, 5, (short) 251);
	public static final ItemMaterial FLINT_AND_STEEL = new GenericTool("Flint and Steel", 259, (short) 64);
	public static final ItemMaterial RED_APPLE = new GenericFood("Apple", 260, 4, FoodEffectType.HUNGER);
	public static final ItemMaterial BOW = new GenericRangedWeapon("Bow", 261, 1, 9, (short) 385);
	public static final ItemMaterial ARROW = new GenericItem("Arrow", 262);
	public static final Coal COAL = Coal.PARENT;	
	public static final ItemMaterial DIAMOND = new GenericItem("Diamond", 264);
	public static final ItemMaterial IRON_INGOT = new GenericItem("Iron Ingot", 265);
	public static final ItemMaterial GOLD_INGOT = new GenericItem("Gold Ingot", 266);
	public static final ItemMaterial IRON_SWORD = new GenericWeapon("Iron Sword", 267, 6, (short) 251);
	public static final ItemMaterial WOODEN_SWORD = new GenericWeapon("Wooden Sword", 268, 4, (short) 60);
	public static final ItemMaterial WOODEN_SHOVEL = new GenericTool("Wooden Shovel", 269, (short) 60);
	public static final ItemMaterial WOODEN_PICKAXE = new GenericWeapon("Wooden Pickaxe", 270, 2, (short) 60);
	public static final ItemMaterial WOODEN_AXE = new GenericWeapon("Wooden Axe", 271, 3, (short) 60);
	public static final ItemMaterial STONE_SWORD = new GenericWeapon("Stone Sword", 272, 5, (short) 132);
	public static final ItemMaterial STONE_SHOVEL = new GenericTool("Stone Shovel", 273, (short) 132);
	public static final ItemMaterial STONE_PICKAXE = new GenericWeapon("Stone Pickaxe", 274, 3, (short) 132);
	public static final ItemMaterial STONE_AXE = new GenericWeapon("Stone Axe", 275, 3, (short) 132);
	public static final ItemMaterial DIAMOND_SWORD = new GenericWeapon("Diamond Sword", 276, 7, (short) 1562);
	public static final ItemMaterial DIAMOND_SHOVEL = new GenericTool("Diamond Shovel", 277, (short) 1562);
	public static final ItemMaterial DIAMOND_PICKAXE = new GenericWeapon("Diamond Pickaxe", 278, 5, (short) 1562);
	public static final ItemMaterial DIAMOND_AXE = new GenericWeapon("Diamond Axe", 279, 6, (short) 1562);
	public static final ItemMaterial STICK = new GenericItem("Stick", 280);
	public static final ItemMaterial BOWL = new GenericItem("Bowl", 281);
	public static final ItemMaterial MUSHROOM_SOUP = new GenericFood("Mushroom Soup", 282, 8, FoodEffectType.HUNGER);
	public static final ItemMaterial GOLD_SWORD = new GenericWeapon("Gold Sword", 283, 4, (short) 33);
	public static final ItemMaterial GOLD_SHOVEL = new GenericTool("Gold Shovel", 284, (short) 33);
	public static final ItemMaterial GOLD_PICKAXE = new GenericWeapon("Gold Pickaxe", 285, 2, (short) 33);
	public static final ItemMaterial GOLD_AXE = new GenericWeapon("Gold Axe", 286, 3, (short) 33);
	public static final ItemMaterial STRING = new GenericItem("String", 287);
	public static final ItemMaterial FEATHER = new GenericItem("Feather", 288);
	public static final ItemMaterial GUNPOWDER = new GenericItem("Gunpowder", 289);
	public static final ItemMaterial WOODEN_HOE = new GenericTool("Wooden Hoe", 290, (short) 60);
	public static final ItemMaterial STONE_HOE = new GenericTool("Stone Hoe", 291, (short) 132);
	public static final ItemMaterial IRON_HOE = new GenericTool("Iron Hoe", 292, (short) 251);
	public static final ItemMaterial DIAMOND_HOE = new GenericTool("Diamond Hoe", 293, (short) 1562);
	public static final ItemMaterial GOLD_HOE = new GenericTool("Gold Hoe", 294, (short) 33);
	public static final ItemMaterial SEEDS = new GenericItem("Seeds", 295);
	public static final ItemMaterial WHEAT = new GenericItem("Wheat", 296);
	public static final ItemMaterial BREAD = new GenericFood("Bread", 297, 5, FoodEffectType.HUNGER);
	public static final ItemMaterial LEATHER_CAP = new GenericArmor("Leather Cap", 298, 1);
	public static final ItemMaterial LEATHER_TUNIC = new GenericArmor("Leather Tunic", 299, 3);
	public static final ItemMaterial LEATHER_PANTS = new GenericArmor("Leather Pants", 300, 2);
	public static final ItemMaterial LEATHER_BOOTS = new GenericArmor("Leather Boots", 301, 1);
	public static final ItemMaterial CHAIN_HELMET = new GenericArmor("Chain Helmet", 302, 2);
	public static final ItemMaterial CHAIN_CHESTPLATE = new GenericArmor("Chain Chestplate", 303, 5);
	public static final ItemMaterial CHAIN_LEGGINGS = new GenericArmor("Chain Leggings", 304, 4);
	public static final ItemMaterial CHAIN_BOOTS = new GenericArmor("Chain Boots", 305, 1);
	public static final ItemMaterial IRON_HELMET = new GenericArmor("Iron Helmet", 306, 2);
	public static final ItemMaterial IRON_CHESTPLATE = new GenericArmor("Iron Chestplate", 307, 6);
	public static final ItemMaterial IRON_LEGGINGS = new GenericArmor("Iron Leggings", 308, 5);
	public static final ItemMaterial IRON_BOOTS = new GenericArmor("Iron Boots", 309, 2);
	public static final ItemMaterial DIAMOND_HELMET = new GenericArmor("Diamond Helmet", 310, 3);
	public static final ItemMaterial DIAMOND_CHESTPLATE = new GenericArmor("Diamond Chestplate", 311, 8);
	public static final ItemMaterial DIAMOND_LEGGINGS = new GenericArmor("Diamond Leggings", 312, 6);
	public static final ItemMaterial DIAMOND_BOOTS = new GenericArmor("Diamond Boots", 313, 3);
	public static final ItemMaterial GOLD_HELMET = new GenericArmor("Gold Helmet", 314, 2);
	public static final ItemMaterial GOLD_CHESTPLATE = new GenericArmor("Gold Chestplate", 315, 5);
	public static final ItemMaterial GOLD_LEGGINGS = new GenericArmor("Gold Leggings", 316, 3);
	public static final ItemMaterial GOLD_BOOTS = new GenericArmor("Gold Boots", 317, 1);
	public static final ItemMaterial FLINT = new GenericItem("Flint", 318);
	public static final ItemMaterial RAW_PORKCHOP = new GenericFood("Raw Porkchop", 319, 3, FoodEffectType.HUNGER);
	public static final ItemMaterial COOKED_PORKCHOP = new GenericFood("Cooked Porkchop", 320, 8, FoodEffectType.HUNGER);
	public static final ItemMaterial PAINTINGS = new GenericItem("Paintings", 321);
	public static final ItemMaterial GOLDEN_APPLE = new GenericFood("Golden Apple", 322, 10, FoodEffectType.HUNGER);
	public static final ItemMaterial SIGN = new GenericItem("Sign", 323);
	public static final ItemMaterial WOODEN_DOOR = new DoorItem("Wooden Door", 324, (DoorBlock) WOODEN_DOOR_BLOCK);
	public static final ItemMaterial BUCKET = new GenericEmptyContainer("Bucket", 325);
	public static final ItemMaterial WATER_BUCKET = new GenericFullContainer("Water Bucket", 326, WATER, (GenericEmptyContainer) BUCKET);
	public static final ItemMaterial LAVA_BUCKET = new GenericFullContainer("Lava Bucket", 327, LAVA, (GenericEmptyContainer) BUCKET);
	public static final ItemMaterial MINECART = new Minecart("Minecart", 328);
	public static final ItemMaterial SADDLE = new GenericItem("Saddle", 329);
	public static final ItemMaterial IRON_DOOR = new DoorItem("Iron Door", 330, (DoorBlock) IRON_DOOR_BLOCK);
	public static final ItemMaterial REDSTONE = new GenericBlockItem("Redstone", 331, VanillaMaterials.REDSTONE_WIRE);
	public static final ItemMaterial SNOWBALL = new GenericItem("Snowball", 332);
	public static final ItemMaterial BOAT = new GenericItem("Boat", 333);
	public static final ItemMaterial LEATHER = new GenericItem("Leather", 334);
	public static final ItemMaterial MILK = new GenericItem("Milk", 335);
	public static final ItemMaterial CLAY_BRICK = new GenericItem("Brick", 336);
	public static final ItemMaterial CLAY = new GenericItem("Clay", 337);
	public static final ItemMaterial SUGAR_CANE = new GenericBlockItem("Sugar Cane", 338, VanillaMaterials.SUGAR_CANE_BLOCK);
	public static final ItemMaterial PAPER = new GenericItem("Paper", 339);
	public static final ItemMaterial BOOK = new GenericItem("Book", 340);
	public static final ItemMaterial SLIMEBALL = new GenericItem("Slimeball", 341);
	public static final ItemMaterial MINECART_CHEST = new StorageMinecart("Minecart with Chest", 342);
	public static final ItemMaterial MINECART_FURNACE = new PoweredMinecart("Minecart with Furnace", 343);
	public static final ItemMaterial EGG = new GenericItem("Egg", 344);
	public static final ItemMaterial COMPASS = new GenericItem("Compass", 345);
	public static final ItemMaterial FISHING_ROD = new GenericTool("Fishing Rod", 346, (short) 65);
	public static final ItemMaterial CLOCK = new GenericItem("Clock", 347);
	public static final ItemMaterial GLOWSTONE_DUST = new GenericItem("Glowstone Dust", 348);
	public static final ItemMaterial RAW_FISH = new GenericFood("Raw Fish", 349, 2, FoodEffectType.HUNGER);
	public static final ItemMaterial COOKED_FISH = new GenericFood("Cooked Fish", 350, 5, FoodEffectType.HUNGER);
	public static final Dye DYE = Dye.PARENT;
	public static final ItemMaterial BONE = new GenericItem("Bone", 352);
	public static final ItemMaterial SUGAR = new GenericItem("Sugar", 353);
	public static final ItemMaterial CAKE = new GenericBlockItem("Cake", 354, VanillaMaterials.CAKE_BLOCK);
	public static final ItemMaterial BED = new GenericBlockItem("Bed", 355, VanillaMaterials.BED_BLOCK);
	public static final ItemMaterial REDSTONE_REPEATER = new GenericBlockItem("Redstone Repeater", 356, VanillaMaterials.REDSTONE_REPEATER_OFF);
	public static final ItemMaterial COOKIE = new GenericFood("Cookie", 357, 1, FoodEffectType.HUNGER);
	public static final ItemMaterial MAP = new GenericItem("Map", 358);
	public static final ItemMaterial SHEARS = new Shears("Shears", 359, (short) 238);
	public static final ItemMaterial MELON_SLICE = new GenericFood("Melon Slice", 360, 2, FoodEffectType.HUNGER);
	public static final ItemMaterial PUMPKIN_SEEDS = new GenericItem("Pumpkin Seeds", 361);
	public static final ItemMaterial MELON_SEEDS = new GenericItem("Melon Seeds", 362);
	public static final ItemMaterial RAW_BEEF = new GenericFood("Raw Beef", 363, 3, FoodEffectType.HUNGER);
	public static final ItemMaterial STEAK = new GenericFood("Steak", 364, 8, FoodEffectType.HUNGER);
	public static final ItemMaterial RAW_CHICKEN = new GenericFood("Raw Chicken", 365, 2, FoodEffectType.HUNGER);
	public static final ItemMaterial COOKED_CHICKEN = new GenericFood("Cooked Chicken", 366, 6, FoodEffectType.HUNGER);
	public static final ItemMaterial ROTTEN_FLESH = new GenericFood("Rotten Flesh", 367, 4, FoodEffectType.HUNGER);
	public static final ItemMaterial ENDER_PEARL = new GenericItem("Ender Pearl", 368);
	public static final ItemMaterial BLAZE_ROD = new GenericItem("Blaze Rod", 369);
	public static final ItemMaterial GHAST_TEAR = new GenericItem("Ghast Tear", 370);
	public static final ItemMaterial GOLD_NUGGET = new GenericItem("Gold Nugget", 371);
	public static final ItemMaterial NETHER_WART = new GenericItem("Nether Wart", 372);
	public static final ItemMaterial GLASS_BOTTLE = new GenericItem("Glass Bottle", 374);
	public static final ItemMaterial SPIDER_EYE = new GenericFood("Spider Eye", 375, 2, FoodEffectType.HUNGER);
	public static final ItemMaterial FERMENTED_SPIDER_EYE = new GenericItem("Fermented Spider Eye", 376);
	public static final ItemMaterial BLAZE_POWDER = new GenericItem("Blaze Powder", 377);
	public static final ItemMaterial MAGMA_CREAM = new GenericItem("Magma Cream", 378);
	public static final ItemMaterial BREWING_STAND = new GenericBlockItem("Brewing Stand", 379, VanillaMaterials.BREWING_STAND_BLOCK);
	public static final ItemMaterial CAULDRON = new GenericBlockItem("Cauldron", 380, VanillaMaterials.CAULDRON_BLOCK);
	public static final ItemMaterial EYE_OF_ENDER = new GenericItem("Eye of Ender", 381);
	public static final ItemMaterial GLISTERING_MELON = new GenericItem("Glistering Melon", 382);
//	public static final ItemMaterial SPAWN_EGG = new SpawnEgg("Spawn Creeper", 383, 50);
//	public static final ItemMaterial CREEPER_SPAWN_EGG = new SpawnEgg("Spawn Creeper", 383, 50);
//	public static final ItemMaterial SKELETON_SPAWN_EGG = new SpawnEgg("Spawn Skeleton", 383, 51);
//	public static final ItemMaterial SPIDER_SPAWN_EGG = new SpawnEgg("Spawn Spider", 383, 52);
//	public static final ItemMaterial ZOMBIE_SPAWN_EGG = new SpawnEgg("Spawn Zombie", 383, 54);
//	public static final ItemMaterial SLIME_SPAWN_EGG = new SpawnEgg("Spawn Slime", 383, 55);
//	public static final ItemMaterial GHAST_SPAWN_EGG = new SpawnEgg("Spawn Ghast", 383, 56);
//	public static final ItemMaterial PIGMAN_SPAWN_EGG = new SpawnEgg("Spawn Pigman", 383, 57);
//	public static final ItemMaterial ENDERMAN_SPAWN_EGG = new SpawnEgg("Spawn Enderman", 383, 58);
//	public static final ItemMaterial CAVESPIDER_SPAWN_EGG = new SpawnEgg("Spawn Cavespider", 383, 59);
//	public static final ItemMaterial SILVERFISH_SPAWN_EGG = new SpawnEgg("Spawn Silverfish", 383, 60);
//	public static final ItemMaterial BLAZE_SPAWN_EGG = new SpawnEgg("Spawn Blaze", 383, 61);
//	public static final ItemMaterial MAGMACUBE_SPAWN_EGG = new SpawnEgg("Spawn Magmacube", 383, 62);
//	public static final ItemMaterial PIG_SPAWN_EGG = new SpawnEgg("Spawn Pig", 383, 90);
//	public static final ItemMaterial SHEEP_SPAWN_EGG = new SpawnEgg("Spawn Sheep", 383, 91);
//	public static final ItemMaterial COW_SPAWN_EGG = new SpawnEgg("Spawn Cow", 383, 92);
//	public static final ItemMaterial CHICKEN_SPAWN_EGG = new SpawnEgg("Spawn Chicken", 383, 93);
//	public static final ItemMaterial SQUID_SPAWN_EGG = new SpawnEgg("Spawn Squid", 383, 94);
//	public static final ItemMaterial WOLF_SPAWN_EGG = new SpawnEgg("Spawn Wolf", 383, 95);
//	public static final ItemMaterial MOOSHROOM_SPAWN_EGG = new SpawnEgg("Spawn Mooshroom", 383, 96);
//	public static final ItemMaterial VILLAGER_SPAWN_EGG = new SpawnEgg("Spawn Villager", 383, 120);
//	public static final ItemMaterial OCELOT_SPAWN_EGG = new SpawnEgg("Spawn Ocelot", 383, 98);
	public static final ItemMaterial BOTTLE_O_ENCHANTING = new GenericItem("Bottle o' Enchanting", 384);
	public static final ItemMaterial FIRE_CHARGE = new GenericBlockItem("Fire Charge", 385, VanillaMaterials.FIRE); //Basic Implementation
	public static final ItemMaterial GOLD_MUSIC_DISC = new GenericItem("Music Disc", 2256);
	public static final ItemMaterial GREEN_MUSIC_DISC = new GenericItem("Music Disc", 2257);
	public static final ItemMaterial ORANGE_MUSIC_DISC = new GenericItem("Music Disc", 2258);
	public static final ItemMaterial RED_MUSIC_DISC = new GenericItem("Music Disc", 2259);
	public static final ItemMaterial CYAN_MUSIC_DISC = new GenericItem("Music Disc", 2260);
	public static final ItemMaterial BLUE_MUSIC_DISC = new GenericItem("Music Disc", 2261);
	public static final ItemMaterial PURPLE_MUSIC_DISC = new GenericItem("Music Disc", 2262);
	public static final ItemMaterial BLACK_MUSIC_DISC = new GenericItem("Music Disc", 2263);
	public static final ItemMaterial WHITE_MUSIC_DISC = new GenericItem("Music Disc", 2264);
	public static final ItemMaterial FOREST_GREEN_MUSIC_DISC = new GenericItem("Music Disc", 2265);
	public static final ItemMaterial BROKEN_MUSIC_DISC = new GenericItem("Music Disc", 2266);
	public static final Potion POTION = Potion.PARENT;
	
	protected static void initialize() {
		((GenericBlock) VanillaMaterials.STONE).setDrop(VanillaMaterials.COBBLESTONE);
		((GenericBlock) VanillaMaterials.GRASS).setDrop(VanillaMaterials.DIRT);
		((GenericBlock) VanillaMaterials.COAL_ORE).setDrop(VanillaMaterials.COAL);
		((GenericBlock) VanillaMaterials.GLASS).setDrop(VanillaMaterials.AIR);
		((GenericBlock) VanillaMaterials.LAPIS_ORE).setDrop(VanillaMaterials.DYE.LAPIS_LAZULI);
		((GenericBlock) VanillaMaterials.BED_BLOCK).setDrop(VanillaMaterials.BED);
		((GenericBlock) VanillaMaterials.WEB).setDrop(VanillaMaterials.STRING);
		VanillaMaterials.TALL_GRASS.DEAD_GRASS.setDrop(VanillaMaterials.AIR);
		VanillaMaterials.TALL_GRASS.TALL_GRASS.setDrop(VanillaMaterials.AIR);
		VanillaMaterials.TALL_GRASS.FERN.setDrop(VanillaMaterials.AIR);
		((GenericBlock) VanillaMaterials.DEAD_BUSH).setDrop(VanillaMaterials.AIR);
		VanillaMaterials.DOUBLE_SLABS.STONE.setSlabMaterial(VanillaMaterials.SLAB.STONE).setDropCount(2);
		VanillaMaterials.DOUBLE_SLABS.SANDSTONE.setSlabMaterial(VanillaMaterials.SLAB.SANDSTONE).setDropCount(2);
		VanillaMaterials.DOUBLE_SLABS.WOOD.setSlabMaterial(VanillaMaterials.SLAB.WOOD).setDropCount(2);
		VanillaMaterials.DOUBLE_SLABS.COBBLESTONE.setSlabMaterial(VanillaMaterials.SLAB.COBBLESTONE).setDropCount(2);
		VanillaMaterials.DOUBLE_SLABS.BRICK.setSlabMaterial(VanillaMaterials.SLAB.BRICK).setDropCount(2);
		VanillaMaterials.DOUBLE_SLABS.STONE_BRICK.setSlabMaterial(VanillaMaterials.SLAB.STONE_BRICK).setDropCount(2);
		((GenericBlock) VanillaMaterials.BOOKSHELF).setDrop(VanillaMaterials.BOOK).setDropCount(3);
		((GenericBlock) VanillaMaterials.FIRE).setDrop(VanillaMaterials.AIR);
		((GenericBlock) VanillaMaterials.MONSTER_SPAWNER).setDrop(VanillaMaterials.AIR);
		((GenericBlock) VanillaMaterials.REDSTONE_WIRE).setDrop(VanillaMaterials.REDSTONE);
		((GenericBlock) VanillaMaterials.DIAMOND_ORE).setDrop(VanillaMaterials.DIAMOND);
		((GenericBlock) VanillaMaterials.WHEATCROP).setDrop(VanillaMaterials.WHEAT);
		((GenericBlock) VanillaMaterials.FARMLAND).setDrop(VanillaMaterials.DIRT);
		((GenericBlock) VanillaMaterials.BURNINGFURNACE).setDrop(VanillaMaterials.FURNACE);
		((GenericBlock) VanillaMaterials.SIGN_POST).setDrop(VanillaMaterials.SIGN);
		((GenericBlock) VanillaMaterials.WOODEN_DOOR_BLOCK).setDrop(VanillaMaterials.WOODEN_DOOR);
		((GenericBlock) VanillaMaterials.WALL_SIGN).setDrop(VanillaMaterials.SIGN);
		((GenericBlock) VanillaMaterials.REDSTONE_ORE).setDrop(VanillaMaterials.REDSTONE);
		((GenericBlock) VanillaMaterials.GLOWING_REDSTONE_ORE).setDrop(VanillaMaterials.REDSTONE);
		((GenericBlock) VanillaMaterials.REDSTONE_TORCH_OFF).setDrop(VanillaMaterials.REDSTONE_TORCH_ON);
		((GenericBlock) VanillaMaterials.SNOW).setDrop(VanillaMaterials.SNOWBALL); // TODO: Make shovels drop snowballs
		((GenericBlock) VanillaMaterials.ICE).setDrop(VanillaMaterials.AIR);
		((GenericBlock) VanillaMaterials.CLAY_BLOCK).setDrop(VanillaMaterials.CLAY).setDropCount(4);
		((GenericBlock) VanillaMaterials.GLOWSTONE_BLOCK).setDrop(VanillaMaterials.GLOWSTONE_DUST);
		((GenericBlock) VanillaMaterials.PORTAL).setDrop(VanillaMaterials.AIR);
		((GenericBlock) VanillaMaterials.CAKE_BLOCK).setDrop(VanillaMaterials.AIR);
		((GenericBlock) VanillaMaterials.REDSTONE_REPEATER_OFF).setDrop(VanillaMaterials.REDSTONE_REPEATER);
		((GenericBlock) VanillaMaterials.REDSTONE_REPEATER_ON).setDrop(VanillaMaterials.REDSTONE_REPEATER);
		((GenericBlock) VanillaMaterials.SILVERFISH_STONE).setDrop(VanillaMaterials.STONE); // TODO: Get drop item based on data
		((GenericBlock) VanillaMaterials.HUGE_BROWN_MUSHROOM).setDrop(VanillaMaterials.BROWN_MUSHROOM);
		((GenericBlock) VanillaMaterials.HUGE_RED_MUSHROOM).setDrop(VanillaMaterials.RED_MUSHROOM);
		((GenericBlock) VanillaMaterials.GLASS_PANE).setDrop(VanillaMaterials.AIR);
		((GenericBlock) VanillaMaterials.MELON).setDrop(VanillaMaterials.MELON_SLICE);
		((GenericBlock) VanillaMaterials.PUMPKIN_STEM).setDrop(VanillaMaterials.AIR);
		((GenericBlock) VanillaMaterials.MELON_STEM).setDrop(VanillaMaterials.MELON_SEEDS);
		((GenericBlock) VanillaMaterials.VINES).setDrop(VanillaMaterials.AIR);
		((GenericBlock) VanillaMaterials.MYCELIUM).setDrop(VanillaMaterials.DIRT);
		((GenericBlock) VanillaMaterials.END_PORTAL).setDrop(VanillaMaterials.AIR);
		((GenericBlock) VanillaMaterials.END_PORTAL_FRAME).setDrop(VanillaMaterials.AIR);
		((GenericBlock) VanillaMaterials.REDSTONE_LAMP_ON).setDrop(VanillaMaterials.REDSTONE_LAMP_OFF);
		
		/*
		//TODO: Not needed?
		Field[] fields = VanillaMaterials.class.getFields();
		for (Field f : fields) {
			if (Modifier.isStatic(f.getModifiers())) {
				try {
					Object value = f.get(null);
					if (value instanceof Material) {
						Material.registerMaterial((Material) value);
					}
				} catch (IllegalArgumentException e) {
				} catch (IllegalAccessException e) {
				}
			}
		}
		*/
	}
}
