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
package org.spout.vanilla.plugin.component.substance.object.vehicle;

import java.util.ArrayList;
import java.util.List;

import org.spout.api.entity.Entity;
import org.spout.api.entity.Player;
import org.spout.api.event.player.PlayerInteractEvent.Action;
import org.spout.api.geo.discrete.Point;
import org.spout.api.inventory.ItemStack;
import org.spout.api.util.Parameter;

import org.spout.vanilla.api.event.entity.EntityMetaChangeEvent;
import org.spout.vanilla.api.event.entity.EntityStatusEvent;

import org.spout.vanilla.plugin.component.misc.DropComponent;
import org.spout.vanilla.plugin.component.substance.object.Item;
import org.spout.vanilla.plugin.component.substance.object.ObjectEntity;
import org.spout.vanilla.plugin.material.VanillaMaterials;
import org.spout.vanilla.plugin.protocol.msg.entity.EntityStatusMessage;

public abstract class MinecartBase extends ObjectEntity {
	private int wobble = 0;

	@Override
	public void onAttached() {
		super.onAttached();
		if (getAttachedCount() == 1) {
			getOwner().add(DropComponent.class).addDrop(new ItemStack(VanillaMaterials.MINECART, 1));
		}
		getOwner().setSavable(true);
	}

	@Override
	public void onInteract(Action action, Entity source) {
		if (!(source instanceof Player)) {
			return;
		}

		if (wobble > 0) {
			wobble--;
		}
		if (Action.LEFT_CLICK.equals(action)) {
			wobble += 10;
			List<Parameter<?>> parameters = new ArrayList<Parameter<?>>();
			parameters.add(new Parameter<Integer>(Parameter.TYPE_INT, 17, wobble / 5)); // Unknown flag; initialized to 0. (Probably time since last collision)
			parameters.add(new Parameter<Integer>(Parameter.TYPE_INT, 19, wobble));
			getOwner().getNetwork().callProtocolEvent(new EntityMetaChangeEvent(getOwner(), parameters));
			getOwner().getNetwork().callProtocolEvent(new EntityStatusEvent(getOwner(), EntityStatusMessage.ENTITY_HURT));

			if (wobble > 40) {
				onDestroy();
				getOwner().remove();
			}
		}
	}

	protected void onDestroy() {
		List<ItemStack> drops = getOwner().get(DropComponent.class).getDrops();
		Point entityPosition = getOwner().getTransform().getPosition();
		for (ItemStack stack : drops) {
			if (stack != null) {
				Item.dropNaturally(entityPosition, stack);
			}
		}
	}
}
