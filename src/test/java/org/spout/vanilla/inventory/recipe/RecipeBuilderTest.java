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
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import org.junit.Test;

import org.spout.api.inventory.RecipeBuilder;
import org.spout.api.inventory.ShapedRecipe;
import org.spout.api.inventory.ShapelessRecipe;
import org.spout.api.material.Material;

import org.spout.vanilla.material.VanillaMaterials;

public class RecipeBuilderTest {
    
    @Test
    public void singleIngredientsTest() {
	    RecipeBuilder<RecipeBuilder<?>> builder = new RecipeBuilder<RecipeBuilder<?>>();
	    builder.addIngredient(VanillaMaterials.DIAMOND).addIngredient(VanillaMaterials.DIAMOND_BLOCK);
	    builder.addIngredient(VanillaMaterials.DIRT, 2);
	    ShapelessRecipe recipe = builder.buildShapelessRecipe();
	    List<Material> materials = new ArrayList<Material>();
	    materials.add(VanillaMaterials.DIAMOND);
	    materials.add(VanillaMaterials.DIAMOND_BLOCK);
	    materials.add(VanillaMaterials.DIRT);
	    materials.add(VanillaMaterials.DIRT);
	    for (Material m : recipe.getIngredients()) {
		    assertTrue(materials.remove(m));
	    }
	    assertTrue(materials.isEmpty());  
    }
    
    @Test
    public void characterIngredientsTest() {
	    RecipeBuilder<RecipeBuilder<?>> builder = new RecipeBuilder<RecipeBuilder<?>>();
	    builder.addIngredient(VanillaMaterials.ARROW).addIngredient(VanillaMaterials.BEDROCK);
	    builder.addIngredient(VanillaMaterials.CACTUS);
	    ShapelessRecipe recipe = builder.buildShapelessRecipe();
	    List<Material> testIngredients = new ArrayList<Material>();
	    List<Material> recipeIngredients = new ArrayList<Material>();
	    System.out.println("Recipe: " + testIngredients);
	    recipeIngredients.addAll(recipe.getIngredients());
	    testIngredients.add(VanillaMaterials.ARROW);
	    testIngredients.add(VanillaMaterials.BEDROCK);
	    testIngredients.add(VanillaMaterials.CACTUS);
	    System.out.println("Test: " + testIngredients);
	    recipeIngredients.removeAll(testIngredients);
	    testIngredients.removeAll(recipe.getIngredients());
	    assertTrue(testIngredients.isEmpty());
	    assertTrue(recipeIngredients.isEmpty());
    }
    
    @Test
    public void overwritingMaterialsTest() {
	    RecipeBuilder<RecipeBuilder<?>> builder = new RecipeBuilder<RecipeBuilder<?>>();
	    builder.addIngredient('A', VanillaMaterials.ARROW).addIngredient('B', VanillaMaterials.BEDROCK);
	    builder.addIngredient('B', VanillaMaterials.CACTUS).addIngredient('A', VanillaMaterials.DIRT);
	    ShapedRecipe recipe = builder.buildShapedRecipe();
	    List<Material> testIngredients = new ArrayList<Material>();
	    List<Material> recipeIngredients = new ArrayList<Material>();
	    recipeIngredients.addAll(recipe.getIngredients());
	    testIngredients.add(VanillaMaterials.CACTUS);
	    testIngredients.add(VanillaMaterials.DIRT);
	    recipeIngredients.removeAll(testIngredients);
	    testIngredients.removeAll(recipe.getIngredients());
	    assertTrue(testIngredients.isEmpty());
	    assertTrue(recipeIngredients.isEmpty());
    }
    
