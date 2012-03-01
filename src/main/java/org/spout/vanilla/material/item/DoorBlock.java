package org.spout.vanilla.material.item;

import org.spout.vanilla.material.attachable.GroundAttachable;

public class DoorBlock extends GroundAttachable {

	private boolean openByHand;
	
	public static final short HINGE_NORTH_WEST = 0x0;
	public static final short HINGE_NORTH_EAST = 0x1;
	public static final short HINGE_SOUTH_EAST = 0x2;
	public static final short HINGE_SOUTH_WEST = 0x3;

	public DoorBlock(String name, int id, boolean openByHand) {
		super(name, id);
		this.openByHand = openByHand;
	}

}
