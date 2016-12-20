package karsch.level.tiles;

import karsch.Values;
import karsch.level.Level.LevelStyle;
import karsch.resources.TextureCache;
import karsch2.io.out.J3OHelper;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;

@SuppressWarnings("serial")
public class Floor extends Tile {
  private String itemName;

  private final Geometry geo;

  public Floor(final int xSize, final int ySize, final LevelStyle levelStyle) {
    super("floor");

    final Box b = new Box(new Vector3f(-2.5f, -.1f, -2.5f), new Vector3f(2.5f,
        0, 2.5f));
    geo = new Geometry("floor", b);
    attachChild(geo);

    // final TextureState ts = DisplaySystem.getDisplaySystem().getRenderer()
    // .createTextureState();

    Texture t = null;

    if (levelStyle == LevelStyle.LS_FARM) {
      t = TextureCache.getInstance().getTexture("GRASS2.PNG");
      itemName = "floor_grass";
    } else if (levelStyle == LevelStyle.LS_FORREST) {
      t = TextureCache.getInstance().getTexture("GRASS2_DARK.PNG");
      itemName = "floor_grass_dark";
    } else {
      t = TextureCache.getInstance().getTexture("DIRT2.PNG");
      itemName = "floor_soil";
    }

    t.setWrap(WrapMode.Repeat);

    final Material mat = new Material(Values.getInstance().getAssetManager(),
        "Common/MatDefs/Light/Lighting.j3md");

    mat.setFloat("Shininess", 0.0f);
    mat.setBoolean("UseMaterialColors", true);
    mat.setColor("Ambient", ColorRGBA.Black);
    mat.setColor("Diffuse", ColorRGBA.White.mult(0.6f));
    // mat.setColor("Specular", ColorRGBA.White);
    mat.setTexture("DiffuseMap", t);

    // TODO check if this works
    b.scaleTextureCoordinates(new Vector2f(xSize, ySize));

    geo.setMaterial(mat);

    if (xSize > 1 || ySize > 1) {
      new Floor(1, 1, levelStyle).save();
    }

    scale(xSize, 1f, ySize);

    // setLocalTranslation(-xSize * 2.5f, 0, -ySize * 2.5f);
    lockAll();
  }

  public String getItemName() {
    return itemName;
  }

  private void save() {
    J3OHelper.save(geo, itemName);
  }
}
