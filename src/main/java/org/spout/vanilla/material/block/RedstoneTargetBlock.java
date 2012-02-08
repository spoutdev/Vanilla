package org.spout.vanilla.material.block;

import org.spout.api.geo.World;
import org.spout.vanilla.material.Block;

public interface RedstoneTargetBlock extends Block {
	/**
	 * Gets if the block provides an attach point for redstone wire.
	 * @param world the blocks are in
	 * @param x coord of this block
	 * @param y coord of this block
	 * @param z coord of this block
	 * @param tx coord of the wire
	 * @param ty coord of the wire
	 * @param tz coord of the wire
	 * @return if the block provides an attachment point from the given face
	 */
	boolean providesAttachPoint(World world, int x, int y, int z, int tx, int ty, int tz);
}
