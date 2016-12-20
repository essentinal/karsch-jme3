package karsch2.es.component;

import com.jme3.math.Vector3f;

import es.core.component.EComponent;

/**
 * This component indicates that an entity should move and stores the movement
 * target.
 * 
 * @author Stephan Dreyer
 * 
 */
public class MovementTargetComponent implements EComponent {
  private final Vector3f source;
  private final Vector3f target;
  private final float speed;
  private final boolean revert;

  public MovementTargetComponent(final Vector3f source, final Vector3f target,
      final float speed, final boolean revert) {
    this.source = source.clone();
    this.target = target.clone();
    this.speed = speed;
    this.revert = revert;
  }

  public boolean isRevert() {
    return revert;
  }

  public Vector3f getSource() {
    return source;
  }

  public Vector3f getTarget() {
    return target;
  }

  public float getSpeed() {
    return speed;
  }

  public MovementTargetComponent setRevert(final boolean revert) {
    return new MovementTargetComponent(source, target, speed, revert);
  }
}
