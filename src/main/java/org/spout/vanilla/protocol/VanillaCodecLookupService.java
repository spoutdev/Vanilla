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
import org.spout.api.protocol.common.codec.CustomDataCodec;

import org.spout.vanilla.protocol.codec.AnimationCodec;
import org.spout.vanilla.protocol.codec.AttachEntityCodec;
import org.spout.vanilla.protocol.codec.BlockActionCodec;
import org.spout.vanilla.protocol.codec.BlockChangeCodec;
import org.spout.vanilla.protocol.codec.ChangeGameStateCodec;
import org.spout.vanilla.protocol.codec.ChatCodec;
import org.spout.vanilla.protocol.codec.CloseWindowCodec;
import org.spout.vanilla.protocol.codec.CollectItemCodec;
import org.spout.vanilla.protocol.codec.CompressedChunkCodec;
import org.spout.vanilla.protocol.codec.CreateEntityCodec;
import org.spout.vanilla.protocol.codec.CreativeCodec;
import org.spout.vanilla.protocol.codec.DestroyEntityCodec;
import org.spout.vanilla.protocol.codec.EnchantItemCodec;
import org.spout.vanilla.protocol.codec.EncryptionKeyRequestCodec;
import org.spout.vanilla.protocol.codec.EncryptionKeyResponseCodec;
import org.spout.vanilla.protocol.codec.EntityActionCodec;
import org.spout.vanilla.protocol.codec.EntityEffectCodec;
import org.spout.vanilla.protocol.codec.EntityEquipmentCodec;
import org.spout.vanilla.protocol.codec.EntityHeadYawCodec;
import org.spout.vanilla.protocol.codec.EntityInteractionCodec;
import org.spout.vanilla.protocol.codec.EntityMetadataCodec;
import org.spout.vanilla.protocol.codec.EntityRemoveEffectCodec;
import org.spout.vanilla.protocol.codec.EntityRotationCodec;
import org.spout.vanilla.protocol.codec.EntityStatusCodec;
import org.spout.vanilla.protocol.codec.EntityTeleportCodec;
import org.spout.vanilla.protocol.codec.EntityVelocityCodec;
import org.spout.vanilla.protocol.codec.ExplosionCodec;
import org.spout.vanilla.protocol.codec.GroundCodec;
import org.spout.vanilla.protocol.codec.HandshakeCodec;
import org.spout.vanilla.protocol.codec.HeldItemChangeCodec;
import org.spout.vanilla.protocol.codec.ItemDataCodec;
import org.spout.vanilla.protocol.codec.KeepAliveCodec;
import org.spout.vanilla.protocol.codec.KickCodec;
import org.spout.vanilla.protocol.codec.LoadChunkCodec;
import org.spout.vanilla.protocol.codec.LoginRequestCodec;
import org.spout.vanilla.protocol.codec.MultiBlockChangeCodec;
import org.spout.vanilla.protocol.codec.OpenWindowCodec;
import org.spout.vanilla.protocol.codec.PlayEffectCodec;
import org.spout.vanilla.protocol.codec.PlayerAbilityCodec;
import org.spout.vanilla.protocol.codec.PlayerBlockPlacementCodec;
import org.spout.vanilla.protocol.codec.PlayerDiggingCodec;
import org.spout.vanilla.protocol.codec.PlayerListCodec;
import org.spout.vanilla.protocol.codec.PlayerLookCodec;
import org.spout.vanilla.protocol.codec.PlayerPositionCodec;
import org.spout.vanilla.protocol.codec.PlayerPositionLookCodec;
import org.spout.vanilla.protocol.codec.ProgressBarCodec;
import org.spout.vanilla.protocol.codec.RelativeEntityPositionCodec;
import org.spout.vanilla.protocol.codec.RelativeEntityPositionRotationCodec;
import org.spout.vanilla.protocol.codec.RespawnCodec;
import org.spout.vanilla.protocol.codec.ServerListPingCodec;
import org.spout.vanilla.protocol.codec.SetExperienceCodec;
import org.spout.vanilla.protocol.codec.SetWindowSlotCodec;
import org.spout.vanilla.protocol.codec.SetWindowSlotsCodec;
import org.spout.vanilla.protocol.codec.SpawnDroppedItemCodec;
import org.spout.vanilla.protocol.codec.SpawnExperienceOrbCodec;
import org.spout.vanilla.protocol.codec.SpawnLightningStrikeCodec;
import org.spout.vanilla.protocol.codec.SpawnMobCodec;
import org.spout.vanilla.protocol.codec.SpawnPaintingCodec;
import org.spout.vanilla.protocol.codec.SpawnPlayerCodec;
import org.spout.vanilla.protocol.codec.SpawnPositionCodec;
import org.spout.vanilla.protocol.codec.SpawnVehicleCodec;
import org.spout.vanilla.protocol.codec.StatisticCodec;
import org.spout.vanilla.protocol.codec.TileEntityDataCodec;
import org.spout.vanilla.protocol.codec.TimeUpdateCodec;
import org.spout.vanilla.protocol.codec.TransactionCodec;
import org.spout.vanilla.protocol.codec.UpdateHealthCodec;
import org.spout.vanilla.protocol.codec.UpdateSignCodec;
import org.spout.vanilla.protocol.codec.UseBedCodec;
import org.spout.vanilla.protocol.codec.WindowClickCodec;

