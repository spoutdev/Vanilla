package org.spout.vanilla.components.misc;

import java.util.Collection;

import org.spout.api.entity.Player;

public interface WindowOwner {
	public boolean open(Player player);

	public boolean close(Player player);

	public void closeAll();

	public WindowComponent removeViewer(Player player);

	public void addViewer(Player player, WindowComponent window);

	/**
	 * Is called when a viewer got removed or added
	 */
	public void onViewersChanged();

	public Collection<Player> getViewers();

	/**
	 * Gets an array of viewers currently viewing this entity
	 * @return an array of player viewers
	 */
	public Player[] getViewerArray();

	public boolean hasViewers();
}
