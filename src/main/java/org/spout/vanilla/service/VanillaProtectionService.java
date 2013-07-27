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
package org.spout.vanilla.service;

import java.util.ArrayList;
import java.util.Collection;

import org.spout.api.geo.Protection;
import org.spout.api.geo.World;
import org.spout.api.geo.discrete.Point;
import org.spout.api.plugin.services.ProtectionService;

public class VanillaProtectionService extends ProtectionService {
	final ArrayList<Protection> protections = new ArrayList<Protection>();

	/**
	 * Returns the protection registered that matches the name provided
	 * @param name Name of the desired protection one is seeking
	 * @return The protection or null if not found
	 */
	@Override
	public Protection getProtection(String name) {
		Protection p = null;
		for (Protection registered : protections) {
			if (registered.getName().equals(name)) {
				p = registered;
				break;
			}
		}
		return p;
	}

	/**
	 * Gets all protections registered with the world provided
	 * @param world The world to lookup
	 * @return A collection of protections or an empty list otherwise
	 */
	@Override
	public Collection<Protection> getAllProtections(World world) {
		ArrayList<Protection> worldProtections = new ArrayList<Protection>();

		for (Protection p : protections) {
			if (p.getWorld().equals(world)) {
				worldProtections.add(p);
			}
		}

		return worldProtections;
	}

	/**
	 * Gets all protections registered to a point
	 * @param point The point to lookup
	 * @return A collection of protections or an empty list otherwise
	 */
	@Override
	public Collection<Protection> getAllProtections(Point point) {
		ArrayList<Protection> pointProtections = new ArrayList<Protection>();

		for (Protection p : protections) {
			if (p.contains(point)) {
				pointProtections.add(p);
			}
		}

		return pointProtections;
	}

	/**
	 * Gets all protections registered under this Protection Service
	 * @return A collection of protections or an empty list otherwise
	 */
	@Override
	public Collection<Protection> getAllProtections() {
		return protections;
	}

	/**
	 * Add a protection to the Protection Service
	 * @param protection the protection to add
	 */
	public void addProtection(Protection protection) {
		if (!protections.contains(protection)) {
			protections.add(protection);
		}
	}
}


