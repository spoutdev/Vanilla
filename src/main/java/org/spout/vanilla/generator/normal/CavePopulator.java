package org.spout.vanilla.generator.normal;

import java.util.Random;

import net.royawesome.jlibnoise.module.source.Perlin;

import org.spout.api.generator.Populator;
import org.spout.api.geo.cuboid.Chunk;
import org.spout.api.geo.discrete.Point;
import org.spout.vanilla.VanillaBlocks;

public class CavePopulator implements Populator {
	private Perlin p;
	private Random r = new Random();
	
	public CavePopulator() {
		p = new Perlin();
		p.setSeed(10);
		p.setOctaveCount(8);
		p.setFrequency(2);
	}
	
	@Override
	public void populate(Chunk c) {
		int x = c.getX() * 16;
		int y = c.getY() * 16;
		int z = c.getZ() * 16;
		Point pt = new Point(c.getWorld(), x + r.nextInt(16), y + r.nextInt(16), z + r.nextInt(16));
		for(int dx = x; dx < x+16; dx++) {
			for(int dz = z; dz < z+16; dz++) {
				for(int dy = y; dy < y+16; dy++) {
					if(Math.sqrt(Math.pow(dx-pt.getX(),2) + Math.pow(dy-pt.getY(),2) + Math.pow(dz-pt.getZ(),2)) > 6) continue;
					if(p.GetValue(dx/5.0 + 0.005, dy/5.0 + 0.005, dz/5.0 + 0.005) > 0 && c.getBlockId(dx, dy, dz) == VanillaBlocks.STONE.getId()) c.setBlockId(dx, dy, dz, VanillaBlocks.AIR.getId(),c.getWorld());
				}
			}
		}
	}

}
