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

import org.spout.vanilla.material.block.GroundAttachable;
import org.spout.vanilla.material.block.Liquid;
import org.spout.vanilla.material.block.Ore;
import org.spout.vanilla.material.block.Solid;
import org.spout.vanilla.material.block.gemblocks.DiamondBlock;
import org.spout.vanilla.material.block.gemblocks.GoldBlock;
import org.spout.vanilla.material.block.gemblocks.IronBlock;
import org.spout.vanilla.material.block.gemblocks.LapisLazuliBlock;
import org.spout.vanilla.material.block.ores.CoalOre;
import org.spout.vanilla.material.block.ores.DiamondOre;
import org.spout.vanilla.material.block.ores.GoldOre;
import org.spout.vanilla.material.block.ores.IronOre;
import org.spout.vanilla.material.block.ores.LapisLazuliOre;
import org.spout.vanilla.material.block.ores.RedstoneOre;
import org.spout.vanilla.material.block.other.BedBlock;
import org.spout.vanilla.material.block.other.Chest;
import org.spout.vanilla.material.block.other.DoorBlock;
import org.spout.vanilla.material.block.other.FarmLand;
import org.spout.vanilla.material.block.other.Fence;
import org.spout.vanilla.material.block.other.FenceGate;
import org.spout.vanilla.material.block.other.Fire;
import org.spout.vanilla.material.block.other.Ladder;
import org.spout.vanilla.material.block.other.Lever;
import org.spout.vanilla.material.block.other.MinecartTrack;
import org.spout.vanilla.material.block.other.MinecartTrackDetector;
import org.spout.vanilla.material.block.other.MinecartTrackPowered;
import org.spout.vanilla.material.block.other.MonsterSpawner;
import org.spout.vanilla.material.block.other.SignBase;
import org.spout.vanilla.material.block.other.Slab;
import org.spout.vanilla.material.block.other.Snow;
import org.spout.vanilla.material.block.other.StoneButton;
import org.spout.vanilla.material.block.other.Torch;
import org.spout.vanilla.material.block.other.TrapDoor;
import org.spout.vanilla.material.block.other.Web;
import org.spout.vanilla.material.block.plants.Cactus;
import org.spout.vanilla.material.block.plants.DeadBush;
import org.spout.vanilla.material.block.plants.Flower;
import org.spout.vanilla.material.block.plants.LilyPad;
import org.spout.vanilla.material.block.plants.NetherWart;
import org.spout.vanilla.material.block.plants.Sapling;
import org.spout.vanilla.material.block.plants.SugarCane;
import org.spout.vanilla.material.block.plants.TallGrass;
import org.spout.vanilla.material.block.plants.Vines;
import org.spout.vanilla.material.block.plants.WheatCrop;
import org.spout.vanilla.material.block.pressureplate.StonePressurePlate;
import org.spout.vanilla.material.block.pressureplate.WoodenPressurePlate;
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
import org.spout.vanilla.material.block.solid.Sand;
import org.spout.vanilla.material.block.solid.Sandstone;
import org.spout.vanilla.material.block.solid.SnowBlock;
import org.spout.vanilla.material.block.solid.SoalSand;
import org.spout.vanilla.material.block.solid.Stone;
import org.spout.vanilla.material.block.solid.StoneBrick;
import org.spout.vanilla.material.block.solid.TNT;
import org.spout.vanilla.material.block.solid.Tree;
import org.spout.vanilla.material.block.solid.Wool;
import org.spout.vanilla.material.item.BlockItem;
import org.spout.vanilla.material.item.EmptyContainer;
import org.spout.vanilla.material.item.Food;
import org.spout.vanilla.material.item.FullContainer;
import org.spout.vanilla.material.item.Tool;
import org.spout.vanilla.material.item.VanillaItemMaterial;
import org.spout.vanilla.material.item.Food.FoodEffectType;
import org.spout.vanilla.material.item.armor.Chestwear;
import org.spout.vanilla.material.item.armor.Footwear;
import org.spout.vanilla.material.item.armor.Headwear;
import org.spout.vanilla.material.item.armor.Legwear;
import org.spout.vanilla.material.item.food.RawBeef;
import org.spout.vanilla.material.item.food.RawChicken;
import org.spout.vanilla.material.item.food.RawFish;
import org.spout.vanilla.material.item.food.RawPorkchop;
import org.spout.vanilla.material.item.other.BlazeRod;
import org.spout.vanilla.material.item.other.Clay;
import org.spout.vanilla.material.item.other.Coal;
import org.spout.vanilla.material.item.other.DoorItem;
import org.spout.vanilla.material.item.other.Dye;
import org.spout.vanilla.material.item.other.FlintAndSteel;
import org.spout.vanilla.material.item.other.LavaBucket;
import org.spout.vanilla.material.item.other.Minecart;
import org.spout.vanilla.material.item.other.Paintings;
import org.spout.vanilla.material.item.other.Potion;
import org.spout.vanilla.material.item.other.PoweredMinecart;
import org.spout.vanilla.material.item.other.Shears;
import org.spout.vanilla.material.item.other.Sign;
import org.spout.vanilla.material.item.other.SpawnEgg;
import org.spout.vanilla.material.item.other.Stick;
import org.spout.vanilla.material.item.other.StorageMinecart;
import org.spout.vanilla.material.item.tools.Axe;
import org.spout.vanilla.material.item.tools.Hoe;
import org.spout.vanilla.material.item.tools.Pickaxe;
import org.spout.vanilla.material.item.tools.Spade;
import org.spout.vanilla.material.item.weapons.Bow;
import org.spout.vanilla.material.item.weapons.Sword;
import org.spout.vanilla.material.block.stairs.BrickStairs;
import org.spout.vanilla.material.block.stairs.CobblestoneStairs;
import org.spout.vanilla.material.block.stairs.NetherBrickStairs;
import org.spout.vanilla.material.block.stairs.StoneBrickStairs;
import org.spout.vanilla.material.block.stairs.WoodenStairs;

