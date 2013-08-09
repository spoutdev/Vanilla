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

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

import org.spout.api.command.Command;
import org.spout.api.command.CommandArguments;
import org.spout.api.component.entity.PlayerNetworkComponent;
import org.spout.api.exception.ArgumentParseException;
import org.spout.api.exception.UnknownPacketException;
import org.spout.api.map.DefaultedKey;
import org.spout.api.map.DefaultedKeyImpl;
import org.spout.api.protocol.ClientSession;
import org.spout.api.protocol.Message;
import org.spout.api.protocol.MessageCodec;
import org.spout.api.protocol.Protocol;
import org.spout.api.protocol.ServerSession;
import org.spout.api.util.Named;

import org.spout.vanilla.ChatStyle;
import org.spout.vanilla.VanillaPlugin;
import org.spout.vanilla.component.entity.player.VanillaPlayerNetworkComponent;
import org.spout.vanilla.protocol.codec.auth.EncryptionKeyRequestCodec;
import org.spout.vanilla.protocol.codec.auth.EncryptionKeyResponseCodec;
import org.spout.vanilla.protocol.codec.entity.EntityActionCodec;
import org.spout.vanilla.protocol.codec.entity.EntityAnimationCodec;
import org.spout.vanilla.protocol.codec.entity.EntityAttachEntityCodec;
import org.spout.vanilla.protocol.codec.entity.EntityDestroyCodec;
import org.spout.vanilla.protocol.codec.entity.EntityEquipmentCodec;
import org.spout.vanilla.protocol.codec.entity.EntityInitializeCodec;
import org.spout.vanilla.protocol.codec.entity.EntityItemDataCodec;
import org.spout.vanilla.protocol.codec.entity.EntityMetadataCodec;
import org.spout.vanilla.protocol.codec.entity.EntityPropertiesCodec;
import org.spout.vanilla.protocol.codec.entity.EntityRelativePositionCodec;
import org.spout.vanilla.protocol.codec.entity.EntityStatusCodec;
import org.spout.vanilla.protocol.codec.entity.EntityTileDataCodec;
import org.spout.vanilla.protocol.codec.entity.SteerVehicleCodec;
import org.spout.vanilla.protocol.codec.entity.effect.EntityEffectCodec;
import org.spout.vanilla.protocol.codec.entity.effect.EntityRemoveEffectCodec;
import org.spout.vanilla.protocol.codec.entity.pos.EntityHeadYawCodec;
import org.spout.vanilla.protocol.codec.entity.pos.EntityRelativePositionYawCodec;
import org.spout.vanilla.protocol.codec.entity.pos.EntityTeleportCodec;
import org.spout.vanilla.protocol.codec.entity.pos.EntityVelocityCodec;
import org.spout.vanilla.protocol.codec.entity.pos.EntityYawCodec;
import org.spout.vanilla.protocol.codec.entity.spawn.EntityExperienceOrbCodec;
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
import org.spout.vanilla.protocol.codec.player.PlayerStatisticCodec;
import org.spout.vanilla.protocol.codec.player.PlayerStatusCodec;
import org.spout.vanilla.protocol.codec.player.PlayerTabCompleteCodec;
import org.spout.vanilla.protocol.codec.player.PlayerTimeCodec;
import org.spout.vanilla.protocol.codec.player.PlayerUseEntityCodec;
import org.spout.vanilla.protocol.codec.player.conn.PlayerHandshakeCodec;
import org.spout.vanilla.protocol.codec.player.conn.PlayerKickCodec;
import org.spout.vanilla.protocol.codec.player.conn.PlayerListCodec;
import org.spout.vanilla.protocol.codec.player.conn.PlayerLoginRequestCodec;
import org.spout.vanilla.protocol.codec.player.conn.PlayerPingCodec;
import org.spout.vanilla.protocol.codec.player.pos.PlayerLookCodec;
import org.spout.vanilla.protocol.codec.player.pos.PlayerPositionCodec;
import org.spout.vanilla.protocol.codec.player.pos.PlayerPositionLookCodec;
import org.spout.vanilla.protocol.codec.player.pos.PlayerRespawnCodec;
import org.spout.vanilla.protocol.codec.player.pos.PlayerSpawnCodec;
import org.spout.vanilla.protocol.codec.player.pos.PlayerSpawnPositionCodec;
import org.spout.vanilla.protocol.codec.scoreboard.ScoreboardDisplayCodec;
import org.spout.vanilla.protocol.codec.scoreboard.ScoreboardObjectiveCodec;
import org.spout.vanilla.protocol.codec.scoreboard.ScoreboardScoreCodec;
import org.spout.vanilla.protocol.codec.scoreboard.ScoreboardTeamCodec;
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
import org.spout.vanilla.protocol.codec.world.ParticleEffectCodec;
import org.spout.vanilla.protocol.codec.world.SoundEffectCodec;
import org.spout.vanilla.protocol.codec.world.block.BlockActionCodec;
import org.spout.vanilla.protocol.codec.world.block.BlockBreakAnimationCodec;
import org.spout.vanilla.protocol.codec.world.block.BlockBulkCodec;
import org.spout.vanilla.protocol.codec.world.block.BlockChangeCodec;
import org.spout.vanilla.protocol.codec.world.block.SignCodec;
import org.spout.vanilla.protocol.codec.world.chunk.ChunkBulkCodec;
import org.spout.vanilla.protocol.codec.world.chunk.ChunkDataCodec;
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
import org.spout.vanilla.protocol.handler.entity.EntityPropertiesHandler;
import org.spout.vanilla.protocol.handler.entity.EntityStatusHandler;
import org.spout.vanilla.protocol.handler.entity.EntityThunderboltHandler;
import org.spout.vanilla.protocol.handler.entity.EntityTileDataHandler;
import org.spout.vanilla.protocol.handler.entity.SteerVehicleHandler;
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
import org.spout.vanilla.protocol.msg.ServerPluginMessage;
import org.spout.vanilla.protocol.msg.player.PlayerChatMessage;
import org.spout.vanilla.protocol.msg.player.conn.PlayerHandshakeMessage;
import org.spout.vanilla.protocol.msg.player.conn.PlayerKickMessage;
import org.spout.vanilla.protocol.netcache.ChunkNetCache;
import org.spout.vanilla.protocol.netcache.protocol.ChunkCacheCodec;
import org.spout.vanilla.protocol.netcache.protocol.ChunkCacheHandler;
import org.spout.vanilla.protocol.plugin.BeaconCodec;
import org.spout.vanilla.protocol.plugin.BeaconHandler;
import org.spout.vanilla.protocol.plugin.CommandBlockCodec;
import org.spout.vanilla.protocol.plugin.CommandBlockHandler;
import org.spout.vanilla.protocol.plugin.RegisterPluginChannelCodec;
import org.spout.vanilla.protocol.plugin.RegisterPluginChannelMessage;
import org.spout.vanilla.protocol.plugin.RegisterPluginChannelMessageHandler;
import org.spout.vanilla.protocol.plugin.UnregisterPluginChannelCodec;
import org.spout.vanilla.protocol.plugin.UnregisterPluginChannelMessageHandler;

