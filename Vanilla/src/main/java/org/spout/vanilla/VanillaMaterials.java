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
 * the MIT license and the SpoutDev license version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license,
 * including the MIT license.
 */
package main.java.org.spout.vanilla;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.ItemMaterial;
import org.spout.api.material.Material;
import org.spout.api.material.MaterialData;
import org.spout.vanilla.material.Food.FoodEffectType;
import org.spout.vanilla.material.attachable.GroundAttachable;
import org.spout.vanilla.material.attachable.WallAttachable;
import org.spout.vanilla.material.block.Air;
import org.spout.vanilla.material.block.Cactus;
import org.spout.vanilla.material.block.DoubleSlab;
import org.spout.vanilla.material.block.Grass;
import org.spout.vanilla.material.block.LongGrass;
import org.spout.vanilla.material.block.Sapling;
import org.spout.vanilla.material.block.Slab;
import org.spout.vanilla.material.block.Solid;
import org.spout.vanilla.material.block.StoneBrick;
import org.spout.vanilla.material.block.Tree;
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
import org.spout.vanilla.material.item.Dye;
import org.spout.vanilla.material.item.RedstoneTorch;
import org.spout.vanilla.material.item.RedstoneWire;

public final class VanillaMaterials {

	public static final BlockMaterial AIR = new Air("Air");
	public static final BlockMaterial STONE = new Solid("Stone", 1).setHardness(1.5F).setResistance(10.0F);
	public static final BlockMaterial GRASS = new Grass("Grass").setHardness(0.6F).setResistance(0.8F);
	public static final BlockMaterial DIRT = new Solid("Dirt", 3).setHardness(0.5F).setResistance(0.8F);
	public static final BlockMaterial COBBLESTONE = new Solid("Cobblestone", 4).setHardness(2.0F).setResistance(10.0F);
	public static final BlockMaterial WOOD = new Solid("Wooden Planks", 5).setHardness(2.0F).setResistance(5.0F);
	public static final BlockMaterial SAPLING = new Sapling("Sapling", 0).setHardness(0.0F).setResistance(0.0F);
	public static final BlockMaterial SPRUCE_SAPLING = new Sapling("Spruce Sapling", 1).setHardness(0.0F).setResistance(0.0F);
	public static final BlockMaterial BIRCH_SAPLING = new Sapling("Birch Sapling", 2).setHardness(0.0F).setResistance(0.0F);
	//public static final BlockMaterial JUNGLE_SAPLING = new Sapling("Jungle Sapling", 3).setHardness(0.0F).setResistance(0.0F);
	public static final BlockMaterial BEDROCK = new Solid("Bedrock", 7).setResistance(6000000.0F);
	public static final BlockMaterial WATER = new GenericLiquid("Water", 8, true).setHardness(100.0F).setResistance(166.7F);
	public static final BlockMaterial STATIONARY_WATER = new GenericLiquid("Stationary Water", 9, false).setHardness(100.0F).setResistance(166.7F);
	public static final BlockMaterial LAVA = new GenericLiquid("Lava", 10, true).setHardness(0.0F).setLightLevel(1).setResistance(0.0F);
	public static final BlockMaterial STATIONARY_LAVA = new GenericLiquid("Stationary Lava", 11, false).setHardness(100.0F).setLightLevel(1).setResistance(166.7F);
	public static final BlockMaterial SAND = new Solid("Sand", 12, true).setHardness(0.5F).setResistance(0.8F);
	public static final BlockMaterial GRAVEL = new Solid("Gravel", 13, true).setHardness(0.6F).setResistance(1.0F);
	public static final BlockMaterial GOLD_ORE = new Solid("Gold Ore", 14).setHardness(3.0F).setResistance(5.0F);
	public static final BlockMaterial IRON_ORE = new Solid("Iron Ore", 15).setHardness(3.0F).setResistance(5.0F);
	public static final BlockMaterial COAL_ORE = new Solid("Coal Ore", 16).setHardness(3.0F).setResistance(5.0F);
	public static final BlockMaterial LOG = new Tree("Wood", 17, 0).setHardness(2.0F).setResistance(3.3F);
	public static final BlockMaterial SPRUCE_LOG = new Tree("Wood", 17, 1).setHardness(0.2F).setResistance(3.3F);
	public static final BlockMaterial BIRCH_LOG = new Tree("Wood", 17, 2).setHardness(0.2F).setResistance(3.3F);
	//public static final BlockMaterial JUNGLE_LOG = new Tree("Wood", 17, 3).setHardness(0.2F).setResistance(3.3F);
	public static final BlockMaterial LEAVES = new Tree("Leaves", 18, 0).setHardness(0.2F).setResistance(3.3F);
	public static final BlockMaterial SPRUCE_LEAVES = new Tree("Leaves", 18, 1).setHardness(0.2F).setResistance(0.3F);
	public static final BlockMaterial BIRCH_LEAVES = new Tree("Leaves", 18, 2).setHardness(0.2F).setResistance(0.3F);
	//public static final BlockMaterial JUNGLE_LEAVES = new Tree("Leaves", 18, 3).setHardness(0.2F).setResistance(0.3F);
	public static final BlockMaterial SPONGE = new Solid("Sponge", 19).setHardness(0.6F).setResistance(1.0F);
	public static final BlockMaterial GLASS = new Solid("Glass", 20).setHardness(0.3F).setResistance(0.5F);
	public static final BlockMaterial LAPIS_ORE = new Solid("Lapis Lazuli Ore", 21).setHardness(3.0F).setResistance(5.0F);
	public static final BlockMaterial LAPIS_BLOCK = new Solid("Lapis Lazuli Block", 22).setHardness(3.0F).setResistance(5.0F);
	public static final BlockMaterial DISPENSER = new Solid("Dispenser", 23).setHardness(3.5F).setResistance(5.8F);
	public static final BlockMaterial SANDSTONE = new Solid("Sandstone", 24).setHardness(0.8F).setResistance(1.3F);
	public static final BlockMaterial NOTEBLOCK = new Solid("Note Block", 25).setHardness(0.8F).setResistance(1.3F);
	public static final BlockMaterial BED_BLOCK = new Solid("Bed", 26).setHardness(0.2F);.setResistance(0.3F);
	public static final BlockMaterial POWERED_RAIL = new Solid("Powered Rail", 27).setHardness(0.7F).setResistance(1.2F);
	public static final BlockMaterial DETECTOR_RAIL = new Solid("Detector Rail", 28).setHardness(0.7F).setResistance(1.2F);
	public static final BlockMaterial PISTON_STICKY_BASE = new Solid("Sticky Piston", 29).setResistance(0.8F);
	public static final BlockMaterial WEB = new Solid("Cobweb", 30).setHardness(4.0F).setResistance(20F);
	public static final BlockMaterial DEAD_SHRUB = new LongGrass("Dead Grass", 31, 0).setHardness(0.0F);
	public static final BlockMaterial TALL_GRASS = new LongGrass("Tall Grass", 31, 1).setHardness(0.0F);
	public static final BlockMaterial FERN = new LongGrass("Fern", 31, 2).setHardness(0.0F);
	public static final BlockMaterial DEAD_BUSH = new LongGrass("Dead Shrubs", 32, 0).setHardness(0.0F);
	public static final BlockMaterial PISTON_BASE = new Solid("Piston", 33).setResistance(0.8F);
	public static final BlockMaterial PISTON_EXTENSION = new Solid("Piston (Head)", 34).setResistance(0.8F);
	public static final BlockMaterial WHITE_WOOL = new Wool("Wool", 35, 0).setHardness(0.8F).setResistance(1.3F);
	public static final BlockMaterial ORANGE_WOOL = new Wool("Orange Wool", 35, 1).setHardness(0.8F).setResistance(1.3F);
	public static final BlockMaterial MAGENTA_WOOL = new Wool("Magenta Wool", 35, 2).setHardness(0.8F).setResistance(1.3F);
	public static final BlockMaterial LIGHT_BLUE_WOOL = new Wool("Light Blue Wool", 35, 3).setHardness(0.8F).setResistance(1.3F);
	public static final BlockMaterial YELLOW_WOOL = new Wool("Yellow Wool", 35, 4).setHardness(0.8F).setResistance(1.3F);
	public static final BlockMaterial LIME_WOOL = new Wool("Light Green Wool", 35, 5).setHardness(0.8F).setResistance(1.3F);
	public static final BlockMaterial PINK_WOOL = new Wool("Pink Wool", 35, 6).setHardness(0.8F).setResistance(1.3F);
	public static final BlockMaterial GREY_WOOL = new Wool("Grey Wool", 35, 7).setHardness(0.8F).setResistance(1.3F);
	public static final BlockMaterial LIGHT_GREY_WOOL = new Wool("Light Grey Wool", 35, 8).setHardness(0.8F).setResistance(1.3F);
	public static final BlockMaterial CYAN_WOOL = new Wool("Cyan Wool", 35, 9).setHardness(0.8F).setResistance(1.3F);
	public static final BlockMaterial PURPLE_WOOL = new Wool("Purple Wool", 35, 10).setHardness(0.8F).setResistance(1.3F);
	public static final BlockMaterial BLUE_WOOL = new Wool("Blue Wool", 35, 11).setHardness(0.8F).setResistance(1.3F);
	public static final BlockMaterial BROWN_WOOL = new Wool("Brown Wool", 35, 12).setHardness(0.8F).setResistance(1.3F);
	public static final BlockMaterial GREEN_WOOL = new Wool("Dark Green Wool", 35, 13).setHardness(0.8F).setResistance(1.3F);
	public static final BlockMaterial RED_WOOL = new Wool("Red Wool", 35, 14).setHardness(0.8F).setResistance(1.3F);
	public static final BlockMaterial BLACK_WOOL = new Wool("Black Wool", 35, 15).setHardness(0.8F).setResistance(1.3F);
	public static final BlockMaterial MOVED_BY_PISTON = new Solid("Moved By Piston", 36).setResistance(0.0F);
	public static final BlockMaterial DANDELION = new GroundAttachable("Dandelion", 37).setHardness(0.0F).setResistance(0.0F);
	public static final BlockMaterial ROSE = new GroundAttachable("Rose", 38).setHardness(0.0F).setResistance(0.0F);
	public static final BlockMaterial BROWN_MUSHROOM = new GroundAttachable("Brown Mushroom", 39).setHardness(0.0F).setResistance(0.0F);
	public static final BlockMaterial RED_MUSHROOM = new GroundAttachable("Red Mushroom", 40).setHardness(0.0F).setResistance(0.0F);
	public static final BlockMaterial GOLD_BLOCK = new Solid("Gold Block", 41).setHardness(3.0F).setResistance(10.0F);
	public static final BlockMaterial IRON_BLOCK = new Solid("Iron Block", 42).setHardness(5.0F).setResistance(10.0F);
	public static final BlockMaterial STONE_DOUBLE_SLABS = new DoubleSlab("Stone Double Slab", 43, 0).setHardness(2.0F).setResistance(10.0F);
	public static final BlockMaterial SANDSTONE_DOUBLE_SLABS = new DoubleSlab("Sandstone Double Slab", 43, 1).setHardness(2.0F).setResistance(10.0F);
	public static final BlockMaterial WOODEN_DOUBLE_SLABS = new DoubleSlab("Wooden Double Slab", 43, 2).setHardness(2.0F).setResistance(10.0F);
	public static final BlockMaterial COBBLESTONE_DOUBLE_SLABS = new DoubleSlab("Stone Double Slab", 43, 3).setHardness(2.0F).setResistance(10.0F);
	public static final BlockMaterial BRICK_DOUBLE_SLABS = new DoubleSlab("Brick Double Slab", 43, 4).setHardness(2.0F).setResistance(10.0F);
	public static final BlockMaterial STONE_BRICK_DOUBLE_SLABS = new DoubleSlab("Stone Brick Double Slab", 43, 5).setHardness(2.0F).setResistance(10.0F);
	public static final BlockMaterial STONE_SLAB = new Slab("Stone Slab", 44, 0).setHardness(2.0F).setResistance(10.0F);
	public static final BlockMaterial SANDSTONE_SLAB = new Slab("Sandstone Slab", 44, 1).setHardness(2.0F).setResistance(10.0F);
	public static final BlockMaterial WOODEN_SLAB = new Slab("Wooden Slab", 44, 2).setHardness(2.0F).setResistance(10.0F);
	public static final BlockMaterial COBBLESTONE_SLAB = new Slab("Stone Slab", 44, 3).setHardness(2.0F).setResistance(10.0F);
	public static final BlockMaterial BRICK_SLAB = new Slab("Brick Slab", 44, 4).setHardness(2.0F).setResistance(10.0F);
	public static final BlockMaterial STONE_BRICK_SLAB = new Slab("Stone Brick Slab", 44, 5).setHardness(2.0F).setResistance(10.0F);
	public static final BlockMaterial BRICK = new Solid("Brick Block", 45).setHardness(2.0F).setResistance(10.0F);
	public static final BlockMaterial TNT = new Solid("TNT", 46).setHardness(0.0F);.setResistance(0.0F);
	public static final BlockMaterial BOOKSHELF = new Solid("Bookshelf", 47).setHardness(1.5F).setResistance(2.5F);
	public static final BlockMaterial MOSS_STONE = new Solid("Moss Stone", 48).setHardness(2.0F).setResistance(10.0F).setResistance(10.0F);
	public static final BlockMaterial OBSIDIAN = new Solid("Obsidian", 49).setHardness(50.0F).setResistance(2000.0F)
	public static final BlockMaterial TORCH = new WallAttachable("Torch", 50).setHardness(0.0F).setResistance(0.0F);
	public static final BlockMaterial FIRE = new Solid("Fire", 51).setHardness(0.0F).setResistance(0.0F);
	public static final BlockMaterial MONSTER_SPAWNER = new Solid("Monster Spawner", 52).setHardness(5.0F).setResistance(8.3F);
	public static final BlockMaterial WOODEN_STAIRS = new Solid("Wooden Stairs", 53);.setResistance(3.0F);
	public static final BlockMaterial CHEST = new Solid("Chest", 54).setHardness(2.5F).setResistance(4.2F);
	public static final BlockMaterial REDSTONE_WIRE = new RedstoneWire("Redstone Wire", 55, 0).setHardness(0.0F);.setResistance(0.0F);
	public static final BlockMaterial DIAMOND_ORE = new Solid("Diamond Ore", 56).setHardness(3.0F).setResistance(5.0F);
	public static final BlockMaterial DIAMOND_BLOCK = new Solid("Diamond Block", 57).setHardness(5.0F).setResistance(10.0F);
	public static final BlockMaterial CRAFTING_TABLE = new Solid("Crafting Table", 58).setHardness(4.2F);
	public static final BlockMaterial CROPS = new Solid("Seeds", 59).setHardness(0.0F);.setResistance(0.0F);
	public static final BlockMaterial FARMLAND = new Solid("Farmland", 60).setHardness(0.6F).setResistance(1.0F);
	public static final BlockMaterial FURNACE = new Solid("Furnace", 61).setHardness(3.5F).setResistance(5.8F);
	public static final BlockMaterial BURNINGFURNACE = new Solid("Burning Furnace", 62).setHardness(3.5F)..setResistance(5.8F);
	public static final BlockMaterial SIGN_POST = new Solid("Sign Post", 63).setHardness(1.0F).setResistance(1.6F);
	public static final BlockMaterial WOODEN_DOOR_BLOCK = new Solid("Wooden Door", 64).setHardness(3.0F);
	public static final BlockMaterial LADDERS = new Solid("Ladders", 65).setHardness(0.4F).setResistance(0.7F);
	public static final BlockMaterial RAILS = new Solid("Rails", 66).setHardness(0.7F).setResistance(1.2F);
	public static final BlockMaterial COBBLESTONE_STAIRS = new Solid("Cobblestone Stairs", 67).setResistance(10.0F);
	public static final BlockMaterial WALL_SIGN = new Solid("Wall Sign", 68).setHardness(1.0F);
	public static final BlockMaterial LEVER = new Solid("Lever", 69).setHardness(0.5F).setResistance(1.7F);
	public static final BlockMaterial STONE_PRESSURE_PLATE = new Solid("Stone Pressure Plate", 70).setHardness(0.5F).setResistance(0.8F);
	public static final BlockMaterial IRON_DOOR_BLOCK = new Solid("Iron Door", 71).setHardness(5.0F).setResistance(8.3F);
	public static final BlockMaterial WOODEN_PRESSURE_PLATE = new Solid("Wooden Pressure Plate", 72).setHardness(0.5F).setResistance(0.8F);
	public static final BlockMaterial REDSTONE_ORE = new Solid("Redstone Ore", 73).setHardness(3.0F).setResistance(5.0F);
	public static final BlockMaterial GLOWING_REDSTONE_ORE = new Solid("Glowing Redstone Ore", 74).setHardness(3.0F).setResistance(5.0F);
	public static final BlockMaterial REDSTONE_TORCH_OFF = new RedstoneTorch("Redstone Torch", 75, false).setHardness(0.0F).setResistance(0.0F);
	public static final BlockMaterial REDSTONE_TORCH_ON = new RedstoneTorch("Redstone Torch (On)", 76, true).setHardness(0.0F).setResistance(0.0F);
	public static final BlockMaterial STONE_BUTTON = new Solid("Stone Button", 77).setHardness(0.5F).setResistance(0.8F);
	public static final BlockMaterial SNOW = new Solid("Snow", 78).setHardness(0.1F).setResistance(0.2F);
	public static final BlockMaterial ICE = new Solid("Ice", 79).setHardness(0.5F).setResistance(0.8F);
	public static final BlockMaterial SNOW_BLOCK = new Solid("Snow Block", 80).setHardness(0.2F).setResistance(0.3F);
	public static final BlockMaterial CACTUS = new Cactus("Cactus", 81).setHardness(0.4F).setResistance(0.7F);
	public static final BlockMaterial CLAY_BLOCK = new Solid("Clay Block", 82).setHardness(0.6F).setResistance(1.0F);
	public static final BlockMaterial SUGAR_CANE_BLOCK = new Solid("Sugar Cane", 83).setHardness(0.0F).setResistance(0.0F);
	public static final BlockMaterial JUKEBOX = new Solid("Jukebox", 84).setHardness(2.0F).setResistance(10.0F);
	public static final BlockMaterial FENCE = new Solid("Fence", 85).setResistance(5.0F).setResistance(5.0F);
	public static final BlockMaterial PUMPKIN = new Solid("Pumpkin", 86).setHardness(1.0F).setResistance(1.7F);
	public static final BlockMaterial NETHERRACK = new Solid("Netherrack", 87).setHardness(0.7F);
	public static final BlockMaterial SOUL_SAND = new Solid("Soul Sand", 88).setHardness(0.5F).setResistance(0.8F);
	public static final BlockMaterial GLOWSTONE_BLOCK = new Solid("Glowstone Block", 89).setHardness(0.3F).setResistance(0.5F);
	public static final BlockMaterial PORTAL = new Solid("Portal", 90).setHardness(-1.0F).setResistance(0.0F);
	public static final BlockMaterial JACK_O_LANTERN = new Solid("Jack 'o' Lantern", 91).setHardness(1.0F).setResistance(1.7F);
	public static final BlockMaterial CAKE_BLOCK = new Solid("Cake Block", 92).setHardness(0.5F).setResistance(0.8F);
	public static final BlockMaterial REDSTONE_REPEATER_OFF = new Solid("Redstone Repeater", 93).setHardness(0.0F).setResistance(0.0F);
	public static final BlockMaterial REDSTONE_REPEATER_ON = new Solid("Redstone Repeater (On)", 94).setHardness(0.0F).setResistance(0.0F);
	public static final BlockMaterial LOCKED_CHEST = new Solid("Locked Chest", 95).setHardness(0.0F).setResistance(0.0F);
	public static final BlockMaterial TRAPDOOR = new Solid("Trapdoor", 96).setHardness(3.0F).setResistance(5.0F);
	public static final BlockMaterial SILVERFISH_STONE = new Solid("Silverfish Stone", 97).setHardness(0.75F).setResistance(10.0F); //Placeholder, block resistance unknown
	public static final BlockMaterial STONE_BRICKS = new StoneBrick("Stone Brick", 98, 0).setHardness(1.5F).setResistance(10.0F);
	public static final BlockMaterial MOSSY_STONE_BRICKS = new StoneBrick("Mossy Stone Brick", 98, 1).setHardness(1.5F).setResistance(10.0F);
	public static final BlockMaterial CRACKED_STONE_BRICKS = new StoneBrick("Cracked Stone Brick", 98, 2).setHardness(1.5F).setResistance(10.0F);
	//public static final BlockMaterial CIRCLE_STONE_BRICKS = new StoneBrick("Circle Stone Brick", 98, 3).setHardness(1.5F).setResistance(10.0F);
	public static final BlockMaterial HUGE_BROWN_MUSHROOM = new Solid("Huge Brown Mushroom", 99).setHardness(0.2F).setResistance(0.3F); //Placeholder, block resistance unknown
	public static final BlockMaterial HUGE_RED_MUSHROOM = new Solid("Huge Red Mushroom", 100).setHardness(0.2F).setResistance(0.3F); //Placeholder, block resistance unknown
	public static final BlockMaterial IRON_BARS = new Solid("Iron Bars", 101).setHardness(5.0F).setResistance(10.0F);
	public static final BlockMaterial GLASS_PANE = new Solid("Glass Pane", 102).setHardness(0.3F).setResistance(0.3F); //Placeholder, block resistance unknown
	public static final BlockMaterial WATERMELON = new Solid("Watermelon", 103).setHardness(1.0F).setResistance(1.7F);
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
	public static final BlockMaterial BREWING_STAND_BLOCK = new Solid("Brewing Stand", 117).setHardness(0.5F).setResistance(0.8F);
	public static final BlockMaterial CAULDRON_BLOCK = new Solid("Cauldron", 118).setHardness(2.0F).setResistance(3.3F);
	public static final BlockMaterial END_PORTAL = new Solid("End Portal", 119).setHardness(-1.0F).setResistance(6000000.0F);;
	public static final BlockMaterial END_PORTAL_FRAME = new Solid("End Portal Frame", 120).setHardness(-1.0F);
	public static final BlockMaterial END_STONE = new Solid("End Stone", 121).setHardness(3.0F).setResistance(15.0F);
	public static final BlockMaterial DRAGON_EGG = new Solid("Dragon Egg", 122).setHardness(3.0F).setResistance(15.0F);
	/*
	 * Items
	 */
	public static final ItemMaterial IRON_SHOVEL = new GenericTool("Iron Shovel", 256, (short) 251);
	public static final ItemMaterial IRON_PICKAXE = new GenericTool("Iron Pickaxe", 257, (short) 251);
	public static final ItemMaterial IRON_AXE = new GenericTool("Iron Axe", 258, (short) 251);
	public static final ItemMaterial FLINT_AND_STEEL = new GenericTool("Flint and Steel", 259, (short) 64);
	public static final ItemMaterial RED_APPLE = new GenericFood("Apple", 260, 4, FoodEffectType.HUNGER);
	public static final ItemMaterial BOW = new GenericRangedWeapon("Bow", 261, 1, 9, (short) 385);
	public static final ItemMaterial ARROW = new GenericItem("Arrow", 262);
	public static final ItemMaterial COAL = new Coal("Coal", 263, 0);
	public static final ItemMaterial CHARCOAL = new Coal("Charcoal", 263, 1);
	public static final ItemMaterial DIAMOND = new GenericItem("Diamond", 264);
	public static final ItemMaterial IRON_INGOT = new GenericItem("Iron Ingot", 265);
	public static final ItemMaterial GOLD_INGOT = new GenericItem("Gold Ingot", 266);
	public static final ItemMaterial IRON_SWORD = new GenericWeapon("Iron Sword", 267, 6, (short) 251);
	public static final ItemMaterial WOODEN_SWORD = new GenericWeapon("Wooden Sword", 268, 4, (short) 60);
	public static final ItemMaterial WOODEN_SHOVEL = new GenericTool("Wooden Shovel", 269, (short) 60);
	public static final ItemMaterial WOODEN_PICKAXE = new GenericTool("Wooden Pickaxe", 270, (short) 60);
	public static final ItemMaterial WOODEN_AXE = new GenericTool("Wooden Axe", 271, (short) 60);
	public static final ItemMaterial STONE_SWORD = new GenericWeapon("Stone Sword", 272, 5, (short) 132);
	public static final ItemMaterial STONE_SHOVEL = new GenericTool("Stone Shovel", 273, (short) 132);
	public static final ItemMaterial STONE_PICKAXE = new GenericTool("Stone Pickaxe", 274, (short) 132);
	public static final ItemMaterial STONE_AXE = new GenericTool("Stone Axe", 275, (short) 132);
	public static final ItemMaterial DIAMOND_SWORD = new GenericWeapon("Diamond Sword", 276, 7, (short) 1562);
	public static final ItemMaterial DIAMOND_SHOVEL = new GenericTool("Diamond Shovel", 277, (short) 1562);
	public static final ItemMaterial DIAMOND_PICKAXE = new GenericTool("Diamond Pickaxe", 278, (short) 1562);
	public static final ItemMaterial DIAMOND_AXE = new GenericTool("Diamond Axe", 279, (short) 1562);
	public static final ItemMaterial STICK = new GenericItem("Stick", 280);
	public static final ItemMaterial BOWL = new GenericItem("Bowl", 281);
	public static final ItemMaterial MUSHROOM_SOUP = new GenericFood("Mushroom Soup", 282, 8, FoodEffectType.HUNGER);
	public static final ItemMaterial GOLD_SWORD = new GenericWeapon("Gold Sword", 283, 4, (short) 33);
	public static final ItemMaterial GOLD_SHOVEL = new GenericTool("Gold Shovel", 284, (short) 33);
	public static final ItemMaterial GOLD_PICKAXE = new GenericTool("Gold Pickaxe", 285, (short) 33);
	public static final ItemMaterial GOLD_AXE = new GenericTool("Gold Axe", 286, (short) 33);
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
	public static final ItemMaterial FLINT = new GenericItem("Flint", 318, 0);
	public static final ItemMaterial RAW_PORKCHOP = new GenericFood("Raw Porkchop", 319, 3, FoodEffectType.HUNGER);
	public static final ItemMaterial COOKED_PORKCHOP = new GenericFood("Cooked Porkchop", 320, 8, FoodEffectType.HUNGER);
	public static final ItemMaterial PAINTINGS = new GenericItem("Paintings", 321);
	public static final ItemMaterial GOLDEN_APPLE = new GenericFood("Golden Apple", 322, 10, FoodEffectType.HUNGER);
	public static final ItemMaterial SIGN = new GenericItem("Sign", 323);
	public static final ItemMaterial WOODEN_DOOR = new GenericItem("Wooden Door", 324);
	public static final ItemMaterial BUCKET = new GenericEmptyContainer("Bucket", 325);
	public static final ItemMaterial WATER_BUCKET = new GenericFullContainer("Water Bucket", 326, WATER, (GenericEmptyContainer)BUCKET);
	public static final ItemMaterial LAVA_BUCKET = new GenericFullContainer("Lava Bucket", 327, LAVA, (GenericEmptyContainer)BUCKET);
	public static final ItemMaterial MINECART = new GenericItem("Minecart", 328);
	public static final ItemMaterial SADDLE = new GenericItem("Saddle", 329);
	public static final ItemMaterial IRON_DOOR = new GenericItem("Iron Door", 330);
	public static final ItemMaterial REDSTONE = new GenericBlockItem("Redstone", 331, VanillaMaterials.REDSTONE_WIRE);
	public static final ItemMaterial SNOWBALL = new GenericItem("Snowball", 332);
	public static final ItemMaterial BOAT = new GenericItem("Boat", 333);
	public static final ItemMaterial LEATHER = new GenericItem("Leather", 334);
	public static final ItemMaterial MILK = new GenericItem("Milk", 335);
	public static final ItemMaterial CLAY_BRICK = new GenericItem("Brick", 336);
	public static final ItemMaterial CLAY = new GenericItem("Clay", 337);
	public static final ItemMaterial SUGAR_CANE = new GenericItem("Sugar Cane", 338);
	public static final ItemMaterial PAPER = new GenericItem("Paper", 339);
	public static final ItemMaterial BOOK = new GenericItem("Book", 340);
	public static final ItemMaterial SLIMEBALL = new GenericItem("Slimeball", 341);
	public static final ItemMaterial MINECART_CHEST = new GenericItem("Minecart with Chest", 342);
	public static final ItemMaterial MINECART_FURNACE = new GenericItem("Minecart with Furnace", 343);
	public static final ItemMaterial EGG = new GenericItem("Egg", 344);
	public static final ItemMaterial COMPASS = new GenericItem("Compass", 345);
	public static final ItemMaterial FISHING_ROD = new GenericTool("Fishing Rod", 346, (short) 65);
	public static final ItemMaterial CLOCK = new GenericItem("Clock", 347);
	public static final ItemMaterial GLOWSTONE_DUST = new GenericItem("Glowstone Dust", 348);
	public static final ItemMaterial RAW_FISH = new GenericFood("Raw Fish", 349, 2, FoodEffectType.HUNGER);
	public static final ItemMaterial COOKED_FISH = new GenericFood("Cooked Fish", 350, 5, FoodEffectType.HUNGER);
	public static final ItemMaterial INK_SAC = new Dye("Ink Sac", 351, 0);
	public static final ItemMaterial ROSE_RED = new Dye("Rose Red", 351, 1);
	public static final ItemMaterial CACTUS_GREEN = new Dye("Cactus Green", 351, 2);
	public static final ItemMaterial COCOA_BEANS = new Dye("Cocoa Beans", 351, 3);
	public static final ItemMaterial LAPIS_LAZULI = new Dye("Lapis Lazuli", 351, 4);
	public static final ItemMaterial PURPLE_DYE = new Dye("Purple Dye", 351, 5);
	public static final ItemMaterial CYAN_DYE = new Dye("Cyan Dye", 351, 6);
	public static final ItemMaterial LIGHT_GRAY_DYE = new Dye("Light Gray Dye", 351, 7);
	public static final ItemMaterial GRAY_DYE = new Dye("Gray Dye", 351, 8);
	public static final ItemMaterial PINK_DYE = new Dye("Pink Dye", 351, 9);
	public static final ItemMaterial LIME_DYE = new Dye("Lime Dye", 351, 10);
	public static final ItemMaterial DANDELION_YELLOW = new Dye("Dandelion Yellow", 351, 11);
	public static final ItemMaterial LIGHT_BLUE_DYE = new Dye("Light Blue Dye", 351, 12);
	public static final ItemMaterial MAGENTA_DYE = new Dye("Magenta Dye", 351, 13);
	public static final ItemMaterial ORANGE_DYE = new Dye("Orange Dye", 351, 14);
	public static final ItemMaterial BONE_MEAL = new Dye("Bone Meal", 351, 15);
	public static final ItemMaterial BONE = new GenericItem("Bone", 352);
	public static final ItemMaterial SUGAR = new GenericItem("Sugar", 353);
	public static final ItemMaterial CAKE = new GenericBlockItem("Cake", 354, VanillaMaterials.CAKE_BLOCK);
	public static final ItemMaterial BED = new GenericItem("Bed", 355);
	public static final ItemMaterial REDSTONE_REPEATER = new GenericItem("Redstone Repeater", 356);
	public static final ItemMaterial COOKIE = new GenericFood("Cookie", 357, 1, FoodEffectType.HUNGER);
	public static final ItemMaterial MAP = new GenericItem("Map", 358);
	public static final ItemMaterial SHEARS = new GenericTool("Shears", 359, (short) 238);
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
	public static final ItemMaterial POTION = new GenericItem("Potion", 373);
	public static final ItemMaterial GLASS_BOTTLE = new GenericItem("Glass Bottle", 374);
	public static final ItemMaterial SPIDER_EYE = new GenericFood("Spider Eye", 375, 2, FoodEffectType.HUNGER);
	public static final ItemMaterial FERMENTED_SPIDER_EYE = new GenericItem("Fermented Spider Eye", 376);
	public static final ItemMaterial BLAZE_POWDER = new GenericItem("Blaze Powder", 377);
	public static final ItemMaterial MAGMA_CREAM = new GenericItem("Magma Cream", 378);
	public static final ItemMaterial BREWING_STAND = new GenericBlockItem("Brewing Stand", 379, VanillaMaterials.BREWING_STAND_BLOCK);
	public static final ItemMaterial CAULDRON = new GenericBlockItem("Cauldron", 380, VanillaMaterials.CAULDRON_BLOCK);
	public static final ItemMaterial EYE_OF_ENDER = new GenericItem("Eye of Ender", 381);
	public static final ItemMaterial GLISTERING_MELON = new GenericItem("Glistering Melon", 382);
	public static final ItemMaterial CREEPER_SPAWN_EGG = new GenericItem("Spawn Creeper", 383, 50);
	public static final ItemMaterial SKELETON_SPAWN_EGG = new GenericItem("Spawn Skeleton", 383, 51);
	public static final ItemMaterial SPIDER_SPAWN_EGG = new GenericItem("Spawn Spider", 383, 52);
	public static final ItemMaterial ZOMBIE_SPAWN_EGG = new GenericItem("Spawn Zombie", 383, 54);
	public static final ItemMaterial SLIME_SPAWN_EGG = new GenericItem("Spawn Slime", 383, 55);
	public static final ItemMaterial GHAST_SPAWN_EGG = new GenericItem("Spawn Ghast", 383, 56);
	public static final ItemMaterial PIGMAN_SPAWN_EGG = new GenericItem("Spawn Pigman", 383, 57);
	public static final ItemMaterial ENDERMAN_SPAWN_EGG = new GenericItem("Spawn Enderman", 383, 58);
	public static final ItemMaterial CAVESPIDER_SPAWN_EGG = new GenericItem("Spawn Cavespider", 383, 59);
	public static final ItemMaterial SILVERFISH_SPAWN_EGG = new GenericItem("Spawn Silverfish", 383, 60);
	public static final ItemMaterial BLAZE_SPAWN_EGG = new GenericItem("Spawn Blaze", 383, 61);
	public static final ItemMaterial MAGMACUBE_SPAWN_EGG = new GenericItem("Spawn Magmacube", 383, 62);
	public static final ItemMaterial PIG_SPAWN_EGG = new GenericItem("Spawn Pig", 383, 90);
	public static final ItemMaterial SHEEP_SPAWN_EGG = new GenericItem("Spawn Sheep", 383, 91);
	public static final ItemMaterial COW_SPAWN_EGG = new GenericItem("Spawn Cow", 383, 92);
	public static final ItemMaterial CHICKEN_SPAWN_EGG = new GenericItem("Spawn Chicken", 383, 93);
	public static final ItemMaterial SQUID_SPAWN_EGG = new GenericItem("Spawn Squid", 383, 94);
	public static final ItemMaterial WOLF_SPAWN_EGG = new GenericItem("Spawn Wolf", 383, 95);
	public static final ItemMaterial MOOSHROOM_SPAWN_EGG = new GenericItem("Spawn Mooshroom", 383, 96);
	public static final ItemMaterial VILLAGER_SPAWN_EGG = new GenericItem("Spawn Villager", 383, 120);
	//public static final ItemMaterial OCELOT_SPAWN_EGG = new GenericItem("Spawn Ocelot", 383,98);
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

	protected static void initialize() {
		Field[] fields = VanillaMaterials.class.getFields();
		for (Field f : fields) {
			if (Modifier.isStatic(f.getModifiers())) {
				try {
					Object value = f.get(null);
					if (value instanceof Material) {
						MaterialData.registerMaterial((Material) value);
					}
				} catch (IllegalArgumentException e) {
				} catch (IllegalAccessException e) {
				}
			}
		}
	}
}