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
package org.spout.vanilla.material.block;

import org.spout.api.Source;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.Material;
import org.spout.api.material.block.BlockFace;
import org.spout.vanilla.material.Plant;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.attachable.GroundAttachable;

public class Flower extends GroundAttachable implements Plant {
    public Flower(String name, int id) {
        super(name, id);
    }

    @Override
    public boolean hasGrowthStages() {
        return false;
    }

    @Override
    public int getNumGrowthStages() {
        return 0;
    }

    @Override
    public int getMinimumLightToGrow() {
        return 8;
    }

    @Override
    public boolean canPlace(World world, int x, int y, int z, short data, BlockFace against, Source source) {
        if (super.canPlace(world, x, y, z, data, against, source)) {
            Block block = world.getBlock(x, y, z).move(against.getOpposite());
            return block.getMaterial() instanceof Grass || block.getMaterial() == VanillaMaterials.DIRT;
        } else {
            return false;
        }
    }

    @Override
    public Material getDrop() {
        return Material.get(getName());
    }
}
