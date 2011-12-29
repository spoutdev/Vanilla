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
