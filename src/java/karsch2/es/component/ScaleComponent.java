package karsch2.es.component;

import com.jme3.math.Vector3f;

import es.core.component.EComponent;

/**
 * This component stores the scale for an entities model.
 * 
 * TODO This is only required once on initialization, maybe no component is
 * needed for that.
 * 
 * @author Stephan Dreyer
 * 
 */
public class ScaleComponent implements EComponent {
  private final Vector3f scale;

  public ScaleComponent(final float scaleX, final float scaleY,
      final float scaleZ) {
    this.scale = new Vector3f(scaleX, scaleY, scaleZ);
  }

  public Vector3f getScale() {
    return scale;
  }
}
