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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import org.jboss.netty.channel.Channel;
import org.spout.api.Spout;
import org.spout.api.chat.ChatArguments;
import org.spout.api.chat.console.Console;
import org.spout.api.command.Command;
import org.spout.api.command.CommandSource;
import org.spout.api.data.ValueHolder;
import org.spout.api.data.ValueHolderBase;
import org.spout.api.geo.World;
import org.spout.api.lang.Locale;
import org.spout.vanilla.protocol.rcon.msg.CommandResponseMessage;
import org.spout.vanilla.protocol.rcon.msg.RconMessage;

/**
 * Session for a remote connection
 */
public class RemoteConnectionSession implements Console, CommandSource {
	private final RemoteConnectionCore core;
	private final AtomicInteger requestId = new AtomicInteger(-1);
	private final AtomicReference<Channel> channel = new AtomicReference<Channel>();
	private State state = State.AUTH;
	private DateFormat format = new SimpleDateFormat("hh:mm:ss");

	public static enum State {
		AUTH,
		COMMANDS,
	}

	public RemoteConnectionSession(RemoteConnectionCore core) {
		this.core = core;
		//requestId.set(new Random().nextInt()); // TODO: Find place to allocate request id, might want to have client do this
	}

	public void send(RconMessage message) {
		getChannel().write(message);
	}

	public Channel getChannel() {
		Channel ret = this.channel.get();
		if (ret == null) {
			throw new IllegalStateException("Session has not yet been connected!");
		}
		return ret;
	}

	public void setChannel(Channel c) {
		if (!channel.compareAndSet(null, c)) {
			throw new IllegalStateException("Channel has already been set!");
		}
	}

	public void setRequestId(int requestId) {
		if (requestId == -1) {
			this.requestId.set(-1);
		} else if (state == State.AUTH) {
			throw new IllegalStateException("Not correctly authenticated yet!");
		} else if (!this.requestId.compareAndSet(-1, requestId)) {
			throw new IllegalStateException("Request id has already been set!");
		}
	}

	public int getRequestId() {
		return requestId.get();
	}

	public boolean isAuthenticated() {
		return requestId.get() != -1;
	}

	public RemoteConnectionCore getCore() {
		return core;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	@Override
	public void addMessage(ChatArguments message) {
		StringBuilder builder = new StringBuilder();
		builder.append('[').append(format.format(new Date())).append("] ");
		builder.append(message.asString());
		// TODO: Split up payload
		send(new CommandResponseMessage(builder.toString()));
	}

	@Override
	public String getName() {
		return isInitialized() ? "RCON:" + channel.get().getRemoteAddress() : "RCON:Unknown";
	}

	@Override
	public void init() {
	}

	@Override
	public boolean isInitialized() {
		return channel.get() != null && channel.get().isConnected();
	}

	@Override
	public void close() {
		getChannel().getCloseFuture().awaitUninterruptibly();
	}

	@Override
	public void setDateFormat(DateFormat format) {
		this.format = format;
	}

	// CommandSource methods
	@Override
	public void sendCommand(String command, ChatArguments arguments) {
		if ("say".equals(command)) {
			addMessage(arguments);
		} else {
			// Throw exception?
			return;
		}
	}

	@Override
	public void processCommand(String command, ChatArguments arguments) {
		Command cmd = Spout.getEngine().getRootCommand().getChild(command);
		if (cmd == null) {
			sendMessage(ChatArguments.fromString("No such command: " + command));
		} else {
			cmd.process(this, command, arguments, false);
		}
	}

	@Override
	public boolean sendMessage(Object... message) {
		return sendMessage(new ChatArguments(message));
	}

	@Override
	public boolean sendMessage(ChatArguments message) {
		addMessage(message);
		return true;
	}

	@Override
	public boolean sendRawMessage(Object... message) {
		return sendRawMessage(new ChatArguments(message));
	}

	@Override
	public boolean sendRawMessage(ChatArguments message) {
		addMessage(message);
		return true;
	}

	@Override
	public Locale getPreferredLocale() {
		return Locale.ENGLISH_US;
	}

	@Override
	public ValueHolder getData(String node) {
		return new ValueHolderBase.NullHolder();
	}

	@Override
	public ValueHolder getData(World world, String node) {
		return new ValueHolderBase.NullHolder();
	}

	@Override
	public boolean hasPermission(String node) {
		return hasPermission(null, node);
	}

	@Override
	public boolean hasPermission(World world, String node) {
		return true;
	}

	@Override
	public boolean isInGroup(String group) {
		return false;
	}

	@Override
	public boolean isInGroup(World world, String group) {
		return false;
	}

	@Override
	public String[] getGroups() {
		return new String[0];
	}

	@Override
	public String[] getGroups(World world) {
		return new String[0];
	}
}
