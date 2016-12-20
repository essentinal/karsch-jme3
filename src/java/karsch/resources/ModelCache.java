package karsch.resources;

import java.util.HashMap;

import karsch.Values;

import com.jme3.asset.plugins.FileLocator;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;

// TODO replace mock loading by loading of models
// TODO add real model caching, if necessary
public class ModelCache {
  private static ModelCache instance;
  // private final CloneImportExport ie;
  private final HashMap<String, Spatial> models;

  private ModelCache() {
    // ie = new CloneImportExport();
    models = new HashMap<String, Spatial>();
  }

  public static ModelCache getInstance() {
    if (instance == null) {
      instance = new ModelCache();
    }
    return instance;
  }

  public Spatial get(final String name) {
    Spatial node = models.get(name);
    if (node == null) {
      node = load3ds(name);
    } else {
      System.out.println("clone of " + name + " loaded from cache");
    }

    // puts a new clone in the hashmap // no it doesnt
    // ie.saveClone(node);
    // models.put(name, node);

    return node;
  }

  // public static Spatial loadJME(final String modelPath) {
  // try {
  // String outfile = modelPath;
  // if (!outfile.endsWith(".jme")) {
  // outfile = modelPath.substring(0, modelPath.indexOf('.')) + ".jme";
  // }
  //
  // Node r;
  //
  // final BinaryImporter bi = BinaryImporter.getInstance();
  // BinaryImporter.debug = true;
  //
  // System.out.println(ResourceLocatorTool.locateResource(
  // ResourceLocatorTool.TYPE_MODEL, outfile) + " loaded");
  // r = (Node) bi.load(ResourceLocatorTool.locateResource(
  // ResourceLocatorTool.TYPE_MODEL, outfile));
  //
  // final Quaternion temp = new Quaternion();
  // temp.fromAngleAxis(FastMath.HALF_PI, new Vector3f(-1, 0, 0));
  // r.setLocalRotation(temp);
  // r.setIsCollidable(true);
  // r.setModelBound(new BoundingBox());
  // r.updateModelBound();
  // r.updateRenderState();
  //
  // return r;
  // } catch (final IOException e) {
  // e.printStackTrace();
  // System.exit(1);
  // return null;
  // }
  // }

  public static Spatial loadBin(final String modelPath) {
    Values.getInstance().getAssetManager()
        .registerLocator("Assets/model/bin/", FileLocator.class);
    return Values.getInstance().getAssetManager().loadModel(modelPath);
  }

  public static Spatial load3ds(final String modelPath) {

    // TODO no implementation yet, return a MOCK object
    final Geometry geo = new Geometry(modelPath, new Box(1f, 1f, 1f));

    // //////////
    // TODO workaround because not material set
    final Material mat = new Material(Values.getInstance().getAssetManager(),
        "Common/MatDefs/Light/Lighting.j3md");

    mat.setFloat("Shininess", 4.0f);
    mat.setBoolean("UseMaterialColors", true);
    mat.setColor("Ambient", new ColorRGBA(.7f, .7f, .7f, 1f));
    mat.setColor("Diffuse", new ColorRGBA(.7f, .7f, .7f, 1f));
    mat.setColor("Specular", ColorRGBA.Black);

    geo.setMaterial(mat);
    // ///////////

    return geo;

    // try {
    // final URL model = ResourceLocatorTool.locateResource(
    // ResourceLocatorTool.TYPE_MODEL, modelPath);
    //
    // final MaxToJme converter = new MaxToJme();
    // final ByteArrayOutputStream BO = new ByteArrayOutputStream();
    //
    // converter.setProperty("texurl", model);
    // converter.setProperty("mtllib", model);
    // System.out.println(Thread.currentThread().getContextClassLoader()
    // .getResource("karsch/resource/textures"));
    // converter.convert(model.openStream(), BO);
    //
    // final Node r = (Node) BinaryImporter.getInstance().load(
    // new ByteArrayInputStream(BO.toByteArray()));
    //
    // final Quaternion temp = new Quaternion();
    // temp.fromAngleAxis(FastMath.HALF_PI, new Vector3f(-1, 0, 0));
    // r.setLocalRotation(temp);
    // r.setIsCollidable(true);
    // r.setModelBound(new BoundingBox());
    // r.updateModelBound();
    // r.updateRenderState();
    //
    // return r;
    //
    // } catch (final IOException e) {
    // e.printStackTrace();
    // return null;
    // }
  }

  // public static void save(Spatial r, String outfile) throws IOException{
  // BinaryExporter.getInstance().save((Savable)r, new
  // File("src/karsch/resource/models/"+outfile));
  // }
}
