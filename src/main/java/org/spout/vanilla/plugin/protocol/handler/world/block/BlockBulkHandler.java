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
package org.spout.vanilla.plugin.protocol.handler.world.block;

import org.spout.api.entity.Player;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Chunk;
import org.spout.api.material.BlockMaterial;
import org.spout.api.protocol.MessageHandler;
import org.spout.api.protocol.Session;
import org.spout.api.protocol.reposition.RepositionManager;
import org.spout.vanilla.plugin.material.VanillaMaterials;
import org.spout.vanilla.plugin.protocol.msg.world.block.BlockBulkMessage;

public class BlockBulkHandler extends MessageHandler<BlockBulkMessage>{

	@Override
	public void handleClient(Session session, BlockBulkMessage message) {
		if (!session.hasPlayer()) {
			return;
		}
		
		Player player = session.getPlayer();
		World world = player.getWorld();
		RepositionManager rm = player.getNetworkSynchronizer().getRepositionManager();
		int baseX = message.getChunkX() << Chunk.BLOCKS.BITS;
		int baseZ = message.getChunkZ() << Chunk.BLOCKS.BITS;
		
		for(int i = 0; i < message.getChanges(); i++){
			int x = rm.convertX(message.getCoordinates()[i * 3] + baseX);
			int y =	rm.convertY(message.getCoordinates()[i * 3 + 1]);
			int z =	rm.convertZ(message.getCoordinates()[i * 3 + 2] + baseZ);
			
			short type = message.getTypes()[i];
			byte data = message.getMetadata()[i];
			
			BlockMaterial material = (BlockMaterial)VanillaMaterials.getMaterial(type, data);
			world.getChunkFromBlock(x, y, z).getBlock(x, y, z).setMaterial(material);
		}
		
		//TODO: implement
		System.out.println(message.toString());
	}

}
