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
import org.spout.vanilla.material.attachable.GroundAttachable;

public class Sapling extends GroundAttachable implements Plant, SubMaterial {
	public final Sapling DEFAULT;
	public final Sapling SPRUCE;
	public final Sapling BIRCH;
	public final Sapling JUNGLE;

	private final Sapling parent;
	private final short data;

	private void setDefault() {
		this.setHardness(0.0F).setResistance(0.0F);
	}

	private Sapling(String name, int data, Sapling parent) {
		super(name, 6);
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

	public Sapling(String name) {
		super(name, 6);
		this.setDefault();
		this.parent = this;
		this.data = 0;
		this.register();
		
		this.DEFAULT = new Sapling("Default Sapling", 0, this);
		this.SPRUCE = new Sapling("Spruce Sapling", 1, this);
		this.BIRCH = new Sapling("Birch Sapling", 2, this);
		this.JUNGLE = new Sapling("Jungle Sapling", 3, this);
	}

	@Override
	public boolean hasGrowthStages() {
		return true;
	}

	@Override
	public int getNumGrowthStages() {
		return 3;
	}

	@Override
	public int getMinimumLightToGrow() {
		return 8;
	}
	
	@Override
	public short getData() {
		return this.data;
	}

	@Override
	public Sapling getParentMaterial() {
		return this.parent;
	}
}
