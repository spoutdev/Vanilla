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
package org.spout.vanilla.command;

import java.util.List;
import java.util.Set;

import org.spout.api.Client;
import org.spout.api.Engine;
import org.spout.api.Platform;
import org.spout.api.Spout;
import org.spout.api.chat.style.ChatStyle;
import org.spout.api.command.CommandContext;
import org.spout.api.command.CommandSource;
import org.spout.api.command.annotated.Command;
import org.spout.api.command.annotated.CommandPermissions;
import org.spout.api.component.Component;
import org.spout.api.component.impl.InteractComponent;
import org.spout.api.entity.Entity;
import org.spout.api.entity.EntityPrefab;
import org.spout.api.entity.Player;
import org.spout.api.exception.CommandException;
import org.spout.api.generator.WorldGeneratorObject;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.cuboid.Chunk;
import org.spout.api.geo.discrete.Point;
import org.spout.api.inventory.ItemStack;
import org.spout.api.lighting.LightingManager;
import org.spout.api.lighting.LightingRegistry;
import org.spout.api.material.Material;
import org.spout.api.material.MaterialRegistry;
import org.spout.api.math.IntVector3;
import org.spout.api.math.Quaternion;
import org.spout.api.math.Vector3;
import org.spout.api.protocol.NetworkSynchronizer;
import org.spout.api.protocol.event.ProtocolEvent;
import org.spout.api.util.BlockIterator;
import org.spout.api.util.OutwardIterator;
import org.spout.vanilla.VanillaPlugin;
import org.spout.vanilla.component.block.material.chest.Chest;
import org.spout.vanilla.component.entity.VanillaEntityComponent;
import org.spout.vanilla.component.entity.inventory.PlayerInventory;
import org.spout.vanilla.component.entity.inventory.WindowHolder;
import org.spout.vanilla.component.entity.living.Ageable;
import org.spout.vanilla.component.entity.living.Human;
import org.spout.vanilla.component.entity.living.Living;
import org.spout.vanilla.component.entity.misc.Burn;
import org.spout.vanilla.component.entity.misc.Effects;
import org.spout.vanilla.component.entity.misc.Head;
import org.spout.vanilla.component.entity.misc.Health;
import org.spout.vanilla.component.entity.misc.Hunger;
import org.spout.vanilla.component.entity.substance.FallingBlock;
import org.spout.vanilla.component.entity.substance.Item;
import org.spout.vanilla.component.entity.substance.Substance;
import org.spout.vanilla.component.entity.substance.test.ForceMessages;
import org.spout.vanilla.data.VanillaData;
import org.spout.vanilla.data.effect.EntityEffect;
import org.spout.vanilla.data.effect.EntityEffectType;
import org.spout.vanilla.data.effect.store.GeneralEffects;
import org.spout.vanilla.inventory.block.BrewingStandInventory;
import org.spout.vanilla.inventory.block.DispenserInventory;
import org.spout.vanilla.inventory.block.EnchantmentTableInventory;
import org.spout.vanilla.inventory.block.FurnaceInventory;
import org.spout.vanilla.inventory.entity.VillagerInventory;
import org.spout.vanilla.inventory.window.WindowType;
import org.spout.vanilla.inventory.window.block.BrewingStandWindow;
import org.spout.vanilla.inventory.window.block.CraftingTableWindow;
import org.spout.vanilla.inventory.window.block.DispenserWindow;
import org.spout.vanilla.inventory.window.block.EnchantmentTableWindow;
import org.spout.vanilla.inventory.window.block.FurnaceWindow;
import org.spout.vanilla.inventory.window.block.chest.ChestWindow;
import org.spout.vanilla.inventory.window.entity.VillagerWindow;
import org.spout.vanilla.material.VanillaBlockMaterial;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.item.VanillaItemMaterial;
import org.spout.vanilla.material.map.Map;
import org.spout.vanilla.protocol.VanillaNetworkSynchronizer;
import org.spout.vanilla.protocol.entity.creature.CreatureType;
import org.spout.vanilla.protocol.entity.object.ObjectType;
import org.spout.vanilla.render.LightRenderEffect;
import org.spout.vanilla.render.SkyRenderEffect;
import org.spout.vanilla.scoreboard.Objective;
import org.spout.vanilla.scoreboard.ObjectiveSlot;
import org.spout.vanilla.scoreboard.Scoreboard;
import org.spout.vanilla.util.explosion.ExplosionModels;
import org.spout.vanilla.world.generator.object.RandomizableObject;
import org.spout.vanilla.world.generator.object.VanillaObjects;
import org.spout.vanilla.world.lighting.VanillaCuboidLightBuffer;

