package org.spout.vanilla.components.substance;

import org.spout.api.component.components.BlockComponent;
import org.spout.api.material.block.BlockFace;

import org.spout.vanilla.VanillaPlugin;
import org.spout.vanilla.material.item.misc.PaintingItem;
import org.spout.vanilla.protocol.entity.object.PaintingEntityProtocol;

public class Painting extends BlockComponent {
	@Override
	public void onAttached() {
		getHolder().getNetwork().setEntityProtocol(VanillaPlugin.VANILLA_PROTOCOL_ID, new PaintingEntityProtocol());
	}

	//TODO Implement paintings.
	public PaintingItem.PaintingStyle getStyle() {
		return PaintingItem.PaintingStyle.Alban;
	}

	public BlockFace getFace() {
		return BlockFace.BOTTOM;
	}

	public int getProtocolFace() {
		return 1;
	}
}
