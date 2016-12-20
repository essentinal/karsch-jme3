package karsch2.es.component;

import es.core.component.EComponent;

/**
 * This component stores the level number the entity belongs to.
 * 
 * @author Stephan Dreyer
 * 
 */
public class LevelAssignmentComponent implements EComponent {
  private final int levelNumber;

  public LevelAssignmentComponent(final int levelNumber) {
    this.levelNumber = levelNumber;
  }

  public int getLevelNumber() {
    return levelNumber;
  }

}
