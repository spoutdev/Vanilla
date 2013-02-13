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
package org.spout.vanilla.plugin.protocol;

import org.spout.api.protocol.HandlerLookupService;

import org.spout.vanilla.plugin.protocol.handler.ServerListPingHandler;
import org.spout.vanilla.plugin.protocol.handler.auth.EncryptionKeyRequestHandler;
import org.spout.vanilla.plugin.protocol.handler.auth.EncryptionKeyResponseHandler;
import org.spout.vanilla.plugin.protocol.handler.entity.EntityActionHandler;
import org.spout.vanilla.plugin.protocol.handler.entity.EntityAnimationHandler;
import org.spout.vanilla.plugin.protocol.handler.entity.EntityEquipmentHandler;
import org.spout.vanilla.plugin.protocol.handler.entity.pos.EntityHeadYawHandler;
import org.spout.vanilla.plugin.protocol.handler.player.PlayerAbilityHandler;
import org.spout.vanilla.plugin.protocol.handler.player.PlayerBlockPlacementHandler;
import org.spout.vanilla.plugin.protocol.handler.player.PlayerChatHandler;
import org.spout.vanilla.plugin.protocol.handler.player.PlayerDiggingHandler;
import org.spout.vanilla.plugin.protocol.handler.player.PlayerGroundHandler;
import org.spout.vanilla.plugin.protocol.handler.player.PlayerHealthHandler;
import org.spout.vanilla.plugin.protocol.handler.player.PlayerHeldItemChangeHandler;
import org.spout.vanilla.plugin.protocol.handler.player.PlayerLocaleViewDistanceHandler;
import org.spout.vanilla.plugin.protocol.handler.player.PlayerLookHandler;
import org.spout.vanilla.plugin.protocol.handler.player.PlayerPositionHandler;
import org.spout.vanilla.plugin.protocol.handler.player.PlayerPositionLookHandler;
import org.spout.vanilla.plugin.protocol.handler.player.PlayerStatusHandler;
import org.spout.vanilla.plugin.protocol.handler.player.PlayerTabCompleteHandler;
import org.spout.vanilla.plugin.protocol.handler.player.PlayerTimeHandler;
import org.spout.vanilla.plugin.protocol.handler.player.PlayerUseEntityHandler;
import org.spout.vanilla.plugin.protocol.handler.player.conn.PlayerHandshakeHandler;
import org.spout.vanilla.plugin.protocol.handler.player.conn.PlayerKickHandler;
import org.spout.vanilla.plugin.protocol.handler.player.conn.PlayerLoginRequestHandler;
import org.spout.vanilla.plugin.protocol.handler.player.conn.PlayerPingHandler;
import org.spout.vanilla.plugin.protocol.handler.player.pos.PlayerSpawnPositionHandler;
import org.spout.vanilla.plugin.protocol.handler.window.WindowClickHandler;
import org.spout.vanilla.plugin.protocol.handler.window.WindowCloseHandler;
import org.spout.vanilla.plugin.protocol.handler.window.WindowCreativeActionHandler;
import org.spout.vanilla.plugin.protocol.handler.window.WindowEnchantItemHandler;
import org.spout.vanilla.plugin.protocol.handler.world.block.SignHandler;
import org.spout.vanilla.plugin.protocol.msg.ServerListPingMessage;
import org.spout.vanilla.plugin.protocol.msg.auth.EncryptionKeyRequestMessage;
import org.spout.vanilla.plugin.protocol.msg.auth.EncryptionKeyResponseMessage;
import org.spout.vanilla.plugin.protocol.msg.entity.EntityActionMessage;
import org.spout.vanilla.plugin.protocol.msg.entity.EntityAnimationMessage;
import org.spout.vanilla.plugin.protocol.msg.entity.EntityEquipmentMessage;
import org.spout.vanilla.plugin.protocol.msg.entity.pos.EntityHeadYawMessage;
import org.spout.vanilla.plugin.protocol.msg.player.PlayerAbilityMessage;
import org.spout.vanilla.plugin.protocol.msg.player.PlayerBlockPlacementMessage;
import org.spout.vanilla.plugin.protocol.msg.player.PlayerChatMessage;
import org.spout.vanilla.plugin.protocol.msg.player.PlayerDiggingMessage;
import org.spout.vanilla.plugin.protocol.msg.player.PlayerGroundMessage;
import org.spout.vanilla.plugin.protocol.msg.player.PlayerHealthMessage;
import org.spout.vanilla.plugin.protocol.msg.player.PlayerHeldItemChangeMessage;
import org.spout.vanilla.plugin.protocol.msg.player.PlayerLocaleViewDistanceMessage;
import org.spout.vanilla.plugin.protocol.msg.player.PlayerStatusMessage;
import org.spout.vanilla.plugin.protocol.msg.player.PlayerTabCompleteMessage;
import org.spout.vanilla.plugin.protocol.msg.player.PlayerTimeMessage;
import org.spout.vanilla.plugin.protocol.msg.player.PlayerUseEntityMessage;
import org.spout.vanilla.plugin.protocol.msg.player.conn.PlayerHandshakeMessage;
import org.spout.vanilla.plugin.protocol.msg.player.conn.PlayerKickMessage;
import org.spout.vanilla.plugin.protocol.msg.player.conn.PlayerLoginRequestMessage;
import org.spout.vanilla.plugin.protocol.msg.player.conn.PlayerPingMessage;
import org.spout.vanilla.plugin.protocol.msg.player.pos.PlayerLookMessage;
import org.spout.vanilla.plugin.protocol.msg.player.pos.PlayerPositionLookMessage;
import org.spout.vanilla.plugin.protocol.msg.player.pos.PlayerPositionMessage;
import org.spout.vanilla.plugin.protocol.msg.player.pos.PlayerSpawnPositionMessage;
import org.spout.vanilla.plugin.protocol.msg.window.WindowClickMessage;
import org.spout.vanilla.plugin.protocol.msg.window.WindowCloseMessage;
import org.spout.vanilla.plugin.protocol.msg.window.WindowCreativeActionMessage;
import org.spout.vanilla.plugin.protocol.msg.window.WindowEnchantItemMessage;
import org.spout.vanilla.plugin.protocol.msg.world.block.SignMessage;

