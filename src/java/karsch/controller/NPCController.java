package karsch.controller;

import java.awt.Point;

import karsch.characters.CharacterBase;
import karsch.interfaces.NPCPassable;
import karsch.level.LevelMap;

import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

// TODO add animation control
@SuppressWarnings("serial")
public class NPCController extends TimedControl {
  private final CharacterBase character;
  private float time = 0; // is not TIME, is range to move
  private float rpf = 0; // range per frame
  private Direction direction = Direction.DIRECTION_NODIR;
  protected LevelMap levelMap;
  protected int x, y;
  private Object actField;

  // protected SpatialTransformer trans;
  protected Object trans;

  public enum Direction {
    DIRECTION_NODIR(-1), DIRECTION_RIGHT(0), DIRECTION_LEFT(1), DIRECTION_DOWN(
        2), DIRECTION_UP(3);

    private final int intRepresentation;

    Direction(final int intRepresentation) {
      this.intRepresentation = intRepresentation;
    }

    public static Direction parse(final int i) {
      for (final Direction d : values()) {
        if (d.intRepresentation == i) {
          return d;
        }
      }

      return null;
    }

    public Quaternion toRotation() {
      switch (this) {
      case DIRECTION_RIGHT:
        return new Quaternion()
            .fromAngleAxis(FastMath.PI / 2f, Vector3f.UNIT_Y);
      case DIRECTION_LEFT:
        return new Quaternion().fromAngleAxis(FastMath.PI / 2f * 3f,
            Vector3f.UNIT_Y);
      case DIRECTION_UP:
        return new Quaternion().fromAngleAxis(FastMath.PI, Vector3f.UNIT_Y);
      default:
        return new Quaternion();
      }
    }

    public Point toTranslation() {
      switch (this) {
      case DIRECTION_RIGHT:
        return new Point(-1, 0);
      case DIRECTION_LEFT:
        return new Point(1, 0);
      case DIRECTION_UP:
        return new Point(0, 1);
      case DIRECTION_DOWN:
        return new Point(0, -1);
      default:
        return new Point();
      }
    }
  }

  private Direction lastDirection;

  private float angle = 0, lastAngle = 0;

  protected boolean run = true;

  public NPCController(final CharacterBase character, final Object trans,
      final int x, final int y) {
    this.character = character;
    this.levelMap = character.getLevelMap();
    this.trans = trans;

    this.x = x;
    this.y = y;

    setMinTime(0);
    setMaxTime(5);
    setEnabled(false);
    actField = levelMap.getLevelMap()[x][y];
  }

  @Override
  public void controlUpdate(final float tpf) {

    if (time == getMinTime()) {
      checkDirections();
    }
    if (run) {
      rpf = 5f * tpf / getSpeed();
      if (time + rpf <= getMaxTime()) {
        rotate();
        if (time + rpf >= getMaxTime() / 2) {
          if (direction == Direction.DIRECTION_DOWN) {
            levelMap.getLevelMap()[x][y - 1] = actField;
          } else if (direction == Direction.DIRECTION_UP) {
            levelMap.getLevelMap()[x][y + 1] = actField;
          } else if (direction == Direction.DIRECTION_LEFT) {
            levelMap.getLevelMap()[x + 1][y] = actField;
          } else if (direction == Direction.DIRECTION_RIGHT) {
            levelMap.getLevelMap()[x - 1][y] = actField;
          }
        }
        time += rpf;
      } else {
        rpf = getMaxTime() - time;
        time += rpf;
      }

      move();

      if (time >= getMaxTime()) {
        dontMove();
        setRun(false);
        time = getMinTime();
      }
    }
  }

  private void dontMove() {
    // trans.setActive(false);
    lastDirection = direction;
    direction = Direction.DIRECTION_NODIR;
  }

  private void startRun() {
    if (!run) {

      time = getMinTime();
      checkRotation();
      setRun(true);
    }
  }

  public void setRun(final boolean run) {
    this.run = run;

    if (run) {
      if (direction != Direction.DIRECTION_NODIR) {
        // trans.setActive(true);
      }
    } else {
      // trans.setActive(false);
    }
  }

  private void checkDirections() {
    int random;
    boolean ok = false;
    while (!ok) {
      random = FastMath.rand.nextInt(5);
      if (random == 0) {
        ok = moveRight();
      } else if (random == 1) {
        ok = moveLeft();
      } else if (random == 2) {
        ok = moveUp();
      } else if (random == 3) {
        ok = moveDown();
      } else {
        // trans.setActive(false);
        direction = Direction.DIRECTION_NODIR;
        ok = true;
      }
    }
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

  private void rotate() {
    if (time >= getMaxTime() / 2)
      return;

    final float percent = time * 2 / (getMaxTime());
    final float newAngle = FastMath.DEG_TO_RAD
        * FastMath.interpolateLinear(percent, lastAngle, angle);

    character.getLocalRotation().fromAngleNormalAxis(newAngle, Vector3f.UNIT_Y);
  }

  private void move() {
    if (direction == Direction.DIRECTION_RIGHT) {
      character.getLocalTranslation().x += rpf;
    } else if (direction == Direction.DIRECTION_LEFT) {
      character.getLocalTranslation().x -= rpf;
    } else if (direction == Direction.DIRECTION_DOWN) {
      character.getLocalTranslation().z += rpf;
    } else if (direction == Direction.DIRECTION_UP) {
      character.getLocalTranslation().z -= rpf;
    }
  }

  private boolean moveRight() {
    if ((direction == Direction.DIRECTION_NODIR)
        && (levelMap.getLevelMap().length > x + 1)
        && (levelMap.getLevelMap()[x + 1][y] instanceof NPCPassable)) {

      x++;
      actField = levelMap.getLevelMap()[x][y];
      levelMap.getLevelMap()[x][y] = character;
      // trans.setActive(true);
      direction = Direction.DIRECTION_RIGHT;

      startRun();
      return true;
    } else {
      return false;
    }

  }

  private boolean moveLeft() {
    if ((direction == Direction.DIRECTION_NODIR) && (x > 0)
        && (levelMap.getLevelMap()[x - 1][y] instanceof NPCPassable)) {

      x--;
      actField = levelMap.getLevelMap()[x][y];
      levelMap.getLevelMap()[x][y] = character;
      // trans.setActive(true);
      direction = Direction.DIRECTION_LEFT;

      startRun();
      return true;
    } else {
      return false;
    }

  }

  private boolean moveDown() {
    if ((direction == Direction.DIRECTION_NODIR)
        && (levelMap.getLevelMap()[x].length > y + 1)
        && (levelMap.getLevelMap()[x][y + 1] instanceof NPCPassable)) {

      y++;
      actField = levelMap.getLevelMap()[x][y];
      levelMap.getLevelMap()[x][y] = character;
      // trans.setActive(true);
      direction = Direction.DIRECTION_DOWN;

      startRun();
      return true;
    } else {
      return false;
    }

  }

  private boolean moveUp() {
    if ((direction == Direction.DIRECTION_NODIR) && (y > 0)
        && (levelMap.getLevelMap()[x][y - 1] instanceof NPCPassable)) {

      y--;
      actField = levelMap.getLevelMap()[x][y];
      levelMap.getLevelMap()[x][y] = character;
      // trans.setActive(true);
      direction = Direction.DIRECTION_UP;

      startRun();
      return true;
    } else {
      return false;
    }

  }

}
