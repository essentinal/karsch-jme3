package karsch2.es.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

import karsch2.core.AnimMapping;
import karsch2.core.AnimationType;
import karsch2.es.component.AnimationComponent;
import karsch2.es.component.InSceneComponent;
import karsch2.es.component.VisibleComponent;
import karsch2.es.component.VisualRepComponent;
import karsch2.io.in.ItemUtil;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.app.Application;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.SceneGraphVisitorAdapter;
import com.jme3.scene.Spatial;

import es.core.entity.Entity;
import es.core.entity.EntityManager;
import es.core.system.ESystem;

@SuppressWarnings("unchecked")
public final class AnimationSystem implements ESystem {
  private static final Logger logger = Logger.getLogger(AnimationSystem.class
      .getName());

  private final Application app;
  private final SpatialRegistry spatialRegistry;

  private final Map<Entity, List<AnimControl>> animControls = new HashMap<Entity, List<AnimControl>>();
  private final Map<Entity, List<AnimChannel>> animChannels = new HashMap<Entity, List<AnimChannel>>();
  private final Map<Entity, Map<AnimationType, AnimMapping>> animMappings = new HashMap<Entity, Map<AnimationType, AnimMapping>>();

  public AnimationSystem(final Application app,
      final SpatialRegistry spatialRegistry) {
    this.app = app;
    this.spatialRegistry = spatialRegistry;
  }

  @Override
  public void process(final float tpf) {

    for (final Entity entity : EntityManager.getInstance().getEntities(
        AnimationComponent.class)) {
      updateAnimation(entity);
    }
  }

  private void updateAnimation(final Entity entity) {
    final AnimationComponent animation = entity
        .getComponent(AnimationComponent.class);
    if (animation != null) {
      if (!entity.hasComponent(VisibleComponent.class)) {
        // remove animation controls
        animChannels.remove(entity);
        animControls.remove(entity);
        animMappings.remove(entity);
      }

      if (animation.isSet()) {
        return;
      } else {

        app.enqueue(new Callable<Void>() {
          @Override
          public Void call() throws Exception {
            if (setAnimation(entity, animation.getType())) {
              entity.addComponent(new AnimationComponent(animation.getType(),
                  true));
            }

            return null;
          }
        });
      }
    }
  }

  private boolean setAnimation(final Entity entity, final AnimationType type) {

    if (!animControls.containsKey(entity)) {
      setAnimControls(entity);

      if (!animControls.containsKey(entity)) {
        logger.log(Level.WARNING, "No animControl found for {0}", entity);
        return false;
      }
    }

    // TODO this may fail
    final AnimMapping mapping = animMappings.get(entity).get(type);
    final String name = mapping.getName();
    final float speed = mapping.getSpeed();

    final List<AnimChannel> channels = animChannels.get(entity);

    if (channels != null) {
      for (final Iterator<AnimChannel> it = channels.iterator(); it.hasNext();) {
        final AnimChannel animChannel = it.next();
        if (animChannel.getAnimationName() == null
            || !animChannel.getAnimationName().equals(name)
            || animChannel.getSpeed() != speed) {
          logger.log(Level.INFO, "Setting animation {0} with speed {1}",
              new Object[] { name, speed });

          animChannel.setAnim(name);
          animChannel.setSpeed(speed);

          return true;
        }
      }
    }

    return false;
  }

  private void setAnimControls(final Entity entity) {

    if (entity.hasComponent(InSceneComponent.class)) {

      final VisualRepComponent visRep = entity
          .getComponent(VisualRepComponent.class);

      if (visRep != null) {
        final Map<AnimationType, AnimMapping> mappings = ItemUtil.getItem(
            visRep.getItemName()).getAnims();
        animMappings.put(entity, mappings);
      }
      final Spatial model = spatialRegistry.getSpatialForEntity(entity);
      final SceneGraphVisitorAdapter visitor = new FindControlsVisitor(entity);
      model.depthFirstTraversal(visitor);

    } else {
      final List<AnimControl> controls = animControls.get(entity);

      if (controls != null) {
        for (final Iterator<AnimControl> it = controls.iterator(); it.hasNext();) {
          final AnimControl animControl = it.next();
          animControl.clearChannels();
        }
      }

      animControls.remove(entity);
      animChannels.remove(entity);
      animMappings.remove(entity);

    }
  }

  private class FindControlsVisitor extends SceneGraphVisitorAdapter {

    private final Entity entity;
    private List<AnimControl> controls;
    private List<AnimChannel> channels;

    public FindControlsVisitor(final Entity entity) {
      this.entity = entity;
      controls = animControls.get(entity);
      channels = animChannels.get(entity);
    }

    @Override
    public void visit(final Geometry geom) {
      super.visit(geom);
      checkForAnimControl(geom);
    }

    @Override
    public void visit(final Node geom) {
      super.visit(geom);
      checkForAnimControl(geom);
    }

    private void checkForAnimControl(final Spatial geom) {
      final AnimControl control = geom.getControl(AnimControl.class);
      if (control == null) {
        return;
      }

      if (controls == null) {
        controls = new ArrayList<AnimControl>();
        animControls.put(entity, controls);
      }
      if (channels == null) {
        channels = new ArrayList<AnimChannel>();
        animChannels.put(entity, channels);
      }

      controls.add(control);
      channels.add(control.createChannel());
    }
  }

}