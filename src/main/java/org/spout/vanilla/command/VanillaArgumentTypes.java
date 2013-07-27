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
package org.spout.vanilla.command;

import org.spout.api.command.CommandArguments;
import org.spout.api.exception.ArgumentParseException;
import org.spout.api.material.Material;
import org.spout.api.material.MaterialRegistry;

import org.spout.vanilla.data.GameMode;
import org.spout.vanilla.material.VanillaMaterials;

/**
 * Handlers for various argument types in Vanilla <p/> All methods should be static, etc, etc, etc
 */
public final class VanillaArgumentTypes {
	private VanillaArgumentTypes() {
		// NO INSTANCES FOR YOU, FOOL!
	}

	public static Material popMaterial(String argName, CommandArguments args) throws ArgumentParseException {
		String arg = args.currentArgument(argName);
		Material mat;
		try {
			mat = VanillaMaterials.getMaterial((short) Integer.parseInt(arg));
		} catch (NumberFormatException ex) {
			mat = MaterialRegistry.get(arg);
		}
		if (mat == null) {
			throw args.failure(argName, "Unknown material: " + arg, false);
		}
		return args.success(argName, mat);
	}

	public static GameMode popGameMode(String argName, CommandArguments args) throws ArgumentParseException {
		String raw = args.currentArgument(argName);
		if (raw.length() == 1) {
			GameMode mode = null;
			if (raw.equalsIgnoreCase("s")) {
				mode = GameMode.SURVIVAL;
			} else if (raw.equalsIgnoreCase("c")) {
				mode = GameMode.CREATIVE;
			} else if (raw.equalsIgnoreCase("a")) {
				mode = GameMode.ADVENTURE;
			}
			if (mode != null) {
				return args.success(argName, mode);
			}
		}
		return args.popEnumValue(argName, GameMode.class);
	}
}
