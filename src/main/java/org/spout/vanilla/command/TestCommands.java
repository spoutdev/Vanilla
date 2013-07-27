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

import org.spout.api.Client;
import org.spout.api.Engine;
import org.spout.api.Platform;
import org.spout.api.command.CommandArguments;
import org.spout.api.command.CommandSource;
import org.spout.api.command.annotated.CommandDescription;
import org.spout.api.command.annotated.Filter;
import org.spout.api.command.annotated.Flag;
import org.spout.api.command.annotated.Permissible;
import org.spout.api.command.filter.PlayerFilter;
import org.spout.api.component.Component;
import org.spout.api.component.entity.InteractComponent;
import org.spout.api.entity.Entity;
import org.spout.api.entity.EntityPrefab;
import org.spout.api.entity.Player;
import org.spout.api.event.ProtocolEvent;
import org.spout.api.exception.CommandException;
import org.spout.api.generator.WorldGeneratorObject;
import org.spout.api.geo.LoadOption;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.cuboid.Chunk;
import org.spout.api.geo.discrete.Point;
import org.spout.api.inventory.ItemStack;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.Material;
import org.spout.api.material.MaterialRegistry;
import org.spout.api.math.Quaternion;
import org.spout.api.math.Vector3;
import org.spout.api.protocol.ServerNetworkSynchronizer;
import org.spout.api.util.BlockIterator;
import org.spout.vanilla.ChatStyle;
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
import org.spout.vanilla.component.entity.misc.EntityHead;
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
import org.spout.vanilla.protocol.VanillaServerNetworkSynchronizer;
import org.spout.vanilla.protocol.entity.creature.CreatureType;
import org.spout.vanilla.protocol.entity.object.ObjectType;
import org.spout.vanilla.render.LightRenderEffect;
import org.spout.vanilla.render.SkyRenderEffect;
import org.spout.vanilla.scoreboard.Objective;
import org.spout.vanilla.scoreboard.ObjectiveSlot;
import org.spout.vanilla.scoreboard.Scoreboard;
import org.spout.vanilla.util.explosion.ExplosionModels;
import org.spout.vanilla.world.generator.normal.object.tree.BigTreeObject;
import org.spout.vanilla.world.generator.object.RandomizableObject;
import org.spout.vanilla.world.generator.object.VanillaObjects;
import org.spout.vanilla.world.lighting.LightingVerification;

import java.util.List;
import java.util.Set;

public class TestCommands {
	private final VanillaPlugin plugin;

	public TestCommands(VanillaPlugin plugin) {
		this.plugin = plugin;
	}

	private Engine getEngine() {
		return plugin.getEngine();
	}

	@CommandDescription(aliases = {"effect", "fx"}, usage = "<type> <duration> [amp]", desc = "Applies an effect.")
	@Permissible("vanilla.command.debug")
	@Filter(PlayerFilter.class)
	public void effect(CommandSource source, CommandArguments args) throws CommandException {
		Player player = args.checkPlayer(source);
		EntityEffectType type = args.popEnumValue("type", EntityEffectType.class);
		float duration = (float) args.popDouble("duration");
		int amp = args.popInteger("amp", 2);
		args.assertCompletelyParsed();

		player.add(Effects.class).add(new EntityEffect(type, amp, duration));
		player.sendMessage(ChatStyle.GREEN + "Applied effect '" + type + "' with amplitude '" + amp + "' for '" + duration + "' seconds.");
	}

	@CommandDescription(aliases = {"testscoreboard", "tsb"}, desc = "Not to be confused with '/scoreboard'")
	@Permissible("vanilla.command.debug")
	@Filter(PlayerFilter.class)
	public void scoreboard(Player player, CommandArguments args) throws CommandException {
		args.assertCompletelyParsed();

		String name = player.getName();
		player.sendMessage(ChatStyle.GREEN + "Displaying scoreboard...");

		Scoreboard scoreboard = player.add(Scoreboard.class);
		scoreboard.createObjective("test_obj_1")
				.setDisplayName(ChatStyle.GREEN + "Test Objective 1")
				.setScore(name, 9001)
				.setCriteria(Objective.CRITERIA_HEALTH)
				.setSlot(ObjectiveSlot.SIDEBAR);

		scoreboard.createObjective("test_obj_2")
				.setDisplayName(ChatStyle.DARK_AQUA + "Test Objective 2")
				.setScore(name, 0)
				.setCriteria(Objective.CRITERIA_TOTAL_KILL_COUNT)
				.setSlot(ObjectiveSlot.LIST);
	}

