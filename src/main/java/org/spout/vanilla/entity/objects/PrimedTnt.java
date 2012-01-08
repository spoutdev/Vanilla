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
package org.spout.vanilla.entity.objects;

import java.util.Random;

import org.getspout.api.entity.Controller;
import org.getspout.api.geo.discrete.Transform;
import org.getspout.api.math.Vector3;

public class PrimedTnt extends Controller {
	float timeToExplode = 4.f;

	Vector3 velocity;
	Vector3 gravity = new Vector3(0,-5, 0);
	Random rng = new Random();

	@Override
	public void onAttached() {
		velocity = new Vector3(rng.nextFloat() * 5, rng.nextFloat()*5, rng.nextFloat() * 5);

	}

	@Override
	public void onTick(float dt) {
		timeToExplode -= dt;

		if(timeToExplode <= 0.f){
			//Explode
			System.out.print("tnt goes boom");
		}

		//Move in a random direction and apply gravity.
		Transform t = parent.getTransform();
		t.setPosition(t.getPosition().add(velocity.scale(dt)).add(gravity.scale(dt)));

	}
	
	public void preSnapshot() {
	}
}