/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, VanillaDev <http://www.spout.org/>
 * Vanilla is licensed under the SpoutDev License Version 1.
 *
 * Vanilla is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the SpoutDev License Version 1.
 *
 * Vanilla is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the SpoutDev License Version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license,
 * including the MIT license.
 */
package org.spout.vanilla.protocol;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import gnu.trove.set.TIntSet;

import org.spout.api.Server;
import org.spout.api.Spout;
import org.spout.api.entity.Entity;
import org.spout.api.event.Event;
import org.spout.api.event.EventHandler;
import org.spout.api.generator.biome.Biome;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.cuboid.Chunk;
import org.spout.api.geo.cuboid.ChunkSnapshot;
import org.spout.api.geo.cuboid.ChunkSnapshot.BlockComponentSnapshot;
import org.spout.api.geo.cuboid.ChunkSnapshot.EntityType;
import org.spout.api.geo.cuboid.ChunkSnapshot.ExtraData;
import org.spout.api.geo.cuboid.ChunkSnapshot.SnapshotType;
import org.spout.api.geo.discrete.Point;
import org.spout.api.inventory.ItemStack;
import org.spout.api.material.BlockMaterial;
import org.spout.api.math.Quaternion;
import org.spout.api.protocol.EntityProtocol;
import org.spout.api.protocol.Message;
import org.spout.api.protocol.NetworkSynchronizer;
import org.spout.api.protocol.Session;
import org.spout.api.protocol.Session.State;
import org.spout.api.protocol.event.ProtocolEvent;
import org.spout.api.protocol.event.ProtocolEventListener;
import org.spout.api.util.hashing.IntPairHashed;
import org.spout.api.util.map.concurrent.TSyncIntPairObjectHashMap;
import org.spout.api.util.set.concurrent.TSyncIntHashSet;
import org.spout.api.util.set.concurrent.TSyncIntPairHashSet;

import org.spout.vanilla.VanillaPlugin;
import org.spout.vanilla.component.inventory.window.DefaultWindow;
import org.spout.vanilla.component.living.Human;
import org.spout.vanilla.component.substance.material.Sign;
import org.spout.vanilla.configuration.VanillaConfiguration;
import org.spout.vanilla.data.Difficulty;
import org.spout.vanilla.data.Dimension;
import org.spout.vanilla.data.GameMode;
import org.spout.vanilla.data.VanillaData;
import org.spout.vanilla.data.Weather;
import org.spout.vanilla.data.WorldType;
import org.spout.vanilla.event.block.BlockActionEvent;
import org.spout.vanilla.event.block.BlockControllerDataEvent;
import org.spout.vanilla.event.block.SignUpdateEvent;
import org.spout.vanilla.event.entity.EntityAnimationEvent;
import org.spout.vanilla.event.entity.EntityCollectItemEvent;
import org.spout.vanilla.event.entity.EntityEquipmentEvent;
import org.spout.vanilla.event.entity.EntityMetaChangeEvent;
import org.spout.vanilla.event.entity.EntityStatusEvent;
import org.spout.vanilla.event.player.network.PlayerBedEvent;
import org.spout.vanilla.event.player.network.PlayerGameStateEvent;
import org.spout.vanilla.event.player.network.PlayerHealthEvent;
import org.spout.vanilla.event.player.network.PlayerListEvent;
import org.spout.vanilla.event.player.network.PlayerPingEvent;
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
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.protocol.msg.entity.EntityAnimationMessage;
import org.spout.vanilla.protocol.msg.entity.EntityEquipmentMessage;
import org.spout.vanilla.protocol.msg.entity.EntityMetadataMessage;
import org.spout.vanilla.protocol.msg.entity.EntityStatusMessage;
import org.spout.vanilla.protocol.msg.entity.EntityTileDataMessage;
import org.spout.vanilla.protocol.msg.entity.pos.EntityTeleportMessage;
import org.spout.vanilla.protocol.msg.player.PlayerBedMessage;
import org.spout.vanilla.protocol.msg.player.PlayerCollectItemMessage;
import org.spout.vanilla.protocol.msg.player.PlayerGameStateMessage;
import org.spout.vanilla.protocol.msg.player.PlayerHealthMessage;
import org.spout.vanilla.protocol.msg.player.PlayerSoundEffectMessage;
import org.spout.vanilla.protocol.msg.player.PlayerTimeMessage;
import org.spout.vanilla.protocol.msg.player.conn.PlayerListMessage;
import org.spout.vanilla.protocol.msg.player.conn.PlayerLoginRequestMessage;
import org.spout.vanilla.protocol.msg.player.conn.PlayerPingMessage;
import org.spout.vanilla.protocol.msg.player.pos.PlayerPositionYawMessage;
import org.spout.vanilla.protocol.msg.player.pos.PlayerRespawnMessage;
import org.spout.vanilla.protocol.msg.player.pos.PlayerSpawnPositionMessage;
import org.spout.vanilla.protocol.msg.player.pos.PlayerYawMessage;
import org.spout.vanilla.protocol.msg.window.WindowCloseMessage;
import org.spout.vanilla.protocol.msg.window.WindowItemsMessage;
import org.spout.vanilla.protocol.msg.window.WindowOpenMessage;
import org.spout.vanilla.protocol.msg.window.WindowPropertyMessage;
import org.spout.vanilla.protocol.msg.window.WindowSlotMessage;
import org.spout.vanilla.protocol.msg.world.EffectMessage;
import org.spout.vanilla.protocol.msg.world.ExplosionMessage;
import org.spout.vanilla.protocol.msg.world.block.BlockActionMessage;
import org.spout.vanilla.protocol.msg.world.block.BlockChangeMessage;
import org.spout.vanilla.protocol.msg.world.block.SignMessage;
import org.spout.vanilla.protocol.msg.world.chunk.ChunkDataMessage;
import org.spout.vanilla.world.generator.biome.VanillaBiome;

