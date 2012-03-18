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
package org.spout.vanilla.material.item;

import java.lang.reflect.Constructor;

import org.spout.api.entity.Entity;
import org.spout.api.event.player.PlayerInteractEvent.Action;
import org.spout.api.geo.discrete.Point;
import org.spout.api.inventory.ItemStack;
import org.spout.api.material.block.BlockFace;
import org.spout.vanilla.entity.VanillaEntity;
import org.spout.vanilla.entity.living.player.SurvivalPlayer;
import org.spout.vanilla.material.generic.GenericItem;

public class SpawnEgg extends GenericItem {
	final Constructor<?> chosen;

	public SpawnEgg(String name, int id, int spawnedEntityId) {
		this(name, id, org.spout.vanilla.entity.Entity.getByID(spawnedEntityId));
	}
	
	public SpawnEgg(String name, int id, org.spout.vanilla.entity.Entity spawnedEntity) {
		super(name, id);

		if (spawnedEntity == null) {
			throw new IllegalArgumentException("Spawned entity can not be null!");
		}
		
		Class<? extends VanillaEntity> controller = spawnedEntity.getController();

		Constructor<?>[] constructors = controller.getConstructors();
		for (Constructor<?> constructor : constructors) {
			if (constructor.getParameterTypes().length == 0) {
				chosen = constructor;
				return;
			}
		}

		chosen = null;
	}

	@Override
	public void onInteract(Entity entity, Point position, Action type, BlockFace clickedFace) {
		if (chosen != null) {
			try {
				entity.getWorld().createAndSpawnEntity(position, (VanillaEntity) chosen.newInstance(new Object[]{}));
			} catch (Exception e) {
				// What to do here?
			}
		}

		ItemStack holding = entity.getInventory().getCurrentItem();
		if (entity.getController() instanceof SurvivalPlayer) {
			if (holding.getAmount() > 1) {
				holding.setAmount(holding.getAmount() - 1);
				entity.getInventory().setItem(holding, entity.getInventory().getCurrentSlot());
			} else if (holding.getAmount() == 1) {
				entity.getInventory().setItem(null, entity.getInventory().getCurrentSlot());
			}
		}
	}
}
