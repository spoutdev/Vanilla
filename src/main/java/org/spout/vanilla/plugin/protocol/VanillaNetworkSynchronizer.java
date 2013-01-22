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
package org.spout.vanilla.plugin.protocol;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

import gnu.trove.set.TIntSet;

import org.spout.api.Server;
import org.spout.api.Spout;
import org.spout.api.entity.Entity;
import org.spout.api.entity.Player;
import org.spout.api.event.EventHandler;
import org.spout.api.generator.biome.Biome;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.cuboid.Chunk;
import org.spout.api.geo.discrete.Point;
import org.spout.api.material.BlockMaterial;
import org.spout.api.math.IntVector3;
import org.spout.api.math.Quaternion;
import org.spout.api.protocol.EntityProtocol;
import org.spout.api.protocol.Message;
import org.spout.api.protocol.NetworkSynchronizer;
import org.spout.api.protocol.Session;
import org.spout.api.protocol.Session.State;
import org.spout.api.protocol.event.ProtocolEvent;
import org.spout.api.protocol.event.ProtocolEventListener;
import org.spout.api.protocol.reposition.RepositionManager;
import org.spout.api.util.FlatIterator;
import org.spout.api.util.hashing.IntPairHashed;
import org.spout.api.util.map.concurrent.TSyncIntPairObjectHashMap;
import org.spout.api.util.set.concurrent.TSyncIntHashSet;
import org.spout.api.util.set.concurrent.TSyncIntPairHashSet;

import org.spout.vanilla.api.component.substance.material.SignComponent;
import org.spout.vanilla.api.data.Difficulty;
import org.spout.vanilla.api.data.Dimension;
import org.spout.vanilla.api.data.GameMode;
import org.spout.vanilla.api.data.Weather;
import org.spout.vanilla.api.data.WorldType;
import org.spout.vanilla.api.material.block.component.VanillaComplexMaterial;
import org.spout.vanilla.api.protocol.msg.VanillaBlockDataChannelMessage;
import org.spout.vanilla.api.world.generator.biome.VanillaBiome;

