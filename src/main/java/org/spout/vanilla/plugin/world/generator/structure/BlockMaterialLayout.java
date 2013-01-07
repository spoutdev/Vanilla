/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, Spout LLC <http://www.spout.org/>
 * Vanilla is licensed under the Spout License Version 1.
 *
 * Vanilla is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the Spout License Version 1.
 *
 * Vanilla is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for
 * more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the Spout License Version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://spout.in/licensev1> for the full license, including
 * the MIT license.
 */
package org.spout.vanilla.plugin.world.generator.structure;

import gnu.trove.map.hash.TCharObjectHashMap;

import org.spout.api.material.BlockMaterial;

public class BlockMaterialLayout {
	private final char layout[][];
	private final TCharObjectHashMap<BlockMaterial> style = new TCharObjectHashMap<BlockMaterial>();

	/**
	 * <p>Constructs the layout using a string. The string has to be formatted
	 * in alphanumeric codes.</p> <p><strong>Example:</strong></p>
	 * <p><pre>setLayout("00000\n00100\n01110\n00100\n00000", Wool.WHITE, Wool.RED);</pre></p>
	 * <p>This would draw a red cross wrapped in white wool</p> <p>The '.' char
	 * represents no material.</p>
	 * @param layout
	 */
	public BlockMaterialLayout(String layout) {
		final String lines[] = layout.split("\n");
		this.layout = new char[lines.length][];
		int row = 0;
		for (String line : lines) {
			this.layout[row] = new char[line.length()];
			for (int col = 0; col < line.length(); col++) {
				this.layout[row][col] = line.charAt(col);
			}
			row++;
		}
		style.put('.', null);
	}

	/**
	 * Construct a layout from a 2D character array. Each character can then be
	 * mapped to a material, and represents a block. The '.' char represents no
	 * material.
	 * @param layout
	 */
	public BlockMaterialLayout(char[][] layout) {
		this.layout = layout;
		style.put('.', null);
	}

	/**
	 * Map a character to a block material.
	 * @param key
	 * @param material
	 * @throws IllegalArgumentException if the reserved '.' char is used as a
	 * key.
	 */
	public void setBlockMaterial(char key, BlockMaterial material) {
		if (key == '.') {
			throw new IllegalArgumentException("The '.' char is reserved for no material");
		}
		style.put(key, material);
	}

	public int getRowLenght() {
		return layout.length;
	}

	public int getColumnLenght(int row) {
		return layout[row].length;
	}

	/**
	 * Returns the block material at the row and column position. Returns null
	 * if no material is set at the position.
	 */
	public BlockMaterial getBlockMaterial(int row, int column) {
		return style.get(layout[row][column]);
	}
}
