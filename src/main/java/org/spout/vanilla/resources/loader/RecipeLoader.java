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
package org.spout.vanilla.resources.loader;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.spout.api.Spout;
import org.spout.api.exception.ConfigurationException;
import org.spout.api.inventory.recipe.Recipe;
import org.spout.api.inventory.recipe.RecipeBuilder;
import org.spout.api.material.Material;
import org.spout.api.material.MaterialRegistry;
import org.spout.api.resource.BasicResourceLoader;
import org.spout.api.util.config.ConfigurationNode;
import org.spout.api.util.config.yaml.YamlConfiguration;

import org.spout.vanilla.resources.RecipeYaml;

public class RecipeLoader extends BasicResourceLoader<RecipeYaml> {
	@Override
	public String getFallbackResourceName() {
		return "recipe://Vanilla/resources/recipes.yml";
	}

	@Override
	public RecipeYaml getResource(InputStream stream) {
		Map<String, Recipe> recipes = new HashMap<String, Recipe>();
		YamlConfiguration config = new YamlConfiguration(stream);
		try {
			config.load();
		} catch (ConfigurationException ex) {
			Spout.getLogger().warning("Unable to load recipes.yml");
		}
		ConfigurationNode recipesNode = config.getChild("recipes");
		for (String key : recipesNode.getKeys(false)) {
			ConfigurationNode recipe = recipesNode.getNode(key);
			RecipeBuilder builder = new RecipeBuilder();
			builder.setIncludeData(recipe.getNode("includedata") != null &&  recipe.getNode("includedata").getBoolean() == true);
			String[] resultString = recipe.getNode("result").getString().split(",");
			Material matched = MaterialRegistry.get(resultString[0]);
			if (matched == null) {
				Spout.getLogger().log(Level.WARNING, "Unknown material: {0}", resultString[0]);
				continue;
			}
			int amount;
			try {
				amount = Integer.parseInt(resultString[1]);
			} catch (NumberFormatException numberFormatException) {
				amount = 1;
			}
			builder.setResult(matched, amount);
			if (recipe.getNode("type").getString().equalsIgnoreCase("Shaped")) {
				for (String inKey : recipe.getNode("ingredients").getKeys(false)) {
					Material ingredient = MaterialRegistry.get(recipe.getNode("ingredients").getNode(inKey).getString());
					if (ingredient == null) {
						Spout.getLogger().log(Level.WARNING, "Unknown material ingredient: {0}", recipe.getNode("ingredients").getNode(inKey).getString());
						continue;
					}
					builder.setIngredient(inKey.charAt(0), ingredient);
				}
				for (Iterator<String> it = recipe.getNode("rows").getStringList().iterator(); it.hasNext();) {
					String row = it.next();
					List<Character> rowChars = new ArrayList<Character>();
					for (char c : row.toCharArray()) {
						rowChars.add(c);
					}
					builder.addRow(rowChars);
				}
				try {
					recipes.put(key, builder.buildShapedRecipe());
				} catch (IllegalStateException ex) {
					Spout.getLogger().log(Level.WARNING, "Error when adding recipe {0} because: {1}", new Object[]{key, ex.getMessage()});
					
				}
			} else if (recipe.getNode("type").getString().equalsIgnoreCase("Shapeless")) {
				for (String rowString : recipe.getNode("ingredients").getStringList(new ArrayList<String>())) {
					Material ingredient = MaterialRegistry.get(rowString);
					if (ingredient == null) continue;
					builder.addIngredient(ingredient);
				}
				try {
					recipes.put(key, builder.buildShapelessRecipe());
				} catch (IllegalStateException ex) {
					Spout.getLogger().log(Level.WARNING, "Error when adding recipe {0} because: {1}", new Object[]{key, ex.getMessage()});
				}
			} else {
				if (Spout.debugMode()) {
					Spout.log("Unknown type " + recipe.getNode("type") + " when loading recipe from recipes.yml");
				}
			}
		}
		recipes.remove(null);
		return new RecipeYaml(recipes);
	}

	@Override
	public String getProtocol() {
		return "recipe";
	}

	@Override
	public String[] getExtensions() {
		return new String[]{"yml"};
	}
}
