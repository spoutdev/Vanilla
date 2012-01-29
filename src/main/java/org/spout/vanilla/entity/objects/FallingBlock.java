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
 * the MIT license and the SpoutDev license version 1 along with this program.  
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license, 
 * including the MIT license.
 */
package org.spout.vanilla.entity.objects;

import org.spout.api.geo.World;
import org.spout.api.geo.discrete.Point;
import org.spout.api.material.BlockMaterial;
import org.spout.api.math.MathHelper;
import org.spout.api.protocol.Message;
import org.spout.vanilla.VanillaBlocks;
import org.spout.vanilla.entity.MovingEntity;
import org.spout.vanilla.material.VanillaBlockMaterial;
import org.spout.vanilla.protocol.msg.SpawnVehicleMessage;

public class FallingBlock extends MovingEntity {
	private final BlockMaterial block;
	public FallingBlock(BlockMaterial block) {
		this.block = block;
	}
	
	@Override
	public void onAttached() {
		super.onAttached();
	}

	@Override
	public void onTick(float dt) {
		if (parent == null || parent.getTransform() == null) {
			return;
		}
		Point position = parent.getTransform().getPosition();
		if (position == null) {
			return;
		}
		World world = position.getWorld();
		int x = MathHelper.floor(position.getX());
		int y = MathHelper.floor(position.getY());
		int z = MathHelper.floor(position.getZ());
		VanillaBlockMaterial material = (VanillaBlockMaterial) world.getBlock(x, y-1, z).getBlockMaterial();
		if (material == VanillaBlocks.air || material.isLiquid()){
			getVelocity().add(0, -0.004, 0);
		}
		else {
			world.setBlockMaterial(x, y, z, block, world);
			this.parent.kill();
		}
		super.onTick(dt);
	}
	
	public Message getSpawnMessage(){
		int spawnId = -1; //TODO: support for other falling block types?
		if (block == VanillaBlocks.sand){
			spawnId = 70;
		}
		if (block == VanillaBlocks.gravel){
			spawnId = 71;
		}
		if (spawnId > 0) {
			Point position = parent.getTransform().getPosition();
			int x = MathHelper.floor(position.getX());
			int y = MathHelper.floor(position.getY());
			int z = MathHelper.floor(position.getZ());
			return new SpawnVehicleMessage(parent.getId(), spawnId, x, y, z);
		}
		return null;
	}
}