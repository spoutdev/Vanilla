package org.spout.vanilla.event.entity;

import org.spout.api.entity.Entity;
import org.spout.api.geo.discrete.Point;

public class EntityDamageByEntityEvent extends EntityDamageEvent {
	private Entity damager;
	
	public EntityDamageByEntityEvent(Entity e, int damageAmount, Entity damager) {
	    super(e, DamageCause.ENTITY_ATTACK, damageAmount);
	    this.damager = damager;
    }
	
	/**
	 * Gets the entity that damaged the entity that was damaged
	 * @return The damaging entity
	 */
	public Entity getDamager() {
		return damager;
	}
	
	/**
	 * Gets the Point position where the event occurred
	 * @return The Point at which the damage happened
	 */
	public Point getPosition() {
		return getEntity().getPosition();
	}
}
