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
package org.spout.vanilla.component.living;

import org.spout.api.component.impl.NavigationComponent;
import org.spout.api.component.impl.PhysicsComponent;
import org.spout.api.component.type.EntityComponent;
import org.spout.api.entity.Entity;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.discrete.Point;
import org.spout.api.material.BlockMaterial;

import org.spout.vanilla.ai.VanillaBlockExaminer;
import org.spout.vanilla.component.misc.DrowningComponent;
import org.spout.vanilla.component.misc.HeadComponent;
import org.spout.vanilla.component.misc.HealthComponent;
import org.spout.vanilla.data.VanillaData;
import org.spout.vanilla.material.block.Liquid;
import org.spout.vanilla.material.block.Solid;

public abstract class Living extends EntityComponent {
	@Override
	public void onAttached() {
		Entity holder = getOwner();
		holder.add(HeadComponent.class);
		holder.add(HealthComponent.class);
		holder.add(PhysicsComponent.class);
		holder.add(DrowningComponent.class);
		holder.add(NavigationComponent.class).setDefaultExaminers(new VanillaBlockExaminer());
		
		holder.setSavable(true);

		//Tracks the number of times this component has been attached (i.e how many times it's been saved, then loaded. 1 = fresh entity)
		holder.getData().put(VanillaData.ATTACHED_COUNT, getAttachedCount() + 1);
	}

	/**
	 * A counter of how many times this component has been attached to an entity
	 * <p/>
	 * Values > 1 indicate how many times this component has been saved to disk,
	 * and reloaded
	 * <p/>
	 * Values == 1 indicate a new component that has never been saved and loaded.
	 * 
	 * @return attached count
	 */
	public final int getAttachedCount() {
		return getOwner().getData().get(VanillaData.ATTACHED_COUNT);
	}

	public HeadComponent getHead() {
		return getOwner().get(HeadComponent.class);
	}

	public HealthComponent getHealth() {
		return getOwner().get(HealthComponent.class);
	}

	public PhysicsComponent getPhysics() {
		return getOwner().get(PhysicsComponent.class);
	}

	@Override
	public void onCollided(Point colliderPoint, Point collidedPoint, Block block) {
		if (getPhysics() == null) {
			return;
		}
		if (block.getMaterial() instanceof Solid) {
			getPhysics().setDamping(1f, 1f);
		} else if (block.getMaterial() instanceof Liquid) {
			getPhysics().setDamping(0.8f, 0.8f);
		}
	}
}
