package org.spout.vanilla.plugin.component.misc;

import org.spout.api.component.type.EntityComponent;
import org.spout.vanilla.plugin.component.living.Living;
import org.spout.vanilla.plugin.data.VanillaData;


public class FireComponent extends EntityComponent {

	private float internalTimer = 0.0f;
	private HealthComponent health;
	private Living living;
	
	@Override
	public void onAttached() {
		health = getOwner().add(HealthComponent.class);
		living = getOwner().get(Living.class);
	}
	
	@Override
	public boolean canTick() {
		return getFireTick() >= 0 && !health.isDead();
	}
	
	@Override
	public void onTick(float dt) {
		living.sendMetaData();
		if (isFireHurting()) {
			if (internalTimer >= 1.0f) {
				health.damage(1);
				internalTimer = 0;
			}
		}
		setFireTick(getFireTick() - dt);
		if (getFireTick() <= 0) {
			living.sendMetaData();
		}
		internalTimer += dt;
	}
	
	private void setFireTick(float fireTick) {
		getOwner().getData().put(VanillaData.FIRE_TICK, fireTick);
	}
	
	public float getFireTick() {
		return getOwner().getData().get(VanillaData.FIRE_TICK);
	}
	
	public boolean isOnFire() {
		return getFireTick() > 0;
	}
	
	private boolean isFireHurting() {
		return getOwner().getData().get(VanillaData.FIRE_HURT);
	}

	/**
	 * Sets the entity on fire.
	 * @param time The amount of time in seconds the entity should be on fire.
	 * @param hurt
	 */
	public void setOnFire(float time, boolean hurt) {
		getOwner().getData().put(VanillaData.FIRE_TICK, time);
		getOwner().getData().put(VanillaData.FIRE_HURT, hurt);
		living.sendMetaData();
	}
}
