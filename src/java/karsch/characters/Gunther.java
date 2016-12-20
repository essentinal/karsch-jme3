package karsch.characters;

import karsch.Values;
import karsch.effects.SpeechBalloon;
import karsch.interfaces.Interactable;
import karsch.level.LevelMap;
import karsch.resources.ModelCache;
import karsch.sound.Dialog;

import com.jme3.bounding.BoundingBox;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;

// TODO get the right animation control
public class Gunther extends CharacterBase implements Interactable {
  private final SpeechBalloon balloon;
  private boolean interacting = false;
  private final Dialog keyDialog, noKeyDialog;

  public Gunther(final int x, final int y, final LevelMap levelMap) {
    super(x, y, levelMap, "gunther");
    model = ModelCache.getInstance().get("gunther.3ds");
    // trans = (SpatialTransformer) model.getController(0);
    // trans.setRepeatType(Controller.RT_WRAP);
    // trans.setSpeed(5f);
    // trans.setMaxTime(25);
    // trans.setActive(true);
    // model.setLocalScale(.04f);
    model.updateModelBound();

    // //////////
    // TODO workaround because not material set
    final Material mat = new Material(Values.getInstance().getAssetManager(),
        "Common/MatDefs/Light/Lighting.j3md");

    mat.setFloat("Shininess", 5.0f);
    mat.setBoolean("UseMaterialColors", true);
    mat.setColor("Ambient", new ColorRGBA(.7f, .7f, .7f, 1f));
    mat.setColor("Diffuse", new ColorRGBA(.7f, .7f, .2f, 1f));
    mat.setColor("Specular", ColorRGBA.White);

    model.setMaterial(mat);
    // ///////////

    attachChild(model);

    attachChild(new Shadow(1.5f, 1.5f));

    balloon = new SpeechBalloon();
    attachChild(balloon);

    setLocalTranslation(new Vector3f(x * 5 + 2.5f, 0, y * 5 + 2.5f));
    setModelBound(new BoundingBox());
    updateModelBound();

    noKeyDialog = new Dialog(balloon);

    noKeyDialog.addText("Hi Karsch!");
    noKeyDialog.addText("Hi Gunther, what's going on here?");
    noKeyDialog.addText("I locked your baby into the sheep's enclosure.");
    noKeyDialog.addText("Ok, give me the key!");
    noKeyDialog.addText("I lost the key!");
    noKeyDialog.addText("What the ...");
    noKeyDialog.addText("Sorry but I don't know it better.");
    noKeyDialog.addText("Ohh you ...");
    noKeyDialog.addText("You gotta find the key.. don't know where I lost it");

    noKeyDialog.addSpeech("01hikarsch.ogg");
    noKeyDialog.addSpeech("02higunther.ogg");
    noKeyDialog.addSpeech("03ilockedyourbaby.ogg");
    noKeyDialog.addSpeech("04givemethekey.ogg");
    noKeyDialog.addSpeech("05ilostthekey.ogg");
    noKeyDialog.addSpeech("06whatthe.ogg");
    noKeyDialog.addSpeech("07sorryidontknow.ogg");
    noKeyDialog.addSpeech("08ohyou.ogg");
    noKeyDialog.addSpeech("09yougottafindthekey.ogg");

    keyDialog = new Dialog(balloon);

    keyDialog.addText("Have a nice day...");
    keyDialog.addText("Never mind!");

    keyDialog.addSpeech("01haveaniceday.ogg");
    keyDialog.addSpeech("02nevermind.ogg");

    lookAt(getLocalTranslation().clone().add(new Vector3f(1, 0, 0)),
        Vector3f.UNIT_Y);

    setCullHint(CullHint.Never);
  }

  public Dialog getKeyDialog() {
    return keyDialog;
  }

  public Dialog getNoKeyDialog() {
    return noKeyDialog;
  }

  @Override
  public void interact(final Karsch karsch) {
    if (!interacting) {
      lookAt(karsch.getLocalTranslation(), Vector3f.UNIT_Y);
      interacting = true;
      karsch.getController().setEnabled(false);
    }

    if (Values.getInstance().getKeys() < 1) {
      if (!noKeyDialog.start()) {
        stopInteraction(karsch);
      }
    } else {
      if (!keyDialog.start()) {
        stopInteraction(karsch);
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
