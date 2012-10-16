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
package org.spout.vanilla.protocol.handler.world.block;

import java.util.Arrays;

import org.spout.api.component.components.BlockComponent;
import org.spout.api.entity.Player;
import org.spout.api.protocol.MessageHandler;
import org.spout.api.protocol.Session;

import org.spout.vanilla.component.substance.material.Sign;
import org.spout.vanilla.protocol.msg.world.block.SignMessage;

public class SignHandler extends MessageHandler<SignMessage> {
	@Override
	public void handleServer(Session session, SignMessage message) {
		if (!session.hasPlayer()) {
			return;
		}

		Player player = session.getPlayer();

		BlockComponent component = player.getWorld().getBlockComponent(message.getX(), message.getY(), message.getZ());
		if (component == null || !(component instanceof Sign)) {
			return;
		}

		String[] text = message.getMessage();
		if (text.length != 4) {
			return;
		}

		Sign sign = (Sign) component;
		sign.setText(text);
		System.out.println("Setting sign text to " + Arrays.toString(sign.getText()));
	}
}
