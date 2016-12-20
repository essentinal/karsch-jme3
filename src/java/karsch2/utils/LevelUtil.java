package karsch2.utils;

import java.awt.Point;

import karsch2.es.component.LevelComponent;

import com.jme3.math.Vector3f;

import es.core.entity.Entity;
import es.core.entity.EntityManager;

public final class LevelUtil {
  public static Vector3f snapWorldToLevelGrid(final Vector3f location,
      final float levelToWorldScale) {
    final Vector3f vec = location.clone();
    vec.x = Math.round(vec.x / levelToWorldScale) * levelToWorldScale;
    // location.y = Math.round(location.y / levelToWorldScale) *
    // levelToWorldScale;
    vec.z = Math.round(vec.z / levelToWorldScale) * levelToWorldScale;

    return vec;
  }

  public static Point convertToLevel(final Vector3f vec,
      final float levelToWorldScale) {
    return new Point(Math.round(vec.x / levelToWorldScale), Math.round(vec.z
        / levelToWorldScale));
  }

  public static Vector3f convertToWorld(final int x, final int y,
      final float levelToWorldScale) {
    return new Vector3f(x * levelToWorldScale, 0, y * levelToWorldScale);
  }

  public static float getLevelScale() {
    for (final Entity lvlEntity : EntityManager.getInstance().getEntities(
        LevelComponent.class)) {
      final LevelComponent lvlComp = lvlEntity
          .getComponent(LevelComponent.class);
      if (lvlComp.isEnabled()) {
        final float levelScale = lvlComp.getLevelToWorldScale();
        return levelScale;
      }
    }

    return 1f;
  }

}
