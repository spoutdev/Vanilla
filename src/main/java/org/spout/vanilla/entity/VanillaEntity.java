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
import java.util.List;
import java.util.Set;

import org.spout.api.collision.BoundingBox;
import org.spout.api.collision.CollisionModel;
import org.spout.api.entity.Controller;
import org.spout.api.entity.Entity;
import org.spout.api.geo.discrete.Point;
import org.spout.api.math.Vector3;
import org.spout.api.math.Vector3m;
import org.spout.api.player.Player;
import org.spout.api.protocol.Message;
import org.spout.vanilla.VanillaMaterials;
import org.spout.vanilla.protocol.msg.EntityAnimationMessage;
import org.spout.vanilla.protocol.msg.EntityHeadYawMessage;
import org.spout.vanilla.protocol.msg.EntityStatusMessage;

/**
 * Entity that is the parent of all Vanilla entities.
 */
public abstract class VanillaEntity extends Controller {
	private int health = 10, maxHealth = 10;
	private int headYaw = 0, headYawLive = 0;
	protected final BoundingBox area = new BoundingBox(-0.3F, 0F, -0.3F, 0.3F, 0.8F, 0.3F);
	protected final Vector3m velocity = new Vector3m(Vector3.ZERO);
	private int fireTicks;
	private boolean flammable;
	private Entity parent;

	@Override
	public void onAttached() {
		parent = getParent();
		parent.setCollision(new CollisionModel(area));
	}

	@Override
	public void onTick(float dt) {
		if (parent.isDead()) {
			return;
		}

		List<Message> toSend = new ArrayList<Message>();
		if (headYawLive != headYaw) {
			headYawLive = headYaw;
			EntityHeadYawMessage message = new EntityHeadYawMessage(parent.getId(), headYaw);
			toSend.add(message);
		}

		if (health <= 0) {
			toSend.add(new EntityStatusMessage(parent.getId(), EntityStatusMessage.ENTITY_DEAD));
		}

		if (toSend.isEmpty()) {
			return;
		}

		Set<Player> onlinePlayers = parent.getWorld().getPlayers();
		for (Player player : onlinePlayers) {
			for (Message message : toSend) {
				player.getSession().send(message);
			}
		}

		if (health <= 0) {
			parent.kill();
		}

		if (parent.isDead()) {
			return;
		}

		checkWeb();
		checkFireTicks();
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

	private void checkWeb() {
		Point pos = parent.getPosition();
		if (pos == null || pos.getWorld() == null) {
			return;
		}
		if (pos.getWorld().getBlock(pos).getBlockMaterial() == VanillaMaterials.WEB) {
			velocity.multiply(0.25F, 0.05F, 0.25F);
		}
	}

	private void checkFireTicks() {
		if (fireTicks > 0) {
			if (!flammable) {
				fireTicks -= 4;
				if (fireTicks < 0) {
					fireTicks = 0;
				}
				return;
			}

			if (fireTicks % 20 == 0) {
				//TODO: damage entity
			}
			--fireTicks;
		}
	}

	private void checkLava() {
		//The code checks for lava within the entity's bounding box shrunk by:
		//-0.10000000149011612D, -0.4000000059604645D, -0.10000000149011612D
	}

	public boolean isFlammable() {
		return flammable;
	}

	public void setFlammable(boolean flammable) {
		this.flammable = flammable;
	}

	public int getFireTicks() {
		return fireTicks;
	}

	public void setFireTicks(int fireTicks) {
		this.fireTicks = fireTicks;
	}

	public Vector3 getVelocity() {
		return velocity;
	}
}