public final class VanillaMaterials {
	public static final BlockMaterial AIR = BlockMaterial.AIR;
	public static final Stone STONE = (Stone) register(new Stone("Stone", 1).setHardness(1.5F).setResistance(10.0F));
	public static final Grass GRASS = (Grass) register(new Grass().setHardness(0.6F).setResistance(0.8F));
	public static final Dirt DIRT = (Dirt) register(new Dirt("Dirt", 3).setHardness(0.5F).setResistance(0.8F));
	public static final Cobblestone COBBLESTONE = (Cobblestone) register(new Cobblestone().setHardness(2.0F).setResistance(10.0F));
	public static final Plank PLANK = Plank.PLANK;
	public static final Sapling SAPLING = Sapling.DEFAULT;
	public static final Bedrock BEDROCK = (Bedrock) register(new Bedrock("Bedrock", 7).setResistance(6000000.0F));
	public static final Liquid WATER = (Liquid) register(new Liquid("Water", 8, true).setHardness(100.0F).setResistance(166.7F).setOpacity((byte) 2));
	public static final Liquid STATIONARY_WATER = (Liquid) register(new Liquid("Stationary Water", 9, false).setHardness(100.0F).setResistance(166.7F).setOpacity((byte) 2));
	public static final Liquid LAVA = (Liquid) register(new Liquid("Lava", 10, true).setHardness(0.0F).setLightLevel(15).setResistance(0.0F));
	public static final Liquid STATIONARY_LAVA = (Liquid) register(new Liquid("Stationary Lava", 11, false).setHardness(100.0F).setLightLevel(15).setResistance(166.7F));
	public static final Sand SAND = (Sand) register(new Sand().setHardness(0.5F).setResistance(0.8F));
	public static final Gravel GRAVEL = (Gravel) register(new Gravel().setHardness(0.6F).setResistance(1.0F));
	public static final CoalOre COAL_ORE = (CoalOre) register(new CoalOre().setHardness(3.0F).setResistance(5.0F));
	public static final IronOre IRON_ORE = (IronOre) register(new IronOre().setHardness(3.0F).setResistance(5.0F));
	public static final GoldOre GOLD_ORE = (GoldOre) register(new GoldOre().setHardness(3.0F).setResistance(5.0F));
	public static final DiamondOre DIAMOND_ORE = (DiamondOre) register(new DiamondOre().setHardness(3.0F).setResistance(5.0F));
	public static final LapisLazuliOre LAPIS_LAZULI_ORE = (LapisLazuliOre) register(new LapisLazuliOre().setMinDropCount(4).setMaxDropCount(8).setHardness(3.0F).setResistance(5.0F));
	public static final Tree LOG = Tree.DEFAULT;
	public static final Leaves LEAVES = Leaves.DEFAULT;
	public static final Solid SPONGE = (Solid) register(new Solid("Sponge", 19).setHardness(0.6F).setResistance(1.0F));
	public static final Glass GLASS = (Glass) register(new Glass("Glass", 20).setHardness(0.3F).setResistance(0.5F));
	public static final Dispenser DISPENSER = (Dispenser) register(new Dispenser("Dispenser", 23).setHardness(3.5F).setResistance(5.8F));
	public static final Sandstone SANDSTONE = Sandstone.SANDSTONE;
	public static final NoteBlock NOTEBLOCK = (NoteBlock) register(new NoteBlock("Note Block", 25).setHardness(0.8F).setResistance(1.3F));
	public static final BedBlock BED_BLOCK = (BedBlock) register(new BedBlock("Bed", 26).setHardness(0.2F).setResistance(0.3F));
	public static final MinecartTrackPowered POWERED_RAIL = (MinecartTrackPowered) register(new MinecartTrackPowered().setHardness(0.7F).setResistance(1.2F));
	public static final MinecartTrackDetector DETECTOR_RAIL = (MinecartTrackDetector) register(new MinecartTrackDetector().setHardness(0.7F).setResistance(1.2F));
	public static final Solid PISTON_STICKY_BASE = (Solid) register(new Solid("Sticky Piston", 29).setResistance(0.8F));
	public static final Solid PISTON_BASE = (Solid) register(new Solid("Piston", 33).setResistance(0.8F));
	public static final Solid PISTON_EXTENSION = (Solid) register(new Solid("Piston (Head)", 34).setResistance(0.8F));
	public static final Solid MOVED_BY_PISTON = (Solid) register(new Solid("Moved By Piston", 36).setResistance(0.0F));
	public static final Web WEB = (Web) register(new Web("Cobweb", 30).setHardness(4.0F).setResistance(20.0F));
	/**
	 * Warning: This is NOT the data=0 sub-block!
	 */
	public static final TallGrass TALL_GRASS = TallGrass.TALL_GRASS;
	public static final DeadBush DEAD_BUSH = (DeadBush) register(new DeadBush("Dead Shrubs", 32).setHardness(0.0F).setResistance(0.0F));
	public static final Wool WOOL = Wool.WHITE;
	public static final Flower DANDELION = (Flower) register(new Flower("Dandelion", 37).setHardness(0.0F).setResistance(0.0F));
	public static final Flower ROSE = (Flower) register(new Flower("Rose", 38).setHardness(0.0F).setResistance(0.0F));
	public static final GroundAttachable BROWN_MUSHROOM = (GroundAttachable) register(new GroundAttachable("Brown Mushroom", 39).setHardness(0.0F).setResistance(0.0F).setLightLevel(1));
	public static final GroundAttachable RED_MUSHROOM = (GroundAttachable) register(new GroundAttachable("Red Mushroom", 40).setHardness(0.0F).setResistance(0.0F));
	public static final DoubleSlab DOUBLE_SLABS = DoubleSlab.STONE;
	public static final Slab SLAB = Slab.STONE;
	public static final Brick BRICK = (Brick) register(new Brick("Brick Block", 45).setHardness(2.0F).setResistance(10.0F));
	public static final TNT TNT = (TNT) register(new TNT().setHardness(0.0F).setResistance(0.0F));
	public static final BookShelf BOOKSHELF = (BookShelf) register(new BookShelf("Bookshelf", 47).setHardness(1.5F).setResistance(2.5F));
	public static final MossStone MOSS_STONE = (MossStone) register(new MossStone("Moss Stone", 48).setHardness(2.0F).setResistance(10.0F).setResistance(10.0F));
	public static final Obsidian OBSIDIAN = (Obsidian) register(new Obsidian("Obsidian", 49).setHardness(50.0F).setResistance(2000.0F));
	public static final Torch TORCH = (Torch) register(new Torch("Torch", 50).setHardness(0.0F).setResistance(0.0F).setLightLevel(14));
	public static final Fire FIRE = (Fire) register(new Fire().setHardness(0.0F).setResistance(0.0F).setLightLevel(15));
	public static final MonsterSpawner MONSTER_SPAWNER = (MonsterSpawner) register(new MonsterSpawner("MonsterEntity Spawner", 52).setHardness(5.0F).setResistance(8.3F));
	public static final WoodenStairs WOODEN_STAIRS = (WoodenStairs) register(new WoodenStairs("Wooden Stairs", 53).setResistance(3.0F));
	public static final Chest CHEST = (Chest) register(new Chest("Chest", 54).setHardness(2.5F).setResistance(4.2F));
	public static final RedstoneWire REDSTONE_WIRE = (RedstoneWire) register(new RedstoneWire().setHardness(0.0F).setResistance(0.0F));
	public static final IronBlock IRON_BLOCK = (IronBlock) register(new IronBlock("Iron Block", 42).setHardness(5.0F).setResistance(10.0F));
	public static final GoldBlock GOLD_BLOCK = (GoldBlock) register(new GoldBlock("Gold Block", 41).setHardness(3.0F).setResistance(10.0F));
	public static final DiamondBlock DIAMOND_BLOCK = (DiamondBlock) register(new DiamondBlock("Diamond Block", 57).setHardness(5.0F).setResistance(10.0F));
	public static final LapisLazuliBlock LAPIS_LAZULI_BLOCK = (LapisLazuliBlock) register(new LapisLazuliBlock("Lapis Lazuli Block", 22).setHardness(3.0F).setResistance(5.0F));
	public static final CraftingTable CRAFTING_TABLE = (CraftingTable) register(new CraftingTable("Crafting Table", 58).setHardness(4.2F));
	public static final WheatCrop WHEATCROP = (WheatCrop) register(new WheatCrop().setResistance(0.0F));
	public static final FarmLand FARMLAND = (FarmLand) register(new FarmLand("Farmland", 60).setHardness(0.6F).setResistance(1.0F));
	public static final Furnace FURNACE = (Furnace) register(new Furnace("Furnace", 61).setHardness(3.5F).setResistance(5.8F));
	public static final Furnace BURNINGFURNACE = (Furnace) register(new Furnace("Burning Furnace", 62).setHardness(3.5F).setResistance(5.8F).setLightLevel(13));
	public static final SignBase SIGN_POST = (SignBase) register(new SignBase("Sign Post", 63).setHardness(1.0F).setResistance(1.6F));
	public static final DoorBlock WOODEN_DOOR_BLOCK = (DoorBlock) register(new DoorBlock("Wooden Door", 64, true).setHardness(3.0F));
	public static final Ladder LADDERS = (Ladder) register(new Ladder("Ladders", 65).setHardness(0.4F).setResistance(0.7F));
	public static final MinecartTrack RAILS = (MinecartTrack) register(new MinecartTrack().setHardness(0.7F).setResistance(1.2F));
	public static final CobblestoneStairs COBBLESTONE_STAIRS = (CobblestoneStairs) register(new CobblestoneStairs("Cobblestone Stairs", 67).setResistance(10.0F));
	public static final SignBase WALL_SIGN = (SignBase) register(new SignBase("Wall Sign", 68).setHardness(1.0F));
	public static final Lever LEVER = (Lever) register(new Lever("Lever", 69).setHardness(0.5F).setResistance(1.7F));
	public static final StonePressurePlate STONE_PRESSURE_PLATE = (StonePressurePlate) register(new StonePressurePlate("Stone Pressure Plate", 70).setHardness(0.5F).setResistance(0.8F));
	public static final DoorBlock IRON_DOOR_BLOCK = (DoorBlock) register(new DoorBlock("Iron Door", 71, false).setHardness(5.0F).setResistance(8.3F));
	public static final WoodenPressurePlate WOODEN_PRESSURE_PLATE = (WoodenPressurePlate) register(new WoodenPressurePlate("Wooden Pressure Plate", 72).setHardness(0.5F).setResistance(0.8F));
	public static final RedstoneOre REDSTONE_ORE = (RedstoneOre) register(new RedstoneOre().setMinDropCount(4).setMaxDropCount(5).setHardness(3.0F).setResistance(5.0F));
	public static final Ore GLOWING_REDSTONE_ORE = (Ore) register(new Ore("Glowing Redstone Ore", 74).setMinDropCount(4).setMaxDropCount(5).setHardness(3.0F).setResistance(5.0F).setLightLevel(3));
	public static final RedstoneTorch REDSTONE_TORCH_OFF = (RedstoneTorch) register(new RedstoneTorch("Redstone Torch", 75, false).setHardness(0.0F).setResistance(0.0F));
	public static final RedstoneTorch REDSTONE_TORCH_ON = (RedstoneTorch) register(new RedstoneTorch("Redstone Torch (On)", 76, true).setHardness(0.0F).setResistance(0.0F).setLightLevel(7));
	public static final StoneButton STONE_BUTTON = (StoneButton) register(new StoneButton("Stone Button", 77).setHardness(0.5F).setResistance(0.8F));
	public static final Snow SNOW = (Snow) register(new Snow().setHardness(0.1F).setResistance(0.2F));
	public static final Ice ICE = (Ice) register(new Ice().setHardness(0.5F).setResistance(0.8F));
	public static final SnowBlock SNOW_BLOCK = (SnowBlock) register(new SnowBlock("Snow Block", 80).setHardness(0.2F).setResistance(0.3F));
	public static final Cactus CACTUS = (Cactus) register(new Cactus().setHardness(0.4F).setResistance(0.7F));
	public static final ClayBlock CLAY_BLOCK = (ClayBlock) register(new ClayBlock("Clay Block", 82).setHardness(0.6F).setResistance(1.0F));
	public static final SugarCane SUGAR_CANE_BLOCK = (SugarCane) register(new SugarCane().setHardness(0.0F).setResistance(0.0F));
	public static final Jukebox JUKEBOX = (Jukebox) register(new Jukebox("Jukebox", 84).setHardness(2.0F).setResistance(10.0F));
	public static final Fence FENCE = (Fence) register(new Fence("Fence", 85).setResistance(5.0F).setResistance(5.0F));
	public static final Solid PUMPKIN = (Solid) register(new Solid("Pumpkin", 86).setHardness(1.0F).setResistance(1.7F));
	public static final NetherRack NETHERRACK = (NetherRack) register(new NetherRack("Netherrack", 87).setHardness(0.7F));
	public static final SoalSand SOUL_SAND = (SoalSand) register(new SoalSand("Soul Sand", 88).setHardness(0.5F).setResistance(0.8F));
	public static final Ore GLOWSTONE_BLOCK = (Ore) register(new Ore("Glowstone Block", 89).setMinDropCount(2).setMaxDropCount(4).setHardness(0.3F).setResistance(0.5F).setLightLevel(15));
	public static final Solid PORTAL = (Solid) register(new Solid("Portal", 90).setHardness(-1.0F).setResistance(0.0F).setLightLevel(11));
	public static final Solid JACK_O_LANTERN = (Solid) register(new Solid("Jack 'o' Lantern", 91).setHardness(1.0F).setResistance(1.7F).setLightLevel(15));
	public static final Solid CAKE_BLOCK = (Solid) register(new Solid("Cake Block", 92).setHardness(0.5F).setResistance(0.8F));
	public static final Solid REDSTONE_REPEATER_OFF = (Solid) register(new Solid("Redstone Repeater", 93).setHardness(0.0F).setResistance(0.0F));
	public static final Solid REDSTONE_REPEATER_ON = (Solid) register(new Solid("Redstone Repeater (On)", 94).setHardness(0.0F).setResistance(0.0F).setLightLevel(9));
	public static final Solid LOCKED_CHEST = (Solid) register(new Solid("Locked Chest", 95).setHardness(0.0F).setResistance(0.0F).setLightLevel(15));
	public static final TrapDoor TRAPDOOR = (TrapDoor) register(new TrapDoor("Trapdoor", 96).setHardness(3.0F).setResistance(5.0F));
	public static final Solid SILVERFISH_STONE = (Solid) register(new Solid("Silverfish Stone", 97).setHardness(0.75F).setResistance(10.0F)); //Placeholder, block resistance unknown
	public static final StoneBrick STONE_BRICK = StoneBrick.STONE;
	public static final MushroomBlock HUGE_BROWN_MUSHROOM = (MushroomBlock) register(new MushroomBlock("Huge Brown Mushroom", 99));
	public static final MushroomBlock HUGE_RED_MUSHROOM = (MushroomBlock) register(new MushroomBlock("Huge Red Mushroom", 100));
	public static final Solid IRON_BARS = (Solid) register(new Solid("Iron Bars", 101).setHardness(5.0F).setResistance(10.0F));
	public static final Solid GLASS_PANE = (Solid) register(new Solid("Glass Pane", 102).setHardness(0.3F).setResistance(0.3F)); //Placeholder, block resistance unknown
	public static final Ore MELON = (Ore) register(new Ore("Melon", 103).setMinDropCount(3).setMaxDropCount(7).setHardness(1.0F).setResistance(1.7F));
	public static final Solid PUMPKIN_STEM = (Solid) register(new Solid("Pumpkin Stem", 104).setHardness(0.0F).setResistance(0.0F));
	public static final Solid MELON_STEM = (Solid) register(new Solid("Melon Stem", 105).setHardness(0.0F).setResistance(0.3F));
	public static final Vines VINES = (Vines) register(new Vines("Vines", 106).setHardness(0.2F).setResistance(0.3F));
	public static final FenceGate FENCE_GATE = (FenceGate) register(new FenceGate("Fence Gate", 107).setHardness(2.0F).setResistance(3.0F));
	public static final BrickStairs BRICK_STAIRS = (BrickStairs) register(new BrickStairs("Brick Stairs", 108).setResistance(10.0F));
	public static final StoneBrickStairs STONE_BRICK_STAIRS = (StoneBrickStairs) register(new StoneBrickStairs("Stone Brick Stairs", 109).setResistance(10.0F));
	public static final Mycelium MYCELIUM = (Mycelium) register(new Mycelium("Mycelium", 110).setHardness(0.6F).setResistance(0.8F));
	public static final LilyPad LILY_PAD = (LilyPad) register(new LilyPad().setHardness(0.0F).setResistance(0.3F)); //Placeholder, block resistance unknown
	public static final NetherBrick NETHER_BRICK = (NetherBrick) register(new NetherBrick("Nether Brick", 112).setHardness(2.0F).setResistance(10.0F));
	public static final Solid NETHER_BRICK_FENCE = (Solid) register(new Solid("Nether Brick Fence", 113).setHardness(2.0F).setResistance(10.0F));
	public static final NetherBrickStairs NETHER_BRICK_STAIRS = (NetherBrickStairs) register(new NetherBrickStairs("Nether Brick Stairs", 114).setResistance(10.0F));
	public static final NetherWart NETHER_WART_BLOCK = (NetherWart) register(new NetherWart().setResistance(0.0F));
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
	public static final FlintAndSteel FLINT_AND_STEEL = register(new FlintAndSteel("Flint and Steel", 259, (short) 64));
	public static final Food RED_APPLE = register(new Food("Apple", 260, 4, FoodEffectType.HUNGER));
	public static final Bow BOW = (Bow) register(new Bow("Bow", 261, (short) 385)).setRangedDamage(9);
	public static final VanillaItemMaterial ARROW = register(new VanillaItemMaterial("Arrow", 262));
	public static final Coal COAL = Coal.COAL;
	public static final VanillaItemMaterial DIAMOND = register(new VanillaItemMaterial("Diamond", 264));
	public static final VanillaItemMaterial IRON_INGOT = register(new VanillaItemMaterial("Iron Ingot", 265));
	public static final VanillaItemMaterial GOLD_INGOT = register(new VanillaItemMaterial("Gold Ingot", 266));

