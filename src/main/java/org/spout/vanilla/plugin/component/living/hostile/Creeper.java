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
package org.spout.vanilla.plugin.component.living.hostile;

import com.bulletphysics.collision.shapes.BoxShape;

import org.spout.api.component.impl.PhysicsComponent;
import org.spout.api.inventory.ItemStack;

import org.spout.vanilla.api.component.Hostile;

import org.spout.vanilla.plugin.VanillaPlugin;
import org.spout.vanilla.plugin.component.living.Living;
import org.spout.vanilla.plugin.component.misc.DropComponent;
import org.spout.vanilla.plugin.component.misc.HealthComponent;
import org.spout.vanilla.plugin.material.VanillaMaterials;
import org.spout.vanilla.plugin.protocol.entity.creature.CreeperEntityProtocol;

/**
 * A component that identifies the entity as a Creeper.
 */
public class Creeper extends Living implements Hostile {
	private byte fuse = -1;
	private boolean charged = false;

	@Override
	public void onAttached() {
		super.onAttached();
		getOwner().getNetwork().setEntityProtocol(VanillaPlugin.VANILLA_PROTOCOL_ID, new CreeperEntityProtocol());
		PhysicsComponent physics = getOwner().add(PhysicsComponent.class);
		getOwner().add(DropComponent.class).addDrop(new ItemStack(VanillaMaterials.GUNPOWDER, getRandom().nextInt(2)));
		physics.setMass(2f);
		physics.setCollisionShape(new BoxShape(1F, 2F, 1F));
		physics.setFriction(10f);
		physics.setRestitution(0f);
		if (getAttachedCount() == 1) {
			getOwner().add(HealthComponent.class).setSpawnHealth(20);
		}
		
		//TODO: Does damage according to range.
	}

	public byte getFuse() {
		return fuse;
	}

	public void setFuse(byte fuse) {
		this.fuse = fuse;
	}

	public boolean isCharged() {
		return charged;
	}

	public void setCharged(boolean charged) {
		this.charged = charged;
	}
}