public class VanillaProtocol extends Protocol {
	public static final DefaultedKey<String> SESSION_ID = new DefaultedKeyImpl<String>("sessionid", "0000000000000000");
	public static final DefaultedKey<String> HANDSHAKE_USERNAME = new DefaultedKeyImpl<String>("handshake_username", "");
	public static final DefaultedKey<Long> LOGIN_TIME = new DefaultedKeyImpl<Long>("handshake_time", -1L);
	public static final DefaultedKey<ChunkNetCache> CHUNK_NET_CACHE = new DefaultedKeyImpl<ChunkNetCache>("chunk_net_cache", (ChunkNetCache) null);
	public static final DefaultedKey<ArrayList<String>> REGISTERED_CUSTOM_PACKETS = new DefaultedKey<ArrayList<String>>() {
		private final List<String> defaultRestricted = Arrays.asList("REGISTER", "UNREGISTER");

		public ArrayList<String> getDefaultValue() {
			return new ArrayList<String>(defaultRestricted);
		}

		public String getKeyString() {
			return "registeredPluginChannels";
		}
	};
	public static final int DEFAULT_PORT = 25565;

	public VanillaProtocol() {
		super("Vanilla", DEFAULT_PORT, 512);
		// THIS FORMATTING IS CORRECT NO MATTER WHAT ANY AUTOFORMATTER SAYS. DON'T CHANGE IT
		/* 0x00 */
		registerPacket(PlayerPingCodec.class, new PlayerPingHandler());
		/* 0x01 */
		registerPacket(PlayerLoginRequestCodec.class, new PlayerLoginRequestHandler());
		/* 0x02 */
		registerPacket(PlayerHandshakeCodec.class, new PlayerHandshakeHandler());
		/* 0x03 */
		registerPacket(PlayerChatCodec.class, new PlayerChatHandler());
		/* 0x04 */
		registerPacket(PlayerTimeCodec.class, new PlayerTimeHandler());
		/* 0x05 */
		registerPacket(EntityEquipmentCodec.class, new EntityEquipmentHandler());
		/* 0x06 */
		registerPacket(PlayerSpawnPositionCodec.class, new PlayerSpawnPositionHandler());
		/* 0x07 */
		registerPacket(PlayerUseEntityCodec.class, new PlayerUseEntityHandler());
		/* 0x08 */
		registerPacket(PlayerHealthCodec.class, new PlayerHealthHandler());
		/* 0x09 */
		registerPacket(PlayerRespawnCodec.class, new PlayerRespawnHandler());
		/* 0x0A */
		registerPacket(PlayerGroundCodec.class, new PlayerGroundHandler());
		/* 0x0B */
		registerPacket(PlayerPositionCodec.class, new PlayerPositionHandler());
		/* 0x0C */
		registerPacket(PlayerLookCodec.class, new PlayerLookHandler());
		/* 0x0D */
		registerPacket(PlayerPositionLookCodec.class, new PlayerPositionLookHandler());
		/* 0x0E */
		registerPacket(PlayerDiggingCodec.class, new PlayerDiggingHandler());
		/* 0x0F */
		registerPacket(PlayerBlockPlacementCodec.class, new PlayerBlockPlacementHandler());
		/* 0x10 */
		registerPacket(PlayerHeldItemChangeCodec.class, new PlayerHeldItemChangeHandler());
		/* 0x11 */
		registerPacket(PlayerBedCodec.class, new PlayerBedHandler());
		/* 0x12 */
		registerPacket(EntityAnimationCodec.class, new EntityAnimationHandler());
		/* 0x13 */
		registerPacket(EntityActionCodec.class, new EntityActionHandler());
		/* 0x14 */
		registerPacket(PlayerSpawnCodec.class, new PlayerSpawnHandler());
		/* 0x15 */ //registerPacket(EntityItemCodec.class, new EntityItemHandler()); // Removed as of 1.4.6
		/* 0x16 */
		registerPacket(PlayerCollectItemCodec.class, new PlayerCollectItemHandler());
		/* 0x17 */
		registerPacket(EntitySpawnObjectCodec.class, new EntityObjectHandler());
		/* 0x18 */
		registerPacket(EntityMobCodec.class, new EntityMobHandler());
		/* 0x19 */
		registerPacket(EntityPaintingCodec.class, new EntityPaintingHandler());
		/* 0x1A */
		registerPacket(EntityExperienceOrbCodec.class, new EntityExperienceOrbHandler());
		/* 0x1B */
		registerPacket(SteerVehicleCodec.class, new SteerVehicleHandler());
		/* 0x1C */
		registerPacket(EntityVelocityCodec.class, new EntityVelocityHandler());
		/* 0x1D */
		registerPacket(EntityDestroyCodec.class, new EntityDestroyHandler());
		/* 0x1E */
		registerPacket(EntityInitializeCodec.class, new EntityInitializeHandler()); //TODO the meaning of this packet is basically that the entity did not move/look since the last such packet. We need to implement this!
		/* 0x1F */
		registerPacket(EntityRelativePositionCodec.class, new EntityRelativePositionHandler());
		/* 0x20 */
		registerPacket(EntityYawCodec.class, new EntityYawHandler()); //TODO rename Entity Look on the minecraft protocol page
		/* 0x21 */
		registerPacket(EntityRelativePositionYawCodec.class, new EntityRelativePositionYawHandler());  //TODO same as above
		/* 0x22 */
		registerPacket(EntityTeleportCodec.class, new EntityTeleportHandler());
		/* 0x23 */
		registerPacket(EntityHeadYawCodec.class, new EntityHeadYawHandler()); //TODO same as above
		/* 0x26 */
		registerPacket(EntityStatusCodec.class, new EntityStatusHandler());
		/* 0x27 */
		registerPacket(EntityAttachEntityCodec.class, new EntityAttachEntityHandler());
		/* 0x28 */
		registerPacket(EntityMetadataCodec.class, new EntityMetadataHandler());
		/* 0x29 */
		registerPacket(EntityEffectCodec.class, new EntityEffectHandler());
		/* 0x2A */
		registerPacket(EntityRemoveEffectCodec.class, new EntityRemoveEffectHandler());
		/* 0x2B */
		registerPacket(PlayerExperienceCodec.class, new PlayerExperienceHandler());
		/* 0x2C */
		registerPacket(EntityPropertiesCodec.class, new EntityPropertiesHandler());
		/* 0x33 */
		registerPacket(ChunkDataCodec.class, new ChunkDataHandler()); //TODO rename on the minecraft protocol page
		/* 0x34 */
		registerPacket(BlockBulkCodec.class, new BlockBulkHandler());
		/* 0x35 */
		registerPacket(BlockChangeCodec.class, new BlockChangeHandler());
		/* 0x36 */
		registerPacket(BlockActionCodec.class, new BlockActionHandler());
		/* 0x37 */
		registerPacket(BlockBreakAnimationCodec.class, new BlockBreakAnimationHandler());
		/* 0x38 */
		registerPacket(ChunkBulkCodec.class, new ChunkBulkHandler());
		/* 0x3C */
		registerPacket(ExplosionCodec.class, new ExplosionHandler());
		/* 0x3D */
		registerPacket(EffectCodec.class, new EffectHandler());
		/* 0x3E */
		registerPacket(SoundEffectCodec.class, new SoundEffectHandler());
		/* 0x3F */
		registerPacket(ParticleEffectCodec.class, new ParticleEffectHandler());
		/* 0x46 */
		registerPacket(PlayerGameStateCodec.class, new PlayerGameStateHandler());
		/* 0x47 */
		registerPacket(EntityThunderboltCodec.class, new EntityThunderboltHandler()); //Minecraft protocol page -> Thunderbolt :/
		/* 0x64 */
		registerPacket(WindowOpenCodec.class, new WindowOpenHandler());
		/* 0x65 */
		registerPacket(WindowCloseCodec.class, new WindowCloseHandler());
		/* 0x66 */
		registerPacket(WindowClickCodec.class, new WindowClickHandler());
		/* 0x67 */
		registerPacket(WindowSlotCodec.class, new WindowSlotHandler());
		/* 0x68 */
		registerPacket(WindowItemsCodec.class, new WindowItemsHandler());
		/* 0x69 */
		registerPacket(WindowPropertyCodec.class, new WindowPropertyHandler()); //Update Window Property on the protocol page!
		/* 0x6A */
		registerPacket(WindowTransactionCodec.class, new WindowTransactionHandler());
		/* 0x6B */
		registerPacket(WindowCreativeActionCodec.class, new WindowCreativeActionHandler());
		/* 0x6C */
		registerPacket(WindowEnchantItemCodec.class, new WindowEnchantItemHandler());
		/* 0x82 */
		registerPacket(SignCodec.class, new SignHandler());
		/* 0x83 */
		registerPacket(EntityItemDataCodec.class, new EntityItemDataHandler());
		/* 0x84 */
		registerPacket(EntityTileDataCodec.class, new EntityTileDataHandler()); //Update Tile Entity...
		/* 0xC8 */
		registerPacket(PlayerStatisticCodec.class, new PlayerStatisticHandler());
		/* 0xC9 */
		registerPacket(PlayerListCodec.class, new PlayerListHandler());
		/* 0xCA */
		registerPacket(PlayerAbilityCodec.class, new PlayerAbilityHandler());
		/* 0xCB */
		registerPacket(PlayerTabCompleteCodec.class, new PlayerTabCompleteHandler());
		/* 0xCC */
		registerPacket(PlayerLocaleViewDistanceCodec.class, new PlayerLocaleViewDistanceHandler());
		/* 0xCD */
		registerPacket(PlayerStatusCodec.class, new PlayerStatusHandler());
		/* 0xCE */
		registerPacket(ScoreboardObjectiveCodec.class, new ScoreboardObjectiveHandler());
		/* 0xCF */
		registerPacket(ScoreboardScoreCodec.class, new ScoreboardScoreHandler());
		/* 0xD0 */
		registerPacket(ScoreboardDisplayCodec.class, new ScoreboardDisplayHandler());
		/* 0xD1 */
		registerPacket(ScoreboardTeamCodec.class, new ScoreboardTeamHandler());
		/* 0xFA */
		registerPacket(ServerPluginCodec.class, new ServerPluginHandler());
		/* 0xFC */
		registerPacket(EncryptionKeyResponseCodec.class, new EncryptionKeyResponseHandler());
		/* 0xFD */
		registerPacket(EncryptionKeyRequestCodec.class, new EncryptionKeyRequestHandler());
		/* 0xFE */
		registerPacket(ServerListPingCodec.class, new ServerListPingHandler());
		/* 0xFF */
		registerPacket(PlayerKickCodec.class, new PlayerKickHandler());
		/* PacketFA wrapped packets */
		registerPacket(RegisterPluginChannelCodec.class, new RegisterPluginChannelMessageHandler());
		registerPacket(UnregisterPluginChannelCodec.class, new UnregisterPluginChannelMessageHandler());
		registerPacket(ChunkCacheCodec.class, new ChunkCacheHandler());
		registerPacket(CommandBlockCodec.class, new CommandBlockHandler());
		registerPacket(BeaconCodec.class, new BeaconHandler());
	}

