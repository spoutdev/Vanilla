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

import org.spout.api.material.SubMaterial;
import org.spout.vanilla.material.Plant;
import org.spout.vanilla.material.generic.GenericBlock;

public class Tree extends GenericBlock implements Plant, SubMaterial {
	
	public final Tree DEFAULT;
	public final Tree SPRUCE;
	public final Tree BIRCH;
	public final Tree JUNGLE;
	
	private final Tree parent;
	private final short data;
	
	private void setDefault() {
		this.setHardness(2.0F).setResistance(3.3F).setOpacity((byte) 1);
	}
	
	private Tree(String name, int data, Tree parent) {
		super(name, 17);
		this.setDefault();
		this.parent = parent;
		this.data = (short) data;
		parent.registerSubMaterial(this);
		this.register();
		
		this.DEFAULT = parent.DEFAULT;
		this.SPRUCE = parent.SPRUCE;
		this.BIRCH = parent.BIRCH;
		this.JUNGLE = parent.JUNGLE;
	}
	
	public Tree(String name) {
		super(name, 17);
		this.setDefault();
		this.parent = this;
		this.data = 0;
		this.register();
		
		this.DEFAULT = new Tree("Default Wood", 0, this);
		this.SPRUCE = new Tree("Spruce Wood", 1, this);
		this.BIRCH = new Tree("Birch Wood", 2, this);
		this.JUNGLE = new Tree("Jungle Wood", 3, this);
	}

	@Override
	public boolean hasGrowthStages() {
		return false;
	}

	@Override
	public int getNumGrowthStages() {
		return 0;
	}

	@Override
	public int getMinimumLightToGrow() {
		return 0;
	}
	
	@Override
	public short getData() {
		return this.data;
	}
	
	@Override
	public Tree getParentMaterial() {
		return this.parent;
	}
}
