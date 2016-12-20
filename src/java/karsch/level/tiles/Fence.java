package karsch.level.tiles;

import java.awt.image.BufferedImage;

import karsch.controller.NPCController.Direction;
import karsch.resources.ModelCache;

import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;

@SuppressWarnings("serial")
public class Fence extends Tile {
  private boolean left = false;
  private boolean right = false;
  private boolean up = false;
  private boolean down = false;
  private Direction direction;
  private String itemName;

  public Fence(final int x, final int y, final BufferedImage levelImage) {
    super("fence" + x + "" + y, x, y, levelImage);
    detectFence(levelImage);

    lockAll();
  }

  private void detectFence(final BufferedImage levelImage) {
    final int actPixel = levelImage.getRGB(x, y);

    if ((x > 0) && (levelImage.getRGB(x - 1, y) == actPixel)) {
      left = true;
    }
    if ((y > 0) && (levelImage.getRGB(x, y - 1) == actPixel)) {
      up = true;
    }
    if ((x <= levelImage.getWidth())
        && (levelImage.getRGB(x + 1, y) == actPixel)) {
      right = true;
    }
    if ((y <= levelImage.getHeight())
        && (levelImage.getRGB(x, y + 1) == actPixel)) {
      down = true;
    }
    createFence();
  }

  public String getConnectionString() {
    String s = "";
    if (up) {
      s += " u";
    }
    if (right) {
      s += " r";
    }
    if (down) {
      s += " d";
    }
    if (left) {
      s += " l";
    }

    return s.trim();
  }

  public Direction getDirection() {
    return direction;
  }

  public String getItemName() {
    return itemName;
  }

  private void createFence() {
    if (left && right && !up && !down) {
      itemName = "fence1";
      direction = Direction.DIRECTION_DOWN;
      model = ModelCache.loadBin("fence1.j3o");
      model.setLocalScale(.025f);
      attachChild(model);
      setLocalTranslation(x * 5f + 2.5f, 0, y * 5 + 2.5f);
    } else if (!left && !right && up && down) {
      itemName = "fence1";
      direction = Direction.DIRECTION_RIGHT;
      model = ModelCache.loadBin("fence1.j3o");
      model.setLocalScale(.025f);
      attachChild(model);
      getLocalRotation().fromAngleAxis(FastMath.PI / 2, Vector3f.UNIT_Y);
      setLocalTranslation(x * 5f + 2.5f, 0, y * 5 + 2.5f);
    } else if (!left && right && !up && down) {
      itemName = "fence1_edge";
      direction = Direction.DIRECTION_LEFT;
      model = ModelCache.loadBin("fence1_edge.j3o");
      model.setLocalScale(.025f);
      attachChild(model);
      getLocalRotation().fromAngleAxis(-FastMath.PI / 2, Vector3f.UNIT_Y);
      setLocalTranslation(x * 5f + 2.5f, 0, y * 5 + 2.5f);
    } else if (!left && right && up && !down) {
      itemName = "fence1_edge";
      direction = Direction.DIRECTION_DOWN;
      model = ModelCache.loadBin("fence1_edge.j3o");
      model.setLocalScale(.025f);
      attachChild(model);
      setLocalTranslation(x * 5f + 2.5f, 0, y * 5 + 2.5f);
    } else if (left && !right && !up && down) {
      itemName = "fence1_edge";
      direction = Direction.DIRECTION_UP;
      model = ModelCache.loadBin("fence1_edge.j3o");
      model.setLocalScale(.025f);
      attachChild(model);
      getLocalRotation().fromAngleAxis(FastMath.PI, Vector3f.UNIT_Y);
      setLocalTranslation(x * 5f + 2.5f, 0, y * 5 + 2.5f);
    } else if (left && !right && up && !down) {
      itemName = "fence1_edge";
      direction = Direction.DIRECTION_RIGHT;
      model = ModelCache.loadBin("fence1_edge.j3o");
      model.setLocalScale(.025f);
      attachChild(model);
      getLocalRotation().fromAngleAxis(-FastMath.PI / 2f * 3f, Vector3f.UNIT_Y);
      setLocalTranslation(x * 5f + 2.5f, 0, y * 5 + 2.5f);
    } else if ((left && !right && !up && !down)
        || (!left && right && !up && !down)) {
      itemName = "fence1";
      direction = Direction.DIRECTION_DOWN;
      model = ModelCache.loadBin("fence1.j3o");
      model.setLocalScale(.025f);
      attachChild(model);
      setLocalTranslation(x * 5f + 2.5f, 0, y * 5 + 2.5f);
    } else if ((!left && !right && up && !down)
        || (!left && !right && !up && down)) {
      itemName = "fence1";
      direction = Direction.DIRECTION_LEFT;
      model = ModelCache.loadBin("fence1.j3o");
      model.setLocalScale(.025f);
      attachChild(model);
      getLocalRotation().fromAngleAxis(FastMath.PI / 2, Vector3f.UNIT_Y);
      setLocalTranslation(x * 5f + 2.5f, 0, y * 5 + 2.5f);
    }
  }
}