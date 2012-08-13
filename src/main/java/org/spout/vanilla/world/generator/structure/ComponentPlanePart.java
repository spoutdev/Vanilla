package org.spout.vanilla.world.generator.structure;

public class ComponentPlanePart extends ComponentCuboidPart {

	public ComponentPlanePart(StructureComponent parent) {
		super(parent);
	}

	@Override
	protected boolean isOuter(int xx, int yy, int zz) {
		if (min.getX() == max.getX()) {
			return yy == min.getY() || zz == min.getZ() || yy == max.getY() || zz == max.getZ();
		} else if (min.getY() == max.getY()) {
			return xx == min.getX() || zz == min.getZ() || xx == max.getX() || zz == max.getZ();
		} else if (min.getZ() == max.getZ()) {
			return yy == min.getY() || xx == min.getX() || yy == max.getY() || xx == max.getX();
		} else {
			return super.isOuter(xx, yy, zz);
		}
	}
}
