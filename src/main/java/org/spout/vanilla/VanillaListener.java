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

import org.spout.api.Client;
import org.spout.api.Spout;
import org.spout.api.entity.Player;
import org.spout.api.event.EventHandler;
import org.spout.api.event.Listener;
import org.spout.api.event.Order;
import org.spout.api.event.Result;
import org.spout.api.event.block.BlockChangeEvent;
import org.spout.api.event.player.PlayerJoinEvent;
import org.spout.api.event.server.ClientEnableEvent;
import org.spout.api.event.server.permissions.PermissionNodeEvent;
import org.spout.api.keyboard.Input;
import org.spout.api.material.BlockMaterial;
import org.spout.api.plugin.Platform;
import org.spout.vanilla.component.inventory.window.DefaultWindow;
import org.spout.vanilla.component.living.Human;
import org.spout.vanilla.component.misc.PickupItemComponent;
import org.spout.vanilla.component.misc.SleepComponent;
import org.spout.vanilla.component.player.HUDComponent;
import org.spout.vanilla.component.player.PingComponent;
import org.spout.vanilla.component.player.PlayerListComponent;
import org.spout.vanilla.configuration.VanillaConfiguration;
import org.spout.vanilla.entity.state.VanillaPlayerInputState;
import org.spout.vanilla.event.block.RedstoneChangeEvent;
import org.spout.vanilla.material.block.redstone.RedstoneSource;

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
		player.add(DefaultWindow.class);
		player.add(PlayerListComponent.class);
		player.add(PingComponent.class);
		player.add(PickupItemComponent.class);
		player.add(SleepComponent.class);
	}

	@EventHandler
	public void onClientEnable(ClientEnableEvent event) {
		final HUDComponent HUD = ((Client) Spout.getEngine()).getActivePlayer().add(HUDComponent.class);
		HUD.openHUD();
		
		((Client) Spout.getEngine()).getActivePlayer().addInputState(VanillaPlugin.class, VanillaPlayerInputState.DEFAULT_STATE);

		if(Spout.getEngine().getPlatform() == Platform.CLIENT){
			Input input = ((Client)Spout.getEngine()).getInput();
			input.bind(VanillaConfiguration.FORWARD.getString(), "+Forward");
			input.bind(VanillaConfiguration.BACKWARD.getString(), "+BackWard");
			input.bind(VanillaConfiguration.LEFT.getString(), "+Left");
			input.bind(VanillaConfiguration.RIGHT.getString(), "+Right");
			input.bind(VanillaConfiguration.UP.getString(), "+Jump");
			input.bind(VanillaConfiguration.DOWN.getString(), "+Crouch");
			input.bind("KEY_F3", "debug_infos");
			input.bind("KEY_SCROLLDOWN", "+Select_Down");
			input.bind("KEY_SCROLLUP", "+Select_Up");
			input.bind("MOUSE_BUTTON0", "+FIRE_1");
			input.bind("MOUSE_BUTTON1", "+INTERACT");
			input.bind("MOUSE_BUTTON2", "+FIRE_2");
		}
	}

	@EventHandler
	public void onBlockChange(BlockChangeEvent event) {
		if (event.isCancelled()) {
			return;
		}

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
		short newPower =  -1;
		if (!(oldMat instanceof RedstoneSource) && newMat instanceof RedstoneSource) {
			prevPower = 0;
			newPower = ((RedstoneSource)newMat).getRedstonePowerStrength(newData);
		} else if (!(newMat instanceof RedstoneSource) && oldMat instanceof RedstoneSource) {
			prevPower = ((RedstoneSource)oldMat).getRedstonePowerStrength(prevData);
			newPower = 0;
		} else if (newMat == oldMat && oldMat instanceof RedstoneSource) {
			prevPower = ((RedstoneSource)oldMat).getRedstonePowerStrength(prevData);
			newPower = ((RedstoneSource)newMat).getRedstonePowerStrength(newData);
		}
		if (prevPower != -1) {
			RedstoneChangeEvent redstoneEvent = new RedstoneChangeEvent(event.getBlock(), event.getCause(), prevPower, newPower);
			Spout.getEventManager().callEvent(redstoneEvent);
			if (redstoneEvent.isCancelled()) {
				event.setCancelled(true);
			}
		}
	}
}
