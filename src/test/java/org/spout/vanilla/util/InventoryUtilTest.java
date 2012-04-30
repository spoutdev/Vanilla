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
package org.spout.vanilla.util;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import org.spout.api.inventory.ItemStack;
import org.spout.vanilla.material.VanillaMaterials;

public class InventoryUtilTest {
	
	@Test
	public void testInventoryUtil() {
		ItemStack stack1 = new ItemStack(VanillaMaterials.CAKE, 20);
		ItemStack stack2 = new ItemStack(VanillaMaterials.CAKE, 20);
		InventoryUtil.mergeStack(stack1, stack2);
		assertEquals(stack1.getAmount(), 0);
		assertEquals(stack2.getAmount(), 40);
		
		stack1.setAmount(20);
		InventoryUtil.mergeStack(stack1, stack2, 1);
		assertEquals(stack1.getAmount(), 19);
		assertEquals(stack2.getAmount(), 41);
		
		stack1.setAmount(0);
		assertEquals(InventoryUtil.nullIfEmpty(stack1), null);
		assertEquals(InventoryUtil.nullIfEmpty(stack2), stack2);
	}
	
}
