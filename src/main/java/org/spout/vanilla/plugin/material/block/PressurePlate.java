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
package org.spout.vanilla.plugin.material.block;

import org.spout.api.collision.CollisionStrategy;
import org.spout.api.entity.Entity;
import org.spout.api.event.Cause;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.discrete.Point;
import org.spout.api.material.DynamicMaterial;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.range.CubicEffectRange;
import org.spout.api.material.range.CuboidEffectRange;
import org.spout.api.material.range.EffectRange;
import org.spout.api.material.range.ListEffectRange;

import org.spout.vanilla.api.data.RedstonePowerMode;

import org.spout.vanilla.plugin.data.effect.store.GeneralEffects;

import com.bulletphysics.collision.shapes.BoxShape;

public abstract class PressurePlate extends AttachedRedstoneSource implements DynamicMaterial {
	public static final int TICK_DELAY = 1000;
	private static final EffectRange physicsRange = new ListEffectRange(new CubicEffectRange(1), new CuboidEffectRange(0, -2, 0, 0, -1, 0));

	public PressurePlate(String name, int id, String model) {
		super(name, id, model);
		this.setAttachable(BlockFace.BOTTOM).setHardness(0.5F).setResistance(0.8F).setTransparent();
		this.setCollision(CollisionStrategy.SOLID);
		this.setCollisionShape(new BoxShape(1.0f, 0.03125f, 1.0f));
		//TODO: Create the box model and different up/down collision shapes
	    // UP height = 0.0625F
	    // DOWN height = 0.03125F
	}

	/**
	 * Gets whether this pressure plate is pressed down
	 * @param block to get it of
	 * @return True if pressed down, False if not
	 */
	public boolean isPressed(Block block) {
		return block.isDataBitSet(0x1);
	}

	@Override
	public void setAttachedFace(Block block, BlockFace attachedFace, Cause<?> cause) {
	}

	@Override
	public BlockFace getAttachedFace(short data) {
		return BlockFace.BOTTOM;
	}

	@Override
	public short getRedstonePowerStrength(short data) {
		return ((data & 0x1) == 1) ? REDSTONE_POWER_MAX : REDSTONE_POWER_MIN;
	}

	/**
	 * Sets whether this pressure plate is pressed down
	 * @param block to set it of
	 * @param pressed whether it is pressed
	 */
	public void setPressed(Block block, boolean pressed) {
		if (this.isPressed(block) != pressed) {
			block.setDataBits(0x1, pressed);
			GeneralEffects.BLOCK_PRESS.playGlobal(block.getPosition(), pressed);
		}
		block.resetDynamic();
	}

	@Override
	public void onCollided(Point colliderPoint, Point collidedPoint, Entity entity) {
		super.onCollided(colliderPoint, collidedPoint, entity);
		if (this.canTrigger(entity)) {
			this.setPressed(collidedPoint.getBlock(), true);
		}
	}

	/**
	 * Checks whether a given entity can trigger this pressure plate
	 * @param entity to check for
	 * @return True if the pressure plate is triggered, False if not
	 */
	public abstract boolean canTrigger(Entity entity);

	@Override
	public short getRedstonePower(Block block, RedstonePowerMode powerMode) {
		return this.isPressed(block) ? REDSTONE_POWER_MAX : REDSTONE_POWER_MIN;
	}

	@Override
	public EffectRange getPhysicsRange(short data) {
		return physicsRange;
	}

	@Override
	public EffectRange getDynamicRange() {
		return EffectRange.THIS;
	}

	@Override
	public void onFirstUpdate(Block block, long currentTime) {
		block.dynamicUpdate(currentTime + TICK_DELAY, true);
	}

	@Override
	public void onDynamicUpdate(Block block, long updateTime, int data) {
		if (!this.isPressed(block)) {
			block.dynamicUpdate(updateTime + TICK_DELAY, true);
			return;
		}

		//TODO: Check if an entity is on top and don't undo it then
		this.setPressed(block, false);
	}
}
