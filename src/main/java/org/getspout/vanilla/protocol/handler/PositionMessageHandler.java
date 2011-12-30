/*
 * This file is part of Vanilla (http://www.getspout.org/).
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
import org.getspout.vanilla.protocol.msg.PositionMessage;

public final class PositionMessageHandler extends MessageHandler<PositionMessage> {
	@Override
	public void handle(Session session, Player player, PositionMessage message) {
		/*if (player == null) {
			return;
		}

		PlayerMoveEvent event = EventFactory.onPlayerMove(player, player.getLocation(), new Location(player.getWorld(), message.getX(), message.getY(), message.getZ(), player.getLocation().getYaw(), player.getLocation().getPitch()));

		if (event.isCancelled()) {
			return;
		}

		Location l = event.getTo();
		l.setYaw(player.getLocation().getYaw());
		l.setPitch(player.getLocation().getPitch());

		player.setRawLocation(l);
		*/
	}
}