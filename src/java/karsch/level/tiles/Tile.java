package karsch.level.tiles;

import java.awt.image.BufferedImage;

import com.jme3.bounding.BoundingBox;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

public abstract class Tile extends Node {
  final Tile tile;
  protected int x, y;
  protected Spatial model;
  protected BufferedImage levelImage;

  public Tile(final String name, final int x, final int y) {
    super(name);
    tile = this;
    this.x = x;
    this.y = y;
  }

  public Tile(final String name, final int x, final int y,
      final BufferedImage levelImage) {
    super(name);
    tile = this;
    this.x = x;
    this.y = y;
    this.levelImage = levelImage;
  }

  public Tile(final String name) {
    super(name);
    tile = this;
  }

  protected void lockAll() {
    setModelBound(new BoundingBox());
    updateModelBound();

    // TODO locking is no longer available in JME3
    // tile.lockBounds();
    // tile.lockMeshes();
    // tile.lockShadows();
    // tile.lockBranch();
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }
}
