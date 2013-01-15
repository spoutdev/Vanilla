package org.spout.vanilla.plugin.component.substance.object.vehicle;

import java.util.ArrayList;
import java.util.List;

import org.spout.api.entity.Entity;
import org.spout.api.entity.Player;
import org.spout.api.event.player.PlayerInteractEvent.Action;
import org.spout.api.geo.discrete.Point;
import org.spout.api.inventory.ItemStack;
import org.spout.api.util.Parameter;
import org.spout.vanilla.plugin.component.misc.DropComponent;
import org.spout.vanilla.plugin.component.substance.Item;
import org.spout.vanilla.plugin.component.substance.object.ObjectEntity;
import org.spout.vanilla.plugin.data.VanillaData;
import org.spout.vanilla.plugin.event.entity.EntityMetaChangeEvent;
import org.spout.vanilla.plugin.event.entity.EntityStatusEvent;
import org.spout.vanilla.plugin.material.VanillaMaterials;
import org.spout.vanilla.plugin.protocol.msg.entity.EntityStatusMessage;

public abstract class MinecartBase extends ObjectEntity {

	private int wobble = 0;
	@Override
	public void onAttached() {
		getOwner().getData().put(VanillaData.ATTACHED_COUNT, getAttachedCount() + 1);
		if (getAttachedCount() == 1) {
			getOwner().add(DropComponent.class).addDrop(new ItemStack(VanillaMaterials.MINECART, 1));
		}
		getOwner().setSavable(true);
		super.onAttached();
	}
	
	@Override
	public void onInteract(Action action, Entity source)  {
		if (!(source instanceof Player)) {
			return;
		}
		
		if (wobble > 0) {
			wobble--;
		}
		if (Action.LEFT_CLICK.equals(action)) {
			wobble += 10;
			List<Parameter<?>> parameters = new ArrayList<Parameter<?>>();
			parameters.add(new Parameter<Integer>(Parameter.TYPE_INT, 17, wobble / 5)); // Unknown flag; initialized to 0. (Probably time since last collision)
			parameters.add(new Parameter<Integer>(Parameter.TYPE_INT, 19, wobble));
			getOwner().getNetwork().callProtocolEvent(new EntityMetaChangeEvent(getOwner(), parameters));
			getOwner().getNetwork().callProtocolEvent(new EntityStatusEvent(getOwner(), EntityStatusMessage.ENTITY_HURT));
			
			if (wobble > 40) {
				onDestroy();
				getOwner().remove();
				
			}
		}
	}
	
	/**
	 * A counter of how many times this component has been attached to an entity
	 * <p/>
	 * Values > 1 indicate how many times this component has been saved to disk,
	 * and reloaded
	 * <p/>
	 * Values == 1 indicate a new component that has never been saved and loaded.
	 * @return attached count
	 */
	public final int getAttachedCount() {
		return getOwner().getData().get(VanillaData.ATTACHED_COUNT);
	}
	
	protected void onDestroy() {
		List<ItemStack> drops = getOwner().get(DropComponent.class).getDrops();
		Point entityPosition = getOwner().getTransform().getPosition();
		for (ItemStack stack : drops) {
			if (stack != null) {
				Item.dropNaturally(entityPosition, stack);
			}
		}
	}
}
