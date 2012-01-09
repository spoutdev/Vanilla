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
package org.spout.vanilla.entity.projectile;

import org.spout.api.entity.Controller;
import org.spout.api.geo.discrete.Point;
import org.spout.api.geo.discrete.Transform;
import org.spout.api.math.Quaternion;
import org.spout.api.math.Vector3;

public class Projectile extends Controller {
	Point start;
	Quaternion rotation;

	final int maxSpeed;

	protected Vector3 velocity;

	public Projectile(Point start, Quaternion rotation, int maxSpeed){
		this.maxSpeed = maxSpeed;
		this.start = start;
		this.rotation = rotation;

		velocity = new Vector3(maxSpeed, 0, 0);
	}

	@Override
	public void onAttached() {
		parent.getTransform().setRotation(rotation);
		parent.getTransform().setPosition(start);

	}

	@Override
	public void onTick(float dt) {
		Transform t = parent.getTransform();
		//position += velocity.transform(rotation) * dt;
		t.setPosition(t.getPosition().add(velocity.transform(t.getRotation()).scale(dt)));
	}
	
	public void preSnapshot() {
	}
}