	@Override
	public Message getCommandMessage(Command command, CommandArguments args) {
		try {
			if (command.getName().equals("kick")) {
				return getKickMessage(args.popRemainingStrings("message"));
			} else if (command.getName().equals("say")) {
				return new PlayerChatMessage(args.popRemainingStrings("message") + "\u00a7r"); // The reset text is a workaround for a change in 1.3 -- Remove if fixed
			} else {
				return new PlayerChatMessage('/' + command.getName() + ' ' + args.popRemainingStrings("message"));
			}
		} catch (ArgumentParseException ex) {
			return new PlayerChatMessage(ChatStyle.RED + ex.getMessage());
		}
	}

	@Override
	@SuppressWarnings ("unchecked")
	public <T extends Message> Message getWrappedMessage(boolean upstream, T dynamicMessage) throws IOException {
		MessageCodec<T> codec = (MessageCodec<T>) getCodecLookupService().find(dynamicMessage.getClass());
		ChannelBuffer buffer = codec.encode(upstream, dynamicMessage);

		return new ServerPluginMessage(getName(codec), buffer.array());
	}

	@Override
	public MessageCodec<?> readHeader(ChannelBuffer buf) throws UnknownPacketException {
		int opcode = buf.readUnsignedByte();
		MessageCodec<?> codec = getCodecLookupService().find(opcode);
		if (codec == null) {
			throw new UnknownPacketException(opcode);
		}
		return codec;
	}

