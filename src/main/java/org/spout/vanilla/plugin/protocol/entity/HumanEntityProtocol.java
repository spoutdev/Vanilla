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
package org.spout.vanilla.plugin.protocol.entity;

import java.util.ArrayList;
import java.util.List;

import org.spout.api.entity.Entity;
import org.spout.api.inventory.ItemStack;
import org.spout.api.protocol.Message;
import org.spout.api.protocol.reposition.RepositionManager;
import org.spout.api.util.Parameter;

import org.spout.vanilla.api.inventory.Slot;

import org.spout.vanilla.plugin.component.inventory.PlayerInventory;
import org.spout.vanilla.plugin.component.living.neutral.Human;
import org.spout.vanilla.plugin.inventory.entity.EntityArmorInventory;
import org.spout.vanilla.plugin.protocol.ChannelBufferUtils;
import org.spout.vanilla.plugin.protocol.msg.entity.EntityEquipmentMessage;
import org.spout.vanilla.plugin.protocol.msg.player.pos.PlayerSpawnMessage;
import org.spout.vanilla.plugin.util.PlayerUtil;

public class HumanEntityProtocol extends VanillaEntityProtocol {
	@Override
	public List<Message> getSpawnMessages(Entity entity, RepositionManager rm) {

		List<Message> messages = new ArrayList<Message>(6);

		Human human = entity.add(Human.class);

		int id = entity.getId();
		int x = ChannelBufferUtils.protocolifyPosition(rm.convertX(entity.getScene().getPosition().getX()));
		int y = ChannelBufferUtils.protocolifyPosition(rm.convertY(entity.getScene().getPosition().getY()));
		int z = ChannelBufferUtils.protocolifyPosition(rm.convertZ(entity.getScene().getPosition().getZ()));
		int r = ChannelBufferUtils.protocolifyYaw(entity.getScene().getRotation().getYaw());
		int p = ChannelBufferUtils.protocolifyPitch(entity.getScene().getRotation().getPitch());

		int item = 0;
		Slot hand = PlayerUtil.getHeldSlot(entity);
		if (hand != null && hand.get() != null) {
			item = hand.get().getMaterial().getId();
		}
		List<Parameter<?>> parameters = new ArrayList<Parameter<?>>();
		parameters.add(new Parameter<Short>(Parameter.TYPE_SHORT, 1, (short) 100));

		messages.add(new PlayerSpawnMessage(id, human.getName(), x, y, z, r, p, item, parameters));

		// Armor
		PlayerInventory inventory = entity.get(PlayerInventory.class);
		final ItemStack boots, leggings, chestplate, helmet, held;
		if (inventory == null) {
			boots = leggings = chestplate = helmet = held = null;
		} else {
			final EntityArmorInventory armor = inventory.getArmor();
			boots = armor.getBoots();
			leggings = armor.getLeggings();
			chestplate = armor.getChestPlate();
			helmet = armor.getHelmet();
			held = inventory.getQuickbar().getSelectedSlot().get();
		}
		if (held != null) {
			messages.add(new EntityEquipmentMessage(entity.getId(), EntityEquipmentMessage.HELD_SLOT, held));
		}
		messages.add(new EntityEquipmentMessage(entity.getId(), EntityEquipmentMessage.BOOTS_SLOT, boots));
		messages.add(new EntityEquipmentMessage(entity.getId(), EntityEquipmentMessage.LEGGINGS_SLOT, leggings));
		messages.add(new EntityEquipmentMessage(entity.getId(), EntityEquipmentMessage.CHESTPLATE_SLOT, chestplate));
		messages.add(new EntityEquipmentMessage(entity.getId(), EntityEquipmentMessage.HELMET_SLOT, helmet));

		return messages;
	}
}
