package org.spout.vanilla.event.entity;

import org.spout.api.entity.Entity;
import org.spout.api.event.HandlerList;
import org.spout.api.event.entity.EntityEvent;
import org.spout.api.exception.InvalidControllerException;
import org.spout.vanilla.component.living.passive.Sheep;

/**
 * Event which is called when a sheep's wool changes.
 */
public class SheepWoolEvent extends EntityEvent {
	private static HandlerList handlers = new HandlerList();
	private final SheepWoolEventType type;
	private int data = 15; // default to white wool
	
	public SheepWoolEvent(Entity e, SheepWoolEventType type) {
		super(e);
		if (!e.has(Sheep.class)) {
			throw new InvalidControllerException();
		}
		this.type = type;
	}
	
	public SheepWoolEvent(Entity e, SheepWoolEventType type, int data) {
		this(e, type);
		this.data = data;
	}
	
	/**
	 * Get the type of change which occurs to the wool.
	 */
	public SheepWoolEventType getType() {
		return type;
	}
	
	/**
	 * Get any additional data associated with this event, ie. the color
	 * for the wool to be dyed.
	 */
	public int getData() {
		return data;
	}

	/**
	 * Set the additional data associated with this event.
	 */
	public void setData(int data) {
		this.data = data;
	}
	
	@Override
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
	
	public enum SheepWoolEventType {
		DYE,
		REGROW;
	}
}