import org.spout.vanilla.plugin.VanillaPlugin;
import org.spout.vanilla.plugin.component.inventory.PlayerInventory;
import org.spout.vanilla.plugin.component.living.neutral.Human;
import org.spout.vanilla.plugin.component.misc.HungerComponent;
import org.spout.vanilla.plugin.component.misc.LevelComponent;
import org.spout.vanilla.plugin.component.test.ForceMessagesComponent;
import org.spout.vanilla.plugin.component.world.VanillaSky;
import org.spout.vanilla.plugin.configuration.VanillaConfiguration;
import org.spout.vanilla.plugin.configuration.WorldConfigurationNode;
import org.spout.vanilla.plugin.data.VanillaData;
import org.spout.vanilla.api.event.block.BlockActionEvent;
import org.spout.vanilla.api.event.block.BlockControllerDataEvent;
import org.spout.vanilla.api.event.block.SignUpdateEvent;
import org.spout.vanilla.api.event.block.network.BlockBreakAnimationEvent;
import org.spout.vanilla.api.event.entity.EntityAnimationEvent;
import org.spout.vanilla.api.event.entity.EntityCollectItemEvent;
import org.spout.vanilla.api.event.entity.EntityEquipmentEvent;
import org.spout.vanilla.api.event.entity.EntityMetaChangeEvent;
import org.spout.vanilla.api.event.entity.EntityStatusEvent;
import org.spout.vanilla.plugin.event.entity.network.EntityEffectEvent;
import org.spout.vanilla.plugin.event.entity.network.EntityRemoveEffectEvent;
import org.spout.vanilla.api.event.item.MapItemUpdateEvent;
import org.spout.vanilla.plugin.event.player.network.PlayerAbilityUpdateEvent;
import org.spout.vanilla.api.event.player.network.PlayerBedEvent;
import org.spout.vanilla.api.event.player.network.PlayerGameStateEvent;
import org.spout.vanilla.api.event.player.network.PlayerHealthEvent;
import org.spout.vanilla.api.event.player.network.PlayerListEvent;
import org.spout.vanilla.api.event.player.network.PlayerPingEvent;
import org.spout.vanilla.api.event.player.network.PlayerSelectedSlotChangeEvent;
import org.spout.vanilla.plugin.event.window.WindowCloseEvent;
import org.spout.vanilla.plugin.event.window.WindowItemsEvent;
import org.spout.vanilla.plugin.event.window.WindowOpenEvent;
import org.spout.vanilla.plugin.event.window.WindowPropertyEvent;
import org.spout.vanilla.plugin.event.window.WindowSlotEvent;
import org.spout.vanilla.plugin.event.world.PlayExplosionEffectEvent;
import org.spout.vanilla.plugin.event.world.PlayParticleEffectEvent;
import org.spout.vanilla.plugin.event.world.PlaySoundEffectEvent;
import org.spout.vanilla.plugin.event.world.TimeUpdateEvent;
import org.spout.vanilla.plugin.event.world.WeatherChangeEvent;
import org.spout.vanilla.plugin.inventory.window.DefaultWindow;
import org.spout.vanilla.plugin.material.VanillaMaterials;
import org.spout.vanilla.plugin.protocol.container.VanillaContainer;
import org.spout.vanilla.plugin.protocol.entity.player.ExperienceChangeEvent;
import org.spout.vanilla.plugin.protocol.msg.entity.EntityAnimationMessage;
import org.spout.vanilla.plugin.protocol.msg.entity.EntityEquipmentMessage;
import org.spout.vanilla.plugin.protocol.msg.entity.EntityItemDataMessage;
import org.spout.vanilla.plugin.protocol.msg.entity.EntityMetadataMessage;
import org.spout.vanilla.plugin.protocol.msg.entity.EntityStatusMessage;
import org.spout.vanilla.plugin.protocol.msg.entity.EntityTileDataMessage;
import org.spout.vanilla.plugin.protocol.msg.entity.effect.EntityEffectMessage;
import org.spout.vanilla.plugin.protocol.msg.entity.effect.EntityRemoveEffectMessage;
import org.spout.vanilla.plugin.protocol.msg.player.PlayerAbilityMessage;
import org.spout.vanilla.plugin.protocol.msg.player.PlayerBedMessage;
import org.spout.vanilla.plugin.protocol.msg.player.PlayerCollectItemMessage;
import org.spout.vanilla.plugin.protocol.msg.player.PlayerExperienceMessage;
import org.spout.vanilla.plugin.protocol.msg.player.PlayerGameStateMessage;
import org.spout.vanilla.plugin.protocol.msg.player.PlayerHealthMessage;
import org.spout.vanilla.plugin.protocol.msg.player.PlayerHeldItemChangeMessage;
import org.spout.vanilla.plugin.protocol.msg.player.PlayerTimeMessage;
import org.spout.vanilla.plugin.protocol.msg.player.conn.PlayerListMessage;
import org.spout.vanilla.plugin.protocol.msg.player.conn.PlayerLoginRequestMessage;
import org.spout.vanilla.plugin.protocol.msg.player.conn.PlayerPingMessage;
import org.spout.vanilla.plugin.protocol.msg.player.pos.PlayerPositionLookMessage;
import org.spout.vanilla.plugin.protocol.msg.player.pos.PlayerRespawnMessage;
import org.spout.vanilla.plugin.protocol.msg.player.pos.PlayerSpawnPositionMessage;
import org.spout.vanilla.plugin.protocol.msg.window.WindowCloseMessage;
import org.spout.vanilla.plugin.protocol.msg.window.WindowItemsMessage;
import org.spout.vanilla.plugin.protocol.msg.window.WindowOpenMessage;
import org.spout.vanilla.plugin.protocol.msg.window.WindowPropertyMessage;
import org.spout.vanilla.plugin.protocol.msg.window.WindowSlotMessage;
import org.spout.vanilla.plugin.protocol.msg.world.EffectMessage;
import org.spout.vanilla.plugin.protocol.msg.world.ExplosionMessage;
import org.spout.vanilla.plugin.protocol.msg.world.SoundEffectMessage;
import org.spout.vanilla.plugin.protocol.msg.world.block.BlockActionMessage;
import org.spout.vanilla.plugin.protocol.msg.world.block.BlockBreakAnimationMessage;
import org.spout.vanilla.plugin.protocol.msg.world.block.BlockChangeMessage;
import org.spout.vanilla.plugin.protocol.msg.world.block.SignMessage;
import org.spout.vanilla.plugin.protocol.msg.world.chunk.ChunkDataMessage;
import org.spout.vanilla.plugin.protocol.reposition.VanillaRepositionManager;

