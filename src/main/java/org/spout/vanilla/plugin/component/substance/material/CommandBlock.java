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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

import org.apache.commons.lang3.builder.ToStringBuilder;

import org.spout.api.Server;
import org.spout.api.Spout;
import org.spout.api.chat.ChatArguments;
import org.spout.api.chat.channel.ChatChannel;
import org.spout.api.chat.channel.PermissionChatChannel;
import org.spout.api.chat.style.ChatStyle;
import org.spout.api.command.Command;
import org.spout.api.command.CommandSource;
import org.spout.api.command.RootCommand;
import org.spout.api.data.ValueHolder;
import org.spout.api.entity.Player;
import org.spout.api.event.Cause;
import org.spout.api.event.server.PreCommandEvent;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.discrete.Point;
import org.spout.api.lang.Locale;
import org.spout.api.util.SpoutToStringStyle;

import org.spout.vanilla.api.component.substance.material.VanillaBlockComponent;
import org.spout.vanilla.api.data.GameMode;
import org.spout.vanilla.plugin.VanillaPlugin;
import org.spout.vanilla.plugin.component.living.neutral.Human;
import org.spout.vanilla.plugin.component.misc.LevelComponent;
import org.spout.vanilla.plugin.configuration.VanillaConfiguration;
import org.spout.vanilla.api.data.VanillaData;

public class CommandBlock extends VanillaBlockComponent implements CommandSource {
	public static final ChatChannel CHAT_CHANNEL = new PermissionChatChannel("CommandBlock", "vanilla.commandblock.recieve");
	public static final char TARGET_CHAR = '@';
	public static final char NEAREST_PLAYER_CHAR = 'p';
	public static final char RANDOM_PLAYER_CHAR = 'r';
	public static final char ALL_PLAYERS_CHAR = 'a';
	public static final String NEAREST_PLAYER = "" + TARGET_CHAR + NEAREST_PLAYER_CHAR;
	public static final String RANDOM_PLAYER = "" + TARGET_CHAR + RANDOM_PLAYER_CHAR;
	public static final String ALL_PLAYERS = "" + TARGET_CHAR + ALL_PLAYERS_CHAR;
	private String name = "" + TARGET_CHAR;

	public void setName(String name) {
		this.name = name;
	}

	public void setCommand(String cmd, Cause<?> cause) {
		if (cause != null && cause.getSource() instanceof CommandSource) {
			CommandSource cmdSource = (CommandSource) cause.getSource();
			ChatArguments prefix = VanillaPlugin.getInstance().getPrefix();
			if (!cmdSource.hasPermission("vanilla.commandblock." + cmd.split(" ")[0])) {
				cmdSource.sendMessage(prefix, ChatStyle.RED, " You don't have permission to do that.");
				return;
			}
			cmdSource.sendMessage(prefix, ChatStyle.WHITE, "Command set: ", ChatStyle.BRIGHT_GREEN, cmd);
		}

		if (!cmd.startsWith("/")) {
			cmd = "say " + cmd;
			// TODO: Add support for '/say' for non-players
		} else {
			cmd = cmd.replaceFirst("/", "");
		}

		getData().put(VanillaData.COMMAND, cmd);
	}

	public void setCommand(String cmd) {
		setCommand(cmd, null);
	}

	public String getCommand() {
		return getData().get(VanillaData.COMMAND);
	}

	public void setPowered(boolean powered) {
		if (isPowered() != powered) {
			getData().put(VanillaData.IS_POWERED, powered);
			if (powered) {
				execute();
			}
		}
	}

	public boolean isPowered() {
		return getData().get(VanillaData.IS_POWERED);
	}

