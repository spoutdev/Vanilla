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
package org.spout.vanilla.material;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.spout.api.Spout;
import org.spout.api.collision.CollisionStrategy;
import org.spout.api.entity.Player;
import org.spout.api.event.Cause;
import org.spout.api.geo.LoadOption;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.cuboid.Chunk;
import org.spout.api.inventory.ItemStack;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.block.BlockFaces;
import org.spout.api.material.range.CuboidEffectRange;
import org.spout.api.material.range.EffectRange;
import org.spout.api.math.GenericMath;
import org.spout.api.math.IntVector3;
import org.spout.api.math.Vector2;
import org.spout.api.plugin.Platform;
import org.spout.api.render.RenderMaterial;
import org.spout.api.util.flag.Flag;
import org.spout.api.util.flag.FlagBundle;

import org.spout.vanilla.component.substance.object.Item;
import org.spout.vanilla.component.world.VanillaSky;
import org.spout.vanilla.data.Instrument;
import org.spout.vanilla.data.MoveReaction;
import org.spout.vanilla.data.RedstonePowerMode;
import org.spout.vanilla.data.VanillaData;
import org.spout.vanilla.data.VanillaRenderMaterials;
import org.spout.vanilla.data.drops.flag.DropFlags;
import org.spout.vanilla.data.drops.flag.PlayerFlags;
import org.spout.vanilla.data.drops.type.block.BlockDrops;
import org.spout.vanilla.data.effect.SoundEffect;
import org.spout.vanilla.data.effect.store.SoundEffects;
import org.spout.vanilla.data.tool.ToolLevel;
import org.spout.vanilla.data.tool.ToolType;
import org.spout.vanilla.event.block.BlockActionEvent;
import org.spout.vanilla.material.block.redstone.IndirectRedstoneSource;
import org.spout.vanilla.material.block.redstone.RedstoneSource;
import org.spout.vanilla.render.VanillaEffects;

public abstract class VanillaBlockMaterial extends BlockMaterial implements VanillaMaterial, IndirectRedstoneSource {
	public static short REDSTONE_POWER_MAX = 15;
	public static short REDSTONE_POWER_MIN = 0;
	private final int minecraftId;
	private float resistance;
	private int meleeDamage = 1;
	private boolean liquidObstacle = true;
	private SoundEffect stepSound = SoundEffects.STEP_STONE;
	private final BlockDrops drops = new BlockDrops();
	private Set<ToolType> miningTypes = new HashSet<ToolType>();
	private ToolLevel miningLevel = ToolLevel.NONE;
	private final Vector2 pos = null; // TODO: Block item rendering

	public VanillaBlockMaterial(String name, int id, String model) {
		this((short) 0, name, id, model);
		if (Spout.getEngine().getPlatform() == Platform.CLIENT) {
			if (!getModel().getRenderMaterial().getRenderEffects().contains(VanillaEffects.SKY_TIME)) {
				getModel().getRenderMaterial().addRenderEffect(VanillaEffects.SKY_TIME);
				getModel().getRenderMaterial().addBufferEffect(VanillaEffects.LIGHTING);
			}
		}
	}

	public VanillaBlockMaterial(short dataMask, String name, int id, String model) {
		super(dataMask, name, model);
		this.minecraftId = id;
		this.setCollision(CollisionStrategy.NOCOLLIDE);
		this.setTransparent();
		this.getDrops().SILK_TOUCH.add(this);
		this.getDrops().DEFAULT.add(this);
		this.getDrops().EXPLOSION.add(this);
		if (Spout.getEngine().getPlatform() == Platform.CLIENT) {
			if (!getModel().getRenderMaterial().getRenderEffects().contains(VanillaEffects.SKY_TIME)) {
				getModel().getRenderMaterial().addRenderEffect(VanillaEffects.SKY_TIME);
				getModel().getRenderMaterial().addBufferEffect(VanillaEffects.LIGHTING);
			}
		}
	}

