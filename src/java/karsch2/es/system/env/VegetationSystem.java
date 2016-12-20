package karsch2.es.system.env;

import java.util.logging.Level;
import java.util.logging.Logger;

import karsch.level.Level.LevelStyle;
import karsch2.es.component.InSceneComponent;
import karsch2.es.component.LevelComponent;
import karsch2.es.component.PositionComponent;
import karsch2.es.component.ScaleComponent;
import karsch2.es.component.TerrainComponent;
import karsch2.es.component.VisibleComponent;
import karsch2.es.system.JMESystem;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.FastMath;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.control.BillboardControl;
import com.jme3.scene.shape.Quad;
import com.jme3.texture.Texture;

import es.core.entity.Entity;
import es.core.entity.EntityManager;

public class VegetationSystem extends JMESystem {
  private static final Logger LOGGER = Logger.getLogger(VegetationSystem.class
      .getName());

  private final AssetManager assetManager;
  private final Node rootNode;

  private int levelNumber = -1;

  private Node vegetationNode;

  public VegetationSystem(final AssetManager assetManager, final Node rootNode) {
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

          // remove
          if (vegetationNode != null) {
            vegetationNode.removeFromParent();
            vegetationNode = null;
          }

          // add
          createVegetation(levelComp);

          levelNumber = levelComp.getLevelNumber();
        }

      }
    }
  }

  private void createVegetation(final LevelComponent levelComp) {
    for (final Entity eTerrain : EntityManager.getInstance().getEntities(
        TerrainComponent.class)) {
      final TerrainComponent terrainComp = eTerrain
          .getComponent(TerrainComponent.class);
      final VisibleComponent visible = eTerrain
          .getComponent(VisibleComponent.class);

      if (terrainComp != null && visible != null) {

        final InSceneComponent inSceneComp = eTerrain
            .getComponent(InSceneComponent.class);
        if (inSceneComp != null) {

          LOGGER.log(Level.INFO, "Create vegetation for terrain {0}", eTerrain);

          final ScaleComponent scaleComp = eTerrain
              .getComponent(ScaleComponent.class);

          final PositionComponent posComp = eTerrain
              .getComponent(PositionComponent.class);

          final LevelStyle style = levelComp.getStyle();
          final float levelScale = levelComp.getLevelToWorldScale();

          for (int i = 0; i < scaleComp.getScale().x; i++) {
            for (int j = 0; j < scaleComp.getScale().z; j++) {
              if (style == LevelStyle.LS_FARM) {
                addFlowers(i, j, "FLOWER", .5f, levelScale);
              } else if (style == LevelStyle.LS_FORREST) {
                addFlowers(i, j, "MUSHROOM", .5f, levelScale);
              } else if (style == LevelStyle.LS_UNDERGRND) {
                addFlowers(i, j, "ROCK", .5f, levelScale);
              } else if (style == LevelStyle.LS_HOUSE) {
                addFlowers(i, j, "SHIT", .3f, levelScale);
              }
            }
          }

        }

        return;
      }

    }
  }

  private void addFlowers(final int x, final int y, final String fileName,
      final float statSize, final float levelScale) {

    if (vegetationNode == null) {
      vegetationNode = new Node("Vegetation");
      rootNode.attachChild(vegetationNode);
    }

    final int random = FastMath.rand.nextInt(20);
    if (random > 2) {
      return;
    }

    final float randSize = FastMath.rand.nextFloat();

    final Quad quad = new Quad(statSize + randSize, statSize + randSize);
    final Geometry geo = new Geometry(fileName + x + y, quad);
    geo.addControl(new BillboardControl());

    final Texture t = assetManager
        .loadTexture(fileName + (random + 1) + ".PNG");

    final Material mat = new Material(assetManager,
        "Common/MatDefs/Misc/Unshaded.j3md");
    mat.setTexture("ColorMap", t);
    mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
    geo.setMaterial(mat);

    geo.setQueueBucket(Bucket.Transparent);

    geo.setLocalTranslation(x * levelScale + 1.5f + FastMath.rand.nextInt(2),
        .5f, y * levelScale + 1.5f + FastMath.rand.nextInt(2));

    vegetationNode.attachChild(geo);
  }

}
