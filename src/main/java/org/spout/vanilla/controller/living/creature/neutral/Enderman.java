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
package org.spout.vanilla.controller.living.creature.neutral;

import java.util.HashSet;
import java.util.Set;

import org.spout.api.inventory.ItemStack;
import org.spout.api.material.BlockMaterial;

import org.spout.vanilla.controller.VanillaControllerTypes;
import org.spout.vanilla.controller.living.Creature;
import org.spout.vanilla.controller.living.creature.Neutral;
import org.spout.vanilla.controller.source.HealthChangeReason;
import org.spout.vanilla.material.VanillaMaterials;

public class Enderman extends Creature implements Neutral {
	private BlockMaterial heldItem;
	private BlockMaterial previouslyHeldItem;
	//private Entity parent; @Zidane: This doesn't seem right, it's always null bc you don't override attachToEntity to give it a value, also seems not needed... we got getParent

	public Enderman() {
		super(VanillaControllerTypes.ENDERMAN);
	}

	public BlockMaterial getPreviouslyHeldItem() {
		return previouslyHeldItem;
	}

	public BlockMaterial getHeldItem() {
		return heldItem;
	}

	public void setHeldItem(BlockMaterial heldItem) {
		this.heldItem = heldItem;
	}

	@Override
	public void finalizeTick() {
		super.finalizeTick();
		this.previouslyHeldItem = heldItem;
	}

	@Override
	public void onAttached() {
		super.onAttached();
		getParent().setMaxHealth(40);
		getParent().setHealth(40, new HealthChangeReason(HealthChangeReason.Type.SPAWN));
	}

	@Override
	public Set<ItemStack> getDrops() {
		Set<ItemStack> drops = new HashSet<ItemStack>();
		int count = getRandom().nextInt(2);
		if (count > 0) {
			drops.add(new ItemStack(VanillaMaterials.ENDER_PEARL, count));
		}

		BlockMaterial held = getHeldItem();
		if (held != null && !held.equals(VanillaMaterials.AIR)) {
			drops.add(new ItemStack(held, 1));
			setHeldItem(null);
		}

		return drops;
	}
}
