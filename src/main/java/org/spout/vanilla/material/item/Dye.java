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

import org.spout.api.entity.Entity;
import org.spout.api.event.player.PlayerInteractEvent.Action;
import org.spout.api.geo.discrete.Point;
import org.spout.api.inventory.ItemStack;
import org.spout.api.material.block.BlockFace;
import org.spout.vanilla.entity.VanillaEntity;
import org.spout.vanilla.entity.living.passive.Sheep;
import org.spout.vanilla.entity.living.player.SurvivalPlayer;
import org.spout.vanilla.material.generic.GenericItem;

public class Dye extends GenericItem {
	
	private final int color;
	
	public Dye(String name, int id, int data) {
		super(name, id, data);
		
		color = 0xF - data;
	}
	
	@Override
	public void onInteract(Entity entity, Entity other) {
		if (!(other.getController() instanceof Sheep)) {
			System.out.println("No sheep: " + other.getClass().getName() + " :(");
			return;
		}
		
		other.setData( "SheepColor", color );
		System.out.println("Sheep go baaaa!");
		
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
