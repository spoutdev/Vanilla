package org.spout.vanilla.material.item.vehicle;

import org.spout.api.entity.Entity;
import org.spout.api.event.player.Action;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.block.BlockFace;
import org.spout.api.math.Vector3;
import org.spout.vanilla.component.entity.substance.vehicle.minecart.MinecartBase;
import org.spout.vanilla.material.block.rail.RailBase;
import org.spout.vanilla.material.item.EntitySpawnItem;

public class MinecartItem<T extends MinecartBase> extends EntitySpawnItem<T> {

	public MinecartItem(String name, int id, Class<? extends T> spawnedComponent) {
		super(name, id, null);
		this.setSpawnedComponent(spawnedComponent);
	}

	@Override
	public void onInteract(Entity entity, Block block, Action type, BlockFace clickedface) {
		super.onInteract(entity, block, type, clickedface);
		if (type == Action.RIGHT_CLICK && block.getMaterial() instanceof RailBase) {
			// Spawn the Minecart on the rail
			//TODO: Spawn position adjustment based on rail type
			this.spawnEntity(block, Vector3.ZERO);
			this.handleSelectionRemove(entity);
		}
	}
}
