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
package org.spout.vanilla.component.living.neutral;

import javax.vecmath.Vector3f;

import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.dynamics.RigidBody;

import org.spout.api.math.MathHelper;

import org.spout.vanilla.VanillaPlugin;
import org.spout.vanilla.component.living.Neutral;
import org.spout.vanilla.component.living.VanillaEntity;
import org.spout.vanilla.component.misc.VanillaPhysicsComponent;
import org.spout.vanilla.protocol.entity.living.EndermanEntityProtocol;

/**
 * A component that identifies the entity as an Enderman.
 */
public class Enderman extends VanillaEntity implements Neutral {
	@Override
	public void onAttached() {
		super.onAttached();
		getHolder().getNetwork().setEntityProtocol(VanillaPlugin.VANILLA_PROTOCOL_ID, new EndermanEntityProtocol());
		VanillaPhysicsComponent physics = getHolder().add(VanillaPhysicsComponent.class);
		physics.setCollisionShape(new BoxShape(MathHelper.toVector3f(0.75f, 3f, 0.75f)));
		physics.setMass(10f);
		Vector3f inertia = new Vector3f();
		physics.getCollisionShape().calculateLocalInertia(physics.getMass(), inertia);
		physics.setCollisionObject(new RigidBody(physics.getMass(), physics.getMotionState(), physics.getCollisionShape(), inertia));
	}
}
