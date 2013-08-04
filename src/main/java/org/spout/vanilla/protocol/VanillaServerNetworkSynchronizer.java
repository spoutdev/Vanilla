/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011 Spout LLC <http://www.spout.org/>
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

import gnu.trove.set.TIntSet;

import org.spout.api.Server;
import org.spout.api.Spout;
import org.spout.api.component.BlockComponentOwner;
import org.spout.api.datatable.ManagedMap;
import org.spout.api.entity.Entity;
import org.spout.api.entity.Player;
import org.spout.api.event.EventHandler;
import org.spout.api.event.Listener;
import org.spout.api.event.Order;
import org.spout.api.event.ProtocolEvent;
import org.spout.api.generator.biome.Biome;
import org.spout.api.geo.LoadOption;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.cuboid.Chunk;
import org.spout.api.geo.discrete.Point;
import org.spout.api.geo.discrete.Transform;
import org.spout.api.material.BlockMaterial;
import org.spout.api.math.IntVector3;
import org.spout.api.math.Quaternion;
import org.spout.api.math.Vector3;
import org.spout.api.protocol.EntityProtocol;
import org.spout.api.protocol.Message;
import org.spout.api.protocol.ServerNetworkSynchronizer;
import org.spout.api.protocol.Session;
import org.spout.api.protocol.Session.State;
import org.spout.api.protocol.event.ChunkFreeEvent;
import org.spout.api.protocol.event.ChunkSendEvent;
import org.spout.api.protocol.event.PositionSendEvent;
import org.spout.api.protocol.event.UpdateBlockEvent;
import org.spout.api.protocol.event.WorldChangeProtocolEvent;
import org.spout.api.protocol.reposition.RepositionManager;
import org.spout.api.util.FlatIterator;
import org.spout.api.util.hashing.IntPairHashed;
import org.spout.api.util.map.concurrent.TSyncIntPairObjectHashMap;
import org.spout.api.util.set.concurrent.TSyncIntHashSet;
import org.spout.api.util.set.concurrent.TSyncIntPairHashSet;

import org.spout.vanilla.VanillaPlugin;
import org.spout.vanilla.component.block.material.Sign;
import org.spout.vanilla.component.entity.inventory.PlayerInventory;
import org.spout.vanilla.component.entity.living.Human;
import org.spout.vanilla.component.entity.misc.Hunger;
import org.spout.vanilla.component.entity.misc.Level;
import org.spout.vanilla.component.entity.substance.test.ForceMessages;
import org.spout.vanilla.component.world.sky.NetherSky;
import org.spout.vanilla.component.world.sky.NormalSky;
import org.spout.vanilla.component.world.sky.Sky;
import org.spout.vanilla.component.world.sky.TheEndSky;
import org.spout.vanilla.data.Difficulty;
import org.spout.vanilla.data.Dimension;
import org.spout.vanilla.data.GameMode;
import org.spout.vanilla.data.VanillaData;
import org.spout.vanilla.data.Weather;
import org.spout.vanilla.data.WorldType;
import org.spout.vanilla.data.configuration.VanillaConfiguration;
import org.spout.vanilla.data.configuration.WorldConfigurationNode;
import org.spout.vanilla.event.entity.network.EntityAnimationEvent;
import org.spout.vanilla.event.entity.network.EntityCollectItemEvent;
import org.spout.vanilla.event.entity.network.EntityEffectEvent;
import org.spout.vanilla.event.entity.network.EntityEquipmentEvent;
import org.spout.vanilla.event.entity.network.EntityMetaChangeEvent;
import org.spout.vanilla.event.entity.network.EntityRemoveEffectEvent;
import org.spout.vanilla.event.entity.network.EntityStatusEvent;
import org.spout.vanilla.event.material.network.BlockActionEvent;
import org.spout.vanilla.event.material.network.BlockBreakAnimationEvent;
import org.spout.vanilla.event.material.network.EntityTileDataEvent;
import org.spout.vanilla.event.material.network.MapItemUpdateEvent;
import org.spout.vanilla.event.material.network.SignUpdateEvent;
import org.spout.vanilla.event.player.network.ListPingEvent;
import org.spout.vanilla.event.player.network.PingEvent;
import org.spout.vanilla.event.player.network.PlayerAbilityUpdateEvent;
import org.spout.vanilla.event.player.network.PlayerBedEvent;
import org.spout.vanilla.event.player.network.PlayerExperienceChangeEvent;
import org.spout.vanilla.event.player.network.PlayerGameStateEvent;
import org.spout.vanilla.event.player.network.PlayerHealthEvent;
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
import org.spout.vanilla.material.block.component.TileMaterial;
import org.spout.vanilla.protocol.container.VanillaContainer;
import org.spout.vanilla.protocol.msg.VanillaBlockDataChannelMessage;
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
import org.spout.vanilla.protocol.msg.player.conn.PlayerLoginRequestMessage;
import org.spout.vanilla.protocol.msg.player.conn.PlayerPingMessage;
import org.spout.vanilla.protocol.msg.player.pos.PlayerPositionLookMessage;
import org.spout.vanilla.protocol.msg.player.pos.PlayerRespawnMessage;
import org.spout.vanilla.protocol.msg.player.pos.PlayerSpawnPositionMessage;
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
import org.spout.vanilla.protocol.msg.world.block.BlockChangeMessage;
import org.spout.vanilla.protocol.msg.world.block.SignMessage;
import org.spout.vanilla.protocol.msg.world.chunk.ChunkDataMessage;
import org.spout.vanilla.protocol.reposition.VanillaRepositionManager;
import org.spout.vanilla.scoreboard.Objective;
import org.spout.vanilla.scoreboard.Team;
import org.spout.vanilla.world.generator.biome.VanillaBiome;
import org.spout.vanilla.world.lighting.VanillaCuboidLightBuffer;
import org.spout.vanilla.world.lighting.VanillaLighting;

