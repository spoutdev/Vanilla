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
package org.spout.vanilla.protocol.entity.object;

import java.util.ArrayList;
import java.util.List;

import org.spout.api.entity.Entity;
import org.spout.api.inventory.ItemStack;
import org.spout.api.material.Material;
import org.spout.api.protocol.Message;
import org.spout.api.protocol.reposition.RepositionManager;
import org.spout.api.util.Parameter;

import org.spout.vanilla.component.entity.substance.ItemFrame;
import org.spout.vanilla.protocol.VanillaChannelBufferUtils;
import org.spout.vanilla.protocol.msg.entity.EntityMetadataMessage;
import org.spout.vanilla.protocol.msg.entity.spawn.EntityObjectMessage;

public class ItemFrameProtocol extends ObjectEntityProtocol {
	public ItemFrameProtocol() {
		super(ObjectType.ITEM_FRAME);
	}

	@Override
	public List<Message> getSpawnMessages(Entity entity, RepositionManager rm) {
		List<Message> msgs = new ArrayList<Message>();
		msgs.add(new EntityObjectMessage(entity, (byte) typeId, VanillaChannelBufferUtils.getNativeDirection(entity.add(ItemFrame.class).getOrientation()), rm));
		msgs.add(new EntityMetadataMessage(entity.getId(), getSpawnParameters(entity)));
		return msgs;
	}

	@Override
	public List<Parameter<?>> getSpawnParameters(Entity entity) {
		List<Parameter<?>> params = super.getSpawnParameters(entity);
		ItemFrame frame = entity.add(ItemFrame.class);
		Material mat = frame.getMaterial();
		params.add(new Parameter<ItemStack>(Parameter.TYPE_ITEM, 2, mat == null ? null : new ItemStack(mat, 1)));
		System.out.println("Face: " + frame.getOrientation());
		return params;
	}
}
