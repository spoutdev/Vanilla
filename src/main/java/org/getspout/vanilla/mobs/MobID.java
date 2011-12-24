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
	
	private MobID(byte data){
		this.id = data;
	}
	
	public int getData(){
		return id;
	}
	
}
