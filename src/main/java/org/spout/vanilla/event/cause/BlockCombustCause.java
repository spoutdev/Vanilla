package org.spout.vanilla.event.cause;

import org.spout.api.geo.cuboid.Block;

public class BlockCombustCause implements CombustCause<Block> {
	private final Block block;

	/**
	 * Contains the source of combustion.
	 * @param block The block causing the combustion.
	 */
	public BlockCombustCause(Block block) {
		this.block = block;
	}
	
	@Override
	public Block getSource() {
		return block;
	}
}
