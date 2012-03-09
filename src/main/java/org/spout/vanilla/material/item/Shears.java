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

import java.util.Random;

import org.spout.api.entity.Entity;
import org.spout.api.inventory.ItemStack;
import org.spout.api.material.MaterialData;
import org.spout.vanilla.entity.living.creature.passive.Sheep;
import org.spout.vanilla.entity.living.player.SurvivalPlayer;
import org.spout.vanilla.entity.object.Item;
import org.spout.vanilla.material.generic.GenericTool;

public class Shears extends GenericTool {
	private Random rand = new Random();
	
	public Shears(String name, int id, short durability) {
		super(name, id, durability);
	}
	
	@Override
	public void onInteract(Entity entity, Entity other) {
		if (!(other.getController() instanceof Sheep)) {
			return;
		}
		
		Sheep sheep = (Sheep) other.getController();
		if (sheep.isSheared()) {
			return;
		}
		
		other.setData("SheepSheared", true);
		
		short col = (short) other.getData("SheepColor").asInt();
		other.getWorld().createAndSpawnEntity(other.getPoint(), new Item(new ItemStack(MaterialData.getMaterial((short) 35, col), rand.nextInt(3) + 1), other.getPoint().normalize()));
		
		ItemStack holding = entity.getInventory().getCurrentItem();
		if (entity.getController() instanceof SurvivalPlayer) {
			holding.setDamage((short)(holding.getDamage() + 1));
			entity.getInventory().setItem(holding, entity.getInventory().getCurrentSlot());
		}
	}
}
