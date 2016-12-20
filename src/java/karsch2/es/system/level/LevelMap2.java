package karsch2.es.system.level;

import java.util.HashSet;
import java.util.Set;

import es.core.entity.Entity;

public class LevelMap2 {
  private final Set<Entity>[][] levelMap;
  private final int xSize;
  private final int ySize;
  private final int levelNumber;

  public LevelMap2(final int x, final int y, final int levelNumber) {
    this.xSize = x;
    this.ySize = y;
    this.levelNumber = levelNumber;
    levelMap = new Set[x][y];
  }

  public Set<Entity> getEntities(final int x, final int y) {
    final Set<Entity> entities = levelMap[x][y];

    if (entities == null) {
      return new HashSet<Entity>(1);
    }

    return new HashSet<Entity>(entities);
  }

  public int getXSize() {
    return xSize;
  }

  public int getYSize() {
    return ySize;
  }

  public int getLevelNumber() {
    return levelNumber;
  }
}
