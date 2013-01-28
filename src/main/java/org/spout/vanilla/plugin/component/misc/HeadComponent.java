/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, Spout LLC <http://www.spout.org/>
 * Vanilla is licensed under the Spout License Version 1.
 *
 * Vanilla is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the Spout License Version 1.
 *
 * Vanilla is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for
 * more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the Spout License Version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://spout.in/licensev1> for the full license, including
 * the MIT license.
 */
package org.spout.vanilla.plugin.component.misc;

import org.spout.api.component.type.EntityComponent;
import org.spout.api.geo.discrete.Point;
import org.spout.api.geo.discrete.Transform;
import org.spout.api.math.Quaternion;
import org.spout.api.math.QuaternionMath;
import org.spout.api.math.Vector3;
import org.spout.api.math.VectorMath;
import org.spout.api.util.BlockIterator;

import org.spout.vanilla.plugin.data.VanillaData;

/**
 * Component that controls the rotation of a head on Vanilla resources.entities.
 */
public class HeadComponent extends EntityComponent {
	private Quaternion lastRotation = Quaternion.IDENTITY;

	@Override
	public void onAttached() {

	}

	/**
	 * Checks whether this head has changed rotation since last tick
	 * @return True if the head rotation is dirty, False if not
	 */
	public boolean isDirty() {
		return !lastRotation.equals(getRotation());
	}

	/**
	 * Sets the rotation of the head to look into a certain direction
	 * @param lookingAt {@link org.spout.api.math.Vector3} to look at
	 */
	public void setLooking(Vector3 lookingAt) {
		setRotation(QuaternionMath.rotationTo(Vector3.FORWARD, lookingAt));
	}

	/**
	 * Gets the {@link Vector3} the head is currently looking at.
	 * @return Head direction vector
	 */
	public Vector3 getLookingAt() {
		return VectorMath.getDirection(getRotation());
	}

	/**
	 * Sets the rotation of the head
	 * @param rotation to set to
	 */
	public void setRotation(Quaternion rotation) {
		lastRotation = getRotation();
		getData().put(VanillaData.HEAD_ROTATION, rotation);
	}

	/**
	 * Gets the rotation of the head
	 * @return Head rotation
	 */
	public Quaternion getRotation() {
		return getData().get(VanillaData.HEAD_ROTATION);
	}

	/**
	 * Sets the current height of the head above the main position
	 * @param height
	 */
	public void setHeight(int height) {
		getData().put(VanillaData.HEAD_HEIGHT, height);
	}

	/**
	 * Gets the current height of the head above the main position
	 * @return Head height
	 */
	public int getHeight() {
		return getData().get(VanillaData.HEAD_HEIGHT);
	}

	/**
	 * Gets the position of the head in the world
	 * @return Head position
	 */
	public Point getPosition() {
		return getOwner().getScene().getPosition().add(0.0f, this.getHeight(), 0.0f);
	}

	/**
	 * Gets the transform of this head in the world
	 * @return Head transform
	 */
	public Transform getHeadTransform() {
		Transform trans = new Transform();
		trans.setPosition(this.getPosition());
		trans.setRotation(getRotation());
		return trans;
	}

	/**
	 * Gets a block iterator that iterates the blocks this head can see<br>
	 * The view distance is limited to the reach of the Entity
	 * @return Block iterator
	 */
	public BlockIterator getBlockView() {
		return getBlockView(getData().get(VanillaData.INTERACT_REACH));
	}

	/**
	 * Gets a block iterator that iterates the blocks this head can see
	 * @param maxDistance the blocks can be iterated
	 * @return Block iterator
	 */
	public BlockIterator getBlockView(int maxDistance) {
		return new BlockIterator(getOwner().getWorld(), getHeadTransform(), maxDistance);
	}
}