public class TestCommands {
	private final VanillaPlugin plugin;

	public TestCommands(VanillaPlugin plugin) {
		this.plugin = plugin;
	}

	private Engine getEngine() {
		return plugin.getEngine();
	}

	@Command(aliases = {"effect", "fx"}, usage = "<type> <duration> [amp]", desc = "Applies an effect.", min = 2, max = 3)
	@CommandPermissions("vanilla.command.debug")
	public void effect(CommandContext args, CommandSource source) throws CommandException {
		if (!(source instanceof Player)) {
			throw new CommandException("You must be a player to apply effects.");
		}

		Player player = (Player) source;
		EntityEffectType type;
		try {
			type = EntityEffectType.valueOf(args.getString(0).toUpperCase());
		} catch (IllegalArgumentException e) {
			throw new CommandException(e);
		}
		float duration = args.getFloat(1);
		int amp = args.length() == 2 ? 0 : args.getInteger(2);

		player.add(Effects.class).add(new EntityEffect(type, amp, duration));
		player.sendMessage(ChatStyle.BRIGHT_GREEN, "Applied effect '" + type + "' with amplitude '" + amp + "' for '" + duration + "' seconds.");
	}

	@Command(aliases = {"testscoreboard", "tsb"}, desc = "Not to be confused with '/scoreboard'")
	@CommandPermissions("vanilla.command.debug")
	public void scoreboard(CommandContext args, CommandSource source) throws CommandException {
		if (!(source instanceof Player)) {
			throw new CommandException("You must be a player to display the scoreboard.");
		}

		Player player = (Player) source;
		String name = player.getName();
		player.sendMessage(ChatStyle.BRIGHT_GREEN, "Displaying scoreboard...");

		Scoreboard scoreboard = player.add(Scoreboard.class);
		scoreboard.createObjective("test_obj_1")
				.setDisplayName(ChatStyle.BRIGHT_GREEN, "Test Objective 1")
				.setScore(name, 9001)
				.setCriteria(Objective.CRITERIA_HEALTH)
				.setSlot(ObjectiveSlot.SIDEBAR);

		scoreboard.createObjective("test_obj_2")
				.setDisplayName(ChatStyle.DARK_CYAN, "Test Objective 2")
				.setScore(name, 0)
				.setCriteria(Objective.CRITERIA_TOTAL_KILL_COUNT)
				.setSlot(ObjectiveSlot.LIST);
	}

	@Command(aliases = {"testteams", "tt"}, desc = "Tests teams functionality.")
	@CommandPermissions("vanilla.command.debug")
	public void teams(CommandContext args, CommandSource source) throws CommandException {
		if (!(source instanceof Player)) {
			throw new CommandException("You must be a player to test teams.");
		}

		Player player = (Player) source;
		String name = player.getName();
		player.sendMessage("Creating team...");

		Scoreboard scoreboard = player.get(Scoreboard.class);
		if (scoreboard == null) {
			throw new CommandException("You do not have an active scoreboard.");
		}

		scoreboard.createTeam("spoutdev")
				.setDisplayName(ChatStyle.DARK_CYAN, "Spout")
				.setPrefix(ChatStyle.BRIGHT_GREEN)
				.addPlayerName(name);
	}
	
	@Command(aliases = "lightcheck", usage = "", desc = "Checks nearby light values", max = 0)
	@CommandPermissions("vanilla.command.debug")
	public void light(CommandContext args, CommandSource source) throws CommandException {
		if (!(source instanceof Player)) {
			throw new CommandException("You must be a player to get a map.");
		}
		Player p = (Player) source;
		Point pos = p.getScene().getPosition();
		World w = pos.getWorld();
		LightingManager<?> manager = LightingRegistry.getContains("blocklight");
		if (manager == null) {
			p.sendMessage("Block light manager not registered");
			return;
		}
		short id = manager.getId();
		OutwardIterator i = new OutwardIterator(pos.getBlockX(), pos.getBlockY(), pos.getBlockZ(), 8);
		while (i.hasNext()) {
			IntVector3 v = i.next();
			Chunk c = w.getChunkFromBlock(v.getX(), v.getY(), v.getZ());
			byte lightOriginal = c.getBlockLight(v.getX(), v.getY(), v.getZ());
			VanillaCuboidLightBuffer buffer = (VanillaCuboidLightBuffer) c.getLightBuffer(id);
			int lightNew = buffer.get(v.getX(), v.getY(), v.getZ());
			if (lightNew != lightOriginal) {
				Spout.getLogger().info(v.getX() + ", " + v.getY() + ", " + v.getZ() + " old=" + lightOriginal + " new=" + lightNew);
			}
		}
	}
	
