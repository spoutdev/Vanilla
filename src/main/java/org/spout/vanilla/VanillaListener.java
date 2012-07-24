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
package org.spout.vanilla;

import java.util.HashSet;

import org.spout.api.Source;
import org.spout.api.entity.Entity;
import org.spout.api.entity.component.Controller;
import org.spout.api.event.EventHandler;
import org.spout.api.event.Listener;
import org.spout.api.event.Order;
import org.spout.api.event.Result;
import org.spout.api.event.entity.EntityHealthChangeEvent;
import org.spout.api.event.entity.EntitySpawnEvent;
import org.spout.api.event.player.PlayerJoinEvent;
import org.spout.api.event.server.permissions.PermissionNodeEvent;
import org.spout.api.event.world.RegionLoadEvent;
import org.spout.api.geo.cuboid.Region;
import org.spout.api.material.BlockMaterial;
import org.spout.api.permissions.PermissionsSubject;
import org.spout.api.player.Player;
import org.spout.api.scheduler.TaskPriority;

import org.spout.vanilla.configuration.VanillaConfiguration;
import org.spout.vanilla.configuration.WorldConfigurationNode;
import org.spout.vanilla.controller.VanillaControllerTypes;
import org.spout.vanilla.controller.living.creature.hostile.Ghast;
import org.spout.vanilla.controller.living.creature.passive.Sheep;
import org.spout.vanilla.controller.living.player.VanillaPlayer;
import org.spout.vanilla.controller.source.ControllerChangeReason;
import org.spout.vanilla.controller.source.HealthChangeReason;
import org.spout.vanilla.controller.world.RegionSpawner;
import org.spout.vanilla.data.VanillaData;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.protocol.VanillaNetworkSynchronizer;
import org.spout.vanilla.protocol.msg.UpdateHealthMessage;
import org.spout.vanilla.util.VanillaNetworkUtil;

public class VanillaListener implements Listener {
	private final VanillaPlugin plugin;

	public VanillaListener(VanillaPlugin plugin) {
		this.plugin = plugin;
	}

	@EventHandler(order = Order.EARLIEST)
	public void onPlayerJoin(PlayerJoinEvent event) {
		// Set their mode
		Player player = event.getPlayer();
		Entity playerEntity = player.getEntity();
		player.getSession().setNetworkSynchronizer(new VanillaNetworkSynchronizer(player, playerEntity));
		VanillaPlayer vanillaPlayer = new VanillaPlayer(player, playerEntity.getWorld().getDataMap().get(VanillaData.GAMEMODE));

		playerEntity.setController(vanillaPlayer, ControllerChangeReason.INITIALIZATION);

		// Set protocol and send packets
		if (vanillaPlayer.isSurvival()) {
			VanillaNetworkUtil.sendPacket(vanillaPlayer.getPlayer(), new UpdateHealthMessage((short) vanillaPlayer.getHealth(), vanillaPlayer.getHunger(), vanillaPlayer.getFoodSaturation()));
		}

		// Make them visible to everyone by default
		vanillaPlayer.setVisible(true);
	}

	@EventHandler
	public void onRegionLoad(RegionLoadEvent event) {
		Region region = event.getRegion();

		RegionSpawner spawner = new RegionSpawner(region);
		region.getTaskManager().scheduleSyncRepeatingTask(plugin, spawner, 100, 100, TaskPriority.LOW);

		WorldConfigurationNode worldConfig = VanillaConfiguration.WORLDS.getOrCreate(event.getWorld());
		if (worldConfig.SPAWN_ANIMALS.getBoolean()) {
			HashSet<BlockMaterial> grass = new HashSet<BlockMaterial>();
			grass.add(VanillaMaterials.GRASS);
			spawner.addSpawnableType(VanillaControllerTypes.SHEEP, grass, 5);

			spawner.addSpawnableType(VanillaControllerTypes.PIG, grass, 5);

			spawner.addSpawnableType(VanillaControllerTypes.COW, grass, 5);

			spawner.addSpawnableType(VanillaControllerTypes.CHICKEN, grass, 5);
		}
		if (worldConfig.SPAWN_MONSTERS.getBoolean()) {
			HashSet<BlockMaterial> endStone = new HashSet<BlockMaterial>();
			endStone.add(VanillaMaterials.END_STONE);
			spawner.addSpawnableType(VanillaControllerTypes.ENDERMAN, endStone, 7);
		}
	}

	@EventHandler(order = Order.MONITOR)
	public void onEntitySpawn(EntitySpawnEvent event) {
		if (event.isCancelled()) {
			return;
		}

		Entity entity = event.getEntity();
		Controller c = entity.getController();
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
	public void onPermissionNode(PermissionNodeEvent event) {
		PermissionsSubject subject = event.getSubject();
		if (VanillaConfiguration.OPS.isOp(subject.getName())) {
			event.setResult(Result.ALLOW);
		}
	}

	@EventHandler
	public void onHealthChange(EntityHealthChangeEvent event) {
		Source source = event.getSource();
		if (source == HealthChangeReason.SPAWN) {
			return;
		}

		Controller c = event.getEntity().getController();
		if (c instanceof VanillaPlayer && ((VanillaPlayer) c).isSurvival()) {
			VanillaPlayer sp = (VanillaPlayer) c;
			short health = (short) sp.getHealth();
			health += (short) event.getChange();
			VanillaNetworkUtil.sendPacket(sp.getPlayer(), new UpdateHealthMessage(health, sp.getHunger(), sp.getFoodSaturation()));
		}
	}
}
