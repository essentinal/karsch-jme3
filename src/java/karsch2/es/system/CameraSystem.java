package karsch2.es.system;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import karsch2.es.component.PlayerControlComponent;

import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;

import es.core.entity.Entity;
import es.core.entity.EntityManager;

@SuppressWarnings("unchecked")
public class CameraSystem extends JMESystem {
  private static final Logger LOGGER = Logger.getLogger(CameraSystem.class
      .getName());

  private final SpatialRegistry spatialRegistry;

  private final Camera cam;
  private Entity playerEntity;

  // TODO put this into a component
  private final Vector3f camPos = new Vector3f(0, 20, 15);

  public CameraSystem(final Camera cam, final SpatialRegistry spatialRegistry) {
    this.cam = cam;
    this.spatialRegistry = spatialRegistry;

    final int camWidth = cam.getWidth();
    final int camHeight = cam.getHeight();
    final float aspect = camWidth / (float) camHeight;

    cam.setFrustumPerspective(65f, aspect, 0.5f, 1000f);
  }

  @Override
  public void process(final float tpf) {
    if (needsUpdate()) {
      updatePlayerEntity();
    }

    updateCam();
  }

  private boolean needsUpdate() {
    if (playerEntity == null) {
      return true;
    }
    // check if the player is still the same person it used to be
    final PlayerControlComponent playerComp = playerEntity
        .getComponent(PlayerControlComponent.class);
    return playerComp == null;
  }

  private boolean updatePlayerEntity() {
    final List<Entity> entities = EntityManager.getInstance().getEntities(
        PlayerControlComponent.class);

    if (entities.size() == 1) {
      this.playerEntity = entities.get(0);
      return true;
    } else {
      LOGGER.log(Level.WARNING, "There are {0} player control components",
          entities.size());
    }

    return false;
  }

  private void updateCam() {
    if (playerEntity != null) {
      final Node player = (Node) spatialRegistry
          .getSpatialForEntity(playerEntity);
      if (player == null) {
        playerEntity = null;
      } else {
        cam.setLocation(player.getLocalTranslation().add(camPos));
        cam.lookAt(player.getLocalTranslation(), Vector3f.UNIT_Y);
      }
    }
  }

}
