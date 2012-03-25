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
package org.spout.vanilla.controller.object.vehicle;

import org.spout.api.collision.BoundingBox;
import org.spout.api.collision.CollisionModel;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.discrete.Point;
import org.spout.api.material.Material;
import org.spout.api.material.block.BlockFace;
import org.spout.api.math.Vector2;
import org.spout.api.math.Vector3;
import org.spout.vanilla.VanillaMaterials;
import org.spout.vanilla.controller.ControllerType;
import org.spout.vanilla.controller.object.Substance;
import org.spout.vanilla.material.block.MinecartTrack;
import org.spout.vanilla.material.block.Solid;
import org.spout.vanilla.material.block.data.PoweredRails;
import org.spout.vanilla.material.block.data.Rails;
import org.spout.vanilla.util.RailsState;

public abstract class Minecart extends Substance implements Vehicle {
	private Material railType = VanillaMaterials.AIR;
	private Rails railData;
	private Vector3 groundFrictionModifier = new Vector3(0.5, 0.5, 0.5);
	private Vector3 airFrictionModifier = new Vector3(0.95, 0.95, 0.95);
	private double maximumVelocity = 0.4;
	private Vector3 realVelocity = Vector3.ZERO;
	private Vector2[] railMovement = new Vector2[] {Vector2.ZERO, Vector2.ZERO};
	private float previousPosY = 0.0f;
	
	private Block railsBlock;

	//TODO: Turn into collision volume (box) instead
	private float width = 0.7F;
	private float length = 0.98F;
	private float height = 0.49F;
	
	public final double getMaximumVelocity() {
		return this.maximumVelocity;
	}
	
	public final Vector3 getGroundFrictionModifier() {
		return this.groundFrictionModifier;
	}
	
	public final Vector3 getAirFrictionModifier() {
		return this.airFrictionModifier;
	}
	
	public void setGroundFrictionModifier(Vector3 friction) {
		this.groundFrictionModifier = friction;
	}
	
	public void setAirFrictionModifier(Vector3 friction) {
		this.airFrictionModifier = friction;
	}
	
	public void setMaximumVelocity(double maximum) {
		this.maximumVelocity = maximum;
	}
	
	@Override
	public void onAttached() {
		super.onAttached();
		getParent().setData(ControllerType.KEY, ControllerType.MINECART.id);
		
		BoundingBox shape = new BoundingBox(0.0F, 0.0F, 0.0F, width, height, length);
		shape.offset(-width / 2, 0.0F, -length / 2);
		this.getParent().setCollision(new CollisionModel(shape));
	}
	
	public void generateRailData() {
		this.railsBlock = this.getParent().getPosition().getWorld().getBlock(this.getParent().getPosition());
		this.railType = this.railsBlock.getMaterial();
		if (!(this.railType instanceof MinecartTrack)) {
			this.railsBlock = this.railsBlock.move(BlockFace.BOTTOM);
			this.railType = this.railsBlock.getMaterial();
		}
		if (this.railType instanceof MinecartTrack) {
			this.railData = (Rails) this.railsBlock.createData();
			this.railMovement[0] = this.railData.getDirections()[0].getOffset().toVector2();
			this.railMovement[1] = this.railData.getDirections()[1].getOffset().toVector2();
		} else {
			this.railData = null;
		}
	}
	
