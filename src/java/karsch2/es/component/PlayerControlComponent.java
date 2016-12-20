package karsch2.es.component;

import es.core.component.EComponent;

/**
 * This component indicates that the entity is player controlled and stores the
 * speed.
 * 
 * @author Stephan Dreyer
 * 
 */
public class PlayerControlComponent implements EComponent {
  private final float speed;

  public PlayerControlComponent(final float speed) {
    this.speed = speed;
  }

  public float getSpeed() {
    return speed;
  }
}
