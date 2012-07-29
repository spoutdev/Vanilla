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
package org.spout.vanilla.controller.block;

import java.util.Collection;
import java.util.HashMap;

import org.spout.api.material.BlockMaterial;

import org.spout.vanilla.controller.VanillaBlockController;
import org.spout.vanilla.controller.VanillaControllerType;
import org.spout.vanilla.controller.WindowController;
import org.spout.vanilla.controller.living.player.VanillaPlayer;
import org.spout.vanilla.window.Window;

public abstract class VanillaWindowBlockController extends VanillaBlockController implements WindowController {
	private HashMap<VanillaPlayer, Window> viewers = new HashMap<VanillaPlayer, Window>();

	protected VanillaWindowBlockController(VanillaControllerType type, BlockMaterial blockMaterial) {
		super(type, blockMaterial);
	}

	public abstract Window createWindow(VanillaPlayer player);

	@Override
	public boolean open(VanillaPlayer player) {
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
		try {
			return this.viewers.remove(player);
		} finally {
			onViewersChanged();
		}
	}

	@Override
	public void addViewer(VanillaPlayer player, Window window) {
		this.viewers.put(player, window);
		this.onViewersChanged();
	}

	/**
	 * Is called when a viewer got removed or added
	 */
	public void onViewersChanged() {
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
}
