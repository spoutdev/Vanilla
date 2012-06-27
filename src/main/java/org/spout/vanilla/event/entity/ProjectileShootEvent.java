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
package org.spout.vanilla.event.entity;

import org.spout.api.entity.Entity;
import org.spout.api.event.Cancellable;
import org.spout.api.event.HandlerList;
import org.spout.api.event.entity.EntityEvent;

import org.spout.vanilla.controller.object.Projectile;

/**
 * Event called when an Entity shoots a Projectile. Not yet implemented.
 */
public class ProjectileShootEvent extends EntityEvent implements Cancellable {
	private static HandlerList handlers = new HandlerList();

	private final Entity shooter;
	private Projectile projectile;

	public ProjectileShootEvent(Entity shooter, Projectile projectile) {
		super(shooter);
		this.shooter = shooter;
		this.projectile = projectile;
	}

	/**
	 * Gets the entity that is shooting the projectile.
	 *
	 * @return The Entity that is shooting the projectile.
	 */
	public Entity getShooter() {
		return shooter;
	}

	/**
	 * Gets the projectile to be shot.
	 *
	 * @return The Projectile to be shot.
	 */
	public Projectile getProjectile() {
		return projectile;
	}

	/**
	 * Sets the projectile to be shot.
	 *
	 * @param projectile The Projectile to be shot.
	 */
	public void setProjectile(Projectile projectile) {
		this.projectile = projectile;
	}

	@Override
	public void setCancelled(boolean cancelled) {
		super.setCancelled(cancelled);
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}