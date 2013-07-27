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
package org.spout.vanilla.entity.component;

import org.junit.Test;
import org.mockito.Mockito;

import org.spout.api.entity.Entity;
import org.spout.api.entity.Player;

import org.spout.vanilla.EntityMocker;
import org.spout.vanilla.component.entity.misc.Level;

import static org.junit.Assert.assertTrue;

public class EntityMockerTest {
	@Test
	public void test() {
		//TODO: Fix this test, I force Level component to a player and it bombs cause getOwner is not mocked to return the player!
		Player player = EntityMocker.mockPlayer();
		Level level = player.add(Level.class);
		Mockito.when(level.getOwner()).thenReturn(player);
		assertTrue("Level is null!", level != null);
		Level sameLevel = player.get(Level.class);
		assertTrue("Level does not match!", level == sameLevel);

		assertTrue("Level data is null!", level.getData() != null);
	}
}
