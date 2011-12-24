package org.getspout.vanilla.entities;

import org.getspout.api.math.Vector3;

/**
 * Represents a land entity controller; has gravity.
 */
public class LandEC extends MovingEC {
	private static float GRAVITY_MULTIPLIER = 23.31f;

	@Override
	public void onAttached() {
		super.onAttached();
	}

	@Override
	public void onTick(float dt) {
		super.onTick(dt);
		updateGravity(dt);
	}

	public void updateGravity(float dt) {
		velocity.add(Vector3.Up.scale(-(dt * GRAVITY_MULTIPLIER)));
	}

}
