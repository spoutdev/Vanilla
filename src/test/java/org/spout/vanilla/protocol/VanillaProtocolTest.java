/*
 * This file is part of vanilla (http://www.spout.org/).
 *
 * vanilla is licensed under the SpoutDev License Version 1.
 *
 * vanilla is free software: you can redistribute it and/or modify
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
 * the MIT license and the SpoutDev license version 1 along with this program.
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
import org.spout.vanilla.VanillaMaterials;
import org.spout.vanilla.entity.EntityEffect;
import org.spout.vanilla.protocol.msg.ActivateItemMessage;
import org.spout.vanilla.protocol.msg.AnimateEntityMessage;
import org.spout.vanilla.protocol.msg.AttachEntityMessage;
import org.spout.vanilla.protocol.msg.BlockChangeMessage;
import org.spout.vanilla.protocol.msg.BlockPlacementMessage;
import org.spout.vanilla.protocol.msg.ChatMessage;
import org.spout.vanilla.protocol.msg.CloseWindowMessage;
import org.spout.vanilla.protocol.msg.CollectItemMessage;
import org.spout.vanilla.protocol.msg.CompressedChunkMessage;
import org.spout.vanilla.protocol.msg.CreateEntityMessage;
import org.spout.vanilla.protocol.msg.CustomDataMessage;
import org.spout.vanilla.protocol.msg.DestroyEntityMessage;
import org.spout.vanilla.protocol.msg.DiggingMessage;
import org.spout.vanilla.protocol.msg.EnchantItemMessage;
import org.spout.vanilla.protocol.msg.EntityActionMessage;
import org.spout.vanilla.protocol.msg.EntityEffectMessage;
import org.spout.vanilla.protocol.msg.EntityEquipmentMessage;
import org.spout.vanilla.protocol.msg.EntityInteractionMessage;
import org.spout.vanilla.protocol.msg.EntityMetadataMessage;
import org.spout.vanilla.protocol.msg.EntityRemoveEffectMessage;
import org.spout.vanilla.protocol.msg.EntityRotationMessage;
import org.spout.vanilla.protocol.msg.EntityStatusMessage;
import org.spout.vanilla.protocol.msg.EntityTeleportMessage;
import org.spout.vanilla.protocol.msg.EntityVelocityMessage;
import org.spout.vanilla.protocol.msg.ExperienceMessage;
import org.spout.vanilla.protocol.msg.ExperienceOrbMessage;
import org.spout.vanilla.protocol.msg.ExplosionMessage;
import org.spout.vanilla.protocol.msg.GroundMessage;
import org.spout.vanilla.protocol.msg.HandshakeMessage;
import org.spout.vanilla.protocol.msg.HealthMessage;
import org.spout.vanilla.protocol.msg.IdentificationMessage;
import org.spout.vanilla.protocol.msg.KickMessage;
import org.spout.vanilla.protocol.msg.LoadChunkMessage;
import org.spout.vanilla.protocol.msg.MapDataMessage;
import org.spout.vanilla.protocol.msg.MultiBlockChangeMessage;
import org.spout.vanilla.protocol.msg.OpenWindowMessage;
import org.spout.vanilla.protocol.msg.PingMessage;
import org.spout.vanilla.protocol.msg.PlayEffectMessage;
import org.spout.vanilla.protocol.msg.BlockActionMessage;
import org.spout.vanilla.protocol.msg.PositionMessage;
import org.spout.vanilla.protocol.msg.PositionRotationMessage;
import org.spout.vanilla.protocol.msg.ProgressBarMessage;
import org.spout.vanilla.protocol.msg.QuickBarMessage;
import org.spout.vanilla.protocol.msg.RelativeEntityPositionMessage;
import org.spout.vanilla.protocol.msg.RelativeEntityPositionRotationMessage;
import org.spout.vanilla.protocol.msg.RespawnMessage;
import org.spout.vanilla.protocol.msg.RotationMessage;
import org.spout.vanilla.protocol.msg.ServerListPingMessage;
import org.spout.vanilla.protocol.msg.SetWindowSlotMessage;
import org.spout.vanilla.protocol.msg.SetWindowSlotsMessage;
import org.spout.vanilla.protocol.msg.SpawnItemMessage;
import org.spout.vanilla.protocol.msg.SpawnLightningStrikeMessage;
import org.spout.vanilla.protocol.msg.SpawnMobMessage;
import org.spout.vanilla.protocol.msg.SpawnPaintingMessage;
import org.spout.vanilla.protocol.msg.SpawnPlayerMessage;
import org.spout.vanilla.protocol.msg.SpawnPositionMessage;
import org.spout.vanilla.protocol.msg.SpawnVehicleMessage;
import org.spout.vanilla.protocol.msg.StateChangeMessage;
import org.spout.vanilla.protocol.msg.StatisticMessage;
import org.spout.vanilla.protocol.msg.TimeMessage;
import org.spout.vanilla.protocol.msg.TransactionMessage;
import org.spout.vanilla.protocol.msg.UpdateSignMessage;
import org.spout.vanilla.protocol.msg.UseBedMessage;
import org.spout.vanilla.protocol.msg.UserListItemMessage;
import org.spout.vanilla.protocol.msg.WindowClickMessage;

import java.io.IOException;

import static org.junit.Assert.*;
import static org.spout.vanilla.protocol.ChannelBufferUtilsTest.TEST_PARAMS;
/**
 *
 * @author zml2008
 */
