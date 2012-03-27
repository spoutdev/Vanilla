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
package org.spout.vanilla.controller;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import org.spout.api.Spout;

import org.spout.api.collision.BoundingBox;
import org.spout.api.collision.CollisionModel;
import org.spout.api.entity.action.ActionController;
import org.spout.api.inventory.ItemStack;
import org.spout.api.math.Quaternion;
import org.spout.api.math.Vector3;
import org.spout.api.player.Player;
import org.spout.api.protocol.Message;

import org.spout.vanilla.protocol.msg.EntityAnimationMessage;
import org.spout.vanilla.protocol.msg.EntityStatusMessage;
import org.spout.vanilla.protocol.msg.EntityVelocityMessage;

/**
 * Controller that is the parent of all Vanilla controllers.
 */
public abstract class VanillaController extends ActionController {
	private static Random rand = new Random();
	//Collision box for controllers
	private final BoundingBox area = new BoundingBox(-0.3F, 0F, -0.3F, 0.3F, 0.8F, 0.3F);
	//Controller flags
	private boolean isFlammable = true, canMove = true;
	//Tick effects
	private int fireTicks = 0;
	private final VanillaControllerType type;
	private EntityVelocityMessage velocityChange;
	private int velocityTicks=0;

	protected VanillaController(VanillaControllerType type) {
		super(type);
		this.type = type;
	}

	public VanillaControllerType getType() {
		return type;
	}

	@Override
	public void onAttached() {
		getParent().setCollision(new CollisionModel(area));
		getParent().setData(VanillaControllerTypes.KEY, getType().getSpawnId());
	}

	@Override
	public void onTick(float dt) {
		//Check controller health, send messages to the client based on current state.
		if (getParent().getHealth() <= 0) {
			sendMessage(getParent().getWorld().getPlayers(), new EntityStatusMessage(getParent().getId(), EntityStatusMessage.ENTITY_DEAD));
			getParent().kill();
		}
		
		velocityTicks++;
		if(velocityChange != null&&velocityTicks==5) {
			sendMessage(getParent().getWorld().getPlayers(),velocityChange);
			velocityChange = null;
			velocityTicks=0;
		}

		super.onTick(dt);
	}

	/**
	 * Get the drops that Vanilla controllers disperse into the world when un-attached (such as entity death). Children controllers
	 * should override this method for their own personal drops.
	 * @return the drops to disperse.
	 */
	public Set<ItemStack> getDrops() {
		return new HashSet<ItemStack>();
	}

	/**
	 * Checks to see if the controller is combustible.
	 * @return true is combustible, false if not
	 */
	public boolean isFlammable() {
		return isFlammable;
	}

	/**
	 * Sets if the controller is combustible or not.
	 * @param isFlammable flag representing combustible status.
	 */
	public void setFlammable(boolean isFlammable) {
		this.isFlammable = isFlammable;
	}

	/**
	 * Gets the amount of ticks the controller has been on fire.
	 * @return amount of ticks
	 */
	public int getFireTicks() {
		return fireTicks;
	}

	/**
	 * Sets the amount of ticks the controller has been on fire.
	 * @param fireTicks the new amount of ticks the controller has been on fire for.
	 */
	public void setFireTicks(int fireTicks) {
		this.fireTicks = fireTicks;
	}

	/**
	 * This sets if a controller moves.
	 * @param canMove true if the controller can move, false otherwise.
	 */
	public void setMoveable(boolean canMove) {
		this.canMove = canMove;
	}

	/**
	 * Returns if the controller can move.
	 * @return true if the controller moves, false if the controller is stationary.
	 */
	public boolean isMoveable() {
		return canMove;
	}

	private void checkFireTicks() {
		if (fireTicks > 0) {
			if (!isFlammable) {
				fireTicks -= 4;
				if (fireTicks < 0) {
					fireTicks = 0;
				}
				return;
			}

			if (fireTicks % 20 == 0) {
				damage(1);
				sendMessage(getParent().getWorld().getPlayers(),
						new EntityAnimationMessage(getParent().getId(), EntityAnimationMessage.ANIMATION_HURT),
						new EntityStatusMessage(getParent().getId(), EntityStatusMessage.ENTITY_HURT));
			}

			--fireTicks;
		}
	}

	//=========================
	//Controller helper methods
	//=========================

	/**
	 * Damages this controller and doesn't send messages to the client.
	 * @param amount amount the controller will be damaged by.
	 */
	public void damage(int amount) {
		getParent().setHealth(getParent().getHealth() - amount);
	}

	/**
	 * This method takes in any amount of messages and sends them to any amount of players.
	 * @param players  specific players to send a message to.
	 * @param messages the message(s) to send
	 */
	public void sendMessage(Set<Player> players, Message... messages) {
		for (Player player : players) {
			for (Message message : messages) {
				sendMessage(player, message);
			}
		}
	}

	/**
	 * This method takes in a message and sends it to a specific player
	 * @param player  specific player to relieve message
	 * @param message specific message to send.
	 */
	public void sendMessage(Player player, Message message) {
		player.getSession().send(message);
	}

	/**
	 * Moves this controller.
	 * @param vect the vector that is applied as the movement.
	 */
	public void move(Vector3 vect) {
		getParent().translate(vect);
		if(velocityChange == null)
			velocityChange = new EntityVelocityMessage(getParent().getId(), (int) vect.getX(), (int) vect.getY(), (int) vect.getZ());
		else
			velocityChange = new EntityVelocityMessage(getParent().getId(), velocityChange.getVelocityX() + (int) vect.getX(), velocityChange.getVelocityY() + (int) vect.getY(), velocityChange.getVelocityZ() + (int) vect.getZ());
	}

	/**
	 * Moves this controller
	 * @param x x-axis to move the controller along
	 * @param y y-axis to move the controller along
	 * @param z z-axis to move the controller along
	 */
	public void move(float x, float y, float z) {
		move(new Vector3(x,y,z));
	}

	/**
	 * Rotates the controller
	 * @param rot the quaternion that is applied as the rotation.
	 */
	public void rotate(Quaternion rot) {
		getParent().rotate(rot);
	}

	/**
	 * Rotates the controller
	 * @param degrees the angle of which to do rotation.
	 * @param x	   x-axis to rotate the controller along
	 * @param y	   y-axis to rotate the controller along
	 * @param z	   z-axis to rotate the controller along
	 */
	public void rotate(float degrees, float x, float y, float z) {
		getParent().rotate(degrees, x, y, z);
	}

	/**
	 * Rolls this controller along an angle.
	 * @param angle the angle in-which to roll
	 */
	public void roll(float angle) {
		getParent().roll(angle);
	}

	/**
	 * If a child controller needs a random number for anything, they should call this method.
	 * This eliminates needless random objects created all the time.
	 * @return random object.
	 */
	public Random getRandom() {
		return rand;
	}
}
