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
package org.spout.vanilla.components.substance.material;

import org.spout.api.entity.Player;
import org.spout.api.inventory.InventoryBase;
import org.spout.api.material.BlockMaterial;

import org.spout.vanilla.components.window.Window;
import org.spout.vanilla.data.VanillaData;
import org.spout.vanilla.inventory.InventoryOwner;
import org.spout.vanilla.material.block.controlled.DispenserBlock;

public class Dispenser extends WindowBlockComponent implements InventoryOwner {
	@Override
	public DispenserBlock getMaterial() {
		return (DispenserBlock) super.getMaterial();
	}

	@Override
	public void setMaterial(BlockMaterial material) {
		if (!(material instanceof DispenserBlock)) {
			throw new IllegalArgumentException("Material passed in must be an instance of a DispenserBlock.");
		}
		super.setMaterial(material);
	}

	public boolean isPowered() {
		return getData().get(VanillaData.IS_POWERED);
	}

	public void setPowered(boolean powered) {
		getData().put(VanillaData.IS_POWERED, powered);
	}

	@Override
	public Window createWindow(Player player) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InventoryBase getInventory() {
		// TODO Auto-generated method stub
		return null;
	}
}
