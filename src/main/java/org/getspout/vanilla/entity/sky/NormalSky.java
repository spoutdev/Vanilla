package org.getspout.vanilla.entity.sky;

import java.util.Random;

import org.getspout.api.entity.Controller;
import org.getspout.vanilla.world.Weather;

public class NormalSky extends Controller {

	final String weatherKey = "weather";
	final String timeKey = "worldTime";
	
	final float maxTime = 12000;
	final float gameSecondsPerSecond = 1.f/0.013f; // ~0.013 MCseconds per RL second. 
	float time = 0;
	
	
	
	
	Weather currentWeather;
	Weather forecast;
	float timeUntilWeatherChange = 0.0f;
	
	
	Random rng = new Random();
	
	@Override
	public void onAttached() {
		currentWeather = Weather.CLEAR;
		forecast = Weather.CLEAR;
		timeUntilWeatherChange = rng.nextFloat() * 300; //Max 5min till pattern change
	}

	@Override
	public void onTick(float dt) {
		time = (time + gameSecondsPerSecond * dt) % maxTime;
		
		
		
		
		
		timeUntilWeatherChange -= dt;
		if(timeUntilWeatherChange <= 0.0f){
			changeWeatherPattern(forecast);
			switch(rng.nextInt(3)){
			case 0:
				forecast = Weather.CLEAR;
				break;
			case 1:
				forecast = Weather.RAIN;
				break;
			case 2:
				forecast = Weather.THUNDERSTORM;
				break;
			}
		}
		
		parent.setMetadata(timeKey, time);
		parent.setMetadata(weatherKey, currentWeather.getId());
	}
	
	public void changeWeatherPattern(Weather pattern){
		currentWeather = pattern;
	}
	
	

}