import static org.spout.vanilla.material.VanillaMaterials.getMinecraftData;
import static org.spout.vanilla.material.VanillaMaterials.getMinecraftId;

public class VanillaServerNetworkSynchronizer extends ServerNetworkSynchronizer implements Listener {
	private static final int SOLID_BLOCK_ID = 1; // Initializer block ID
	private static final byte[] SOLID_CHUNK_DATA = new byte[Chunk.BLOCKS.HALF_VOLUME * 5];
	private static final byte[] AIR_CHUNK_DATA = new byte[Chunk.BLOCKS.HALF_VOLUME * 5];
	private static final double STANCE = 1.62001D;
	private static final int FORCE_MASK = 0xFF; // force an update to be sent every 5 seconds
	private static final int HASH_SEED = 0xB346D76A;
	public static final int WORLD_HEIGHT = 256;
	private boolean first = true;
	// Synchronized internally; no need for a lock
	private final TSyncIntPairObjectHashMap<TSyncIntHashSet> initColumns = new TSyncIntPairObjectHashMap<TSyncIntHashSet>();
	private final ConcurrentLinkedQueue<Long> emptyColumns = new ConcurrentLinkedQueue<Long>();
	private final TSyncIntPairHashSet activeColumns = new TSyncIntPairHashSet();
	private final ChunkInit chunkInit;
	private int minY = 0;
	private int maxY = 256;
	private int stepY = 160;
	private int offsetY = 0;
	private final VanillaRepositionManager vpm = new VanillaRepositionManager();
	private boolean teleported = false;
	private boolean teleportPending = false;
	private int tickCounter = 0;
	private volatile boolean worldChanged = false;
	/**
	 * The point that the player was at for the last tick. Used to check world changes
	 */
	//TODO: can we not check the player's live transform versus snapshot?
	private Point lastPosition = null;
	/**
	 * If the player is going to teleport, this is the point the player was at before. Used to prevent teleport -> free -> send
	 */
	private Point holdingPosition = null;
	private final LinkedHashSet<Chunk> observed = new LinkedHashSet<Chunk>();
	/**
	 * Includes chunks that need to be observed. When observation is successfully attained or no longer wanted, point is removed
	 */
	private final Set<Point> chunksToObserve = new LinkedHashSet<Point>();
	private Point lastChunkCheck = Point.invalid;
	// Base points used so as not to load chunks unnecessarily
	private final Set<Point> chunkInitQueue = new LinkedHashSet<Point>();
	private final Set<Point> priorityChunkSendQueue = new LinkedHashSet<Point>();
	private final Set<Point> chunkSendQueue = new LinkedHashSet<Point>();
	private final Set<Point> chunkFreeQueue = new LinkedHashSet<Point>();
	/**
	 * Chunks that have initialized on the client. May also have chunks that have been sent.
	 */
	private final Set<Point> initializedChunks = new LinkedHashSet<Point>();
	/**
	 * Chunks that have been sent to the client
	 */
	private final Set<Point> activeChunks = new LinkedHashSet<Point>();

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

	public VanillaServerNetworkSynchronizer(Session session) {
		// The minimum block distance is a radius for sending chunks before login/respawn
		// It needs to be > 0 for reliable login and preventing falling through the world
		super(session, 2);
		session.getEngine().getEventManager().registerEvents(this, session);
		chunkInit = ChunkInit.getChunkInit(VanillaConfiguration.CHUNK_INIT.getString("client"));
		setRepositionManager(vpm);
	}

	@EventHandler (order = Order.MONITOR)
	public void onChunkFree(ChunkFreeEvent event) {
		freeChunk(event.getPoint());
	}

	private void freeChunk(Point p) {
		int x = (int) p.getX() >> Chunk.BLOCKS.BITS;
		int y = (int) p.getY() >> Chunk.BLOCKS.BITS; // + SEALEVEL_CHUNK;
		int z = (int) p.getZ() >> Chunk.BLOCKS.BITS;

		RepositionManager rm = getRepositionManager();

		int cY = rm.convertChunkY(y);

		if (cY < 0 || cY >= WORLD_HEIGHT >> Chunk.BLOCKS.BITS) {
			return;
		}

		TIntSet column = initColumns.get(x, z);
		if (column != null) {
			column.remove(y);
			if (column.isEmpty()) {
				emptyColumns.add(IntPairHashed.key(x, z));
			}
		}
	}

	public boolean isTeleportPending() {
		return teleportPending;
	}

	public void clearTeleportPending() {
		if (!teleported) {
			teleportPending = false;
		}
	}

	/**
	 * Resets all chunk stores for the client.  This method is only called during the pre-snapshot part of the tick.
	 */
	protected void resetChunks() {
		priorityChunkSendQueue.clear();
		chunkSendQueue.clear();
		chunkFreeQueue.clear();
		chunkInitQueue.clear();
		activeColumns.clear();
		initColumns.clear();
		lastChunkCheck = Point.invalid;
		synchronizedEntities.clear();

		this.emptyColumns.clear();
		this.activeColumns.clear();
		this.initColumns.clear();
	}