public class VanillaProtocolTest {
	private static final VanillaCodecLookupService CODEC_LOOKUP = new VanillaCodecLookupService();
	private static final Message[] TEST_MESSAGES = new Message[] {
			new PingMessage(42),
			new IdentificationMessage(0, "Tester", 244888L, 0, -1, 0, 128, 20, "MAGICAL"),
			new HandshakeMessage("Player"),
			new ChatMessage("<Spouty> This is a thing called a chat message"),
			new TimeMessage(666L),
			new EntityEquipmentMessage(234, 3, 2, 3),
			new SpawnPositionMessage(42, 42, 42),
			new EntityInteractionMessage(1123, 4455, true),
			new HealthMessage(1, 2, 3.4F),
			new RespawnMessage((byte) -1, (byte) 0, (byte) 1, 128, 435556L, "VERYFANCY"),
			new GroundMessage(true),
			new PositionMessage(128, 256, 512, 3.4D, true),
			new RotationMessage(1F, 2F, false),
			new PositionRotationMessage(1, 2, 5, 3.62, 4, 3, false),
			new DiggingMessage(DiggingMessage.STATE_START_DIGGING, 1, 2, 3, 4),
			new BlockPlacementMessage(1, 2, 3, 4, 89, 2, 7, null),
			new ActivateItemMessage(4),
			new UseBedMessage(0, 3, 42, 42, 42),
			new AnimateEntityMessage(1234, AnimateEntityMessage.ANIMATION_HURT),
			new EntityActionMessage(5, 2),
			new SpawnPlayerMessage(24, "risaccess1", 8000, 28, 900, 0, 0, 89),
			new SpawnItemMessage(1234, 89, 3, (short) 4, 1, 2, 3, 34, 56, 55),
			new CollectItemMessage(1234, 5678),
			new SpawnVehicleMessage(1, 3, 3, 654, 1234, 778, 656, 4354, 6564),
			new SpawnMobMessage(123, 255, 1, 2, 4, 34, 55, TEST_PARAMS),
			new SpawnPaintingMessage(4, "KEBAB", 2, 3, 4, 56),
			new ExperienceOrbMessage(34, 1, 2, 3, (short)34),
			new EntityVelocityMessage(1, 2, 3, 4),
			new DestroyEntityMessage(2),
			new CreateEntityMessage(2),
			new RelativeEntityPositionMessage(2, 1, 1, 1),
			new EntityRotationMessage(1234, 34, 5),
			new RelativeEntityPositionRotationMessage(1, 2, 3, 4, 45, 54),
			new EntityTeleportMessage(1, 2, 3, 4, 5, 6),
			new EntityStatusMessage(1, 2),
			new AttachEntityMessage(1, 2),
			new EntityMetadataMessage(1, TEST_PARAMS),
			new EntityEffectMessage(1, EntityEffect.BLINDNESS.getId(), (byte) 1, (short) 34),
			new EntityRemoveEffectMessage(1, EntityEffect.BLINDNESS.getId()),
			new ExperienceMessage(1.2F, (short) 2, (short) 3),
			new LoadChunkMessage(0, -2, true),
			new CompressedChunkMessage(1, 2, 3, 1, 2, 1, new byte[] {1,2, 33, 44, 55}),
			new MultiBlockChangeMessage(2, 3, new short[] {2, 3, 4}, new byte[] {1, 2, 3}, new byte[] {3, 4, 5}),
			new BlockChangeMessage(1, 2, 3, 87, 2),
			new BlockActionMessage(1, 2, 3, (byte) 4, (byte) 5),
			new ExplosionMessage(3, 4, 5, 24, new byte[] {1, 2, 3, 1, 1, 2, 1, 1, 1}),
			new PlayEffectMessage(34566, 1, 2, 34, 5),
			new StateChangeMessage((byte) 3, (byte) 1),
			new SpawnLightningStrikeMessage(34, 1, 23, 45, 55),
			new OpenWindowMessage(1, 2, "Furnace", 42),
			new CloseWindowMessage(23),
			new WindowClickMessage(1, 2, false, 34, true, 5, 5, 12, null),
			new SetWindowSlotMessage(1, 2, 45, 5, 5, null),
			new SetWindowSlotsMessage(3, new ItemStack[] {new ItemStack(VanillaMaterials.PISTON_BASE, 3), new ItemStack(VanillaMaterials.ARROW, 23)}),
			new ProgressBarMessage(2, 4, 55),
			new TransactionMessage(1, 55, true),
			new QuickBarMessage((short) 1, (short) 2, (short) 3, (short) 4, null),
			new EnchantItemMessage(2, 3),
			new UpdateSignMessage(1, 2, 3, new String[] {"This", "is", "a", "sign"}),
			new MapDataMessage((short)1, (short)2, new byte[] {2, 3, 8, 127, 123}),
			new StatisticMessage(1, (byte) 5),
			new UserListItemMessage("Player", true, (short)23),
			new CustomDataMessage("EMERGENCY_SERVICES", new byte[] {0, 1, 1, 8, 9, 9, 8, 8, 8, 1, 9, 9, 9, 1, 1, 9, 7, 2, 5, 3}),
			new ServerListPingMessage(),
			new KickMessage("This is a test")
	};

