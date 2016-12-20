package karsch2.es.component;

import es.core.component.EComponent;

public class CollisionGroupComponent implements EComponent {
  private final String collisionGroupName;

  public CollisionGroupComponent(final String collisionGroupName) {
    this.collisionGroupName = collisionGroupName;
  }

  public String getCollisionGroupName() {
    return collisionGroupName;
  }
}
