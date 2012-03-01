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
 * the MIT license and the SpoutDev license version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license,
 * including the MIT license.
 */
package org.spout.vanilla.entity;


import org.spout.api.collision.BoundingBox;
import org.spout.api.collision.CollisionModel;
import org.spout.api.geo.discrete.Point;
import org.spout.api.math.Vector3;
import org.spout.api.math.Vector3m;
import org.spout.vanilla.VanillaMaterials;

/**
 * Moving entity controller
 */
public abstract class MovingEntity extends MinecraftEntity {
	protected final BoundingBox area = new BoundingBox(-0.3F, 0F, -0.3F, 0.3F, 0.8F, 0.3F);
	protected final Vector3m velocity = new Vector3m(Vector3.ZERO);
	private int fireTicks;
	private boolean flammable;

	@Override
	public void onAttached() {
		super.onAttached();
		parent.setCollision(new CollisionModel(area));
	}

	@Override
	public void onTick(float dt) {
		if(parent.isDead()) return;
		checkWeb();
		updateMovement(dt);
		checkFireTicks();
		super.onTick(dt);
	}

	private void updateMovement(float dt) {
		/*
		final Pointm location = parent.getPoint();
		//List<BoundingBox> colliding = this.getCollidingBoundingBoxes();
		final BoundingBox position = area.clone().offset(location);

		Vector3m offset = velocity.clone();
		for (BoundingBox box : colliding) {
			Vector3 collision = CollisionHelper.getCollision(position, box);
			if (collision != null) {
				collision = collision.subtract(location);
				//System.out.println("Collision: " + collision);
				if (collision.getX() != 0F) {
					offset.setX(collision.getX());
				}
				if (collision.getY() != 0F) {
					offset.setY(collision.getY());
				}
				if (collision.getZ() != 0F) {
					offset.setZ(collision.getZ());
				}
			}
		}

		//if (colliding.size() > 0)
		//	System.out.println("Old: " + velocity + " New: " + offset + " Colliding: " + colliding.size());


		if (offset.getX() != velocity.getX()) {
			velocity.setX(0);
		}
		if (offset.getY() != velocity.getY()) {
			velocity.setY(0);
		}
		if (offset.getZ() != velocity.getZ()) {
			velocity.setZ(0);
		}

		location.add(offset);
		Point old = parent.getPoint();
		parent.setPoint(location);
		//if (colliding.size() > 0)
		//	System.out.println("Moved from " + old + " to " + parent.getPoint() + ". Expected: " + location);
		 * */

	}

	@Override
	public void preSnapshot() {

	}

	private void checkWeb() {
		Point pos = parent.getPoint();
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
