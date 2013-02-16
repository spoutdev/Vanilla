/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, Spout LLC <http://www.spout.org/>
 * Vanilla is licensed under the Spout License Version 1.
 *
 * Vanilla is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the Spout License Version 1.
 *
 * Vanilla is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for
 * more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the Spout License Version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://spout.in/licensev1> for the full license, including
 * the MIT license.
 */
package org.spout.vanilla.world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.spout.api.component.Component;
import org.spout.api.entity.Player;
import org.spout.api.geo.LoadOption;
import org.spout.api.geo.World;
import org.spout.api.geo.discrete.Point;
import org.spout.api.math.GenericMath;

import org.spout.vanilla.data.Weather;

import org.spout.vanilla.component.substance.Lightning;
import org.spout.vanilla.data.VanillaData;
import org.spout.vanilla.data.effect.store.GeneralEffects;

public class LightningSimulator extends Component {
	private static final int MAX_LIGHTNING_BRANCHES = 5;
	private static Random ra = new Random();
	final WeatherSimulator weather;
	final HashMap<Player, Integer> playerCountdown = new HashMap<Player, Integer>();

	public LightningSimulator(WeatherSimulator weather) {
		this.weather = weather;
	}

	public World getWorld() {
		return this.weather.getWorld();
	}

	/**
	 * Gets the strength of the thunder storm, which is affected by the duration
	 * @param factor to apply to the changing states
	 * @return the strength
	 */
	public float getThunderStrength(float factor) {
		float currentThunderStrength = weather.getSky().getData().get(VanillaData.CURRENT_LIGHTNING_STRENGTH);
		float previousThunderStrength = weather.getSky().getData().get(VanillaData.PREVIOUS_LIGHTNING_STRENGTH);
		return (previousThunderStrength + factor * (currentThunderStrength - previousThunderStrength));
	}

	@Override
	public boolean canTick() {
		return true;
	}

