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
package org.spout.vanilla.world.generator.structure;

public class ComponentPlanePart extends ComponentCuboidPart {
	public ComponentPlanePart(StructureComponent parent) {
		super(parent);
	}

	@Override
	protected boolean isOuter(int xx, int yy, int zz) {
		if (min.getX() == max.getX()) {
			return yy == min.getY() || zz == min.getZ() || yy == max.getY() || zz == max.getZ();
		} else if (min.getY() == max.getY()) {
			return xx == min.getX() || zz == min.getZ() || xx == max.getX() || zz == max.getZ();
		} else if (min.getZ() == max.getZ()) {
			return yy == min.getY() || xx == min.getX() || yy == max.getY() || xx == max.getX();
		} else {
			return super.isOuter(xx, yy, zz);
		}
	}
}