import static org.spout.vanilla.plugin.material.VanillaMaterials.getMinecraftData;
import static org.spout.vanilla.plugin.material.VanillaMaterials.getMinecraftId;

public class VanillaNetworkSynchronizer extends NetworkSynchronizer implements ProtocolEventListener {
	private static final int SOLID_BLOCK_ID = 1; // Initializer block ID
	private static final byte[] SOLID_CHUNK_DATA = new byte[Chunk.BLOCKS.HALF_VOLUME * 5];
	private static final byte[] AIR_CHUNK_DATA = new byte[Chunk.BLOCKS.HALF_VOLUME * 5];
	private static final double STANCE = 1.62001D;
	private static final int FORCE_MASK = 0xFF; // force an update to be sent every 5 seconds
	private static final int HASH_SEED = 0xB346D76A;
	private boolean first = true;
	private final TSyncIntPairObjectHashMap<TSyncIntHashSet> initializedChunks = new TSyncIntPairObjectHashMap<TSyncIntHashSet>();
	private final ConcurrentLinkedQueue<Long> emptyColumns = new ConcurrentLinkedQueue<Long>();
	private TSyncIntPairHashSet activeChunks = new TSyncIntPairHashSet();
	private Object initChunkLock = new Object();
	private final ChunkInit chunkInit;
	private int minY = 0;
	private int maxY = 256;
	private int lowY = 32;
	private int highY = 224;
	private int stepY = 160;
	private int offsetY = 0;
	private final VanillaRepositionManager vpm = new VanillaRepositionManager();

	static {
		int i = 0;
		for (int c = 0; c < Chunk.BLOCKS.VOLUME; c++) { // blocks
			SOLID_CHUNK_DATA[i] = SOLID_BLOCK_ID;
			AIR_CHUNK_DATA[i++] = 0;
		}
		for (int c = 0; c < Chunk.BLOCKS.HALF_VOLUME; c++) { // block data
			SOLID_CHUNK_DATA[i] = 0x00;
			AIR_CHUNK_DATA[i++] = 0x00;
		}
		for (int c = 0; c < Chunk.BLOCKS.HALF_VOLUME; c++) { // block light
			SOLID_CHUNK_DATA[i] = 0x00;
			AIR_CHUNK_DATA[i++] = 0x00;
		}
		for (int c = 0; c < Chunk.BLOCKS.HALF_VOLUME; c++) { // sky light
			SOLID_CHUNK_DATA[i] = 0x00;
			AIR_CHUNK_DATA[i++] = (byte) 0xFF;
		}
	}

	public VanillaNetworkSynchronizer(Session session) {
		// The minimum block distance is a radius for sending chunks before login/respawn
		// It needs to be > 0 for reliable login and preventing falling through the world
		super(session, 2);
		registerProtocolEvents(this);
		chunkInit = ChunkInit.getChunkInit(VanillaConfiguration.CHUNK_INIT.getString("client"));
		setRepositionManager(vpm);
	}

	@Override
	protected void freeChunk(Point p) {
		int x = (int) p.getX() >> Chunk.BLOCKS.BITS;
		int y = (int) p.getY() >> Chunk.BLOCKS.BITS; // + SEALEVEL_CHUNK;
		int z = (int) p.getZ() >> Chunk.BLOCKS.BITS;

		RepositionManager rm = getRepositionManager();

		int cY = rm.convertChunkY(y);

		if (cY < 0 || cY >= p.getWorld().getHeight() >> Chunk.BLOCKS.BITS) {
			return;
		}

		TIntSet column = initializedChunks.get(x, z);
		if (column != null) {
			column.remove(y);
			if (column.isEmpty()) {
				emptyColumns.add(IntPairHashed.key(x, z));
			}
		}
	}

	@Override
	protected void initChunk(Point p) {
	}