import static org.spout.vanilla.material.VanillaMaterials.getMinecraftData;
import static org.spout.vanilla.material.VanillaMaterials.getMinecraftId;

public class VanillaNetworkSynchronizer extends NetworkSynchronizer implements ProtocolEventListener {
	private static final int SOLID_BLOCK_ID = 1; // Initializer block ID
	private static final byte[] SOLID_CHUNK_DATA = new byte[Chunk.BLOCKS.HALF_VOLUME * 5];
	private static final byte[] AIR_CHUNK_DATA = new byte[Chunk.BLOCKS.HALF_VOLUME * 5];
	private static final double STANCE = 1.6D;
	private boolean first = true;
	private final TSyncIntPairObjectHashMap<TSyncIntHashSet> initializedChunks = new TSyncIntPairObjectHashMap<TSyncIntHashSet>();
	private final ConcurrentLinkedQueue<Long> emptyColumns = new ConcurrentLinkedQueue<Long>();
	private TSyncIntPairHashSet activeChunks = new TSyncIntPairHashSet();
	private Object initChunkLock = new Object();
	private final ChunkInit chunkInit;

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
		super(session, 0);
		registerProtocolEvents(this);
		chunkInit = ChunkInit.getChunkInit(VanillaConfiguration.CHUNK_INIT.getString("client"));
	}

	@Override
	protected void freeChunk(Point p) {
		int x = (int) p.getX() >> Chunk.BLOCKS.BITS;
		int y = (int) p.getY() >> Chunk.BLOCKS.BITS; // + SEALEVEL_CHUNK;
		int z = (int) p.getZ() >> Chunk.BLOCKS.BITS;

		if (y < 0 || y >= p.getWorld().getHeight() >> Chunk.BLOCKS.BITS) {
			return;
		}

		TIntSet column = initializedChunks.get(x, z);
		if (column != null) {
			column.remove(y);
			if (column.isEmpty()) {
				emptyColumns.add(IntPairHashed.key(x, z));
			} // TODO - is this required?
			/*
			else {
				CCMsg = new ChunkDataMessage(x, z, false, null, null, null, true);
			}*/
		}
	}

	@Override
	protected void initChunk(Point p) {

		int x = p.getChunkX();
		int y = p.getChunkY();// + SEALEVEL_CHUNK;
		int z = p.getChunkZ();

		if (y < 0 || y >= p.getWorld().getHeight() >> Chunk.BLOCKS.BITS) {
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
	public Collection<Chunk> sendChunk(Chunk c) {

		int x = c.getX();
		int y = c.getY();// + SEALEVEL_CHUNK;
		int z = c.getZ();

		if (y < 0 || y >= c.getWorld().getHeight() >> Chunk.BLOCKS.BITS) {
			return null;
		}

		initChunk(c.getBase());

		Collection<Chunk> chunks = null;
		
		List<ProtocolEvent> events = new ArrayList<ProtocolEvent>();

		if (activeChunks.add(x, z)) {
			Point p = c.getBase();
			int[][] heights = getColumnHeights(p);
			BlockMaterial[][] materials = getColumnTopmostMaterials(p);

			byte[][] packetChunkData = new byte[16][];

			for (int cube = 0; cube < 16; cube++) {
				Point pp = new Point(c.getWorld(), x << Chunk.BLOCKS.BITS, cube << Chunk.BLOCKS.BITS, z << Chunk.BLOCKS.BITS);
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

			ChunkDataMessage CCMsg = new ChunkDataMessage(x, z, true, new boolean[16], packetChunkData, biomeData, player.getSession());
			player.getSession().send(false, CCMsg);

			chunks = chunkInit.getChunks(c);
		}

		if (chunks == null || !chunks.contains(c)) {

			byte[] fullChunkData = ChunkInit.getChunkFullData(c, events);

			byte[][] packetChunkData = new byte[16][];
			packetChunkData[y] = fullChunkData;
			ChunkDataMessage CCMsg = new ChunkDataMessage(x, z, false, new boolean[16], packetChunkData, null, player.getSession());
			player.getSession().send(false, CCMsg);
		}
		
		for (ProtocolEvent e : events) {
			this.callProtocolEvent(e);
		}

		return chunks;
	}

	@Override
	protected void sendPosition(Point p, Quaternion rot) {
		//TODO we have EntityProtocols in Vanilla....what does this actually do?
		Session session = player.getSession();
		if (p.distanceSquared(player.getTransform().getPosition()) >= 16) {
			EntityTeleportMessage ETMMsg = new EntityTeleportMessage(player.getId(), (int) p.getX(), (int) p.getY(), (int) p.getZ(), (int) rot.getYaw(), (int) rot.getPitch());
			PlayerYawMessage PLMsg = new PlayerYawMessage(rot.getYaw(), rot.getPitch(), true);
			session.sendAll(false, ETMMsg, PLMsg);
		} else {
			PlayerPositionYawMessage PPLMsg = new PlayerPositionYawMessage(p.getX(), p.getY() + STANCE, p.getZ(), STANCE, rot.getYaw(), rot.getPitch(), true);
			session.send(false, PPLMsg);
		}
	}

	@Override
	protected void worldChanged(World world) {
		GameMode gamemode = world.getComponentHolder().getData().get(VanillaData.GAMEMODE);
		Difficulty difficulty = world.getComponentHolder().getData().get(VanillaData.DIFFICULTY);
		Dimension dimension = world.getComponentHolder().getData().get(VanillaData.DIMENSION);
		WorldType worldType = world.getComponentHolder().getData().get(VanillaData.WORLD_TYPE);
		player.add(Human.class).setGamemode(gamemode, false);
		//TODO Handle infinite height
		if (first) {
			first = false;
			int entityId = player.getId();
			Server server = (Server) session.getEngine();
			PlayerLoginRequestMessage idMsg = new PlayerLoginRequestMessage(entityId, worldType.toString(), gamemode.getId(), (byte) dimension.getId(), difficulty.getId(), (byte) server.getMaxPlayers());
			player.getSession().send(false, true, idMsg);
			player.getSession().setState(State.GAME);
			for (int slot = 0; slot < 4; slot++) {
				ItemStack slotItem = player.add(Human.class).getInventory().getArmor().get(slot);
				player.getSession().send(false, new EntityEquipmentMessage(entityId, slot, slotItem));
			}
		} else {
			player.getSession().send(false, new PlayerRespawnMessage(dimension.getId(), difficulty.getId(), gamemode.getId(), 256, worldType.toString()));
		}

		Point pos = world.getSpawnPoint().getPosition();
		PlayerSpawnPositionMessage SPMsg = new PlayerSpawnPositionMessage((int) pos.getX(), (int) pos.getY(), (int) pos.getZ());
		player.getSession().send(false, SPMsg);
	}

	@Override
	public void preSnapshot() {
		super.preSnapshot();

		Long key;
		while ((key = this.emptyColumns.poll()) != null) {
			int x = IntPairHashed.key1(key);
			int z = IntPairHashed.key2(key);
			TIntSet column = initializedChunks.get(x, z);
			if (column.isEmpty()) {
				column = initializedChunks.remove(x, z);
				activeChunks.remove(x, z);
				session.send(false, new ChunkDataMessage(x, z, true, null, null, null, true, player.getSession()));
			}
		}
	}

	@Override
	public void updateBlock(Chunk chunk, int x, int y, int z, BlockMaterial material, short data) {
		short id = getMinecraftId(material);
		x += chunk.getBlockX();
		y += chunk.getBlockY();
		z += chunk.getBlockZ();
		if (y >= 0 && y < chunk.getWorld().getHeight()) {
			BlockChangeMessage BCM = new BlockChangeMessage(x, y, z, id, getMinecraftData(material, data));
			session.send(false, BCM);
		}
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
				messages.addAll(ep.getSpawnMessages(e));
			}
			if (update) {
				messages.addAll(ep.getUpdateMessages(e));
			}
			for (Message message : messages) {
				this.session.send(false, message);
			}
		}
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
		int size = event.getWindow().getInventorySize() - event.getWindow().getOwner().get(Human.class).getInventory().getMain().size();
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
		return new WindowSlotMessage(event.getWindow(), event.getSlot(), event.getItem());
	}

	@EventHandler
	public Message onWindowSetSlots(WindowItemsEvent event) {
		return new WindowItemsMessage(event.getWindow(), event.gets());
	}

	@EventHandler
	public Message onWindowProperty(WindowPropertyEvent event) {
		return new WindowPropertyMessage(event.getWindow(), event.getId(), event.getValue());
	}

	@EventHandler
	public Message onSoundEffect(PlaySoundEffectEvent event) {
		return new PlayerSoundEffectMessage(event.getSound().getName(), event.getPosition(), event.getVolume(), event.getPitch());
	}

	@EventHandler
	public Message onExplosionEffect(PlayExplosionEffectEvent event) {
		return new ExplosionMessage(event.getPosition(), event.getSize(), new byte[0]);
	}

	@EventHandler
	public Message onParticleEffect(PlayParticleEffectEvent event) {
		int x = event.getPosition().getBlockX();
		int y = event.getPosition().getBlockY();
		int z = event.getPosition().getBlockZ();
		if (y < 0 || y > 255) {
			return null; // don't play effects outside the byte range
		}
		return new EffectMessage(event.getEffect().getId(), x, y, z, event.getData());
	}

	@EventHandler
	public Message onBlockAction(BlockActionEvent event) {
		int id = VanillaMaterials.getMinecraftId(event.getMaterial());
		if (id == -1) {
			return null;
		} else {
			return new BlockActionMessage(event.getBlock(), (short) id, event.getData1(), event.getData2());
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
		return new PlayerBedMessage(event.getPlayer(), event.getBed());
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
		//TODO: Fix these stats?
		return new PlayerHealthMessage((short) player.get(Human.class).getHealth().getHealth(), (short) 0, 0);
	}

	@EventHandler
	public Message onEntityMetaChange(EntityMetaChangeEvent event) {
		return new EntityMetadataMessage(event.getEntity().getId(), event.getParameters());
	}

	@EventHandler
	public Message onSignUpdate(SignUpdateEvent event) {
		Sign sign = event.getSign();
		return new SignMessage(sign.getOwner().getX(), sign.getOwner().getY(), sign.getOwner().getZ(), event.getLines());
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
		return new PlayerTimeMessage(event.getNewTime());
	}

	@EventHandler
	public Message onBlockControllerData(BlockControllerDataEvent event) {
		Block b = event.getBlock();
		return new EntityTileDataMessage(b.getX(), b.getY(), b.getZ(), event.getAction(), event.getData());
	}

	public static enum ChunkInit {
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
			ChunkSnapshot snapshot = c.getSnapshot(SnapshotType.BOTH, EntityType.BLOCK_COMPONENTS, ExtraData.NO_EXTRA_DATA);
			short[] rawBlockIdArray = snapshot.getBlockIds();
			short[] rawBlockData = snapshot.getBlockData();
			byte[] rawBlockLight = snapshot.getBlockLight();
			byte[] rawSkyLight = snapshot.getSkyLight();
			byte[] fullChunkData = new byte[Chunk.BLOCKS.HALF_VOLUME * 5];

			int arrIndex = 0;
			BlockMaterial material1, material2;
			for (int i = 0; i < rawBlockIdArray.length; i += 2) {
				material1 = BlockMaterial.get(rawBlockIdArray[i]);
				material2 = BlockMaterial.get(rawBlockIdArray[i + 1]);
				// data
				fullChunkData[arrIndex + rawBlockIdArray.length] = (byte) ((getMinecraftData(material2, rawBlockData[i + 1]) << 4) | (getMinecraftData(material1, rawBlockData[i])));
				// id
				fullChunkData[i] = (byte) (getMinecraftId(material1) & 0xFF);
				fullChunkData[i + 1] = (byte) (getMinecraftId(material2) & 0xFF);

				arrIndex++;
			}
			
			for (BlockComponentSnapshot component : snapshot.getBlockComponents()) {
				System.out.println("Found block component: " + component.getComponent().getName());
				if (Sign.class.isAssignableFrom(component.getComponent())) {
					Sign sign = (Sign)c.getBlockComponent(component.getX(), component.getY(), component.getZ());
					updateEvents.add(new SignUpdateEvent(sign, sign.getText()));
					System.out.println("Found sign component: " + Arrays.toString(sign.getText()));
				}
			}

			arrIndex = rawBlockIdArray.length + (rawBlockData.length >> 1);

			// The old method which doesn't use the Minecraft data conversion function
			/*
			for (int i = 0; i < rawBlockData.length; i += 2) {
				fullChunkData[arrIndex++] = (byte) ((rawBlockData[i + 1] << 4) | (rawBlockData[i] & 0xF));
			}
			*/

			System.arraycopy(rawBlockLight, 0, fullChunkData, arrIndex, rawBlockLight.length);
			arrIndex += rawBlockLight.length;

			System.arraycopy(rawSkyLight, 0, fullChunkData, arrIndex, rawSkyLight.length);

			arrIndex += rawSkyLight.length;
			return fullChunkData;
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
