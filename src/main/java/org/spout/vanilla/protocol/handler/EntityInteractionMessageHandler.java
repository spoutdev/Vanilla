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
package org.spout.vanilla.protocol.handler;

import org.spout.api.entity.Entity;
import org.spout.api.inventory.ItemStack;
import org.spout.api.player.Player;
import org.spout.api.protocol.MessageHandler;
import org.spout.api.protocol.Session;
import org.spout.vanilla.entity.VanillaEntity;
import org.spout.vanilla.material.Weapon;
import org.spout.vanilla.protocol.msg.EntityInteractionMessage;

public class EntityInteractionMessageHandler extends MessageHandler<EntityInteractionMessage> {
	@Override
	public void handle(Session session, Player player, EntityInteractionMessage message) {
		//TODO what happens if the entity is in a different region?
		Entity clickedEntity = player.getEntity().getWorld().getRegion(player.getEntity().getPoint()).getEntity(message.getTarget());
		if (clickedEntity == null) {
			return;
		}
		if (message.isPunching()) {
			if (clickedEntity.getController() instanceof VanillaEntity) {
				ItemStack is = player.getEntity().getInventory().getCurrentItem();
				int damage = 1;
				if (is instanceof Weapon) {
					damage = ((Weapon) is).getDamage();
				}
				((VanillaEntity) clickedEntity.getController()).damage(damage);
			}
		} else {
		}
	}
}
