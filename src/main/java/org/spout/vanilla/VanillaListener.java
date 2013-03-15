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
package org.spout.vanilla;

import org.spout.api.Client;
import org.spout.api.Platform;

import org.spout.api.component.impl.CameraComponent;
import org.spout.api.component.impl.InteractComponent;
import org.spout.api.entity.Player;
import org.spout.api.event.EventHandler;
import org.spout.api.event.Listener;
import org.spout.api.event.Order;
import org.spout.api.event.Result;
import org.spout.api.event.block.BlockChangeEvent;
import org.spout.api.event.engine.EngineStartEvent;
import org.spout.api.event.entity.EntityHiddenEvent;
import org.spout.api.event.entity.EntityShownEvent;
import org.spout.api.event.player.PlayerJoinEvent;
import org.spout.api.event.server.permissions.PermissionNodeEvent;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.block.BlockSnapshot;

import org.spout.vanilla.component.entity.inventory.PlayerInventory;
import org.spout.vanilla.component.entity.inventory.WindowHolder;
import org.spout.vanilla.component.entity.living.Human;
import org.spout.vanilla.component.entity.misc.Health;
import org.spout.vanilla.component.entity.misc.Hunger;
import org.spout.vanilla.component.entity.misc.Level;
import org.spout.vanilla.component.entity.misc.PlayerItemCollector;
import org.spout.vanilla.component.entity.misc.Sleep;
import org.spout.vanilla.component.entity.player.HUD;
import org.spout.vanilla.component.entity.player.Ping;
import org.spout.vanilla.component.entity.player.PlayerList;
import org.spout.vanilla.component.entity.player.hud.VanillaArmorWidget;
import org.spout.vanilla.component.entity.player.hud.VanillaCrosshair;
import org.spout.vanilla.component.entity.player.hud.VanillaDrowning;
import org.spout.vanilla.component.entity.player.hud.VanillaExpBar;
import org.spout.vanilla.component.entity.player.hud.VanillaHunger;
import org.spout.vanilla.component.entity.player.hud.VanillaQuickbar;
import org.spout.vanilla.component.world.sky.Sky;
import org.spout.vanilla.data.configuration.VanillaConfiguration;
import org.spout.vanilla.event.block.RedstoneChangeEvent;
import org.spout.vanilla.input.VanillaInputExecutor;
import org.spout.vanilla.material.block.redstone.RedstoneSource;
import org.spout.vanilla.protocol.ClientAuthentification;
import org.spout.vanilla.protocol.PasteExceptionHandler;

public class VanillaListener implements Listener {
	private VanillaPlugin instance;
	public VanillaListener(VanillaPlugin plugin) {
		instance = plugin;
	}

	@EventHandler(order = Order.EARLIEST)
	public void onPermissionNode(PermissionNodeEvent event) {
		if (VanillaConfiguration.OPS.isOp(event.getSubject().getName())) {
			event.setResult(Result.ALLOW);
		}
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		player.add(Human.class).setName(player.getName());
		player.add(PlayerInventory.class);
		player.add(WindowHolder.class);
		player.add(PlayerList.class);
		player.add(Ping.class);
		player.add(PlayerItemCollector.class);
		player.add(Sleep.class);
		player.add(Hunger.class);
		player.add(Level.class);
		player.getSession().setUncaughtExceptionHandler(new PasteExceptionHandler(player.getSession()));
		Sky sky = player.getWorld().get(Sky.class);
		if (sky != null) {
			sky.updatePlayer(player);
		}
	}

	@EventHandler
	public void onGameStart(EngineStartEvent event) {
		if (instance.getEngine() instanceof Client) {
			return;
		}

		Player player = ((Client) instance.getEngine()).getActivePlayer();

		HUD HUD = player.add(org.spout.vanilla.component.entity.player.HUD.class);
		HUD.setDefault(VanillaArmorWidget.class);
		HUD.setDefault(VanillaQuickbar.class);
		HUD.setDefault(VanillaCrosshair.class);
		HUD.setDefault(VanillaExpBar.class);
		HUD.setDefault(VanillaDrowning.class);
		HUD.setDefault(VanillaHunger.class);
		HUD.setupHUD();
		HUD.openHUD();

		player.add(Human.class);
		player.add(PlayerInventory.class);
		player.add(WindowHolder.class);
		player.add(CameraComponent.class);
		player.add(Health.class);
		player.add(Hunger.class);
		player.add(InteractComponent.class).setRange(5f);

		((Client) instance.getEngine()).getInputManager().addInputExecutor(new VanillaInputExecutor(player));

		String username = VanillaConfiguration.USERNAME.getString();
		String password = VanillaConfiguration.PASSWORD.getString();

		Thread loginAuth = new Thread(new ClientAuthentification(username, password));
		loginAuth.start();
	}

	@EventHandler
	public void onBlockChange(BlockChangeEvent event) {
		if (RedstoneChangeEvent.getHandlerList().getRegisteredListeners().length == 0) {
			return;
		}

		//Redstone event
		BlockMaterial oldMat = event.getBlock().getMaterial();
		BlockMaterial newMat = event.getSnapshot().getMaterial();
		BlockSnapshot initialState = new BlockSnapshot(event.getBlock());
		//RedstoneChangeEvent
		//Three possibilities here:
		//1.) Redstone source material was placed, generating power
		//2.) Redstone source material was removed, removing power
		//3.) Redstone source material's data level changed, indicating change in power
		short prevPower = -1;
		short newPower = -1;
		if (!(oldMat instanceof RedstoneSource) && newMat instanceof RedstoneSource) {
			prevPower = 0;
			newPower = ((RedstoneSource) newMat).getRedstonePowerStrength(event.getSnapshot());
		} else if (!(newMat instanceof RedstoneSource) && oldMat instanceof RedstoneSource) {
			prevPower = ((RedstoneSource) oldMat).getRedstonePowerStrength(initialState);
			newPower = 0;
		} else if (newMat == oldMat && oldMat instanceof RedstoneSource) {
			prevPower = ((RedstoneSource) oldMat).getRedstonePowerStrength(initialState);
			newPower = ((RedstoneSource) newMat).getRedstonePowerStrength(event.getSnapshot());
		}
		if (prevPower != -1) {
			RedstoneChangeEvent redstoneEvent = new RedstoneChangeEvent(event.getBlock(), event.getCause(), prevPower, newPower);
			instance.getEngine().getEventManager().callEvent(redstoneEvent);
			if (redstoneEvent.isCancelled()) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onEntityHide(EntityHiddenEvent event) {
		//TODO maps, sounds, etc.
		if (event.getEntity() instanceof Player) {
			event.getHiddenFrom().get(PlayerList.class).force();
		}
	}

	@EventHandler
	public void onEntityShow(EntityShownEvent event) {
		if (event.getEntity() instanceof Player) {
			event.getHiddenFrom().get(PlayerList.class).force();
		}
	}
}
