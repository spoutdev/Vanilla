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
package org.spout.vanilla.controller.living.creature.hostile;

import org.spout.api.entity.Entity;
import org.spout.api.inventory.ItemStack;
import org.spout.vanilla.controller.VanillaControllerType;
import org.spout.vanilla.controller.VanillaControllerTypes;
import org.spout.vanilla.controller.living.Creature;
import org.spout.vanilla.controller.living.creature.Hostile;
import org.spout.vanilla.controller.source.HealthChangeReason;
import org.spout.vanilla.material.VanillaMaterials;

import java.util.HashSet;
import java.util.Set;

public class Slime extends Creature implements Hostile {
	private Entity parent;

	protected Slime(VanillaControllerType type) {
		super(type);
	}

	@Override
	public void onAttached() {
		super.onAttached();
		parent = getParent();
		int size = getRandom().nextInt(4);
		int health = size > 0 ? size * 4 : 1;
		parent.setData("SlimeSize", size);
		parent.setMaxHealth(health);
		parent.setHealth(health, new HealthChangeReason(HealthChangeReason.Type.SPAWN));
	}

	public Slime() {
		super(VanillaControllerTypes.SLIME);
	}

	@Override
	public Set<ItemStack> getDrops() {
		Set<ItemStack> drops = new HashSet<ItemStack>();
		if (getSize() == 0) {
			return drops;
		}

		int count = getRandom().nextInt(3);
		if (count > 0) {
			drops.add(new ItemStack(VanillaMaterials.SLIMEBALL, count));
		}

		return drops;
	}

	/**
	 * Gets the size of the slime between 0 and 4.
	 *
	 * @return slime's size.
	 */
	public byte getSize() {
		return (byte) parent.getData("SlimeSize").asInt();
	}

	/**
	 * Sets the size of the slime. Must be between 0 and 4.
	 *
	 * @param size
	 */
	public void setSize(byte size) {
		if (size < 0 || size > 4) {
			throw new IllegalArgumentException("A Slime's size must be between 0 and 4");
		}

		parent.setData("SlimeSize", size);
	}
}
