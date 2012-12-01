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
package org.spout.vanilla.component.misc;

import java.awt.Color;

import org.spout.api.Client;
import org.spout.api.Spout;
import org.spout.api.component.components.EntityComponent;
import org.spout.api.entity.Entity;
import org.spout.api.entity.Player;
import org.spout.api.gui.Widget;
import org.spout.api.gui.component.RenderPartsHolderComponent;
import org.spout.api.gui.render.RenderPart;
import org.spout.api.math.Rectangle;

import org.spout.vanilla.component.player.HUDComponent;
import org.spout.vanilla.data.RenderMaterials;
import org.spout.vanilla.data.VanillaData;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.source.DamageCause;

/**
 * The drowning component requires a health component and head component
 */
public class DrowningComponent extends EntityComponent {
	private Entity owner;
	private HealthComponent health;
	private HeadComponent head;
	public static final float MAX_AIR = VanillaData.AIR_SECS.getDefaultValue();
	private int damageTimer = 20;
	// Client
	private final Widget bubbles = new Widget();
	private static final float SCALE = 0.75f; // TODO: Apply directly from engine

	@Override
	public void onAttached() {
		owner = getOwner();
		health = owner.add(HealthComponent.class);
		head = owner.add(HeadComponent.class);
		if (Spout.getEngine() instanceof Client && getOwner() instanceof Player) {
			// Air meter
			final RenderPartsHolderComponent bubblesRect = bubbles.add(RenderPartsHolderComponent.class);
			float x = 0.09f * SCALE;
			float dx = 0.06f * SCALE;
			for (int i = 0; i < 10; i++) {
				final RenderPart bubble = new RenderPart();
				bubble.setRenderMaterial(RenderMaterials.ICONS_MATERIAL);
				bubble.setColor(Color.WHITE);
				bubble.setSprite(new Rectangle(x, -0.69f, 0.06f * SCALE, 0.06f));
				bubble.setSource(new Rectangle(16f / 256f, 18f / 256f, 9f / 256f, 9f / 256f));
				bubblesRect.add(bubble);
				x += dx;
			}
			getOwner().add(HUDComponent.class).attachWidget(bubbles);
		}
	}

	@Override
	public void onTick(float dt) {
		switch (Spout.getPlatform()) {
			case PROXY:
			case SERVER:
				if (owner.getWorld().getBlock(head.getPosition()).getMaterial() != VanillaMaterials.WATER) {
					setAir(MAX_AIR);
					return;
				}
				setAir(getAir() - dt);
				if (getAir() < 0) {
					// out of air; damage one heart every second
					if (damageTimer-- < 0) {
						health.damage(2, DamageCause.DROWN);
						damageTimer = 20;
					}
				}
				break;
			case CLIENT:
				// Animate air meter
				final float maxSecsBubbles = VanillaData.AIR_SECS.getDefaultValue();
				final float secsBubbles = getData().get(VanillaData.AIR_SECS);
				if (secsBubbles == maxSecsBubbles) {
					hideBubbles();
				} else {
					final float nbBubExact = secsBubbles / maxSecsBubbles * 10f;
					final int nbBub = (int) nbBubExact;
					int bubId = 0;
					for (RenderPart bub : bubbles.get(RenderPartsHolderComponent.class).getRenderParts()) {
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
				}
				bubbles.update();
				break;
		}
	}

	public void hideBubbles() {
		for (RenderPart bubble : bubbles.get(RenderPartsHolderComponent.class).getRenderParts()) {
			bubble.setSource(new Rectangle(34f / 256f, 18f / 256f, 9f / 256f, 9f / 256f)); // Empty
		}
	}

	public void showBubbles() {
		for (RenderPart bubble : bubbles.get(RenderPartsHolderComponent.class).getRenderParts()) {
			bubble.setSource(new Rectangle(16f / 256f, 18f / 256f, 9f / 256f, 9f / 256f)); // Full
		}
	}

	public float getAir() {
		return getData().get(VanillaData.AIR_SECS);
	}

	public void setAir(float airSecs) {
		getData().put(VanillaData.AIR_SECS, airSecs);
	}
}
