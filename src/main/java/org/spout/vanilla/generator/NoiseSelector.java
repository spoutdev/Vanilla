package org.spout.vanilla.generator;

import net.royawesome.jlibnoise.module.modifier.Turbulence;
import net.royawesome.jlibnoise.module.source.Voronoi;

import org.spout.api.generator.biome.BiomeSelector;

/**
 * @author zml2008
 */
public class NoiseSelector extends BiomeSelector {
	private Turbulence noise = new Turbulence();
	private Voronoi base = new Voronoi();
	
	public NoiseSelector() {
		base.setFrequency(Math.PI);
		noise.SetSourceModule(0, base);
		noise.setRoughness(4);
		noise.setFrequency(0.8);
		noise.setPower(1.3);
	}
	
	@Override
	public int pickBiome(int x, int y, int z, int maxBiomes, long seed) {
		base.setSeed((int) seed);
		return Math.abs((int)(noise.GetValue(x / 256.0 + 0.05, y + 0.05, z / 256.0 + 0.05) * 32 % maxBiomes));
	}
}
