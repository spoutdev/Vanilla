package org.getspout.vanilla.entity.objects;

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

}
