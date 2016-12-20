package karsch2.es.system;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.jme3.scene.Spatial;

import es.core.entity.Entity;

public class SpatialRegistry {
  private final Map<Entity, Spatial> spatials = new ConcurrentHashMap<Entity, Spatial>();

  public void setSpatialForEntity(final Entity entity, final Spatial spatial) {
    final Spatial s = spatials.put(entity, spatial);
    if (s != null) {
      throw new RuntimeException(
          "Error, spatial has not been unassigned before");
    }
  }

  public Spatial getSpatialForEntity(final Entity entity) {
    return spatials.get(entity);
  }

  public Spatial removeSpatialForEntity(final Entity entity) {
    return spatials.remove(entity);
  }

}
