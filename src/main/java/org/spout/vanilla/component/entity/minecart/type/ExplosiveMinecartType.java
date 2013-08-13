package org.spout.vanilla.component.entity.minecart.type;

import org.spout.api.inventory.ItemStack;
import org.spout.api.material.BlockMaterial;
import org.spout.vanilla.component.entity.minecart.MinecartType;
import org.spout.vanilla.component.entity.misc.DeathDrops;
import org.spout.vanilla.material.VanillaMaterials;

public class ExplosiveMinecartType extends MinecartType {

	@Override
	public void onAttached() {
		super.onAttached();
		if (getAttachedCount() == 1) {
			getOwner().add(DeathDrops.class).addDrop(new ItemStack(VanillaMaterials.TNT, 1));
		}
	}

	@Override
	public BlockMaterial getDefaultDisplayedBlock() {
		return VanillaMaterials.TNT;
	}
}
