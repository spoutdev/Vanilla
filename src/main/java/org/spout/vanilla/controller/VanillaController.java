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
import org.spout.api.entity.Controller;
import org.spout.api.geo.discrete.Point;
import org.spout.api.inventory.ItemStack;
import org.spout.api.material.BlockMaterial;
import org.spout.api.math.Vector3;
import org.spout.api.player.Player;
import org.spout.api.protocol.Message;

import org.spout.vanilla.VanillaMaterials;
import org.spout.vanilla.protocol.msg.EntityAnimationMessage;
import org.spout.vanilla.protocol.msg.EntityHeadYawMessage;
import org.spout.vanilla.protocol.msg.EntityStatusMessage;

/**
 * Controller that is the parent of all Vanilla controllers.
 */
public abstract class VanillaController extends Controller {
	//Collision box for controllers
	protected final BoundingBox area = new BoundingBox(-0.3F, 0F, -0.3F, 0.3F, 0.8F, 0.3F);
	private static Random rand = new Random();
	private boolean flammable = true, canMove = true;
	private int headYaw = 0, headYawLive = 0, fireTicks = 0;
	private Vector3 lavaField, velocity = Vector3.ZERO;
	//Velocity constants
	private Vector3 waterVelocity = new Vector3(2f, 2f, 2f);
	private Vector3 webVelocity = new Vector3(0.001f, 0.001f, 0.001f);
	private Vector3 soulSandVelocity = new Vector3(2f, 0f, 0f); //TODO Guessed here...needs to be tweaked.
	
	@Override
	public void onAttached() {
		getParent().setCollision(new CollisionModel(area));
	}

	@Override
	public void onTick(float dt) {
		//Check controller health, send messages to the client based on current state.
		if (getParent().getHealth() <= 0) {
			sendMessage(new EntityStatusMessage(getParent().getId(), EntityStatusMessage.ENTITY_DEAD));
			//We can kill the controller here but the tick will continue due to death occurring at Pre-Snapshot and not Stage 1
			getParent().kill();
		}

		//Stop the tick if the parent is dead. No need to do any redundant further tick work if this controller is going to be detached.
		if (getParent().isDead()) {
			return;
		}

		if (headYawLive != headYaw) {
			headYawLive = headYaw;
			sendMessage(new EntityHeadYawMessage(getParent().getId(), headYaw));
		}

		/**
		 * Check to see if the child controller can move. This solves the issue with doing needless checks 
		 * for things that only affect moving controllers and eliminates the need for a MovingController 
		 * abstraction layer.
		 */
		if (canMove) {
			Vector3 temp = determineVelocity();
			if (!temp.equals(velocity)) {
				//Set the new velocity.
				setVelocity(temp);
			}
		}
		
		//Check to see if the controller can be burned.
		if (flammable) {
			checkLava();
		}
	}

	/**
	 * Get the death drops this controller will disperse into the world when it dies. Children controllers
	 * should override this method and adjust the drops for the ControllerType they represent.
	 * @return the drops to disperse.
	 */
	public Set<ItemStack> getDeathDrops() {
		return new HashSet<ItemStack>();
	}

	public void setHeadYaw(int headYaw) {
		headYawLive = headYaw;
	}

