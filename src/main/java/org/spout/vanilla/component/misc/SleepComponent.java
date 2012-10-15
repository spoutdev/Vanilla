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
package org.spout.vanilla.component.misc;

import org.spout.api.component.components.EntityComponent;
import org.spout.api.entity.Player;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Block;

import org.spout.vanilla.component.world.VanillaSky;
import org.spout.vanilla.data.Animation;
import org.spout.vanilla.data.Time;
import org.spout.vanilla.event.entity.EntityAnimationEvent;
import org.spout.vanilla.event.player.network.PlayerBedEvent;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.misc.BedBlock;

public class SleepComponent extends EntityComponent {
	private final float sleepSeconds = 5;
	private float sleepTimer = sleepSeconds;
	private boolean sleeping, skipNight;
	private Block bed;
	private Player player;

	public Block getBed() {
		return bed;
	}

	public void sleep(Block bed) {
		sleeping = true;
		this.bed = bed;
		occupy(true);
		player.getNetwork().callProtocolEvent(new PlayerBedEvent(player, bed, true));
	}

	public void wake() {
		sleeping = false;
		skipNight = false;
		occupy(false);
		bed = null;
		player.getNetworkSynchronizer().callProtocolEvent(new EntityAnimationEvent(player, Animation.LEAVE_BED));
	}

	private void occupy(boolean occupy) {
		if (bed != null && bed.getMaterial() == VanillaMaterials.BED_BLOCK) {
			((BedBlock) bed.getMaterial()).setOccupied(bed, player, occupy);
		}
	}

	public boolean canSkipNight() {
		return skipNight;
	}

	@Override
	public void onAttached() {
		if (!(getOwner() instanceof Player)) {
			throw new IllegalStateException("SleepComponent can only be attached to Player entities.");
		}
		player = (Player) getOwner();
	}

	@Override
	public void onTick(float dt) {
		// Decrement sleep timer if sleeping
		if (sleeping) {
			sleepTimer -= dt;
		} else {
			sleepTimer = sleepSeconds;
		}

		// If sleep timer if finished try and skip to day
		if (sleepTimer <= 0) {
			skipNight = true;
		}
	}
}
