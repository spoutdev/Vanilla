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

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.spout.api.Spout;
import org.spout.api.inventory.Recipe;
import org.spout.api.inventory.RecipeBuilder;
import org.spout.api.inventory.ShapedRecipe;
import org.spout.api.inventory.ShapelessRecipe;

import org.spout.vanilla.VanillaPlugin;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.item.misc.Dye;

public class VanillaRecipes {
	public static final ShapelessRecipe WOODEN_PLANK;
	public static final ShapelessRecipe BONE_MEAL;
	public static final ShapedRecipe WOODEN_PICKAXE;
	private static Map<String, Recipe> yamlRecipes = new ConcurrentHashMap<String, Recipe>();
	private static final File RECIPES_FILE = new File(VanillaPlugin.getInstance().getDataFolder(), "recipes.yml");

	public VanillaRecipes() {
	}

	static {
		WOODEN_PLANK = add(create().setResult(VanillaMaterials.PLANK, 4).addIngredient(VanillaMaterials.LOG).buildShapelessRecipe());
		BONE_MEAL = add(create().setResult(Dye.BONE_MEAL, 3).addIngredient(VanillaMaterials.BONE).buildShapelessRecipe());
		WOODEN_PICKAXE = add(create().setResult(VanillaMaterials.WOODEN_PICKAXE, 1).addIngredient('P', VanillaMaterials.PLANK).addIngredient('S', VanillaMaterials.STICK).addRow("PPP").addRow("ESE").addRow("ESE").buildShapedRecipe());
		RecipeYamlParser.parseFileToRecipes(VanillaPlugin.getInstance(), RECIPES_FILE);
	}

	private static <T extends Recipe> T add(T recipe) {
		Spout.getEngine().getRecipeManager().addRecipe(recipe);
		return recipe;
	}

	public static <T extends Recipe> T addYamlRecipe(T recipe, String name) {
		yamlRecipes.put(name, recipe);
		add(recipe);
		return recipe;
	}

	public static Recipe get(String name) {
		return yamlRecipes.get(name);
	}

	private static RecipeBuilder<?> create() {
		return new RecipeBuilder<RecipeBuilder<?>>();
	}
}
