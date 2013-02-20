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
package org.spout.vanilla.material.item.misc;

import org.spout.api.component.Component;
import org.spout.api.entity.Entity;
import org.spout.api.event.player.PlayerInteractEvent.Action;
import org.spout.api.geo.LoadOption;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.inventory.Slot;
import org.spout.api.material.Material;
import org.spout.api.material.block.BlockFace;

import org.spout.vanilla.component.living.hostile.Blaze;
import org.spout.vanilla.component.living.hostile.CaveSpider;
import org.spout.vanilla.component.living.hostile.Creeper;
import org.spout.vanilla.component.living.hostile.Ghast;
import org.spout.vanilla.component.living.hostile.MagmaCube;
import org.spout.vanilla.component.living.hostile.Silverfish;
import org.spout.vanilla.component.living.hostile.Skeleton;
import org.spout.vanilla.component.living.hostile.Slime;
import org.spout.vanilla.component.living.hostile.Spider;
import org.spout.vanilla.component.living.hostile.Witch;
import org.spout.vanilla.component.living.hostile.Zombie;
import org.spout.vanilla.component.living.neutral.Bat;
import org.spout.vanilla.component.living.neutral.Enderman;
import org.spout.vanilla.component.living.neutral.PigZombie;
import org.spout.vanilla.component.living.neutral.Wolf;
import org.spout.vanilla.component.living.passive.Chicken;
import org.spout.vanilla.component.living.passive.Cow;
import org.spout.vanilla.component.living.passive.MooshroomCow;
import org.spout.vanilla.component.living.passive.Ocelot;
import org.spout.vanilla.component.living.passive.Pig;
import org.spout.vanilla.component.living.passive.Sheep;
import org.spout.vanilla.component.living.passive.Squid;
import org.spout.vanilla.component.living.passive.Villager;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.item.VanillaItemMaterial;
import org.spout.vanilla.util.PlayerUtil;

public class SpawnEgg extends VanillaItemMaterial {
	public static final SpawnEgg PARENT = new SpawnEgg("Spawn Egg"); //There is no entity with the ID 0 so this egg is invalid
	public static final SpawnEgg BAT = new SpawnEgg("Spawn Bat", 65, Bat.class, PARENT);
	public static final SpawnEgg CREEPER = new SpawnEgg("Spawn Creeper", 50, Creeper.class, PARENT);
	public static final SpawnEgg SKELETON = new SpawnEgg("Spawn Skeleton", 51, Skeleton.class, PARENT);
	public static final SpawnEgg SPIDER = new SpawnEgg("Spawn Spider", 52, Spider.class, PARENT);
	public static final SpawnEgg ZOMBIE = new SpawnEgg("Spawn Zombie", 54, Zombie.class, PARENT);
	public static final SpawnEgg SLIME = new SpawnEgg("Spawn Slime", 55, Slime.class, PARENT);
	public static final SpawnEgg GHAST = new SpawnEgg("Spawn Ghast", 56, Ghast.class, PARENT);
	public static final SpawnEgg PIGMAN = new SpawnEgg("Spawn Pigman", 57, PigZombie.class, PARENT);
	public static final SpawnEgg ENDERMAN = new SpawnEgg("Spawn Enderman", 58, Enderman.class, PARENT);
	public static final SpawnEgg CAVESPIDER = new SpawnEgg("Spawn Cavespider", 59, CaveSpider.class, PARENT);
	public static final SpawnEgg SILVERFISH = new SpawnEgg("Spawn Silverfish", 60, Silverfish.class, PARENT);
	public static final SpawnEgg BLAZE = new SpawnEgg("Spawn Blaze", 61, Blaze.class, PARENT);
	public static final SpawnEgg MAGMACUBE = new SpawnEgg("Spawn Magmacube", 62, MagmaCube.class, PARENT);
	public static final SpawnEgg PIG = new SpawnEgg("Spawn Pig", 90, Pig.class, PARENT);
	public static final SpawnEgg SHEEP = new SpawnEgg("Spawn Sheep", 91, Sheep.class, PARENT);
	public static final SpawnEgg COW = new SpawnEgg("Spawn Cow", 92, Cow.class, PARENT);
	public static final SpawnEgg CHICKEN = new SpawnEgg("Spawn Chicken", 93, Chicken.class, PARENT);
	public static final SpawnEgg SQUID = new SpawnEgg("Spawn Squid", 94, Squid.class, PARENT);
	public static final SpawnEgg WOLF = new SpawnEgg("Spawn Wolf", 95, Wolf.class, PARENT);
	public static final SpawnEgg MOOSHROOM = new SpawnEgg("Spawn Mooshroom", 96, MooshroomCow.class, PARENT);
	public static final SpawnEgg WITCH = new SpawnEgg("Spawn Witch", 66, Witch.class, PARENT);
	public static final SpawnEgg VILLAGER = new SpawnEgg("Spawn Villager", 120, Villager.class, PARENT);
	public static final SpawnEgg OCELOT = new SpawnEgg("Spawn Ocelot", 98, Ocelot.class, PARENT);
	private Class<? extends Component> entityComponent;

	private SpawnEgg(String name) {
		super((short) 0x007F, name, 383, null);
	}

	private SpawnEgg(String name, int data, Class<? extends Component> entityComponent, Material parent) {
		super(name, 383, data, parent, null);
		this.entityComponent = entityComponent;
	}

	@Override
	public void onInteract(Entity entity, Block block, Action type, BlockFace clickedface) {
		if (type != Action.RIGHT_CLICK) {
			return;
		}
		Slot slot = PlayerUtil.getHeldSlot(entity);
		if (!PlayerUtil.isCostSuppressed(entity) && slot != null && slot.get() != null && slot.get().getMaterial().isMaterial(VanillaMaterials.SPAWN_EGG)) {
			slot.addAmount(-1);
		}
		block.getWorld().createAndSpawnEntity(block.translate(clickedface).getPosition(), entityComponent, LoadOption.NO_LOAD);
	}
}
