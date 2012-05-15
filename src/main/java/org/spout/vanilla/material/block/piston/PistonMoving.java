package org.spout.vanilla.material.block.piston;

import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.block.BlockFace;
import org.spout.vanilla.controller.VanillaControllerTypes;
import org.spout.vanilla.controller.block.MovingPistonController;
import org.spout.vanilla.material.VanillaBlockMaterial;
import org.spout.vanilla.util.MoveReaction;

/**
 * A temporary block type with controller which animates the extension of the piston
 */
public class PistonMoving extends VanillaBlockMaterial {

	public PistonMoving(String name, int id) {
		super(name, id);
	}
	
	@Override
	public void loadProperties() {
		super.loadProperties();
		this.setResistance(0.0F).setDrop(null);
		this.setController(VanillaControllerTypes.PISTON_MOVING);
	}

	@Override
	public MoveReaction getMoveReaction(Block block) {
		return MoveReaction.DENY;
	}

	@Override
	public boolean canPlace(Block block, short data, BlockFace facing, boolean isClickedBlock) {
		return false;
	}

	@Override
	public MovingPistonController getController(Block block) {
		return (MovingPistonController) super.getController(block);
	}

	public void create(Block block, short data) {
		block.setMaterial(this, data);
		this.getController(block);
	}
}
