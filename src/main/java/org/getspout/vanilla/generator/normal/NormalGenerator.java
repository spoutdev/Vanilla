/*
 * This file is part of Vanilla (http://www.getspout.org/).
 *
 * Vanilla is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Vanilla is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.getspout.vanilla.generator.normal;

import java.util.Random;

import net.royawesome.jlibnoise.module.source.Perlin;

import org.getspout.api.generator.Populator;
import org.getspout.api.generator.WorldGenerator;
import org.getspout.api.util.cuboid.CuboidShortBuffer;
import org.getspout.vanilla.VanillaBlocks;

public class NormalGenerator implements WorldGenerator {
	int seed = 42;
	Perlin noiseHeight = new Perlin();
	Perlin noiseJitter = new Perlin();
	Perlin noiseType = new Perlin();
	Random random = new Random();
	
	public NormalGenerator() {
		noiseHeight.setOctaveCount(5);
		noiseHeight.setSeed(seed);

		noiseJitter.setOctaveCount(5);
		noiseJitter.setSeed(seed);
		
		noiseType.setOctaveCount(5);
		noiseType.setSeed(seed);
		
		random.setSeed(seed);
	}

	private final Populator[] populators = new Populator[]{new TreePopulator(), new PondPopulator(), new StrongholdPopulator(), new VillagePopulator(), new AbandonedMineshaftPopulator(), new DungeonPopulator()};
	
	public Populator[] getPopulators() {
		return populators;
	}

	public void generate(CuboidShortBuffer blockData, int chunkX, int chunkY, int chunkZ) {
		int x = chunkX * 16;
		int y = chunkY * 16;
		int z = chunkZ * 16;
		
		if (y > 127) {
			blockData.flood((short)0);
			//return;
		}
		if (chunkY < 0) {
			blockData.flood(VanillaBlocks.bedrock.getId());
			//return;
		}

		for (int dx = x; dx < (x+16); dx++) {
			for (int dy = y; dy < (y+16); dy++) {
				for (int dz = z; dz < (z+16); dz++) {
					if (dy == 64) {
						blockData.set(dx, dy, dz, VanillaBlocks.grass.getId());
					}
					else if (dy < 64 & dy >= 60) {
						blockData.set(dx, dy, dz, VanillaBlocks.dirt.getId());
					}
					else if (dy < 60) {
						blockData.set(dx, dy, dz, VanillaBlocks.stone.getId());
					}
				}
			}
		}
	}
}