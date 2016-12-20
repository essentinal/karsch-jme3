package karsch.level.tiles;

import karsch.Values;
import karsch.characters.Karsch;
import karsch.controller.NPCController.Direction;
import karsch.interfaces.Interactable;
import karsch.interfaces.KarschPassable;
import karsch.level.Freefield;
import karsch.level.LevelText;
import karsch.resources.ModelCache;
import karsch.sound.SoundManager;

import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;

// TODO implement animation control
public class FenceGate extends Tile implements KarschPassable, Interactable {
  // private final SpatialTransformer trans;
  private Direction direction = Direction.DIRECTION_DOWN;
  private boolean open = false;

  public FenceGate(final int x, final int y, final LevelText levelText) {
    super("fenceGate" + x + "" + y, x, y);

    model = ModelCache.getInstance().get("fencegate.3ds");
    model.setLocalScale(.025f);
    attachChild(model);

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
    if (open)
      return;

    final Values values = Values.getInstance();
    final int keys = values.getKeys();
    if (keys > 0) {
      karsch.getController().setEnabled(false);
      SoundManager.getInstance().playSoundOnce("creak1.ogg");

      values.setKeys(keys - 1);
      // TODO
      // values.getHudState().setKeys(keys - 1);
      // trans.setActive(true);
      values.getLevelGameState().getKarsch().getController()
          .setActField(new Freefield());
      // TODO
      // values.getHudState().displayTextTime("The gate is now open", 1000);
      open = true;

      new Thread() {
        @Override
        public void run() {
          try {
            sleep(2000);
          } catch (final InterruptedException e) {
            e.printStackTrace();
          }
          stopInteraction(Values.getInstance().getLevelGameState().getKarsch());
        }
      }.start();

    } else {
      // TODO
      System.out.println("You have no key");
      // values.getHudState().displayTextTime("You have no key", 2000);
    }
  }

  // @Override
  @Override
  public void stopInteraction(final Karsch karsch) {
    karsch.getController().setEnabled(true);
  }

  // @Override
  @Override
  public boolean canPass() {
    return open;
  }

}