	//Swords
	public static final Sword WOODEN_SWORD = (Sword) register(new Sword("Wooden Sword", 268, (short) 60).setDamage(4));
	public static final Sword GOLD_SWORD = (Sword) register(new Sword("Gold Sword", 283, (short) 33).setDamage(4));
	public static final Sword STONE_SWORD = (Sword) register(new Sword("Stone Sword", 272, (short) 132).setDamage(5));
	public static final Sword IRON_SWORD = (Sword) register(new Sword("Iron Sword", 267, (short) 251).setDamage(6));
	public static final Sword DIAMOND_SWORD = (Sword) register(new Sword("Diamond Sword", 276, (short) 1562).setDamage(7));
	//Spades
	public static final Spade GOLD_SPADE = register(new Spade("Gold Spade", 284, (short) 33));
	public static final Spade WOODEN_SPADE = register(new Spade("Wooden Spade", 269, (short) 60));
	public static final Spade STONE_SPADE = register(new Spade("Stone Spade", 273, (short) 132));
	public static final Spade IRON_SPADE = register(new Spade("Iron Spade", 256, (short) 251));
	public static final Spade DIAMOND_SPADE = register(new Spade("Diamond Spade", 277, (short) 1562));
	//Pickaxes
	public static final Pickaxe GOLD_PICKAXE = (Pickaxe) register(new Pickaxe("Gold Pickaxe", 285, (short) 33).setDamage(2));
	public static final Pickaxe WOODEN_PICKAXE = (Pickaxe) register(new Pickaxe("Wooden Pickaxe", 270, (short) 60).setDamage(2));
	public static final Pickaxe STONE_PICKAXE = (Pickaxe) register(new Pickaxe("Stone Pickaxe", 274, (short) 132).setDamage(3));
	public static final Pickaxe IRON_PICKAXE = (Pickaxe) register(new Pickaxe("Iron Pickaxe", 257, (short) 251).setDamage(4));
	public static final Pickaxe DIAMOND_PICKAXE = (Pickaxe) register(new Pickaxe("Diamond Pickaxe", 278, (short) 1562).setDamage(5));
	//Axes
	public static final Axe GOLD_AXE = (Axe) register(new Axe("Gold Axe", 286, (short) 33).setDamage(3));
	public static final Axe WOODEN_AXE = (Axe) register(new Axe("Wooden Axe", 271, (short) 60).setDamage(3));
	public static final Axe STONE_AXE = (Axe) register(new Axe("Stone Axe", 275, (short) 132).setDamage(3));
	public static final Axe IRON_AXE = (Axe) register(new Axe("Iron Axe", 258, (short) 251).setDamage(5));
	public static final Axe DIAMOND_AXE = (Axe) register(new Axe("Diamond Axe", 279, (short) 1562).setDamage(6));
	//Hoes
	public static final Hoe GOLD_HOE = register(new Hoe("Gold Hoe", 294, (short) 33));
	public static final Hoe WOODEN_HOE = register(new Hoe("Wooden Hoe", 290, (short) 60));
	public static final Hoe STONE_HOE = register(new Hoe("Stone Hoe", 291, (short) 132));
	public static final Hoe IRON_HOE = register(new Hoe("Iron Hoe", 292, (short) 251));
	public static final Hoe DIAMOND_HOE = register(new Hoe("Diamond Hoe", 293, (short) 1562));
	//Headwear
	public static final Headwear LEATHER_CAP = register(new Headwear("Leather Cap", 298, 1));
	public static final Headwear CHAIN_HELMET = register(new Headwear("Chain Helmet", 302, 2));
	public static final Headwear IRON_HELMET = register(new Headwear("Iron Helmet", 306, 2));
	public static final Headwear GOLD_HELMET = register(new Headwear("Gold Helmet", 314, 2));
	public static final Headwear DIAMOND_HELMET = register(new Headwear("Diamond Helmet", 310, 3));
	//Chestwear
	public static final Chestwear LEATHER_TUNIC = register(new Chestwear("Leather Tunic", 299, 3));
	public static final Chestwear CHAIN_CHESTPLATE = register(new Chestwear("Chain Chestplate", 303, 5));
	public static final Chestwear IRON_CHESTPLATE = register(new Chestwear("Iron Chestplate", 307, 6));
	public static final Chestwear DIAMOND_CHESTPLATE = register(new Chestwear("Diamond Chestplate", 311, 8));
	public static final Chestwear GOLD_CHESTPLATE = register(new Chestwear("Gold Chestplate", 315, 5));
	//Legwear
	public static final Legwear LEATHER_PANTS = register(new Legwear("Leather Pants", 300, 2));
	public static final Legwear CHAIN_LEGGINGS = register(new Legwear("Chain Leggings", 304, 4));
	public static final Legwear IRON_LEGGINGS = register(new Legwear("Iron Leggings", 308, 5));
	public static final Legwear DIAMOND_LEGGINGS = register(new Legwear("Diamond Leggings", 312, 6));
	public static final Legwear GOLD_LEGGINGS = register(new Legwear("Gold Leggings", 316, 3));
	//Footwear
	public static final Footwear LEATHER_BOOTS = register(new Footwear("Leather Boots", 301, 1));
	public static final Footwear CHAIN_BOOTS = register(new Footwear("Chain Boots", 305, 1));
	public static final Footwear IRON_BOOTS = register(new Footwear("Iron Boots", 309, 2));
	public static final Footwear DIAMOND_BOOTS = register(new Footwear("Diamond Boots", 313, 3));
	public static final Footwear GOLD_BOOTS = register(new Footwear("Gold Boots", 317, 1));

