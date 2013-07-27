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
package org.spout.vanilla.protocol;

import org.spout.api.protocol.HandlerLookupService;

import org.spout.vanilla.protocol.handler.ServerListPingHandler;
import org.spout.vanilla.protocol.handler.ServerPluginHandler;
import org.spout.vanilla.protocol.handler.auth.EncryptionKeyRequestHandler;
import org.spout.vanilla.protocol.handler.auth.EncryptionKeyResponseHandler;
import org.spout.vanilla.protocol.handler.entity.EntityActionHandler;
import org.spout.vanilla.protocol.handler.entity.EntityAnimationHandler;
import org.spout.vanilla.protocol.handler.entity.EntityAttachEntityHandler;
import org.spout.vanilla.protocol.handler.entity.EntityDestroyHandler;
import org.spout.vanilla.protocol.handler.entity.EntityEffectHandler;
import org.spout.vanilla.protocol.handler.entity.EntityEquipmentHandler;
import org.spout.vanilla.protocol.handler.entity.EntityExperienceOrbHandler;
import org.spout.vanilla.protocol.handler.entity.EntityInitializeHandler;
import org.spout.vanilla.protocol.handler.entity.EntityItemDataHandler;
import org.spout.vanilla.protocol.handler.entity.EntityMetadataHandler;
import org.spout.vanilla.protocol.handler.entity.EntityMobHandler;
import org.spout.vanilla.protocol.handler.entity.EntityObjectHandler;
import org.spout.vanilla.protocol.handler.entity.EntityPaintingHandler;
import org.spout.vanilla.protocol.handler.entity.EntityStatusHandler;
import org.spout.vanilla.protocol.handler.entity.EntityThunderboltHandler;
import org.spout.vanilla.protocol.handler.entity.EntityTileDataHandler;
import org.spout.vanilla.protocol.handler.entity.effect.EntityRemoveEffectHandler;
import org.spout.vanilla.protocol.handler.entity.pos.EntityHeadYawHandler;
import org.spout.vanilla.protocol.handler.entity.pos.EntityRelativePositionHandler;
import org.spout.vanilla.protocol.handler.entity.pos.EntityRelativePositionYawHandler;
import org.spout.vanilla.protocol.handler.entity.pos.EntityTeleportHandler;
import org.spout.vanilla.protocol.handler.entity.pos.EntityVelocityHandler;
import org.spout.vanilla.protocol.handler.entity.pos.EntityYawHandler;
import org.spout.vanilla.protocol.handler.player.PlayerAbilityHandler;
import org.spout.vanilla.protocol.handler.player.PlayerBedHandler;
import org.spout.vanilla.protocol.handler.player.PlayerBlockPlacementHandler;
import org.spout.vanilla.protocol.handler.player.PlayerChatHandler;
import org.spout.vanilla.protocol.handler.player.PlayerCollectItemHandler;
import org.spout.vanilla.protocol.handler.player.PlayerDiggingHandler;
import org.spout.vanilla.protocol.handler.player.PlayerExperienceHandler;
import org.spout.vanilla.protocol.handler.player.PlayerGameStateHandler;
import org.spout.vanilla.protocol.handler.player.PlayerGroundHandler;
import org.spout.vanilla.protocol.handler.player.PlayerHealthHandler;
import org.spout.vanilla.protocol.handler.player.PlayerHeldItemChangeHandler;
import org.spout.vanilla.protocol.handler.player.PlayerLocaleViewDistanceHandler;
import org.spout.vanilla.protocol.handler.player.PlayerRespawnHandler;
import org.spout.vanilla.protocol.handler.player.PlayerStatisticHandler;
import org.spout.vanilla.protocol.handler.player.PlayerStatusHandler;
import org.spout.vanilla.protocol.handler.player.PlayerTabCompleteHandler;
import org.spout.vanilla.protocol.handler.player.PlayerTimeHandler;
import org.spout.vanilla.protocol.handler.player.PlayerUseEntityHandler;
import org.spout.vanilla.protocol.handler.player.conn.PlayerHandshakeHandler;
import org.spout.vanilla.protocol.handler.player.conn.PlayerKickHandler;
import org.spout.vanilla.protocol.handler.player.conn.PlayerListHandler;
import org.spout.vanilla.protocol.handler.player.conn.PlayerLoginRequestHandler;
import org.spout.vanilla.protocol.handler.player.conn.PlayerPingHandler;
import org.spout.vanilla.protocol.handler.player.pos.PlayerLookHandler;
import org.spout.vanilla.protocol.handler.player.pos.PlayerPositionHandler;
import org.spout.vanilla.protocol.handler.player.pos.PlayerPositionLookHandler;
import org.spout.vanilla.protocol.handler.player.pos.PlayerSpawnHandler;
import org.spout.vanilla.protocol.handler.player.pos.PlayerSpawnPositionHandler;
import org.spout.vanilla.protocol.handler.scoreboard.ScoreboardDisplayHandler;
import org.spout.vanilla.protocol.handler.scoreboard.ScoreboardObjectiveHandler;
import org.spout.vanilla.protocol.handler.scoreboard.ScoreboardScoreHandler;
import org.spout.vanilla.protocol.handler.scoreboard.ScoreboardTeamHandler;
import org.spout.vanilla.protocol.handler.window.WindowClickHandler;
import org.spout.vanilla.protocol.handler.window.WindowCloseHandler;
import org.spout.vanilla.protocol.handler.window.WindowCreativeActionHandler;
import org.spout.vanilla.protocol.handler.window.WindowEnchantItemHandler;
import org.spout.vanilla.protocol.handler.window.WindowItemsHandler;
import org.spout.vanilla.protocol.handler.window.WindowOpenHandler;
import org.spout.vanilla.protocol.handler.window.WindowPropertyHandler;
import org.spout.vanilla.protocol.handler.window.WindowSlotHandler;
import org.spout.vanilla.protocol.handler.window.WindowTransactionHandler;
import org.spout.vanilla.protocol.handler.world.EffectHandler;
import org.spout.vanilla.protocol.handler.world.ExplosionHandler;
import org.spout.vanilla.protocol.handler.world.ParticleEffectHandler;
import org.spout.vanilla.protocol.handler.world.SoundEffectHandler;
import org.spout.vanilla.protocol.handler.world.block.BlockActionHandler;
import org.spout.vanilla.protocol.handler.world.block.BlockBreakAnimationHandler;
import org.spout.vanilla.protocol.handler.world.block.BlockBulkHandler;
import org.spout.vanilla.protocol.handler.world.block.BlockChangeHandler;
import org.spout.vanilla.protocol.handler.world.block.SignHandler;
import org.spout.vanilla.protocol.handler.world.chunk.ChunkBulkHandler;
import org.spout.vanilla.protocol.handler.world.chunk.ChunkDataHandler;
import org.spout.vanilla.protocol.msg.ServerListPingMessage;
import org.spout.vanilla.protocol.msg.ServerPluginMessage;
import org.spout.vanilla.protocol.msg.auth.EncryptionKeyRequestMessage;
import org.spout.vanilla.protocol.msg.auth.EncryptionKeyResponseMessage;
import org.spout.vanilla.protocol.msg.entity.EntityActionMessage;
import org.spout.vanilla.protocol.msg.entity.EntityAnimationMessage;
import org.spout.vanilla.protocol.msg.entity.EntityAttachEntityMessage;
import org.spout.vanilla.protocol.msg.entity.EntityDestroyMessage;
import org.spout.vanilla.protocol.msg.entity.EntityEquipmentMessage;
import org.spout.vanilla.protocol.msg.entity.EntityInitializeMessage;
import org.spout.vanilla.protocol.msg.entity.EntityItemDataMessage;
import org.spout.vanilla.protocol.msg.entity.EntityMetadataMessage;
import org.spout.vanilla.protocol.msg.entity.EntityStatusMessage;
import org.spout.vanilla.protocol.msg.entity.EntityTileDataMessage;
import org.spout.vanilla.protocol.msg.entity.effect.EntityEffectMessage;
import org.spout.vanilla.protocol.msg.entity.effect.EntityRemoveEffectMessage;
import org.spout.vanilla.protocol.msg.entity.pos.EntityHeadYawMessage;
import org.spout.vanilla.protocol.msg.entity.pos.EntityRelativePositionMessage;
import org.spout.vanilla.protocol.msg.entity.pos.EntityRelativePositionYawMessage;
import org.spout.vanilla.protocol.msg.entity.pos.EntityTeleportMessage;
import org.spout.vanilla.protocol.msg.entity.pos.EntityVelocityMessage;
import org.spout.vanilla.protocol.msg.entity.pos.EntityYawMessage;
import org.spout.vanilla.protocol.msg.entity.spawn.EntityExperienceOrbMessage;
import org.spout.vanilla.protocol.msg.entity.spawn.EntityMobMessage;
import org.spout.vanilla.protocol.msg.entity.spawn.EntityObjectMessage;
import org.spout.vanilla.protocol.msg.entity.spawn.EntityPaintingMessage;
import org.spout.vanilla.protocol.msg.entity.spawn.EntityThunderboltMessage;
import org.spout.vanilla.protocol.msg.player.PlayerAbilityMessage;
import org.spout.vanilla.protocol.msg.player.PlayerBedMessage;
import org.spout.vanilla.protocol.msg.player.PlayerBlockPlacementMessage;
import org.spout.vanilla.protocol.msg.player.PlayerChatMessage;
import org.spout.vanilla.protocol.msg.player.PlayerCollectItemMessage;
import org.spout.vanilla.protocol.msg.player.PlayerDiggingMessage;
import org.spout.vanilla.protocol.msg.player.PlayerExperienceMessage;
import org.spout.vanilla.protocol.msg.player.PlayerGameStateMessage;
import org.spout.vanilla.protocol.msg.player.PlayerGroundMessage;
import org.spout.vanilla.protocol.msg.player.PlayerHealthMessage;
import org.spout.vanilla.protocol.msg.player.PlayerHeldItemChangeMessage;
import org.spout.vanilla.protocol.msg.player.PlayerLocaleViewDistanceMessage;
import org.spout.vanilla.protocol.msg.player.PlayerStatisticMessage;
import org.spout.vanilla.protocol.msg.player.PlayerStatusMessage;
import org.spout.vanilla.protocol.msg.player.PlayerTabCompleteMessage;
import org.spout.vanilla.protocol.msg.player.PlayerTimeMessage;
import org.spout.vanilla.protocol.msg.player.PlayerUseEntityMessage;
import org.spout.vanilla.protocol.msg.player.conn.PlayerHandshakeMessage;
import org.spout.vanilla.protocol.msg.player.conn.PlayerKickMessage;
import org.spout.vanilla.protocol.msg.player.conn.PlayerListMessage;
import org.spout.vanilla.protocol.msg.player.conn.PlayerLoginRequestMessage;
import org.spout.vanilla.protocol.msg.player.conn.PlayerPingMessage;
import org.spout.vanilla.protocol.msg.player.pos.PlayerLookMessage;
import org.spout.vanilla.protocol.msg.player.pos.PlayerPositionLookMessage;
import org.spout.vanilla.protocol.msg.player.pos.PlayerPositionMessage;
import org.spout.vanilla.protocol.msg.player.pos.PlayerRespawnMessage;
import org.spout.vanilla.protocol.msg.player.pos.PlayerSpawnMessage;
import org.spout.vanilla.protocol.msg.player.pos.PlayerSpawnPositionMessage;
import org.spout.vanilla.protocol.msg.scoreboard.ScoreboardDisplayMessage;
import org.spout.vanilla.protocol.msg.scoreboard.ScoreboardObjectiveMessage;
import org.spout.vanilla.protocol.msg.scoreboard.ScoreboardScoreMessage;
import org.spout.vanilla.protocol.msg.scoreboard.ScoreboardTeamMessage;
import org.spout.vanilla.protocol.msg.window.WindowClickMessage;
import org.spout.vanilla.protocol.msg.window.WindowCloseMessage;
import org.spout.vanilla.protocol.msg.window.WindowCreativeActionMessage;
import org.spout.vanilla.protocol.msg.window.WindowEnchantItemMessage;
import org.spout.vanilla.protocol.msg.window.WindowItemsMessage;
import org.spout.vanilla.protocol.msg.window.WindowOpenMessage;
import org.spout.vanilla.protocol.msg.window.WindowPropertyMessage;
import org.spout.vanilla.protocol.msg.window.WindowSlotMessage;
import org.spout.vanilla.protocol.msg.window.WindowTransactionMessage;
import org.spout.vanilla.protocol.msg.world.EffectMessage;
import org.spout.vanilla.protocol.msg.world.ExplosionMessage;
import org.spout.vanilla.protocol.msg.world.ParticleEffectMessage;
import org.spout.vanilla.protocol.msg.world.SoundEffectMessage;
import org.spout.vanilla.protocol.msg.world.block.BlockActionMessage;
import org.spout.vanilla.protocol.msg.world.block.BlockBreakAnimationMessage;
import org.spout.vanilla.protocol.msg.world.block.BlockBulkMessage;
import org.spout.vanilla.protocol.msg.world.block.BlockChangeMessage;
import org.spout.vanilla.protocol.msg.world.block.SignMessage;
import org.spout.vanilla.protocol.msg.world.chunk.ChunkBulkMessage;
import org.spout.vanilla.protocol.msg.world.chunk.ChunkDataMessage;

