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
package org.spout.vanilla.entity.living.passive;

import java.util.Random;
import org.spout.vanilla.entity.Entity;
import org.spout.vanilla.entity.AnimalEntity;
import org.spout.vanilla.entity.PassiveEntity;

public class Sheep extends AnimalEntity implements PassiveEntity {
	private int countdown = 0;
	private final Random rand = new Random();
	@Override
	public void onAttached() {
		super.onAttached();
		parent.setData(Entity.KEY, Entity.Sheep.id);
	}

	@Override
	public void onTick(float dt) {
		if (--countdown <= 0) {
			countdown = rand.nextInt(7) + 3;
			float x = (rand.nextBoolean() ? 1 : -1) * rand.nextFloat();
			float y = rand.nextFloat();
			float z = (rand.nextBoolean() ? 1 : -1) * rand.nextFloat();
			this.velocity.add(x, y, z);
		}
		super.onTick(dt);
	}
}
