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

import java.util.ArrayList;
import java.util.Arrays;

import org.spout.api.geo.cuboid.Chunk;
import org.spout.api.inventory.ItemStack;
import org.spout.api.material.block.BlockFace;
import org.spout.api.math.Vector3;
import org.spout.api.protocol.Message;
import org.spout.api.util.Parameter;

import org.spout.vanilla.data.Animation;
import org.spout.vanilla.data.GameMode;
import org.spout.vanilla.inventory.window.WindowType;
import org.spout.vanilla.material.VanillaMaterials;
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
import org.spout.vanilla.protocol.msg.entity.EntityInteractMessage;
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
import org.spout.vanilla.protocol.msg.entity.spawn.EntityItemMessage;
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
import org.spout.vanilla.protocol.msg.player.PlayerSoundEffectMessage;
import org.spout.vanilla.protocol.msg.player.PlayerStatisticMessage;
import org.spout.vanilla.protocol.msg.player.PlayerStatusMessage;
import org.spout.vanilla.protocol.msg.player.PlayerTabCompleteMessage;
import org.spout.vanilla.protocol.msg.player.PlayerTimeMessage;
import org.spout.vanilla.protocol.msg.player.conn.PlayerHandshakeMessage;
import org.spout.vanilla.protocol.msg.player.conn.PlayerKickMessage;
import org.spout.vanilla.protocol.msg.player.conn.PlayerListMessage;
import org.spout.vanilla.protocol.msg.player.conn.PlayerLoginRequestMessage;
import org.spout.vanilla.protocol.msg.player.conn.PlayerPingMessage;
import org.spout.vanilla.protocol.msg.player.pos.PlayerPositionMessage;
import org.spout.vanilla.protocol.msg.player.pos.PlayerPositionYawMessage;
import org.spout.vanilla.protocol.msg.player.pos.PlayerRespawnMessage;
import org.spout.vanilla.protocol.msg.player.pos.PlayerSpawnMessage;
import org.spout.vanilla.protocol.msg.player.pos.PlayerSpawnPositionMessage;
import org.spout.vanilla.protocol.msg.player.pos.PlayerYawMessage;
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
import org.spout.vanilla.protocol.msg.world.block.BlockActionMessage;
import org.spout.vanilla.protocol.msg.world.block.BlockBreakAnimationMessage;
import org.spout.vanilla.protocol.msg.world.block.BlockBulkMessage;
import org.spout.vanilla.protocol.msg.world.block.BlockChangeMessage;
import org.spout.vanilla.protocol.msg.world.block.SignMessage;
import org.spout.vanilla.protocol.msg.world.chunk.ChunkBulkMessage;
import org.spout.vanilla.protocol.msg.world.chunk.ChunkDataMessage;

import static org.spout.vanilla.protocol.ChannelBufferUtilsTest.TEST_PARAMS;

