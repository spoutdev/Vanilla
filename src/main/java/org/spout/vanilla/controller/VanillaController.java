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
import org.spout.api.collision.CollisionStrategy;
import org.spout.api.entity.Entity;
import org.spout.api.entity.action.ActionController;
import org.spout.api.entity.PlayerController;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.discrete.Point;
import org.spout.api.inventory.ItemStack;
import org.spout.api.math.MathHelper;
import org.spout.api.math.Quaternion;
import org.spout.api.math.Vector2;
import org.spout.api.math.Vector3;
import org.spout.api.player.Player;
import org.spout.api.protocol.Message;

import org.spout.vanilla.controller.object.moving.Item;
import org.spout.vanilla.controller.source.HealthChangeReason;
import org.spout.vanilla.protocol.msg.EntityAnimationMessage;
import org.spout.vanilla.protocol.msg.EntityStatusMessage;

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
	private int velocityTicks = 0;
	private int positionTicks = 0;
	private Vector3 oldPosition;
	private Vector3 oldRotation;
	private Vector3 movedVelocity = Vector3.ZERO;
	private Vector3 velocity = Vector3.ZERO;
	private float maxSpeed = Float.MAX_VALUE; //might turn this into a vector too?
	private int clientX, clientY, clientZ;
	private int clientYaw, clientPitch;
	private Point lastKnownPosition;

	public boolean needsVelocityUpdate() {
		return velocityTicks++ % 5 == 0;
	}
	
	public boolean needsPositionUpdate() {
		return positionTicks++ % 60 == 0;
	}
	
	protected VanillaController(VanillaControllerType type) {
		super(type);
		this.type = type;
	}
	
	@Override
	public void onDeath() {
		//Don't count disconnects/unknown exceptions as dead (!Yes that's a difference!)
		if (this instanceof PlayerController) {
			Player toCheck = ((PlayerController) this).getPlayer();
			if(toCheck.getSession() == null || toCheck.getSession().getPlayer() == null) {
				return;
			}

	    }
        for(ItemStack drop : getDrops()) {
        Item item = new Item(drop, Vector3.ZERO);
        	if(lastKnownPosition != null) {
        		lastKnownPosition.getWorld().createAndSpawnEntity(lastKnownPosition, item);
        	}
        }
    }

	public float getMaxSpeed() {
		return this.maxSpeed;
	}

	public void setMaxSpeed(float maxSpeed) {
		this.maxSpeed = maxSpeed;
	}

	public Vector3 getVelocity() {
		return velocity;
	}
	
	public Vector3 getPreviousPosition() {
		return this.oldPosition;
	}
	
	public Vector3 getPreviousRotation() {
		return this.oldRotation;
	}
	
	public int getClientPosX() {
		return this.clientX;
	}
	
	public int getClientPosY() {
		return this.clientY;
	}
	
	public int getClientPosZ() {
		return this.clientZ;
	}
	
	public int getClientYaw() {
		return this.clientYaw;
	}
	
	public int getClientPitch() {
		return this.clientPitch;
	}
	
	//FIXME: MOVE TO MATHHELPER!
	public static byte wrapByte(int value) {
		value %= 256;
		if (value < 0) {
			value += 256;
		}
		return (byte) value;
	}
	
	public void updateClientPosition() {
		this.clientX = MathHelper.floor(this.getParent().getPosition().getX() * 32.0);
		this.clientY = MathHelper.floor(this.getParent().getPosition().getY() * 32.0);
		this.clientZ = MathHelper.floor(this.getParent().getPosition().getZ() * 32.0);
		this.clientYaw = wrapByte(MathHelper.floor(this.getParent().getYaw() / 360f * 256f));
		this.clientPitch = wrapByte(MathHelper.floor(this.getParent().getPitch() / 360f * 256f));
	}
	
	public void setClientPosition(int x, int y, int z, int yaw, int pitch) {
		this.clientX = x;
		this.clientY = y;
		this.clientZ = z;
		this.clientYaw = yaw;
		this.clientPitch = pitch;
	}

	public void setVelocity(Vector3 velocity) {
		this.velocity = velocity;
	}

	public Vector3 getMovedVelocity() {
		return this.movedVelocity;
	}

	@Override
	public VanillaControllerType getType() {
		return type;
	}

	public BoundingBox getBounds() {
		return this.area;
	}

	@Override
	public void onAttached() {
		getParent().setCollision(new CollisionModel(area));
		getParent().getCollision().setStrategy(CollisionStrategy.SOFT);
		getParent().setData(VanillaControllerTypes.KEY, getType().getID());
		this.oldPosition = getParent().getPosition();
		this.oldRotation = getParent().getRotation().getAxisAngles();
		this.updateClientPosition();
	}
	
	@Override
	public void onCollide(Block block) {
		this.setVelocity(Vector3.ZERO);
	}
	
	@Override
	public void onCollide(Entity entity) {
		//push entities apart
		//TODO: Ignore if this entity is a passenger?
		Vector2 diff = entity.getPosition().subtract(this.getParent().getPosition()).toVector2();
		float distance = diff.length();
		if (distance > 0.1f) {	
			double factor = Math.min(1f / distance, 1f) / distance * 0.05;
			diff = diff.multiply(factor);
			this.setVelocity(this.getVelocity().add(diff.toVector3()));
		}
	}

	@Override
	public void onTick(float dt) {		
		//Check controller health, send messages to the client based on current state.
		if (getParent().getHealth() <= 0) {
			broadcastPacket(new EntityStatusMessage(getParent().getId(), EntityStatusMessage.ENTITY_DEAD));
			getParent().kill();
		}
		
		this.oldPosition = getParent().getPosition();
		this.oldRotation = getParent().getRotation().getAxisAngles();

		//HACK: Store Position for later purposes
		if(getParent().getPosition() != null  && getParent().getPosition() != Point.invalid) {
			lastKnownPosition = getParent().getPosition();
		}
		
		super.onTick(dt);
	}

	/**
	 * Get the drops that Vanilla controllers disperse into the world when
	 * un-attached (such as entity death). Children controllers should override
	 * this method for their own personal drops.
	 *
	 * @return the drops to disperse.
	 */
	public Set<ItemStack> getDrops() {
		return new HashSet<ItemStack>();
	}

	/**
	 * Checks to see if the controller is combustible.
	 *
	 * @return true is combustible, false if not
	 */
	public boolean isFlammable() {
		return isFlammable;
	}

	/**
	 * Sets if the controller is combustible or not.
	 *
	 * @param isFlammable flag representing combustible status.
	 */
	public void setFlammable(boolean isFlammable) {
		this.isFlammable = isFlammable;
	}

	/**
	 * Gets the amount of ticks the controller has been on fire.
	 *
	 * @return amount of ticks
	 */
	public int getFireTicks() {
		return fireTicks;
	}

	/**
	 * Sets the amount of ticks the controller has been on fire.
	 *
	 * @param fireTicks the new amount of ticks the controller has been on fire
	 * for.
	 */
	public void setFireTicks(int fireTicks) {
		this.fireTicks = fireTicks;
	}

	/**
	 * This sets if a controller moves.
	 *
	 * @param canMove true if the controller can move, false otherwise.
	 */
	public void setMoveable(boolean canMove) {
		this.canMove = canMove;
	}

	/**
	 * Returns if the controller can move.
	 *
	 * @return true if the controller moves, false if the controller is
	 * stationary.
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
				broadcastPacket(new EntityAnimationMessage(getParent().getId(), EntityAnimationMessage.ANIMATION_HURT), new EntityStatusMessage(getParent().getId(), EntityStatusMessage.ENTITY_HURT));
			}

			--fireTicks;
		}
	}

	//=========================
	//Controller helper methods
	//=========================
	/**
	 * Damages this controller and doesn't send messages to the client.
	 *
	 * @param amount amount the controller will be damaged by.
	 */
	public void damage(int amount) {
		getParent().setHealth(getParent().getHealth() - amount, new HealthChangeReason(HealthChangeReason.Type.UNKNOWN));
	}

	/**
	 * This method takes any amount of messages and sends them to every online player on the server.
	 *
	 * @param messages
	 */
	public void broadcastPacket(Message... messages) {
		sendPacket(Spout.getEngine().getOnlinePlayers(), messages);
	}

	/**
	 * This method takes in any amount of messages and sends them to any amount of
	 * players.
	 *
	 * @param players specific players to send a message to.
	 * @param messages the message(s) to send
	 */
	public void sendPacket(Player[] players, Message... messages) {
		for (Player player : players) {
			for (Message message : messages) {
				sendPacket(player, message);
			}
		}
	}

	/**
	 * This method takes in a message and sends it to a specific player
	 *
	 * @param player specific player to relieve message
	 * @param message specific message to send.
	 */
	public void sendPacket(Player player, Message message) {
		player.getSession().send(message);
	}

	/**
	 * Moves this controller.
	 *
	 * @param vect the vector that is applied as the movement.
	 */
	public void move(Vector3 vect) {
		getParent().translate(vect);
	}

	/**
	 * Uses the current velocity and maximum speed to move this entity
	 */
	public void move() {
		this.movedVelocity = Vector3.min(this.velocity, new Vector3(this.maxSpeed, this.maxSpeed, this.maxSpeed));
		this.move(this.movedVelocity);
	}

	/**
	 * Moves this controller
	 *
	 * @param x x-axis to move the controller along
	 * @param y y-axis to move the controller along
	 * @param z z-axis to move the controller along
	 */
	public void move(float x, float y, float z) {
		move(new Vector3(x, y, z));
	}

	/**
	 * Rotates the controller
	 *
	 * @param rot the quaternion that is applied as the rotation.
	 */
	public void rotate(Quaternion rot) {
		getParent().rotate(rot);
	}

	/**
	 * Rotates the controller
	 *
	 * @param degrees the angle of which to do rotation.
	 * @param x	x-axis to rotate the controller along
	 * @param y	y-axis to rotate the controller along
	 * @param z	z-axis to rotate the controller along
	 */
	public void rotate(float degrees, float x, float y, float z) {
		getParent().rotate(degrees, x, y, z);
	}

	/**
	 * Rolls this controller along an angle.
	 *
	 * @param angle the angle in-which to roll
	 */
	public void roll(float angle) {
		getParent().roll(angle);
	}

	/**
	 * If a child controller needs a random number for anything, they should call
	 * this method. This eliminates needless random objects created all the time.
	 *
	 * @return random object.
	 */
	public Random getRandom() {
		return rand;
	}

}
