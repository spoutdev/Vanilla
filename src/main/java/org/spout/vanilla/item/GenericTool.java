package org.spout.vanilla.item;

import org.spout.api.material.BlockMaterial;
import org.spout.api.material.GenericItemMaterial;
import org.spout.vanilla.material.Tool;

public class GenericTool extends GenericItemMaterial implements Tool {

	public GenericTool(String name, int id) {
		super(name, id);
	}

	public short getDurability() {
		// TODO Auto-generated method stub
		return 0;
	}

	public Tool setDurability(short durability) {
		// TODO Auto-generated method stub
		return this;
	}

	public float getStrengthModifier(BlockMaterial block) {
		// TODO Auto-generated method stub
		return 0;
	}

	public Tool setStrengthModifier(BlockMaterial block, float modifier) {
		// TODO Auto-generated method stub
		return this;
	}

	public BlockMaterial[] getStrengthModifiedBlocks() {
		// TODO Auto-generated method stub
		return null;
	}

}
