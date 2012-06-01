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
package org.spout.vanilla.inventory.recipe;

import org.spout.api.Spout;
import org.spout.api.inventory.Recipe;

import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.item.misc.Dye;
import static org.spout.vanilla.inventory.recipe.VanillaRecipeBuilder.create;

public class VanillaRecipes {
	public static final SingleShapelessInputOutputRecipe WOODEN_PLANK;
	public static final SingleShapelessInputOutputRecipe BONE_MEAL;
	public static final SimpleShapedToolRecipe WOODEN_PICKAXE;
	
	static {
		WOODEN_PLANK = add(create().setResult(VanillaMaterials.PLANK, 4).addIngredient(VanillaMaterials.LOG).buildSingleShapelessInputOutputRecipe());
		BONE_MEAL = add(create().setResult(Dye.BONE_MEAL, 3).addIngredient(VanillaMaterials.BONE).buildSingleShapelessInputOutputRecipe());
		WOODEN_PICKAXE = add(create().setResult(VanillaMaterials.WOODEN_PICKAXE, 1).setMajorMaterial('P', VanillaMaterials.PLANK).setMinorMaterial('S', VanillaMaterials.STICK).addRow("PPP").addRow("ESE").addRow("ESE").buildSimpleShapedToolRecipe());
	}

	private static <T extends Recipe> T add(T recipe) {
		Spout.getEngine().getRecipeManager().addRecipe(recipe);
		return recipe;
	}
}
