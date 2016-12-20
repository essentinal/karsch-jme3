package karsch2.es.system;

import java.awt.Point;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import karsch2.core.Item;
import karsch2.es.component.ChangeLevelComponent;
import karsch2.es.component.ItemNameComponent;
import karsch2.es.component.LevelAssignmentComponent;
import karsch2.es.component.LevelComponent;
import karsch2.es.component.LevelLinkComponent;
import karsch2.es.component.MarkerComponent;
import karsch2.es.component.MovementTargetComponent;
import karsch2.es.component.PlayerControlComponent;
import karsch2.es.component.PositionComponent;
import karsch2.es.component.ScaleComponent;
import karsch2.es.component.StaticComponent;
import karsch2.es.component.VisibleComponent;
import karsch2.io.LevelIO;
import karsch2.io.in.ItemUtil;
import karsch2.io.out.XMLItemRef;
import karsch2.io.out.XMLLevel;
import karsch2.utils.LevelUtil;

import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;

import es.core.entity.Entity;
import es.core.entity.EntityManager;
import es.core.system.ESystem;

@SuppressWarnings("unchecked")
public class LevelSystem implements ESystem {
  private static final Logger LOGGER = Logger.getLogger(LevelSystem.class
      .getName());

  private int currentLevel = 1;
  private int lastLevel;
  private final AssetManager assetManager;
  private XMLLevel level;

  public LevelSystem(final AssetManager assetManager) {
    this.assetManager = assetManager;
  }

  @Override
  public void process(final float tpf) {
    final List<Entity> changeLevel = EntityManager.getInstance().getEntities(
        ChangeLevelComponent.class);

    for (final Entity e : changeLevel) {
      // TODO what if there where multiple entities?

      final ChangeLevelComponent changeLvl = e
          .getComponent(ChangeLevelComponent.class);

      if (changeLvl.isNextLevel()) {
        currentLevel += 1;
      } else if (changeLvl.isPrevLevel()) {
        currentLevel -= 1;
      } else {
        currentLevel = changeLvl.getNewLevel();
      }

      if (currentLevel < 1) {
        currentLevel = 1;
      }

      LOGGER.log(Level.INFO, "Set new level:{0}", currentLevel);

      e.removeComponent(ChangeLevelComponent.class);
    }

    if (lastLevel != currentLevel) {
      final List<Entity> lvlEntities = EntityManager.getInstance().getEntities(
          LevelComponent.class);

      if (lvlEntities.isEmpty() || !trySetEnabled(lvlEntities, currentLevel)) {
        try {
          // load the level
          createLevel(currentLevel);

          // create a new level entity
          final Entity lvlEntity = EntityManager.getInstance().createEntity();

          // assign a new LevelComponent
          lvlEntity.addComponent(new LevelComponent(level.getStyle(),
              currentLevel, level.getLevelToWorldScale(), true));
        } catch (final Exception e) {
          currentLevel = lastLevel;
          return;
        }
      }

      movePlayerToLevel(lastLevel, currentLevel);
      setEntitiesEnabled(currentLevel);
      lastLevel = currentLevel;
    }
  }

  private boolean trySetEnabled(final List<Entity> entities,
      final int levelNumber) {

    boolean retBool = false;

    for (final Entity entity : entities) {
      final LevelComponent lvlComp = entity.getComponent(LevelComponent.class);
      if (lvlComp != null) {
        if (lvlComp.getLevelNumber() == levelNumber) {

          if (!lvlComp.isEnabled()) {
            // if there is an entity with the right number that is disabled,
            // enable it
            entity.addComponent(new LevelComponent(lvlComp.getStyle(), lvlComp
                .getLevelNumber(), lvlComp.getLevelToWorldScale(), true));

            // set the return value to true because the level has been enabled
            retBool |= true;
          }
        } else if (lvlComp.isEnabled()) {
          // if the entity has not the right level number, disable it
          entity.addComponent(new LevelComponent(lvlComp.getStyle(), lvlComp
              .getLevelNumber(), lvlComp.getLevelToWorldScale(), false));
          // retBool |= true;
        }
      }
    }

    return retBool;
  }

