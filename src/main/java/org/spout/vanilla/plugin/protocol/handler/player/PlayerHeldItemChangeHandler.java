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
package org.spout.vanilla.plugin.protocol.handler.player;

import org.spout.api.Client;
import org.spout.api.Spout;
import org.spout.api.entity.Player;
import org.spout.api.inventory.ItemStack;
import org.spout.api.protocol.MessageHandler;
import org.spout.api.protocol.Session;

import org.spout.vanilla.plugin.component.inventory.PlayerInventory;
import org.spout.vanilla.plugin.component.living.neutral.Human;
import org.spout.vanilla.plugin.event.entity.EntityEquipmentEvent;
import org.spout.vanilla.plugin.event.player.PlayerHeldItemChangeEvent;
import org.spout.vanilla.plugin.inventory.player.PlayerQuickbar;
import org.spout.vanilla.plugin.protocol.msg.player.PlayerHeldItemChangeMessage;

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
		int newSlot = message.getSlot();
		if (newSlot < 0 || newSlot > 8) {
			return;
		}
		Player player = session.getPlayer();
		PlayerQuickbar quickbar = session.getPlayer().add(PlayerInventory.class).getQuickbar();
		PlayerHeldItemChangeEvent event = new PlayerHeldItemChangeEvent(player, quickbar.getCurrentSlot(), newSlot);
		if (!Spout.getEngine().getEventManager().callEvent(event).isCancelled()) {
			quickbar.setCurrentSlot(newSlot);
			ItemStack item = quickbar.getCurrentItem();
			player.getNetwork().callProtocolEvent(new EntityEquipmentEvent(player, 0, item));
		}
	}

	@Override
	public void handleClient(Session session, PlayerHeldItemChangeMessage message) {
		Player player = ((Client) Spout.getEngine()).getActivePlayer();
		player.get(PlayerInventory.class).getQuickbar().setCurrentSlot(message.getSlot());
	}
}
