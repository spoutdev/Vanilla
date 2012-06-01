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


import org.spout.api.inventory.ShapedRecipe;
import org.spout.api.material.Material;


/**
 * Represents a tool recipe with a few constraints. 
 * It can have only two materials within the recipe (namely one major and one minor material). 
 * This recipe should only be used for recipes such as a Wooden Sword or a Wooden Pickaxe; both which have a 3x3 crafting recipe 
 * that have a major (plank) and minor (stick) material with everything else in the matrix as null.
 * <p/>
 * Example:
 * ---------------------
 * | Plank Plank Plank |
 * | Null  Stick Null  |
 * | Null  Stick Null  |
 * ---------------------
 * 
 */
public class SimpleShapedToolRecipe extends ShapedRecipe{
	private Material majorMaterial;
	private Material minorMaterial;
	private Character majorChar;
	private Character minorChar;

	public SimpleShapedToolRecipe(VanillaRecipeBuilder<?> builder) {
	    super(builder);
	    this.majorMaterial = builder.majorMaterial;
	    this.minorMaterial = builder.minorMaterial;
	    this.majorChar = builder.majorChar;
	    this.minorChar = builder.minorChar;
	    
	}

	public Material getMajorMaterial() {
	    return majorMaterial;
	}
	
	public Material getMinorMaterial() {
	    return minorMaterial;
	}

	public Character getMajorChar() {
	    return majorChar;
	}

	public Character getMinorChar() {
	    return minorChar;
	}
}
