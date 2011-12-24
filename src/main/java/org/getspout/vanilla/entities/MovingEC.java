package org.getspout.vanilla.entities;

import org.getspout.api.geo.discrete.Transform;
import org.getspout.api.math.Vector3;

/**
 * Moving entity controller
 */
public class MovingEC extends MinecraftEC {
	protected Vector3 velocity = Vector3.ZERO;

	@Override
	public void onAttached() {
		super.onAttached();
	}

	@Override
	public void onTick(float dt) {
		super.onTick(dt);
		updateMovement(dt);
	}

	private void updateMovement(float dt) {
		Transform t = parent.getTransform();
		t.setPosition(t.getPosition().add(velocity));
		//TODO: collision
	}

}
