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

import java.util.Arrays;

import org.spout.api.geo.cuboid.Chunk;
import org.spout.api.inventory.ItemStack;
import org.spout.api.material.block.BlockFace;
import org.spout.api.protocol.Message;
import org.spout.api.protocol.common.message.CustomDataMessage;

import org.spout.vanilla.data.GameMode;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.protocol.msg.BlockActionMessage;
import org.spout.vanilla.protocol.msg.BlockChangeMessage;
import org.spout.vanilla.protocol.msg.ChangeGameStateMessage;
import org.spout.vanilla.protocol.msg.ChatMessage;
import org.spout.vanilla.protocol.msg.CompressedChunkMessage;
import org.spout.vanilla.protocol.msg.CreativeMessage;
import org.spout.vanilla.protocol.msg.EnchantItemMessage;
import org.spout.vanilla.protocol.msg.EncryptionKeyRequestMessage;
import org.spout.vanilla.protocol.msg.EncryptionKeyResponseMessage;
import org.spout.vanilla.protocol.msg.ExplosionMessage;
import org.spout.vanilla.protocol.msg.GroundMessage;
import org.spout.vanilla.protocol.msg.HandshakeMessage;
import org.spout.vanilla.protocol.msg.HeldItemChangeMessage;
import org.spout.vanilla.protocol.msg.ItemDataMessage;
import org.spout.vanilla.protocol.msg.KeepAliveMessage;
import org.spout.vanilla.protocol.msg.KickMessage;
import org.spout.vanilla.protocol.msg.LoadChunkMessage;
import org.spout.vanilla.protocol.msg.LoginRequestMessage;
import org.spout.vanilla.protocol.msg.MultiBlockChangeMessage;
import org.spout.vanilla.protocol.msg.PlayEffectMessage;
import org.spout.vanilla.protocol.msg.PlayerAbilityMessage;
import org.spout.vanilla.protocol.msg.PlayerBlockPlacementMessage;
import org.spout.vanilla.protocol.msg.PlayerDiggingMessage;
import org.spout.vanilla.protocol.msg.PlayerListMessage;
import org.spout.vanilla.protocol.msg.PlayerLookMessage;
import org.spout.vanilla.protocol.msg.PlayerPositionLookMessage;
import org.spout.vanilla.protocol.msg.PlayerPositionMessage;
import org.spout.vanilla.protocol.msg.RespawnMessage;
import org.spout.vanilla.protocol.msg.ServerListPingMessage;
import org.spout.vanilla.protocol.msg.SetExperienceMessage;
import org.spout.vanilla.protocol.msg.SpawnPositionMessage;
import org.spout.vanilla.protocol.msg.StatisticMessage;
import org.spout.vanilla.protocol.msg.TileEntityDataMessage;
import org.spout.vanilla.protocol.msg.TimeUpdateMessage;
import org.spout.vanilla.protocol.msg.UpdateHealthMessage;
import org.spout.vanilla.protocol.msg.UpdateSignMessage;
import org.spout.vanilla.protocol.msg.entity.EntityActionMessage;
import org.spout.vanilla.protocol.msg.entity.EntityAnimationMessage;
import org.spout.vanilla.protocol.msg.entity.EntityAttachEntityMessage;
import org.spout.vanilla.protocol.msg.entity.EntityCollectItemMessage;
import org.spout.vanilla.protocol.msg.entity.EntityCreateMessage;
import org.spout.vanilla.protocol.msg.entity.EntityDestroyMessage;
import org.spout.vanilla.protocol.msg.entity.EntityEffectMessage;
import org.spout.vanilla.protocol.msg.entity.EntityEquipmentMessage;
import org.spout.vanilla.protocol.msg.entity.EntityHeadYawMessage;
import org.spout.vanilla.protocol.msg.entity.EntityInteractionMessage;
import org.spout.vanilla.protocol.msg.entity.EntityMetadataMessage;
import org.spout.vanilla.protocol.msg.entity.EntityRelativePositionMessage;
import org.spout.vanilla.protocol.msg.entity.EntityRelativePositionRotationMessage;
import org.spout.vanilla.protocol.msg.entity.EntitySpawnExperienceOrbMessage;
import org.spout.vanilla.protocol.msg.entity.EntitySpawnItemMessage;
import org.spout.vanilla.protocol.msg.entity.EntitySpawnLightningStrikeMessage;
import org.spout.vanilla.protocol.msg.entity.EntitySpawnPaintingMessage;
import org.spout.vanilla.protocol.msg.entity.EntityRemoveEffectMessage;
import org.spout.vanilla.protocol.msg.entity.EntityRotationMessage;
import org.spout.vanilla.protocol.msg.entity.EntitySpawnPlayerMessage;
import org.spout.vanilla.protocol.msg.entity.EntityStatusMessage;
import org.spout.vanilla.protocol.msg.entity.EntityTeleportMessage;
import org.spout.vanilla.protocol.msg.entity.EntityUseBedMessage;
import org.spout.vanilla.protocol.msg.entity.EntityVelocityMessage;
import org.spout.vanilla.protocol.msg.entity.EntitySpawnMobMessage;
import org.spout.vanilla.protocol.msg.entity.EntitySpawnVehicleMessage;
import org.spout.vanilla.protocol.msg.window.WindowClickMessage;
import org.spout.vanilla.protocol.msg.window.WindowCloseMessage;
import org.spout.vanilla.protocol.msg.window.WindowOpenMessage;
import org.spout.vanilla.protocol.msg.window.WindowPropertyMessage;
import org.spout.vanilla.protocol.msg.window.WindowSetSlotMessage;
import org.spout.vanilla.protocol.msg.window.WindowSetSlotsMessage;
import org.spout.vanilla.protocol.msg.window.WindowTransactionMessage;
import org.spout.vanilla.window.WindowType;

