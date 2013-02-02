/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, Spout LLC <http://www.spout.org/>
 * Vanilla is licensed under the Spout License Version 1.
 *
 * Vanilla is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the Spout License Version 1.
 *
 * Vanilla is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for
 * more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the Spout License Version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://spout.in/licensev1> for the full license, including
 * the MIT license.
 */
package org.spout.vanilla.plugin.inventory.window.gui;

import java.util.ArrayList;
import java.util.List;

import org.spout.api.Client;
import org.spout.api.Spout;
import org.spout.api.event.player.PlayerKeyEvent;
import org.spout.api.gui.component.ControlComponent;
import org.spout.api.gui.render.RenderPart;
import org.spout.api.input.Keyboard;
import org.spout.api.math.IntVector2;
import org.spout.api.math.Vector2;

import org.spout.vanilla.api.inventory.Slot;
import org.spout.vanilla.api.inventory.window.AbstractWindow;
import org.spout.vanilla.api.inventory.window.ClickArguments;

import org.spout.vanilla.plugin.component.inventory.WindowHolder;

public class InventorySlot extends ControlComponent {
	private RenderItemStack item;
	private final RenderPart hitBox = new RenderPart();
	private Vector2 pos = Vector2.ZERO;
	private Slot entry;

	public void setSlot(Slot entry) {
		this.entry = entry;
	}

	public Slot getSlot() {
		return entry;
	}

	public void setRenderItemStack(RenderItemStack item) {
		this.item = item;
		update();
	}

	public void setPosition(Vector2 pos) {
		this.pos = pos;
		update();
	}

	public Vector2 getPosition() {
		return pos;
	}

	public void update() {
		if (item != null) {
			item.setZIndex(0);
			item.setPosition(pos);
		}
	}

	public AbstractWindow getWindow() {
		if (!(Spout.getEngine() instanceof Client)) {
			throw new IllegalStateException("Cannot handle GUIs on the server.");
		}
		return ((Client) Spout.getEngine()).getActivePlayer().get(WindowHolder.class).getActiveWindow();
	}

	@Override
	public List<RenderPart> getRenderParts() {
		List<RenderPart> parts = new ArrayList<RenderPart>();
		if (item != null && item.getRenderMaterial() != null) {
			parts.add(item);
		}
		return parts;
	}

	@Override
	public void onClicked(IntVector2 position, boolean mouseDown) {
		AbstractWindow window = getWindow();
		window.onClick(new ClickArguments(entry, false, window.isShiftDown()));
	}

	@Override
	public void onKey(PlayerKeyEvent event) {
		Keyboard key = event.getKey();
		getWindow().setShiftDown((key == Keyboard.KEY_LSHIFT || key == Keyboard.KEY_RSHIFT) && event.isPressed());
	}

	@Override
	public void onMouseMove(IntVector2 position) {
	}
}
