package karsch2.es.system;

import karsch2.es.component.LevelAssignmentComponent;
import karsch2.es.component.LevelComponent;
import karsch2.es.component.PositionComponent;
import karsch2.es.component.VisibleComponent;
import karsch2.es.component.VisualRepComponent;

import com.jme3.app.Application;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;

import es.core.entity.Entity;
import es.core.entity.EntityManager;
import es.core.system.ESystem;

@SuppressWarnings("unchecked")
public class VisibilitySystem implements ESystem {
  private final Application app;
  private final float distance;

  public VisibilitySystem(final Application app, final float distance) {
    this.app = app;
    this.distance = distance;
  }

  @Override
  public void process(final float tpf) {
    for (final Entity eLevel : EntityManager.getInstance().getEntities(
        LevelComponent.class)) {
      final LevelComponent levelComp = eLevel
          .getComponent(LevelComponent.class);
      if (levelComp.isEnabled()) {

        final int levelNumber = levelComp.getLevelNumber();
        for (final Entity e : EntityManager.getInstance().getEntities(
            VisualRepComponent.class, VisibleComponent.class)) {

          final LevelAssignmentComponent levelAssignComp = e
              .getComponent(LevelAssignmentComponent.class);

          if (levelAssignComp != null
              && levelAssignComp.getLevelNumber() == levelNumber) {
            updateVisibility(e);
          }
        }
      }
    }
  }

  private void updateVisibility(final Entity entity) {

    final PositionComponent position = entity
        .getComponent(PositionComponent.class);
    final VisibleComponent visible = entity
        .getComponent(VisibleComponent.class);
    if (position != null) {
      final boolean isInCameraRange = isInCameraRange(position.getLocation());
      if (visible != null && !isInCameraRange) {
        entity.removeComponent(VisibleComponent.class);
      } else if (visible == null && isInCameraRange) {
        entity.addComponent(new VisibleComponent());
      }
    }
  }

  private boolean isInCameraRange(final Vector3f location) {
    // TODO this may fail because it is not executed on the render thread
    return FastMath.abs(app.getCamera().distanceToNearPlane(location)) < distance;
  }
}