	private void initChunkRaw(Point p) {
		int x = p.getChunkX();
		int y = p.getChunkY();// + SEALEVEL_CHUNK;
		int z = p.getChunkZ();

		RepositionManager rm = getRepositionManager();

		int cY = rm.convertChunkY(y);

		if (cY < 0 || cY >= p.getWorld().getHeight() >> Chunk.BLOCKS.BITS) {
			return;
		}

		TSyncIntHashSet column = initializedChunks.get(x, z);
		if (column == null) {
			column = new TSyncIntHashSet();
			synchronized (initChunkLock) {
				TSyncIntHashSet oldColumn = initializedChunks.putIfAbsent(x, z, column);
				if (oldColumn != null) {
					column = oldColumn;
				}
			}
		}
		column.add(y);
	}

	private static BlockMaterial[][] getColumnTopmostMaterials(Point p) {
		BlockMaterial[][] materials = new BlockMaterial[Chunk.BLOCKS.SIZE][Chunk.BLOCKS.SIZE];

		World w = p.getWorld();

		for (int xx = 0; xx < Chunk.BLOCKS.SIZE; xx++) {
			for (int zz = 0; zz < Chunk.BLOCKS.SIZE; zz++) {
				materials[xx][zz] = w.getTopmostBlock(p.getBlockX() + xx, p.getBlockZ() + zz, true);
			}
		}
		return materials;
	}

	private static int[][] getColumnHeights(Point p) {
		int[][] heights = new int[Chunk.BLOCKS.SIZE][Chunk.BLOCKS.SIZE];

		World w = p.getWorld();

		for (int xx = 0; xx < Chunk.BLOCKS.SIZE; xx++) {
			for (int zz = 0; zz < Chunk.BLOCKS.SIZE; zz++) {
				heights[xx][zz] = w.getSurfaceHeight(p.getBlockX() + xx, p.getBlockZ() + zz, true);
			}
		}
		return heights;
	}

	private static byte[] emptySkyChunkData;
	private static byte[] emptyGroundChunkData;

	static {
		emptySkyChunkData = new byte[Chunk.BLOCKS.HALF_VOLUME * 5];
		emptyGroundChunkData = new byte[Chunk.BLOCKS.HALF_VOLUME * 5];

		int j = Chunk.BLOCKS.VOLUME << 1;
		// Sky light = F
		for (int i = 0; i < Chunk.BLOCKS.HALF_VOLUME; i++) {
			emptySkyChunkData[j] = (byte) 0xFF;
		}
	}

	@Override
	protected boolean canSendChunk(Chunk c, Set<Chunk> unsendable) {
		if (!c.canSend()) {
			if (unsendable != null) {
				unsendable.add(c);
			}
			c.populate(true, true, true);
			return false;
		}
		if (activeChunks.contains(c.getX(), c.getZ())) {
			return true;
		}
		boolean canSend = true;
		Collection<Chunk> chunks = chunkInit.getChunks(c);
		for (Chunk cc : chunks) {
			if (!cc.canSend()) {
				if (unsendable != null) {
					unsendable.add(c);
				}
				canSend = false;
				cc.populate(true, true, true);
			}
		}
		return canSend;
	}

	@Override
	public Collection<Chunk> sendChunk(Chunk c) {
		if (canSendChunk(c, null)) {
			return sendChunk(c, true);
		} else {
			return null;
		}
	}

