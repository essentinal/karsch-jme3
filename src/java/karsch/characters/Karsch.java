package karsch.characters;

import karsch.Values;
import karsch.controller.KarschController;
import karsch.effects.Pow;
import karsch.effects.SpeechBalloon;
import karsch.interfaces.Collectable;
import karsch.level.LevelMap;
import karsch.level.LevelText;
import karsch.level.tiles.SpikeTrap;
import karsch.resources.ModelCache;
import karsch.utils.NodeUtils;

import com.jme3.animation.AnimControl;
import com.jme3.scene.Node;

// TODO trigger animation controller
// TODO get the right animation control
public class Karsch extends CharacterBase implements Collectable {
  private final KarschController karschController;
  private boolean dead = false;
  private int babiesFound = 0;
  private final SpeechBalloon speech;

  public Karsch(final int x, final int y, final LevelMap levelMap,
      final LevelText levelText) {
    super(x, y, levelMap, "karsch");
    model = ModelCache.loadBin("karsch_walk.j3o");
    // model = ModelCache.loadBin("karsch_dance.j3o");
    // trans = (SpatialTransformer) model.getController(0);
    // trans.setRepeatType(Controller.RT_WRAP);
    // trans.setSpeed(30f * Values.getInstance().getSpeed());
    // trans.setMaxTime(19);

    // //////////
    // TODO workaround because not material set
    // final Material mat = new Material(Values.getInstance().getAssetManager(),
    // "Common/MatDefs/Light/Lighting.j3md");
    //
    // mat.setFloat("Shininess", 5.0f);
    // mat.setBoolean("UseMaterialColors", true);
    // mat.setColor("Ambient", new ColorRGBA(.7f, .7f, .7f, 1f));
    // mat.setColor("Diffuse", new ColorRGBA(.7f, .7f, .7f, 1f));
    // mat.setColor("Specular", ColorRGBA.White);
    // mat.setTexture("DiffuseMap",
    // TextureCache.getInstance().getTexture("KARSCH_F.PNG"));
    //
    // model.setMaterial(mat);
    // ///////////

    model.setLocalScale(.03f);
    // model.setModelBound(new BoundingBox());
    // model.setLocalTranslation(0, 1.5f, 0);
    // model.updateModelBound();

    final AnimControl ac = NodeUtils.findAnimControl(model);

    // try {
    // final AnimChannel channel = ac.createChannel();
    // channel.setAnim("walk");
    // channel.setSpeed(1.5f);
    // } catch (final Exception e) {
    // e.printStackTrace();
    // }

    attachChild(model);
    attachChild(new Shadow(1.5f, 1.5f));

    karschController = new KarschController(this,
    // animation control should be here
        ac, x, y);
    addControl(karschController);

    speech = new SpeechBalloon();
    attachChild(speech);

    karschController.setLastDirection(levelText.getRotationEntrance());
  }

  public boolean moveDown() {
    karschController.moveDown();
    return true;
  }

  public boolean moveLeft() {
    karschController.moveLeft();
    return true;
  }

  public boolean moveRight() {
    karschController.moveRight();
    return true;
  }

  public boolean moveUp() {
    karschController.moveUp();
    return true;
  }

  public KarschController getController() {
    return karschController;
  }

  @Override
  public void setPause(final boolean pause) {
    if (controller != null)
      controller.setEnabled(!pause);
    // if (trans != null && pause)
    // trans.setActive(false);
    // if (track != null) {
    // if (pause) {
    // track.stop();
    // } else {
    // track.play();
    // }
    // }
    if (audioController != null)
      audioController.setEnabled(!pause);
  }

  // @Override
  @Override
  public void collect(final Node source) {
    if (!dead) {
      dead = true;
      Pow pow = null;
      if (source instanceof Cow2) {
        pow = new Pow(this, Pow.TYPE_COWPOW);
      } else if (source instanceof SpikeTrap) {
        pow = new Pow(this, Pow.TYPE_SPIKEOUCH);
      }

      final Values values = Values.getInstance();
      values.setBabies(values.getBabies() - babiesFound);

      // TODO
      // values.getHudState().setBabies(values.getBabies());
      pow.setActive();
    }
  }

  public int getBabiesFound() {
    return babiesFound;
  }

  /**
   * Increments the found babies
   */
  public void incBabiesFound() {
    babiesFound++;
  }

  public void setBabiesFound(final int babiesFound) {
    this.babiesFound = babiesFound;
  }

  public void speak(final String text) {
    speech.showText(text);
  }

  public void checkInteractions() {
    karschController.checkInteractions();
  }
}
