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
package org.spout.vanilla.world.lighting;

import org.spout.api.Spout;
import org.spout.api.geo.cuboid.Block;

public class VanillaLighting {
	public static final VanillaSkylightLightingManager SKY_LIGHT = new VanillaSkylightLightingManager("skylight");
	public static final VanillaBlocklightLightingManager BLOCK_LIGHT = new VanillaBlocklightLightingManager("block");

	public static void initialize() {
		boolean initialized = false;
		if (initialized) {
			Spout.getLogger().info("Vanilla lighting initialized more than once");
		}
	}
	
	// TODO - add these to World/Region/Chunk/Block
	public static byte getLight(Block b) {
		return (byte) Math.max(getBlockLight(b), getSkyLight(b));
	}
	
	public static byte getBlockLight(Block b) {
		return getLight(b, BLOCK_LIGHT);
	}

	public static byte getSkyLight(Block b) {
		return getLight(b, SKY_LIGHT);
	}
	
	public static byte getLight(Block b, VanillaLightingManager manager) {
		VanillaCuboidLightBuffer light =  (VanillaCuboidLightBuffer) b.getChunk().getLightBuffer(manager.getId());
		return light.get(b.getX(), b.getY(), b.getZ());
	}
}
