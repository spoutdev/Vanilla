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
package org.spout.vanilla.material.block.solid;

import org.spout.vanilla.data.Instrument;
import org.spout.vanilla.data.drops.SwitchDrops;
import org.spout.vanilla.data.effect.store.SoundEffects;
import org.spout.vanilla.data.tool.ToolType;
import org.spout.vanilla.material.InitializableMaterial;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.SolidMoving;

public class Gravel extends SolidMoving implements InitializableMaterial {
	public Gravel(String name, int id) {
		super(name, id);
		this.setHardness(0.6F).setResistance(1.0F).setStepSound(SoundEffects.STEP_GRAVEL);
		this.addMiningType(ToolType.SPADE);
	}

	@Override
	public void initialize() {
		SwitchDrops drops = getDrops().DEFAULT.clear().addSwitch().setChance(0.1);
		drops.TRUE.add(VanillaMaterials.FLINT);
		drops.FALSE.add(this);
	}

	@Override
	public Instrument getInstrument() {
		return Instrument.SNARE_DRUM;
	}
}
