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
package org.spout.vanilla.entity;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;

import org.spout.api.Source;
import org.spout.api.Spout;
import org.spout.api.collision.BoundingBox;
import org.spout.api.collision.CollisionModel;
import org.spout.api.collision.CollisionStrategy;
import org.spout.api.entity.BasicController;
import org.spout.api.entity.Entity;
import org.spout.api.entity.Player;
import org.spout.api.geo.discrete.Transform;
import org.spout.api.inventory.ItemStack;
import org.spout.api.math.MathHelper;
import org.spout.api.math.Quaternion;
import org.spout.api.math.Vector3;
import org.spout.api.protocol.event.ProtocolEvent;
import org.spout.api.tickable.TickPriority;

import org.spout.vanilla.data.VanillaData;
import org.spout.vanilla.data.drops.Drops;
import org.spout.vanilla.data.drops.flag.DropFlagSingle;
import org.spout.vanilla.entity.component.HealthOwner;
import org.spout.vanilla.entity.component.basic.FireDamageComponent;
import org.spout.vanilla.entity.component.basic.HealthComponent;
import org.spout.vanilla.entity.component.physics.BlockCollisionComponent;
import org.spout.vanilla.entity.object.moving.Item;

/**
 * Controller that is the parent of all entity controllers.
 */
public abstract class VanillaEntityController extends BasicController implements VanillaController, HealthOwner {
	private final VanillaControllerType type;
	private final BoundingBox area = new BoundingBox(-0.3F, 0F, -0.3F, 0.3F, 0.8F, 0.3F);
	private static Random rand = new Random();
	private Drops drops = new Drops();
	// Protocol: last known updated client transform
	private Transform lastClientTransform = new Transform();
	// Tick effects
	private int positionTicks = 0;
	private int velocityTicks = 0;
	// Velocity-related
	private Vector3 velocity = Vector3.ZERO;
	private Vector3 movementSpeed = Vector3.ZERO;
	private Vector3 maxSpeed = new Vector3(0.4, 0.4, 0.4);
	// Block collision handling
	protected BlockCollisionComponent blockCollisionComponent;
	protected HealthComponent healthComponent;
	protected FireDamageComponent fireDamageComponent;

	protected VanillaEntityController(VanillaControllerType type) {
		super(type);
		this.type = type;
	}

	@Override
	public void onAttached() {
		getParent().setCollision(new CollisionModel(area));
		getParent().getCollision().setStrategy(CollisionStrategy.SOLID);
		getDataMap().put(VanillaData.CONTROLLER_TYPE, getType().getMinecraftId());
		this.lastClientTransform = getParent().getTransform();

		healthComponent = addComponent(new HealthComponent(TickPriority.HIGHEST));
		blockCollisionComponent = addComponent(new BlockCollisionComponent(TickPriority.HIGHEST));
		fireDamageComponent = addComponent(new FireDamageComponent(TickPriority.HIGHEST));

		// Load data
		maxSpeed = getDataMap().get(VanillaData.MAX_SPEED, maxSpeed);
		movementSpeed = getDataMap().get(VanillaData.MOVEMENT_SPEED, movementSpeed);
		if (getDataMap().containsKey(VanillaData.VELOCITY)) {
			velocity = getDataMap().get(VanillaData.VELOCITY);
		}
	}

	@Override
	public void onSave() {
		// Load data
		getDataMap().put(VanillaData.MAX_SPEED, maxSpeed);
		getDataMap().put(VanillaData.MOVEMENT_SPEED, movementSpeed);
		getDataMap().put(VanillaData.VELOCITY, velocity);
	}

	@Override
	public void onTick(float dt) {
		if (this.healthComponent.isDying()) {
			this.healthComponent.onTick(dt);
			return;
		}

		positionTicks++;
		velocityTicks++;

		super.onTick(dt);
	}

	@Override
	public void onDeath() {
		for (ItemStack drop : getDrops(getHealth().getLastDamageCause(), getHealth().getLastDamager())) {
			if (drop == null) {
				continue;
			}
			Item item = new Item(drop, Vector3.ZERO);
			getParent().getLastTransform().getPosition().getWorld().createAndSpawnEntity(getParent().getLastTransform().getPosition(), item);
			// TODO: Drop experience
		}
	}

	public void kill() {
		getParent().kill();
	}

	@Override
	public VanillaControllerType getType() {
		return type;
	}

	@Override
	public void callProtocolEvent(ProtocolEvent event) {
		for (Player player : getParent().getWorld().getNearbyPlayers(getParent(), 160)) {
			if (player == null || player.getNetworkSynchronizer() == null) {
				continue;
			}
			player.getNetworkSynchronizer().callProtocolEvent(event);
		}
	}

	/**
	 * Gets the fire damage component
	 * @return entity fire damage process
	 */
	public FireDamageComponent getFireDamage() {
		return fireDamageComponent;
	}

	/**
	 * Gets the block collision component
	 * @return block collision process
	 */
	public BlockCollisionComponent getCollisionComponent() {
		return blockCollisionComponent;
	}

	@Override
	public HealthComponent getHealth() {
		return healthComponent;
	}

