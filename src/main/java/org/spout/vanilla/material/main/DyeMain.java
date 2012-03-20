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

import org.spout.vanilla.material.item.Dye;

public class DyeMain extends Dye {
	public final Dye INK_SAC = new Dye("Ink Sac", DyeColor.BLACK, this);
	public final Dye ROSE_RED = new Dye("Rose Red", DyeColor.RED, this);
	public final Dye CACTUS_GREEN = new Dye("Cactus Green", DyeColor.GREEN, this);
	public final Dye COCOA_BEANS = new Dye("Cocoa Beans", DyeColor.BROWN, this);
	public final Dye LAPIS_LAZULI = new Dye("Lapis Lazuli", DyeColor.BLUE, this);
	public final Dye PURPLE = new Dye("Purple Dye", DyeColor.PURPLE, this);
	public final Dye CYAN = new Dye("Cyan Dye", DyeColor.CYAN, this);
	public final Dye LIGHT_GRAY = new Dye("Light Gray Dye", DyeColor.LIGHT_GRAY, this);
	public final Dye GRAY = new Dye("Gray Dye", DyeColor.GRAY, this);
	public final Dye PINK = new Dye("Pink Dye", DyeColor.PINK, this);
	public final Dye LIME = new Dye("Lime Dye", DyeColor.LIME, this);
	public final Dye DANDELION_YELLOW = new Dye("Dandelion Yellow", DyeColor.YELLOW, this);
	public final Dye LIGHT_BLUE = new Dye("Light Blue Dye", DyeColor.LIGHT_BLUE, this);
	public final Dye MAGENTA = new Dye("Magenta Dye", DyeColor.MAGENTA, this);
	public final Dye ORANGE = new Dye("Orange Dye", DyeColor.ORANGE, this);
	public final Dye BONE_MEAL = new Dye("Bone Meal", DyeColor.WHITE, this);

	public DyeMain(String name) {
		super(name);
	}
}
