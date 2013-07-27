/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011 Spout LLC <http://www.spout.org/>
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
package org.spout.vanilla.command;

import gnu.trove.iterator.TLongIterator;
import gnu.trove.list.linked.TLongLinkedList;
import org.spout.api.Client;
import org.spout.api.Engine;
import org.spout.api.Server;
import org.spout.api.command.CommandArguments;
import org.spout.api.command.CommandSource;
import org.spout.api.command.annotated.CommandDescription;
import org.spout.api.command.annotated.Filter;
import org.spout.api.command.annotated.Permissible;
import org.spout.api.command.filter.PlayerFilter;
import org.spout.api.entity.Player;
import org.spout.api.exception.ArgumentParseException;
import org.spout.api.exception.CommandException;
import org.spout.api.generator.biome.Biome;
import org.spout.api.generator.biome.BiomeGenerator;
import org.spout.api.geo.World;
import org.spout.api.geo.discrete.Point;
import org.spout.api.inventory.Inventory;
import org.spout.api.inventory.ItemStack;
import org.spout.api.material.Material;
import org.spout.api.scheduler.TaskPriority;
import org.spout.api.util.concurrent.AtomicFloat;
import org.spout.vanilla.ChatStyle;
import org.spout.vanilla.VanillaPlugin;
import org.spout.vanilla.component.entity.inventory.PlayerInventory;
import org.spout.vanilla.component.entity.living.Human;
import org.spout.vanilla.component.entity.misc.Health;
import org.spout.vanilla.component.entity.misc.Level;
import org.spout.vanilla.component.world.sky.Sky;
import org.spout.vanilla.data.GameMode;
import org.spout.vanilla.data.Time;
import org.spout.vanilla.data.Weather;
import org.spout.vanilla.data.configuration.OpConfiguration;
import org.spout.vanilla.data.configuration.VanillaConfiguration;
import org.spout.vanilla.event.cause.HealthChangeCause;

public class AdministrationCommands {
	private final VanillaPlugin plugin;
	private final TicksPerSecondMonitor tpsMonitor;

	public AdministrationCommands(VanillaPlugin plugin) {
		this.plugin = plugin;
		tpsMonitor = new TicksPerSecondMonitor();
		plugin.setTPSMonitor(tpsMonitor);
		plugin.getEngine().getScheduler().scheduleSyncRepeatingTask(plugin, tpsMonitor, 0, 50, TaskPriority.CRITICAL);
	}

	private Engine getEngine() {
		return plugin.getEngine();
	}

	@CommandDescription(aliases = "clear", usage = "[player] [item] [data]", desc = "Clears the target's inventory")
	@Permissible("vanilla.command.clear")
	public void clear(CommandSource source, CommandArguments args) throws CommandException {
		Player player = args.popPlayerOrMe("player", source);
		Material filter = VanillaArgumentTypes.popMaterial("filter", args);
		Integer data = args.popInteger("data");
		args.assertCompletelyParsed();


		PlayerInventory inv = player.get(PlayerInventory.class);
		if (inv == null) {
			throw new CommandException(player.getName() + " doesn't have a inventory!");
		} else {
			// Count the items and clear the inventory
			Inventory[] inventories = new Inventory[]{inv.getMain(), inv.getQuickbar(), inv.getArmor()};
			int cleared = 0;
			for (int k = 0; k < inventories.length; k++) {
				for (int i = 0; i < inventories[k].size(); i++) {
					if (inventories[k].get(i) != null && (filter == null || inventories[k].get(i).isMaterial(filter)) && (data == null || inventories[k].get(i).getData() == data)) {
						cleared += inventories[k].get(i).getAmount();
						inventories[k].set(i, null);
					}
				}
			}

			if (cleared == 0) {
				throw new CommandException("Inventory is already empty");
			}

			source.sendMessage(plugin.getPrefix() + ChatStyle.GREEN + "Cleared the inventory of " + player.getName() + ", removing " + cleared + " items.");
		}
	}