	@Command(aliases = "skylightcheck", usage = "", desc = "Checks nearby sky light values", max = 0)
	@CommandPermissions("vanilla.command.debug")
	public void skyLight(CommandContext args, CommandSource source) throws CommandException {
		if (!(source instanceof Player)) {
			throw new CommandException("You must be a player to get a map.");
		}
		Player p = (Player) source;
		Point pos = p.getScene().getPosition();
		World w = pos.getWorld();
		LightingManager<?> manager = LightingRegistry.getContains("skylight");
		if (manager == null) {
			p.sendMessage("Sky light manager not registered");
			return;
		}
		short id = manager.getId();
		OutwardIterator i = new OutwardIterator(pos.getBlockX(), pos.getBlockY(), pos.getBlockZ(), 3);
		while (i.hasNext()) {
			IntVector3 v = i.next();
			Chunk c = w.getChunkFromBlock(v.getX(), v.getY(), v.getZ());
			byte lightOriginal = c.getBlockLight(v.getX(), v.getY(), v.getZ());
			VanillaCuboidLightBuffer buffer = (VanillaCuboidLightBuffer) c.getLightBuffer(id);
			int lightNew = buffer.get(v.getX(), v.getY(), v.getZ());
			if (lightNew != lightOriginal) {
				Spout.getLogger().info(v.getX() + ", " + v.getY() + ", " + v.getZ() + " old=" + lightOriginal + " new=" + lightNew);
			}
		}
	}


	// TODO - There needs to be a method that guarantees unique data values on a per-server basis
	private int mapId = 1;

	@Command(aliases = "map", usage = "", desc = "Creates a map", max = 0)
	@CommandPermissions("vanilla.command.debug")
	public void map(CommandContext args, CommandSource source) throws CommandException {
		if (!(source instanceof Player)) {
			throw new CommandException("You must be a player to get a map.");
		}
		Player p = (Player) source;
		ItemStack i = new ItemStack(VanillaMaterials.MAP, ++mapId, 1);
		p.get(PlayerInventory.class).add(i);
	}

	@Command(aliases = "mapdraw", usage = "<bx> <by> <tx> <ty> <col>", desc = "Draws a rectangle on the current map.  The top nibble for col is the colour and the bottom nibble is the brightness",
			min = 5, max = 5)
	@CommandPermissions("vanilla.command.debug")
	public void mapDraw(CommandContext args, CommandSource source) throws CommandException {
		if (!(source instanceof Player)) {
			throw new CommandException("You must be a player to hold a map.");
		}
		Player p = (Player) source;
		PlayerInventory inventory = p.get(PlayerInventory.class);
		if (inventory == null) {
			throw new CommandException("Player has no inventory.");
		}
		ItemStack i = inventory.getQuickbar().getSelectedSlot().get();
		if (i == null || !(i.getMaterial() instanceof Map)) {
			throw new CommandException("Held item is not a map");
		}
		Map m = (Map) i.getMaterial();
		int bx = args.getInteger(0);
		int by = args.getInteger(1);
		int tx = args.getInteger(2);
		int ty = args.getInteger(3);
		int col = args.getInteger(4);
		if (bx < 0 || bx >= m.getWidth()) {
			throw new CommandException("bx component is out of range");
		}
		if (by < 0 || by >= m.getHeight()) {
			throw new CommandException("by component is out of range");
		}
		if (tx < 0 || tx >= m.getWidth()) {
			throw new CommandException("tx component is out of range");
		}
		if (ty < 0 || ty >= m.getHeight()) {
			throw new CommandException("ty component is out of range");
		}
		if (bx > tx) {
			throw new CommandException("bx cannot be greater than tx");
		}
		if (by > ty) {
			throw new CommandException("by cannot be greater than ty");
		}
		for (ProtocolEvent e : m.drawRectangle(i, bx, by, tx, ty, col)) {
			p.getNetworkSynchronizer().callProtocolEvent(e);
		}
	}

