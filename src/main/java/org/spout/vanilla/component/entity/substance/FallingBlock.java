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

import org.spout.api.component.entity.PhysicsComponent;
import org.spout.api.event.entity.EntityCollideBlockEvent;
import org.spout.api.event.entity.EntityCollideEvent;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.discrete.Point;
import org.spout.api.inventory.ItemStack;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.block.BlockFace;

import org.spout.vanilla.VanillaPlugin;
import org.spout.vanilla.protocol.entity.object.FallingBlockProtocol;
import org.spout.vanilla.protocol.entity.object.ObjectType;

public class FallingBlock extends Substance {
	private BlockMaterial material;

	@Override
	public void onAttached() {
		getOwner().getNetwork().setEntityProtocol(VanillaPlugin.VANILLA_PROTOCOL_ID, new FallingBlockProtocol(ObjectType.FALLING_OBJECT));
	}

	public void setMaterial(BlockMaterial material) {
		if (material == null) {
			throw new IllegalArgumentException("Cannot set a null material for the FallingBlock");
		}
		if (material.equals(this.material)) {
			return;
		}
		this.material = material;
		//Physics
		PhysicsComponent physics = getOwner().getPhysics();
		physics.activate(material.getMass(), material.getShape(), false, true);
		physics.setFriction(material.getFriction());
		physics.setRestitution(material.getRestitution());
	}

	public BlockMaterial getMaterial() {
		return material;
	}

	@Override
	public void onCollided(EntityCollideEvent event) {
		if (event instanceof EntityCollideBlockEvent) {
			final EntityCollideBlockEvent collideBlockEvent = (EntityCollideBlockEvent) event;
			final Point point = new Point(collideBlockEvent.getContactInfo().getNormal(), getOwner().getWorld());
			final int x = point.getBlockX();
			final int y = point.getBlockX();
			final int z = point.getBlockX();
			final Block translated = getOwner().getWorld().getBlock(x, y, z).translate(BlockFace.TOP);
			final BlockMaterial material = translated.getMaterial();
			if (material.isPlacementObstacle()) {
				Item.dropNaturally(point, new ItemStack(getMaterial(), 1));
			} else {
				translated.setMaterial(getMaterial(), getMaterial().toCause(point));
			}
			getOwner().remove();
		}
	}
}
