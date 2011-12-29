package org.getspout.vanilla.entity.sky;

import java.util.Random;

import org.getspout.api.entity.Controller;
import org.getspout.vanilla.world.Weather;

public class NormalSky extends Controller {

	final String weatherID = "weatherID";
	final String timeKey = "worldTime";
	
	final float maxTime = 12000;
	final float gameSecondsPerSecond = 1.f/0.013f; // ~0.013 MCseconds per RL second. 
	float time = 0;
	
	
	
	
	Weather currentWeather;
	Weather forcast;
	float timeUntilWeatherChange = 0.0f;
	
	
	Random rng = new Random();
	
	@Override
	public void onAttached() {
		currentWeather = Weather.CLEAR;
		forcast = Weather.CLEAR;
		timeUntilWeatherChange = rng.nextFloat() * 300; //Max 5min till pattern change
	}

	@Override
	public void onTick(float dt) {
		time = (time + gameSecondsPerSecond * dt) % maxTime;
		
		
		
		
		
		timeUntilWeatherChange -= dt;
		if(timeUntilWeatherChange <= 0.0f){
			changeWeatherPattern(forcast);
			switch(rng.nextInt(3)){
			case 0:
				forcast = Weather.CLEAR;
				break;
			case 1:
				forcast = Weather.RAIN;
				break;
			case 2:
				forcast = Weather.THUNDERSTORM;
				break;
			}
		}
		
		
	}
	
	public void changeWeatherPattern(Weather pattern){
		currentWeather = pattern;
	}
	
	

}
