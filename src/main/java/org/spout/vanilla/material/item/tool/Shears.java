/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011 Spout LLC <http://www.spout.org/>
 * Vanilla is licensed under the Spout License Version 1.
 *
 * Vanilla is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the Spout License Version 1.
 *
 * Vanilla is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for
 * more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the Spout License Version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://spout.in/licensev1> for the full license, including
 * the MIT license.
 */
package org.spout.vanilla.material.item.tool;

import org.spout.api.entity.Entity;
import org.spout.api.event.player.Action;
import org.spout.api.inventory.ItemStack;

import org.spout.math.GenericMath;
import org.spout.vanilla.VanillaPlugin;
import org.spout.vanilla.component.entity.living.passive.Sheep;
import org.spout.vanilla.component.entity.substance.Item;
import org.spout.vanilla.data.tool.ToolType;
import org.spout.vanilla.event.entity.SheepShearedEvent;
import org.spout.vanilla.material.VanillaMaterials;

public class Shears extends Tool {
	public Shears(String name, int id, short durability) {
		super(name, id, durability, ToolType.SHEARS, null);
	}

	@Override
	public void onInteract(Entity entity, Entity other, Action action) {
		if (action == Action.RIGHT_CLICK) {
			Sheep sheep = other.get(Sheep.class);

			if (sheep == null) {
				return;
			}

			if (sheep.isSheared()) {
				//TODO: Also return if this is a baby sheep
				return;
			}
			short col = sheep.getColor().getData();
			ItemStack itemStack = new ItemStack(VanillaMaterials.WOOL, col, GenericMath.getRandom().nextInt(3) + 1);

			SheepShearedEvent event = VanillaPlugin.getInstance().getEngine().getEventManager().callEvent(new SheepShearedEvent(other, entity, itemStack));

			if (event.isCancelled()) {
				return;
			}

			sheep.setSheared(true);

			Item.dropNaturally(other.getPhysics().getPosition(), event.getItemStack());
		}
	}
}
