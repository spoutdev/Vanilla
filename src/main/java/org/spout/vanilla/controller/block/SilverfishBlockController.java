package org.spout.vanilla.controller.block;

import org.spout.api.entity.Controller;
import org.spout.api.entity.type.ControllerType;
import org.spout.api.material.BlockMaterial;

import org.spout.vanilla.controller.VanillaBlockController;
import org.spout.vanilla.controller.VanillaController;
import org.spout.vanilla.controller.VanillaControllerType;
import org.spout.vanilla.controller.VanillaControllerTypes;
import org.spout.vanilla.material.VanillaMaterials;

public class SilverfishBlockController extends VanillaBlockController {
	public SilverfishBlockController() {
		super(VanillaControllerTypes.SILVERFISH, VanillaMaterials.SILVERFISH_STONE);
	}

	@Override
	public void onAttached() {

	}

	@Override
	public void onTick(float dt) {

	}
}
