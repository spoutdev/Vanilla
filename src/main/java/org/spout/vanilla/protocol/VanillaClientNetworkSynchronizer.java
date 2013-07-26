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
package org.spout.vanilla.protocol;

import org.spout.api.Spout;
import org.spout.api.entity.Entity;
import org.spout.api.entity.Player;
import org.spout.api.event.EventHandler;
import org.spout.api.event.Listener;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.BlockMaterial;
import org.spout.api.protocol.ClientNetworkSynchronizer;
import org.spout.api.protocol.Message;
import org.spout.api.protocol.Session;
import org.spout.vanilla.VanillaPlugin;
import org.spout.vanilla.component.block.material.Sign;
import org.spout.vanilla.component.entity.inventory.PlayerInventory;
import org.spout.vanilla.component.entity.living.Human;
import org.spout.vanilla.component.entity.misc.Hunger;
import org.spout.vanilla.component.entity.misc.Level;
import org.spout.vanilla.data.Weather;
import org.spout.vanilla.event.block.BlockActionEvent;
import org.spout.vanilla.event.block.SignUpdateEvent;
import org.spout.vanilla.event.block.network.BlockBreakAnimationEvent;
import org.spout.vanilla.event.block.network.EntityTileDataEvent;
import org.spout.vanilla.event.entity.EntityAnimationEvent;
import org.spout.vanilla.event.entity.EntityCollectItemEvent;
import org.spout.vanilla.event.entity.EntityEquipmentEvent;
import org.spout.vanilla.event.entity.EntityMetaChangeEvent;
import org.spout.vanilla.event.entity.EntityStatusEvent;
import org.spout.vanilla.event.entity.network.EntityEffectEvent;
import org.spout.vanilla.event.entity.network.EntityRemoveEffectEvent;
import org.spout.vanilla.event.item.MapItemUpdateEvent;
import org.spout.vanilla.event.player.network.PlayerAbilityUpdateEvent;
import org.spout.vanilla.event.player.network.PlayerBedEvent;
import org.spout.vanilla.event.player.network.PlayerGameStateEvent;
import org.spout.vanilla.event.player.network.PlayerHealthEvent;
import org.spout.vanilla.event.player.network.PlayerListEvent;
import org.spout.vanilla.event.player.network.PlayerPingEvent;
import org.spout.vanilla.event.player.network.PlayerSelectedSlotChangeEvent;
import org.spout.vanilla.event.scoreboard.ObjectiveActionEvent;
import org.spout.vanilla.event.scoreboard.ObjectiveDisplayEvent;
import org.spout.vanilla.event.scoreboard.ScoreUpdateEvent;
import org.spout.vanilla.event.scoreboard.TeamActionEvent;
import org.spout.vanilla.event.window.WindowCloseEvent;
import org.spout.vanilla.event.window.WindowItemsEvent;
import org.spout.vanilla.event.window.WindowOpenEvent;
import org.spout.vanilla.event.window.WindowPropertyEvent;
import org.spout.vanilla.event.window.WindowSlotEvent;
import org.spout.vanilla.event.world.PlayExplosionEffectEvent;
import org.spout.vanilla.event.world.PlayParticleEffectEvent;
import org.spout.vanilla.event.world.PlaySoundEffectEvent;
import org.spout.vanilla.event.world.TimeUpdateEvent;
import org.spout.vanilla.event.world.WeatherChangeEvent;
import org.spout.vanilla.inventory.window.DefaultWindow;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.protocol.entity.player.ExperienceChangeEvent;
import org.spout.vanilla.protocol.msg.entity.EntityAnimationMessage;
import org.spout.vanilla.protocol.msg.entity.EntityEquipmentMessage;
import org.spout.vanilla.protocol.msg.entity.EntityItemDataMessage;
import org.spout.vanilla.protocol.msg.entity.EntityMetadataMessage;
import org.spout.vanilla.protocol.msg.entity.EntityStatusMessage;
import org.spout.vanilla.protocol.msg.entity.EntityTileDataMessage;
import org.spout.vanilla.protocol.msg.entity.effect.EntityEffectMessage;
import org.spout.vanilla.protocol.msg.entity.effect.EntityRemoveEffectMessage;
import org.spout.vanilla.protocol.msg.player.PlayerAbilityMessage;
import org.spout.vanilla.protocol.msg.player.PlayerBedMessage;
import org.spout.vanilla.protocol.msg.player.PlayerCollectItemMessage;
import org.spout.vanilla.protocol.msg.player.PlayerExperienceMessage;
import org.spout.vanilla.protocol.msg.player.PlayerGameStateMessage;
import org.spout.vanilla.protocol.msg.player.PlayerHealthMessage;
import org.spout.vanilla.protocol.msg.player.PlayerHeldItemChangeMessage;
import org.spout.vanilla.protocol.msg.player.PlayerTimeMessage;
import org.spout.vanilla.protocol.msg.player.conn.PlayerListMessage;
import org.spout.vanilla.protocol.msg.player.conn.PlayerPingMessage;
import org.spout.vanilla.protocol.msg.scoreboard.ScoreboardDisplayMessage;
import org.spout.vanilla.protocol.msg.scoreboard.ScoreboardObjectiveMessage;
import org.spout.vanilla.protocol.msg.scoreboard.ScoreboardScoreMessage;
import org.spout.vanilla.protocol.msg.scoreboard.ScoreboardTeamMessage;
import org.spout.vanilla.protocol.msg.window.WindowCloseMessage;
import org.spout.vanilla.protocol.msg.window.WindowItemsMessage;
import org.spout.vanilla.protocol.msg.window.WindowOpenMessage;
import org.spout.vanilla.protocol.msg.window.WindowPropertyMessage;
import org.spout.vanilla.protocol.msg.window.WindowSlotMessage;
import org.spout.vanilla.protocol.msg.world.EffectMessage;
import org.spout.vanilla.protocol.msg.world.ExplosionMessage;
import org.spout.vanilla.protocol.msg.world.SoundEffectMessage;
import org.spout.vanilla.protocol.msg.world.block.BlockActionMessage;
import org.spout.vanilla.protocol.msg.world.block.BlockBreakAnimationMessage;
import org.spout.vanilla.protocol.msg.world.block.SignMessage;
import org.spout.vanilla.protocol.reposition.VanillaRepositionManager;
import org.spout.vanilla.scoreboard.Objective;
import org.spout.vanilla.scoreboard.Team;

