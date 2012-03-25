/*
 * This file is part of Vanilla (http://www.spout.org/).
 *
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
package org.spout.vanilla.material.block;

import org.spout.api.material.source.DataSource;
import org.spout.vanilla.material.MovingBlock;
import org.spout.vanilla.material.generic.GenericBlock;

import java.util.HashMap;
import java.util.Map;

public class Wool extends GenericBlock implements MovingBlock {
	public static final Wool WHITE = register(new Wool("White Wool"));
	public static final Wool ORANGE = register(new Wool("Orange Wool", WoolColor.Orange, WHITE));
	public static final Wool MAGENTA = register(new Wool("Magenta Wool", WoolColor.Magenta, WHITE));
	public static final Wool LIGHTBLUE = register(new Wool("Light Blue Wool", WoolColor.LightBlue, WHITE));
	public static final Wool YELLOW = register(new Wool("Yellow Wool", WoolColor.Yellow, WHITE));
	public static final Wool LIME = register(new Wool("Lime Wool", WoolColor.Lime, WHITE));
	public static final Wool PINK = register(new Wool("Pink Wool", WoolColor.Pink, WHITE));
	public static final Wool GRAY = register(new Wool("Gray Wool", WoolColor.Gray, WHITE));
	public static final Wool SILVER = register(new Wool("Silver Wool", WoolColor.Silver, WHITE));
	public static final Wool CYAN = register(new Wool("Cyan Wool", WoolColor.Cyan, WHITE));
	public static final Wool PURPLE = register(new Wool("Purple Wool", WoolColor.Purple, WHITE));
	public static final Wool BLUE = register(new Wool("Blue Wool", WoolColor.Blue, WHITE));
	public static final Wool BROWN = register(new Wool("Brown Wool", WoolColor.Brown, WHITE));
	public static final Wool GREEN = register(new Wool("Green Wool", WoolColor.Green, WHITE));
	public static final Wool RED = register(new Wool("Red Wool", WoolColor.Red, WHITE));
	public static final Wool BLACK = register(new Wool("Black Wool", WoolColor.Black, WHITE));
	
	public static enum WoolColor implements DataSource {
		White(0),
		Orange(1),
		Magenta(2),
		LightBlue(3),
		Yellow(4),
		Lime(5),
		Pink(6),
		Gray(7),
		Silver(8),
		Cyan(9),
		Purple(10),
		Blue(11),
		Brown(12),
		Green(13),
		Red(14),
		Black(15);

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

	private final WoolColor color;

	private Wool(String name) {
		super(name, 35);
		this.setDefault();
		this.color = WoolColor.White;
	}

	private Wool(String name, WoolColor color, Wool parent) {
		super(name, 35, color.getData(), parent);
		this.setDefault();
		this.color = color;
	}

	private void setDefault() {
		this.setHardness(0.8F).setResistance(1.3F);
	}

	@Override
	public boolean isMoving() {
		return false;
	}

	public WoolColor getColor() {
		return color;
	}

	@Override
	public short getData() {
		return color.getData();
	}
	
	@Override
	public Wool getParentMaterial() {
		return (Wool) super.getParentMaterial();
	}
}
