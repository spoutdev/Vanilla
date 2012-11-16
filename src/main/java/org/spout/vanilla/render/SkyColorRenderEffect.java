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
package org.spout.vanilla.render;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.spout.api.render.WorldRenderEffect;

public class SkyColorRenderEffect implements WorldRenderEffect {
	
	private final static FloatBuffer ambient = BufferUtils.createFloatBuffer(4);
	private final static FloatBuffer diffuse = BufferUtils.createFloatBuffer(4);
	private final static FloatBuffer specular = BufferUtils.createFloatBuffer(4);
	
	private final static FloatBuffer lightDir = BufferUtils.createFloatBuffer(4);
	
	private final static FloatBuffer white = BufferUtils.createFloatBuffer(4);
	
	static{
		ambient.clear();
		ambient.put(0.1f).put(0.1f).put(0.1f).put(1f);
		ambient.flip();
		
		diffuse.clear();
		diffuse.put(0.75f).put(0.75f).put(0.75f).put(1f);
		diffuse.flip();
		
		specular.clear();
		specular.put(1f).put(1f).put(1f).put(1f);
		specular.flip();
		
		lightDir.clear();
		lightDir.put(1f).put(0f).put(0f).put(0f);
		lightDir.flip();
		
		white.clear();
		white.put(1f).put(1f).put(1f).put(1f);
		white.flip();
	}
	
	@Override
	public void preRender() {		
		GL11.glEnable(GL11.GL_LIGHTING);
		//GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		GL11.glEnable(GL11.GL_LIGHT0);

		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_AMBIENT, ambient);
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_SPECULAR, specular);
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_DIFFUSE, diffuse);
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, lightDir);
		
		GL11.glMaterial(GL11.GL_FRONT,GL11.GL_AMBIENT_AND_DIFFUSE, white);
		GL11.glMaterial(GL11.GL_FRONT,GL11.GL_SPECULAR, white);
	}

	@Override
	public void postRender() {
		GL11.glDisable(GL11.GL_LIGHTING);
		//GL11.glDisable(GL11.GL_COLOR_MATERIAL);
		GL11.glDisable(GL11.GL_LIGHT0);	
	}

}
