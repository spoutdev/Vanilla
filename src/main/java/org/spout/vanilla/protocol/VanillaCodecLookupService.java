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

import org.spout.api.protocol.CodecLookupService;

import org.spout.vanilla.protocol.codec.auth.EncryptionKeyRequestCodec;
import org.spout.vanilla.protocol.codec.auth.EncryptionKeyResponseCodec;
import org.spout.vanilla.protocol.codec.entity.EntityActionCodec;
import org.spout.vanilla.protocol.codec.entity.EntityAnimationCodec;
import org.spout.vanilla.protocol.codec.entity.EntityAttachEntityCodec;
import org.spout.vanilla.protocol.codec.entity.EntityDestroyCodec;
import org.spout.vanilla.protocol.codec.entity.EntityEquipmentCodec;
import org.spout.vanilla.protocol.codec.entity.EntityInitializeCodec;
import org.spout.vanilla.protocol.codec.entity.EntityInteractCodec;
import org.spout.vanilla.protocol.codec.entity.EntityItemDataCodec;
import org.spout.vanilla.protocol.codec.entity.EntityMetadataCodec;
import org.spout.vanilla.protocol.codec.entity.EntityRelativePositionCodec;
import org.spout.vanilla.protocol.codec.entity.EntityStatusCodec;
import org.spout.vanilla.protocol.codec.entity.EntityTileDataCodec;
import org.spout.vanilla.protocol.codec.entity.effect.EntityEffectCodec;
import org.spout.vanilla.protocol.codec.entity.effect.EntityRemoveEffectCodec;
import org.spout.vanilla.protocol.codec.entity.pos.EntityHeadYawCodec;
import org.spout.vanilla.protocol.codec.entity.pos.EntityRelativePositionYawCodec;
import org.spout.vanilla.protocol.codec.entity.pos.EntityTeleportCodec;
import org.spout.vanilla.protocol.codec.entity.pos.EntityVelocityCodec;
import org.spout.vanilla.protocol.codec.entity.pos.EntityYawCodec;
import org.spout.vanilla.protocol.codec.entity.spawn.EntityExperienceOrbCodec;
import org.spout.vanilla.protocol.codec.entity.spawn.EntityItemCodec;
import org.spout.vanilla.protocol.codec.entity.spawn.EntityMobCodec;
import org.spout.vanilla.protocol.codec.entity.spawn.EntityPaintingCodec;
import org.spout.vanilla.protocol.codec.entity.spawn.EntitySpawnObjectCodec;
import org.spout.vanilla.protocol.codec.entity.spawn.EntityThunderboltCodec;
import org.spout.vanilla.protocol.codec.player.PlayerAbilityCodec;
import org.spout.vanilla.protocol.codec.player.PlayerBedCodec;
import org.spout.vanilla.protocol.codec.player.PlayerBlockPlacementCodec;
import org.spout.vanilla.protocol.codec.player.PlayerChatCodec;
import org.spout.vanilla.protocol.codec.player.PlayerCollectItemCodec;
import org.spout.vanilla.protocol.codec.player.PlayerDiggingCodec;
import org.spout.vanilla.protocol.codec.player.PlayerExperienceCodec;
import org.spout.vanilla.protocol.codec.player.PlayerGameStateCodec;
import org.spout.vanilla.protocol.codec.player.PlayerGroundCodec;
import org.spout.vanilla.protocol.codec.player.PlayerHealthCodec;
import org.spout.vanilla.protocol.codec.player.PlayerHeldItemChangeCodec;
import org.spout.vanilla.protocol.codec.player.PlayerLocaleViewDistanceCodec;
import org.spout.vanilla.protocol.codec.player.PlayerSoundEffectCodec;
import org.spout.vanilla.protocol.codec.player.PlayerStatisticCodec;
import org.spout.vanilla.protocol.codec.player.PlayerStatusCodec;
import org.spout.vanilla.protocol.codec.player.PlayerTabCompleteCodec;
import org.spout.vanilla.protocol.codec.player.PlayerTimeCodec;
import org.spout.vanilla.protocol.codec.player.conn.PlayerHandshakeCodec;
import org.spout.vanilla.protocol.codec.player.conn.PlayerKickCodec;
import org.spout.vanilla.protocol.codec.player.conn.PlayerListCodec;
import org.spout.vanilla.protocol.codec.player.conn.PlayerLoginRequestCodec;
import org.spout.vanilla.protocol.codec.player.conn.PlayerPingCodec;
import org.spout.vanilla.protocol.codec.player.pos.PlayerPositionCodec;
import org.spout.vanilla.protocol.codec.player.pos.PlayerPositionYawCodec;
import org.spout.vanilla.protocol.codec.player.pos.PlayerRespawnCodec;
import org.spout.vanilla.protocol.codec.player.pos.PlayerSpawnCodec;
import org.spout.vanilla.protocol.codec.player.pos.PlayerSpawnPositionCodec;
import org.spout.vanilla.protocol.codec.player.pos.PlayerYawCodec;
import org.spout.vanilla.protocol.codec.server.ServerListPingCodec;
import org.spout.vanilla.protocol.codec.server.ServerPluginCodec;
import org.spout.vanilla.protocol.codec.window.WindowClickCodec;
import org.spout.vanilla.protocol.codec.window.WindowCloseCodec;
import org.spout.vanilla.protocol.codec.window.WindowCreativeActionCodec;
import org.spout.vanilla.protocol.codec.window.WindowEnchantItemCodec;
import org.spout.vanilla.protocol.codec.window.WindowItemsCodec;
import org.spout.vanilla.protocol.codec.window.WindowOpenCodec;
import org.spout.vanilla.protocol.codec.window.WindowPropertyCodec;
import org.spout.vanilla.protocol.codec.window.WindowSlotCodec;
import org.spout.vanilla.protocol.codec.window.WindowTransactionCodec;
import org.spout.vanilla.protocol.codec.world.EffectCodec;
import org.spout.vanilla.protocol.codec.world.ExplosionCodec;
import org.spout.vanilla.protocol.codec.world.block.BlockActionCodec;
import org.spout.vanilla.protocol.codec.world.block.BlockBreakAnimationCodec;
import org.spout.vanilla.protocol.codec.world.block.BlockBulkCodec;
import org.spout.vanilla.protocol.codec.world.block.BlockChangeCodec;
import org.spout.vanilla.protocol.codec.world.block.SignCodec;
import org.spout.vanilla.protocol.codec.world.chunk.ChunkBulkCodec;
import org.spout.vanilla.protocol.codec.world.chunk.ChunkDataCodec;