	public VanillaBlockMaterial(String name, int id, int data, VanillaBlockMaterial parent, String model) {
		super(name, data, parent, model);
		this.minecraftId = id;
		this.setCollision(CollisionStrategy.NOCOLLIDE);
		this.setTransparent();
		this.getDrops().SILK_TOUCH.add(this);
		this.getDrops().DEFAULT.add(this);
		this.getDrops().EXPLOSION.add(this);
		if (Spout.getEngine().getPlatform() == Platform.CLIENT) {
			if (!getModel().getRenderMaterial().getRenderEffects().contains(VanillaEffects.SKY_TIME)) {
				getModel().getRenderMaterial().addRenderEffect(VanillaEffects.SKY_TIME);
				getModel().getRenderMaterial().addBufferEffect(VanillaEffects.LIGHTING);
			}
		}
	}

	@Override
	public final int getMinecraftId() {
		return minecraftId;
	}

	@Override
	public short getMinecraftData(short data) {
		return (short) (data & 0xF);
	}

	@Override
	public RenderMaterial getRenderMaterial() {
		return VanillaRenderMaterials.BLOCKS_MATERIAL;
	}

	@Override
	public Vector2 getSpritePosition() {
		return pos;
	}

	/**
	 * Called when this block is destroyed because of an explosion
	 * @param block that got ignition
	 * @param cause of the ignition
	 */
	public void onIgnite(Block block, Cause<?> cause) {
		HashSet<Flag> dropFlags = new HashSet<Flag>();
		if (GenericMath.getRandom().nextInt(100) < 77) {
			dropFlags.add(DropFlags.NO_DROPS);
		} else {
			dropFlags.add(DropFlags.EXPLOSION_DROPS);
		}
		this.destroy(block, dropFlags, cause);
	}

	@Override
	public void onPostDestroy(Block block, Set<Flag> flags) {
		//TODO stack items together for more performance
		final Random random = GenericMath.getRandom();
		for (ItemStack item : this.getDrops().getDrops(random, flags)) {
			Item.dropNaturally(block.getPosition(), item);
		}
	}

	@Override
	public void onUpdate(BlockMaterial oldMaterial, Block block) {
		super.onUpdate(oldMaterial, block);
	}

	@Override
	public boolean canCreate(Block block, short data, Cause<?> cause) {
		return !block.getMaterial().isPlacementObstacle();
	}

	@Override
	public VanillaBlockMaterial setFriction(float friction) {
		return (VanillaBlockMaterial) super.setFriction(friction);
	}

	@Override
	public VanillaBlockMaterial setHardness(float hardness) {
		return (VanillaBlockMaterial) super.setHardness(hardness);
	}

	/**
	 * Sets the sound played when the block is being walked over or is being placed
	 * @param sound to play
	 * @return this Material
	 */
	public VanillaBlockMaterial setStepSound(SoundEffect sound) {
		this.stepSound = sound;
		return this;
	}

	/**
	 * Gets the sound played when the block is being walked over or is being placed
	 * @return step sound
	 */
	public SoundEffect getStepSound() {
		return this.stepSound;
	}

	@Override
	public boolean isPlacementObstacle() {
		return true;
	}

	@Override
	public boolean hasPhysics() {
		return false;
	}

	@Override
	public boolean hasNBTData() {
		return false;
	}

	@Override
	public int getDamage() {
		return this.meleeDamage;
	}

	@Override
	public VanillaBlockMaterial setDamage(int damage) {
		this.meleeDamage = damage;
		return this;
	}

	/**
	 * Gets the instrument note blocks play with this block material below
	 * @return the instrument
	 */
	public Instrument getInstrument() {
		return Instrument.PIANO;
	}

	@Override
	public boolean isRedstoneConductor() {
		return false;
	}

	@Override
	public short getIndirectRedstonePower(Block block, BlockFace direction, RedstonePowerMode powerMode) {
		return this.getRedstonePower(block);
	}

	@Override
	public final boolean hasIndirectRedstonePower(Block block, BlockFace direction, RedstonePowerMode powerMode) {
		return this.getIndirectRedstonePower(block, direction, powerMode) > 0;
	}

