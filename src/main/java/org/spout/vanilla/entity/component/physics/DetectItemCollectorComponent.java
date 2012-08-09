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
package org.spout.vanilla.entity.component.physics;

import org.spout.api.entity.BasicComponent;
import org.spout.api.entity.Player;
import org.spout.api.tickable.TickPriority;

import org.spout.vanilla.configuration.VanillaConfiguration;
import org.spout.vanilla.entity.VanillaPlayerController;
import org.spout.vanilla.entity.object.moving.Item;
import org.spout.vanilla.data.VanillaData;
import org.spout.vanilla.protocol.msg.entity.EntityCollectItemMessage;

import static org.spout.vanilla.util.VanillaNetworkUtil.sendPacketsToNearbyPlayers;

public class DetectItemCollectorComponent extends BasicComponent<Item> {
	private final int DISTANCE = VanillaConfiguration.ITEM_PICKUP_RANGE.getInt();
	private Player player = null;
	private int unCollectibleTicks;

	public DetectItemCollectorComponent(TickPriority priority) {
		super(priority);
	}

	@Override
	public void onAttached() {
		unCollectibleTicks = getParent().getDataMap().get(VanillaData.UNCOLLECTABLE_TICKS);
	}

	@Override
	public boolean canTick() {
		if (unCollectibleTicks > 0) {
			unCollectibleTicks--;
			return false;
		}
		Player closestPlayer = getParent().getParent().getWorld().getNearestPlayer(getParent().getParent(), DISTANCE);
		if (closestPlayer == null) {
			return false;
		}

		player = closestPlayer;
		return true;
	}

	@Override
	public void onTick(float dt) {
		int collected = getParent().getParent().getId();
		int collector = player.getId();
		//TODO Put this in VNS
		sendPacketsToNearbyPlayers(player.getPosition(), player.getViewDistance(), new EntityCollectItemMessage(collected, collector));
		//TODO Handle other controllers within other protocols
		if (player.getController() instanceof VanillaPlayerController) {
			((VanillaPlayerController) player.getController()).getInventory().getMain().addItem(getParent().getItemStack(), true, true);
		}
		getParent().kill();
	}

	public int getUnCollectibleTicks() {
		return unCollectibleTicks;
	}
}