	private int chunksSent = 0;
	private Set<Point> unsendable = new HashSet<Point>();

	private Iterator<Point> attemptSendChunk(Iterator<Point> i, Iterable<Point> queue, Point p) {
		Chunk c = p.getWorld().getChunkFromBlock(p, LoadOption.LOAD_ONLY);
		if (c == null) {
			unsendable.add(p);
			return i;
		}
		if (unsendable.contains(p)) {
			return i;
		}
		if (canSendChunk(c)) {
			Collection<Chunk> sent = doSendChunk(c);
			activeChunks.add(c.getBase());
			i.remove();
			if (sent != null) {
				for (Chunk s : sent) {
					Point base = s.getBase();
					boolean removed = priorityChunkSendQueue.remove(base);
					removed |= chunkSendQueue.remove(base);
					if (removed) {
						if (initializedChunks.contains(base)) {
							activeChunks.add(base);
						}
						chunksSent++;
						i = queue.iterator();
					}
				}
			}
			chunksSent++;
		} else {
			unsendable.add(p);
		}
		return i;
	}

	private void checkChunkUpdates(Point currentPosition) {

		// Recalculating these
		priorityChunkSendQueue.clear();
		chunkSendQueue.clear();
		chunkFreeQueue.clear();
		chunkInitQueue.clear();

		World world = currentPosition.getWorld();
		int bx = (int) currentPosition.getX();
		int by = (int) currentPosition.getY();
		int bz = (int) currentPosition.getZ();

		int cx = bx >> Chunk.BLOCKS.BITS;
		int cy = by >> Chunk.BLOCKS.BITS;
		int cz = bz >> Chunk.BLOCKS.BITS;

		Point playerChunkBase = Chunk.pointToBase(currentPosition);
		Point playerHoldingChunkBase = holdingPosition == null ? null : Chunk.pointToBase(holdingPosition);

		for (Point p : initializedChunks) {
			if (!isInViewVolume(p, playerChunkBase, viewDistance)) {
				if (playerHoldingChunkBase == null || p.getMaxDistance(playerHoldingChunkBase) > blockMinimumViewDistance) {
					chunkFreeQueue.add(p);
				}
			}
		}

		Iterator<IntVector3> itr = getViewableVolume(cx, cy, cz, viewDistance);

		while (itr.hasNext()) {
			IntVector3 v = itr.next();
			Point base = new Point(world, v.getX() << Chunk.BLOCKS.BITS, v.getY() << Chunk.BLOCKS.BITS, v.getZ() << Chunk.BLOCKS.BITS);
			boolean inTargetArea = playerChunkBase.getMaxDistance(base) <= blockMinimumViewDistance;
			if (!activeChunks.contains(base)) {
				if (inTargetArea) {
					priorityChunkSendQueue.add(base);
				} else {
					chunkSendQueue.add(base);
				}
			}
			if (!initializedChunks.contains(base)) {
				chunkInitQueue.add(base);
			}
		}
	}

	private void checkObserverUpdateQueue() {
		Iterator<Point> i = chunksToObserve.iterator();
		while (i.hasNext()) {
			Point p = i.next();
			if (!chunkInitQueue.contains(p) && !this.initializedChunks.contains(p)) {
				i.remove();
			} else {
				Chunk c = p.getWorld().getChunkFromBlock(p, LoadOption.NO_LOAD);
				if (c != null) {
					observe(c);
					i.remove();
				}
			}
		}
	}

	private void observe(Point p) {
		Chunk c = p.getWorld().getChunkFromBlock(p, LoadOption.NO_LOAD);
		if (c != null) {
			observe(c);
		} else {
			chunksToObserve.add(p);
		}
	}

	private void observe(Chunk c) {
		observed.add(c);
		c.refreshObserver(player);
	}

	private void removeObserver(Point p) {
		Chunk c = p.getWorld().getChunkFromBlock(p, LoadOption.NO_LOAD);
		if (c != null) {
			removeObserver(c);
		}
		chunksToObserve.remove(p);
	}

	private void removeObserver(Chunk c) {
		observed.remove(c);
		c.removeObserver(player);
	}

	private void initColumnRaw(Point p) {
		int x = p.getChunkX();
		int y = p.getChunkY();// + SEALEVEL_CHUNK;
		int z = p.getChunkZ();

		RepositionManager rm = getRepositionManager();

		int cY = rm.convertChunkY(y);

		if (cY < 0 || cY >= WORLD_HEIGHT >> Chunk.BLOCKS.BITS) {
			return;
		}

		TSyncIntHashSet column = initColumns.get(x, z);
		if (column == null) {
			column = new TSyncIntHashSet();
			TSyncIntHashSet oldColumn = initColumns.putIfAbsent(x, z, column);
			if (oldColumn != null) {
				column = oldColumn;
			}
		}
		column.add(y);
	}

	private static BlockMaterial[][] getColumnTopmostMaterials(Point p) {
		BlockMaterial[][] materials = new BlockMaterial[Chunk.BLOCKS.SIZE][Chunk.BLOCKS.SIZE];

		World w = p.getWorld();

		for (int xx = 0; xx < Chunk.BLOCKS.SIZE; xx++) {
			for (int zz = 0; zz < Chunk.BLOCKS.SIZE; zz++) {
				materials[xx][zz] = w.getTopmostBlock(p.getBlockX() + xx, p.getBlockZ() + zz, LoadOption.LOAD_GEN);
			}
		}
		return materials;
	}

