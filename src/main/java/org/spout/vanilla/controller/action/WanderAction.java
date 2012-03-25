/*
 * This file is part of SpoutAPI (http://www.spout.org/).
 *
 * SpoutAPI is licensed under the SpoutDev License Version 1.
 *
 * SpoutAPI is free software: you can redistribute it and/or modify
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
package org.spout.vanilla.controller.action;

import java.util.Random;

import org.spout.api.entity.Entity;
import org.spout.api.entity.EntityAction;

import org.spout.vanilla.controller.VanillaController;

public class WanderAction extends EntityAction<VanillaController> {
	private static final int WANDER_FREQ = 2;
	private final Random rand = new Random();
	private int countdown;

	@Override
	public boolean shouldRun(Entity entity, VanillaController controller) {
		return --countdown <= 0;
	}

	@Override
	public void run(Entity entity, VanillaController controller) {
		countdown = controller.getRandom().nextInt(7) + 3;
		float x = (controller.getRandom().nextBoolean() ? 1 : -1) * controller.getRandom().nextFloat();
		float z = (controller.getRandom().nextBoolean() ? 1 : -1) * controller.getRandom().nextFloat();
		float rotate = (controller.getRandom().nextBoolean() ? 1 : -1) * controller.getRandom().nextFloat();
		//Rotate the sheep!
		entity.rotate(rotate, x, 0, z);
		//Move the sheep!
		entity.translate(x, 0, z);
		/*Pointm toLoc = entity.getPoint();
		int x = floor(entity.getX()), y = floor(entity.getY()), z = floor(entity.getZ());
        toLoc.add(rand.nextFloat() / 4, 0, rand.nextFloat() / 4);
        BlockMaterial targetId = entity.getWorld().getBlockMaterial(x, y, z);
        if (targetId.isOpaque()) {
            toLoc.add(0, 2, 0);
            if (entity.getWorld().getBlockMaterial(x, y, z).isOpaque()) {
                toLoc.setX(entity.getX());
                toLoc.setZ(entity.getZ());
            }
        }
        entity.setPoint(toLoc);*/
	}
}
