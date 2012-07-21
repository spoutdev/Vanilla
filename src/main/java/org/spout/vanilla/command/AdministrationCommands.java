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
package org.spout.vanilla.command;

import java.util.Set;

import org.spout.api.Spout;
import org.spout.api.chat.style.ChatStyle;
import org.spout.api.command.CommandContext;
import org.spout.api.command.CommandSource;
import org.spout.api.command.annotated.Command;
import org.spout.api.command.annotated.CommandPermissions;
import org.spout.api.entity.Entity;
import org.spout.api.exception.CommandException;
import org.spout.api.generator.biome.Biome;
import org.spout.api.generator.biome.BiomeGenerator;
import org.spout.api.geo.LoadOption;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Chunk;
import org.spout.api.geo.discrete.Point;
import org.spout.api.inventory.ItemStack;
import org.spout.api.material.Material;
import org.spout.api.material.block.BlockFace;
import org.spout.api.player.Player;
import org.spout.api.protocol.NetworkSynchronizer;

import org.spout.vanilla.VanillaPlugin;
import org.spout.vanilla.configuration.OpConfiguration;
import org.spout.vanilla.configuration.VanillaConfiguration;
import org.spout.vanilla.controller.living.player.VanillaPlayer;
import org.spout.vanilla.controller.world.VanillaSky;
import org.spout.vanilla.data.GameMode;
import org.spout.vanilla.data.Weather;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.util.VanillaBlockUtil;

public class AdministrationCommands {
	private final VanillaPlugin plugin;

	public AdministrationCommands(VanillaPlugin plugin) {
		this.plugin = plugin;
	}

	@Command(aliases = {"tp", "teleport"}, usage = "<player> [player|x] [y] [z]", desc = "Teleport to a location", min = 1, max = 3)
	@CommandPermissions("vanilla.command.tp")
	public void tp(CommandContext args, CommandSource source) throws CommandException {
		Player player = null;
		Point point = null;
		if (args.length() == 1) {
			if (!(source instanceof Player)) {
				throw new CommandException("You must be a player to teleport to another player!");
			}

			player = (Player) source;
			Player to = Spout.getEngine().getPlayer(args.getString(0), true);
			if (to != null) {
				point = to.getEntity().getPosition();
			} else {
				throw new CommandException(args.getString(0) + " is not online!");
			}
		}

		if (args.length() == 2) {
			player = Spout.getEngine().getPlayer(args.getString(0), true);
			if (player == null) {
				throw new CommandException(args.getString(0) + " is not online!");
			}

			Player to = Spout.getEngine().getPlayer(args.getString(1), true);
			if (to != null) {
				point = to.getEntity().getPosition();
			} else {
				throw new CommandException(args.getString(1) + " is not online!");
			}
		}

		if (args.length() == 3) {
			if (!(source instanceof Player)) {
				throw new CommandException("You must be a player to teleport to coordinates!");
			}

			player = (Player) source;
			Entity playerEntity = player.getEntity();
			point = new Point(playerEntity.getWorld(), args.getInteger(0), args.getInteger(1), args.getInteger(2));
		}

		if (player == null) {
			throw new CommandException("Player not found!");
		}

		if (point == null) {
			throw new CommandException("Point not found!");
		}

		point.getWorld().getChunkFromBlock(point);
		player.getEntity().setPosition(point);
		player.getNetworkSynchronizer().setPositionDirty();
	}

	@Command(aliases = {"give"}, usage = "[player] <block> [amount] ", desc = "Lets a player spawn items", min = 1, max = 3)
	@CommandPermissions("vanilla.command.give")
	public void give(CommandContext args, CommandSource source) throws CommandException {
		int index = 0;
		Player player = null;
		Material material = null;

		if (args.length() == 1) {
			if (!(source instanceof Player)) {
				throw new CommandException("You must be a player to give yourself materials!");
			}

			player = (Player) source;
		} else {
			player = Spout.getEngine().getPlayer(args.getString(index++), true);
			if (player == null) {
				throw new CommandException(args.getString(0) + " is not online.");
			}
		}
		VanillaPlayer vplayer = (VanillaPlayer) player.getEntity().getController();

		short data = 0;
		if (args.isInteger(index)) {
			material = VanillaMaterials.getMaterial((short) args.getInteger(index));
		} else {
			String name = args.getString(index);

			if (name.contains(":")) {
				String[] parts = args.getString(index).split(":");
				material = VanillaMaterials.getMaterial(Short.parseShort(parts[0]));
				data = Short.parseShort(parts[1]);
			} else {
				material = Material.get(args.getString(index));
				if (material != null) {
					data = material.getData();
				}
			}
		}

		if (material == null) {
			throw new CommandException(args.getString(index) + " is not a block!");
		}

		int count = args.getInteger(2, 1);

		vplayer.getInventory().getMain().addItem(new ItemStack(material, data, count), false);

		source.sendMessage("Gave ", vplayer.getPlayer().getName(), " ", count, " ", material.getSubMaterial(data).getDisplayName());
	}

