package karsch2.es.system;

import java.util.concurrent.Callable;

import karsch2.core.AnimationType;
import karsch2.es.component.AnimationComponent;
import karsch2.es.component.MovementTargetComponent;
import karsch2.es.component.PositionComponent;
import karsch2.es.component.VisibleComponent;
import karsch2.utils.LevelUtil;
import karsch2.utils.RotationUtils;

import com.jme3.app.Application;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

import es.core.entity.Entity;
import es.core.entity.EntityManager;
import es.core.system.ESystem;

@SuppressWarnings("unchecked")
public class MovementSystem implements ESystem {
  private static final float MIN_MOVEMENT = 0.1f;
  private static final float MIN_ROTATION = 0.1f;

  private final Application app;
  private final SpatialRegistry spatialRegistry;

  public MovementSystem(final Application app,
      final SpatialRegistry spatialRegistry) {
    this.app = app;
    this.spatialRegistry = spatialRegistry;
  }

  @Override
  public void process(final float tpf) {
    for (final Entity e : EntityManager.getInstance().getEntities(
    // MovementVectorComponent.class,
        MovementTargetComponent.class)) {
      updateLocation(e, tpf);
    }

  }

  private boolean updateLocation(final Entity entity, final float tpf) {
    final PositionComponent position = entity
        .getComponent(PositionComponent.class);
    // TODO vector movement unsupported!
    // final MovementVectorComponent movementVector = entity
    // .getComponent(MovementVectorComponent.class);
    final MovementTargetComponent movementTarget = entity
        .getComponent(MovementTargetComponent.class);

    if (position != null) {
      // if (movementVector != null) {
      // entity.addComponent(new PositionComponent(position.getLocation().add(
      // movementVector.getMovement().mult(tpf)), position.getRotation()));
      // } else
      if (movementTarget != null) {

        if (movementTarget.isRevert()) {

          final Vector3f snappedLocation = LevelUtil.snapWorldToLevelGrid(
              movementTarget.getSource(), LevelUtil.getLevelScale());

          entity.addComponent(new MovementTargetComponent(movementTarget
              .getSource(), snappedLocation, movementTarget.getSpeed(), false));

        } else {
          final Vector3f target = movementTarget.getTarget();

          final Vector3f loc = position.getLocation();

          // ignore y for location diff
          final Vector3f diff = new Vector3f(target.x - loc.x, 0f, target.z
              - loc.z);

          final Vector3f dir = diff.normalize();

          final float speed = movementTarget.getSpeed();

          if (diff.length() < MIN_MOVEMENT * speed) {
            finishMovement(entity, target, position.getRotation());
          } else {

            final Vector3f nextPosition = position.getLocation().add(
                dir.mult(tpf * speed));

            final Quaternion rot = updateRotation(entity, nextPosition,
                position.getRotation(), dir);

            startMovement(entity, nextPosition, rot);
          }
        }
      }

      app.enqueue(new Callable<Void>() {

        @Override
        public Void call() throws Exception {
          if (entity.hasComponent(VisibleComponent.class)) {
            final Spatial model = spatialRegistry.getSpatialForEntity(entity);

            model.setLocalTranslation(position.getLocation());
            model.setLocalRotation(position.getRotation());
          }
          return null;
        }
      });
    }

    return true;
  }

  private Quaternion updateRotation(final Entity entity,
      final Vector3f position, final Quaternion rot, final Vector3f lookAt) {

    final Quaternion targetRot = rot.clone();
    targetRot.lookAt(lookAt, Vector3f.UNIT_Y);

    final float currentDegrees = rot.toAngles(null)[1] * FastMath.RAD_TO_DEG;
    final float targetDegrees = targetRot.toAngles(null)[1]
        * FastMath.RAD_TO_DEG;

    final float deltaDegrees = RotationUtils.diffAngleDeg(currentDegrees,
        targetDegrees);

    if (FastMath.abs(deltaDegrees) < MIN_ROTATION) {
      // no rotation necessary
      return rot;
    }

    final float speed = 20f;
    final float newDegrees;

    // this prevents rotation oscillation, just set the remaining degrees if
    // they are very small
    if (deltaDegrees > 0f) {
      newDegrees = Math.min(speed, deltaDegrees);
    } else {
      newDegrees = Math.max(-speed, deltaDegrees);
    }

    float newAngle = (currentDegrees - newDegrees) * FastMath.DEG_TO_RAD;

    if (newAngle > FastMath.TWO_PI) {
      newAngle -= FastMath.TWO_PI;
    }

    targetRot.fromAngleNormalAxis(newAngle, Vector3f.UNIT_Y);

    return targetRot;
  }

  private void finishMovement(final Entity entity, final Vector3f position,
      final Quaternion rotation) {
    entity.removeComponent(MovementTargetComponent.class);
    entity.addComponent(new PositionComponent(position, rotation));
    entity.addComponent(new AnimationComponent(AnimationType.STAND, false));
  }

  private void startMovement(final Entity entity, final Vector3f position,
      final Quaternion rotation) {
    entity.addComponent(new PositionComponent(position, rotation));
    entity.addComponent(new AnimationComponent(AnimationType.MOVE, false));
  }
}
