package org.spout.vanilla.components.substance.material;

import org.spout.api.component.components.BlockComponent;
import org.spout.api.material.BlockMaterial;

import org.spout.vanilla.material.block.controlled.MonsterSpawnerBlock;

public class MonsterSpawner extends BlockComponent {
	@Override
	public MonsterSpawnerBlock getMaterial() {
		return (MonsterSpawnerBlock) super.getMaterial();
	}

	@Override
	public void setMaterial(BlockMaterial material) {
		if (!(material instanceof MonsterSpawnerBlock)) {
			throw new IllegalArgumentException("Material passed in must be an instance of a MonsterSpawnerBlock.");
		}
		super.setMaterial(material);
	}
}
