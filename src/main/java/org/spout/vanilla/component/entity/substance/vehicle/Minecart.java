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
package org.spout.vanilla.component.entity.substance.vehicle;

import org.spout.api.component.entity.PhysicsComponent;
import org.spout.api.datatable.ManagedMap;
import org.spout.api.inventory.ItemStack;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.Material;
import org.spout.api.math.Vector3;

import org.spout.physics.collision.shape.BoxShape;

import org.spout.vanilla.component.entity.minecart.MinecartType;
import org.spout.vanilla.component.entity.minecart.type.RideableMinecartType;
import org.spout.vanilla.component.entity.misc.DeathDrops;
import org.spout.vanilla.component.entity.misc.MetadataComponent;
import org.spout.vanilla.data.Metadata;
import org.spout.vanilla.data.VanillaData;
import org.spout.vanilla.material.VanillaBlockMaterial;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.protocol.entity.object.ObjectEntityProtocol;
import org.spout.vanilla.protocol.entity.object.ObjectType;

public class Minecart extends VehicleSubstance {
	@Override
	public void onAttached() {
		super.onAttached();
		setEntityProtocol(new ObjectEntityProtocol(ObjectType.MINECART));

		if (getAttachedCount() == 1) {
			// Set drops
			getOwner().add(DeathDrops.class).addDrop(new ItemStack(VanillaMaterials.MINECART, 1));
			// Set the displayed block
			setDisplayedBlock(getType().getDefaultDisplayedBlock());
		}

		PhysicsComponent physics = getOwner().getPhysics();
		physics.activate(1f, new BoxShape(0.98f, 0.49f, 0.7f), false, true);

		// Add metadata for the shaking of the Minecart and the displayed Block
		MetadataComponent metadata = getOwner().add(MetadataComponent.class);
		metadata.addMeta(Metadata.TYPE_INT, 17, VanillaData.MINECART_SHAKE_FORCE);
		metadata.addMeta(Metadata.TYPE_INT, 18, VanillaData.MINECART_SHAKE_DIR);
		metadata.addMeta(new Metadata<Integer>(Metadata.TYPE_INT, 20) {
			@Override
			public Integer getValue() {
				BlockMaterial material = getDisplayedBlock();
				if (material instanceof VanillaBlockMaterial) {
					VanillaBlockMaterial vanillaMat = (VanillaBlockMaterial) material;
					return (vanillaMat.getMinecraftId()  & 0xFFFF) | (vanillaMat.getMinecraftData(vanillaMat.getData()) << 16);
				} else {
					return 0;
				}
			}

			@Override
			public void setValue(Integer value) {
				int fullState = value.intValue();
				Material material = VanillaMaterials.getMaterial((short) (fullState & 0xFFFF), (short) (fullState >> 16));
				if (material instanceof BlockMaterial) {
					setDisplayedBlock((BlockMaterial) material);
				} else {
					setDisplayedBlock(VanillaMaterials.AIR);
				}
			}
		});
		metadata.addMeta(Metadata.TYPE_INT, 21, VanillaData.MINECART_BLOCK_OFFSET);
		metadata.addMeta(new Metadata<Byte>(Metadata.TYPE_BYTE, 22) {
			@Override
			public Byte getValue() {
				return getOwner().getData().get(VanillaData.MINECART_BLOCK_ID) == 0 ? (byte) 0 : (byte) 1;
			}

			@Override
			public void setValue(Byte value) {
				if (value.byteValue() != (byte) 1) {
					// Hide it by showing air
					setDisplayedBlock(VanillaMaterials.AIR);
				} else if (getDisplayedBlock() == VanillaMaterials.AIR) {
					// Make visible...make visible what? Just use the default Block for now
					// Hopefully the real block to display will arrive later on
					setDisplayedBlock(Minecart.this.getType().getDefaultDisplayedBlock());
				}
			}
		});
	}

	@Override
	public void damage(float damage) {
		super.damage(damage);
		shake((int) damage);
	}

	@Override
	public void onTick(float dt) {
		super.onTick(dt);

		// Slowly reduce shakiness over time
		int shakeForce = getShakingForce();
		if (shakeForce > 0) {
			setShakingForce(shakeForce - 1);
		}

		getOwner().getPhysics().setMovementVelocity(new Vector3(0.2, 0.0, 0.0));
	}

	@Override
	protected void onDestroy() {
		getType().onDestroy();
		super.onDestroy();
	}

	/**
	 * Shakes this Minecart back and forth
	 *
	 * @param amount to shake (more)
	 */
	public void shake(int amount) {
		ManagedMap data = getOwner().getData();
		data.put(VanillaData.MINECART_SHAKE_DIR, -data.get(VanillaData.MINECART_SHAKE_DIR));
		setShakingForce(getShakingForce() + amount);
	}

	/**
	 * Sets the Minecart Type component of this Minecart
	 * 
	 * @param type to set to
	 */
	public <T extends MinecartType> T setType(Class<T> type) {
		return getOwner().add(type);
	}

	/**
	 * Gets the Minecart Type component of this Minecart
	 * 
	 * @return Minecart Type
	 */
	public MinecartType getType() {
		MinecartType type = getOwner().get(MinecartType.class);
		if (type == null) {
			type = setType(RideableMinecartType.class);
		}
		return type;
	}

	public int getShakingForce() {
		return getOwner().getData().get(VanillaData.MINECART_SHAKE_FORCE);
	}

	public void setShakingForce(int force) {
		getOwner().getData().put(VanillaData.MINECART_SHAKE_FORCE, force);
	}

	/**
	 * Gets the displayed block in this Minecart. If no Block is displayed, AIR is returned.
	 *
	 * @return Displayed Block Material
	 */
	public BlockMaterial getDisplayedBlock() {
		short id = getOwner().getData().get(VanillaData.MINECART_BLOCK_ID);
		short data = getOwner().getData().get(VanillaData.MINECART_BLOCK_DATA);
		BlockMaterial material = BlockMaterial.get(id, data);
		return material == null ? VanillaMaterials.AIR : material;
	}

	/**
	 * Sets the displayed block in this Minecart. To hide the Block, pass in null or AIR.
	 *
	 * @param material to set to
	 */
	public void setDisplayedBlock(BlockMaterial material) {
		BlockMaterial displayed = material;
		if (displayed == null) {
			displayed = VanillaMaterials.AIR;
		}
		getOwner().getData().put(VanillaData.MINECART_BLOCK_ID, material.getId());
		getOwner().getData().put(VanillaData.MINECART_BLOCK_DATA, material.getData());
	}

	public int getDisplayedBlockOffset() {
		return getOwner().getData().get(VanillaData.MINECART_BLOCK_OFFSET);
	}

	public void setDisplayedBlockOffset(int offset) {
		getOwner().getData().put(VanillaData.MINECART_BLOCK_OFFSET, offset);
	}
}
