/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, Spout LLC <http://www.spout.org/>
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
package org.spout.vanilla.plugin.material.item.tool;

import java.util.Random;

import org.spout.api.entity.Entity;
import org.spout.api.event.player.PlayerInteractEvent.Action;
import org.spout.api.geo.LoadOption;
import org.spout.api.inventory.ItemStack;

import org.spout.vanilla.api.data.GameMode;

import org.spout.vanilla.plugin.component.inventory.PlayerInventory;
import org.spout.vanilla.plugin.component.living.passive.Sheep;
import org.spout.vanilla.plugin.component.substance.object.Item;
import org.spout.vanilla.plugin.data.VanillaData;
import org.spout.vanilla.plugin.data.tool.ToolType;
import org.spout.vanilla.plugin.inventory.player.PlayerQuickbar;
import org.spout.vanilla.plugin.material.VanillaMaterials;

public class Shears extends Tool {
	private Random rand = new Random();

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

			sheep.setSheared(true);
			short col = sheep.getColor().getData();

			other.getWorld().createAndSpawnEntity(other.getTransform().getPosition(), Item.class, LoadOption.NO_LOAD);
			Item item = entity.add(Item.class);
			item.setItemStack(new ItemStack(VanillaMaterials.WOOL, col, rand.nextInt(3) + 1));

			if (entity.getData().get(VanillaData.GAMEMODE).equals(GameMode.SURVIVAL)) {
				PlayerQuickbar quickbar = entity.get(PlayerInventory.class).getQuickbar();
				quickbar.getSelectedSlot().addData(1);
			}
		}
	}
}