	@CommandDescription(aliases = {"testteams", "tt"}, desc = "Tests teams functionality.")
	@Permissible("vanilla.command.debug")
	@Filter(PlayerFilter.class)
	public void teams(Player player, CommandArguments args) throws CommandException {
		args.assertCompletelyParsed();

		String name = player.getName();
		player.sendMessage("Creating team...");

		Scoreboard scoreboard = player.get(Scoreboard.class);
		if (scoreboard == null) {
			throw new CommandException("You do not have an active scoreboard.");
		}

		scoreboard.createTeam("spoutdev")
				.setDisplayName(ChatStyle.DARK_AQUA + "Spout")
				.setPrefix(ChatStyle.GREEN.toString())
				.addPlayerName(name);
	}

	@CommandDescription(aliases = "chunklight", usage = "", desc = "Tests lighting in current chunk")
	@Permissible("vanilla.command.debug")
	@Filter(PlayerFilter.class)
	public void chunkLight(Player player, CommandArguments args) throws CommandException {
		args.assertCompletelyParsed();

		Chunk c = player.getChunk();
		if (c == null) {
			throw new CommandException("You are somenow in a null chunk. This is probably bad.");
		}
		LightingVerification.checkChunk(c, false);
	}

	@CommandDescription(aliases = "alllight", usage = "", desc = "Tests lighting in all loaded chunks in the current world")
	@Permissible("vanilla.command.debug")
	@Filter(PlayerFilter.class)
	public void allLight(Player player, CommandArguments args) throws CommandException {
		args.assertCompletelyParsed();

		LightingVerification.checkAll(player.getWorld(), true);
	}

	@CommandDescription(aliases = "checkheight", usage = "", desc = "Finds surface height of current column")
	@Permissible("vanilla.command.debug")
	@Filter(PlayerFilter.class)
	public void targetHeight(Player player, CommandArguments args) throws CommandException {
		args.assertCompletelyParsed();

		Point pos = player.getPhysics().getPosition();

		int height = pos.getWorld().getSurfaceHeight(pos.getBlockX(), pos.getBlockZ());

		player.sendMessage("You are at " + pos.getBlockX() + ", " + pos.getBlockY() + ", " + pos.getBlockZ());
		player.sendMessage("Surface Height " + height + " " + (pos.getBlockY() - height) + " blocks below");
	}

	@CommandDescription(aliases = "getblock", usage = "<world> <x> <y> <z>", desc = "Finds block at the given coords")
	@Permissible("vanilla.command.debug")
	public void getBlock(CommandSource source, CommandArguments args) throws CommandException {
		Point p = args.popPoint("loc", source);
		args.assertCompletelyParsed();

		Chunk c = p.getChunk(LoadOption.NO_LOAD);
		if (c == null) {
			throw new CommandException("Chunk not loaded");
		}
		int blockState = c.getBlockFullState(p.getBlockX(), p.getBlockY(), p.getBlockZ());
		BlockMaterial m = BlockMaterial.get(blockState);
		source.sendMessage("Material at " + p.getBlockX() + ", " + p.getBlockY() + ", " + p.getBlockZ() + " is " + m.getClass().getSimpleName());
	}

	@CommandDescription(aliases = "growtree", usage = "", desc = "grows a tree at the current location")
	@Permissible("vanilla.command.debug")
	@Filter(PlayerFilter.class)
	public void growTree(Player player, CommandArguments args) throws CommandException {
		args.assertCompletelyParsed();
		Point pos = player.getPhysics().getPosition();

		BigTreeObject tree = new BigTreeObject();
		tree.placeObject(pos.getWorld(), pos.getBlockX(), pos.getBlockY(), pos.getBlockZ());
		player.teleport(pos.add(new Vector3(0, 50, 0)));
	}

	// TODO - There needs to be a method that guarantees unique data values on a per-server basis
	private int mapId = 1;

	@CommandDescription(aliases = "map", usage = "", desc = "Creates a map")
	@Permissible("vanilla.command.debug")
	@Filter(PlayerFilter.class)
	public void map(Player player, CommandArguments args) throws CommandException {
		args.assertCompletelyParsed();

		ItemStack i = new ItemStack(VanillaMaterials.MAP, ++mapId, 1);
		player.get(PlayerInventory.class).add(i);
	}

