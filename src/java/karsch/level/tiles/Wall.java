package karsch.level.tiles;

import karsch.Values;
import karsch.resources.TextureCache;

import com.jme3.bounding.BoundingBox;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;

@SuppressWarnings("serial")
public class Wall extends Tile {
  public Wall(final int x, final int y) {
    this(x, y, false);
  }

  public Wall(final int x, final int y, final boolean black) {
    super("wall" + String.valueOf(x) + String.valueOf(y), x, y);
    final Geometry geo = new Geometry("wall" + String.valueOf(x)
        + String.valueOf(y), new Box(new Vector3f(x * 5, 0, y * 5),
        new Vector3f(x * 5 + 5f, 5f, y * 5 + 5f)));
    attachChild(geo);

    final Material mat = new Material(Values.getInstance().getAssetManager(),
        "Common/MatDefs/Light/Lighting.j3md");

    mat.setFloat("Shininess", 5.0f);
    mat.setBoolean("UseMaterialColors", true);
    mat.setColor("Specular", ColorRGBA.Gray);

    if (black) {
      mat.setColor("Ambient", ColorRGBA.Gray);
      mat.setColor("Diffuse", ColorRGBA.Black);

    } else {

      mat.setColor("Ambient", ColorRGBA.Gray);
      mat.setColor("Diffuse", ColorRGBA.White);

      final Texture t = TextureCache.getInstance().getTexture("DIRT.PNG");
      mat.setTexture("DiffuseMap", t);

    }

    geo.setMaterial(mat);
    geo.setModelBound(new BoundingBox());

    // J3OHelper.save(geo, black ? "blackwall" : "wall");

    updateModelBound();

    lockAll();
  }
}