	public int getHeadYaw() {
		return headYaw;
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

	public void setVelocity(Vector3 velocity) {
		this.velocity = velocity;
		getParent().translate(velocity);
	}

	/**
	 * If a child controller needs a random number for anything, they should call this method.
	 * This eliminates needless random objects created all the time.
	 * @return random object.
	 */
	public Random getRandom() {
		return rand;
	}

	/**
	 * This sets if a controller moves.
	 * @param canMove true if the controller can move, false if the controller can not.
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

	/**
	 * Damages this controller and sends messages to the client.
	 * @param amount amount the controller will be damaged by.
	 * @param viewer the player that will view the effect   
	 * @param animate true if the entity should show an effect, false if the entity should just be damaged with no
	 *                effect shown to clients.   
	 */
	public void damage(int amount, Player viewer, boolean animate) {
		getParent().setHealth(getParent().getHealth() - amount);
		if (animate) {
			EntityAnimationMessage message1 = new EntityAnimationMessage(getParent().getId(), EntityAnimationMessage.ANIMATION_HURT);
			EntityStatusMessage message2 = new EntityStatusMessage(getParent().getId(), EntityStatusMessage.ENTITY_HURT);
			//If the player is null that means we should send the messages to all players and not just a viewer.
			if (viewer == null) {
				sendMessage(message1, message2);
			} else {
				sendMessage(viewer, message1, message2);
			}
		}
	}

	/**
	 * Damages this controller and sends messages to the client. This method sends to ALL online players in the controller's world.
	 * @param amount amount the controller will be damaged by.   
	 * @param animate true if the entity should show an effect, false if the entity should just be damaged with no
	 *                effect shown to clients.   
	 */	
	public void damage(int amount, boolean animate) {
		damage(amount, null, animate);		
	}

	/**
	 * This method checks to see if the entity came into contact with a block that would change its' velocity.
	 * This has no effect on non-moving controllers.
	 */
	public Vector3 determineVelocity() {
		BlockMaterial waist = getParent().getWorld().getBlock(getParent().getPosition()).getBlockMaterial();
		if (waist.equals(VanillaMaterials.WEB)) {
			if(Spout.getGame().debugMode()) {
				System.out.println("\nDetected moving controller and block that changes velocity!\n------------------\nCurrent controller: " + this.toString() + "\nCurrent block: " + waist.getDisplayName() + "\nCurrent velocity: " + velocity);
			}
			return webVelocity;
		} else if (waist.equals(VanillaMaterials.WATER)) {
			if(Spout.getGame().debugMode()) {
				System.out.println("\nDetected moving controller and block that changes velocity!\n------------------\nCurrent controller: " + this.toString() + "\nCurrent block: " + waist.getDisplayName() + "\nCurrent velocity: " + velocity);
			}
			return waterVelocity;
		}

		/**
		 * If execution got here, that means we should check to see if the block under the controller's position
		 * is a block (y - 1). 
		 */
		float x = getParent().getPosition().getX() - 1;
		float y = getParent().getPosition().getY() - 1;
		float z = getParent().getPosition().getZ();
		
		BlockMaterial feet = getParent().getWorld().getBlock(new Point(getParent().getWorld(), x, y, z)).getBlockMaterial();
		if (feet.equals(VanillaMaterials.WEB)) {
			if(Spout.getGame().debugMode()) {
				System.out.println("\nDetected moving controller and block that changes velocity!\n------------------\nCurrent controller: " + this.toString() + "\nCurrent block: " + waist.getDisplayName() + "\nCurrent velocity: " + velocity);
			}
			return webVelocity;
		} else if (feet.equals(VanillaMaterials.SOUL_SAND)) {
			if(Spout.getGame().debugMode()) {
				System.out.println("\nDetected moving controller and block that changes velocity!\n------------------\nCurrent controller: " + this.toString() + "\nCurrent block: " + waist.getDisplayName() + "\nCurrent velocity: " + velocity);
			}
			return soulSandVelocity;
		} else if(feet.equals(VanillaMaterials.WATER)) {
			if(Spout.getGame().debugMode()) {
				System.out.println("\nDetected moving controller and block that changes velocity!\n------------------\nCurrent controller: " + this.toString() + "\nCurrent block: " + waist.getDisplayName() + "\nCurrent velocity: " + velocity);
			}
			return waterVelocity;
		}
		
		return Vector3.ZERO;
	}
	
	/**
	 * This method takes in any amount of messages and sends them to all players in the world in-which the
	 * message occurred.
	 * @param messages the message(s) to send.
	 */
	public void sendMessage(Message... messages) {
		Set<Player> onlinePlayers = getParent().getWorld().getPlayers();
		for (Player player : onlinePlayers) {
			for (Message message : messages) {
				player.getSession().send(message);
			}
		}
	}

	/**
	 * This method takes in any amount of messages and sends them to a player specified.
	 * @param player player to send message to
	 * @param messages the message(s) to send
	 */
	public void sendMessage(Player player, Message... messages) {
		for (Message message : messages) {
			player.getSession().send(message);
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
				//TODO: damage controller
			}
			--fireTicks;
		}
	}

	private void checkLava() {
		if (lavaField == null) {
			lavaField = area.contract(-0.10000000149011612f, -0.4000000059604645f, -0.10000000149011612f).getPosition();
		}
		Point point = new Point(getParent().getWorld(), lavaField.getX(), lavaField.getY(), lavaField.getZ());
		if (getParent().getWorld().getBlock(point).getBlockMaterial() == VanillaMaterials.LAVA) {
			damage(1, true); //TODO: What are the real values for lava damage per tick again :P
		}
	}
}