	public void execute() {
		if (!(Spout.getEngine() instanceof Server)) {
			throw new IllegalStateException("CommandBlocks are run from the server.");
		}

		String cmd = getCommand();
		if (cmd == null) {
			return;
		}

		char[] chars = cmd.toCharArray();
		int targetIndex = -1;
		for (int i = 0; i < chars.length; i++) {
			if (chars[i] == TARGET_CHAR && i + 1 < chars.length && isTarget(chars[i + 1])) {
				// string contains an identifier
				targetIndex = i;
				break;
			}
		}

		List<String> cmds = new ArrayList<String>();
		if (targetIndex != -1) {

			// if we found a target, format the command
			final String statement = getStatement(cmd, targetIndex);
			final PlayerFilter filter = new PlayerFilter(statement);
			try {
				filter.init();
			} catch (IllegalArgumentException e) {
				Spout.getLogger().log(Level.WARNING, "Could not execute CommandBlock at "
						+ getBlock().getPosition().toString() + " because of illegal syntax.", e);
			}

			final Point center = new Point(getBlock().getWorld(), filter.x, filter.y, filter.z);
			final char target = statement.charAt(1);
			final List<Player> allPlayers = Arrays.asList(((Server) Spout.getEngine()).getOnlinePlayers());

			switch (target) {

				case NEAREST_PLAYER_CHAR:
					// find the closest suitable player
					List<Player> nearbyPlayers = center.getWorld().getNearbyPlayers(center, filter.maxRadius);
					Player nearbyPlayer = filter.findPlayer(nearbyPlayers);
					if (nearbyPlayer == null) {
						return;
					}
					cmds.add(cmd.replace(statement, nearbyPlayer.getName()));
					break;

				case RANDOM_PLAYER_CHAR:
					// find a random but suitable player in any location
					Collections.shuffle(allPlayers);
					Player player = filter.findPlayer(allPlayers, true);
					if (player == null) {
						return;
					}
					cmds.add(cmd.replace(statement, player.getName()));
					break;

				case ALL_PLAYERS_CHAR:
					// use all players, but filter out any players that are not suitable
					for (Player p : filter.filter(allPlayers, true)) {
						cmds.add(cmd.replace(statement, p.getName()));
					}
					break;
			}
		} else {
			cmds.add(cmd);
		}

		for (String c : cmds) {
			// send commands
			String[] args = c.split(" ");
			String root = args[0];
			ChatArguments arguments = new ChatArguments();
			if (args.length > 1) {
				for (int i = 1; i < args.length; i++) {
					arguments.append(args[i]);
					if (i != args.length - 1) {
						arguments.append(" ");
					}
				}
			}
			// TODO: Argument handling doesn't seem to be working... am I doing it wrong?
			sendMessage("Processing command: " + c);
			processCommand(root, new ChatArguments(arguments));
		}
	}

	private boolean isTarget(char c) {
		return c == NEAREST_PLAYER_CHAR || c == RANDOM_PLAYER_CHAR || c == ALL_PLAYERS_CHAR;
	}

	private String getStatement(String cmd, int index) {
		cmd = cmd.substring(index);
		if (cmd.length() > 6 && cmd.charAt(2) == '[' && cmd.contains("]")) {
			return cmd.substring(0, cmd.indexOf("]") + 1);
		}
		return cmd.substring(0, 2);
	}

	private class PlayerFilter {
		private final Block block = getBlock();
		private final String statement;
		private int x = block.getX();
		private int y = block.getY();
		private int z = block.getZ();
		private int maxRadius = 40;
		private int minRadius = 0;
		private GameMode mode = null;
		private int playerListSize = 0;
		private boolean reversedList = false;
		private int maxExpLevel = 0;
		private int minExpLevel = 1;
		private String objective; // TODO: Implement
		private int maxScore = -1;
		private int minScore = 0;

		public PlayerFilter(String statement) {
			this.statement = statement;
		}

		public void init() throws IllegalArgumentException {
			/*
			 * Accepted syntax:
			 * @<target>[<argument>=<v>,...]
			 *
			 * Arguments:
			 * x: x-coordinate of search center
			 * y: y-coordinate of search center
			 * z: z-coordinate of search center
			 * r: maximum radius of search
			 * rm: minimum radius of search
			 * m: player's gamemode
			 * c: number of players to include (reversed list if negative)
			 * l: maximum experience of players
			 * ml: minimum experience of players
			 * <objective>: maximum score of 'objective'
			 * <objective>_min: minimum score of 'objective'
			 */

			if (!statement.startsWith("@")) {
				throw new IllegalArgumentException("Arguments must begin with an '@' symbol.");
			}

			int startArgs = statement.indexOf("["), endArgs = statement.indexOf("]");
			if (startArgs != -1 && endArgs != -1) {
				// has arguments
				String[] args = statement.substring(startArgs + 1, endArgs).split(",");
				// check first 4 args for the shortcut syntax
				for (int i = 0; i < 4; i++) {
					String arg = args[i];
					if (arg == null || arg.contains("=")) {
						// null arg or arg has a v break
						continue;
					}
					switch (i) {
						case 0:
							x = parseInt(arg);
							break;
						case 1:
							y = parseInt(arg);
							break;
						case 2:
							z = parseInt(arg);
							break;
						case 3:
							maxRadius = parseInt(arg);
							break;
					}
				}

				for (int i = 0; i < args.length; i++) {
					String arg = args[i];
					if (arg == null || !arg.contains("=")) {
						continue;
					}
					String[] s = arg.split("=");
					String a = s[0];
					String v = s[1];
					if (a.equalsIgnoreCase("x")) {
						x = parseInt(v);
					} else if (a.equalsIgnoreCase("y")) {
						y = parseInt(v);
					} else if (a.equalsIgnoreCase("z")) {
						z = parseInt(v);
					} else if (a.equalsIgnoreCase("r")) {
						maxRadius = parseInt(v);
					} else if (a.equalsIgnoreCase("rm")) {
						minRadius = parseInt(v);
					} else if (a.equalsIgnoreCase("m")) {
						mode = GameMode.get(parseInt(v));
					} else if (a.equalsIgnoreCase("c")) {
						int vv = parseInt(v);
						if (vv < 0) {
							reversedList = true;
						}
						playerListSize = Math.abs(vv);
					} else if (a.equalsIgnoreCase("l")) {
						maxExpLevel = parseInt(v);
					} else if (a.equalsIgnoreCase("lm")) {
						minExpLevel = parseInt(v);
					}
					// TODO: Implement scoring
				}
			}
		}

