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
package org.spout.vanilla.plugin.world;

import java.util.Random;

import org.spout.api.component.Component;
import org.spout.api.entity.Player;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.cuboid.Region;
import org.spout.api.geo.discrete.Point;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.block.BlockFace;
import org.spout.api.math.MathHelper;
import org.spout.api.scheduler.TaskPriority;
import org.spout.vanilla.plugin.VanillaPlugin;
import org.spout.vanilla.plugin.material.VanillaBlockMaterial;
import org.spout.vanilla.plugin.material.VanillaMaterials;

public class SnowSimulator extends Component {
	final WeatherSimulator weather;
	private int count = 0;

	public SnowSimulator(WeatherSimulator weather) {
		this.weather = weather;
	}

	public World getWorld() {
		return this.weather.getWorld();
	}

	@Override
	public boolean canTick() {
		return true;
	}

	@Override
	public void onTick(float dt) {
		if (count++ % 4 == 0) {
			for (Player player : getWorld().getPlayers()) {
				if (!player.isOnline()) {
					continue;
				}
				Region r = player.getRegion();
				r.getTaskManager().scheduleSyncDelayedTask(VanillaPlugin.getInstance(), new SnowfallTask(player, r), TaskPriority.LOWEST);
			}
		}
	}

	private class SnowfallTask implements Runnable {
		private final Player player;
		private final Region region;
		private SnowfallTask(Player player, Region region) {
			this.player = player;
			this.region = region;
		}
		@Override
		public void run() {
			if (!player.isOnline() || !weather.isRaining()) {
				return;
			}
			Random rand = MathHelper.getRandom();
			Point playerPos = player.getTransform().getPosition();
			final int posX = MathHelper.floor(playerPos.getX());
			final int posY = MathHelper.floor(playerPos.getY());
			final int posZ = MathHelper.floor(playerPos.getZ());
			for (int tries = 0; tries < 10; tries++) {
				//pick a random chunk between -4, -4, to 4, 4 relative to the player's position
				int cx = (rand.nextBoolean() ? -1 : 1) * rand.nextInt(5);
				int cz = (rand.nextBoolean() ? -1 : 1) * rand.nextInt(5);

				//pick random coords to try at inside the chunk (0, 0) to (15, 15)
				int rx = rand.nextInt(16);
				int rz = rand.nextInt(16);

				//pick a offset from the player's y position (-15 - +15) of their position
				int offsetY = (rand.nextBoolean() ? -1 : 1) * rand.nextInt(15);

				int x = posX + cx * 16 + rx;
				int y = posY + offsetY;
				int z = posZ + cz * 16 + rz;
				if (region.containsBlock(x, y, z)) {
					if (weather.isSnowingAt(x, y, z)) {
						//Try to find the surface
						for (int dy = 1; dy < 16; dy++) {
							if (region.containsBlock(x, y - dy, z)) {
								BlockMaterial mat = region.getBlockMaterial(x, y - dy, z);
								if (mat instanceof VanillaBlockMaterial) {
									VanillaBlockMaterial vbm = (VanillaBlockMaterial)mat;
									if (vbm.canSupport(VanillaMaterials.SNOW, BlockFace.TOP)) {
										Block block = region.getBlock(x, y - dy + 1, z);
										if (!VanillaMaterials.SNOW.willMeltAt(block)) {
											block.setMaterial(VanillaMaterials.SNOW);
										}
										return;
									} else if (vbm == VanillaMaterials.SNOW) {
										short data = region.getBlockData(x, y - dy, z);
										region.setBlockData(x, y - dy, z, (short) ((data + 1) & 0xF), null);
										return;
									} else {
										break;
									}
								}
							}
						}
					}
				}
			}
		}
	}
}
