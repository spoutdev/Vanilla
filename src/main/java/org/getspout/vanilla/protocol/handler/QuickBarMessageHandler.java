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
package org.getspout.vanilla.protocol.handler;

import org.getspout.api.player.Player;
import org.getspout.api.protocol.MessageHandler;
import org.getspout.api.protocol.Session;
import org.getspout.vanilla.protocol.msg.QuickBarMessage;

public class QuickBarMessageHandler extends MessageHandler<QuickBarMessage> {
	@Override
	public void handle(Session session, Player player, QuickBarMessage message) {
		/*if (player.getGameMode() != GameMode.CREATIVE) {
			player.kickPlayer("Now now, don't try that here. Won't work.");
			return;
		}
		SpoutInventory inv = player.getInventory();
		int slot = inv.getItemSlot(message.getSlot());

		if (slot < 0 || slot > 8 || !checkValidId(message.getSlot())) {
			player.onSlotSet(inv, slot, inv.getItem(slot));
		}
		SpoutItemStack newItem = new SpoutItemStack(message.getId(), message.getAmount(), message.getDamage(), message.getNbtData());
		SpoutItemStack currentItem = inv.getItem(slot);

		inv.setItem(slot, newItem);
		if (currentItem != null) {
			player.setItemOnCursor(currentItem);
		} else {
			player.setItemOnCursor(null);
		}
		*/
	}

	/*public boolean checkValidId(int id) {
		return BlockProperties.get(id) == null && ItemProperties.get(id) == null;
	}*/
}