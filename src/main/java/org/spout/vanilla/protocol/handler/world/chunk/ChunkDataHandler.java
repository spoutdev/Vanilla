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
package org.spout.vanilla.protocol.handler.world.chunk;

import org.spout.api.entity.Player;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Chunk;
import org.spout.api.material.BlockMaterial;
import org.spout.api.protocol.ClientSession;
import org.spout.api.protocol.MessageHandler;
import org.spout.api.protocol.reposition.RepositionManager;

import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.protocol.msg.world.chunk.ChunkDataMessage;

public class ChunkDataHandler extends MessageHandler<ChunkDataMessage> {
	@Override
	public void handleClient(ClientSession session, ChunkDataMessage message) {
		if (!session.hasPlayer()) {
			return;
		}

		Player player = session.getPlayer();
		World world = player.getEngine().getDefaultWorld();//player.getWorld();
		RepositionManager rm = player.getNetworkSynchronizer().getRepositionManager();

		int baseX = message.getX() << Chunk.BLOCKS.BITS;
		int baseZ = message.getZ() << Chunk.BLOCKS.BITS;

		final byte[][] data = message.getData();

		for (int i = 0; i < 16; i++) {
			int baseY = i << Chunk.BLOCKS.BITS;

			if (data[i] == null) {
				continue;
			}

			int index = 0;
			for (int xx = 0; xx < Chunk.BLOCKS.SIZE; xx++) {
				for (int yy = 0; yy < Chunk.BLOCKS.SIZE; yy++) {
					for (int zz = 0; zz < Chunk.BLOCKS.SIZE; zz++) {
						int x = rm.convertX(xx + baseX);
						int y = rm.convertY(yy + baseY);
						int z = rm.convertZ(zz + baseZ);

						short type = data[i][index];
						byte dat;
						if (index % 2 == 0) {
							dat = (byte) (data[i][(index / 2) + 4096] >> 4);
						} else {
							dat = (byte) (data[i][(index / 2) + 4096] & 0xF);
						}
						index++;

						if (type > 0) {
							BlockMaterial material = (BlockMaterial) VanillaMaterials.getMaterial(type, dat);
							world.getChunkFromBlock(x, y, z).getBlock(x, y, z).setMaterial(material);
						}
					}
				}
			}
		}
	}
}
