/*
 * This file is part of Vanilla (http://www.spout.org/).
 *
 * Vanilla is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Vanilla is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.spout.vanilla.event.player;

import org.spout.api.event.Cancellable;
import org.spout.api.event.HandlerList;
import org.spout.api.event.player.PlayerEvent;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.player.Player;

/**
 * This event is fired when the player is almost about to enter the bed.
 */
public class PlayerBedEvent extends PlayerEvent implements Cancellable {
	private static HandlerList handlers = new HandlerList();

	private Block bed;

	private boolean entered;

	public PlayerBedEvent(Player p) {
		super(p);
	}

	/**
	 * Returns the bed block involved in this event.
	 *
	 * @return the bed block involved in this event
	 */
	public Block getBed() {
		return bed;
	}

	public void setBed(Block bed) {
		this.bed = bed;
	}

	/**
	 * Gets if the player entered the bed.
	 *
	 * @return True if the bed was entered.
	 */
	public boolean isEntered() {
		return entered;
	}

	/**
	 * Gets if the player left the bed.
	 *
	 * @return False if the bed was left.
	 */
	public boolean isLeft() {
		return !entered;
	}

	public void setEntered(boolean entered) {
		this.entered = entered;
	}

	@Override
	public void setCancelled(boolean cancelled) {
		super.setCancelled(cancelled);
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}