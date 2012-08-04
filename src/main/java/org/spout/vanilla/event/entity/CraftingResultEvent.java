package org.spout.vanilla.event.entity;

import org.spout.api.entity.component.Controller;
import org.spout.api.event.Cancellable;
import org.spout.api.event.HandlerList;
import org.spout.api.event.entity.EntityEvent;
import org.spout.api.inventory.ItemStack;

/**
 * Event which is called when a furnace finishes smelting an object, or a CraftingTable finishes crafting, or a player finishes crafting
 * todo implement event in CraftingInventory
 */
public class CraftingResultEvent extends EntityEvent implements Cancellable{

	private static HandlerList handlers = new HandlerList();
	private Controller controller;
	private ItemStack result;


	public CraftingResultEvent (Controller controller, ItemStack result){
		super(controller.getParent());
		this.controller = controller;
		this.result = result;
	}

	/**
	 * Returns the controller of this CraftingResultEvent
	 * @return controller
	 */

	public Controller getController(){
		return controller;
	}

	/**
	 * Returns the result of this CraftingResultEvent
	 * @return result
	 */

	public ItemStack getResult(){
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
