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
package org.spout.vanilla.material;

import org.spout.api.geo.World;
import org.spout.api.material.BlockMaterial;

public interface VanillaBlockMaterial extends BlockMaterial {
	public boolean isLiquid();
	
	/**
	 * Represents power that comes into the block from a redstone wire or a torch that is below the block
	 * Indirect power from below powers redstone wire, but level indirect power just inverts adjacent redstone torches.
	 * @return the indirect redstone power.
	 */
	public short getIndirectRedstonePower(World world, int x, int y, int z);
	
	/**
	 * Represents power that comes from a repeater that points to this block.
	 * This power can be used by all neighbors that are redstone targets, even if they wouldn't attach.
	 * @return the direct redstone power.
	 */
	public short getDirectRedstonePower(World world, int x, int y, int z);
}
