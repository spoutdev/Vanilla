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
package org.spout.vanilla.controller.living.logic;

import org.spout.api.geo.discrete.Point;
import org.spout.api.inventory.ItemStack;
import org.spout.api.material.Material;
import org.spout.api.math.Vector3;
import org.spout.api.tickable.LogicPriority;
import org.spout.api.tickable.LogicRunnable;
import org.spout.vanilla.controller.living.Creature;
import org.spout.vanilla.controller.object.moving.Item;

/**
 * Logic for creatures that drop an {@link Item} every x ticks.
 *
 */
public class DropItemTimeBasedLogic extends LogicRunnable<Creature> {

	private int itemDropMaxTime;
	private int itemDropMinTime;
	private Material itemToDrop;
	private int numberOfItems;
	private int ticksTillNextDrop;

	/**
	 * Creates the logic for creatures that drop an {@link Item} every x ticks with normal priority.
	 * 
	 * @param parent the creature dropping the item
	 * @param itemToDrop the Item to be creature by the entity
	 * @param amountOfItems how many of the specified items should be dropped 
	 * @param itemDropMinTime the minimum time between to drops
	 * @param itemDropMaxTime the maximum time between to drops
	 */
	public DropItemTimeBasedLogic(Creature parent, Material itemToDrop, int amountOfItems, int itemDropMinTime, int itemDropMaxTime) {
		this(parent, itemToDrop, amountOfItems, itemDropMinTime, itemDropMaxTime, LogicPriority.NORMAL);
	}

	/**
	 * Creates the logic for creatures that drop an {@link Item} every x ticks.
	 * 
	 * @param parent the creature dropping the item
	 * @param itemToDrop the Item to be creature by the entity
	 * @param amountOfItems how many of the specified items should be dropped 
	 * @param itemDropMinTime the minimum time between to drops
	 * @param itemDropMaxTime the maximum time between to drops
	 * @param priority the priority of this logic
	 */
	public DropItemTimeBasedLogic(Creature parent, Material itemToDrop, int amountOfItems, int itemDropMinTime, int itemDropMaxTime, LogicPriority priority) {
		super(parent, priority);
		this.itemToDrop = itemToDrop;
		this.numberOfItems = amountOfItems;
		this.itemDropMinTime = itemDropMinTime;
		this.itemDropMaxTime = itemDropMaxTime;
		ticksTillNextDrop = parent.getRandom().nextInt(itemDropMaxTime - itemDropMinTime) + itemDropMinTime;
	}

	@Override
	public void run() {
		final Point position = parent.getParent().getLastTransform().getPosition();
		final Item item = new Item(new ItemStack(itemToDrop, numberOfItems), Vector3.ZERO);
		position.getWorld().createAndSpawnEntity(position, item);
		ticksTillNextDrop = parent.getRandom().nextInt(itemDropMaxTime - itemDropMinTime) + itemDropMinTime;
	}

	@Override
	public boolean shouldRun(float dt) {
		ticksTillNextDrop--;
		if (ticksTillNextDrop <= 0) {
			return true;
		}
		return false;
	}

}
