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
package org.spout.vanilla.controller.living.creature.passive;

import org.spout.api.inventory.ItemStack;
import org.spout.vanilla.controller.VanillaControllerTypes;
import org.spout.vanilla.controller.living.Creature;
import org.spout.vanilla.controller.living.creature.Passive;
import org.spout.vanilla.controller.source.HealthChangeReason;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.Wool;

import java.util.HashSet;
import java.util.Set;

public class Sheep extends Creature implements Passive {
	private int color;

	public Sheep() {
		this(0x0);
	}

	public Sheep(Wool.WoolColor color) {
		this(color.getData());
	}

	public Sheep(int color) {
		super(VanillaControllerTypes.SHEEP);
		this.color = color;
	}

	@Override
	public void onAttached() {
		super.onAttached();
		getParent().setData("SheepSheared", false);
		getParent().setData("SheepColor", color);
		getParent().setMaxHealth(8);
		getParent().setHealth(8, new HealthChangeReason(HealthChangeReason.Type.SPAWN));
	}

	/**
	 * Whether or not the sheep's wool has been sheared.
	 *
	 * @return true if sheared.
	 */
	public boolean isSheared() {
		return getParent().getData("SheepSheared").asBool();
	}

	/**
	 * Sets whether or not the sheep's wool has been sheared.
	 *
	 * @param sheared
	 */
	public void setSheared(boolean sheared) {
		getParent().setData("SheepSheared", sheared);
	}

	/**
	 * Gets the color of the sheep.
	 *
	 * @return color of the sheep.
	 */
	public Wool.WoolColor getColor() {
		return Wool.WoolColor.getById((short) color);
	}

	/**
	 * Sets the color of the sheep.
	 *
	 * @param color
	 */
	public void setColor(Wool.WoolColor color) {
		getParent().setData("SheepColor", color.getData());
		this.color = color.getData();
	}

	@Override
	public Set<ItemStack> getDrops() {
		Set<ItemStack> drops = new HashSet<ItemStack>();
		if (!isSheared()) {
			drops.add(new ItemStack(VanillaMaterials.WOOL.getSubMaterial((short) color), 1));
		}

		return drops;
	}
}