	private static int[][] getColumnHeights(Point p) {
		int[][] heights = new int[Chunk.BLOCKS.SIZE][Chunk.BLOCKS.SIZE];

		World w = p.getWorld();

		for (int xx = 0; xx < Chunk.BLOCKS.SIZE; xx++) {
			for (int zz = 0; zz < Chunk.BLOCKS.SIZE; zz++) {
				heights[xx][zz] = w.getSurfaceHeight(p.getBlockX() + xx, p.getBlockZ() + zz, LoadOption.LOAD_GEN);
			}
		}
		return heights;
	}

	private static final byte[] emptySkyChunkData;
	private static final byte[] emptyGroundChunkData;

	static {
		emptySkyChunkData = new byte[Chunk.BLOCKS.HALF_VOLUME * 5];
		emptyGroundChunkData = new byte[Chunk.BLOCKS.HALF_VOLUME * 5];

		int j = Chunk.BLOCKS.VOLUME << 1;
		// Sky light = F
		for (int i = 0; i < Chunk.BLOCKS.HALF_VOLUME; i++) {
			emptySkyChunkData[j] = (byte) 0xFF;
		}
	}

	protected boolean canSendChunk(Chunk c) {
		if (activeColumns.contains(c.getX(), c.getZ())) {
			return true;
		}
		Collection<Chunk> chunks = chunkInit.getChunks(c);
		if (chunks == null) {
			return false;
		}
		return true;
	}

	@EventHandler (order = Order.LATEST)
	public void onChunkSendLatest(ChunkSendEvent event) {
		event.getMessages().clear();
	}

	// We want some extra handling here; therefore we want to handle our own sending.
	@EventHandler (order = Order.MONITOR)
	public void onChunkSendMonitor(ChunkSendEvent event) {
		if (event.isForced() || canSendChunk(event.getChunk())) {
			doSendChunk(event.getChunk());
		}
	}

	protected Collection<Chunk> doSendChunk(Chunk c) {

		int x = c.getX();
		int y = c.getY();// + SEALEVEL_CHUNK;
		int z = c.getZ();

		RepositionManager rm = getRepositionManager();

		int cY = rm.convertChunkY(y);

		if (cY < 0 || cY >= WORLD_HEIGHT >> Chunk.BLOCKS.BITS) {
			return null;
		}

		initColumnRaw(c.getBase());

		Collection<Chunk> chunks = null;

		List<ProtocolEvent> events = new ArrayList<ProtocolEvent>();

		if (activeColumns.add(x, z)) {
			Point p = c.getBase();
			int[][] heights = getColumnHeights(p);
			BlockMaterial[][] materials = getColumnTopmostMaterials(p);

			byte[][] packetChunkData = new byte[16][];

			for (int cube = 0; cube < 16; cube++) {
				int serverCube = rm.getInverse().convertChunkY(cube);
				Point pp = new Point(c.getWorld(), x << Chunk.BLOCKS.BITS, serverCube << Chunk.BLOCKS.BITS, z << Chunk.BLOCKS.BITS);
				packetChunkData[cube] = chunkInit.getChunkData(heights, materials, pp, events);
			}

			Chunk chunk = p.getWorld().getChunkFromBlock(p, LoadOption.LOAD_ONLY);
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
			player.getSession().send(CCMsg);

			chunks = chunkInit.getChunks(c);
		}

		if (chunks == null || !chunks.contains(c)) {

			byte[] fullChunkData = ChunkInit.getChunkFullData(c, events);

			byte[][] packetChunkData = new byte[16][];
			packetChunkData[cY] = fullChunkData;
			ChunkDataMessage CCMsg = new ChunkDataMessage(x, z, false, new boolean[16], packetChunkData, null, player.getSession(), getRepositionManager());
			player.getSession().send(CCMsg);

			if (chunks == null) {
				chunks = new ArrayList<Chunk>(1);
			}
			chunks.add(c);
		}

		for (ProtocolEvent e : events) {
			player.getNetwork().callProtocolEvent(e, player);
		}

		return chunks;
	}

	public void sendPosition() {
		Point p = player.getPhysics().getPosition();
		Quaternion rot = player.getPhysics().getRotation();
		PlayerPositionLookMessage PPLMsg = new PlayerPositionLookMessage(p.getX(), p.getY() + STANCE, p.getZ(), p.getY(), rot.getYaw(), rot.getPitch(), true, VanillaBlockDataChannelMessage.CHANNEL_ID, getRepositionManager());
		session.send(PPLMsg);
	}

	@EventHandler
	public void onPositionSend(PositionSendEvent event) {
		Point p = event.getPoint();
		Quaternion rot = event.getRotation();
		PlayerPositionLookMessage PPLMsg = new PlayerPositionLookMessage(p.getX(), p.getY() + STANCE, p.getZ(), p.getY(), rot.getYaw(), rot.getPitch(), true, VanillaBlockDataChannelMessage.CHANNEL_ID, getRepositionManager());
		event.getMessages().add(PPLMsg);
	}

	@EventHandler (order = Order.LATEST)
	public void onWorldChangeLatest(WorldChangeProtocolEvent event) {
		event.getMessages().clear();
	}