	@Override
	protected Collection<Chunk> sendChunk(Chunk c, boolean force) {

		if (!force) {
			return sendChunk(c);
		}

		int x = c.getX();
		int y = c.getY();// + SEALEVEL_CHUNK;
		int z = c.getZ();

		RepositionManager rm = getRepositionManager();

		int cY = rm.convertChunkY(y);

		if (cY < 0 || cY >= c.getWorld().getHeight() >> Chunk.BLOCKS.BITS) {
			return null;
		}

		initChunkRaw(c.getBase());

		Collection<Chunk> chunks = null;

		List<ProtocolEvent> events = new ArrayList<ProtocolEvent>();

		if (activeChunks.add(x, z)) {
			Point p = c.getBase();
			int[][] heights = getColumnHeights(p);
			BlockMaterial[][] materials = getColumnTopmostMaterials(p);

			byte[][] packetChunkData = new byte[16][];

			for (int cube = 0; cube < 16; cube++) {
				int serverCube = rm.getInverse().convertChunkY(cube);
				Point pp = new Point(c.getWorld(), x << Chunk.BLOCKS.BITS, serverCube << Chunk.BLOCKS.BITS, z << Chunk.BLOCKS.BITS);
				packetChunkData[cube] = chunkInit.getChunkData(heights, materials, pp, events);
			}

			Chunk chunk = p.getWorld().getChunkFromBlock(p);
			byte[] biomeData = new byte[Chunk.BLOCKS.AREA];
			for (int dx = x; dx < x + Chunk.BLOCKS.SIZE; ++dx) {
				for (int dz = z; dz < z + Chunk.BLOCKS.SIZE; ++dz) {
					Biome biome = chunk.getBiome(dx & Chunk.BLOCKS.MASK, 0, dz & Chunk.BLOCKS.MASK);
					if (biome instanceof VanillaBiome) {
						biomeData[(dz & Chunk.BLOCKS.MASK) << 4 | (dx & Chunk.BLOCKS.MASK)] = (byte) ((VanillaBiome) biome).getBiomeId();
					}
				}
			}

			ChunkDataMessage CCMsg = new ChunkDataMessage(x, z, true, new boolean[16], packetChunkData, biomeData, player.getSession(), getRepositionManager());
			player.getSession().send(false, CCMsg);

			chunks = chunkInit.getChunks(c);
		}

		if (chunks == null || !chunks.contains(c)) {

			byte[] fullChunkData = ChunkInit.getChunkFullData(c, events);

			byte[][] packetChunkData = new byte[16][];
			packetChunkData[cY] = fullChunkData;
			ChunkDataMessage CCMsg = new ChunkDataMessage(x, z, false, new boolean[16], packetChunkData, null, player.getSession(), getRepositionManager());
			player.getSession().send(false, CCMsg);

			if (chunks == null) {
				chunks = new ArrayList<Chunk>(1);
			}
			chunks.add(c);
		}

		for (ProtocolEvent e : events) {
			this.callProtocolEvent(e);
		}

		return chunks;
	}

	public void sendPosition() {
		sendPosition(player.getTransform().getPosition(), player.getTransform().getRotation());
	}

	@Override
	protected void sendPosition(Point p, Quaternion rot) {
		PlayerPositionLookMessage PPLMsg = new PlayerPositionLookMessage(p.getX(), p.getY() + STANCE, p.getZ(), p.getY(), rot.getYaw(), rot.getPitch(), true, VanillaBlockDataChannelMessage.CHANNEL_ID, getRepositionManager());
		session.send(false, PPLMsg);
	}

	@Override
	protected void worldChanged(World world) {
		WorldConfigurationNode node = VanillaConfiguration.WORLDS.get(world);
		maxY = node.MAX_Y.getInt() & (~Chunk.BLOCKS.MASK);
		minY = node.MIN_Y.getInt() & (~Chunk.BLOCKS.MASK);
		stepY = node.STEP_Y.getInt() & (~Chunk.BLOCKS.MASK);
		lowY = maxY - stepY;
		highY = minY + stepY;
		lastY = Integer.MAX_VALUE;

		GameMode gamemode = world.getComponentHolder().getData().get(VanillaData.GAMEMODE);
		Difficulty difficulty = world.getComponentHolder().getData().get(VanillaData.DIFFICULTY);
		Dimension dimension = world.getComponentHolder().getData().get(VanillaData.DIMENSION);
		WorldType worldType = world.getComponentHolder().getData().get(VanillaData.WORLD_TYPE);

		//Use existing gamemode if we are not logging in
		Human human = player.get(Human.class);
		if (human != null && !first) {
			gamemode = human.getGameMode();
		}

		//TODO Handle infinite height
		int entityId = player.getId();

		if (first) {
			first = false;
			Server server = (Server) session.getEngine();
			PlayerLoginRequestMessage idMsg = new PlayerLoginRequestMessage(entityId, worldType.toString(), gamemode.getId(), (byte) dimension.getId(), difficulty.getId(), (byte) server.getMaxPlayers());
			player.getSession().send(false, true, idMsg);
			player.getSession().setState(State.GAME);
		} else {
			player.getSession().send(false, new PlayerRespawnMessage(0, difficulty.getId(), gamemode.getId(), 256, worldType.toString()));
			player.getSession().send(false, new PlayerRespawnMessage(1, difficulty.getId(), gamemode.getId(), 256, worldType.toString()));
			player.getSession().send(false, new PlayerRespawnMessage(dimension.getId(), difficulty.getId(), gamemode.getId(), 256, worldType.toString()));
		}

		PlayerInventory inv = player.get(PlayerInventory.class);
		if (inv != null) {
			inv.updateAll();
		}
		if (human != null) {
			human.setGamemode(gamemode, false); // Must be here because gamemode may be different; false because client is updated in next call
			human.updateAbilities();
		}
		Point pos = world.getSpawnPoint().getPosition();
		PlayerSpawnPositionMessage SPMsg = new PlayerSpawnPositionMessage((int) pos.getX(), (int) pos.getY(), (int) pos.getZ(), getRepositionManager());
		player.getSession().send(false, SPMsg);
		session.send(false, new PlayerHeldItemChangeMessage(session.getPlayer().add(PlayerInventory.class).getQuickbar().getSelectedSlot().getIndex()));

		VanillaSky.getSky(world).updatePlayer(player);
	}

