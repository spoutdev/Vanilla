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
package org.spout.vanilla.protocol;

import org.spout.api.protocol.CodecLookupService;

import org.spout.vanilla.protocol.codec.AttachEntityCodec;
import org.spout.vanilla.protocol.codec.BlockActionCodec;
import org.spout.vanilla.protocol.codec.BlockChangeCodec;
import org.spout.vanilla.protocol.codec.BlockPlacementCodec;
import org.spout.vanilla.protocol.codec.ChangeItemCodec;
import org.spout.vanilla.protocol.codec.ChatCodec;
import org.spout.vanilla.protocol.codec.CloseWindowCodec;
import org.spout.vanilla.protocol.codec.CollectItemCodec;
import org.spout.vanilla.protocol.codec.CompressedChunkCodec;
import org.spout.vanilla.protocol.codec.CreateEntityCodec;
import org.spout.vanilla.protocol.codec.CustomDataCodec;
import org.spout.vanilla.protocol.codec.DestroyEntityCodec;
import org.spout.vanilla.protocol.codec.DiggingCodec;
import org.spout.vanilla.protocol.codec.EnchantItemCodec;
import org.spout.vanilla.protocol.codec.EntityActionCodec;
import org.spout.vanilla.protocol.codec.EntityAnimationCodec;
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
import org.spout.vanilla.protocol.codec.ExperienceCodec;
import org.spout.vanilla.protocol.codec.ExperienceOrbCodec;
import org.spout.vanilla.protocol.codec.ExplosionCodec;
import org.spout.vanilla.protocol.codec.GroundCodec;
import org.spout.vanilla.protocol.codec.HandshakeCodec;
import org.spout.vanilla.protocol.codec.HealthCodec;
import org.spout.vanilla.protocol.codec.IdentificationCodec;
import org.spout.vanilla.protocol.codec.KickCodec;
import org.spout.vanilla.protocol.codec.LoadChunkCodec;
import org.spout.vanilla.protocol.codec.MapDataCodec;
import org.spout.vanilla.protocol.codec.MultiBlockChangeCodec;
import org.spout.vanilla.protocol.codec.OpenWindowCodec;
import org.spout.vanilla.protocol.codec.PingCodec;
import org.spout.vanilla.protocol.codec.PlayEffectCodec;
import org.spout.vanilla.protocol.codec.PlayerAbilityCodec;
import org.spout.vanilla.protocol.codec.PositionCodec;
import org.spout.vanilla.protocol.codec.PositionRotationCodec;
import org.spout.vanilla.protocol.codec.ProgressBarCodec;
import org.spout.vanilla.protocol.codec.QuickBarCodec;
import org.spout.vanilla.protocol.codec.RelativeEntityPositionCodec;
import org.spout.vanilla.protocol.codec.RelativeEntityPositionRotationCodec;
import org.spout.vanilla.protocol.codec.RespawnCodec;
import org.spout.vanilla.protocol.codec.RotationCodec;
import org.spout.vanilla.protocol.codec.ServerListPingCodec;
import org.spout.vanilla.protocol.codec.SetWindowSlotCodec;
import org.spout.vanilla.protocol.codec.SetWindowSlotsCodec;
import org.spout.vanilla.protocol.codec.SpawnItemCodec;
import org.spout.vanilla.protocol.codec.SpawnLightningStrikeCodec;
import org.spout.vanilla.protocol.codec.SpawnMobCodec;
import org.spout.vanilla.protocol.codec.SpawnPaintingCodec;
import org.spout.vanilla.protocol.codec.SpawnPlayerCodec;
import org.spout.vanilla.protocol.codec.SpawnPositionCodec;
import org.spout.vanilla.protocol.codec.SpawnVehicleCodec;
import org.spout.vanilla.protocol.codec.StateChangeCodec;
import org.spout.vanilla.protocol.codec.StatisticCodec;
import org.spout.vanilla.protocol.codec.TileEntityDataCodec;
import org.spout.vanilla.protocol.codec.TimeCodec;
import org.spout.vanilla.protocol.codec.TransactionCodec;
import org.spout.vanilla.protocol.codec.UpdateSignCodec;
import org.spout.vanilla.protocol.codec.UseBedCodec;
import org.spout.vanilla.protocol.codec.UserListItemCodec;
import org.spout.vanilla.protocol.codec.WindowClickCodec;

