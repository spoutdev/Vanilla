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
package org.spout.vanilla.material.generic;

import org.spout.api.geo.World;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.GenericBlockMaterial;
import org.spout.api.material.Material;
import org.spout.api.material.block.BlockFace;
import org.spout.api.math.Vector3;
import org.spout.vanilla.VanillaMaterials;
import org.spout.vanilla.material.Block;
import org.spout.vanilla.material.block.data.MaterialData;
import org.spout.vanilla.material.block.data.SimpleMaterialData;
import org.spout.vanilla.material.item.RedstoneTorch;
import org.spout.vanilla.material.item.RedstoneWire;

public class GenericBlock extends GenericBlockMaterial implements Block {

	private static BlockFace indirectSourcesWire[] = {BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST, BlockFace.NORTH};
	private float resistance;
	private Material dropMaterial;
	private int dropCount;

	public GenericBlock(String name, int id) {
		super(name, id);

		dropMaterial = this;
		dropCount = 1;
	}

	public GenericBlock(String name, int id, int data) {
		super(name, id, data);

		dropMaterial = this;
		dropCount = 1;
	}

	@Override
	public short getIndirectRedstonePower(World world, int x, int y, int z) {
		short indirect = 0;
		for (BlockFace face : indirectSourcesWire) {
			Vector3 offset = face.getOffset();
			int tx = (int) (x + offset.getX()), ty = (int) (y + offset.getY()), tz = (int) (z + offset.getZ());
			BlockMaterial material = world.getBlockMaterial(tx, ty, tz);
			if (material instanceof RedstoneWire) {
				indirect = (short) Math.max(indirect, world.getBlockData(tx, ty, tz));
			}
		}

		BlockMaterial material = world.getBlockMaterial(x, y - 1, z); //Check for redstone torch below
		if (material instanceof RedstoneTorch) {
			RedstoneTorch torch = (RedstoneTorch) material;
			indirect = (short) Math.max(indirect, torch.getRedstonePower(world, x, y - 1, z, x, y, z));
		}
		return (short) Math.max(indirect, getDirectRedstonePower(world, x, y, z));
	}

	@Override
	public short getDirectRedstonePower(World world, int x, int y, int z) {
		// TODO Waiting for repeaters
		return 0;
	}

	public GenericBlock setResistance(Float newResistance) {
		resistance = newResistance;
		return this;
	}

	public float getResistance() {
		return resistance;
	}

	@Override
	public GenericBlock setFriction(float friction) {
		return (GenericBlock) super.setFriction(friction);
	}

	@Override
	public GenericBlock setHardness(float hardness) {
		return (GenericBlock) super.setHardness(hardness);
	}

	public GenericBlock setLightLevel(int level) {
		return setLightLevel((byte) level);
	}

	@Override
	public GenericBlock setLightLevel(byte level) {
		return (GenericBlock) super.setLightLevel(level);
	}
	
	@Override
	public Material getDrop() {
		return dropMaterial;

	}
	

	@Override
	public int getDropCount() {
		return dropCount;
	}
	

	public GenericBlock setDrop(Material id) {
		dropMaterial = id;
		return this;
	}
	
	public GenericBlock setDropCount(int count) {
		dropCount = count;
		
		return this;
	}
	
	public MaterialData createData(short data) {
		return new SimpleMaterialData(this, data);
	}
		
}
