package org.spout.vanilla.components.player;

import org.spout.api.Spout;
import org.spout.api.component.components.EntityComponent;
import org.spout.api.entity.Player;
import org.spout.vanilla.data.GameMode;
import org.spout.vanilla.data.VanillaData;
import org.spout.vanilla.event.player.PlayerGameModeChangedEvent;

public class GameModeComponent extends EntityComponent {

	public GameModeComponent() {
	}
	
	/**
	 * Retrieve the current gamemode of the player.
	 * @return The Gamemode of the player.
	 */
	public GameMode getGameMode() {
		return getDatatable().get(VanillaData.GAMEMODE);
	}
	
	/**
	 * Checks if the player is in survival mode.
	 * @return
	 */
	public boolean isSurvival() {
		return getGameMode().equals(GameMode.SURVIVAL);
	}
	
	public boolean isCreative() {
		return getGameMode().equals(GameMode.CREATIVE);
	}
	
	public boolean isAdventure() {
		return getGameMode().equals(GameMode.ADVENTURE);
	}
	
	public void setGameMode(GameMode gamemode) {
		PlayerGameModeChangedEvent event = new PlayerGameModeChangedEvent(((Player)getHolder()), gamemode);
		Spout.getEngine().getEventManager().callEvent(event);
		if (!event.isCancelled()) {
			getDatatable().put(VanillaData.GAMEMODE, event.getMode());
		}
	}
}