public class VanillaCodecLookupService extends CodecLookupService {
	public VanillaCodecLookupService() {
		try {
			/* 0x00 */
			bind(PingCodec.class);
			/* 0x01 */
			bind(IdentificationCodec.class);
			/* 0x02 */
			bind(HandshakeCodec.class);
			/* 0x03 */
			bind(ChatCodec.class);
			/* 0x04 */
			bind(TimeCodec.class);
			/* 0x05 */
			bind(EntityEquipmentCodec.class);
			/* 0x06 */
			bind(SpawnPositionCodec.class);
			/* 0x07 */
			bind(EntityInteractionCodec.class);
			/* 0x08 */
			bind(HealthCodec.class);
			/* 0x09 */
			bind(RespawnCodec.class);
			/* 0x0A */
			bind(GroundCodec.class);
			/* 0x0B */
			bind(PositionCodec.class);
			/* 0x0C */
			bind(RotationCodec.class);
			/* 0x0D */
			bind(PositionRotationCodec.class);
			/* 0x0E */
			bind(DiggingCodec.class);
			/* 0x0F */
			bind(BlockPlacementCodec.class);
			/* 0x10 */
			bind(ChangeItemCodec.class);
			/* 0x11 */
			bind(UseBedCodec.class);
			/* 0x12 */
			bind(EntityAnimationCodec.class);
			/* 0x13 */
			bind(EntityActionCodec.class);
			/* 0x14 */
			bind(SpawnPlayerCodec.class);
			/* 0x15 */
			bind(SpawnItemCodec.class);
			/* 0x16 */
			bind(CollectItemCodec.class);
			/* 0x17 */
			bind(SpawnVehicleCodec.class);
			/* 0x18 */
			bind(SpawnMobCodec.class);
			/* 0x19 */
			bind(SpawnPaintingCodec.class);
			/* 0x1A */
			bind(ExperienceOrbCodec.class);
			/* 0x1C */
			bind(EntityVelocityCodec.class);
			/* 0x1D */
			bind(DestroyEntityCodec.class);
			/* 0x1E */
			bind(CreateEntityCodec.class);
			/* 0x1F */
			bind(RelativeEntityPositionCodec.class);
			/* 0x20 */
			bind(EntityRotationCodec.class);
			/* 0x21 */
			bind(RelativeEntityPositionRotationCodec.class);
			/* 0x22 */
			bind(EntityTeleportCodec.class);
			/* 0x23 */
			bind(EntityHeadYawCodec.class);
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
			bind(ExperienceCodec.class);
			/* 0x32 */
			bind(LoadChunkCodec.class);
			/* 0x33 */
			bind(CompressedChunkCodec.class);
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
			bind(StateChangeCodec.class);
			/* 0x47 */
			bind(SpawnLightningStrikeCodec.class);
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
			bind(ProgressBarCodec.class);
			/* 0x6A */
			bind(TransactionCodec.class);
			/* 0x6B */
			bind(QuickBarCodec.class);
			/* 0x6C */
			bind(EnchantItemCodec.class);
			/* 0x82 */
			bind(UpdateSignCodec.class);
			/* 0x83 */
			bind(MapDataCodec.class);
			/* 0x84 */
			bind(TileEntityDataCodec.class);
			/* 0xC8 */
			bind(StatisticCodec.class);
			/* 0xC9 */
			bind(UserListItemCodec.class);
			/* 0xCA */
			bind(PlayerAbilityCodec.class);
			/* 0xFA */
			bind(CustomDataCodec.class);
			/* 0xFE */
			bind(ServerListPingCodec.class);
			/* 0xFF */
			bind(KickCodec.class);
		} catch (Exception ex) {
			throw new ExceptionInInitializerError(ex);
		}
	}
}
