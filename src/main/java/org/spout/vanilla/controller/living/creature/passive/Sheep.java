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
package org.spout.vanilla.controller.living.creature.passive;

import java.util.HashSet;
import java.util.Set;

import org.spout.api.Source;
import org.spout.api.inventory.ItemStack;

import org.spout.vanilla.controller.VanillaActionController;
import org.spout.vanilla.controller.VanillaControllerTypes;
import org.spout.vanilla.controller.living.Creature;
import org.spout.vanilla.controller.living.creature.Passive;
import org.spout.vanilla.controller.source.DamageCause;
import org.spout.vanilla.controller.source.HealthChangeReason;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.solid.Wool;

public class Sheep extends Creature implements Passive {
	private boolean isSheared = false;
	private short sheepColor = 0;

	public Sheep() {
		super(VanillaControllerTypes.SHEEP);
	}

	@Override
	public void onAttached() {
		setHealth(8, HealthChangeReason.SPAWN);
		setMaxHealth(8);
		super.onAttached();
		isSheared = data().get("sheep_sheared", false);
		sheepColor = data().get("sheep_color", (short) 0);
	}

	@Override
	public void onSave() {
		super.onSave();
		data().put("sheep_sheared", isSheared);
		data().put("sheep_color", sheepColor);
	}

	/**
	 * Whether or not the sheep's wool has been sheared.
	 * @return true if sheared.
	 */
	public boolean isSheared() {
		return isSheared;
	}

	/**
	 * Sets whether or not the sheep's wool has been sheared.
	 * @param sheared
	 */
	public void setSheared(boolean sheared) {
		this.isSheared = sheared;
	}

	/**
	 * Gets the color of the sheep.
	 * @return color of the sheep.
	 */
	public Wool.WoolColor getColor() {
		return Wool.WoolColor.getById(sheepColor);
	}

	/**
	 * Sets the color of the sheep.
	 * @param color
	 */
	public void setColor(Wool.WoolColor color) {
		sheepColor = color.getData();
	}

	@Override
	public Set<ItemStack> getDrops(Source source, VanillaActionController lastDamager) {
		Set<ItemStack> drops = new HashSet<ItemStack>();
		if (!isSheared()) {
			if (source == DamageCause.BURN) {
				drops.add(new ItemStack(Wool.GRAY, 1));
			} else {
				drops.add(new ItemStack(VanillaMaterials.WOOL.getSubMaterial(sheepColor), 1));
			}
		}

		return drops;
	}
}