  private void setEntitiesEnabled(final int levelNumber) {
    final List<Entity> entities = EntityManager.getInstance().getEntities(
        LevelAssignmentComponent.class);

    for (final Entity e : entities) {
      final LevelAssignmentComponent levelAssignment = e
          .getComponent(LevelAssignmentComponent.class);

      if (levelAssignment.getLevelNumber() == levelNumber) {
        // ok
        e.addComponent(new VisibleComponent());
      } else {
        // TODO use a Active flag or somewhat
        e.removeComponent(VisibleComponent.class);
      }
    }
  }

  private void movePlayerToLevel(final int lastLevelNumber,
      final int nextLevelNumber) {
    final List<Entity> players = EntityManager.getInstance().getEntities(
        PlayerControlComponent.class);

    final Entity ePlayer = players.get(0);
    ePlayer.addComponent(new LevelAssignmentComponent(nextLevelNumber));

    final PositionComponent posCompPl = ePlayer
        .getComponent(PositionComponent.class);

    final List<Entity> eMarkers = EntityManager.getInstance().getEntities(
        MarkerComponent.class);

    for (final Entity eMarker : eMarkers) {
      final LevelAssignmentComponent lvlAssignComp = eMarker
          .getComponent(LevelAssignmentComponent.class);

      final LevelLinkComponent lvlLinkComp = eMarker
          .getComponent(LevelLinkComponent.class);
      if (lvlAssignComp != null
          && lvlAssignComp.getLevelNumber() == nextLevelNumber
          && lvlLinkComp != null
          && lvlLinkComp.getLevelNumber() == lastLevelNumber) {

        final PositionComponent posMarkerComp = eMarker
            .getComponent(PositionComponent.class);
        if (posMarkerComp != null) {
          final MarkerComponent markerComp = eMarker
              .getComponent(MarkerComponent.class);

          final Vector3f newPPos = markerComp.getInteractionLocation();
          final Vector3f newPTarget = posMarkerComp.getLocation();

          final Vector3f dir = newPTarget.subtract(newPPos).normalizeLocal();

          ePlayer.addComponent(new PositionComponent(newPPos.add(dir),
              posCompPl.getRotation()));

          final PlayerControlComponent playerControlComp = ePlayer
              .getComponent(PlayerControlComponent.class);
          // TODO magic number
          ePlayer.addComponent(new MovementTargetComponent(posCompPl
              .getLocation(), newPTarget, playerControlComp.getSpeed() * 0.8f,
              false));
          return;
        }
      }
    }
  }

  private void createLevel(final int levelNumber) {
    level = LevelIO.loadLevel(assetManager, "level" + levelNumber
        + ".level.xml");
    final float scale = level.getLevelToWorldScale();

    LOGGER.log(Level.INFO, "Creating level {0}", levelNumber);

    for (final List<XMLItemRef> itms : level.getItemRefs().values()) {
      for (final XMLItemRef itmRef : itms) {
        final String itemName = itmRef.getName();
        final Item item = ItemUtil.getItem(itemName);

        if (item != null) {
          final Entity e = EntityManager.getInstance().createEntity();
          e.addComponents(item.getComponents());
          e.addComponent(new PositionComponent(LevelUtil.convertToWorld(
              itmRef.getLevelX(), itmRef.getLevelY(), scale), itmRef
              .getDirection().toRotation()));

          e.addComponent(new LevelAssignmentComponent(levelNumber));
          e.addComponent(new ItemNameComponent(itemName));

          final float xScale = itmRef.getxSize() != null ? itmRef.getxSize()
              : 1f;
          final float zScale = itmRef.getySize() != null ? itmRef.getySize()
              : 1f;
          if (xScale != 1f || zScale != 1f) {
            e.addComponent(new ScaleComponent(xScale, 1f, zScale));
          }

          if (item.getType() != null) {
            switch (item.getType()) {
            case MARKER:
              final Point p = itmRef.getInteractionLocation();
              e.addComponent(new MarkerComponent(LevelUtil.convertToWorld(p.x,
                  p.y, scale)));

              break;
            case LEVEL_STATIC:
              e.addComponent(new StaticComponent());

              break;
            default:
              break;
            }
          }

          if (itmRef.getLevelLink() != null) {
            e.addComponent(new LevelLinkComponent(itmRef.getLevelLink()));
          }

        } else {
          LOGGER.log(Level.WARNING, "Item {0} was not found", itmRef.getName());
        }
      }
    }
  }
}
