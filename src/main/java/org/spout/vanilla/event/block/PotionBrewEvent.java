package org.spout.vanilla.event.block;

import org.spout.api.event.Cancellable;
import org.spout.api.event.Cause;
import org.spout.api.event.HandlerList;
import org.spout.api.event.block.BlockEvent;
import org.spout.api.inventory.ItemStack;
import org.spout.vanilla.component.block.material.BrewingStand;

/**
 * @author thehutch
 */
public class PotionBrewEvent extends BlockEvent implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	private BrewingStand brewingStand;
	private ItemStack source;
	private ItemStack result;

	public PotionBrewEvent(BrewingStand brewingStand, Cause<?> reason, ItemStack source, ItemStack result) {
		super(brewingStand.getBlock(), reason);
		this.brewingStand = brewingStand;
		this.source = source;
		this.result = result;
	}

	public BrewingStand getBrewingStand() {
		return brewingStand;
	}

	public ItemStack getSource() {
		return source;
	}

	public ItemStack getResult() {
		return result;
	}

	@Override
	public void setCancelled(boolean cancelled) {
		super.setCancelled(cancelled);
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
}
