package org.spout.vanilla.component.entity.minecart.type;

import org.spout.api.material.BlockMaterial;
import org.spout.vanilla.component.entity.minecart.MinecartType;
import org.spout.vanilla.material.VanillaMaterials;

public class SpawnerMinecartType extends MinecartType {

	@Override
	public BlockMaterial getDefaultDisplayedBlock() {
		return VanillaMaterials.MONSTER_SPAWNER;
	}
}
