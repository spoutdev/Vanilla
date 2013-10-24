/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011 Spout LLC <http://www.spout.org/>
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
package org.spout.vanilla.component.entity.player;

import org.spout.api.geo.discrete.Point;
import org.spout.api.geo.discrete.Transform;
import org.spout.api.render.Camera;
import org.spout.api.render.ViewFrustum;

import org.spout.math.matrix.Matrix4f;
import org.spout.math.vector.Vector4f;
import org.spout.vanilla.component.entity.misc.EntityHead;

public class PlayerHead extends EntityHead implements Camera {
	private Matrix4f projection;
	private Matrix4f view;
	private ViewFrustum frustum = new ViewFrustum();
	private float fieldOfView = 75f;

	public void setScale(float scale) { //1/2
		projection = Matrix4f.createPerspective(fieldOfView * scale, 4.0f / 3.0f, .001f * scale, 1000f * scale);
		updateView();
	}

	@Override
	public void onAttached() {
		// TODO Get FOV
		projection = Matrix4f.createPerspective(fieldOfView, 4.0f / 3.0f, .001f, 1000f);
		updateView();
	}

	@Override
	public Matrix4f getProjection() {
		return projection;
	}

	@Override
	public Matrix4f getView() {
		return view;
	}

	@Override
	public void updateView() {
		Transform transform = getOwner().getPhysics().getTransform();
		Point point = transform.getPosition().add(0.0f, getHeight(), 0.0f);
		Matrix4f pos = Matrix4f.createTranslation(point.mul(-1));
		Matrix4f rot = getRotation();
		view = pos.mul(rot);
		frustum.update(projection, view, transform.getPosition());
	}

	@Override
	public void updateReflectedView() {
		Transform transform = getOwner().getPhysics().getTransform();
		Point point = transform.getPosition().add(0.0f, getHeight(), 0.0f);
		Matrix4f pos = Matrix4f.createTranslation(point.mul(-1));
		Matrix4f rot = getRotation();
		view = Matrix4f.createScaling(new Vector4f(1, -1, 1, 1)).mul(pos).mul(rot);
		frustum.update(projection, view, transform.getPosition());
	}

	@Override
	public boolean canTick() {
		return false;
	}

	@Override
	public ViewFrustum getFrustum() {
		return frustum;
	}

	@Override
	public Matrix4f getRotation() {
		return Matrix4f.createRotation(getOrientation());
	}
}
