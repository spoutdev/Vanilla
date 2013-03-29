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
import org.spout.api.util.cuboid.ChunkCuboidLightBufferWrapper;
import org.spout.api.util.cuboid.ImmutableCuboidBlockMaterialBuffer;
import org.spout.api.util.set.TInt10Procedure;

public class ClearLightProcedure extends TInt10Procedure {
	
	private int currentLevel;
	private final ChunkCuboidLightBufferWrapper<VanillaCuboidLightBuffer> light;
	private final ImmutableCuboidBlockMaterialBuffer material;
	private final VanillaLightingManager manager;
	
	public ClearLightProcedure(VanillaLightingManager manager, ChunkCuboidLightBufferWrapper<VanillaCuboidLightBuffer> light, ImmutableCuboidBlockMaterialBuffer material) {
		this.light = light;
		this.material = material;
		this.manager = manager;
		this.currentLevel = 16;
	}
	
	public void setCurrentLevel(int level) {
		this.currentLevel = level;
	}
	
	@Override
	public boolean execute(int x, int y, int z) {
		return execute(x, y, z, false);
	}
	
	public boolean execute(int x, int y, int z, boolean root) {
		int lightLevel = manager.getLightLevel(light, x, y, z);
		int computedLevel = manager.computeLightLevel(light, material, x, y, z);
		//Spout.getLogger().info("Clearing lower " + x + ", " + y + ", " + z + " actual " + lightLevel + " computed " + computedLevel);
		if (computedLevel < lightLevel) {
			if (!root && lightLevel != currentLevel) {
				throw new IllegalStateException("Light dirty block added to wrong set, light level " + lightLevel + ", expected level " + currentLevel);
			}
			manager.setLightLevel(light, x, y, z, 0);
		}
		return true;
	}

}
