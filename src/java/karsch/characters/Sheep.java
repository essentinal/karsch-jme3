package karsch.characters;

import karsch.Values;
import karsch.controller.NPCController;
import karsch.effects.SpeechBalloon;
import karsch.interfaces.Interactable;
import karsch.level.LevelMap;
import karsch.resources.ModelCache;
import karsch.resources.TextureCache;
import karsch.sound.Dialog;

import com.jme3.bounding.BoundingBox;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;

//TODO get the right animation control
public class Sheep extends CharacterBase implements Interactable {
  private boolean interacting = false;
  private final SpeechBalloon balloon;

  private final Dialog dialog;

  public Sheep(final int x, final int y, final LevelMap levelMap) {
    super(x, y, levelMap, "sheep");
    model = ModelCache.getInstance().get("sheep1.3ds");
    // trans = (SpatialTransformer) model.getController(0);
    // trans.setActive(false);
    // trans.setRepeatType(Controller.RT_WRAP);
    // trans.setSpeed(30f * Values.getInstance().getSpeed());
    // trans.setMaxTime(19);
    // model.setLocalScale(.06f);

    // model.setLocalTranslation(new Vector3f(0, -2.5f, -2.5f));

    model.setLocalTranslation(new Vector3f(0f, 1f, 0));
    model.setModelBound(new BoundingBox());
    model.updateModelBound();

    // //////////
    // TODO workaround because not material set
    final Material mat = new Material(Values.getInstance().getAssetManager(),
        "Common/MatDefs/Light/Lighting.j3md");

    mat.setFloat("Shininess", 5.0f);
    mat.setBoolean("UseMaterialColors", true);
    mat.setColor("Ambient", new ColorRGBA(.7f, .7f, .7f, 1f));
    mat.setColor("Diffuse", new ColorRGBA(.7f, .7f, .7f, 1f));
    mat.setColor("Specular", ColorRGBA.White);
    mat.setTexture("DiffuseMap",
        TextureCache.getInstance().getTexture("SHEEP1.PNG"));

    model.setMaterial(mat);
    // ///////////

    setLocalTranslation(new Vector3f(x * 5 + 2.5f, 0, y * 5 + 2.5f));
    attachChild(model);

    attachChild(new Shadow(1.5f, 1.5f));

    controller = new NPCController(this, null, x, y);
    controller.setSpeed(2 / Values.getInstance().getSpeed());
    addControl(controller);
    addSound("sheep", 1, 3, 5);

    balloon = new SpeechBalloon();
    attachChild(balloon);

    dialog = new Dialog(balloon);

    dialog.addText("Baa  ");
    dialog.addText("That's a sheep. Looks very fluffy.");

    dialog.addSpeech("sheep1.ogg");
    dialog.addSpeech("thatsasheep.ogg");
  }

  public Dialog getDialog() {
    return dialog;
  }

  @Override
  public void interact(final Karsch karsch) {
    if (!interacting) {
      lookAt(karsch.getLocalTranslation(), Vector3f.UNIT_Y);
      interacting = true;
      controller.setEnabled(false);
      // trans.setActive(false);
      karsch.getController().setEnabled(false);
    }

    if (dialog.start()) {

    } else {
      stopInteraction(karsch);
    }
  }

  @Override
  public void stopInteraction(final Karsch karsch) {
    interacting = false;
    controller.setEnabled(true);
    // trans.setActive(true);
    karsch.getController().setEnabled(true);
  }
}