	@Override
	protected void resetChunks() {
		super.resetChunks();
		this.emptyColumns.clear();
		this.activeChunks.clear();
		this.initializedChunks.clear();
	}

	private int lastY = Integer.MIN_VALUE;

	@Override
	public void finalizeTick() {
		Point currentPosition = player.getTransform().getPosition();

		int y = currentPosition.getBlockY();

		if (y != lastY && !isTeleportPending()) {

			lastY = y;
			int cY = getRepositionManager().convertY(y);

			if (cY >= maxY || cY < minY) {
				int steps = (cY - ((maxY + minY) >> 1)) / stepY;

				offsetY -= steps * stepY;
				vpm.setOffset(offsetY);
				cY = getRepositionManager().convertY(y);

				if (cY >= maxY) {
					offsetY -= stepY;
				} else if (cY < minY) {
					offsetY += stepY;
				}

				vpm.setOffset(offsetY);
				setRespawned();
			}
		}

		super.finalizeTick();
	}

	@Override
	public void preSnapshot() {
		super.preSnapshot();

		Long key;
		while ((key = this.emptyColumns.poll()) != null) {
			int x = IntPairHashed.key1(key);
			int z = IntPairHashed.key2(key);
			TIntSet column = initializedChunks.get(x, z);
			if (column != null && column.isEmpty()) {
				column = initializedChunks.remove(x, z);
				activeChunks.remove(x, z);
				session.send(false, new ChunkDataMessage(x, z, true, null, null, null, true, player.getSession(), getRepositionManager()));
			}
		}
	}

	@Override
	public void updateBlock(Chunk chunk, int x, int y, int z, BlockMaterial material, short data) {
		short id = getMinecraftId(material);
		x += chunk.getBlockX();
		y += chunk.getBlockY();
		z += chunk.getBlockZ();
		BlockChangeMessage BCM = new BlockChangeMessage(x, y, z, id, getMinecraftData(material, data), getRepositionManager());
		session.send(false, BCM);
	}

	@Override
	public void syncEntity(Entity e, boolean spawn, boolean destroy, boolean update) {
		super.syncEntity(e, spawn, destroy, update);
		EntityProtocol ep = e.getNetwork().getEntityProtocol(VanillaPlugin.VANILLA_PROTOCOL_ID);
		if (ep != null) {
			List<Message> messages = new ArrayList<Message>();
			// Sync using vanilla protocol
			if (destroy) {
				messages.addAll(ep.getDestroyMessages(e));
			}
			if (spawn) {
				messages.addAll(ep.getSpawnMessages(e, getRepositionManager()));
			}
			if (update) {
				boolean force = shouldForce(e.getId());
				messages.addAll(ep.getUpdateMessages(e, getRepositionManager(), force));
			}
			for (Message message : messages) {
				this.session.send(false, message);
			}
		}
	}

	private boolean shouldForce(int entityId) {
		int hash = HASH_SEED;
		hash += (hash << 5) + entityId;
		hash += (hash << 5) + tickCounter;
		return (hash & FORCE_MASK) == 0 || (Spout.debugMode() && getPlayer().has(ForceMessagesComponent.class));
	}