	// Same as chunk sending
	@EventHandler (order = Order.MONITOR)
	public void onWorldChangeMonitor(WorldChangeProtocolEvent event) {
		worldChanged(event.getWorld());
	}

	/**
	 * Sent as the initial login process if first is true, otherwise handled as a normal world change.
	 * @param world
	 */
	private void worldChanged(World world) {
		// TODO: this needs to be re-written to either be only about world-changes, or to handle first connection completely in the if statement.
		WorldConfigurationNode node = VanillaConfiguration.WORLDS.get(world);
		maxY = node.MAX_Y.getInt() & (~Chunk.BLOCKS.MASK);
		minY = node.MIN_Y.getInt() & (~Chunk.BLOCKS.MASK);
		stepY = node.STEP_Y.getInt() & (~Chunk.BLOCKS.MASK);
		int lowY = maxY - stepY;
		int highY = minY + stepY;
		lastY = Integer.MAX_VALUE;

		final ManagedMap data = world.getData();
		final Human human = player.add(Human.class);
		GameMode gamemode = null;
		Difficulty difficulty = data.get(VanillaData.DIFFICULTY);
		Dimension dimension = data.get(VanillaData.DIMENSION);
		WorldType worldType = data.get(VanillaData.WORLD_TYPE);

		int entityId = player.getId();

		if (first) {
			//  MC Packet Order: 0x01 Ligin, 0xFA Custom (ServerTypeName), 0x06 SpawnPos, 0xCA PlayerAbilities, 0x10 BlockSwitch
			first = false;
			if (human != null && human.getAttachedCount() > 1) {
				gamemode = human.getGameMode();
			} else {
				gamemode = data.get(VanillaData.GAMEMODE);
				if (human != null) {
					human.setGamemode(gamemode, false);
				}
			}

			Server server = (Server) session.getEngine();
			player.getSession().setState(State.GAME);
			PlayerLoginRequestMessage idMsg = new PlayerLoginRequestMessage(entityId, worldType.toString(), gamemode.getId(), (byte) dimension.getId(), difficulty.getId(), (byte) server.getMaxPlayers());
			player.getSession().send(idMsg);

		} else {
			if (human != null) {
				gamemode = human.getGameMode();
			}
			player.getSession().send(new PlayerRespawnMessage(0, difficulty.getId(), gamemode.getId(), 256, worldType.toString()));
			player.getSession().send(new PlayerRespawnMessage(1, difficulty.getId(), gamemode.getId(), 256, worldType.toString()));
			player.getSession().send(new PlayerRespawnMessage(dimension.getId(), difficulty.getId(), gamemode.getId(), 256, worldType.toString()));
		}

		Point pos = world.getSpawnPoint().getPosition();
		PlayerSpawnPositionMessage SPMsg = new PlayerSpawnPositionMessage((int) pos.getX(), (int) pos.getY(), (int) pos.getZ(), getRepositionManager());
		player.getSession().send(SPMsg);

		if (human != null) {
			human.updateAbilities();
		}

		session.send(new PlayerHeldItemChangeMessage(player.add(PlayerInventory.class).getQuickbar().getSelectedSlot().getIndex()));

		// Vanilla Only, MC does not send full inventory update on world change, nor on first connect
		PlayerInventory inv = player.get(PlayerInventory.class);
		if (inv != null) {
			inv.updateAll();
		}
		// TODO: Send scoreboard data here
		Sky sky;

		switch (dimension) {
			case NETHER:
				sky = world.add(NetherSky.class);
				break;
			case THE_END:
				sky = world.add(TheEndSky.class);
				break;
			default:
				sky = world.add(NormalSky.class);
		}
		sky.updatePlayer(player);
		// TODO: notify all connected players of login PacketChat 'using: server'
		// TODO: Send player all nearby players
		this.sendPosition(); // Send LookAndPosition
		sky.updatePlayer(player);
		// TODO: Handle custom texture packs
		// TODO: Send any player effects
	}

	private int lastY = Integer.MIN_VALUE;

	@Override
	public void finalizeTick() {
		super.finalizeTick();
		tickCounter++;
		Point currentPosition = player.getPhysics().getPosition();

		int y = currentPosition.getBlockY();

		if (y != lastY && !teleportPending) {

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
				teleported = true;
				teleportPending = true;
			}
		}

		//TODO: update chunk lists?
		final int prevViewDistance = viewDistance;
		final int currentViewDistance = player.getViewDistance() >> Chunk.BLOCKS.BITS;
		if (viewDistance != currentViewDistance) {
			viewDistance = currentViewDistance;
		}

		if (prevViewDistance != currentViewDistance || worldChanged || (!currentPosition.equals(lastChunkCheck) && currentPosition.getManhattanDistance(lastChunkCheck) > Chunk.BLOCKS.SIZE >> 1)) {
			checkChunkUpdates(currentPosition);
			lastChunkCheck = currentPosition;
			worldChanged = false;
		}
		if (lastPosition == null || lastPosition.getWorld() != currentPosition.getWorld()) {
			clearObservers();
			worldChanged = true;
			teleported = true;
			teleportPending = true;
		}

		lastPosition = currentPosition;

		if (!teleportPending) {
			holdingPosition = currentPosition;
		}

