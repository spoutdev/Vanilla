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
package org.spout.vanilla.plugin.protocol;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.spout.api.Server;
import org.spout.api.Spout;
import org.spout.api.protocol.PortBinding;

import org.spout.vanilla.plugin.configuration.VanillaConfiguration;

public class LANThread extends Thread {
	private final byte[] contents;
	DatagramSocket socket;
	SocketAddress address;

	public LANThread() {
		super("LAN Discovery");
		setDaemon(true);
		InetSocketAddress vanillaSocket = getVanillaPort();
		try {
			this.contents = ("[MOTD]" + VanillaConfiguration.MOTD.getString() + "[/MOTD][AD]" + InetAddress.getLocalHost().getHostName() + ":" + vanillaSocket.getPort() + "[/AD]").getBytes();
		} catch (UnknownHostException e) {
			throw new RuntimeException(e);
		}
	}

	private InetSocketAddress getVanillaPort() {
		for (PortBinding binding : ((Server) Spout.getEngine()).getBoundAddresses()) {
			if (binding.getProtocol() instanceof VanillaProtocol && binding.getAddress() instanceof InetSocketAddress) {
				return ((InetSocketAddress) binding.getAddress());
			}
		}
		throw new IllegalStateException("No vanilla port found!");
	}

	public void start() {
		try {
			socket = new DatagramSocket();
			address = new InetSocketAddress(InetAddress.getByName("224.0.2.60"), 4445);
		} catch (SocketException e) {
			throw new RuntimeException(e);
		} catch (UnknownHostException e) {
			throw new RuntimeException(e);
		}
		super.start();
	}

	@Override
	public void run() {
		while (!this.isInterrupted()) {
			try {
				socket.send(new DatagramPacket(contents, contents.length, address));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				break;
			}
		}
	}
}
