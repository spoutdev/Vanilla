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
package org.spout.vanilla.event.entity;

import java.util.List;

import org.spout.api.entity.Entity;
import org.spout.api.event.HandlerList;
import org.spout.api.event.ProtocolEvent;
import org.spout.api.util.Parameter;

/**
 * Event which is called when an Entity changes meta-data
 */
public class EntityMetaChangeEvent extends ProtocolEvent {
	private static final HandlerList handlers = new HandlerList();
	private final List<Parameter<?>> parameters;
	private final Entity entity;

	public EntityMetaChangeEvent(Entity e, List<Parameter<?>> parameters) {
		this.entity = e;
		this.parameters = parameters;
	}

	/**
	 * Gets the updates meta data parameters of the Entity
	 * @return parameter list
	 */
	public List<Parameter<?>> getParameters() {
		return parameters;
	}

	/**
	 * Gets the entity associated with this event.
	 *
	 * @return The entity associated with the event.
	 */
	public Entity getEntity() {
		return entity;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
