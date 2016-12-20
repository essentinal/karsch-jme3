package karsch2.es.component;

import com.jme3.math.Vector3f;

import es.core.component.EComponent;

public class MarkerComponent implements EComponent {
  private final Vector3f interactionLocation;

  public MarkerComponent(final Vector3f interactionLocation) {
    this.interactionLocation = interactionLocation.clone();
  }

  public Vector3f getInteractionLocation() {
    return interactionLocation;
  }
}