	@Command(aliases = {"deop"}, usage = "<player>", desc = "Revoke a players operator status", min = 1, max = 1)
	@CommandPermissions("vanilla.command.deop")
	public void deop(CommandContext args, CommandSource source) throws CommandException {
		if (args.length() != 1) {
			throw new CommandException("Please provide a player to remove operator status!");
		}

		String playerName = args.getString(0);
		OpConfiguration ops = VanillaConfiguration.OPS;
		ops.setOp(playerName, false);
		source.sendMessage(ChatStyle.YELLOW, playerName, " had their operator status revoked!");
		Player player = Spout.getEngine().getPlayer(playerName, true);
		if (player != null && !source.equals(player)) {
			player.sendMessage(ChatStyle.YELLOW, "You had your operator status revoked!");
		}
	}

	@Command(aliases = {"op"}, usage = "<player>", desc = "Make a player an operator", min = 1, max = 1)
	@CommandPermissions("vanilla.command.op")
	public void op(CommandContext args, CommandSource source) throws CommandException {
		if (args.length() != 1) {
			throw new CommandException("Please player to OP!");
		}

		String playerName = args.getString(0);
		OpConfiguration ops = VanillaConfiguration.OPS;
		ops.setOp(playerName, true);
		source.sendMessage(ChatStyle.YELLOW, playerName, " is now an operator!");
		Player player = Spout.getEngine().getPlayer(playerName, true);
		if (player != null && !source.equals(player)) {
			player.sendMessage(ChatStyle.YELLOW, "You are now an operator!");
		}
	}

	@Command(aliases = {"time"}, usage = "<add|set> <0-24000|day|night|dawn|dusk> [world]", desc = "Set the time of the server", min = 2, max = 3)
	@CommandPermissions("vanilla.command.time")
	public void time(CommandContext args, CommandSource source) throws CommandException {
		int time = 0;
		boolean relative = false;
		if (args.getString(0).equalsIgnoreCase("set")) {
			if (args.isInteger(1)) {
				time = args.getInteger(1);
			} else if (args.getString(1).equalsIgnoreCase("day")) {
				time = 6000;
			} else if (args.getString(1).equalsIgnoreCase("night")) {
				time = 18000;
			} else if (args.getString(1).equalsIgnoreCase("dawn")) {
				time = 0;
			} else if (args.getString(1).equalsIgnoreCase("dusk")) {
				time = 12000;
			} else {
				throw new CommandException("'" + args.getString(1) + "' is not a valid time.");
			}
		} else if (args.getString(0).equalsIgnoreCase("add")) {
			relative = true;
			if (args.isInteger(1)) {
				time = args.getInteger(1);
			} else {
				throw new CommandException("Argument to 'add' must be an integer.");
			}
		}

		World world;
		if (args.length() == 3) {
			world = plugin.getEngine().getWorld(args.getString(2));
			if (world == null) {
				throw new CommandException("'" + args.getString(2) + "' is not a valid world.");
			}
		} else if (source instanceof Player) {
			Player player = (Player) source;
			world = player.getEntity().getWorld();
		} else {
			throw new CommandException("You must specify a world.");
		}

		VanillaSky sky = VanillaSky.getSky(world);
		if (sky == null) {
			throw new CommandException("The world '" + args.getString(2) + "' is not available.");
		}

		sky.setTime(relative ? (sky.getTime() + time) : time);
		source.sendMessage("Set ", world.getName(), "'s time to: ", sky.getTime());
	}

