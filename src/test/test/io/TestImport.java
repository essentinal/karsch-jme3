package test.io;

import karsch.utils.NodeUtils;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.Skeleton;
import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.light.AmbientLight;
import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.debug.SkeletonDebugger;

public class TestImport extends SimpleApplication {

  public TestImport() {

    setShowSettings(false);

  }

  @Override
  public void simpleInitApp() {
    convert("karsch_dance");
  }

  private void convert(final String name) {
    assetManager.registerLocator("Assets/model/ogre/test/", FileLocator.class);

    // create the geometry and attach it
    final Node model1 = (Node) assetManager.loadModel(name + ".scene");

    // model1.setLocalScale(new Vector3f(1f / 12f, 1f / 12f, 1f / 12f));
    // model1.center();

    model1.scale(0.1f);

    // TestOgreMaxImport.addLodControl(model1);

    // model1.setLocalScale(.1f);

    // J3OHelper.save(model1, name);

    // assetManager.registerLocator("Assets/model/bin/", FileLocator.class);
    //
    // final Node model2 = (Node) assetManager.loadModel(name + ".j3o");

    final AnimControl ac = NodeUtils.findAnimControl(model1);

    try {
      final Skeleton skel = ac.getSkeleton();

      // add the skeleton debugger to the node with the anim control

      final SkeletonDebugger skeletonDebug = new SkeletonDebugger("skeleton",
          skel);
      final Material mat = new Material(assetManager,
          "Common/MatDefs/Misc/Unshaded.j3md");
      mat.setColor("Color", ColorRGBA.Green);
      mat.getAdditionalRenderState().setDepthTest(false);
      skeletonDebug.setMaterial(mat);
      ((Node) ac.getSpatial()).attachChild(skeletonDebug);

    } catch (final Exception e) {
      e.printStackTrace();
    }

    rootNode.attachChild(model1);

    try {

      final AnimChannel channel = ac.createChannel();
      channel.setAnim("walk");
    } catch (final Exception e) {
      e.printStackTrace();
    }

    flyCam.setMoveSpeed(40f);

    rootNode.addLight(new AmbientLight());

    final PointLight pl = new PointLight();
    pl.setRadius(25f);
    pl.setPosition(new Vector3f(10, 5, 10));
    rootNode.addLight(pl);
  }

  public static void main(final String[] args) {
    new TestImport().start();
  }
}
