package org.getspout.vanilla.generator.normal;

import java.util.Random;

import org.getspout.api.generator.Populator;
import org.getspout.api.generator.WorldGenerator;
import org.getspout.api.geo.World;
import org.getspout.api.geo.cuboid.Block;

public class NormalGenerator implements WorldGenerator {

	public Block[][] generate(World w, Random rng, int x, int z) {
		// TODO Auto-generated method stub
		return null;
	}

	public Populator[] getPopulators() {
		return new Populator[]{new TreePopulator(), new PondPopulator(), new StrongholdPopulator(), new VillagePopulator(), new AbandonedMineshaftPopulator(), new DungeonPopulator()};
	}

}
