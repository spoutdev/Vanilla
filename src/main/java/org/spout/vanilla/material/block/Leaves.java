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

import java.util.Random;

import org.spout.api.material.Material;
import org.spout.api.material.SubMaterial;
import org.spout.vanilla.VanillaMaterials;

public class Leaves extends Solid implements SubMaterial {
	public final Leaves DEFAULT;
	public final Leaves SPRUCE;
	public final Leaves BIRCH;
	public final Leaves JUNGLE;
	
	private Random rand = new Random();
	private final Leaves parent;
	private final short data;
	
	private void setDefault() {
		this.setHardness(0.2F).setResistance(0.3F);
	}

	private Leaves(String name, int data, Leaves parent) {
		super(name, 18);
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

	public Leaves(String name) {
		super(name, 18);
		this.setDefault();
		this.parent = this;
		this.data = 0;
		this.register();
		
		this.DEFAULT = new Leaves("Default Leaves", 0, this);
		this.SPRUCE = new Leaves("Spruce Leaves", 0, this);
		this.BIRCH = new Leaves("Birch Leaves", 0, this);
		this.JUNGLE = new Leaves("Jungle Leaves", 0, this);
	}

	// TODO: Shears
	@Override
	public Material getDrop() {
		if (rand.nextInt(20) == 0) {
			return VanillaMaterials.SAPLING;
		}
		else if (rand.nextInt(200) == 0) {
			return VanillaMaterials.RED_APPLE;
		}
		
		return VanillaMaterials.AIR;
	}
	
	// TODO: Decay
	
	@Override
	public short getData() {
		return this.data;
	}

	@Override
	public Leaves getParentMaterial() {
		return this.parent;
	}
}
