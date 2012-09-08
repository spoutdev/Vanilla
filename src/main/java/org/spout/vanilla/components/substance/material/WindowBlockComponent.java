package org.spout.vanilla.components.substance.material;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

import org.spout.api.component.components.BlockComponent;
import org.spout.api.entity.Entity;
import org.spout.api.entity.Player;
import org.spout.api.event.player.PlayerInteractEvent.Action;

import org.spout.vanilla.components.misc.WindowComponent;
import org.spout.vanilla.components.misc.WindowOwner;

public abstract class WindowBlockComponent extends BlockComponent implements WindowOwner {
	private HashMap<Player, WindowComponent> viewers = new HashMap<Player, WindowComponent>();

	public abstract WindowComponent createWindow(Player player);

	@Override
	public void onInteract(Entity entity, Action type) {
		super.onInteract(entity, type);
		if (type == Action.RIGHT_CLICK) {
			this.open((Player) entity);
		}
	}

	@Override
	public boolean open(Player player) {
		if (!this.viewers.containsKey(player)) {
			WindowComponent w = this.createWindow(player);
			this.addViewer(player, w);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean close(Player player) {
		WindowComponent w = this.removeViewer(player);
		if (w != null) {
			player.detach(WindowComponent.class);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void closeAll() {
		for (Player player : this.getViewerArray()) {
			this.close(player);
		}
	}

	@Override
	public WindowComponent removeViewer(Player player) {
		try {
			return this.viewers.remove(player);
		} finally {
			onViewersChanged();
		}
	}

	@Override
	public void addViewer(Player player, WindowComponent window) {
		this.viewers.put(player, window);
		this.onViewersChanged();
	}

	/**
	 * Is called when a viewer got removed or added
	 */
	@Override
	public void onViewersChanged() {
	}

	@Override
	public Collection<Player> getViewers() {
		return Collections.unmodifiableSet(this.viewers.keySet());
	}

	/**
	 * Gets an array of viewers currently viewing this entity
	 * @return an array of player viewers
	 */
	@Override
	public Player[] getViewerArray() {
		return this.viewers.keySet().toArray(new Player[0]);
	}

	@Override
	public boolean hasViewers() {
		return !this.viewers.isEmpty();
	}
}
