/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011 Spout LLC <http://www.spout.org/>
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
package org.spout.vanilla.world.generator.structure;

import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import gnu.trove.iterator.TObjectIntIterator;
import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TObjectIntHashMap;

import org.spout.vanilla.util.MathHelper;

public abstract class WeightedNextStructurePiece extends StructurePiece {
	private final TObjectIntMap<Constructor<? extends StructurePiece>> weightedPieces =
			new TObjectIntHashMap<Constructor<? extends StructurePiece>>();

	public WeightedNextStructurePiece(Structure parent) {
		super(parent);
	}

	public WeightedNextStructurePiece(Structure parent, WeightedNextPieceCache defaults) {
		this(parent);
		addNextPieces(defaults);
	}

	public void addNextPieces(Map<Class<? extends StructurePiece>, Integer> weightedPieces) {
		for (Entry<Class<? extends StructurePiece>, Integer> weightedPiece : weightedPieces.entrySet()) {
			addNextPiece(weightedPiece.getKey(), weightedPiece.getValue());
		}
	}

	public void addNextPieces(TObjectIntMap<Class<? extends StructurePiece>> weightedPieces) {
		final TObjectIntIterator<Class<? extends StructurePiece>> iterator = weightedPieces.iterator();
		while (iterator.hasNext()) {
			iterator.advance();
			addNextPiece(iterator.key(), iterator.value());
		}
	}

	public final void addNextPieces(WeightedNextPieceCache cache) {
		this.weightedPieces.putAll(cache.getContents());
	}

	public void addNextPiece(Class<? extends StructurePiece> piece, int weight) {
		try {
			weightedPieces.put(piece.getConstructor(Structure.class), weight);
		} catch (Exception ex) {
			throw new IllegalArgumentException("Couldn't get the parent constructor: " + ex.getMessage());
		}
	}

	public boolean hasNextPiece(Class<? extends StructurePiece> piece) {
		for (Constructor<? extends StructurePiece> weightedPiece : weightedPieces.keySet()) {
			if (weightedPiece.getDeclaringClass().equals(piece)) {
				return true;
			}
		}
		return false;
	}

	public void removeNextPieces(Collection<Class<? extends StructurePiece>> pieces) {
		for (Class<? extends StructurePiece> piece : pieces) {
			removeNextPiece(piece);
		}
	}

	public void removeNextPiece(Class<? extends StructurePiece> piece) {
		final TObjectIntIterator<Constructor<? extends StructurePiece>> iterator = weightedPieces.iterator();
		while (iterator.hasNext()) {
			iterator.advance();
			if (iterator.key().getDeclaringClass().equals(piece)) {
				iterator.remove();
				return;
			}
		}
	}

	public int getNextPieceWeight(Class<? extends StructurePiece> piece) {
		final TObjectIntIterator<Constructor<? extends StructurePiece>> iterator = weightedPieces.iterator();
		while (iterator.hasNext()) {
			iterator.advance();
			if (iterator.key().getDeclaringClass().equals(piece)) {
				return iterator.value();
			}
		}
		return -1;
	}

	public void setNextPieceWeight(Class<? extends StructurePiece> piece, int weight) {
		final TObjectIntIterator<Constructor<? extends StructurePiece>> iterator = weightedPieces.iterator();
		while (iterator.hasNext()) {
			iterator.advance();
			if (iterator.key().getDeclaringClass().equals(piece)) {
				iterator.setValue(weight);
				return;
			}
		}
	}

	protected StructurePiece getNextPiece() {
		try {
			return MathHelper.chooseWeightedRandom(getRandom(), weightedPieces).newInstance(parent);
		} catch (Exception ex) {
			throw new IllegalStateException("Failed to create new instance of a piece: " + ex.getMessage());
		}
	}

	public static class WeightedNextPieceCache {
		private final TObjectIntMap<Constructor<? extends StructurePiece>> weightedPieces =
				new TObjectIntHashMap<Constructor<? extends StructurePiece>>();

		private TObjectIntMap<Constructor<? extends StructurePiece>> getContents() {
			return weightedPieces;
		}

		public WeightedNextPieceCache addAll(TObjectIntMap<Class<? extends StructurePiece>> weightedPieces) {
			final TObjectIntIterator<Class<? extends StructurePiece>> iterator = weightedPieces.iterator();
			while (iterator.hasNext()) {
				iterator.advance();
				add(iterator.key(), iterator.value());
			}
			return this;
		}

		public WeightedNextPieceCache add(Class<? extends StructurePiece> piece, int weight) {
			try {
				weightedPieces.put(piece.getConstructor(Structure.class), weight);
			} catch (Exception ex) {
				throw new IllegalArgumentException("Couldn't get the parent constructor: " + ex.getMessage());
			}
			return this;
		}

		public WeightedNextPieceCache clear() {
			weightedPieces.clear();
			return this;
		}
	}
}
