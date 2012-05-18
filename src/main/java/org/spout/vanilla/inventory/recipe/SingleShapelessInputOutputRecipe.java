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

import org.spout.api.Spout;
import org.spout.api.entity.Controller;
import org.spout.api.inventory.ItemStack;
import org.spout.api.inventory.ShapelessRecipe;
import org.spout.api.material.Material;

import org.spout.vanilla.VanillaPlugin;

/**
 * Takes in a variable material input amount and gives a variable material output amount (Shapeless).
 */
public class SingleShapelessInputOutputRecipe {
	/**
	 * Constructs a single input and output recipe.
	 * @param instance        Instance of our plugin
	 * @param craftingEnabler The controller that would "manage" this recipe. Example would be CraftingController or VanillaPlayer (as part of their inventory would be a crafting matrix).
	 * @param output          Type of material that would be recieved in return.
	 * @param key             Character identifier for the input material.
	 * @param outputAmount    Amount of the output material returned from this recipe.
	 */
	public SingleShapelessInputOutputRecipe(VanillaPlugin instance, Controller craftingEnabler, String name, Material output, int outputAmount, char key, Material input, int inputAmount) {
		ShapelessRecipe shapeless = new ShapelessRecipe(instance, name, new ItemStack(output, outputAmount));
		//Keep looping through the specified input amount and add in ingredient requirements.
		for (int i = 0; i < inputAmount; i++) {
			shapeless.addIngredient(key, input);
		}
		Spout.getEngine().getRecipeManager().addRecipe(shapeless);
	}
}