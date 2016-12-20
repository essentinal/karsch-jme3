package karsch.level;

import karsch.level.Level.LevelStyle;

public class LevelMap {
  private final Object[][] levelMap;
  private final int xSize, ySize, levelNumber;
  private final LevelStyle levelStyle;

  public LevelMap(final int x, final int y, final LevelStyle levelStyle,
      final int levelNumber) {
    this.xSize = x;
    this.ySize = y;
    this.levelStyle = levelStyle;
    this.levelNumber = levelNumber;
    levelMap = new Object[x][y];
  }

  public Object[][] getLevelMap() {
    return levelMap;
  }

  public LevelStyle getStyle() {
    return levelStyle;
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
