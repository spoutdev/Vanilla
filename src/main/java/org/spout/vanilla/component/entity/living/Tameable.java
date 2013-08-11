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
package org.spout.vanilla.component.entity.living;

/**
 * An Entity that may/can be tamed and has an owner after being tamed
 */
public interface Tameable {
	/**
	 * Gets whether this Entity is tamed
	 * 
	 * @return True if tamed, False if not (and wild)
	 */
	public boolean isTamed();

	/**
	 * Sets whether this Entity is tamed
	 * 
	 * @param tamed state to set to
	 */
	public void setTamed(boolean tamed);

	/**
	 * Gets whether this Entity can actually be tamed (right now)
	 * 
	 * @return True if it can be tamed, False if not
	 */
	public boolean canBeTamed();

	/**
	 * Gets the name of the (Player) owner of this Entity
	 * 
	 * @return Name of the owner
	 */
	public String getOwnerName();

	/**
	 * Sets the name of the (Player) owner of this Entity
	 * 
	 * @param owner to set to
	 */
	public void setOwnerName(String owner);
}