	@Override
	public ChannelBuffer writeHeader(MessageCodec<?> codec, ChannelBuffer data) {
		ChannelBuffer buffer = ChannelBuffers.buffer(1);
		buffer.writeByte(codec.getOpcode());
		return buffer;
	}

	@Override
	public Message getKickMessage(String message) {
		return new PlayerKickMessage(message);
	}

	@Override
	public Message getIntroductionMessage(String playerName, InetSocketAddress addr) {
		return new PlayerHandshakeMessage((byte) VanillaPlugin.MINECRAFT_PROTOCOL_ID, VanillaPlugin.getInstance().getUsername(), addr.getHostName(), addr.getPort());
	}

	public static MessageCodec<?> getCodec(String name, Protocol activeProtocol) {
		for (Pair<Integer, String> item : activeProtocol.getDynamicallyRegisteredPackets()) {
			MessageCodec<?> codec = activeProtocol.getCodecLookupService().find(item.getLeft());
			if (getName(codec).equalsIgnoreCase(name)) {
				return codec;
			}
		}
		return null;
	}

	public static String getName(MessageCodec<?> codec) {
		if (codec instanceof Named) {
			return ((Named) codec).getName();
		} else {
			return "SPOUT:" + codec.getOpcode();
		}
	}

	@Override
	public Class<? extends PlayerNetworkComponent> getServerNetworkComponent(ServerSession session) {
		return VanillaPlayerNetworkComponent.class;
	}

