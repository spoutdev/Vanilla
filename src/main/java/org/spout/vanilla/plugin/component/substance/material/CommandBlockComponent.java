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
package org.spout.vanilla.plugin.component.substance.material;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.commons.lang3.Validate;
import org.spout.api.Client;
import org.spout.api.Server;
import org.spout.api.Spout;
import org.spout.api.chat.ChatArguments;
import org.spout.api.chat.channel.ChatChannel;
import org.spout.api.chat.channel.PermissionChatChannel;
import org.spout.api.chat.style.ChatStyle;
import org.spout.api.command.Command;
import org.spout.api.command.CommandSource;
import org.spout.api.data.ValueHolder;
import org.spout.api.entity.Player;
import org.spout.api.event.Cause;
import org.spout.api.event.server.PreCommandEvent;
import org.spout.api.geo.World;
import org.spout.api.geo.discrete.Point;
import org.spout.api.lang.Locale;
import org.spout.api.math.MathHelper;
import org.spout.vanilla.api.component.substance.material.VanillaBlockComponent;
import org.spout.vanilla.api.data.GameMode;
import org.spout.vanilla.plugin.VanillaPlugin;
import org.spout.vanilla.plugin.component.living.neutral.Human;
import org.spout.vanilla.plugin.data.VanillaData;
import org.spout.vanilla.api.event.block.CommandBlockUpdateEvent;

public class CommandBlockComponent extends VanillaBlockComponent implements CommandSource {
	
	public static final ChatChannel COMMANDBLOCK_BROADCAST_CHANNEL = new PermissionChatChannel("Command block broadcasts", "vanilla.commandblock.broadcast");
	
	private final AtomicReference<ChatChannel> activeChannel = new AtomicReference<ChatChannel>();
	
	@Override
	public void onAttached() {
		super.onAttached();
		this.activeChannel.set(COMMANDBLOCK_BROADCAST_CHANNEL);
	}
	
	@Override
	public String getName() {
		return "@";
	}
	
	@Override
	public String[] getGroups() {
		return new String[0];
	}

	@Override
	public String[] getGroups(World arg0) {
		return new String[0];
	}

