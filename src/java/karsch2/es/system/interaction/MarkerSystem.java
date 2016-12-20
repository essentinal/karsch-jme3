package karsch2.es.system.interaction;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import karsch2.es.component.ChangeLevelComponent;
import karsch2.es.component.LevelComponent;
import karsch2.es.component.LevelLinkComponent;
import karsch2.es.component.MarkerComponent;
import karsch2.es.component.PlayerControlComponent;
import karsch2.es.component.PositionComponent;
import karsch2.es.component.VisibleComponent;
import es.core.entity.Entity;
import es.core.entity.EntityManager;
import es.core.system.ESystem;

@SuppressWarnings("unchecked")
public class MarkerSystem implements ESystem {
  private static final Logger LOGGER = Logger.getLogger(MarkerSystem.class
      .getName());
  private static final float MARKER_PICKUP_DISTANCE = 0.5f;

  @Override
  public void process(final float tpf) {
    final List<Entity> eMarkers = EntityManager.getInstance().getEntities(
        MarkerComponent.class);

    final List<Entity> players = EntityManager.getInstance().getEntities(
        PlayerControlComponent.class);

    for (final Entity ePlayer : players) {
      final PositionComponent posCompPl = ePlayer
          .getComponent(PositionComponent.class);

      if (posCompPl != null) {
        for (final Entity eMarker : eMarkers) {

          final MarkerComponent markerComp = eMarker
              .getComponent(MarkerComponent.class);

          if (markerComp != null
              && eMarker.hasComponent(VisibleComponent.class)) {
            final float dist = markerComp.getInteractionLocation().distance(
                posCompPl.getLocation());

            if (dist < MARKER_PICKUP_DISTANCE) {
              playerCollectItem(ePlayer, eMarker);
            }
          }
        }
      }
    }
  }

  private void playerCollectItem(final Entity player, final Entity item) {
    LOGGER.log(Level.INFO, "Player {0} collects item {1}", new Object[] {
        player, item });

    final LevelLinkComponent lvlLinkComp = item
        .getComponent(LevelLinkComponent.class);
    if (lvlLinkComp != null) {
      final List<Entity> entities = EntityManager.getInstance().getEntities(
          LevelComponent.class);
      if (!entities.isEmpty()) {
        for (final Entity e : entities) {
          if (e.getComponent(LevelComponent.class).isEnabled()) {
            e.addComponent(new ChangeLevelComponent(lvlLinkComp
                .getLevelNumber()));
            break;
          }
        }
      } else {
        LOGGER.log(Level.WARNING, "Components are empty!");
      }
    }
  }
}
