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
package org.spout.vanilla.plugin.component.player.hud;

import java.awt.Color;

import org.spout.api.gui.Widget;
import org.spout.api.gui.component.RenderPartsHolderComponent;
import org.spout.api.gui.render.RenderPart;
import org.spout.api.gui.render.RenderPartPack;
import org.spout.api.math.Rectangle;

import org.spout.vanilla.api.inventory.entity.QuickbarInventory;

import org.spout.vanilla.plugin.component.inventory.PlayerInventoryComponent;
import org.spout.vanilla.plugin.component.player.HUDComponent;
import org.spout.vanilla.api.data.VanillaRenderMaterials;

public class VanillaHotBar extends HotBarWidget {
	
	private RenderPartPack hotbarPack = new RenderPartPack(VanillaRenderMaterials.HOTBAR_MATERIAL);
	
	@Override
	public void init(Widget hotbar, HUDComponent hud) {
		super.init(hotbar, hud);
		// Setup the hotbar
		final RenderPartsHolderComponent hotbarRect = widget.add(RenderPartsHolderComponent.class);
		hotbarRect.add(hotbarPack);
		
		final RenderPart hotbarBgRect = new RenderPart();
		hotbarBgRect.setColor(Color.WHITE);
		hotbarBgRect.setSprite(new Rectangle(START_X, -1f, 1.42f * SCALE, 0.17f));
		hotbarBgRect.setSource(new Rectangle(0, 0, 0.71f, 0.085f));
		hotbarPack.add(hotbarBgRect, 1);

		final RenderPart hotbarSlotRect = new RenderPart();
		hotbarSlotRect.setColor(Color.WHITE);
		hotbarSlotRect.setSource(new Rectangle(0, 22f / 256f, 30f / 256f, 24f / 256f));
		hotbarSlotRect.setSprite(new Rectangle(-0.72f * SCALE + (0 * .1175f), -1.005f, 0.24f * SCALE, 0.24f * SCALE));
		hotbarPack.add(hotbarSlotRect, 0);
		
		attach();
	}

	/**
	 * Sets the selected hotbar slot.
	 * @param slot Index of the slot to set
	 */
	@Override
	public void update() {
		PlayerInventoryComponent inventory = hud.getOwner().get(PlayerInventoryComponent.class);
		if (inventory == null) {
			return;
		}
		QuickbarInventory quickbar = inventory.getQuickbar();
		if (quickbar == null) {
			return;
		}
		int slot = quickbar.getSelectedSlot().getIndex();
		
		if (slot < 0 || slot > 8) {
			throw new IllegalArgumentException("Slot must be between 0 and 8");
		}

		final RenderPart rect = hotbarPack.get(1);
		rect.setSprite(new Rectangle(-0.72f * SCALE + (slot * .1175f), -1.005f, 0.24f * SCALE, 0.24f * SCALE));
		widget.update();
	}


	@Override
	public void animate() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void show() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void hide() {
		throw new UnsupportedOperationException("Not supported yet.");
	}
}
