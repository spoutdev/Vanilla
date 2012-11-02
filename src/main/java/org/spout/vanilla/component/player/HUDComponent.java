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
import org.spout.api.chat.style.ChatStyle;
import org.spout.api.component.components.EntityComponent;
import org.spout.api.entity.Player;
import org.spout.api.gui.Screen;
import org.spout.api.gui.Widget;
import org.spout.api.gui.component.LabelComponent;
import org.spout.api.gui.component.RenderPartsHolderComponent;
import org.spout.api.gui.render.RenderPart;
import org.spout.api.math.Rectangle;
import org.spout.api.plugin.Platform;
import org.spout.api.render.Font;
import org.spout.api.render.RenderMaterial;

import org.spout.vanilla.VanillaPlugin;
import org.spout.vanilla.data.VanillaData;

/**
 * Component attached to clients-only inwhich serves to update the Heads Up Display.
 */
public class HUDComponent extends EntityComponent {
	// The main HUD screen
	private final Screen HUD = new Screen();
	private final float ratio = 0.75f; // This is temporary it will be applied directly by the engine
	
	// The materials used to construct the render
	private final RenderMaterial hotbarMaterial = (RenderMaterial) Spout.getFilesystem().getResource("material://Vanilla/resources/gui/smt/HotbarGUIMaterial.smt");
	private final RenderMaterial iconsMaterial = (RenderMaterial) Spout.getFilesystem().getResource("material://Vanilla/resources/gui/smt/IconsGUIMaterial.smt");
	private final Font font = (Font) Spout.getFilesystem().getResource("font://Spout/resources/resources/fonts/ubuntu/Ubuntu-M.ttf");
	
	// The core elements of the main HUD
	private final Widget hotbar = new Widget();
	private final Widget hearts = new Widget();
	private final Widget exp = new Widget();
	private final Widget bubbles = new Widget();
	private final Widget armor = new Widget();
	private final Widget hunger = new Widget();
	
	private final float maxSecsBubbles = VanillaData.AIR_SECS.getDefaultValue();

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

	@Override
	public void onTick(float dt) {
		
		// Remaining air handling
		final float secsBubbles = getData().get(VanillaData.AIR_SECS);
		if (secsBubbles==maxSecsBubbles) {
			hideBubbles();
		} else {
			final float nbBubExact = secsBubbles / maxSecsBubbles * 10f;
			final int nbBub = (int) nbBubExact;
			int bubId = 0;
			for (RenderPart bub : bubbles.get(RenderPartsHolderComponent.class).getRenderParts()) {
				if (bubId>nbBub) {
					bub.setSource(new Rectangle(34f / 256f, 18f / 256f, 9f / 256f, 9f / 256f)); // Empty
				} else if (bubId==nbBub) {
					if (nbBubExact-nbBub >= 0.02f) {
						bub.setSource(new Rectangle(34f / 256f, 18f / 256f, 9f / 256f, 9f / 256f)); // Empty
					} else {
						bub.setSource(new Rectangle(25f / 256f, 18f / 256f, 9f / 256f, 9f / 256f)); // Explode
					}
				} else {
					bub.setSource(new Rectangle(16f / 256f, 18f / 256f, 9f / 256f, 9f / 256f)); // full
				}
				bubId++;
			}
		}
		bubbles.update();
	}

	public void hideBubbles() {
		for (RenderPart part : bubbles.get(RenderPartsHolderComponent.class).getRenderParts()) {
			part.setSource(new Rectangle(34f / 256f, 18f / 256f, 9f / 256f, 9f / 256f)); // empty
		}
	}

	public void showBubbles() {
		for (RenderPart part : bubbles.get(RenderPartsHolderComponent.class).getRenderParts()) {
			part.setSource(new Rectangle(16f / 256f, 18f / 256f, 9f / 256f, 9f / 256f)); // full
		}
	}

	public void openHUD() {
		((Client) Spout.getEngine()).getScreenStack().openScreen(HUD);
	}

