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

import org.spout.api.entity.Entity;

import org.spout.vanilla.controller.VanillaControllerType;
import org.spout.vanilla.controller.VanillaControllerTypes;
import org.spout.vanilla.controller.living.Creature;
import org.spout.vanilla.controller.living.creature.Hostile;
import org.spout.vanilla.data.VanillaData;
import org.spout.vanilla.material.VanillaMaterials;

public class Slime extends Creature implements Hostile {
	private byte size;

	public Slime() {
		this((byte) 0);
	}

	public Slime(VanillaControllerType type) {
		this(type, (byte) 0);
	}

	public Slime(byte size) {
		super(VanillaControllerTypes.SLIME);
		this.size = size;
	}

	public Slime(VanillaControllerType type, byte size) {
		super(type);
		this.size = size;
	}

	@Override
	public void onAttached() {
		super.onAttached();
		setSize(data().get(VanillaData.SLIME_SIZE, this.size));
		getHealth().setSpawnHealth(size > 0 ? size * 4 : 1);
	}

	@Override
	public void onSave() {
		super.onSave();
		data().put(VanillaData.SLIME_SIZE, size);
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
		if (this.size == 0 && size > 0) {
			getDrops().addRange(VanillaMaterials.SLIMEBALL, 2);
		} else if (this.size > 0 && size == 0) {
			getDrops().remove(VanillaMaterials.SLIMEBALL);
		}
		this.size = size;
	}
}
