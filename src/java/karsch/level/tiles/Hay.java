package karsch.level.tiles;

import karsch.controller.NPCController.Direction;
import karsch.resources.ModelCache;

import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;

@SuppressWarnings("serial")
public class Hay extends Tile {
  private String itemName;

  public Hay(final int x, final int y) {
    super("hay" + x + " " + y, x, y);

    loadModel(FastMath.rand.nextInt(3) + 1);
  }

  private void loadModel(final int number) {
    itemName = "hay" + number;
    model = ModelCache.loadBin(itemName + ".j3o");
    model.setLocalScale(.02f);

    final float randX = FastMath.rand.nextFloat();
    final float randY = FastMath.rand.nextFloat();
    setLocalTranslation(new Vector3f(x * 5 + 1.5f + randX, 0, y * 5 + 1.5f
        + randY));
    attachChild(model);
    // setLocalScale(scale + (0.5f*FastMath.rand.nextFloat()) );

    // final Material mat = new Material(Values.getInstance().getAssetManager(),
    // "Common/MatDefs/Misc/Unshaded.j3md");

    // mat.setFloat("Shininess", 0.0f);
    // mat.setBoolean("UseMaterialColors", true);
    // mat.setColor("Ambient", new ColorRGBA(.7f, .7f, .7f, 1f));
    // mat.setColor("Diffuse", new ColorRGBA(.7f, .7f, .7f, 1f));
    // mat.setColor("Specular", ColorRGBA.Black);

    // final Texture t = TextureCache.getInstance().getTexture("HAY.PNG");
    // mat.setTexture("ColorMap", t);
    //
    // model.setMaterial(mat);

    final Direction direction = Direction.parse(FastMath.rand.nextInt(4));

    if (direction == Direction.DIRECTION_LEFT) {
      getLocalRotation()
          .fromAngleAxis(-FastMath.HALF_PI, new Vector3f(0, 1, 0));
    } else if (direction == Direction.DIRECTION_RIGHT) {
      getLocalRotation().fromAngleAxis(FastMath.HALF_PI, new Vector3f(0, 1, 0));
    } else if (direction == Direction.DIRECTION_UP) {
      getLocalRotation().fromAngleAxis(FastMath.PI, new Vector3f(0, 1, 0));
    }

    lockAll();
  }

  public String getItemName() {
    return itemName;
  }
}