public class VanillaProtocolTest extends BaseProtocolTest {
	private static final VanillaCodecLookupService CODEC_LOOKUP = new VanillaCodecLookupService();
	static boolean[] allFalse = new boolean[16];
	static byte[] chunkData = new byte[10240];
	static byte[][] columnData = new byte[][]{chunkData, chunkData, chunkData, chunkData, chunkData, chunkData, chunkData, chunkData, chunkData, chunkData, chunkData, chunkData, chunkData, chunkData, chunkData, chunkData};
	static byte[] biomeData1 = new byte[256];
	static byte[] biomeData2 = new byte[256];
	private static final Message[] TEST_MESSAGES = new Message[]{
			new PlayerPingMessage(42),
			new WindowCreativeActionMessage((short) 0, new ItemStack(VanillaMaterials.BED, 10, 20)),
			new PlayerLoginRequestMessage(0, "nether", (byte) 0, (byte) 0, (byte) 0, (short) 10),
			new PlayerHandshakeMessage((byte) 42, "Spouty", "SpoutTron", 9001),
			new PlayerChatMessage("<Spouty> This is a thing called a chat message"),
			new PlayerTimeMessage(666L),
			new EntityEquipmentMessage(234, 3, new ItemStack(VanillaMaterials.PLANK, 3, 55)),
			new PlayerSpawnPositionMessage(42, 42, 42),
			new EntityInteractMessage(1123, 4455, true),
			new PlayerHealthMessage((short) 1, (short) 2, 3.4F),
			new PlayerRespawnMessage(89, (byte) 0, (byte) 1, 128, "VERYFANCY"),
			new PlayerGroundMessage(true),
			new PlayerPositionMessage(128, 256, 512, 3.4D, true),
			new PlayerYawMessage(1F, 2F, false),
			new PlayerPositionYawMessage(1, 2, 5, 3.62, 4, 3, false),
			new PlayerDiggingMessage(PlayerDiggingMessage.STATE_START_DIGGING, 1, 2, 3, BlockFace.NORTH),
			new PlayerBlockPlacementMessage(1, 2, 3, BlockFace.NORTH, new Vector3(0.1875F, 0.5F, 0.0F)),
			new PlayerHeldItemChangeMessage(4),
			new PlayerLocaleViewDistanceMessage("en_GB", PlayerLocaleViewDistanceMessage.VIEW_NORMAL, (byte) 0, (byte) 0),
			new PlayerTabCompleteMessage("behindcursor"),
			new PlayerBedMessage(0, 3, 42, 42, 42),
			new EntityAnimationMessage(1234, (byte) Animation.DAMAGE_ANIMATION.getId()),
			new EntityActionMessage(5, 2),
			new PlayerSpawnMessage(24, "risaccess1", 8000, 28, 900, 0, 0, 89, new ArrayList<Parameter<?>>()),
			new EntityItemMessage(1234, 89, 3, (short) 4, 1, 2, 3, 34, 56, 55),
			new PlayerCollectItemMessage(1234, 5678),
			new EntityObjectMessage(1, (byte) 1, 200, 175, 132, 50, (short) 62, (short) 56, (short) 78),
			new EntityMobMessage(123, 255, 1, 2, 4, 34, 55, 33, (short) 0, (short) 0, (short) 0, TEST_PARAMS),
			new EntityPaintingMessage(4, "KEBAB", 2, 3, 4, 56),
			new EntityExperienceOrbMessage(34, 1, 2, 3, (short) 34),
			new EntityVelocityMessage(1, 2, 3, 4),
			new EntityInitializeMessage(2),
			new EntityRelativePositionMessage(2, 1, 1, 1),
			new EntityYawMessage(1234, 34, 5),
			new EntityRelativePositionYawMessage(1, 2, 3, 4, 45, 54),
			new EntityTeleportMessage(1, 2, 3, 4, 5, 6),
			new EntityHeadYawMessage(45, 3),
			new EntityStatusMessage(1, (byte) 2),
			new EntityAttachEntityMessage(1, 2),
			new EntityMetadataMessage(1, TEST_PARAMS),
			new EntityEffectMessage(1, (byte) 1, (byte) 1, (short) 34),
			new EntityRemoveEffectMessage(1, (byte) 1),
			new PlayerSoundEffectMessage("random.bow", 12.5f, 0.0f, 12.0f, 1.0f, 1.0f),
			new EntityDestroyMessage(new int[]{2}),
			new PlayerExperienceMessage(1.2F, (short) 2, (short) 3),
			new ChunkDataMessage(1, 2, true, new boolean[16], new byte[][]{new byte[16 * 16 * 16 * 5 / 2], null, null, null, null, null, null, null, null, null, new byte[Chunk.BLOCKS.HALF_VOLUME * 5], null, null, null, null, null}, new byte[16 * 16], null),
			new BlockBulkMessage(2, 3, new short[]{2, 3, 4, /**/ 3, 6, 4, /**/ 8, 5, 5}, new short[]{1, 2, 3}, new byte[]{3, 4, 5}),
			new BlockChangeMessage(1, 2, 3, (short) 87, 2),
			new BlockActionMessage(1, 2, 3, (byte) 4, (byte) 5, (byte) 29),
			new ChunkBulkMessage(new int[]{0, 1}, new int[]{3, 4}, new boolean[][]{allFalse, allFalse}, new byte[][][]{columnData, columnData}, new byte[][]{biomeData1, biomeData2}),
			new ExplosionMessage(3, 4, 5, 24, new byte[]{1, 2, 3, 1, 1, 2, 1, 1, 1}),
			new EffectMessage(34566, 1, 2, 34, 5),
			new PlayerGameStateMessage(PlayerGameStateMessage.CHANGE_GAME_MODE, GameMode.CREATIVE),
			new EntityThunderboltMessage(34, 1, 23, 45, 55),
			new WindowOpenMessage(1, WindowType.FURNACE, "container.furnace", 42),
			new WindowCloseMessage(23),
			new WindowClickMessage(1, 2, false, 34, true, new ItemStack(VanillaMaterials.PLANK, 1, 1)),
			new WindowSlotMessage(1, 2, new ItemStack(VanillaMaterials.PLANK, 1, 2)),
			new WindowItemsMessage((byte) 3, new ItemStack[]{new ItemStack(VanillaMaterials.PISTON_BASE, 3), new ItemStack(VanillaMaterials.ARROW, 23)}),
			new WindowPropertyMessage(2, 4, 55),
			new WindowTransactionMessage(1, 55, true),
			new WindowEnchantItemMessage(2, 3),
			new SignMessage(1, 2, 3, new String[]{"This", "is", "a", "sign"}),
			new EntityItemDataMessage((short) 1, (short) 2, new byte[]{2, 3, 8, 127, 123}),
			new EntityTileDataMessage(23, 45, 903, 1, 0, 0, 0),
			new PlayerStatisticMessage(1, (byte) 5),
			new EncryptionKeyResponseMessage(false, new byte[]{(byte) 7, (byte) 4, (byte) 1, (byte) 122}, new byte[]{(byte) 6, (byte) 3, (byte) 4, (byte) 122}),
			new EncryptionKeyRequestMessage("This is a server", false, new byte[]{(byte) 1, (byte) 2, (byte) 3, (byte) 10}, new byte[]{(byte) 12, (byte) 54, (byte) 4, (byte) 122}),
			new PlayerListMessage("Player", true, (short) 23),
			new ServerPluginMessage("EMERGENCY", new byte[]{0, 1, 1, 8, 9, 9, 8, 8, 8, 1, 9, 9, 9, 1, 1, 9, 7, 2, 5, 3}),
			new ServerListPingMessage(),
			new PlayerKickMessage("This is a test"),
			new PlayerAbilityMessage(true, true, true, true, (byte) 0, (byte) 5),
			new PlayerStatusMessage((byte) 0),
			new BlockBreakAnimationMessage(1, 1, 1, 1, (byte) 2)
	};

	static {
		for (Message msg : TEST_MESSAGES) {
			if (msg instanceof ChunkDataMessage) {
				Arrays.fill(((ChunkDataMessage) msg).getData()[0], (byte) 5);
			}
		}
	}

	public VanillaProtocolTest() {
		super(CODEC_LOOKUP, TEST_MESSAGES);
	}
}
