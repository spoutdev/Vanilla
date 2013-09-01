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

import org.spout.vanilla.data.resources.VanillaMaterialModels;
import org.spout.vanilla.data.tool.ToolType;
import org.spout.vanilla.material.block.Solid;
import org.spout.vanilla.material.block.solid.Wool.WoolColor;

public class StainedClay extends Solid {
	public static final StainedClay WHITE_STAINED_CLAY = new StainedClay("White Stained Clay", VanillaMaterialModels.STAINED_CLAY_WHITE);
	public static final StainedClay ORANGE_STAINED_CLAY = new StainedClay("Orange Stained Clay", WoolColor.ORANGE, WHITE_STAINED_CLAY, VanillaMaterialModels.STAINED_CLAY_ORANGE);
	public static final StainedClay MAGENTA_STAINED_CLAY = new StainedClay("Magenta Stained Clay", WoolColor.MAGENTA, WHITE_STAINED_CLAY, VanillaMaterialModels.STAINED_CLAY_MAGENTA);
	public static final StainedClay LIGHTBLUE_STAINED_CLAY = new StainedClay("Light Blue Stained Clay", WoolColor.LIGHTBLUE, WHITE_STAINED_CLAY, VanillaMaterialModels.STAINED_CLAY_LIGHTBLUE);
	public static final StainedClay YELLOW_STAINED_CLAY = new StainedClay("Yellow Stained Clay", WoolColor.YELLOW, WHITE_STAINED_CLAY, VanillaMaterialModels.STAINED_CLAY_YELLOW);
	public static final StainedClay LIME_STAINED_CLAY = new StainedClay("Lime Stained Clay", WoolColor.LIME, WHITE_STAINED_CLAY, VanillaMaterialModels.STAINED_CLAY_LIME);
	public static final StainedClay PINK_STAINED_CLAY = new StainedClay("Pink Stained Clay", WoolColor.PINK, WHITE_STAINED_CLAY, VanillaMaterialModels.STAINED_CLAY_PINK);
	public static final StainedClay GRAY_STAINED_CLAY = new StainedClay("Gray Stained Clay", WoolColor.GRAY, WHITE_STAINED_CLAY, VanillaMaterialModels.STAINED_CLAY_GRAY);
	public static final StainedClay SILVER_STAINED_CLAY = new StainedClay("Light Gray Stained Clay", WoolColor.SILVER, WHITE_STAINED_CLAY, VanillaMaterialModels.STAINED_CLAY_SILVER);
	public static final StainedClay CYAN_STAINED_CLAY = new StainedClay("Cyan Stained Clay", WoolColor.CYAN, WHITE_STAINED_CLAY, VanillaMaterialModels.STAINED_CLAY_CYAN);
	public static final StainedClay PURPLE_STAINED_CLAY = new StainedClay("Purple Stained Clay", WoolColor.PURPLE, WHITE_STAINED_CLAY, VanillaMaterialModels.STAINED_CLAY_PURPLE);
	public static final StainedClay BLUE_STAINED_CLAY = new StainedClay("Blue Stained Clay", WoolColor.BLUE, WHITE_STAINED_CLAY, VanillaMaterialModels.STAINED_CLAY_BLUE);
	public static final StainedClay BROWN_STAINED_CLAY = new StainedClay("Brown Stained Clay", WoolColor.BROWN, WHITE_STAINED_CLAY, VanillaMaterialModels.STAINED_CLAY_BROWN);
	public static final StainedClay GREEN_STAINED_CLAY = new StainedClay("Green Stained Clay", WoolColor.GREEN, WHITE_STAINED_CLAY, VanillaMaterialModels.STAINED_CLAY_GREEN);
	public static final StainedClay RED_STAINED_CLAY = new StainedClay("Red Stained Clay", WoolColor.RED, WHITE_STAINED_CLAY, VanillaMaterialModels.STAINED_CLAY_RED);
	public static final StainedClay BLACK_STAINED_CLAY = new StainedClay("Black Stained Clay", WoolColor.BLACK, WHITE_STAINED_CLAY, VanillaMaterialModels.STAINED_CLAY_BLACK);
	private final WoolColor color;

	private StainedClay(String name, String model) {
		super((short) 0x000F, name, 159, model);
		this.color = WoolColor.WHITE;
		this.setHardness(0.5F).setResistance(30.0f);
	}

	private StainedClay(String name, Wool.WoolColor color, StainedClay parent, String model) {
		super(name, 159, color.getData(), parent, model);
		this.color = color;
		this.setHardness(0.5F).setResistance(30.0f);
		this.addMiningType(ToolType.PICKAXE);
	}
	// TODO: This block is immune to fire/fire spread
}
