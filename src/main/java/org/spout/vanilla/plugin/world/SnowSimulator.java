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
import org.spout.api.math.Vector3;
import org.spout.api.scheduler.TaskPriority;

import org.spout.vanilla.plugin.VanillaPlugin;
import org.spout.vanilla.plugin.material.VanillaBlockMaterial;
import org.spout.vanilla.plugin.material.VanillaMaterials;
import org.spout.vanilla.plugin.material.block.misc.Snow;

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
		count++;
		if (count >= JUMP_TABLE.length) {
			count = 0;
		}
		for (Player player : getWorld().getPlayers()) {
			if (!player.isOnline()) {
				continue;
			}
			Region r = player.getRegion();
			r.getTaskManager().scheduleSyncDelayedTask(VanillaPlugin.getInstance(), new SnowfallTask(player, r, JUMP_TABLE[count]), TaskPriority.LOWEST);
		}
	}

	private static final Vector3[] JUMP_TABLE = new Vector3[3];

	static {
		for (int i = 0; i < JUMP_TABLE.length; i++) {
			JUMP_TABLE[i] = countToOffset(i);
		}
	}

	private static Vector3 countToOffset(int count) {
		switch (count) {
			case 0:
				return Vector3.ZERO;
			case 1:
				return new Vector3(0, 16, 0);
			case 2:
				return new Vector3(0, -16, 0);
			default:
				return Vector3.ZERO;
		}
	}

	private class SnowfallTask implements Runnable {
		private final Player player;
		private final Region region;
		private final Vector3 offset;

		private SnowfallTask(Player player, Region region, Vector3 offset) {
			this.player = player;
			this.region = region;
			this.offset = offset;
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

				int x = posX + cx * 16 + rx + offset.getFloorX();
				int y = posY + offsetY + offset.getFloorY();
				int z = posZ + cz * 16 + rz + offset.getFloorZ();
				if (region.containsBlock(x, y, z)) {
					if (weather.isSnowingAt(x, y, z)) {
						//Try to find the surface
						for (int dy = 1; dy < 16; dy++) {
							if (region.containsBlock(x, y - dy, z)) {
								Block block = region.getBlock(x, y - dy, z);
								BlockMaterial mat = block.getMaterial();
								if (mat instanceof VanillaBlockMaterial) {
									VanillaBlockMaterial vbm = (VanillaBlockMaterial) mat;
									//Place snow ontop of solid
									if (vbm.canSupport(VanillaMaterials.SNOW, BlockFace.TOP)) {
										Block above = block.translate(BlockFace.TOP);
										if (!VanillaMaterials.SNOW.willMeltAt(above)) {
											above.setMaterial(VanillaMaterials.SNOW);
										}
										break;
									//Try to grow snow
									} else if (vbm instanceof Snow) {
										short data = block.getData();
										if (data == 0x7) {
											Block above = block.translate(BlockFace.TOP);
											if (above.getMaterial() == BlockMaterial.AIR) {
												above.setMaterial(VanillaMaterials.SNOW);
											}
										} else {
											block.setData(data + 1);
										}
										break;
									} else {
										break;
									}
								}
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
