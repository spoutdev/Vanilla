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

import org.spout.api.material.DataSource;
import org.spout.api.material.SubMaterial;
import org.spout.vanilla.material.MovingBlock;
import org.spout.vanilla.material.generic.GenericBlock;

public class Wool extends GenericBlock implements MovingBlock, SubMaterial {
	public final Wool WHITE;
	public final Wool ORANGE;
	public final Wool MAGENTA;
	public final Wool LIGHTBLUE;
	public final Wool YELLOW;
	public final Wool LIME;
	public final Wool PINK;
	public final Wool GRAY;
	public final Wool SILVER;
	public final Wool CYAN;
	public final Wool PURPLE;
	public final Wool BLUE;
	public final Wool BROWN;
	public final Wool GREEN;
	public final Wool RED;
	public final Wool BLACK;

	public static enum Color implements DataSource {
		White(0), Orange(1), Magenta(2), LightBlue(3),
		Yellow(4), Lime(5), Pink(6), Gray(7), Silver(8),
		Cyan(9), Purple(10), Blue(11), Brown(12), Green(13),
		Red(14), Black(15);
		
		private final short data;
		
		private Color(int data) {
			this.data = (short) data;
		}
		
		@Override
		public short getData() {
			return this.data;
		}
	}
	
	private final Color color;
	private final Wool parent;

	private void setDefault() {
		this.setHardness(0.8F).setResistance(1.3F);
	}
	
	private Wool(String name, Color color, Wool parent) {
		super(name, 35);
		this.setDefault();
		this.color = color;
		this.parent = parent;
		parent.registerSubMaterial(this);
		this.register();
		
		this.WHITE = parent.WHITE;
		this.ORANGE = parent.ORANGE;
		this.MAGENTA = parent.MAGENTA;
		this.LIGHTBLUE = parent.LIGHTBLUE;
		this.YELLOW = parent.YELLOW;
		this.LIME = parent.LIME;
		this.PINK = parent.PINK;
		this.GRAY = parent.GRAY;
		this.SILVER = parent.SILVER;
		this.CYAN = parent.CYAN;
		this.PURPLE = parent.PURPLE;
		this.BLUE = parent.BLUE;
		this.BROWN = parent.BROWN;
		this.GREEN = parent.GREEN;
		this.RED = parent.RED;
		this.BLACK = parent.BLACK;
	}

	public Wool(String name) {
		super(name, 35);
		this.setDefault();
		this.color = Color.White;
		this.parent = this;
		this.register();
		
		this.WHITE = new Wool("White Wool", Color.White, this);
		this.ORANGE = new Wool("Orange Wool", Color.Orange, this);
		this.MAGENTA = new Wool("Magenta Wool", Color.Magenta, this);
		this.LIGHTBLUE = new Wool("Light Blue Wool", Color.LightBlue, this);
		this.YELLOW = new Wool("Yellow Wool", Color.Yellow, this);
		this.LIME = new Wool("Lime Wool", Color.Lime, this);
		this.PINK = new Wool("Pink Wool", Color.Pink, this);
		this.GRAY = new Wool("Gray Wool", Color.Gray, this);
		this.SILVER = new Wool("Silver Wool", Color.Silver, this);
		this.CYAN = new Wool("Cyan Wool", Color.Cyan, this);
		this.PURPLE = new Wool("Purple Wool", Color.Purple, this);
		this.BLUE = new Wool("Blue Wool", Color.Blue, this);
		this.BROWN = new Wool("Brown Wool", Color.Brown, this);
		this.GREEN = new Wool("Green Wool", Color.Green, this);
		this.RED = new Wool("Red Wool", Color.Red, this);
		this.BLACK = new Wool("Black Wool", Color.Black, this);
	}

	@Override
	public boolean isMoving() {
		return false;
	}

	public Color getColor() {
		return this.color;
	}

	@Override
	public short getData() {
		return this.color.getData();
	}

	@Override
	public Wool getParentMaterial() {
		return this.parent;
	}
}