public class VanillaClientNetworkSynchronizer extends ClientNetworkSynchronizer implements Listener {
	private final VanillaRepositionManager vpm = new VanillaRepositionManager();

	public VanillaClientNetworkSynchronizer(Session session) {
		super(session);
		Spout.getEventManager().registerEvents(this, VanillaPlugin.getInstance());
		setRepositionManager(vpm);
	}
	
	// TODO verify all of these are client -> server

	@EventHandler
	public Message onEntityTileData(EntityTileDataEvent event) {
		Block b = event.getBlock();
		return new EntityTileDataMessage(b.getX(), b.getY(), b.getZ(), event.getAction(), event.getData(), getRepositionManager());
	}

	@EventHandler
	public Message onMapItemUpdate(MapItemUpdateEvent event) {
		return new EntityItemDataMessage(VanillaMaterials.MAP, (short) event.getItemData(), event.getData());
	}

	@EventHandler
	public Message onPlayerAbilityUpdate(PlayerAbilityUpdateEvent event) {
		return new PlayerAbilityMessage(event.getGodMode(), event.isFlying(), event.canFly(), event.isCreativeMode(), event.getFlyingSpeed(), event.getWalkingSpeed());
	}

	@EventHandler
	public Message onEntityEquipment(EntityEquipmentEvent event) {
		return new EntityEquipmentMessage(event.getEntity().getId(), event.getSlot(), event.getItem());
	}

