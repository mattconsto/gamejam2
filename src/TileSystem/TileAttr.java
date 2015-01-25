package TileSystem;

public enum TileAttr {
	NONE, HUT, BOAT, CORPSE, SKELETON, PLANE, FIRE, RELIGIOUS_ARTIFACT, ALIEN_ARTIFACT, TREE, PINE_TREE, PALM_TREE, SHRUB, CAVE, HUTONFIRE,POND;
	
	public String toLetter() {
		if(this != NONE) {
			return this.name().substring(0, 1);
		}
		return "";
	}
}
