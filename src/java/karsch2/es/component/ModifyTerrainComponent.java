package karsch2.es.component;

import es.core.component.EComponent;

public class ModifyTerrainComponent implements EComponent {
  private final boolean stone;
  private final boolean forest;
  private final boolean water;

  public ModifyTerrainComponent(final boolean stone, final boolean forest,
      final boolean water) {
    this.stone = stone;
    this.forest = forest;
    this.water = water;
  }

  public boolean isStone() {
    return stone;
  }

  public boolean isForest() {
    return forest;
  }

  public boolean isWater() {
    return water;
  }

}