public class VanillaCodecLookupService extends CodecLookupService {
	public VanillaCodecLookupService() {
		try {
			/* 0x00 */
			bind(PlayerPingCodec.class);
			/* 0x01 */
			bind(PlayerLoginRequestCodec.class);
			/* 0x02 */
			bind(PlayerHandshakeCodec.class);
			/* 0x03 */
			bind(PlayerChatCodec.class);
			/* 0x04 */
			bind(PlayerTimeCodec.class);
			/* 0x05 */
			bind(EntityEquipmentCodec.class);
			/* 0x06 */
			bind(PlayerSpawnPositionCodec.class);
			/* 0x07 */
			bind(EntityInteractCodec.class); //TODO rename Use Entity on the minecraft protocol page
			/* 0x08 */
			bind(PlayerHealthCodec.class);
			/* 0x09 */
			bind(PlayerRespawnCodec.class);
			/* 0x0A */
			bind(PlayerGroundCodec.class);
			/* 0x0B */
			bind(PlayerPositionCodec.class);
			/* 0x0C */
			bind(PlayerYawCodec.class);
			/* 0x0D */
			bind(PlayerPositionYawCodec.class);
			/* 0x0E */
			bind(PlayerDiggingCodec.class);
			/* 0x0F */
			bind(PlayerBlockPlacementCodec.class);
			/* 0x10 */
			bind(PlayerHeldItemChangeCodec.class);
			/* 0x11 */
			bind(PlayerBedCodec.class);
			/* 0x12 */
			bind(EntityAnimationCodec.class);
			/* 0x13 */
			bind(EntityActionCodec.class);
			/* 0x14 */
			bind(PlayerSpawnCodec.class);
			/* 0x15 */
			bind(EntityItemCodec.class);
			/* 0x16 */
			bind(PlayerCollectItemCodec.class);
			/* 0x17 */
			bind(EntitySpawnObjectCodec.class);
			/* 0x18 */
			bind(EntityMobCodec.class);
			/* 0x19 */
			bind(EntityPaintingCodec.class);
			/* 0x1A */
			bind(EntityExperienceOrbCodec.class);
			/* 0x1C */
			bind(EntityVelocityCodec.class);
			/* 0x1D */
			bind(EntityDestroyCodec.class);
			/* 0x1E */
			bind(EntityInitializeCodec.class); //TODO the meaning of this packet is basically that the entity did not move/look since the last such packet. We need to implement this!
			/* 0x1F */
			bind(EntityRelativePositionCodec.class);
			/* 0x20 */
			bind(EntityYawCodec.class); //TODO rename Entity Look on the minecraft protocol page
			/* 0x21 */
			bind(EntityRelativePositionYawCodec.class);  //TODO same as above
			/* 0x22 */
			bind(EntityTeleportCodec.class);
			/* 0x23 */
			bind(EntityHeadYawCodec.class); //TODO same as above
			/* 0x26 */
			bind(EntityStatusCodec.class);
			/* 0x27 */
			bind(EntityAttachEntityCodec.class);
			/* 0x28 */
			bind(EntityMetadataCodec.class);
			/* 0x29 */
			bind(EntityEffectCodec.class);
			/* 0x2A */
			bind(EntityRemoveEffectCodec.class);
			/* 0x2B */
			bind(PlayerExperienceCodec.class);
			/* 0x33 */
			bind(ChunkDataCodec.class); //TODO rename on the minecraft protocol page
			/* 0x34 */
			bind(BlockBulkCodec.class);
			/* 0x35 */
			bind(BlockChangeCodec.class);
			/* 0x36 */
			bind(BlockActionCodec.class);
			/* 0x37 */
			bind(BlockBreakAnimationCodec.class);
			/* 0x38 */
			bind(ChunkBulkCodec.class);
			/* 0x3C */
			bind(ExplosionCodec.class);
			/* 0x3D */
			bind(EffectCodec.class);
			/* 0x3E */
			bind(PlayerSoundEffectCodec.class);
			/* 0x46 */
			bind(PlayerGameStateCodec.class);
			/* 0x47 */
			bind(EntityThunderboltCodec.class); //Minecraft protocol page -> Thunderbolt :/
			/* 0x64 */
			bind(WindowOpenCodec.class);
			/* 0x65 */
			bind(WindowCloseCodec.class);
			/* 0x66 */
			bind(WindowClickCodec.class);
			/* 0x67 */
			bind(WindowSlotCodec.class);
			/* 0x68 */
			bind(WindowItemsCodec.class);
			/* 0x69 */
			bind(WindowPropertyCodec.class); //Update Window Property on the protocol page!
			/* 0x6A */
			bind(WindowTransactionCodec.class);
			/* 0x6B */
			bind(WindowCreativeActionCodec.class);
			/* 0x6C */
			bind(WindowEnchantItemCodec.class);
			/* 0x82 */
			bind(SignCodec.class);
			/* 0x83 */
			bind(EntityItemDataCodec.class);
			/* 0x84 */
			bind(EntityTileDataCodec.class); //Update Tile Entity...
			/* 0xC8 */
			bind(PlayerStatisticCodec.class);
			/* 0xC9 */
			bind(PlayerListCodec.class);
			/* 0xCA */
			bind(PlayerAbilityCodec.class);
			/* 0xCB */
			bind(PlayerTabCompleteCodec.class);
			/* 0xCC */
			bind(PlayerLocaleViewDistanceCodec.class);
			/* 0xCD */
			bind(PlayerStatusCodec.class);
			/* 0xFA */
			bind(ServerPluginCodec.class);
			/* 0xFC */
			bind(EncryptionKeyResponseCodec.class);
			/* 0xFD */
			bind(EncryptionKeyRequestCodec.class);
			/* 0xFE */
			bind(ServerListPingCodec.class);
			/* 0xFF */
			bind(PlayerKickCodec.class);
		} catch (Exception ex) {
			throw new ExceptionInInitializerError(ex);
		}
	}
}
