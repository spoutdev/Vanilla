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

import java.util.List;
import java.util.Set;

import org.spout.api.Client;
import org.spout.api.Spout;
import org.spout.api.chat.style.ChatStyle;
import org.spout.api.command.CommandContext;
import org.spout.api.command.CommandSource;
import org.spout.api.command.annotated.Command;
import org.spout.api.command.annotated.CommandPermissions;
import org.spout.api.component.Component;
import org.spout.api.component.implementation.HitBlockComponent;
import org.spout.api.entity.Entity;
import org.spout.api.entity.EntityPrefab;
import org.spout.api.entity.Player;
import org.spout.api.exception.CommandException;
import org.spout.api.generator.WorldGeneratorObject;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.cuboid.Chunk;
import org.spout.api.geo.discrete.Point;
import org.spout.api.material.Material;
import org.spout.api.material.MaterialRegistry;
import org.spout.api.math.Vector3;
import org.spout.api.plugin.Platform;
import org.spout.api.protocol.NetworkSynchronizer;
import org.spout.api.util.BlockIterator;
import org.spout.vanilla.VanillaPlugin;
import org.spout.vanilla.component.inventory.WindowHolder;
import org.spout.vanilla.component.living.Living;
import org.spout.vanilla.component.living.hostile.Creeper;
import org.spout.vanilla.component.living.hostile.EnderDragon;
import org.spout.vanilla.component.living.hostile.Skeleton;
import org.spout.vanilla.component.living.hostile.Zombie;
import org.spout.vanilla.component.living.neutral.Enderman;
import org.spout.vanilla.component.living.neutral.Human;
import org.spout.vanilla.component.misc.HealthComponent;
import org.spout.vanilla.component.misc.HungerComponent;
import org.spout.vanilla.component.substance.material.chest.Chest;
import org.spout.vanilla.component.substance.object.FallingBlock;
import org.spout.vanilla.component.test.TransformDebugComponent;
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
import org.spout.vanilla.protocol.VanillaNetworkSynchronizer;
import org.spout.vanilla.render.LightRenderEffect;
import org.spout.vanilla.util.explosion.ExplosionModels;
import org.spout.vanilla.world.generator.object.RandomizableObject;
import org.spout.vanilla.world.generator.object.VanillaObjects;

public class TestCommands {
	@SuppressWarnings("unused")
	private final VanillaPlugin plugin;

	public TestCommands(VanillaPlugin instance) {
		plugin = instance;
	}
	