	@Override
	public Class<? extends PlayerNetworkComponent> getClientNetworkComponent(ClientSession session) {
		return VanillaPlayerNetworkComponent.class;
	}

	@Override
	public void initializeServerSession(ServerSession session) {
		List<MessageCodec<?>> dynamicCodecList = new ArrayList<MessageCodec<?>>();
		for (Pair<Integer, String> item : getDynamicallyRegisteredPackets()) {
			MessageCodec<?> codec = getCodecLookupService().find(item.getLeft());
			if (codec != null) {
				dynamicCodecList.add(codec);
			} else {
				throw new IllegalStateException("Dynamic packet class" + item.getRight() + " claims to be registered but is not in our CodecLookupService!");
			}
		}

		session.send(new RegisterPluginChannelMessage(dynamicCodecList));
	}

	@Override
	public void initializeClientSession(ClientSession session) {
		List<MessageCodec<?>> dynamicCodecList = new ArrayList<MessageCodec<?>>();
		for (Pair<Integer, String> item : getDynamicallyRegisteredPackets()) {
			MessageCodec<?> codec = getCodecLookupService().find(item.getLeft());
			if (codec != null) {
				dynamicCodecList.add(codec);
			} else {
				throw new IllegalStateException("Dynamic packet class" + item.getRight() + " claims to be registered but is not in our CodecLookupService!");
			}
		}
		session.send(new RegisterPluginChannelMessage(dynamicCodecList));
	}
}
