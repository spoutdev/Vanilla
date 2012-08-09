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
package org.spout.vanilla.entity.block;

import java.util.Collection;
import java.util.HashMap;

import org.spout.api.entity.Entity;
import org.spout.api.event.player.PlayerInteractEvent.Action;
import org.spout.api.material.BlockMaterial;

import org.spout.vanilla.entity.VanillaBlockController;
import org.spout.vanilla.entity.VanillaControllerType;
import org.spout.vanilla.entity.WindowController;
import org.spout.vanilla.entity.VanillaPlayerController;
import org.spout.vanilla.window.Window;

public abstract class VanillaWindowBlockController extends VanillaBlockController implements WindowController {
	private HashMap<VanillaPlayerController, Window> viewers = new HashMap<VanillaPlayerController, Window>();

	protected VanillaWindowBlockController(VanillaControllerType type, BlockMaterial blockMaterial) {
		super(type, blockMaterial);
	}

	public abstract Window createWindow(VanillaPlayerController player);

	@Override
	public void onInteract(Entity entity, Action type) {
		super.onInteract(entity, type);
		if (type == Action.RIGHT_CLICK && entity.getController() instanceof VanillaPlayerController) {
			this.open((VanillaPlayerController) entity.getController());
		}
	}

	@Override
	public boolean open(VanillaPlayerController player) {
		if (!this.viewers.containsKey(player)) {
			Window w = this.createWindow(player);
			this.addViewer(player, w);
			player.setWindow(w);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean close(VanillaPlayerController player) {
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
		for (VanillaPlayerController player : this.getViewerArray()) {
			this.close(player);
		}
	}

	@Override
	public Window removeViewer(VanillaPlayerController player) {
		try {
			return this.viewers.remove(player);
		} finally {
			onViewersChanged();
		}
	}

	@Override
	public void addViewer(VanillaPlayerController player, Window window) {
		this.viewers.put(player, window);
		this.onViewersChanged();
	}

	/**
	 * Is called when a viewer got removed or added
	 */
	public void onViewersChanged() {
	}

	@Override
	public Collection<VanillaPlayerController> getViewers() {
		return this.viewers.keySet();
	}

	/**
	 * Gets an array of viewers currently viewing this entity
	 * @return an array of player viewers
	 */
	public VanillaPlayerController[] getViewerArray() {
		return this.viewers.keySet().toArray(new VanillaPlayerController[0]);
	}

	@Override
	public boolean hasViewers() {
		return !this.viewers.isEmpty();
	}
}
