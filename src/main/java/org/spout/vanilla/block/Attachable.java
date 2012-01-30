package org.spout.vanilla.block;

import org.spout.api.material.BlockMaterial;
import org.spout.api.material.block.BlockFace;
import org.spout.vanilla.material.VanillaBlockMaterial;

public interface Attachable extends VanillaBlockMaterial {
	/**
	 * Gets which data id should be set for the given face
	 * @param face where the block will be attached to
	 * @return data for the given face
	 */
	public short getDataForFace(BlockFace face);
	
	/**
	 * Gets the face the block is attached to by given data
	 * @param data that the block has
	 * @return to which face the block is attached to
	 */
	public BlockFace getAttachedTo(short data);
}
