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
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.spout.vanilla.component.entity.player;

import org.spout.api.component.impl.CameraComponent;
import org.spout.api.geo.discrete.Transform;
import org.spout.api.math.Matrix;
import org.spout.api.math.MatrixMath;
import org.spout.vanilla.component.entity.misc.Head;

import org.spout.api.component.type.EntityComponent;
import org.spout.api.geo.discrete.Transform;
import org.spout.api.math.Matrix;
import org.spout.api.math.MatrixMath;
import org.spout.api.math.Vector3;
import org.spout.api.render.Camera;
import org.spout.api.render.ViewFrustum;

public class VanillaCamera extends EntityComponent implements Camera {
	private Matrix projection;
	private Matrix view;
	private ViewFrustum frustum = new ViewFrustum();
	private float fieldOfView = 75f;

	public void setScale(float scale) { //1/2
		projection = MatrixMath.createPerspective(fieldOfView * scale, 4.0f / 3.0f, .001f * scale, 1000f * scale);
		updateView();
	}

	@Override
	public void onAttached() {
		// TODO Get FOV
		projection = MatrixMath.createPerspective(fieldOfView, 4.0f / 3.0f, .001f, 1000f);
		updateView();
	}

	@Override
	public Matrix getProjection() {
		return projection;
	}

	@Override
	public Matrix getView() {
		return view;
	}

	@Override
	public void updateView() {
		Transform transform = getOwner().getScene().getRenderTransform();
		Matrix pos = MatrixMath.createTranslated(transform.getPosition().multiply(-1));
		view = pos.multiply(getRotation());
		frustum.update(projection, view, transform.getPosition());
	}

	@Override
	public void updateReflectedView() {
		Transform transform = getOwner().getScene().getRenderTransform();
		Matrix pos = MatrixMath.createTranslated(transform.getPosition().multiply(-1, 1, -1));
		view = MatrixMath.createScaled(new Vector3(1,-1,1)).multiply(pos).multiply(getRotation());
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
	public Matrix getRotation() {
		Head head = getOwner().get(Head.class);
		return MatrixMath.createRotated(head.getRotation());
	}
}