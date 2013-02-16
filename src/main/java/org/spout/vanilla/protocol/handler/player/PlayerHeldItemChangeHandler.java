/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, Spout LLC <http://www.spout.org/>
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
package org.spout.vanilla.protocol.handler.player;

import org.spout.api.Spout;
import org.spout.api.entity.Player;
import org.spout.api.protocol.MessageHandler;
import org.spout.api.protocol.Session;

import org.spout.vanilla.api.event.player.PlayerHeldItemChangeEvent;
import org.spout.vanilla.api.event.player.network.PlayerSelectedSlotChangeEvent;
import org.spout.vanilla.inventory.entity.QuickbarInventory;

import org.spout.vanilla.plugin.component.living.neutral.Human;
import org.spout.vanilla.protocol.msg.player.PlayerHeldItemChangeMessage;
import org.spout.vanilla.plugin.util.PlayerUtil;

public final class PlayerHeldItemChangeHandler extends MessageHandler<PlayerHeldItemChangeMessage> {
	@Override
	public void handleServer(Session session, PlayerHeldItemChangeMessage message) {
		if (!session.hasPlayer()) {
			return;
		}
		Human human = session.getPlayer().get(Human.class);
		if (human == null) {
			return;
		}
		final int newSlot = message.getSlot();
		if (newSlot < 0 || newSlot > 8) {
			return;
		}
		Player player = session.getPlayer();
		QuickbarInventory quickbar = PlayerUtil.getQuickbar(session.getPlayer());
		if (quickbar == null) {
			return;
		}
		PlayerHeldItemChangeEvent event = new PlayerHeldItemChangeEvent(player, quickbar.getSelectedSlot().getIndex(), newSlot);
		if (Spout.getEngine().getEventManager().callEvent(event).isCancelled()) {
			// Reset
			player.getNetworkSynchronizer().callProtocolEvent(new PlayerSelectedSlotChangeEvent(session.getPlayer(), event.getPreviousSlot()));
		} else {
			quickbar.setSelectedSlot(event.getNewSlot());
			quickbar.updateHeldItem(player);
			// Changed slot by event handler
			if (newSlot != event.getNewSlot()) {
				player.getNetworkSynchronizer().callProtocolEvent(new PlayerSelectedSlotChangeEvent(session.getPlayer(), event.getNewSlot()));
			}
		}
	}

	@Override
	public void handleClient(Session session, PlayerHeldItemChangeMessage message) {
		QuickbarInventory quickbar = PlayerUtil.getQuickbar(session.getPlayer());
		if (quickbar == null) {
			return;
		}
		quickbar.setSelectedSlot(message.getSlot());
	}
}
