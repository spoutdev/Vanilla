package org.spout.vanilla.material.item;

import org.spout.api.Source;
import org.spout.api.entity.Entity;
import org.spout.api.event.player.PlayerInteractEvent.Action;
import org.spout.api.geo.World;
import org.spout.api.geo.discrete.Point;
import org.spout.api.material.block.BlockFace;
import org.spout.vanilla.material.block.Solid;
import org.spout.vanilla.material.generic.GenericItem;

public class DoorItem extends GenericItem {

	private DoorBlock doorBlock;

	public DoorItem(String name, int id, DoorBlock woodenDoorBlock) {
		super(name, id);
		this.doorBlock = woodenDoorBlock;
	}

	@Override
	public void onInteract(Entity entity, Point position, Action type, BlockFace clickedFace) {
		super.onInteract(entity, position, type, clickedFace);
		if(clickedFace != BlockFace.TOP) {
			return;
		}
		World world = position.getWorld();
		int x = (int) position.getX();
		int y = (int) position.getY();
		int z = (int) position.getZ();
		if(world.getBlockId(x, y, z) == 0 && world.getBlockId(x, y+1, z) == 0 && world.getBlockMaterial(x, y-1, z) instanceof Solid) {
			System.out.println("Placing door");
		}
		/*
		 * Formula kinda copied from minecraft source
		 */
		short hinge = (short) (((short) Math.floor((double) ((entity.getYaw() + 180F) * 4F) - 0.5D)) & 3);
		placeDoorBlock(world, x, y, z, hinge, doorBlock, entity);
	}

	public static void placeDoorBlock(World world, int x, int y, int z, short hinge, DoorBlock doorBlock, Source source) {
		world.setBlockIdAndData(x, y, z, doorBlock.getId(), hinge, false, source);
		world.setBlockIdAndData(x, y+1, z, doorBlock.getId(), (short) (hinge | 0x8), false, source);
	}

}
