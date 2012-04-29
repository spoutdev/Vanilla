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

import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.discrete.Point;
import org.spout.api.material.Material;
import org.spout.api.material.block.BlockFace;
import org.spout.api.math.MathHelper;
import org.spout.api.math.Vector2;
import org.spout.api.math.Vector3;

import org.spout.vanilla.controller.VanillaControllerType;
import org.spout.vanilla.controller.object.Substance;
import org.spout.vanilla.controller.source.HealthChangeReason;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.MinecartTrack;
import org.spout.vanilla.material.block.data.PoweredRails;
import org.spout.vanilla.material.block.data.Rails;
import org.spout.vanilla.material.block.generic.Solid;
import org.spout.vanilla.util.RailsState;

public abstract class Minecart extends Substance implements Vehicle {
	private Material railType = VanillaMaterials.AIR;
	private Rails railData;
	private Vector3 groundFrictionModifier = new Vector3(0.5, 0.5, 0.5);
	private Vector3 airFrictionModifier = new Vector3(0.95, 0.95, 0.95);
	private Vector2[] railMovement = new Vector2[]{Vector2.ZERO, Vector2.ZERO};
	private float previousPosY = 0.0f;
	private Block railsBlock;

	protected Minecart(VanillaControllerType type) {
		super(type);
		this.setMaxSpeed(0.4f);
		this.getBounds().set(-0.35f, 0.0f, -0.49f, 0.35f, 0.49f, 0.49f);
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

	public boolean isOnRail() {
		return this.railData != null;
	}

	@Override
	public void onAttached() {
		super.onAttached();
		this.setVelocity(new Vector3(0, 0, 0.2));
		this.getParent().setHealth(40, new HealthChangeReason(HealthChangeReason.Type.SPAWN));
	}

	public void generateRailData(Point position) {
		this.railsBlock = position.getWorld().getBlock(position);
		this.railType = this.railsBlock.getMaterial();
		if (!(this.railType instanceof MinecartTrack)) {
			this.railsBlock = this.railsBlock.translate(BlockFace.BOTTOM);
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

		//update fire ticks
		int fireticks = this.getFireTicks();
		if (fireticks > 0) {
			this.setFireTicks(fireticks - 1);
		}

		//update health to regenerate
		int health = this.getParent().getHealth();
		if (health < 40) {
			this.getParent().setHealth(health + 1, new HealthChangeReason(HealthChangeReason.Type.REGENERATION));
		}

		//get current rails below minecart
		Point position = getParent().getPosition();
		if (position.getWorld() == null) {
			return;
		}
		this.generateRailData(position);

		Vector2 velocity = this.getVelocity().toVector2();
		float velocityY = this.getVelocity().getY() - 0.04f;

		if (this.railData != null) {
			//on tracks
			this.previousPosY = position.getY();

			final float slopedMotion = 0.0078125f;

			//Move a minecart up or down sloped tracks
			if (this.railData.isSloped()) {
				velocity = velocity.subtract(this.railMovement[0].multiply(slopedMotion));
			}

			//rail motion is calculated from the rails
			Vector2 railMotion = this.railMovement[1].subtract(this.railMovement[0]);
			if (railMotion.getX() == -2 || railMotion.getY() == -2) {
				railMotion = railMotion.multiply(-1);
			}

			//reverse motion if needed
			if ((velocity.getX() * railMotion.getX() + velocity.getY() * railMotion.getY()) < 0.0) {
				railMotion = railMotion.multiply(-1);
			}

			//rail motion is applied (railFactor is used to normalize the rail motion to current motion)
			float railFactor = (float) MathHelper.sqrt(velocity.lengthSquared() / railMotion.lengthSquared());
			velocity = railMotion.multiply(railFactor);

			//slows down minecarts on unpowered booster rails
			if (this.railData instanceof PoweredRails && !((PoweredRails) this.railData).isPowered()) {
				if (velocity.lengthSquared() < 0.0009) {
					velocity = Vector2.ZERO;
				} else {
					velocity = velocity.multiply(0.5);
				}
				velocityY = 0f;
			}

			Vector3 railsPosition = this.railsBlock.getPosition().add(0.5f, this.getBounds().getSize().getY(), 0.5f);

			//position is adjusted to snap to the rails
			Vector3 adjustment = railsPosition.subtract(position);

			if (railMotion.getX() == 0) {
				//traveling along Z
				if (this.railData.isSloped()) {
					adjustment = adjustment.add(Vector3.UP);
				}
				adjustment = adjustment.multiply(1f, 1f, 0f);
			} else if (railMotion.getY() == 0) {
				//traveling along X
				if (this.railData.isSloped()) {
					adjustment = adjustment.add(Vector3.UP);
				}
				adjustment = adjustment.multiply(0f, 1f, 1f);
			} else {
				//travel in a corner
				Vector3 adj = adjustment.abs();
				float fact = 1f - 0.5f / (adj.getX() + adj.getZ());
				adjustment = adjustment.multiply(fact);
			}

			//apply adjustment
			this.getParent().setPosition(position = position.add(adjustment));
		} else if (this.railType == VanillaMaterials.AIR) {
			//in the air
			velocity = velocity.multiply(this.airFrictionModifier.toVector2());
			velocityY *= this.airFrictionModifier.getY();
		} else {
			//on the ground
			velocity = velocity.multiply(this.groundFrictionModifier.toVector2());
			velocityY *= this.groundFrictionModifier.getY();
		}

		//update velocity and move
		this.setVelocity(velocity.toVector3(velocityY));
		this.move();

		//perform some post move updates
		this.onPostMove(dt);

		position = this.getParent().getPosition();
		velocity = this.getVelocity().toVector2();
		velocityY = this.getVelocity().getY();

		//post-move updates
		if (this.railData != null) {
			//snap to correct Y when changing sloped rails downwards
			Block newBlock = position.getWorld().getBlock(position);

			Vector2 blockChange = new Vector2(newBlock.getX() - this.railsBlock.getX(), newBlock.getZ() - this.railsBlock.getZ());

			//moved down the slope? Go one down if so
			if (this.railData.isSloped() && blockChange.equals(this.railMovement[0])) {
				position = position.add(new Vector3(0f, -1f, 0f));
				newBlock = newBlock.translate(BlockFace.BOTTOM);
				this.getParent().setPosition(position);
			}

			if (newBlock.getMaterial() instanceof MinecartTrack) {
				//x and z motion slowing down when moving up slopes
				//snap to correct Y (required for proper Y measures)

				//calculate the current Y from the rails (triangle)
				Rails rails = (Rails) newBlock.createData();
				RailsState state = rails.getState();
				float changeY = (newBlock.getY() - position.getY()) + this.getBounds().getSize().getY();
				System.out.println("state=" + state);
				if (state == RailsState.NORTH_SLOPED) {
					changeY += (position.getX() - newBlock.getX()) + 1;
				} else if (state == RailsState.EAST_SLOPED) {
					changeY += (position.getZ() - newBlock.getZ()) + 1;
				} else if (state == RailsState.SOUTH_SLOPED) {
					changeY += (position.getX() - newBlock.getX()) + 1;
				} else if (state == RailsState.WEST_SLOPED) {
					changeY += (position.getZ() - newBlock.getZ()) + 1;
				}

				float velLength = velocity.length();
				if (velLength > 0.01f) {
					double slopeSlowDown = (this.previousPosY - position.getY()) * 0.05 / velLength + 1.0;
					velocity = velocity.multiply(slopeSlowDown);
				}

				position = position.add(new Vector3(0f, changeY, 0f));
			}
			this.getParent().setPosition(position);

			//make sure velocity follows block changes
			position = this.getParent().getPosition();

			if (blockChange.getX() != 0f || blockChange.getY() != 0f) {
				velocity = blockChange.multiply(velocity.length());
			}

			//powered track boosting
			//Launch on powered rails
			if (this.railData instanceof PoweredRails && ((PoweredRails) this.railData).isPowered()) {
				double velLength = velocity.toVector3(velocityY).length();
				if (velLength > 0.01) {
					//simple motion boosting when already moving
					//take part of velocity and add
					velocity = velocity.add(velocity.multiply(0.06 / velLength));
				} else {
					//push a minecart slightly forward when hitting a solid block
					for (BlockFace dir : this.railData.getDirections()) {
						if (this.railsBlock.translate(dir).getMaterial() instanceof Solid) {
							velocity = dir.getOpposite().getOffset().toVector2().multiply(0.02);
							break;
						}
					}
				}
			}
		}

		//update yaw
		float newyaw = (float) Math.toDegrees(Math.atan2(velocity.getX(), velocity.getY()));
		if (MathHelper.getAngleDifference(this.getParent().getYaw(), newyaw) > 170f) {
			newyaw = MathHelper.wrapAngle(newyaw + 180f);
		}
		this.getParent().setYaw(newyaw);

		//finally update velocity and let others know we are DONE here
		this.setVelocity(velocity.toVector3(velocityY));
		this.onVelocityUpdated(dt);

		//TODO: move events?
	}

	public void onPostMove(float dt) {
		if (this.isOnRail()) {
			Vector3 velocity = this.getVelocity();
			velocity = velocity.multiply(0.997, 0.0, 0.997);
			this.setVelocity(velocity);
		}
	}

	/**
	 * Fired when all velocity updating is finished
	 * Velocity changes at this point have no effect for the current tick
	 * @param dt
	 */
	public void onVelocityUpdated(float dt) {
	}
}
