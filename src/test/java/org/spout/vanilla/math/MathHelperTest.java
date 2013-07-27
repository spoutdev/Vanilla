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
package org.spout.vanilla.math;

import java.util.Random;

import org.junit.Assert;
import org.junit.Test;

import org.spout.api.math.GenericMath;

import org.spout.vanilla.util.MathHelper;

public class MathHelperTest {
	private static final int REPEATS = 1000000;
	private static final int SLICES = 1000;
	private static final double SLICE_SIZE = 1d / SLICES;

	@Test
	public void testHashToFloat1D() {
		final Random random = new Random();
		final double[] results = new double[SLICES];
		for (long i = 0; i < REPEATS; i++) {
			final float f = MathHelper.hashToFloat(random.nextInt(), random.nextInt());
			results[(int) (f / SLICE_SIZE)]++;
		}
		Assert.assertTrue(standardDeviation(results) <= 75);
	}

	@Test
	public void testHashToFloat2D() {
		final Random random = new Random();
		final double[] results = new double[SLICES];
		for (long i = 0; i < REPEATS; i++) {
			final float f = MathHelper.hashToFloat(random.nextInt(), random.nextInt(), random.nextInt());
			results[(int) (f / SLICE_SIZE)]++;
		}
		Assert.assertTrue(standardDeviation(results) <= 75);
	}

	@Test
	public void testHashToFloat3D() {
		final Random random = new Random();
		final double[] results = new double[SLICES];
		for (long i = 0; i < REPEATS; i++) {
			final float f = MathHelper.hashToFloat(random.nextInt(), random.nextInt(), random.nextInt(), random.nextInt());
			results[(int) (f / SLICE_SIZE)]++;
		}
		Assert.assertTrue(standardDeviation(results) <= 75);
	}

	private static double standardDeviation(double[] values) {
		double[] vals = values.clone();
		final double mean = GenericMath.mean(vals);
		for (int i = 0; i < vals.length; i++) {
			vals[i] -= mean;
			vals[i] *= vals[i];
		}
		return Math.sqrt(GenericMath.mean(vals));
	}
}