public class VanillaCodecLookupService extends CodecLookupService {
	public VanillaCodecLookupService() {
		try {
			/* 0x00 */
			bind(KeepAliveCodec.class);
			/* 0x01 */
			bind(LoginRequestCodec.class);
			/* 0x02 */
			bind(HandshakeCodec.class);
			/* 0x03 */
			bind(ChatCodec.class);
			/* 0x04 */
			bind(TimeUpdateCodec.class);
			/* 0x05 */
			bind(EntityEquipmentCodec.class);
			/* 0x06 */
			bind(SpawnPositionCodec.class);
			/* 0x07 */
			bind(EntityInteractionCodec.class); //TODO rename Use Entity on the minecraft protocol page
			/* 0x08 */
			bind(UpdateHealthCodec.class);
			/* 0x09 */
			bind(RespawnCodec.class);
			/* 0x0A */
			bind(GroundCodec.class);
			/* 0x0B */
			bind(PlayerPositionCodec.class);
			/* 0x0C */
			bind(PlayerLookCodec.class);
			/* 0x0D */
			bind(PlayerPositionLookCodec.class);
			/* 0x0E */
			bind(PlayerDiggingCodec.class);
			/* 0x0F */
			bind(PlayerBlockPlacementCodec.class);
			/* 0x10 */
			bind(HeldItemChangeCodec.class);
			/* 0x11 */
			bind(UseBedCodec.class);
			/* 0x12 */
			bind(AnimationCodec.class);
			/* 0x13 */
			bind(EntityActionCodec.class);
			/* 0x14 */
			bind(SpawnPlayerCodec.class);
			/* 0x15 */
			bind(SpawnDroppedItemCodec.class);
			/* 0x16 */
			bind(CollectItemCodec.class);
			/* 0x17 */
			bind(SpawnVehicleCodec.class);
			/* 0x18 */
			bind(SpawnMobCodec.class);
			/* 0x19 */
			bind(SpawnPaintingCodec.class);
			/* 0x1A */
			bind(SpawnExperienceOrbCodec.class);
			/* 0x1C */
			bind(EntityVelocityCodec.class);
			/* 0x1D */
			bind(DestroyEntityCodec.class);
			/* 0x1E */
			bind(CreateEntityCodec.class); //TODO the meaning of this packet is basically that the entity did not move/look since the last such packet. We need to implement this!
			/* 0x1F */
			bind(RelativeEntityPositionCodec.class);
			/* 0x20 */
			bind(EntityRotationCodec.class); //TODO rename Entity Look on the minecraft protocol page
			/* 0x21 */
			bind(RelativeEntityPositionRotationCodec.class);  //TODO same as above
			/* 0x22 */
			bind(EntityTeleportCodec.class);
			/* 0x23 */
			bind(EntityHeadYawCodec.class); //TODO same as above
			/* 0x26 */
			bind(EntityStatusCodec.class);
			/* 0x27 */
			bind(AttachEntityCodec.class);
			/* 0x28 */
			bind(EntityMetadataCodec.class);
			/* 0x29 */
			bind(EntityEffectCodec.class);
			/* 0x2A */
			bind(EntityRemoveEffectCodec.class);
			/* 0x2B */
			bind(SetExperienceCodec.class);
			/* 0x32 */
			bind(LoadChunkCodec.class); //TODO not sure about this one, Map Column Allocation on the protocol page o.O
			/* 0x33 */
			bind(CompressedChunkCodec.class); //TODO rename on the minecraft protocol page
			/* 0x34 */
			bind(MultiBlockChangeCodec.class);
			/* 0x35 */
			bind(BlockChangeCodec.class);
			/* 0x36 */
			bind(BlockActionCodec.class);
			/* 0x3C */
			bind(ExplosionCodec.class);
			/* 0x3D */
			bind(PlayEffectCodec.class);
			/* 0x46 */
			bind(ChangeGameStateCodec.class);
			/* 0x47 */
			bind(SpawnLightningStrikeCodec.class); //Minecraft protocol page -> Thunderbolt :/
			/* 0x64 */
			bind(OpenWindowCodec.class);
			/* 0x65 */
			bind(CloseWindowCodec.class);
			/* 0x66 */
			bind(WindowClickCodec.class);
			/* 0x67 */
			bind(SetWindowSlotCodec.class);
			/* 0x68 */
			bind(SetWindowSlotsCodec.class);
			/* 0x69 */
			bind(ProgressBarCodec.class); //Update Window Property on the protocol page!
			/* 0x6A */
			bind(TransactionCodec.class);
			/* 0x6B */
			bind(CreativeCodec.class);
			/* 0x6C */
			bind(EnchantItemCodec.class);
			/* 0x82 */
			bind(UpdateSignCodec.class);
			/* 0x83 */
			bind(ItemDataCodec.class);
			/* 0x84 */
			bind(TileEntityDataCodec.class); //Update Tile Entity...
			/* 0xC8 */
			bind(StatisticCodec.class);
			/* 0xC9 */
			bind(PlayerListCodec.class);
			/* 0xCA */
			bind(PlayerAbilityCodec.class);
			/* 0xFA */
			bind(CustomDataCodec.class);
			/* 0xFC */
			bind(EncryptionKeyResponseCodec.class);
			/* 0xFD */
			bind(EncryptionKeyRequestCodec.class);
			/* 0xFE */
			bind(ServerListPingCodec.class);
			/* 0xFF */
			bind(KickCodec.class);
		} catch (Exception ex) {
			throw new ExceptionInInitializerError(ex);
		}
	}
}
