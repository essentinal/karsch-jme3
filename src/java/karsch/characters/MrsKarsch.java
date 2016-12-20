package karsch.characters;

import karsch.Values;
import karsch.effects.SpeechBalloon;
import karsch.interfaces.Interactable;
import karsch.level.LevelManager;
import karsch.level.LevelMap;
import karsch.resources.ModelCache;
import karsch.sound.Dialog;

import com.jme3.bounding.BoundingBox;
import com.jme3.math.Vector3f;

// TODO get the right animation control
public class MrsKarsch extends CharacterBase implements Interactable {
  private final SpeechBalloon balloon;
  private boolean interacting = false;
  private final Dialog dialog;

  private final Dialog enoughBabiesDialog;
  private final Dialog tooLittleBabiesDialog;

  public MrsKarsch(final int x, final int y, final LevelMap levelMap) {
    super(x, y, levelMap, "mrskarsch");
    model = ModelCache.loadBin("mrskarsch.j3o");
    // trans = (SpatialTransformer) model.getController(0);
    // trans.setRepeatType(Controller.RT_WRAP);
    // trans.setSpeed(5f);
    // trans.setMaxTime(25);
    // trans.setActive(true);
    model.setLocalScale(.008f);
    model.setLocalTranslation(0, 1.8f, 0);

    // //////////
    // TODO workaround because not material set
    // final Material mat = new Material(Values.getInstance().getAssetManager(),
    // "Common/MatDefs/Light/Lighting.j3md");
    //
    // mat.setFloat("Shininess", 0.0f);
    // mat.setBoolean("UseMaterialColors", true);
    // mat.setColor("Ambient", new ColorRGBA(.7f, .7f, .7f, 1f));
    // mat.setColor("Diffuse", new ColorRGBA(.7f, .7f, .7f, 1f));
    // mat.setColor("Specular", ColorRGBA.Black);
    // mat.setTexture("DiffuseMap",
    // TextureCache.getInstance().getTexture("DRESS1.PNG"));
    //
    // model.setMaterial(mat);
    // ///////////

    attachChild(model);

    attachChild(new Shadow(1.5f, 1.5f));

    balloon = new SpeechBalloon();
    attachChild(balloon);

    setLocalTranslation(new Vector3f(x * 5 + 2.5f, 0, y * 5 + 2.5f));
    setModelBound(new BoundingBox());
    updateModelBound();

    dialog = new Dialog(balloon);
    dialog.addText("Hi darling, have you seen the babies?");
    dialog.addText("No...  ");
    dialog.addText("They must be playing on the outside!");
    dialog.addText("And now?");
    dialog.addText("Look for the babies and take them home, Karsch.");

    dialog.addSpeech("01hidarlinghaveyouseen.ogg");
    dialog.addSpeech("02no.ogg");
    dialog.addSpeech("03theymustbeplaying.ogg");
    dialog.addSpeech("04andnow.ogg");
    dialog.addSpeech("05lookforthebabies.ogg");

    enoughBabiesDialog = new Dialog(balloon);
    enoughBabiesDialog.addText("Oh my darling, you found all babies!");
    enoughBabiesDialog.addText("Of course I did");
    enoughBabiesDialog.addText("Thanks sweety, I love you");

    enoughBabiesDialog.addSpeech("01ohmydarling.ogg");
    enoughBabiesDialog.addSpeech("02ofcourseidid.ogg");
    enoughBabiesDialog.addSpeech("03thankssweety.ogg");

    tooLittleBabiesDialog = new Dialog(balloon);
    tooLittleBabiesDialog.addText("You come home without all the babies?");
    tooLittleBabiesDialog.addText("Oh my god...");
    tooLittleBabiesDialog.addText("They will find their way home...");

    tooLittleBabiesDialog.addSpeech("01youcomehomewithout.ogg");
    tooLittleBabiesDialog.addSpeech("02ohmygod.ogg");
    tooLittleBabiesDialog.addSpeech("03theywillfindtheirway.ogg");

    lookAt(getLocalTranslation().clone().add(new Vector3f(-1, 0, 0)),
        Vector3f.UNIT_Y);
  }

  public Dialog getDialog() {
    return dialog;
  }

  public Dialog getEnoughBabiesDialog() {
    return enoughBabiesDialog;
  }

  public Dialog getTooLittleBabiesDialog() {
    return tooLittleBabiesDialog;
  }

  // @Override
  @Override
  public void interact(final Karsch karsch) {
    if (!interacting) {
      lookAt(karsch.getLocalTranslation(), Vector3f.UNIT_Y);
      interacting = true;
      karsch.getController().setEnabled(false);
    }

    Dialog dia = null;
    if (levelMap.getLevelNumber() == 1) {
      dia = dialog;
    } else if (Values.getInstance().getBabies() >= 5) {
      dia = enoughBabiesDialog;
    } else {
      dia = tooLittleBabiesDialog;
    }

    if (!dia.start()) {
      if (levelMap.getLevelNumber() == 1) {
        stopInteraction(karsch);
      } else {
        LevelManager.getInstance().loadCredits();
      }
    }

  }

  // @Override
  @Override
  public void stopInteraction(final Karsch karsch) {
    interacting = false;
    karsch.getController().setEnabled(true);
  }
}