	@Command(aliases = {"gamemode", "gm"}, usage = "[player] <0|1|survival|creative> (0 = SURVIVAL, 1 = CREATIVE)", desc = "Change a player's game mode", min = 1, max = 2)
	@CommandPermissions("vanilla.command.gamemode")
	public void gamemode(CommandContext args, CommandSource source) throws CommandException {
		int index = 0;
		Player player;
		if (args.length() == 2) {
			player = Spout.getEngine().getPlayer(args.getString(index++), true);
			if (player == null) {
				throw new CommandException(args.getString(0) + " is not online.");
			}
		} else {
			if (!(source instanceof Player)) {
				throw new CommandException("You must be a player to toggle your game mode.");
			}

			player = (Player) source;
		}

		if (!(player.getEntity().getController() instanceof VanillaPlayer)) {
			throw new CommandException("Invalid player!");
		}

		int mode;
		if (args.isInteger(index)) {
			mode = args.getInteger(index);
		} else if (args.getString(index).equalsIgnoreCase("survival")) {
			mode = 0;
		} else if (args.getString(index).equalsIgnoreCase("creative")) {
			mode = 1;
		} else {
			throw new CommandException("A game mode must be either a number between 1 and 2, 'CREATIVE' or 'SURVIVAL'");
		}

		String message;
		VanillaPlayer p = (VanillaPlayer) player.getEntity().getController();
		switch (mode) {
			case 0:
				p.setGameMode(GameMode.SURVIVAL);
				message = "SURVIVAL";
				break;
			case 1:
				p.setGameMode(GameMode.CREATIVE);
				message = "CREATIVE";
				break;
			default:
				throw new CommandException("A game mode must be either a number between 1 and 2, 'CREATIVE' or 'SURVIVAL'");
		}
		if (!player.equals(source)) {
			source.sendMessage(p.getPlayer().getName(), "'s game mode has been changed to ", message, ".");
		}
	}

	@Command(aliases = "xp", usage = "[player] <amount>", desc = "Give/take experience from a player", max = 2)
	@CommandPermissions("vanilla.command.xp")
	public void xp(CommandContext args, CommandSource source) throws CommandException {
		// If source is player
		if (args.length() == 1) {
			if (source instanceof Player) {
				@SuppressWarnings("unused")
				Player sender = (Player) source;
				int amount = args.getInteger(0);
				source.sendMessage("You have been given ", amount, " xp.");
				// TODO: Give player 'amount' of xp.
			} else {
				throw new CommandException("You must be a player to give yourself xp.");
			}
		}

		// If source is not player
		if (args.length() == 2) {
			Player player = Spout.getEngine().getPlayer(args.getString(0), true);
			if (player != null) {
				int amount = args.getInteger(1);
				player.sendMessage("You have been given ", amount, " xp.");
				// TODO: Give player 'amount' of xp.
			} else {
				throw new CommandException(args.getString(0) + " is not online.");
			}
		}
	}

	@Command(aliases = "weather", usage = "<0|1|2> (0 = CLEAR, 1 = RAIN/SNOW, 2 = THUNDERSTORM) [world]", desc = "Changes the weather", min = 1, max = 2)
	@CommandPermissions("vanilla.command.weather")
	public void weather(CommandContext args, CommandSource source) throws CommandException {
		World world = null;
		if (source instanceof Player && args.length() == 1) {
			Player player = (Player) source;
			world = player.getEntity().getWorld();
		} else if (args.length() == 2) {
			world = plugin.getEngine().getWorld(args.getString(1));
			if (world == null) {
				throw new CommandException("Invalid world '" + args.getString(1) + "'.");
			}
		} else {
			throw new CommandException("You need to specify a world.");
		}

		Weather weather;
		if (args.isInteger(0)) {
			int mode = args.getInteger(0);
			switch (mode) {
				case 0:
					source.sendMessage("Weather set to CLEAR.");
					weather = Weather.CLEAR;
					break;
				case 1:
					source.sendMessage("Weather set to RAIN/SNOW.");
					weather = Weather.RAIN;
					break;
				case 2:
					source.sendMessage("Weather set to THUNDERSTORM.");
					weather = Weather.THUNDERSTORM;
					break;
				default:
					throw new CommandException("Weather must be between 0 and 2.");
			}
		} else if (args.getString(0).equalsIgnoreCase("clear")) {
			source.sendMessage("Weather set to CLEAR.");
			weather = Weather.CLEAR;
		} else if (args.getString(0).equalsIgnoreCase("rain") || args.getString(0).equalsIgnoreCase("snow")) {
			source.sendMessage("Weather set to RAIN/SNOW.");
			weather = Weather.RAIN;
		} else if (args.getString(0).equalsIgnoreCase("thunderstorm")) {
			source.sendMessage("Weather set to THUNDERSTORM.");
			weather = Weather.THUNDERSTORM;
		} else {
			throw new CommandException("Weather must be a mode between 0 and 2, 'CLEAR', 'RAIN', 'SNOW', or 'THUNDERSTORM'");
		}

		VanillaSky sky = VanillaSky.getSky(world);
		if (sky == null) {
			throw new CommandException("The sky of world '" + world.getName() + "' is not availible.");
		}

		sky.setWeather(weather);
	}

