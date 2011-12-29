package org.getspout.vanilla.world;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;



public enum Weather {
	CLEAR(0),
	RAIN(1),
	THUNDERSTORM(2);
	
	final int id;
	private final static TIntObjectMap<Weather> weathers = new TIntObjectHashMap<Weather>();
	
	private Weather(int id){
		this.id = id;
	}
	
	public int getId(){
		return id;
	}
	
	public static Weather fromID(int id){
		return weathers.get(id);
	}
	
	static{
		for(Weather w : Weather.values()){
			weathers.put(w.getId(), w);
		}
		
	}
	
	
}