	@Command(aliases = "mapflood", usage = "<bx> <by> <tx> <ty> <col>", desc = "Floods the current map with the given color", min = 1, max = 1)
	@CommandPermissions("vanilla.command.debug")
	public void mapFlood(CommandContext args, CommandSource source) throws CommandException {
		if (!(source instanceof Player)) {
			throw new CommandException("You must be a player to hold a map.");
		}
		Player p = (Player) source;
		PlayerInventory inventory = p.get(PlayerInventory.class);
		if (inventory == null) {
			throw new CommandException("Player has no inventory.");
		}
		ItemStack i = inventory.getQuickbar().getSelectedSlot().get();
		if (i == null || !(i.getMaterial() instanceof Map)) {
			throw new CommandException("Held item is not a map");
		}
		Map m = (Map) i.getMaterial();
		int col = args.getInteger(0);
		for (ProtocolEvent e : m.flood(i, col)) {
			p.getNetworkSynchronizer().callProtocolEvent(e);
		}
	}

	@Command(aliases = "respawn", usage = "", desc = "Forces the client to respawn", max = 0)
	@CommandPermissions("vanilla.command.debug")
	public void respawn(CommandContext args, CommandSource source) throws CommandException {
		if (!(source instanceof Player)) {
			throw new CommandException("You must be a player to respawn.");
		}
		((Player) source).getNetworkSynchronizer().setRespawned();
	}

	@Command(aliases = "sun", usage = "<x> <y> <z>", desc = "Sets the sun direction.", max = 3)
	@CommandPermissions("vanilla.command.debug")
	public void setSunDirection(CommandContext args, CommandSource source) throws CommandException {
		if (args.length() == 0) {
			LightRenderEffect.setSun(null);
			SkyRenderEffect.setSun(null);
		} else if (args.length() == 3) {
			Vector3 dir = new Vector3(args.getDouble(0), args.getDouble(1), args.getDouble(2));
			LightRenderEffect.setSun(dir);
			SkyRenderEffect.setSun(dir);
		} else {
			throw new CommandException("You must provide 3 coords or none to clear");
		}
	}

	@Command(aliases = "findframe", usage = "<radius>", desc = "Find a nether portal frame.", min = 1, max = 1)
	@CommandPermissions("vanilla.command.debug")
	public void findFrame(CommandContext args, CommandSource source) throws CommandException {
		if (!(source instanceof Player)) {
			throw new CommandException("You must be a player to find a nether frame.");
		}

		Player player = (Player) source;
		if (VanillaObjects.NETHER_PORTAL.find(player.getScene().getPosition(), args.getInteger(0))) {
			player.sendMessage(ChatStyle.BRIGHT_GREEN, "Found portal frame!");
		} else {
			player.sendMessage(ChatStyle.RED, "Portal frame not found.");
		}
	}

	@Command(aliases = "traceray", desc = "Set all blocks that cross your view to stone.", max = 0)
	@CommandPermissions("vanilla.command.debug")
	public void traceray(CommandContext args, CommandSource source) throws CommandException {
		if (!(source instanceof Player)) {
			throw new CommandException("You must be a player to trace a ray!");
		}
		Player player;
		if (getEngine().getPlatform() != Platform.CLIENT) {
			player = (Player) source;
		} else {
			player = ((Client) getEngine()).getActivePlayer();
		}

		BlockIterator blockIt;
		if (getEngine().getPlatform() != Platform.CLIENT) {
			blockIt = player.get(Head.class).getBlockView();
		} else {
			blockIt = player.get(InteractComponent.class).getAlignedBlocks();
		}

		Block block = null;
		while (blockIt.hasNext()) {
			block = blockIt.next();
			if (block.getMaterial().isPlacementObstacle()) {
				break;
			}
			block.setMaterial(VanillaMaterials.STONE);
		}
	}

	@CommandPermissions("vanilla.command.debug")
	@Command(aliases = "resetpos", desc = "Resets players position", max = 0)
	public void resetPosition(CommandContext args, CommandSource source) throws CommandException {
		if (!(source instanceof Player)) {
			throw new CommandException("You must be a player reset position!");
		}
		Player player = (Player) source;
		((VanillaNetworkSynchronizer) player.getNetworkSynchronizer()).sendPosition();
	}