	/**
	 * Sets the last known transformation known by the clients<br>
	 * This should only be called by the protocol classes
	 * @param transform to set to
	 */
	public void setLastClientTransform(Transform transform) {
		this.lastClientTransform = transform.copy();
	}

	/**
	 * Gets the last known transformation updated to the clients
	 * @return the last known transform by the clients
	 */
	public Transform getLastClientTransform() {
		return this.lastClientTransform;
	}

	/**
	 * Gets the bounding box for the entity that this entity supplies
	 * @return the bounding box for the entity
	 */
	public BoundingBox getBounds() {
		return this.area;
	}

	/**
	 * Tests if a velocity update is needed for this entity<br>
	 * This is called by the entity protocol
	 * @return True if a velocity update is needed
	 */
	public boolean needsVelocityUpdate() {
		return velocityTicks % 5 == 0;
	}

	/**
	 * Tests if a position update is needed for this entity<br>
	 * This is called by the entity protocol
	 * @return True if a position update is needed
	 */
	public boolean needsPositionUpdate() {
		return positionTicks % 30 == 0;
	}

	/**
	 * Gets the current velocity of this entity
	 * @return the velocity
	 */
	public Vector3 getVelocity() {
		return velocity;
	}

	/**
	 * Sets the current velocity for this entity
	 * @param velocity to set to
	 */
	public void setVelocity(Vector3 velocity) {
		if (velocity == null) {
			if (Spout.debugMode()) {
				Spout.getLogger().log(Level.SEVERE, "Velocity of " + this.toString() + " set to null!");
				Spout.getLogger().log(Level.SEVERE, "Report this to http://issues.spout.org");
			}
			velocity = Vector3.ZERO;
		}
		this.velocity = velocity;
	}

	/**
	 * Gets the speed of the entity during the prior movement. This will always be lower than the maximum speed.
	 * @return the moved velocity
	 */
	public Vector3 getMovementSpeed() {
		return movementSpeed;
	}

	/**
	 * Gets the maximum speed this entity is allowed to move at once.
	 * @return the maximum velocity
	 */
	public Vector3 getMaxSpeed() {
		return maxSpeed;
	}

	/**
	 * Sets the maximum speed this entity is allowed to move at once.
	 * @param maxSpeed to set to
	 */
	public void setMaxSpeed(Vector3 maxSpeed) {
		this.maxSpeed = maxSpeed;
	}

	/**
	 * Get the drops that Vanilla controllers disperse into the world when un-attached (such as entity death). Children controllers should override this method for their own personal drops.
	 * @param source Source of death
	 * @param lastDamager Controller that killed this entity, can be null if death was caused by natural sources such as drowning or burning
	 * @return the drops to disperse.
	 */
	public List<ItemStack> getDrops(Source source, Entity lastDamager) {
		return this.getDrops().getDrops(rand, new HashSet<DropFlagSingle>());
	}

	/**
	 * Gets the drops for this Entity
	 * @return the drops
	 */
	public Drops getDrops() {
		return this.drops;
	}

	// =========================
	// Controller helper methods
	// =========================

	/**
	 * Moves this entity.
	 * @param vect the vector that is applied as the movement.
	 */
	public void move(Vector3 vect) {
		getParent().translate(vect);
	}

	/**
	 * Uses the current velocity and maximum speed to move this entity.
	 */
	public void move() {
		movementSpeed = MathHelper.min(velocity, maxSpeed);
		move(movementSpeed);
	}

	/**
	 * Moves this entity
	 * @param x x-axis to move the entity along
	 * @param y y-axis to move the entity along
	 * @param z z-axis to move the entity along
	 */
	public void move(float x, float y, float z) {
		move(new Vector3(x, y, z));
	}

	/**
	 * Rotates the entity
	 * @param rot the quaternion that is applied as the rotation.
	 */
	public void rotate(Quaternion rot) {
		getParent().rotate(rot);
	}

	/**
	 * Rotates the entity
	 * @param degrees the angle of which to do rotation.
	 * @param x x-axis to rotate the entity along
	 * @param y y-axis to rotate the entity along
	 * @param z z-axis to rotate the entity along
	 */
	public void rotate(float degrees, float x, float y, float z) {
		getParent().rotate(degrees, x, y, z);
	}

	/**
	 * Sets the yaw angle for this entity
	 * @param angle to set to
	 */
	public void yaw(float angle) {
		getParent().yaw(angle);
	}

	/**
	 * Sets the pitch angle for this entity
	 * @param angle to set to
	 */
	public void pitch(float angle) {
		getParent().pitch(angle);
	}

	/**
	 * Rolls this entity along an angle.
	 * @param angle the angle in-which to roll
	 */
	public void roll(float angle) {
		getParent().roll(angle);
	}

	/**
	 * Gets the position of this entity at the start of this tick
	 * @return previous position
	 */
	public Vector3 getPreviousPosition() {
		return getParent().getLastTransform().getPosition();
	}

	/**
	 * Gets the rotation of this entity at the start of this tick
	 * @return previous rotation
	 */
	public Quaternion getPreviousRotation() {
		return getParent().getLastTransform().getRotation();
	}

	/**
	 * If a child entity needs a random number for anything, they should call this method. This eliminates needless random objects created all the time.
	 * @return random object.
	 */
	public Random getRandom() {
		return rand;
	}
}
