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
import org.spout.api.material.ItemMaterial;
import org.spout.api.material.Material;
import org.spout.api.player.Player;
import org.spout.api.protocol.MessageHandler;
import org.spout.api.protocol.Session;

import org.spout.vanilla.configuration.VanillaConfiguration;
import org.spout.vanilla.controller.VanillaController;
import org.spout.vanilla.controller.living.player.VanillaPlayer;
import org.spout.vanilla.material.Weapon;
import org.spout.vanilla.protocol.msg.EntityAnimationMessage;
import org.spout.vanilla.protocol.msg.EntityInteractionMessage;
import org.spout.vanilla.protocol.msg.EntityStatusMessage;

public class EntityInteractionMessageHandler extends MessageHandler<EntityInteractionMessage> {
	@Override
	public void handleServer(Session session, Player player, EntityInteractionMessage message) {
		Entity clickedEntity = player.getEntity().getWorld().getEntity(message.getTarget());
		if (clickedEntity == null) {
			return;
		}

		if (message.isPunching()) {
			if (clickedEntity.getController() instanceof VanillaPlayer && !VanillaConfiguration.PLAYER_PVP_ENABLED.getBoolean()) {
				return;
			}

			if (clickedEntity.getController() instanceof VanillaController) {
				ItemStack is = player.getEntity().getInventory().getCurrentItem();

				int damage = 1;
				if (is != null && is.getMaterial() != null && is.getMaterial() instanceof Weapon) {
					damage = ((Weapon) is.getMaterial()).getDamage();
				}

				VanillaController temp = (VanillaController) clickedEntity.getController();
				if (!temp.getParent().isDead()) {
					temp.damage(damage);
					temp.broadcastPacket(new EntityAnimationMessage(temp.getParent().getId(), EntityAnimationMessage.ANIMATION_HURT), new EntityStatusMessage(temp.getParent().getId(), EntityStatusMessage.ENTITY_HURT));
				}
			}
		} else {
			ItemStack holding = player.getEntity().getInventory().getCurrentItem();
			if (holding == null) {
				return;
			}

			Material mat = holding.getMaterial();
			if (mat instanceof ItemMaterial) {
				((ItemMaterial) mat).onInteract(player.getEntity(), clickedEntity);
			}
		}
	}
}
