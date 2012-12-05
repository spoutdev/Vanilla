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
package org.spout.vanilla.component.substance.object;

import org.spout.api.collision.CollisionStrategy;
import org.spout.api.component.components.EntityComponent;
import org.spout.api.geo.World;
import org.spout.api.geo.discrete.Point;
import org.spout.api.material.BlockMaterial;
import org.spout.api.math.MathHelper;
import org.spout.vanilla.VanillaPlugin;
import org.spout.vanilla.material.VanillaBlockMaterial;
import org.spout.vanilla.protocol.entity.object.FallingBlockProtocol;
import org.spout.vanilla.protocol.entity.object.ObjectType;

public class FallingBlock extends EntityComponent {
	private static float FALL_INCREMENT = -0.04F;
	private static float FALL_MULTIPLIER = 0.98F;
	private VanillaBlockMaterial material;
	private float fallSpeed = 0F;

	@Override
	public void onAttached() {
		getOwner().getNetwork().setEntityProtocol(VanillaPlugin.VANILLA_PROTOCOL_ID, new FallingBlockProtocol(ObjectType.FALLING_OBJECT));
	}

	public void setMaterial(VanillaBlockMaterial material) {
		this.material = material;
	}

	public VanillaBlockMaterial getMaterial() {
		return material;
	}

	@Override
	public void onTick(float dt) {
		Point pos = this.getOwner().getTransform().getPosition();
		World world = pos.getWorld();
		int x = pos.getBlockX();
		int y = pos.getBlockY();
		int z = pos.getBlockZ();
		int fallAmt = Math.max(1, MathHelper.floor(Math.abs(fallSpeed)));
		BlockMaterial material = world.getBlockMaterial(x, y - fallAmt, z);
		while (isObstacle(material)) {
			//Find a place for the block
			BlockMaterial above = pos.getWorld().getBlockMaterial(x, y - fallAmt + 1, z);
			if (!isObstacle(above)) {
				pos.getWorld().setBlockMaterial(x, y - fallAmt + 1,z, getMaterial(), getMaterial().getData(), getMaterial().toCause(pos));
				this.getOwner().remove();
				break;
			} else {
				material = above;
				fallAmt--;
			}
		}
		if (!this.getOwner().isRemoved()) {
			fallSpeed += (FALL_INCREMENT * dt * 20);
			this.getOwner().getTransform().setPosition(pos.add(0, fallSpeed, 0F));
			fallSpeed *= FALL_MULTIPLIER;
		}
	}

	public float getFallingSpeed() {
		return fallSpeed;
	}

	private boolean isObstacle(BlockMaterial material) {
		if (material == BlockMaterial.AIR) {
			return false;
		}
		if (material.getCollisionModel().getStrategy() != CollisionStrategy.SOLID) {
			return false;
		}
		if (material instanceof VanillaBlockMaterial) {
			VanillaBlockMaterial vbm = (VanillaBlockMaterial)material;
			if (!vbm.isPlacementObstacle()) {
				return false;
			}
		}
		return true;
	}
}
