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
package org.spout.vanilla.protocol.container;

import org.spout.api.component.type.BlockComponent;
import org.spout.api.geo.cuboid.BlockComponentContainer;
import org.spout.api.geo.cuboid.BlockContainer;
import org.spout.api.geo.cuboid.Chunk;
import org.spout.api.geo.cuboid.ContainerFillOrder;
import org.spout.api.geo.cuboid.LightContainer;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.block.BlockFullState;
import org.spout.api.util.hashing.NibblePairHashed;

import static org.spout.vanilla.material.VanillaMaterials.getMinecraftData;
import static org.spout.vanilla.material.VanillaMaterials.getMinecraftId;

public class VanillaContainer implements BlockContainer, LightContainer, BlockComponentContainer {
	private static final int HALF_VOLUME = Chunk.BLOCKS.HALF_VOLUME;
	private static final int VOLUME = Chunk.BLOCKS.VOLUME;
	private int index;
	private boolean evenLight;
	private final byte[] fullChunkData;
	private BlockMaterial material1;
	private BlockMaterial material2;
	private short nibbleStore;
	private BlockComponent[] components;
	private int[] componentX;
	private int[] componentY;
	private int[] componentZ;

	public VanillaContainer() {
		fullChunkData = new byte[HALF_VOLUME * 5];
		index = 0;
	}

	@Override
	public ContainerFillOrder getOrder() {
		return ContainerFillOrder.XZY;
	}

	@Override
	public void setBlockFullState(int state) {
		short id = BlockFullState.getId(state);
		short data = BlockFullState.getData(state);
		if ((index & 1) == 0) {
			material1 = BlockMaterial.get(id, data);
			nibbleStore = getMinecraftData(material1, data);
		} else {
			material2 = BlockMaterial.get(id, data);
			nibbleStore |= getMinecraftData(material2, data) << 4;
			fullChunkData[index - 1] = (byte) (getMinecraftId(material1) & 0xFF);
			fullChunkData[index] = (byte) (getMinecraftId(material2) & 0xFF);
			fullChunkData[(index >> 1) + VOLUME] = (byte) nibbleStore;
		}
		index++;
	}

	public void setLightMode(boolean blockLight) {
		if (blockLight) {
			index = HALF_VOLUME * 3;
		} else {
			index = HALF_VOLUME * 4;
		}
		evenLight = true;
	}

	@Override
	public void setLightLevel(byte light) {
		if (evenLight) {
			nibbleStore = light;
		} else {
			nibbleStore = NibblePairHashed.key(light, nibbleStore & 0x0F);
			fullChunkData[index++] = (byte) nibbleStore;
		}
		evenLight = !evenLight;
	}

	public byte[] getChunkFullData() {
		return fullChunkData;
	}

	@Override
	public void setBlockComponent(int x, int y, int z, BlockComponent component) {
		components[index] = component;
		componentX[index] = x;
		componentY[index] = y;
		componentZ[index] = z;
		index++;
	}

	@Override
	public void setBlockComponentCount(int count) {
		components = new BlockComponent[count];
		componentX = new int[count];
		componentY = new int[count];
		componentZ = new int[count];

		index = 0;
	}

	public BlockComponent[] getBlockComponent() {
		return components;
	}

	public int[] getXArray() {
		return componentX;
	}

	public int[] getYArray() {
		return componentY;
	}

	public int[] getZArray() {
		return componentZ;
	}

	@Override
	public int getBlockComponentCount() {
		return components.length;
	}
}
