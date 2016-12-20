package karsch2.es.component;

import es.core.component.EComponent;

public class LevelLinkComponent implements EComponent {
  private final int levelNumber;

  public LevelLinkComponent(final int levelNumber) {
    this.levelNumber = levelNumber;
  }

  public int getLevelNumber() {
    return levelNumber;
  }
}
