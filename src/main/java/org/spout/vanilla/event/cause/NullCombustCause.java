package org.spout.vanilla.event.cause;

import org.spout.api.geo.cuboid.Block;

public class NullCombustCause implements CombustCause<Object> {
	@Override
	public Block getSource() {
		return null;
	}
}