	public static final Stick STICK = register(new Stick("Stick", 280));
	public static final VanillaItemMaterial BOWL = register(new VanillaItemMaterial("Bowl", 281));
	public static final Food MUSHROOM_SOUP = register(new Food("Mushroom Soup", 282, 8, FoodEffectType.HUNGER));
	public static final VanillaItemMaterial STRING = register(new VanillaItemMaterial("String", 287));
	public static final VanillaItemMaterial FEATHER = register(new VanillaItemMaterial("Feather", 288));
	public static final VanillaItemMaterial GUNPOWDER = register(new VanillaItemMaterial("Gunpowder", 289));
	public static final VanillaItemMaterial SEEDS = register(new VanillaItemMaterial("Seeds", 295));
	public static final VanillaItemMaterial WHEAT = register(new VanillaItemMaterial("Wheat", 296));
	public static final Food BREAD = register(new Food("Bread", 297, 5, FoodEffectType.HUNGER));
	public static final VanillaItemMaterial FLINT = register(new VanillaItemMaterial("Flint", 318));
	public static final RawPorkchop RAW_PORKCHOP = register(new RawPorkchop());
	public static final Food COOKED_PORKCHOP = register(new Food("Cooked Porkchop", 320, 8, FoodEffectType.HUNGER));
	public static final VanillaItemMaterial PAINTINGS = register(new Paintings());
	public static final Food GOLDEN_APPLE = register(new Food("Golden Apple", 322, 10, FoodEffectType.HUNGER));
	public static final Sign SIGN = register(new Sign("Sign", 323));
	public static final DoorItem WOODEN_DOOR = register(new DoorItem("Wooden Door", 324, WOODEN_DOOR_BLOCK));
	public static final EmptyContainer BUCKET = register(new EmptyContainer("Bucket", 325));
	public static final FullContainer WATER_BUCKET = register(new FullContainer("Water Bucket", 326, WATER, BUCKET));
	public static final LavaBucket LAVA_BUCKET = register(new LavaBucket("Lava Bucket", 327, LAVA, BUCKET));
	public static final Minecart MINECART = register(new Minecart("Minecart", 328));
	public static final VanillaItemMaterial SADDLE = register(new VanillaItemMaterial("Saddle", 329));
	public static final DoorItem IRON_DOOR = register(new DoorItem("Iron Door", 330, IRON_DOOR_BLOCK));
	public static final BlockItem REDSTONE = register(new BlockItem("Redstone", 331, VanillaMaterials.REDSTONE_WIRE));
	public static final VanillaItemMaterial SNOWBALL = register(new VanillaItemMaterial("Snowball", 332));
	public static final VanillaItemMaterial BOAT = register(new VanillaItemMaterial("Boat", 333));
	public static final VanillaItemMaterial LEATHER = register(new VanillaItemMaterial("Leather", 334));
	public static final VanillaItemMaterial MILK = register(new VanillaItemMaterial("Milk", 335));
	public static final VanillaItemMaterial CLAY_BRICK = register(new VanillaItemMaterial("Brick", 336));
	public static final Clay CLAY = register(new Clay());
	public static final BlockItem SUGAR_CANE = register(new BlockItem("Sugar Cane", 338, VanillaMaterials.SUGAR_CANE_BLOCK));
	public static final VanillaItemMaterial PAPER = register(new VanillaItemMaterial("Paper", 339));
	public static final VanillaItemMaterial BOOK = register(new VanillaItemMaterial("Book", 340));
	public static final VanillaItemMaterial SLIMEBALL = register(new VanillaItemMaterial("Slimeball", 341));
	public static final StorageMinecart MINECART_CHEST = register(new StorageMinecart());
	public static final PoweredMinecart MINECART_FURNACE = register(new PoweredMinecart());
	public static final VanillaItemMaterial EGG = register(new VanillaItemMaterial("Egg", 344));
	public static final VanillaItemMaterial COMPASS = register(new VanillaItemMaterial("Compass", 345));
	public static final Tool FISHING_ROD = register(new Tool("Fishing Rod", 346, (short) 65));
	public static final VanillaItemMaterial CLOCK = register(new VanillaItemMaterial("Clock", 347));
	public static final VanillaItemMaterial GLOWSTONE_DUST = register(new VanillaItemMaterial("Glowstone Dust", 348));
	public static final RawFish RAW_FISH = register(new RawFish());
	public static final Food COOKED_FISH = register(new Food("Cooked Fish", 350, 5, FoodEffectType.HUNGER));
	public static final Dye DYE = Dye.INK_SAC;
	public static final VanillaItemMaterial BONE = register(new VanillaItemMaterial("Bone", 352));
	public static final VanillaItemMaterial SUGAR = register(new VanillaItemMaterial("Sugar", 353));
	public static final BlockItem CAKE = register(new BlockItem("Cake", 354, VanillaMaterials.CAKE_BLOCK));
	public static final BlockItem BED = register(new BlockItem("Bed", 355, VanillaMaterials.BED_BLOCK));
	public static final BlockItem REDSTONE_REPEATER = register(new BlockItem("Redstone Repeater", 356, VanillaMaterials.REDSTONE_REPEATER_OFF));
	public static final Food COOKIE = register(new Food("Cookie", 357, 1, FoodEffectType.HUNGER));
	public static final VanillaItemMaterial MAP = register(new VanillaItemMaterial("Map", 358));
	public static final Shears SHEARS = register(new Shears());
	public static final Food MELON_SLICE = register(new Food("Melon Slice", 360, 2, FoodEffectType.HUNGER));
	public static final VanillaItemMaterial PUMPKIN_SEEDS = register(new VanillaItemMaterial("Pumpkin Seeds", 361));
	public static final VanillaItemMaterial MELON_SEEDS = register(new VanillaItemMaterial("Melon Seeds", 362));
	public static final RawBeef RAW_BEEF = register(new RawBeef());
	public static final Food STEAK = register(new Food("Steak", 364, 8, FoodEffectType.HUNGER));
	public static final RawChicken RAW_CHICKEN = register(new RawChicken());
	public static final Food COOKED_CHICKEN = register(new Food("Cooked Chicken", 366, 6, FoodEffectType.HUNGER));
	public static final Food ROTTEN_FLESH = register(new Food("Rotten Flesh", 367, 4, FoodEffectType.HUNGER));
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
	/**
	 * Warning: This is NOT the data=0 sub-block!
	 */
	public static final SpawnEgg SPAWN_EGG = SpawnEgg.PIG;
	public static final VanillaItemMaterial BOTTLE_O_ENCHANTING = register(new VanillaItemMaterial("Bottle o' Enchanting", 384));
	public static final BlockItem FIRE_CHARGE = register(new BlockItem("Fire Charge", 385, VanillaMaterials.FIRE)); //Basic Implementation
	public static final VanillaItemMaterial GOLD_MUSIC_DISC = register(new VanillaItemMaterial("Music Disc", 2256));
	public static final VanillaItemMaterial GREEN_MUSIC_DISC = register(new VanillaItemMaterial("Music Disc", 2257));
	public static final VanillaItemMaterial ORANGE_MUSIC_DISC = register(new VanillaItemMaterial("Music Disc", 2258));
	public static final VanillaItemMaterial RED_MUSIC_DISC = register(new VanillaItemMaterial("Music Disc", 2259));
	public static final VanillaItemMaterial CYAN_MUSIC_DISC = register(new VanillaItemMaterial("Music Disc", 2260));
	public static final VanillaItemMaterial BLUE_MUSIC_DISC = register(new VanillaItemMaterial("Music Disc", 2261));
	public static final VanillaItemMaterial PURPLE_MUSIC_DISC = register(new VanillaItemMaterial("Music Disc", 2262));
	public static final VanillaItemMaterial BLACK_MUSIC_DISC = register(new VanillaItemMaterial("Music Disc", 2263));
	public static final VanillaItemMaterial WHITE_MUSIC_DISC = register(new VanillaItemMaterial("Music Disc", 2264));
	public static final VanillaItemMaterial FOREST_GREEN_MUSIC_DISC = register(new VanillaItemMaterial("Music Disc", 2265));
	public static final VanillaItemMaterial BROKEN_MUSIC_DISC = register(new VanillaItemMaterial("Music Disc", 2266));
	public static final Potion POTION = Potion.EMPTY;
	private static boolean initialized = false;

