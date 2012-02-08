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
 * the MIT license and the SpoutDev license version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license,
 * including the MIT license.
 */
package org.spout.vanilla.generator.normal.decorator;

import net.royawesome.jlibnoise.module.source.Perlin;
import org.spout.api.geo.cuboid.Chunk;
import org.spout.api.geo.discrete.Point;
import org.spout.vanilla.VanillaMaterials;
import org.spout.vanilla.biome.BiomeDecorator;

import java.util.Random;

/**
 * Decorator that decorates a biome with caves.
 */
public class CaveDecorator extends BiomeDecorator {
	private Perlin p;
	private Random r = new Random();

	public CaveDecorator() {
		p = new Perlin();
		p.setSeed(10);
		p.setOctaveCount(8);
		p.setFrequency(2);
	}

	@Override
	public void decorate(Chunk c) {
		int x = c.getX() * 16;
		int y = c.getY() * 16;
		int z = c.getZ() * 16;
		Point pt = new Point(c.getWorld(), x + r.nextInt(16), y + r.nextInt(16), z + r.nextInt(16));
		for(int dx = x; dx < x+16; dx++) {
			for(int dz = z; dz < z+16; dz++) {
				for(int dy = y; dy < y+16; dy++) {
					if(Math.sqrt(Math.pow(dx-pt.getX(),2) + Math.pow(dy-pt.getY(),2) + Math.pow(dz-pt.getZ(),2)) > 6) continue;
					if(p.GetValue(dx/5.0 + 0.005, dy/5.0 + 0.005, dz/5.0 + 0.005) > 0 && c.getBlockId(dx, dy, dz) == VanillaMaterials.STONE.getId()) c.setBlockId(dx, dy, dz, VanillaMaterials.AIR.getId(),c.getWorld());
				}
			}
		}
	}
}
