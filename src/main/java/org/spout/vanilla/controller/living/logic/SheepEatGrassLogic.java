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
package org.spout.vanilla.controller.living.logic;

import static org.spout.vanilla.util.VanillaNetworkUtil.broadcastPacket;

import java.util.Random;

import org.spout.api.math.Vector3;
import org.spout.api.tickable.LogicPriority;
import org.spout.api.tickable.LogicRunnable;
import org.spout.vanilla.controller.living.creature.passive.Sheep;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.protocol.msg.EntityStatusMessage;

/**
 * Logic for Sheeps eating grass.
 */
public class SheepEatGrassLogic extends LogicRunnable<Sheep> {

	private static final int CHANCE_AS_ADULT = 1000;
	private static final int CHANCE_AS_BABY = 50;

	/**
	 * Create a LogicRunnable for Sheeps eating grass with normal priority.
	 * 
	 * @param parent the sheep eating grass.
	 */
	public SheepEatGrassLogic(Sheep parent) {
		this(parent, LogicPriority.NORMAL);
	}

	/**
	 * Create a LogicRunnable for Sheeps eating grass.
	 * 
	 * @param parent the sheep eating grass.
	 * @param priority the priority of this logic. 
	 */
	public SheepEatGrassLogic(Sheep parent, LogicPriority priority) {
		super(parent, priority);
	}

	@Override
	public void run() {
		final Vector3 position = parent.getPreviousPosition();
		final int x = position.getFloorX();
		final int y = position.getFloorY() - 1;
		final int z = position.getFloorZ();
		if (isBlockEatableTallGrass(x, y, z)) {
			parent.getParent().getWorld().setBlockMaterial(x, y, z, VanillaMaterials.AIR, (short) 0, parent.getParent());
			onGrassEaten();
		} else if (parent.getParent().getWorld().getBlockMaterial(x, y, z) == VanillaMaterials.GRASS) {
			parent.getParent().getWorld().setBlockMaterial(x, y, z, VanillaMaterials.DIRT, (short) 0, parent.getParent());
			onGrassEaten();
		}
	}

	/**
	 * Is called when the sheep eats grass. Applies the bonuses to the sheep.
	 */
	private void onGrassEaten() {
		broadcastPacket(new EntityStatusMessage(parent.getParent().getId(), EntityStatusMessage.SHEEP_EAT_GRASS));
		parent.setSheared(false);
		long newTimeUntilAdult = parent.getTimeUntilAdult() - 1200;
		if (newTimeUntilAdult < 0) {
			newTimeUntilAdult = 0;
		}
		parent.setTimeUntilAdult(newTimeUntilAdult);
	}

	@Override
	public boolean shouldRun(float dt) {
		final Random random = parent.getRandom();
		final Vector3 position = parent.getPreviousPosition();
		final int x = position.getFloorX();
		final int y = position.getFloorY() - 1;
		final int z = position.getFloorZ();
		int maxInt = 0;
		if (parent.isBaby()) {
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
		return parent.getParent().getWorld().getBlockMaterial(x, y, z) == VanillaMaterials.GRASS;

	}

	private boolean isBlockEatableTallGrass(int x, int y, int z) {
		return parent.getParent().getWorld().getBlockMaterial(x, y, z) == VanillaMaterials.TALL_GRASS && parent.getParent().getWorld().getBlockData(x, y, z) == 1;
	}

}