	public static void initialize() {
		if (initialized) {
			return;
		}
		VanillaMaterials.STONE.setDrop(VanillaMaterials.COBBLESTONE);
		VanillaMaterials.GRASS.setDrop(VanillaMaterials.DIRT);
		VanillaMaterials.COAL_ORE.setDrop(VanillaMaterials.COAL);
		VanillaMaterials.GLASS.setDrop(VanillaMaterials.AIR);
		VanillaMaterials.LAPIS_LAZULI_ORE.setDrop(Dye.LAPIS_LAZULI);
		VanillaMaterials.BED_BLOCK.setDrop(VanillaMaterials.BED);
		VanillaMaterials.WEB.setDrop(VanillaMaterials.STRING);
		TallGrass.DEAD_GRASS.setDrop(VanillaMaterials.AIR);
		TallGrass.TALL_GRASS.setDrop(VanillaMaterials.AIR);
		TallGrass.FERN.setDrop(VanillaMaterials.AIR);
		VanillaMaterials.DEAD_BUSH.setDrop(VanillaMaterials.AIR);
		VanillaMaterials.BOOKSHELF.setDrop(VanillaMaterials.BOOK).setDropCount(3);
		VanillaMaterials.FIRE.setDrop(VanillaMaterials.AIR);
		VanillaMaterials.MONSTER_SPAWNER.setDrop(VanillaMaterials.AIR);
		VanillaMaterials.REDSTONE_WIRE.setDrop(VanillaMaterials.REDSTONE);
		VanillaMaterials.DIAMOND_ORE.setDrop(VanillaMaterials.DIAMOND);
		VanillaMaterials.SUGAR_CANE_BLOCK.setDrop(VanillaMaterials.SUGAR_CANE);
		VanillaMaterials.WHEATCROP.setDrop(VanillaMaterials.WHEAT);
		VanillaMaterials.FARMLAND.setDrop(VanillaMaterials.DIRT);
		VanillaMaterials.BURNINGFURNACE.setDrop(VanillaMaterials.FURNACE);
		VanillaMaterials.SIGN_POST.setDrop(VanillaMaterials.SIGN);
		VanillaMaterials.WOODEN_DOOR_BLOCK.setDrop(VanillaMaterials.WOODEN_DOOR);
		VanillaMaterials.WALL_SIGN.setDrop(VanillaMaterials.SIGN);
		VanillaMaterials.REDSTONE_ORE.setDrop(VanillaMaterials.REDSTONE);
		VanillaMaterials.GLOWING_REDSTONE_ORE.setDrop(VanillaMaterials.REDSTONE);
		VanillaMaterials.REDSTONE_TORCH_OFF.setDrop(VanillaMaterials.REDSTONE_TORCH_ON);
		VanillaMaterials.SNOW.setDrop(VanillaMaterials.SNOWBALL);
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
		VanillaMaterials.NETHER_WART_BLOCK.setDrop(VanillaMaterials.NETHER_WART);
		initialized = true;
	}
}
