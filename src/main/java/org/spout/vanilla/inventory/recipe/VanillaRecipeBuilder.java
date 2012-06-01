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

import org.spout.api.inventory.RecipeBuilder;
import org.spout.api.material.Material;

@SuppressWarnings("unchecked")
public class VanillaRecipeBuilder<T extends VanillaRecipeBuilder<?>> extends RecipeBuilder<VanillaRecipeBuilder<?>>{
	public Material majorMaterial;
	public Material minorMaterial;
	public Character majorChar;
	public Character minorChar;
	
	public static VanillaRecipeBuilder<?> create() {
		return new VanillaRecipeBuilder<VanillaRecipeBuilder<?>>();
	}
	
	public SimpleShapedRecipe buildSimpleShapedRecipe() {
		if (ingredientsMap.size() > 1) {
			throw new IllegalStateException("Tried to create a SimpleShapedRecipe, but the recipe contains too many materials.");
		} else if (ingredientsMap.size() < 1) {
			if (majorMaterial != null && majorChar != null) {
				ingredientsMap.clear();
				ingredientsMap.put(majorChar, majorMaterial);
			} else {
				throw new IllegalStateException("Tried to create a SimpleShapedRecipe, but there was not enough information.");
			}
		}
		return new SimpleShapedRecipe(this);
	}

	public SimpleShapedToolRecipe buildSimpleShapedToolRecipe() {
		if (majorMaterial == null || minorMaterial == null || minorChar == null || majorChar == null) {
			if (ingredientsMap.size() < 2) {
				throw new IllegalStateException("Tried to create a SimpleShapedToolRecipe, but there was not enough information.");
			} else if (ingredientsMap.size() > 2) {
				throw new IllegalStateException("Tried to create a SimpleShapedToolRecipe, but the recipe contains too many materials.");
			} else {
				minorChar = (Character) ingredientsMap.keySet().toArray()[0];
				majorChar = (Character) ingredientsMap.keySet().toArray()[1];
				minorMaterial = (Material) ingredientsMap.values().toArray()[0];
				majorMaterial = (Material) ingredientsMap.values().toArray()[1];
			}
		}
		ingredientsMap.clear();
		ingredientsMap.put(majorChar, majorMaterial);
		ingredientsMap.put(minorChar, minorMaterial);
		return new SimpleShapedToolRecipe(this);
	}

	public SingleShapelessInputOutputRecipe buildSingleShapelessInputOutputRecipe() {
		if (ingredients.isEmpty() ) {
			if (ingredientsMap.values().size() > 0) {
				ingredients.addAll(ingredientsMap.values());
			} else {
				throw new IllegalStateException("Tried to create a SingleShapelessInputOutputRecipe, but there were not enough materials.");
			}
		}
		if (ingredients.size() > 1) {
			throw new IllegalStateException("Tried to create a SingleShapelessInputOutputRecipe, but the recipe contains too many materials.");
		}
		return new SingleShapelessInputOutputRecipe(this);
	}


	/**
	 * Set the value of minorMaterial
	 *
	 * @param c Character that represents the material
	 * @param material Material to set
	 * @return this 
	 */
	public T setMinorMaterial(Character c, Material material) {
		this.minorMaterial = material;
		this.minorChar = c;
		addIngredient(material);
		return (T) this;
	}


	/**
	 * Set the value of majorMaterial. Can be used as a shortcut method for Recipes that use both a major and minor material.
	 * Also functions as the material in recipes with a single material.
	 *
	 * @param c Character that represents the material
	 * @param material Material to set
	 * @return this 
	 */

	public T setMajorMaterial(Character c, Material material) {
		this.majorMaterial = material;
		this.majorChar = c;
		addIngredient(material);
		return (T) this;
	}
}
