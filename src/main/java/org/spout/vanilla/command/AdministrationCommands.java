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

/**
 * Commands to emulate core Minecraft admin functions.
 */
public class AdministrationCommands {
	public AdministrationCommands(VanillaPlugin plugin) {
	}

	@Command(aliases = {"gamemode", "gm"}, usage = "[player 1/survival or 2/creative] or [1/survival or 2/creative] (if source is a player)", desc = "Change a player's gamemode", max = 2)
	@CommandPermissions("vanilla.command.gamemode")
	public void gamemode(CommandContext args, CommandSource source) throws CommandException {
		source.sendMessage("Gamemode worked.");
		//TODO implement gamemode.
	}

	@Command(aliases = "xp", usage = "[amount]", desc = "Give/take experience from a player", max = 1)
	@CommandPermissions("vanilla.command.xp")
	public void xp(CommandContext args, CommandSource source) throws CommandException {
		source.sendMessage("Xp worked.");
		//TODO implement xp.
	}

	@Command(aliases = "weather", usage = "[weather] (1/rain, 2/lightning, 3/snow)", desc = "Toggles weather on/off", max = 1)
	@CommandPermissions("vanilla.command.weather")
	public void weather(CommandContext args, CommandSource source) throws CommandException {
		source.sendMessage("Weather worked.");
		//TODO implement weather.
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
