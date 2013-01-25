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

import org.spout.api.component.impl.SceneComponent;
import org.spout.api.inventory.ItemStack;
import org.spout.api.util.Parameter;

import org.spout.vanilla.api.component.Hostile;
import org.spout.vanilla.plugin.VanillaPlugin;
import org.spout.vanilla.plugin.component.living.Living;
import org.spout.vanilla.plugin.component.misc.DropComponent;
import org.spout.vanilla.plugin.component.misc.HealthComponent;
import org.spout.vanilla.plugin.data.VanillaData;
import org.spout.vanilla.plugin.material.VanillaMaterials;
import org.spout.vanilla.plugin.protocol.entity.creature.CreeperEntityProtocol;

/**
 * A component that identifies the entity as a Creeper.
 */
public class Creeper extends Living implements Hostile {
	@Override
	public void onAttached() {
		super.onAttached();
		getOwner().getNetwork().setEntityProtocol(VanillaPlugin.VANILLA_PROTOCOL_ID, new CreeperEntityProtocol());
		SceneComponent scene = getOwner().getScene();
		getOwner().add(DropComponent.class).addDrop(new ItemStack(VanillaMaterials.GUNPOWDER, getRandom().nextInt(2))).addXpDrop((short) 5);
		scene.setShape(2f, new BoxShape(1F, 2F, 1F));
		scene.setFriction(10f);
		scene.setRestitution(0f);
		if (getAttachedCount() == 1) {
			getOwner().add(HealthComponent.class).setSpawnHealth(20);
		}

		//TODO: Does damage according to range.
	}

	public byte getFuse() {
		return getData().get(VanillaData.CREEPER_FUSE);
	}

	public void setFuse(byte fuse) {
		getData().put(VanillaData.CREEPER_FUSE, fuse);
		setMetadata(new Parameter<Byte>(Parameter.TYPE_BYTE, 16, fuse));
	}

	public boolean isCharged() {
		return getData().get(VanillaData.CHARGED);
	}

	public void setCharged(boolean charged) {
		getData().put(VanillaData.CHARGED, charged);
		setMetadata(new Parameter<Byte>(Parameter.TYPE_BYTE, 17, charged ? (byte) 1 : 0));
	}
}
