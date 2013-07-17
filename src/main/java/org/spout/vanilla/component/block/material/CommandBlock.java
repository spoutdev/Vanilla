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
package org.spout.vanilla.component.block.material;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

import org.spout.api.Server;
import org.spout.api.Spout;
import org.spout.api.command.Command;
import org.spout.api.command.CommandArguments;
import org.spout.api.command.CommandSource;
import org.spout.api.data.ValueHolder;
import org.spout.api.entity.Player;
import org.spout.api.event.Cause;
import org.spout.api.event.server.PreCommandEvent;
import org.spout.api.exception.CommandException;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.discrete.Point;
import org.spout.api.lang.Locale;
import org.spout.api.util.SpoutToStringStyle;

import org.spout.vanilla.ChatStyle;
import org.spout.vanilla.VanillaPlugin;
import org.spout.vanilla.component.block.VanillaBlockComponent;
import org.spout.vanilla.component.entity.living.Human;
import org.spout.vanilla.component.entity.misc.Level;
import org.spout.vanilla.data.GameMode;
import org.spout.vanilla.data.VanillaData;
import org.spout.vanilla.data.configuration.VanillaConfiguration;
import org.spout.vanilla.scoreboard.Objective;
import org.spout.vanilla.scoreboard.Scoreboard;
import org.spout.vanilla.scoreboard.Team;

public class CommandBlock extends VanillaBlockComponent implements CommandSource {
	public static final char TARGET_CHAR = '@';
	public static final char NEAREST_PLAYER_CHAR = 'p';
	public static final char RANDOM_PLAYER_CHAR = 'r';
	public static final char ALL_PLAYERS_CHAR = 'a';
	public static final String NEAREST_PLAYER = "" + TARGET_CHAR + NEAREST_PLAYER_CHAR;
	public static final String RANDOM_PLAYER = "" + TARGET_CHAR + RANDOM_PLAYER_CHAR;
	public static final String ALL_PLAYERS = "" + TARGET_CHAR + ALL_PLAYERS_CHAR;
	private String name = "" + TARGET_CHAR;

	/**
	 * Sets the name displayed when a command is executed.
	 * @param name to display
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Sets the command executed when {@link #execute()} is called.
	 * @param cmd to execute
	 * @param cause of command, can be null
	 */
	public void setCommand(String cmd, Cause<?> cause) {
		if (cause != null && cause.getSource() instanceof CommandSource) {
			CommandSource cmdSource = (CommandSource) cause.getSource();
			String prefix = VanillaPlugin.getInstance().getPrefix();
			if (!cmdSource.hasPermission("vanilla.commandblock." + cmd.split(" ")[0])) {
				cmdSource.sendMessage(prefix + ChatStyle.RED + " You don't have permission to do that.");
				return;
			}
			cmdSource.sendMessage(prefix + ChatStyle.WHITE + "CommandDescription set: " + ChatStyle.GREEN + cmd);
		}

		if (!cmd.startsWith("/")) {
			cmd = "say " + cmd;
		} else {
			cmd = cmd.replaceFirst("/", "");
		}

		if (Spout.debugMode()) {
			Spout.getLogger().info("CommandBlock at " + getPoint().toString() + " command set to " + cmd);
		}

		getDatatable().put(VanillaData.COMMAND, cmd);
	}

	/**
	 * Sets the command to be executed on {@link #execute()}.
	 * @param cmd to execute
	 */
	public void setCommand(String cmd) {
		setCommand(cmd, null);
	}

	/**
	 * Returns the command to execute.
	 * @return command to execute
	 */
	public String getCommand() {
		return getDatatable().get(VanillaData.COMMAND);
	}

	/**
	 * Sets whether the command block is powered.
	 * @param powered if this command block is powered
	 */
	public void setPowered(boolean powered) {
		if (isPowered() != powered) {
			getDatatable().put(VanillaData.IS_POWERED, powered);
			if (powered) {
				execute();
			}
		}
	}

	/**
	 * Returns true if this command block is powered.
	 * @return true if powered
	 */
	public boolean isPowered() {
		return getDatatable().get(VanillaData.IS_POWERED);
	}