	@Command(aliases = "torch", desc = "Place a torch.", max = 0)
	@CommandPermissions("vanilla.command.debug")
	public void torch(CommandContext args, CommandSource source) throws CommandException {
		if (!(source instanceof Player) && getEngine().getPlatform() != Platform.CLIENT) {
			throw new CommandException("You must be a player to trace a ray!");
		}
		Player player;
		if (getEngine().getPlatform() != Platform.CLIENT) {
			player = (Player) source;
		} else {
			player = ((Client) getEngine()).getActivePlayer();
		}

		BlockIterator blockIt = player.get(InteractComponent.class).getAlignedBlocks();

		Block block = null;
		while (blockIt.hasNext()) {
			block = blockIt.next();
			if (block.getMaterial().isPlacementObstacle()) {
				block.setMaterial(VanillaMaterials.TORCH);
				break;
			}
		}
	}

	@Command(aliases = "window", usage = "<type>", desc = "Open a window.", min = 1, max = 1)
	@CommandPermissions("vanilla.command.debug")
	public void window(CommandContext args, CommandSource source) throws CommandException {
		if (!(source instanceof Player)) {
			throw new CommandException("You must be a player to open a window.");
		}

		WindowType type;
		try {
			type = WindowType.valueOf(args.getString(0).toUpperCase());
		} catch (IllegalArgumentException e) {
			throw new CommandException("Window not found.");
		}

		Player player = (Player) source;
		switch (type) {
			case CHEST:
				player.get(WindowHolder.class).openWindow(new ChestWindow(player, new Chest()));
				break;
			case CRAFTING_TABLE:
				player.get(WindowHolder.class).openWindow(new CraftingTableWindow(player));
				break;
			case FURNACE:
				player.get(WindowHolder.class).openWindow(new FurnaceWindow(player, null, new FurnaceInventory()));
				break;
			case DISPENSER:
				player.get(WindowHolder.class).openWindow(new DispenserWindow(player, new DispenserInventory()));
				break;
			case ENCHANTMENT_TABLE:
				player.get(WindowHolder.class).openWindow(new EnchantmentTableWindow(player, null, new EnchantmentTableInventory()));
				break;
			case BREWING_STAND:
				player.get(WindowHolder.class).openWindow(new BrewingStandWindow(player, null, new BrewingStandInventory()));
				break;
			case VILLAGER:
				player.get(WindowHolder.class).openWindow(new VillagerWindow(player, new VillagerInventory()));
				break;
			default:
				throw new CommandException("Window not supported.");
		}
	}

	@Command(aliases = "damage", usage = "<amount>", desc = "Damage yourself", min = 1, max = 1)
	@CommandPermissions("vanilla.command.debug")
	public void damage(CommandContext args, CommandSource source) throws CommandException {
		if (!(source instanceof Player) && getEngine().getPlatform() != Platform.CLIENT) {
			throw new CommandException("You must be a player to damage yourself!");
		}
		Player player;
		if (getEngine().getPlatform() != Platform.CLIENT) {
			player = (Player) source;
		} else {
			player = ((Client) getEngine()).getActivePlayer();
		}
		player.get(Health.class).damage(args.getInteger(0));
	}

	@Command(aliases = "hunger", usage = "<amount> <hungry>", desc = "Modify your hunger", min = 2, max = 2)
	@CommandPermissions("vanilla.command.debug")
	public void hunger(CommandContext args, CommandSource source) throws CommandException {
		Hunger hunger = null;
		if (getEngine().getPlatform() == Platform.CLIENT) {
			hunger = ((Client) getEngine()).getActivePlayer().get(Hunger.class);
		} else {
			if (!(source instanceof Player)) {
				throw new CommandException("You must be a player to change your hunger!");
			}
			hunger = ((Player) source).get(Hunger.class);
		}

		hunger.setHunger(args.getInteger(0));
		hunger.setPoisoned(Boolean.valueOf(args.getString(1)));
	}

	@Command(aliases = {"explode"}, usage = "<explode>", desc = "Create an explosion")
	@CommandPermissions("vanilla.command.debug")
	public void explode(CommandContext args, CommandSource source) throws CommandException {
		if (!(source instanceof Player)) {
			throw new CommandException("You must be a player to cause an explosion");
		}

		Entity entity = (Player) source;
		Point position = entity.getScene().getPosition();

		ExplosionModels.SPHERICAL.execute(position, 4.0f);
	}

