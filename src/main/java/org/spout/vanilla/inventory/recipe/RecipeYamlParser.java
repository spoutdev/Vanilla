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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.spout.api.exception.ConfigurationException;
import org.spout.api.inventory.Recipe;
import org.spout.api.inventory.RecipeBuilder;
import org.spout.api.inventory.ShapedRecipe;
import org.spout.api.inventory.ShapelessRecipe;
import org.spout.api.material.Material;
import org.spout.api.material.MaterialRegistry;
import org.spout.api.plugin.Plugin;
import org.spout.api.util.config.ConfigurationNode;
import org.spout.api.util.config.yaml.YamlConfiguration;

public class RecipeYamlParser {	
	public static Set<Recipe> parseFileToRecipes(Plugin plugin, File file) {
		Set<Recipe> recipes = new HashSet<Recipe>();
		YamlConfiguration config = new YamlConfiguration(file);
		ConfigurationNode recipesNode = config.getChild("recipes");
		for (String key : recipesNode.getKeys(false)) {
			ConfigurationNode recipe = recipesNode.getNode(key);
			RecipeBuilder<?> builder = new RecipeBuilder<RecipeBuilder<?>>();
			String[] resultString = recipe.getString("result").split(",");
			Material matched = MaterialRegistry.get(resultString[0]);
			int amount;
			try {
				amount = Integer.parseInt(resultString[1]);
			} catch (NumberFormatException numberFormatException) {
				amount = 1;
			}
			builder.setResult(matched, amount);
			if (recipe.getNode("type").getString().equalsIgnoreCase("Shaped")) {
				for (String inKey : recipe.getNode("ingredients").getKeys(false)) {
					builder.addIngredient(inKey.charAt(0), MaterialRegistry.get(recipe.getString("ingredients." + inKey)));
				}
				for(Object rowObject : recipe.getNode("rows").getList(new ArrayList<String>())) {
					String row = (String) rowObject;
					List<Character> rowChars = new ArrayList<Character>();
					for (char c : row.toCharArray()) {
					    rowChars.add(c);
					}
					builder.addRow(rowChars);
				}
				recipes.add(VanillaRecipes.addYamlRecipe(builder.buildShapedRecipe(), key));
			} else if (recipe.getNode("type").getString().equalsIgnoreCase("Shapeless")) {
				for(Object rowObject : recipe.getNode("ingredients").getList(new ArrayList<String>())) {
					String ingredient = (String) rowObject;
					builder.addIngredient(MaterialRegistry.get(ingredient));
				}
				recipes.add(VanillaRecipes.addYamlRecipe(builder.buildShapelessRecipe(), key));
			}
		}
		return recipes;
	}
	
	public static void parseRecipesToFile(Set<Recipe> recipes, File file) {
		YamlConfiguration config = new YamlConfiguration(file);
		ConfigurationNode recipesNode = config.getChild("recipes");
		for(Recipe recipe : recipes) {
			ConfigurationNode recipeNode = recipesNode.getChild(recipe.getResult().getMaterial().getName());
			String resultString = recipe.getResult().getMaterial().getName() + "," + recipe.getResult().getAmount();
			recipeNode.getChild("result").setValue(resultString);
			if (recipe instanceof ShapedRecipe) {
				ShapedRecipe shapedRecipe = (ShapedRecipe) recipe;
				for (Entry<Character, Material> entry : shapedRecipe.getIngredientsMap().entrySet()) {
					recipeNode.getChild("ingredients").getChild("" + entry.getKey()).setValue(entry.getValue().getName());
				}
				List<String> characters = new ArrayList<String>();
				for (List<Character> row : shapedRecipe.getRows()) {
					StringBuilder builder = new StringBuilder();
					for (Character c : row) {
						builder.append(c);
					}
					characters.add(builder.toString());
				}
				recipeNode.getNode("rows").setValue(characters);
			} else if (recipe instanceof ShapelessRecipe) {
				recipeNode.getNode("ingredients").setValue(recipe.getIngredients());
			}
		}
		config.addChild(recipesNode);
		try {
			config.save();
		} catch (ConfigurationException ex) {
			// TODO: something here?
		}
	}
}
