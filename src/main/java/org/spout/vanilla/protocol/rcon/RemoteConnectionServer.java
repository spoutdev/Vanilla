/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011 Spout LLC <http://www.spout.org/>
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
package org.spout.vanilla.protocol.rcon;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteOrder;
import java.util.logging.Logger;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.ChannelGroupFuture;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.util.concurrent.GlobalEventExecutor;

import org.spout.api.Server;
import org.spout.api.protocol.PortBinding;
import org.spout.cereal.config.yaml.YamlConfiguration;

import org.spout.vanilla.protocol.VanillaProtocol;

/**
 * Represents a remote connection server.
 */
public class RemoteConnectionServer extends RemoteConnectionCore {
	private final RconConfiguration config;
	private final ChannelGroup group = new DefaultChannelGroup("RconChannels", GlobalEventExecutor.INSTANCE);
	private final ServerBootstrap bootstrap = new ServerBootstrap();

	public RemoteConnectionServer(Logger logger, File dataFolder) {
		super(logger);
		this.config = new RconConfiguration(new YamlConfiguration(new File(dataFolder, "rcon.yml")));
		/*bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
			public ChannelPipeline getPipeline() throws Exception {
				RemoteConnectionSession session = new RemoteConnectionSession(RemoteConnectionServer.this);
				return Channels.pipeline(new RconEncoder(session),
						new RconDecoder(session),
						new ChannelTracker(),
						new RconHandler(session));
			}
		});*/
	}

	public void bindDefaultPorts(Server server) {
		if (!config.ENABLED.getBoolean()) {
			return;
		}
		int bindPort = config.PORT.getInt();

		for (PortBinding binding : server.getBoundAddresses()) {
			if (binding.getProtocol() instanceof VanillaProtocol) {
				if (binding.getAddress() instanceof InetSocketAddress) {
					InetSocketAddress addr = (InetSocketAddress) binding.getAddress();
					if (addr.getPort() == bindPort) {
						getLogger().warning("Port binding on " + addr + " is already occupying the port set for rcon, not binding!");
						continue;
					}

					bind(new InetSocketAddress(addr.getAddress(), bindPort));
				}
			}
		}
	}

	public void bind(SocketAddress address) {
		group.add(bootstrap.bind(address).awaitUninterruptibly().channel());
	}

	public String getPassword() {
		return config.PASSWORD.getString();
	}

	public void close() throws IOException {
		ChannelGroupFuture f = group.close().awaitUninterruptibly();
		if (!f.isSuccess()) {
			for (ChannelFuture future : f) {
				if (!future.isSuccess()) {
					throw new IOException(future.cause());
				}
			}
		}
		bootstrap.group().shutdownGracefully();
	}

	/**
	 * See {@link ChannelGroup}
	 */
	private final class ChannelTracker extends SimpleChannelInboundHandler<Object> {
		@Override
		public void channelActive(ChannelHandlerContext ctx) {
			// Add all open channels to the global group so that they are
			// closed on shutdown.
			group.add(ctx.channel());
		}

		@Override
		protected void channelRead0(ChannelHandlerContext chc, Object i) throws Exception {
			chc.fireChannelRead(i);
		}

	}
}
