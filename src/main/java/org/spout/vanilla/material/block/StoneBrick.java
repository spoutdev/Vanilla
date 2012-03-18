/*
 * This file is part of Vanilla (http://www.spout.org/).
 *
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
package org.spout.vanilla.material.block;

import org.spout.api.material.Material;
import org.spout.api.material.SubMaterial;
import org.spout.vanilla.material.generic.GenericBlock;

public class StoneBrick extends GenericBlock implements SubMaterial {
	public final StoneBrick STONE;
	public final StoneBrick MOSSY_STONE;
	public final StoneBrick CRACKED_STONE;

	private final StoneBrick parent;
	private final short data;

	private void setDefault() {
		this.setHardness(1.5F);
		this.setResistance(10.0F);
	}

	private StoneBrick(String name, int data, StoneBrick parent) {
		super(name, 98);
		this.setDefault();
		this.parent = parent;
		this.data = (short) data;
		this.register();
		parent.registerSubMaterial(this);
		
		this.STONE = parent.STONE;
		this.MOSSY_STONE = parent.MOSSY_STONE;
		this.CRACKED_STONE = parent.CRACKED_STONE;
	}

	public StoneBrick(String name) {
		super(name, 98);
		this.setDefault();
		this.parent = this;
		this.data = 0;
		this.register();
		
		this.STONE = new StoneBrick("Stone Brick", 0, this);
		this.MOSSY_STONE = new StoneBrick("Mossy Stone Brick", 1, this);
		this.CRACKED_STONE = new StoneBrick("Cracked Stone Brick", 2, this);
	}

	@Override
	public short getData() {
		return this.data;
	}

	@Override
	public Material getParentMaterial() {
		return this.parent;
	}
}
