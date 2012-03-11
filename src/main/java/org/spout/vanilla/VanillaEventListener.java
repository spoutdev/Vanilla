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
import org.spout.vanilla.entity.living.creature.neutral.Enderman;
import org.spout.vanilla.entity.living.creature.passive.Sheep;
import org.spout.vanilla.entity.living.player.CreativePlayer;
import org.spout.vanilla.entity.living.player.SurvivalPlayer;
import org.spout.vanilla.entity.world.RegionEntitySpawner;
import org.spout.vanilla.protocol.VanillaNetworkSynchronizer;

public class VanillaEventListener implements Listener {
	private final VanillaPlugin plugin;

	public VanillaEventListener(VanillaPlugin plugin) {
		this.plugin = plugin;
	}

	@EventHandler(order = Order.EARLIEST)
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		Controller mode;
		if (VanillaConfiguration.PLAYER_DEFAULT_GAMEMODE.getString().equalsIgnoreCase("creative")) {
			mode = new CreativePlayer(player);
		} else {
			mode = new SurvivalPlayer(player);
		}

		Entity playerEntity = player.getEntity();
		playerEntity.setController(mode);
		player.setNetworkSynchronizer(new VanillaNetworkSynchronizer(player, playerEntity));
	}

	@EventHandler(order = Order.LATEST)
	public void onPlayerLeave(PlayerLeaveEvent event) {
		Entity entity = event.getPlayer().getEntity();
		if (entity != null) {
			entity.getInventory().removeViewer(event.getPlayer().getNetworkSynchronizer());
		}
	}

	@EventHandler()
	public void onRegionLoad(RegionLoadEvent event) {
		Region region = event.getRegion();
		if (region.getAll(RegionEntitySpawner.class).isEmpty()) {
			region.getWorld().createAndSpawnEntity(new Point(region.getWorld(), region.getX() * Region.EDGE, region.getY() * Region.EDGE, region.getZ() * Region.EDGE), new RegionEntitySpawner(region));
		}
	}

	@EventHandler(order = Order.MONITOR)
	public void onEntitySpawn(EntitySpawnEvent event) {
		if (event.isCancelled()) {
			return;
		}

		Entity entity = event.getEntity();
		Controller c = entity.getController();
		if (c != null) {
			if (c instanceof RegionEntitySpawner) {
				RegionEntitySpawner spawner = (RegionEntitySpawner) c;
				HashSet<BlockMaterial> grass = new HashSet<BlockMaterial>();
				grass.add(VanillaMaterials.GRASS);
				spawner.addSpawnableType(Sheep.class, grass, 5);

				HashSet<BlockMaterial> endStone = new HashSet<BlockMaterial>();
				endStone.add(VanillaMaterials.END_STONE);
				spawner.addSpawnableType(Enderman.class, endStone, 7);
			}
		}
	}

	@EventHandler(order = Order.EARLIEST)
	public void onPermissionNode(PermissionNodeEvent event) {
		PermissionsSubject subject = event.getSubject();
		if (VanillaConfiguration.OPS.isOp(subject.getName())) {
			event.setResult(Result.ALLOW);
		}
	}
}
