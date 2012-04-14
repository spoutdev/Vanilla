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
import org.spout.api.event.entity.EntityControllerChangeEvent;
import org.spout.api.event.entity.EntityHealthChangeEvent;
import org.spout.vanilla.controller.VanillaControllerTypes;
import org.spout.vanilla.controller.source.ControllerInitialization;
import org.spout.vanilla.controller.source.HealthChangeReason;
import org.spout.vanilla.material.VanillaMaterials;

import java.util.Arrays;
import java.util.HashSet;

import org.spout.api.Spout;
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
import org.spout.vanilla.controller.VanillaController;
import org.spout.vanilla.controller.living.Creature;
import org.spout.vanilla.controller.living.creature.hostile.Ghast;
import org.spout.vanilla.controller.living.player.CreativePlayer;
import org.spout.vanilla.controller.living.player.SurvivalPlayer;
import org.spout.vanilla.controller.living.player.VanillaPlayer;
import org.spout.vanilla.controller.world.RegionSpawner;
import org.spout.vanilla.protocol.VanillaNetworkSynchronizer;
import org.spout.vanilla.protocol.event.HealthEvent;
import org.spout.vanilla.protocol.event.SpawnPlayerEvent;
import org.spout.vanilla.protocol.event.StateChangeEvent;
import org.spout.vanilla.protocol.msg.UserListItemMessage;

public class VanillaEventListener implements Listener {
	private final VanillaPlugin plugin;

	public VanillaEventListener(VanillaPlugin plugin) {
		this.plugin = plugin;
	}

	@EventHandler(order = Order.EARLIEST)
	public void playerJoin(PlayerJoinEvent event) {
		
		// Set their mode
		Player player = event.getPlayer();
		VanillaPlayer mode;
		if (VanillaConfiguration.PLAYER_DEFAULT_GAMEMODE.getString().equalsIgnoreCase("creative")) {
			mode = new CreativePlayer(player);
		} else {
			mode = new SurvivalPlayer(player);
		}

		Entity playerEntity = player.getEntity();
		playerEntity.setController(mode, new ControllerInitialization());

		// Set protocol and send packets
		player.setNetworkSynchronizer(new VanillaNetworkSynchronizer(player, playerEntity));
		if (mode instanceof SurvivalPlayer) {
			SurvivalPlayer s = (SurvivalPlayer) mode;
			player.getNetworkSynchronizer().callProtocolEvent(new HealthEvent((short) playerEntity.getHealth(), s.getHunger(), s.getFoodSaturation()));
		}

		for (Player p : playerEntity.getWorld().getPlayers()) {
			player.getNetworkSynchronizer().callProtocolEvent(new SpawnPlayerEvent(player));
		}
	}

	@EventHandler(order = Order.LATEST)
	public void playerLeave(PlayerLeaveEvent event) {
		Entity entity = event.getPlayer().getEntity();
		if (entity != null) {
			entity.getInventory().removeViewer(event.getPlayer().getNetworkSynchronizer());
		}
		//Tell anyone that we have an player less :(
		((VanillaController) entity.getController()).sendMessage(new HashSet<Player>(Arrays.asList(Spout.getEngine().getOnlinePlayers())), new UserListItemMessage(event.getPlayer().getName(), false, (short) 99));
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

		// debug code
		if (c instanceof Creature) {
			Creature creature = (Creature) c;
			creature.setTimeUntilAdult(-23999);
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
		if (c instanceof SurvivalPlayer) {
			SurvivalPlayer sp = (SurvivalPlayer) c;
			short health = (short) sp.getParent().getHealth();
			health += (short) event.getChange();
			sp.getPlayer().getNetworkSynchronizer().callProtocolEvent(new HealthEvent(health, sp.getHunger(), sp.getFoodSaturation()));
		}
	}

	@EventHandler
	public void syncGameMode(EntityControllerChangeEvent event) {
		Controller c = event.getNewController();
		if (!(c instanceof VanillaPlayer) || event.getSource() instanceof ControllerInitialization) {
			return;
		}

		VanillaPlayer p = (VanillaPlayer) c;
		byte mode = 0;
		if (p instanceof CreativePlayer) {
			mode = 1;
		}

		p.getPlayer().getNetworkSynchronizer().callProtocolEvent(new StateChangeEvent((byte) 3, mode));
	}
}