		private int parseInt(String str) {
			try {
				return Integer.parseInt(str);
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException("Specified argument '" + str + "' was a String, int expected.", e);
			}
		}

		public Player findPlayer(List<Player> players, boolean ignoreDistance) {
			for (Player p : players) {
				if (accept(p)) {
					return p;
				}
			}
			return null;
		}

		public Player findPlayer(List<Player> players) {
			return findPlayer(players, false);
		}

		public List<Player> filter(List<Player> players, boolean ignoreDistance) {
			if (reversedList) {
				Collections.reverse(players);
			}
			List<Player> filteredPlayers = new ArrayList<Player>();
			int entries = 0;
			for (Player player : players) {
				if (accept(player, ignoreDistance) && (playerListSize == 0 || entries < playerListSize)) {
					entries++;
					filteredPlayers.add(player);
				}
			}
			return filteredPlayers;
		}

		public List<Player> filter(List<Player> players) {
			return filter(players, false);
		}

		public boolean accept(Player p, boolean ignoreDistance) {
			Point center = new Point(block.getWorld(), x, y, z);
			int lvl = p.add(LevelComponent.class).getLevel();
			int distance = (int) p.getScene().getPosition().distance(center);
			return ((distance >= minRadius && distance <= maxRadius) || ignoreDistance)
					&& (mode == null || mode == p.add(Human.class).getGameMode())
					&& lvl >= minExpLevel && (maxExpLevel == 0 || lvl <= maxExpLevel);
		}

		public boolean accept(Player p) {
			return accept(p, false);
		}

		@Override
		public String toString() {
			return new ToStringBuilder(SpoutToStringStyle.INSTANCE)
					.append("x", x)
					.append("y", y)
					.append("z", z)
					.append("max_radius", maxRadius)
					.append("min_radius", minRadius)
					.append("mode", mode)
					.append("list_size", playerListSize)
					.append("reversed_list", reversedList)
					.append("max_exp_lvl", maxExpLevel)
					.append("min_exp_lvl", minExpLevel)
					.append("objective", objective)
					.append("max_score", maxScore)
					.append("min_score", minScore).build();
		}
	}

	@Override
	public boolean sendMessage(Object... message) {
		return sendMessage(new ChatArguments(message));
	}

	@Override
	public void sendCommand(String command, ChatArguments arguments) {
	}

	@Override
	public void processCommand(String command, ChatArguments arguments) {
		PreCommandEvent event = Spout.getEventManager().callEvent(new PreCommandEvent(this, command, arguments));
		if (event.isCancelled()) {
			return;
		}
		command = event.getCommand();
		arguments = event.getArguments();

		final RootCommand rootCmd = Spout.getEngine().getRootCommand();
		Command cmd = rootCmd.getChild(command);
		if (cmd != null) {
			cmd.process(this, command, arguments, false);
		} else {
			Spout.getLogger().warning("CommandBlock tried to process unknown command: " + command);
		}
	}

	@Override
	public boolean sendMessage(ChatArguments message) {
		if (VanillaConfiguration.COMMAND_BLOCK_VERBOSE.getBoolean()) {
			Spout.getLogger().info("[ " + getName() + "] " + message.getPlainString());
			return true;
		}
		return false;
	}

	@Override
	public boolean sendRawMessage(Object... message) {
		return sendMessage(message);
	}

	@Override
	public boolean sendRawMessage(ChatArguments message) {
		return sendMessage(message);
	}

	@Override
	public Locale getPreferredLocale() {
		return Locale.ENGLISH_US;
	}

	@Override
	public ChatChannel getActiveChannel() {
		for (ChatChannel channel : ChatChannel.Registry.getAllChannels()) {
			if (channel.getName().equalsIgnoreCase(getData().get(VanillaData.CHAT_CHANNEL))) {
				return channel;
			}
		}
		getData().put(VanillaData.CHAT_CHANNEL, CHAT_CHANNEL.getName());
		return CHAT_CHANNEL;
	}

	@Override
	public void setActiveChannel(ChatChannel chan) {
		getData().put(VanillaData.CHAT_CHANNEL, chan.getName());
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
	public String getName() {
		return name;
	}
}