public class VanillaHandlerLookupService extends HandlerLookupService {
	public VanillaHandlerLookupService() {
		try {
			

			/* 0x04 */
			bind(PlayerTimeMessage.class, PlayerTimeHandler.class);
			/* 0x05 */
			bind(EntityEquipmentMessage.class, EntityEquipmentHandler.class);
			/* 0x06 */
			bind(PlayerSpawnPositionMessage.class, PlayerSpawnPositionHandler.class);
			
			/* 0x08 */
			bind(PlayerHealthMessage.class,PlayerHealthHandler.class);
			
			bind(PlayerHandshakeMessage.class, PlayerHandshakeHandler.class);
			bind(PlayerLoginRequestMessage.class, PlayerLoginRequestHandler.class);
			bind(PlayerChatMessage.class, PlayerChatHandler.class);
			bind(PlayerGroundMessage.class, PlayerGroundHandler.class);
			bind(PlayerPositionMessage.class, PlayerPositionHandler.class);
			bind(PlayerLookMessage.class, PlayerLookHandler.class);
			bind(PlayerPositionLookMessage.class, PlayerPositionLookHandler.class);
			bind(PlayerKickMessage.class, PlayerKickHandler.class);
			bind(PlayerDiggingMessage.class, PlayerDiggingHandler.class);
			bind(PlayerBlockPlacementMessage.class, PlayerBlockPlacementHandler.class);
			bind(WindowClickMessage.class, WindowClickHandler.class);
			bind(WindowCloseMessage.class, WindowCloseHandler.class);
			bind(PlayerHeldItemChangeMessage.class, PlayerHeldItemChangeHandler.class);
			bind(EntityActionMessage.class, EntityActionHandler.class);
			bind(EntityAnimationMessage.class, EntityAnimationHandler.class);
			bind(PlayerPingMessage.class, PlayerPingHandler.class);
			bind(WindowCreativeActionMessage.class, WindowCreativeActionHandler.class);
			bind(SignMessage.class, SignHandler.class);
			bind(PlayerUseEntityMessage.class, PlayerUseEntityHandler.class);
			bind(EntityHeadYawMessage.class, EntityHeadYawHandler.class);
			bind(PlayerAbilityMessage.class, PlayerAbilityHandler.class);
			bind(ServerListPingMessage.class, ServerListPingHandler.class);
			bind(PlayerStatusMessage.class, PlayerStatusHandler.class);
			bind(EncryptionKeyResponseMessage.class, EncryptionKeyResponseHandler.class);
			bind(EncryptionKeyRequestMessage.class, EncryptionKeyRequestHandler.class);
			bind(PlayerTabCompleteMessage.class, PlayerTabCompleteHandler.class);
			bind(PlayerLocaleViewDistanceMessage.class, PlayerLocaleViewDistanceHandler.class);
			bind(WindowEnchantItemMessage.class, WindowEnchantItemHandler.class);
		} catch (Exception ex) {
			throw new ExceptionInInitializerError(ex);
		}
	}
}
