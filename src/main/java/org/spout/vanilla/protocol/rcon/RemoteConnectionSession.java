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
package org.spout.vanilla.protocol.rcon;

/**
 * Session for a remote connection
 */
public class RemoteConnectionSession {
	/*
	private final RemoteConnectionCore core;
	private final AtomicInteger requestId = new AtomicInteger(-1);
	private final AtomicReference<Channel> channel = new AtomicReference<Channel>();
	private State state = State.AUTH;
	private final AtomicReference<ChatChannel> activeChannel = new AtomicReference<ChatChannel>();
	private DateFormat format = new SimpleDateFormat("hh:mm:ss");

	@Override
	public boolean isInGroup(World world, String group) {
		return false;
	}

	@Override
	public String[] getGroups(World world) {
		return new String[0];
	}

	@Override
	public ValueHolder getData(World world, String node) {
		return null;
	}

	@Override
	public boolean hasData(String node) {
		return false;
	}

	@Override
	public boolean hasData(World world, String node) {
		return false;
	}

	public static enum State {
		AUTH,
		COMMANDS,
	}

	public RemoteConnectionSession(RemoteConnectionCore core) {
		this.core = core;
		activeChannel.set(VanillaPlugin.getInstance().getEngine().getChatChannelFactory().create(this));
		//requestId.set(new Random().nextInt()); // TODO: Find place to allocate request id, might want to have client do this
	}

	public String getName() {
		return isInitialized() ? "RCON:" + channel.get().getRemoteAddress() : "RCON:Unknown";
	}

	public void init() {
	}

	public boolean isInitialized() {
		return channel.get() != null && channel.get().isConnected();
	}

	public void close() {
		getChannel().getCloseFuture().awaitUninterruptibly();
	}

	public void setDateFormat(DateFormat format) {
		this.format = format;
	}

	public void send(RconMessage message) {
		getChannel().write(message);
	}

	public void addMessage(ChatArguments message) {
		StringBuilder builder = new StringBuilder();
		builder.append('[').append(format.format(new Date())).append("] ");
		builder.append(message.asString());
		// TODO: Split up payload
		send(new CommandResponseMessage(builder.toString()));
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

	// CommandSource methods
	public void sendCommand(String command, ChatArguments arguments) {
		if ("say".equals(command)) {
			addMessage(arguments);
		} else {
			// Throw exception?
		}
	}

	public void processCommand(String command, ChatArguments arguments) {
		Command cmd = VanillaPlugin.getInstance().getEngine().getRootCommand().getChild(command);
		if (cmd == null) {
			sendMessage(ChatArguments.fromString("No such command: " + command));
		} else {
			cmd.process(this, command, arguments, false);
		}
	}

	public boolean sendMessage(Object... message) {
		return sendMessage(new ChatArguments(message));
	}

	public boolean sendMessage(ChatArguments message) {
		addMessage(message);
		return true;
	}

	public boolean sendRawMessage(Object... message) {
		return sendRawMessage(new ChatArguments(message));
	}

	public boolean sendRawMessage(ChatArguments message) {
		addMessage(message);
		return true;
	}

	public Locale getPreferredLocale() {
		return Locale.ENGLISH_US;
	}

	public ValueHolder getData(String node) {
		return new ValueHolderBase.NullHolder();
	}

	public boolean hasPermission(String node) {
		return hasPermission(null, node);
	}

	public boolean hasPermission(World world, String node) {
		return true;
	}

	public boolean isInGroup(String group) {
		return false;
	}

	public String[] getGroups() {
		return new String[0];
	}

	public boolean isGroup() {
		return false;
	}

	@Override
	public ChatChannel getActiveChannel() {
		return activeChannel.get();
	}

	@Override
	public void setActiveChannel(ChatChannel channel) {
		Preconditions.checkNotNull(channel);
		channel.onAttachTo(this);
		activeChannel.getAndSet(channel).onDetachedFrom(this);
	}*/
}
