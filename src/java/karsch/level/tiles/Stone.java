package karsch.level.tiles;

import karsch.Values;
import karsch.resources.TextureCache;

import com.jme3.bounding.BoundingBox;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Matrix3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Sphere;
import com.jme3.texture.Texture;

// TODO check material
public class Stone extends Tile {
  public Stone(final int x, final int y) {
    super("stone" + String.valueOf(x) + String.valueOf(y), x, y);
    final Geometry geo = new Geometry("stone" + String.valueOf(x)
        + String.valueOf(y), new Sphere(5, 5, 2.5f));

    attachChild(geo);

    // TextureState
    // ts=DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
    //
    // ts.setTexture(TextureManager.loadTexture(
    // ResourceLocatorTool.locateResource(ResourceLocatorTool.TYPE_TEXTURE,
    // "DIRT.PNG"),
    // Texture.MinificationFilter.BilinearNearestMipMap,
    // Texture.MagnificationFilter.Bilinear),0);
    //
    //
    // ts.setEnabled(true);
    // setRenderState(ts);
    //
    // MaterialState ms =
    // DisplaySystem.getDisplaySystem().getRenderer().createMaterialState();
    // ms.setDiffuse(new ColorRGBA(.7f,.7f,.7f,1f));
    // ms.setEnabled(true);
    //
    // setRenderState(ms);
    //
    // updateRenderState();

    final Material mat = new Material(Values.getInstance().getAssetManager(),
        "Common/MatDefs/Light/Lighting.j3md");

    mat.setFloat("Shininess", 0.0f);
    mat.setBoolean("UseMaterialColors", true);
    mat.setColor("Specular", ColorRGBA.White);
    mat.setColor("Ambient", new ColorRGBA(.7f, .7f, .7f, 1f));
    mat.setColor("Diffuse", new ColorRGBA(.7f, .7f, .7f, 1f));

    final Texture t = TextureCache.getInstance().getTexture("DIRT.PNG");
    mat.setTexture("DiffuseMap", t);

    geo.setMaterial(mat);

    geo.setModelBound(new BoundingBox());
    geo.updateModelBound();
    setLocalRotation(new Matrix3f(0, FastMath.rand.nextFloat(), 0, 0, 0,
        FastMath.rand.nextFloat(), 0, 0, FastMath.rand.nextFloat()));

    // J3OHelper.save(geo, "stone");

    setLocalTranslation(x * 5 + 2.5f, 0, y * 5 + 2.5f);

    lockAll();
  }
}