	@EventHandler
	public Message onWindowOpen(WindowOpenEvent event) {
		if (event.getWindow() instanceof DefaultWindow) {
			return null; // no message for the default Window
		}
		PlayerInventory inventory = event.getWindow().getPlayerInventory();
		int size = event.getWindow().getSize() - (inventory.getMain().size() + inventory.getQuickbar().size());
		return new WindowOpenMessage(event.getWindow(), size);
	}

	@EventHandler
	public Message onWindowClose(WindowCloseEvent event) {
		if (event.getWindow() instanceof DefaultWindow) {
			return null; // no message for the default Window
		}
		return new WindowCloseMessage(event.getWindow());
	}

	@EventHandler
	public Message onWindowSetSlot(WindowSlotEvent event) {
		//TODO: investigate why this happens (12-1-2013)
		if (event.getItem() != null && event.getItem().getMaterial() == BlockMaterial.AIR) {
			return null;
		}
		return new WindowSlotMessage(event.getWindow(), event.getSlot(), event.getItem());
	}

	@EventHandler
	public Message onWindowItems(WindowItemsEvent event) {
		return new WindowItemsMessage(event.getWindow(), event.getItems());
	}

	@EventHandler
	public Message onWindowProperty(WindowPropertyEvent event) {
		return new WindowPropertyMessage(event.getWindow(), event.getId(), event.getValue());
	}

	@EventHandler
	public Message onSoundEffect(PlaySoundEffectEvent event) {
		return new SoundEffectMessage(event.getSound().getName(), event.getPosition(), event.getVolume(), event.getPitch(), getRepositionManager());
	}

	@EventHandler
	public Message onExplosionEffect(PlayExplosionEffectEvent event) {
		return new ExplosionMessage(event.getPosition(), event.getSize(), new byte[0], getRepositionManager());
	}

	@EventHandler
	public Message onParticleEffect(PlayParticleEffectEvent event) {
		int x = event.getPosition().getBlockX();
		int y = event.getPosition().getBlockY();
		int z = event.getPosition().getBlockZ();
		return new EffectMessage(event.getEffect().getId(), x, y, z, event.getData(), getRepositionManager());
	}

	@EventHandler
	public Message onBlockAction(BlockActionEvent event) {
		int id = VanillaMaterials.getMinecraftId(event.getMaterial());
		if (id == -1) {
			return null;
		} else {
			return new BlockActionMessage(event.getBlock(), (short) id, event.getData1(), event.getData2(), getRepositionManager());
		}
	}

	@EventHandler
	public Message onPlayerKeepAlive(PlayerPingEvent event) {
		return new PlayerPingMessage(event.getHash());
	}

	@EventHandler
	public Message onPlayerUpdateUserList(PlayerListEvent event) {
		String name = event.getPlayerDisplayName();
		return new PlayerListMessage(name, event.getOnline(), (short) event.getPingDelay());
	}

	@EventHandler
	public Message onPlayerBed(PlayerBedEvent event) {
		return new PlayerBedMessage(event.getPlayer(), event.getBed(), getRepositionManager());
	}

	@EventHandler
	public Message onEntityAnimation(EntityAnimationEvent event) {
		return new EntityAnimationMessage(event.getEntity().getId(), (byte) event.getAnimation().getId());
	}

	@EventHandler
	public Message onEntityStatus(EntityStatusEvent event) {
		return new EntityStatusMessage(event.getEntity().getId(), event.getStatus());
	}

	@EventHandler
	public Message onPlayerUpdateStats(PlayerHealthEvent event) {
		Hunger hunger = player.get(Hunger.class);
		return new PlayerHealthMessage((short) player.get(Human.class).getHealth().getHealth(), (short) hunger.getHunger(), hunger.getFoodSaturation());
	}

