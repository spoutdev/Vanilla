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
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.block.BlockFaces;

import org.spout.vanilla.configuration.VanillaConfiguration;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.attachable.Attachable;
import org.spout.vanilla.material.block.attachable.GroundAttachable;

public class Cactus extends GroundAttachable {

	public Cactus() {
		super("Cactus", 81);
		
		//TODO: Maybe a proper material state for this?
		//Perhaps 'canBlockCactus' or something...
		this.addAllowedNeighbour(VanillaMaterials.AIR);
		this.addAllowedNeighbour(VanillaMaterials.TORCH);
		this.addAllowedNeighbour(VanillaMaterials.REDSTONE_TORCH_OFF);
		this.addAllowedNeighbour(VanillaMaterials.REDSTONE_TORCH_ON);
		this.addAllowedNeighbour(VanillaMaterials.LEVER);
		this.addAllowedNeighbour(VanillaMaterials.DEAD_BUSH);
		this.addAllowedNeighbour(VanillaMaterials.TALL_GRASS);
		this.addAllowedNeighbour(VanillaMaterials.REDSTONE_WIRE);
	}
	
	private Set<BlockMaterial> allowedNeighbours = new HashSet<BlockMaterial>();

	public void addAllowedNeighbour(BlockMaterial mat) {
		this.allowedNeighbours.add(mat);
	}
	
	@Override
	public void onUpdate(Block block) {
		if (VanillaConfiguration.CACTUS_PHYSICS.getBoolean()) {
			super.onUpdate(block);
		}
	}
	
	@Override
	public boolean canAttachTo(BlockMaterial material, BlockFace face) {
		if (super.canAttachTo( material, face)) {
			return material.equals(VanillaMaterials.SAND) ||  material.equals(VanillaMaterials.CACTUS);
		}
		return false;
	}
	
	@Override
	public boolean canAttachTo(Block block, BlockFace attachedFace) {
		if (super.canAttachTo(block, attachedFace)) {
			//surrounded by air or non-occupying block?
			BlockMaterial mat;
			for (BlockFace face : BlockFaces.NESW) {
				mat = block.translate(face).getMaterial();
				if (!this.allowedNeighbours.contains(mat)) {
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
	@Override
	public <T extends BlockMaterial & Attachable> boolean canSupport(T material, BlockFace face) {
		return face == BlockFace.TOP && material.equals(VanillaMaterials.CACTUS);
	}
}
