package org.spout.vanilla.block;

import org.spout.api.material.GenericBlockMaterial;
import org.spout.api.material.block.BlockFace;

public class TorchAttachable extends GenericBlockMaterial implements Attachable {
	public TorchAttachable(String name, int id) {
		super(name, id);
	}

	@Override
	public short getDataForFace(BlockFace face) {
		switch(face) {
		case EAST:
			return 0x2;
		case WEST:
			return 0x1;
		case SOUTH:
			return 0x4;
		case NORTH:
			return 0x3;
		default:
			return 0x5; //Standing on floor, this will be default if other faces are passed, too
		}
	}

	@Override
	public BlockFace getAttachedTo(short data) {
		switch(data) {
		case 0x2:
			return BlockFace.EAST;
		case 0x1:
			return BlockFace.WEST;
		case 0x4:
			return BlockFace.SOUTH;
		case 0x3:
			return BlockFace.NORTH;
		case 0x5:
			return BlockFace.BOTTOM;
		default:
			return BlockFace.BOTTOM;
		}
	}

}
