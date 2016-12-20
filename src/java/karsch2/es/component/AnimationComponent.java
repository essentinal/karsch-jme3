package karsch2.es.component;

import karsch2.core.AnimationType;
import es.core.component.EComponent;

/**
 * This component stores/sets the current animation type.
 * 
 * @author Stephan Dreyer
 * 
 */
public class AnimationComponent implements EComponent {
  private final AnimationType type;
  private final boolean isSet;

  public AnimationComponent(final AnimationType type, final boolean isSet) {
    this.type = type;
    this.isSet = isSet;
  }

  public boolean isSet() {
    return isSet;
  }

  public AnimationType getType() {
    return type;
  }

}