	@Override
	public void onTick(float dt) {
		super.onTick(dt);

		//TODO: store last known positions

		//update fire ticks
		int fireticks = this.getFireTicks();
		if (fireticks > 0) {
			this.setFireTicks(fireticks - 1);
		}

		//TODO: update health to regenerate

		//get current rails below minecart
		this.generateRailData();
		
		Vector3 velocity = this.getVelocity();
		if (this.railData != null) {
			//on tracks
			Vector3 position = this.getParent().getPosition();
			this.previousPosY = position.getY();
			position = position.multiply(1, 0, 1).add(0f, (float) this.railsBlock.getY() + this.height, 0f);
			
			final float slopedMotion = 0.0078125f;
			
			//Move a minecart up or down sloped tracks
			if (this.railData.isSloped()) {
				position = position.add(0.0f, 1.0f, 0.0f);
				velocity = velocity.add(this.railMovement[0].getX() * slopedMotion, 0.0f, this.railMovement[0].getY() * slopedMotion);
			}

			//rail motion is calculated from the rails
			Vector2 railMotion = this.railMovement[1].subtract(this.railMovement[0]);

			//reverse motion if needed
			if ((velocity.getX() * railMotion.getX() + velocity.getZ() * railMotion.getY()) < 0.0) {
				railMotion = railMotion.multiply(-1);
			}

			//rail motion is applied (railFactor is used to normalize the rail motion to current motion)
			float railFactor = (float) getScale(railMotion.getX(), railMotion.getY(), velocity.getX(), velocity.getZ());
			velocity = new Vector3(railMotion.getX(), velocity.getY(), railMotion.getY());
			velocity = velocity.multiply(railFactor, 1.0, railFactor);
			
			//slows down minecarts on unpowered booster rails
			if (this.railData instanceof PoweredRails && !((PoweredRails) this.railData).isPowered()) {
				if (velocity.lengthSquared() < 0.0009) {
					velocity = Vector3.ZERO;
				} else {
					velocity = velocity.multiply(0.5, 0.0, 0.5);
				}
			}
			
			//location is updated to follow the tracks
			Vector2 railOffset = new Vector2(this.railsBlock.getX() + 0.5f, this.railsBlock.getZ() + 0.5f);
			Vector2 oldRail = railOffset.add(this.railMovement[0].multiply(0.5f));

			//positional adjustment (center in middle of tracks)
			railMotion = railOffset.add(this.railMovement[1].multiply(0.5f)).subtract(oldRail);

			Vector2 factor;
			if (railMotion.getX() == 0) {
				factor = new Vector2(1, position.getZ() - this.railsBlock.getY());
			} else if (railMotion.getY() == 0) {
				factor = new Vector2(position.getX() - this.railsBlock.getX(), 1);
			} else {
				double f = railMotion.getX() * (position.getX() - oldRail.getX()) + railMotion.getY() * (position.getZ() - oldRail.getY());
				f *= 2.0;
				factor = new Vector2(f, f);
			}
			railMotion = railMotion.multiply(factor);
			position = oldRail.add(railMotion).toVector3(position.getY());
			
			this.getParent().setPosition(new Point(position, this.getParent().getWorld()));
		} else if (this.railType == VanillaMaterials.AIR) {
			//in the air
			velocity = velocity.multiply(this.airFrictionModifier);
		} else {
			//on the ground
			velocity = velocity.multiply(this.groundFrictionModifier);
		}

		//update velocity
		this.setVelocity(velocity);

		//limit movement and move
		this.realVelocity = Vector3.min(velocity, new Vector3(this.maximumVelocity, this.maximumVelocity, this.maximumVelocity));
		this.getParent().translate(this.realVelocity);
		
		//post-move updates
		if (this.railData != null) {
			//snap to correct Y on block change
			if (this.railData.isSloped()) {
				//TODO: update locY on sloped rails
			}
			
			//perform some post move updates
			this.onPostMove(dt);
			
			//ensure that the minecart Y is correct
			if (this.railData.isSloped()) {
				
				
				
				
				
				
				
			} else {
				
			}
			
			//powered track boosting
			//Launch on powered rails
			if (this.railData instanceof PoweredRails && ((PoweredRails) this.railData).isPowered()) {
				velocity = this.getVelocity();
				double velLength = velocity.length();
				if (velLength > 0.01) {
					//simple motion boosting when already moving
					double launchFactor = 0.06D / velLength;
					Vector3 factor = velocity.multiply(launchFactor, 0.0, launchFactor);
					velocity = velocity.add(factor);
				} else {
					//push a minecart slightly forward when hitting a solid block
					BlockFace pushDirection = null;
					if (this.railData.getState() == RailsState.SOUTH) {
						if (this.railsBlock.clone().move(BlockFace.SOUTH).getMaterial() instanceof Solid) {
							pushDirection = BlockFace.NORTH;
						} else if (this.railsBlock.clone().move(BlockFace.NORTH).getMaterial() instanceof Solid) {
							pushDirection = BlockFace.SOUTH;
						}
					} else if (this.railData.getState() == RailsState.WEST) {
						if (this.railsBlock.clone().move(BlockFace.WEST).getMaterial() instanceof Solid) {
							pushDirection = BlockFace.EAST;
						} else if (this.railsBlock.clone().move(BlockFace.EAST).getMaterial() instanceof Solid) {
							pushDirection = BlockFace.WEST;
						}
					}
					if (pushDirection != null) { 
						velocity = pushDirection.getOffset().multiply(0.02);
					}
				}
				this.setVelocity(velocity);
			}
			
			//let others know we are DONE here
			this.onVelocityUpdated(dt);
		}
		
		//update yaw
		float newyaw = (float) Math.toDegrees(Math.atan2(velocity.getX(), velocity.getZ()));
		if (getAngleDifference(this.getParent().getYaw(), newyaw) > 170f) {
			newyaw = normalAngle(newyaw + 180f);
		}
		this.getParent().setYaw(newyaw);
		
		//TODO: move events?
	}
	
	public void onPostMove(float dt) {
		Vector3 velocity = this.getVelocity();
		velocity = velocity.multiply(0.997, 0.0, 0.997);
		this.setVelocity(velocity);
	}
	
	public void onVelocityUpdated(float dt) {
	}

	//TODO: Place in MathHelper class?
	public static float getAngleDifference(float angle1, float angle2) {
		return Math.abs(normalAngle(angle1 - angle2));
	}
	public static float normalAngle(float angle) {
		while (angle <= -180) angle += 360;
		while (angle > 180) angle -= 360;
		return angle;
	}
	public static double getScale(double fromX, double fromZ, double toX, double toZ) {
		return getScaleSquared(fromX * fromX + fromZ * fromZ, toX * toX + toZ * toZ);
	}
	public static double getScaleSquared(double fromSquared, double toSquared) {
		return Math.sqrt(fromSquared / toSquared);
	}
	
}
