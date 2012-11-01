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
package org.spout.vanilla.component.player;

import java.awt.Color;

import org.spout.api.Client;
import org.spout.api.Spout;
import org.spout.api.component.components.EntityComponent;
import org.spout.api.entity.Player;
import org.spout.api.gui.Screen;
import org.spout.api.gui.Widget;
import org.spout.api.gui.component.TexturedRectComponent;
import org.spout.api.math.Rectangle;
import org.spout.api.plugin.Platform;
import org.spout.api.render.RenderMaterial;

import org.spout.vanilla.VanillaPlugin;

/**
 * Component attached to clients-only inwhich serves to update the Heads Up Display.
 */
public class HUDComponent extends EntityComponent {
	//The main HUD screen
	private final Screen HUD = new Screen();
	private final float ratio = 0.75f; // This is temporary it will be applied directly by the engine
	//The materials used to construct the render
	private final RenderMaterial hotbarMaterial = (RenderMaterial) Spout.getFilesystem().getResource("material://Vanilla/resources/gui/smt/HotbarGUIMaterial.smt");
	private final RenderMaterial heartsMaterial = (RenderMaterial) Spout.getFilesystem().getResource("material://Vanilla/resources/gui/smt/HeartsGUIMaterial.smt");
	//The core elements of the main HUD
	private final Widget hotbar = new Widget();
	private final Widget[] hearts = new Widget[10];

	@Override
	public void onAttached() {
		if (!(getOwner() instanceof Player)) {
			throw new IllegalStateException("May only attach this component to players!");
		}
		if (Spout.getPlatform() != Platform.CLIENT) {
			throw new IllegalStateException("This component is only attached to clients!");
		}
		constructHUD();
	}

	public void openHUD() {
		((Client) Spout.getEngine()).getScreenStack().openScreen(HUD);
	}
	
	private void constructHUD() {
		//Setup the hotbar
		final TexturedRectComponent hotbarRectangle = hotbar.add(TexturedRectComponent.class);
		hotbarRectangle.setRenderMaterial(hotbarMaterial);
		hotbarRectangle.setColor(Color.WHITE);
		hotbarRectangle.setSprite(new Rectangle(-0.71f * ratio, -1f, 1.42f * ratio, 0.17f));
		hotbarRectangle.setSource(new Rectangle(0, 0, 0.71f, 0.085f));
		HUD.attachWidget(VanillaPlugin.getInstance(), hotbar);
		
		//Setup the hearts display
		float dx = 0.05f * ratio;
		float x = -0.71f * ratio;
		for (int i=0 ; i < 10; i++) {
			hearts[i] = new Widget();
			final TexturedRectComponent heart = hearts[i].add(TexturedRectComponent.class);
			heart.setRenderMaterial(heartsMaterial);
			heart.setColor(Color.WHITE);
			heart.setSprite(new Rectangle(x, -0.83f, 0.05f*ratio, 0.05f));
			heart.setSource(new Rectangle(53f/256f, 0, 9f/256f, 9f/256f));
			HUD.attachWidget(VanillaPlugin.getInstance(), hearts[i]);
			x += dx;
		}
		
		//TODO: Armor bar
		
		//TODO: Exp bar
		
		//TODO: Food bar
		
		//TODO: Pumpkin head
	}
}
