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

import org.spout.api.material.source.DataSource;
import org.spout.vanilla.material.Fuel;
import org.spout.vanilla.material.block.generic.Solid;

public class Plank extends Solid implements Fuel {
	public static final Plank PLANK = register(new Plank("Oak Plank"));
	public static final Plank PINE = register(new Plank("Pine Plank", WoodType.PINE, PLANK));
	public static final Plank BIRCH = register(new Plank("Birch Plank", WoodType.BIRCH, PLANK));
	public static final Plank JUNGLE = register(new Plank("Jungle Plank", WoodType.JUNGLE, PLANK));
	private final WoodType type;
	
	public final float BURN_TIME = 15.f;

	public Plank(String name) {
		super(name, 05);
		this.setDefault();
		this.type = WoodType.OAK;
	}

	public Plank(String name, WoodType type, Plank parent) {
		super(name, 05, type.getData(), parent);
		this.setDefault();
		this.type = type;
	}

	private void setDefault() {
		this.setHardness(0.8F).setResistance(1.3F);
	}

	public WoodType getType() {
		return type;
	}

	@Override
	public short getData() {
		return type.getData();
	}

	@Override
	public Plank getParentMaterial() {
		return (Plank) super.getParentMaterial();
	}

	public static enum WoodType implements DataSource {
		OAK(0),
		PINE(1),
		BIRCH(2),
		JUNGLE(3),;
		private final short data;

		private WoodType(int data) {
			this.data = (short) data;
		}

		@Override
		public short getData() {
			return this.data;
		}
	}

	@Override
	public float getFuelTime() {
		return BURN_TIME;
	}
}