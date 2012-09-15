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
package org.spout.vanilla.protocol.entity.living;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.spout.api.entity.Entity;
import org.spout.api.inventory.ItemStack;
import org.spout.api.protocol.Message;
import org.spout.api.util.Parameter;

import org.spout.vanilla.components.living.Human;
import org.spout.vanilla.components.player.PlayerInventory;
import org.spout.vanilla.protocol.entity.VanillaEntityProtocol;
import org.spout.vanilla.protocol.msg.player.pos.PlayerSpawnMessage;

public class HumanEntityProtocol extends VanillaEntityProtocol {
	@Override
	public List<Message> getSpawnMessages(Entity entity) {
		Human human = entity.add(Human.class);

		int id = entity.getId();
		int x = (int) (entity.getTransform().getPosition().getX() * 32);
		int y = (int) (entity.getTransform().getPosition().getY() * 32);
		int z = (int) (entity.getTransform().getPosition().getZ() * 32);
		int r = (int) (-entity.getTransform().getYaw() * 32);
		int p = (int) (entity.getTransform().getPitch() * 32);

		int item = 0;
		if (entity.has(PlayerInventory.class)) {
			ItemStack hand = entity.get(PlayerInventory.class).getQuickbar().getCurrentItem();
			if (hand != null) {
				item = hand.getMaterial().getId();
			}
		}
		List<Parameter<?>> parameters = new ArrayList<Parameter<?>>();
		parameters.add(new Parameter<Short>(Parameter.TYPE_SHORT, 1, (short) 100));
		return Arrays.<Message>asList(new PlayerSpawnMessage(id, human.getName(), x, y, z, r, p, item, parameters));
	}
}
