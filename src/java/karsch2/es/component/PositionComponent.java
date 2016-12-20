package karsch2.es.component;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

import es.core.component.EComponent;

/**
 * This component stores the location and rotation of an entity.
 * 
 * TODO maybe merge with {@link ScaleComponent}
 * 
 * @author Stephan Dreyer
 * 
 */
public final class PositionComponent implements EComponent {

  private final Vector3f location;
  private final Quaternion rotation;

  public PositionComponent(final Vector3f location, final Quaternion rotation) {
    this.location = location.clone();
    this.rotation = rotation.clone();
  }

  public Vector3f getLocation() {
    return location;
  }

  public Quaternion getRotation() {
    return rotation;
  }
}