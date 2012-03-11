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
package org.spout.vanilla.entity;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.spout.api.entity.Entity;
import org.spout.api.geo.World;
import org.spout.api.geo.discrete.Point;
import org.spout.api.inventory.ItemStack;
import org.spout.vanilla.entity.object.Item;

public abstract class LivingEntity extends VanillaEntity {
	private Entity parent;
	private Random rand = new Random();
	private World prevWorld;
	private Point prevPoint;

	public Set<ItemStack> getDeathDrops() {
		return new HashSet<ItemStack>();
	}

	@Override
	public void onAttached() {
		parent = getParent();
	}

	@Override
	public void onTick(float dt) {
		if (parent.getWorld() != null) {
			prevWorld = parent.getWorld();
		}
		if (parent.getPosition() != null) {
			prevPoint = parent.getPosition();
		}

		super.onTick(dt);
	}

	@Override
	public void onDeath() {
		Set<ItemStack> drops = getDeathDrops();
		for (ItemStack drop : drops) {
			Item item = new Item(drop, prevPoint.normalize());
			prevWorld.createAndSpawnEntity(prevPoint, item);
		}
	}

	public Random getRandom() {
		return rand;
	}
}
