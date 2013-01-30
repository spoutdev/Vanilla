/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, Spout LLC <http://www.spout.org/>
 * Vanilla is licensed under the Spout License Version 1.
 *
 * Vanilla is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the Spout License Version 1.
 *
 * Vanilla is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for
 * more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the Spout License Version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://spout.in/licensev1> for the full license, including
 * the MIT license.
 */
package org.spout.vanilla.plugin.protocol.handler;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import org.spout.api.Spout;
import org.spout.api.chat.ChatArguments;
import org.spout.api.chat.style.ChatStyle;
import org.spout.api.component.type.BlockComponent;
import org.spout.api.entity.Player;
import org.spout.api.protocol.MessageHandler;
import org.spout.api.protocol.Session;

import org.spout.vanilla.plugin.VanillaPlugin;
import org.spout.vanilla.plugin.component.substance.material.CommandBlock;
import org.spout.vanilla.plugin.protocol.msg.ServerPluginMessage;

public class ServerPluginHandler extends MessageHandler<ServerPluginMessage> {
	public static final String COMMAND_BLOCK_CHANNEL = "MC|AdvCdm";

	@Override
	public void handleServer(Session session, ServerPluginMessage msg) {
		byte[] data = msg.getData();
		DataInputStream in = new DataInputStream(new ByteArrayInputStream(data));
		System.out.println("Custom payload received");
		if (msg.getType().equals(COMMAND_BLOCK_CHANNEL) && data.length >= 14) {
			System.out.println("CommandBlock info received: " + data.length);
			try {
				int x = in.readInt();
				int y = in.readInt();
				int z = in.readInt();
				int strLength = (data.length - 12) / 2;
				char[] chars = new char[strLength];
				for (int i = 0; i < strLength; i++) {
					chars[i] = in.readChar();
					System.out.println("Char: " + chars[i]);
				}
				String text = new String(chars).substring(1, strLength);
				System.out.println("At: {" + x + "," + y + "," + z + "}");
				System.out.println("Text: " + text);
				BlockComponent c = Spout.getEngine().getDefaultWorld().getBlock(x, y, z).getComponent();
				Player player = session.getPlayer();
				ChatArguments prefix = VanillaPlugin.getInstance().getPrefix();
				if (c == null || !(c instanceof CommandBlock)) {
					Spout.getLogger().warning("CommandBlock information received, but there is no CommandBlock at {" + x + "," + y + "," + z + "}.");
				} else if (player.hasPermission("vanilla.commandblock." + text.split(" ")[0])) {
					((CommandBlock) c).setCommand(text);
					player.sendMessage(prefix, ChatStyle.WHITE, "Command set: ", ChatStyle.BRIGHT_GREEN, text);
				} else {
					player.sendMessage(prefix, ChatStyle.RED, "You don't have permission to do that.");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
