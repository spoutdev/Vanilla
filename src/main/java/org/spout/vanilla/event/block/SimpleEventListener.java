package org.spout.vanilla.event.block;

import org.spout.api.event.Listener;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.block.BlockSnapshot;

import org.spout.vanilla.event.cause.BlockGrowCause;

/**
 * Example class
 */
public class SimpleEventListener implements Listener {

	public void onBlockGrowEvent(VanillaBlockChangeEvent event) {
		switch (event.getBlockChangeEventType()) {
			case GROW:
				BlockGrowCause blockGrowCause = event.getBlockGrowCause();
				if (blockGrowCause.getNewState().getMaterial() == BlockMaterial.ERROR) {
					event.setCancelled(true);
				}
				blockGrowCause.setNewState(new BlockSnapshot(blockGrowCause.getSource()));
				break;
			default:
				return;
		}
	}

}
