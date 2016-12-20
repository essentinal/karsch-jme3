package karsch2;

import karsch2.es.system.AnimationSystem;
import karsch2.es.system.InputSystem;
import karsch2.es.system.JMESystem;
import karsch2.es.system.LevelSystem;
import karsch2.es.system.MovementSystem;
import karsch2.es.system.SpatialRegistry;
import karsch2.es.system.TerrainSystem;
import karsch2.es.system.VisualRepSystem;
import karsch2.es.system.env.SkySystem;
import karsch2.es.system.interaction.CollisionSystem;
import karsch2.es.system.interaction.MarkerSystem;
import karsch2.io.in.LevelLoader;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.ClasspathLocator;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.light.PointLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;

import es.core.world.EWorld;

public class Karsch2 extends SimpleApplication {

  @Override
  public void simpleInitApp() {
    getContext().setTitle("Karsch the Pig 2");

    initLoaders();
    initDeprecated();
    initES();
  }

  boolean deleteFlyCam = true;

  @Override
  public void simpleUpdate(final float tpf) {
    if (deleteFlyCam) {
      try {
        if (inputManager.hasMapping("FLYCAM_Left")) {

          inputManager.addMapping("FLYCAM_Left", new MouseAxisTrigger(
              MouseInput.AXIS_X, true));
          inputManager.addMapping("FLYCAM_Right", new MouseAxisTrigger(
              MouseInput.AXIS_X, false));
          inputManager.addMapping("FLYCAM_Up", new MouseAxisTrigger(
              MouseInput.AXIS_Y, false));
          inputManager.addMapping("FLYCAM_Down", new MouseAxisTrigger(
              MouseInput.AXIS_Y, true));

          deleteFlyCam = false;
        }
      } catch (final Exception ex) {

      }
    }
  }

  private void initDeprecated() {
    final String[] assetPaths = new String[] { "karsch/resource/textures/",
        "karsch/resource/levels/", "karsch/resource/models/",
        "karsch/resource/sound/", "karsch/resource/sound/speech/gunther/",
        "karsch/resource/sound/speech/karsch/",
        "karsch/resource/sound/speech/mrskarsch/", "model/bin/",
        "model/ogre/tree1/", "model/ogre/fence1/", "model/ogre/house_inside/",
        "model/ogre/house/", "model/ogre/mrskarsch/", "model/ogre/hay/",
        "model/ogre/bridge1/", };

    for (final String path : assetPaths) {
      assetManager.registerLocator(path, ClasspathLocator.class);
    }

    final PointLight l = new PointLight();
    l.setColor(ColorRGBA.White);
    l.setRadius(25f);
    l.setPosition(new Vector3f(0, 5, 0));
    rootNode.addLight(l);
    final AmbientLight al = new AmbientLight();
    al.setColor(ColorRGBA.White.mult(0.5f));
    rootNode.addLight(al);
    final DirectionalLight dl = new DirectionalLight();
    dl.setDirection(new Vector3f(5, 15, 5).negate());
    rootNode.addLight(dl);

    flyCam.setMoveSpeed(20f);

    // flyCam.setEnabled(false);
    cam.setLocation(new Vector3f(50, 50, 100));
    cam.lookAt(new Vector3f(50, 1, 70), Vector3f.UNIT_Y);
  }

  private void initLoaders() {
    assetManager.registerLoader(LevelLoader.class, "level.xml");
  }

  private void initES() {
    final SpatialRegistry spatialRegistry = new SpatialRegistry();

    final EWorld w = new EWorld();
    w.addSystem(new VisualRepSystem(this, spatialRegistry, rootNode));
    w.addSystem(new MovementSystem(this, spatialRegistry));
    w.addSystem(new AnimationSystem(this, spatialRegistry));
    w.addSystem(new InputSystem(inputManager));
    w.addSystem(new LevelSystem(assetManager));
    w.addSystem(new CollisionSystem());
    w.addSystem(new MarkerSystem());

    // optional this is buggy
    // w.addSystem(new VisibilitySystem(this, 200f));

    // addJMESystem(new CameraSystem(cam, spatialRegistry));
    addJMESystem(new TerrainSystem(assetManager, spatialRegistry, cam, rootNode));
    // addJMESystem(new WaterSystem(rootNode, assetManager, viewPort));
    addJMESystem(new SkySystem(assetManager, rootNode));
    // addJMESystem(new VegetationSystem(assetManager, rootNode));

    w.setUpdatesPerSecond(25);
    w.start();
  }

  private void addJMESystem(final JMESystem system) {
    rootNode.addControl(system);
  }

  public static void main(final String[] args) {
    final Karsch2 app = new Karsch2();
    app.setShowSettings(false);
    app.start();
  }

}
