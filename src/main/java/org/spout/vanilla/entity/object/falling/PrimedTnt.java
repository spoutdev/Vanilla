/*
 * This file is part of Vanilla (http://www.spout.org/).
 *
 * Vanilla is licensed under the SpoutDev License Version 1.
 *
 * Vanilla is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the SpoutDev License Version 1.
 *
 * Vanilla is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the SpoutDev License Version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license,
 * including the MIT license.
 */
package org.spout.vanilla.entity.object.falling;

import java.util.Random;

import org.spout.api.math.Vector3;
import org.spout.vanilla.entity.object.Falling;

public class PrimedTnt extends Falling {
	private float timeToExplode = 4.f;
	private Vector3 velocity;
	private Vector3 gravity = new Vector3(0,5,0);
	private Random rng = new Random();

	@Override
	public void onAttached() {
		velocity = new Vector3(rng.nextFloat() * 5, rng.nextFloat() * 5, rng.nextFloat() * 5);
	}

	@Override
	public void onTick(float dt) {
		timeToExplode -= dt;
		if (timeToExplode <= 0.f) {
			//Explode
			System.out.print("tnt goes boom!");
		}

		//Move in a random direction and apply gravity.
		getParent().translate(velocity);
		getParent().translate(gravity);
	}

	@Override
	public void preSnapshot() {

	}
}
