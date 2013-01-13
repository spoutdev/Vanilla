package org.spout.vanilla.plugin.event.block.network;

import org.spout.api.entity.Entity;
import org.spout.api.geo.discrete.Point;
import org.spout.api.protocol.event.ProtocolEvent;

public class BlockBreakAnimationEvent implements ProtocolEvent {

	private final Point point;
	private final byte level;
	private final Entity entity;

	public BlockBreakAnimationEvent(Entity entity, Point point, byte level) {
		this.entity = entity;
		this.point = point;
		this.level = level;
	}

	public Point getPoint() {
		return point;
	}

	public byte getLevel() {
		return level;
	}

	public Entity getEntity() {
		return entity;
	}
	
}
