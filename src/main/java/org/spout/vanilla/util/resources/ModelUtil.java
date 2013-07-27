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
package org.spout.vanilla.util.resources;

import org.spout.api.material.block.BlockFace;
import org.spout.api.material.block.BlockFaces;

public class ModelUtil {
	@SuppressWarnings("unchecked")
	public static String[] getDirectionalModels(String baseModel, BlockFaces faces) {
		final String[] dirModels = new String[faces.size()];
		int i = 0;
		for (BlockFace face : faces) {
			dirModels[i++] = getDirectionalModel(baseModel, face);
		}
		return dirModels;
	}

	public static String getDirectionalModel(String baseModel, BlockFace face) {
		final String dirModel;
		final int index = baseModel.lastIndexOf('.');
		if (index == -1) {
			dirModel = baseModel + "_" + face.name().charAt(0);
		} else {
			dirModel = new StringBuilder(baseModel).insert(index, "_" + face.name().charAt(0)).toString();
		}
		return dirModel;
	}
}
