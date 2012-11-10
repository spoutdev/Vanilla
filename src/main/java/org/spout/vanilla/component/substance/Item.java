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
package org.spout.vanilla.component.substance;

import org.spout.api.component.components.EntityComponent;
import org.spout.api.component.components.PhysicsComponent;
import org.spout.api.data.Data;
import org.spout.api.entity.Entity;
import org.spout.api.geo.LoadOption;
import org.spout.api.geo.discrete.Point;
import org.spout.api.inventory.ItemStack;
import org.spout.api.math.Vector3;

import org.spout.vanilla.VanillaPlugin;
import org.spout.vanilla.data.VanillaData;
import org.spout.vanilla.protocol.entity.object.ItemEntityProtocol;

import com.bulletphysics.collision.shapes.BoxShape;

public class Item extends EntityComponent {
	@Override
	public void onAttached() {
		getOwner().getNetwork().setEntityProtocol(VanillaPlugin.VANILLA_PROTOCOL_ID, new ItemEntityProtocol());
		getOwner().add(PhysicsComponent.class).setCollisionShape(new BoxShape(0.25F, 0.25F, 0.25F));
	}

	@Override
	public boolean canTick() {
		return true;
	}

	@Override
	public void onTick(float dt) {
		if (getUncollectableTicks() > 0) {
			setUncollectableTicks(getUncollectableTicks() - 1);
		}
	}

	public ItemStack getItemStack() {
		return getData().get(Data.HELD_ITEM);
	}

	public void setItemStack(ItemStack stack) {
		getData().put(Data.HELD_ITEM, stack);
	}

	public int getUncollectableTicks() {
		return getData().get(VanillaData.UNCOLLECTABLE_TICKS);
	}

	public void setUncollectableTicks(int uncollectableTicks) {
		getData().put(VanillaData.UNCOLLECTABLE_TICKS, uncollectableTicks);
	}

	public boolean canBeCollected() {
		return getUncollectableTicks() <= 0;
	}

	public PhysicsComponent getPhysics() {
		return getOwner().get(PhysicsComponent.class);
	}

	/**
	 * Drops an item at the position with the item stack specified with a natural random velocity
	 * @param position to spawn the item
	 * @param itemStack to set to the item
	 * @return the Item entity
	 */
	public static Item dropNaturally(Point position, ItemStack itemStack) {
		Vector3 velocity = new Vector3(Math.random() * 0.2 - 0.1, 0.2, Math.random() * 0.2 - 0.1);
		return drop(position, itemStack, velocity);
	}

	/**
	 * Drops an item at the position with the item stack specified
	 * @param position to spawn the item
	 * @param itemStack to set to the item
	 * @param velocity to drop at
	 * @return the Item entity
	 */
	public static Item drop(Point position, ItemStack itemStack, Vector3 velocity) {
		Entity entity = position.getWorld().createEntity(position, Item.class);
		Item item = entity.add(Item.class);
		item.setItemStack(itemStack);
		item.getPhysics().setLinearVelocity(velocity);
		if (position.getChunk(LoadOption.NO_LOAD) != null) {
			position.getWorld().spawnEntity(entity);
		}
		return item;
	}
}
