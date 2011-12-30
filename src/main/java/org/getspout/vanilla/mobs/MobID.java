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

public enum MobID {
	Creeper((byte)50),
	Skeleton((byte)51),
	Spider((byte)52),
	GiantZombie((byte)53),
	Zombie((byte)54),
	Slime((byte)55),
	Ghast((byte)56),
	ZombiePigman((byte)57),
	Enderman((byte)58),
	CaveSpider((byte)59),
	Silverfish((byte)60),
	Blaze((byte)61),
	MagmaCube((byte)62),
	EnderDragon((byte)64),
	Pig((byte)90),
	Sheep((byte)91),
	Cow((byte)92),
	Chicken((byte)93),
	Squid((byte)94),
	Wolf((byte)95),
	Mooshroom((byte)96),
	SnowGolem((byte)97),
	Villager((byte)120); //Squidward

	public final int id;

	public static final String KEY = "MobID";

	private MobID(byte data){
		this.id = data;
	}

	public int getData(){
		return id;
	}
}