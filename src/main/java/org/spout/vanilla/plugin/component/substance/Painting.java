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
package org.spout.vanilla.plugin.component.substance;

import java.util.List;

import org.spout.api.entity.Entity;
import org.spout.api.entity.Player;
import org.spout.api.event.player.PlayerInteractEvent.Action;
import org.spout.api.geo.discrete.Point;
import org.spout.api.inventory.ItemStack;
import org.spout.api.material.block.BlockFace;

import org.spout.vanilla.api.data.PaintingType;
import org.spout.vanilla.plugin.VanillaPlugin;
import org.spout.vanilla.plugin.component.VanillaComponent;
import org.spout.vanilla.plugin.component.misc.DropComponent;
import org.spout.vanilla.plugin.data.VanillaData;
import org.spout.vanilla.plugin.material.VanillaMaterials;
import org.spout.vanilla.plugin.protocol.entity.object.PaintingEntityProtocol;

public class Painting extends VanillaComponent {

	public Point originalPos;
	@Override
	public void onAttached() {
		super.onAttached();
		getOwner().getNetwork().setEntityProtocol(VanillaPlugin.VANILLA_PROTOCOL_ID, new PaintingEntityProtocol());
		if (getAttachedCount() == 1) {
			getOwner().add(DropComponent.class).addDrop(new ItemStack(VanillaMaterials.PAINTING, 1));
		}
		getOwner().setSavable(true);
	}

	public boolean isStatic() {
		return true;
	}
	
	public void onTick(float dt) {
	}
	
	public PaintingType getType() {
		return getOwner().getData().get(VanillaData.PAINTING_TYPE);
	}

	public void setType(PaintingType type) {
		getOwner().getData().put(VanillaData.PAINTING_TYPE, type);
	}

	public BlockFace getFace() {
		return getOwner().getData().get(VanillaData.PAINTING_FACE);
	}

	public void setFace(BlockFace face) {
		getOwner().getData().put(VanillaData.PAINTING_FACE, face);
	}

	public int getNativeFace() {
		return getNativeFace(getFace());
	}

	public static int getNativeFace(BlockFace face) {
		switch (face) {
			case NORTH:
				return 3;
			case SOUTH:
				return 1;
			case EAST:
				return 0;
			case WEST:
				return 2;
			default:
				return -1;
		}
	}
	
	@Override
	public void onInteract(Action action, Entity source) {
		if (!(source instanceof Player)) {
			return;
		}
		
		if (Action.LEFT_CLICK.equals(action)) {
			destroy();
		}
	}
	
	private void destroy() {
		List<ItemStack> drops = getOwner().get(DropComponent.class).getDrops();
		Point entityPosition = getOwner().getTransform().getPosition();
		for (ItemStack stack : drops) {
			if (stack != null) {
				Item.dropNaturally(entityPosition, stack);
			}
		}
		getOwner().remove();
	}
}
