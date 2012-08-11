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
package org.spout.vanilla.entity.component.physics;

import org.spout.api.entity.BasicComponent;
import org.spout.api.entity.Controller;
import org.spout.api.entity.Entity;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.discrete.Point;
import org.spout.api.material.BlockMaterial;
import org.spout.api.tickable.TickPriority;

import org.spout.vanilla.material.VanillaBlockMaterial;
import org.spout.vanilla.material.VanillaMaterials;

/**
 * Plays a step sound effect while walking
 */
public class StepSoundComponent extends BasicComponent<Controller> {
	private static final float SOUND_VOLUME = 0.15f;
	private static final float SOUND_DISTANCE_INTERVAL = 1.667f;
	private float walkedDistance = 0.0f;
	private Point previousPosition;

	public StepSoundComponent(TickPriority priority) {
		super(priority);
	}

	@Override
	public void onAttached() {
		previousPosition = getParent().getParent().getPosition();
	}

	@Override
	public boolean canTick() {
		Point pos = getParent().getParent().getPosition();
		walkedDistance += pos.distance(this.previousPosition);
		this.previousPosition = pos;
		return this.walkedDistance >= SOUND_DISTANCE_INTERVAL;
	}

	@Override
	public void onTick(float dt) {
		Entity parent = getParent().getParent();
		Point pos = parent.getPosition();

		// check ground block and play sound
		Block ground = parent.getWorld().getBlock(pos.subtract(0.0, 0.2, 0.0), parent);
		BlockMaterial groundMat = ground.getMaterial();
		if (!groundMat.isMaterial(VanillaMaterials.AIR) && groundMat instanceof VanillaBlockMaterial) {
			this.walkedDistance = 0.0f; // reset
			((VanillaBlockMaterial) groundMat).getStepSound().playGlobal(pos, SOUND_VOLUME, 1.0f);
		}
	}
}
