/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, VanillaDev <http://www.spout.org/>
 * Vanilla is licensed under the SpoutDev License Version 1.
 *
 * Vanilla is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the SpoutDev License Version 1.
 *
 * Vanilla is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the SpoutDev License Version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license,
 * including the MIT license.
 */
package org.spout.vanilla.components.living;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

import org.spout.api.Source;
import org.spout.api.entity.Player;
import org.spout.api.event.player.PlayerInteractEvent.Action;
import org.spout.api.inventory.InventoryBase;
import org.spout.vanilla.VanillaPlugin;
import org.spout.vanilla.components.misc.WindowComponent;
import org.spout.vanilla.components.misc.WindowOwner;
import org.spout.vanilla.inventory.CraftingInventory;
import org.spout.vanilla.inventory.InventoryOwner;
import org.spout.vanilla.protocol.entity.BasicMobEntityProtocol;

/**
 * A component that identifies the entity as a Villager.
 */
public class Villager extends VanillaEntity implements WindowOwner, InventoryOwner {
	@Override
	public void onAttached() {
		super.onAttached();
		getHolder().getNetwork().setEntityProtocol(VanillaPlugin.VANILLA_PROTOCOL_ID, new BasicMobEntityProtocol(120)); //Index 16 (int): Unknown, example: 0
	}

	private HashMap<Player, WindowComponent> viewers = new HashMap<Player, WindowComponent>();

	@Override
	public void onInteract(Action action, Source source) {
		super.onInteract(action, source);
		if (action == Action.RIGHT_CLICK) {
			this.open((Player) source);
		}
	}

	@Override
	public boolean open(Player player) {
		if (!this.viewers.containsKey(player)) {
			WindowComponent w = this.createWindow(player);
			this.addViewer(player, w);
			return true;
		} else {
			return false;
		}
	}

	private WindowComponent createWindow(Player player) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean close(Player player) {
		WindowComponent w = this.removeViewer(player);
		if (w != null) {
			player.detach(WindowComponent.class);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void closeAll() {
		for (Player player : this.getViewerArray()) {
			this.close(player);
		}
	}

	@Override
	public WindowComponent removeViewer(Player player) {
		try {
			return this.viewers.remove(player);
		} finally {
			onViewersChanged();
		}
	}

	@Override
	public void addViewer(Player player, WindowComponent window) {
		this.viewers.put(player, window);
		this.onViewersChanged();
	}

	/**
	 * Is called when a viewer got removed or added
	 */
	@Override
	public void onViewersChanged() {
	}

	@Override
	public Collection<Player> getViewers() {
		return Collections.unmodifiableSet(this.viewers.keySet());
	}

	/**
	 * Gets an array of viewers currently viewing this entity
	 * @return an array of player viewers
	 */
	@Override
	public Player[] getViewerArray() {
		return this.viewers.keySet().toArray(new Player[0]);
	}

	@Override
	public boolean hasViewers() {
		return !this.viewers.isEmpty();
	}

	@Override
	public CraftingInventory getInventory() {
		// TODO Auto-generated method stub
		return null;
	}
}
