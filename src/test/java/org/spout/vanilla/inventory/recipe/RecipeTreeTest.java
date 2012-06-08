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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import org.spout.api.inventory.RecipeBuilder;
import org.spout.api.inventory.RecipeTree;
import org.spout.api.inventory.ShapedRecipe;
import org.spout.api.material.Material;

import org.spout.vanilla.material.VanillaMaterials;

import static org.junit.Assert.assertSame;

public class RecipeTreeTest {
	@Test
	public void treeTest() {
		RecipeBuilder<RecipeBuilder<?>> builder = new RecipeBuilder<RecipeBuilder<?>>();
		builder.addIngredient('A', VanillaMaterials.ARROW).addIngredient('B', VanillaMaterials.BEDROCK);
		builder.addRow("AAA").addRow("BBB").addRow("AAA");
		ShapedRecipe recipe = builder.buildShapedRecipe();
		List<List<Material>> testIngredients = new ArrayList<List<Material>>();
		testIngredients.add(new ArrayList<Material>(Arrays.asList(VanillaMaterials.ARROW, VanillaMaterials.ARROW, VanillaMaterials.ARROW)));
		testIngredients.add(new ArrayList<Material>(Arrays.asList(VanillaMaterials.BEDROCK, VanillaMaterials.BEDROCK, VanillaMaterials.BEDROCK)));
		testIngredients.add(new ArrayList<Material>(Arrays.asList(VanillaMaterials.ARROW, VanillaMaterials.ARROW, VanillaMaterials.ARROW)));
		RecipeTree tree = new RecipeTree();
		tree.addRecipe(recipe);
		assertSame(recipe, tree.matchShapedRecipe(testIngredients));
	}
}
