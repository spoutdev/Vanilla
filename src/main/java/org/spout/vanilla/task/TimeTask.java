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
package org.spout.vanilla.task;

import org.spout.api.geo.World;
import org.spout.api.player.Player;

import org.spout.vanilla.data.Time;
import org.spout.vanilla.protocol.msg.TimeUpdateMessage;

/**
 * Running task to update and keep time synchronized.
 */
public class TimeTask implements Runnable {
	private final World world;
	private final Time time;
	private long update;
	private int modifier;

	public TimeTask(World world, Time time, int modifier) {
		this.world = world;
		this.time = time;
		this.modifier = modifier;
		update = -1;
	}

	@Override
	public void run() {
		long current = update;
		//-1 means the task just started.
		if (update == -1) {
			update = time.getLength();
		} else {
			update+= modifier;
		}
		//Only send time update message if time actually updated
		if (current != update  && update != -1) {
			for (Player player : world.getPlayers()) {
				player.getSession().send(new TimeUpdateMessage(update));
			}
		}
	}

	/**
	 * Sets the time of this task.
	 * @param update the new running time of this task
	 */
	public void setTime(long update) {
		this.update = update;
	}

	/**
	 * Gets the running time of this task.
	 * @return the current running time of the task.
	 */
	public long getTime() {
		return update;
	}

	/**
	 * Returns the current modifier to the time.
	 * @return the current modifier.
	 */
	public int getModifier() {
		return modifier;
	}

	/**
	 * Sets the time modifier of this task.
	 * @param modifier new modifier for the time.
	 */
	public void setModifier(int modifier) {
		this.modifier = modifier;
	}
}
