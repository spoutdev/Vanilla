package org.spout.vanilla.data;

public enum EntityProtocolID {
	BAT(65),
	BLAZE(61),
	CAVESPIDER(59),
	CHICKEN(93),
	COW(92),
	CREEPER(50),
	ENDERDRAGON(63),
	ENDERMAN(58),
	GHAST(56),
	GIANT(53),
	IRONGOLEM(99),
	MAGMACUBE(62),
	MUSHROOMCOW(96),
	OCELOT(98),
	PIG(90),
	PIGZOMBIE(57),
	SHEEP(91),
	SILVERFISH(60),
	SKELETON(51),
	SLIME(55),
	SNOWGOLEM(97),
	SPIDER(52),
	SQUID(94),
	VILLAGER(120),
	WITCH(66),
	WITHER(64),
	WOLF(95),
	ZOMBIE(54);
	
	private int id;
	
	EntityProtocolID(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
}
