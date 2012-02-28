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
 * the MIT license and the SpoutDev license version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license,
 * including the MIT license.
 */
package org.spout.vanilla.material.block;

import org.spout.vanilla.material.Block;
import org.spout.vanilla.material.generic.GenericBlock;

public class Air extends GenericBlock implements Block {
	public Air(String name) {
		super(name, 0);
	}

	@Override
	public float getFriction() {
		return 0;
	}

	@Override
	public GenericBlock setFriction(float slip) {
		return this;
	}

	@Override
	public float getHardness() {
		return 0;
	}

	@Override
	public GenericBlock setHardness(float hardness) {
		return this;
	}

	@Override
	public boolean isOpaque() {
		return false;
	}

	@Override
	public GenericBlock setOpaque(boolean opaque) {
		return this;
	}

	@Override
	public int getLightLevel() {
		return 0;
	}

	@Override
	public GenericBlock setLightLevel(int level) {
		return this;
	}
}
