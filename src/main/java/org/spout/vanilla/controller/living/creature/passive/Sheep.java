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

import java.util.HashSet;
import java.util.Set;

import org.spout.api.entity.Entity;
import org.spout.api.geo.discrete.Transform;
import org.spout.api.inventory.ItemStack;
import org.spout.api.math.Quaternion;
import org.spout.api.math.Vector3;

import org.spout.vanilla.VanillaMaterials;
import org.spout.vanilla.controller.ControllerType;
import org.spout.vanilla.controller.living.Creature;
import org.spout.vanilla.controller.living.creature.Passive;
import org.spout.vanilla.material.block.Wool;

public class Sheep extends Creature implements Passive {
	private int color;
	private Entity parent = getParent();
	
	public Sheep() {
		this(0x0);
	}

	public Sheep(Wool.WoolColor color) {
		this(color.getData());
	}

	public Sheep(int color) {
		super();
		this.color = color;
	}

	@Override
	public void onAttached() {
		super.onAttached();
		parent.setData(ControllerType.KEY, ControllerType.SHEEP.id);
		parent.setData("SheepSheared", false);
		parent.setData("SheepColor", color);
		parent.setMaxHealth(5);
		parent.setHealth(5);
	}

	/**
	 * Whether or not the sheep's wool has been sheared.
	 * 
	 * @return true if sheared.
	 */
	public boolean isSheared() {
		return parent.getData("SheepSheared").asBool();
	}

	/**
	 * Sets whether or not the sheep's wool has been sheared.
	 * 
	 * @param sheared
	 */
	public void setSheared(boolean sheared) {
		parent.setData("SheepSheared", sheared);
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
		parent.setData("SheepColor", color.getData());
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
