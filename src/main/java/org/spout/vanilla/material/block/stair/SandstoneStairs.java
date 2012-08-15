package org.spout.vanilla.material.block.stair;

import org.spout.vanilla.data.drops.flag.ToolTypeFlags;
import org.spout.vanilla.material.block.Stairs;

public class SandstoneStairs extends Stairs {

	public SandstoneStairs(String name, int id) {
		super(name, id);
		this.setHardness(2.0F).setResistance(10.0F);
		this.getDrops().NOT_CREATIVE.addFlags(ToolTypeFlags.PICKAXE);
	}
}
