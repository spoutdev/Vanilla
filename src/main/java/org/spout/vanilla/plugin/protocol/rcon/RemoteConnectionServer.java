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
package org.spout.vanilla.plugin.protocol.rcon;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteOrder;
import java.util.logging.Logger;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.buffer.HeapChannelBufferFactory;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.ChannelGroupFuture;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.channel.socket.ServerSocketChannel;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import org.spout.api.Server;
import org.spout.api.protocol.PortBinding;
import org.spout.api.util.config.yaml.YamlConfiguration;

import org.spout.vanilla.plugin.protocol.VanillaProtocol;

/**
 * Represents a remote connection server.
 */
public class RemoteConnectionServer extends RemoteConnectionCore {
	private final RconConfiguration config;
	private final ChannelGroup group = new DefaultChannelGroup("RconChannels");
	private final ServerBootstrap bootstrap = new ServerBootstrap();

	public RemoteConnectionServer(Logger logger, File dataFolder) {
		super(logger);
		this.config = new RconConfiguration(new YamlConfiguration(new File(dataFolder, "rcon.yml")));
		bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
			public ChannelPipeline getPipeline() throws Exception {
				RemoteConnectionSession session = new RemoteConnectionSession(RemoteConnectionServer.this);
				return Channels.pipeline(new RconEncoder(session),
						new RconDecoder(session),
						new ChannelTracker(),
						new RconHandler(session));
			}
		});

		NioServerSocketChannelFactory factory = new NioServerSocketChannelFactory() {
			@Override
			public ServerSocketChannel newChannel(ChannelPipeline pipeline) {
				ServerSocketChannel channel = super.newChannel(pipeline);
				// This is the factory type used in org.jboss.netty.channel.DefaultServerChannelConfig, modified to be little-endian
				// Netty 4.x should no longer need this, since the byte-order conversion can be done through the ChannelBuffer.
				// This type of fix may reduce object creation though.
				channel.getConfig().setBufferFactory(HeapChannelBufferFactory.getInstance(ByteOrder.LITTLE_ENDIAN));
				return channel;
			}
		};
		bootstrap.setFactory(factory);
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
		group.add(bootstrap.bind(address));
	}

	public String getPassword() {
		return config.PASSWORD.getString();
	}

	public void close() throws IOException {
		ChannelGroupFuture f = group.close().awaitUninterruptibly();
		if (!f.isCompleteSuccess()) {
			for (ChannelFuture future : f) {
				if (!future.isSuccess()) {
					throw new IOException(future.getCause());
				}
			}
		}
		bootstrap.releaseExternalResources();
	}

	/**
	 * See {@link ChannelGroup}
	 */
	private final class ChannelTracker extends SimpleChannelUpstreamHandler {
		@Override
		public void channelOpen(ChannelHandlerContext ctx, ChannelStateEvent e) {
			// Add all open channels to the global group so that they are
			// closed on shutdown.
			group.add(e.getChannel());
		}
	}
}
