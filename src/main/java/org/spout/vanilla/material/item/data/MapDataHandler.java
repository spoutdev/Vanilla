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

import java.io.IOException;
import org.spout.nbt.ByteArrayTag;
import org.spout.nbt.ByteTag;
import org.spout.nbt.IntTag;
import org.spout.nbt.ShortTag;
import org.spout.nbt.stream.NBTInputStream;
import org.spout.nbt.stream.NBTOutputStream;



public class MapDataHandler extends ExtraDataHandler{

	@Override
	public String getName() {
		return "map";
	}

	@Override
	public MapData load(NBTInputStream stream) {
		try{
		ByteTag scale = (ByteTag) stream.readTag();
		ByteTag dimension = (ByteTag) stream.readTag();
		ShortTag height = (ShortTag) stream.readTag();
		ShortTag width = (ShortTag) stream.readTag();
		IntTag xCenter = (IntTag) stream.readTag();
		IntTag zCenter = (IntTag) stream.readTag();
		ByteArrayTag colors = (ByteArrayTag) stream.readTag();
		MapData data = new MapData(scale.getValue(), dimension.getValue(), height.getValue(), width.getValue(), xCenter.getValue(), zCenter.getValue(), colors.getValue());
		return data;
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return new MapData();
	}

	@Override
	public void save(ExtraData data, NBTOutputStream stream) throws IOException{
		if(!(data instanceof MapData)) {
			throw new UnsupportedOperationException("Invalid data type: "+data.getName()+" for a map!");
		}
		MapData mdata = (MapData) data;
		byte scale = mdata.getScale();
		byte dimension = mdata.getDimension();
		short height = mdata.getHeight();
		short width = mdata.getWidth();
		int xCenter = mdata.getXCenter();
		int yCenter = mdata.getZCenter();
		byte[] colors = mdata.getColors();
		stream.writeTag(new ByteTag("scale", scale));
		stream.writeTag(new ByteTag("dimension", dimension));
		stream.writeTag(new ShortTag("height", height));
		stream.writeTag(new ShortTag("width", width));
		stream.writeTag(new IntTag("xCenter", xCenter));
		stream.writeTag(new IntTag("yCenter", yCenter));
		stream.writeTag(new ByteArrayTag("colors", colors));
		
	}

}
