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
package org.spout.vanilla.plugin.command;

import org.spout.api.Spout;
import org.spout.api.chat.ChatArguments;
import org.spout.api.chat.style.ChatStyle;
import org.spout.api.command.CommandContext;
import org.spout.api.command.CommandSource;
import org.spout.api.command.annotated.Command;
import org.spout.api.entity.Player;
import org.spout.api.exception.CommandException;
import org.spout.api.geo.discrete.Point;
import org.spout.api.inventory.ItemStack;
import org.spout.api.material.Material;
import org.spout.vanilla.api.data.GameMode;
import org.spout.vanilla.api.data.Weather;
import org.spout.vanilla.plugin.VanillaPlugin;
import org.spout.vanilla.plugin.component.inventory.PlayerInventory;
import org.spout.vanilla.plugin.component.living.neutral.Human;
import org.spout.vanilla.plugin.component.substance.material.CommandBlockComponent;
import org.spout.vanilla.plugin.component.world.VanillaSky;
import org.spout.vanilla.plugin.material.VanillaMaterials;

public class CommandBlockCommands {
	
	private final VanillaPlugin plugin;
	
	public CommandBlockCommands(VanillaPlugin plugin) {
		this.plugin = plugin;
	}
	
	@Command(aliases = "clear", usage = "<player> [item] [data]", desc = "Clears your inventory", min = 1, max = 3)
	public void clear(CommandContext args, CommandSource source) throws CommandException {
		Player player = Spout.getEngine().getPlayer(args.getString(0), false);
		if (player == null) {
			throw new CommandException(args.getString(0) + " is not online.");
		}
		
		int cleared = 0;
		if (args.length() == 1) {
			PlayerInventory inv = player.get(PlayerInventory.class);
			for (int i = 0; i < inv.getMain().size(); i++) {
				if (inv.getMain().get(i) != null) {
					cleared += inv.getMain().get(i).getAmount();
					inv.getMain().set(i, null);
				}
			}
			
			for (int i = 0; i < inv.getQuickbar().size(); i++) {
				if (inv.getQuickbar().get(i) != null) {
					cleared += inv.getQuickbar().get(i).getAmount();
					inv.getQuickbar().set(i, null);
				}
			}
		} else {
			Material material;
			if (args.isInteger(1)) {
				material = VanillaMaterials.getMaterial((short) args.getInteger(1));
			} else {
				material = Material.get(args.getString(1));
			}
			
			int data = 0;
			
			if (args.length() > 2) {
				data = args.getInteger(2);
			}
			
			PlayerInventory inv = player.get(PlayerInventory.class);
			for (int i = 0; i < inv.getMain().size(); i++) {
				if (inv.getMain().get(i) != null && inv.getMain().get(i).isMaterial(material) && inv.getMain().get(i).getData() == data) {
					cleared += inv.getMain().get(i).getAmount();
					inv.getMain().set(i, null);
				}
			}
			
			for (int i = 0; i < inv.getQuickbar().size(); i++) {
				if (inv.getQuickbar().get(i) != null && inv.getQuickbar().get(i).isMaterial(material) && inv.getQuickbar().get(i).getData() == data) {
					cleared += inv.getQuickbar().get(i).getAmount();
					inv.getQuickbar().set(i, null);
				}
			}
		}
		
		source.sendMessage("Cleared the inventory of ", player.getName(), ", removing ", cleared, " items.");
	}
	
	@Command(aliases = {"give"}, usage = "<player> <block> [amount] [data]", desc = "Give items to a player", min = 2, max = 4)
	public void give(CommandContext args, CommandSource source) throws CommandException {
		Player player = Spout.getEngine().getPlayer(args.getString(0), false);
		if (player == null) {
			throw new CommandException(args.getString(0) + " is not online.");
		}
		
		Material material;
		if (args.isInteger(1)) {
			material = VanillaMaterials.getMaterial((short) args.getInteger(1));
		} else {
			material = Material.get(args.getString(1));
		}
		
		int quantity = 1;
		int data = 0;
		
		if (args.length() > 2) {
			quantity = args.getInteger(2);
		}
		
		if (args.length() > 3) {
			data = args.getInteger(3);
		}
		
		player.get(PlayerInventory.class).add(new ItemStack(material, data, quantity));
		
		source.sendMessage("Given ", material.getDisplayName(), " * ", quantity, " to ", player.getName());
	}
	
	@Command(aliases = {"time"}, usage = "<add|set> <0-24000|day|night>", desc = "Set the time of the server", min = 2, max = 2)
	public void time(CommandContext args, CommandSource source) throws CommandException {
		int time;
		if (args.isInteger(1)) {
			time = args.getInteger(1);
		} else if (args.getString(1).equalsIgnoreCase("day")) {
			time = 0;
		} else if (args.getString(1).equalsIgnoreCase("night")) {
			time = 12500;
		} else {
			throw new CommandException("\"" + args.getString(1) + "\" is not a valid time.");
		}
		
		VanillaSky sky = VanillaSky.getSky(((CommandBlockComponent) source).getBlock().getWorld());
		
		if (args.getString(0).equalsIgnoreCase("add")) {
			sky.setTime(sky.getTime() + time);
		} else if (args.getString(0).equalsIgnoreCase("set")) {
			sky.setTime(time);
		} else {
			throw new CommandException("Invalid mode.");
		}
		
		source.sendMessage("Set the time to ", time);
	}
	
