package org.spout.vanilla.event.cause;

import org.spout.api.entity.Entity;

public class EntityCombustCause implements CombustCause<Entity> {
	private final Entity entity;

	/**
	 * Contains the source of combustion.
	 * @param block The entity causing the combustion.
	 */
	public EntityCombustCause(Entity entity) {
		this.entity = entity;
	}
	
	@Override
	public Entity getSource() {
		return entity;
	}
}