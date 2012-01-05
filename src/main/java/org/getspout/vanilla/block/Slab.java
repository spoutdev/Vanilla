package org.getspout.vanilla.block;

import org.getspout.vanilla.material.SolidBlock;

public class Slab extends GenericBlockMaterial implements SolidBlock {

	public Slab(String name, int id, int data) {
		super(name, id, data);
	}

	public boolean isFallingBlock() {
		return false;
	}
}
