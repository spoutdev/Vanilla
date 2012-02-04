/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.spout.vanilla.item;

import org.spout.api.material.GenericItemMaterial;
import org.spout.api.material.Material;
import org.spout.vanilla.material.VanillaItemMaterial;

/**
 *
 * @author ZNickq
 */
public class GenericVanillaItemMaterial extends GenericItemMaterial implements VanillaItemMaterial {

	public GenericVanillaItemMaterial(String name, int id) {
		super(name, id);
	}

	public GenericVanillaItemMaterial(String name, int id, int data, boolean bool) {
		super(name, id, data, bool);
	}

	public GenericVanillaItemMaterial(String name, int id, int data) {
		super(name, id, data);
	}

	@Override
	public Material getBlock() {
		return null;
	}
}
