/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, VanillaDev <http://www.spout.org/>
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
package org.spout.vanilla.controller.living.player;

import org.spout.api.tickable.LogicPriority;
import org.spout.api.tickable.LogicRunnable;
import org.spout.vanilla.configuration.VanillaConfiguration;
import org.spout.vanilla.protocol.msg.KeepAliveMessage;
import org.spout.vanilla.protocol.msg.PlayerListMessage;
import org.spout.vanilla.util.VanillaNetworkUtil;

/**
 *
 * @author ZNickq
 */
public class PingProcess extends LogicRunnable<VanillaPlayer> {
  private int count = 0, ping;
  private long lastUserList = 0,unresponsiveTicks = VanillaConfiguration.PLAYER_TIMEOUT_TICKS.getInt(), lastPing = 0;

  public PingProcess(VanillaPlayer aThis, LogicPriority logicPriority) {
    super(aThis, logicPriority);
  }

  @Override
  public boolean shouldRun(float f) {
    return true;
  }

  @Override
  public void run() {
    count++;

		if (lastPing++ > VanillaConfiguration.PLAYER_TIMEOUT_TICKS.getInt() / 2) {
			VanillaNetworkUtil.sendPacket(getParent().getParent(), new KeepAliveMessage(getParent().getRandom().nextInt()));
			lastPing = 0;
		}
		unresponsiveTicks--;
		if (unresponsiveTicks == 0) {
			getParent().getParent().kick("Connection Timeout!");
		}
    
		if (lastUserList++ >= 20) {
			VanillaNetworkUtil.broadcastPacket(new PlayerListMessage(getParent().getTabListName(), true, (short) getPing()));
			lastUserList = 0;
		}
  }

  public int getPing() {
    return ping;
  }
  
	public void resetTimeoutTicks() {
		ping = count;
		count = 0;
		unresponsiveTicks = VanillaConfiguration.PLAYER_TIMEOUT_TICKS.getInt();
	}

  
}
