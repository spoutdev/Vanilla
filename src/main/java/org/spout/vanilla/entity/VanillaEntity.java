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
package org.spout.vanilla.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.spout.api.entity.Controller;
import org.spout.api.geo.World;
import org.spout.api.player.Player;
import org.spout.api.protocol.Message;
import org.spout.vanilla.protocol.msg.EntityAnimationMessage;
import org.spout.vanilla.protocol.msg.EntityHeadYawMessage;
import org.spout.vanilla.protocol.msg.EntityStatusMessage;

/**
 * Entity that is the parent of all Vanilla entities.
 */
public abstract class VanillaEntity extends Controller {
	private int health = 10, maxHealth = 10;
	private int headYaw=0, headYawLive=0;

	@Override
	public void onTick(float dt) {
		if (parent.isDead()) {
			return;
		}
		List<Message> toSend = new ArrayList<Message>();
		
		if(headYawLive!=headYaw) {
			headYawLive = headYaw;
			EntityHeadYawMessage message = new EntityHeadYawMessage(parent.getId(), headYaw);
			toSend.add(message);
		}
		if(health <= 0) {
			toSend.add(new EntityStatusMessage(parent.getId(), EntityStatusMessage.ENTITY_DEAD));
		}
		if(toSend.isEmpty())
			return;
		Set<Player> onlinePlayers = parent.getWorld().getPlayers();
		for(Player player : onlinePlayers) {
			for(Message message : toSend) {
				player.getSession().send(message);
			}
		}
		
		if (health <= 0) {
			parent.kill();
		}
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int newHealth) {
		health = newHealth;
		if (health > maxHealth) {
			health = maxHealth;
		}
	}

	public void damage(int amount) {
		health -= amount;
		EntityAnimationMessage message = new EntityAnimationMessage(parent.getId(), EntityAnimationMessage.ANIMATION_HURT);
		EntityStatusMessage message2 = new EntityStatusMessage(parent.getId(), EntityStatusMessage.ENTITY_HURT);
		Set<Player> players = parent.getWorld().getPlayers();
		for (Player player : players) {
			player.getSession().send(message);
			player.getSession().send(message2);
		}
	}

	public int getMaxHealth() {
		return maxHealth;
	}

	public void setMaxHealth(int newMax) {
		maxHealth = newMax;
	}

	public void kill() {
		setHealth(0);
	}
	
	public void setHeadYaw(int headYaw) {
		headYawLive = headYaw;
	}
	
	public int getHeadYaw() {
		return headYaw;
	}
	
	public int getLiveHeadYaw() {
		return headYawLive;
	}
}
