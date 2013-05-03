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
package org.spout.vanilla.inventory.window.gui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.spout.api.Client;
import org.spout.api.event.player.input.PlayerClickEvent;
import org.spout.api.event.player.input.PlayerKeyEvent;
import org.spout.api.gui.component.ControlComponent;
import org.spout.api.gui.render.RenderPart;
import org.spout.api.gui.render.RenderPartPack;
import org.spout.api.inventory.ItemStack;
import org.spout.api.inventory.Slot;
import org.spout.api.math.IntVector2;
import org.spout.api.math.Rectangle;
import org.spout.api.math.Vector2;
import org.spout.api.render.SpoutRenderMaterials;

import org.spout.vanilla.VanillaPlugin;
import org.spout.vanilla.component.entity.inventory.WindowHolder;
import org.spout.vanilla.data.VanillaRenderMaterials;
import org.spout.vanilla.inventory.window.AbstractWindow;

public class RenderSlot extends ControlComponent {
	private Vector2 pos = Vector2.ZERO;
	private Slot slot;
	private boolean hovered;

	public void setSlot(Slot slot) {
		this.slot = slot;
	}

	public Slot getSlot() {
		return slot;
	}

	public void setPosition(Vector2 pos) {
		this.pos = pos;
		getOwner().setBounds(new Rectangle(pos, RenderItemStack.SPRITE_EXTENTS));
	}

	public Vector2 getPosition() {
		return pos;
	}

	public AbstractWindow getWindow() {
		if (!(VanillaPlugin.getInstance().getEngine() instanceof Client)) {
			throw new IllegalStateException("Cannot handle GUIs on the server.");
		}
		return ((Client) VanillaPlugin.getInstance().getEngine()).getActivePlayer().get(WindowHolder.class).getActiveWindow();
	}

	@Override
	public List<RenderPartPack> getRenderPartPacks() {
		List<RenderPartPack> parts = new ArrayList<RenderPartPack>();
		System.out.println("Getting render parts");
		if (hovered) {
			System.out.println("Hovering");
			RenderPartPack boxPack = new RenderPartPack(SpoutRenderMaterials.GUI_COLOR);
			RenderPart box = new RenderPart();
			box.setZIndex(1);
			box.setColor(Color.RED);
			box.setSprite(new Rectangle(pos, RenderItemStack.SPRITE_EXTENTS));
			boxPack.add(box);
			parts.add(boxPack);
		}
		ItemStack item = slot.get();
		if (item != null) {
			System.out.println("Item not null");
			RenderPartPack itemPack = new RenderPartPack(VanillaRenderMaterials.ITEMS_MATERIAL);
			RenderItemStack itemPart = new RenderItemStack(item);
			itemPart.setZIndex(2);
			itemPart.setPosition(pos);
			itemPack.add(itemPart);
			parts.add(itemPack);
		}
		return parts;
	}

	@Override
	public void onClick(PlayerClickEvent event) {
		System.out.println("Slot clicked!");
	}

	@Override
	public void onKey(PlayerKeyEvent event) {
		System.out.println("Key pressed!");
	}

	@Override
	public void onMouseMoved(IntVector2 prev, IntVector2 pos, boolean hovered) {
		if (this.hovered != hovered) {
			System.out.println("Hovering went from " + this.hovered + " to " + hovered);
			this.hovered = hovered;
			getOwner().update();
		}
	}
}
