package org.spout.vanilla.entity;

import java.lang.reflect.Constructor;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.spout.api.entity.Controller;
import org.spout.api.entity.Entity;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Chunk;
import org.spout.api.geo.cuboid.Region;
import org.spout.api.geo.discrete.Point;
import org.spout.api.material.BlockMaterial;
import org.spout.api.player.Player;

public class RegionEntitySpawner extends Controller {
	private static Map<Class<? extends Controller>, Boolean> validConstructors = new ConcurrentHashMap<Class<? extends Controller>, Boolean>();
	private static final int SPAWN_TRIES = 6;
	
	final Region region;
	final Random rand = new Random();
	final Map<Class<? extends Controller>, SpawnInformation> spawnableTypes = new ConcurrentHashMap<Class<? extends Controller>, SpawnInformation>();
	public RegionEntitySpawner(Region region) {
		this.region = region;
	}
	
	/**
	 * Adds a controller type to the list of entity types this region spawner will try to spawn.
	 * 
	 * @throws IllegalStateException if the controller lacks a default no-argument constructor
	 * @param type to spawn
	 * @param canSpawnOn a set of block materials that this entity can spawn on
	 * @param amount of this type of entity that should be spawned per chunk, on average
	 */
	public void addSpawnableType(Class<? extends Controller> type, Set<BlockMaterial> canSpawnOn, int amount) {
		Boolean existing = validConstructors.get(type);
		boolean valid = existing != null && existing.booleanValue();
		
		if (!valid) {
			Constructor<?>[] constructors = type.getConstructors();
			for (Constructor<?> constructor : constructors) {
				if (constructor.getParameterTypes().length == 0) {
					valid = true;
					validConstructors.put(type, true);
					break;
				}
			}
		}
		
		if (valid) {
			spawnableTypes.put(type, new SpawnInformation(amount, canSpawnOn));
		}
		else {
			throw new IllegalStateException("Class " + type + " is does not have a default constructor");
		}
	}
	
	/**
	 * Region this spawner manages.
	 * 
	 * @return region
	 */
	public Region getRegion() {
		return region;
	}

	@Override
	public void onAttached() {

	}

	@Override
	public void onTick(float dt) {
		World world = region.getWorld();
		Set<Player> players = world.getPlayers();
		if (players.size() == 0) {
			return;
		}
		for (int dx = 0; dx < Region.REGION_SIZE; dx++) {
			for (int dy = 0; dy < Region.REGION_SIZE; dy++) {
				for (int dz = 0; dz < Region.REGION_SIZE; dz++) {
					Chunk chunk = region.getChunk(dx, dy, dz, false);
					if (chunk != null && chunk.isLoaded() && chunk.isPopulated()) {
						spawn(chunk);
					}
				}
			}
		}
	}
	
	public void spawn(Chunk chunk) {
		Iterator<Entry<Class<? extends Controller>, SpawnInformation>> i = spawnableTypes.entrySet().iterator();
		while(i.hasNext()) {
			Entry<Class<? extends Controller>, SpawnInformation> entry = i.next();
			SpawnInformation info = entry.getValue();
			Set<Entity> existing = region.getAll(entry.getKey());
			if (existing.size() < info.amount) {
				int randX = this.rand.nextInt(Chunk.CHUNK_SIZE);
				int randY = this.rand.nextInt(Chunk.CHUNK_SIZE);
				int randZ = this.rand.nextInt(Chunk.CHUNK_SIZE);
				
				//TODO: check for nearby player?
				
				BlockMaterial material = chunk.getBlockMaterial(randX, randY, randZ);
				if (info.canSpawnOn.contains(material) && canSpawnAt(chunk, randX, randY + 1, randZ)) {
					for (int tries = 0; tries < SPAWN_TRIES; tries++) {
						int x = randX + (this.rand.nextInt(SPAWN_TRIES) - this.rand.nextInt(SPAWN_TRIES));
						int y = randY + 1;
						int z = randZ  + (this.rand.nextInt(SPAWN_TRIES) - this.rand.nextInt(SPAWN_TRIES));
						if (canSpawnAt(chunk, x, y, z)) {
							Constructor<?> construct = null;
							Constructor<?>[] constructors = entry.getKey().getConstructors();
							for (Constructor<?> constructor : constructors) {
								if (constructor.getParameterTypes().length == 0) {
									construct = constructor;
									break;
								}
							}
							if (construct != null) {
								try {
									Controller controller = (Controller)construct.newInstance();
									x += chunk.getX() * 16 + 0.5F;
									y += chunk.getY() * 16 + 1F;
									z += chunk.getZ() * 16 + 0.5F;
									region.getWorld().createAndSpawnEntity(new Point(region.getWorld(), x, y, z), controller);
								} catch (Exception e) {
									throw new RuntimeException("Unable to spawn " + entry.getKey(), e);
								}
							}
						}
					}
				}
			}
		}
	}

	private boolean canSpawnAt(Chunk chunk, int randX, int randY, int randZ) {
		int x = chunk.getX() * 16 + randX;
		int y = chunk.getY() * 16 + randY;
		int z = chunk.getZ() * 16 + randZ;
		World world = chunk.getWorld();
		if (world.getBlockId(x, y + 1, z) != 0) {
			return false;
		}
		if (world.getBlockId(x + 1, y, z) != 0) {
			return false;
		}
		if (world.getBlockId(x - 1, y, z) != 0) {
			return false;
		}
		if (world.getBlockId(x, y, z + 1) != 0) {
			return false;
		}
		if (world.getBlockId(x, y, z - 1) != 0) {
			return false;
		}
		return true;
	}

	@Override
	public void preSnapshot() {
	}
}

class SpawnInformation {
	protected final int amount;
	protected final Set<BlockMaterial> canSpawnOn;
	
	public SpawnInformation(int amount, Set<BlockMaterial> canSpawnOn) {
		this.amount = amount;
		this.canSpawnOn = canSpawnOn;
	}
}
