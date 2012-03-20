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
package org.spout.vanilla.material.main;

import org.spout.vanilla.material.block.Wool;

public class WoolMain extends Wool {
	public final Wool WHITE = new Wool("White Wool", WoolColor.White, this);
	public final Wool ORANGE = new Wool("Orange Wool", WoolColor.Orange, this);
	public final Wool MAGENTA = new Wool("Magenta Wool", WoolColor.Magenta, this);
	public final Wool LIGHTBLUE = new Wool("Light Blue Wool", WoolColor.LightBlue, this);
	public final Wool YELLOW = new Wool("Yellow Wool", WoolColor.Yellow, this);
	public final Wool LIME = new Wool("Lime Wool", WoolColor.Lime, this);
	public final Wool PINK = new Wool("Pink Wool", WoolColor.Pink, this);
	public final Wool GRAY = new Wool("Gray Wool", WoolColor.Gray, this);
	public final Wool SILVER = new Wool("Silver Wool", WoolColor.Silver, this);
	public final Wool CYAN = new Wool("Cyan Wool", WoolColor.Cyan, this);
	public final Wool PURPLE = new Wool("Purple Wool", WoolColor.Purple, this);
	public final Wool BLUE = new Wool("Blue Wool", WoolColor.Blue, this);
	public final Wool BROWN = new Wool("Brown Wool", WoolColor.Brown, this);
	public final Wool GREEN = new Wool("Green Wool", WoolColor.Green, this);
	public final Wool RED = new Wool("Red Wool", WoolColor.Red, this);
	public final Wool BLACK = new Wool("Black Wool", WoolColor.Black, this);
	
	public WoolMain(String name) {
		super(name);
	}

}
