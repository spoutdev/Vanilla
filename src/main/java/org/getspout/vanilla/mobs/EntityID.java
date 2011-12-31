/*
 * This file is part of Vanilla (http://www.getspout.org/).
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
package org.getspout.vanilla.mobs;


import gnu.trove.map.hash.TIntObjectHashMap;

public enum EntityID {
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
	Creeper(50),
	Skeleton(51),
	Spider(52),
	GiantZombie(53),
	Zombie(54),
	Slime(55),
	Ghast(56),
	ZombiePigman(57),
	Enderman(58),
	CaveSpider(59),
	Silverfish(60),
	Blaze(61),
	MagmaCube(62),
	EnderDragon(64),
	Pig(90),
	Sheep(91),
	Cow(92),
	Chicken(93),
	Squid(94),
	Wolf(95),
	Mooshroom(96),
	SnowGolem(97),
	Villager(120),//Squidward
	EnderCrystal(200); 

	public final int id;

	public static final String KEY = "EntityID";
	
	final static TIntObjectHashMap<EntityID> map = new TIntObjectHashMap<EntityID>();

	private EntityID(int data){
		this.id = (byte)data;
	}

	public int getID(){
		return id;
	}
	
	public static EntityID getByID(int id){
		return map.get(id);
	}
	
	static{
		for(EntityID m : EntityID.values()){
			map.put(m.getID(), m);
		}
		
	}
}