/*
 * This file is part of Vanilla.
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
 * the MIT license and the SpoutDev license version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license,
 * including the MIT license.
 */
package org.spout.vanilla.protocol.handler;

import org.spout.api.player.Player;
import org.spout.api.protocol.MessageHandler;
import org.spout.api.protocol.Session;
import org.spout.vanilla.entity.VanillaEntity;
import org.spout.vanilla.protocol.msg.EntityHeadYawMessage;



public class EntityHeadYawMessageHandler extends MessageHandler<EntityHeadYawMessage>{

	@Override
	public void handle(Session session, Player player, EntityHeadYawMessage message) {
		if(player.getEntity().getController() == null)
			return;
		if(!(player.getEntity().getController() instanceof VanillaEntity))
			return;
		VanillaEntity vanilla = (VanillaEntity) player.getEntity().getController();
		vanilla.setHeadYaw(message.getHeadYaw());
	}

}