	@CommandDescription(aliases = "mapdraw", usage = "<bx> <by> <tx> <ty> <col>", desc = "Draws a rectangle on the current map.  The top nibble for col is the colour and the bottom nibble is the brightness")
	@Permissible("vanilla.command.debug")
	@Filter(PlayerFilter.class)
	public void mapDraw(Player player, CommandArguments args) throws CommandException {
		args.assertCompletelyParsed();
		PlayerInventory inventory = player.get(PlayerInventory.class);
		if (inventory == null) {
			throw new CommandException("Player has no inventory.");
		}
		ItemStack i = inventory.getQuickbar().getSelectedSlot().get();
		if (i == null || !(i.getMaterial() instanceof Map)) {
			throw new CommandException("Held item is not a map");
		}
		Map m = (Map) i.getMaterial();
		int bx = args.popInteger("bx");
		int by = args.popInteger("by");
		int tx = args.popInteger("tx");
		int ty = args.popInteger("ty");
		int col = args.popInteger("col");
		args.assertCompletelyParsed();

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
			player.getNetworkSynchronizer().callProtocolEvent(e);
		}
	}

	@CommandDescription(aliases = "mapflood", usage = "<col>", desc = "Floods the current map with the given color")
	@Permissible("vanilla.command.debug")
	@Filter(PlayerFilter.class)
	public void mapFlood(Player player, CommandArguments args) throws CommandException {
		args.assertCompletelyParsed();
		PlayerInventory inventory = player.get(PlayerInventory.class);
		if (inventory == null) {
			throw new CommandException("Player has no inventory.");
		}
		ItemStack i = inventory.getQuickbar().getSelectedSlot().get();
		if (i == null || !(i.getMaterial() instanceof Map)) {
			throw new CommandException("Held item is not a map");
		}
		Map m = (Map) i.getMaterial();
		int col = args.popInteger("col");
		for (ProtocolEvent e : m.flood(i, col)) {
			player.getNetworkSynchronizer().callProtocolEvent(e);
		}
	}

	@CommandDescription(aliases = "respawn", usage = "", desc = "Forces the client to respawn")
	@Permissible("vanilla.command.debug")
	@org.spout.api.command.annotated.Platform(Platform.SERVER)
	@Filter(PlayerFilter.class)
	public void respawn(Player player, CommandArguments args) throws CommandException {
		args.assertCompletelyParsed();
		((ServerNetworkSynchronizer) player.getNetworkSynchronizer()).forceRespawn();
	}

	@CommandDescription(aliases = "sun", usage = "<x> <y> <z>", desc = "Sets the sun direction.")
	@Permissible("vanilla.command.debug")
	public void setSunDirection(CommandSource source, CommandArguments args) throws CommandException {
		Vector3 dir = args.popVector3("dir", null);
		args.assertCompletelyParsed();
		LightRenderEffect.setSun(dir);
		SkyRenderEffect.setSun(dir);
	}

	@CommandDescription(aliases = "findframe", usage = "<radius>", desc = "Find a nether portal frame.")
	@Permissible("vanilla.command.debug")
	@Filter(PlayerFilter.class)
	public void findFrame(Player player, CommandArguments args) throws CommandException {
		final int radius = args.popInteger("radius");
		args.assertCompletelyParsed();

		if (VanillaObjects.NETHER_PORTAL.find(player.getPhysics().getPosition(), radius)) {
			player.sendMessage(ChatStyle.GREEN + "Found portal frame!");
		} else {
			player.sendMessage(ChatStyle.RED + "Portal frame not found.");
		}
	}

	@CommandDescription(aliases = "traceray", desc = "Set all blocks that cross your view to stone.")
	@Permissible("vanilla.command.debug")
	@Filter(PlayerFilter.class)
	public void traceray(CommandSource source, CommandArguments args) throws CommandException {
		args.assertCompletelyParsed();
		Player player = (Player) source;

		BlockIterator blockIt;
		if (getEngine().getPlatform() != Platform.CLIENT) {
			blockIt = player.get(EntityHead.class).getBlockView();
		} else {
			blockIt = player.get(InteractComponent.class).getAlignedBlocks();
		}

		Block block;
		while (blockIt.hasNext()) {
			block = blockIt.next();
			if (block.getMaterial().isPlacementObstacle()) {
				break;
			}
			block.setMaterial(VanillaMaterials.STONE);
		}
	}

	@Permissible("vanilla.command.debug")
	@CommandDescription(aliases = "resetpos", desc = "Resets players position")
	@Filter(PlayerFilter.class)
	public void resetPosition(Player player, CommandArguments args) throws CommandException {
		args.assertCompletelyParsed();
		((VanillaServerNetworkSynchronizer) player.getNetworkSynchronizer()).sendPosition();
	}

	@CommandDescription(aliases = "torch", desc = "Place a torch.")
	@Filter(PlayerFilter.class)
	@Permissible("vanilla.command.debug")
	public void torch(Player player, CommandArguments args) throws CommandException {
		args.assertCompletelyParsed();

		BlockIterator blockIt = player.get(InteractComponent.class).getAlignedBlocks();
		Block block;
		while (blockIt.hasNext()) {
			block = blockIt.next();
			if (block.getMaterial().isPlacementObstacle()) {
				block.setMaterial(VanillaMaterials.TORCH);
				break;
			}
		}
	}

	@CommandDescription(aliases = "window", usage = "<type>", desc = "Open a window.")
	@Permissible("vanilla.command.debug")
	@Filter(PlayerFilter.class)
	public void window(Player player, CommandArguments args) throws CommandException {
		WindowType type = args.popEnumValue("type", WindowType.class);
		args.assertCompletelyParsed();

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

	@CommandDescription(aliases = "damage", usage = "<amount>", desc = "Damage yourself")
	@Filter(PlayerFilter.class)
	@Permissible("vanilla.command.debug")
	public void damage(Player player, CommandArguments args) throws CommandException {
		final int amount = args.popInteger("amount");
		args.assertCompletelyParsed();
		player.get(Health.class).damage(amount);
	}

	@CommandDescription(aliases = "hunger", usage = "<amount> <hungry>", desc = "Modify your hunger")
	@Filter(PlayerFilter.class)
	@Permissible("vanilla.command.debug")
	public void hunger(Player player, CommandArguments args) throws CommandException {
		final int amount = args.popInteger("amount");
		final boolean hungry = args.popBoolean("hungry");
		args.assertCompletelyParsed();

		Hunger hunger = player.get(Hunger.class);
		hunger.setHunger(amount);
		hunger.setPoisoned(hungry);
	}

	@CommandDescription(aliases = {"explode"}, usage = "<explode>", desc = "Create an explosion")
	@Permissible("vanilla.command.debug")
	@Filter(PlayerFilter.class)
	public void explode(Player player, CommandArguments args) throws CommandException {
		args.assertCompletelyParsed();

		Point position = player.getPhysics().getPosition();
		ExplosionModels.SPHERICAL.execute(position, 4.0f);
	}

	@CommandDescription(aliases = {"object", "obj"}, usage = "[-f] <object>", flags = {@Flag(aliases = {"force", "f"})},
			desc = "Spawn a WorldGeneratorObject at your location. Use -f to ignore canPlace check")
	@Filter(PlayerFilter.class)
	@Permissible("vanilla.command.debug")
	public void generateObject(Player player, CommandArguments args) throws CommandException {
		final WorldGeneratorObject object = VanillaObjects.byName(args.currentArgument("object"));
		if (object == null) {
			throw args.failure("object", "Unknown object!", false);
		} else {
			args.success("object", object);
		}
		args.assertCompletelyParsed();

		final Point loc = player.getPhysics().getPosition();
		final World world = loc.getWorld();
		final int x = loc.getBlockX();
		final int y = loc.getBlockY();
		final int z = loc.getBlockZ();
		final boolean force = args.has("force");
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

	@CommandDescription(aliases = {"killall", "ka"}, desc = "Kill all non-player or world entities within a world")
	@Permissible("vanilla.command.debug")
	public void killall(CommandSource source, CommandArguments args) throws CommandException {
		World world = args.popWorld("world", source);
		args.assertCompletelyParsed();

		List<Entity> entities = world.getAll();
		int count = 0;
		for (Entity entity : entities) {
			if (entity instanceof Player || (entity.get(VanillaEntityComponent.class) == null)) {
				continue;
			}
			count++;
			/*VanillaEntityComponent comp = entity.get(VanillaEntityComponent.class); // This has the potential to be REALLY spammy!
			if (comp instanceof Item) {
				Item item = (Item) comp;
				ItemStack stack = item.getItemStack();
				getEngine().getLogger().info("Removing item (" + stack + ") at " + entity.getPhysics().getTransform().getPosition().toBlockString());
			} else {
				getEngine().getLogger().info("Killing " + comp.getClass().getSimpleName() + " at " + entity.getPhysics().getTransform().getPosition().toBlockString());
			}*/
			entity.remove();
		}
		if (count > 0) {
				if (count == 1) {
					args.logAndNotify(plugin, source, "1 entity has been killed by " + source.getName() +  ".");
				} else {
					args.logAndNotify(plugin, source, count + " entities have been killed by " + source.getName() + ".");
				}
		} else {
			source.sendMessage("No valid entities found to kill");
		}
	}

	@CommandDescription(aliases = "debug", usage = "<resend|resendall|look|packets)", desc = "Debug commands")
	@Permissible("vanilla.command.debug")
	public void debug(CommandSource source, CommandArguments args) throws CommandException {
		String action = args.popString("action");
		Player player = args.popPlayerOrMe("player", source);
		args.assertCompletelyParsed();

		if (action.contains("look")) {
			Quaternion rotation = player.getData().get(VanillaData.HEAD_ROTATION);
			Point startPosition = player.getPhysics().getPosition();
			Vector3 offset = rotation.getDirection().multiply(0.1);
			for (int i = 0; i < 100; i++) {
				startPosition = startPosition.add(offset);
				GeneralEffects.NOTE_PARTICLE.playGlobal(startPosition);
			}
			player.sendMessage("Yaw = " + rotation.getYaw());
			player.sendMessage("Pitch = " + rotation.getPitch());
		} else if (action.contains("packets")) {
			player.add(ForceMessages.class);
		} else {
			if (getEngine() instanceof Client) {
				throw new CommandException("You cannot resend chunks in client mode.");
			}
			if (action.contains("resendall")) {
				ServerNetworkSynchronizer network = (ServerNetworkSynchronizer) player.getNetworkSynchronizer();
				Set<Chunk> chunks = network.getActiveChunks();
				for (Chunk c : chunks) {
					network.sendChunk(c);
				}

				source.sendMessage("All chunks resent");
			} else if (action.contains("resend")) {
				((ServerNetworkSynchronizer) player.getNetworkSynchronizer()).sendChunk(player.getChunk());
				source.sendMessage("Chunk resent");
			}
		}
	}

	@CommandDescription(aliases = "spawn", desc = "Spawns a living entity at your location")
	@Filter(PlayerFilter.class)
	public void spawn(Player player, CommandArguments args) throws CommandException {
		final String name = args.popString("name");

		Class<? extends Component> clazz;
		boolean child = false;
		//See if it is a living?
		try {
			clazz = CreatureType.valueOf(name.toUpperCase()).getComponentType();
			if (Ageable.class.isAssignableFrom(clazz) && args.flags().hasFlag("child")) {
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
			final EntityPrefab prefab = getEngine().getFileSystem().getResource("entity://Vanilla/entities/" + clazz.getSimpleName().toLowerCase() + "/" + clazz.getSimpleName().toLowerCase() + ".sep");
			entity = prefab.createEntity(player.getPhysics().getPosition());
		} else {
			entity = player.getWorld().createEntity(player.getPhysics().getPosition(), clazz);
		}
		//Optional param was provided (ie the block material for a falling block).
		if (args.length() == 2) {
			//Now we know its either a living or substance. Lets figure out which.
			if (Living.class.isAssignableFrom(clazz)) {
				final Living living = entity.get(Living.class);
				if (name.equalsIgnoreCase("human")) {
					((Human) living).setName(args.popString("disp_name"));
				}
			} else if (Substance.class.isAssignableFrom(clazz)) {
				final Substance substance = entity.get(Substance.class);
				switch (ObjectType.valueOf(name.toUpperCase())) {
					case ITEM:
						Material item = VanillaArgumentTypes.popMaterial("item", args);
						((Item) substance).setItemStack(new ItemStack(item, 1));
						break;
					case FALLING_OBJECT:
						Material block = VanillaArgumentTypes.popMaterial("block", args);
						if (!(block instanceof BlockMaterial)) {
							throw new CommandException("Material " + block.getDisplayName() + " is not a block!");
						}
						((FallingBlock) substance).setMaterial((BlockMaterial) block);
						break;
				}
			}
		}
		args.assertCompletelyParsed();
		player.getWorld().spawnEntity(entity);
		if (child) {
			entity.get(Ageable.class).setAge(Ageable.MIN_AGE);
		}
	}

	@CommandDescription(aliases = "fire", usage = "<time> [hurt]", desc = "Set you on fire")
	@Permissible("vanilla.command.debug")
	@Filter(PlayerFilter.class)
	public void fire(Player player, CommandArguments args) throws CommandException {
		final float time = args.popFloat("time");
		final boolean hurt = args.popBoolean("hurt", false);
		args.assertCompletelyParsed();

		Burn fire = player.add(Burn.class);
		fire.setOnFire(time, hurt);
	}
}
