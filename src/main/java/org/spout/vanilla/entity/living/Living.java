/*
 * This file is part of Vanilla (http://www.spout.org/).
 *
 * Vanilla is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Vanilla is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.spout.vanilla.entity.living;

import org.spout.api.math.Vector3;
import org.spout.api.protocol.Message;
import org.spout.vanilla.entity.MovingEntity;

/**
 * Represents a living entity controller that moves.
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