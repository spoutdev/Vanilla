package org.getspout.vanilla.world;

public enum Environment {
	NORMAL(0),
	NETHER(-1),
	THEEND(1);
	
	final int dim;
	
	private Environment(int dim){
		this.dim = dim;
	}
	
	public int getDimension(){
		return dim;
	}
	
}
