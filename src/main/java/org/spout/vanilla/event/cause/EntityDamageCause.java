package org.spout.vanilla.event.cause;

import org.spout.api.entity.Entity;

public class EntityDamageCause implements DamageCause<Entity> {

	private final DamageType type;
	private final Entity entity;

	/**
	 * Contains the source and type of damage.
	 * @param entity The entity causing the damage
	 * @param type The cause of the damage.
	 */
	public EntityDamageCause(Entity entity, DamageType type) {
		this.entity = entity;
		this.type = type;
	}

	public DamageType getType() {
		return type;
	}

	@Override
	public Entity getSource() {
		return entity;
	}
}
