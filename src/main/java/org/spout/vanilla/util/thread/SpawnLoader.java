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
package org.spout.vanilla.util.thread;

import org.spout.api.geo.LoadOption;
import org.spout.api.geo.World;
import org.spout.api.math.IntVector3;
import org.spout.api.util.FlatIterator;
import org.spout.api.util.thread.MultiTasker;

import org.spout.vanilla.VanillaPlugin;

/**
 * Loads a chunk area around a central chunk using multiple threads<br>
 * Should only be used once to load chunks, and when done with it, discarded
 */
public class SpawnLoader extends MultiTasker<IntVector3> {
	private final FlatIterator iter = new FlatIterator();
	private String loadingName;
	private int startAmount;
	private int step;
	private World world;

	public SpawnLoader(int threadCount) {
		super(threadCount);
	}

	@Override
	protected void handle(IntVector3 task, int remaining) {
		if (remaining % this.step == 0) {
			VanillaPlugin.getInstance().getEngine().getLogger().info(this.loadingName + " [" + this.world.getName() + "], " + (((this.startAmount - remaining) * 100) / this.startAmount) + "% complete");
		}
		this.world.getChunk(task.getX(), task.getY(), task.getZ(), LoadOption.LOAD_GEN);
	}

	public synchronized void load(World world, int cx, int cz, int radius, boolean generate) {
		this.iter.reset(cx, 0, cz, 16, radius);
		this.loadingName = generate ? "Generating" : "Loading";
		this.world = world;
		while (this.iter.hasNext()) {
			addTask(this.iter.next());
		}
		this.startAmount = this.getRemaining();
		this.step = this.startAmount / 10;
		start();
		finish();
		try {
			join();
		} catch (InterruptedException e) {
			VanillaPlugin.getInstance().getEngine().getLogger().info("Interrupted when waiting for spawn area to load");
		}
	}
}
