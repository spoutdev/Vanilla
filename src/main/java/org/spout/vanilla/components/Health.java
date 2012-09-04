package org.spout.vanilla.components;

import org.spout.api.Source;
import org.spout.api.Spout;
import org.spout.api.component.components.EntityComponent;
import org.spout.api.entity.Entity;
import org.spout.api.event.entity.EntityHealthChangeEvent;

import org.spout.vanilla.data.VanillaData;
import org.spout.vanilla.source.DamageCause;
import org.spout.vanilla.source.HealthChangeCause;

public class Health extends EntityComponent {
	//animation
	public boolean hasDeathAnimation = true;
	//damage
	private DamageCause lastDamageCause = DamageCause.UNKNOWN;
	private Entity lastDamager;
	private int deathTicks;

	public Health() {

	}

	/**
	 * Gets the last cause of the damage
	 * @return the last damager
	 */
	public DamageCause getLastDamageCause() {
		return lastDamageCause;
	}

	/**
	 * Gets the last entity that damages this entity
	 * @return last damager
	 */
	public Entity getLastDamager() {
		return lastDamager;
	}

	/**
	 * Gets the maximum health this entity can have
	 * @return the maximum health
	 */
	public int getMaxHealth() {
		return getDatatable().get(VanillaData.MAX_HEALTH);
	}

	/**
	 * Sets the maximum health this entity can have
	 * @param maxHealth to set to
	 */
	public void setMaxHealth(int maxHealth) {
		getDatatable().put(VanillaData.MAX_HEALTH, maxHealth);
	}

	/**
	 * Sets the initial maximum health and sets the health to this value
	 * @param maxHealth of this health component
	 */
	public void setSpawnHealth(int maxHealth) {
		this.setMaxHealth(maxHealth);
		this.setHealth(maxHealth, HealthChangeCause.SPAWN);
	}

	/**
	 * Gets the health of this entity (hitpoints)
	 * @return the health value
	 */
	public int getHealth() {
		return getDatatable().get(VanillaData.HEALTH);
	}

	/**
	 * Sets the current health value for this entity
	 * @param health hitpoints value to set to
	 * @param source of the change
	 */
	public void setHealth(int health, Source source) {
		EntityHealthChangeEvent event = new EntityHealthChangeEvent(getHolder(), source, health - getHealth());
		Spout.getEngine().getEventManager().callEvent(event);
		if (!event.isCancelled()) {
			if (getHealth() + event.getChange() > getMaxHealth()) {
				getDatatable().put(VanillaData.HEALTH, getMaxHealth());
			} else {
				getDatatable().put(VanillaData.HEALTH, getHealth() + event.getChange());
			}
		}
	}

	/**
	 * Sets the health value to 0
	 * @param source of the change
	 */
	public void kill(Source source) {
		setHealth(0, source);
	}

	/**
	 * Returns true if the entity is equal to or less than zero health remaining
	 * @return dead
	 */
	public boolean isDead() {
		return getDatatable().get(VanillaData.HEALTH) <= 0;
	}

	/**
	 * Returns true if the entity is dying
	 * @return dying
	 */
	public boolean isDying() {
		return deathTicks > 0;
	}	
}
