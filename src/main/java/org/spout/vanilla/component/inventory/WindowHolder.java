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
package org.spout.vanilla.component.inventory;

import java.util.logging.Level;

import org.spout.api.Spout;
import org.spout.api.component.components.EntityComponent;
import org.spout.api.entity.Player;

import org.spout.vanilla.inventory.window.DefaultWindow;
import org.spout.vanilla.inventory.window.Window;

public class WindowHolder extends EntityComponent {
	private DefaultWindow defaultWindow;
	private Window activeWindow;

	public Window getActiveWindow() {
		return activeWindow;
	}

	public DefaultWindow getDefaultWindow() {
		return defaultWindow;
	}

	public void open(Window window) {
		close();
		debug("Opening " + window.getClass().getCanonicalName());
		window.open();
		this.activeWindow = window;
	}

	public void close() {
		if (activeWindow instanceof DefaultWindow) {
			return;
		}
		debug("Closing " + activeWindow.getClass().getCanonicalName());
		activeWindow.close();
		activeWindow = defaultWindow;
	}

	private void debug(String msg) {
		if (Spout.debugMode()) {
			Spout.getLogger().log(Level.INFO, msg);
		}
	}

	@Override
	public void onAttached() {
		if (!(getOwner() instanceof Player)) {
			throw new IllegalStateException("A Window may only be attached to a player.");
		}
		defaultWindow = new DefaultWindow((Player) getOwner());
		this.activeWindow = defaultWindow;
	}

	@Override
	public void onDetached() {
		activeWindow.close();
	}

	@Override
	public boolean canTick() {
		return activeWindow.canTick();
	}

	@Override
	public void onTick(float dt) {
		activeWindow.onTick(dt);
	}
}
