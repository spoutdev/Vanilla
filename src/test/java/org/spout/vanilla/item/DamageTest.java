package org.spout.vanilla.item;

import org.junit.Test;

import org.spout.api.inventory.ItemStack;
import org.spout.vanilla.controller.source.DamageCause;
import org.spout.vanilla.enchantment.Enchantments;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.item.armor.Armor;
import org.spout.vanilla.util.EnchantmentUtil;

import static org.junit.Assert.assertTrue;

public class DamageTest {

	@Test
	public void testDamageModifier() {
		ItemStack test = new ItemStack(VanillaMaterials.DIAMOND_CHESTPLATE, 1);
		EnchantmentUtil.addEnchantment(test, Enchantments.PROTECTION, 4, false);
		assertTrue(EnchantmentUtil.hasEnchantment(test, Enchantments.PROTECTION));

		Armor armor = (Armor) test.getMaterial();
		assertTrue((int) Math.ceil(.04 * (armor.getBaseProtection() + armor.getProtection(test, DamageCause.CACTUS))) == 1);
	}
}
