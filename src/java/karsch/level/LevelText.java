package karsch.level;

import java.awt.Point;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import karsch.KarschSimpleGame;
import karsch.controller.NPCController.Direction;
import karsch.level.Level.LevelStyle;

public class LevelText {
  private final Properties prop;

  public LevelText(final int levelNumber) {
    prop = new Properties();
    final boolean b = load("level" + levelNumber + ".txt");

    if (!b) {
      throw new RuntimeException("Level text could not be loaded");
    }
  }

  private boolean load(final String filename) {
    InputStream fin = null;

    final ClassLoader cl = getClass().getClassLoader();

    for (final String dir : KarschSimpleGame.assetPaths) {
      try {
        fin = cl.getResourceAsStream(dir + filename);
        if (fin != null) {
          break;
        }
      } catch (final Exception e) {
        e.printStackTrace();
      }
    }

    if (fin == null) {
      return false;
    }

    try {
      if (fin != null) {
        prop.load(fin);
        fin.close();
      }
    } catch (final IOException e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  public String getText(final String key) {
    String text = prop.getProperty(key);
    if (text == null)
      text = "no key";
    return text;
  }

  public Direction getRotationField(final int x, final int y) {
    String text = prop.getProperty(x + "," + y);
    if (text == null)
      return Direction.DIRECTION_NODIR;
    try {
      text = text.split("rotation")[1];
      text = text.substring(text.indexOf('(') + 1, text.indexOf(')'));

      if (text.equalsIgnoreCase("down")) {
        return Direction.DIRECTION_DOWN;
      } else if (text.equalsIgnoreCase("up")) {
        return Direction.DIRECTION_UP;
      } else if (text.equalsIgnoreCase("left")) {
        return Direction.DIRECTION_LEFT;
      } else if (text.equalsIgnoreCase("right")) {
        return Direction.DIRECTION_RIGHT;
      }
    } catch (final Exception e) {
      return Direction.DIRECTION_NODIR;
    }

    return Direction.DIRECTION_NODIR;
  }

  public Direction getRotationEntrance() {
    final String text = prop.getProperty("entrance");
    if (text == null) {
      System.err.println("entrance not found in leveltext");
      return Direction.DIRECTION_NODIR;
    }
    try {
      if (text.equalsIgnoreCase("down")) {
        return Direction.DIRECTION_DOWN;
      } else if (text.equalsIgnoreCase("up")) {
        return Direction.DIRECTION_UP;
      } else if (text.equalsIgnoreCase("left")) {
        return Direction.DIRECTION_LEFT;
      } else if (text.equalsIgnoreCase("right")) {
        return Direction.DIRECTION_RIGHT;
      }
    } catch (final Exception e) {
      System.err.println("entrance not found in leveltext");
      return Direction.DIRECTION_NODIR;
    }
    System.err.println("entrance not found in leveltext");
    return Direction.DIRECTION_NODIR;
  }

  public Direction getRotationExit() {
    final String text = prop.getProperty("exit");
    if (text == null) {
      System.err.println("exit not found in leveltext");
      return Direction.DIRECTION_NODIR;
    }
    try {
      if (text.equalsIgnoreCase("down")) {
        return Direction.DIRECTION_DOWN;
      } else if (text.equalsIgnoreCase("up")) {
        return Direction.DIRECTION_UP;
      } else if (text.equalsIgnoreCase("left")) {
        return Direction.DIRECTION_LEFT;
      } else if (text.equalsIgnoreCase("right")) {
        return Direction.DIRECTION_RIGHT;
      }
    } catch (final Exception e) {
      System.err.println("exit not found in leveltext");
      return Direction.DIRECTION_NODIR;
    }
    System.err.println("exit not found in leveltext");
    return Direction.DIRECTION_NODIR;
  }

  public Point getSecondRollingStoneField(final int x, final int y) {
    final String text = prop.getProperty(x + "," + y);

    try {
      int oX, oY;

      oX = Integer.parseInt(text.split(",")[0]);
      oY = Integer.parseInt(text.split(",")[1]);
      return new Point(oX, oY);
    } catch (final Exception e) {
      return null;
    }
  }

  public LevelStyle getLevelStyle() {
    final String text = prop.getProperty("levelstyle");
    if (text.trim().equalsIgnoreCase("forest")) {
      return Level.LevelStyle.LS_FORREST;
    } else if (text.trim().equalsIgnoreCase("underground")) {
      return Level.LevelStyle.LS_UNDERGRND;
    } else if (text.trim().equalsIgnoreCase("house")) {
      return Level.LevelStyle.LS_HOUSE;
    } else {
      return Level.LevelStyle.LS_FARM;
    }
  }
}
