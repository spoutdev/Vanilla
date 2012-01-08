/*
 * This file is part of Vanilla (http://www.spout.org/).
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
package org.spout.vanilla.generator;

import java.util.Arrays;
import java.util.List;
import org.getspout.api.generator.Populator;
import org.getspout.api.generator.WorldGenerator;
import org.getspout.api.geo.World;
import org.getspout.api.geo.cuboid.Chunk;

/**
 * Base chunk generator class.
 */
public abstract class BaseGenerator implements WorldGenerator {
	//private static final Set<Integer> noSpawnFloors = new HashSet<Integer>(Arrays.asList(BlockID.FIRE, BlockID.CACTUS, BlockID.LEAVES));
	//private final Map<String, Map<String, OctaveGenerator>> octaveCache = new HashMap<String, Map<String, OctaveGenerator>>();
	private final List<Populator> populators;

	protected BaseGenerator(Populator... args) {
		populators = Arrays.asList(args);
	}

	/**
	 * @param world The world to create OctaveGenerators for
	 * @param octaves The map to put the OctaveGenerators into
	 */
	//protected void createWorldOctaves(World world, Map<String, OctaveGenerator> octaves) {
	//}

	/**
	 * @param world The world to look for in the cache
	 * @return A map of {@link OctaveGenerator}s created by
	 *         {@link #createWorldOctaves(World, Map)}
	 */
	/*protected final Map<String, OctaveGenerator> getWorldOctaves(World world) {
		if (octaveCache.get(world.getName()) == null) {
			Map<String, OctaveGenerator> octaves = new HashMap<String, OctaveGenerator>();
			createWorldOctaves(world, octaves);
			octaveCache.put(world.getName(), octaves);
			return octaves;
		}
		return octaveCache.get(world.getName());
	}*/

	/**
	 * Create a new byte[] buffer of the proper size.
	 *
	 * @param fill The Material to fill with.
	 * @return A new filled byte[16 * 16 * 128];
	 */
	protected byte[] start(World world, int fill) {
		byte[] data = new byte[Chunk.CHUNK_SIZE * Chunk.CHUNK_SIZE * Chunk.CHUNK_SIZE];
		Arrays.fill(data, (byte) fill);
		return data;
	}

	/**
	 * Set the given block to the given type.
	 *
	 * @param data The buffer to write to.
	 * @param x The chunk X coordinate.
	 * @param y The Y coordinate.
	 * @param z The chunk Z coordinate.
	 * @param id The block type.
	 */
	protected void set(byte[] data, World world, int x, int y, int z, int id) {
		if (data == null) {
			throw new IllegalStateException();
		}
		if (x < 0 || y < 0 || z < 0 || x >= Chunk.CHUNK_SIZE || y >= Chunk.CHUNK_SIZE || z >= Chunk.CHUNK_SIZE) {
			return;
		}
		data[(x & 0xF) << 8 | (z & 0xF) << 4 | y & 0xF] = (byte) id;
	}

	/**
	 * Get the given block type.
	 *
	 * @param data The buffer to read from.
	 * @param x The chunk X coordinate.
	 * @param y The Y coordinate.
	 * @param z The chunk Z coordinate.
	 * @return The type of block at the location.
	 */
	protected int get(byte[] data, World world, int x, int y, int z) {
		if (data == null) {
			throw new IllegalStateException();
		}
		if (x < 0 || y < 0 || z < 0 || x >= Chunk.CHUNK_SIZE || y >= Chunk.CHUNK_SIZE || z >= Chunk.CHUNK_SIZE) {
			return 0;
		}
		return data[(x & 0xF) << 8 | (z & 0xF) << 4 | y & 0xF];
	}

	public final List<Populator> getDefaultPopulators() {
		return populators;
	}
}