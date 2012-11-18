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
package org.spout.vanilla.render;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.spout.api.Spout;
import org.spout.api.material.block.BlockFace;
import org.spout.api.model.mesh.OrientedMesh;
import org.spout.api.model.mesh.OrientedMeshFace;
import org.spout.api.model.mesh.Vertex;
import org.spout.api.render.BatchEffect;
import org.spout.api.render.SnapshotRender;
import org.spout.api.render.Texture;

public class SkyColorBatchEffect implements BatchEffect {

	private static final Texture light = (Texture)Spout.getEngine().getFilesystem().getResource("texture://Vanilla/resources/light.png");
	//private static final float step = 255f / 16f;
	
	@Override
	public void preBatch(SnapshotRender snapshotRender) {
		if(snapshotRender.getSnapshotModel() != null && snapshotRender.getMesh() instanceof OrientedMesh){
			OrientedMesh original = (OrientedMesh)snapshotRender.getMesh();
			List<OrientedMeshFace> meshFace = new ArrayList<OrientedMeshFace>();
			
			int x = snapshotRender.getPosition().getFloorX();
			int y = snapshotRender.getPosition().getFloorY();
			int z = snapshotRender.getPosition().getFloorZ();
			
			byte blockLight = snapshotRender.getSnapshotModel().getCenter().getBlockLight(x, y, z);
			byte skyLight = snapshotRender.getSnapshotModel().getCenter().getBlockSkyLight(x, y, z);
			//int total = MathHelper.clamp(blockLight + skyLight, 0, 15);

			Color color = new Color(light.getImage().getRGB(blockLight, skyLight));
			//Color color = new Color(blockLight * step,total * step,skyLight * step);
			
			for(OrientedMeshFace face : original){
				Iterator<Vertex> it = face.iterator();
				Vertex v1 = new Vertex(it.next());
				Vertex v2 = new Vertex(it.next());
				Vertex v3 = new Vertex(it.next());
				
				v1.color = color;
				v2.color = color;
				v3.color = color;
				
				meshFace.add(new OrientedMeshFace(v1, v2, v3, new HashSet<BlockFace>(face.getSeeFromFace())));
			}
			
			snapshotRender.setMesh(new OrientedMesh(meshFace));
		}
	}

	@Override
	public void postBatch(SnapshotRender snapshotRender) {

	}

}