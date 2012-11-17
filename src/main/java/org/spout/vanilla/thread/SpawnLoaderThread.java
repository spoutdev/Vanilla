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
package org.spout.vanilla.thread;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

import org.spout.api.Spout;
import org.spout.api.geo.World;
import org.spout.api.math.IntVector3;

public class SpawnLoaderThread extends Thread {
	private final static AtomicInteger idCounter = new AtomicInteger(0);
	private final static AtomicInteger size = new AtomicInteger();
	private final static ConcurrentLinkedQueue<IntPoint3> queue = new ConcurrentLinkedQueue<IntPoint3>();
	private final int step;
	private final int total;
	private final String initChunkType;

	public SpawnLoaderThread(int total, int step, String initChunkType) {
		super("Spawn Loader Thread - " + idCounter.getAndIncrement());
		this.step = step;
		this.total = total;
		this.initChunkType = initChunkType;
	}

	public static void addChunk(World w, int x, int y, int z) {
		queue.add(new IntPoint3(w, x, y, z));
		size.incrementAndGet();
	}

	public void run() {
		boolean done = false;
		int remaining = size.get();
		while (!done) {
			IntPoint3 p = queue.poll();
			if (p == null) {
				done = true;
				continue;
			}
			remaining = size.decrementAndGet();
			if (remaining % step == 0) {
				Spout.getLogger().info(initChunkType + " [" + p.getWorld().getName() + "], " + (((total - remaining) * 100) / total) + "% complete");
			}
			p.getWorld().getChunk(p.getX(), p.getY(), p.getZ());
		}
	}

	private static class IntPoint3 extends IntVector3 {
		private final World w;

		public IntPoint3(World w, int x, int y, int z) {
			super(x, y, z);
			this.w = w;
		}

		public World getWorld() {
			return w;
		}
	}
}
