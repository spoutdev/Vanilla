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
package org.spout.vanilla.entity.creature.neutral;

import org.spout.api.data.Data;
import org.spout.api.inventory.ItemStack;

import org.spout.vanilla.entity.VanillaControllerTypes;
import org.spout.vanilla.entity.creature.Creature;

public class Human extends Creature {
	private String displayName;
	private ItemStack renderedItemInHand;

	public Human() {
		super(VanillaControllerTypes.HUMAN);
	}

	@Override
	public void onAttached() {
		if (displayName == null || displayName.isEmpty()) {
			displayName = getDataMap().get(Data.TITLE);
		}
		if (renderedItemInHand == null) {
			renderedItemInHand = getDataMap().get(Data.HELD_ITEM);
		}
		super.onAttached();
	}

	@Override
	public void onSave() {
		super.onSave();
		getDataMap().put(Data.TITLE, displayName);
		getDataMap().put(Data.HELD_ITEM, renderedItemInHand);
	}

	/**
	 * Gets the item rendered in the human's hand; not neccassaily the actual item in the human's hand.
	 * @return rendered item in hand
	 */
	public ItemStack getRenderedItemInHand() {
		return renderedItemInHand;
	}

	/**
	 * Sets the item rendered in the human's hand; not neccassaily the actual item in the human's hand.
	 * @param renderedItemInHand
	 */
	public void setRenderedItemInHand(ItemStack renderedItemInHand) {
		this.renderedItemInHand = renderedItemInHand;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
}
