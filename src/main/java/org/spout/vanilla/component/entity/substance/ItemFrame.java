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
package org.spout.vanilla.component.entity.substance;

import java.util.ArrayList;
import java.util.List;

import org.spout.api.event.entity.EntityInteractEvent;
import org.spout.api.event.player.PlayerInteractEntityEvent;
import org.spout.api.geo.discrete.Point;
import org.spout.api.inventory.ItemStack;
import org.spout.api.inventory.Slot;
import org.spout.api.material.Material;
import org.spout.api.material.block.BlockFace;
import org.spout.api.util.Parameter;

import org.spout.vanilla.component.entity.misc.MetadataComponent;
import org.spout.vanilla.data.Metadata;
import org.spout.vanilla.event.entity.network.EntityMetaChangeEvent;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.protocol.entity.object.ItemFrameProtocol;
import org.spout.vanilla.util.PlayerUtil;

public class ItemFrame extends Substance {
	private Material material;
	private BlockFace orientation;

	@Override
	public void onAttached() {
		setEntityProtocol(new ItemFrameProtocol());

		// Add metadata for the displayed item
		getOwner().add(MetadataComponent.class).addMeta(new Metadata<ItemStack>(Metadata.TYPE_ITEM, 2) {
			private ItemStack lastItem = null;

			@Override
			public ItemStack getValue() {
				Material mat = getMaterial();
				if (mat == null) {
					return null;
				}
				// Re-use last item to avoid the creation of too many items
				// If material changes, create a new item to break the reference
				if (lastItem == null || !lastItem.isMaterial(mat)) {
					lastItem = new ItemStack(mat, 1);
				}
				return lastItem;
			}

			@Override
			public void setValue(ItemStack value) {
				setMaterial(value == null ? null : value.getMaterial());
			}
		});
	}

	@Override
	public void onInteract(final EntityInteractEvent event) {
		if (event instanceof PlayerInteractEntityEvent) {
			final PlayerInteractEntityEvent pie = (PlayerInteractEntityEvent) event;
			switch (pie.getAction()) {
				case LEFT_CLICK:
					Point pos = getOwner().getPhysics().getPosition();
					Item.dropNaturally(pos, new ItemStack(VanillaMaterials.ITEM_FRAME, 1));
					if (material != null) {
						Item.dropNaturally(pos, new ItemStack(material, 1));
					}
					getOwner().remove();
					break;
				case RIGHT_CLICK:
					Slot slot = PlayerUtil.getHeldSlot(getOwner());
					if (slot != null && slot.get() != null) {
						setMaterial(slot.get().getMaterial());
						slot.addAmount(-1);
					}
					break;
			}
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
