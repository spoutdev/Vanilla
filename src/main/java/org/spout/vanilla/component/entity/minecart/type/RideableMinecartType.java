package org.spout.vanilla.component.entity.minecart.type;

import org.spout.api.entity.Player;
import org.spout.api.event.entity.EntityInteractEvent;
import org.spout.api.event.player.PlayerInteractEntityEvent;
import org.spout.api.material.BlockMaterial;
import org.spout.vanilla.component.entity.minecart.MinecartType;
import org.spout.vanilla.material.VanillaMaterials;

public class RideableMinecartType extends MinecartType {

	@Override
	public void onInteract(final EntityInteractEvent<?> event) {
		if (event instanceof PlayerInteractEntityEvent) {
			final PlayerInteractEntityEvent pie = (PlayerInteractEntityEvent) event;
			final Player player = (Player) pie.getEntity();
			switch (pie.getAction()) {
				case RIGHT_CLICK:
					//TODO: Make player enter Minecart here
					System.out.println("MINECART ENTER: " + player.getName());
			}
		}
	}

	@Override
	public BlockMaterial getDefaultDisplayedBlock() {
		return VanillaMaterials.AIR;
	}
}
