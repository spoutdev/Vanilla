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
package org.spout.vanilla.components.entity.misc;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

import org.spout.api.entity.Entity;

import org.spout.vanilla.EntityMocker;
import org.spout.vanilla.TestVanilla;
import org.spout.vanilla.component.entity.misc.Health;
import org.spout.vanilla.data.configuration.VanillaConfiguration;
import org.spout.vanilla.event.cause.HealthChangeCause;

public class HealthTest {
	@Test
	public void testHealthComponent() throws IOException {
		TestVanilla.init();
		VanillaConfiguration config = TestVanilla.getInstance().getConfig();
		VanillaConfiguration.PLAYER_SURVIVAL_ENABLE_HEALTH.setConfiguration(config);

		Entity entity = EntityMocker.mockEntity();
		Health health = entity.add(Health.class);

		health.setMaxHealth(15);
		assertEquals(15.0f, health.getMaxHealth(), 0.0f);
		assertEquals(1.0f, health.getHealth(), 0.0f);

		health.setSpawnHealth(20);
		assertEquals(20.0f, health.getMaxHealth(), 0.0f);
		assertEquals(20.0f, health.getHealth(), 0.0f);

		health.damage(1.0f);
		assertEquals(19.0f, health.getHealth(), 0.0f);

		health.kill(HealthChangeCause.DAMAGE);
		assertEquals(0.0f, health.getHealth(), 0.0f);
		assertTrue(health.isDead());

		health.heal(5.0f);
		assertEquals(5.0f, health.getHealth(), 0.0f);
		assertFalse(health.isDead());

		health.setDeathTicks(30);
		assertEquals(30, health.getDeathTicks());
		assertTrue(health.isDying());

		health.setHealth(-1.0f, HealthChangeCause.UNKNOWN);
		assertTrue(health.hasInfiniteHealth());

	}
}
