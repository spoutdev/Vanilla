package org.spout.vanilla.material;

import org.spout.api.material.BlockMaterial;
import org.spout.api.material.ItemMaterial;

public interface Tool extends ItemMaterial {

	public short getDurability();

	public Tool setDurability(short durability);

	public float getStrengthModifier(BlockMaterial block);

	public Tool setStrengthModifier(BlockMaterial block, float modifier);

	public BlockMaterial[] getStrengthModifiedBlocks();
}
