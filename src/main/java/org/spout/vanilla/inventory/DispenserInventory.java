/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, SpoutDev <http://www.spout.org/>
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
package org.spout.vanilla.inventory;

import org.spout.api.inventory.Inventory;

import org.spout.vanilla.controller.block.DispenserController;

/**
 * Represents a dispenser inventory belonging to a dispenser controller.
 */
public class DispenserInventory extends Inventory implements VanillaInventory {
	private static final long serialVersionUID = 1L;
	private final DispenserController owner;

	public DispenserInventory(DispenserController owner) {
		super(45);
		this.owner = owner;
	}

	/**
	 * Returns the dispenser controller that this inventory belongs to.
	 * @return owner the dispenser controller
	 */
	public DispenserController getOwner() {
		return owner;
	}

	@Override
	public Window getWindow() {
		return Window.DISPENSER;
	}
}
