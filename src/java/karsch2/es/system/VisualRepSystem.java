package karsch2.es.system;

import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

import karsch2.es.component.AnimationComponent;
import karsch2.es.component.InSceneComponent;
import karsch2.es.component.PositionComponent;
import karsch2.es.component.ScaleComponent;
import karsch2.es.component.StaticComponent;
import karsch2.es.component.VisibleComponent;
import karsch2.es.component.VisualRepComponent;

import com.jme3.app.Application;
import com.jme3.asset.AssetManager;
import com.jme3.scene.BatchNode;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import es.core.entity.Entity;
import es.core.entity.EntityManager;
import es.core.system.ESystem;

@SuppressWarnings("unchecked")
public class VisualRepSystem implements ESystem {
  private static final Logger LOGGER = Logger.getLogger(VisualRepSystem.class
      .getName());

  private final Application app;
  private final SpatialRegistry spatialRegistry;
  private final AssetManager assetManager;
  private final Node node;
  private final BatchNode batchNode;

  // TODO batching should care much more about the location of spatials
  private boolean needsRebatch;
  private final float rebatchCheckInterval = 5f;
  private float time;

  public VisualRepSystem(final Application app,
      final SpatialRegistry spatialRegistry, final Node rootNode) {
    this.app = app;
    this.spatialRegistry = spatialRegistry;
    this.assetManager = app.getAssetManager();
    this.node = new Node("Dynamic Node");
    this.batchNode = new BatchNode("Static Node");
    rootNode.attachChild(node);
    rootNode.attachChild(batchNode);
  }

  @Override
  public void process(final float tpf) {

    for (final Entity e : EntityManager.getInstance().getEntities(
        VisualRepComponent.class, VisibleComponent.class)) {
      updateVisualRep(e);
    }

    time += tpf;
    if (time > rebatchCheckInterval) {
      time = 0f;
      if (needsRebatch) {
        needsRebatch = false;
        // doBatching();
      }
    }
  }

  private boolean updateVisualRep(final Entity entity) {
    // TODO aggregate detaches/attaches

    final VisualRepComponent visRep = entity
        .getComponent(VisualRepComponent.class);
    final VisibleComponent visible = entity
        .getComponent(VisibleComponent.class);

    if (visRep != null && visible != null) {
      final InSceneComponent inSceneComp = entity
          .getComponent(InSceneComponent.class);
      if (inSceneComp == null) {

        final String currentModelName = visRep.getAssetName();
        final Spatial currentModel = assetManager.loadModel(currentModelName);

        entity.addComponent(new InSceneComponent());

        app.enqueue(new Callable<Void>() {

          @Override
          public Void call() throws Exception {
            final PositionComponent position = entity
                .getComponent(PositionComponent.class);
            final StaticComponent staticComp = entity
                .getComponent(StaticComponent.class);
            final ScaleComponent scaleComp = entity
                .getComponent(ScaleComponent.class);
            final AnimationComponent animComp = entity
                .getComponent(AnimationComponent.class);

            currentModel.scale(visRep.getScale().x, visRep.getScale().y,
                visRep.getScale().z);
            currentModel.setLocalTranslation(visRep.getTranslation());

            final Node n = new Node(entity.toString());
            n.attachChild(currentModel);

            if (scaleComp != null) {
              n.setLocalScale(scaleComp.getScale().x, scaleComp.getScale().y,
                  scaleComp.getScale().z);
            }
            n.setLocalTranslation(position.getLocation());
            n.setLocalRotation(position.getRotation());

            if (animComp != null && animComp.isSet()) {
              entity.addComponent(new AnimationComponent(animComp.getType(),
                  false));
            }

            if (staticComp != null) {
              batchNode.attachChild(n);

              needsRebatch = true;
            } else {
              node.attachChild(n);
            }
            spatialRegistry.setSpatialForEntity(entity, n);

            LOGGER.log(
                Level.FINE,
                "Number of visible entities: {0}",
                EntityManager.getInstance()
                    .getEntities(VisualRepComponent.class).size());

            return null;
          }
        });

        entity.addComponent(visRep);
      }
    } else {

      app.enqueue(new Callable<Void>() {

        @Override
        public Void call() throws Exception {
          final InSceneComponent inSceneComp = entity
              .getComponent(InSceneComponent.class);
          if (inSceneComp != null) {
            entity.removeComponent(InSceneComponent.class);

            // dispose if the entity has no VisualRepComponent
            // anymore..
            final Spatial model = spatialRegistry
                .removeSpatialForEntity(entity);

            if (model != null) {
              model.removeFromParent();
            }

            final StaticComponent itemTypeComp = entity
                .getComponent(StaticComponent.class);

            if (itemTypeComp != null) {
              needsRebatch = true;
            }
          }

          return null;
        }
      });

      return false;
    }
    return true;
  }

  private void doBatching() {
    app.enqueue(new Callable<Void>() {

      @Override
      public Void call() throws Exception {
        // TODO batch in different zones
        batchNode.batch();

        return null;
      }
    });

  }
}
