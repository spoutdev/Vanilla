/*
 * This file is part of Vanilla (http://www.spout.org/).
 *
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
 * the MIT license and the SpoutDev license version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license,
 * including the MIT license.
 */
package org.spout.vanilla.command;

import java.util.Set;

import org.spout.api.Spout;
import org.spout.api.command.CommandContext;
import org.spout.api.command.CommandSource;
import org.spout.api.command.annotated.Command;
import org.spout.api.command.annotated.CommandPermissions;
import org.spout.api.exception.CommandException;
import org.spout.api.geo.cuboid.Chunk;
import org.spout.api.player.Player;
import org.spout.api.protocol.NetworkSynchronizer;
import org.spout.vanilla.VanillaPlugin;
import org.spout.vanilla.entity.living.player.CreativePlayer;
import org.spout.vanilla.entity.living.player.MinecraftPlayer;
import org.spout.vanilla.entity.living.player.SurvivalPlayer;
import org.spout.vanilla.protocol.msg.StateChangeMessage;

/**
 * Commands to emulate core Minecraft admin functions.
 */
public class AdministrationCommands {
	public AdministrationCommands(VanillaPlugin plugin) {
	}

	@Command(aliases = {"gamemode", "gm"}, usage = "[player] <1|2> (1 = SURVIVAL, 2 = CREATIVE)", desc = "Change a player's game mode", min = 1, max = 2)
	@CommandPermissions("vanilla.command.gamemode")
	public void gamemode(CommandContext args, CommandSource source) throws CommandException {

		int index = 0;
		Player player;

		if (args.length() == 2) {
			player = Spout.getGame().getPlayer(args.getString(index++), true);

			if (player == null) {
				throw new CommandException(args.getString(0) + " is not online.");
			}
		} else {
			if (!(source instanceof Player)) {
				throw new CommandException("You must be a player to toggle your game mode.");
			}

			player = (Player)source;
		}

		int mode;

		if (args.isInteger(index)) {
			mode = args.getInteger(index);
		} else if (args.getString(index).equalsIgnoreCase("survival")) {
			mode = 1;
		} else if (args.getString(index).equalsIgnoreCase("creative")) {
			mode = 2;
		} else {
			throw new CommandException("A game mode must be either a number between 1 and 2, 'CREATIVE' or 'SURVIVAL'");
		}

		MinecraftPlayer controller;
		String message;

		switch (mode) {
			case 1:
				controller = new SurvivalPlayer(player);
				message = "SURVIVAL.";
				break;
			case 2:
				controller = new CreativePlayer(player);
				message = "CREATIVE.";
				break;
			default:
				throw new CommandException("A game mode must be either a number between 1 and 2, 'CREATIVE' or 'SURVIVAL'");
		}

		if (player.getEntity().getController().getClass().isInstance(controller)) {
			source.sendMessage(player.getName() + " is already in the choosen game mode.");
			return;
		}

		player.sendMessage("Your game mode has been changed to " + message);
		player.getEntity().setController(controller);
		player.getSession().send(new StateChangeMessage((byte)3, (byte)--mode));

		if (!player.equals(source)) {
			source.sendMessage(player.getName() + "'s game mode has been changed to " + message);
		}
	}

	@Command(aliases = "xp", usage = "[player] <amount>", desc = "Give/take experience from a player", max = 2)
	@CommandPermissions("vanilla.command.xp")
	public void xp(CommandContext args, CommandSource source) throws CommandException {
		// If source is player
		if (args.length() == 1) {
			if (source instanceof Player) {
				Player sender = (Player) source;
				int amount = args.getInteger(0);
				source.sendMessage("You have been given " + amount + " xp.");
				// TODO: Give player 'amount' of xp.
			} else {
				throw new CommandException("You must be a player to give yourself xp.");
			}
		}

		// If source is not player
		if (args.length() == 2) {
			Player player = Spout.getGame().getPlayer(args.getString(0), true);
			if (player != null) {
				int amount = args.getInteger(1);
				player.sendMessage("You have been given " + amount + " xp.");
				// TODO: Give player 'amount' of xp.
			} else {
				throw new CommandException(args.getString(0) + " is not online.");
			}
		}
	}

	@Command(aliases = "weather", usage = "<1|2|3> (1 = RAIN, 2 = SNOW, 3 = LIGHTNING)", desc = "Toggles weather on/off", max = 1)
	@CommandPermissions("vanilla.command.weather")
	public void weather(CommandContext args, CommandSource source) throws CommandException {
		if (args.length() == 1) {
			if (args.isInteger(0)) {
				int mode = args.getInteger(0);
				switch (mode) {
					case 1:
						source.sendMessage("Weather set to RAIN.");
						break; // TODO: Switch weather to rain
					case 2:
						source.sendMessage("Weather set to SNOW.");
						break; // TODO: Switch weather to snow
					case 3:
						source.sendMessage("Weather set to LIGHTNING.");
						break; // TODO: Start a storm
					default:
						throw new CommandException("Weather must be between 1 and 3.");
				}
			} else if (args.getString(0).equalsIgnoreCase("rain")) {
				source.sendMessage("Weather set to RAIN.");
				// TODO: Switch weather to rain.
			} else if (args.getString(0).equalsIgnoreCase("snow")) {
				source.sendMessage("Weather set to SNOW.");
				// TODO: Switch weather to snow.
			} else if (args.getString(0).equalsIgnoreCase("lightning")) {
				source.sendMessage("Weather set to LIGHTNING.");
				// TODO: Switch weather to lightning.
			} else {
				throw new CommandException("Weather must be a mode between 1 and 3, 'RAIN', 'SNOW', or 'LIGHTNING'");
			}
		}
	}

	@Command(aliases = "debug", usage = "[type] (/resend /resendall)", desc = "Debug commands", max = 1)
	//@CommandPermissions("vanilla.command.debug")
	public void debug(CommandContext args, CommandSource source) throws CommandException {
		Player player = null;
		if (source instanceof Player) {
			player = (Player)source;
		}
		else {
			player = Spout.getGame().getPlayer(args.getString(1, ""), true);
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
		}
		else if (args.getString(0, "").contains("resend")) {
			player.getNetworkSynchronizer().sendChunk(player.getEntity().getChunk());
			source.sendMessage("Chunk resent");
		}
	}
}
