package test.io;

import java.util.Random;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.light.AmbientLight;
import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.LodControl;
import com.jme3.scene.shape.Box;

/**
 * This is a test class for loading a Ogre XML scene exported by OgreMax.
 * 
 * @author Stephan Dreyer
 * 
 */
public class TestOgreMaxImport extends SimpleApplication {

  @Override
  public void simpleInitApp() {
    assetManager.registerLocator("Assets/model/ogre/test/", FileLocator.class);

    // create the geometry and attach it
    final Node model = (Node) assetManager.loadModel("karsch_dance.scene");
    // resize it, because of the large 3dsmax scales
    model.setLocalScale(.02f);

    // attach to root node

    for (int x = 0; x < 8; x++) {
      for (int y = 0; y < 1; y++) {
        final Node n = model.clone(false);
        n.setLocalTranslation(x * 2f, 0f, y * 2f);
        addLodControl(n);
        findAnimControl(n);
        rootNode.attachChild(n);
      }
    }

    flyCam.setMoveSpeed(40f);
    cam.setLocation(new Vector3f(15, 10, 15));
    cam.lookAt(Vector3f.ZERO, Vector3f.UNIT_Y);
    cam.setFrustumNear(1f);

    // add some lights
    rootNode.addLight(new AmbientLight());

    final PointLight pl = new PointLight();
    pl.setPosition(new Vector3f(-3f, 3f, 1f));
    rootNode.addLight(pl);

    // add a box as floor
    final Box b = new Box(100f, 0.1f, 100f);
    final Geometry geo = new Geometry("floor", b);

    final Material mat = new Material(assetManager,
        "Common/MatDefs/Misc/Unshaded.j3md");
    mat.setColor("Color", ColorRGBA.LightGray);
    geo.setMaterial(mat);

    rootNode.attachChild(geo);
  }

  /**
   * Method to traverse through the scene graph and add a {@link LodControl} to
   * the mesh.
   * 
   * @param parent
   *          The Node to add the control to.
   */
  public static void addLodControl(final Spatial parent) {
    if (parent instanceof Node) {
      for (final Spatial s : ((Node) parent).getChildren()) {
        addLodControl(s);
      }
    } else if (parent instanceof Geometry) {
      final LodControl lc = new LodControl();
      lc.setTrisPerPixel(0.1f);

      // the distance for LOD changes is set here, you may adjust this
      lc.setDistTolerance(1f);
      parent.addControl(lc);
    }
  }

  /**
   * Method to find the animation control, because it is not on the models root
   * node.
   * 
   * @param parent
   *          The spatial to search.
   * @return The {@link AnimControl} or null if it does not exist.
   */
  public AnimControl findAnimControl(final Spatial parent) {
    final AnimControl animControl = parent.getControl(AnimControl.class);
    if (animControl != null) {
      return animControl;
    }

    if (parent instanceof Node) {
      for (final Spatial s : ((Node) parent).getChildren()) {
        final AnimControl animControl2 = findAnimControl(s);
        // create a channel and start the walk animation
        final AnimChannel channel = animControl2.createChannel();
        channel.setAnim("walk");
        channel.setSpeed(1f + new Random().nextFloat() * 0.1f);
        if (animControl2 != null) {
          return animControl2;
        }
      }
    }

    return null;
  }

  public static void main(final String[] args) {
    new TestOgreMaxImport().start();
  }
}
