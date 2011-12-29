package org.getspout.vanilla.entity.projectile;

import org.getspout.api.entity.Controller;
import org.getspout.api.geo.discrete.Point;
import org.getspout.api.geo.discrete.Transform;
import org.getspout.api.math.Quaternion;
import org.getspout.api.math.Vector3;

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

}
