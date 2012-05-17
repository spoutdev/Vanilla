package org.spout.vanilla.material.block.solid;

import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.block.BlockFaces;
import org.spout.vanilla.material.block.Directional;
import org.spout.vanilla.material.block.Solid;
import org.spout.vanilla.util.VanillaPlayerUtil;

public class Pumpkin extends Solid implements Directional {
	private final boolean lantern;

	public Pumpkin(String name, int id, boolean lantern) {
		super(name, id);
		this.lantern = lantern;
	}

	@Override
	public void initialize() {
		super.initialize();
		this.setHardness(1.0F).setResistance(1.7F);
		if (lantern) {
			this.setLightLevel(15);
		}
	}

	@Override
	public BlockFace getFacing(Block block) {
		return BlockFaces.EWNS.get(block.getData() - 2);
	}

	@Override
	public void setFacing(Block block, BlockFace facing) {
		block.setData((short) (BlockFaces.EWNS.indexOf(facing, 0) + 2));
	}

	@Override
	public boolean onPlacement(Block block, short data, BlockFace against, boolean isClickedBlock) {
		if (super.onPlacement(block, data, against, isClickedBlock)) {
			this.setFacing(block, VanillaPlayerUtil.getFacing(block.getSource()).getOpposite());
			return true;
		}

		return false;
	}

	/**
	 * Whether this pumpkin block material is a jack o' lantern
	 * 
	 * @return true if jack o' lantern
	 */
	public boolean isLantern() {
		return lantern;
	}
}
