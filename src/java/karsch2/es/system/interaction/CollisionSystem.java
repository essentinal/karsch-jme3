package karsch2.es.system.interaction;

import java.awt.Point;
import java.util.logging.Level;
import java.util.logging.Logger;

import karsch2.es.component.CollisionGroupComponent;
import karsch2.es.component.ItemNameComponent;
import karsch2.es.component.MovementTargetComponent;
import karsch2.es.component.PositionComponent;
import karsch2.es.component.VisibleComponent;
import karsch2.utils.LevelUtil;
import es.core.entity.Entity;
import es.core.entity.EntityManager;
import es.core.system.ESystem;

@SuppressWarnings("unchecked")
public class CollisionSystem implements ESystem {
  private static final Logger LOGGER = Logger.getLogger(CollisionSystem.class
      .getName());

  private float levelScale;

  @Override
  public void process(final float tpf) {
    levelScale = LevelUtil.getLevelScale();

    for (final Entity e : EntityManager.getInstance().getEntities(
        MovementTargetComponent.class)) {

      if (e.hasComponent(VisibleComponent.class)) {
        checkCollisionWithOther(e);
      }
    }
  }

  private void checkCollisionWithOther(final Entity eMoving) {
    final CollisionGroupComponent collision = eMoving
        .getComponent(CollisionGroupComponent.class);

    final PositionComponent positionMoving = eMoving
        .getComponent(PositionComponent.class);

    if (collision != null && positionMoving != null) {

      for (final Entity eOther : EntityManager.getInstance().getEntities(
          CollisionGroupComponent.class)) {
        // self collision is not permitted
        if (!eMoving.equals(eOther)
            && eOther.hasComponent(VisibleComponent.class)) {
          final boolean isCollision = checkCollision(eMoving, collision,
              positionMoving, eOther);

          if (isCollision) {
            if (LOGGER.isLoggable(Level.INFO)) {
              final ItemNameComponent name1 = eMoving
                  .getComponent(ItemNameComponent.class);
              final ItemNameComponent name2 = eOther
                  .getComponent(ItemNameComponent.class);

              LOGGER.log(Level.INFO, "Collision between " + name1.getName()
                  + " and " + name2.getName());
            }
            return;
          }
        }
      }
    }
  }

  private boolean checkCollision(final Entity eMoving,
      final CollisionGroupComponent collisionMoving,
      final PositionComponent positionMoving, final Entity eOther) {

    final PositionComponent positionOther = eOther
        .getComponent(PositionComponent.class);

    final CollisionGroupComponent collisionOther = eOther
        .getComponent(CollisionGroupComponent.class);

    if (positionOther != null
        && collisionMoving.getCollisionGroupName().equals(
            collisionOther.getCollisionGroupName())) {

      final MovementTargetComponent moveTarget = eMoving
          .getComponent(MovementTargetComponent.class);

      // ignore reverting movements
      if (!moveTarget.isRevert()) {

        final Point targetPoint = LevelUtil.convertToLevel(
            moveTarget.getTarget(), levelScale);

        final Point otherLocPoint = LevelUtil.convertToLevel(
            positionOther.getLocation(), levelScale);

        if (targetPoint.equals(otherLocPoint)) {
          eMoving.addComponent(moveTarget.setRevert(true));
          return true;
        }
      }
    }

    return false;
  }
}
