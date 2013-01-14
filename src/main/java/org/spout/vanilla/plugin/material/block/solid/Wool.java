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
package org.spout.vanilla.plugin.material.block.solid;

import java.util.HashMap;
import java.util.Map;

import org.spout.api.material.BlockMaterial;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.source.DataSource;

import org.spout.vanilla.api.material.Burnable;
import org.spout.vanilla.plugin.data.effect.store.SoundEffects;
import org.spout.vanilla.plugin.material.VanillaMaterials;
import org.spout.vanilla.plugin.material.block.Solid;
import org.spout.vanilla.plugin.resources.VanillaMaterialModels;

public class Wool extends Solid implements Burnable {
	public static final Wool WHITE_WOOL = new Wool("White Wool", VanillaMaterialModels.WOOL_WHITE);
	public static final Wool ORANGE_WOOL = new Wool("Orange Wool", WoolColor.ORANGE, WHITE_WOOL, VanillaMaterialModels.WOOL_ORANGE);
	public static final Wool MAGENTA_WOOL = new Wool("Magenta Wool", WoolColor.MAGENTA, WHITE_WOOL, VanillaMaterialModels.WOOL_MAGENTA);
	public static final Wool LIGHTBLUE_WOOL = new Wool("Light Blue Wool", WoolColor.LIGHTBLUE, WHITE_WOOL, VanillaMaterialModels.WOOL_LIGHTBLUE);
	public static final Wool YELLOW_WOOL = new Wool("Yellow Wool", WoolColor.YELLOW, WHITE_WOOL, VanillaMaterialModels.WOOL_YELLOW);
	public static final Wool LIME_WOOL = new Wool("Lime Wool", WoolColor.LIME, WHITE_WOOL, VanillaMaterialModels.WOOL_LIME);
	public static final Wool PINK_WOOL = new Wool("Pink Wool", WoolColor.PINK, WHITE_WOOL, VanillaMaterialModels.WOOL_PINK);
	public static final Wool GRAY_WOOL = new Wool("Gray Wool", WoolColor.GRAY, WHITE_WOOL, VanillaMaterialModels.WOOL_GRAY);
	public static final Wool SILVER_WOOL = new Wool("Light Gray Wool", WoolColor.SILVER, WHITE_WOOL, VanillaMaterialModels.WOOL_SILVER);
	public static final Wool CYAN_WOOL = new Wool("Cyan Wool", WoolColor.CYAN, WHITE_WOOL, VanillaMaterialModels.WOOL_CYAN);
	public static final Wool PURPLE_WOOL = new Wool("Purple Wool", WoolColor.PURPLE, WHITE_WOOL, VanillaMaterialModels.WOOL_PURPLE);
	public static final Wool BLUE_WOOL = new Wool("Blue Wool", WoolColor.BLUE, WHITE_WOOL, VanillaMaterialModels.WOOL_BLUE);
	public static final Wool BROWN_WOOL = new Wool("Brown Wool", WoolColor.BROWN, WHITE_WOOL, VanillaMaterialModels.WOOL_BROWN);
	public static final Wool GREEN_WOOL = new Wool("Green Wool", WoolColor.GREEN, WHITE_WOOL, VanillaMaterialModels.WOOL_GREEN);
	public static final Wool RED_WOOL = new Wool("Red Wool", WoolColor.RED, WHITE_WOOL, VanillaMaterialModels.WOOL_RED);
	public static final Wool BLACK_WOOL = new Wool("Black Wool", WoolColor.BLACK, WHITE_WOOL, VanillaMaterialModels.WOOL_BLACK);
	private final WoolColor color;

	private Wool(String name, String model) {
		super((short) 0x000F, name, 35, model);
		this.color = WoolColor.WHITE;
		this.setHardness(0.8F).setResistance(1.3F).setStepSound(SoundEffects.STEP_CLOTH);
	}

	private Wool(String name, WoolColor color, Wool parent, String model) {
		super(name, 35, color.getData(), parent, model);
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

	@Override
	public Wool getParentMaterial() {
		return (Wool) super.getParentMaterial();
	}

	@Override
	public boolean canSupport(BlockMaterial material, BlockFace face) {
		if (material.equals(VanillaMaterials.FIRE)) {
			return true;
		} else {
			return super.canSupport(material, face);
		}
	}

	public static enum WoolColor implements DataSource {
		WHITE(0),
		ORANGE(1),
		MAGENTA(2),
		LIGHTBLUE(3),
		YELLOW(4),
		LIME(5),
		PINK(6),
		GRAY(7),
		SILVER(8),
		CYAN(9),
		PURPLE(10),
		BLUE(11),
		BROWN(12),
		GREEN(13),
		RED(14),
		BLACK(15);
		private final short data;
		private static final Map<Short, WoolColor> ids = new HashMap<Short, WoolColor>();

		private WoolColor(int data) {
			this.data = (short) data;
		}

		@Override
		public short getData() {
			return this.data;
		}

		public static WoolColor getById(short id) {
			return ids.get(id);
		}

		static {
			for (WoolColor color : WoolColor.values()) {
				ids.put(color.getData(), color);
			}
		}
	}
}
