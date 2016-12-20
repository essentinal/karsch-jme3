package karsch.level.tiles;

import karsch.Values;
import karsch.characters.Karsch;
import karsch.controller.NPCController.Direction;
import karsch.interfaces.Interactable;
import karsch.level.LevelText;
import karsch.resources.ModelCache;
import karsch.sound.SoundManager;

import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

// TODO implement the animation controller
public class Lever extends Tile implements Interactable {
  // private final SpatialTransformer trans;
  private Direction direction = Direction.DIRECTION_DOWN;
  private boolean open = false, interacting = false;

  public Lever(final int x, final int y, final LevelText levelText) {
    super("lever" + x + "" + y, x, y);

    try {
      Values.getInstance().getLevelGameState().getRiddle().addLever(this);
    } catch (final Exception e) {
    }

    model = ModelCache.getInstance().get("lever.3ds");
    model.setLocalScale(.020f);
    final Node leverNode = new Node("levernode");
    leverNode.attachChild(model);
    leverNode.getLocalRotation().fromAngleAxis(-FastMath.HALF_PI,
        new Vector3f(0, 1, 0));
    attachChild(leverNode);

    // TODO
    // trans = (SpatialTransformer) model.getController(0);
    // trans.setRepeatType(Controller.RT_CLAMP);
    // trans.setActive(true);
    // trans.update(.1f);
    // trans.setActive(false);
    // trans.setCurTime(0);

    setLocalTranslation(x * 5f + 2.5f, 0, y * 5 + 2.5f);

    direction = levelText.getRotationField(x, y);

    if (direction == Direction.DIRECTION_LEFT) {
      getLocalRotation()
          .fromAngleAxis(-FastMath.HALF_PI, new Vector3f(0, 1, 0));
    } else if (direction == Direction.DIRECTION_RIGHT) {
      getLocalRotation().fromAngleAxis(FastMath.HALF_PI, new Vector3f(0, 1, 0));
    } else if (direction == Direction.DIRECTION_UP) {
      getLocalRotation().fromAngleAxis(FastMath.PI, new Vector3f(0, 1, 0));
    }
  }

  public Direction getDirection() {
    return direction;
  }

  // @Override
  @Override
  public void interact(final Karsch karsch) {
    if (interacting)
      return;
    interacting = true;

    karsch.getController().setEnabled(false);
    SoundManager.getInstance().playSoundOnce("creak1.ogg");

    // TODO
    // trans.setActive(true);
    // if (open) {
    // trans.setSpeed(-15);
    // } else {
    // trans.setSpeed(15);
    // }

    open = !open;

    new Thread() {
      @Override
      public void run() {
        try {
          sleep(1000);
        } catch (final InterruptedException e) {
          e.printStackTrace();
        }
        stopInteraction(Values.getInstance().getLevelGameState().getKarsch());
      }
    }.start();

    Values.getInstance().getLevelGameState().getRiddle().checkRiddle();
  }

  // @Override
  @Override
  public void stopInteraction(final Karsch karsch) {
    interacting = false;
    karsch.getController().setEnabled(true);

    // TODO
    // Values.getInstance().getHudState()
    // .displayTextTime("Something happened... ", 1000);
    System.out.println("Something happened... ");
  }

  public boolean getOpen() {
    return open;
  }

}
