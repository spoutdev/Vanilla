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

import org.spout.api.Spout;
import org.spout.api.command.CommandContext;
import org.spout.api.command.CommandSource;
import org.spout.api.command.annotated.Command;
import org.spout.api.command.annotated.CommandPermissions;
import org.spout.api.exception.CommandException;
import org.spout.api.player.Player;
import org.spout.vanilla.VanillaPlugin;

/**
 * Commands to emulate core Minecraft admin functions.
 */
public class AdministrationCommands {
	
	public AdministrationCommands(VanillaPlugin plugin) {
		
	}
	
	@Command(aliases = {"gamemode", "gm"}, usage = "[player] <1|2> (1 = SURVIVAL, 2 = CREATIVE)", desc = "Change a player's gamemode", max = 2)
	@CommandPermissions("vanilla.command.gamemode")
	public void gamemode(CommandContext args, CommandSource source) throws CommandException {
		// If source is player
		if (args.length() == 1) {
			if (source instanceof Player) {
				Player sender = (Player) source;
				if (this.isInteger(args.getString(0))) {
					int mode = args.getInteger(0);
					switch (mode) {
						case 1:
							source.sendMessage("Your gamemode has been switched to SURVIVAL."); 
							break; // TODO: Switch sender to survival
						case 2:
							source.sendMessage("Your gamemode has been switched to CREATIVE."); 
							break; // TODO: Switch sender to creative
						default:
							throw new CommandException("You must be a player to toggle your gamemode.");
					}
				} else if (args.getString(0).equalsIgnoreCase("creative")) {
					source.sendMessage("Your gamemode has been switched to CREATIVE.");
					// TODO: Switch sender to creative
				} else if (args.getString(0).equalsIgnoreCase("survival")) {
					source.sendMessage("Your gamemode has been switched to SURVIVAL.");
					// TODO: Switch sender to survival
				} else {
					throw new CommandException("A gamemode must be either a number between 1 and 2, 'CREATIVE' or 'SURVIVAL'");
				}
			} else {
				throw new CommandException("You must be a player to toggle your gamemode.");
			}
		}
		
		// If source is not player
		if (args.length() == 2) {
			Player player = Spout.getGame().getPlayer(args.getString(0), true);
			if (player != null) {
				if (this.isInteger(args.getString(1))) {
					int mode = args.getInteger(1);
					switch (mode) {
						case 1: 
							source.sendMessage(player.getName() + "'s gamemode has been switched to SURVIVAL.");
							player.sendMessage("Your gamemode has been switched to SURVIVAL."); 
							break; // TODO: Switch player to survival
						case 2: 
							source.sendMessage(player.getName() + "'s gamemode has been switched to SURVIVAL.");
							player.sendMessage("Your gamemode has been switched to CREATIVE."); 
							break; // TODO: Switch player to creative
						default: 
							throw new CommandException("A gamemode must be between 1 and 2.");
					}
				} else if (args.getString(1).equalsIgnoreCase("creative")) {
					source.sendMessage(player.getName() + "'s gamemode has been switched to CREATIVE.");
					player.sendMessage("Your gamemode has been switched to CREATIVE.");
					// TODO: Switch player to creative
				} else if (args.getString(1).equalsIgnoreCase("survival")) {
					source.sendMessage(player.getName() + "'s gamemode has been switched to SURVIVAL.");
					player.sendMessage("Your gamemode has been switched to SURVIVAL.");
					// TODO: Switch player to survival
				} else {
					throw new CommandException("A gamemode must be either a number between 1 and 2, 'CREATIVE' or 'SURVIVAL'");
				}
			} else {
				throw new CommandException(args.getString(0) + " is not online.");
			}
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
			if (this.isInteger(args.getString(0))) {
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
	
	public boolean isInteger(String arg) {
		try {
			Integer.parseInt(arg);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}
}