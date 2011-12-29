/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.getspout.vanilla.entity.living;

import org.getspout.api.math.Vector3;
import org.getspout.vanilla.entity.MovingEntity;

/**
 *
 * @author simplyianm
 */
public class Living extends MovingEntity {
	private static float GRAVITY_MULTIPLIER = 23.31f;

	private boolean gravity = true;

	@Override
	public void onAttached() {
		super.onAttached();
	}

	@Override
	public void onTick(float dt) {
		super.onTick(dt);

		if (gravity) {
			updateGravity(dt);
		}
	}

	protected void updateGravity(float dt) {
		velocity.add(Vector3.Up.scale(-(dt * GRAVITY_MULTIPLIER)));
	}

	public boolean isGravity() {
		return gravity;
	}

	public void setGravity(boolean gravity) {
		this.gravity = gravity;
	}

}
