package org.spout.vanilla.protocol;

import org.spout.api.entity.Controller;
import org.spout.api.entity.Entity;
import org.spout.api.geo.discrete.atomic.Transform;
import org.spout.api.protocol.EntityProtocol;
import org.spout.api.protocol.Message;
import org.spout.vanilla.entity.living.player.MinecraftPlayer;
import org.spout.vanilla.entity.objects.FallingBlock;
import org.spout.vanilla.protocol.msg.DestroyEntityMessage;
import org.spout.vanilla.protocol.msg.EntityTeleportMessage;
import org.spout.vanilla.protocol.msg.SpawnPlayerMessage;

public class VanillaEntityProtocol implements EntityProtocol {

	@Override
	public Message getSpawnMessage(Entity entity) {
		Controller c = entity.getLiveController();
		//TODO: this if-else structure is terrible and not OO. Fix!
		if (c instanceof MinecraftPlayer) {
			MinecraftPlayer mcp = (MinecraftPlayer)c;
			int id = entity.getId();
			String name = mcp.getPlayer().getName();
			Transform t = entity.getTransform();
			int x = (int)(t.getPosition().getX() * 32);
			int y = (int)(t.getPosition().getY() * 32);
			int z = (int)(t.getPosition().getZ() * 32);
			int r = 0;
			int p = 0;
			int item = 0;
			return new SpawnPlayerMessage(id, name, x, y, z, r, p, item);
		} 
		else if (c instanceof FallingBlock) {
			return ((FallingBlock)c).getSpawnMessage();
		}
		else {
			return null;
		}
	}

	@Override
	public Message getDestroyMessage(Entity entity) {
		return new DestroyEntityMessage(entity.getId());
	}

	@Override
	public Message getUpdateMessage(Entity entity) {
		Transform t = entity.getLiveTransform();
		int id = entity.getId();
		int x = (int)(t.getPosition().getX() * 32);
		int y = (int)(t.getPosition().getY() * 32);
		int z = (int)(t.getPosition().getZ() * 32);
		int r = 0;
		int p = 0;
		// TODO - improve efficiency
		return new EntityTeleportMessage(id, x, y, z, r, p);
	}

}