	/**
	 * Executes the command specified by {@link #getCommand()}.
	 */
	public void execute() {
		if (!(VanillaPlugin.getInstance().getEngine() instanceof Server)) {
			throw new IllegalStateException("CommandBlocks are run from the server.");
		}

		if (Spout.debugMode()) {
			Spout.getLogger().info("Executing CommandBlock at: " + getPoint().toString());
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
				Spout.getLogger().log(java.util.logging.Level.WARNING, "Could not execute CommandBlock at "
						+ getBlock().getPosition().toString() + " because of illegal syntax.", e);
			}

			if (Spout.debugMode()) {
				Spout.getLogger().info("Statement: " + statement);
				Spout.getLogger().info("Corresponding Filter: " + filter);
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
					Player player = filter.findPlayer(allPlayers);
					if (player == null) {
						return;
					}
					cmds.add(cmd.replace(statement, player.getName()));
					break;

				case ALL_PLAYERS_CHAR:
					// use all players, but filter out any players that are not suitable
					for (Player p : filter.filter(allPlayers)) {
						cmds.add(cmd.replace(statement, p.getName()));
					}
					break;
			}
		} else {
			cmds.add(cmd);
		}

		for (String c : cmds) {
			// send commands
			String root = c.split(" ")[0];
			int argsIndex = c.indexOf(' ') + 1;
			String[] args = argsIndex > 0 ? c.substring(argsIndex).split(" ") : new String[0];
			processCommand(root, args);
		}
	}

	private static boolean isTarget(char c) {
		return c == NEAREST_PLAYER_CHAR || c == RANDOM_PLAYER_CHAR || c == ALL_PLAYERS_CHAR;
	}

	private static String getStatement(String cmd, int index) {
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
		private int maxRadius = -1;
		private int minRadius = -1;
		private GameMode mode = null;
		private int playerListSize = 0;
		private boolean reversedList = false;
		private int maxExpLevel = -1;
		private int minExpLevel = -1;
		private String playerName = null;
		private boolean playerNameInverted = false;
		private String teamName = null;
		private boolean teamNameInverted = false;
		private final Map<String, Integer> minScores = new HashMap<String, Integer>();
		private final Map<String, Integer> maxScores = new HashMap<String, Integer>();

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

				for (String arg : args) {
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
					} else if (a.equals("name")) {
						playerNameInverted = v.startsWith("!");
						playerName = playerNameInverted ? v.substring(1) : v;
					} else if (a.equals("team")) {
						teamNameInverted = v.startsWith("!");
						teamName = teamNameInverted ? v.substring(1) : v;
					} else if (a.startsWith("score_")) {
						int value = parseInt(v);
						String obj = a.replace("score_", "");
						if (a.endsWith("_min")) {
							minScores.put(obj.replace("_min", ""), value);
						} else {
							maxScores.put(obj, value);
						}
					}
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

		public Player findPlayer(List<Player> players) {
			for (Player p : players) {
				if (accept(p)) {
					return p;
				}
			}
			return null;
		}

		public List<Player> filter(List<Player> players) {
			if (reversedList) {
				Collections.reverse(players);
			}
			List<Player> filteredPlayers = new ArrayList<Player>();
			int entries = 0;
			for (Player player : players) {
				if (accept(player) && (playerListSize == 0 || entries < playerListSize)) {
					entries++;
					filteredPlayers.add(player);
				}
			}
			return filteredPlayers;
		}

		public boolean accept(Player p) {
			Point center = new Point(block.getWorld(), x, y, z);
			int distance = Math.round((float) p.getScene().getPosition().distance(center)); // the distance from the cmd block

			// get various principles (these all default to true if not specified)
			boolean closeEnough = (minRadius == -1 || distance >= minRadius) && (maxRadius == -1 || distance <= maxRadius);
			boolean correctMode = mode == null || mode.equals(p.get(Human.class).getGameMode());
			int lvl = p.get(Level.class).getLevel();
			boolean enoughExp = (minExpLevel == -1 || lvl >= minExpLevel) && (maxExpLevel == -1 || lvl <= maxExpLevel);

			// check if the name is correct if specified
			boolean correctName = playerName == null; // default to whether the player name is set
			if (playerName != null) {
				if (playerNameInverted) {
					// make sure the player does not have the specified name
					correctName = !p.getName().equalsIgnoreCase(playerName);
				} else {
					// make sure the player does have the specified name
					correctName = p.getName().equals(playerName);
				}
			}

			// check if the team is correct if specified
			Scoreboard board = p.get(Scoreboard.class);
			boolean correctTeam = teamName == null; // default to whether the team name is set
			if (teamName != null && board != null) {
				Team team = board.getTeam(teamName);
				if (team != null) {
					if (teamNameInverted) {
						// make sure the player is not on the specified team
						correctTeam = !team.getPlayerNames().contains(p.getName());
					} else {
						// make sure the player is on the specified team
						correctTeam = team.getPlayerNames().contains(p.getName());
					}
				}
			}

			// make sure the player has the right amount of points for the specified objectives
			boolean enoughPoints = minScores.isEmpty() && maxScores.isEmpty(); // default to whether there are any objectives set
			if (board != null) {
				// first make sure the player has the minimum requirements
				for (Map.Entry<String, Integer> e : minScores.entrySet()) {
					Objective obj = board.getObjective(e.getKey());
					if (obj == null) {
						continue;
					}

					int score = obj.getScore(p.getName()); // player's score for specified objective
					int minScore = e.getValue(); // specified minimum score
					enoughPoints = score >= minScore;
					if (!enoughPoints) {
						// not enough? break.
						break;
					}
				}

				// next make sure the player isn't over the maximums
				for (Map.Entry<String, Integer> e : maxScores.entrySet()) {
					Objective obj = board.getObjective(e.getKey());
					if (obj == null) {
						continue;
					}

					int score = obj.getScore(p.getName()); // player's score for specified objective
					int maxScore = e.getValue(); // specified maximum score
					enoughPoints = score <= maxScore;
					if (!enoughPoints) {
						// too many points? break.
						break;
					}
				}
			}

			if (Spout.debugMode()) {
				Spout.log("Trying to accept: " + p.getName());
				Spout.log("\tDistance From Origin: " + distance);
				Spout.log("\tClose Enough: " + closeEnough);
				Spout.log("\tCorrect Mode: " + correctMode);
				Spout.log("\tEnough Exp: " + enoughExp);
				Spout.log("\tCorrect Name: " + correctName);
				Spout.log("\tCorrect Team: " + correctTeam);
				Spout.log("\tEnough Points: " + enoughPoints);
			}

			return closeEnough && correctMode && enoughExp && correctName && correctTeam && enoughPoints;
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
					.append("player_name", playerName)
					.append("player_name_inverted", playerNameInverted)
					.append("team_name", teamName)
					.append("team_name_inverted", teamNameInverted)
					.append("min_scores", minScores)
					.append("max_scores", maxScores)
					.toString();
		}
	}

	@Override
	public void sendMessage(String message) {
		if (VanillaConfiguration.COMMAND_BLOCK_VERBOSE.getBoolean()) {
			Spout.getLogger().info("[ " + getName() + "] " + message);
		}
	}

	@Override
	public void sendCommand(String command, String... args) {
	}

	@Override
	public void processCommand(String command, String... args) {
		PreCommandEvent event = VanillaPlugin.getInstance().getEngine().getEventManager().callEvent(new PreCommandEvent(this, command, args));
		if (event.isCancelled()) {
			return;
		}
		command = event.getCommand();
		CommandArguments arguments = event.getArguments();

		if (Spout.debugMode()) {
			Spout.getLogger().info("Executing: " + command + " " + arguments);
		}

		Command cmd = Spout.getCommandManager().getCommand(command, false);
		if (cmd == null) {
			Spout.getLogger().warning("CommandBlock tried to send unknown command: " + command);
			return;
		}

		try {
			cmd.process(this, args);
		} catch (CommandException e) {
			sendMessage(ChatStyle.RED + e.getMessage());
		}
	}

	@Override
	public Locale getPreferredLocale() {
		return Locale.ENGLISH_US;
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
