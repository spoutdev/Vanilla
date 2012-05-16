/*
 * This file is part of Vanilla.
 *
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
 * the MIT license and the SpoutDev license version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license,
 * including the MIT license.
 */
package org.spout.vanilla.material.item.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.spout.api.inventory.ItemStack;
import org.spout.nbt.IntTag;
import org.spout.nbt.StringTag;
import org.spout.nbt.stream.NBTInputStream;
import org.spout.nbt.stream.NBTOutputStream;
import org.spout.vanilla.VanillaPlugin;

public class ExtraDataFactory {

	private static boolean enabled = false;
	private static int ids = 1;
	private static Map<Integer, ExtraData> datas = new HashMap<Integer, ExtraData>();
	private static Map<String, ExtraDataHandler> handlers = new HashMap<String, ExtraDataHandler>();

	public static void enable(VanillaPlugin instance) throws IOException {
		if (enabled) {
			return;
		}
		File totalIds = new File(instance.getDataFolder() + File.separator + "data", "total.dat");
		if(totalIds.exists()) {
			NBTInputStream nstream = new NBTInputStream(new FileInputStream(totalIds));
			ids = ((IntTag) nstream.readTag()).getValue();
			for(int i=1;i<=ids;i++) {
				File dataFile = new File(instance.getDataFolder() + File.separator + "data", "d"+i+".dat");
				if(!dataFile.exists()) {
					continue;
				}
				NBTInputStream nsstream = new NBTInputStream(new FileInputStream(dataFile));
				StringTag handlerName = (StringTag) nsstream.readTag();
				if(!handlers.containsKey(handlerName.getValue())) {
					System.out.println("Could not load item data, invalid handler: "+handlerName.getValue()+"!");
					continue;
				}
				ExtraData loaded = handlers.get(handlerName.getValue()).load(nstream);
				datas.put(i, loaded);
			}
		}
		enabled = true;

	}

	public static void disable(VanillaPlugin instance) throws IOException {
		File dataFolder = new File(instance.getDataFolder(), "data");
		dataFolder.mkdirs();
		for (Integer integer : datas.keySet()) {
			File file = new File(dataFolder, "d" + integer + ".dat");
			if (!file.exists()) {
				file.createNewFile();
			}
			NBTOutputStream nstream = new NBTOutputStream(new FileOutputStream(file));
			try {
				ExtraData data = datas.get(integer);
				nstream.writeTag(new StringTag("handler", data.getName()));
				handlers.get(data.getName()).save(data, nstream);
			} catch (Exception ex) {
				System.out.println("Could not save item data!");
				ex.printStackTrace();
			}
			nstream.close();
		}
		File idFile = new File(dataFolder, "total.dat");
		if (!idFile.exists()) {
			idFile.createNewFile();
		}
		NBTOutputStream nstream = new NBTOutputStream(new FileOutputStream(idFile));
		nstream.writeTag(new IntTag("total", datas.size()));
		nstream.close();
		enabled = false;
	}

	public static int getID() {
		return ++ids;
	}

	public static ExtraData getExtraData(ItemStack itemStack) {
		if (itemStack.getAuxData() == null) {
			return null;
		}
		Integer id = (Integer) itemStack.getAuxData().get("ExtraDataID").getValue();
		return datas.get(id);
	}

	public static void registerHandler(ExtraDataHandler handler) {
		handlers.put(handler.getName(), handler);
	}

	public static void unregisterHandler(String name) {
		handlers.remove(name);
	}
}
