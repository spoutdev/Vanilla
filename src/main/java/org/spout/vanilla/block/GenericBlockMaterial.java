/*
 * This file is part of Vanilla (http://www.spout.org/).
 *
 * Vanilla is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Vanilla is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.spout.vanilla.block;

import org.spout.api.material.BlockMaterial;
import org.spout.api.render.BlockDesign;
import org.spout.vanilla.item.GenericItemMaterial;

public class GenericBlockMaterial extends GenericItemMaterial implements BlockMaterial {

	private BlockDesign design;

	private GenericBlockMaterial(String name, int id, int data, boolean subtypes) {
		super(name, id, data, subtypes);
	}

	protected GenericBlockMaterial(String name, int id, int data) {
		this(name, id, data, true);
	}

	protected GenericBlockMaterial(String name, int id) {
		this(name, id, 0, false);
	}

	public float getFriction() {
		return 0;
	}

	public BlockMaterial setFriction(float friction) {
		return this;
	}

	public float getHardness() {
		return 0;
	}

	public BlockMaterial setHardness(float hardness) {
		return this;
	}

	public boolean isOpaque() {
		return false;
	}

	public BlockMaterial setOpaque(boolean opaque) {
		return this;
	}

	public int getLightLevel() {
		return 0;
	}

	public BlockMaterial setLightLevel(int level) {
		return this;
	}

	public BlockDesign getBlockDesign() {
		return design;
	}

	public BlockMaterial setBlockDesign(BlockDesign design) {
		this.design = design;
		return this;
	}
}
