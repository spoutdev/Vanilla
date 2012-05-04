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

import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;
import org.jboss.netty.buffer.ChannelBuffer;
import org.junit.Test;
import org.spout.api.inventory.ItemStack;
import org.spout.api.protocol.Message;
import org.spout.api.protocol.MessageCodec;
import org.spout.api.protocol.common.message.CustomDataMessage;
import org.spout.vanilla.controller.effect.EntityEffect;
import org.spout.vanilla.controller.living.player.GameMode;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.protocol.msg.*;

import java.io.IOException;

import static org.junit.Assert.*;
import static org.spout.vanilla.protocol.ChannelBufferUtilsTest.TEST_PARAMS;

public class VanillaProtocolTest {
	private static final VanillaCodecLookupService CODEC_LOOKUP = new VanillaCodecLookupService();
	private static final Message[] TEST_MESSAGES = new Message[]{new KeepAliveMessage(42), new LoginRequestMessage(0, "Tester", 0, -1, 0, 256, 20, "MAGICAL"), new HandshakeMessage("Player"), new ChatMessage("<Spouty> This is a thing called a chat message"), new TimeUpdateMessage(666L), new EntityEquipmentMessage(234, 3, 2, 3), new SpawnPositionMessage(42, 42, 42), new EntityInteractionMessage(1123, 4455, true), new UpdateHealthMessage((short) 1, (short) 2, 3.4F), new RespawnMessage(89, (byte) 0, (byte) 1, 128, "VERYFANCY"), new GroundMessage(true), new PlayerPositionMessage(128, 256, 512, 3.4D, true), new PlayerLookMessage(1F, 2F, false), new PlayerPositionLookMessage(1, 2, 5, 3.62, 4, 3, false), new PlayerDiggingMessage(PlayerDiggingMessage.STATE_START_DIGGING, 1, 2, 3, 4), new PlayerBlockPlacementMessage(1, 2, 3, 4, 89, 2, 7, null), new HeldItemChangeMessage(4), new UseBedMessage(0, 3, 42, 42, 42), new EntityAnimationMessage(1234, EntityAnimationMessage.ANIMATION_HURT), new EntityActionMessage(5, 2), new SpawnPlayerMessage(24, "risaccess1", 8000, 28, 900, 0, 0, 89), new SpawnDroppedItemMessage(1234, 89, 3, (short) 4, 1, 2, 3, 34, 56, 55), new CollectItemMessage(1234, 5678), new SpawnVehicleMessage(1, 3, 3, 654, 1234, 778, 656, 4354, 6564), new SpawnMobMessage(123, 255, 1, 2, 4, 34, 55, 33, TEST_PARAMS), new SpawnPaintingMessage(4, "KEBAB", 2, 3, 4, 56), new SpawnExperienceOrbMessage(34, 1, 2, 3, (short) 34), new EntityVelocityMessage(1, 2, 3, 4), new DestroyEntityMessage(2), new CreateEntityMessage(2), new RelativeEntityPositionMessage(2, 1, 1, 1), new EntityRotationMessage(1234, 34, 5), new RelativeEntityPositionRotationMessage(1, 2, 3, 4, 45, 54), new EntityTeleportMessage(1, 2, 3, 4, 5, 6), new EntityHeadYawMessage(45, 3), new EntityStatusMessage(1, (byte) 2), new AttachEntityMessage(1, 2), new EntityMetadataMessage(1, TEST_PARAMS), new EntityEffectMessage(1, EntityEffect.BLINDNESS.getId(), (byte) 1, (short) 34), new EntityRemoveEffectMessage(1, EntityEffect.BLINDNESS.getId()), new SetExperienceMessage(1.2F, (short) 2, (short) 3), new LoadChunkMessage(0, -2, true), new CompressedChunkMessage(1, 2, true, new boolean[16], 1, new byte[][]{new byte[16 * 16 * 16 * 5 / 2], null, null, null, null, null, null, null, null, null, new byte[16 * 16 * 16 * 5 / 2], null, null, null, null, null}, new byte[16 * 16]), new MultiBlockChangeMessage(2, 3, new short[]{2, 3, 4, /**/ 3, 6, 4, /**/ 8, 5, 5}, new short[]{1, 2, 3}, new byte[]{3, 4, 5}), new BlockChangeMessage(1, 2, 3, 87, 2), new BlockActionMessage(1, 2, 3, (byte) 4, (byte) 5), new ExplosionMessage(3, 4, 5, 24, new byte[]{1, 2, 3, 1, 1, 2, 1, 1, 1}), new PlayEffectMessage(34566, 1, 2, 34, 5), new ChangeGameStateMessage(ChangeGameStateMessage.CHANGE_GAME_MODE, GameMode.CREATIVE), new SpawnLightningStrikeMessage(34, 1, 23, 45, 55), new OpenWindowMessage(1, 2, "container.furnace", 42), new CloseWindowMessage(23), new WindowClickMessage(1, 2, false, 34, true, 5, 5, 12, null), new SetWindowSlotMessage(1, 2, 45, 5, 5, null), new SetWindowSlotsMessage((byte) 3, new ItemStack[]{new ItemStack(VanillaMaterials.PISTON_BASE, 3), new ItemStack(VanillaMaterials.ARROW, 23)}), new ProgressBarMessage(2, 4, 55), new TransactionMessage(1, 55, true), new CreativeMessage((short) 1, (short) 2, (short) 3, (short) 4, null), new EnchantItemMessage(2, 3), new UpdateSignMessage(1, 2, 3, new String[]{"This", "is", "a", "sign"}), new ItemDataMessage((short) 1, (short) 2, new byte[]{2, 3, 8, 127, 123}), new TileEntityDataMessage(23, 45, 903, 1, 98, 0, 0), new StatisticMessage(1, (byte) 5), new PlayerListMessage("Player", true, (short) 23), new CustomDataMessage("EMERGENCY", new byte[]{0, 1, 1, 8, 9, 9, 8, 8, 8, 1, 9, 9, 9, 1, 1, 9, 7, 2, 5, 3}), new ServerListPingMessage(), new KickMessage("This is a test"), new PlayerAbilityMessage(true, true, true, true)};