	@CommandDescription(aliases = {"give"}, usage = "[player] <block> [amount] [data]", desc = "Lets a player spawn items")
	@Permissible("vanilla.command.give")
	public void give(CommandSource source, CommandArguments args) throws CommandException {
		Player player;
		if (args.length() != 1) {
			player = args.popPlayerOrMe("player", source);
		} else {
			player = args.checkPlayer(source);
		}
		Material material = VanillaArgumentTypes.popMaterial("material", args);
		int quantity = args.popInteger("quantity");
		int data = args.popInteger("data");
		args.assertCompletelyParsed();


		PlayerInventory inventory = player.get(PlayerInventory.class);
		if (inventory != null) {
			inventory.add(new ItemStack(material, data, quantity));
			source.sendMessage(plugin.getPrefix() + ChatStyle.GREEN + "Gave "
					+ ChatStyle.WHITE + player.getName() + " " + quantity + ChatStyle.GREEN + " of " + ChatStyle.WHITE
					+ material.getDisplayName());
		} else {
			throw new CommandException(player.getName() + " doesn't have a inventory!");
		}
	}

	@CommandDescription(aliases = {"deop"}, usage = "<player>", desc = "Revoke a players operator status")
	@Permissible("vanilla.command.deop")
	public void deop(CommandSource source, CommandArguments args) throws CommandException {
		String playerName = args.popString("player");
		args.assertCompletelyParsed();

		OpConfiguration ops = VanillaConfiguration.OPS;
		if (!ops.isOp(playerName)) {
			source.sendMessage(plugin.getPrefix() + playerName + ChatStyle.RED + " is not an operator!");
			return;
		}

		ops.setOp(playerName, false);
		source.sendMessage(plugin.getPrefix() + playerName + ChatStyle.RED + " had their operator status revoked!");
		if (getEngine() instanceof Server) {
			Player player = ((Server) getEngine()).getPlayer(playerName, true);
			if (player != null && !source.equals(player)) {
				player.sendMessage(plugin.getPrefix() + ChatStyle.RED + "You had your operator status revoked!");
			}
		}
	}

	@CommandDescription(aliases = {"op"}, usage = "<player>", desc = "Make a player an operator")
	@Permissible("vanilla.command.op")
	public void op(CommandSource source, CommandArguments args) throws CommandException {
		String playerName = args.popString("player");
		args.assertCompletelyParsed();

		OpConfiguration ops = VanillaConfiguration.OPS;
		if (ops.isOp(playerName)) {
			source.sendMessage(plugin.getPrefix() + ChatStyle.RED + playerName + " is already an operator!");
			return;
		}

		ops.setOp(playerName, true);
		source.sendMessage(plugin.getPrefix() + ChatStyle.RED + playerName + " is now an operator!");
		if (getEngine() instanceof Server) {
			Player player = ((Server) getEngine()).getPlayer(playerName, true);
			if (player != null && !source.equals(player)) {
				player.sendMessage(plugin.getPrefix() + ChatStyle.YELLOW + "You are now an operator!");
			}
		}
	}

	@CommandDescription(aliases = {"time"}, usage = "<add|set> <0-24000|day|night|dawn|dusk> [world]", desc = "Set the time of the server")
	@Permissible("vanilla.command.time")
	public void time(CommandSource source, CommandArguments args) throws CommandException {
		String relativeCheck = args.currentArgument("relative");
		boolean relative;
		if (relativeCheck.equalsIgnoreCase("add") || relativeCheck.equalsIgnoreCase("set")) {
			relative = args.success("relative", relativeCheck.equalsIgnoreCase("add"));
		} else {
			throw args.failure("relative", "Value must be either 'add' or 'set'", true, "add", "set");
		}

		long time;
		try {
			time = args.popInteger("time");
		} catch (ArgumentParseException ex) {
			if (!args.hasMore() || relative) {
				throw ex;
			} else {
				try {
					time = args.success("time", Time.get(args.currentArgument("time")).getTime());
				} catch (IllegalArgumentException e) {
					throw args.failure("time", args.currentArgument("time") + " is not a valid time.", true);
				}
			}
		}
		World world = args.popWorld("world", source);
		args.assertCompletelyParsed();

		Sky sky = world.get(Sky.class);
		if (sky == null) {
			throw new CommandException("The sky for " + world.getName() + " is not available.");
		}
		sky.setTime(relative ? (sky.getTime() + time) : time);
		if (getEngine() instanceof Client) {
			source.sendMessage(plugin.getPrefix() + ChatStyle.GREEN + "You set "
					+ ChatStyle.WHITE + world.getName() + ChatStyle.GREEN
					+ " to time: " + ChatStyle.WHITE + sky.getTime());
		} else {
			((Server) getEngine()).broadcastMessage(plugin.getPrefix() + ChatStyle.WHITE
					+ world.getName() + ChatStyle.GREEN + " set to: " + ChatStyle.WHITE + sky.getTime());
		}
	}

