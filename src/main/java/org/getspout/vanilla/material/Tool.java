package org.getspout.vanilla.material;

import org.getspout.api.material.BlockMaterial;
import org.getspout.api.material.ItemMaterial;

public interface Tool extends ItemMaterial {

	public short getDurability();

	public Tool setDurability(short durability);

	public float getStrengthModifier(BlockMaterial block);

	public Tool setStrengthModifier(BlockMaterial block, float modifier);

	public BlockMaterial[] getStrengthModifiedBlocks();
}
