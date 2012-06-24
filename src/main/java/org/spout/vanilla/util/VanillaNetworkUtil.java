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
package org.spout.vanilla.util;

import java.util.ArrayList;
import java.util.Set;

import org.spout.api.Spout;
import org.spout.api.entity.Entity;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.discrete.Point;
import org.spout.api.player.Player;
import org.spout.api.protocol.Message;

import org.spout.vanilla.protocol.msg.BlockActionMessage;
import org.spout.vanilla.protocol.msg.PlayEffectMessage;

public class VanillaNetworkUtil {
	public static int BLOCK_EFFECT_RANGE = 16;

	/**
	 * This method takes any amount of messages and sends them to every online player on the server.
	 * @param messages
	 */
	public static void broadcastPacket(Message... messages) {
		sendPacket(Spout.getEngine().getOnlinePlayers(), messages);
	}

	/**
	 * This method takes in any amount of messages and sends them to any amount of
	 * players.
	 * @param players specific players to send a message to.
	 * @param messages the message(s) to send
	 */
	public static void sendPacket(Player[] players, Message... messages) {
		for (Player player : players) {
			for (Message message : messages) {
				sendPacket(player, message);
			}
		}
	}

	/**
	 * This method takes any amount of messages and sends them to all players except the collection of
	 * ignored players specified.
	 * @param ignore Players to ignore when sending messages
	 * @param messages Messages to send
	 */
	public static void broadcastPacket(Player[] ignore, Message... messages) {
		ArrayList<Player> toSend = new ArrayList<Player>();
		for (Player player : Spout.getEngine().getOnlinePlayers()) {
			for (Player ignored : ignore) {
				if (player.equals(ignored)) {
					continue;
				}
				toSend.add(player);
			}
		}
		sendPacket(toSend.toArray(new Player[toSend.size()]), messages);
	}

	/**
	 * This method takes in a message and sends it to a specific player
	 * @param player specific player to relieve message
	 * @param messages specific message to send.
	 */
	public static void sendPacket(Player player, Message... messages) {
		for (Message message : messages) {
			player.getSession().send(message);
		}
	}

	/**
	 * This method sends an effect play message for a block to all nearby players in a 16-block radius
	 * @param block The block that the effect comes from.
	 * @param effect The effect to play
	 */
	public static void playBlockEffect(Block block, Entity ignore, PlayEffectMessage.Messages effect) {
		playBlockEffect(block, ignore, BLOCK_EFFECT_RANGE, effect, 0);
	}

	/**
	 * This method sends an effect play message for a block to all nearby players in a 16-block radius
	 * @param block The block that the effect comes from.
	 * @param effect The effect to play
	 * @param data The data to use for the effect
	 */
	public static void playBlockEffect(Block block, Entity ignore, PlayEffectMessage.Messages effect, int data) {
		sendPacketsToNearbyPlayers(block.getPosition(), ignore, BLOCK_EFFECT_RANGE, new PlayEffectMessage(effect.getId(), block, data));
	}

	/**
	 * This method sends an effect play message for a block to all nearby players
	 * @param block The block that the effect comes from.
	 * @param range The range (circular) from the entity in-which the nearest player should be searched for.
	 * @param effect The effect to play
	 */
	public static void playBlockEffect(Block block, Entity ignore, int range, PlayEffectMessage.Messages effect) {
		playBlockEffect(block, ignore, range, effect, 0);
	}

	/**
	 * This method sends an effect play message for a block to all nearby players
	 * @param block The block that the effect comes from.
	 * @param range The range (circular) from the entity in-which the nearest player should be searched for.
	 * @param effect The effect to play
	 * @param data The data to use for the effect
	 */
	public static void playBlockEffect(Block block, Entity ignore, int range, PlayEffectMessage.Messages effect, int data) {
		sendPacketsToNearbyPlayers(block.getPosition(), ignore, range, new PlayEffectMessage(effect.getId(), block, data));
	}

	/**
	 * Sends a block action message to all nearby players in a 48-block radius
	 */
	public static void playBlockAction(Block block, byte arg1, byte arg2) {
		sendPacketsToNearbyPlayers(block.getPosition(), 48, new BlockActionMessage(block, arg1, arg2));
	}

	/**
	 * Sends a block action message to all nearby players
	 */
	public static void playBlockAction(Block block, int range, byte arg1, byte arg2) {
		sendPacketsToNearbyPlayers(block.getPosition(), range, new BlockActionMessage(block, arg1, arg2));
	}

	/**
	 * This method sends any amount of packets to all nearby players of a position (within a specified range).
	 * @param position The position that the packet relates to. It will be used as the central point to send packets in a range from.
	 * @param range The range (circular) from the entity in-which the nearest player should be searched for.
	 * @param messages The messages that should be sent to the discovered nearest player.
	 */
	public static void sendPacketsToNearbyPlayers(Point position, Entity ignore, int range, Message... messages) {
		Set<Player> players = position.getWorld().getNearbyPlayers(position, ignore, range);
		for (Player plr : players) {
			plr.getSession().sendAll(messages);
		}
	}

	/**
	 * This method sends any amount of packets to all nearby players of a position (within a specified range).
	 * @param position The position that the packet relates to. It will be used as the central point to send packets in a range from.
	 * @param range The range (circular) from the entity in-which the nearest player should be searched for.
	 * @param messages The messages that should be sent to the discovered nearest player.
	 */
	public static void sendPacketsToNearbyPlayers(Point position, int range, Message... messages) {
		Set<Player> players = position.getWorld().getNearbyPlayers(position, range);
		for (Player plr : players) {
			plr.getSession().sendAll(messages);
		}
	}

	/**
	 * This method sends any amount of packets to all nearby players of an entity (within a specified range).
	 * @param entity The entity that the packet relates to. It will be used as the central point to send packets in a range from.
	 * @param range The range (circular) from the entity in-which the nearest player should be searched for.
	 * @param messages The messages that should be sent to the discovered nearest player.
	 */
	public static void sendPacketsToNearbyPlayers(Entity entity, int range, Message... messages) {
		if (entity == null || entity.getRegion() == null) {
			return;
		}
		Set<Player> players = entity.getWorld().getNearbyPlayers(entity, range);
		for (Player plr : players) {
			plr.getSession().sendAll(messages);
		}
	}

	/**
	 * This method sends any amount of packets and sends them to the nearest player from the entity specified.
	 * @param entity The entity that the packet relates to. It will be used as the central point to send packets in a range from.
	 * @param range The range (circular) from the entity in-which the nearest player should be searched for.
	 * @param messages The messages that should be sent to the discovered nearest player.
	 */
	public static void sendPacketsToNearestPlayer(Entity entity, int range, Message... messages) {
		if (entity == null || entity.getRegion() == null) {
			return;
		}

		Player plr = entity.getWorld().getNearestPlayer(entity, range);
		//Only send if we have a player nearby.
		if (plr != null) {
			plr.getSession().sendAll(messages);
		}
	}
}
