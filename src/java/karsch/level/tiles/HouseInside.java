package karsch.level.tiles;

import java.awt.image.BufferedImage;

import karsch.controller.NPCController.Direction;
import karsch.level.LevelFactory;
import karsch.resources.ModelCache;

import com.jme3.bounding.BoundingBox;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;

@SuppressWarnings("serial")
public class HouseInside extends Tile {
  private boolean newHouse = true;

  public HouseInside(final int x, final int y, final BufferedImage levelImage) {
    super("house" + x + "" + y, x, y, levelImage);

    detectHouse();
    // lockAll();
  }

  private void detectHouse() {
    int xSize = 1;
    int ySize = 1;

    if ((x > 0) && (levelImage.getRGB(x - 1, y) != LevelFactory.COLOR_WALL)) {
      newHouse = false;
    } else if ((y > 0)
        && (levelImage.getRGB(x, y - 1) != LevelFactory.COLOR_WALL)) {
      newHouse = false;
    } else {
      int i = 1;
      while ((x + i <= levelImage.getWidth())
          && (levelImage.getRGB(x + i, y) != LevelFactory.COLOR_WALL)) {
        xSize++;
        i++;
      }
      i = 1;
      while ((y + i <= levelImage.getHeight())
          && (levelImage.getRGB(x, y + i) != LevelFactory.COLOR_WALL)) {
        ySize++;
        i++;
      }
    }
    if (newHouse) {
      createHouse(xSize, ySize);
    }
  }

  private int xSize, ySize;

  private void createHouse(final int xSize, final int ySize) {
    this.xSize = xSize;
    this.ySize = ySize;

    model = ModelCache.loadBin("house1_inside.j3o");
    model.setLocalScale(new Vector3f(1f / 12f, 1f / 16f, 1f / 16f));
    attachChild(model);

    setModelBound(new BoundingBox());
    updateModelBound();

    setQueueBucket(Bucket.Opaque);

    if (xSize > ySize) {
      getLocalRotation().fromAngleAxis(-FastMath.PI / 2, new Vector3f(0, 1, 0));
      setLocalScale(new Vector3f(ySize, 4, xSize));
    } else {
      setLocalScale(new Vector3f(xSize, 4, ySize));
    }

    setLocalTranslation((x + xSize / 2f) * 5f, 0f, (y + ySize / 2f) * 5f);
  }

  public Direction getDirection() {
    return xSize > ySize ? Direction.DIRECTION_RIGHT : Direction.DIRECTION_DOWN;
  }

  public int getXSize() {
    return xSize;
  }

  public int getYSize() {
    return ySize;
  }

  public boolean getNewHouse() {
    return newHouse;
  }
}
