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
package org.spout.vanilla.protocol.rcon;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

import org.spout.vanilla.protocol.rcon.handler.RconMessageHandler;
import org.spout.vanilla.protocol.rcon.msg.RconMessage;

/**
 * Handler for rcon connections
 */
public class RconHandler extends SimpleChannelHandler {
	private final RemoteConnectionSession session;

	public RconHandler(RemoteConnectionSession session) {
		this.session = session;
	}

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent event) throws Exception {
		session.setChannel(ctx.getChannel());
		session.getCore().getLogger().info("RCON Channel " + ctx.getChannel() + " connected");
	}

	@Override
	public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent event) {
		session.getCore().getLogger().info("RCON Channel " + ctx.getChannel() + " disconnected");
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent event) {
		if (event.getMessage() instanceof RconMessage) {
			RconMessage msg = (RconMessage) event.getMessage();
			@SuppressWarnings("unchecked")
			RconMessageHandler<RconMessage> handler = (RconMessageHandler<RconMessage>) session.getCore().getHandlerLookupService().find(msg.getClass());
			handler.handle(session, msg);
		}
	}
}