import static org.spout.vanilla.protocol.ChannelBufferUtilsTest.TEST_PARAMS;

public class VanillaProtocolTest extends BaseProtocolTest {
	private static final VanillaCodecLookupService CODEC_LOOKUP = new VanillaCodecLookupService();
	private static final Message[] TEST_MESSAGES = new Message[]{
			new KeepAliveMessage(42),
			new LoginRequestMessage(0, "Tester", 0, -1, 0, 256, 20, "MAGICAL"),
			new HandshakeMessage("Player"),
			new ChatMessage("<Spouty> This is a thing called a chat message"),
			new TimeUpdateMessage(666L),
			new EntityEquipmentMessage(234, 3, 2, 3),
			new SpawnPositionMessage(42, 42, 42),
			new EntityInteractionMessage(1123, 4455, true),
			new UpdateHealthMessage((short) 1, (short) 2, 3.4F),
			new RespawnMessage(89, (byte) 0, (byte) 1, 128, "VERYFANCY"),
			new GroundMessage(true),
			new PlayerPositionMessage(128, 256, 512, 3.4D, true),
			new PlayerLookMessage(1F, 2F, false),
			new PlayerPositionLookMessage(1, 2, 5, 3.62, 4, 3, false),
			new PlayerDiggingMessage(PlayerDiggingMessage.STATE_START_DIGGING, 1, 2, 3, BlockFace.NORTH),
			new PlayerBlockPlacementMessage(1, 2, 3, BlockFace.NORTH, 89, 2, 7, null),
			new HeldItemChangeMessage(4),
			new EntityUseBedMessage(0, 3, 42, 42, 42),
			new EntityAnimationMessage(1234, EntityAnimationMessage.ANIMATION_HURT),
			new EntityActionMessage(5, 2),
			new EntitySpawnPlayerMessage(24, "risaccess1", 8000, 28, 900, 0, 0, 89),
			new EntitySpawnItemMessage(1234, 89, 3, (short) 4, 1, 2, 3, 34, 56, 55),
			new EntityCollectItemMessage(1234, 5678),
			new EntitySpawnVehicleMessage(1, 3, 3.0, 654.0, 1234.0, 77, 0.54, 0.23, 0.7),
			new EntitySpawnMobMessage(123, 255, 1, 2, 4, 34, 55, 33, TEST_PARAMS),
			new EntitySpawnPaintingMessage(4, "KEBAB", 2, 3, 4, 56),
			new EntitySpawnExperienceOrbMessage(34, 1, 2, 3, (short) 34),
			new EntityVelocityMessage(1, 2, 3, 4),
			new EntityDestroyMessage(2),
			new EntityCreateMessage(2),
			new EntityRelativePositionMessage(2, 1, 1, 1),
			new EntityRotationMessage(1234, 34, 5),
			new EntityRelativePositionRotationMessage(1, 2, 3, 4, 45, 54),
			new EntityTeleportMessage(1, 2, 3, 4, 5, 6),
			new EntityHeadYawMessage(45, 3),
			new EntityStatusMessage(1, (byte) 2),
			new EntityAttachEntityMessage(1, 2),
			new EntityMetadataMessage(1, TEST_PARAMS),
			new EntityEffectMessage(1, (byte) 1, (byte) 1, (short) 34),
			new EntityRemoveEffectMessage(1, (byte) 1),
			new SetExperienceMessage(1.2F, (short) 2, (short) 3),
			new LoadChunkMessage(0, -2, true),
			new CompressedChunkMessage(1, 2, true, new boolean[16], 1, new byte[][]{new byte[16 * 16 * 16 * 5 / 2], null, null, null, null, null, null, null, null, null, new byte[Chunk.BLOCKS.HALF_VOLUME * 5], null, null, null, null, null}, new byte[16 * 16]),
			new MultiBlockChangeMessage(2, 3, new short[]{2, 3, 4, /**/ 3, 6, 4, /**/ 8, 5, 5}, new short[]{1, 2, 3}, new byte[]{3, 4, 5}),
			new BlockChangeMessage(1, 2, 3, 87, 2),
			new BlockActionMessage(1, 2, 3, (byte) 4, (byte) 5),
			new ExplosionMessage(3, 4, 5, 24, new byte[]{1, 2, 3, 1, 1, 2, 1, 1, 1}),
			new PlayEffectMessage(34566, 1, 2, 34, 5),
			new ChangeGameStateMessage(ChangeGameStateMessage.CHANGE_GAME_MODE, GameMode.CREATIVE),
			new EntitySpawnLightningStrikeMessage(34, 1, 23, 45, 55),
			new WindowOpenMessage(1, WindowType.FURNACE, "container.furnace", 42),
			new WindowCloseMessage(23),
			new WindowClickMessage(1, 2, false, 34, true, 5, 5, 12, null),
			new WindowSetSlotMessage(1, 2, 45, 5, 5, null),
			new WindowSetSlotsMessage((byte) 3, new ItemStack[]{new ItemStack(VanillaMaterials.PISTON_BASE, 3), new ItemStack(VanillaMaterials.ARROW, 23)}),
			new WindowPropertyMessage(2, 4, 55),
			new WindowTransactionMessage(1, 55, true),
			new CreativeMessage((short) 1, (short) 2, (short) 3, (short) 4, null),
			new EnchantItemMessage(2, 3),
			new UpdateSignMessage(1, 2, 3, new String[]{"This", "is", "a", "sign"}),
			new ItemDataMessage((short) 1, (short) 2, new byte[]{2, 3, 8, 127, 123}),
			new TileEntityDataMessage(23, 45, 903, 1, 98, 0, 0),
			new StatisticMessage(1, (byte) 5),
			new EncryptionKeyResponseMessage(new byte[]{(byte) 7, (byte) 4, (byte) 1, (byte) 122}, true),
			new EncryptionKeyRequestMessage("This is a server", new byte[]{(byte) 1, (byte) 2, (byte) 3, (byte) 10}, true),
			new PlayerListMessage("Player", true, (short) 23),
			new CustomDataMessage("EMERGENCY", new byte[]{0, 1, 1, 8, 9, 9, 8, 8, 8, 1, 9, 9, 9, 1, 1, 9, 7, 2, 5, 3}),
			new ServerListPingMessage(),
			new KickMessage("This is a test"),
			new PlayerAbilityMessage(true, true, true, true)
	};

	static {
		for (Message msg : TEST_MESSAGES) {
			if (msg instanceof CompressedChunkMessage) {
				Arrays.fill(((CompressedChunkMessage) msg).getData()[0], (byte) 5);
			}
		}
	}

	public VanillaProtocolTest() {
		super(CODEC_LOOKUP, TEST_MESSAGES);
	}
}