	@Command(aliases = {"object", "obj"}, usage = "<name>", flags = "f", desc = "Spawn a WorldGeneratorObject at your location. Use -f to ignore canPlace check", min = 1, max = 2)
	@CommandPermissions("vanilla.command.debug")
	public void generateObject(CommandContext args, CommandSource source) throws CommandException {
		if (!(source instanceof Player)) {
			throw new CommandException("The source must be a player.");
		}
		final WorldGeneratorObject object = VanillaObjects.byName(args.getString(0));
		if (object == null) {
			throw new CommandException("Invalid object name.");
		}
		final Player player = (Player) source;
		final Point loc = player.getScene().getPosition();
		final World world = loc.getWorld();
		final int x = loc.getBlockX();
		final int y = loc.getBlockY();
		final int z = loc.getBlockZ();
		final boolean force = args.hasFlag('f');
		if (!object.canPlaceObject(world, x, y, z)) {
			player.sendMessage("Couldn't place the object.");
			if (!force) {
				return;
			}
			player.sendMessage("Forcing placement.");
		}
		object.placeObject(world, x, y, z);
		if (object instanceof RandomizableObject) {
			((RandomizableObject) object).randomize();
		}
	}

	@Command(aliases = {"killall", "ka"}, desc = "Kill all non-player or world entities within a world", min = 0, max = 1)
	@CommandPermissions("vanilla.command.debug")
	public void killall(CommandContext args, CommandSource source) throws CommandException {
		World world = null;
		boolean isConsole = false;

		if (!(source instanceof Player)) {
			if (args.length() == 0) {
				throw new CommandException("Need to provide a world when executing from the console");
			}
			String name = args.getString(0);
			world = getEngine().getWorld(name);
			isConsole = true;
		}
		if (world == null && isConsole) {
			throw new CommandException("World specified is not loaded");
		}
		if (world == null) {
			world = ((Player) source).getWorld();
		}
		List<Entity> entities = world.getAll();
		int count = 0;
		for (Entity entity : entities) {
			if (entity instanceof Player || (entity.get(VanillaEntityComponent.class) == null)) {
				continue;
			}
			count++;
			entity.remove();
			getEngine().getLogger().info(entity.get(VanillaEntityComponent.class) + " was killed");
		}
		if (count > 0) {
			if (!isConsole) {
				if (count == 1) {
					source.sendMessage("1 entity has been killed.");
				} else {
					source.sendMessage(count, " entities have been killed.");
				}
			}
		} else {
			source.sendMessage("No valid entities found to kill");
		}
	}

	@Command(aliases = "debug", usage = "[type] (/resend /resendall /look /packets)", desc = "Debug commands", max = 2)
	@CommandPermissions("vanilla.command.debug")
	public void debug(CommandContext args, CommandSource source) throws CommandException {
		Player player;
		if (source instanceof Player) {
			player = (Player) source;
		} else {
			if (getEngine() instanceof Client) {
				throw new CommandException("You cannot search for players unless you are in server mode.");
			}
			player = getEngine().getPlayer(args.getString(1, ""), true);
			if (player == null) {
				source.sendMessage("Must be a player or send player name in arguments");
				return;
			}
		}

		if (args.getString(0, "").contains("look")) {
			Quaternion rotation = player.getData().get(VanillaData.HEAD_ROTATION);
			Point startPosition = player.getScene().getPosition();
			Vector3 offset = rotation.getDirection().multiply(0.1);
			for (int i = 0; i < 100; i++) {
				startPosition = startPosition.add(offset);
				GeneralEffects.NOTE_PARTICLE.playGlobal(startPosition);
			}
			player.sendMessage("Yaw = ", rotation.getYaw());
			player.sendMessage("Pitch = ", rotation.getPitch());
		} else if (args.getString(0, "").contains("resendall")) {
			NetworkSynchronizer network = player.getNetworkSynchronizer();
			Set<Chunk> chunks = network.getActiveChunks();
			for (Chunk c : chunks) {
				network.sendChunk(c);
			}

			source.sendMessage("All chunks resent");
		} else if (args.getString(0, "").contains("resend")) {
			player.getNetworkSynchronizer().sendChunk(player.getChunk());
			source.sendMessage("Chunk resent");
		} else if (args.getString(0, "").contains("relight")) {
			for (Chunk chunk : VanillaBlockMaterial.getChunkColumn(player.getChunk())) {
				chunk.initLighting();
			}
			source.sendMessage("Chunk lighting is being initialized");
		} else if (args.getString(0, "").contains("packets")) {
			player.add(ForceMessages.class);
		}
	}

