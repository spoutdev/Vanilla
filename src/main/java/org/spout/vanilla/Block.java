/*
 * This file is part of Vanilla (http://www.spout.org/).
 *
 * Vanilla is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Vanilla is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.spout.vanilla;

import gnu.trove.map.hash.TIntObjectHashMap;

public enum Block {
	Stone(1),
	Grass(2),
	Dirt(3),
	Cobblestone(4),
	WoodPlanks(5),
	NormalSapling(6,0),
	SpruceSapling(6,1),
	BirchSapling(6,2),
	Bedrock(7),
	Water(8),
	StationaryWater(9),
	Lava(10),
	StationaryLava(11),
	Sand(12),
	Gravel(13),
	GoldOre(14),
	IronOre(15),
	CoalOre(16),
	Wood(17,0),
	Sprucewood(17,1),
	Birchwood(17,2),
	Leaves(18,0),
	SpruceLeaves(18,1),
	BirchLeaves(18,2),
	DarkLeaves(18,3),
	Sponge(19),
	Glass(20),
	LapisOre(21),
	LapisBlock(22),
	Dispenser(23),
	Sandstone(24),
	NoteBlock(25),
	Bed(26),
	PoweredRail(27),
	DetectorRail(28),
	StickyPiston(29),
	Cobweb(30),
	TallGrass(31,1),
	DeadShrub(31,0),
	Fern(31,2),
	DeadBush(32),
	Piston(33),
	PistonExtension(34),
	WhiteWool(35,0),
	OrangeWool(35,1),
	MagentaWool(35,2),
	LightBlueWool(35,3),
	YellowWool(35,4),
	LimeWool(35,5),
	PinkWool(35,6),
	GrayWool(35,7),
	LightGrayWool(35,8),
	CyanWool(35,9),
	PurpleWool(35,10),
	BlueWool(35,11),
	BrownWool(35,12),
	GreenWool(35,13),
	RedWool(35,14),
	BlackWool(35,15),
	BlockMovedByPiston(36),
	YellowFlower(37),
	RedFlower(38),
	BrownMushroom(39),
	RedMushrrom(40),
	GoldBlock(41),
	IronBlock(42),
	DoubleStoneSlab(43,0),
	DoubleSandstoneSlab(43,1),
	DoubleWoodenSlab(43,2),
	DoubleCobblestoneSlab(43,3),
	DoubleBrickSlab(43,4),
	DoubleStoneBrickSlab(43,5),
	DoubleSmoothStoneSlab(43,6),
	StoneSlab(44,0),
	SandstoneSlab(44,1),
	WoodenSlab(44,2),
	CobblestoneSlab(44,3),
	BrickSlab(44,4),
	StoneBrickSlab(44,5),
	SmoothStoneSlab(44,6),
	Bricks(45),
	TNT(46),
	Bookshelf(47),
	MossyCobblestone(48),
	Obsidian(49),
	Torch(50),
	Fire(51),
	MonsterSpawner(52),
	WoodenStairs(53),
	Chest(54),
	RedstoneWire(55),
	DiamondOre(56),
	DiamondBlock(57),
	CraftingTable(58),
	Wheat(59),
	Farmland(60),
	Furnace(61),
	FurnaceOn(62),
	SignPost(63),
	WoodenDoor(64),
	Ladder(65),
	Rails(66),
	CobblestoneStairs(67),
	WallSign(68),
	Lever(69),
	StonePressurePlate(70),
	IronDoor(71),
	WoodenPressurePlate(72),
	RedstoneOre(73),
	GlowingRedstoneOre(74),
	RedstoneTorchOff(75),
	RedstoneTorch(76),
	StoneButton(77),
	Snow(78),
	Ice(79),
	SnowBlock(80),
	Cactus(81),
	ClayBlock(82),
	SurgarCane(83),
	Jukebox(84),
	Fence(85),
	Pumpkin(86),
	Netherrack(87),
	SoulSand(88),
	Glowstone(89),
	Portal(90),
	JackOLantern(91),
	Cake(92), //It's a lie!
	RedstoneRepeater(93),
	RedstoneRepeaterOff(94),
	LockedChest(95),
	Trapdoor(96),
	SilverfishStone(97,0),
	SilverfishCobblestone(97,1),
	SilverfishStoneBrick(97,2),
	StoneBrick(98, 0),
	MossyStoneBrick(98,1),
	CrackedStoneBrick(98,2),
	HugeBrownMushroomFleshy(99,0),
	HugeBrownMushroomCorner1(99,1),
	HugeBrownMushroomSide1(99,2),
	HugeBrownMushroomCorner2(99,3),
	HugeBrownMushroomSide2(99,4),
	HugeBrownMushroomTop(99,5),
	HugeBrownMushroomSide3(99,6),
	HugeBrownMushroomCorner3(99,7),
	HugeBrownMushroomSide4(99,8),
	HugeBrownMushroomCorner4(99,9),
	HugeBrownMushroomStem(99,10),
	HugeRedMushroomFleshy(100,0),
	HugeRedMushroomCorner1(100,1),
	HugeRedMushroomSide1(100,2),
	HugeRedMushroomCorner2(100,3),
	HugeRedMushroomSide2(100,4),
	HugeRedMushroomTop(100,5),
	HugeRedMushroomSide3(100,6),
	HugeRedMushroomCorner3(100,7),
	HugeRedMushroomSide4(100,8),
	HugeRedMushroomCorner4(100,9),
	HugeRedMushroomStem(100,10),
	IronBars(101),
	GlassPane(102),
	Melon(103),
	PumpkinStem(104),
	MelonStem(105),
	Vines(106),
	FenceGate(107),
	BrickStairs(108),
	StoneBrickStairs(109),
	Mycelium(110),
	LilyPad(111),
	NetherBrick(112),
	NetherBrickFence(113),
	NetherBrickStairs(114),
	NetherWart(115),
	EnchantmentTable(116),
	BrewingStand(117),
	Caudron(118),
	EndPortal(119),
	EndPortalFrame(120),
	EndStone(121),
	DragonEgg(122),	
	;
	final short id;
	final byte data;
	
	final static TIntObjectHashMap<Block> map = new TIntObjectHashMap<Block>();
	
	private Block(int id){
		this(id, 0);
	}
	private Block(int id, int data){
		this.id = (short)id;
		this.data = (byte)data;
	}
	
	public short getID(){
		return id;
	}
	
	public static Block fromID(int id){
		return map.get(id);
	}
	
	
	static{
		for(Block m : Block.values()){
			map.put(m.getID(), m);
		}
		
	}
}
