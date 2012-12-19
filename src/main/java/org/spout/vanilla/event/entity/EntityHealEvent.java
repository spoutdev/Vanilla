package org.spout.vanilla.event.entity;

import org.spout.api.entity.Entity;
import org.spout.api.event.HandlerList;
import org.spout.vanilla.event.cause.HealCause;
import org.spout.vanilla.event.cause.HealthChangeCause;

public class EntityHealEvent extends EntityHealthChangeEvent {
	private static HandlerList handlers = new HandlerList();
	private final HealCause cause;

	public EntityHealEvent(Entity e, int heal) {
		super(e, HealthChangeCause.HEAL, heal);
		this.cause = HealCause.UNKNOWN;
	}

	public EntityHealEvent(Entity e, int heal, HealCause cause) {
		super(e, HealthChangeCause.DAMAGE, heal);
		this.cause = cause;
	}

	/**
	 * Gets the type of damage. Defaults to UNKNOWN.
	 * @return type
	 */
	public HealCause getHealCause() {
		return cause;
	}
	
	/**
	 * Gets the damage dealt to the health component.
	 * @return The damage to the health component.
	 */
	public int getHealAmount() {
		return getChange();
	}

	/**
	 * Sets the damage to be dealt to the health component.
	 * @param damage The amount of damage dealt.
	 */
	public void setHealAmount(int amount) {
		setChange(amount);
	}

	@Override
	public void setCancelled(boolean cancelled) {
		super.setCancelled(cancelled);
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}