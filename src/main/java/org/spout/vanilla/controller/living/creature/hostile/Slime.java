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
package org.spout.vanilla.controller.living.creature.hostile;

import java.util.HashSet;
import java.util.Set;

import org.spout.api.Source;
import org.spout.api.entity.Entity;
import org.spout.api.entity.type.ControllerType;
import org.spout.api.entity.type.EmptyConstructorControllerType;
import org.spout.api.inventory.ItemStack;

import org.spout.vanilla.controller.VanillaActionController;
import org.spout.vanilla.controller.VanillaControllerType;
import org.spout.vanilla.controller.VanillaControllerTypes;
import org.spout.vanilla.controller.living.Creature;
import org.spout.vanilla.controller.living.creature.Hostile;
import org.spout.vanilla.controller.source.HealthChangeReason;
import org.spout.vanilla.material.VanillaMaterials;

public class Slime extends Creature implements Hostile {
	private byte size;

	public Slime(byte size) {
		super(VanillaControllerTypes.SLIME);
		this.size = size;
	}

	public Slime() {
		super(VanillaControllerTypes.SLIME);
		this.size = ((Byte) data().get("slime_size", getRandom().nextInt(4)));
	}

	public Slime(VanillaControllerType type, byte size) {
		super(type);
		this.size = size;
	}

	public Slime(VanillaControllerType type) {
		super(type);
		this.size = ((Byte) data().get("slime_size", getRandom().nextInt(4)));
	}

	@Override
	public void onAttached() {
		int health = size > 0 ? size * 4 : 1;
		setHealth(health, HealthChangeReason.SPAWN);
		setMaxHealth(health);
		super.onAttached();
	}

	@Override
	public void onSave() {
		super.onSave();
		data().put("slime_size", size);
	}

	@Override
	public void onDeath() {
		if (size > 0) {
			Entity parent = getParent();
			for (int s = 0; s < getRandom().nextInt(2) + 2; s++) {
				byte newSize = size;
				newSize /= 2;
				parent.getWorld().createAndSpawnEntity(parent.getPosition(), new Slime(newSize));
			}
		}
	}

	@Override
	public Set<ItemStack> getDrops(Source source, VanillaActionController lastDamager) {
		Set<ItemStack> drops = new HashSet<ItemStack>();
		if (getSize() == 0) {
			return drops;
		}

		int count = getRandom().nextInt(3);
		if (count > 0 && size <= 0) {
			drops.add(new ItemStack(VanillaMaterials.SLIMEBALL, count));
		}

		return drops;
	}

	/**
	 * Gets the size of the slime between 0 and 4.
	 * @return slime's size.
	 */
	public byte getSize() {
		return size;
	}

	/**
	 * Sets the size of the slime. Must be between 0 and 4.
	 * @param size
	 */
	public void setSize(byte size) {
		if (size < 0 || size > 4) {
			throw new IllegalArgumentException("A Slime's size must be between 0 and 4");
		}

		this.size = size;
	}
}
