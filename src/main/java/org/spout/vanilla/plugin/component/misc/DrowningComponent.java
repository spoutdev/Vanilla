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
package org.spout.vanilla.plugin.component.misc;

import java.awt.Color;

import org.spout.api.Client;
import org.spout.api.Spout;
import org.spout.api.component.type.EntityComponent;
import org.spout.api.entity.Entity;
import org.spout.api.entity.Player;
import org.spout.api.geo.World;
import org.spout.api.gui.Widget;
import org.spout.api.gui.component.RenderPartsHolderComponent;
import org.spout.api.gui.render.RenderPart;
import org.spout.api.math.Rectangle;

import org.spout.vanilla.api.data.GameMode;
import org.spout.vanilla.plugin.component.living.neutral.Human;
import org.spout.vanilla.plugin.component.player.HUDComponent;
import org.spout.vanilla.plugin.component.player.hud.DrowningWidget;
import org.spout.vanilla.plugin.data.VanillaData;
import org.spout.vanilla.plugin.data.VanillaRenderMaterials;
import org.spout.vanilla.plugin.event.cause.BlockDamageCause;
import org.spout.vanilla.plugin.event.cause.DamageCause.DamageType;
import org.spout.vanilla.plugin.material.block.liquid.Water;

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
	//private final Widget bubbles = new Widget();
	//private static final float SCALE = 0.75f; // TODO: Apply directly from engine

	@Override
	public void onAttached() {
		owner = getOwner();
		health = owner.add(HealthComponent.class);
		head = owner.add(HeadComponent.class);
	}

	@Override
	public boolean canTick() {
		return !health.isDead();
	}

	@Override
	public void onTick(float dt) {
		switch (Spout.getPlatform()) {
			case PROXY:
			case SERVER:
				World world = head.getPosition().getWorld();
				if (!(world.getBlock(head.getPosition()).getMaterial() instanceof Water)) {
					setAir(MAX_AIR);
					return;
				}

				if (getOwner() instanceof Player && !getOwner().get(Human.class).getGameMode().equals(GameMode.SURVIVAL)) {
					return;
				}

				setAir(getAir() - dt);
				if (getAir() < 0) {
					// out of air; damage one heart every second
					if (damageTimer-- < 0) {
						health.damage(2, new BlockDamageCause(world.getBlock(head.getPosition()), DamageType.DROWN));
						damageTimer = 20;
					}
				}
				break;
			case CLIENT:
				if (!(getOwner() instanceof Player)) {
					return;
				}
				// Animate air meter
                                setAir(5);
				final float maxSecsBubbles = VanillaData.AIR_SECS.getDefaultValue();
				final float secsBubbles = getData().get(VanillaData.AIR_SECS);
				if (secsBubbles == maxSecsBubbles) {
					hideBubbles();
				} else {
					final float nbBubExact = secsBubbles / maxSecsBubbles * 10f;
                                        getOwner().add(HUDComponent.class).setDrowning(nbBubExact);
				}
				break;
		}
	}

	public void hideBubbles() {
		for (RenderPart bubble : getOwner().add(HUDComponent.class).getAirMeter().getRenderParts()) {
			bubble.setSource(new Rectangle(34f / 256f, 18f / 256f, 9f / 256f, 9f / 256f)); // Empty
		}
                
                getOwner().add(HUDComponent.class).getAirMeter().update();
	}

	public void showBubbles() {
		for (RenderPart bubble : getOwner().add(HUDComponent.class).getAirMeter().getRenderParts()) {
			bubble.setSource(new Rectangle(16f / 256f, 18f / 256f, 9f / 256f, 9f / 256f)); // Full
		}
                
                getOwner().add(HUDComponent.class).getAirMeter().update();
	}

	public float getAir() {
		return getData().get(VanillaData.AIR_SECS);
	}

	public void setAir(float airSecs) {
		getData().put(VanillaData.AIR_SECS, airSecs);
	}
}
