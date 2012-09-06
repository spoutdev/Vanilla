package org.spout.vanilla.components.substance;

import org.spout.api.component.components.EntityComponent;
import org.spout.api.data.Data;
import org.spout.api.inventory.ItemStack;

import org.spout.vanilla.data.VanillaData;

public class Item extends EntityComponent {
	@Override
	public boolean canTick() {
		return true;
	}

	@Override
	public void onTick(float dt) {
		if (getUncollectableTicks() > 0) {
			setUncollectableTicks(getUncollectableTicks() - 1);
		}
	}

	public ItemStack getItemStack() {
		return getData().get(Data.HELD_ITEM);
	}

	public void setItemStack(ItemStack stack) {
		getData().put(Data.HELD_ITEM, stack);
	}

	public int getUncollectableTicks() {
		return getData().get(VanillaData.UNCOLLECTABLE_TICKS);
	}

	public void setUncollectableTicks(int uncollectableTicks) {
		getData().put(VanillaData.UNCOLLECTABLE_TICKS, uncollectableTicks);
	}
}
