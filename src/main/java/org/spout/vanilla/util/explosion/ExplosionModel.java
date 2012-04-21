/*
 * This file is part of Vanilla (http://www.spout.org/).
 *
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
package org.spout.vanilla.util.explosion;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.spout.api.Source;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.discrete.Point;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.block.BlockFace;
import org.spout.api.player.Player;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.protocol.msg.ExplosionMessage;

public abstract class ExplosionModel {
	
	protected List<Block> blocksToDestroy = new ArrayList<Block>(100);
	public Random random = new Random();
	private final double messageRadiusSquared = Math.pow(64, 2.0);

	public synchronized void execute(Point position, float size, boolean fire, Source source) {
		//find all entities in the affected blocks and perform damage
		//TODO: Entity Damage
		
		//TODO: Block Event?
		
		//perform block changes
		BlockMaterial material;
		for (Block block : this.blocksToDestroy) {
			material = block.getMaterial();

			if (material == VanillaMaterials.AIR) {
				if (fire) {
					BlockMaterial below = block.clone().move(BlockFace.BOTTOM).getMaterial();
					if (below.isSolid() && this.random.nextInt(3) == 0) {
						block.setMaterial(VanillaMaterials.FIRE);
					}
				}
			} else if (material != VanillaMaterials.FIRE) {
				//TODO: Item dropping yield?
				material.onDestroy(block.getWorld(), block.getX(), block.getY(), block.getZ());
				block.setMaterial(VanillaMaterials.AIR);
			}
		}
		
		//explosion packet (TODO: Limit the amount sent per tick? Don't want to lag-out clients!)
		for (Player player : position.getWorld().getPlayers()) {
			if (player.getEntity().getPosition().distanceSquared(position) < this.messageRadiusSquared) {
				player.getSession().send(new ExplosionMessage(position, size, new byte[0]));
			}
		}
		
	}

}