	@Override
	public boolean hasPermission(String node) {
		return true;
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
	public boolean isInGroup(World arg0, String arg1) {
		return false;
	}

	@Override
	public ValueHolder getData(String node) {
		return null;
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

	@Override
	public Locale getPreferredLocale() {
		return Locale.ENGLISH_US;
	}
	
	public void executeCommandInput() {
		String[] split = this.getCommandInput().split(" ");
		if (split.length != 0)
		{
			String cmd;
			String args = "";
			
			cmd = split[0];
			if ( split.length > 1 ) {
				for ( int i = 1; i < split.length; i++ ) {
					args += " " + split[i];
				}
				args = args.substring(1);
			}
			
			this.sendCommand(cmd, ChatArguments.fromFormatString(args));
		}
	}

	@Override
	public void processCommand(String command, ChatArguments arguments) {
		PreCommandEvent event = Spout.getEventManager().callEvent(new PreCommandEvent(this, command, arguments));
		if (event.isCancelled()) {
			return;
		}
		command = event.getCommand();
		arguments = event.getArguments();
		
		List<ChatArguments> possibleArguments = getAllPossibleCommandArguments(arguments);
		
		Command cmd = VanillaPlugin.getInstance().getEngine().getRootCommand().getChild(command, false);
		if (cmd != null) {
			for (ChatArguments args : possibleArguments) {
				cmd.process(this, command, args, false);
			}
		}
	}
	
	private Filter getFilter(char[] filter) {
		Integer x = null;
		Integer y = null;
		Integer z = null;
		Integer maxRadius = null;
		int minRadius = 0;
		int minLevel = 0;
		Integer maxLevel = null;
		Integer quantity = null;
		GameMode mode = null;
		
		int index = 0;
		for (int i = 0; i < filter.length; i++) {
			if (Character.isDigit(filter[i])) {
				int size = 0;
				while (i + ++size < filter.length && Character.isDigit(filter[i + size]));
				char[] buffer = new char[--size];
				System.arraycopy(filter, i, buffer, 0, buffer.length);
				
				if (index == 0) {
					x = Integer.parseInt(new String(buffer));
				} else if (index == 1) {
					y = Integer.parseInt(new String(buffer));
				} else if (index == 2) {
					z = Integer.parseInt(new String(buffer));
				} else {
					return null;
				}
				
				i += size + 1;
				if (filter[i] != ',') {
					return null;
				}
				
				index++;
			} else if (Character.isAlphabetic(filter[i])) {
				String parameter;
				{
					int size = 0;
					while (i + ++size < filter.length && Character.isDigit(filter[i + size]));
					char[] buffer = new char[size];
					System.arraycopy(filter, i, buffer, 0, buffer.length);
					parameter = new String(buffer);
					
					i += size;
				}
				
				if (i >= filter.length || filter[i] != '=') {
					return null;
				}
				
				int value;
				{
					i++;
					int size = 0;
					while (i + ++size < filter.length && Character.isDigit(filter[i + size]));
					char[] buffer = new char[size];
					System.arraycopy(filter, i, buffer, 0, buffer.length);
					value = Integer.parseInt(new String(buffer));
					
					i += size;
				}
				
				if (i < filter.length && filter[i] != ',') {
					return null;
				}
				
				if (parameter.equals("x")) {
					x = value;
				} else if (parameter.equals("y")) {
					y = value;
				} else if (parameter.equals("z")) {
					z = value;
				} else if (parameter.equals("r")) {
					maxRadius = value;
				} else if (parameter.equals("rm")) {
					minRadius = value;
				} else if (parameter.equals("m")) {
					mode = GameMode.get(value % GameMode.values().length);
				} else if (parameter.equals("c")) {
					quantity = value;
				} else if (parameter.equals("l")) {
					maxLevel = value;
				} else if (parameter.equals("lm")) {
					minLevel = value;
				} else {
					return null;
				}
				
				index++;
			}
		}
		
		return new Filter(x == null || y == null || z == null ? null : new Point(getBlock().getWorld(), x, y, z), maxRadius, minRadius, maxLevel, minLevel, quantity, mode);
	}
	
	private List<Player> getAllMatchingPlayers(Filter filter) {
		List<Player> matching = new ArrayList<Player>();
		
		if (filter == null) {
			return matching;
		}
		
		Point center = filter.getPosition();
		Integer radius = filter.getMaxRadius();
		
		// position filtering
		if (center != null) { 
			if (radius == null) {
				radius = 1;
			}
			
			matching.addAll(center.getWorld().getNearbyPlayers(center, radius));
			for (int i = 0; i < matching.size(); i++) {
				Player player = matching.get(i);
				if (player.getTransform().getPosition().distance(center) < filter.getMinRadius()) {
					matching.remove(i--);
				}
			}
		} else { // Server-wide
			switch (Spout.getPlatform()) {
				case SERVER:
					if (radius == null) {
						Collections.addAll(matching, ((Server) Spout.getEngine()).getOnlinePlayers());
					} else {
						matching.addAll(getBlock().getWorld().getNearbyPlayers(center, radius));
					}
					break;
				case CLIENT:
					if (radius == null) {
						matching.add(((Client) Spout.getEngine()).getActivePlayer());
					} else {
						center = getBlock().getPosition();
						Player player = getBlock().getWorld().getNearestPlayer(center, radius);
						if (player != null) {
							matching.add(player);
						}
					}
					break;
				default:
					break;
			}
		}
		
		// result limit
		if (filter.getQuantity() != null) { // limited
			if (filter.getQuantity() > 0) {
				for (int i = filter.getQuantity(); i < matching.size();) {
					matching.remove(i);
				}
			} else if (filter.getQuantity() < 0) {
				for (int i = -filter.getQuantity(); i < matching.size(); i++) {
					matching.remove(matching.size() - i);
				}	
			}
		}
		
		// gamemode filters
		if (filter.getGameMode() != null) {
			for (int i = 0; i < matching.size(); i++) {
				Player player = matching.get(i);
				if (!player.get(Human.class).getGameMode().equals(filter.getGameMode())) {
					matching.remove(i--);
				}
			}
		}
		
		return matching;
	}
	
	private Player getNearestMatchingPlayer(Filter filter) {
		List<Player> matching = new ArrayList<Player>();
		
		if (filter == null) {
			return null;
		}
		
		Point center = filter.getPosition();
		Integer radius = filter.getMaxRadius();
		
		if (center == null) {
			center = getBlock().getPosition();
		}
		
		// position filtering
		if (radius == null) {
			matching.addAll(center.getWorld().getPlayers());
		} else {
			matching.addAll(center.getWorld().getNearbyPlayers(center, radius));
		}
		
		for (int i = 0; i < matching.size(); i++) {
			Player player = matching.get(i);
			if (player.getTransform().getPosition().distance(center) < filter.getMinRadius()) {
				matching.remove(i--);
			}
		}
		
		// result limit
		if (filter.getQuantity() != null) { // limited
			if (filter.getQuantity() > 0) {
				for (int i = filter.getQuantity(); i < matching.size();) {
					matching.remove(i);
				}
			} else if (filter.getQuantity() < 0) {
				for (int i = -filter.getQuantity(); i < matching.size(); i++) {
					matching.remove(matching.size() - i);
				}	
			}
		}
		
		// gamemode and level filters
		if (filter.getGameMode() != null) {
			for (int i = 0; i < matching.size(); i++) {
				Player player = matching.get(i);
				if (!player.get(Human.class).getGameMode().equals(filter.getGameMode())) {
					matching.remove(i--);
				}
			}
		}
		
		if (matching.isEmpty()) {
			return null;
		}
		
		// getting nearest players from the matching set
		Player result = matching.get(0);
		double minDistance = result.getTransform().getPosition().distance(center);
		for (int i = 1; i < matching.size(); i++) {
			double distance = matching.get(i).getTransform().getPosition().distance(center);
			if (distance < minDistance) {
				minDistance = distance;
				result = matching.get(i);
			}
		}
		
		return result;
	}
	
	private Player getRandomMatchingPlayer(Filter filter) {
		List<Player> players = getAllMatchingPlayers(filter);
		if (players.isEmpty()) {
			return null;
		}
		
		Random rand = MathHelper.getRandom();
		return players.get(rand.nextInt(players.size())); 
	}
	
	private List<ChatArguments> getAllPossibleCommandArguments(ChatArguments arguments) {
		List<ChatArguments> result = new LinkedList<ChatArguments>();
		char[] str = arguments.toFormatString().toCharArray();
		for (int i = 0; i < str.length; i++) {
			if (str[i] == '@' && i + 1 < str.length) {
				i++;
				if (str[i] == 'p') { // nearest player
					char[] filter = new char[0];
					String regex = "@p";
					if (i + 1 < str.length && str[i + 1] == '[') {
						i++;
						int size = 0;
						while (i + ++size < str.length && str[i + size] != ']');
						if (str[i + size] == ']') {
							filter = new char[--size];
							System.arraycopy(str, i + 1, filter, 0, filter.length);
							i += size + 1;
						}
						regex = "@p\\[[^\\]]+\\]";
					}
					
					Player player = getNearestMatchingPlayer(getFilter(filter));
					if (player != null) {
						arguments = ChatArguments.fromFormatString(arguments.toFormatString().replaceFirst(regex, player.getName()));
						result.addAll(getAllPossibleCommandArguments(arguments));
					}
					return result;
				} else if (str[i] == 'r') { // random player
					char[] filter = new char[0];
					String regex = "@r";
					if (i + 1 < str.length && str[i + 1] == '[') {
						i++;
						int size = 0;
						while (i + ++size < str.length && str[i + size] != ']');
						if (str[i + size] == ']') {
							filter = new char[--size];
							System.arraycopy(str, i + 1, filter, 0, filter.length);
							i += size + 1;
						}
						regex = "@r\\[[^\\]]+\\]";
					}
					
					Player player = getRandomMatchingPlayer(getFilter(filter));
					if (player != null) {
						arguments = ChatArguments.fromFormatString(arguments.toFormatString().replaceFirst(regex, player.getName()));
						result.addAll(getAllPossibleCommandArguments(arguments));
					}
					return result;
				} else if (str[i] == 'a') { // all players
					char[] filter = new char[0];
					String regex = "@a";
					if (i + 1 < str.length && str[i + 1] == '[') {
						i++;
						int size = 0;
						while (i + ++size < str.length && str[i + size] != ']');
						if (str[i + size] == ']') {
							filter = new char[--size];
							System.arraycopy(str, i + 1, filter, 0, filter.length);
							i += size + 1;
						}
						regex = "@a\\[[^\\]]+\\]";
					}
					
					List<Player> players = getAllMatchingPlayers(getFilter(filter));
					for (Player player : players) {
						result.addAll(getAllPossibleCommandArguments(ChatArguments.fromFormatString(arguments.toFormatString().replaceFirst(regex, player.getName()))));
					}
					return result;
				}
			}
		}
		result.add(arguments);
		return result;
	}

	@Override
	public void sendCommand(String command, ChatArguments arguments) {
		processCommand(command, arguments);
	}

	@Override
	public boolean sendMessage(Object... text) {
		return sendMessage(new ChatArguments(text));
	}

	@Override
	public boolean sendMessage(ChatArguments message) {
		return sendRawMessage(message);
	}

	@Override
	public boolean sendRawMessage(Object... text) {
		return sendRawMessage(new ChatArguments(text));
	}

	@Override
	public boolean sendRawMessage(ChatArguments message) {
		//TODO: toggle this depending on the gamerule
		getActiveChannel().broadcastToReceivers(new ChatArguments(ChatStyle.GRAY, ChatStyle.ITALIC, "[@: ", message, ChatStyle.RESET, ChatStyle.GRAY, ChatStyle.ITALIC, "]"));
		return true;
	}
	
	@Override
	public ChatChannel getActiveChannel() {
		return this.activeChannel.get();
	}

	@Override
	public void setActiveChannel(ChatChannel channel) {
		Validate.notNull(channel);
		channel.onAttachTo(this);
		this.activeChannel.getAndSet(channel).onDetachedFrom(this);
	}
	
	public boolean isPowered() {
		return getData().get(VanillaData.IS_POWERED);
	}

	public boolean setPowered(boolean powered) {
		boolean previousState = isPowered();
		getData().put(VanillaData.IS_POWERED, powered);
		return previousState;
	}
	
	public void setCommandInput(String command, Cause<?> cause) {
		if (cause == null) {
			throw new IllegalArgumentException("Cause may not be null");
		}
		
		CommandBlockUpdateEvent event = new CommandBlockUpdateEvent(this, command, cause);
		Spout.getEventManager().callEvent(event);
		
		if (!event.isCancelled()) {
			for (Player p : this.getOwner().getChunk().getObservingPlayers()) {
				p.getNetworkSynchronizer().callProtocolEvent(event);
			}
			getData().put(VanillaData.COMMAND_BLOCK_INPUT, event.getCommand());
		}
	}
	
	public String getCommandInput() {
		return getData().get(VanillaData.COMMAND_BLOCK_INPUT);
	}
	
	public class Filter {
		
		private Point p;
		private Integer maxRadius;
		private int minRadius;
		private int minLevel;
		private Integer maxLevel;
		private Integer quantity;
		private GameMode mode;
		
		public Filter(Point p, Integer maxRadius, int minRadius, Integer maxLevel, int minLevel, Integer quantity, GameMode mode) {
			this.p = p;
			this.maxRadius = maxRadius;
			this.minRadius = minRadius;
			this.minLevel = minLevel;
			this.maxLevel = maxLevel;
			this.quantity = quantity;
			this.mode = mode;
		}
		
		public Point getPosition() {
			return this.p;
		}
		
		public Integer getMaxRadius() {
			return this.maxRadius;
		}
		
		public int getMinRadius() {
			return this.minRadius;
		}
		
		public Integer getMaxLevel() {
			return this.maxLevel;
		}
		
		public int getMinLevel() {
			return this.minLevel;
		}
		
		public Integer getQuantity() {
			return this.quantity;
		}
		
		public GameMode getGameMode() {
			return this.mode;
		}
	}
}
