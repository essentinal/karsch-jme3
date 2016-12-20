package karsch.controller;

import karsch.Values;
import karsch.characters.Karsch;
import karsch.controller.NPCController.Direction;
import karsch.interfaces.Collectable;
import karsch.interfaces.Interactable;
import karsch.interfaces.KarschPassable;
import karsch.level.LevelMap;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;

// TODO animation control

public class KarschController extends TimedControl {
  private final Karsch karsch;
  private float time = 0; // is not TIME, is range to move
  private float rpf = 0; // range per frame
  private Direction direction = Direction.DIRECTION_NODIR;
  private final LevelMap levelMap;
  private int x, y;
  private Object actField;
  private Direction lastDirection = Direction.DIRECTION_NODIR;

  private float angle = 0, lastAngle = 0;

  protected boolean run, rotateonly;

  // private SpatialTransformer trans;
  private final AnimControl animControl;
  // private final Object trans;

  private final AnimChannel walkCh;

  public KarschController(final Karsch karsch, final AnimControl animControl,
      final int x, final int y) {
    this.karsch = karsch;
    this.animControl = animControl;
    this.levelMap = karsch.getLevelMap();

    this.walkCh = animControl.createChannel();
    walkCh.setAnim("walk");

    this.x = x;
    this.y = y;

    setMinTime(0f);
    setMaxTime(5f);
    setSpeed(.5f / Values.getInstance().getSpeed());
    // trans.update(.1f);
    setRun(false);
    pickUp();
  }

  @Override
  protected void controlRender(final RenderManager rm, final ViewPort vp) {

  }

  @Override
  protected void controlUpdate(final float tpf) {
    if (run) {
      rpf = 5f * tpf / getSpeed();
      if (time + rpf <= getMaxTime()) {
        if (lastDirection != Direction.DIRECTION_NODIR) {
          rotate();
        }
        time += rpf;
      } else {
        rpf = getMaxTime() - time;
        time += rpf;
      }

      if (!rotateonly)
        move();

      if (time >= getMaxTime()) {

        dontMove();
        setRun(false);
        time = getMinTime();
        checkActField();
        rotateonly = false;
      }
    }
  }

  private void checkActField() {
    if (actField instanceof Collectable) {
      ((Collectable) actField).collect(karsch);
    }
  }

  public void setRun(final boolean run) {
    this.run = run;
    if (run && !rotateonly) {
      if (direction != Direction.DIRECTION_NODIR) {
        // trans.setActive(true);
        walkCh.setSpeed(4f);
      }
    } else {
      walkCh.setSpeed(0f);
      // trans.setActive(false);
    }
  }

  public void dontMove() {
    setRun(false);
    lastDirection = direction;
    direction = Direction.DIRECTION_NODIR;
    // time = getMinTime();

  }

  private void startRun() {
    if (!run) {
      time = getMinTime();
      setRun(true);
      karsch.setX(x);
      karsch.setY(y);
      checkRotation();
    }
  }

  private void drop() {
    // System.out.println("drop " + actField);
    if (actField != null)
      levelMap.getLevelMap()[x][y] = actField;
  }

  private void pickUp() {
    actField = levelMap.getLevelMap()[x][y];
    levelMap.getLevelMap()[x][y] = karsch;
  }

  private void move() {
    Vector3f diff = null;

    if (direction == Direction.DIRECTION_RIGHT) {
      diff = new Vector3f(rpf, 0f, 0f);
    } else if (direction == Direction.DIRECTION_LEFT) {
      diff = new Vector3f(-rpf, 0f, 0f);
    } else if (direction == Direction.DIRECTION_DOWN) {
      diff = new Vector3f(0f, 0f, rpf);
    } else if (direction == Direction.DIRECTION_UP) {
      diff = new Vector3f(0f, 0f, -rpf);
    }

    if (diff != null) {
      karsch.setLocalTranslation(karsch.getLocalTranslation().add(diff));
    }
  }

  private void rotate() {
    if (time > getMaxTime() / 2)
      return;

    final float percent = time * 2 / (getMaxTime());
    final float newAngle = FastMath.DEG_TO_RAD
        * FastMath.interpolateLinear(percent, lastAngle, angle);

    karsch.getLocalRotation().fromAngleNormalAxis(newAngle, Vector3f.UNIT_Y);
  }

  private void checkRotation() {
    if (lastDirection == Direction.DIRECTION_DOWN) {
      if (direction == Direction.DIRECTION_LEFT) {
        lastAngle = 360;
      } else {
        lastAngle = 0;
      }
    } else if (lastDirection == Direction.DIRECTION_UP) {
      lastAngle = 180;
    } else if (lastDirection == Direction.DIRECTION_RIGHT) {
      lastAngle = 90;
    } else if (lastDirection == Direction.DIRECTION_LEFT) {
      lastAngle = 270;
    }

    if (direction == Direction.DIRECTION_DOWN) {
      if (lastDirection == Direction.DIRECTION_LEFT) {
        angle = 360;
      } else {
        angle = 0;
      }
    } else if (direction == Direction.DIRECTION_UP) {
      angle = 180;
    } else if (direction == Direction.DIRECTION_RIGHT) {
      angle = 90;
    } else if (direction == Direction.DIRECTION_LEFT) {
      angle = 270;
    }
  }

