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
package org.spout.vanilla.ai.sensor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.spout.api.ai.Sensor;
import org.spout.api.ai.goap.PlannerAgent;
import org.spout.api.ai.goap.WorldState;
import org.spout.api.entity.Player;
import org.spout.api.inventory.ItemStack;

import org.spout.vanilla.inventory.entity.QuickbarInventory;
import org.spout.vanilla.material.VanillaMaterial;
import org.spout.vanilla.util.PlayerUtil;

/**
 * A sensor that finds {@link Player}s holding a specified {@link VanillaMaterial}.
 */
public class NearbyMaterialHolderSensor implements Sensor {
	private PlannerAgent agent;
	private VanillaMaterial material;
	private WorldState state;
	private int sensorRadius;

	public NearbyMaterialHolderSensor(PlannerAgent agent, VanillaMaterial material) {
		this.agent = agent;
		this.material = material;
		this.state = WorldState.createEmptyState();
	}

	@Override
	public WorldState generateState() {
		List<Player> players = new ArrayList<Player>();
		for (Player player : agent.getEntity().getWorld().getNearbyPlayers(agent.getEntity(), sensorRadius)) {
			QuickbarInventory quickbar = PlayerUtil.getQuickbar(player);
			if (quickbar == null) {
				continue;
			}
			ItemStack selectedItem = quickbar.getSelectedItem();
			if (selectedItem != null && material.isMaterial(selectedItem.getMaterial())) {
				players.add(player);
			}
		}
		boolean found = players.size() > 0;
		state.put("hasNearbyMaterialHolders", found);
		state.put("players", found ? players : Collections.emptyList());
		return state;
	}

	public int getSensorRadius() {
		return sensorRadius;
	}

	public void setSensorRadius(int sensorRadius) {
		this.sensorRadius = sensorRadius;
	}

	public List<Player> getPlayers() {
		final List<Player> targets = state.get("players");
		return targets == null ? new ArrayList<Player>() : targets;
	}

	public boolean hasFoundPlayers() {
		return (Boolean) state.get("hasNearbyMaterialHolders");
	}
}
