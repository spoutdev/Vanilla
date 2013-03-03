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
package org.spout.vanilla.component.entity.player.hud;

import java.awt.Color;

import org.spout.api.gui.Widget;
import org.spout.api.gui.component.RenderPartsHolderComponent;
import org.spout.api.gui.render.RenderPart;
import org.spout.api.gui.render.RenderPartPack;
import org.spout.api.math.Rectangle;

import org.spout.vanilla.component.entity.player.HUD;
import org.spout.vanilla.data.VanillaRenderMaterials;

public class VanillaArmorWidget extends ArmorWidget {
	private final RenderPartPack armorPack = new RenderPartPack(VanillaRenderMaterials.ICONS_MATERIAL);

	@Override
	public void init(Widget armor, HUD hud) {
		super.init(armor, hud);
		final RenderPartsHolderComponent armorRect = widget.add(RenderPartsHolderComponent.class);
		armorRect.add(armorPack);
		float x = START_X;
		float dx = 0.06f * SCALE;
		for (int i = 0; i < 10; i++) {
			final RenderPart armorPart = new RenderPart();
			armorPart.setColor(Color.WHITE);
			armorPart.setSprite(new Rectangle(x, -0.7f, 0.06f * SCALE, 0.06f));
			armorPart.setSource(new Rectangle(52f / 256f, 9f / 256f, 12f / 256f, 12f / 256f));
			armorPack.add(armorPart);
			x += dx;
		}

		attach();
	}

	@Override
	public void update() {

		int amount = hud.getArmor();

		if (amount == 0) {
			for (RenderPart armorPart : armorPack.getRenderParts()) {
				armorPart.setSource(new Rectangle(52f / 256f, 9f / 256f, 12f / 256f, 12f / 256f)); // No icon
			}
		} else {
			for (int i = 0; i < 10; i++) {
				float x = 0;
				if (amount >= 2) {
					x = 43f; // Full
					amount -= 2;
				} else if (amount == 1) {
					x = 25f; // Half
					amount = 0;
				} else if (amount == 0) {
					x = 16f; // Empty
				}
				armorPack.get(i).setSource(new Rectangle(x / 256f, 9f / 256f, 9f / 256f, 9f / 256f));
			}
		}
		widget.update();
	}

	@Override
	public void animate() {
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