	@Command(aliases = {"gamemode"}, usage = "<0|1|2|survival|creative|adventure|s|c|a> <player>", desc = "Change a player's game mode", min = 2, max = 2)
	public void gamemode(CommandContext args, CommandSource source) throws CommandException {
		Player player = Spout.getEngine().getPlayer(args.getString(1), false);
		if (player == null) {
			throw new CommandException(args.getString(1) + " is not online.");
		}

		if (!player.has(Human.class)) {
			throw new CommandException("Invalid player!");
		}

		GameMode mode;

		try {
			if (args.isInteger(0)) {
				mode = GameMode.get(args.getInteger(0));
			} else if (args.getString(0).length() == 1) {
				String str = args.getString(0);
				if (str.equalsIgnoreCase("s")) {
					mode = GameMode.SURVIVAL;
				} else if (str.equalsIgnoreCase("c")) {
					mode = GameMode.CREATIVE;
				} else if (str.equalsIgnoreCase("a")) {
					mode = GameMode.ADVENTURE;
				} else {
					throw new Exception();
				}
			} else {
				mode = GameMode.get(args.getString(0));
			}
		} catch (Exception e) {
			throw new CommandException("Invalid GameMode.");
		}

		player.get(Human.class).setGamemode(mode);
		
		source.sendMessage("Set ", player.getName(), "'s game mode to ", mode.name(), " Mode");
	}
	
	@Command(aliases = "xp", usage = "[player] <amount>", desc = "Give/take experience from a player", min = 1, max = 2)
	public void xp(CommandContext args, CommandSource source) throws CommandException {
		
	}
	
	@Command(aliases = "weather", usage = "<clear|rain|thunder> [duration]", desc = "Changes the weather", min = 1, max = 2)
	public void weather(CommandContext args, CommandSource source) throws CommandException {
		Weather forecast;
		if (args.getString(0).equalsIgnoreCase("clear")) {
			forecast = Weather.CLEAR;
		} else if (args.getString(0).equalsIgnoreCase("rain")) {
			forecast = Weather.RAIN;
		} else if (args.getString(0).equalsIgnoreCase("thunder")) {
			forecast = Weather.THUNDERSTORM;
		} else {
			throw new CommandException("\"" + args.getString(0) + "\" is not a valid weather state");
		}
		
		VanillaSky.getSky(((CommandBlockComponent) source).getBlock().getWorld()).setWeather(forecast);
		source.sendMessage("Changing to ", args.getString(0), " weather");
	}
	
	@Command(aliases = "say", usage = "<message>", desc = "Broadcasts a message to the server", min = 1, max = 2)
	public void say(CommandContext args, CommandSource source) throws CommandException {
		source.getActiveChannel().broadcastToReceivers(new ChatArguments("[@]", args.getJoinedString(0)));
	}
	
	@Command(aliases = {"tell"}, usage = "<target> <message>", desc = "Tell a message to a specific user", min = 2)
	public void tell(CommandContext args, CommandSource source) throws CommandException {
		String playerName = args.getString(0);
		ChatArguments message = args.getJoinedString(1);
		Player player = plugin.getEngine().getPlayer(playerName, false);
		if (player != null) {
			player.sendMessage(ChatStyle.GRAY, "(From ", source.getName(), "): ", message);
		} else {
			throw new CommandException("Player '" + playerName + "' not found.");
		}
	}

	@Command(aliases = {"me"}, usage = "<action>", desc = "Emote in the third person", min = 1)
	public void emote(CommandContext args, CommandSource source) throws CommandException {
		source.getActiveChannel().broadcastToReceivers(new ChatArguments("* ", source.getName(), " ", args.getJoinedString(0)));
	}
	
	@Command(aliases = "tp", usage = "<player> <target|x y z>", desc = "Teleports the player.", min = 2, max = 4)
	public void tp(CommandContext args, CommandSource source) throws CommandException {
		Player player = plugin.getEngine().getPlayer(args.getString(0), false);
		Point destination;
		String destinationName;
		
		if (player == null) {
			throw new CommandException("The specified player is not online.");
		}
		
		if (args.length() == 2) {
			Player target = plugin.getEngine().getPlayer(args.getString(1), false);
			if (target == null) {
				throw new CommandException("The targetted player is not online.");
			}
			destination = target.getTransform().getPosition();
			destinationName = target.getName();
		} else if (args.length() == 4) {
			Point position = player.getTransform().getPosition();
			float[] coord = new float[] {position.getX(), position.getY(), position.getZ()};
			for (int i = 1; i < 4; i++) {
				String s = args.getString(i);
				if (s.startsWith("~")) {
					coord[i - 1] += Integer.parseInt(s.substring(1));
				} else {
					coord[i - 1] = Integer.parseInt(s);
				}
			}
			destination = new Point(player.getWorld(), coord[0], coord[1], coord[2]);
			destinationName = "(" + coord[0] + ", " + coord[1] + ", " + coord[2] + ")";
		} else {
			throw new CommandException("Invalid parameters");
		}
		
		player.teleport(destination);
		source.sendMessage("Teleported ", player.getName(), " to ", destinationName);
	}
}
