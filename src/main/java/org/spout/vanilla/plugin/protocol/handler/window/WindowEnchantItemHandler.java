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
package org.spout.vanilla.plugin.protocol.handler.window;

import org.spout.api.entity.Player;
import org.spout.api.protocol.MessageHandler;
import org.spout.api.protocol.Session;

import org.spout.vanilla.api.enchantment.Enchantment;
import org.spout.vanilla.plugin.component.inventory.WindowHolder;
import org.spout.vanilla.plugin.enchantment.VanillaEnchantments;
import org.spout.vanilla.plugin.inventory.block.EnchantmentTableInventory;
import org.spout.vanilla.plugin.protocol.msg.window.WindowEnchantItemMessage;

public class WindowEnchantItemHandler extends MessageHandler<WindowEnchantItemMessage> {
	@Override
	public void handleServer(Session session, WindowEnchantItemMessage message) {
		if (!session.hasPlayer()) {
			return;
		}
		Player player = session.getPlayer();
		EnchantmentTableInventory inv = (EnchantmentTableInventory) player.get(WindowHolder.class).getActiveWindow().getInventoryConverters().get(2).getInventory();
		int enchantSlot = message.getEnchantment();
		int enchantLevel = inv.getEnchantmentLevel(enchantSlot);
		System.out.println("Enchantment slot: " + enchantSlot);
		System.out.println("Enchantment level: " + enchantLevel);

		Enchantment.addEnchantment(inv.get(), VanillaEnchantments.UNBREAKING, true);
		// TODO: Use enchantment level to calculate enchantments for an item
	}
}
