package org.spout.vanilla.component.entity.minecart;

import org.spout.api.material.BlockMaterial;
import org.spout.vanilla.component.entity.VanillaEntityComponent;

public abstract class MinecartType extends VanillaEntityComponent {

	/**
	 * Gets the default Block Material to display in the Minecart
	 * 
	 * @return default Block Material to display
	 */
	public abstract BlockMaterial getDefaultDisplayedBlock();

	/**
	 * Called when the owner has been destroyed
	 */
	public void onDestroy() {
	}

	/**
	 * Called when the owner has been activated by activator rails
	 */
	public void onActivate() {
	}
}
