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
package org.spout.vanilla.protocol;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.spout.api.Spout;
import org.spout.vanilla.VanillaPlugin;

/**
 * http://wiki.vg/Session#Login
 */
public class ClientAuthentification implements Runnable {
	private static final String URL = "https://login.minecraft.net/";
	private String urlParameters;
	
	public ClientAuthentification(String username, String password) {
		try {
			urlParameters = (new StringBuilder("user="))
							.append(URLEncoder.encode(username, "UTF-8"))
							.append("&password=").append(URLEncoder.encode(password, "UTF-8"))
							.append("&version=").append(12).toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		HttpURLConnection connection = null;
		String response = "";
		try {
			URL url = new URL(URL);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Length", Integer.toString(urlParameters.getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");
            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            
            DataOutputStream e = new DataOutputStream(connection.getOutputStream());
            e.writeBytes(urlParameters);
            e.flush();
            e.close();
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuffer responseBuffer = new StringBuffer();
            String line;
            while((line = rd.readLine()) != null) {
                    responseBuffer.append(line);
                    responseBuffer.append('\r');
            }
            rd.close();
            response = responseBuffer.toString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (connection!=null) {
				connection.disconnect();
			}
		}
		
		if (Spout.getEngine().debugMode()) {
			Spout.getLogger().info("Authentification: " + response);
		}
		if (response.contains(":")) {
			String[] infos = response.split(":");
			VanillaPlugin.getInstance().setClientAuthInfos(infos[2], infos[3]);
		} else {
			Spout.getLogger().info("Authentification failed: "+response);
		}
	}
}
