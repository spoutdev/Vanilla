/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, SpoutDev <http://www.spout.org/>
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
import org.spout.api.protocol.Message;
import org.spout.api.protocol.common.message.CustomDataMessage;

import org.spout.vanilla.controller.effect.EntityEffect;
import org.spout.vanilla.controller.living.player.GameMode;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.protocol.msg.AnimationMessage;
import org.spout.vanilla.protocol.msg.AttachEntityMessage;
import org.spout.vanilla.protocol.msg.BlockActionMessage;
import org.spout.vanilla.protocol.msg.BlockChangeMessage;
import org.spout.vanilla.protocol.msg.ChangeGameStateMessage;
import org.spout.vanilla.protocol.msg.ChatMessage;
import org.spout.vanilla.protocol.msg.CloseWindowMessage;
import org.spout.vanilla.protocol.msg.CollectItemMessage;
import org.spout.vanilla.protocol.msg.CompressedChunkMessage;
import org.spout.vanilla.protocol.msg.CreateEntityMessage;
import org.spout.vanilla.protocol.msg.CreativeMessage;
import org.spout.vanilla.protocol.msg.DestroyEntityMessage;
import org.spout.vanilla.protocol.msg.EnchantItemMessage;
import org.spout.vanilla.protocol.msg.EntityActionMessage;
import org.spout.vanilla.protocol.msg.EntityEffectMessage;
import org.spout.vanilla.protocol.msg.EntityEquipmentMessage;
import org.spout.vanilla.protocol.msg.EntityHeadYawMessage;
import org.spout.vanilla.protocol.msg.EntityInteractionMessage;
import org.spout.vanilla.protocol.msg.EntityMetadataMessage;
import org.spout.vanilla.protocol.msg.EntityRemoveEffectMessage;
import org.spout.vanilla.protocol.msg.EntityRotationMessage;
import org.spout.vanilla.protocol.msg.EntityStatusMessage;
import org.spout.vanilla.protocol.msg.EntityTeleportMessage;
import org.spout.vanilla.protocol.msg.EntityVelocityMessage;
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
import org.spout.vanilla.protocol.msg.OpenWindowMessage;
import org.spout.vanilla.protocol.msg.PlayEffectMessage;
import org.spout.vanilla.protocol.msg.PlayerAbilityMessage;
import org.spout.vanilla.protocol.msg.PlayerBlockPlacementMessage;
import org.spout.vanilla.protocol.msg.PlayerDiggingMessage;
import org.spout.vanilla.protocol.msg.PlayerListMessage;
import org.spout.vanilla.protocol.msg.PlayerLookMessage;
import org.spout.vanilla.protocol.msg.PlayerPositionLookMessage;
import org.spout.vanilla.protocol.msg.PlayerPositionMessage;
import org.spout.vanilla.protocol.msg.ProgressBarMessage;
import org.spout.vanilla.protocol.msg.RelativeEntityPositionMessage;
import org.spout.vanilla.protocol.msg.RelativeEntityPositionRotationMessage;
import org.spout.vanilla.protocol.msg.RespawnMessage;
import org.spout.vanilla.protocol.msg.ServerListPingMessage;
import org.spout.vanilla.protocol.msg.SetExperienceMessage;
import org.spout.vanilla.protocol.msg.SetWindowSlotMessage;
import org.spout.vanilla.protocol.msg.SetWindowSlotsMessage;
import org.spout.vanilla.protocol.msg.SpawnDroppedItemMessage;
import org.spout.vanilla.protocol.msg.SpawnExperienceOrbMessage;
import org.spout.vanilla.protocol.msg.SpawnLightningStrikeMessage;
import org.spout.vanilla.protocol.msg.SpawnMobMessage;
import org.spout.vanilla.protocol.msg.SpawnPaintingMessage;
import org.spout.vanilla.protocol.msg.SpawnPlayerMessage;
import org.spout.vanilla.protocol.msg.SpawnPositionMessage;
import org.spout.vanilla.protocol.msg.SpawnVehicleMessage;
import org.spout.vanilla.protocol.msg.StatisticMessage;
import org.spout.vanilla.protocol.msg.TileEntityDataMessage;
import org.spout.vanilla.protocol.msg.TimeUpdateMessage;
import org.spout.vanilla.protocol.msg.TransactionMessage;
import org.spout.vanilla.protocol.msg.UpdateHealthMessage;
import org.spout.vanilla.protocol.msg.UpdateSignMessage;
import org.spout.vanilla.protocol.msg.UseBedMessage;
import org.spout.vanilla.protocol.msg.WindowClickMessage;

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
			new PlayerDiggingMessage(PlayerDiggingMessage.STATE_START_DIGGING, 1, 2, 3, 4),
			new PlayerBlockPlacementMessage(1, 2, 3, 4, 89, 2, 7, null),
			new HeldItemChangeMessage(4),
			new UseBedMessage(0, 3, 42, 42, 42),
			new AnimationMessage(1234, AnimationMessage.ANIMATION_HURT),
			new EntityActionMessage(5, 2),
			new SpawnPlayerMessage(24, "risaccess1", 8000, 28, 900, 0, 0, 89),
			new SpawnDroppedItemMessage(1234, 89, 3, (short) 4, 1, 2, 3, 34, 56, 55),
			new CollectItemMessage(1234, 5678),
			new SpawnVehicleMessage(1, 3, 3, 654, 1234, 778, 656, 4354, 6564),
			new SpawnMobMessage(123, 255, 1, 2, 4, 34, 55, 33, TEST_PARAMS),
			new SpawnPaintingMessage(4, "KEBAB", 2, 3, 4, 56),
			new SpawnExperienceOrbMessage(34, 1, 2, 3, (short) 34),
			new EntityVelocityMessage(1, 2, 3, 4),
			new DestroyEntityMessage(2),
			new CreateEntityMessage(2),
			new RelativeEntityPositionMessage(2, 1, 1, 1),
			new EntityRotationMessage(1234, 34, 5),
			new RelativeEntityPositionRotationMessage(1, 2, 3, 4, 45, 54),
			new EntityTeleportMessage(1, 2, 3, 4, 5, 6),
			new EntityHeadYawMessage(45, 3),
			new EntityStatusMessage(1, (byte) 2),
			new AttachEntityMessage(1, 2),
			new EntityMetadataMessage(1, TEST_PARAMS),
			new EntityEffectMessage(1, EntityEffect.BLINDNESS.getId(), (byte) 1, (short) 34),
			new EntityRemoveEffectMessage(1, EntityEffect.BLINDNESS.getId()),
			new SetExperienceMessage(1.2F, (short) 2, (short) 3),
			new LoadChunkMessage(0, -2, true),
			new CompressedChunkMessage(1, 2, true, new boolean[16], 1, new byte[][]{new byte[16 * 16 * 16 * 5 / 2], null, null, null, null, null, null, null, null, null, new byte[Chunk.CHUNK_VOLUME * 5 / 2], null, null, null, null, null}, new byte[16 * 16]),
			new MultiBlockChangeMessage(2, 3, new short[]{2, 3, 4, /**/ 3, 6, 4, /**/ 8, 5, 5}, new short[]{1, 2, 3}, new byte[]{3, 4, 5}),
			new BlockChangeMessage(1, 2, 3, 87, 2),
			new BlockActionMessage(1, 2, 3, (byte) 4, (byte) 5),
			new ExplosionMessage(3, 4, 5, 24, new byte[]{1, 2, 3, 1, 1, 2, 1, 1, 1}),
			new PlayEffectMessage(34566, 1, 2, 34, 5),
			new ChangeGameStateMessage(ChangeGameStateMessage.CHANGE_GAME_MODE, GameMode.CREATIVE),
			new SpawnLightningStrikeMessage(34, 1, 23, 45, 55),
			new OpenWindowMessage(1, 2, "container.furnace", 42),
			new CloseWindowMessage(23),
			new WindowClickMessage(1, 2, false, 34, true, 5, 5, 12, null),
			new SetWindowSlotMessage(1, 2, 45, 5, 5, null),
			new SetWindowSlotsMessage((byte) 3, new ItemStack[]{new ItemStack(VanillaMaterials.PISTON_BASE, 3), new ItemStack(VanillaMaterials.ARROW, 23)}),
			new ProgressBarMessage(2, 4, 55),
			new TransactionMessage(1, 55, true),
			new CreativeMessage((short) 1, (short) 2, (short) 3, (short) 4, null),
			new EnchantItemMessage(2, 3),
			new UpdateSignMessage(1, 2, 3, new String[]{"This", "is", "a", "sign"}),
			new ItemDataMessage((short) 1, (short) 2, new byte[]{2, 3, 8, 127, 123}),
			new TileEntityDataMessage(23, 45, 903, 1, 98, 0, 0),
			new StatisticMessage(1, (byte) 5),
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
