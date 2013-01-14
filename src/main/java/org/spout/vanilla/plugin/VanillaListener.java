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
package org.spout.vanilla.plugin;

import org.spout.api.Client;
import org.spout.api.Spout;
import org.spout.api.component.impl.CameraComponent;
import org.spout.api.component.impl.HitBlockComponent;
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
import org.spout.api.plugin.Platform;

import org.spout.vanilla.api.material.block.redstone.RedstoneSource;
import org.spout.vanilla.plugin.component.inventory.PlayerInventory;
import org.spout.vanilla.plugin.component.inventory.WindowHolder;
import org.spout.vanilla.plugin.component.living.neutral.Human;
import org.spout.vanilla.plugin.component.misc.HealthComponent;
import org.spout.vanilla.plugin.component.misc.HungerComponent;
import org.spout.vanilla.plugin.component.misc.LevelComponent;
import org.spout.vanilla.plugin.component.misc.PickupItemComponent;
import org.spout.vanilla.plugin.component.misc.SleepComponent;
import org.spout.vanilla.plugin.component.player.HUDComponent;
import org.spout.vanilla.plugin.component.player.PingComponent;
import org.spout.vanilla.plugin.component.player.PlayerListComponent;
import org.spout.vanilla.plugin.configuration.VanillaConfiguration;
import org.spout.vanilla.plugin.event.block.RedstoneChangeEvent;
import org.spout.vanilla.plugin.input.VanillaInputExecutor;
import org.spout.vanilla.plugin.protocol.PasteExceptionHandler;

public class VanillaListener implements Listener {
	private final VanillaPlugin plugin;

	public VanillaListener(VanillaPlugin plugin) {
		this.plugin = plugin;
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
		player.add(PlayerListComponent.class);
		player.add(PingComponent.class);
		player.add(PickupItemComponent.class);
		player.add(SleepComponent.class);
		player.add(HungerComponent.class);
		player.add(LevelComponent.class);
		player.getSession().setUncaughtExceptionHandler(new PasteExceptionHandler(player.getSession()));
	}

	@EventHandler
	public void onGameStart(EngineStartEvent event) {
		if (Spout.getPlatform() != Platform.CLIENT) {
			return;
		}
		Player player = ((Client) Spout.getEngine()).getActivePlayer();
		player.add(Human.class);
		player.add(HUDComponent.class).openHUD();
		player.add(PlayerInventory.class);
		player.add(WindowHolder.class);
		player.add(CameraComponent.class).setSpeed(10f);
		player.add(HealthComponent.class);
		player.add(HungerComponent.class);
		player.add(HitBlockComponent.class).setRange(5f);

		((Client) Spout.getEngine()).getInputManager().addInputExecutors(new VanillaInputExecutor(player));
	}

	@EventHandler
	public void onBlockChange(BlockChangeEvent event) {
		if (RedstoneChangeEvent.getHandlerList().getRegisteredListeners().length == 0) {
			return;
		}

		//Redstone event
		BlockMaterial oldMat = event.getBlock().getMaterial();
		BlockMaterial newMat = event.getSnapshot().getMaterial();
		short prevData = event.getBlock().getData();
		short newData = event.getSnapshot().getData();
		//RedstoneChangeEvent
		//Three possibilities here:
		//1.) Redstone source material was placed, generating power
		//2.) Redstone source material was removed, removing power
		//3.) Redstone source material's data level changed, indicating change in power
		short prevPower = -1;
		short newPower = -1;
		if (!(oldMat instanceof RedstoneSource) && newMat instanceof RedstoneSource) {
			prevPower = 0;
			newPower = ((RedstoneSource) newMat).getRedstonePowerStrength(newData);
		} else if (!(newMat instanceof RedstoneSource) && oldMat instanceof RedstoneSource) {
			prevPower = ((RedstoneSource) oldMat).getRedstonePowerStrength(prevData);
			newPower = 0;
		} else if (newMat == oldMat && oldMat instanceof RedstoneSource) {
			prevPower = ((RedstoneSource) oldMat).getRedstonePowerStrength(prevData);
			newPower = ((RedstoneSource) newMat).getRedstonePowerStrength(newData);
		}
		if (prevPower != -1) {
			RedstoneChangeEvent redstoneEvent = new RedstoneChangeEvent(event.getBlock(), event.getCause(), prevPower, newPower);
			Spout.getEventManager().callEvent(redstoneEvent);
			if (redstoneEvent.isCancelled()) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onEntityHide(EntityHiddenEvent event) {
		//TODO maps, sounds, etc.
		if (event.getEntity() instanceof Player) {
			event.getHiddenFrom().get(PlayerListComponent.class).force();
		}
	}

	@EventHandler
	public void onEntityShow(EntityShownEvent event) {
		if (event.getEntity() instanceof Player) {
			event.getHiddenFrom().get(PlayerListComponent.class).force();
		}
	}
}
