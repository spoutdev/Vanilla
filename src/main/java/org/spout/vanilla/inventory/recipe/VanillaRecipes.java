/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, SpoutDev <http://www.spout.org/>
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
package org.spout.vanilla.inventory.recipe;

import org.spout.vanilla.VanillaPlugin;
import org.spout.vanilla.inventory.VanillaItemStack;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.item.misc.Dye;

public class VanillaRecipes {
	public final SingleShapelessInputOutputRecipe WOODEN_PLANK;
	public final SingleShapelessInputOutputRecipe BONE_MEAL;
	public final SimpleShapedToolRecipe WOODEN_PICKAXE;

	public VanillaRecipes(final VanillaPlugin instance) {
		/**
		 * Examples...I need feedback!
		 */
		WOODEN_PLANK = new SingleShapelessInputOutputRecipe(instance, null, "Wooden Plank", VanillaMaterials.LOG, 1, 'L', VanillaMaterials.PLANK, 4);
		BONE_MEAL = new SingleShapelessInputOutputRecipe(instance, null, "Bone Meal", VanillaMaterials.BONE, 1, 'B', Dye.BONE_MEAL, 3);
		WOODEN_PICKAXE = new SimpleShapedToolRecipe(instance, null, "Wooden Pickaxe", VanillaMaterials.PLANK, VanillaMaterials.STICK, 'P', 'S', 'E', new VanillaItemStack(VanillaMaterials.WOODEN_PICKAXE, 1), "PPP", "ESE", "ESE");
	}
}
