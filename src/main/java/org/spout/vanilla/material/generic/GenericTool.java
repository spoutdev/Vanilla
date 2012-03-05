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
package org.spout.vanilla.material.generic;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.spout.api.material.BlockMaterial;
import org.spout.vanilla.material.Tool;

public class GenericTool extends GenericItem implements Tool {
	private short durability;
	private Map<BlockMaterial, Float> strengthModifiers = new HashMap<BlockMaterial, Float>();

	public GenericTool(String name, int id, short durability) {
		super(name, id);
		this.durability = durability;
	}

	@Override
	public short getDurability() {
		return durability;
	}

	@Override
	public Tool setDurability(short durability) {
		this.durability = durability;
		return this;
	}

	@Override
	public float getStrengthModifier(BlockMaterial block) {
		if (!(strengthModifiers.containsKey(block))) {
			return (float) 1.0;
		}
		return strengthModifiers.get(block);
	}

	@Override
	public Tool setStrengthModifier(BlockMaterial block, float modifier) {
		strengthModifiers.put(block, modifier);
		return this;
	}

	@Override
	public Set<BlockMaterial> getStrengthModifiedBlocks() {
		return strengthModifiers.keySet();
	}
}
