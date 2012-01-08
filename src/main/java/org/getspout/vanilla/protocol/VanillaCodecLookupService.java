/*
 * This file is part of Vanilla (http://www.spout.org/).
 *
 * Vanilla is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Vanilla is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.getspout.vanilla.protocol;

import org.getspout.api.protocol.CodecLookupService;
import org.getspout.vanilla.protocol.codec.ActivateItemCodec;
import org.getspout.vanilla.protocol.codec.AnimateEntityCodec;
import org.getspout.vanilla.protocol.codec.AttachEntityCodec;
import org.getspout.vanilla.protocol.codec.BlockChangeCodec;
import org.getspout.vanilla.protocol.codec.BlockPlacementCodec;
import org.getspout.vanilla.protocol.codec.ChatCodec;
import org.getspout.vanilla.protocol.codec.CloseWindowCodec;
import org.getspout.vanilla.protocol.codec.CollectItemCodec;
import org.getspout.vanilla.protocol.codec.CompressedChunkCodec;
import org.getspout.vanilla.protocol.codec.CreateEntityCodec;
import org.getspout.vanilla.protocol.codec.DestroyEntityCodec;
import org.getspout.vanilla.protocol.codec.DiggingCodec;
import org.getspout.vanilla.protocol.codec.EnchantItemCodec;
import org.getspout.vanilla.protocol.codec.EntityActionCodec;
import org.getspout.vanilla.protocol.codec.EntityEffectCodec;
import org.getspout.vanilla.protocol.codec.EntityEquipmentCodec;
import org.getspout.vanilla.protocol.codec.EntityInteractionCodec;
import org.getspout.vanilla.protocol.codec.EntityMetadataCodec;
import org.getspout.vanilla.protocol.codec.EntityRemoveEffectCodec;
import org.getspout.vanilla.protocol.codec.EntityRotationCodec;
import org.getspout.vanilla.protocol.codec.EntityStatusCodec;
import org.getspout.vanilla.protocol.codec.EntityTeleportCodec;
import org.getspout.vanilla.protocol.codec.EntityVelocityCodec;
import org.getspout.vanilla.protocol.codec.ExperienceCodec;
import org.getspout.vanilla.protocol.codec.ExperienceOrbCodec;
import org.getspout.vanilla.protocol.codec.ExplosionCodec;
import org.getspout.vanilla.protocol.codec.GroundCodec;
import org.getspout.vanilla.protocol.codec.HandshakeCodec;
import org.getspout.vanilla.protocol.codec.HealthCodec;
import org.getspout.vanilla.protocol.codec.IdentificationCodec;
import org.getspout.vanilla.protocol.codec.KickCodec;
import org.getspout.vanilla.protocol.codec.LoadChunkCodec;
import org.getspout.vanilla.protocol.codec.MapDataCodec;
import org.getspout.vanilla.protocol.codec.MultiBlockChangeCodec;
import org.getspout.vanilla.protocol.codec.OpenWindowCodec;
import org.getspout.vanilla.protocol.codec.PingCodec;
import org.getspout.vanilla.protocol.codec.PlayEffectCodec;
import org.getspout.vanilla.protocol.codec.PlayNoteCodec;
import org.getspout.vanilla.protocol.codec.PositionCodec;
import org.getspout.vanilla.protocol.codec.PositionRotationCodec;
import org.getspout.vanilla.protocol.codec.ProgressBarCodec;
import org.getspout.vanilla.protocol.codec.QuickBarCodec;
import org.getspout.vanilla.protocol.codec.RelativeEntityPositionCodec;
import org.getspout.vanilla.protocol.codec.RelativeEntityPositionRotationCodec;
import org.getspout.vanilla.protocol.codec.RespawnCodec;
import org.getspout.vanilla.protocol.codec.RotationCodec;
import org.getspout.vanilla.protocol.codec.ServerListPingCodec;
import org.getspout.vanilla.protocol.codec.SetWindowSlotCodec;
import org.getspout.vanilla.protocol.codec.SetWindowSlotsCodec;
import org.getspout.vanilla.protocol.codec.SpawnItemCodec;
import org.getspout.vanilla.protocol.codec.SpawnLightningStrikeCodec;
import org.getspout.vanilla.protocol.codec.SpawnMobCodec;
import org.getspout.vanilla.protocol.codec.SpawnPaintingCodec;
import org.getspout.vanilla.protocol.codec.SpawnPlayerCodec;
import org.getspout.vanilla.protocol.codec.SpawnPositionCodec;
import org.getspout.vanilla.protocol.codec.SpawnVehicleCodec;
import org.getspout.vanilla.protocol.codec.StateChangeCodec;
import org.getspout.vanilla.protocol.codec.StatisticCodec;
import org.getspout.vanilla.protocol.codec.TimeCodec;
import org.getspout.vanilla.protocol.codec.TransactionCodec;
import org.getspout.vanilla.protocol.codec.UpdateSignCodec;
import org.getspout.vanilla.protocol.codec.UserListItemCodec;
import org.getspout.vanilla.protocol.codec.WindowClickCodec;

public class VanillaCodecLookupService extends CodecLookupService {
	public VanillaCodecLookupService() {
		try {
            /* 0x00 */ bind(PingCodec.class);
            /* 0x01 */ bind(IdentificationCodec.class);
            /* 0x02 */ bind(HandshakeCodec.class);
            /* 0x03 */ bind(ChatCodec.class);
            /* 0x04 */ bind(TimeCodec.class);
            /* 0x05 */ bind(EntityEquipmentCodec.class);
            /* 0x06 */ bind(SpawnPositionCodec.class);
            /* 0x07 */ bind(EntityInteractionCodec.class);
            /* 0x08 */ bind(HealthCodec.class);
            /* 0x09 */ bind(RespawnCodec.class);
            /* 0x0A */ bind(GroundCodec.class);
            /* 0x0B */ bind(PositionCodec.class);
            /* 0x0C */ bind(RotationCodec.class);
            /* 0x0D */ bind(PositionRotationCodec.class);
            /* 0x0E */ bind(DiggingCodec.class);
            /* 0x0F */ bind(BlockPlacementCodec.class);
            /* 0x10 */ bind(ActivateItemCodec.class);
            /* 0x12 */ bind(AnimateEntityCodec.class);
            /* 0x13 */ bind(EntityActionCodec.class);
            /* 0x14 */ bind(SpawnPlayerCodec.class);
            /* 0x15 */ bind(SpawnItemCodec.class);
            /* 0x16 */ bind(CollectItemCodec.class);
            /* 0x17 */ bind(SpawnVehicleCodec.class);
            /* 0x18 */ bind(SpawnMobCodec.class);
            /* 0x19 */ bind(SpawnPaintingCodec.class);
            /* 0x1A */ bind(ExperienceOrbCodec.class);
            /* 0x1C */ bind(EntityVelocityCodec.class);
            /* 0x1D */ bind(DestroyEntityCodec.class);
            /* 0x1E */ bind(CreateEntityCodec.class);
            /* 0x1F */ bind(RelativeEntityPositionCodec.class);
            /* 0x20 */ bind(EntityRotationCodec.class);
            /* 0x21 */ bind(RelativeEntityPositionRotationCodec.class);
            /* 0x22 */ bind(EntityTeleportCodec.class);
            /* 0x26 */ bind(EntityStatusCodec.class);
            /* 0x27 */ bind(AttachEntityCodec.class);
            /* 0x28 */ bind(EntityMetadataCodec.class);
            /* 0x29 */ bind(EntityEffectCodec.class);
            /* 0x2A */ bind(EntityRemoveEffectCodec.class);
            /* 0x2B */ bind(ExperienceCodec.class);
            /* 0x32 */ bind(LoadChunkCodec.class);
            /* 0x33 */ bind(CompressedChunkCodec.class);
            /* 0x34 */ bind(MultiBlockChangeCodec.class);
            /* 0x35 */ bind(BlockChangeCodec.class);
            /* 0x36 */ bind(PlayNoteCodec.class);
            /* 0x3C */ bind(ExplosionCodec.class);
            /* 0x3D */ bind(PlayEffectCodec.class);
            /* 0x46 */ bind(StateChangeCodec.class);
            /* 0x47 */ bind(SpawnLightningStrikeCodec.class);
            /* 0x64 */ bind(OpenWindowCodec.class);
            /* 0x65 */ bind(CloseWindowCodec.class);
            /* 0x66 */ bind(WindowClickCodec.class);
            /* 0x67 */ bind(SetWindowSlotCodec.class);
            /* 0x68 */ bind(SetWindowSlotsCodec.class);
            /* 0x69 */ bind(ProgressBarCodec.class);
            /* 0x6A */ bind(TransactionCodec.class);
            /* 0x6B */ bind(QuickBarCodec.class);
            /* 0x6C */ bind(EnchantItemCodec.class);
            /* 0x82 */ bind(UpdateSignCodec.class);
            /* 0x83 */ bind(MapDataCodec.class);
            /* 0xC8 */ bind(StatisticCodec.class);
            /* 0xC9 */ bind(UserListItemCodec.class);
            /* 0xFE */ bind(ServerListPingCodec.class);
            /* 0xFF */ bind(KickCodec.class);
		} catch (Exception ex) {
			throw new ExceptionInInitializerError(ex);
		}
	}
}