    @Test
    public void shapedAmountsTest() {
	    RecipeBuilder<RecipeBuilder<?>> builder = new RecipeBuilder<RecipeBuilder<?>>();
	    builder.addIngredient('A', VanillaMaterials.ARROW).addIngredient('B', VanillaMaterials.BEDROCK);
	    builder.addRow("AAA").addRow("BBB").addRow("AAA");
	    ShapedRecipe recipe = builder.buildShapedRecipe();
	    List<Material> testIngredients = new ArrayList<Material>();
	    List<Material> recipeIngredients = new ArrayList<Material>();
	    recipeIngredients.addAll(recipe.getIngredients());
	    testIngredients.add(VanillaMaterials.ARROW);
	    testIngredients.add(VanillaMaterials.BEDROCK);
	    recipeIngredients.removeAll(testIngredients);
	    testIngredients.removeAll(recipe.getIngredients());
	    assertTrue(testIngredients.isEmpty());
	    assertTrue(recipeIngredients.isEmpty());
    }
    
    @Test
    public void rowsTest() {
	    RecipeBuilder<RecipeBuilder<?>> builder = new RecipeBuilder<RecipeBuilder<?>>();
	    builder.addIngredient('A', VanillaMaterials.ARROW).addIngredient('B', VanillaMaterials.BEDROCK);
	    builder.addRow("AAA").addRow("BBB").addRow("AAA");
	    ShapedRecipe recipe = builder.buildShapedRecipe();
	    List<List<Character>> rows = recipe.getRows();
	    assertTrue(rows.get(0).get(0) == 'A');
	    assertTrue(rows.get(0).get(1) == 'A');
	    assertTrue(rows.get(0).get(2) == 'A');
	    assertTrue(rows.get(1).get(0) == 'B');
	    assertTrue(rows.get(1).get(1) == 'B');
	    assertTrue(rows.get(1).get(2) == 'B');
	    assertTrue(rows.get(2).get(0) == 'A');
	    assertTrue(rows.get(2).get(1) == 'A');
	    assertTrue(rows.get(2).get(2) == 'A');
    }
    
    @Test
    public void replaceRowsTest() {
	    RecipeBuilder<RecipeBuilder<?>> builder = new RecipeBuilder<RecipeBuilder<?>>();
	    builder.addIngredient('A', VanillaMaterials.ARROW).addIngredient('B', VanillaMaterials.BEDROCK);
	    builder.addRow("AAA").addRow("BBB").addRow("AAA").replaceRow(1, "CCC");
	    ShapedRecipe recipe = builder.buildShapedRecipe();
	    List<List<Character>> rows = recipe.getRows();
	    assertTrue(rows.get(0).get(0) == 'A');
	    assertTrue(rows.get(0).get(1) == 'A');
	    assertTrue(rows.get(0).get(2) == 'A');
	    assertTrue(rows.get(1).get(0) == 'C');
	    assertTrue(rows.get(1).get(1) == 'C');
	    assertTrue(rows.get(1).get(2) == 'C');
	    assertTrue(rows.get(2).get(0) == 'A');
	    assertTrue(rows.get(2).get(1) == 'A');
	    assertTrue(rows.get(2).get(2) == 'A');
    }
    
    @Test
    public void cloneTest() {
	    RecipeBuilder<RecipeBuilder<?>> builder = new RecipeBuilder<RecipeBuilder<?>>();
	    builder.addIngredient('A', VanillaMaterials.ARROW).addIngredient('B', VanillaMaterials.BEDROCK);
	    builder.addRow("AAA").addRow("BBB").addRow("AAA");
	    ShapedRecipe recipe1 = builder.buildShapedRecipe();
	    RecipeBuilder<RecipeBuilder<?>> builder2 = new RecipeBuilder<RecipeBuilder<?>>();
	    builder2.clone(recipe1);
	    builder2.replaceRow(1, "CCC");
	    ShapedRecipe recipe2 = builder2.buildShapedRecipe();
	    assertTrue(!recipe1.equals(recipe2));
	    RecipeBuilder<RecipeBuilder<?>> builder3 = new RecipeBuilder<RecipeBuilder<?>>();
	    builder3.addIngredient('A', VanillaMaterials.ARROW).addIngredient('B', VanillaMaterials.BEDROCK);
	    builder3.addRow("AAA").addRow("CCC").addRow("AAA");
	    ShapedRecipe recipe3 = builder3.buildShapedRecipe();
	    assertTrue(recipe2.equals(recipe3));
    }
}