	@Command(aliases = "spawn", desc = "Spawns a living entity at your location", min = 1, max = 2)
	public void spawn(CommandContext args, CommandSource source) throws CommandException {
		final Player player;
		if (!(source instanceof Player)) {
			if (getEngine().getPlatform() != Platform.CLIENT) {
				throw new CommandException("Only a player may spawn a Vanilla entity!");
			} else {
				player = ((Client) getEngine()).getActivePlayer();
			}
		} else {
			player = (Player) source;
		}

		final String name = args.getString(0);
		Class<? extends Component> clazz;
		boolean child = false;
		//See if it is a living?
		try {
			clazz = CreatureType.valueOf(name.toUpperCase()).getComponentType();
			if (Ageable.class.isAssignableFrom(clazz) && args.length() >= 2 && args.getString(1).equalsIgnoreCase("child")) {
				child = true;
			}
		} catch (Exception e1) {
			try {
				//Living failed? Try object
				clazz = ObjectType.valueOf(name.toUpperCase()).getComponentType();
			} catch (Exception e2) {
				throw new CommandException(name + " is neither a living or substance entity!");
			}
		}
		//if (!player.hasPermission("vanilla.command.spawn." + clazz.getName().toLowerCase())) {
		//	throw new CommandException("You do not have permission to spawn a(n) " + clazz.getSimpleName().toLowerCase());
		//}
		//TODO ServerEntityPrefab!
		//How about some client support?
		final Entity entity;
		if (getEngine() instanceof Client) {
			final EntityPrefab prefab = (EntityPrefab) getEngine().getFilesystem().getResource("entity://Vanilla/entities/" + clazz.getSimpleName().toLowerCase() + "/" + clazz.getSimpleName().toLowerCase() + ".sep");
			entity = prefab.createEntity(player.getScene().getPosition());
		} else {
			entity = player.getWorld().createEntity(player.getScene().getPosition(), clazz);
		}
		//Optional param was provided (ie the block material for a falling block).
		if (args.length() == 2) {
			//Now we know its either a living or substance. Lets figure out which.
			if (Living.class.isAssignableFrom(clazz)) {
				final Living living = entity.get(Living.class);
				if (name.equalsIgnoreCase("human")) {
					((Human) living).setName(args.getString(1));
				}
			} else if (Substance.class.isAssignableFrom(clazz)) {
				final Substance substance = entity.get(Substance.class);
				switch (ObjectType.valueOf(name.toUpperCase())) {
					case ITEM:
						Material item = MaterialRegistry.get(args.getString(1));
						if (item == null || !(item instanceof VanillaItemMaterial)) {
							throw new CommandException(args.getString(1) + " is not a valid VanillaItemMaterial!");
						}
						((Item) substance).setItemStack(new ItemStack(item, 1));
						break;
					case FALLING_OBJECT:
						Material block = MaterialRegistry.get(args.getString(1));
						if (block == null || !(block instanceof VanillaBlockMaterial)) {
							throw new CommandException(args.getString(1) + " is not a valid VanillaBlockMaterial!");
						}
						((FallingBlock) substance).setMaterial((VanillaBlockMaterial) block);
						break;
				}
			}
		}
		player.getWorld().spawnEntity(entity);
		if (child) {
			entity.get(Ageable.class).setAge(Ageable.MIN_AGE);
		}
	}

	@Command(aliases = "fire", usage = "<time> <hurt>", desc = "Set you on fire", min = 2, max = 2)
	@CommandPermissions("vanilla.command.debug")
	public void fire(CommandContext args, CommandSource source) throws CommandException {
		Burn fire = null;
		if (getEngine().getPlatform() == Platform.CLIENT) {
			fire = ((Client) getEngine()).getActivePlayer().add(Burn.class);
		} else {
			if (!(source instanceof Player)) {
				throw new CommandException("You must be a player to change be burnable!");
			}
			fire = ((Player) source).add(Burn.class);
		}

		fire.setOnFire(args.getFloat(0), Boolean.parseBoolean(args.getString(1)));
	}
}