	@Command(aliases = "sun", usage = "<x> <y> <z>", desc = "Sets the sun direction.")
	@CommandPermissions("vanilla.command.debug")
	public void setSunDirection(CommandContext args, CommandSource source) throws CommandException {
		if (args.length() == 0) {
			LightRenderEffect.setSun(null);
		} else if (args.length() == 3) {
			LightRenderEffect.setSun(new Vector3(args.getDouble(0), args.getDouble(1), args.getDouble(2)));
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
		if (VanillaObjects.NETHER_PORTAL.find(player.getTransform().getPosition(), args.getInteger(0))) {
			player.sendMessage(ChatStyle.BRIGHT_GREEN, "Found portal frame!");
		} else {
			player.sendMessage(ChatStyle.RED, "Portal frame not found.");
		}
	}

	@Command(aliases = "traceray", desc = "Set all blocks that cross your view to stone.")
	@CommandPermissions("vanilla.command.debug")
	public void traceray(CommandContext args, CommandSource source) throws CommandException {
		if (!(source instanceof Player) && Spout.getPlatform() != Platform.CLIENT) {
			throw new CommandException("You must be a player to trace a ray!");
		}
		Player player;
		if (Spout.getPlatform() != Platform.CLIENT) {
			player = (Player) source;
		} else {
			player = ((Client) Spout.getEngine()).getActivePlayer();
		}

		BlockIterator blockIt = player.get(HitBlockComponent.class).getAlignedBlocks();

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
	@Command(aliases = "resetpos", desc = "Resets players position")
	public void resetPosition(CommandContext args, CommandSource source) throws CommandException {
		if (!(source instanceof Player) && Spout.getPlatform() != Platform.SERVER) {
			throw new CommandException("You must be a player reset position!");
		}
		Player player = (Player) source;
		((VanillaNetworkSynchronizer)player.getNetworkSynchronizer()).sendPosition();
	}

	@Command(aliases = "torch", desc = "Place a torch.")
	@CommandPermissions("vanilla.command.debug")
	public void torch(CommandContext args, CommandSource source) throws CommandException {
		if (!(source instanceof Player) && Spout.getPlatform() != Platform.CLIENT) {
			throw new CommandException("You must be a player to trace a ray!");
		}
		Player player;
		if (Spout.getPlatform() != Platform.CLIENT) {
			player = (Player) source;
		} else {
			player = ((Client) Spout.getEngine()).getActivePlayer();
		}

		BlockIterator blockIt = player.get(HitBlockComponent.class).getAlignedBlocks();

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
				player.get(WindowHolder.class).openWindow(new FurnaceWindow(player, new FurnaceInventory()));
				break;
			case DISPENSER:
				player.get(WindowHolder.class).openWindow(new DispenserWindow(player, new DispenserInventory()));
				break;
			case ENCHANTMENT_TABLE:
				player.get(WindowHolder.class).openWindow(new EnchantmentTableWindow(player, new EnchantmentTableInventory()));
				break;
			case BREWING_STAND:
				player.get(WindowHolder.class).openWindow(new BrewingStandWindow(player, new BrewingStandInventory()));
				break;
			case VILLAGER:
				player.get(WindowHolder.class).openWindow(new VillagerWindow(player, new VillagerInventory()));
				break;
			default:
				throw new CommandException("Window not supported.");
		}
	}

	@Command(aliases = "damage", usage = "<amount>", desc = "Damage yourself")
	@CommandPermissions("vanilla.command.debug")
	public void damage(CommandContext args, CommandSource source) throws CommandException {
		if (!(source instanceof Player) && Spout.getPlatform() != Platform.CLIENT) {
			throw new CommandException("You must be a player to damage yourself!");
		}
		Player player;
		if (Spout.getPlatform() != Platform.CLIENT) {
			player = (Player) source;
		} else {
			player = ((Client) Spout.getEngine()).getActivePlayer();
		}
		player.get(HealthComponent.class).damage(args.getInteger(0));
	}

	@Command(aliases = "hunger", usage = "<amount> <hungry>", desc = "Modify your hunger", min = 2, max = 2)
	@CommandPermissions("vanilla.command.debug")
	public void hunger(CommandContext args, CommandSource source) throws CommandException {
		if (Spout.getPlatform() != Platform.CLIENT) {
			throw new CommandException("Only clients can modify the hunger bar.");
		}
		HungerComponent hunger = ((Client) Spout.getEngine()).getActivePlayer().get(HungerComponent.class);
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
		Point position = entity.getTransform().getPosition();

		ExplosionModels.SPHERICAL.execute(position, 4.0f);
	}

	@Command(aliases = {"tpworld", "tpw"}, usage = "<world name>", desc = "Teleport to a world's spawn.", min = 1, max = 1)
	@CommandPermissions("vanilla.command.debug")
	public void tpWorld(CommandContext args, CommandSource source) throws CommandException {
		if (!(source instanceof Player)) {
			throw new CommandException("You must be a player to teleport");
		}
		final Player player = (Player) source;
		final World world = args.getWorld(0);
		if (world != null) {
			final Point loc = world.getSpawnPoint().getPosition();
			world.getChunkFromBlock(loc);
			player.teleport(loc);
		} else {
			throw new CommandException("Please enter a valid world");
		}
	}

	@Command(aliases = {"tppos"}, usage = "<name> <world> <x> <y> <z>", desc = "Teleport to coordinates!", min = 5, max = 5)
	@CommandPermissions("vanilla.command.debug")
	public void tppos(CommandContext args, CommandSource source) throws CommandException {
		Player player = Spout.getEngine().getPlayer(args.getString(0), true);
		if (!(source instanceof Player) && player == null) {
			throw new CommandException("Must specify a valid player to tppos from the console.");
		}

		World world = Spout.getEngine().getWorld(args.getString(1));
		//If the source of the command is a player and they do not provide a valid player...teleport the source instead.
		if (player == null) {
			player = (Player) source;
		}

		if (world != null) {
			Point loc = new Point(world, args.getInteger(2), args.getInteger(3), args.getInteger(4));
			//Make sure the chunk the player is teleported to is loaded.
			world.getChunkFromBlock(loc);
			player.teleport(loc);
		} else {
			throw new CommandException("Please enter a valid world");
		}
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
		final Point loc = player.getTransform().getPosition();
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

	@Command(aliases = {"killall", "ka"}, desc = "Kill all non-player or world resources.entities within a world", min = 0, max = 1)
	@CommandPermissions("vanilla.command.debug")
	public void killall(CommandContext args, CommandSource source) throws CommandException {
		World world = null;
		boolean isConsole = false;

		if (!(source instanceof Player)) {
			if (args.length() == 0) {
				throw new CommandException("Need to provide a world when executing from the console");
			}
			String name = args.getString(0);
			world = Spout.getEngine().getWorld(name);
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
			if (entity instanceof Player || !entity.has(Living.class)) {
				continue;
			}
			count++;
			entity.remove();
			Spout.log(entity.get(Living.class) + " was killed");
		}
		if (count > 0) {
			if (!isConsole) {
				if (count == 1) {
					source.sendMessage("1 entity has been killed.");
				} else {
					source.sendMessage(count, " resources.entities have been killed.");
				}
			}
		} else {
			source.sendMessage("No valid resources.entities found to kill");
		}
	}

	@Command(aliases = "debug", usage = "[type] (/resend /resendall)", desc = "Debug commands", max = 2)
	@CommandPermissions("vanilla.command.debug")
	public void debug(CommandContext args, CommandSource source) throws CommandException {
		Player player;
		if (source instanceof Player) {
			player = (Player) source;
		} else {
			if (Spout.getEngine() instanceof Client) {
				throw new CommandException("You cannot search for players unless you are in server mode.");
			}
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
			player.getNetworkSynchronizer().sendChunk(player.getChunk());
			source.sendMessage("Chunk resent");
		} else if (args.getString(0, "").contains("relight")) {
			for (Chunk chunk : VanillaBlockMaterial.getChunkColumn(player.getChunk())) {
				chunk.initLighting();
			}
			source.sendMessage("Chunk lighting is being initialized");
		}
	}

	@Command(aliases = "spawnmob", desc = "Spawns a Living at your location", min = 1, max = 2)
	@CommandPermissions("vanilla.command.spawnmob")
	public void spawnmob(CommandContext args, CommandSource source) throws CommandException {
		if (!(source instanceof Player) && Spout.getPlatform() != Platform.CLIENT) {
			throw new CommandException("Only a player may spawn a Living!");
		}
		Player player;
		if (Spout.getPlatform() != Platform.CLIENT) {
			player = (Player) source;
		} else {
			player = ((Client) Spout.getEngine()).getActivePlayer();
		}

		final Point pos = player.getTransform().getPosition();
		final String name = args.getString(0);
		Class<? extends Component> clazz;
		// TODO: Make entity prefabs for all resources.entities
		if (name.isEmpty()) {
			throw new CommandException("It appears that you forgot to enter in the name of the Living.");
		} else if (name.equalsIgnoreCase("enderman")) {
			clazz = Enderman.class;
		} else if (name.equalsIgnoreCase("enderdragon")) {
			clazz = EnderDragon.class;
		} else if (name.equalsIgnoreCase("movingblock")) {
			clazz = FallingBlock.class;
		} else if (name.equalsIgnoreCase("npc")) {
			clazz = Human.class;
		} else if (name.equalsIgnoreCase("creeper")) {
			clazz = Creeper.class;
		} else if (name.equalsIgnoreCase("zombie")) {
			clazz = Zombie.class;
		} else if (name.equalsIgnoreCase("skeleton")) {
			clazz = Skeleton.class;
		} else {
			throw new CommandException(name + " was not a valid name for a Living!");
		}
		Entity entity = pos.getWorld().createEntity(pos, clazz);
		if (clazz.equals(FallingBlock.class)) {
			if (args.length() == 2) {
				final String materialName = args.getString(1);
				final Material mat = MaterialRegistry.get(materialName);
				if (mat instanceof VanillaBlockMaterial) {
					entity.add(FallingBlock.class).setMaterial((VanillaBlockMaterial) mat);
				}
			} else {
				entity.add(FallingBlock.class).setMaterial(VanillaMaterials.SAND);
			}
		} else if (clazz.equals(Human.class)) {
			String npcName = "Steve";
			if (args.length() == 2) {
				npcName = args.getString(1);
			}
			entity.add(Human.class).setName(npcName);
			if (Spout.getPlatform() == Platform.CLIENT) {
				EntityPrefab humanPrefab = (EntityPrefab) Spout.getFilesystem().getResource("entity://Vanilla/resources/entities/human/human.sep");
				entity = humanPrefab.createEntity(pos);
			}
		} else if (clazz.equals(Enderman.class)) {
			if (Spout.getPlatform() == Platform.CLIENT) {
				EntityPrefab endermanPrefab = (EntityPrefab) Spout.getFilesystem().getResource("entity://Vanilla/resources/entities/enderman/enderman.sep");
				entity = endermanPrefab.createEntity(pos);
			}
		} else if (clazz.equals(Creeper.class)) {
			if (Spout.getPlatform() == Platform.CLIENT) {
				EntityPrefab creeperPrefab = (EntityPrefab) Spout.getFilesystem().getResource("entity://Vanilla/resources/entities/creeper/creeper.sep");
				entity = creeperPrefab.createEntity(pos);
			}
		} else if (clazz.equals(Zombie.class)) {
			if (Spout.getPlatform() == Platform.CLIENT) {
				EntityPrefab zombiePrefab = (EntityPrefab) Spout.getFilesystem().getResource("entity://Vanilla/resources/entities/zombie/zombie.sep");
				entity = zombiePrefab.createEntity(pos);
			}
		} else if (clazz.equals(Skeleton.class)) {
			if (Spout.getPlatform() == Platform.CLIENT) {
				EntityPrefab skeletonPrefab = (EntityPrefab) Spout.getFilesystem().getResource("entity://Vanilla/resources/entities/skeleton/skeleton.sep");
				entity = skeletonPrefab.createEntity(pos);
			}
		}
		entity.setSavable(false);
		if (Spout.getPlatform() == Platform.SERVER && Spout.debugMode()) {
			entity.add(TransformDebugComponent.class);
		}
		pos.getWorld().spawnEntity(entity);
	}
}
