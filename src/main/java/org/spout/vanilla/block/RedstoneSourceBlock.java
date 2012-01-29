package org.spout.vanilla.block;

import org.spout.api.geo.World;
import org.spout.vanilla.material.VanillaBlockMaterial;

public interface RedstoneSourceBlock extends VanillaBlockMaterial {
	/**
	 * Gets how much redstone power the block at x, y, z provides to block tx, ty, tz
	 * @param world world the blocks are in
	 * @param x coord of source block
	 * @param y coord of source block
	 * @param z coord of source block
	 * @param tx coord of target block
	 * @param ty coord of target block
	 * @param tz coord of target block
	 * @return how much power source block provides to target block
	 */
	short getRedstonePower(World world, int x, int y, int z, int tx, int ty, int tz);

	/**
	 * Gets if the block provides power to the target block
	 * @param world world the blocks are in
	 * @param x coord of source block
	 * @param y coord of source block
	 * @param z coord of source block
	 * @param tx coord of target block
	 * @param ty coord of target block
	 * @param tz coord of target block
	 * @return if the source block provides power to the target block
	 */
	boolean providesPowerTo(World world, int x, int y, int z, int tx, int ty, int tz);
	
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
