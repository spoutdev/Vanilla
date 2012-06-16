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
package org.spout.vanilla.data.effect;

import org.spout.vanilla.data.effect.type.Blindness;
import org.spout.vanilla.data.effect.type.FireResistance;
import org.spout.vanilla.data.effect.type.Haste;
import org.spout.vanilla.data.effect.type.Hunger;
import org.spout.vanilla.data.effect.type.InstantDamage;
import org.spout.vanilla.data.effect.type.InstantHeal;
import org.spout.vanilla.data.effect.type.Invisibility;
import org.spout.vanilla.data.effect.type.JumpBoost;
import org.spout.vanilla.data.effect.type.MiningFatigue;
import org.spout.vanilla.data.effect.type.Nausea;
import org.spout.vanilla.data.effect.type.NightVision;
import org.spout.vanilla.data.effect.type.Poison;
import org.spout.vanilla.data.effect.type.Regeneration;
import org.spout.vanilla.data.effect.type.Resistance;
import org.spout.vanilla.data.effect.type.Slowness;
import org.spout.vanilla.data.effect.type.Speed;
import org.spout.vanilla.data.effect.type.Strength;
import org.spout.vanilla.data.effect.type.WaterBreathing;
import org.spout.vanilla.data.effect.type.Weakness;

public class EffectTypes {
	public static final Speed SPEED = new Speed();
	public static final Slowness SLOWNESS = new Slowness();
	public static final Haste HASTE = new Haste();
	public static final MiningFatigue MINING_FATIGUE = new MiningFatigue();
	public static final Strength STRENGTH = new Strength();
	public static final InstantHeal INSTANT_HEAL = new InstantHeal();
	public static final InstantDamage INSTANT_DAMAGE = new InstantDamage();
	public static final JumpBoost JUMP_BOOST = new JumpBoost();
	public static final Nausea NAUSEA = new Nausea();
	public static final Regeneration REGENERATION = new Regeneration();
	public static final Resistance RESISTANCE = new Resistance();
	public static final FireResistance FIRE_RESISTANCE = new FireResistance();
	public static final WaterBreathing WATER_BREATHING = new WaterBreathing();
	public static final Invisibility INVISIBILITY = new Invisibility();
	public static final Blindness BLINDNESS = new Blindness();
	public static final NightVision NIGHT_VISION = new NightVision();
	public static final Hunger HUNGER = new Hunger();
	public static final Weakness WEAKNESS = new Weakness();
	public static final Poison POISON = new Poison();
}
