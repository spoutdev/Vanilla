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
package org.spout.vanilla.component.substance.object.vehicle;

import java.util.ArrayList;
import java.util.List;

import org.spout.api.entity.Entity;
import org.spout.api.event.player.PlayerInteractEvent.Action;
import org.spout.api.inventory.ItemStack;
import org.spout.api.material.block.BlockFace;
import org.spout.api.util.Parameter;

import org.spout.vanilla.VanillaPlugin;
import org.spout.vanilla.component.substance.object.Item;
import org.spout.vanilla.component.substance.object.ObjectEntity;
import org.spout.vanilla.event.entity.EntityMetaChangeEvent;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.protocol.ChannelBufferUtils;
import org.spout.vanilla.protocol.entity.object.vehicle.BoatObjectEntityProtocol;

public class Boat extends ObjectEntity {
	private int timeSinceLastHit = 0;
	private BlockFace direction = BlockFace.NORTH;
	private int damageTaken = 0;

	@Override
	public void onTick(float dt) {
		timeSinceLastHit++;
	}

	@Override
	public void onInteract(Action action, Entity source) {
		if (action == Action.LEFT_CLICK) {
			timeSinceLastHit = 0;
			damage(10);
			if (damageTaken > 40) {
				Entity entity = getOwner();
				Item.dropNaturally(entity.getScene().getPosition(), new ItemStack(VanillaMaterials.BOAT, 1));
				entity.remove();
			}
		}
	}

	@Override
	public void onAttached() {
		getOwner().getNetwork().setEntityProtocol(VanillaPlugin.VANILLA_PROTOCOL_ID, new BoatObjectEntityProtocol());
		super.onAttached();
	}

	public void setDamageTaken(int damageTaken) {
		this.damageTaken = damageTaken;
		List<Parameter<?>> params = new ArrayList<Parameter<?>>();
		params.add(new Parameter<Integer>(Parameter.TYPE_INT, 19, damageTaken));
		getOwner().getNetwork().callProtocolEvent(new EntityMetaChangeEvent(getOwner(), params));
	}

	public int getDamageTaken() {
		return damageTaken;
	}

	public void damage(int amount) {
		setDamageTaken(getDamageTaken() + amount);
	}

	public int getTimeSinceLastHit() {
		return timeSinceLastHit;
	}

	public BlockFace getForwardDirection() {
		return direction;
	}

	public void setForwardDirection(BlockFace direction) {
		this.direction = direction;
		List<Parameter<?>> params = new ArrayList<Parameter<?>>();
		params.add(new Parameter<Integer>(Parameter.TYPE_INT, 18, (int) ChannelBufferUtils.getNativeDirection(direction)));
		getOwner().getNetwork().callProtocolEvent(new EntityMetaChangeEvent(getOwner(), params));
	}
}
