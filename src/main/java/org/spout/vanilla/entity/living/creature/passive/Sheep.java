/*
 * This file is part of Vanilla (http://www.spout.org/).
 *
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
package org.spout.vanilla.entity.living.creature.passive;

import java.util.HashSet;
import java.util.Set;

import org.spout.api.inventory.ItemStack;
import org.spout.api.material.Material;
import org.spout.api.protocol.EntityProtocol;
import org.spout.api.protocol.EntityProtocolStore;
import org.spout.vanilla.entity.Entity;
import org.spout.vanilla.entity.living.Creature;
import org.spout.vanilla.entity.living.creature.Passive;
import org.spout.vanilla.material.block.Wool;

public class Sheep extends Creature implements Passive {
	private int countdown = 0;
	private int color;
	private static EntityProtocolStore entityProtocolStore = new EntityProtocolStore();

	public Sheep() {
		this(0x0);
	}

	public Sheep(Wool.Color color) {
		this(color.getData());
	}

	public Sheep(int color) {
		super();
		this.color = color;
	}

	@Override
	public void onAttached() {
		super.onAttached();
		getParent().setData(Entity.KEY, Entity.Sheep.id);
		getParent().setData("SheepSheared", false);
		getParent().setData("SheepColor", color);
	}

	@Override
	public EntityProtocol getEntityProtocol(int protocolId) {
		return entityProtocolStore.getEntityProtocol(protocolId);
	}

	public static void setEntityProtocol(int protocolId, EntityProtocol protocol) {
		entityProtocolStore.setEntityProtocol(protocolId, protocol);
	}

	@Override
	public void onTick(float dt) {
		if (--countdown <= 0) {
			countdown = getRandom().nextInt(7) + 3;
			float x = (getRandom().nextBoolean() ? 1 : -1) * getRandom().nextFloat();
			float y = getRandom().nextFloat();
			float z = (getRandom().nextBoolean() ? 1 : -1) * getRandom().nextFloat();
			getParent().translate(x, y, z);
		}

		super.onTick(dt);
	}

	public boolean isSheared() {
		return getParent().getData("SheepSheared").asBool();
	}

	public void setSheared(boolean sheared) {
		getParent().setData("SheepSheared", sheared);
	}

	public int getColor() {
		return getParent().getData("SheepColor").asInt();
	}

	public void setColor() {
		getParent().setData("SheepColor", getRandom());
	}

	@Override
	public Set<ItemStack> getDeathDrops() {
		Set<ItemStack> drops = new HashSet<ItemStack>();

		if (!isSheared()) {
			drops.add(new ItemStack(Material.get((short) 35), (short) getColor(), 1));
		}

		return drops;
	}
}
