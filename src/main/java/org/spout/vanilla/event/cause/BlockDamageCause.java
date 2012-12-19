package org.spout.vanilla.event.cause;

import org.spout.api.geo.cuboid.Block;

public class BlockDamageCause implements DamageCause<Block> {

	private final DamageType type;
	private final Block block;

	/**
	 * Contains the source and type of damage.
	 * @param block The block causing the damage
	 * @param type The cause of the damage.
	 */
	public BlockDamageCause(Block block, DamageType type) {
		this.block = block;
		this.type = type;
	}

	public DamageType getType() {
		return type;
	}

	@Override
	public Block getSource() {
		return block;
	}
}
