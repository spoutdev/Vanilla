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
package org.spout.vanilla.util;

import java.awt.*;

import org.spout.api.util.config.serialization.GenericType;
import org.spout.api.util.config.serialization.Serializer;

public class ColorSerializer extends Serializer {
	@Override
	protected Object handleSerialize(GenericType type, Object value) {
		final Color color = (Color) value;
		return "rgb=" + color.getRed() + "," + color.getGreen() + "," + color.getBlue();
	}

	@Override
	protected Object handleDeserialize(GenericType type, Object value) {
		final String[] string = ((String) value).split(",");
		return new Color(Integer.parseInt(string[0].substring(4)), Integer.parseInt(string[1]), Integer.parseInt(string[2]));
	}

	@Override
	public boolean isApplicable(GenericType type) {
		return Color.class.isAssignableFrom(type.getMainType());
	}

	@Override
	protected int getParametersRequired() {
		return 0;
	}
}