public class VanillaHandlerLookupService extends HandlerLookupService {
	public VanillaHandlerLookupService() {
		try {
			/* 0x00 */
			bind(PlayerPingMessage.class, PlayerPingHandler.class);
			/* 0x01 */
			bind(PlayerLoginRequestMessage.class, PlayerLoginRequestHandler.class);
			/* 0x02 */
			bind(PlayerHandshakeMessage.class, PlayerHandshakeHandler.class);
			/* 0x03 */
			bind(PlayerChatMessage.class, PlayerChatHandler.class);
			/* 0x04 */
			bind(PlayerTimeMessage.class, PlayerTimeHandler.class);
			/* 0x05 */
			bind(EntityEquipmentMessage.class, EntityEquipmentHandler.class);
			/* 0x06 */
			bind(PlayerSpawnPositionMessage.class, PlayerSpawnPositionHandler.class);
			/* 0x07 */
			bind(PlayerUseEntityMessage.class, PlayerUseEntityHandler.class);
			/* 0x08 */
			bind(PlayerHealthMessage.class, PlayerHealthHandler.class);
			/* 0x09 */
			bind(PlayerRespawnMessage.class, PlayerRespawnHandler.class);
			/* 0x0A */
			bind(PlayerGroundMessage.class, PlayerGroundHandler.class);
			/* 0x0B */
			bind(PlayerPositionMessage.class, PlayerPositionHandler.class);
			/* 0x0C */
			bind(PlayerLookMessage.class, PlayerLookHandler.class);
			/* 0x0D */
			bind(PlayerPositionLookMessage.class, PlayerPositionLookHandler.class);
			/* 0x0E */
			bind(PlayerDiggingMessage.class, PlayerDiggingHandler.class);
			/* 0x0F */
			bind(PlayerBlockPlacementMessage.class, PlayerBlockPlacementHandler.class);
			/* 0x10 */
			bind(PlayerHeldItemChangeMessage.class, PlayerHeldItemChangeHandler.class);
			/* 0x11 */
			bind(PlayerBedMessage.class, PlayerBedHandler.class);
			/* 0x12 */
			bind(EntityAnimationMessage.class, EntityAnimationHandler.class);
			/* 0x13 */
			bind(EntityActionMessage.class, EntityActionHandler.class);
			/* 0x14 */
			bind(PlayerSpawnMessage.class, PlayerSpawnHandler.class);
			/* 0x15 */
			//bind(EntityItemCodec.class); // Removed as of 1.4.6
			/* 0x16 */
			bind(PlayerCollectItemMessage.class, PlayerCollectItemHandler.class);
			/* 0x17 */
			bind(EntityObjectMessage.class, EntityObjectHandler.class);
			/* 0x18 */
			bind(EntityMobMessage.class, EntityMobHandler.class);
			/* 0x19 */
			bind(EntityPaintingMessage.class, EntityPaintingHandler.class);
			/* 0x1A */
			bind(EntityExperienceOrbMessage.class, EntityExperienceOrbHandler.class);
			/* 0x1C */
			bind(EntityVelocityMessage.class, EntityVelocityHandler.class);
			/* 0x1D */
			bind(EntityDestroyMessage.class, EntityDestroyHandler.class);
			/* 0x1E */
			bind(EntityInitializeMessage.class, EntityInitializeHandler.class); //TODO the meaning of this packet is basically that the entity did not move/look since the last such packet. We need to implement this!
			/* 0x1F */
			bind(EntityRelativePositionMessage.class, EntityRelativePositionHandler.class);
			/* 0x20 */
			bind(EntityYawMessage.class, EntityYawHandler.class); //TODO rename Entity Look on the minecraft protocol page
			/* 0x21 */
			bind(EntityRelativePositionYawMessage.class, EntityRelativePositionYawHandler.class);  //TODO same as above
			/* 0x22 */
			bind(EntityTeleportMessage.class, EntityTeleportHandler.class);
			/* 0x23 */
			bind(EntityHeadYawMessage.class, EntityHeadYawHandler.class);
			/* 0x26 */
			bind(EntityStatusMessage.class, EntityStatusHandler.class);
			/* 0x27 */
			bind(EntityAttachEntityMessage.class, EntityAttachEntityHandler.class);
			/* 0x28 */
			bind(EntityMetadataMessage.class, EntityMetadataHandler.class);
			/* 0x29 */
			bind(EntityEffectMessage.class, EntityEffectHandler.class);
			/* 0x2A */
			bind(EntityRemoveEffectMessage.class, EntityRemoveEffectHandler.class);
			/* 0x2B */
			bind(PlayerExperienceMessage.class, PlayerExperienceHandler.class);
			/* 0x33 */
			bind(ChunkDataMessage.class, ChunkDataHandler.class); //TODO rename on the minecraft protocol page
			/* 0x34 */
			bind(BlockBulkMessage.class, BlockBulkHandler.class);
			/* 0x35 */
			bind(BlockChangeMessage.class, BlockChangeHandler.class);
			/* 0x36 */
			bind(BlockActionMessage.class, BlockActionHandler.class);
			/* 0x37 */
			bind(BlockBreakAnimationMessage.class, BlockBreakAnimationHandler.class);
			/* 0x38 */
			bind(ChunkBulkMessage.class, ChunkBulkHandler.class);
			/* 0x3C */
			bind(ExplosionMessage.class, ExplosionHandler.class);
			/* 0x3D */
			bind(EffectMessage.class, EffectHandler.class);
			/* 0x3E */
			bind(SoundEffectMessage.class, SoundEffectHandler.class);
			/* 0x3F */
			bind(ParticleEffectMessage.class, ParticleEffectHandler.class);
			/* 0x46 */
			bind(PlayerGameStateMessage.class, PlayerGameStateHandler.class);
			/* 0x47 */
			bind(EntityThunderboltMessage.class, EntityThunderboltHandler.class); //Minecraft protocol page -> Thunderbolt :/
			/* 0x64 */
			bind(WindowOpenMessage.class, WindowOpenHandler.class);
			/* 0x65 */
			bind(WindowCloseMessage.class, WindowCloseHandler.class);
			/* 0x66 */
			bind(WindowClickMessage.class, WindowClickHandler.class);
			/* 0x67 */
			bind(WindowSlotMessage.class, WindowSlotHandler.class);
			/* 0x68 */
			bind(WindowItemsMessage.class, WindowItemsHandler.class);
			/* 0x69 */
			bind(WindowPropertyMessage.class, WindowPropertyHandler.class); //Update Window Property on the protocol page!
			/* 0x6A */
			bind(WindowTransactionMessage.class, WindowTransactionHandler.class);
			/* 0x6B */
			bind(WindowCreativeActionMessage.class, WindowCreativeActionHandler.class);
			/* 0x6C */
			bind(WindowEnchantItemMessage.class, WindowEnchantItemHandler.class);
			/* 0x82 */
			bind(SignMessage.class, SignHandler.class);
			/* 0x83 */
			bind(EntityItemDataMessage.class, EntityItemDataHandler.class);
			/* 0x84 */
			bind(EntityTileDataMessage.class, EntityTileDataHandler.class);
			/* 0xC8 */
			bind(PlayerStatisticMessage.class, PlayerStatisticHandler.class);
			/* 0xC9 */
			bind(PlayerListMessage.class, PlayerListHandler.class);
			/* 0xCA */
			bind(PlayerAbilityMessage.class, PlayerAbilityHandler.class);
			/* 0xCB */
			bind(PlayerTabCompleteMessage.class, PlayerTabCompleteHandler.class);
			/* 0xCC */
			bind(PlayerLocaleViewDistanceMessage.class, PlayerLocaleViewDistanceHandler.class);
			/* 0xCD */
			bind(PlayerStatusMessage.class, PlayerStatusHandler.class);
			/* 0xCE */
			bind(ScoreboardObjectiveMessage.class, ScoreboardObjectiveHandler.class);
			/* 0xCF */
			bind(ScoreboardScoreMessage.class, ScoreboardScoreHandler.class);
			/* 0xD0 */
			bind(ScoreboardDisplayMessage.class, ScoreboardDisplayHandler.class);
			/* 0xD1 */
			bind(ScoreboardTeamMessage.class, ScoreboardTeamHandler.class);
			/* 0xFA */
			bind(ServerPluginMessage.class, ServerPluginHandler.class);
			/* 0xFC */
			bind(EncryptionKeyResponseMessage.class, EncryptionKeyResponseHandler.class);
			/* 0xFD */
			bind(EncryptionKeyRequestMessage.class, EncryptionKeyRequestHandler.class);
			/* 0xFE */
			bind(ServerListPingMessage.class, ServerListPingHandler.class);
			/* 0xFF */
			bind(PlayerKickMessage.class, PlayerKickHandler.class);
		} catch (Exception ex) {
			throw new ExceptionInInitializerError(ex);
		}
	}
}
