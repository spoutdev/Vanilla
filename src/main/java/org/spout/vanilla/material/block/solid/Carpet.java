/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011 Spout LLC <http://www.spout.org/>
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
package org.spout.vanilla.material.block.solid;

import org.spout.vanilla.data.effect.store.SoundEffects;
import org.spout.vanilla.data.resources.VanillaMaterialModels;
import org.spout.vanilla.material.Burnable;
import org.spout.vanilla.material.block.Solid;
import org.spout.vanilla.material.block.solid.Wool.WoolColor;

public class Carpet extends Solid implements Burnable {
	public static final Carpet WHITE_CARPET = new Carpet("White Carpet", VanillaMaterialModels.CARPET);
	public static final Carpet ORANGE_CARPET = new Carpet("Orange Carpet", WoolColor.ORANGE, WHITE_CARPET, VanillaMaterialModels.CARPET_ORANGE);
	public static final Carpet MAGENTA_CARPET = new Carpet("Magenta Carpet", WoolColor.MAGENTA, WHITE_CARPET, VanillaMaterialModels.CARPET_MAGENTA);
	public static final Carpet LIGHTBLUE_CARPET = new Carpet("Light Blue Carpet", WoolColor.LIGHTBLUE, WHITE_CARPET, VanillaMaterialModels.CARPET_LIGHTBLUE);
	public static final Carpet YELLOW_CARPET = new Carpet("Yellow Carpet", WoolColor.YELLOW, WHITE_CARPET, VanillaMaterialModels.CARPET_YELLOW);
	public static final Carpet LIME_CARPET = new Carpet("Lime Carpet", WoolColor.LIME, WHITE_CARPET, VanillaMaterialModels.CARPET_LIME);
	public static final Carpet PINK_CARPET = new Carpet("Pink Carpet", WoolColor.PINK, WHITE_CARPET, VanillaMaterialModels.CARPET_PINK);
	public static final Carpet GRAY_CARPET = new Carpet("Gray Carpet", WoolColor.GRAY, WHITE_CARPET, VanillaMaterialModels.CARPET_GRAY);
	public static final Carpet SILVER_CARPET = new Carpet("Light Gray Carpet", WoolColor.SILVER, WHITE_CARPET, VanillaMaterialModels.CARPET_SILVER);
	public static final Carpet CYAN_CARPET = new Carpet("Cyan Carpet", WoolColor.CYAN, WHITE_CARPET, VanillaMaterialModels.CARPET_CYAN);
	public static final Carpet PURPLE_CARPET = new Carpet("Purple Carpet", WoolColor.PURPLE, WHITE_CARPET, VanillaMaterialModels.CARPET_PURPLE);
	public static final Carpet BLUE_CARPET = new Carpet("Blue Carpet", WoolColor.BLUE, WHITE_CARPET, VanillaMaterialModels.CARPET_BLUE);
	public static final Carpet BROWN_CARPET = new Carpet("Brown Carpet", WoolColor.BROWN, WHITE_CARPET, VanillaMaterialModels.CARPET_BROWN);
	public static final Carpet GREEN_CARPET = new Carpet("Green Carpet", WoolColor.GREEN, WHITE_CARPET, VanillaMaterialModels.CARPET_GREEN);
	public static final Carpet RED_CARPET = new Carpet("Red Carpet", WoolColor.RED, WHITE_CARPET, VanillaMaterialModels.CARPET_RED);
	public static final Carpet BLACK_CARPET = new Carpet("Black Carpet", WoolColor.BLACK, WHITE_CARPET, VanillaMaterialModels.CARPET_BLACK);
	private final WoolColor color;

	private Carpet(String name, String model) {
		super((short) 0x000F, name, 171, model);
		this.color = WoolColor.WHITE;
		this.setHardness(0.8F).setResistance(1.3F).setStepSound(SoundEffects.STEP_CLOTH);
	}

	private Carpet(String name, WoolColor color, Carpet parent, String model) {
		super(name, 171, color.getData(), parent, model);
		this.color = color;
		this.setHardness(0.8F).setResistance(1.3F).setStepSound(SoundEffects.STEP_CLOTH);
	}

	@Override
	public int getBurnPower() {
		return 30;
	}

	@Override
	public int getCombustChance() {
		return 60;
	}

	public WoolColor getColor() {
		return color;
	}
}