  public void moveRight() {
    if (isEnabled() && (direction == Direction.DIRECTION_NODIR)) {
      if ((levelMap.getLevelMap().length > x + 1)
          && (levelMap.getLevelMap()[x + 1][y] instanceof KarschPassable)
          && ((KarschPassable) levelMap.getLevelMap()[x + 1][y]).canPass()) {

        drop();
        x++;
        pickUp();

        // trans.setActive(true);
        rotateonly = false;
      } else {
        rotateonly = true;
      }

      direction = Direction.DIRECTION_RIGHT;

      startRun();
    }
  }

  public void moveLeft() {
    if (isEnabled() && (direction == Direction.DIRECTION_NODIR)) {
      if ((x > 0)
          && (levelMap.getLevelMap()[x - 1][y] instanceof KarschPassable)
          && ((KarschPassable) levelMap.getLevelMap()[x - 1][y]).canPass()) {

        drop();
        x--;
        pickUp();

        // trans.setActive(true);

        rotateonly = false;
      } else {
        rotateonly = true;
      }
      direction = Direction.DIRECTION_LEFT;

      startRun();
    }
  }

  public void moveDown() {
    if (isEnabled() && (direction == Direction.DIRECTION_NODIR)) {
      if ((levelMap.getLevelMap()[x].length > y + 1)
          && (levelMap.getLevelMap()[x][y + 1] instanceof KarschPassable)
          && ((KarschPassable) levelMap.getLevelMap()[x][y + 1]).canPass()) {

        drop();
        y++;
        pickUp();

        // trans.setActive(true);
        rotateonly = false;
      } else {
        rotateonly = true;
      }

      direction = Direction.DIRECTION_DOWN;

      startRun();
    }
  }

  public void moveUp() {
    if (isEnabled() && (direction == Direction.DIRECTION_NODIR)) {
      if ((y > 0)
          && (levelMap.getLevelMap()[x][y - 1] instanceof KarschPassable)
          && ((KarschPassable) levelMap.getLevelMap()[x][y - 1]).canPass()) {

        drop();
        y--;
        pickUp();
        // trans.setActive(true);
        rotateonly = false;
      } else {
        rotateonly = true;
      }

      direction = Direction.DIRECTION_UP;

      startRun();
    }
  }

  public void setActField(final int x, final int y) {
    actField = levelMap.getLevelMap()[x][y];
    levelMap.getLevelMap()[x][y] = karsch;
  }

  public void checkInteractions() {
    if (run)
      return;
    Interactable ia = null;
    if (lastDirection == Direction.DIRECTION_LEFT && x > 0
        && levelMap.getLevelMap()[x - 1][y] instanceof Interactable) {
      ia = (Interactable) levelMap.getLevelMap()[x - 1][y];
    } else if (lastDirection == Direction.DIRECTION_RIGHT
        && (levelMap.getLevelMap().length > x + 1)
        && levelMap.getLevelMap()[x + 1][y] instanceof Interactable) {
      ia = (Interactable) levelMap.getLevelMap()[x + 1][y];
    } else if (lastDirection == Direction.DIRECTION_UP && y > 0
        && levelMap.getLevelMap()[x][y - 1] instanceof Interactable) {
      ia = (Interactable) levelMap.getLevelMap()[x][y - 1];
    } else if (lastDirection == Direction.DIRECTION_DOWN
        && (levelMap.getLevelMap()[0].length > y + 1)
        && levelMap.getLevelMap()[x][y + 1] instanceof Interactable) {
      ia = (Interactable) levelMap.getLevelMap()[x][y + 1];
    }

    if (ia != null) {
      ia.interact(karsch);

    }
  }

  public void setActField(final Object o) {
    actField = o;
  }

  public Object getActField() {
    return actField;
  }

  public void setLastDirection(final Direction lastDirection) {
    this.lastDirection = lastDirection;
    switch (lastDirection) {
    case DIRECTION_RIGHT:
      karsch.lookAt(
          karsch.getLocalTranslation().clone().add(new Vector3f(1, 0, 0)),
          Vector3f.UNIT_Y);
      break;
    case DIRECTION_LEFT:
      karsch.lookAt(
          karsch.getLocalTranslation().clone().add(new Vector3f(-1, 0, 0)),
          Vector3f.UNIT_Y);
      break;
    case DIRECTION_UP:
      karsch.lookAt(
          karsch.getLocalTranslation().clone().add(new Vector3f(0, 0, -1)),
          Vector3f.UNIT_Y);
      break;
    case DIRECTION_DOWN:
      karsch.lookAt(
          karsch.getLocalTranslation().clone().add(new Vector3f(0, 0, 1)),
          Vector3f.UNIT_Y);
      break;
    }
  }

}
