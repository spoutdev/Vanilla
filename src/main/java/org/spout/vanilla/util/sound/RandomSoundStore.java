/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, VanillaDev <http://www.spout.org/>
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
package org.spout.vanilla.util.sound;

//TODO: Add the mob random sounds
public class RandomSoundStore {
	public static final RandomSound RANDOM_BOWHIT = new RandomSound(Sound.RANDOM_BOWHIT1, Sound.RANDOM_BOWHIT2, Sound.RANDOM_BOWHIT3, Sound.RANDOM_BOWHIT4);
	public static final RandomSound RANDOM_EAT = new RandomSound(Sound.RANDOM_EAT1, Sound.RANDOM_EAT2, Sound.RANDOM_EAT3);
	public static final RandomSound RANDOM_GLASS = new RandomSound(Sound.RANDOM_GLASS1, Sound.RANDOM_GLASS2, Sound.RANDOM_GLASS3);
	public static final RandomSound RANDOM_EXPLODE = new RandomSound(Sound.RANDOM_EXPLODE1, Sound.RANDOM_EXPLODE2, Sound.RANDOM_EXPLODE3, Sound.RANDOM_EXPLODE4);
	public static final RandomSound RANDOM_DOOR = new RandomSound(Sound.RANDOM_DOOR_OPEN, Sound.RANDOM_DOOR_CLOSE);
	public static final RandomSound AMBIENT_CAVE = new RandomSound(Sound.AMBIENT_CAVE_CAVE1, Sound.AMBIENT_CAVE_CAVE2, Sound.AMBIENT_CAVE_CAVE3, 
			Sound.AMBIENT_CAVE_CAVE4, Sound.AMBIENT_CAVE_CAVE5, Sound.AMBIENT_CAVE_CAVE6, Sound.AMBIENT_CAVE_CAVE7, Sound.AMBIENT_CAVE_CAVE8, 
			Sound.AMBIENT_CAVE_CAVE9, Sound.AMBIENT_CAVE_CAVE10, Sound.AMBIENT_CAVE_CAVE11, Sound.AMBIENT_CAVE_CAVE12, Sound.AMBIENT_CAVE_CAVE13);

	/*
	 * Step sounds
	 */
	public static final RandomSound STEP_CLOTH = new RandomSound(Sound.STEP_CLOTH1, Sound.STEP_CLOTH2, Sound.STEP_CLOTH3, Sound.STEP_CLOTH4);
	public static final RandomSound STEP_GRASS = new RandomSound(Sound.STEP_GRASS1, Sound.STEP_GRASS2, Sound.STEP_GRASS3, Sound.STEP_GRASS4);
	public static final RandomSound STEP_GRAVEL = new RandomSound(Sound.STEP_GRAVEL1, Sound.STEP_GRAVEL2, Sound.STEP_GRAVEL3, Sound.STEP_GRAVEL4);
	public static final RandomSound STEP_SAND = new RandomSound(Sound.STEP_SAND1, Sound.STEP_SAND2, Sound.STEP_SAND3, Sound.STEP_SAND4);
	public static final RandomSound STEP_SNOW = new RandomSound(Sound.STEP_SNOW1, Sound.STEP_SNOW2, Sound.STEP_SNOW3, Sound.STEP_SNOW4);
	public static final RandomSound STEP_STONE = new RandomSound(Sound.STEP_STONE1, Sound.STEP_STONE2, Sound.STEP_STONE3, Sound.STEP_STONE4);
	public static final RandomSound STEP_WOOD = new RandomSound(Sound.STEP_WOOD1, Sound.STEP_WOOD2, Sound.STEP_WOOD3, Sound.STEP_WOOD4);
}
