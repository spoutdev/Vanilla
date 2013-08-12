/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011 Spout LLC <http://www.spout.org/>
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
package org.spout.vanilla.component.entity.substance.vehicle;

import java.util.List;

import org.spout.api.event.entity.EntityInteractEvent;
import org.spout.api.event.player.PlayerInteractEntityEvent;
import org.spout.api.geo.discrete.Point;
import org.spout.api.inventory.ItemStack;
import org.spout.vanilla.component.entity.misc.DeathDrops;
import org.spout.vanilla.component.entity.misc.MetadataComponent;
import org.spout.vanilla.component.entity.substance.Item;
import org.spout.vanilla.component.entity.substance.Substance;
import org.spout.vanilla.data.Metadata;
import org.spout.vanilla.data.VanillaData;
import org.spout.vanilla.event.entity.network.EntityStatusEvent;
import org.spout.vanilla.protocol.msg.entity.EntityStatusMessage;

/**
 * A destroyable vehicle substance that can take damage.
 * Instead of losing health, it loses durability.
 * Durability is recovered at a fixed rate over time.
 */
public class VehicleSubstance extends Substance {

	@Override
	public void onAttached() {
		super.onAttached();

		// Set initial durability to the maximum
		getOwner().getData().put(VanillaData.DURABILITY, getMaxDurability());

		// Add metadata for damage taken
		getOwner().add(MetadataComponent.class).addMeta(new Metadata<Float>(Metadata.TYPE_FLOAT, 19) {
			@Override
			public Float getValue() {
				return getMaxDurability() - getDurability();
			}

			@Override
			public void setValue(Float value) {
				setDurability(getMaxDurability() - value);
			}
		});
	}

	@Override
	public void onTick(float dt) {
		super.onTick(dt);

		// Regenerate durability
		float durability = getDurability();
		if (durability < getMaxDurability()) {
			setDurability(durability + getDurabilityRestoreRate() * dt);
		}
	}

	public float getMaxDurability() {
		return 40.0f;
	}

	/**
	 * Gets the rate in durability/second that durability is restored
	 * 
	 * @return Durability restore rate
	 */
	public float getDurabilityRestoreRate() {
		return 20.0f;
	}

	@Override
	public void onInteract(final EntityInteractEvent<?> event) {
		if (event instanceof PlayerInteractEntityEvent) {
			final PlayerInteractEntityEvent pie = (PlayerInteractEntityEvent) event;
			switch (pie.getAction()) {
				case LEFT_CLICK:
					damage(10.0f);
					if (getDurability() == 0.0f) {
						onDestroy();
						getOwner().remove();
					}
			}
		}
	}

	protected void onDestroy() {
		// Spawn drops (if applicable)
		DeathDrops deathDropsComp = getOwner().get(DeathDrops.class);
		if (deathDropsComp == null) {
			return;
		}
		List<ItemStack> drops = deathDropsComp.getDrops();
		Point entityPosition = getOwner().getPhysics().getPosition();
		for (ItemStack stack : drops) {
			if (stack != null) {
				Item.dropNaturally(entityPosition, stack);
			}
		}
	}

	public void damage(float damage) {
		setDurability(getDurability() - damage);
		if (damage > 0.0f) {
			getOwner().getNetwork().callProtocolEvent(new EntityStatusEvent(getOwner(), EntityStatusMessage.ENTITY_HURT));
		}
	}

	public float getDurability() {
		return getOwner().getData().get(VanillaData.DURABILITY);
	}

	public void setDurability(float durability) {
		//TODO: Add a Float-based clamp function to GenericMath
		float clampDurability = durability;
		if (durability < 0) {
			clampDurability = 0.0f;
		} else if (durability > getMaxDurability()) {
			clampDurability = getMaxDurability();
		}
		getOwner().getData().put(VanillaData.DURABILITY, clampDurability);
	}
}
