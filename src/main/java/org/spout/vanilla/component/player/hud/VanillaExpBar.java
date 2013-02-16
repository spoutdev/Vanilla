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
package org.spout.vanilla.component.player.hud;

import java.awt.*;

import org.spout.api.chat.ChatArguments;
import org.spout.api.chat.style.ChatStyle;
import org.spout.api.gui.Widget;
import org.spout.api.gui.component.LabelComponent;
import org.spout.api.gui.component.RenderPartsHolderComponent;
import org.spout.api.gui.render.RenderPart;
import org.spout.api.gui.render.RenderPartPack;
import org.spout.api.math.Rectangle;

import org.spout.vanilla.component.player.HUDComponent;
import org.spout.vanilla.data.VanillaRenderMaterials;

public class VanillaExpBar extends ExpBarWidget {
	private RenderPartPack expPack = new RenderPartPack(VanillaRenderMaterials.ICONS_MATERIAL);
	
	@Override
	public void init(Widget exp, HUDComponent hud) {
		super.init(exp, hud);
		final LabelComponent lvlTxt = widget.add(LabelComponent.class);
		widget.getTransform().add(-0.02f, -0.79f);
		lvlTxt.setFont(VanillaRenderMaterials.FONT);
		lvlTxt.setText(new ChatArguments(ChatStyle.BRIGHT_GREEN, "50"));

		// Setup survival-specific hud components
		boolean survival = true;
		if (survival) {
			// Experience bar
			final RenderPartsHolderComponent expRect = widget.add(RenderPartsHolderComponent.class);
			expRect.add(expPack);
			
			final RenderPart expBgRect = new RenderPart();
			expBgRect.setColor(Color.WHITE);
			expBgRect.setSprite(new Rectangle(START_X, -0.82f, 1.81f * SCALE, 0.04f));
			expBgRect.setSource(new Rectangle(0, 64f / 256f, 0.91f, 0.019f));
			expPack.add(expBgRect);

			final RenderPart expBarRect = new RenderPart();
			expBarRect.setColor(Color.WHITE);
			expPack.add(expBarRect);
			
			final RenderPart rect = expPack.get(1);
			rect.setSprite(new Rectangle(START_X, -0.82f, 1.81f * SCALE * 0f, 0.04f));
			rect.setSource(new Rectangle(0, 69f / 256f, 182f / 256f * 0f, 5f / 256f));
		}
		
		attach();
	}

	@Override
	public void update() {
		float percent = hud.getExpPercent();
		final RenderPart rect = expPack.get(1);
		rect.setSprite(new Rectangle(START_X, -0.82f, 1.81f * SCALE * percent, 0.04f));
		rect.setSource(new Rectangle(0, 69f / 256f, 182f / 256f * percent, 5f / 256f));
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