		if (!worldChanged) {
			for (Point p : chunkFreeQueue) {
				if (initializedChunks.contains(p)) {
					removeObserver(p);
				}
			}

			for (Point p : chunkInitQueue) {
				if (!initializedChunks.contains(p)) {
					observe(p);
				}
			}

			checkObserverUpdateQueue();
		}
	}

	@Override
	public void preSnapshot() {
		super.preSnapshot();
		if (worldChanged) {
			Point ep = player.getPhysics().getPosition();
			resetChunks();
			worldChanged(ep.getWorld());
		} else if (!worldChanged) {
			unsendable.clear();
			for (Point p : chunkFreeQueue) {
				if (initializedChunks.remove(p)) {
					freeChunk(p);
					activeChunks.remove(p);
				}
			}
			chunkFreeQueue.clear();

			int modifiedChunksPerTick = (!priorityChunkSendQueue.isEmpty() ? 4 : 1) * CHUNKS_PER_TICK;
			chunksSent = Math.max(0, chunksSent - modifiedChunksPerTick);

			for (Point p : chunkInitQueue) {
				if (initializedChunks.add(p)) {
					// TODO: protocol - init chunk?
				}
			}

			chunkInitQueue.clear();

			Iterator<Point> i;

			i = priorityChunkSendQueue.iterator();
			while (i.hasNext() && chunksSent < CHUNKS_PER_TICK) {
				Point p = i.next();
				i = attemptSendChunk(i, priorityChunkSendQueue, p);
			}

			if (!priorityChunkSendQueue.isEmpty()) {
				return;
			}

			if (teleported && !player.getPhysics().isTransformDirty()) {
				sendPosition();
				teleported = false;
			}

			boolean tickTimeRemaining = Spout.getScheduler().getRemainingTickTime() > 0;

			i = chunkSendQueue.iterator();
			while (i.hasNext() && chunksSent < CHUNKS_PER_TICK && tickTimeRemaining) {
				Point p = i.next();
				i = attemptSendChunk(i, chunkSendQueue, p);
				tickTimeRemaining = Spout.getScheduler().getRemainingTickTime() > 0;
			}
		}

		Long key;
		while ((key = this.emptyColumns.poll()) != null) {
			int x = IntPairHashed.key1(key);
			int z = IntPairHashed.key2(key);
			TIntSet column = initColumns.get(x, z);
			if (column != null && column.isEmpty()) {
				column = initColumns.remove(x, z);
				activeColumns.remove(x, z);
				session.send(new ChunkDataMessage(x, z, true, null, null, null, true, player.getSession(), getRepositionManager()));
			}
		}
	}

	@EventHandler
	public void onBlockUpdate(UpdateBlockEvent event) {
		BlockMaterial material = event.getChunk().getBlockMaterial(event.getX(), event.getY(), event.getZ());
		short data = event.getChunk().getBlockData(event.getX(), event.getY(), event.getZ());
		short id = getMinecraftId(material);
		int x = event.getX() + event.getChunk().getBlockX();
		int y = event.getY() + event.getChunk().getBlockY();
		int z = event.getZ() + event.getChunk().getBlockZ();
		BlockChangeMessage BCM = new BlockChangeMessage(x, y, z, id, getMinecraftData(material, data), getRepositionManager());
		session.send(BCM);
	}

	@Override
	public void syncEntity(Entity e, Transform liveTransform, boolean spawn, boolean destroy, boolean update) {
		super.syncEntity(e, liveTransform, spawn, destroy, update);
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
				messages.addAll(ep.getUpdateMessages(e, liveTransform, getRepositionManager(), force));
			}
			for (Message message : messages) {
				this.session.send(message);
			}
		}
	}

	private boolean shouldForce(int entityId) {
		int hash = HASH_SEED;
		hash += (hash << 5) + entityId;
		hash += (hash << 5) + tickCounter;
		return (hash & FORCE_MASK) == 0 || (VanillaPlugin.getInstance().getEngine().debugMode() && getPlayer().get(ForceMessages.class) != null);
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
	public void onEntityTileData(EntityTileDataEvent event) {
		Block b = event.getBlock();
		event.getMessages().add(new EntityTileDataMessage(b.getX(), b.getY(), b.getZ(), event.getAction(), event.getData(), getRepositionManager()));
	}

	@EventHandler
	public void onMapItemUpdate(MapItemUpdateEvent event) {
		event.getMessages().add(new EntityItemDataMessage(VanillaMaterials.MAP, (short) event.getItemData(), event.getData()));
	}

	@EventHandler
	public void onPlayerAbilityUpdate(PlayerAbilityUpdateEvent event) {
		event.getMessages().add(new PlayerAbilityMessage(event.getGodMode(), event.isFlying(), event.canFly(), event.isCreativeMode(), event.getFlyingSpeed(), event.getWalkingSpeed()));
	}

	@EventHandler
	public void onEntityEquipment(EntityEquipmentEvent event) {
		event.getMessages().add(new EntityEquipmentMessage(event.getEntity().getId(), event.getSlot(), event.getItem()));
	}

	@EventHandler
	public void onWindowOpen(WindowOpenEvent event) {
		if (event.getWindow() instanceof DefaultWindow) {
			event.getMessages().add(null); // no message for the default Window
		}
		PlayerInventory inventory = event.getWindow().getPlayerInventory();
		int size = event.getWindow().getSize() - (inventory.getMain().size() + inventory.getQuickbar().size());
		event.getMessages().add(new WindowOpenMessage(event.getWindow(), size));
	}

	@EventHandler
	public void onWindowClose(WindowCloseEvent event) {
		if (event.getWindow() instanceof DefaultWindow) {
			event.getMessages().add(null); // no message for the default Window
		}
		event.getMessages().add(new WindowCloseMessage(event.getWindow()));
	}

	@EventHandler
	public void onWindowSetSlot(WindowSlotEvent event) {
		//TODO: investigate why this happens (12-1-2013)
		if (event.getItem() != null && event.getItem().getMaterial() == BlockMaterial.AIR) {
			event.getMessages().add(null);
		}
		event.getMessages().add(new WindowSlotMessage(event.getWindow(), event.getSlot(), event.getItem()));
	}

	@EventHandler
	public void onWindowItems(WindowItemsEvent event) {
		event.getMessages().add(new WindowItemsMessage(event.getWindow(), event.getItems()));
	}

	@EventHandler
	public void onWindowProperty(WindowPropertyEvent event) {
		event.getMessages().add(new WindowPropertyMessage(event.getWindow(), event.getId(), event.getValue()));
	}

	@EventHandler
	public void onSoundEffect(PlaySoundEffectEvent event) {
		event.getMessages().add(new SoundEffectMessage(event.getSound().getName(), event.getPosition(), event.getVolume(), event.getPitch(), getRepositionManager()));
	}

	@EventHandler
	public void onExplosionEffect(PlayExplosionEffectEvent event) {
		event.getMessages().add(new ExplosionMessage(event.getPosition(), event.getSize(), new byte[0], getRepositionManager()));
	}

	@EventHandler
	public void onParticleEffect(PlayParticleEffectEvent event) {
		int x = event.getPosition().getBlockX();
		int y = event.getPosition().getBlockY();
		int z = event.getPosition().getBlockZ();
		event.getMessages().add(new EffectMessage(event.getEffect().getId(), x, y, z, event.getData(), getRepositionManager()));
	}

	@EventHandler
	public void onBlockAction(BlockActionEvent event) {
		int id = VanillaMaterials.getMinecraftId(event.getMaterial());
		if (id == -1) {
			event.getMessages().add(null);
		} else {
			event.getMessages().add(new BlockActionMessage(event.getBlock(), (short) id, event.getData1(), event.getData2(), getRepositionManager()));
		}
	}

	@EventHandler
	public void onPlayerKeepAlive(PingEvent event) {
		event.getMessages().add(new PlayerPingMessage(event.getHash()));
	}

	@EventHandler
	public void onPlayerUpdateUserList(ListPingEvent event) {
		String name = event.getPlayerDisplayName();
		event.getMessages().add(new PlayerListMessage(name, event.getOnline(), (short) event.getPingDelay()));
	}

	@EventHandler
	public void onPlayerBed(PlayerBedEvent event) {
		event.getMessages().add(new PlayerBedMessage(event.getPlayer(), event.getBed(), getRepositionManager()));
	}

	@EventHandler
	public void onEntityAnimation(EntityAnimationEvent event) {
		event.getMessages().add(new EntityAnimationMessage(event.getEntity().getId(), (byte) event.getAnimation().getId()));
	}

	@EventHandler
	public void onEntityStatus(EntityStatusEvent event) {
		event.getMessages().add(new EntityStatusMessage(event.getEntity().getId(), event.getStatus()));
	}

	@EventHandler
	public void onPlayerUpdateStats(PlayerHealthEvent event) {
		Hunger hunger = player.get(Hunger.class);
		event.getMessages().add(new PlayerHealthMessage((short) player.get(Human.class).getHealth().getHealth(), (short) hunger.getHunger(), hunger.getFoodSaturation()));
	}

	@EventHandler
	public void onEntityMetaChange(EntityMetaChangeEvent event) {
		event.getMessages().add(new EntityMetadataMessage(event.getEntity().getId(), event.getParameters()));
	}

	@EventHandler
	public void onSignUpdate(SignUpdateEvent event) {
		Sign sign = event.getSign();
		event.getMessages().add(new SignMessage(sign.getOwner().getX(), sign.getOwner().getY(), sign.getOwner().getZ(), event.getLines(), getRepositionManager()));
	}

	@EventHandler
	public void onEntityCollectItem(EntityCollectItemEvent event) {
		event.getMessages().add(new PlayerCollectItemMessage(event.getCollected().getId(), event.getEntity().getId()));
	}

	@EventHandler
	public void onPlayerGameState(PlayerGameStateEvent event) {
		event.getMessages().add(new PlayerGameStateMessage(event.getReason(), event.getGameMode()));
	}

	@EventHandler
	public void onWeatherChanged(WeatherChangeEvent event) {
		Weather newWeather = event.getNewWeather();
		if (newWeather.equals(Weather.RAIN) || newWeather.equals(Weather.THUNDERSTORM)) {
			event.getMessages().add(new PlayerGameStateMessage(PlayerGameStateMessage.BEGIN_RAINING));
		} else {
			event.getMessages().add(new PlayerGameStateMessage(PlayerGameStateMessage.END_RAINING));
		}
	}

	@EventHandler
	public void onTimeUpdate(TimeUpdateEvent event) {
		event.getMessages().add(new PlayerTimeMessage(event.getWorld().getAge(), event.getNewTime()));
	}

	@EventHandler
	public void onEntityRemoveEffect(EntityRemoveEffectEvent event) {
		System.out.println("Removing effect");
		event.getMessages().add(new EntityRemoveEffectMessage(event.getEntity().getId(), (byte) event.getEffect().getId()));
	}

	@EventHandler
	public void onBlockBreakAnimation(BlockBreakAnimationEvent event) {
		event.getMessages().add(new BlockBreakAnimationMessage(event.getEntity().getId(), (int) event.getPoint().getX(), (int) event.getPoint().getY(), (int) event.getPoint().getZ(), event.getLevel(), getRepositionManager()));
	}

	@EventHandler
	public void onEntityEffect(EntityEffectEvent event) {
		event.getMessages().add(new EntityEffectMessage(event.getEntity().getId(), (byte) event.getEffect().getType().getId(), (byte) 0, (short) (event.getEffect().getDuration() * 20)));
	}

	@EventHandler
	public void onExperienceChange(PlayerExperienceChangeEvent event) {
		Player player = event.getPlayer();
		Level level = player.get(Level.class);

		if (level != null) {
			event.getMessages().add(new PlayerExperienceMessage(level.getProgress(), level.getLevel(), event.getNewExp()));
		}
	}

	@EventHandler
	public void onPlayerSelectedSlotChange(PlayerSelectedSlotChangeEvent event) {
		event.getMessages().add(new PlayerHeldItemChangeMessage(event.getSelectedSlot()));
	}

	@EventHandler
	public void onObjectiveAction(ObjectiveActionEvent event) {
		Objective obj = event.getObjective();
		event.getMessages().add(new ScoreboardObjectiveMessage(obj.getName(), obj.getDisplayName(), event.getAction()));
	}

	@EventHandler
	public void onObjectiveDisplay(ObjectiveDisplayEvent event) {
		event.getMessages().add(new ScoreboardDisplayMessage((byte) event.getSlot().ordinal(), event.getObjectiveName()));
	}

	@EventHandler
	public void onScoreUpdate(ScoreUpdateEvent event) {
		event.getMessages().add(new ScoreboardScoreMessage(event.getKey(), event.isRemove(), event.getObjectiveName(), event.getValue()));
	}

	@EventHandler
	public void onTeamAction(TeamActionEvent event) {
		Team team = event.getTeam();
		event.getMessages().add(new ScoreboardTeamMessage(
				team.getName(), event.getAction(),
				team.getDisplayName(),
				team.getPrefix(), team.getSuffix(),
				team.isFriendlyFire(), event.getPlayers()
		));
	}

	@Override
	public Set<Chunk> getActiveChunks() {
		return Collections.EMPTY_SET;
	}

	public enum ChunkInit {
		CLIENT_SEL,
		FULL_COLUMN,
		HEIGHTMAP,
		EMPTY_COLUMN;

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

		public Collection<Chunk> getChunks(final Chunk c) {
			if (this.sendColumn()) {
				final int x = c.getX();
				final int z = c.getZ();
				final int height = WORLD_HEIGHT >> Chunk.BLOCKS.BITS;
				List<Chunk> chunks = new ArrayList<Chunk>(height);
				List<Vector3> ungenerated = new ArrayList<Vector3>(height);
				for (int y = 0; y < height; y++) {
					Chunk cc = c.getWorld().getChunk(x, y, z, LoadOption.LOAD_ONLY);
					if (cc == null) {
						c.getRegion().getTaskManager().scheduleSyncDelayedTask(VanillaPlugin.getInstance(), new Runnable() {
							public void run() {
								for (int y = 0; y < height; y++) {
									c.getWorld().getChunk(x, y, z, LoadOption.LOAD_GEN);
								}
							}
						});
						ungenerated.add(new Vector3(x, y, z));
					} else {
						chunks.add(cc);
					}
				}
				if (ungenerated.isEmpty()) {
					return chunks;
				} else {
					c.getWorld().queueChunksForGeneration(ungenerated);
					return null;
				}
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
			Chunk c = p.getWorld().getChunkFromBlock(p, LoadOption.LOAD_ONLY);
			return getChunkFullData(c, updateEvents);
		}

		public static byte[] getChunkFullData(Chunk c, List<ProtocolEvent> updateEvents) {

			VanillaContainer container = new VanillaContainer();
			c.fillBlockContainer(container);
			c.fillBlockComponentContainer(container);

			VanillaCuboidLightBuffer blockLight = (VanillaCuboidLightBuffer) c.getLightBuffer(VanillaLighting.BLOCK_LIGHT.getId());
			VanillaCuboidLightBuffer skyLight = (VanillaCuboidLightBuffer) c.getLightBuffer(VanillaLighting.SKY_LIGHT.getId());

			container.copyLight(true, blockLight);
			container.copyLight(false, skyLight);

			int[] componentX = container.getXArray();
			int[] componentY = container.getYArray();
			int[] componentZ = container.getZArray();

			for (int i = 0; i < container.getBlockComponentCount(); i++) {
				BlockMaterial bm = c.getBlockMaterial(componentX[i], componentY[i], componentZ[i]);
				BlockComponentOwner owner = container.getBlockComponent()[i];
				if (bm instanceof TileMaterial) {
					ProtocolEvent event = ((TileMaterial) bm).getUpdate(c.getWorld(), componentX[i], componentY[i], componentZ[i], owner);
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
				if (arg.equalsIgnoreCase(in)) {
					return true;
				}
			}
			return false;
		}
	}
}
