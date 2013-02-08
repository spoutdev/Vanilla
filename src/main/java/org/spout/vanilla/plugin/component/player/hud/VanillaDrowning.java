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
import org.spout.api.math.Rectangle;

import org.spout.vanilla.plugin.component.misc.Drowning;
import org.spout.vanilla.plugin.component.player.HUDComponent;
import org.spout.vanilla.api.data.VanillaRenderMaterials;

public class VanillaDrowning extends DrowningWidget {
	@Override
	public void init(Widget drowning, HUDComponent hud) {
		super.init(drowning, hud);

		final RenderPartsHolderComponent bubblesRect = widget.add(RenderPartsHolderComponent.class);
		float x = 0.09f * SCALE;
		float dx = 0.06f * SCALE;
		for (int i = 0; i < 10; i++) {
			final RenderPart bubble = new RenderPart();
			bubble.setRenderMaterial(VanillaRenderMaterials.ICONS_MATERIAL);
			bubble.setColor(Color.WHITE);
			bubble.setSprite(new Rectangle(x, -0.69f, 0.06f * SCALE, 0.06f));
			bubble.setSource(new Rectangle(16f / 256f, 18f / 256f, 9f / 256f, 9f / 256f));
			bubblesRect.add(bubble);
			x += dx;
		}
		
		attach();
	}

	@Override
	public void update() {
		float nbBubExact = hud.getOwner().get(Drowning.class).getNbBubExact();
		final int nbBub = (int) nbBubExact;
		int bubId = 0;
		for (RenderPart bub : widget.get(RenderPartsHolderComponent.class).getRenderParts()) {
			if (bubId > nbBub) {
				bub.setSource(new Rectangle(34f / 256f, 18f / 256f, 9f / 256f, 9f / 256f)); // Empty
			} else if (bubId == nbBub) {
				if (nbBubExact - nbBub >= 0.02f) {
					bub.setSource(new Rectangle(34f / 256f, 18f / 256f, 9f / 256f, 9f / 256f)); // Empty
				} else {
					bub.setSource(new Rectangle(25f / 256f, 18f / 256f, 9f / 256f, 9f / 256f)); // Explode
				}
			} else {
				bub.setSource(new Rectangle(16f / 256f, 18f / 256f, 9f / 256f, 9f / 256f)); // Full
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
		for (RenderPart bubble : widget.getRenderParts()) {
			bubble.setSource(new Rectangle(16f / 256f, 18f / 256f, 9f / 256f, 9f / 256f)); // Full
		}
		widget.update();
	}

	@Override
	public void hide() {
		for (RenderPart bubble : widget.getRenderParts()) {
			bubble.setSource(new Rectangle(34f / 256f, 18f / 256f, 9f / 256f, 9f / 256f)); // Empty
		}
		widget.update();
	}
}
