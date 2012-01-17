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
 * the MIT license and the SpoutDev license version 1 along with this program.  
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license, 
 * including the MIT license.
 */
package org.spout.vanilla.event.entity;

import java.util.List;

import org.spout.api.entity.Entity;
import org.spout.api.event.HandlerList;
import org.spout.api.event.entity.EntityEvent;
import org.spout.api.inventory.ItemStack;

/**
 * Called when an entity dies.
 */
public class EntityDeathEvent extends EntityEvent {
	public EntityDeathEvent(Entity e) {
		super(e);
		// TODO Auto-generated constructor stub
	}

	private static HandlerList handlers = new HandlerList();

	private int dropExp;

	private List<ItemStack> drops;

	/**
	 * Gets the amount of experience to drop.
	 *
	 * @return The amount of experience to drop.
	 */
	public int getDropExp() {
		return dropExp;
	}

	/**
	 * Sets the amount of experience to drop.
	 *
	 * @param dropExp The experience to set.
	 */
	public void setDropExp(int dropExp) {
		this.dropExp = dropExp;
	}

	/**
	 * The drops to drop.
	 *
	 * @return The drops to drop.
	 */
	public List<ItemStack> getDrops() {
		return drops;
	}

	/**
	 * Sets the drops to drop.
	 *
	 * @param drops The drops to set.
	 */
	public void setDrops(List<ItemStack> drops) {
		this.drops = drops;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}