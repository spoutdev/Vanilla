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
package org.spout.vanilla.protocol.entity.creature;

import java.util.List;

import org.spout.api.entity.Entity;
import org.spout.api.util.Parameter;

import org.spout.vanilla.component.entity.living.passive.Horse;

public class HorseEntityProtocol extends CreatureProtocol {
	public static final int TYPE_INDEX = 19; // MC Data index for horse type
	public static final int VARIANT_INDEX = 20; // MC Data index for horse variant & marking
	public static final int OWNER_INDEX = 21; // MC Data index for owner's name
	public static final int ARMOR_TYPE_INDEX = 22; // MC Data index for amount of armor type the horse currently wears, None 0, Iron 1, Gold 2, Diamond 3

	public HorseEntityProtocol() {
		super(CreatureType.HORSE);
	}

	@Override
	public List<Parameter<?>> getSpawnParameters(Entity entity) {
		List<Parameter<?>> parameters = super.getSpawnParameters(entity);
		Horse horse = entity.add(Horse.class);
		// parameters.add(new Parameter<Integer>(Parameter.TYPE_INT, 16, ??)) - Unknown info sent, ID 16
		parameters.add(new Parameter<Byte>(Parameter.TYPE_BYTE, TYPE_INDEX, horse.getHorseTypeId()));
		parameters.add(new Parameter<Integer>(Parameter.TYPE_INT, VARIANT_INDEX, horse.getVariant().getVariantId() | horse.getMarking().getMarkingId()));
		parameters.add(new Parameter<String>(Parameter.TYPE_STRING, OWNER_INDEX, horse.getOwnerName()));
		parameters.add(new Parameter<Integer>(Parameter.TYPE_INT, ARMOR_TYPE_INDEX, 0));
		return parameters;
	}
}
