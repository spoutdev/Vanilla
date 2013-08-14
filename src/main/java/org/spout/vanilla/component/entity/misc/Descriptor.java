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
package org.spout.vanilla.component.entity.misc;

import org.spout.api.geo.discrete.Point;
import org.spout.vanilla.component.entity.VanillaEntityComponent;

/**
 * Creates a description of entities (mainly to help debugging them)
 */
public class Descriptor extends VanillaEntityComponent {
	private String name = "Unknown";

	/**
	 * Gets the name of the Entity
	 * 
	 * @return entity name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the Entity
	 * 
	 * @param name of the Entity to set to
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Generates a small description that helps identify where and what this Entity is
	 * 
	 * @return Entity description
	 */
	public String getDescription() {
		Point pos = getOwner().getPhysics().getPosition();
		StringBuilder desc = new StringBuilder();
		desc.append(getName()).append(' ');
		desc.append("[x=").append(pos.getBlockX());
		desc.append(",y=").append(pos.getBlockY());
		desc.append(",z=").append(pos.getBlockZ());
		desc.append(",ID=").append(getOwner().getId());
		return desc.toString();
	}
}
