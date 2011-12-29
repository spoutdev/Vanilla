package org.getspout.vanilla.entity;

import org.getspout.api.geo.discrete.Point;
import org.getspout.api.geo.discrete.Transform;
import org.getspout.api.material.MaterialData;
import org.getspout.api.math.Vector3;

/**
 * Moving entity controller
 */
public class MovingEntity extends MinecraftEntity {

	protected Vector3 velocity = Vector3.ZERO;
	private int fireTicks;
	private boolean flammable;

	@Override
	public void onAttached() {
		super.onAttached();
	}

	@Override
	public void onTick(float dt) {
		super.onTick(dt);
//		checkWeb();
		updateMovement(dt);
		checkFireTicks();
	}

	private void updateMovement(float dt) {
		Transform t = parent.getTransform();
		t.setPosition(t.getPosition().add(velocity));
		//TODO: collision
	}

	private void checkWeb() {
		Point pos = parent.getTransform().getPosition();
		if (pos.getWorld().getBlock(pos).getBlockMaterial().equals(MaterialData.web)) {
			velocity = Vector3.ZERO;
		}
	}

	private void checkFireTicks() {
		if (fireTicks > 0) {
			if (!flammable) {
				fireTicks -= 4;
				if (fireTicks < 0) {
					fireTicks = 0;
				}
				return;
			}

			if (fireTicks % 20 == 0) {
				//TODO: damage entity
			}
			--fireTicks;
		}
	}

	private void checkLava() {
		//The code checks for lava within the entity's bounding box shrunk by:
		//-0.10000000149011612D, -0.4000000059604645D, -0.10000000149011612D
	}
	
	public boolean isFlammable() {
		return flammable;
	}

	public void setFlammable(boolean flammable) {
		this.flammable = flammable;
	}

	public int getFireTicks() {
		return fireTicks;
	}

	public void setFireTicks(int fireTicks) {
		this.fireTicks = fireTicks;
	}

	public Vector3 getVelocity() {
		return velocity;
	}

	public void setVelocity(Vector3 velocity) {
		this.velocity = velocity;
	}
}
