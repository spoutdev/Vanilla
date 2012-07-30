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
package org.spout.vanilla.controller.living.creature.passive;

import java.util.Collection;
import java.util.HashMap;

import org.spout.api.entity.Entity;
import org.spout.api.event.player.PlayerInteractEvent.Action;
import org.spout.vanilla.controller.InventoryOwner;
import org.spout.vanilla.controller.VanillaControllerTypes;
import org.spout.vanilla.controller.WindowController;
import org.spout.vanilla.controller.living.Creature;
import org.spout.vanilla.controller.living.creature.Passive;
import org.spout.vanilla.controller.living.player.VanillaPlayer;
import org.spout.vanilla.controller.source.HealthChangeReason;
import org.spout.vanilla.inventory.entity.VillagerInventory;
import org.spout.vanilla.window.Window;
import org.spout.vanilla.window.entity.VillagerWindow;

public class Villager extends Creature implements Passive, WindowController, InventoryOwner {
	protected final VillagerInventory inventory = new VillagerInventory();
	private HashMap<VanillaPlayer, Window> viewers = new HashMap<VanillaPlayer, Window>();

	public Villager() {
		super(VanillaControllerTypes.VILLAGER);
	}

	@Override
	public void onAttached() {
		super.onAttached();
		setMaxHealth(20);
		setHealth(20, HealthChangeReason.SPAWN);
		setDeathAnimation(true);
	}

	@Override
	public void onInteract(Entity entity, Action type) {
		super.onInteract(entity, type);
		if (type == Action.RIGHT_CLICK && entity.getController() instanceof VanillaPlayer) {
			this.open((VanillaPlayer) entity.getController());
		}
	}

	@Override
	public boolean open(VanillaPlayer player) {
		if (!this.viewers.containsKey(player)) {
			Window w = new VillagerWindow(player, this);
			this.addViewer(player, w);
			player.setWindow(w);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean close(VanillaPlayer player) {
		Window w = this.removeViewer(player);
		if (w != null) {
			if (player.getActiveWindow() == w) {
				player.closeWindow();
			}
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void closeAll() {
		for (VanillaPlayer player : this.getViewerArray()) {
			this.close(player);
		}
	}

	@Override
	public Window removeViewer(VanillaPlayer player) {
		return this.viewers.remove(player);
	}

	@Override
	public void addViewer(VanillaPlayer player, Window window) {
		this.viewers.put(player, window);
	}

	@Override
	public Collection<VanillaPlayer> getViewers() {
		return this.viewers.keySet();
	}

	/**
	 * Gets an array of viewers currently viewing this controller
	 * @return an array of player viewers
	 */
	public VanillaPlayer[] getViewerArray() {
		return this.viewers.keySet().toArray(new VanillaPlayer[0]);
	}

	@Override
	public boolean hasViewers() {
		return !this.viewers.isEmpty();
	}

	@Override
	public VillagerInventory getInventory() {
		return this.inventory;
	}
}
