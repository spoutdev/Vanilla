/*
 * This file is part of vanilla (http://www.spout.org/).
 *
 * vanilla is licensed under the SpoutDev License Version 1.
 *
 * vanilla is free software: you can redistribute it and/or modify
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
package org.spout.vanilla.controller.object.falling;

import org.spout.api.entity.Entity;
import org.spout.api.entity.EntityAction;
import org.spout.api.geo.discrete.Point;
import org.spout.api.material.BlockMaterial;
import org.spout.vanilla.VanillaMaterials;

import static org.spout.api.math.MathHelper.floor;

/**
 * @author zml2008
 */
public class FallingBlockAction extends EntityAction<FallingBlock> {

    @Override
    public boolean shouldRun(Entity entity, FallingBlock block) {
		Point pos = entity.getPosition();
		BlockMaterial mat = entity.getWorld().getBlockMaterial(floor(pos.getX()), floor(pos.getY()) - 1, floor(pos.getZ()));
        return mat == VanillaMaterials.AIR || mat.isLiquid();
    }

    @Override
    public void run(Entity entity, FallingBlock controller) {
		Point pos = entity.getPosition();
        entity.getWorld().setBlockMaterial(floor(pos.getX()), floor(pos.getY()), floor(pos.getZ()), controller.getBlock(), controller.getBlock().getData(), true, entity);
        entity.kill();
    }
}
