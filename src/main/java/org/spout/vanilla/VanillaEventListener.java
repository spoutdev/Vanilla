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
 * the MIT license and the SpoutDev License Version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license,
 * including the MIT license.
 */
package org.spout.vanilla;

import org.spout.api.Source;
import org.spout.api.event.entity.EntityHealthChangeEvent;
import org.spout.api.inventory.ItemStack;
import org.spout.vanilla.controller.VanillaControllerTypes;
import org.spout.vanilla.controller.living.creature.passive.Sheep;
import org.spout.vanilla.controller.living.player.GameMode;
import org.spout.vanilla.controller.source.ControllerInitialization;
import org.spout.vanilla.controller.source.HealthChangeReason;
import org.spout.vanilla.material.VanillaMaterials;

import java.util.HashSet;

import org.spout.api.entity.Controller;
import org.spout.api.entity.Entity;
import org.spout.api.event.EventHandler;
import org.spout.api.event.Listener;
import org.spout.api.event.Order;
import org.spout.api.event.Result;
import org.spout.api.event.entity.EntitySpawnEvent;
import org.spout.api.event.player.PlayerJoinEvent;
import org.spout.api.event.player.PlayerLeaveEvent;
import org.spout.api.event.server.permissions.PermissionNodeEvent;
import org.spout.api.event.world.RegionLoadEvent;
import org.spout.api.geo.cuboid.Region;
import org.spout.api.geo.discrete.Point;
import org.spout.api.material.BlockMaterial;
import org.spout.api.permissions.PermissionsSubject;
import org.spout.api.player.Player;

import org.spout.vanilla.configuration.VanillaConfiguration;
import org.spout.vanilla.controller.living.creature.hostile.Ghast;
import org.spout.vanilla.controller.living.player.VanillaPlayer;
import org.spout.vanilla.controller.world.RegionSpawner;
import org.spout.vanilla.protocol.VanillaNetworkSynchronizer;
import org.spout.vanilla.protocol.msg.PlayerHealthMessage;
import org.spout.vanilla.protocol.msg.SpawnPlayerMessage;

public class VanillaEventListener implements Listener {
	@SuppressWarnings("unused")
	private final VanillaPlugin plugin;

	public VanillaEventListener(VanillaPlugin plugin) {
		this.plugin = plugin;
	}

	@EventHandler(order = Order.EARLIEST)
	public void playerJoin(PlayerJoinEvent event) {
		
		// Set their mode
		Player player = event.getPlayer();
		Entity playerEntity = player.getEntity();
		player.setNetworkSynchronizer(new VanillaNetworkSynchronizer(player, playerEntity));
		VanillaPlayer mode;
		if (VanillaConfiguration.PLAYER_DEFAULT_GAMEMODE.getString().equalsIgnoreCase("creative")) {
			mode = new VanillaPlayer(player, GameMode.CREATIVE);
		} else {
			mode = new VanillaPlayer(player, GameMode.SURVIVAL);
		}

		playerEntity.setController(mode, new ControllerInitialization());

		// Set protocol and send packets
		if (mode.isSurvival()) {
			mode.sendPacket(mode.getPlayer(), new PlayerHealthMessage((short) playerEntity.getHealth(), mode.getHunger(), mode.getFoodSaturation()));
		}
		
		int item = 0;
		ItemStack currentItem = playerEntity.getInventory().getCurrentItem();
		if (currentItem != null) {
			item = currentItem.getMaterial().getId();
		}

		mode.broadcastPacket(new SpawnPlayerMessage(playerEntity.getId(), player.getName(), playerEntity.getPosition(), (int) playerEntity.getYaw(), (int) playerEntity.getPitch(), item));
	}

	@EventHandler(order = Order.LATEST)
	public void playerLeave(PlayerLeaveEvent event) {
		Entity entity = event.getPlayer().getEntity();
		if (entity != null) {
			entity.getInventory().removeViewer(event.getPlayer().getNetworkSynchronizer());
		}
	}

	@EventHandler()
	public void regionLoad(RegionLoadEvent event) {
		Region region = event.getRegion();
		if (region.getAll(RegionSpawner.class).isEmpty()) {
			region.getWorld().createAndSpawnEntity(new Point(region.getWorld(), region.getX() * Region.EDGE, region.getY() * Region.EDGE, region.getZ() * Region.EDGE), new RegionSpawner(region));
		}
	}

	@EventHandler(order = Order.MONITOR)
	public void entitySpawn(EntitySpawnEvent event) {
		if (event.isCancelled()) {
			return;
		}

		Entity entity = event.getEntity();
		Controller c = entity.getController();
		if (c != null) {
			if (c instanceof RegionSpawner) {
				RegionSpawner spawner = (RegionSpawner) c;
				HashSet<BlockMaterial> grass = new HashSet<BlockMaterial>();
				grass.add(VanillaMaterials.GRASS);
				spawner.addSpawnableType(VanillaControllerTypes.SHEEP, grass, 5);

				HashSet<BlockMaterial> endStone = new HashSet<BlockMaterial>();
				endStone.add(VanillaMaterials.END_STONE);
				spawner.addSpawnableType(VanillaControllerTypes.ENDERMAN, endStone, 7);
			}
		}
		
		if (c instanceof Sheep) {
			Sheep sheep = (Sheep) c;
			sheep.setTimeUntilAdult(100);
		}

		if (c instanceof Ghast) {
			Ghast ghast = (Ghast) c;
			ghast.setRedEyes(true);
		}
	}

	@EventHandler(order = Order.EARLIEST)
	public void checkPermission(PermissionNodeEvent event) {
		PermissionsSubject subject = event.getSubject();
		if (VanillaConfiguration.OPS.isOp(subject.getName()) && event.getResult() == Result.DEFAULT) {
			event.setResult(Result.ALLOW);
		}
	}

	@EventHandler
	public void syncHealth(EntityHealthChangeEvent event) {
		Source source = event.getSource();
		if (source instanceof HealthChangeReason && ((HealthChangeReason) source).getType().equals(HealthChangeReason.Type.SPAWN)) {
			return;
		}

		Controller c = event.getEntity().getController();
		if (c instanceof VanillaPlayer && ((VanillaPlayer) c).isSurvival()) {
			VanillaPlayer sp = (VanillaPlayer) c;
			short health = (short) sp.getParent().getHealth();
			health += (short) event.getChange();
			sp.sendPacket(sp.getPlayer(), new PlayerHealthMessage(health, sp.getHunger(), sp.getFoodSaturation()));
		}
	}
}