	@Override
	public final short getRedstonePower(Block block) {
		return this.getRedstonePower(block, RedstonePowerMode.ALL);
	}

	@Override
	public short getRedstonePower(Block block, RedstonePowerMode powerMode) {
		if (!this.isRedstoneConductor()) {
			return REDSTONE_POWER_MIN;
		}

		short power = 0;
		Block neigh;
		BlockMaterial mat;
		for (BlockFace face : BlockFaces.NESWBT) {
			neigh = block.translate(face);
			mat = neigh.getMaterial();
			if (mat instanceof RedstoneSource) {
				power = (short) Math.max(power, ((RedstoneSource) mat).getDirectRedstonePower(neigh, face.getOpposite(), powerMode));
			}
		}
		return power;
	}

	@Override
	public final boolean hasRedstonePower(Block block) {
		return this.hasRedstonePower(block, RedstonePowerMode.ALL);
	}

	@Override
	public final boolean hasRedstonePower(Block block, RedstonePowerMode powerMode) {
		return this.getRedstonePower(block, powerMode) > 0;
	}

	public VanillaBlockMaterial setResistance(Float newResistance) {
		resistance = newResistance;
		return this;
	}

	public float getResistance() {
		return resistance;
	}

	/**
	 * Gets whether this block material can support the attachable block material to the face given
	 * @param material to attach
	 * @param face of this block to attach to
	 * @return
	 */
	public boolean canSupport(BlockMaterial material, BlockFace face) {
		return false;
	}

	/**
	 * Gets the move reaction of block material for the block specified
	 * @param block that is being moved
	 * @return the move reaction
	 */
	public MoveReaction getMoveReaction(Block block) {
		return MoveReaction.BREAK;
	}

	/**
	 * Gets whether this block material cancels block placement when clicked
	 */
	public boolean isPlacementSuppressed() {
		return false;
	}

	/**
	 * Gets if this material is a liquid obstacle
	 * @return True if it can stop liquids, False if this material gets destroyed
	 */
	public boolean isLiquidObstacle() {
		return this.liquidObstacle;
	}

	/**
	 * Sets if this material is a liquid obstacle
	 * @param state True to make it an obstacle, False to let liquids destroy this block
	 */
	public VanillaBlockMaterial setLiquidObstacle(boolean state) {
		this.liquidObstacle = state;
		return this;
	}

	private void updateDropFlags() {
		this.drops.NOT_CREATIVE.clearFlags();
		this.drops.NOT_CREATIVE.addFlags(PlayerFlags.CREATIVE.NOT);
		if (this.miningLevel != null && this.miningLevel != ToolLevel.NONE) {
			this.drops.NOT_CREATIVE.addFlags(this.miningLevel.getDropFlag());
			Flag[] typeFlags = new Flag[this.miningTypes.size()];
			int i = 0;
			for (ToolType type : this.miningTypes) {
				typeFlags[i] = type.getDropFlag();
				i++;
			}
			this.drops.NOT_CREATIVE.addFlags(new FlagBundle(typeFlags));
		}
	}

	public VanillaBlockMaterial setMiningLevel(ToolLevel miningLevel) {
		this.miningLevel = miningLevel;
		updateDropFlags();
		return this;
	}

	/**
	 * Adds the mining type to this Block material<br>
	 * This type will be used when checking for drops and when calculating the digging time
	 * @param miningType to add
	 * @return this material
	 */
	public VanillaBlockMaterial addMiningType(ToolType miningType) {
		this.miningTypes.add(miningType);
		updateDropFlags();
		return this;
	}

	/**
	 * Gets if the mining type is set for this Block material<br>
	 * This type is used when checking for drops and when calculating digging time
	 * @return True if the tool type is set
	 */
	public boolean isMiningType(ToolType toolType) {
		return this.miningTypes.contains(toolType);
	}

	/**
	 * Gets the mining level required for breaking this BlockMaterial<br>
	 * This level has to be met to spawn drops
	 * @return mining level
	 */
	public ToolLevel getMiningLevel() {
		return miningLevel;
	}

