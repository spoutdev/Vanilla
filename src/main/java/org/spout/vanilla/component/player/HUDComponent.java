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
import org.spout.api.chat.ChatArguments;
import org.spout.api.chat.style.ChatStyle;
import org.spout.api.component.components.EntityComponent;
import org.spout.api.entity.Player;
import org.spout.api.gui.Screen;
import org.spout.api.gui.Widget;
import org.spout.api.gui.component.LabelComponent;
import org.spout.api.gui.component.RenderPartsHolderComponent;
import org.spout.api.gui.component.TexturedRectComponent;
import org.spout.api.gui.render.RenderPart;
import org.spout.api.math.Rectangle;
import org.spout.api.plugin.Platform;

import org.spout.vanilla.VanillaPlugin;
import org.spout.vanilla.data.VanillaRenderMaterials;

/**
 * Component attached to clients-only that updates the Heads Up Display.
 */
public class HUDComponent extends EntityComponent {
	private static final float SCALE = 0.75f; // TODO: Apply directly from engine
	private static final float START_X = -0.71f * SCALE;
	// The main hud screen
	private final Screen hud = new Screen();
	// The core elements of the main hud
	private final Widget crosshair = new Widget();
	private final Widget hotbar = new Widget();
	private final Widget exp = new Widget();
	private final Widget armor = new Widget();

	@Override
	public void onAttached() {
		if (!(getOwner() instanceof Player)) {
			throw new IllegalStateException("May only attach this component to players!");
		}
		if (Spout.getPlatform() != Platform.CLIENT) {
			throw new IllegalStateException("This component is only attached to clients!");
		}

		initHUD();
		// TODO: Call these methods in DrowningComponent, HealthComponent, etc.
		setArmor(15);
		setHotbarSlot(0);
	}

	public void attachWidget(Widget widget) {
		hud.attachWidget(VanillaPlugin.getInstance(), widget);
	}

	public void openHUD() {
		((Client) Spout.getEngine()).getScreenStack().openScreen(hud);
	}

	/**
	 * Sets the amount of armor to display.
	 * @param amount Amount of armor to display
	 */
	public void setArmor(int amount) {
		RenderPartsHolderComponent armorRect = armor.get(RenderPartsHolderComponent.class);

		if (amount == 0) {
			for (RenderPart armorPart : armorRect.getRenderParts()) {
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
				armorRect.get(i).setSource(new Rectangle(x / 256f, 9f / 256f, 9f / 256f, 9f / 256f));
			}
		}

		armor.update();
	}

	/**
	 * Modify the advancement of the xp bar.
	 * @param percent The advancement between 0 and 1
	 */
	public void setExp(float percent) {
		final RenderPart rect = exp.get(RenderPartsHolderComponent.class).get(1);
		rect.setSprite(new Rectangle(START_X, -0.82f, 1.81f * SCALE * percent, 0.04f));
		rect.setSource(new Rectangle(0, 69f / 256f, 182f / 256f * percent, 5f / 256f));
		exp.update();
	}

	/**
	 * Sets the selected hotbar slot.
	 * @param slot Index of the slot to set
	 */
	public void setHotbarSlot(int slot) {
		if (slot < 0 || slot > 8) {
			throw new IllegalArgumentException("Slot must be between 0 and 8");
		}

		final RenderPart rect = hotbar.get(RenderPartsHolderComponent.class).get(1);
		rect.setSprite(new Rectangle(-0.72f * SCALE + (slot * .1175f), -1.005f, 0.24f * SCALE, 0.24f * SCALE));
		hotbar.update();
	}

	private void initHUD() {
		hud.setTakesInput(false);

		float x = START_X;

		// Setup crosshairs
		final TexturedRectComponent crosshairRect = crosshair.add(TexturedRectComponent.class);
		crosshairRect.setRenderMaterial(VanillaRenderMaterials.ICONS_MATERIAL);
		crosshairRect.setColor(Color.WHITE);
		crosshairRect.setSprite(new Rectangle(-0.0625f * SCALE, -0.0625f, 0.125f * SCALE, 0.125f));
		crosshairRect.setSource(new Rectangle(0f / 256f, 0f / 256f, 16f / 256f, 16f / 256f));
		hud.attachWidget(VanillaPlugin.getInstance(), crosshair);

		// Setup the hotbar
		final RenderPartsHolderComponent hotbarRect = hotbar.add(RenderPartsHolderComponent.class);
		final RenderPart hotbarBgRect = new RenderPart();
		hotbarBgRect.setRenderMaterial(VanillaRenderMaterials.HOTBAR_MATERIAL);
		hotbarBgRect.setColor(Color.WHITE);
		hotbarBgRect.setSprite(new Rectangle(x, -1f, 1.42f * SCALE, 0.17f));
		hotbarBgRect.setSource(new Rectangle(0, 0, 0.71f, 0.085f));
		hotbarRect.add(hotbarBgRect, 1);

		final RenderPart hotbarSlotRect = new RenderPart();
		hotbarSlotRect.setRenderMaterial(VanillaRenderMaterials.HOTBAR_MATERIAL);
		hotbarSlotRect.setColor(Color.WHITE);
		hotbarSlotRect.setSource(new Rectangle(0, 22f / 256f, 30f / 256f, 24f / 256f));
		hotbarRect.add(hotbarSlotRect, 0);
		hud.attachWidget(VanillaPlugin.getInstance(), hotbar);

		// Experience level text
		final LabelComponent lvlTxt = exp.add(LabelComponent.class);
		exp.setGeometry(new Rectangle(-0.02f, -0.79f, 0, 0));
		lvlTxt.setFont(VanillaRenderMaterials.FONT);
		lvlTxt.setText(new ChatArguments(ChatStyle.BRIGHT_GREEN, "50"));

		// Setup survival-specific hud components
		boolean survival = true;
		if (survival) {
			// Experience bar
			final RenderPartsHolderComponent expRect = exp.add(RenderPartsHolderComponent.class);
			final RenderPart expBgRect = new RenderPart();
			expBgRect.setRenderMaterial(VanillaRenderMaterials.ICONS_MATERIAL);
			expBgRect.setColor(Color.WHITE);
			expBgRect.setSprite(new Rectangle(x, -0.82f, 1.81f * SCALE, 0.04f));
			expBgRect.setSource(new Rectangle(0, 64f / 256f, 0.91f, 0.019f));
			expRect.add(expBgRect);

			final RenderPart expBarRect = new RenderPart();
			expBarRect.setRenderMaterial(VanillaRenderMaterials.ICONS_MATERIAL);
			expBarRect.setColor(Color.WHITE);
			expRect.add(expBarRect);
			setExp(0);

			float dx = 0.06f * SCALE;

			// Armor bar
			final RenderPartsHolderComponent armorRect = armor.add(RenderPartsHolderComponent.class);
			x = START_X;
			for (int i = 0; i < 10; i++) {
				final RenderPart armorPart = new RenderPart();
				armorPart.setRenderMaterial(VanillaRenderMaterials.ICONS_MATERIAL);
				armorPart.setColor(Color.WHITE);
				armorPart.setSprite(new Rectangle(x, -0.7f, 0.06f * SCALE, 0.06f));
				armorPart.setSource(new Rectangle(52f / 256f, 9f / 256f, 12f / 256f, 12f / 256f));
				armorRect.add(armorPart);
				x += dx;
			}
			hud.attachWidget(VanillaPlugin.getInstance(), armor);
		}

		hud.attachWidget(VanillaPlugin.getInstance(), exp);
	}
}
