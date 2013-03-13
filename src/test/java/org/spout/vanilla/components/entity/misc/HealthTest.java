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
package org.spout.vanilla.components.entity.misc;

import org.junit.Test;

import org.spout.api.entity.Entity;

import org.spout.vanilla.EntityMocker;
import org.spout.vanilla.component.entity.misc.Health;
import org.spout.vanilla.event.cause.HealthChangeCause;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class HealthTest {

	@Test
	public void testHealthComponent() {
		Entity entity = EntityMocker.mockEntity();
		Health health = entity.add(Health.class);

		health.setMaxHealth(15);
		assertEquals(15, health.getMaxHealth());
		assertEquals(1, health.getHealth());

		health.setSpawnHealth(20);
		assertEquals(20, health.getMaxHealth());
		assertEquals(20, health.getHealth());

		health.damage(1);
		assertEquals(19, health.getHealth());

		health.kill(HealthChangeCause.DAMAGE);
		assertEquals(0, health.getHealth());
		assertTrue(health.isDead());

		health.heal(5);
		assertEquals(5, health.getHealth());
		assertFalse(health.isDead());

		health.setDeathTicks(30);
		assertEquals(30, health.getDeathTicks());
		assertTrue(health.isDying());

		health.setHealth(-1, HealthChangeCause.UNKNOWN);
		assertTrue(health.hasInfiniteHealth());

	}
}
