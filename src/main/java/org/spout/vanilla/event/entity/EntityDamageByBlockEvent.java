package org.spout.vanilla.event.entity;

import org.spout.api.entity.Entity;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.discrete.Point;

public class EntityDamageByBlockEvent extends EntityDamageEvent {
	private Block block;
	
	public EntityDamageByBlockEvent(Entity e, DamageCause dmgCause, int damageAmount, Block block) {
	    super(e, dmgCause, damageAmount);
	    this.block = block;
    }
	
	/**
	 * Gets the damaging block
	 * @return The Block that damaged the entity
	 */
	public Block getBlock() {
		return block;
	}
	
	/**
	 * Gets the Point position where the event occurred
	 * @return The Point at which the damage happened
	 */
	public Point getPosition() {
		return block.getPosition();
	}
}
