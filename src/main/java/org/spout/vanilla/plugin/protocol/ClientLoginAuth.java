package org.spout.vanilla.plugin.protocol;

import java.net.HttpURLConnection;
import java.net.URL;

import org.spout.api.protocol.Session;
import org.spout.vanilla.plugin.VanillaPlugin;

public class ClientLoginAuth implements Runnable {
	private static final String baseURL = "http://session.minecraft.net/game/joinserver.jsp?";
	private final Session session;
	private final String params;
	
	public ClientLoginAuth(Session session, String hash) {
		this.session = session;
		VanillaPlugin p = VanillaPlugin.getInstance();
		this.params = "user="+p.getUsername()+"&sessionId="+p.getSessionId()+"&serverId="+hash;
	}
	
	@Override
	public void run() {
		HttpURLConnection connection = null;
		try {
			URL url = new URL(baseURL+params);
			connection = (HttpURLConnection) url.openConnection();
			connection.setConnectTimeout(30000);
			connection.setReadTimeout(30000);
			//TODO: falling asleep, i'll continue these later if someone haven't done it
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (connection!=null) {
				connection.disconnect();
			}
		}
	}
}
