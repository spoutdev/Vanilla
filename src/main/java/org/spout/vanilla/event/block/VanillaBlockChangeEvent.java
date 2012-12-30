package org.spout.vanilla.event.block;

import org.spout.api.event.Cause;
import org.spout.api.event.block.BlockChangeEvent;

import org.spout.vanilla.event.cause.BlockGrowCause;

/**
 * An event which is called when a change of a Vanilla Block happens.
 */
public class VanillaBlockChangeEvent extends BlockChangeEvent {

	public static enum BlockChangeEventType {
		BREAK, FADE, MELT, DISAPPEAR, DECAY, FORM, SPREADING, GROW, IGNITE
	}

	private final BlockChangeEventType blockChangeEventType;

	public VanillaBlockChangeEvent(Cause<?> reason, BlockChangeEventType blockChangeEventType) {
		super(reason);
		this.blockChangeEventType = blockChangeEventType;
	}

	public BlockChangeEventType getBlockChangeEventType() {
		return blockChangeEventType;
	}

	public BlockGrowCause getBlockGrowCause() {
		return (getCause() instanceof  BlockGrowCause) ? (BlockGrowCause) getCause() : null;
	}

	// and more to go...
}
