package karsch2.es.system.env;

import karsch2.es.component.LevelComponent;
import karsch2.es.system.JMESystem;

import com.jme3.asset.AssetManager;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.util.SkyFactory;

import es.core.entity.Entity;
import es.core.entity.EntityManager;

public class SkySystem extends JMESystem {

  private final AssetManager assetManager;
  private final Node rootNode;

  private Spatial sky;

  private int levelNumber = -1;

  public SkySystem(final AssetManager assetManager, final Node rootNode) {
    this.assetManager = assetManager;
    this.rootNode = rootNode;
  }

  @Override
  public void process(final float tpf) {
    for (final Entity eLevel : EntityManager.getInstance().getEntities(
        LevelComponent.class)) {
      final LevelComponent levelComp = eLevel
          .getComponent(LevelComponent.class);
      if (levelComp.isEnabled()) {

        if (levelNumber == levelComp.getLevelNumber()) {
          // all ok
          break;
        } else {

          if (sky != null) {
            // remove
            sky.removeFromParent();
            sky = null;
          }

          switch (levelComp.getStyle()) {
          case LS_FARM:
          case LS_FORREST:
            sky = SkyFactory.createSky(assetManager,
                "Textures/Sky/Bright/BrightSky.dds", false);
            rootNode.attachChild(sky);

            levelNumber = levelComp.getLevelNumber();
            return;

          default:
            break;
          }

        }

      }
    }
  }

}
