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

import java.util.ArrayList;

import org.spout.api.Spout;
import org.spout.api.entity.Controller;
import org.spout.api.inventory.ItemStack;
import org.spout.api.inventory.ShapedRecipe;
import org.spout.api.material.Material;

import org.spout.vanilla.VanillaPlugin;

public class SimpleShapedToolRecipe {
	/**
	 * Constructs a tool recipe given a few constrains. It can have only two materials within the recipe (namely one major and one minor material). This recipe should only be used
	 * for recipes such as a Wooden Sword or a Wooden Pickaxe; both which have a 3x3 crafting recipe that have a major (plank) and minor (stick) material with everything else in
	 * the matrix as null.
	 * <p/>
	 * Example:
	 * ---------------------
	 * | Plank Plank Plank |
	 * | Null  Stick Null  |
	 * | Null  Stick Null  |
	 * ---------------------
	 * @param instance        Instance of our plugin
	 * @param craftingEnabler The controller that would "manage" this recipe. Example would be CraftingController or VanillaPlayer (as part of their inventory would be a crafting matrix).
	 * @param name            Name of the recipe
	 * @param major           The main ingredient of the recipe
	 * @param minor           The minor ingredient of the recipe
	 * @param majorC          Character represents major ingredient
	 * @param minorC          Character represents minor ingredient
	 * @param emptyC          Character represents empty crafting matrix slot
	 * @param result          The resulting item stack
	 * @param rows            The rows that should be registered for the recipe. This can be any amount (variable).
	 */
	public SimpleShapedToolRecipe(VanillaPlugin instance, Controller craftingEnabler, String name, Material major, Material minor, char majorC, char minorC, char emptyC,
								  ItemStack result, String... rows) {
		ShapedRecipe recipe = new ShapedRecipe(instance, name, result);
		//Loop through the strings provided as rows.
		for (String str : rows) {
			char[] chars = str.toCharArray();
			ArrayList<Character> row = new ArrayList<Character>();
			for (char c : chars) {
				row.add(c);
			}
			if (!row.isEmpty()) {
				recipe.addRow(row);
			}
		}
		//Assign recipe positions to materials.
		recipe.getIngredients().put(majorC, major);
		recipe.getIngredients().put(minorC, minor);
		recipe.getIngredients().put(emptyC, null);

		//Register the recipe
		Spout.getEngine().getRecipeManager().addRecipe(recipe);
	}
}
