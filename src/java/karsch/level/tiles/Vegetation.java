package karsch.level.tiles;

import karsch.Values;
import karsch.level.Freefield;
import karsch.level.Level.LevelStyle;
import karsch.level.LevelMap;
import karsch.resources.TextureCache;

import com.jme3.bounding.BoundingBox;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.FastMath;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.control.BillboardControl;
import com.jme3.scene.shape.Quad;
import com.jme3.texture.Texture;

@SuppressWarnings("serial")
// TODO blending, dest and source function
public class Vegetation extends Tile {
  public Vegetation(final LevelMap levelMap) {
    super("vegetation");

    for (int i = 0; i < levelMap.getXSize(); i++) {
      for (int j = 0; j < levelMap.getYSize(); j++) {
        if (levelMap.getLevelMap()[i][j] instanceof Freefield) {
          if (levelMap.getStyle() == LevelStyle.LS_FARM) {
            addFlowers(i, j, "FLOWER", .5f);
          } else if (levelMap.getStyle() == LevelStyle.LS_FORREST) {
            addFlowers(i, j, "MUSHROOM", .5f);
          } else if (levelMap.getStyle() == LevelStyle.LS_UNDERGRND) {
            addFlowers(i, j, "ROCK", .5f);
          } else if (levelMap.getStyle() == LevelStyle.LS_HOUSE) {
            addFlowers(i, j, "SHIT", .3f);
          }
        }
      }
    }

    // final BlendState bs = DisplaySystem.getDisplaySystem().getRenderer()
    // .createBlendState();
    // bs.setBlendEnabled(true);
    // bs.setSourceFunction(BlendState.SourceFunction.One);
    // bs.setDestinationFunction(BlendState.DestinationFunction.OneMinusSourceAlpha);
    // bs.setTestEnabled(true);
    // bs.setTestFunction(BlendState.TestFunction.GreaterThan);
    //
    // bs.setEnabled(true);
    //
    // setRenderState(bs);
    // updateRenderState();

    setModelBound(new BoundingBox());
    updateModelBound();

    // if (levelMap.getStyle() == Level.LS_HOUSE) {
    // bs.setReference(0.6f);
    // bs.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
    // }

    lockAll();
  }

  private void addFlowers(final int x, final int y, final String fileName,
      final float statSize) {
    final int random = FastMath.rand.nextInt(10);
    if (random > 2)
      return;

    // final BillboardNode bbn = new BillboardNode("vegNode");
    // bbn.setAlignment(BillboardNode.CAMERA_ALIGNED);

    final float randSize = FastMath.rand.nextFloat();

    final Quad quad = new Quad(statSize + randSize, statSize + randSize);
    final Geometry geo = new Geometry(fileName + x + y, quad);
    geo.addControl(new BillboardControl());

    // final TextureState ts = DisplaySystem.getDisplaySystem().getRenderer()
    // .createTextureState();
    // ts.setTexture(TextureCache.getInstance().getTexture(
    // fileName + (random + 1) + ".PNG"));
    // quad.setRenderState(ts);
    //
    // quad.setLightCombineMode(LightCombineMode.Off);
    //
    // quad.updateRenderState();

    final Texture t = TextureCache.getInstance().getTexture(
        fileName + (random + 1) + ".PNG");

    final Material mat = new Material(Values.getInstance().getAssetManager(),
        "Common/MatDefs/Misc/Unshaded.j3md");
    mat.setTexture("ColorMap", t);
    mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);

    geo.setQueueBucket(Bucket.Transparent);

    geo.setMaterial(mat);

    // bbn.attachChild(quad);

    geo.setLocalTranslation(x * 5 + 1.5f + FastMath.rand.nextInt(2), .5f, y * 5
        + 1.5f + FastMath.rand.nextInt(2));

    attachChild(geo);
  }
}
