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
package org.spout.vanilla.protocol.entity.creature;

import java.util.List;

import org.spout.api.data.Data;
import org.spout.api.entity.Entity;
import org.spout.api.inventory.ItemStack;
import org.spout.api.util.Parameter;

public class EndermanEntityProtocol extends CreatureProtocol {
	public final static int ITEM_ID_INDEX = 16; // The MC metadata index for the item in the Enderman's hand.
	public final static int ITEM_DATA_INDEX = 17; // The MC metadata indexfor the item data in the Enderman's hand.

	public EndermanEntityProtocol() {
		super(CreatureType.ENDERMAN);
	}

	@Override
	public List<Parameter<?>> getSpawnParameters(Entity entity) {
		ItemStack held = entity.getDatatable().get(Data.HELD_ITEM);
		List<Parameter<?>> parameters = super.getSpawnParameters(entity);
		if (held != null) {
			parameters.add(new Parameter<Byte>(Parameter.TYPE_BYTE, ITEM_ID_INDEX, (byte) held.getMaterial().getId()));
			parameters.add(new Parameter<Byte>(Parameter.TYPE_BYTE, ITEM_DATA_INDEX, (byte) held.getData()));
		}
		return parameters;
	}
}
