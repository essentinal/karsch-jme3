package karsch.level.tiles;

import java.awt.Point;

import karsch.Values;
import karsch.characters.Karsch;
import karsch.characters.Shadow;
import karsch.effects.SpeechBalloon;
import karsch.interfaces.Interactable;
import karsch.level.Freefield;
import karsch.level.LevelMap;
import karsch.level.LevelText;
import karsch.resources.TextureCache;
import karsch.sound.Dialog;

import com.jme3.bounding.BoundingBox;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Sphere;
import com.jme3.texture.Texture;

@SuppressWarnings("serial")
public class RollingStone extends Tile implements Interactable {
  private final LevelMap levelMap;
  private final Point rollingStoneGoalField;
  private final int xGoal, yGoal;
  private final Vector3f goalFieldPos;
  private final Vector3f startFieldPos;

  private boolean interacting = false;
  private final SpeechBalloon balloon;
  private final Dialog dialog;

  public RollingStone(final int x, final int y, final LevelText levelText,
      final LevelMap levelMap) {
    super("rollingStone" + x + "" + y, x, y);
    final Geometry geo = new Geometry("rollingStone" + String.valueOf(x)
        + String.valueOf(y), new Sphere(8, 8, 2.5f));
    attachChild(geo);
    this.levelMap = levelMap;

    try {
      Values.getInstance().getLevelGameState().getRiddle().addStone(this);
    } catch (final Exception e) {
      e.printStackTrace();
    }

    rollingStoneGoalField = levelText.getSecondRollingStoneField(x, y);
    xGoal = (int) rollingStoneGoalField.getX();
    yGoal = (int) rollingStoneGoalField.getY();
    startFieldPos = new Vector3f(x * 5 + 2.5f, 2.5f, y * 5 + 2.5f);
    goalFieldPos = new Vector3f(xGoal * 5f + 2.5f, 2.5f, yGoal * 5f + 2.5f);

    // final TextureState ts = DisplaySystem.getDisplaySystem().getRenderer()
    // .createTextureState();
    //
    // ts.setTexture(TextureManager.loadTexture(ResourceLocatorTool
    // .locateResource(ResourceLocatorTool.TYPE_TEXTURE, "DIRT.PNG"),
    // Texture.MinificationFilter.BilinearNearestMipMap,
    // Texture.MagnificationFilter.Bilinear), 0);
    //
    // ts.setEnabled(true);
    // setRenderState(ts);
    //
    // final MaterialState ms = DisplaySystem.getDisplaySystem().getRenderer()
    // .createMaterialState();
    // ms.setDiffuse(new ColorRGBA(.4f, .4f, .45f, 1f));
    // ms.setEnabled(true);
    //
    // setRenderState(ms);
    //
    // updateRenderState();

    final Material mat = new Material(Values.getInstance().getAssetManager(),
        "Common/MatDefs/Light/Lighting.j3md");

    mat.setFloat("Shininess", 0.0f);
    mat.setBoolean("UseMaterialColors", true);
    mat.setColor("Specular", ColorRGBA.Black);
    mat.setColor("Ambient", ColorRGBA.Black);
    mat.setColor("Diffuse", new ColorRGBA(.7f, .7f, .7f, 1f));

    final Texture t = TextureCache.getInstance().getTexture("DIRT.PNG");
    mat.setTexture("DiffuseMap", t);

    geo.setMaterial(mat);

    geo.setModelBound(new BoundingBox());
    geo.updateModelBound();

    // J3OHelper.save(geo, "interactablestone");

    // setLocalRotation(new Matrix3f( 0,FastMath.rand.nextFloat(),0,
    // 0,0,FastMath.rand.nextFloat(),
    // 0,0,FastMath.rand.nextFloat()));

    final Shadow shadow = new Shadow(4, 4);
    shadow.setLocalTranslation(0, -2.5f, 0);
    attachChild(shadow);
    setLocalTranslation(startFieldPos);

    balloon = new SpeechBalloon();
    dialog = new Dialog(balloon);

    dialog.addText("");
    dialog.addText("This stone blocks my way.");

    dialog.addSpeech("silence.ogg");
    dialog.addSpeech("thisstoneblocks.ogg");
  }

  public Point getRollingStoneGoalField() {
    return rollingStoneGoalField;
  }

  public void open() {
    setLocalTranslation(goalFieldPos);
    levelMap.getLevelMap()[xGoal][yGoal] = this;
    levelMap.getLevelMap()[x][y] = new Freefield();
  }

  public void close() {
    setLocalTranslation(startFieldPos);
    levelMap.getLevelMap()[x][y] = this;
    levelMap.getLevelMap()[xGoal][yGoal] = new Freefield();
  }

  public Dialog getDialog() {
    return dialog;
  }

  // @Override
  @Override
  public void interact(final Karsch karsch) {
    if (!interacting) {
      interacting = true;
      karsch.getController().setEnabled(false);
    }

    if (dialog.start()) {

    } else {
      stopInteraction(karsch);
    }
  }

  // @Override
  @Override
  public void stopInteraction(final Karsch karsch) {
    interacting = false;
    karsch.getController().setEnabled(true);
  }
}