	@EventHandler
	public Message onEntityMetaChange(EntityMetaChangeEvent event) {
		return new EntityMetadataMessage(event.getEntity().getId(), event.getParameters());
	}

	@EventHandler
	public Message onSignUpdate(SignUpdateEvent event) {
		Sign sign = event.getSign();
		return new SignMessage(sign.getOwner().getX(), sign.getOwner().getY(), sign.getOwner().getZ(), event.getLines(), getRepositionManager());
	}

	@EventHandler
	public Message onEntityCollectItem(EntityCollectItemEvent event) {
		return new PlayerCollectItemMessage(event.getCollected().getId(), event.getEntity().getId());
	}

	@EventHandler
	public Message onPlayerGameState(PlayerGameStateEvent event) {
		return new PlayerGameStateMessage(event.getReason(), event.getGameMode());
	}

	@EventHandler
	public Message onWeatherChanged(WeatherChangeEvent event) {
		Weather newWeather = event.getNewWeather();
		if (newWeather.equals(Weather.RAIN) || newWeather.equals(Weather.THUNDERSTORM)) {
			return new PlayerGameStateMessage(PlayerGameStateMessage.BEGIN_RAINING);
		} else {
			return new PlayerGameStateMessage(PlayerGameStateMessage.END_RAINING);
		}
	}

	@EventHandler
	public Message onTimeUpdate(TimeUpdateEvent event) {
		return new PlayerTimeMessage(event.getWorld().getAge(), event.getNewTime());
	}

	@EventHandler
	public Message onEntityRemoveEffect(EntityRemoveEffectEvent event) {
		System.out.println("Removing effect");
		return new EntityRemoveEffectMessage(event.getEntity().getId(), (byte) event.getEffect().getId());
	}

	@EventHandler
	public Message onBlockBreakAnimation(BlockBreakAnimationEvent event) {
		return new BlockBreakAnimationMessage(event.getEntity().getId(), (int) event.getPoint().getX(), (int) event.getPoint().getY(), (int) event.getPoint().getZ(), event.getLevel(), getRepositionManager());
	}

	@EventHandler
	public Message onEntityEffect(EntityEffectEvent event) {
		return new EntityEffectMessage(event.getEntity().getId(), (byte) event.getEffect().getType().getId(), (byte) 0, (short) (event.getEffect().getDuration() * 20));
	}

	@EventHandler
	public Message onExperienceChange(ExperienceChangeEvent event) {
		Entity entity = event.getEntity();
		Level level = entity.get(Level.class);

		if (!(entity instanceof Player)) {
			return null;
		}

		if (level == null) {
			return null;
		}

		return new PlayerExperienceMessage(level.getProgress(), level.getLevel(), event.getNewExp());
	}

	@EventHandler
	public Message onPlayerSelectedSlotChange(PlayerSelectedSlotChangeEvent event) {
		return new PlayerHeldItemChangeMessage(event.getSelectedSlot());
	}

	@EventHandler
	public Message onObjectiveAction(ObjectiveActionEvent event) {
		Objective obj = event.getObjective();
		return new ScoreboardObjectiveMessage(obj.getName(), obj.getDisplayName(), event.getAction());
	}

	@EventHandler
	public Message onObjectiveDisplay(ObjectiveDisplayEvent event) {
		return new ScoreboardDisplayMessage((byte) event.getSlot().ordinal(), event.getObjectiveName());
	}

	@EventHandler
	public Message onScoreUpdate(ScoreUpdateEvent event) {
		return new ScoreboardScoreMessage(event.getKey(), event.isRemove(), event.getObjectiveName(), event.getValue());
	}

	@EventHandler
	public Message onTeamAction(TeamActionEvent event) {
		Team team = event.getTeam();
		return new ScoreboardTeamMessage(
				team.getName(), event.getAction(),
				team.getDisplayName(),
				team.getPrefix(), team.getSuffix(),
				team.isFriendlyFire(), event.getPlayers()
		);
	}

}