	@Override
	public Iterator<IntVector3> getViewableVolume(int cx, int cy, int cz, int viewDistance) {
		RepositionManager rmI = this.getRepositionManager().getInverse();

		int convertY = rmI.convertChunkY(0);

		return new FlatIterator(cx, convertY, cz, 16, viewDistance);
	}

	@Override
	public boolean isInViewVolume(Point playerChunkBase, Point testChunkBase, int viewDistance) {
		if (playerChunkBase == null) {
			return false;
		}
		int distance = Math.abs(playerChunkBase.getChunkX() - testChunkBase.getChunkX()) + Math.abs(playerChunkBase.getChunkZ() - testChunkBase.getChunkZ());
		return distance <= viewDistance;
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
		HungerComponent hungerComponent = player.get(HungerComponent.class);
		return new PlayerHealthMessage((short) player.get(Human.class).getHealth().getHealth(), (short) hungerComponent.getHunger(), hungerComponent.getFoodSaturation());
	}

	@EventHandler
	public Message onEntityMetaChange(EntityMetaChangeEvent event) {
		return new EntityMetadataMessage(event.getEntity().getId(), event.getParameters());
	}

	@EventHandler
	public Message onSignUpdate(SignUpdateEvent event) {
		SignComponent sign = event.getSign();
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
		return new EntityRemoveEffectMessage(event.getEntity().getId(), event.getEffect().getStatusID());
	}

	@EventHandler
	public Message onBlockBreakAnimation(BlockBreakAnimationEvent event) {
		return new BlockBreakAnimationMessage(event.getEntity().getId(), (int) event.getPoint().getX(), (int) event.getPoint().getY(), (int) event.getPoint().getZ(), event.getLevel(), getRepositionManager());
	}

	@EventHandler
	public Message onEntityEffect(EntityEffectEvent event) {
		return new EntityEffectMessage(event.getEntity().getId(), event.getEffect().getEffect().getStatusID(), (byte) 0, (short) (event.getEffect().getTimer() * 20));
	}

	@EventHandler
	public Message onBlockControllerData(BlockControllerDataEvent event) {
		Block b = event.getBlock();
		return new EntityTileDataMessage(b.getX(), b.getY(), b.getZ(), event.getAction(), event.getData(), getRepositionManager());
	}

