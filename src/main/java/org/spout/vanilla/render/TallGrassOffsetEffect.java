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
package org.spout.vanilla.render;

import org.spout.api.geo.discrete.Point;
import org.spout.api.render.effect.MeshEffect;
import org.spout.api.render.effect.SnapshotMesh;

public class TallGrassOffsetEffect implements MeshEffect {
	@Override
	public void preMesh(SnapshotMesh mesh) {
		final Point pos = mesh.getPosition();
		long hash = pos.getFloorX() * 393443L
				^ pos.getFloorY() * 96754111L
				^ pos.getFloorZ() * 228329L;
		hash *= hash * (hash + 89249651343L);
		mesh.setPosition(pos.add(
				((hash >> 16 & 0xF) / 15f - 0.5f) * 0.5f,
				((hash >> 20 & 0xF) / 15f - 1) * 0.2f,
				((hash >> 24 & 0xF) / 15f - 0.5f) * 0.5f));
	}

	@Override
	public void postMesh(SnapshotMesh mesh) {
	}
}
