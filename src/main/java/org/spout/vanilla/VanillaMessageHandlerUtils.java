package org.spout.vanilla;

import org.spout.api.material.block.BlockFace;

public class VanillaMessageHandlerUtils {
	public static BlockFace messageToBlockFace(int messageFace) {
		switch(messageFace) {
			case 0:
				return BlockFace.BOTTOM;
			case 1:
				return BlockFace.TOP;
			case 2:
				return BlockFace.EAST;
			case 3:
				return BlockFace.WEST;
			case 4:
				return BlockFace.NORTH;
			case 5:
				return BlockFace.SOUTH;
			default:
				return BlockFace.THIS;
		}
	}
}
