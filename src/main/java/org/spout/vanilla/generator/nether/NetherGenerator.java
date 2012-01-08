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
package org.spout.vanilla.generator.nether;

import org.getspout.api.generator.Populator;
import org.getspout.api.generator.WorldGenerator;
import org.getspout.api.util.cuboid.CuboidShortBuffer;

public class NetherGenerator implements WorldGenerator {
	public void generate(CuboidShortBuffer blockData, int chunkX, int chunkY, int chunkZ) {
	}
	
	private final Populator[] populators = new Populator[] {new FortressPopulator()};

	public Populator[] getPopulators() {
		return populators;
	}
}