	@EventHandler
	public Message onExperienceChange(ExperienceChangeEvent event) {
		Entity entity = event.getEntity();
		LevelComponent level = entity.get(LevelComponent.class);

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

	public enum ChunkInit {
		CLIENT_SEL, FULL_COLUMN, HEIGHTMAP, EMPTY_COLUMN;

		public static ChunkInit getChunkInit(String init) {
			if (init == null) {
				return CLIENT_SEL;
			} else if (isEqual(init, "auto", "client", "client_sel")) {
				return CLIENT_SEL;
			} else if (isEqual(init, "full", "fullcol", "full_column")) {
				return FULL_COLUMN;
			} else if (isEqual(init, "empty", "emptycol", "empty_column")) {
				return EMPTY_COLUMN;
			} else if (isEqual(init, "heightmap", "height_map")) {
				return HEIGHTMAP;
			} else {
				Spout.getLogger().info("Invalid chunk init setting, " + init + ", using default setting auto");
				Spout.getLogger().info("Valid settings are:");
				Spout.getLogger().info("client_sel Allows client selection, defaults to full columns");
				Spout.getLogger().info("fullcol    Sends full columns");
				Spout.getLogger().info("heightmap  Sends a heightmap including the topmost block");
				Spout.getLogger().info("empty      Sends empty columns");

				return CLIENT_SEL;
			}
		}

		public Collection<Chunk> getChunks(Chunk c) {
			if (this.sendColumn()) {
				int x = c.getX();
				int z = c.getZ();
				int height = c.getWorld().getHeight() >> Chunk.BLOCKS.BITS;
				List<Chunk> chunks = new ArrayList<Chunk>(height);
				for (int y = 0; y < height; y++) {
					chunks.add(c.getWorld().getChunk(x, y, z));
				}
				return chunks;
			} else {
				List<Chunk> chunks = new ArrayList<Chunk>(1);
				chunks.add(c);
				return chunks;
			}
		}

		public boolean sendColumn() {
			return this == CLIENT_SEL || this == FULL_COLUMN;
		}

		public byte[] getChunkData(int[][] heights, BlockMaterial[][] materials, Point p, List<ProtocolEvent> updateEvents) {
			switch (this) {
				case CLIENT_SEL:
					return getChunkFullColumn(heights, materials, p, updateEvents);
				case FULL_COLUMN:
					return getChunkFullColumn(heights, materials, p, updateEvents);
				case HEIGHTMAP:
					return getChunkHeightMap(heights, materials, p);
				case EMPTY_COLUMN:
					return getEmptyChunk(heights, materials, p);
				default:
					return getChunkFullColumn(heights, materials, p, updateEvents);
			}
		}

		private static byte[] getChunkFullColumn(int[][] heights, BlockMaterial[][] materials, Point p, List<ProtocolEvent> updateEvents) {
			Chunk c = p.getWorld().getChunkFromBlock(p);
			return getChunkFullData(c, updateEvents);
		}

		public static byte[] getChunkFullData(Chunk c, List<ProtocolEvent> updateEvents) {

			VanillaContainer container = new VanillaContainer();
			c.fillBlockContainer(container);
			container.setLightMode(true);
			c.fillBlockLightContainer(container);
			container.setLightMode(false);
			c.fillSkyLightContainer(container);
			c.fillBlockComponentContainer(container);

			int[] componentX = container.getXArray();
			int[] componentY = container.getYArray();
			int[] componentZ = container.getZArray();

			for (int i = 0; i < container.getBlockComponentCount(); i++) {
				BlockMaterial bm = c.getBlockMaterial(componentX[i], componentY[i], componentZ[i]);
				if (bm instanceof VanillaComplexMaterial) {
					ProtocolEvent event = ((VanillaComplexMaterial) bm).getUpdate(c.getWorld(), componentX[i], componentY[i], componentZ[i]);
					if (event != null) {
						updateEvents.add(event);
					}
				}
			}

			return container.getChunkFullData();
		}

		private static byte[] getEmptyChunk(int[][] heights, BlockMaterial[][] materials, Point p) {
			int chunkY = p.getChunkY();
			return chunkY <= 4 ? emptyGroundChunkData : emptySkyChunkData;
		}

		private static byte[] getChunkHeightMap(int[][] heights, BlockMaterial[][] materials, Point p) {
			int chunkY = p.getChunkY();
			byte[] packetChunkData = new byte[Chunk.BLOCKS.HALF_VOLUME * 5];
			int baseY = chunkY << Chunk.BLOCKS.BITS;

			for (int xx = 0; xx < Chunk.BLOCKS.SIZE; xx++) {
				for (int zz = 0; zz < Chunk.BLOCKS.SIZE; zz++) {
					int dataOffset = xx | (zz << Chunk.BLOCKS.BITS);
					int threshold = heights[xx][zz] - baseY;
					if (chunkY == 0 && threshold < 0) {
						threshold = 0;
					}
					int yy;
					// Set blocks below height to the solid block
					for (yy = 0; yy < Chunk.BLOCKS.SIZE && yy <= threshold; yy++) {
						if (yy == threshold) {
							BlockMaterial bm = materials[xx][zz];
							if (bm == null) {
								bm = VanillaMaterials.STONE;
							}
							int converted = getMinecraftId(bm.getId());
							packetChunkData[dataOffset] = (byte) converted;
						} else {
							packetChunkData[dataOffset] = SOLID_BLOCK_ID;
						}
						dataOffset += Chunk.BLOCKS.AREA;
					}
					// Set sky light of blocks above height to 15
					// Use half of start offset and add the block id and data length (2 volumes)
					byte mask = (xx & 0x1) == 0 ? (byte) 0x0F : (byte) 0xF0;
					dataOffset = Chunk.BLOCKS.DOUBLE_VOLUME + (dataOffset >> 1);
					for (; yy < Chunk.BLOCKS.SIZE; yy++) {
						packetChunkData[dataOffset] |= mask;
						dataOffset += Chunk.BLOCKS.HALF_AREA;
					}
				}
			}
			return packetChunkData;
		}

		private static boolean isEqual(String in, String... args) {

			if (in == null) {
				return false;
			}
			for (String arg : args) {
				if (arg.toLowerCase().equals(in.toLowerCase())) {
					return true;
				}
			}
			return false;
		}
	}
}
