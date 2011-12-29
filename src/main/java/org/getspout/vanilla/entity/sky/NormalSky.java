package org.getspout.vanilla.entity.sky;

import org.getspout.api.entity.Controller;

public class NormalSky extends Controller {

	final float maxTime = 12000;
	final float gameSecondsPerSecond = 76.92f; // ~0.013 MCseconds per RL second.  1/0.013 RL second to MC second
	float time = 0;
	
	
	@Override
	public void onAttached() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTick(float dt) {
		time = (time + gameSecondsPerSecond * dt) % maxTime;

	}

}
