/*
 * This file is part of Vanilla (http://www.getspout.org/).
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
package org.getspout.vanilla.entity.projectile;

import org.getspout.api.geo.discrete.Point;
import org.getspout.api.math.Quaternion;

public class Arrow extends Projectile {
	final static int maxArrowSpeed = 10;

	public Arrow(Point start, Quaternion rotation, float charge){
		super(start, rotation, maxArrowSpeed);

		velocity = velocity.scale(charge);
	}

	@Override
	public void onAttached() {
		super.onAttached();
		//TODO arrow model;
	}

	@Override
	public void onTick(float dt) {
		super.onTick(dt);
		//Do Gravity here?
	}
}