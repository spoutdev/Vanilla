package org.spout.vanilla.material.generic;

import org.spout.api.entity.Entity;
import org.spout.api.event.player.PlayerInteractEvent.Action;
import org.spout.api.geo.discrete.Point;
import org.spout.api.inventory.ItemStack;
import org.spout.api.material.BlockMaterial;
import org.spout.api.math.MathHelper;
import org.spout.vanilla.entity.living.player.SurvivalPlayer;
import org.spout.vanilla.material.BlockItem;

public class GenericBlockItem extends GenericItem implements BlockItem{

	BlockMaterial onPlace = null;

	public GenericBlockItem(String name, int id, BlockMaterial onPlace) {
		super(name, id);

		if (onPlace == null) {
			throw new NullPointerException("Block material can not be null");
		}

		this.onPlace = onPlace;
	}

	public GenericBlockItem(String name, int id, int data, BlockMaterial onPlace) {
		super(name, id, data);

		if (onPlace == null) {
			throw new NullPointerException("Block material can not be null");
		}

		this.onPlace = onPlace;
	}

	public GenericBlockItem(String name, int id, int data, boolean subtypes, BlockMaterial onPlace) {
		super(name, id, data, subtypes);

		if (onPlace == null) {
			throw new NullPointerException("Block material can not be null");
		}

		this.onPlace = onPlace;
	}

	@Override
	public void onInteract(Entity entity, Point position, Action type) {
		if (type != Action.RIGHT_CLICK){
			return;
		}

		ItemStack holding = entity.getInventory().getCurrentItem();
		if (holding == null || holding.getMaterial() != this) {
			throw new IllegalStateException("Interaction with an entity that is not holding this material!");
		}
		if (entity.getController() instanceof SurvivalPlayer) {
			if (holding.getAmount() > 1) {
				holding.setAmount(holding.getAmount() - 1);
			}
			else if (holding.getAmount() == 1){
				entity.getInventory().setItem(null, entity.getInventory().getCurrentSlot());
			}
			else {
				throw new IllegalStateException("Entity is holding zero or negative sized item!");
			}
		}
		int x = MathHelper.floor(position.getX());
		int y = MathHelper.floor(position.getY());
		int z = MathHelper.floor(position.getZ());
		System.out.println("Placing Block " + getBlock() + " on Interact at " + position);
		position.getWorld().setBlockIdAndData(x, y, z, getBlock().getId(), getBlock().getData(), entity);
	}

	@Override
	public BlockMaterial getBlock() {
		return onPlace;
	}
}
