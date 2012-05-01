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
 * the MIT license and the SpoutDev License Version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license,
 * including the MIT license.
 */
package org.spout.vanilla.material.block;

import java.util.HashSet;
import java.util.Set;

import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.Material;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.block.BlockFaces;

import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.attachable.GroundAttachable;

public class SugarCane extends GroundAttachable {
	private final Set<Material> validBases = new HashSet<Material>(4);

	public SugarCane() {
		super("Sugar Cane", 83);

		validBases.add(VanillaMaterials.DIRT);
		validBases.add(VanillaMaterials.GRASS);
		validBases.add(VanillaMaterials.SAND);
		validBases.add(VanillaMaterials.SUGAR_CANE_BLOCK);
	}
	
	@Override
	public boolean canAttachTo(BlockMaterial material, BlockFace face) {
		return super.canAttachTo(material, face) && this.validBases.contains(material);
	}
	
	@Override
	public boolean canAttachTo(Block block, BlockFace face) {
		if (super.canAttachTo(block, face)) {
			BlockMaterial wmat;
			for (BlockFace around : BlockFaces.NESW) {
				wmat = block.translate(around).getMaterial();
				if (wmat.equals(VanillaMaterials.STATIONARY_WATER) || wmat.equals(VanillaMaterials.WATER)) {
					return true;
				}
			}
		}
		return false;
	}
}