	private void constructHUD() {
		float x = -0.71f * ratio;

		// Setup the hotbar
		final RenderPartsHolderComponent hotbarRect = hotbar.add(RenderPartsHolderComponent.class);
		final RenderPart hotbarBgRect = new RenderPart();
		hotbarBgRect.setRenderMaterial(hotbarMaterial);
		hotbarBgRect.setColor(Color.WHITE);
		hotbarBgRect.setSprite(new Rectangle(x, -1f, 1.42f * ratio, 0.17f));
		hotbarBgRect.setSource(new Rectangle(0, 0, 0.71f, 0.085f));
		hotbarRect.add(hotbarBgRect, 1);

		final RenderPart hotbarSlotRect = new RenderPart();
		hotbarSlotRect.setRenderMaterial(hotbarMaterial);
		hotbarSlotRect.setColor(Color.WHITE);
		hotbarRect.add(hotbarSlotRect, 0);
		setHotbarSlot(0);
		HUD.attachWidget(VanillaPlugin.getInstance(), hotbar);

		// Setup experience bar
		final RenderPartsHolderComponent expRect = exp.add(RenderPartsHolderComponent.class);
		final RenderPart expBgRect = new RenderPart();
		expBgRect.setRenderMaterial(iconsMaterial);
		expBgRect.setColor(Color.WHITE);
		expBgRect.setSprite(new Rectangle(x, -0.82f, 1.81f * ratio, 0.04f));
		expBgRect.setSource(new Rectangle(0, 64f / 256f, 0.91f, 0.019f));
		expRect.add(expBgRect, 1);

		final RenderPart expBarRect = new RenderPart();
		expBarRect.setRenderMaterial(iconsMaterial);
		expBarRect.setColor(Color.WHITE);
		expRect.add(expBarRect, 0);
		
		final LabelComponent lvlTxt = exp.add(LabelComponent.class);
		exp.setGeometry(new Rectangle(0, -0.82f, 0, 0));
		lvlTxt.setFont(font);
		lvlTxt.setText(ChatStyle.BRIGHT_GREEN+"50");
		
		setExp(0.5f);
		
		HUD.attachWidget(VanillaPlugin.getInstance(), exp);

		float dx = 0.06f * ratio;

		// Setup the hearts display
		final RenderPartsHolderComponent heartsRect = hearts.add(RenderPartsHolderComponent.class);
		for (int i = 0; i < 10; i++) {
			final RenderPart heartRect = new RenderPart();
			heartRect.setRenderMaterial(iconsMaterial);
			heartRect.setColor(Color.WHITE);
			heartRect.setSprite(new Rectangle(x + 0.005f, -0.77f, 0.065f * ratio, 0.065f));
			heartRect.setSource(new Rectangle(53f / 256f, 0, 9f / 256f, 9f / 256f));
			heartsRect.add(heartRect);
			x += dx;
		}

		x = -0.71f * ratio;
		for (int i = 0; i < 10; i++) {
			final RenderPart heartBgRect = new RenderPart();
			heartBgRect.setRenderMaterial(iconsMaterial);
			heartBgRect.setColor(Color.WHITE);
			heartBgRect.setSprite(new Rectangle(x, -0.77f, 0.065f * ratio, 0.065f));
			heartBgRect.setSource(new Rectangle(16f / 256f, 0, 9f / 256f, 9f / 256f));
			heartsRect.add(heartBgRect);
			x += dx;
		}
		HUD.attachWidget(VanillaPlugin.getInstance(), hearts);

		// Setup bubbles display
		final RenderPartsHolderComponent bubblesRect = bubbles.add(RenderPartsHolderComponent.class);
		x = 0.09f * ratio;
		for (int i = 0; i < 10; i++) {
			final RenderPart bubbleRect = new RenderPart();
			bubbleRect.setRenderMaterial(iconsMaterial);
			bubbleRect.setColor(Color.WHITE);
			bubbleRect.setSprite(new Rectangle(x, -0.69f, 0.06f * ratio, 0.06f));
			bubbleRect.setSource(new Rectangle(16f / 256f, 18f / 256f, 9f / 256f, 9f / 256f));
			bubblesRect.add(bubbleRect);
			x += dx;
		}
		HUD.attachWidget(VanillaPlugin.getInstance(), bubbles);

		// Setup the hunger display
		final RenderPartsHolderComponent hungerRect = hunger.add(RenderPartsHolderComponent.class);
		x = 0.09f * ratio;
		for (int i = 0; i < 10; i++) {
			final RenderPart hungerPart = new RenderPart();
			hungerPart.setRenderMaterial(iconsMaterial);
			hungerPart.setColor(Color.WHITE);
			hungerPart.setSprite(new Rectangle(x, -0.77f, 0.075f * ratio, 0.07f));
			hungerPart.setSource(new Rectangle(25f / 256f, 36f / 256f, -10f / 256f, 9f / 256f));
			hungerRect.add(hungerPart);
			x += dx;
		}
		HUD.attachWidget(VanillaPlugin.getInstance(), hunger);

		// Setup the armor display
		final RenderPartsHolderComponent armorRect = armor.add(RenderPartsHolderComponent.class);
		x = -0.71f * ratio;
		for (int i = 0; i < 10; i++) {
			final RenderPart armorPart = new RenderPart();
			armorPart.setRenderMaterial(iconsMaterial);
			armorPart.setColor(Color.WHITE);
			armorPart.setSprite(new Rectangle(x, -0.71f, 0.08f * ratio, 0.08f));
			armorPart.setSource(new Rectangle(43f / 256f, 9f / 256f, 12f / 256f, 12f / 256f));
			armorRect.add(armorPart);
			x += dx;
		}
		HUD.attachWidget(VanillaPlugin.getInstance(), armor);

		// TODO: Pumpkin head
	}

	/**
	 * Sets the selected hotbar slot
	 * @param slot Index of the slot to set
	 */
	public void setHotbarSlot(int slot) {
		if (slot < 0 || slot > 8) {
			throw new IllegalArgumentException("Slot must be between 0 and 8");
		}

		final RenderPart rect = hotbar.get(RenderPartsHolderComponent.class).get(1);
		rect.setSprite(new Rectangle(-0.72f * ratio + (slot * .1175f), -1f, 0.23f * ratio, 0.23f * ratio));
		rect.setSource(new Rectangle(0, 22f / 256f, 30f / 256f, 24f / 256f));
		hotbar.update();
	}

	/**
	 * Modify the advancement of the xp bar.
	 * @param percent The advancement between 0 and 1
	 */
	public void setExp(float percent) {
		final RenderPart rect = exp.get(RenderPartsHolderComponent.class).get(1);
		rect.setSprite(new Rectangle(-0.71f * ratio, -0.82f, 1.81f * ratio * percent, 0.04f));
		rect.setSource(new Rectangle(0, 69f / 256f, 182f / 256f * percent, 5f / 256f));
		exp.update();
	}
}
