package karsch2.es.system;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import karsch2.es.component.InSceneComponent;
import karsch2.es.component.LevelComponent;
import karsch2.es.component.ModifyTerrainComponent;
import karsch2.es.component.MovementTargetComponent;
import karsch2.es.component.PositionComponent;
import karsch2.es.component.ScaleComponent;
import karsch2.es.component.TerrainComponent;
import karsch2.es.component.VisibleComponent;
import karsch2.utils.ImageUtils;
import karsch2.utils.LevelUtil;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.jme3.terrain.geomipmap.TerrainLodControl;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.terrain.geomipmap.lodcalc.DistanceLodCalculator;
import com.jme3.terrain.heightmap.AbstractHeightMap;
import com.jme3.terrain.heightmap.ImageBasedHeightMap;
import com.jme3.texture.Image;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;
import com.jme3.texture.Texture2D;
import com.jme3.texture.plugins.AWTLoader;

import es.core.entity.Entity;
import es.core.entity.EntityManager;

public class TerrainSystem extends JMESystem {
  private static final Logger LOGGER = Logger.getLogger(TerrainSystem.class
      .getName());

  private static final boolean DEBUG = false;

  private final AssetManager assetManager;
  private final Camera cam;
  private final Node rootNode;
  private final SpatialRegistry spatialRegistry;

  private TerrainQuad terrain;
  private Material mat_terrain;

  private int levelNumber = -1;

