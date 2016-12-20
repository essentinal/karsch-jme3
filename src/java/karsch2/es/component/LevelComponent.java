package karsch2.es.component;

import karsch.level.Level.LevelStyle;
import es.core.component.EComponent;

/**
 * This component stores the level number, level scale and enabled flag.
 * 
 * @author Stephan Dreyer
 * 
 */
public class LevelComponent implements EComponent {
  private final int levelNumber;
  private final boolean enabled;
  private final LevelStyle style;
  private final float levelToWorldScale;

  public LevelComponent(final LevelStyle style, final int levelNumber,
      final float levelToWorldScale, final boolean enabled) {
    this.style = style;
    this.levelNumber = levelNumber;
    this.levelToWorldScale = levelToWorldScale;
    this.enabled = enabled;
  }

  public LevelStyle getStyle() {
    return style;
  }

  public int getLevelNumber() {
    return levelNumber;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public float getLevelToWorldScale() {
    return levelToWorldScale;
  }
}
