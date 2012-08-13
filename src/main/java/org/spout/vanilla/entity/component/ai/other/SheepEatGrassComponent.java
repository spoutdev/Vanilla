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
package org.spout.vanilla.entity.component.ai.other;

import java.util.Random;

import org.spout.api.entity.BasicComponent;
import org.spout.api.math.Vector3;
import org.spout.api.tickable.TickPriority;

import org.spout.vanilla.entity.creature.passive.Sheep;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.protocol.msg.entity.EntityStatusMessage;

import static org.spout.vanilla.util.VanillaNetworkUtil.broadcastPacket;

/**
 * Sheep component for eating grass
 */
public class SheepEatGrassComponent extends BasicComponent<Sheep> {
	private static final int CHANCE_AS_ADULT = 1000;
	private static final int CHANCE_AS_BABY = 50;

	/**
	 * Creates a Component for Sheeps eating grass.
	 * @param priority the priority of this component.
	 */
	public SheepEatGrassComponent(TickPriority priority) {
		super(priority);
	}

	@Override
	public boolean canTick() {
		final Random random = getParent().getRandom();
		final Vector3 position = getParent().getPreviousPosition();
		final int x = position.getFloorX();
		final int y = position.getFloorY() - 1;
		final int z = position.getFloorZ();
		int maxInt;
		if (getParent().getGrowing().isBaby()) {
			maxInt = CHANCE_AS_BABY;
		} else {
			maxInt = CHANCE_AS_ADULT;
		}
		if (random.nextInt(maxInt) != 0) {
			return false;
		}
		if (isBlockEatableTallGrass(x, y, z)) {
			return true;
		}
		return getParent().getParent().getWorld().getBlockMaterial(x, y, z) == VanillaMaterials.GRASS;
	}

	@Override
	public void onTick(float dt) {
		final Vector3 position = getParent().getParent().getLastTransform().getPosition();
		final int x = position.getFloorX();
		final int y = position.getFloorY() - 1;
		final int z = position.getFloorZ();
		if (isBlockEatableTallGrass(x, y, z)) {
			getParent().getParent().getWorld().setBlockMaterial(x, y, z, VanillaMaterials.AIR, (short) 0, getParent().getParent());
			onGrassEaten();
		} else if (getParent().getParent().getWorld().getBlockMaterial(x, y, z) == VanillaMaterials.GRASS) {
			getParent().getParent().getWorld().setBlockMaterial(x, y, z, VanillaMaterials.DIRT, (short) 0, getParent().getParent());
			onGrassEaten();
		}
	}

	/**
	 * Is called when the sheep eats grass. Applies the bonuses to the sheep.
	 */
	private void onGrassEaten() {
		broadcastPacket(new EntityStatusMessage(getParent().getParent().getId(), EntityStatusMessage.SHEEP_EAT_GRASS));
		getParent().setSheared(false);
		long newTimeUntilAdult = getParent().getGrowing().getTimeUntilAdult() - 1200;
		if (newTimeUntilAdult < 0) {
			newTimeUntilAdult = 0;
		}
		getParent().getGrowing().setTimeUntilAdult(newTimeUntilAdult);
	}

	private boolean isBlockEatableTallGrass(int x, int y, int z) {
		return getParent().getParent().getWorld().getBlockMaterial(x, y, z) == VanillaMaterials.TALL_GRASS && getParent().getParent().getWorld().getBlockData(x, y, z) == 1;
	}
}