  public TerrainSystem(final AssetManager assetManager,
      final SpatialRegistry spatialRegistry, final Camera cam,
      final Node rootNode) {
    this.assetManager = assetManager;
    this.spatialRegistry = spatialRegistry;
    this.cam = cam;
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

          if (terrain != null) {
            // remove
            terrain.removeFromParent();
            terrain = null;
          }

          for (final Entity eTerrain : EntityManager.getInstance().getEntities(
              TerrainComponent.class)) {
            if (updateTerrain(eTerrain)) {
              levelNumber = levelComp.getLevelNumber();
            }
          }
        }

      }
    }

    if (terrain != null) {
      for (final Entity eMove : EntityManager.getInstance().getEntities(
          MovementTargetComponent.class)) {
        stickToTerrain(eMove);
      }
    }
  }

  private void stickToTerrain(final Entity entity) {
    final PositionComponent posComp = entity
        .getComponent(PositionComponent.class);
    if (posComp != null) {
      final Vector3f loc = posComp.getLocation().clone();

      final float y = terrain.getHeight(new Vector2f(loc.x, loc.z));

      loc.y = y;

      entity.addComponent(new PositionComponent(loc, posComp.getRotation()));
    }
  }

  private boolean updateTerrain(final Entity entity) {
    boolean updated = false;

    final TerrainComponent terrainComp = entity
        .getComponent(TerrainComponent.class);
    final VisibleComponent visible = entity
        .getComponent(VisibleComponent.class);

    if (terrainComp != null && visible != null) {
      if (terrain == null) {
        // add

        final InSceneComponent inSceneComp = entity
            .getComponent(InSceneComponent.class);
        if (inSceneComp == null) {
          entity.addComponent(new InSceneComponent());

          LOGGER.log(Level.INFO, "Create terrain for {0}", entity);

          final ScaleComponent scaleComp = entity
              .getComponent(ScaleComponent.class);

          final PositionComponent posComp = entity
              .getComponent(PositionComponent.class);

          init(terrainComp.getTextureName(), scaleComp.getScale().x,
              scaleComp.getScale().z, posComp.getLocation());

          updated = true;
        }
      }
    }

    return updated;
  }

  private void init(final String textureName, final float sizeX,
      final float sizeY, final Vector3f position) {

    LOGGER.log(Level.INFO, "Creating new terrain {0}x{1}", new Object[] {
        sizeX, sizeY });

    final int maxSize = (int) Math.max(sizeX, sizeY);

    final int size = FastMath.nearestPowerOfTwo(maxSize);

    /** 1. Create terrain material and load four textures into it. */
    mat_terrain = new Material(assetManager,
        "Common/MatDefs/Terrain/TerrainLighting.j3md");
    mat_terrain.setBoolean("useTriPlanarMapping", false);
    mat_terrain.setBoolean("WardIso", true);

    createLevelMaps(maxSize, maxSize, 5);

    final Texture alpha = new Texture2D(alphaMapTerrain);
    final Texture alpha2 = new Texture2D(alphaMapWater);

    /** 1.1) Add ALPHA map (for red-blue-green coded splat textures) */
    mat_terrain.setTexture("AlphaMap", alpha);
    mat_terrain.setTexture("AlphaMap_1", alpha2);

    /** 1.2) Add GRASS texture into the red layer (Tex1). */
    final Texture grass = assetManager.loadTexture(textureName);
    grass.setWrap(WrapMode.Repeat);
    mat_terrain.setTexture("DiffuseMap", grass);
    mat_terrain.setFloat("DiffuseMap_0_scale", size);

    /** 1.3) Add DIRT texture into the green layer (Tex2) */
    final Texture darkGreen = assetManager
        .loadTexture("dark_forrest_floor.png");
    darkGreen.setWrap(WrapMode.Repeat);
    mat_terrain.setTexture("DiffuseMap_1", darkGreen);
    mat_terrain.setFloat("DiffuseMap_1_scale", size);
    //
    /** 1.4) Add ROAD texture into the blue layer (Tex3) */
    final Texture dirt = assetManager.loadTexture("DIRT.PNG");
    dirt.setWrap(WrapMode.Repeat);
    mat_terrain.setTexture("DiffuseMap_2", dirt);
    mat_terrain.setFloat("DiffuseMap_2_scale", size);

    final Texture noise = assetManager
        .loadTexture("Textures/Terrain/splat/dirt.jpg");
    noise.setWrap(WrapMode.Repeat);
    mat_terrain.setTexture("DiffuseMap_3", noise);
    mat_terrain.setFloat("DiffuseMap_3_scale", size / 4);

    final Texture riverRock = assetManager
        .loadTexture("Textures/Terrain/Pond/Pond.jpg");
    riverRock.setWrap(WrapMode.Repeat);
    mat_terrain.setTexture("DiffuseMap_4", riverRock);
    mat_terrain.setFloat("DiffuseMap_4_scale", size);

    /** 2.a Create a custom height map from an image */
    AbstractHeightMap heightmap = null;

    heightmap = new ImageBasedHeightMap(heightMap, 0.01f);

    heightmap.load();

    /**
     * 3. We have prepared material and heightmap. Now we create the actual
     * terrain: 3.1) Create a TerrainQuad and name it "my terrain". 3.2) A good
     * value for terrain tiles is 64x64 -- so we supply 64+1=65. 3.3) We
     * prepared a heightmap of size 512x512 -- so we supply 512+1=513. 3.4) As
     * LOD step scale we supply Vector3f(1,1,1). 3.5) We supply the prepared
     * heightmap itself.
     */
    final int patchSize = 65;
    terrain = new TerrainQuad("my terrain", patchSize, size * 4 + 1,
        heightmap.getHeightMap());

    /**
     * 4. We give the terrain its material, position & scale it, and attach it.
     */
    terrain.setMaterial(mat_terrain);

    terrain.setLocalTranslation(terrain.getLocalTranslation().setY(
        -terrain.getWorldBound().getCenter().y));

    final Node n = new Node("terrain");
    n.attachChild(terrain);

    rootNode.attachChild(n);

    final Vector3f correction = new Vector3f();

    final float scale = LevelUtil.getLevelScale();

    if (sizeX > sizeY) {
      correction.z = (sizeX - sizeY) * scale;
    } else if (sizeX < sizeY) {
      correction.x = (sizeY - sizeX) * scale;
    }

    n.setLocalTranslation(position.add(correction));

    /** 5. The LOD (level of detail) depends on were the camera is: */
    final TerrainLodControl control = new TerrainLodControl(terrain, cam);
    control.setLodCalculator(new DistanceLodCalculator(patchSize, 0.1f)); // patch
                                                                          // size,
                                                                          // and
                                                                          // a
                                                                          // multiplier
                                                                          // terrain.addControl(control);
  }

  private void createLevelMaps(final int width, final int height,
      final int scale) {

    final Random rand = new Random(levelNumber);

    // create the tree image
    final BufferedImage imageTree = new BufferedImage(width * scale, height
        * scale, BufferedImage.TYPE_INT_ARGB);

    // create the stone image
    final BufferedImage imageStone = new BufferedImage(width * scale, height
        * scale, BufferedImage.TYPE_INT_ARGB);

    final BufferedImage imageWater = new BufferedImage(width * scale, height
        * scale, BufferedImage.TYPE_INT_RGB);

    BufferedImage imageHeight = new BufferedImage(width * scale,
        height * scale, BufferedImage.TYPE_INT_RGB);
    final Graphics2D gHeightImage = imageHeight.createGraphics();

    // draw the height image background
    gHeightImage.setColor(Color.GRAY);
    gHeightImage.fillRect(0, 0, imageTree.getWidth(), imageTree.getHeight());

    final Graphics2D gTree = imageTree.createGraphics();
    final Graphics2D gStone = imageStone.createGraphics();
    final Graphics2D gWater = imageWater.createGraphics();

    final float levelScale = LevelUtil.getLevelScale();

    for (final Entity e : EntityManager.getInstance().getEntities(
        ModifyTerrainComponent.class)) {
      if (e.hasComponent(VisibleComponent.class)) {
        final PositionComponent posComp = e
            .getComponent(PositionComponent.class);

        if (posComp != null) {

          final Point p = LevelUtil.convertToLevel(posComp.getLocation(),
              levelScale);

          final ModifyTerrainComponent modifyComp = e
              .getComponent(ModifyTerrainComponent.class);

          if (modifyComp.isForest()) {
            gTree
                .setColor(new Color(0f, 1f, 0f, rand.nextFloat() * 0.8f + 0.1f));
            gTree.fillRect((p.x * scale) - 1, (p.y * scale) - 1, scale, scale);

            gHeightImage.setColor(Color.LIGHT_GRAY);

          } else if (modifyComp.isStone()) {
            gStone.setColor(new Color(0f, 0f, 1f,
                rand.nextFloat() * 0.3f + 0.7f));

            final int rndI = rand.nextInt(2) - 1;

            gStone.fillRect((p.x * scale) + rndI, (p.y * scale) + rndI, scale
                + (rndI * 2), scale + (rndI * 2));

            gHeightImage.setColor(Color.DARK_GRAY);

          } else if (modifyComp.isWater()) {
            gWater.setColor(new Color(rand.nextFloat() * 0.3f + 0.6f, 0f, 0f));

            gWater.fillRect((p.x * scale) - 3, (p.y * scale) - 3, scale - 1,
                scale - 1);

            gHeightImage.setColor(Color.BLACK);

          }

          gHeightImage.fillRect((p.x * scale) - 1, (p.y * scale) - 1, scale,
              scale);

        }
      }
    }

    // cleanup
    gHeightImage.dispose();
    gTree.dispose();
    gStone.dispose();
    gWater.dispose();

    // blur the heightmap
    imageHeight = ImageUtils.createGaussianBlurFilter(3, 3).filter(imageHeight,
        null);

    // blur the stone and tree images
    final BufferedImage imageTreeBlurred = ImageUtils.createGaussianBlurFilter(
        4, 4).filter(imageTree, null);
    final BufferedImage imageStoneBlurred = ImageUtils
        .createGaussianBlurFilter(4, 4).filter(imageStone, null);

    // create the combined image
    final BufferedImage combined = new BufferedImage(imageTree.getWidth(),
        imageTree.getHeight(), BufferedImage.TYPE_INT_ARGB);

    final BufferedImage imageWaterBlurred = ImageUtils
        .createGaussianBlurFilter(4, 4).filter(imageWater, null);

    // draw the combined image
    final Graphics2D gCombined = combined.createGraphics();

    gCombined.setColor(new Color(0.6f, 0f, 0f, 0.8f));
    gCombined.fillRect(0, 0, imageTree.getWidth(), imageTree.getHeight());

    gCombined.setComposite(AlphaComposite.SrcOver);

    gCombined.drawImage(imageStoneBlurred, 0, 0, null);
    gCombined.drawImage(imageTreeBlurred, 0, 0, null);

    gCombined.dispose();

    final WritableRaster raster = combined.getRaster();
    for (int x = 0; x < raster.getWidth(); x++) {
      for (int y = 0; y < raster.getWidth(); y++) {
        final int[] pixel = new int[4];
        raster.getPixel(x, y, pixel);
        pixel[3] = rand.nextInt(64);
        raster.setPixel(x, y, pixel);
      }
    }

    if (DEBUG) {
      // save the images for debug purposes
      try {
        ImageIO.write(combined, "PNG", new File("level.png"));
      } catch (final IOException e1) {
        e1.printStackTrace();
      }

      try {
        ImageIO.write(imageHeight, "PNG", new File("level_height.png"));
      } catch (final IOException e1) {
        e1.printStackTrace();
      }

      try {
        ImageIO.write(imageWaterBlurred, "PNG", new File("level_water.png"));
      } catch (final IOException e1) {
        e1.printStackTrace();
      }
    }

    // convert the images
    final AWTLoader loader = new AWTLoader();

    heightMap = loader.load(imageHeight, true);
    alphaMapWater = loader.load(imageWaterBlurred, true);
    alphaMapTerrain = loader.load(combined, true);
  }

  Image heightMap;
  Image alphaMapWater;
  Image alphaMapTerrain;
}
