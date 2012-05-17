package org.spout.vanilla.material.block;

import org.spout.api.material.BlockMaterial;
import org.spout.vanilla.material.VanillaBlockMaterial;

public class Portal extends VanillaBlockMaterial {

	private BlockMaterial frameMaterial;
	
	public Portal(String name, int id) {
		super(name, id);
	}

	public BlockMaterial getFrameMaterial() {
		return this.frameMaterial;
	}

	public Portal setFrameMaterial(BlockMaterial material) {
		this.frameMaterial = material;
		return this;
	}

}
