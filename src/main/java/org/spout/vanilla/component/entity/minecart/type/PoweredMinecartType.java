package org.spout.vanilla.component.entity.minecart.type;

import org.spout.api.Platform;
import org.spout.api.Spout;
import org.spout.api.entity.Player;
import org.spout.api.event.entity.EntityInteractEvent;
import org.spout.api.event.player.PlayerInteractEntityEvent;
import org.spout.api.inventory.ItemStack;
import org.spout.api.inventory.Slot;
import org.spout.api.material.BlockMaterial;
import org.spout.vanilla.component.entity.minecart.MinecartType;
import org.spout.vanilla.component.entity.misc.DeathDrops;
import org.spout.vanilla.component.entity.misc.MetadataComponent;
import org.spout.vanilla.data.Metadata;
import org.spout.vanilla.material.Fuel;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.util.PlayerUtil;

public class PoweredMinecartType extends MinecartType {
	private float fuel = 0f;

	@Override
	public void onAttached() {
		super.onAttached();
		if (getAttachedCount() == 1) {
			getOwner().add(DeathDrops.class).addDrop(new ItemStack(VanillaMaterials.FURNACE, 1));
		}

		// Add smoking puff effect metadata
		getOwner().add(MetadataComponent.class).addMeta(new Metadata<Byte>(Metadata.TYPE_BYTE, 16) {
			@Override
			public Byte getValue() {
				return isFueled() ? (byte) 1 : (byte) 0;
			}

			@Override
			public void setValue(Byte value) {
				setFueled(value.byteValue() == (byte) 1);
			}
		});
	}

	@Override
	public void onDetached() {
		super.onDetached();

		// Unregister smoking puff metadata
		MetadataComponent metadata = getOwner().get(MetadataComponent.class);
		if (metadata != null) {
			metadata.removeMeta(16);
		}
	}

	public void setFueled(boolean isFueled) {
		fuel = isFueled ? 180f : 0f;
	}

	public boolean isFueled() {
		return fuel > 0f;
	}

	@Override
	public boolean canTick() {
		return isFueled();
	}

	@Override
	public void onTick(float dt) {
		// Do not change the fuel counter on the client
		if (Spout.getPlatform() == Platform.SERVER) {
			fuel -= dt;
			if (fuel <= 0f) {
				fuel = 0f;
			}
		}
	}

	@Override
	public void onInteract(final EntityInteractEvent<?> event) {
		if (event instanceof PlayerInteractEntityEvent) {
			final PlayerInteractEntityEvent pie = (PlayerInteractEntityEvent) event;
			final Player player = (Player) pie.getEntity();
			switch (pie.getAction()) {
				case LEFT_CLICK:
					Slot slot = PlayerUtil.getHeldSlot(player);
					if (slot.get() != null) {
						ItemStack stack = slot.get();
						if (stack.getMaterial() instanceof Fuel) {
							setFueled(true);
							slot.addAmount(-1);
						}
					}
			}
		}
		super.onInteract(event);
	}

	@Override
	public BlockMaterial getDefaultDisplayedBlock() {
		return VanillaMaterials.FURNACE;
	}
}
