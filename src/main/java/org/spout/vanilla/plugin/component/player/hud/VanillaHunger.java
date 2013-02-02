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
import java.util.List;
import java.util.Random;

import org.spout.api.gui.Widget;
import org.spout.api.gui.component.RenderPartsHolderComponent;
import org.spout.api.gui.render.RenderPart;
import org.spout.api.math.Rectangle;
import org.spout.vanilla.plugin.component.misc.HungerComponent;
import org.spout.vanilla.plugin.component.player.HUDComponent;
import org.spout.vanilla.api.data.VanillaRenderMaterials;

public class VanillaHunger extends HungerWidget {
	private int hungerTicks;
	private final Random random = new Random();

	@Override
	public void init(Widget widget, HUDComponent hud) {
		super.init(widget, hud);
		final RenderPartsHolderComponent hungerRect = widget.add(RenderPartsHolderComponent.class);
		float x = 0.09f * SCALE;
		float dx = 0.06f * SCALE;
		for (int i = 0; i < 10; i++) {
			final RenderPart hunger = new RenderPart();
			hunger.setRenderMaterial(VanillaRenderMaterials.ICONS_MATERIAL);
			hunger.setColor(Color.WHITE);
			hunger.setSprite(new Rectangle(x, -0.77f, 0.075f * SCALE, 0.07f));
			hunger.setSource(new Rectangle(52f / 256f, 27f / 256f, 9f / 256f, 9f / 256f));
			hungerRect.add(hunger);
			x += dx;
		}

		x = 0.09f * SCALE;
		for (int i = 0; i < 10; i++) {
			final RenderPart hungerBg = new RenderPart();
			hungerBg.setRenderMaterial(VanillaRenderMaterials.ICONS_MATERIAL);
			hungerBg.setColor(Color.WHITE);
			hungerBg.setSprite(new Rectangle(x, -0.77f, 0.075f * SCALE, 0.07f));
			hungerBg.setSource(new Rectangle(16f / 256f, 27f / 256f, 9f / 256f, 9f / 256f));
			hungerRect.add(hungerBg);
			x += dx;
		}
		
		attach();
	}

	@Override
	public void animate() {
		HungerComponent hunger = hud.getOwner().get(HungerComponent.class);
		if (hunger == null) {
			return;
		}
		
		float x;
		float y;
		float dx = 0.06f * SCALE;

		// Animate hunger bar
		float saturation = hunger.getFoodSaturation();
		
		if (saturation <= 0) {
			List<RenderPart> parts = widget.get(RenderPartsHolderComponent.class).getRenderParts();
			if (hungerTicks == 98) {
				x = 0.09f * SCALE;
				y = -0.77f;
				for (int i = 0; i < 10; i++) {

					RenderPart part = parts.get(i);
					RenderPart partBg = parts.get(i + 10);

					int rand = random.nextInt(3);
					if (rand == 0) {
						y = -0.765f; // Twitch up
					} else if (rand == 1) {
						y = -0.775f; // Twitch down
					}

					part.setSprite(new Rectangle(x, y, 0.075f * SCALE, 0.07f));
					partBg.setSprite(new Rectangle(x, y, 0.075f * SCALE, 0.07f));
					x += dx;
				}
				hungerTicks++;
				widget.update();
			} else if (hungerTicks == 100) {
				// Reset hunger bar to normal
				x = 0.09f * SCALE;
				for (int i = 0; i < 10; i++) {
					parts.get(i).setSprite(new Rectangle(x, -0.77f, 0.075f * SCALE, 0.07f));
					parts.get(i + 10).setSprite(new Rectangle(x, -0.77f, 0.075f * SCALE, 0.07f));
					x += dx;
				}
				hungerTicks = 0;
			} else {
				hungerTicks++;
			}
		}
	}

	@Override
	public void show() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void hide() {
		throw new UnsupportedOperationException("Not supported yet.");
	}
	
	@Override
	public void update() {
		HungerComponent hungerComp = hud.getOwner().get(HungerComponent.class);
		int hunger = hungerComp.getHunger();
		
		float fx = hungerComp.getFx();
		float bx = hungerComp.getBx();
		//Widget testWidget = getOwner().get(HUDComponent.class).getHungerMeter().getWidget();
		
		RenderPartsHolderComponent hungerRect = widget.get(RenderPartsHolderComponent.class);
		
		if (hunger == 0) {

			for (int i = 0; i < 10; i++) {
				hungerRect.get(i).setSource(new Rectangle(142f / 256f, 27f / 256f, 9f / 256f, 9f / 256f)); // Foreground
			}

			for (int i = 10; i < 20; i++) {
				hungerRect.get(i).setSource(new Rectangle(bx / 256f, 27f / 256f, 9f / 256f, 9f / 256f)); // Background
			}
		} else {

			for (int i = 9; i >= 0; i--) {
				if (hunger == 0) {
					fx = 142f; // Empty
				} else if (hunger == 1) {
					fx += 9f; // Half
					hunger = 0;
				} else {
					hunger -= 2; // Full
				}
				hungerRect.get(i).setSource(new Rectangle(fx / 256f, 27f / 256f, 9f / 256f, 9f / 256f));
			}

			for (int i = 19; i >= 10; i--) {
				hungerRect.get(i).setSource(new Rectangle(bx / 256f, 27f / 256f, 9f / 256f, 9f / 256f));
			}
		}

		widget.update();
	}
}
