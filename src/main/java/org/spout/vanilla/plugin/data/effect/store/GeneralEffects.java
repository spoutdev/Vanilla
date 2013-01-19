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
package org.spout.vanilla.plugin.data.effect.store;

import org.spout.vanilla.api.data.effect.Effect;

import org.spout.vanilla.plugin.data.effect.BatchEffect;
import org.spout.vanilla.plugin.data.effect.GeneralEffect;
import org.spout.vanilla.plugin.data.effect.SoundEffect;
import org.spout.vanilla.plugin.data.effect.type.BatchExplosionEffect;
import org.spout.vanilla.plugin.data.effect.type.BreakBlockEffect;
import org.spout.vanilla.plugin.data.effect.type.ExplosionEffect;
import org.spout.vanilla.plugin.data.effect.type.LavaFizzEffect;
import org.spout.vanilla.plugin.data.effect.type.MusicDiscEffect;
import org.spout.vanilla.plugin.data.effect.type.NoteParticleEffect;
import org.spout.vanilla.plugin.data.effect.type.PressBlockEffect;
import org.spout.vanilla.plugin.data.effect.type.SmokeEffect;
import org.spout.vanilla.plugin.data.effect.type.ToggleSoundEffect;

public class GeneralEffects {
	public static final ExplosionEffect EXPLOSION_PARTICLES = new ExplosionEffect();
	public static final PressBlockEffect BLOCK_PRESS = new PressBlockEffect();
	public static final SoundEffect RANDOM_CLICK1 = new SoundEffect(SoundEffects.RANDOM_CLICK, 1.0f, 1.0f); //1000
	public static final SoundEffect RANDOM_CLICK2 = new SoundEffect(SoundEffects.RANDOM_CLICK, 1.0f, 1.2f); //1001
	public static final SoundEffect RANDOM_BOW = new SoundEffect(SoundEffects.RANDOM_BOW, 1.0F, 1.2F); //1002
	public static final SoundEffect TRIPWIRE_SNAP = new SoundEffect(SoundEffects.RANDOM_BOWHIT, 0.4f, 1.09f).randomPitch(0.24f);
	public static final GeneralEffect RANDOM_DOOR = new GeneralEffect(1003);
	public static final GeneralEffect RANDOM_FIZZ = new GeneralEffect(1004);
	public static final MusicDiscEffect MUSIC_DISC = new MusicDiscEffect(1005);
	public static final GeneralEffect GHAST_CHARGE = new GeneralEffect(1007);
	public static final GeneralEffect GHAST_FIREBALL = new GeneralEffect(1008);
	public static final GeneralEffect SHOOT_FIREBALL = new GeneralEffect(1009);
	public static final GeneralEffect ZOMBIE_DAMAGE_WOOD = new GeneralEffect(1010);
	public static final GeneralEffect ZOMBIE_DAMAGE_METAL = new GeneralEffect(1011);
	public static final GeneralEffect ZOMBIE_BREAK = new GeneralEffect(1012);
	public static final SmokeEffect SMOKE = new SmokeEffect(2000);
	public static final BreakBlockEffect BREAKBLOCK = new BreakBlockEffect(2001);
	public static final GeneralEffect SPLASHPOTION = new GeneralEffect(2002);
	public static final GeneralEffect ENDEREYE = new GeneralEffect(2003);
	public static final GeneralEffect MOBSPAWN = new GeneralEffect(2004);
	public static final GeneralEffect NOTE_PARTICLE = new NoteParticleEffect();
	public static final LavaFizzEffect LAVA_FIZZ = new LavaFizzEffect();
	public static final BatchExplosionEffect EXPLOSION = new BatchExplosionEffect(SoundEffects.RANDOM_EXPLODE);
	public static final BatchEffect EXTINGUISH = new BatchEffect(SoundEffects.RANDOM_FIZZ, GeneralEffects.SMOKE);
	public static final ToggleSoundEffect DOOR = new ToggleSoundEffect(SoundEffects.RANDOM_DOOR_OPEN, SoundEffects.RANDOM_DOOR_CLOSE);
	public static final ToggleSoundEffect CHEST = new ToggleSoundEffect(SoundEffects.RANDOM_CHESTOPEN, SoundEffects.RANDOM_CHESTCLOSED).adjust(0.5f, 0.9f).randomPitch(0.1f);
	// Lightning sound effects
	public static final SoundEffect LIGHTNING_THUNDER = SoundEffects.AMBIENT_WEATHER_THUNDER.adjust(10000.0f, 0.7f).randomPitch(0.4f);
	public static final SoundEffect LIGHTNING_EXPLODE = SoundEffects.RANDOM_EXPLODE.adjust(2.0f, 0.4f).randomPitch(0.4f);
	public static final Effect LIGHTNING_STRIKE = new BatchEffect(LIGHTNING_THUNDER, LIGHTNING_EXPLODE);
}
