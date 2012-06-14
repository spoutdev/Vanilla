package org.spout.vanilla.event.entity;

import org.spout.api.entity.Entity;
import org.spout.api.event.Cancellable;
import org.spout.api.event.HandlerList;
import org.spout.api.event.entity.EntityEvent;

public class EntityDamageEvent extends EntityEvent implements Cancellable {
	private static HandlerList handlers = new HandlerList();

	private int damageAmount;
	private final DamageCause damageCause;
	private final Entity entity;
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
	
	public EntityDamageEvent(final Entity e, final DamageCause dmgCause, final int damageAmount) {
	    super(e);
	    this.damageCause = dmgCause;
	    this.entity = e;
	    this.damageAmount = damageAmount;
    }
	
	public DamageCause getCause() {
		return damageCause;
	}
	
	public int getDamageAmount() {
		return damageAmount;
	}
	
	public Entity getEntity() {
		return entity;
	}
	
	public void setDamageAmount(final int damageAmount) {
		this.damageAmount = damageAmount;
	}
	
	public boolean isCancelled() {
		return cancelled;
	}
	
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

	@Override
    public HandlerList getHandlers() {
	    return handlers;
    }
	
	public enum DamageCause {
		/**
         * Damage caused when an entity touches a block (e.g. Cactus)
         */
        TOUCH,
        /**
         * Damage caused when an entity attacks another entity.
         */
        ENTITY_ATTACK,
        /**
         * Damage caused when the entity is hit by a projectile.
         */
        PROJECTILE,
        /**
         * Damage caused by being suffocated
         */
        SUFFOCATION,
        /**
         * Damage caused when an entity falls further than 3 blocks
         */
        FALL,
        /**
         * Damage caused by direct exposure to fire
         */
        FIRE,
        /**
         * Damage caused due to burning caused by fire
         */
        FIRE_BURNS,
        /**
         * Damage caused due to melting (a snowman)
         */
        MELTING,
        /**
         * Damage caused by direct exposure to lava
         */
        LAVA,
        /**
         * Damage caused by drowning
         */
        DROWNING,
        /**
         * Damage caused by being blown up
         */
        BLOCK_EXPLOSION,
        /**
         * Damage caused by being in the area when an entity explodes.
         */
        ENTITY_EXPLOSION,
        /**
         * Damage caused by falling through the void
         */
        VOID,
        /**
         * Damage caused by being hit by lightning
         */
        LIGHTNING,
        /**
         * Damage caused by committing suicide
         */
        SUICIDE,
        /**
         * Damage caused by starving
         */
        STARVATION,
        /**
         * Damage caused due to a timed poison effect
         */
        POISON,
        /**
         * Damage caused by being hit by a damage potion or spell
         */
        MAGIC,
        /**
         * Anything else (a plugin maybe)
         */
        OTHER;
	}
}
