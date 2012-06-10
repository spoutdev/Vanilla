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
package org.spout.vanilla.controller.action;

import org.spout.api.entity.Entity;
import org.spout.api.entity.component.controller.action.EntityAction;
import org.spout.api.geo.LoadOption;
import org.spout.api.geo.discrete.Point;
import org.spout.api.material.BlockMaterial;

import org.spout.vanilla.controller.VanillaActionController;

public class GravityAction extends EntityAction<VanillaActionController> {
	@Override
	public boolean shouldRun(Entity entity, VanillaActionController controller) {
		Point future = entity.getPosition().add(controller.getVelocity());
		//Non observers entities should not be loading chunks
		if (!entity.isObserver() && future.getWorld().getChunkFromBlock(future, LoadOption.NO_LOAD) == null) {
			return false;
		}
		BlockMaterial block = entity.getWorld().getBlock(future).getMaterial();
		return !block.isSolid();
	}

	@Override
	public void run(Entity entity, VanillaActionController controller, float dt) {
		controller.setVelocity(controller.getVelocity().subtract(0, 0.04f, 0));
		controller.move();
	}
}
