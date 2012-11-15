/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, VanillaDev <http://www.spout.org/>
 * Vanilla is licensed under the SpoutDev License Version 1.
 *
 * Vanilla is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the SpoutDev License Version 1.
 *
 * Vanilla is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the SpoutDev License Version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license,
 * including the MIT license.
 */
package org.spout.vanilla.protocol;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.spout.api.protocol.Session;
import org.spout.vanilla.configuration.VanillaConfiguration;

public class ClientLoginAuth implements Runnable {
	private final static String targetURL = "https://login.minecraft.net/";
	
	private final Session session;
	private String urlParams;
	
	public ClientLoginAuth(Session session) {
		this.session = session;
		String username = VanillaConfiguration.USERNAME.getString();
		String password = VanillaConfiguration.PASSWORD.getString();
		try {
			urlParams = new StringBuilder("user=")
							.append(URLEncoder.encode(username, "UTF-8"))
							.append("&password=")
							.append(URLEncoder.encode(password, "UTF-8"))
							.append("&version=").append(12).toString();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		HttpURLConnection conn;
		String result = "";
		try {
			URL url = new URL(targetURL);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", Integer.toString(urlParams.getBytes().length));
            conn.setRequestProperty("Content-Language", "en-US");
            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            DataOutputStream e = new DataOutputStream(conn.getOutputStream());
            e.writeBytes(urlParams);
            e.flush();
            e.close();
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuffer response = new StringBuffer();
            String line;
            while ((line = rd.readLine()) != null) {
                    response.append(line);
                    System.out.println(line);
                    response.append('\r');
            }
            rd.close();
            result = response.toString();

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("Result: "+result);
		
		if (result.contains(":")) {
			String sessionId = result.split(":")[3].trim();
			session.getDataMap().put(VanillaProtocol.SESSION_ID, sessionId);
		}
		
	}

}
