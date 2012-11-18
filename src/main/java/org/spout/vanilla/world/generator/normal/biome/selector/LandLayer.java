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
package org.spout.vanilla.world.generator.normal.biome.selector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.royawesome.jlibnoise.module.modifier.Turbulence;
import net.royawesome.jlibnoise.module.source.Voronoi;

import org.spout.api.math.MathHelper;

import org.spout.vanilla.world.generator.biome.selector.BiomeSelectorElement;
import org.spout.vanilla.world.generator.biome.selector.BiomeSelectorLayer;

public class LandLayer implements BiomeSelectorLayer {
	private final List<BiomeSelectorElement> selectorElements = new ArrayList<BiomeSelectorElement>();
	private final Voronoi landBase = new Voronoi();
	private final Turbulence land = new Turbulence();

	public LandLayer(double scale) {
		landBase.setFrequency(0.007 / scale);
		landBase.setDisplacement(1);
		land.SetSourceModule(0, landBase);
		land.setFrequency(0.004);
		land.setPower(70);
	}

	@Override
	public BiomeSelectorElement pick(int x, int y, int z, long seed) {
		landBase.setSeed((int) seed * 11);
		land.setSeed((int) seed * 13);
		final float size = selectorElements.size() / 2f;
		return selectorElements.get(MathHelper.floor(land.GetValue(x, 0, z) * size + size));
	}

	public void addLandElements(BiomeSelectorElement... selectorElements) {
		this.selectorElements.addAll(Arrays.asList(selectorElements));
	}
}
