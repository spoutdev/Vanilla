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
package org.spout.vanilla.entity.component.basic;

import java.util.ArrayList;
import java.util.List;

import org.spout.api.entity.BasicComponent;
import org.spout.api.tickable.TickPriority;
import org.spout.api.util.Parameter;

import org.spout.vanilla.entity.VanillaEntityController;
import org.spout.vanilla.event.entity.EntityMetaChangeEvent;

public class MetaDataComponent extends BasicComponent<VanillaEntityController> {
	private List<Parameter<?>> parameters = new ArrayList<Parameter<?>>();
	private boolean dirty = false;

	public MetaDataComponent(TickPriority priority) {
		super(priority);
	}

	public void setData(List<Parameter<?>> parameters) {
		this.parameters = parameters;
		dirty = true;
	}

	public List<Parameter<?>> getData() {
		return parameters;
	}

	public void addData(Parameter<?> parameter) {
		parameters.add(parameter);
		dirty = true;
	}

	@Override
	public boolean canTick() {
		return dirty;
	}

	@Override
	public void onTick(float dt) {
		dirty = false;
		getParent().callProtocolEvent(new EntityMetaChangeEvent(getParent().getParent(), getData()));
	}
}
