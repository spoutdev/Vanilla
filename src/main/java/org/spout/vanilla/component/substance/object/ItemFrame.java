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
package org.spout.vanilla.component.substance.object;

import java.util.ArrayList;
import java.util.List;

import org.spout.api.entity.Entity;
import org.spout.api.event.player.PlayerInteractEvent.Action;
import org.spout.api.geo.discrete.Point;
import org.spout.api.inventory.ItemStack;
import org.spout.api.inventory.Slot;
import org.spout.api.material.Material;
import org.spout.api.material.block.BlockFace;
import org.spout.api.util.Parameter;

import org.spout.vanilla.VanillaPlugin;
import org.spout.vanilla.event.entity.EntityMetaChangeEvent;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.protocol.entity.object.ItemFrameProtocol;
import org.spout.vanilla.util.PlayerUtil;

public class ItemFrame extends ObjectEntity {
	private Material material;
	private BlockFace orientation;

	@Override
	public void onAttached() {
		getOwner().getNetwork().setEntityProtocol(VanillaPlugin.VANILLA_PROTOCOL_ID, new ItemFrameProtocol());
	}

	@Override
	public void onInteract(Action action, Entity source) {
		Entity entity = getOwner();
		switch (action) {
			case LEFT_CLICK:
				Point pos = entity.getScene().getPosition();
				Item.dropNaturally(pos, new ItemStack(VanillaMaterials.ITEM_FRAME, 1));
				if(material != null){
					Item.dropNaturally(pos, new ItemStack(material, 1));
				}
				entity.remove();
				break;
			case RIGHT_CLICK:
				Slot slot = PlayerUtil.getHeldSlot(source);
				if (slot != null && slot.get() != null) {
					setMaterial(slot.get().getMaterial());
					slot.addAmount(-1);
				}
				break;
		}
	}

	public void setMaterial(Material material) {
		this.material = material;
		List<Parameter<?>> params = new ArrayList<Parameter<?>>();
		params.add(new Parameter<ItemStack>(Parameter.TYPE_ITEM, 2, material == null ? null : new ItemStack(material, 1)));
		getOwner().getNetwork().callProtocolEvent(new EntityMetaChangeEvent(getOwner(), params));
	}

	public Material getMaterial() {
		return material;
	}

	public void setOrientation(BlockFace orientation) {
		if (orientation == BlockFace.BOTTOM || orientation == BlockFace.TOP || orientation == BlockFace.THIS) {
			throw new IllegalArgumentException("Specified orientation must be NESW");
		}
		this.orientation = orientation;
	}

	public BlockFace getOrientation() {
		return orientation;
	}
}
