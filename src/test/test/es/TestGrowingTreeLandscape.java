package test.es;

import karsch.Values;
import karsch.level.Level.LevelStyle;
import karsch.level.tiles.Floor;
import karsch2.core.Item;
import karsch2.es.component.PositionComponent;
import karsch2.es.system.AnimationSystem;
import karsch2.es.system.MovementSystem;
import karsch2.es.system.SpatialRegistry;
import karsch2.es.system.VisibilitySystem;
import karsch2.es.system.VisualRepSystem;
import karsch2.io.in.ItemUtil;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.ClasspathLocator;
import com.jme3.light.DirectionalLight;
import com.jme3.light.PointLight;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.util.SkyFactory;

import es.core.entity.Entity;
import es.core.entity.EntityManager;
import es.core.system.ESystem;
import es.core.world.EWorld;

public class TestGrowingTreeLandscape extends SimpleApplication {

  @Override
  public void simpleInitApp() {
    getContext().setTitle("Karsch the Pig 2");

    initDeprecated();
    initES();
  }

  private void initDeprecated() {
    final String[] assetPaths = new String[] { "karsch/resource/textures/",
        "karsch/resource/levels/", "karsch/resource/models/",
        "karsch/resource/sound/", "karsch/resource/sound/speech/gunther/",
        "karsch/resource/sound/speech/karsch/",
        "karsch/resource/sound/speech/mrskarsch/", "model/bin/",
        "model/ogre/tree1/", "model/ogre/fence1/", "model/ogre/house_inside/",
        "model/ogre/house/", "model/ogre/mrskarsch/", "model/ogre/hay/",
        "karsch/resource/textures/" };

    for (final String path : assetPaths) {
      assetManager.registerLocator(path, ClasspathLocator.class);
    }

    Values.getInstance().setAssetManager(assetManager);

    // rootNode.addLight(new AmbientLight());
    //
    final PointLight pl = new PointLight();
    pl.setPosition(new Vector3f(0f, 15f, 0f));
    pl.setRadius(50f);
    rootNode.addLight(pl);

    final DirectionalLight dl = new DirectionalLight();
    dl.setDirection(new Vector3f(1f, 2f, 1f).negate());
    rootNode.addLight(dl);

    rootNode.attachChild(SkyFactory.createSky(assetManager,
        "Textures/Sky/Bright/BrightSky.dds", false));

    rootNode.attachChild(new Floor(500, 500, LevelStyle.LS_FORREST));

    cam.setLocation(new Vector3f(0f, 3f, 5f));
    flyCam.setMoveSpeed(20f);
  }

  private void initES() {
    final SpatialRegistry spatialRegistry = new SpatialRegistry();

    final EWorld w = new EWorld();
    w.addSystem(new VisibilitySystem(this, 200f));
    w.addSystem(new VisualRepSystem(this, spatialRegistry, rootNode));
    w.addSystem(new MovementSystem(this, spatialRegistry));
    w.addSystem(new AnimationSystem(this, spatialRegistry));
    w.addSystem(new MassiveTreeCreationSystem());

    w.setUpdatesPerSecond(25);
    w.start();
  }

  public static void main(final String[] args) {
    final TestGrowingTreeLandscape app = new TestGrowingTreeLandscape();
    app.start();
  }

  private static class MassiveTreeCreationSystem implements ESystem {

    private float time;
    private int radius = 1;

    @Override
    public void process(final float tpf) {
      time += tpf;
      if (time > 1f) {
        time = 0f;

        final Item item = ItemUtil.getItem("tree1");

        radius += 8;

        final int nSteps = (int) (radius * FastMath.PI / 10f);

        for (int i = 0; i < nSteps + 1; i++) {

          final float theta = 360f * i / nSteps;
          final float theta_radian = theta * FastMath.PI / 180f;

          final float x = (float) (radius * Math.cos(theta_radian));
          final float y = (float) (radius * Math.sin(theta_radian));

          final Entity e = EntityManager.getInstance().createEntity();
          e.addComponents(item.getComponents());
          e.addComponent(new PositionComponent(new Vector3f(x, 0f, y),
              Quaternion.ZERO));
        }

      }
    }
  }
}
