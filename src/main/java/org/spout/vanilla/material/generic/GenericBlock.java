package org.spout.vanilla.material.generic;

import org.spout.api.geo.World;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.GenericBlockMaterial;
import org.spout.api.material.block.BlockFace;
import org.spout.api.math.Vector3;
import org.spout.vanilla.material.Block;
import org.spout.vanilla.material.item.RedstoneTorch;
import org.spout.vanilla.material.item.RedstoneWire;

public class GenericBlock extends GenericBlockMaterial implements Block {

	private static BlockFace indirectSourcesWire[] = {BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST, BlockFace.NORTH};

	private float resistance;
	
	public GenericBlock(String name, int id) {
		super(name, id);
	}

	public GenericBlock(String name, int id, int data) {
		super(name, id, data);
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

	@Override
	public GenericBlock setLightLevel(int level) {
		return (GenericBlock) super.setLightLevel(level);
	}

	@Override
	public GenericBlock setOpaque(boolean opaque) {
		return (GenericBlock) super.setOpaque(opaque);
	}

}
