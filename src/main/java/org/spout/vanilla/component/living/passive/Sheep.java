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
package org.spout.vanilla.component.living.passive;

import org.spout.vanilla.VanillaPlugin;
import org.spout.vanilla.component.living.Passive;
import org.spout.vanilla.component.living.VanillaEntity;
import org.spout.vanilla.data.VanillaData;
import org.spout.vanilla.material.block.solid.Wool;
import org.spout.vanilla.protocol.entity.living.SheepEntityProtocol;

/**
 * A component that identifies the entity as a Sheep.
 */
public class Sheep extends VanillaEntity implements Passive {
	@Override
	public void onAttached() {
		super.onAttached();
		getOwner().getNetwork().setEntityProtocol(VanillaPlugin.VANILLA_PROTOCOL_ID, new SheepEntityProtocol());
	}

	public boolean isSheared() {
		return getOwner().getData().get(VanillaData.SHEARED);
	}

	public void setSheared(boolean sheared) {
		getOwner().getData().put(VanillaData.SHEARED, sheared);
	}

	/**
	 * Gets the color of the sheep.
	 * @return color of the sheep.
	 */
	public Wool.WoolColor getColor() {
		return Wool.WoolColor.getById(getOwner().getData().get(VanillaData.WOOL_COLOR));
	}

	/**
	 * Sets the color of the sheep.
	 * @param color
	 */
	public void setColor(Wool.WoolColor color) {
		getOwner().getData().put(VanillaData.WOOL_COLOR, color.getData());
	}
}
