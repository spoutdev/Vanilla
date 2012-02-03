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

import org.spout.api.io.store.map.MemoryStoreMap;
import org.spout.api.io.store.map.SimpleStoreMap;

public class BiomeMap {
	SimpleStoreMap<Integer, BiomeType> map;
	
	public BiomeMap(){
		//Todo: Make this saveable
		map = new MemoryStoreMap<Integer, BiomeType>();
		
			
		
	}
	
	public void addBiome(BiomeType biome){
		map.set(map.getSize(), biome);
	}
	
	/**
	 * 
	 * 
	 */
	public BiomeType getBiome(int x, int z){
		//TODO This needs to generate a noise function relying on x and z to generate a map that is [0-map.getSize()] so that we can select
		//Biomes for the biome generator
		int biomeX = (x/16) % map.getSize();
		int biomeZ = (z/16) % map.getSize();
		
		int biome = (biomeX ^ biomeZ) % map.getSize();
		
		
		return map.get(biome);
	}
	
}
