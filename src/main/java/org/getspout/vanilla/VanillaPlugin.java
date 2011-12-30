package org.getspout.vanilla;

import org.getspout.api.Spout;
import org.getspout.api.geo.World;
import org.getspout.api.geo.discrete.Point;
import org.getspout.api.plugin.CommonPlugin;
import org.getspout.api.protocol.Protocol;
import org.getspout.vanilla.entity.sky.NetherSky;
import org.getspout.vanilla.entity.sky.NormalSky;
import org.getspout.vanilla.entity.sky.TheEndSky;
import org.getspout.vanilla.generator.nether.NetherGenerator;
import org.getspout.vanilla.generator.normal.NormalGenerator;
import org.getspout.vanilla.generator.theend.TheEndGenerator;
import org.getspout.vanilla.protocol.VanillaProtocol;

public class VanillaPlugin extends CommonPlugin {

	@Override
	public void onLoad() {
		// TODO - do we need a protocol manager ?
		// getGame().getProtocolManager().register ...
		Protocol.registerProtocol(0, new VanillaProtocol());
		
		//getGame().getEventManager().registerEvent(ChunkObservableEvent.class, Order.MONITOR, executor, owner)
		
		getLogger().info("Vanilla loaded");
	}

	@Override
	public void onDisable() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEnable() {
		World normal = Spout.getGame().loadWorld("world", new NormalGenerator());
		normal.createAndSpawnEntity(new Point(normal,0.f,0.f, 0.f), new NormalSky());
		
		World nether = Spout.getGame().loadWorld("world_nether", new NetherGenerator());
		nether.createAndSpawnEntity(new Point(nether, 0.f, 0.f, 0.f), new NetherSky());
		
		World end = Spout.getGame().loadWorld("world_end", new TheEndGenerator());
		end.createAndSpawnEntity(new Point(end, 0.f, 0.f, 0.f), new TheEndSky());
		
		
	}

}
