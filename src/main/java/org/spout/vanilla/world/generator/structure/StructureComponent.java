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
package org.spout.vanilla.world.generator.structure;

import java.util.List;
import java.util.Random;

import org.spout.api.generator.WorldGeneratorObject;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.discrete.Point;
import org.spout.api.material.BlockMaterial;
import org.spout.api.math.MathHelper;
import org.spout.api.math.Quaternion;
import org.spout.api.math.Vector3;

import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.Liquid;

public abstract class StructureComponent {
	protected Structure parent;
	protected StructureComponent lastComponent = null;
	protected Point position = Point.invalid;
	protected Quaternion rotation = Quaternion.IDENTITY;
	protected Vector3 rotationPoint = Vector3.ZERO;

	public StructureComponent(Structure parent) {
		this.parent = parent;
	}

	public Random getRandom() {
		return parent.getRandom();
	}

	public Structure getParent() {
		return parent;
	}

	public StructureComponent getLastComponent() {
		return lastComponent;
	}

	public void setLastComponent(StructureComponent lastComponent) {
		this.lastComponent = lastComponent;
	}

	public Block getBlock(int xx, int yy, int zz) {
		return position.getWorld().getBlock(transform(xx, yy, zz), position.getWorld());
	}

	public BlockMaterial getBlockMaterial(int xx, int yy, int zz) {
		final Vector3 transformed = transform(xx, yy, zz);
		return position.getWorld().getBlockMaterial(transformed.getFloorX(), transformed.getFloorY(), transformed.getFloorZ());
	}

	public void setBlockMaterial(int xx, int yy, int zz, BlockMaterial material) {
		setBlockMaterial(xx, yy, zz, material, material.getData());
	}

	public void setBlockMaterial(int xx, int yy, int zz, BlockMaterial material, short data) {
		final Vector3 transformed = transform(xx, yy, zz);
		position.getWorld().setBlockMaterial(transformed.getFloorX(), transformed.getFloorY(), transformed.getFloorZ(),
				material, data, position.getWorld());
		//		if (material instanceof Attachable) {
		//			final Attachable attachable = (Attachable) material;
		//			final Block block = getBlock(xx, yy, zz);
		//			attachable.setAttachedFace(block,
		//					BlockFace.fromYaw(attachable.getAttachedFace(block).getDirection().getYaw() + rotation.getYaw()));
		//		} else if (material instanceof Directional) {
		//			final Directional directional = (Directional) material;
		//			final Block block = getBlock(xx, yy, zz);
		//			directional.setFacing(block,
		//					BlockFace.fromYaw(directional.getFacing(block).getDirection().getYaw() + rotation.getYaw()));
		//		}
	}

	public void randomSetBlockMaterial(float odd, int xx, int yy, int zz, BlockMaterial material) {
		randomSetBlockMaterial(odd, xx, yy, zz, material, material.getData());
	}

	public void randomSetBlockMaterial(float odd, int xx, int yy, int zz, BlockMaterial material, short data) {
		if (getRandom().nextFloat() > odd) {
			setBlockMaterial(xx, yy, zz, material, data);
		}
	}

	public void fillDownwards(int xx, int yy, int zz, short limit, BlockMaterial material) {
		fillDownwards(xx, yy, zz, limit, material, material.getData());
	}

	public void fillDownwards(int xx, int yy, int zz, short limit, BlockMaterial material, short data) {
		short counter = 0;
		Block block;
		while (((block = getBlock(xx, yy, zz)).getMaterial().isMaterial(VanillaMaterials.AIR)
				|| block.getMaterial() instanceof Liquid) && counter++ < limit) {
			block.setMaterial(material, data);
			yy--;
		}
	}

	public void placeObject(int xx, int yy, int zz, WorldGeneratorObject object) {
		final Vector3 transformed = transform(xx, yy, zz);
		if (object.canPlaceObject(position.getWorld(), transformed.getFloorX(), transformed.getFloorY(), transformed.getFloorZ())) {
			object.placeObject(position.getWorld(), transformed.getFloorX(), transformed.getFloorY(), transformed.getFloorZ());
		}
	}

	protected Vector3 transform(int x, int y, int z) {
		return MathHelper.round(MathHelper.transform(new Vector3(x, y, z).subtract(rotationPoint), rotation).add(rotationPoint).add(position));
	}

	public Point getPosition() {
		return position;
	}

	public void setPosition(Point position) {
		this.position = position;
	}

	public void offsetPosition(int x, int y, int z) {
		position = position.add(x, y, z);
	}

	public void offsetPosition(Vector3 offset) {
		offsetPosition(offset.getFloorX(), offset.getFloorY(), offset.getFloorZ());
	}

	public Quaternion getRotation() {
		return rotation;
	}

	public void setRotation(Quaternion rotation) {
		this.rotation = rotation;
	}

	public Vector3 getRotationPoint() {
		return rotationPoint;
	}

	public void setRotationPoint(Vector3 rotationPoint) {
		this.rotationPoint = rotationPoint;
	}

	public abstract boolean canPlace();

	public abstract void place();

	public abstract void randomize();

	public abstract List<StructureComponent> getNextComponents();

	public abstract BoundingBox getBoundingBox();

	public static class BoundingBox {
		private Vector3 min;
		private Vector3 max;

		public BoundingBox(Vector3 min, Vector3 max) {
			this.min = min;
			this.max = max;
		}

		public Vector3 getMax() {
			return max;
		}

		public void setMax(Vector3 max) {
			this.max = max;
		}

		public Vector3 getMin() {
			return min;
		}

		public void setMin(Vector3 min) {
			this.min = min;
		}

		public boolean intersects(BoundingBox box) {
			Vector3 rMax = box.getMax();
			if (rMax.getX() < min.getX()
					|| rMax.getY() < min.getY()
					|| rMax.getZ() < min.getZ()) {
				return false;
			}
			Vector3 rMin = box.getMin();
			if (rMin.getX() > max.getX()
					|| rMin.getY() > max.getY()
					|| rMin.getZ() > max.getZ()) {
				return false;
			}
			return true;
		}

		@Override
		public boolean equals(Object o) {
			if (o == null) {
				return false;
			}
			if (!(o instanceof BoundingBox)) {
				return false;
			}
			final BoundingBox other = (BoundingBox) o;
			return other.max.equals(max) && other.min.equals(min);
		}

		@Override
		public int hashCode() {
			int hash = 5;
			hash = 89 * hash + (min != null ? min.hashCode() : 0);
			hash = 89 * hash + (max != null ? max.hashCode() : 0);
			return hash;
		}
	}
}