	/**
	 * Gets the drops for this block material
	 * @return the drops
	 */
	public BlockDrops getDrops() {
		return this.drops;
	}

	// Utilities
	private static final EffectRange FARMLAND_CHECK_RANGE = new CuboidEffectRange(-1, -1, -1, 1, -1, 1);

	/**
	 * Plays a block action for this type of Block Material
	 * @param block to play at
	 * @param arg1 for the action
	 * @param arg2 for the action
	 */
	public static void playBlockAction(Block block, byte arg1, byte arg2) {
		BlockActionEvent event = new BlockActionEvent(block, block.getMaterial(), arg1, arg2);
		for (Player player : block.getChunk().getObservingPlayers()) {
			player.getNetworkSynchronizer().callProtocolEvent(event);
		}
	}

	/**
	 * Gets if rain is falling nearby the block specified
	 * @param block to check it nearby of
	 * @return True if it is raining, False if not
	 */
	public static boolean hasRainNearby(Block block) {
		VanillaSky sky = VanillaSky.getSky(block.getWorld());
		if (sky.hasWeather()) {
			if (sky.getWeatherSimulator().isRainingAt(block.getX(), block.getY(), block.getZ(), false)) {
				for (BlockFace face : BlockFaces.NESW) {
					if (block.translate(face).isAtSurface()) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Gets if rain is falling on top of the block specified
	 * @param block to check
	 * @return True if rain is falling on the Block, false if not
	 */
	public static boolean isRaining(Block block) {
		return block.getWorld().getComponentHolder().getData().get(VanillaData.WORLD_WEATHER).isRaining() && block.isAtSurface();
	}

	/**
	 * Gets the chance of a crop block growing<br>
	 * The higher the value, the lower the chance.
	 * @param block to check
	 * @return the growth chance
	 */
	public static int getCropGrowthChance(Block block) {
		BlockMaterial material = block.getMaterial();
		float rate = 1.0f;
		float farmLandRate;
		Block rel;
		for (IntVector3 coord : FARMLAND_CHECK_RANGE) {
			rel = block.translate(coord);
			if (rel.isMaterial(VanillaMaterials.FARMLAND)) {
				if (VanillaMaterials.FARMLAND.isWet(rel)) {
					farmLandRate = 3.0f;
				} else {
					farmLandRate = 1.0f;
				}
				if (rel.getX() != 0 && rel.getZ() != 0) {
					// this is farmland at a neighbor
					farmLandRate /= 4.0f;
				}
				rate += farmLandRate;
			}
		}

		// Half, yes or no? Check for neighboring crops
		if (block.translate(-1, 0, -1).isMaterial(material) || block.translate(1, 0, 1).isMaterial(material)
				|| block.translate(-1, 0, 1).isMaterial(material) || block.translate(1, 0, -1).isMaterial(material)) {
			return (int) (50f / rate);
		}
		if ((block.translate(-1, 0, 0).isMaterial(material) || block.translate(1, 0, 0).isMaterial(material))
				&& (block.translate(0, 0, -1).isMaterial(material) || block.translate(0, 0, 1).isMaterial(material))) {
			return (int) (50f / rate);
		}
		return (int) (25f / rate);
	}

	/**
	 * Gets a vertical column of chunks
	 * @param middle chunk
	 * @return list of chunks in the column
	 */
	public static List<Chunk> getChunkColumn(Chunk middle) {
		Chunk top = middle;
		Chunk tmp;
		while (true) {
			tmp = top.getRelative(BlockFace.TOP, LoadOption.NO_LOAD);
			if (tmp != null && tmp.isLoaded()) {
				top = tmp;
			} else {
				break;
			}
		}
		List<Chunk> rval = new ArrayList<Chunk>();
		while (top != null && top.isLoaded()) {
			rval.add(top);
			top = top.getRelative(BlockFace.BOTTOM, LoadOption.NO_LOAD);
		}
		return rval;
	}
}