	@CommandDescription(aliases = {"gamemode", "gm"}, usage = "<0|1|2|survival|creative|adventure|s|c|a> [player] (0 = SURVIVAL, 1 = CREATIVE, 2 = ADVENTURE)", desc = "Change a player's game mode")
	@Permissible("vanilla.command.gamemode")
	public void gamemode(CommandSource source, CommandArguments args) throws CommandException {
		GameMode mode = VanillaArgumentTypes.popGameMode("gamemode", args);
		Player player = args.popPlayerOrMe("player", source);
		args.assertCompletelyParsed();

		Human human = player.get(Human.class);
		if (human == null) {
			throw new CommandException("Invalid player!");
		}


		human.setGamemode(mode);

		if (!player.equals(source)) {
			source.sendMessage(plugin.getPrefix() + ChatStyle.WHITE + player.getName()
					+ "'s " + ChatStyle.GREEN + "gamemode has been changed to "
					+ ChatStyle.WHITE + mode.name() + ChatStyle.GREEN + ".");
		}
	}

	@CommandDescription(aliases = "xp", usage = "[player] <amount>", desc = "Give/take experience from a player")
	@Permissible("vanilla.command.xp")
	public void xp(CommandSource source, CommandArguments args) throws CommandException {
		Player player = args.popPlayerOrMe("player", source);
		int amount = args.popInteger("amount");
		args.assertCompletelyParsed();

		Level level = player.get(Level.class);
		if (level == null) {
			throw new CommandException(player.getDisplayName() + " does not have experience.");
		}

		level.addExperience(amount);
		player.sendMessage(plugin.getPrefix() + ChatStyle.GREEN + "Your experience has been set to " + ChatStyle.WHITE + amount + ChatStyle.GREEN + ".");
	}

	@CommandDescription(aliases = "weather", usage = "<0|1|2|clear|rain|thunder> (0 = CLEAR, 1 = RAIN/SNOW, 2 = THUNDERSTORM) [world]", desc = "Changes the weather")
	@Permissible("vanilla.command.weather")
	public void weather(CommandSource source, CommandArguments args) throws CommandException {
		Weather weather = args.popEnumValue("weather", Weather.class);
		World world = args.popWorld("world", source);
		args.assertCompletelyParsed();

		Sky sky = world.get(Sky.class);
		if (sky == null) {
			throw new CommandException("The sky of world '" + world.getName() + "' is not available.");
		}

		sky.setWeather(weather);
		String message;
		switch (weather) {
			case RAIN:
				message = plugin.getPrefix() + ChatStyle.GREEN + "Weather set to " + ChatStyle.WHITE + "RAIN/SNOW" + ChatStyle.GREEN + ".";
				break;
			default:
				message = plugin.getPrefix() + ChatStyle.GREEN + "Weather set to " + ChatStyle.WHITE + weather.name() + ChatStyle.GREEN + ".";
				break;
		}
		if (getEngine() instanceof Client) {
			source.sendMessage(message);
		} else {
			for (Player player : ((Server) getEngine()).getOnlinePlayers()) {
				if (player.getWorld().equals(world)) {
					player.sendMessage(message);
				}
			}
		}
	}

