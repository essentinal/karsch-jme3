package karsch2.es.component;

import es.core.component.EComponent;

/**
 * This component requests to set a new level.
 * 
 * @author Stephan Dreyer
 * 
 */
public class ChangeLevelComponent implements EComponent {
  private int newLevel;
  private boolean nextLevel;
  private boolean prevLevel;

  public ChangeLevelComponent(final int newLevel) {
    this.newLevel = newLevel;
  }

  public ChangeLevelComponent(final boolean nextLevel, final boolean prevLevel) {
    this.nextLevel = nextLevel;
    this.prevLevel = prevLevel;
  }

  public boolean isNextLevel() {
    return nextLevel;
  }

  public boolean isPrevLevel() {
    return prevLevel;
  }

  public int getNewLevel() {
    return newLevel;
  }
}
