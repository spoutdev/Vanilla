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

import java.awt.*;

import org.spout.api.gui.Widget;
import org.spout.api.gui.component.TexturedRectComponent;
import org.spout.api.math.Rectangle;

import org.spout.vanilla.component.entity.player.HUD;
import org.spout.vanilla.data.VanillaRenderMaterials;

public class VanillaCrosshair extends CrosshairWidget {
	@Override
	public void init(Widget crosshair, HUD hud) {
		super.init(crosshair, hud);
		final TexturedRectComponent crosshairRect = widget.add(TexturedRectComponent.class);
		crosshairRect.setRenderMaterial(VanillaRenderMaterials.ICONS_MATERIAL);
		crosshairRect.setColor(Color.WHITE);
		crosshairRect.setSprite(new Rectangle(-0.0625f * SCALE, -0.0625f, 0.125f * SCALE, 0.125f));
		crosshairRect.setSource(new Rectangle(0f / 256f, 0f / 256f, 16f / 256f, 16f / 256f));
		
		attach();
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

	@Override
	public void update() {
		throw new UnsupportedOperationException("Not supported yet.");
	}
}
