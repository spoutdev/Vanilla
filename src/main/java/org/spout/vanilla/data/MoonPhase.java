package org.spout.vanilla.data;

public enum MoonPhase {
	FULL_MOON(0),
	WANING_GIBBOUS(1),
	LAST_QUARTER(2),
	WANING_CRESENT(3),
	NEW_MOON(4),
	WAXING_CRESENT(5),
	FIRST_QUARTER(6),
	WAXING_GIBBOUS(7);
	
	private int id;
	MoonPhase(int id) {
		this.id = id;
	}
	
	public static MoonPhase get(int id) {
		return values()[id];
	}
	
	public int getId() {
		return id;
	}
}