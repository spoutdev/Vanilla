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
package org.spout.vanilla.protocol;

import org.spout.api.entity.Entity;
import org.spout.api.player.Player;
import org.spout.api.protocol.Protocol;
import org.spout.api.protocol.Session;
import org.spout.vanilla.configuration.VanillaConfiguration;
import org.spout.vanilla.controller.living.player.GameMode;
import org.spout.vanilla.controller.living.player.VanillaPlayer;
import org.spout.vanilla.controller.source.ControllerChangeReason;
import org.spout.vanilla.protocol.event.UpdateHealthProtocolEvent;

public class VanillaProtocol extends Protocol {
	public VanillaProtocol() {
		super("Vanilla", new VanillaCodecLookupService(), new VanillaHandlerLookupService(), new VanillaPlayerProtocol());
	}

	public void initializePlayer(Player player, Session session) {
		Entity playerEntity = player.getEntity();
		player.setNetworkSynchronizer(new VanillaNetworkSynchronizer(player, session, playerEntity));
		VanillaPlayer vanillaPlayer;
		if (VanillaConfiguration.PLAYER_DEFAULT_GAMEMODE.getString().equalsIgnoreCase("creative")) {
			vanillaPlayer = new VanillaPlayer(player, GameMode.CREATIVE);
		} else {
			vanillaPlayer = new VanillaPlayer(player, GameMode.SURVIVAL);
		}

		playerEntity.setController(vanillaPlayer, ControllerChangeReason.INITIALIZATION);

		// Set protocol and send packets
		if (vanillaPlayer.isSurvival()) {
			player.getNetworkSynchronizer().callProtocolEvent(new UpdateHealthProtocolEvent((short) vanillaPlayer.getHealth(), vanillaPlayer.getHunger(), vanillaPlayer.getFoodSaturation()));
		}
	}
}
