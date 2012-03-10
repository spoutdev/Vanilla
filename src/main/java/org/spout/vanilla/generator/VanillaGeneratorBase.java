/*
 * This file is part of Vanilla (http://www.spout.org/).
 *
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
package org.spout.vanilla.generator;

import org.spout.api.generator.biome.BiomeGenerator;
import org.spout.api.geo.World;
import org.spout.api.geo.discrete.Point;
import org.spout.api.geo.discrete.atomic.Transform;
import org.spout.api.math.Quaternion;
import org.spout.api.math.Vector3;
import org.spout.api.util.cuboid.CuboidShortBuffer;
import org.spout.vanilla.VanillaMaterials;
import org.spout.vanilla.entity.object.Sky;
import org.spout.vanilla.entity.object.sky.VanillaSky;

public abstract class VanillaGeneratorBase extends BiomeGenerator {

	protected final VanillaSky sky;

	public VanillaGeneratorBase(float spawnX, float spawnY, float spawnZ, VanillaSky sky) {
		super(spawnX, spawnY, spawnZ);
		this.sky = sky;
	}

	@Override
	public void generate(CuboidShortBuffer blockData, int chunkX, int chunkY, int chunkZ) {
		super.generate(blockData, chunkX, chunkY, chunkZ);

		if (chunkY < 0) {
			blockData.flood(VanillaMaterials.BEDROCK.getId());
			return;
		}
	}

	@Override
	public Transform getSpawn(World world) {
		return new Transform(new Point(world, this.spawnX, this.spawnY, this.spawnZ), Quaternion.identity, Vector3.ONE);
	}

	public VanillaSky getSky() {
		return this.sky;
	}
}