	@CommandDescription(aliases = {"kill"}, usage = "[player]", desc = "Kill yourself or another player")
	@Permissible("vanilla.command.kill")
	public void kill(CommandSource source, CommandArguments args) throws CommandException {
		Player player = args.popPlayerOrMe("player", source);
		args.assertCompletelyParsed();

		Health health = player.get(Health.class);
		if (health == null) {
			throw new CommandException(player.getDisplayName() + " can not be killed.");
		}
		health.kill(HealthChangeCause.COMMAND);
	}

	@CommandDescription(aliases = {"version", "vr"}, usage = "", desc = "Print out the version information for Vanilla")
	@Permissible("vanilla.command.version")
	public void getVersion(CommandSource source, CommandArguments args) throws ArgumentParseException {
		args.assertCompletelyParsed();

		source.sendMessage("Vanilla " + plugin.getDescription().getVersion()
				+ " (Implementing Minecraft protocol v" + plugin.getDescription().getData("protocol") + ")");

		source.sendMessage("Powered by Spout " + getEngine().getVersion() + " (Implementing SpoutAPI " + getEngine().getAPIVersion() + ")");
	}

	@CommandDescription(aliases = {"biome"}, usage = "", desc = "Print out the name of the biome at the current location")
	@Permissible("vanilla.command.biome")
	@Filter(PlayerFilter.class)
	public void getBiomeName(CommandSource source, CommandArguments args) throws CommandException {
		args.assertCompletelyParsed();

		Player player = (Player) source;
		if (!(player.getPhysics().getPosition().getWorld().getGenerator() instanceof BiomeGenerator)) {
			throw new CommandException("This map does not appear to have any biome data.");
		}
		Point pos = player.getPhysics().getPosition();
		Biome biome = pos.getWorld().getBiome(pos.getBlockX(), pos.getBlockY(), pos.getBlockZ());
		source.sendMessage(plugin.getPrefix() + ChatStyle.GREEN + "Current biome: " + ChatStyle.WHITE + (biome != null ? biome.getName() : "none"));
	}

	@CommandDescription(aliases = {"tps"}, usage = "", desc = "Print out the current engine ticks per second")
	@Permissible("vanilla.command.tps")
	public void getTPS(CommandSource source, CommandArguments args) throws ArgumentParseException {
		args.assertCompletelyParsed();

		source.sendMessage("TPS: " + tpsMonitor.getTPS());
		source.sendMessage("Average TPS: " + tpsMonitor.getAvgTPS());
	}

	private static class TicksPerSecondMonitor implements Runnable, TPSMonitor {
		private static final int MAX_MEASUREMENTS = 20 * 60;
		private final TLongLinkedList timings = new TLongLinkedList();
		private long lastTime = System.currentTimeMillis();
		private final AtomicFloat ticksPerSecond = new AtomicFloat(20);
		private final AtomicFloat avgTicksPerSecond = new AtomicFloat(20);

		@Override
		public void run() {
			long time = System.currentTimeMillis();
			timings.add(time - lastTime);
			lastTime = time;
			if (timings.size() > MAX_MEASUREMENTS) {
				timings.removeAt(0);
			}
			final int size = timings.size();
			if (size > 20) {
				TLongIterator i = timings.iterator();
				int count = 0;
				long last20 = 0;
				long total = 0;
				while (i.hasNext()) {
					long next = i.next();
					if (count > size - 20) {
						last20 += next;
					}
					total += next;
					count++;
				}
				ticksPerSecond.set(1000F / (last20 / 20F));
				avgTicksPerSecond.set(1000F / (total / ((float) size)));
			}
		}

		public float getTPS() {
			return ticksPerSecond.get();
		}

		public float getAvgTPS() {
			return avgTicksPerSecond.get();
		}
	}

	public static interface TPSMonitor {
		public float getTPS();

		public float getAvgTPS();
	}
}
