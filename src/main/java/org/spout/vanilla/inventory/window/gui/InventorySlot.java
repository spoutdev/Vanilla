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
package org.spout.vanilla.inventory.window.gui;

import java.util.Arrays;
import java.util.List;

import org.spout.api.component.components.WidgetComponent;
import org.spout.api.gui.render.RenderPart;
import org.spout.api.input.KeyEvent;
import org.spout.api.math.IntVector2;
import org.spout.api.math.Vector2;

public class InventorySlot extends WidgetComponent {
	private RenderItemStack item;

	public void setRenderItemStack(RenderItemStack item) {
		this.item = item;
	}

	public void setPosition(Vector2 pos) {
		item.setPosition(pos);
	}

	public Vector2 getPosition() {
		return item.getPosition();
	}

	@Override
	public List<RenderPart> getRenderParts() {
		return Arrays.<RenderPart>asList(item);
	}

	@Override
	public void onClicked(IntVector2 position, boolean mouseDown) {
	}

	@Override
	public void onKey(KeyEvent event) {
	}

	@Override
	public void onMouseMove(IntVector2 position) {
	}
}