	@Command(aliases = "debug", usage = "[type] (/resend /resendall)", desc = "Debug commands", max = 2)
	@CommandPermissions("vanilla.command.debug")
	public void debug(CommandContext args, CommandSource source) {
		Player player = null;
		if (source instanceof Player) {
			player = (Player) source;
		} else {
			player = Spout.getEngine().getPlayer(args.getString(1, ""), true);
			if (player == null) {
				source.sendMessage("Must be a player or send player name in arguments");
				return;
			}
		}

		if (args.getString(0, "").contains("resendall")) {
			NetworkSynchronizer network = player.getNetworkSynchronizer();
			Set<Chunk> chunks = network.getActiveChunks();
			for (Chunk c : chunks) {
				network.sendChunk(c);
			}

			source.sendMessage("All chunks resent");
		} else if (args.getString(0, "").contains("resend")) {
			player.getNetworkSynchronizer().sendChunk(player.getEntity().getChunk());
			source.sendMessage("Chunk resent");
		} else if (args.getString(0, "").contains("relight")) {
			for (Chunk chunk : VanillaBlockUtil.getChunkColumn(player.getEntity().getChunk())) {
				chunk.initLighting();
			}
			source.sendMessage("Chunk lighting is being initialized");
		}
	}

	@Command(aliases = {"kill"}, usage = "<player>", desc = "Kill yourself or another player", min = 0, max = 1)
	@CommandPermissions("vanilla.command.kill")
	public void setHealth(CommandContext args, CommandSource source) throws CommandException {
		if (args.length() < 1) {
			if (!(source instanceof Player)) {
				throw new CommandException("Don't be silly...you cannot kill yourself as the console.");
			}
			((Player) source).getEntity().kill();
		} else {
			Player victim = Spout.getEngine().getPlayer(args.getString(0), true);
			if (victim != null) {
				victim.getEntity().kill();
			}
		}
	}

	@Command(aliases = {"version", "vr"}, usage = "", desc = "Print out the version information for Vanilla", min = 0, max = 0)
	@CommandPermissions("vanilla.command.version")
	public void getVersion(CommandContext args, CommandSource source) {
		source.sendMessage("Vanilla ", plugin.getDescription().getVersion(), " (Implementing Minecraft protocol v", plugin.getDescription().getData("protocol").get(), ")");
		source.sendMessage("Powered by Spout " + Spout.getEngine().getVersion(), " (Implementing SpoutAPI ", Spout.getAPIVersion(), ")");
	}

	@Command(aliases = {"biome"}, usage = "", desc = "Print out the name of the biome at the current location", min = 0, max = 0)
	@CommandPermissions("vanilla.command.biome")
	public void getBiomeName(CommandContext args, CommandSource source) throws CommandException {
		if (!(source instanceof Player)) {
			throw new CommandException("Only a player may call this command.");
		}
		Player player = (Player) source;
		if (!(player.getEntity().getPosition().getWorld().getGenerator() instanceof BiomeGenerator)) {
			throw new CommandException("This map does not appear to have any biome data.");
		}
		Point pos = player.getEntity().getPosition();
		Biome biome = pos.getWorld().getBiomeType(pos.getBlockX(), pos.getBlockY(), pos.getBlockZ());
		source.sendMessage("Current biome: ", (biome != null ? biome.getName() : "none"));
	}
}
