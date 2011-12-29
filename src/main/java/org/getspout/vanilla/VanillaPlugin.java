package org.getspout.vanilla;

import org.getspout.api.plugin.CommonPlugin;
import org.getspout.api.protocol.Protocol;
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
		// TODO Auto-generated method stub
		
	}

}
