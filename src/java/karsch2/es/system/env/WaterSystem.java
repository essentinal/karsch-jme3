package karsch2.es.system.env;

import karsch2.es.component.LevelComponent;
import karsch2.es.component.ModifyTerrainComponent;
import karsch2.es.component.PositionComponent;
import karsch2.es.component.VisibleComponent;
import karsch2.es.system.JMESystem;

import com.jme3.asset.AssetManager;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.texture.Texture2D;
import com.jme3.water.WaterFilter;

import es.core.entity.Entity;
import es.core.entity.EntityManager;

public class WaterSystem extends JMESystem {

  private final Node rootNode;
  private final AssetManager assetManager;
  private final ViewPort viewPort;
  private FilterPostProcessor fpp;
  private WaterFilter water;

  private boolean isInitialized;

  private float time;
  private final float changeSpeedTime = 5f;

  private int levelNumber = -1;

  public WaterSystem(final Node rootNode, final AssetManager assetManager,
      final ViewPort viewPort) {
    this.rootNode = rootNode;
    this.assetManager = assetManager;
    this.viewPort = viewPort;
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

          if (isInitialized) {
            isInitialized = false;

            fpp.removeFilter(water);
            water = null;
            viewPort.removeProcessor(fpp);
            fpp = null;
          }

          if (init()) {
            isInitialized = true;
          }

          levelNumber = levelComp.getLevelNumber();
          break;
        }

      }
    }

    if (isInitialized) {
      time += tpf;
      if (time > changeSpeedTime) {
        time = 0;
        water.setSpeed((FastMath.nextRandomFloat() * 0.4f) + 0.2f);
      }
    }
  }

  private boolean init() {
    float minX = Float.POSITIVE_INFINITY;
    float maxX = Float.NEGATIVE_INFINITY;
    float minY = Float.POSITIVE_INFINITY;
    float maxY = Float.NEGATIVE_INFINITY;

    for (final Entity e : EntityManager.getInstance().getEntities(
        ModifyTerrainComponent.class)) {
      if (e.hasComponent(VisibleComponent.class)
          && e.getComponent(ModifyTerrainComponent.class).isWater()) {
        final PositionComponent posComp = e
            .getComponent(PositionComponent.class);

        if (posComp != null) {
          minX = Math.min(minX, posComp.getLocation().x);
          minY = Math.min(minY, posComp.getLocation().z);

          maxX = Math.max(maxX, posComp.getLocation().x);
          maxY = Math.max(maxY, posComp.getLocation().z);
        }
      }
    }

    if (!Float.isInfinite(minX) && !Float.isInfinite(minY)) {

      final float width = maxX - minX;
      final float height = maxY - minY;

      Vector2f windDir;
      if (width > height) {
        windDir = new Vector2f(1f, 0.5f);
      } else {
        windDir = new Vector2f(0.5f, 1f);
      }

      final Vector3f lightDir = new Vector3f(1f, 0f, 0.1f).negate();

      fpp = new FilterPostProcessor(assetManager);
      water = new WaterFilter(rootNode, lightDir);

      water.setCenter(new Vector3f(minX + (width / 2f), -1f, minY
          + (height / 2f)));
      water.setRadius(Math.max(width, height) / 1.4f);
      water.setWaveScale(0.005f);
      water.setWindDirection(windDir);
      water.setUseFoam(true);
      water.setUseRipples(true);
      water.setWaterColor(ColorRGBA.Blue.mult(2.0f));
      water.setWaterTransparency(0.01f);
      // water.setRefractionConstant(0.2f);
      water.setRefractionStrength(0.2f);
      water.setSpeed(0.3f);
      water.setColorExtinction(new Vector3f(1f, 2f, 3f));

      water.setMaxAmplitude(0.5f);
      water.setFoamExistence(new Vector3f(0.25f, 0.5f, 2.8f));
      water.setReflectionMapSize(256);
      water.setFoamTexture((Texture2D) assetManager
          .loadTexture("Common/MatDefs/Water/Textures/foam2.jpg"));
      water.setWaterHeight(-0.7f);
      fpp.addFilter(water);

      viewPort.addProcessor(fpp);

      // waterProcessor = new SimpleWaterProcessor(assetManager);
      // waterProcessor.setReflectionScene(rootNode);
      // // waterProcessor.setWaterColor(ColorRGBA.Blue);
      // waterProcessor.setWaterTransparency(5f);
      // // waterProcessor.setWaterDepth(2f);
      // // waterProcessor.setDebug(true);
      // viewPort.addProcessor(waterProcessor);
      //
      // // waterProcessor.setLightPosition(lightPos);
      //
      // // create water quad
      // // waterPlane = waterProcessor.createWaterGeometry(100, 100);
      //
      // // waterPlane = assetManager
      // // .loadModel("Models/WaterTest/WaterTest.mesh.xml");
      // // waterPlane.setMaterial(waterProcessor.getMaterial());
      // // waterPlane.setLocalScale(40);
      // // waterPlane.setLocalTranslation(minX, 0f, minY);
      //
      // final float width = maxX - minX;
      // final float height = maxY - minY;
      //
      // final Quad quad = new Quad(width * 2, height * 2);
      //
      // // the texture coordinates define the general size of the waves
      // quad.scaleTextureCoordinates(new Vector2f(width / 10f, height / 10f));
      //
      // waterPlane = new Geometry("water", quad);
      // waterPlane.setShadowMode(ShadowMode.Receive);
      // waterPlane.setLocalRotation(new Quaternion().fromAngleAxis(
      // -FastMath.HALF_PI, Vector3f.UNIT_X));
      // waterPlane.setMaterial(waterProcessor.getMaterial());
      // waterPlane.setLocalTranslation(minX + (width / 2f), -0.2f, minY
      // + (height / 2f));
      //
      // rootNode.attachChild(waterPlane);
      //
      // rootNode.updateGeometricState();

      return true;
    }
    return false;
  }
}
