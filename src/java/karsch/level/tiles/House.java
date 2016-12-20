package karsch.level.tiles;

import java.awt.image.BufferedImage;

import karsch.controller.NPCController.Direction;
import karsch.resources.ModelCache;

import com.jme3.bounding.BoundingBox;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;

@SuppressWarnings("serial")
public class House extends Tile {
  private boolean newHouse = true;

  public House(final int x, final int y, final BufferedImage levelImage) {
    super("house" + x + "" + y, x, y, levelImage);

    detectHouse();
    // lockAll();
  }

  private void detectHouse() {
    final int actPixel = levelImage.getRGB(x, y);
    int xSize = 1;
    int ySize = 1;

    if ((x > 0) && (levelImage.getRGB(x - 1, y) == actPixel)) {
      newHouse = false;
    } else if ((y > 0) && (levelImage.getRGB(x, y - 1) == actPixel)) {
      newHouse = false;
    } else {
      int i = 1;
      while ((x + i <= levelImage.getWidth())
          && (levelImage.getRGB(x + i, y) == actPixel)) {
        xSize++;
        i++;
      }
      i = 1;
      while ((y + i <= levelImage.getHeight())
          && (levelImage.getRGB(x, y + i) == actPixel)) {
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

    model = ModelCache.loadBin("house1.j3o");
    model.setLocalScale(new Vector3f(1f / 12f, 1f / 16f, 1f / 16f));
    attachChild(model);

    setModelBound(new BoundingBox());
    updateModelBound();

    setQueueBucket(Bucket.Opaque);

    if (xSize > ySize) {
      // model.setLocalTranslation(0f, 0f, 0f);

      this.xSize = ySize;
      this.ySize = xSize;

      setLocalScale(new Vector3f(ySize, xSize, 8));

      getLocalRotation().fromAngleAxis(-FastMath.PI / 2, new Vector3f(0, 1, 0));

    } else {

      this.xSize = xSize;
      this.ySize = ySize;

      // model.setLocalTranslation(0f, 0f, -1.25f);
      setLocalScale(new Vector3f(xSize, ySize, 4));
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