	@Test
	public void testMessageCodecLookup() {
		for (Message message : TEST_MESSAGES) {
			MessageCodec codec = CODEC_LOOKUP.find(message.getClass());
			assertNotNull("Message " + message + " did not have a codec!", codec);
			int opcode = codec.getOpcode();
			if (!codec.isExpanded()) {
				opcode = opcode << 8;
			}
			MessageCodec idCodec = CODEC_LOOKUP.find(opcode);
			assertNotNull("No codec for opcode "+ opcode + " in codec lookup!", idCodec);
		}
	}
	
	@Test
	public void testMessageEncoding() throws IOException {
		for (Message message : TEST_MESSAGES) {
			MessageCodec codec = CODEC_LOOKUP.find(message.getClass());
			ChannelBuffer encoded = codec.encode(message);
			Message decoded = codec.decode(encoded);
		assertEquals("Failed for : " + message.getClass(), message.toString(), decoded.toString());
		}
	}
	
	@Test
	public void testTestCompleteness() {
		final TIntSet testedOpcodes = new TIntHashSet();
		for (Message message : TEST_MESSAGES) {
			MessageCodec codec = CODEC_LOOKUP.find(message.getClass());
			if (codec != null) {
				int opcode = codec.getOpcode();
				if (!codec.isExpanded()) {
					opcode = opcode << 8;
				}
				testedOpcodes.add(opcode);
			}
		}
		for (MessageCodec<?> codec : CODEC_LOOKUP.getCodecs()) {
			int opcode = codec.getOpcode();
			if (!codec.isExpanded()) {
				opcode = opcode << 8;
			}
			assertTrue("Opcode "+ opcode + " (non-expanded: " + (opcode >> 8) + ") not tested", testedOpcodes.contains(opcode));
		}
	}
}