	@Test
	public void testMessageCodecLookup() {
		for (Message message : TEST_MESSAGES) {
			MessageCodec<?> codec = CODEC_LOOKUP.find(message.getClass());
			assertNotNull("Message " + message + " did not have a codec!", codec);
			int opcode = codec.getOpcode();
			if (!codec.isExpanded()) {
				opcode <<= 8;
			}
			MessageCodec<?> idCodec = CODEC_LOOKUP.find(opcode);
			assertNotNull("No codec for opcode " + opcode + " in codec lookup!", idCodec);
		}
	}

	@Test
	public void testMessageEncoding() throws IOException {
		for (Message message : TEST_MESSAGES) {
			@SuppressWarnings("rawtypes") MessageCodec codec = CODEC_LOOKUP.find(message.getClass());
			@SuppressWarnings("unchecked") ChannelBuffer encoded = codec.encode(message);
			Message decoded = codec.decode(encoded);
			assertEquals("Failed for : " + message.getClass(), message.toString(), decoded.toString());
		}
	}

	@Test
	public void testTestCompleteness() {
		final TIntSet testedOpcodes = new TIntHashSet();
		for (Message message : TEST_MESSAGES) {
			MessageCodec<?> codec = CODEC_LOOKUP.find(message.getClass());
			if (codec != null) {
				int opcode = codec.getOpcode();
				if (!codec.isExpanded()) {
					opcode <<= 8;
				}
				testedOpcodes.add(opcode);
			}
		}
		for (MessageCodec<?> codec : CODEC_LOOKUP.getCodecs()) {
			int opcode = codec.getOpcode();
			if (!codec.isExpanded()) {
				opcode <<= 8;
			}
			assertTrue("Opcode " + opcode + " (non-expanded: " + (opcode >> 8) + ") not tested", testedOpcodes.contains(opcode));
		}
	}
}
