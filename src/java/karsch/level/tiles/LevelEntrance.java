package karsch.level.tiles;

import java.awt.image.BufferedImage;

import karsch.Values;
import karsch.characters.Karsch;
import karsch.controller.NPCController.Direction;
import karsch.interfaces.Collectable;
import karsch.interfaces.KarschPassable;
import karsch.level.LevelManager;
import karsch.level.LevelText;
import karsch.resources.ModelCache;
import karsch.resources.TextureCache;
import karsch2.io.out.J3OHelper;

import com.jme3.bounding.BoundingBox;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

// TODO add animation control
public class LevelEntrance extends Tile implements KarschPassable, Collectable {
  private final int levelOffset;
  private Direction direction = Direction.DIRECTION_NODIR;
  private boolean isStartPos = false;
  private String itemName;

  public LevelEntrance(final int x, final int y,
      final BufferedImage levelImage, final int levelOffset,
      final LevelText levelText, final Vector3f center, final boolean isStartPos) {
    super("leveloffset " + levelOffset, x, y);
    this.isStartPos = isStartPos;
    this.levelOffset = levelOffset;

    if (levelOffset > 0) {
      direction = levelText.getRotationExit();
      itemName = "exit";
    } else {
      direction = levelText.getRotationEntrance();
      itemName = "entrance";
    }

    if (!isStartPos) {
      model = ModelCache.getInstance().get("sign1.3ds");

      final Material mat = new Material(Values.getInstance().getAssetManager(),
          "Common/MatDefs/Light/Lighting.j3md");

      mat.setFloat("Shininess", 5.0f);
      mat.setBoolean("UseMaterialColors", true);
      mat.setColor("Ambient", new ColorRGBA(.7f, .7f, .7f, 1f));
      mat.setColor("Diffuse", new ColorRGBA(.7f, .7f, .7f, 1f));
      mat.setColor("Specular", ColorRGBA.White);
      mat.setTexture("DiffuseMap",
          TextureCache.getInstance().getTexture("SIGN1.PNG"));

      model.setMaterial(mat);

      J3OHelper.save(model, itemName);

      // model.setLocalScale(.05f);
      attachChild(model);
      setModelBound(new BoundingBox());
      updateModelBound();

      // TODO
      // final SpatialTransformer trans = (SpatialTransformer) model
      // .getController(0);
      // trans.setRepeatType(Controller.RT_WRAP);
      // trans.setActive(true);

      if (direction == Direction.DIRECTION_RIGHT) {
        getLocalRotation().fromAngleAxis(FastMath.PI + 0.2f, Vector3f.UNIT_Y);
        setLocalTranslation(x * 5 + 4.5f, -.5f, y * 5 + 1.0f);
      } else if (direction == Direction.DIRECTION_LEFT) {
        setLocalTranslation(x * 5 + 0.5f, -.5f, y * 5 + 1.0f);
        getLocalRotation().fromAngleAxis(-0.2f, Vector3f.UNIT_Y);
      } else if (direction == Direction.DIRECTION_DOWN) {
        setLocalTranslation(x * 5 + 4.5f, -.5f, y * 5 + 4.5f);
        getLocalRotation().fromAngleAxis(FastMath.HALF_PI + 0.3f,
            Vector3f.UNIT_Y);
      } else if (direction == Direction.DIRECTION_UP) {
        getLocalRotation().fromAngleAxis(-FastMath.HALF_PI + 0.3f,
            Vector3f.UNIT_Y);
        setLocalTranslation(x * 5 + 4.5f, -.5f, y * 5 + 0.5f);
      }
    } else {
      setLocalTranslation(x * 5 + 4.5f, -.5f, y * 5 + 4.5f);
      getLocalRotation()
          .fromAngleAxis(FastMath.HALF_PI + 0.3f, Vector3f.UNIT_Y);
    }

  }

  public String getItemName() {
    return itemName;
  }

  @Override
  public void collect(final Node source) {
    if (source instanceof Karsch && !isStartPos) {
      if (levelOffset > 0) {
        System.out.println("ENTRANCE: level +" + levelOffset);
      } else {
        System.out.println("ENTRANCE: level " + levelOffset);
      }

      final Karsch karsch = Values.getInstance().getLevelGameState()
          .getKarsch();
      karsch.setBabiesFound(0);
      LevelManager.getInstance().loadLevel(levelOffset, this);

      karsch.getController().setLastDirection(direction);

    }
  }

  public Vector3f getDirectionVector() {
    if (direction == Direction.DIRECTION_RIGHT)
      return new Vector3f(-1, 0, 0);
    else if (direction == Direction.DIRECTION_LEFT)
      return new Vector3f(1, 0, 0);
    else if (direction == Direction.DIRECTION_DOWN)
      return new Vector3f(0, 0, -1);
    else
      return new Vector3f(0, 0, 1);
  }

  public Direction getDirection() {
    return direction;
  }

  @Override
  public boolean canPass() {
    return true;
  }

  public int getLevelOffset() {
    return levelOffset;
  }

}