	@Override
	public void onTick(float dt) {
		float currentThunderStrength = weather.getSky().getData().get(VanillaData.CURRENT_LIGHTNING_STRENGTH);
		weather.getSky().getData().put(VanillaData.PREVIOUS_LIGHTNING_STRENGTH, currentThunderStrength);
		if (this.weather.isThundering()) {
			currentThunderStrength = Math.min(1.0f, currentThunderStrength + 0.01f);
		} else {
			currentThunderStrength = Math.max(0.0f, currentThunderStrength - 0.01f);
		}
		weather.getSky().getData().put(VanillaData.CURRENT_LIGHTNING_STRENGTH, currentThunderStrength);
		try {
			updatePlayerTimers();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updatePlayerTimers() {
		if (this.weather.getCurrent() == Weather.THUNDERSTORM) {
			if (getIntensity() == null) {
				setIntensity(Intensity.getRandomIntensity(ra, false));
			}
			List<Player> toStrike = new ArrayList<Player>();
			for (Player player : getWorld().getPlayers()) {
				if (!player.isOnline()) {
					continue;
				}
				Integer ticksLeft = playerCountdown.get(player);
				if (ticksLeft == null) {
					playerCountdown.put(player, getTicksBeforeNextLightning(ra));
				} else if (ticksLeft == 1) {
					//weed out dc'd players
					if (player.isOnline()) {
						toStrike.add(player);
						playerCountdown.put(player, getTicksBeforeNextLightning(ra));
					}
				} else {
					playerCountdown.put(player, ticksLeft - 1);
				}
			}
			strikePlayers(toStrike);
		} else {
			setIntensity(null);
		}
	}

	public void strikePlayers(List<Player> toStrike) {
		for (Player player : toStrike) {
			Point playerPos = player.getScene().getPosition();
			final int posX = GenericMath.floor(playerPos.getX());
			final int posY = GenericMath.floor(playerPos.getY());
			final int posZ = GenericMath.floor(playerPos.getZ());
			for (int tries = 0; tries < 10; tries++) {
				//pick a random chunk between -4, -4, to 4, 4 relative to the player's position to strike at
				int cx = (ra.nextBoolean() ? -1 : 1) * ra.nextInt(5);
				int cz = (ra.nextBoolean() ? -1 : 1) * ra.nextInt(5);

				//pick random coords to try to strike at inside the chunk (0, 0) to (15, 15)
				int rx = ra.nextInt(16);
				int rz = ra.nextInt(16);

				//pick a offset from the player's y position to strike at (-15 - +15) of their position
				int offsetY = (ra.nextBoolean() ? -1 : 1) * ra.nextInt(15);

				int x = posX + cx * 16 + rx;
				int y = posY + offsetY;
				int z = posZ + cz * 16 + rz;

				if (weather.isRainingAt(x, y, z, false)) {
					int lightning = 1;
					//30% chance of extra lightning at the spot
					if (ra.nextInt(10) < 3) {
						lightning += ra.nextInt(MAX_LIGHTNING_BRANCHES);
					}
					for (int strikes = 0; strikes < lightning; strikes++) {
						float adjustX = 0.5F;
						float adjustY = 0.0F;
						float adjustZ = 0.5F;
						//if there are extra strikes, tweak their placement slightly
						if (strikes > 0) {
							adjustX += (ra.nextBoolean() ? -1 : 1) * ra.nextInt(2);
							adjustY += (ra.nextBoolean() ? -1 : 1) * ra.nextInt(8);
							adjustZ += (ra.nextBoolean() ? -1 : 1) * ra.nextInt(2);
						}
						World world = getWorld();
						Point point = new Point(world, x + adjustX, y + adjustY, z + adjustZ);
						world.createAndSpawnEntity(point, Lightning.class, LoadOption.NO_LOAD);
						for (Player p : GeneralEffects.LIGHTNING_THUNDER.getNearbyPlayers(point, null, 600)) {
							double dist = p.getScene().getPosition().distanceSquared(point);
							float volume = (float) (10000F - Math.pow(dist, 0.73));
							if (volume > 0) {
								GeneralEffects.LIGHTNING_THUNDER.adjust(volume, 0.7F).play(p, point);
							}
						}
					}
					//success, go to the next player
					break;
				}
			}
		}
	}

	public void setIntensity(Intensity in) {
		weather.getSky().getData().put(VanillaData.STORM_INTENSITY, in != null ? in.ordinal() : -1);
	}

	public Intensity getIntensity() {
		int intensity = weather.getSky().getData().get(VanillaData.STORM_INTENSITY);
		if (intensity >= 0 && intensity < Intensity.values().length) {
			return Intensity.values()[intensity];
		}
		return null;
	}

	public int getTicksBeforeNextLightning(Random rand) {
		return getIntensity().baseTicks + rand.nextInt(getIntensity().randomTicks);
	}

	public static enum Intensity {
		STRONG_ELECTRICAL_STORM(3, 6),
		ELECTRICAL_STORM(10, 20),
		STRONG_THUNDERSTORM(20, 80),
		THUNDERSTORM(50, 160),
		WEAK_THUNDERSTORM(100, 500),
		RAINSTORM(200, 1000);
		final int baseTicks, randomTicks;

		Intensity(int baseTicks, int randomTicks) {
			this.baseTicks = baseTicks;
			this.randomTicks = randomTicks;
		}

		public static Intensity getRandomIntensity(Random rand, boolean override) {
			int r = rand.nextInt(100);
			if (r < 5 || override) {
				return STRONG_ELECTRICAL_STORM;
			}
			if (r < 15) {
				return ELECTRICAL_STORM;
			}
			if (r < 30) {
				return STRONG_THUNDERSTORM;
			}
			if (r < 50) {
				return THUNDERSTORM;
			}
			if (r < 75) {
				return WEAK_THUNDERSTORM;
			}
			return RAINSTORM;
		}
	}
}
