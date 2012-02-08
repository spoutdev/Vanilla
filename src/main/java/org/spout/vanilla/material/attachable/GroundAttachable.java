package org.spout.vanilla.material.attachable;

import org.spout.api.material.block.BlockFace;

public class GroundAttachable extends AbstractAttachable {

	public GroundAttachable(String name, int id) {
		super(name, id);
	}

	public GroundAttachable(String name, int id, int data) {
		super(name, id, data);
	}

	@Override
	public short getDataForFace(BlockFace face) {
		return getData(); //data will be either 0 or used for other stuff.
	}

	@Override
	public BlockFace getFaceAttachedTo(short data) {
		return BlockFace.BOTTOM;
	}

}
