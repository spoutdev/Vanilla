/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, SpoutDev <http://www.spout.org/>
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
package org.spout.vanilla.runnable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.cuboid.Region;
import org.spout.api.material.BlockMaterial;
import org.spout.api.scheduler.ParallelRunnable;
import org.spout.api.util.list.concurrent.ConcurrentList;

import org.spout.vanilla.material.block.ScheduleUpdated;

public class BlockScheduler implements ParallelRunnable {
	private static class Update {
		public Update(Block block, int delay) {
			this.block = block;
			this.counter = new AtomicInteger(delay);
		}

		public final Block block;
		public final AtomicInteger counter;
	}

	private static Map<Region, BlockScheduler> schedulers = new HashMap<Region, BlockScheduler>();

	public static void remove(Region region) {
		synchronized (schedulers) {
			schedulers.remove(region);
			//do something? Saving?
		}
	}

	public static void schedule(Block block, int delay) {
		synchronized (schedulers) {
			BlockScheduler scheduler = schedulers.get(block.getRegion());
			if (scheduler != null) {
				scheduler.updates.addDelayed(new Update(block, delay));
			}
		}
	}

	public BlockScheduler() {
		this.region = null;
		this.updates = null;
	}

	private BlockScheduler(Region region) {
		this.region = region;
		this.updates = new ConcurrentList<Update>();
	}

	private final ConcurrentList<Update> updates;
	private final Region region;

	@Override
	public void run() {
		this.updates.sync();
		if (!this.updates.isEmpty()) {
			Iterator<Update> iter = this.updates.iterator();
			Update update;
			while (iter.hasNext()) {
				update = iter.next();
				if (update.counter.decrementAndGet() <= 0) {
					System.out.println("EXECUTE: " + update.block.getX() + "/" + update.block.getY() + "/" + update.block.getZ());
					iter.remove();
					//perform update
					BlockMaterial mat = update.block.getSubMaterial();
					if (mat instanceof ScheduleUpdated) {
						((ScheduleUpdated) mat).onDelayedUpdate(update.block);
					}
				}
			}
		}
	}

	@Override
	public BlockScheduler newInstance(Region r) {
		BlockScheduler scheduler = new BlockScheduler(r);
		synchronized (schedulers) {
			schedulers.put(r, scheduler);
		}
		return scheduler;
	}
}
