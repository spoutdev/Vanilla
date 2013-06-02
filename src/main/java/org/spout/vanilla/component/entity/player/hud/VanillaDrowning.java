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

import org.spout.vanilla.component.entity.misc.Drowning;
import org.spout.vanilla.component.entity.player.HUD;
import org.spout.vanilla.data.VanillaRenderMaterials;

public class VanillaDrowning extends DrowningWidget {
	private final RenderPartPack bubblePack = new RenderPartPack(VanillaRenderMaterials.ICONS_MATERIAL);
	private final Rectangle bubbleFull = new Rectangle(16f / 256f, 19f / 256f, 9f / 256f, 8f / 256f);
	private final Rectangle bubbleExplode = new Rectangle(25f / 256f, 19f / 256f, 9f / 256f, 8f / 256f);
	private final Rectangle bubbleEmpty = new Rectangle(34f / 256f, 19f / 256f, 9f / 256f, 8f / 256f);

	@Override
	public void init(Widget drowning, HUD hud) {
		super.init(drowning, hud);

		final RenderPartsHolderComponent bubblesRect = widget.add(RenderPartsHolderComponent.class);
		bubblesRect.add(bubblePack);

		float x = 0.09f * SCALE;
		float dx = 0.06f * SCALE;
		for (int i = 0; i < 10; i++) {
			final RenderPart bubble = new RenderPart();
			bubble.setColor(Color.WHITE);
			bubble.setSprite(new Rectangle(x, -0.69f, 0.06f * SCALE, 0.06f));
			bubble.setSource(bubbleFull);
			bubblePack.add(bubble);
			x += dx;
		}

		attach();
	}

	@Override
	public void update() {
		float nbBubExact = hud.getOwner().get(Drowning.class).getNbBubExact();
		final int nbBub = (int) nbBubExact;
		int bubId = 0;
		for (RenderPart bub : bubblePack.getRenderParts()) {
			if (bubId > nbBub) {
				bub.setSource(bubbleEmpty);
			} else if (bubId == nbBub) {
				if (nbBubExact - nbBub >= 0.02f) {
					bub.setSource(bubbleEmpty);
				} else {
					bub.setSource(bubbleExplode);
				}
			} else {
				bub.setSource(bubbleFull);
			}
			bubId++;
		}
		widget.update();
	}

	@Override
	public void animate() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void show() {
		for (RenderPart bubble : bubblePack.getRenderParts()) {
			bubble.setSource(bubbleFull);
		}
		widget.update();
	}

	@Override
	public void hide() {
		for (RenderPart bubble : bubblePack.getRenderParts()) {
			bubble.setSource(bubbleEmpty);
		}
		widget.update();
	}
}
