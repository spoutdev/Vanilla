/*
 * This file is part of Vanilla (http://www.spout.org/).
 *
 * Vanilla is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Vanilla is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.spout.vanilla.protocol.msg;

import org.spout.api.protocol.Message;

public class EnchantItemMessage extends Message {
	private final int transaction, enchantment;

	public EnchantItemMessage(int transaction, int enchantment) {
		this.transaction = transaction;
		this.enchantment = enchantment;
	}

	public int getTransaction() {
		return transaction;
	}

	public int getEnchantment() {
		return enchantment;
	}

	@Override
	public String toString() {
		return "EnchantItemMessage{transaction=" + transaction + ",enchantment=" + enchantment + "}";
	}
}