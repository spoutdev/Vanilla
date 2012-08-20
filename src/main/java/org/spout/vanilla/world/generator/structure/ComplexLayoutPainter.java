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
package org.spout.vanilla.world.generator.structure;

import gnu.trove.map.hash.TShortObjectHashMap;

import org.spout.api.material.BlockMaterial;
import org.spout.api.material.block.BlockFace;
import org.spout.api.math.Vector3;

/**
 * Draws a plane with the defined complex layout
 * @author tux
 */
public class ComplexLayoutPainter {
	StructureComponent parent;
	private short layout[][] = null;
	private TShortObjectHashMap<BlockMaterial> style = new TShortObjectHashMap<BlockMaterial>();

	public ComplexLayoutPainter(StructureComponent parent) {
		this.parent = parent;
	}

	public void setBlockMaterial(char key, BlockMaterial material) {
		style.put((short) key, material);
	}

	/**
	 * <p>Sets the layout by using a string. The string has to be formatted in alphanumeric codes</p>
	 * <p><strong>Example:</strong></p>
	 * <p><pre>setLayout("00000\n00100\n01110\n00100\n00000", Wool.WHITE, Wool.RED);</pre></p>
	 * <p>This would draw a red cross wrapped in white wool</p>
	 * <p>The key index begins at 0-9, then a-z</p>
	 * @param layout
	 * @param materials
	 */
	public void setLayout(String layout) {
		String lines[] = layout.split("\n");
		int rows = lines.length;
		int cols = lines[0].length();

		int row = 0;
		this.layout = new short[rows][cols];
		for (String line : lines) {
			for (int col = 0; col < line.length(); col++) {
				this.layout[row][col] = (short) line.charAt(col);
			}
			row++;
		}
	}

	/**
	 * Draws the set layout
	 * @param x origin coord
	 * @param y origin coord
	 * @param z origin coord
	 */
	public void draw() {
		for (int zz = 0; zz < layout.length; zz++) {
			for (int xx = 0; xx < layout[0].length; xx++) {
				BlockMaterial material = style.get(layout[zz][xx]);
				getParent().setBlockMaterial(xx, 0, zz, material);
			}
		}
	}

	public StructureComponent getParent() {
		return parent;
	}
}
