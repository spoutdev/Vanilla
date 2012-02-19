package org.spout.vanilla.entity.living.player;

import java.util.Collections;

import org.spout.api.entity.Controller;
import org.spout.api.entity.Entity;
import org.spout.api.protocol.EntityProtocol;
import org.spout.api.protocol.Message;
import org.spout.vanilla.entity.living.passive.Sheep;
import org.spout.vanilla.protocol.msg.SpawnMobMessage;

public class SheepEntityProtocol extends BasicEntityProtocol implements EntityProtocol {

	@SuppressWarnings("unchecked")
	@Override
	public Message getSpawnMessage(Entity entity) {
		Controller c = entity.getController();
		if (c == null) {
			return null;
		}
		int id = entity.getId();
		int x = (int) (entity.getX() * 32);
		int y = (int) (entity.getY() * 32);
		int z = (int) (entity.getZ() * 32);
		int r = (int) (entity.getYaw() * 32);
		int p = (int) (entity.getPitch() * 32);
		if (c instanceof Sheep) {
			return new SpawnMobMessage(id, 91, x, y, z, r, p, Collections.EMPTY_LIST);
		}
		
		return null;
	}

}
