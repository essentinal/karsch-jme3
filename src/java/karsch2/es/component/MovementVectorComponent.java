package karsch2.es.component;

import com.jme3.math.Vector3f;

import es.core.component.EComponent;

/**
 * This component indicates that an entity should move and stores the movement
 * vector.
 * 
 * @author Stephan Dreyer
 * 
 */
public class MovementVectorComponent implements EComponent {
  private final Vector3f movement;

  // TODO not supported for now
  private MovementVectorComponent(final Vector3f movement) {
    this.movement = movement.clone();
  }

  public Vector3f getMovement() {
    return movement;
  }
}
