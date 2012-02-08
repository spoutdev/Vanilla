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
package org.spout.vanilla.biome;

import org.spout.api.geo.cuboid.Chunk;
import org.spout.api.util.cuboid.CuboidShortBuffer;

import java.util.ArrayList;

public abstract class BiomeType {
	ArrayList<BiomeDecorator> decorators = new ArrayList<BiomeDecorator>();
	
	public BiomeType(){
		this.registerDecorators();
	}
	
	
	public void register(BiomeDecorator decorator){
		decorators.add(decorator);
	}
	
	/**
	 * Called during the Biome's construction.
	 * Registers all decorators to be called during the populate stage of world generation
	 */
	public abstract void registerDecorators();
	
	
	public abstract void generateTerrain(CuboidShortBuffer blockData, int chunkX, int chunkY, int chunkZ);
	
	public final void decorate(Chunk c){
		for(BiomeDecorator b : decorators){
			b.decorate(c);
		}
	}
}
